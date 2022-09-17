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
import org.pushingpixels.radiance.theming.internal.utils.RadianceCoreUtilities;
import org.pushingpixels.radiance.theming.internal.utils.RadianceImageCreator;
import org.pushingpixels.radiance.theming.internal.utils.RadianceSizeUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Border for toolbar.
 * 
 * @author Kirill Grouchnikov
 */
public class RadianceToolBarBorder extends AbstractBorder implements UIResource {
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		// failsafe for LAF change
		if (!RadianceCoreUtilities.isCurrentLookAndFeel())
			return;

		Graphics2D graphics = (Graphics2D) g.create();
		graphics.translate(x, y);
		double scaleFactor = RadianceCommonCortex.getScaleFactor(c);
		graphics.scale(1.0f / scaleFactor, 1.0f / scaleFactor);

		if (((JToolBar) c).isFloatable()) {
			RadianceColorScheme scheme = RadianceColorSchemeUtilities
					.getColorScheme(c, RadianceThemingSlices.ColorSchemeAssociationKind.SEPARATOR,
							ComponentState.ENABLED);
			int dragBumpsWidth = (int) (0.75 * RadianceSizeUtils
					.getToolBarDragInset(RadianceSizeUtils
							.getComponentFontSize(c)));
			if (((JToolBar) c).getOrientation() == SwingConstants.HORIZONTAL) {
				// fix for defect 3 on NB module
				int height = c.getHeight() - 4;
				if (height > 0) {
					BufferedImage dragImage = RadianceImageCreator.getDragImage(
							c, scheme, dragBumpsWidth, height, 2);
					if (c.getComponentOrientation().isLeftToRight()) {
						graphics.drawImage(dragImage, 2, 1, null);
					} else {
						graphics.drawImage(dragImage, 
								c.getBounds().width - dragBumpsWidth - 2, 1, null);
					}
				}
			} else {
				// vertical
				// fix for defect 3 on NB module
				int width = c.getWidth() - 4;
				if (width > 0) {
					BufferedImage dragImage = RadianceImageCreator.getDragImage(
							c, scheme, width, dragBumpsWidth, 2);
					graphics.drawImage(dragImage, 2, 2, null);
				}
			}
		}
		graphics.dispose();
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return this.getBorderInsets(c, new Insets(0, 0, 0, 0));
	}

	@Override
	public Insets getBorderInsets(Component c, Insets newInsets) {
		Insets defaultInsets = RadianceSizeUtils
				.getToolBarInsets(RadianceSizeUtils.getComponentFontSize(c));
		newInsets.set(defaultInsets.top, defaultInsets.left,
				defaultInsets.bottom, defaultInsets.right);

		JToolBar toolbar = (JToolBar) c;
		if (toolbar.isFloatable()) {
			int dragInset = RadianceSizeUtils
					.getToolBarDragInset(RadianceSizeUtils
							.getComponentFontSize(c));
			if (toolbar.getOrientation() == SwingConstants.HORIZONTAL) {
				if (toolbar.getComponentOrientation().isLeftToRight()) {
					newInsets.left = dragInset;
				} else {
					newInsets.right = dragInset;
				}
			} else {// vertical
				newInsets.top = dragInset;
			}
		}

		Insets margin = toolbar.getMargin();

		if (margin != null) {
			newInsets.left += margin.left;
			newInsets.top += margin.top;
			newInsets.right += margin.right;
			newInsets.bottom += margin.bottom;
		}

		return newInsets;
	}
}
