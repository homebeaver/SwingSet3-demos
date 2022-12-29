package org.jdesktop.swingx.demos.svg;

import java.awt.*;
import java.awt.geom.*;
import java.util.Stack;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.RadianceIconUIResource;

/**
 * This class has been automatically generated using 
 * <a href="https://jdesktop.wordpress.com/2022/09/25/svg-icons/">Radiance SVG converter</a>.
 */
public class CircleFlagES implements RadianceIcon {
    private Shape shape = null;
    private GeneralPath generalPath = null;
    private Paint paint = null;
    private Stroke stroke = null;
    private RadianceIcon.ColorFilter colorFilter = null;
    private Stack<AffineTransform> transformsStack = new Stack<>();

	// EUG https://github.com/homebeaver (rotation + point/axis reflection)
    private int rsfx = 1, rsfy = 1;
    public void setReflection(boolean horizontal, boolean vertical) {
    	this.rsfx = vertical ? -1 : 1;
    	this.rsfy = horizontal ? -1 : 1;
    }    
    public boolean isReflection() {
		return rsfx==-1 || rsfy==-1;
	}
	
    private double theta = 0;
    public void setRotation(double theta) {
    	this.theta = theta;
    }    
    public double getRotation() {
		return theta;
	}
	// EUG -- END

    

	private void _paint0(Graphics2D g,float origAlpha) {
transformsStack.push(g.getTransform());
// 
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -0.0f, -0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(0.0f, 128.0f);
generalPath.lineTo(256.0f, 96.0f);
generalPath.lineTo(512.0f, 128.0f);
generalPath.lineTo(512.0f, 384.0f);
generalPath.lineTo(256.0f, 416.0f);
generalPath.lineTo(0.0f, 384.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 218, 68, 255)) : new Color(255, 218, 68, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_1
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(0.0f, 0.0f);
generalPath.lineTo(512.0f, 0.0f);
generalPath.lineTo(512.0f, 128.0f);
generalPath.lineTo(0.0f, 128.0f);
generalPath.closePath();
generalPath.moveTo(0.0f, 384.0f);
generalPath.lineTo(512.0f, 384.0f);
generalPath.lineTo(512.0f, 512.0f);
generalPath.lineTo(0.0f, 512.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(144.0f, 304.0f);
generalPath.lineTo(128.0f, 304.0f);
generalPath.lineTo(128.0f, 224.0f);
generalPath.lineTo(144.0f, 224.0f);
generalPath.closePath();
generalPath.moveTo(272.0f, 304.0f);
generalPath.lineTo(288.0f, 304.0f);
generalPath.lineTo(288.0f, 224.0f);
generalPath.lineTo(272.0f, 224.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(238, 238, 238, 255)) : new Color(238, 238, 238, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2_1
shape = new Ellipse2D.Double(160.0, 264.0, 96.0, 64.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(238, 238, 238, 255)) : new Color(238, 238, 238, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_0
shape = new RoundRectangle2D.Double(128.0, 192.0, 16.0, 24.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_1
shape = new RoundRectangle2D.Double(272.0, 192.0, 16.0, 24.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3_2
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(208.0f, 272.0f);
generalPath.lineTo(208.0f, 296.0f);
generalPath.curveTo(208.0f, 309.25482f, 218.74516f, 320.0f, 232.0f, 320.0f);
generalPath.curveTo(245.25484f, 320.0f, 256.0f, 309.25482f, 256.0f, 296.0f);
generalPath.lineTo(256.0f, 272.0f);
generalPath.lineTo(232.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
shape = new RoundRectangle2D.Double(120.0, 208.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
shape = new RoundRectangle2D.Double(264.0, 208.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
shape = new RoundRectangle2D.Double(120.0, 304.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7
shape = new RoundRectangle2D.Double(264.0, 304.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_8
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(160.0f, 272.0f);
generalPath.lineTo(160.0f, 296.0f);
generalPath.curveTo(160.0f, 304.0f, 164.0f, 310.0f, 169.0f, 315.0f);
generalPath.lineTo(174.0f, 309.0f);
generalPath.lineTo(179.0f, 319.0f);
generalPath.curveTo(182.2847f, 319.80524f, 185.7153f, 319.80524f, 189.0f, 319.0f);
generalPath.lineTo(194.0f, 309.0f);
generalPath.lineTo(199.0f, 315.0f);
generalPath.curveTo(205.0f, 310.0f, 208.0f, 304.0f, 208.0f, 296.0f);
generalPath.lineTo(208.0f, 272.0f);
generalPath.lineTo(199.0f, 272.0f);
generalPath.lineTo(194.0f, 280.0f);
generalPath.lineTo(189.0f, 272.0f);
generalPath.lineTo(179.0f, 272.0f);
generalPath.lineTo(174.0f, 280.0f);
generalPath.lineTo(169.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_9
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(122.0f, 252.0f);
generalPath.lineTo(294.0f, 252.0f);
generalPath.moveTo(122.0f, 276.0f);
generalPath.lineTo(150.0f, 276.0f);
generalPath.moveTo(266.0f, 276.0f);
generalPath.lineTo(294.0f, 276.0f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_10
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(122.0f, 248.0f);
generalPath.curveTo(119.79086f, 248.0f, 118.0f, 249.79086f, 118.0f, 252.0f);
generalPath.curveTo(118.0f, 254.20914f, 119.79086f, 256.0f, 122.0f, 256.0f);
generalPath.lineTo(294.0f, 256.0f);
generalPath.curveTo(296.20914f, 256.0f, 298.0f, 254.20914f, 298.0f, 252.0f);
generalPath.curveTo(298.0f, 249.79086f, 296.20914f, 248.0f, 294.0f, 248.0f);
generalPath.closePath();
generalPath.moveTo(122.0f, 272.0f);
generalPath.curveTo(119.79086f, 272.0f, 118.0f, 273.79086f, 118.0f, 276.0f);
generalPath.curveTo(118.0f, 278.20914f, 119.79086f, 280.0f, 122.0f, 280.0f);
generalPath.lineTo(150.0f, 280.0f);
generalPath.curveTo(152.20914f, 280.0f, 154.0f, 278.20914f, 154.0f, 276.0f);
generalPath.curveTo(154.0f, 273.79086f, 152.20914f, 272.0f, 150.0f, 272.0f);
generalPath.closePath();
generalPath.moveTo(266.0f, 272.0f);
generalPath.curveTo(263.79086f, 272.0f, 262.0f, 273.79086f, 262.0f, 276.0f);
generalPath.curveTo(262.0f, 278.20914f, 263.79086f, 280.0f, 266.0f, 280.0f);
generalPath.lineTo(294.0f, 280.0f);
generalPath.curveTo(296.20914f, 280.0f, 298.0f, 278.20914f, 298.0f, 276.0f);
generalPath.curveTo(298.0f, 273.79086f, 296.20914f, 272.0f, 294.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_11
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(196.0f, 168.0f);
generalPath.curveTo(189.0f, 168.0f, 183.0f, 173.0f, 181.0f, 179.0f);
generalPath.lineTo(176.0f, 178.0f);
generalPath.curveTo(167.0f, 178.0f, 160.0f, 185.0f, 160.0f, 194.0f);
generalPath.curveTo(160.0f, 203.0f, 167.0f, 210.0f, 176.0f, 210.0f);
generalPath.curveTo(183.0f, 210.0f, 189.0f, 206.0f, 191.0f, 199.0f);
generalPath.curveTo(196.95886f, 201.14955f, 203.62482f, 199.58109f, 208.0f, 195.0f);
generalPath.curveTo(212.37518f, 199.58109f, 219.04114f, 201.14955f, 225.0f, 199.0f);
generalPath.curveTo(226.98476f, 205.19539f, 232.5231f, 209.57224f, 239.00946f, 210.07143f);
generalPath.curveTo(245.49582f, 210.5706f, 251.63866f, 207.09274f, 254.54802f, 201.27402f);
generalPath.curveTo(257.4574f, 195.45528f, 256.554f, 188.45428f, 252.26282f, 183.56471f);
generalPath.curveTo(247.97165f, 178.67514f, 241.14716f, 176.87057f, 235.0f, 179.0f);
generalPath.curveTo(233.03815f, 173.72618f, 228.45024f, 169.87057f, 222.91742f, 168.84598f);
generalPath.curveTo(217.38458f, 167.82138f, 211.72025f, 169.77843f, 208.0f, 174.0f);
generalPath.curveTo(205.0f, 170.0f, 201.0f, 168.0f, 196.0f, 168.0f);
generalPath.closePath();
generalPath.moveTo(196.0f, 176.0f);
generalPath.curveTo(201.0f, 176.0f, 204.0f, 180.0f, 204.0f, 184.0f);
generalPath.curveTo(204.0f, 189.0f, 201.0f, 192.0f, 196.0f, 192.0f);
generalPath.curveTo(192.0f, 192.0f, 188.0f, 189.0f, 188.0f, 184.0f);
generalPath.curveTo(188.0f, 180.0f, 192.0f, 176.0f, 196.0f, 176.0f);
generalPath.closePath();
generalPath.moveTo(220.0f, 176.0f);
generalPath.curveTo(225.0f, 176.0f, 228.0f, 180.0f, 228.0f, 184.0f);
generalPath.curveTo(228.0f, 189.0f, 225.0f, 192.0f, 220.0f, 192.0f);
generalPath.curveTo(216.0f, 192.0f, 212.0f, 189.0f, 212.0f, 184.0f);
generalPath.curveTo(212.0f, 180.0f, 216.0f, 176.0f, 220.0f, 176.0f);
generalPath.closePath();
generalPath.moveTo(176.0f, 186.0f);
generalPath.lineTo(180.0f, 187.0f);
generalPath.lineTo(184.0f, 195.0f);
generalPath.curveTo(184.0f, 199.0f, 180.0f, 202.0f, 176.0f, 202.0f);
generalPath.curveTo(172.0f, 202.0f, 168.0f, 199.0f, 168.0f, 194.0f);
generalPath.curveTo(168.0f, 190.0f, 172.0f, 186.0f, 176.0f, 186.0f);
generalPath.closePath();
generalPath.moveTo(240.0f, 186.0f);
generalPath.curveTo(245.0f, 186.0f, 248.0f, 190.0f, 248.0f, 194.0f);
generalPath.curveTo(248.0f, 199.0f, 245.0f, 202.0f, 240.0f, 202.0f);
generalPath.curveTo(236.0f, 202.0f, 232.0f, 199.0f, 232.0f, 195.0f);
generalPath.lineTo(236.0f, 187.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(238, 238, 238, 255)) : new Color(238, 238, 238, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_12
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_13
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(200.0f, 160.0f);
generalPath.lineTo(216.0f, 160.0f);
generalPath.lineTo(216.0f, 192.0f);
generalPath.lineTo(200.0f, 192.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_14
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(208.0f, 224.0f);
generalPath.lineTo(256.0f, 224.0f);
generalPath.lineTo(256.0f, 272.0f);
generalPath.lineTo(208.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(238, 238, 238, 255)) : new Color(238, 238, 238, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_15
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(248.0f, 208.0f);
generalPath.lineTo(240.0f, 216.0f);
generalPath.lineTo(176.0f, 216.0f);
generalPath.lineTo(168.0f, 208.0f);
generalPath.curveTo(168.0f, 195.0f, 186.0f, 184.0f, 208.0f, 184.0f);
generalPath.curveTo(230.0f, 184.0f, 248.0f, 195.0f, 248.0f, 208.0f);
generalPath.closePath();
generalPath.moveTo(160.0f, 224.0f);
generalPath.lineTo(208.0f, 224.0f);
generalPath.lineTo(208.0f, 272.0f);
generalPath.lineTo(160.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_16
shape = new RoundRectangle2D.Double(222.0, 232.0, 20.0, 32.0, 20.0, 20.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_17
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(168.0f, 232.0f);
generalPath.lineTo(168.0f, 240.0f);
generalPath.lineTo(176.0f, 240.0f);
generalPath.lineTo(176.0f, 256.0f);
generalPath.lineTo(168.0f, 256.0f);
generalPath.lineTo(168.0f, 264.0f);
generalPath.lineTo(200.0f, 264.0f);
generalPath.lineTo(200.0f, 256.0f);
generalPath.lineTo(192.0f, 256.0f);
generalPath.lineTo(192.0f, 240.0f);
generalPath.lineTo(200.0f, 240.0f);
generalPath.lineTo(200.0f, 232.0f);
generalPath.closePath();
generalPath.moveTo(176.0f, 216.0f);
generalPath.lineTo(240.0f, 216.0f);
generalPath.lineTo(240.0f, 224.0f);
generalPath.lineTo(176.0f, 224.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 152, 17, 255)) : new Color(255, 152, 17, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18_0
shape = new Ellipse2D.Double(180.0, 196.0, 12.0, 12.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 218, 68, 255)) : new Color(255, 218, 68, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18_1
shape = new Ellipse2D.Double(202.0, 196.0, 12.0, 12.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 218, 68, 255)) : new Color(255, 218, 68, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18_2
shape = new Ellipse2D.Double(224.0, 196.0, 12.0, 12.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 218, 68, 255)) : new Color(255, 218, 68, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_19
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(169.0f, 272.0f);
generalPath.lineTo(169.0f, 315.0f);
generalPath.curveTo(171.98724f, 317.0737f, 175.40668f, 318.4415f, 179.0f, 319.0f);
generalPath.lineTo(179.0f, 272.0f);
generalPath.lineTo(169.0f, 272.0f);
generalPath.closePath();
generalPath.moveTo(189.0f, 272.0f);
generalPath.lineTo(189.0f, 319.0f);
generalPath.curveTo(192.59332f, 318.4415f, 196.01276f, 317.0737f, 199.0f, 315.0f);
generalPath.lineTo(199.0f, 272.0f);
generalPath.lineTo(189.0f, 272.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(216, 0, 39, 255)) : new Color(216, 0, 39, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_20
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_20_0
shape = new Ellipse2D.Double(192.0, 256.0, 32.0, 32.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(51, 138, 243, 255)) : new Color(51, 138, 243, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_20_1
shape = new RoundRectangle2D.Double(264.0, 320.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(51, 138, 243, 255)) : new Color(51, 138, 243, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_20_2
shape = new RoundRectangle2D.Double(120.0, 320.0, 32.0, 16.0, 16.0, 16.0);
paint = (colorFilter != null) ? colorFilter.filter(new Color(51, 138, 243, 255)) : new Color(51, 138, 243, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());

}



	private void innerPaint(Graphics2D g) {
        float origAlpha = 1.0f;
        Composite origComposite = g.getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = 
                (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }
        
	    _paint0(g, origAlpha);


	    shape = null;
	    generalPath = null;
	    paint = null;
	    stroke = null;
        transformsStack.clear();
	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static double getOrigX() {
        return 0.0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 0.0;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 512.0;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 512.0;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private CircleFlagES() {
        this.width = (int) getOrigWidth();
        this.height = (int) getOrigHeight();
	}

    @Override
	public int getIconHeight() {
		return height;
	}

    @Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public synchronized void setDimension(Dimension newDimension) {
		this.width = newDimension.width;
		this.height = newDimension.height;
	}

    @Override
    public boolean supportsColorFilter() {
        return true;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
    }

    @Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if(getRotation()!=0) {
            g2d.rotate(getRotation(), x+width/2, y+height/2);
        }
        if(isReflection()) {
        	g2d.translate(x+width/2, y+height/2);
        	g2d.scale(this.rsfx, this.rsfy);
        	g2d.translate(-x-width/2, -y-height/2);
        }
		g2d.translate(x, y);

        double coef1 = (double) this.width / getOrigWidth();
        double coef2 = (double) this.height / getOrigHeight();
        double coef = Math.min(coef1, coef2);
        g2d.clipRect(0, 0, this.width, this.height);
        g2d.scale(coef, coef);
        g2d.translate(-getOrigX(), -getOrigY());
        if (coef1 != coef2) {
            if (coef1 < coef2) {
               int extraDy = (int) ((getOrigWidth() - getOrigHeight()) / 2.0);
               g2d.translate(0, extraDy);
            } else {
               int extraDx = (int) ((getOrigHeight() - getOrigWidth()) / 2.0);
               g2d.translate(extraDx, 0);
            }
        }
        Graphics2D g2ForInner = (Graphics2D) g2d.create();
        innerPaint(g2ForInner);
        g2ForInner.dispose();
        g2d.dispose();
	}
    
    /**
     * Returns a new instance of this icon with specified dimensions.
     *
     * @param width Required width of the icon
     * @param height Required height of the icon
     * @return A new instance of this icon with specified dimensions.
     */
    public static RadianceIcon of(int width, int height) {
       CircleFlagES base = new CircleFlagES();
       base.width = width;
       base.height = height;
       return base;
    }

    /**
     * Returns a new {@link UIResource} instance of this icon with specified dimensions.
     *
     * @param width Required width of the icon
     * @param height Required height of the icon
     * @return A new {@link UIResource} instance of this icon with specified dimensions.
     */
    public static RadianceIconUIResource uiResourceOf(int width, int height) {
       CircleFlagES base = new CircleFlagES();
       base.width = width;
       base.height = height;
       return new RadianceIconUIResource(base);
    }

    /**
     * Returns a factory that returns instances of this icon on demand.
     *
     * @return Factory that returns instances of this icon on demand.
     */
    public static Factory factory() {
        return CircleFlagES::new;
    }
}

