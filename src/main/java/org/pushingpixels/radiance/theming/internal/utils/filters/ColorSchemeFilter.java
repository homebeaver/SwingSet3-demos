/*
 * $Id: ColorSchemeFilter.java 2353 2009-12-11 04:57:29Z kirillcool $
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.pushingpixels.radiance.theming.internal.utils.filters;

import org.pushingpixels.radiance.common.api.filter.RadianceAbstractFilter;
import org.pushingpixels.radiance.theming.api.colorscheme.RadianceColorScheme;
import org.pushingpixels.radiance.theming.internal.utils.HashMapKey;
import org.pushingpixels.radiance.theming.internal.utils.LazyResettableHashMap;
import org.pushingpixels.radiance.theming.internal.utils.RadianceColorUtilities;
import org.pushingpixels.radiance.theming.internal.utils.RadianceCoreUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

/*
 * @author Romain Guy <romain.guy@mac.com>
 * @author Kirill Grouchnikov
 */

public class ColorSchemeFilter extends RadianceAbstractFilter {
    private int[] interpolated;

    private static final int MAPSTEPS = 512;

    private final static LazyResettableHashMap<ColorSchemeFilter> filters =
            new LazyResettableHashMap<>("ColorSchemeFilter");

    private float originalBrightnessFactor;
    private float alpha;

    public static ColorSchemeFilter getColorSchemeFilter(RadianceColorScheme scheme,
            float originalBrightnessFactor, float alpha) {
        HashMapKey key = RadianceCoreUtilities.getHashKey(scheme.getDisplayName(),
                originalBrightnessFactor, alpha);
        ColorSchemeFilter filter = filters.get(key);
        if (filter == null) {
            filter = new ColorSchemeFilter(scheme, originalBrightnessFactor, alpha);
            filters.put(key, filter);
        }
        return filter;
    }

    public static int[] getInterpolatedColors(RadianceColorScheme scheme) {
        // collect the brightness factors of the color scheme
        Map<Integer, Color> schemeColorMapping = new TreeMap<>();
        int ultraLight = scheme.getUltraLightColor().getRGB();
        int extraLight = scheme.getExtraLightColor().getRGB();
        int light = scheme.getLightColor().getRGB();
        int mid = scheme.getMidColor().getRGB();
        int dark = scheme.getDarkColor().getRGB();
        int ultraDark = scheme.getUltraDarkColor().getRGB();
        // Are the colors identical?
        if ((ultraLight == extraLight) && (ultraLight == light) && (ultraLight == mid) &&
                (ultraLight == dark) && (ultraLight == ultraDark)) {
            Color lighter = RadianceColorUtilities.deriveByBrightness(scheme.getLightColor(), 0.2f);
            Color darker = RadianceColorUtilities.deriveByBrightness(scheme.getLightColor(), -0.2f);
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(lighter.getRGB()),
                    lighter);
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(light),
                    scheme.getLightColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(darker.getRGB()),
                    darker);
        } else {
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(ultraLight),
                    scheme.getUltraLightColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(extraLight),
                    scheme.getExtraLightColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(light),
                    scheme.getLightColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(mid),
                    scheme.getMidColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(dark),
                    scheme.getDarkColor());
            schemeColorMapping.put(
                    RadianceColorUtilities.getColorBrightness(ultraDark),
                    scheme.getUltraDarkColor());
        }

        List<Integer> schemeBrightness = new ArrayList<>(schemeColorMapping.keySet());
        Collections.sort(schemeBrightness);

        int lowestSchemeBrightness = schemeBrightness.get(0);
        int highestSchemeBrightness = schemeBrightness.get(schemeBrightness.size() - 1);
        boolean hasSameBrightness = (highestSchemeBrightness == lowestSchemeBrightness);

        Map<Integer, Color> stretchedColorMapping = new TreeMap<>();
        for (Map.Entry<Integer, Color> entry : schemeColorMapping.entrySet()) {
            int brightness = entry.getKey();
            int stretched = hasSameBrightness ? brightness
                    : 255 - 255 * (highestSchemeBrightness - brightness)
                    / (highestSchemeBrightness - lowestSchemeBrightness);
            stretchedColorMapping.put(stretched, entry.getValue());
        }
        schemeBrightness = new ArrayList<>(stretchedColorMapping.keySet());
        Collections.sort(schemeBrightness);

        int[] interpolated = new int[MAPSTEPS];
        for (int i = 0; i < MAPSTEPS; i++) {
            int brightness = (int) (256.0 * i / MAPSTEPS);
            if (schemeBrightness.contains(brightness)) {
                interpolated[i] = stretchedColorMapping.get(brightness).getRGB();
            } else {
                if (hasSameBrightness) {
                    interpolated[i] = stretchedColorMapping.get(lowestSchemeBrightness)
                            .getRGB();
                } else {
                    int currIndex = 0;
                    while (true) {
                        int currStopValue = schemeBrightness.get(currIndex);
                        int nextStopValue = schemeBrightness.get(currIndex + 1);
                        if ((brightness > currStopValue) && (brightness < nextStopValue)) {
                            // interpolate
                            Color currStopColor = stretchedColorMapping.get(currStopValue);
                            Color nextStopColor = stretchedColorMapping.get(nextStopValue);
                            interpolated[i] = RadianceColorUtilities.getInterpolatedRGB(
                                    currStopColor, nextStopColor,
                                    1.0 - (double) (brightness - currStopValue)
                                            / (double) (nextStopValue - currStopValue));
                            break;
                        }
                        currIndex++;
                    }
                }
            }
        }
        return interpolated;
    }

    /**
     * @throws IllegalArgumentException if <code>scheme</code> is null
     */
    private ColorSchemeFilter(RadianceColorScheme scheme, float originalBrightnessFactor,
            float alpha) {
        if (scheme == null) {
            throw new IllegalArgumentException("Color scheme cannot be null");
        }

        this.originalBrightnessFactor = originalBrightnessFactor;
        this.alpha = alpha;
        this.interpolated = getInterpolatedColors(scheme);
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        int width = src.getWidth();
        int height = src.getHeight();

        int[] pixels = new int[width * height];
        getPixels(src, 0, 0, width, height, pixels);
        mixColor(pixels);
        setPixels(dst, 0, 0, width, height, pixels);

        return dst;
    }

    private void mixColor(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];

            int brightness = RadianceColorUtilities.getColorBrightness(argb);

            int a = (argb >>> 24) & 0xFF;
            int r = (argb >>> 16) & 0xFF;
            int g = (argb >>> 8) & 0xFF;
            int b = (argb >>> 0) & 0xFF;

            float[] hsb = Color.RGBtoHSB(r, g, b, null);
            int pixelColor = interpolated[brightness * MAPSTEPS / 256];

            int ri = (pixelColor >>> 16) & 0xFF;
            int gi = (pixelColor >>> 8) & 0xFF;
            int bi = (pixelColor >>> 0) & 0xFF;
            float[] hsbi = Color.RGBtoHSB(ri, gi, bi, null);

            hsb[0] = hsbi[0];
            hsb[1] = hsbi[1];
            if (this.originalBrightnessFactor >= 0.0f) {
                hsb[2] = this.originalBrightnessFactor * hsb[2]
                        + (1.0f - this.originalBrightnessFactor) * hsbi[2];
            } else {
                hsb[2] = hsb[2] * hsbi[2] * (1.0f + this.originalBrightnessFactor);
            }

            int result = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
            int finalAlpha = (int) (a * this.alpha);

            pixels[i] = (finalAlpha << 24) | ((result >> 16) & 0xFF) << 16
                    | ((result >> 8) & 0xFF) << 8 | (result & 0xFF);
        }
    }
}
