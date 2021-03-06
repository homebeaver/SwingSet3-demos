/*
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
package swingset.plaf;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * This class describes a theme using "blue-green" colors.
 *
 * @author Steve Wilson
 */
public class AquaTheme extends DefaultMetalTheme {

    public String getName() { return "Aqua"; }

    // aka primary control dark shadow color
    private final ColorUIResource primary1 = new ColorUIResource(ColorUnit.CADET_BLUE);
    // aka primary control shadow color
    private final ColorUIResource primary2 = new ColorUIResource(ColorUnit.GLACIER);
    // aka primary control color
    private final ColorUIResource primary3 = new ColorUIResource(ColorUnit.BLIZZARD_BLUE);

    // control info, control highlight and the secondary colors remain unchanged / here the names for docu
    // control info : color/Black
    // control highlight : color/White
    
    // aka control dark shadow color
    private final ColorUIResource secondary1 = new ColorUIResource(ColorUnit.CURSED_GRAY);
    // aka control disabled color
    private final ColorUIResource secondary2 = new ColorUIResource(ColorUnit.NOBEL);
    // aka control color
    private final ColorUIResource secondary3 = new ColorUIResource(ColorUnit.NEON_SILVER);

    /**
     * Replaces the primary 1 color <a href="https://en.wikipedia.org/wiki/Blue-gray#Dark_blue-gray">Dark blue-gray</a> RGB (102 102 153) HEX #666699:
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(102,102,153); border:solid 1px #ccc;text-align:right;">
     * <p>
     * with <a href="https://www.htmlcsscolor.com/hex/669999">Cadet Blue</a>
     * or <a href="https://colornames.org/color/669999">Desaturated Cyan</a> RGB (102 153 153) HEX #669999:
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(102,153,153); border:solid 1px #ccc;text-align:right">
     */
    @Override
    protected ColorUIResource getPrimary1() { return primary1; }
    
    /**
     * Replaces the primary 2 color <a href="https://www.htmlcsscolor.com/hex/9999CC">Blue Bell in htmlcsscolor.com</a> RGB (153 153 204) HEX #9999CC:
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(153,153,204); border:solid 1px #ccc;text-align:right;">
     * <p>
     * with <a href="https://colornames.org/color/80C0C0">Misali Neptune</a> 
     * or <a href="https://www.htmlcsscolor.com/hex/80C0C0">Glacier</a> RGB (128 192 192) HEX #80C0C0:
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(128,192,192); border:solid 1px #ccc;text-align:right;">
     */
    @Override
    protected ColorUIResource getPrimary2() { return primary2; }
  
    /**
     * Replaces the primary 3 color <a href="https://en.wikipedia.org//wiki/Lavender_(color)#Lavender_blue">Lavender blue</a> RGB (204 204 255) HEX #CCCCFF :
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(204,204,255); border:solid 1px #ccc;text-align:right;">
     * <p>
     * with <a href="https://www.htmlcsscolor.com/hex/9FEBEB">Blizzard Blue</a> RGB (159 235 235) HEX #9FEBEB :
<p style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(159,235,235); border:solid 1px #ccc;text-align:right;">
     */
    @Override
    protected ColorUIResource getPrimary3() { return primary3; }

}
