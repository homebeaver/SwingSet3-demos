/*
 * the origin/inception swingset2 was without a package : all classes and resources in one dir
 * 
 * (de) Es gibt über 16Mio Farben 256*256*256. Nur wenige sind einheitlich benannt :
 * - https://en.wikipedia.org/wiki/Template:Color_chart_X11
 * - https://www.w3.org/TR/css-color-3/#html4 und https://www.w3.org/TR/css-color-3/#svg-color
 * Es gibt zwischen X11 und W3C Unstimmigkleiten bei der Benennung
 * siehe https://en.wikipedia.org/wiki/X11_color_names
 * - X11 vs W3C : Color names with clashing definitions: 
 * - X11 vs W3C : Colors with multiple names: awt.Color wie X11
 *
 * wikipedia und zwei weitere Seiten listen weiter Namen:
 * - https://en.wikipedia.org/wiki/List_of_colors:_A%E2%80%93F
 * - https://en.wikipedia.org/wiki/List_of_colors:_G%E2%80%93M
 * - https://en.wikipedia.org/wiki/List_of_colors:_N%E2%80%93Z
 * - https://colornames.org/color/CCCCCC
 * - https://www.htmlcsscolor.com/hex/CCCCCC
 *  
 */
/**
 * <code>swingset.plaf</code> contains extensions of DefaultMetalTheme. <br>
 * <code>DefaultMetalTheme</code> has 6 color definitions + white + black
<p>
primary 1 color <a href="https://en.wikipedia.org/wiki/Blue-gray#Dark_blue-gray" title="Blue-gray">Dark blue-gray</a>
, <a href="https://colornames.org/color/666699">colornames.org</a>
, <a href="https://www.htmlcsscolor.com/hex/666699">Scampi in htmlcsscolor.com</a>:
</p>
<p title="𝗥𝗚𝗕 (102 102 153)&#10;𝗛𝗘𝗫 #666699" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(102,102,153); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p>
primary 2 color <a href="https://colornames.org/color/9999CC">MarioKart Cubic Centimeters Hack</a>
, <a href="https://www.htmlcsscolor.com/hex/9999CC">Blue Bell in htmlcsscolor.com</a>:
</p>
<p title="𝗥𝗚𝗕 (153 153 204)&#10;𝗛𝗘𝗫 #9999CC" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(153,153,204); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p>
primary 3 color <a href="https://en.wikipedia.org/wiki/Lavender_(color)#Lavender_blue" title="Lavender (color)">Lavender blue</a>
, <a href="https://colornames.org/color/CCCCFF">Periwinkle in colornames.org</a>
, <a href="https://www.htmlcsscolor.com/hex/CCCCFF">Lavender Blue in htmlcsscolor.com</a>:
</p>
<p title="𝗥𝗚𝗕 (204 204 255)&#10;𝗛𝗘𝗫 #CCCCFF" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(204,204,255); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p>
secondary 1 color <a href="https://colornames.org/color/666666">Cursed Grey</a>
 or <a href="https://www.htmlcsscolor.com/hex/666666">Dim Gray</a>:
</p>
<p title="𝗥𝗚𝗕 (102 102 102)&#10;𝗛𝗘𝗫 #666666" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(102,102,102); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p>
secondary 2 color <a href="https://colornames.org/color/999999">Million Grey </a>
 or <a href="https://www.htmlcsscolor.com/hex/999999">Nobel</a>:
</p>
<p title="𝗥𝗚𝗕 (153 153 153)&#10;𝗛𝗘𝗫 #999999" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(153,153,153); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p>
secondary 3 color <a href="https://en.wikipedia.org/wiki/Silver_(color)#Silver" title="Silver (color)">Neon silver</a>
 or <a href="https://colornames.org/color/CCCCCC">Cerebral Grey</a>
 or <a href="https://www.htmlcsscolor.com/hex/CCCCCC">Very Light Grey</a>:
</p>
<p title="𝗥𝗚𝗕 (204 204 204)&#10;𝗛𝗘𝗫 #CCCCCC" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(204,204,204); border:solid 1px #ccc;text-align:right;cursor:help;">
</p>

<p> NB: <code>OceanTheme</code> override this colors with </p>
<p>
<a href="https://www.htmlcsscolor.com/hex/333333">Night Rider</a> 
or <a href="https://colornames.org/color/333333">Dark Charcoal</a> 
for Black aka OCEAN_BLACK :
</p>
<p title="𝗥𝗚𝗕 (51 51 51)&#10;𝗛𝗘𝗫 #333333" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(51,51,51); border:solid 1px #ccc;text-align:right;cursor:help;">
Night Rider</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/6382BF">Havelock Blue</a> for PRIMARY1 :
</p>
<p title="𝗥𝗚𝗕 (99 130 191)&#10;𝗛𝗘𝗫 #6382BF" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(99,130,191); border:solid 1px #ccc;text-align:right;cursor:help;">
Havelock Blue</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/A3B8CC">Heather</a> 
or <a href="https://colornames.org/color/A3B8CC">Mood Cloud Blue</a> 
for PRIMARY2 :
</p>
<p title="𝗥𝗚𝗕 (163 184 204)&#10;𝗛𝗘𝗫 #A3B8CC" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(163,184,204); border:solid 1px #ccc;text-align:right;cursor:help;">
Heather</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/B8CFE5">Tropical Blue</a> 
for PRIMARY3 :
</p>
<p title="𝗥𝗚𝗕 (184 207 229)&#10;𝗛𝗘𝗫 #B8CFE5" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(184,207,229); border:solid 1px #ccc;text-align:right;cursor:help;">
Tropical Blue</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/7A8A99">Light Slate Grey</a> 
or <a href="https://colornames.org/color/7A8A99">Delta Queen Dam</a> 
for SECONDARY1 :
</p>
<p title="𝗥𝗚𝗕 (122 138 153)&#10;𝗛𝗘𝗫 #7A8A99" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(122,138,153); border:solid 1px #ccc;text-align:right;cursor:help;">
Light Slate Grey</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/B8CFE5">Tropical Blue</a> 
for SECONDARY2 (equals to PRIMARY3) :
</p>
<p title="𝗥𝗚𝗕 (184 207 229)&#10;𝗛𝗘𝗫 #B8CFE5" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(184,207,229); border:solid 1px #ccc;text-align:right;cursor:help;">
Tropical Blue</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/EEEEEE">Whisper</a> 
or <a href="https://colornames.org/color/EEEEEE">Screeching White</a> 
for SECONDARY3 :
</p>
<p title="𝗥𝗚𝗕 (238 238 238)&#10;𝗛𝗘𝗫 #EEEEEE" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(238,238,238); border:solid 1px #ccc;text-align:right;cursor:help;">
Whisper</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/C8DDF2">Pattens Blue</a> 
for "TabbedPane.contentAreaColor", "TabbedPane.selected", ... :
</p>
<p title="𝗥𝗚𝗕 (200 221 242)&#10;𝗛𝗘𝗫 #C8DDF2" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(200,221,242); border:solid 1px #ccc;text-align:right;cursor:help;">
Pattens Blue</p>

<p>
<a href="https://www.htmlcsscolor.com/hex/999999">Nobel</a> 
or <a href="https://colornames.org/color/999999">Million Grey</a> 
for INACTIVE_CONTROL_TEXT_COLOR,MENU_DISABLED_FOREGROUND aka (STEEL)secondary 2:
</p>
<p title="𝗥𝗚𝗕 (153 153 153)&#10;𝗛𝗘𝗫 #999999" style="width:10em;height:1em; padding:5px;margin:auto; background-color:rgb(153,153,153); border:solid 1px #ccc;text-align:right;cursor:help;">
Nobel</p>

 */

// Ausserdem werden in Ocean noch weitere Farben verwendet:
//  TODO          "Button.disabledToolBarBorderBackground","ToolBar.borderColor" cccccc,
//  TODO          "TabbedPane.tabAreaBackground", dadada,

package swingset.plaf;
