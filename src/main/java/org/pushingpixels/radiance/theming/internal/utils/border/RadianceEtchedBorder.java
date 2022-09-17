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
package org.pushingpixels.radiance.theming.internal.utils.border;

import org.pushingpixels.radiance.common.api.RadianceCommonCortex;
import org.pushingpixels.radiance.theming.api.ComponentState;
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices;
import org.pushingpixels.radiance.theming.api.colorscheme.RadianceColorScheme;
import org.pushingpixels.radiance.theming.internal.utils.RadianceColorSchemeUtilities;
import org.pushingpixels.radiance.theming.internal.utils.RadianceSizeUtils;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Custom implementation of etched border.
 * 
 * @author Kirill Grouchnikov
 */
public class RadianceEtchedBorder implements Border {
	/**
	 * Returns the highlight color for the specified component.
	 * 
	 * @param c
	 *            Component.
	 * @return Matching highlight color.
	 */
	public Color getHighlightColor(Component c) {
		RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.getColorScheme(
				c, RadianceThemingSlices.ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
		return colorScheme.getSeparatorPrimaryColor();
	}

	/**
	 * Returns the shadow color for the specified component.
	 * 
	 * @param c
	 *            Component.
	 * @return Matching shadow color.
	 */
	public Color getShadowColor(Component c) {
		RadianceColorScheme colorScheme = RadianceColorSchemeUtilities.getColorScheme(
				c, RadianceThemingSlices.ColorSchemeAssociationKind.SEPARATOR, ComponentState.ENABLED);
		return colorScheme.getSeparatorSecondaryColor();
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		int w = width;
		int h = height;

		Graphics2D g2d = (Graphics2D) g.create();
		float strokeWidth = RadianceSizeUtils.getBorderStrokeWidth(c);
		g2d.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_ROUND));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);

		g2d.translate(x, y);

		g2d.setColor(getShadowColor(c));

		// this is to prevent clipping of thick outer borders.
		float delta = strokeWidth / 2.0f;

		g2d.draw(new Rectangle2D.Float(delta, delta, w - delta - 2
				* strokeWidth, h - delta - 2 * strokeWidth));
		// g2d.drawRect(0, 0, w - 2, h - 2);

		g2d.setColor(getHighlightColor(c));
		g2d.draw(new Line2D.Float(strokeWidth, h - 3 * strokeWidth,
				strokeWidth, strokeWidth));
		// g2d.drawLine(1, h - 3, 1, 1);
		g2d.draw(new Line2D.Float(delta + strokeWidth, delta + strokeWidth, w
				- delta - 3 * strokeWidth, delta + strokeWidth));
		// g2d.drawLine(1, 1, w - 3, 1);

		g2d.draw(new Line2D.Float(delta, h - delta - strokeWidth, w - delta
				- strokeWidth, h - delta - strokeWidth));
		// g2d.drawLine(0, h - 1, w - 1, h - 1);
		g2d.draw(new Line2D.Float(w - delta - strokeWidth, h - delta
				- strokeWidth, w - delta - strokeWidth, delta));
		// g2d.drawLine(w - 1, h - 1, w - 1, 0);

		g2d.dispose();

		// this is a fix for defect 248 - in order to paint the TitledBorder
		// text respecting the AA settings of the display, we have to
		// set rendering hints on the passed Graphics object.
        RadianceCommonCortex.installDesktopHints((Graphics2D) g, c.getFont());
	}

	@Override
	public Insets getBorderInsets(Component c) {
		float borderStrokeWidth = RadianceSizeUtils.getBorderStrokeWidth(c);
		int prefSize = (int) (Math.ceil(2.0 * borderStrokeWidth));
		return new Insets(prefSize, prefSize, prefSize, prefSize);
	}
}
