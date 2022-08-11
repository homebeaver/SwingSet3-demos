package swingset.plaf;
/*
 *
 * Copyright (c) 2007, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.awt.Color;

import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * This class describes a higher-contrast Metal Theme.
 *
 * @author Michael C. Albers
 */

public class ContrastTheme extends DefaultMetalTheme {

    /**
     * Returns the name of this theme. This returns {@code "Contrast"}.
     */
	@Override
	public String getName() { return "Contrast"; }

    private final ColorUIResource primary1 = new ColorUIResource(Color.BLACK); 
    private final ColorUIResource primary2 = new ColorUIResource(204, 204, 204); // neon silver
    private final ColorUIResource primary3 = new ColorUIResource(Color.WHITE);
    private final ColorUIResource primaryHighlight = new ColorUIResource(102,102,102); // cursed gray

    private final ColorUIResource secondary2 = new ColorUIResource(204, 204, 204);
    private final ColorUIResource secondary3 = new ColorUIResource(Color.WHITE);
    private final ColorUIResource controlHighlight = new ColorUIResource(102,102,102);

    // replaces DARK_BLUE_GRAY 0x666699 102,102,153 with BLACK
    protected ColorUIResource getPrimary1() { return primary1; }
    // replaces BLUE_BELL      0x9999CC 153,153,204 with neon silver
    protected ColorUIResource getPrimary2() { return primary2; }
    // replaces LEVANDER_BLUE  0xCCCCFF 204,204,255 with WHITE
    protected ColorUIResource getPrimary3() { return primary3; }
    
    /**
     * {@inheritDoc}
     * <p>
     * Replaces key="??controlHighlight" WHITE with cursed gray
     */
	@Override
    public ColorUIResource getPrimaryControlHighlight() { return primaryHighlight;}

	// no replacement for getSecondary1 CURSED_GRAY 0x666666 102,102,102
    // replaces NOBEL          0x999999 153,153,153 with neon silver
    protected ColorUIResource getSecondary2() { return secondary2; }
    // replaces NEON_SILVER    0xCCCCCC 204,204,204 with WHITE
    protected ColorUIResource getSecondary3() { return secondary3; }
    
    /**
     * {@inheritDoc}
     * <p>
     * Replaces key="controlHighlight" WHITE with {@code super.getSecondary3()}, neon silver 204,204,204
     */
	@Override
    public ColorUIResource getControlHighlight() { return super.getSecondary3(); }

	// replaces focus color key="Button.focus" und andere, super.getPrimary2(), BLUE_BELL 0x9999CC 153,153,204
    public ColorUIResource getFocusColor() { return getBlack(); }
    // replaces text highlight color
    public ColorUIResource getTextHighlightColor() { return getBlack(); }
    // replaces highlighted text color
    public ColorUIResource getHighlightedTextColor() { return getWhite(); }
    // replaces menu selected background color
    public ColorUIResource getMenuSelectedBackground() { return getBlack(); }
    public ColorUIResource getMenuSelectedForeground() { return getWhite(); }
    public ColorUIResource getAcceleratorForeground() { return getBlack(); }
    public ColorUIResource getAcceleratorSelectedForeground() { return getWhite(); }


    public void addCustomEntriesToTable(UIDefaults table) {

        Border blackLineBorder = new BorderUIResource(new LineBorder( getBlack() ));
        table.put( "ToolTip.border", blackLineBorder);
        table.put( "TitledBorder.border", blackLineBorder);

        Object textBorder = new BorderUIResource( 
        	new CompoundBorder(blackLineBorder, new BasicBorders.MarginBorder()) );
        table.put( "TextField.border", textBorder);
        table.put( "PasswordField.border", textBorder);
        table.put( "TextArea.border", textBorder);
        table.put( "TextPane.border", textBorder);
        table.put( "EditorPane.border", textBorder);

    }

}
