/*
 * Copyright (c) 2005-2021 Radiance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of the copyright holder nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.radiance.theming.internal.ui;

//import org.pushingpixels.radiance.animation.api.Timeline;
//import org.pushingpixels.radiance.animation.api.Timeline.RepeatBehavior;
//import org.pushingpixels.radiance.animation.api.Timeline.TimelineState;
//import org.pushingpixels.radiance.animation.api.swing.EventDispatchThreadTimelineCallbackAdapter;
import org.pushingpixels.trident.api.Timeline;
import org.pushingpixels.trident.api.Timeline.RepeatBehavior;
import org.pushingpixels.trident.api.Timeline.TimelineState;
import org.pushingpixels.trident.api.swing.EventDispatchThreadTimelineCallbackAdapter;

import org.pushingpixels.radiance.common.api.RadianceCommonCortex;
import org.pushingpixels.radiance.theming.api.*;
import org.pushingpixels.radiance.theming.api.colorscheme.RadianceColorScheme;
import org.pushingpixels.radiance.theming.api.painter.border.RadianceBorderPainter;
import org.pushingpixels.radiance.theming.api.painter.fill.RadianceFillPainter;
import org.pushingpixels.radiance.theming.api.shaper.ClassicButtonShaper;
import org.pushingpixels.radiance.theming.api.shaper.RadianceButtonShaper;
import org.pushingpixels.radiance.theming.api.tabbed.*;
import org.pushingpixels.radiance.theming.internal.AnimationConfigurationManager;
import org.pushingpixels.radiance.theming.internal.RadianceThemingWidgetRepository;
import org.pushingpixels.radiance.theming.internal.RadianceSynapse;
import org.pushingpixels.radiance.theming.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.radiance.theming.internal.animation.StateTransitionTracker;
import org.pushingpixels.radiance.theming.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.radiance.theming.internal.utils.*;
import org.pushingpixels.radiance.theming.internal.utils.icon.TransitionAwareIcon;
import org.pushingpixels.radiance.theming.internal.utils.scroll.RadianceScrollButton;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.*;

/**
 * UI for tabbed panes in <b>Radiance</b> look and feel.
 * 
 * @author Kirill Grouchnikov
 */
public class RadianceTabbedPaneUI extends BasicTabbedPaneUI {
    /**
     * Current mouse location.
     */
    private Point radianceMouseLocation;

    /**
     * Hash map for storing already computed backgrounds.
     */
    private static LazyResettableHashMap<BufferedImage> backgroundMap =
            new LazyResettableHashMap<>("RadianceTabbedPaneUI.background");

    /**
     * Hash map for storing already computed backgrounds.
     */
    private static LazyResettableHashMap<BufferedImage> closeButtonMap =
            new LazyResettableHashMap<>("RadianceTabbedPaneUI.closeButton");

    /**
     * Key - tab component. Value - the looping timeline that animates the tab component when it's
     * marked as modified (with {@link RadianceSynapse#CONTENTS_MODIFIED} property).
     */
    private Map<Component, Timeline> modifiedTimelines;

    /**
     * Currently selected index (for selection animations).
     */
    private int currSelectedIndex;

    private StateTransitionMultiTracker<Integer> stateTransitionMultiTracker;

    // private JPanel tabStrip;
    //
    // private JScrollablePanel<JPanel> scrollableTabStrip;

    private Set<RadianceThemingWidget<JComponent>> themingWidgets;

    public static ComponentUI createUI(JComponent comp) {
        RadianceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new RadianceTabbedPaneUI();
    }

    /**
     * Mouse handler for rollover effects.
     */
    private MouseRolloverHandler radianceRolloverHandler;

    /**
     * Tracks changes to the tabbed pane contents. Each tab component is tracked for changes on the
     * {@link RadianceSynapse#CONTENTS_MODIFIED} property.
     */
    private TabbedContainerListener radianceContainerListener;

    /**
     * Listener for animation effects on tab selection.
     */
    private ChangeListener radianceSelectionListener;

    private boolean radianceContentOpaque;

    private Map<Integer, Color> tabTextColorMap;

    /**
     * Tracks changes to the tabbed pane contents. Each tab component is tracked for changes on the
     * {@link RadianceSynapse#CONTENTS_MODIFIED} property.
     * 
     * @author Kirill Grouchnikov
     */
    private final class TabbedContainerListener extends ContainerAdapter {
        /**
         * Property change listeners on the tab components.
         * <p/>
         * Fixes defect 135 - memory leaks on tabbed panes.
         */
        private Map<Component, List<PropertyChangeListener>> listeners = new HashMap<>();

        /**
         * Creates a new container listener.
         */
        private TabbedContainerListener() {
        }

        /**
         * Tracks all existing tab component.
         */
        private void trackExistingTabs() {
            // register listeners on all existing tabs
            for (int i = 0; i < RadianceTabbedPaneUI.this.tabPane.getTabCount(); i++) {
                this.trackTab(RadianceTabbedPaneUI.this.tabPane.getComponentAt(i));
            }
        }

        /**
         * Tracks changes in a single tab component.
         * 
         * @param tabComponent
         *            Tab component.
         */
        private void trackTab(final Component tabComponent) {
            if (tabComponent == null) {
                return;
            }

            PropertyChangeListener tabModifiedListener = propertyChangeEvent -> {
                if (RadianceSynapse.CONTENTS_MODIFIED.equals(propertyChangeEvent.getPropertyName())) {
                    Object oldValue = propertyChangeEvent.getOldValue();
                    Object newValue = propertyChangeEvent.getNewValue();
                    boolean wasModified = Boolean.TRUE.equals(oldValue);
                    boolean isModified = Boolean.TRUE.equals(newValue);

                    if (wasModified) {
                        if (!isModified) {
                            Timeline modifiedTimeline = modifiedTimelines.get(tabComponent);
                            modifiedTimeline.cancel();
                            modifiedTimelines.remove(tabComponent);
                        }
                    } else {
                        if (isModified) {
                            int tabIndex = RadianceTabbedPaneUI.this.tabPane
                                    .indexOfComponent(tabComponent);
                            if (tabIndex >= 0) {
                                trackTabModification(tabIndex, tabComponent);
                            }
                        }
                    }
                }
            };
            tabComponent.addPropertyChangeListener(tabModifiedListener);
            // fix for defect 135 - memory leaks on tabbed panes
            List<PropertyChangeListener> currList = this.listeners.get(tabComponent);
            if (currList == null) {
                currList = new LinkedList<>();
            }
            currList.add(tabModifiedListener);
            // System.err.println(this.hashCode() + " adding for " +
            // tabComponent.hashCode());
            this.listeners.put(tabComponent, currList);
            // Fix for defect 104 - a 'modified' component is added to
            // the tabbed pane. In this case it should be animated from the
            // beginning.
            if (tabComponent instanceof JComponent) {
                if (Boolean.TRUE.equals(((JComponent) tabComponent)
                        .getClientProperty(RadianceSynapse.CONTENTS_MODIFIED))) {
                    int tabIndex = RadianceTabbedPaneUI.this.tabPane
                            .indexOfComponent(tabComponent);
                    if (tabIndex >= 0) {
                        trackTabModification(tabIndex, tabComponent);
                    }
                }
            }
        }

        @Override
        public void componentAdded(final ContainerEvent e) {
            final Component tabComponent = e.getChild();
            if (tabComponent instanceof UIResource) {
                return;
            }
            this.trackTab(tabComponent);
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            // fix for defect 135 - memory leaks on tabbed panes
            final Component tabComponent = e.getChild();
            if (tabComponent == null) {
                return;
            }
            // System.err.println(this.hashCode() + " removing for " +
            // tabComponent.hashCode());
            if (tabComponent instanceof UIResource) {
                return;
            }
            for (PropertyChangeListener pcl : this.listeners.get(tabComponent)) {
                tabComponent.removePropertyChangeListener(pcl);
            }
            this.listeners.get(tabComponent).clear();
            this.listeners.remove(tabComponent);

            // has running timeline?
            Timeline timeline = modifiedTimelines.get(tabComponent);
            if (timeline != null) {
                timeline.cancel();
                modifiedTimelines.remove(tabComponent);
            }
        }

    }

    /**
     * Listener for rollover animation effects.
     * 
     * @author Kirill Grouchnikov
     */
    private class MouseRolloverHandler implements MouseListener, MouseMotionListener {
        /**
         * Index of the tab that was rolled over on the previous mouse event.
         */
        private int prevRolledOver = -1;

        /**
         * Indicates whether the previous mouse event was located in a close button.
         */
        private boolean prevInCloseButton = false;

        /**
         * Tab index of the last mouse pressed event that happened in a close button.
         */
        private int tabOfPressedCloseButton = -1;

        @Override
        public void mouseClicked(final MouseEvent e) {
            final int tabIndex = RadianceTabbedPaneUI.this
                    .tabForCoordinate(RadianceTabbedPaneUI.this.tabPane, e.getX(), e.getY());
            TabCloseCallback closeCallback = RadianceCoreUtilities.getTabCloseCallback(
                    RadianceTabbedPaneUI.this.tabPane, tabIndex);
            if (closeCallback == null)
                return;

            final RadianceThemingSlices.TabCloseKind tabCloseKind = closeCallback
                    .onAreaClick(RadianceTabbedPaneUI.this.tabPane, tabIndex, e);
            if (tabCloseKind == RadianceThemingSlices.TabCloseKind.NONE)
                return;

            SwingUtilities.invokeLater(
                    () -> RadianceTabbedPaneUI.this.tryCloseTabs(tabIndex, tabCloseKind));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            this.handleMouseMoveDrag(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setRolloverTab(tabForCoordinate(tabPane, e.getX(), e.getY()));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!tabPane.isEnabled()) {
                return;
            }
            int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());
            if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex)) {
                Rectangle rect = new Rectangle();
                rect = getTabBounds(tabIndex, rect);
                Rectangle close = getCloseButtonRectangleForEvents(tabIndex, rect.x, rect.y,
                        rect.width, rect.height);
                boolean inCloseButton = close.contains(e.getPoint());
                this.tabOfPressedCloseButton = inCloseButton ? tabIndex : -1;
                if (tabIndex != tabPane.getSelectedIndex()) {
                    // enhancement 307 - don't select tab on pressing its
                    // close button
                    if (inCloseButton) {
                        return;
                    }
                    // Clicking on unselected tab, change selection, do NOT
                    // request focus.
                    // This will trigger the focusIndex to change by way
                    // of stateChanged.
                    tabPane.setSelectedIndex(tabIndex);
                } else if (tabPane.isRequestFocusEnabled()) {
                    // Clicking on selected tab, try and give the tabbedpane
                    // focus. Repaint will occur in focusGained.
                    tabPane.requestFocus();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            this.handleMouseMoveDrag(e);
        }

        /**
         * Handles the move and drag mouse events.
         * 
         * @param e
         *            Mouse event to handle.
         */
        private void handleMouseMoveDrag(MouseEvent e) {
            if (e.getSource() != tabPane)
                return;

            setRolloverTab(tabForCoordinate(tabPane, e.getX(), e.getY()));
            if (!AnimationConfigurationManager.getInstance()
                    .isAnimationAllowed(RadianceThemingSlices.AnimationFacet.ROLLOVER, tabPane)) {
                return;
            }

            RadianceTabbedPaneUI.this.radianceMouseLocation = e.getPoint();
            int currRolledOver = RadianceTabbedPaneUI.this.getRolloverTab();
            TabCloseCallback tabCloseCallback = RadianceCoreUtilities.getTabCloseCallback(
                    tabPane, currRolledOver);
            // System.err.println("Mouse moved " + currRolledOver + ":" +
            // prevRolledOver);
            if (currRolledOver == this.prevRolledOver) {
                if (currRolledOver >= 0) {
                    Rectangle rect = new Rectangle();
                    rect = getTabBounds(currRolledOver, rect);
                    Rectangle close = getCloseButtonRectangleForEvents(currRolledOver, rect.x,
                            rect.y, rect.width, rect.height);
                    // System.out.println("move " + rect + " " + close + " "
                    // + e.getPoint());
                    boolean inCloseButton = close.contains(e.getPoint());
                    if (this.prevInCloseButton == inCloseButton)
                        return;
                    this.prevInCloseButton = inCloseButton;
                    if (tabCloseCallback != null) {
                        if (inCloseButton) {
                            String closeButtonTooltip = tabCloseCallback
                                    .getCloseButtonTooltip(tabPane, currRolledOver);
                            tabPane.setToolTipTextAt(currRolledOver, closeButtonTooltip);
                        } else {
                            String areaTooltip = tabCloseCallback.getAreaTooltip(tabPane,
                                    currRolledOver);
                            tabPane.setToolTipTextAt(currRolledOver, areaTooltip);
                        }
                    }
                    if ((currRolledOver >= 0) && (currRolledOver < tabPane.getTabCount())) {
                        StateTransitionTracker tracker = getTracker(currRolledOver, true,
                                currRolledOver == currSelectedIndex);
                        tracker.getModel().setRollover(false);
                        tracker.endTransition();
                    }
                }
            } else {
                if ((this.prevRolledOver >= 0) && (this.prevRolledOver < tabPane.getTabCount())
                        && tabPane.isEnabledAt(this.prevRolledOver)) {
                    getTracker(prevRolledOver, true, prevRolledOver == currSelectedIndex).getModel()
                            .setRollover(false);
                }
                if ((currRolledOver >= 0) && (currRolledOver < tabPane.getTabCount())
                        && tabPane.isEnabledAt(currRolledOver)) {
                    getTracker(currRolledOver, false, currRolledOver == currSelectedIndex)
                            .getModel().setRollover(true);
                }
            }
            this.prevRolledOver = currRolledOver;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setRolloverTab(-1);
            // fix for bug 69 - non-selected non-rollover tab
            // may remain with close button after moving mouse quickly
            // to inner JTabbedPane
            if ((this.prevRolledOver >= 0)
                    && (this.prevRolledOver < RadianceTabbedPaneUI.this.tabPane.getTabCount())
                    && RadianceTabbedPaneUI.this.tabPane.isEnabledAt(this.prevRolledOver)) {
                // only the previously rolled-over tab needs to be
                // repainted (fade-out) instead of repainting the
                // whole tab as before.
                getTracker(prevRolledOver, true, prevRolledOver == currSelectedIndex).getModel()
                        .setRollover(false);

                if (RadianceCoreUtilities.getTabCloseCallback(tabPane,
                        this.prevRolledOver) != null) {
                    tabPane.setToolTipTextAt(this.prevRolledOver, null);
                }
            }
            this.prevRolledOver = -1;
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            // enhancement 307 - moving the tab close to be on mouse release
            // and not on mouse press.
            final int tabIndex = RadianceTabbedPaneUI.this
                    .tabForCoordinate(RadianceTabbedPaneUI.this.tabPane, e.getX(), e.getY());
            // check that the mouse release is on the same tab as
            // mouse press, and that the tab has close button
            if (RadianceCoreUtilities.hasCloseButton(RadianceTabbedPaneUI.this.tabPane, tabIndex)
                    && (tabIndex == this.tabOfPressedCloseButton)) {
                SwingUtilities.invokeLater(() -> {
                    if ((tabIndex >= 0)
                            && RadianceTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) {
                        Rectangle rect = new Rectangle();
                        rect = RadianceTabbedPaneUI.this.getTabBounds(tabIndex, rect);

                        Rectangle close = RadianceTabbedPaneUI.this
                                .getCloseButtonRectangleForEvents(tabIndex, rect.x, rect.y,
                                        rect.width, rect.height);
                        // System.out.println("press " + close + " "
                        // + e.getPoint());
                        if (close.contains(e.getPoint())) {
                            TabCloseCallback closeCallback = RadianceCoreUtilities
                                    .getTabCloseCallback(RadianceTabbedPaneUI.this.tabPane,
                                            tabIndex);

                            RadianceThemingSlices.TabCloseKind tabCloseKind = (closeCallback == null) ? RadianceThemingSlices.TabCloseKind.THIS
                                    : closeCallback.onCloseButtonClick(
                                            RadianceTabbedPaneUI.this.tabPane, tabIndex, e);

                            RadianceTabbedPaneUI.this.tryCloseTabs(tabIndex, tabCloseKind);
                        }
                    }
                });
                this.tabOfPressedCloseButton = -1;
            }
        }
    }

    /**
     * Creates the new UI delegate.
     */
    private RadianceTabbedPaneUI() {
        super();
        this.stateTransitionMultiTracker = new StateTransitionMultiTracker<>();
        this.currSelectedIndex = -1;
        this.tabTextColorMap = new HashMap<>();
    }

    @Override
    public void installUI(JComponent c) {
        this.themingWidgets = RadianceThemingWidgetRepository.getRepository().getMatchingWidgets(c);

        super.installUI(c);

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.uninstallUI();
        }
        super.uninstallUI(c);
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        // Install listener to repaint the tabbed pane
        // on mouse move (for rollover effects).
        this.radianceRolloverHandler = new MouseRolloverHandler();
        this.tabPane.addMouseMotionListener(this.radianceRolloverHandler);
        this.tabPane.addMouseListener(this.radianceRolloverHandler);

        // Add container listener to wire property change listener
        // on each tab in the tabbed pane.
        this.radianceContainerListener = new TabbedContainerListener();
        this.radianceContainerListener.trackExistingTabs();

        for (int i = 0; i < this.tabPane.getTabCount(); i++) {
            Component tabComp = this.tabPane.getComponentAt(i);
            if (RadianceCoreUtilities.isTabModified(tabComp)) {
                trackTabModification(i, tabComp);
            }
        }

        this.tabPane.addContainerListener(this.radianceContainerListener);

        this.radianceSelectionListener = changeEvent -> SwingUtilities.invokeLater(() -> {
            if (RadianceTabbedPaneUI.this.tabPane == null) {
                return;
            }
            int selected = RadianceTabbedPaneUI.this.tabPane.getSelectedIndex();

            // fix for issue 437 - track the selection change,
            // fading out the previously selected tab
            if ((currSelectedIndex >= 0)
                    && (currSelectedIndex < RadianceTabbedPaneUI.this.tabPane.getTabCount())
                    && RadianceTabbedPaneUI.this.tabPane.isEnabledAt(currSelectedIndex)) {
                StateTransitionTracker tracker = getTracker(currSelectedIndex,
                        getRolloverTabIndex() == currSelectedIndex, true);
                tracker.getModel().setSelected(false);
            }
            currSelectedIndex = selected;
            if ((selected >= 0) && (selected < RadianceTabbedPaneUI.this.tabPane.getTabCount())
                    && RadianceTabbedPaneUI.this.tabPane.isEnabledAt(selected)) {
                StateTransitionTracker tracker = getTracker(selected,
                        getRolloverTabIndex() == selected, false);
                tracker.getModel().setSelected(true);
            }
        });
        this.tabPane.getModel().addChangeListener(this.radianceSelectionListener);

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.installListeners();
        }
    }

    @Override
    protected void uninstallListeners() {
        super.uninstallListeners();
        if (this.radianceRolloverHandler != null) {
            this.tabPane.removeMouseMotionListener(this.radianceRolloverHandler);
            this.tabPane.removeMouseListener(this.radianceRolloverHandler);
            this.radianceRolloverHandler = null;
        }
        if (this.radianceContainerListener != null) {
            for (Map.Entry<Component, List<PropertyChangeListener>> entry : this.radianceContainerListener.listeners
                    .entrySet()) {
                Component comp = entry.getKey();
                // System.out.println(this.containerListener.hashCode() +"
                // removing all for" + comp.hashCode());
                for (PropertyChangeListener pcl : entry.getValue()) {
                    comp.removePropertyChangeListener(pcl);
                }
            }
            this.radianceContainerListener.listeners.clear();

            this.tabPane.removeContainerListener(this.radianceContainerListener);
            this.radianceContainerListener = null;
        }
        this.tabPane.getModel().removeChangeListener(this.radianceSelectionListener);
        this.radianceSelectionListener = null;

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.uninstallListeners();
        }
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();

        this.radianceContentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");

        this.modifiedTimelines = new HashMap<>();
        this.currSelectedIndex = this.tabPane.getSelectedIndex();

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallDefaults() {
        for (Timeline timeline : this.modifiedTimelines.values())
            timeline.cancel();
        this.modifiedTimelines.clear();

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.uninstallDefaults();
        }

        super.uninstallDefaults();
    }

    @Override
    protected void installComponents() {
        super.installComponents();

        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.installComponents();
        }
    }

    @Override
    protected void uninstallComponents() {
        for (RadianceThemingWidget themingWidget : this.themingWidgets) {
            themingWidget.uninstallComponents();
        }

        super.uninstallComponents();
    }

    /**
     * Retrieves tab background.
     */
    private static BufferedImage getTabBackground(JTabbedPane tabPane, int width, int height,
            RadianceColorScheme fillScheme, RadianceColorScheme borderScheme,
            boolean paintOnlyBorder) {
        double scale = RadianceCommonCortex.getScaleFactor(tabPane);
        RadianceFillPainter fillPainter = RadianceCoreUtilities.getFillPainter(tabPane);
        RadianceBorderPainter borderPainter = RadianceCoreUtilities.getBorderPainter(tabPane);
        RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(tabPane);

        float borderDelta = 2.0f * RadianceSizeUtils.getBorderStrokeWidth(tabPane);
        float borderInsets = RadianceSizeUtils.getBorderStrokeWidth(tabPane) / 2.0f;
        int dy = (int) (2 + borderDelta);
        Set<RadianceThemingSlices.Side> straightSides = EnumSet.of(RadianceThemingSlices.Side.BOTTOM);

        float cornerRadius = height / 3.0f;
        if (shaper instanceof ClassicButtonShaper) {
            cornerRadius = RadianceSizeUtils
                    .getClassicButtonCornerRadius(RadianceSizeUtils.getComponentFontSize(tabPane));
            width -= 1;
        }

        Shape contour = RadianceOutlineUtilities.getBaseOutline(width, height + dy,
                cornerRadius, straightSides, borderInsets);

        BufferedImage result = RadianceCoreUtilities.getBlankImage(scale, width, height);
        Graphics2D resGraphics = result.createGraphics();

        if (!paintOnlyBorder) {
            fillPainter.paintContourBackground(resGraphics, tabPane, width, height + dy, contour,
                    false, fillScheme, true);
        }

        float borderThickness = RadianceSizeUtils.getBorderStrokeWidth(tabPane);
        Shape contourInner = borderPainter.isPaintingInnerContour()
                ? RadianceOutlineUtilities.getBaseOutline(width, height + dy,
                        cornerRadius - borderThickness, straightSides,
                        borderThickness + borderInsets)
                : null;

        borderPainter.paintBorder(resGraphics, tabPane, width, height + dy, contour, contourInner,
                borderScheme);

        resGraphics.dispose();
        return result;
    }

    /**
     * Retrieves tab background that will be shown on the screen. Unlike
     * {@link #getTabBackground(JTabbedPane, int, int, RadianceColorScheme, RadianceColorScheme, boolean)}
     * , the result is rotated as necessary (for {@link SwingConstants#LEFT} and
     * {@link SwingConstants#RIGHT} placement) and blended for selected tabs.
     */
    private static BufferedImage getFinalTabBackgroundImage(JTabbedPane tabPane, int tabIndex,
            int width, int height, int tabPlacement, RadianceThemingSlices.Side side,
            RadianceColorScheme colorScheme, RadianceColorScheme borderScheme) {
        double scale = RadianceCommonCortex.getScaleFactor(tabPane);
        RadianceFillPainter fillPainter = RadianceCoreUtilities.getFillPainter(tabPane);
        RadianceBorderPainter borderPainter = RadianceCoreUtilities.getBorderPainter(tabPane);
        RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(tabPane);
        Component compForBackground = tabPane.getTabComponentAt(tabIndex);
        if (compForBackground == null)
            compForBackground = tabPane.getComponentAt(tabIndex);
        if (compForBackground == null)
            compForBackground = tabPane;
        Color tabColor = compForBackground.getBackground();
        if (tabColor instanceof UIResource) {
            // special handling of tabs placed in decoration areas
            tabColor = RadianceColorUtilities.getBackgroundFillColor(compForBackground);
        }
        ImageHashMapKey key = RadianceCoreUtilities.getScaleAwareHashKey(
                scale, width, height, tabPlacement,
                fillPainter.getDisplayName(), borderPainter.getDisplayName(),
                shaper.getDisplayName(), tabPlacement == SwingConstants.BOTTOM, side.name(),
                colorScheme.getDisplayName(), borderScheme.getDisplayName(), tabColor);

        RadianceSkin skin = RadianceCoreUtilities.getSkin(tabPane);
        BufferedImage result = RadianceTabbedPaneUI.backgroundMap.get(key);
        if (result == null) {
            BufferedImage backgroundImage = null;

            switch (tabPlacement) {
                case BOTTOM:
                    BufferedImage unrotated = getFinalTabBackgroundImage(tabPane, tabIndex,
                            width, height, SwingConstants.TOP, side, colorScheme, borderScheme);
                    return RadianceImageCreator.getRotated(scale, unrotated, 2);
                case TOP:
                case LEFT:
                case RIGHT:
                    backgroundImage = RadianceTabbedPaneUI.getTabBackground(tabPane, width, height,
                            colorScheme, borderScheme, false);
                    int fw = backgroundImage.getWidth();
                    int fh = backgroundImage.getHeight();
                    BufferedImage fade = RadianceCoreUtilities.getBlankUnscaledImage(
                            backgroundImage);
                    Graphics2D fadeGraphics = fade.createGraphics();
                    fadeGraphics.setColor(tabColor);
                    fadeGraphics.fillRect(0, 0, fw, fh);
                    BufferedImage background = RadianceTabbedPaneUI.getTabBackground(tabPane,
                            width, height, colorScheme, borderScheme, true);
                    fadeGraphics.drawImage(background, 0, 0, background.getWidth(),
                            background.getHeight(), null);

                    backgroundImage = RadianceCoreUtilities.blendImagesVertical(backgroundImage,
                            fade, skin.getTabFadeStart(), skin.getTabFadeEnd());
            }
            RadianceTabbedPaneUI.backgroundMap.put(key, backgroundImage);
        }
        return backgroundMap.get(key);
    }

    /**
     * Retrieves the image of the close button.
     */
    private static BufferedImage getCloseButtonImage(JTabbedPane tabPane, int width, int height,
            boolean toPaintBorder, RadianceColorScheme fillScheme,
            RadianceColorScheme markScheme) {
        RadianceFillPainter fillPainter = RadianceCoreUtilities.getFillPainter(tabPane);
        if (fillPainter == null)
            return null;

        double scale = RadianceCommonCortex.getScaleFactor(tabPane);
        ImageHashMapKey key = RadianceCoreUtilities.getScaleAwareHashKey(
                scale, width, height, toPaintBorder,
                fillPainter.getDisplayName(), fillScheme.getDisplayName(),
                markScheme.getDisplayName());
        BufferedImage result = RadianceTabbedPaneUI.closeButtonMap.get(key);
        if (result == null) {
            result = RadianceCoreUtilities.getBlankImage(scale, width, height);
            Graphics2D finalGraphics = (Graphics2D) result.getGraphics().create();

            if (toPaintBorder) {
                Shape contour = RadianceOutlineUtilities.getBaseOutline(width, height, 1, null);
                fillPainter.paintContourBackground(finalGraphics, tabPane, width, height, contour,
                        false, fillScheme, true);
                // finalGraphics.drawImage(background, 0, 0, null);
                RadianceBorderPainter borderPainter =
                        RadianceCoreUtilities.getBorderPainter(tabPane);
                finalGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                borderPainter.paintBorder(finalGraphics, tabPane, width, height, contour, null,
                        markScheme);
            }

            finalGraphics.setStroke(new BasicStroke(
                    RadianceSizeUtils.getTabCloseButtonStrokeWidth(tabPane)));

            int delta = (int) (Math.floor(RadianceSizeUtils.getBorderStrokeWidth(tabPane)));
            if (delta % 2 != 0)
                delta--;
            int iconSize = width - delta;

            Icon closeIcon = RadianceImageCreator.getCloseIcon(tabPane, iconSize, markScheme);
            finalGraphics.translate(delta / 2, delta / 2);
            closeIcon.paintIcon(tabPane, finalGraphics, 0, 0);

            finalGraphics.dispose();
            RadianceTabbedPaneUI.closeButtonMap.put(key, result);
        }
        return result;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, final int tabIndex, final int x,
            final int y, int w, int h, boolean isSelected) {
        double scale = RadianceCommonCortex.getScaleFactor(this.tabPane);
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setComposite(WidgetUtilities.getAlphaComposite(this.tabPane, g));

        boolean isEnabled = this.tabPane.isEnabledAt(tabIndex);
        ComponentState currState = this.getTabState(tabIndex, false);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(tabIndex);

        boolean isRollover = (this.getRolloverTab() == tabIndex);
        if (!isSelected && !isRollover && (modelStateInfo == null)) {
            return;
        }

        RadianceColorScheme baseBorderScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, tabIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER, currState);
        RadianceColorScheme baseColorScheme = RadianceColorSchemeUtilities
                .getColorScheme(this.tabPane, tabIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB, currState);
        BufferedImage fullOpacity;
        // double scaleFactor = UIUtil.getScaleFactor();
        // Slightly reduce the tab width to create "gaps" between tab visuals
        w -= 1;

        // check if have windowModified property
        Component comp = this.tabPane.getComponentAt(tabIndex);
        boolean isWindowModified = RadianceCoreUtilities.isTabModified(comp);
        boolean toMarkModifiedCloseButton = RadianceCoreUtilities
                .toAnimateCloseIconOfModifiedTab(this.tabPane, tabIndex);
        if (isWindowModified && isEnabled && !toMarkModifiedCloseButton) {
            RadianceColorScheme colorScheme2 = RadianceColorSchemeUtilities.YELLOW;
            RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.ORANGE;

            float cyclePos = this.modifiedTimelines.get(comp).getTimelinePosition();

            BufferedImage layer1 = RadianceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane,
                    tabIndex, w, h, tabPlacement, RadianceThemingSlices.Side.BOTTOM, colorScheme,
                    baseBorderScheme);
            BufferedImage layer2 = RadianceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane,
                    tabIndex, w, h, tabPlacement, RadianceThemingSlices.Side.BOTTOM, colorScheme2,
                    baseBorderScheme);

            fullOpacity = RadianceCoreUtilities.getBlankUnscaledImage(layer1);
            Graphics2D g2d = fullOpacity.createGraphics();
            if (cyclePos < 1.0f)
                g2d.drawImage(layer1, 0, 0, layer1.getWidth(), layer1.getHeight(), null);
            if (cyclePos > 0.0f) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                g2d.drawImage(layer2, 0, 0, layer2.getWidth(), layer2.getHeight(), null);
            }
            g2d.dispose();
        } else {
            BufferedImage layerBase = RadianceTabbedPaneUI.getFinalTabBackgroundImage(this.tabPane,
                    tabIndex, w, h, tabPlacement, RadianceThemingSlices.Side.BOTTOM,
                    baseColorScheme, baseBorderScheme);

            if ((modelStateInfo == null) || currState.isDisabled()
                    || (modelStateInfo.getStateContributionMap().size() == 1)) {
                fullOpacity = layerBase;
            } else {
                fullOpacity = RadianceCoreUtilities.getBlankUnscaledImage(layerBase);
                Graphics2D g2d = fullOpacity.createGraphics();
                // draw the base layer
                g2d.drawImage(layerBase, 0, 0, layerBase.getWidth(), layerBase.getHeight(), null);

                // draw the other active layers
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo
                        .getStateContributionMap().entrySet()) {
                    ComponentState activeState = activeEntry.getKey();
                    if (activeState == currState)
                        continue;

                    float stateContribution = activeEntry.getValue().getContribution();
                    if (stateContribution > 0.0f) {
                        g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
                        RadianceColorScheme fillScheme = RadianceColorSchemeUtilities
                                .getColorScheme(this.tabPane, tabIndex,
                                        RadianceThemingSlices.ColorSchemeAssociationKind.TAB, activeState);
                        RadianceColorScheme borderScheme = RadianceColorSchemeUtilities
                                .getColorScheme(this.tabPane, tabIndex,
                                        RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER, activeState);
                        BufferedImage layer = RadianceTabbedPaneUI.getFinalTabBackgroundImage(
                                this.tabPane, tabIndex, w, h, tabPlacement,
                                RadianceThemingSlices.Side.BOTTOM, fillScheme, borderScheme);
                        g2d.drawImage(layer, 0, 0, layer.getWidth(), layer.getHeight(), null);
                    }
                }
            }
        }

        // at this point the 'fillOpacity' has all the relevant layers for the
        // fill + border

        // Special handling of selected tabs under skins that show partial visuals
        boolean isCloseMarkOnParentBackground = (RadianceCoreUtilities.getSkin(tabPane)
                .getTabFadeEnd() <= 0.5);
        ComponentState markState = currState;

        if (isCloseMarkOnParentBackground) {
            // Ignore all other "aspects" of tab's state
            markState = this.getTabState(tabIndex, true);
            // this.tabPane.isEnabledAt(tabIndex) ? ComponentState.ENABLED
            // : ComponentState.DISABLED_UNSELECTED;
        }
        RadianceColorScheme baseMarkScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, tabIndex,
                isCloseMarkOnParentBackground ? RadianceThemingSlices.ColorSchemeAssociationKind.FILL
                        : RadianceThemingSlices.ColorSchemeAssociationKind.MARK,
                markState);

        // fix for defect 138
        graphics.clip(new Rectangle(x, y, w, h));

        float finalAlpha = 0.0f;
        StateTransitionTracker tabTracker = this.stateTransitionMultiTracker.getTracker(tabIndex);
        if (!isSelected && (modelStateInfo != null)) {
            finalAlpha += tabTracker.getFacetStrength(RadianceThemingSlices.ComponentStateFacet.ROLLOVER);
        } else {
            if (isRollover || isSelected) {
                finalAlpha = 1.0f;
            }
        }

        finalAlpha *= RadianceColorSchemeUtilities.getAlpha(this.tabPane.getComponentAt(tabIndex),
                currState);

        graphics.setComposite(WidgetUtilities.getAlphaComposite(this.tabPane, finalAlpha, g));
        RadianceCommonCortex.drawImageWithScale(graphics, scale, fullOpacity, x, y);

        // Check if requested to paint close buttons.
        if (RadianceCoreUtilities.hasCloseButton(this.tabPane, tabIndex) && isEnabled) {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            float alpha = (isSelected || isRollover) ? 1.0f : 0.0f;
            if (!isSelected) {
                if (tabTracker != null) {
                    alpha = tabTracker.getFacetStrength(RadianceThemingSlices.ComponentStateFacet.ROLLOVER);
                }
            }
            if (alpha > 0.0) {
                graphics.setComposite(
                        WidgetUtilities.getAlphaComposite(this.tabPane, finalAlpha * alpha, g));

                // paint close button
                Rectangle orig = this.getCloseButtonRectangleForDraw(tabIndex, x, y, w, h);

                boolean toPaintCloseBorder = false;
                if (isRollover) {
                    if (this.radianceMouseLocation != null) {
                        Rectangle bounds = new Rectangle();
                        bounds = this.getTabBounds(tabIndex, bounds);
                        if (toRotateTabsOnPlacement(tabPlacement)) {
                            bounds = new Rectangle(bounds.x, bounds.y, bounds.height, bounds.width);
                        }
                        Rectangle rect = this.getCloseButtonRectangleForEvents(tabIndex, bounds.x,
                                bounds.y, bounds.width, bounds.height);
                        // System.out.println("paint " + bounds + " " + rect +"
                        // "
                        // + mouseLocation);
                        if (rect.contains(this.radianceMouseLocation)) {
                            toPaintCloseBorder = true;
                        }
                    }
                }

                if (isWindowModified && isEnabled && toMarkModifiedCloseButton) {
                    RadianceColorScheme colorScheme2 = RadianceColorSchemeUtilities.YELLOW;
                    RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.ORANGE;

                    float cyclePos = this.modifiedTimelines.get(comp).getTimelinePosition();

                    BufferedImage layer1 = RadianceTabbedPaneUI.getCloseButtonImage(this.tabPane,
                            orig.width, orig.height, toPaintCloseBorder, colorScheme,
                            baseMarkScheme);
                    BufferedImage layer2 = RadianceTabbedPaneUI.getCloseButtonImage(this.tabPane,
                            orig.width, orig.height, toPaintCloseBorder, colorScheme2,
                            baseMarkScheme);

                    if (cyclePos < 1.0f) {
                        RadianceCommonCortex.drawImageWithScale(graphics, scale, layer1, orig.x, orig.y);
                    }
                    if (cyclePos > 0.0f) {
                        graphics.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                        RadianceCommonCortex.drawImageWithScale(graphics, scale, layer2, orig.x, orig.y);
                    }
                } else {
                    BufferedImage layerBase = RadianceTabbedPaneUI.getCloseButtonImage(
                            this.tabPane, orig.width, orig.height, toPaintCloseBorder,
                            baseColorScheme, baseMarkScheme);

                    if ((modelStateInfo == null) || currState.isDisabled()
                            || (modelStateInfo.getStateContributionMap().size() == 1)) {
                        RadianceCommonCortex.drawImageWithScale(graphics, scale, layerBase, orig.x, orig.y);
                    } else {
                        BufferedImage complete = RadianceCoreUtilities.getBlankUnscaledImage(layerBase);
                        Graphics2D g2d = complete.createGraphics();
                        // draw the base layer
                        g2d.drawImage(layerBase, 0, 0, layerBase.getWidth(),
                                layerBase.getHeight(), null);

                        // draw the other active layers
                        Map<ComponentState, StateTransitionTracker.StateContributionInfo> contributionInfoMap = isCloseMarkOnParentBackground
                                ? modelStateInfo.getStateNoSelectionContributionMap()
                                : modelStateInfo.getStateContributionMap();
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : contributionInfoMap
                                .entrySet()) {
                            ComponentState activeState = activeEntry.getKey();
                            if (activeState == currState)
                                continue;

                            float stateContribution = activeEntry.getValue().getContribution();
                            if (stateContribution > 0.0f) {
                                g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));
                                RadianceColorScheme fillScheme = RadianceColorSchemeUtilities
                                        .getColorScheme(this.tabPane, tabIndex,
                                                RadianceThemingSlices.ColorSchemeAssociationKind.TAB, activeState);
                                RadianceColorScheme markScheme = RadianceColorSchemeUtilities
                                        .getColorScheme(this.tabPane, tabIndex,
                                                isCloseMarkOnParentBackground
                                                        ? RadianceThemingSlices.ColorSchemeAssociationKind.FILL
                                                        : RadianceThemingSlices.ColorSchemeAssociationKind.MARK,
                                                activeState);
                                BufferedImage layer = RadianceTabbedPaneUI.getCloseButtonImage(
                                        this.tabPane, orig.width, orig.height, toPaintCloseBorder,
                                        fillScheme, markScheme);
                                g2d.drawImage(layer, 0, 0, layer.getWidth(),
                                        layer.getHeight(), null);
                            }
                        }
                        g2d.dispose();
                        RadianceCommonCortex.drawImageWithScale(graphics, scale, complete, orig.x, orig.y);
                    }
                }
            }
        }

        graphics.dispose();
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects,
            int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // empty to remove Basic functionality
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w,
            int h, boolean isSelected) {
        // empty to remove Basic functionality
    }

    @Override
    protected JButton createScrollButton(final int direction) {
        double scale = RadianceCommonCortex.getScaleFactor(this.tabPane);
        RadianceScrollButton ssb = new RadianceScrollButton();
        Icon icon = new TransitionAwareIcon(ssb, scheme -> {
            // fix for defect 279 - tab pane might not yet have the font installed.
            int fontSize = RadianceSizeUtils.getComponentFontSize(tabPane);
            return RadianceImageCreator.getArrowIcon(scale, fontSize, direction, scheme);
        }, "radiance.theming.internal.tabbedpane.scroll." + direction);
        ssb.setIcon(icon);
        return ssb;
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        boolean toSwap = toRotateTabsOnPlacement(tabPlacement);
        if (toSwap)
            return this.getTabExtraWidth(tabPlacement, tabIndex)
                    + super.calculateTabWidth(tabPlacement, tabIndex, this.getFontMetrics());
        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        boolean toSwap = toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            return super.calculateTabHeight(tabPlacement, tabIndex, metrics.getHeight());
        }
        return this.getTabExtraWidth(tabPlacement, tabIndex)
                + super.calculateTabWidth(tabPlacement, tabIndex, metrics);
    }

    @Override
    protected int calculateMaxTabHeight(int tabPlacement) {
        if (toRotateTabsOnPlacement(tabPlacement))
            return super.calculateMaxTabHeight(tabPlacement);
        int result = 0;
        for (int i = 0; i < this.tabPane.getTabCount(); i++)
            result = Math.max(result,
                    this.calculateTabHeight(tabPlacement, i, this.getFontMetrics().getHeight()));
        return result;
    }

    @Override
    protected int getTabRunOverlay(int tabPlacement) {
        boolean toSwap = this.toRotateTabsOnPlacement(tabPlacement);
        if (toSwap)
            return super.getTabRunOverlay(tabPlacement);

        return 0;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        int selectedIndex = tabPane.getSelectedIndex();
        int tabPlacement = tabPane.getTabPlacement();

        ensureCurrentLayout();

        // If scrollable tabs are enabled, the tab area will be
        // painted by the scrollable tab panel instead.
        if (tabPane.getLayout().getClass() == TabbedPaneLayout.class) {
            paintTabArea(g, tabPlacement, selectedIndex);
        }

        int width = tabPane.getWidth();
        int height = tabPane.getHeight();
        Insets insets = tabPane.getInsets();

        int x = insets.left;
        int y = insets.top;
        int w = width - insets.right - insets.left;
        int h = height - insets.top - insets.bottom;

        switch (tabPlacement) {
            case LEFT:
                x += calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                w -= (x - insets.left);
                break;
            case RIGHT:
                w -= calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
                break;
            case BOTTOM:
                h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                break;
            case TOP:
            default:
                y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                h -= (y - insets.top);
        }

        Graphics2D g2d = (Graphics2D) g.create(x, y, w, h);
        BackgroundPaintingUtils.update(g2d, c, false);

        paintContentBorder(g, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
            Rectangle iconRect, Rectangle textRect) {
        boolean toSwap = toRotateTabsOnPlacement(tabPlacement);
        if (toSwap) {
            Graphics2D tempG = (Graphics2D) g.create();
            Rectangle tabRect = rects[tabIndex];
            Rectangle correctRect = new Rectangle(tabRect.x, tabRect.y, tabRect.height,
                    tabRect.width);
            if (tabPlacement == SwingConstants.LEFT) {
                // rotate 90 degrees counterclockwise for LEFT orientation
                tempG.rotate(-Math.PI / 2, tabRect.x, tabRect.y);
                tempG.translate(-tabRect.height, 0);
            } else {
                // rotate 90 degrees clockwise for RIGHT orientation
                tempG.rotate(Math.PI / 2, tabRect.x, tabRect.y);
                tempG.translate(0, -tabRect.getWidth());
            }
            tempG.setColor(Color.red);
            rects[tabIndex] = correctRect;
            super.paintTab(tempG, tabPlacement, rects, tabIndex, iconRect, textRect);
            rects[tabIndex] = tabRect;
            tempG.dispose();
        } else {
            if (tabPane.getLayout().getClass() == TabbedPaneLayout.class) {
                super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
            } else {
                // scrolled tabs are painted by
                // BasicTabbedPaneUI.ScrollableTabPanel
                // which does not have the right rendering hints
                Graphics2D g2d = (Graphics2D) g.create();
                RadianceCommonCortex.installDesktopHints(g2d, tabPane.getFont());
                super.paintTab(g2d, tabPlacement, rects, tabIndex, iconRect, textRect);
                g2d.dispose();
            }
        }
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        if (this.radianceContentOpaque) {
            int width = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
            if ((tabPlacement == SwingConstants.TOP) || (tabPlacement == SwingConstants.BOTTOM))
                width = Math.max(width, tabPane.getWidth());
            int height = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            if (toRotateTabsOnPlacement(tabPlacement))
                height = Math.max(height, tabPane.getHeight());

            // restrict the painting to the tab area only
            Graphics2D g2d = (Graphics2D) g.create(0, 0, width, height);
            BackgroundPaintingUtils.update(g2d, this.tabPane, true);
            g2d.dispose();
        }
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }

    /**
     * Retrieves the close button rectangle for drawing purposes.
     * 
     * @param tabIndex
     *            Tab index.
     * @param x
     *            X coordinate of the tab.
     * @param y
     *            Y coordinate of the tab.
     * @param width
     *            The tab width.
     * @param height
     *            The tab height.
     * @return The close button rectangle.
     */
    protected Rectangle getCloseButtonRectangleForDraw(int tabIndex, int x, int y, int width,
            int height) {
        int dimension = RadianceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);

        int borderDelta = (int) Math.ceil(3.0f + RadianceSizeUtils.getBorderStrokeWidth(tabPane));

        int xs = this.tabPane.getComponentOrientation().isLeftToRight()
                ? x + width - dimension - borderDelta
                : x + borderDelta;
        int ys = y + (height - dimension) / 2 + 1;
        if (this.tabPane.getTabPlacement() == SwingUtilities.BOTTOM) {
            ys -= 2;
        }
        return new Rectangle(xs, ys, dimension, dimension);
    }

    /**
     * Retrieves the close button rectangle for event handling.
     * 
     * @param tabIndex
     *            Tab index.
     * @param x
     *            X coordinate of the tab.
     * @param y
     *            Y coordinate of the tab.
     * @param w
     *            The tab width.
     * @param h
     *            The tab height.
     * @return The close button rectangle.
     */
    protected Rectangle getCloseButtonRectangleForEvents(int tabIndex, int x, int y, int w, int h) {
        int tabPlacement = this.tabPane.getTabPlacement();
        boolean toSwap = toRotateTabsOnPlacement(tabPlacement);
        if (!toSwap) {
            return this.getCloseButtonRectangleForDraw(tabIndex, x, y, w, h);
        }
        int dimension = RadianceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);

        Point2D transCorner;
        Rectangle rectForDraw = this.getCloseButtonRectangleForDraw(tabIndex, x, y, h, w);
        AffineTransform trans = new AffineTransform();
        if (tabPlacement == SwingConstants.LEFT) {
            trans.rotate(-Math.PI / 2, x, y);
            trans.translate(-h, 0);
            Point2D.Double origCorner = new Point2D.Double(rectForDraw.getMaxX(),
                    rectForDraw.getMinY());
            transCorner = trans.transform(origCorner, null);
        } else {
            // rotate 90 degrees clockwise for RIGHT orientation
            trans.rotate(Math.PI / 2, x, y);
            trans.translate(0, -w);
            Point2D.Double origCorner = new Point2D.Double(rectForDraw.getMinX(),
                    rectForDraw.getMaxY());
            transCorner = trans.transform(origCorner, null);
        }
        return new Rectangle((int) transCorner.getX(), (int) transCorner.getY(), dimension,
                dimension);
    }

    /**
     * Implementation of the fade tracker callback that repaints a single tab.
     * 
     * @author Kirill Grouchnikov
     */
    protected class TabRepaintCallback extends EventDispatchThreadTimelineCallbackAdapter {
        /**
         * The associated tabbed pane.
         */
        protected JTabbedPane tabbedPane;

        /**
         * The associated tab index.
         */
        protected int tabIndex;

        /**
         * Creates new tab repaint callback.
         * 
         * @param tabPane
         *            The associated tabbed pane.
         * @param tabIndex
         *            The associated tab index.
         */
        public TabRepaintCallback(JTabbedPane tabPane, int tabIndex) {
            this.tabbedPane = tabPane;
            this.tabIndex = tabIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintTab();
        }

        @Override
        public void onTimelineStateChanged(TimelineState oldState, TimelineState newState,
                float durationFraction, float timelinePosition) {
            this.repaintTab();
        }

        /**
         * Repaints the relevant tab.
         */
        protected void repaintTab() {
            SwingUtilities.invokeLater(() -> {
                if (RadianceTabbedPaneUI.this.tabPane == null) {
                    // may happen if the LAF was switched in the meantime
                    return;
                }
                RadianceTabbedPaneUI.this.ensureCurrentLayout();
                int tabCount = RadianceTabbedPaneUI.this.tabPane.getTabCount();
                if ((tabCount > 0) && (TabRepaintCallback.this.tabIndex < tabCount)
                        && (TabRepaintCallback.this.tabIndex < RadianceTabbedPaneUI.this.rects.length)) {
                    // need to retrieve the tab rectangle since the tabs
                    // can be moved while animating (especially when the
                    // current layout is SCROLL_LAYOUT)
                    Rectangle rect = RadianceTabbedPaneUI.this.getTabBounds(
                            RadianceTabbedPaneUI.this.tabPane, TabRepaintCallback.this.tabIndex);
                    // System.out.println("Repainting " + tabIndex);
                    RadianceTabbedPaneUI.this.tabPane.repaint(rect);
                }
            });
        }
    }

    /**
     * Ensures the current layout.
     */
    protected void ensureCurrentLayout() {
        if (!this.tabPane.isValid()) {
            this.tabPane.validate();
        }
        /*
         * If tabPane doesn't have a peer yet, the validate() call will silently fail. We handle
         * that by forcing a layout if tabPane is still invalid. See bug 4237677.
         */
        if (!this.tabPane.isValid()) {
            LayoutManager lm = this.tabPane.getLayout();
            if (lm instanceof BasicTabbedPaneUI.TabbedPaneLayout) {
                BasicTabbedPaneUI.TabbedPaneLayout layout = (BasicTabbedPaneUI.TabbedPaneLayout) lm;
                layout.calculateLayoutInfo();
            }
        }
    }

    /**
     * Tries closing tabs based on the specified tab index and tab close kind.
     * 
     * @param tabIndex
     *            Tab index.
     * @param tabCloseKind
     *            Tab close kind.
     */
    protected void tryCloseTabs(int tabIndex, RadianceThemingSlices.TabCloseKind tabCloseKind) {
        if (tabCloseKind == null)
            return;
        if (tabCloseKind == RadianceThemingSlices.TabCloseKind.NONE)
            return;

        if (tabCloseKind == RadianceThemingSlices.TabCloseKind.ALL_BUT_THIS) {
            // close all but this
            Set<Integer> indexes = new HashSet<>();
            for (int i = 0; i < this.tabPane.getTabCount(); i++)
                if (i != tabIndex)
                    indexes.add(i);
            this.tryCloseTabs(indexes);
            return;
        }
        if (tabCloseKind == RadianceThemingSlices.TabCloseKind.ALL) {
            // close all
            Set<Integer> indexes = new HashSet<>();
            for (int i = 0; i < this.tabPane.getTabCount(); i++)
                indexes.add(i);
            this.tryCloseTabs(indexes);
            return;
        }
        this.tryCloseTab(tabIndex);
    }

    /**
     * Tries closing a single tab.
     * 
     * @param tabIndex
     *            Tab index.
     */
    protected void tryCloseTab(int tabIndex) {
        Component component = this.tabPane.getComponentAt(tabIndex);
        Set<Component> componentSet = new HashSet<>();
        componentSet.add(component);

        // check if there's at least one listener
        // that vetoes the closing
        boolean isVetoed = false;
        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof VetoableTabCloseListener) {
                VetoableTabCloseListener vetoableListener = (VetoableTabCloseListener) listener;
                isVetoed = isVetoed || vetoableListener.vetoTabClosing(this.tabPane, component);
            }
            if (listener instanceof VetoableMultipleTabCloseListener) {
                VetoableMultipleTabCloseListener vetoableListener = (VetoableMultipleTabCloseListener) listener;
                isVetoed = isVetoed || vetoableListener.vetoTabsClosing(this.tabPane, componentSet);
            }
        }
        if (isVetoed)
            return;

        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof TabCloseListener)
                ((TabCloseListener) listener).tabClosing(this.tabPane, component);
            if (listener instanceof MultipleTabCloseListener)
                ((MultipleTabCloseListener) listener).tabsClosing(this.tabPane, componentSet);
        }

        this.tabPane.remove(tabIndex);
        if (this.tabPane.getTabCount() > 0) {
            this.selectPreviousTab(0);
            this.selectNextTab(this.tabPane.getSelectedIndex());
        }
        this.tabPane.repaint();

        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof TabCloseListener)
                ((TabCloseListener) listener).tabClosed(this.tabPane, component);
            if (listener instanceof MultipleTabCloseListener)
                ((MultipleTabCloseListener) listener).tabsClosed(this.tabPane, componentSet);
        }
    }

    /**
     * Tries closing the specified tabs.
     * 
     * @param tabIndexes
     *            Tab indexes.
     */
    protected void tryCloseTabs(Set<Integer> tabIndexes) {
        Set<Component> componentSet = new HashSet<>();
        for (int tabIndex : tabIndexes) {
            componentSet.add(this.tabPane.getComponentAt(tabIndex));
        }

        // check if there's at least one listener
        // that vetoes the closing
        boolean isVetoed = false;
        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof VetoableMultipleTabCloseListener) {
                VetoableMultipleTabCloseListener vetoableListener = (VetoableMultipleTabCloseListener) listener;
                isVetoed = isVetoed || vetoableListener.vetoTabsClosing(this.tabPane, componentSet);
            }
        }
        if (isVetoed)
            return;

        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof MultipleTabCloseListener)
                ((MultipleTabCloseListener) listener).tabsClosing(this.tabPane, componentSet);
        }

        for (Component toRemove : componentSet) {
            this.tabPane.remove(toRemove);
        }

        if (this.tabPane.getTabCount() > 0) {
            this.selectPreviousTab(0);
            this.selectNextTab(this.tabPane.getSelectedIndex());
        }
        this.tabPane.repaint();

        for (BaseTabCloseListener listener : RadianceThemingCortex.ComponentScope
                .getAllTabCloseListeners(this.tabPane)) {
            if (listener instanceof MultipleTabCloseListener)
                ((MultipleTabCloseListener) listener).tabsClosed(this.tabPane, componentSet);
        }
    }

    @Override
    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        int delta = 0;
        if (RadianceCoreUtilities.hasCloseButton(this.tabPane, tabIndex)) {
            if (this.tabPane.getComponentOrientation().isLeftToRight()) {
                delta = 5 - RadianceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex);
            } else {
                delta = RadianceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex) - 5;
            }
        }
        return delta + super.getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
    }

    @Override
    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return (tabPlacement == SwingConstants.BOTTOM) ? -1 : 1;
    }

    /**
     * Returns extra width for the specified tab.
     * 
     * @param tabPlacement
     *            Tab placement.
     * @param tabIndex
     *            Tab index.
     * @return Extra width for the specified tab.
     */
    protected int getTabExtraWidth(int tabPlacement, int tabIndex) {
        int extraWidth;
        RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(this.tabPane);
        if (shaper instanceof ClassicButtonShaper)
            extraWidth = (int) (2.0 * RadianceSizeUtils.getClassicButtonCornerRadius(
                    RadianceSizeUtils.getComponentFontSize(this.tabPane)));
        else
            extraWidth = super.calculateTabHeight(tabPlacement, tabIndex,
                    this.getFontMetrics().getHeight()) / 3;

        if (RadianceCoreUtilities.hasCloseButton(this.tabPane, tabIndex)
                && this.tabPane.isEnabledAt(tabIndex)) {
            extraWidth += (4 + RadianceCoreUtilities.getCloseButtonSize(this.tabPane, tabIndex));
        }

        // System.out.println(tabPane.getTitleAt(tabIndex) + ":" + extraWidth);
        return extraWidth;
    }

    /**
     * Returns the index of the tab currently being rolled-over.
     * 
     * @return Index of the tab currently being rolled-over.
     */
    public int getRolloverTabIndex() {
        return this.getRolloverTab();
    }

    /**
     * Sets new value for tab area insets.
     * 
     * @param insets
     *            Tab area insets.
     */
    public void setTabAreaInsets(Insets insets) {
        Insets old = this.tabAreaInsets;
        this.tabAreaInsets = insets;
        // Fire a property change event so that the tabbed
        // pane can revalidate itself
        WidgetUtilities.firePropertyChangeEvent(this.tabPane, "tabAreaInsets", old, tabAreaInsets);
    }

    /**
     * Returns tab area insets.
     * 
     * @return Tab area insets.
     */
    public Insets getTabAreaInsets() {
        return this.tabAreaInsets;
    }

    /**
     * Returns the tab rectangle for the specified tab.
     * 
     * @param tabIndex
     *            Index of a tab.
     * @return The tab rectangle for the specified parameters.
     */
    public Rectangle getTabRectangle(int tabIndex) {
        return this.rects[tabIndex];
    }

    @Override
    protected boolean shouldPadTabRun(int tabPlacement, int run) {
        // Don't pad last run
        return this.runCount > 1 && run < this.runCount - 1;
    }

    @Override
    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
            return super.createLayoutManager();
        }
        return new TabbedPaneLayout();
    }

    /**
     * Layout for the tabbed pane.
     * 
     * @author Kirill Grouchnikov
     */
    public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
        /**
         * Creates a new layout.
         */
        public TabbedPaneLayout() {
            RadianceTabbedPaneUI.this.super();
        }

        @Override
        protected void normalizeTabRuns(int tabPlacement, int tabCount, int start, int max) {
            // Only normalize the runs for top & bottom; normalizing
            // doesn't look right for Metal's vertical tabs
            // because the last run isn't padded and it looks odd to have
            // fat tabs in the first vertical runs, but slimmer ones in the
            // last (this effect isn't noticeable for horizontal tabs).
            if (tabPlacement == TOP || tabPlacement == BOTTOM) {
                super.normalizeTabRuns(tabPlacement, tabCount, start, max);
            }
        }

        @Override
        protected void rotateTabRuns(int tabPlacement, int selectedRun) {
            // Don't rotate runs!
        }

        @Override
        protected void padSelectedTab(int tabPlacement, int selectedIndex) {
            // Don't pad selected tab
        }
    }

    @Override
    protected Insets getContentBorderInsets(int tabPlacement) {
        Insets insets = RadianceSizeUtils.getTabbedPaneContentInsets(this.tabPane);

        RadianceThemingSlices.TabContentPaneBorderKind kind = RadianceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_FULL)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        boolean isPlacement = (kind == RadianceThemingSlices.TabContentPaneBorderKind.SINGLE_PLACEMENT)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        int delta = isDouble
                ? (int) Math.ceil(RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) + 1.5f)
                : 0;

        if (isPlacement) {
            switch (tabPlacement) {
                case TOP:
                    return new Insets(insets.top + delta, 0, 0, 0);
                case LEFT:
                    return new Insets(0, insets.left + delta, 0, 0);
                case RIGHT:
                    return new Insets(0, 0, 0, insets.right + delta);
                case BOTTOM:
                    return new Insets(0, 0, insets.bottom + delta, 0);
            }
        } else {
            switch (tabPlacement) {
                case TOP:
                    return new Insets(insets.top + delta, insets.left, insets.bottom, insets.right);
                case LEFT:
                    return new Insets(insets.top, insets.left + delta, insets.bottom, insets.right);
                case RIGHT:
                    return new Insets(insets.top, insets.left, insets.bottom, insets.right + delta);
                case BOTTOM:
                    return new Insets(insets.top, insets.left, insets.bottom + delta, insets.right);
            }
        }
        return insets;
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        RadianceColorScheme scheme = RadianceColorSchemeUtilities.getColorScheme(this.tabPane,
                selectedIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB, ComponentState.ENABLED);
        this.highlight = scheme.getSeparatorSecondaryColor();
        super.paintContentBorder(g, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex,
            int x, int y, int w, int h) {
        RadianceThemingSlices.TabContentPaneBorderKind kind = RadianceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_FULL)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        boolean isPlacement = (kind == RadianceThemingSlices.TabContentPaneBorderKind.SINGLE_PLACEMENT)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        if (isPlacement) {
            if (tabPlacement != SwingConstants.BOTTOM)
                return;
        }

        Rectangle selRect = selectedIndex < 0 ? null
                : this.getTabBounds(selectedIndex, this.calcRect);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane);
        int joinKind = BasicStroke.JOIN_ROUND;
        int capKind = BasicStroke.CAP_BUTT;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int) (strokeWidth / 2.0);
        int ribbonDelta = (int) Math.ceil(strokeWidth + 1.5f);

        boolean isUnbroken = (tabPlacement != BOTTOM || selectedIndex < 0 || (selRect.y - 1 > h)
                || (selRect.x < x || selRect.x > x + w));

        x += offset;
        y += offset;
        w -= 2 * offset;
        h -= 2 * offset;

        // Draw unbroken line if tabs are not on BOTTOM, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        RadianceColorScheme borderScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, selectedIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER,
                ComponentState.SELECTED);
        Color darkShadowColor = RadianceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x, y + h - 1, x + w, y + h - 1);
        } else {
            // Break line to show visual connection to selected tab
            RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = (shaper instanceof ClassicButtonShaper) ? 1 : 0;
            float borderInsets = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) / 2.0f;
            GeneralPath bottomOutline = new GeneralPath();
            bottomOutline.moveTo(x, y + h - 1);
            bottomOutline.lineTo(selRect.x + borderInsets, y + h - 1);
            int bumpHeight = super.calculateTabHeight(tabPlacement, 0,
                    RadianceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            bottomOutline.lineTo(selRect.x + borderInsets, y + h + bumpHeight);
            if (selRect.x + selRect.width < x + w) {
                float selectionEndX = selRect.x + selRect.width - delta - 1 - borderInsets;
                bottomOutline.lineTo(selectionEndX, y + h - 1 + bumpHeight);
                bottomOutline.lineTo(selectionEndX, y + h - 1);
                bottomOutline.lineTo(x + w, y + h - 1);
            }
            g2d.setPaint(new GradientPaint(x, y + h - 1, darkShadowColor, x, y + h - 1 + bumpHeight,
                    RadianceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(bottomOutline);
        }

        if (isDouble) {
            if (tabPlacement == BOTTOM) {
                g2d.setColor(this.highlight);
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x, y + h - 1 - ribbonDelta, x + w, y + h - 1 - ribbonDelta);
            }
            if (tabPlacement == LEFT) {
                g2d.setPaint(new GradientPaint(x, y + h - 1, darkShadowColor, x + 4 * ribbonDelta,
                        y + h - 1, this.highlight));
                g2d.drawLine(x, y + h - 1, x + 4 * ribbonDelta, y + h - 1);
            }
            if (tabPlacement == RIGHT) {
                g2d.setPaint(new GradientPaint(x + w - 1 - 4 * ribbonDelta, y + h - 1,
                        this.highlight, x + w - 1, y + h - 1, darkShadowColor));
                g2d.drawLine(x + w - 1 - 4 * ribbonDelta, y + h - 1, x + w - 1, y + h - 1);
            }
        }

        g2d.dispose();
    }

    @Override
    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex,
            int x, int y, int w, int h) {
        RadianceThemingSlices.TabContentPaneBorderKind kind = RadianceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_FULL)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        boolean isPlacement = (kind == RadianceThemingSlices.TabContentPaneBorderKind.SINGLE_PLACEMENT)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        if (isPlacement) {
            if (tabPlacement != SwingConstants.LEFT)
                return;
        }
        int ribbonDelta = (int) (RadianceSizeUtils.getBorderStrokeWidth(tabPane) + 1.5f);

        Rectangle selRect = selectedIndex < 0 ? null
                : this.getTabBounds(selectedIndex, this.calcRect);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane);
        int joinKind = BasicStroke.JOIN_ROUND;
        int capKind = BasicStroke.CAP_BUTT;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int) (strokeWidth / 2.0);

        boolean isUnbroken = (tabPlacement != LEFT || selectedIndex < 0
                || (selRect.x + selRect.width + 1 < x) || (selRect.y < y || selRect.y > y + h));

        x += offset;
        y += offset;
        // w -= 2 * offset;
        h -= 2 * offset;

        // Draw unbroken line if tabs are not on LEFT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        RadianceColorScheme borderScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, selectedIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER,
                ComponentState.SELECTED);
        Color darkShadowColor = RadianceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x, y, x, y + h);
        } else {
            // Break line to show visual connection to selected tab
            RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = (shaper instanceof ClassicButtonShaper) ? 1 : 0;

            float borderInsets = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) / 2.0f;
            GeneralPath leftOutline = new GeneralPath();
            leftOutline.moveTo(x, y);
            leftOutline.lineTo(x, selRect.y + borderInsets + delta + 1);
            int bumpWidth = super.calculateTabHeight(tabPlacement, 0,
                    RadianceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            leftOutline.lineTo(x - bumpWidth, selRect.y + borderInsets + delta + 1);
            if (selRect.y + selRect.height < y + h) {
                float selectionEndY = selRect.y + selRect.height - borderInsets;
                leftOutline.lineTo(x - bumpWidth, selectionEndY);
                leftOutline.lineTo(x, selectionEndY);
                leftOutline.lineTo(x, y + h);
            }
            g2d.setPaint(new GradientPaint(x, y, darkShadowColor, x - bumpWidth, y,
                    RadianceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(leftOutline);
        }

        if (isDouble) {
            if (tabPlacement == LEFT) {
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x + ribbonDelta, y, x + ribbonDelta, y + h);
                // g2d.setColor(this.highlight);
                // g2d.drawLine(x + 1 + ribbonDelta, y + 1, x + 1 + ribbonDelta,
                // y +
                // h - 1);
            }
            if (tabPlacement == TOP) {
                g2d.setPaint(new GradientPaint(x, y, darkShadowColor, x, y + 4 * ribbonDelta,
                        this.highlight));
                g2d.drawLine(x, y, x, y + 4 * ribbonDelta);
            }
            if (tabPlacement == BOTTOM) {
                g2d.setPaint(new GradientPaint(x, y + h - 1 - 4 * ribbonDelta, this.highlight, x,
                        y + h - 1, darkShadowColor));
                g2d.drawLine(x, y + h - 1 - 4 * ribbonDelta, x, y + h - 1);
            }
        }
        g2d.dispose();
    }

    @Override
    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex,
            int x, int y, int w, int h) {
        RadianceThemingSlices.TabContentPaneBorderKind kind = RadianceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_FULL)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        boolean isPlacement = (kind == RadianceThemingSlices.TabContentPaneBorderKind.SINGLE_PLACEMENT)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        if (isPlacement) {
            if (tabPlacement != SwingConstants.RIGHT)
                return;
        }
        int ribbonDelta = (int) (RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) + 1.5f);

        Rectangle selRect = selectedIndex < 0 ? null
                : this.getTabBounds(selectedIndex, this.calcRect);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane);
        int joinKind = BasicStroke.JOIN_ROUND;
        int capKind = BasicStroke.CAP_BUTT;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int) (strokeWidth / 2.0);

        boolean isUnbroken = (tabPlacement != RIGHT || selectedIndex < 0 || (selRect.x - 1 > w)
                || (selRect.y < y || selRect.y > y + h));

        x += offset;
        y += offset;
        w -= 2 * offset;
        h -= 2 * offset;

        // Draw unbroken line if tabs are not on RIGHT, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        RadianceColorScheme borderScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, selectedIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER,
                ComponentState.SELECTED);
        Color darkShadowColor = RadianceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x + w - 1, y, x + w - 1, y + h);
        } else {
            // Break line to show visual connection to selected tab
            RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = (shaper instanceof ClassicButtonShaper) ? 1 : 0;

            float borderInsets = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) / 2.0f;
            GeneralPath rightOutline = new GeneralPath();
            rightOutline.moveTo(x + w - 1, y);
            rightOutline.lineTo(x + w - 1, selRect.y + borderInsets);
            int bumpWidth = super.calculateTabHeight(tabPlacement, 0,
                    RadianceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            rightOutline.lineTo(x + w - 1 + bumpWidth, selRect.y + borderInsets);
            if (selRect.y + selRect.height < y + h) {
                float selectionEndY = selRect.y + selRect.height - delta - 1 - borderInsets;
                rightOutline.lineTo(x + w - 1 + bumpWidth, selectionEndY);
                rightOutline.lineTo(x + w - 1, selectionEndY);
                rightOutline.lineTo(x + w - 1, y + h);
            }
            g2d.setPaint(new GradientPaint(x + w - 1, y, darkShadowColor, x + w - 1 + bumpWidth, y,
                    RadianceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(rightOutline);
        }

        if (isDouble) {
            if (tabPlacement == RIGHT) {
                g2d.setColor(this.highlight);
                // g2d.drawLine(x + w - 2 - ribbonDelta, y + 1, x + w - 2 -
                // ribbonDelta, y + h - 1);
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x + w - 1 - ribbonDelta, y, x + w - 1 - ribbonDelta, y + h);
            }
            if (tabPlacement == TOP) {
                g2d.setPaint(new GradientPaint(x + w - 1, y, darkShadowColor, x + w - 1,
                        y + 4 * ribbonDelta, this.highlight));
                g2d.drawLine(x + w - 1, y, x + w - 1, y + 4 * ribbonDelta);
            }
            if (tabPlacement == BOTTOM) {
                g2d.setPaint(new GradientPaint(x + w - 1, y + h - 1 - 4 * ribbonDelta,
                        this.highlight, x + w - 1, y + h - 1, darkShadowColor));
                g2d.drawLine(x + w - 1, y + h - 1 - 4 * ribbonDelta, x + w - 1, y + h - 1);
            }
        }
        g2d.dispose();
    }

    @Override
    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x,
            int y, int w, int h) {
        RadianceThemingSlices.TabContentPaneBorderKind kind = RadianceCoreUtilities.getContentBorderKind(this.tabPane);
        boolean isDouble = (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_FULL)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        boolean isPlacement = (kind == RadianceThemingSlices.TabContentPaneBorderKind.SINGLE_PLACEMENT)
                || (kind == RadianceThemingSlices.TabContentPaneBorderKind.DOUBLE_PLACEMENT);
        if (isPlacement) {
            if (tabPlacement != SwingConstants.TOP)
                return;
        }

        Rectangle selRect = selectedIndex < 0 ? null
                : this.getTabBounds(selectedIndex, this.calcRect);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        float strokeWidth = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane);
        int joinKind = BasicStroke.JOIN_ROUND;
        int capKind = BasicStroke.CAP_BUTT;
        g2d.setStroke(new BasicStroke(strokeWidth, capKind, joinKind));
        int offset = (int) (strokeWidth / 2.0);

        int ribbonDelta = (int) Math.ceil(strokeWidth + 1.5f);

        boolean isUnbroken = (tabPlacement != TOP || selectedIndex < 0
                || (selRect.y + selRect.height + 1 < y) || (selRect.x < x || selRect.x > x + w));

        x += offset;
        y += offset;
        w -= 2 * offset;
        // h -= 2 * offset;

        // Draw unbroken line if tabs are not on TOP, OR
        // selected tab is not in run adjacent to content, OR
        // selected tab is not visible (SCROLL_TAB_LAYOUT)
        RadianceColorScheme borderScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.tabPane, selectedIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB_BORDER,
                ComponentState.SELECTED);
        Color darkShadowColor = RadianceColorUtilities.getMidBorderColor(borderScheme);
        if (isUnbroken) {
            g2d.setColor(this.highlight);
            g2d.drawLine(x, y, x + w, y);
        } else {
            // Break line to show visual connection to selected tab
            RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(this.tabPane);
            int delta = (shaper instanceof ClassicButtonShaper) ? 1 : 0;
            float borderInsets = RadianceSizeUtils.getBorderStrokeWidth(this.tabPane) / 2.0f;
            GeneralPath topOutline = new GeneralPath();
            topOutline.moveTo(x, y);
            topOutline.lineTo(selRect.x + borderInsets, y);
            int bumpHeight = super.calculateTabHeight(tabPlacement, 0,
                    RadianceSizeUtils.getComponentFontSize(this.tabPane)) / 2;
            topOutline.lineTo(selRect.x + borderInsets, y - bumpHeight);
            if (selRect.x + selRect.width < x + w) {
                float selectionEndX = selRect.x + selRect.width - delta - 1 - borderInsets;
                topOutline.lineTo(selectionEndX, y - bumpHeight);
                topOutline.lineTo(selectionEndX, y);
                topOutline.lineTo(x + w, y);
            }
            g2d.setPaint(new GradientPaint(x, y, darkShadowColor, x, y - bumpHeight,
                    RadianceColorUtilities.getAlphaColor(darkShadowColor, 0)));
            g2d.draw(topOutline);
        }

        if (isDouble) {
            if (tabPlacement == TOP) {
                g2d.setColor(darkShadowColor);
                g2d.drawLine(x, y + ribbonDelta, x + w, y + ribbonDelta);
            }
            if (tabPlacement == LEFT) {
                g2d.setPaint(new GradientPaint(x, y, darkShadowColor, x + 4 * ribbonDelta, y,
                        this.highlight));
                g2d.drawLine(x, y, x + 4 * ribbonDelta, y);
            }
            if (tabPlacement == RIGHT) {
                g2d.setPaint(new GradientPaint(x + w - 1 - 4 * ribbonDelta, y, this.highlight,
                        x + w - 1, y, darkShadowColor));
                g2d.drawLine(x + w - 1 - 4 * ribbonDelta, y, x + w - 1, y);
            }
        }

        g2d.dispose();
    }

    @Override
    public Rectangle getTabBounds(JTabbedPane pane, int i) {
        this.ensureCurrentLayout();
        Rectangle tabRect = new Rectangle();
        return this.getTabBounds(i, tabRect);
    }

    private StateTransitionTracker.ModelStateInfo getModelStateInfo(int tabIndex) {
        if (this.stateTransitionMultiTracker.size() == 0)
            return null;
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(tabIndex);
        if (tracker == null) {
            return null;
        } else {
            return tracker.getModelStateInfo();
        }
    }

    /**
     * Returns the current state for the specified tab.
     * 
     * @param tabIndex
     *            Tab index.
     * @return The current state for the specified tab.
     */
    protected ComponentState getTabState(int tabIndex, boolean toAllowIgnoringSelectedState) {
        boolean isEnabled = this.tabPane.isEnabledAt(tabIndex);
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(tabIndex);
        boolean ignoreSelectedState = toAllowIgnoringSelectedState
                && (RadianceCoreUtilities.getSkin(tabPane).getTabFadeEnd() <= 0.5);
        if (tracker == null) {
            boolean isRollover = this.getRolloverTabIndex() == tabIndex;
            boolean isSelected = ignoreSelectedState ? false
                    : this.tabPane.getSelectedIndex() == tabIndex;
            return ComponentState.getState(isEnabled, isRollover, isSelected);
        } else {
            ComponentState fromTracker = ignoreSelectedState
                    ? tracker.getModelStateInfo().getCurrModelStateNoSelection()
                    : tracker.getModelStateInfo().getCurrModelState();
            return fromTracker;
            // return ComponentState.getState(isEnabled,
            // fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER),
            // ignoreSelectedState ? false :
            // fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
        }
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
            int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        g.setFont(font);

        View v = this.getTextViewForTab(tabIndex);
        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = this.tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            // Special handling of tabs under skins that show partial visuals
            boolean isTextOnParentBackground = (RadianceCoreUtilities.getSkin(tabPane)
                    .getTabFadeEnd() <= 0.5);
            ComponentState currState = this.getTabState(tabIndex, true);
            if (isTextOnParentBackground) {
                // Ignore all other "aspects" of tab's state
                currState = this.tabPane.isEnabledAt(tabIndex) ? ComponentState.ENABLED
                        : ComponentState.DISABLED_UNSELECTED;
            }
            StateTransitionTracker.ModelStateInfo modelStateInfo = isTextOnParentBackground ? null
                    : this.getModelStateInfo(tabIndex);

            // System.out.println("Tab " + title + ":" + currState);
            Color fg = null;
            if (modelStateInfo != null) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo
                        .getStateContributionMap();
                RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.getColorScheme(
                        tabPane, tabIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB, currState);
                if (currState.isDisabled() || (activeStates == null)
                        || (activeStates.size() == 1)) {
                    fg = colorScheme.getForegroundColor();
                } else {
                    float aggrRed = 0;
                    float aggrGreen = 0;
                    float aggrBlue = 0;

                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates
                            .entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        RadianceColorScheme scheme = RadianceColorSchemeUtilities.getColorScheme(
                                tabPane, tabIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB, activeState);
                        Color schemeFg = scheme.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        // System.out.println("\t" + activeState + ":"
                        // + contribution + ":" + scheme.getDisplayName()
                        // + ":" + schemeFg);
                        aggrRed += schemeFg.getRed() * contribution;
                        aggrGreen += schemeFg.getGreen() * contribution;
                        aggrBlue += schemeFg.getBlue() * contribution;
                    }
                    // System.out.println("");
                    fg = new Color((int) aggrRed, (int) aggrGreen, (int) aggrBlue);
                }
            } else {
                RadianceColorScheme scheme = RadianceColorSchemeUtilities.getColorScheme(tabPane,
                        tabIndex, RadianceThemingSlices.ColorSchemeAssociationKind.TAB, currState);
                fg = scheme.getForegroundColor();
            }

            Graphics2D graphics = (Graphics2D) g.create();
            if (currState.isDisabled()) {
                Color bgFillColor = RadianceColorUtilities.getBackgroundFillColor(this.tabPane);
                fg = RadianceColorUtilities.getInterpolatedColor(fg, bgFillColor,
                        RadianceColorSchemeUtilities.getAlpha(
                                this.tabPane.getComponentAt(tabIndex), currState));
            }
            graphics.clip(getTabRectangle(tabIndex));
            RadianceTextUtilities.paintText(graphics, textRect, title, mnemIndex,
                    graphics.getFont(), fg, null);
            graphics.dispose();

            this.tabTextColorMap.put(tabIndex, fg);
        }
    }

    @Override
    protected void paintIcon(Graphics g, int tabPlacement, int tabIndex, Icon icon,
            Rectangle iconRect, boolean isSelected) {
        if (icon == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(iconRect.x, iconRect.y);

        ComponentState currentState = this.getTabState(tabIndex, true);
        StateTransitionTracker tabTracker = stateTransitionMultiTracker.getTracker(tabIndex);
        if (currentState.isDisabled()) {
            // No support yet for transitions between disabled and enabled / active
            // states
            Icon disabledIcon = RadianceCoreUtilities.getFilteredIcon(this.tabPane, tabIndex,
                    icon, currentState, this.tabTextColorMap.get(tabIndex));
            disabledIcon.paintIcon(this.tabPane, g2d, 0, 0);
        } else {
            // Active states are painted on top of the icon that corresponds to the
            // enabled state
            Icon enabledIcon = RadianceCoreUtilities.getFilteredIcon(this.tabPane, tabIndex,
                    icon, ComponentState.ENABLED, this.tabTextColorMap.get(tabIndex));
            enabledIcon.paintIcon(this.tabPane, g2d, 0, 0);
            if ((tabTracker != null) && (tabTracker.getActiveStrength() > 0.0f)) {
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> entry :
                        tabTracker.getModelStateInfo().getStateContributionMap().entrySet()) {
                    if (entry.getKey() == ComponentState.ENABLED) {
                        continue;
                    }
                    float contribution = entry.getValue().getContribution();
                    if (contribution > 0.0f) {
                        Icon activeIcon = RadianceCoreUtilities.getFilteredIcon(this.tabPane, tabIndex,
                                icon, entry.getKey(), this.tabTextColorMap.get(tabIndex));
                        if (activeIcon != enabledIcon) {
                            g2d.setComposite(WidgetUtilities.getAlphaComposite(this.tabPane,
                                    contribution, g));
                            activeIcon.paintIcon(this.tabPane, g2d, 0, 0);
                        }
                    }
                }
            }
        }
        g2d.dispose();
    }

    @Override
    protected MouseListener createMouseListener() {
        return null;
    }

    /**
     * Extension point to allow horizontal orientation of left / right placed tabs.
     * 
     * @param tabPlacement
     *            Tab placement.
     * @return Indication whether the tabs in the specified placement should be rotated.
     */
    protected boolean toRotateTabsOnPlacement(int tabPlacement) {
        return (tabPlacement == SwingConstants.LEFT) || (tabPlacement == SwingConstants.RIGHT);
    }

    private StateTransitionTracker getTracker(final int tabIndex, boolean initialRollover,
            boolean initialSelected) {
        StateTransitionTracker tracker = stateTransitionMultiTracker.getTracker(tabIndex);
        if (tracker == null) {
            ButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(tabPane, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(() -> new TabRepaintCallback(tabPane, tabIndex));
            stateTransitionMultiTracker.addTracker(tabIndex, tracker);
        }
        return tracker;
    }

    private void trackTabModification(int tabIndex, Component tabComponent) {
        Timeline modifiedTimeline =
                AnimationConfigurationManager.getInstance().modifiedTimelineBuilder(this.tabPane)
                        .addCallback(new TabRepaintCallback(tabPane, tabIndex))
                        .build();
        modifiedTimeline.playLoop(RepeatBehavior.REVERSE);
        modifiedTimelines.put(tabComponent, modifiedTimeline);
    }

    @Override
    public void update(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        RadianceCommonCortex.installDesktopHints(g2d, c.getFont());
        super.update(g2d, c);
        g2d.dispose();
    }
}
