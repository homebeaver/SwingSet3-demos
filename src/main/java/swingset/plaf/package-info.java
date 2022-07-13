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
 * <code>swingset.plaf</code> contains extensions of DefaultMetalTheme.
 * <code>DefaultMetalTheme</code> has 6 color definitions
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

 */
package swingset.plaf;