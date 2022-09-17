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
package org.pushingpixels.radiance.component.internal.theming.common;

import org.pushingpixels.radiance.component.api.common.JCommandButton;
import org.pushingpixels.radiance.common.api.RadianceCommonCortex;
import org.pushingpixels.radiance.common.api.icon.RadianceIcon;
import org.pushingpixels.radiance.theming.api.ComponentState;
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices.ColorSchemeAssociationKind;
import org.pushingpixels.radiance.theming.api.colorscheme.RadianceColorScheme;
import org.pushingpixels.radiance.theming.internal.animation.StateTransitionTracker;
import org.pushingpixels.radiance.theming.internal.utils.*;
import org.pushingpixels.radiance.theming.internal.utils.icon.TransitionAware;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Icon with transition-aware capabilities. Has a delegate that does the actual
 * painting based on the transition themes.
 *
 * @author Kirill Grouchnikov
 */
@TransitionAware
public class TransitionAwareRadianceIcon implements RadianceIcon {
    /**
     * The width of the rendered image.
     */
    protected int width;

    /**
     * The height of the rendered image.
     */
    protected int height;

    /**
     * The delegate needs to implement the method in this interface based on the
     * provided theme. The theme is computed based on the transitions that are
     * happening on the associated control.
     *
     * @author Kirill Grouchnikov
     */
    @FunctionalInterface
    public interface Delegate {
        /**
         * Returns the icon that matches the specified theme.
         *
         * @param scheme Color scheme.
         * @param width  Icon width.
         * @param height Icon height.
         * @return Icon that matches the specified theme.
         */
        RadianceIcon getColorSchemeIcon(RadianceColorScheme scheme, int width, int height);
    }

    /**
     * The associated component.
     */
    private JComponent comp;

    private StateTransitionTrackerDelegate stateTransitionTrackerDelegate;

    /**
     * Delegate to compute the actual icons.
     */
    private Delegate delegate;

    /**
     * Icon cache to speed up the subsequent icon painting. The basic assumption
     * is that the {@link #delegate} returns an icon that paints the same for
     * the same parameters.
     */
    private LazyResettableHashMap<RadianceIcon> iconMap;

    public interface StateTransitionTrackerDelegate {
        StateTransitionTracker getStateTransitionTracker();
    }

    /**
     * Creates a new transition-aware icon.
     *
     * @param button                         Associated command button.
     * @param stateTransitionTrackerDelegate State transition tracker delegate
     * @param delegate                       Delegate to compute the actual icons.
     * @param initialDim                     Initial icon dimension.
     */
    public TransitionAwareRadianceIcon(JCommandButton button,
            StateTransitionTrackerDelegate stateTransitionTrackerDelegate,
            Delegate delegate, Dimension initialDim) {
        this.comp = button;
        this.stateTransitionTrackerDelegate = stateTransitionTrackerDelegate;
        this.delegate = delegate;
        this.iconMap = new LazyResettableHashMap<>("TransitionAwareRadianceIcon");
        this.width = initialDim.width;
        this.height = initialDim.height;
    }

    /**
     * Returns the current icon to paint.
     *
     * @return Icon to paint.
     */
    private RadianceIcon getIconToPaint() {
        double scale = RadianceCommonCortex.getScaleFactor(this.comp);
        StateTransitionTracker stateTransitionTracker = this.stateTransitionTrackerDelegate
                .getStateTransitionTracker();

        StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker
                .getModelStateInfo();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates =
                modelStateInfo.getStateContributionMap();

        ComponentState currState = modelStateInfo.getCurrModelState();

        RadianceColorScheme baseScheme = RadianceColorSchemeUtilities.getColorScheme(
                this.comp, ColorSchemeAssociationKind.MARK, currState);
        float baseAlpha = RadianceColorSchemeUtilities.getAlpha(this.comp, currState);

        ImageHashMapKey keyBase = RadianceCoreUtilities.getScaleAwareHashKey(
                scale, baseScheme.getDisplayName(), baseAlpha, this.width, this.height);
        // System.out.println(key);
        RadianceIcon layerBase = this.iconMap.get(keyBase);
        if (layerBase == null) {
            RadianceIcon baseFullOpacity = this.delegate.getColorSchemeIcon(baseScheme, width, height);
            if (baseAlpha == 1.0f) {
                layerBase = baseFullOpacity;
                iconMap.put(keyBase, layerBase);
            } else {
                BufferedImage baseImage = RadianceCoreUtilities.getBlankImage(scale,
                        baseFullOpacity.getIconWidth(), baseFullOpacity.getIconHeight());
                Graphics2D g2base = baseImage.createGraphics();
                g2base.setComposite(AlphaComposite.SrcOver.derive(baseAlpha));
                baseFullOpacity.paintIcon(this.comp, g2base, 0, 0);
                g2base.dispose();
                layerBase = new ScaleAwareImageWrapperIcon(baseImage, scale);
                iconMap.put(keyBase, layerBase);
            }
        }
        if (currState.isDisabled() || (activeStates.size() == 1)) {
            return layerBase;
        }

        BufferedImage result = RadianceCoreUtilities.getBlankImage(scale,
                layerBase.getIconWidth(), layerBase.getIconHeight());
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // draw the base layer
        layerBase.paintIcon(this.comp, g2d, 0, 0);

        // draw the other active layers
        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry
                : activeStates.entrySet()) {
            ComponentState activeState = activeEntry.getKey();
            // System.out.println("Painting state " + activeState + "[curr is "
            // + currState + "] with " + activeEntry.getValue());
            if (activeState == currState)
                continue;

            float stateContribution = activeEntry.getValue().getContribution();
            if (stateContribution > 0.0f) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(stateContribution));

                RadianceColorScheme scheme = RadianceColorSchemeUtilities.getColorScheme(this.comp,
                        ColorSchemeAssociationKind.MARK, activeState);
                float alpha = RadianceColorSchemeUtilities.getAlpha(this.comp, activeState);

                ImageHashMapKey key = RadianceCoreUtilities.getScaleAwareHashKey(scale,
                        scheme.getDisplayName(), alpha, this.width, this.height);
                RadianceIcon layer = iconMap.get(key);
                if (layer == null) {
                    RadianceIcon fullOpacity = this.delegate.getColorSchemeIcon(scheme, width, height);
                    if (alpha == 1.0f) {
                        layer = fullOpacity;
                        iconMap.put(key, layer);
                    } else {
                        BufferedImage image = RadianceCoreUtilities.getBlankImage(scale,
                                fullOpacity.getIconWidth(), fullOpacity.getIconHeight());
                        Graphics2D g2layer = image.createGraphics();
                        g2layer.setComposite(AlphaComposite.SrcOver.derive(alpha));
                        fullOpacity.paintIcon(this.comp, g2layer, 0, 0);
                        g2layer.dispose();
                        layer = new ScaleAwareImageWrapperIcon(image, scale);
                        iconMap.put(key, layer);
                    }
                }
                layer.paintIcon(this.comp, g2d, 0, 0);
            }
        }
        g2d.dispose();
        return new ScaleAwareImageWrapperIcon(result, scale);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.getIconToPaint().paintIcon(c, g, x, y);
    }

    @Override
    public void setDimension(Dimension newDimension) {
        this.width = newDimension.width;
        this.height = newDimension.height;
    }

    @Override
    public boolean supportsColorFilter() {
        return false;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }
}
