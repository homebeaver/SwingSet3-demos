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
package org.pushingpixels.radiance.theming.internal.utils;

//import org.pushingpixels.radiance.animation.api.Timeline;
//import org.pushingpixels.radiance.animation.api.Timeline.TimelineState;
import org.pushingpixels.trident.api.Timeline;
import org.pushingpixels.trident.api.Timeline.TimelineState;

import org.pushingpixels.radiance.common.api.RadianceCommonCortex;
import org.pushingpixels.radiance.theming.api.ComponentState;
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices;
import org.pushingpixels.radiance.theming.api.colorscheme.RadianceColorScheme;
import org.pushingpixels.radiance.theming.api.painter.border.RadianceBorderPainter;
import org.pushingpixels.radiance.theming.api.painter.fill.RadianceFillPainter;
import org.pushingpixels.radiance.theming.api.shaper.PillButtonShaper;
import org.pushingpixels.radiance.theming.api.shaper.RadianceButtonShaper;
import org.pushingpixels.radiance.theming.api.shaper.RectangularButtonShaper;
import org.pushingpixels.radiance.theming.internal.RadianceSynapse;
import org.pushingpixels.radiance.theming.internal.animation.ModificationAwareUI;
import org.pushingpixels.radiance.theming.internal.animation.StateTransitionTracker;
import org.pushingpixels.radiance.theming.internal.animation.TransitionAwareUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

/**
 * Delegate class for painting backgrounds of buttons in <b>Radiance</b> look and feel. This class
 * is <b>for internal use only</b>.
 *
 * @author Kirill Grouchnikov
 */
public class ButtonBackgroundDelegate {
    /**
     * Cache for background images.
     */
    private static LazyResettableHashMap<BufferedImage> regularBackgrounds =
            new LazyResettableHashMap<>("ButtonBackgroundDelegate");

    /**
     * Retrieves the background for the specified button.
     *
     * @param button        Button.
     * @param shaper        Button shaper.
     * @param fillPainter   Button fill painter.
     * @param borderPainter Button border painter.
     * @param width         Button width.
     * @param height        Button height.
     * @return Button background.
     */
    private static BufferedImage getFullAlphaBackground(AbstractButton button,
            RadianceButtonShaper shaper, RadianceFillPainter fillPainter,
            RadianceBorderPainter borderPainter, int width, int height) {
        double scale = RadianceCommonCortex.getScaleFactor(button);

        TransitionAwareUI transitionAwareUI = (TransitionAwareUI) button.getUI();
        StateTransitionTracker.ModelStateInfo modelStateInfo = transitionAwareUI
                .getTransitionTracker().getModelStateInfo();

        ComponentState currState = modelStateInfo.getCurrModelState();

        // ComponentState prevState = stateTransitionModel.getPrevModelState();

        // System.out.println(button.getText() + ": " + prevState.name() +
        // " -> "
        // + state.name() + " at "
        // + stateTransitionModel.getTransitionPosition());

        // compute the straight sides
        Set<RadianceThemingSlices.Side> straightSides = RadianceCoreUtilities.getSides(button,
                RadianceSynapse.BUTTON_STRAIGHT_SIDE);

        boolean isRoundButton = PillButtonShaper.isRoundButton(button);
        float radius = 0.0f;
        if (shaper instanceof RectangularButtonShaper) {
            radius = ((RectangularButtonShaper) shaper).getCornerRadius(button, 0.0f);
        }

        Set<RadianceThemingSlices.Side> openSides = RadianceCoreUtilities.getSides(button,
                RadianceSynapse.BUTTON_OPEN_SIDE);
        // String openKey = "";
        // for (Side oSide : openSides) {
        // openKey += oSide.name() + "-";
        // }
        // String extraModelKey = "";
        // for (String modelKey : extraModelKeys) {
        // extraModelKey += (modelKey + "-");
        // }
        boolean isContentAreaFilled = button.isContentAreaFilled();
        boolean isBorderPainted = button.isBorderPainted();

        // compute color scheme
        RadianceColorScheme baseBorderScheme = RadianceColorSchemeUtilities.getColorScheme(button,
                RadianceThemingSlices.ColorSchemeAssociationKind.BORDER, currState);

        // see if need to use attention-drawing animation
        // boolean isWindowModified = false;
        if (button.getUI() instanceof ModificationAwareUI) {
            ModificationAwareUI modificationAwareUI = (ModificationAwareUI) button.getUI();
            Timeline modificationTimeline = modificationAwareUI.getModificationTimeline();
            if (modificationTimeline != null) {
                if (modificationTimeline.getState() != TimelineState.IDLE) {
                    // isWindowModified = true;
                    RadianceColorScheme colorScheme2 = RadianceColorSchemeUtilities.YELLOW;
                    RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.ORANGE;
                    float cyclePos = modificationTimeline.getTimelinePosition();

                    ImageHashMapKey key1 = RadianceCoreUtilities.getScaleAwareHashKey(
                            scale, width, height,
                            colorScheme.getDisplayName(), baseBorderScheme.getDisplayName(),
                            shaper.getDisplayName(), fillPainter.getDisplayName(),
                            borderPainter.getDisplayName(), straightSides, openSides,
                            button.getClass().getName(), isRoundButton, radius, isContentAreaFilled,
                            isBorderPainted, RadianceSizeUtils.getComponentFontSize(button));
                    BufferedImage layer1 = regularBackgrounds.get(key1);
                    if (layer1 == null) {
                        layer1 = createBackgroundImage(button, scale, shaper, fillPainter, borderPainter,
                                width, height, colorScheme, baseBorderScheme, openSides,
                                isContentAreaFilled, isBorderPainted);

                        regularBackgrounds.put(key1, layer1);
                    }
                    ImageHashMapKey key2 = RadianceCoreUtilities.getScaleAwareHashKey(
                            scale, width, height,
                            colorScheme2.getDisplayName(), baseBorderScheme.getDisplayName(),
                            shaper.getDisplayName(), fillPainter.getDisplayName(),
                            borderPainter.getDisplayName(), straightSides, openSides,
                            button.getClass().getName(), isRoundButton, radius, isContentAreaFilled,
                            isBorderPainted, RadianceSizeUtils.getComponentFontSize(button));
                    BufferedImage layer2 = regularBackgrounds.get(key2);
                    if (layer2 == null) {
                        layer2 = createBackgroundImage(button, scale, shaper, fillPainter, borderPainter,
                                width, height, colorScheme2, baseBorderScheme, openSides,
                                isContentAreaFilled, isBorderPainted);

                        regularBackgrounds.put(key2, layer2);
                    }

                    BufferedImage result = RadianceCoreUtilities.getBlankUnscaledImage(layer1);
                    Graphics2D g2d = result.createGraphics();
                    if (cyclePos < 1.0f)
                        g2d.drawImage(layer1, 0, 0, layer1.getWidth(), layer1.getHeight(), null);
                    if (cyclePos > 0.0f) {
                        g2d.setComposite(AlphaComposite.SrcOver.derive(cyclePos));
                        g2d.drawImage(layer2, 0, 0, layer2.getWidth(), layer2.getHeight(), null);
                    }
                    g2d.dispose();
                    return result;
                }
            }
        }

        // see if need to use transition animation. Important - don't do it
        // on pulsating buttons (such as default or close buttons
        // of modified frames).

        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo
                .getStateContributionMap();

        RadianceColorScheme baseFillScheme = RadianceColorSchemeUtilities.getColorScheme(button,
                currState);
        ImageHashMapKey keyBase = RadianceCoreUtilities.getScaleAwareHashKey(
                scale, width, height,
                baseFillScheme.getDisplayName(), baseBorderScheme.getDisplayName(),
                shaper.getDisplayName(), fillPainter.getDisplayName(),
                borderPainter.getDisplayName(), straightSides, openSides,
                button.getClass().getName(), isRoundButton, (int) (1000 * radius),
                isContentAreaFilled, isBorderPainted,
                RadianceSizeUtils.getComponentFontSize(button));
        BufferedImage layerBase = regularBackgrounds.get(keyBase);
        if (layerBase == null) {
            layerBase = createBackgroundImage(button, scale, shaper, fillPainter, borderPainter, width,
                    height, baseFillScheme, baseBorderScheme, openSides, isContentAreaFilled,
                    isBorderPainted);
            regularBackgrounds.put(keyBase, layerBase);
        }
        if (currState.isDisabled() || (activeStates.size() == 1)) {
            return layerBase;
        }

        BufferedImage result = RadianceCoreUtilities.getBlankUnscaledImage(layerBase);
        Graphics2D g2d = result.createGraphics();
        // draw the base layer
        g2d.drawImage(layerBase, 0, 0, layerBase.getWidth(), layerBase.getHeight(), null);
        // System.out.println("\nPainting base state " + currState);

        // draw the other active layers
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates
                .entrySet()) {
            ComponentState activeState = activeEntry.getKey();
            // System.out.println("Painting state " + activeState + "[curr is "
            // + currState + "] with " + activeEntry.getValue());
            if (activeState == currState) {
                continue;
            }

            float stateContribution = activeEntry.getValue().getContribution();
            if (stateContribution > 0.0f) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));

                RadianceColorScheme fillScheme = RadianceColorSchemeUtilities
                        .getColorScheme(button, activeState);
                RadianceColorScheme borderScheme = RadianceColorSchemeUtilities
                        .getColorScheme(button, RadianceThemingSlices.ColorSchemeAssociationKind.BORDER, activeState);
                ImageHashMapKey key = RadianceCoreUtilities.getScaleAwareHashKey(
                        scale, width, height,
                        fillScheme.getDisplayName(), borderScheme.getDisplayName(),
                        shaper.getDisplayName(), fillPainter.getDisplayName(),
                        borderPainter.getDisplayName(), straightSides, openSides,
                        button.getClass().getName(), isRoundButton, (int) (1000 * radius),
                        isContentAreaFilled, isBorderPainted,
                        RadianceSizeUtils.getComponentFontSize(button));
                BufferedImage layer = regularBackgrounds.get(key);
                if (layer == null) {
                    layer = createBackgroundImage(button, scale, shaper, fillPainter, borderPainter, width,
                            height, fillScheme, borderScheme, openSides, isContentAreaFilled,
                            isBorderPainted);
                    regularBackgrounds.put(key, layer);
                }
                g2d.drawImage(layer, 0, 0, layer.getWidth(), layer.getHeight(), null);
            }
        }
        g2d.dispose();
        return result;
    }

    private static BufferedImage createBackgroundImage(AbstractButton button,
                                                       double scale, RadianceButtonShaper shaper, RadianceFillPainter fillPainter,
                                                       RadianceBorderPainter borderPainter, int width, int height,
                                                       RadianceColorScheme colorScheme, RadianceColorScheme borderScheme,
                                                       Set<RadianceThemingSlices.Side> openSides, boolean isContentAreaFilled, boolean isBorderPainted) {
        int openDelta = (int) (Math.ceil(3.0 * RadianceSizeUtils.getBorderStrokeWidth(button)));
        openDelta *= scale;
        int deltaLeft = ((openSides != null) && openSides.contains(RadianceThemingSlices.Side.LEFT)) ? openDelta : 0;
        int deltaRight = ((openSides != null) && openSides.contains(RadianceThemingSlices.Side.RIGHT)) ? openDelta : 0;
        int deltaTop = ((openSides != null) && openSides.contains(RadianceThemingSlices.Side.TOP)) ? openDelta : 0;
        int deltaBottom = ((openSides != null) && openSides.contains(RadianceThemingSlices.Side.BOTTOM)) ? openDelta : 0;

        // System.err.println(key);
        float borderDelta = RadianceSizeUtils.getBorderStrokeWidth(button) / 2.0f;
        Shape contour = shaper.getButtonOutline(button, borderDelta, width + deltaLeft + deltaRight,
                height + deltaTop + deltaBottom, false);

        BufferedImage newBackground = RadianceCoreUtilities.getBlankImage(scale, width, height);
        Graphics2D finalGraphics = (Graphics2D) newBackground.getGraphics();
        finalGraphics.translate(-deltaLeft, -deltaTop);
        if (isContentAreaFilled) {
            fillPainter.paintContourBackground(finalGraphics, button,
                    width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, contour, false,
                    colorScheme, true);
        }

        if (isBorderPainted) {
            float borderThickness = RadianceSizeUtils.getBorderStrokeWidth(button);
            Shape contourInner = borderPainter.isPaintingInnerContour()
                    ? shaper.getButtonOutline(button, borderDelta + borderThickness,
                    width + deltaLeft + deltaRight, height + deltaTop + deltaBottom, true)
                    : null;
            borderPainter.paintBorder(finalGraphics, button, width + deltaLeft + deltaRight,
                    height + deltaTop + deltaBottom, contour, contourInner, borderScheme);
        }
        return newBackground;
    }

    /**
     * Updates background of the specified button.
     *
     * @param g      Graphic context.
     * @param button Button to update.
     */
    public void updateBackground(Graphics g, AbstractButton button) {
        // failsafe for LAF change
        if (!RadianceCoreUtilities.isCurrentLookAndFeel()) {
            return;
        }

        if (RadianceCoreUtilities.isButtonNeverPainted(button)) {
            return;
        }

        int width = button.getWidth();
        int height = button.getHeight();
        int y = 0;
        if (RadianceCoreUtilities.isScrollButton(button)
                || RadianceCoreUtilities.isSpinnerButton(button)) {
            PairwiseButtonBackgroundDelegate.updatePairwiseBackground(g, button, width, height,
                    false);
            return;
        }

        RadianceFillPainter fillPainter = RadianceCoreUtilities.getFillPainter(button);
        RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(button);
        RadianceBorderPainter borderPainter = RadianceCoreUtilities.getBorderPainter(button);

        BufferedImage bgImage = getFullAlphaBackground(button, shaper,
                fillPainter, borderPainter, width, height);

        TransitionAwareUI transitionAwareUI = (TransitionAwareUI) button.getUI();
        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker
                .getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo
                .getStateContributionMap();

        // Two special cases here:
        // 1. Button has flat appearance.
        // 2. Button is disabled.
        // For both cases, we need to set custom translucency.
        boolean isFlat = RadianceCoreUtilities.hasFlatAppearance(button);
        boolean isSpecial = isFlat || !button.isEnabled();
        float extraAlpha = 1.0f;
        if (isSpecial) {
            if (isFlat) {
                // Special handling of flat buttons
                extraAlpha = 0.0f;
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates
                        .entrySet()) {
                    ComponentState activeState = activeEntry.getKey();
                    if (activeState.isDisabled())
                        continue;
                    if (activeState == ComponentState.ENABLED)
                        continue;
                    extraAlpha += activeEntry.getValue().getContribution();
                }
            } else {
                if (!button.isEnabled()) {
                    extraAlpha = RadianceColorSchemeUtilities.getAlpha(button,
                            modelStateInfo.getCurrModelState());
                }
            }
        }
        if (extraAlpha > 0.0f) {
            Graphics2D graphics = (Graphics2D) g.create();
            graphics.setComposite(WidgetUtilities.getAlphaComposite(button, extraAlpha, g));
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            RadianceCommonCortex.drawImageWithScale(graphics, RadianceCommonCortex.getScaleFactor(button),
                    bgImage, 0, y);
            graphics.dispose();
        }
    }

    /**
     * Returns <code>true</code> if the specified <i>x,y </i> location is contained within the look
     * and feel's defined shape of the specified component. <code>x</code> and <code>y</code> are
     * defined to be relative to the coordinate system of the specified component.
     *
     * @param button the component where the <i>x,y </i> location is being queried;
     * @param x      the <i>x </i> coordinate of the point
     * @param y      the <i>y </i> coordinate of the point
     * @return <code>true</code> if the specified <i>x,y </i> location is contained within the look
     * and feel's defined shape of the specified component, <code>false</code> otherwise.
     */
    public static boolean contains(AbstractButton button, int x, int y) {
        // failsafe for LAF change
        if (!RadianceCoreUtilities.isCurrentLookAndFeel()) {
            return false;
        }
        RadianceButtonShaper shaper = RadianceCoreUtilities.getButtonShaper(button);
        if (shaper == null) {
            return false;
        }
        Shape contour = shaper.getButtonOutline(button, 0.0f, button.getWidth(), button.getHeight(),
                false);
        return contour.contains(x, y);
    }
}
