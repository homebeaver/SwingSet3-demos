package org.jdesktop.swingx.demos.svg;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.ref.WeakReference;
import java.util.Base64;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.RadianceIconUIResource;

/**
 * This class has been automatically generated using <a
 * href="https://github.com/kirill-grouchnikov/radiance">Radiance SVG transcoder</a>.
 */
public class FlagBR implements RadianceIcon {
    private Shape shape = null;
    private GeneralPath generalPath = null;
    private Paint paint = null;
    private Stroke stroke = null;
    private Shape clip = null;
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 134.67820739746094f, 82.27474212646484f));
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
generalPath.moveTo(188.04008f, 253.79486f);
generalPath.lineTo(189.29973f, 258.72116f);
generalPath.lineTo(185.00262f, 256.00226f);
generalPath.lineTo(180.70683f, 258.72116f);
generalPath.lineTo(181.96648f, 253.79488f);
generalPath.lineTo(178.05136f, 250.54858f);
generalPath.lineTo(183.12698f, 250.22475f);
generalPath.lineTo(185.00261f, 245.49806f);
generalPath.lineTo(186.88086f, 250.22475f);
generalPath.lineTo(191.95517f, 250.54858f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(147.1574f, 214.22998f);
generalPath.lineTo(148.41704f, 219.16023f);
generalPath.lineTo(144.11993f, 216.43738f);
generalPath.lineTo(139.82414f, 219.16023f);
generalPath.lineTo(141.08247f, 214.22998f);
generalPath.lineTo(137.16869f, 210.98766f);
generalPath.lineTo(142.243f, 210.6625f);
generalPath.lineTo(144.11992f, 205.93713f);
generalPath.lineTo(145.99684f, 210.6625f);
generalPath.lineTo(151.07115f, 210.98766f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_2
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(136.37695f, 159.78578f);
generalPath.lineTo(137.6366f, 164.71207f);
generalPath.lineTo(133.3408f, 161.99185f);
generalPath.lineTo(129.0437f, 164.71207f);
generalPath.lineTo(130.30334f, 159.78578f);
generalPath.lineTo(126.38824f, 156.54082f);
generalPath.lineTo(131.46387f, 156.21567f);
generalPath.lineTo(133.34079f, 151.48898f);
generalPath.lineTo(135.21642f, 156.21567f);
generalPath.lineTo(140.29205f, 156.54082f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_3
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(614.6782f, -82.27474f);
generalPath.lineTo(614.6782f, 442.27475f);
generalPath.lineTo(-134.67816f, 442.27475f);
generalPath.lineTo(-134.67816f, -82.27474f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 156, 55, 255)) : new Color(0, 156, 55, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_4
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(550.9829f, 180.0f);
generalPath.lineTo(240.0f, 378.57947f);
generalPath.lineTo(-70.98289f, 180.0f);
generalPath.lineTo(240.00003f, -18.579453f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(254, 224, 0, 255)) : new Color(254, 224, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_5
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(239.99934f, 48.86263f);
generalPath.curveTo(312.4129f, 48.86263f, 371.1374f, 107.586395f, 371.1374f, 180.00067f);
generalPath.curveTo(371.1374f, 252.41496f, 312.41287f, 311.1374f, 239.99934f, 311.1374f);
generalPath.curveTo(167.58585f, 311.1374f, 108.86264f, 252.41364f, 108.86264f, 180.00069f);
generalPath.curveTo(108.86264f, 107.58774f, 167.58582f, 48.86264f, 239.99934f, 48.86264f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 34, 119, 255)) : new Color(0, 34, 119, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_6
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(283.12247f, 135.4954f);
generalPath.lineTo(284.3821f, 140.42169f);
generalPath.lineTo(280.08765f, 137.70148f);
generalPath.lineTo(275.79053f, 140.42169f);
generalPath.lineTo(277.05148f, 135.4954f);
generalPath.lineTo(273.13504f, 132.25044f);
generalPath.lineTo(278.212f, 131.9253f);
generalPath.lineTo(280.08762f, 127.1986f);
generalPath.lineTo(281.96188f, 131.9253f);
generalPath.lineTo(287.0375f, 132.25044f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_7
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(272.91962f, 172.94147f);
generalPath.lineTo(273.82373f, 176.48517f);
generalPath.lineTo(270.7347f, 174.52893f);
generalPath.lineTo(267.64307f, 176.48517f);
generalPath.lineTo(268.55115f, 172.94147f);
generalPath.lineTo(265.73312f, 170.60721f);
generalPath.lineTo(269.38388f, 170.37457f);
generalPath.lineTo(270.73474f, 166.97495f);
generalPath.lineTo(272.0843f, 170.37457f);
generalPath.lineTo(275.73505f, 170.60721f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(136.37628f, 159.78578f);
generalPath.lineTo(137.63593f, 164.71207f);
generalPath.lineTo(133.34146f, 161.99185f);
generalPath.lineTo(129.04436f, 164.71207f);
generalPath.lineTo(130.30533f, 159.78578f);
generalPath.lineTo(126.38888f, 156.54082f);
generalPath.lineTo(131.46585f, 156.21567f);
generalPath.lineTo(133.34148f, 151.48898f);
generalPath.lineTo(135.21576f, 156.21567f);
generalPath.lineTo(140.29138f, 156.54082f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(191.39345f, 186.39853f);
generalPath.lineTo(192.45747f, 190.55553f);
generalPath.lineTo(188.83049f, 188.26091f);
generalPath.lineTo(185.20485f, 190.55553f);
generalPath.lineTo(186.26888f, 186.39853f);
generalPath.lineTo(182.96442f, 183.65717f);
generalPath.lineTo(187.247f, 183.38489f);
generalPath.lineTo(188.83049f, 179.39708f);
generalPath.lineTo(190.41531f, 183.38489f);
generalPath.lineTo(194.69789f, 183.65717f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(157.76866f, 205.12422f);
generalPath.lineTo(158.44673f, 207.7744f);
generalPath.lineTo(156.13495f, 206.31119f);
generalPath.lineTo(153.82448f, 207.7744f);
generalPath.lineTo(154.50122f, 205.12422f);
generalPath.lineTo(152.39561f, 203.37814f);
generalPath.lineTo(155.12509f, 203.20367f);
generalPath.lineTo(156.13493f, 200.66057f);
generalPath.lineTo(157.14478f, 203.20367f);
generalPath.lineTo(159.87558f, 203.37814f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(147.15607f, 214.23393f);
generalPath.lineTo(148.41571f, 219.16022f);
generalPath.lineTo(144.12125f, 216.44f);
generalPath.lineTo(139.82414f, 219.16022f);
generalPath.lineTo(141.08511f, 214.23393f);
generalPath.lineTo(137.16867f, 210.98897f);
generalPath.lineTo(142.24564f, 210.66382f);
generalPath.lineTo(144.12126f, 205.93713f);
generalPath.lineTo(145.99554f, 210.66382f);
generalPath.lineTo(151.07117f, 210.98897f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_12
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(132.94955f, 225.3792f);
generalPath.lineTo(134.01227f, 229.5362f);
generalPath.lineTo(130.38663f, 227.24158f);
generalPath.lineTo(126.76099f, 229.5362f);
generalPath.lineTo(127.8237f, 225.3792f);
generalPath.lineTo(124.52058f, 222.64178f);
generalPath.lineTo(128.80183f, 222.36685f);
generalPath.lineTo(130.38666f, 218.3764f);
generalPath.lineTo(131.97015f, 222.36685f);
generalPath.lineTo(136.25273f, 222.64178f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
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
generalPath.moveTo(173.19781f, 226.39961f);
generalPath.lineTo(174.26053f, 230.55925f);
generalPath.lineTo(170.63622f, 228.262f);
generalPath.lineTo(167.01057f, 230.55925f);
generalPath.lineTo(168.07329f, 226.39961f);
generalPath.lineTo(164.77017f, 223.66089f);
generalPath.lineTo(169.05275f, 223.38728f);
generalPath.lineTo(170.63625f, 219.39815f);
generalPath.lineTo(172.21974f, 223.38728f);
generalPath.lineTo(176.501f, 223.66089f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(168.07722f, 240.07082f);
generalPath.lineTo(168.98265f, 243.61452f);
generalPath.lineTo(165.89232f, 241.65695f);
generalPath.lineTo(162.80199f, 243.61452f);
generalPath.lineTo(163.70741f, 240.07082f);
generalPath.lineTo(160.89203f, 237.73656f);
generalPath.lineTo(164.54279f, 237.50392f);
generalPath.lineTo(165.89233f, 234.10165f);
generalPath.lineTo(167.24188f, 237.50392f);
generalPath.lineTo(170.89264f, 237.73656f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(188.03944f, 253.79486f);
generalPath.lineTo(189.29909f, 258.72116f);
generalPath.lineTo(185.00462f, 256.00095f);
generalPath.lineTo(180.70752f, 258.72116f);
generalPath.lineTo(181.96849f, 253.79488f);
generalPath.lineTo(178.05205f, 250.54991f);
generalPath.lineTo(183.12901f, 250.22476f);
generalPath.lineTo(185.00464f, 245.49808f);
generalPath.lineTo(186.87892f, 250.22476f);
generalPath.lineTo(191.95456f, 250.54991f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_16
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(241.81812f, 284.65582f);
generalPath.lineTo(242.30452f, 286.5486f);
generalPath.lineTo(240.65228f, 285.50308f);
generalPath.lineTo(239.00269f, 286.5486f);
generalPath.lineTo(239.48645f, 284.65582f);
generalPath.lineTo(237.98358f, 283.4107f);
generalPath.lineTo(239.93188f, 283.2838f);
generalPath.lineTo(240.65225f, 281.46768f);
generalPath.lineTo(241.37395f, 283.2838f);
generalPath.lineTo(243.32356f, 283.4107f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(243.55228f, 204.0126f);
generalPath.lineTo(244.61366f, 208.17093f);
generalPath.lineTo(240.98802f, 205.87498f);
generalPath.lineTo(237.36238f, 208.17093f);
generalPath.lineTo(238.4251f, 204.0126f);
generalPath.lineTo(235.12198f, 201.27388f);
generalPath.lineTo(239.40456f, 200.99895f);
generalPath.lineTo(240.98805f, 197.01114f);
generalPath.lineTo(242.57288f, 200.99895f);
generalPath.lineTo(246.85414f, 201.27388f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_18
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(255.54482f, 215.2332f);
generalPath.lineTo(256.60886f, 219.39153f);
generalPath.lineTo(252.98454f, 217.09427f);
generalPath.lineTo(249.3589f, 219.39153f);
generalPath.lineTo(250.42029f, 215.2332f);
generalPath.lineTo(247.11717f, 212.49579f);
generalPath.lineTo(251.39844f, 212.22086f);
generalPath.lineTo(252.98457f, 208.23305f);
generalPath.lineTo(254.56807f, 212.22086f);
generalPath.lineTo(258.85065f, 212.49579f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
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
generalPath.moveTo(234.02223f, 223.37274f);
generalPath.lineTo(234.7003f, 226.02292f);
generalPath.lineTo(232.38852f, 224.56104f);
generalPath.lineTo(230.07805f, 226.02292f);
generalPath.lineTo(230.75479f, 223.37274f);
generalPath.lineTo(228.64787f, 221.62799f);
generalPath.lineTo(231.37868f, 221.45352f);
generalPath.lineTo(232.38852f, 218.90909f);
generalPath.lineTo(233.39836f, 221.45352f);
generalPath.lineTo(236.12917f, 221.62799f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_20
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(225.00768f, 216.60918f);
generalPath.lineTo(225.9131f, 220.15288f);
generalPath.lineTo(222.82277f, 218.19531f);
generalPath.lineTo(219.73244f, 220.15288f);
generalPath.lineTo(220.63786f, 216.60918f);
generalPath.lineTo(217.82248f, 214.27226f);
generalPath.lineTo(221.47324f, 214.04094f);
generalPath.lineTo(222.82278f, 210.63998f);
generalPath.lineTo(224.17233f, 214.04094f);
generalPath.lineTo(227.82309f, 214.27226f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_21
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(243.26083f, 242.43414f);
generalPath.lineTo(244.52048f, 247.36043f);
generalPath.lineTo(240.22601f, 244.64021f);
generalPath.lineTo(235.92891f, 247.36043f);
generalPath.lineTo(237.18988f, 242.43414f);
generalPath.lineTo(233.27344f, 239.18918f);
generalPath.lineTo(238.3504f, 238.86403f);
generalPath.lineTo(240.22603f, 234.13734f);
generalPath.lineTo(242.10031f, 238.86403f);
generalPath.lineTo(247.17593f, 239.18918f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_22
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(269.44864f, 255.46956f);
generalPath.lineTo(270.35275f, 259.0159f);
generalPath.lineTo(267.26373f, 257.05835f);
generalPath.lineTo(264.1734f, 259.0159f);
generalPath.lineTo(265.07883f, 255.46956f);
generalPath.lineTo(262.26212f, 253.13794f);
generalPath.lineTo(265.91287f, 252.90398f);
generalPath.lineTo(267.26373f, 249.50568f);
generalPath.lineTo(268.61328f, 252.90398f);
generalPath.lineTo(272.26273f, 253.13794f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_23
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(280.57672f, 271.78564f);
generalPath.lineTo(281.63943f, 275.94528f);
generalPath.lineTo(278.0151f, 273.648f);
generalPath.lineTo(274.38812f, 275.94528f);
generalPath.lineTo(275.45215f, 271.78564f);
generalPath.lineTo(272.14902f, 269.04956f);
generalPath.lineTo(276.43027f, 268.77332f);
generalPath.lineTo(278.01508f, 264.7855f);
generalPath.lineTo(279.59592f, 268.77332f);
generalPath.lineTo(283.87982f, 269.04956f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_24
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(292.17007f, 258.7714f);
generalPath.lineTo(293.0768f, 262.31378f);
generalPath.lineTo(289.98648f, 260.35886f);
generalPath.lineTo(286.89484f, 262.31378f);
generalPath.lineTo(287.80157f, 258.7714f);
generalPath.lineTo(284.98618f, 256.43845f);
generalPath.lineTo(288.6343f, 256.2045f);
generalPath.lineTo(289.98648f, 252.80354f);
generalPath.lineTo(291.3347f, 256.2045f);
generalPath.lineTo(294.98544f, 256.43845f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_25
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(307.80673f, 282.233f);
generalPath.lineTo(308.71216f, 285.778f);
generalPath.lineTo(305.62314f, 283.82047f);
generalPath.lineTo(302.5328f, 285.778f);
generalPath.lineTo(303.43692f, 282.233f);
generalPath.lineTo(300.62152f, 279.90005f);
generalPath.lineTo(304.2736f, 279.6661f);
generalPath.lineTo(305.62314f, 276.26648f);
generalPath.lineTo(306.97003f, 279.6661f);
generalPath.lineTo(310.62344f, 279.90005f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_26
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(308.3381f, 268.60544f);
generalPath.lineTo(309.40082f, 272.76245f);
generalPath.lineTo(305.77383f, 270.46652f);
generalPath.lineTo(302.1482f, 272.76245f);
generalPath.lineTo(303.21222f, 268.60544f);
generalPath.lineTo(299.9104f, 265.8654f);
generalPath.lineTo(304.19165f, 265.5931f);
generalPath.lineTo(305.77383f, 261.60266f);
generalPath.lineTo(307.35733f, 265.5931f);
generalPath.lineTo(311.64124f, 265.8654f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_27
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(307.60052f, 255.73128f);
generalPath.lineTo(308.5086f, 259.27365f);
generalPath.lineTo(305.41696f, 257.31873f);
generalPath.lineTo(302.32794f, 259.27365f);
generalPath.lineTo(303.23206f, 255.73128f);
generalPath.lineTo(300.418f, 253.39569f);
generalPath.lineTo(304.06744f, 253.16173f);
generalPath.lineTo(305.417f, 249.76212f);
generalPath.lineTo(306.76785f, 253.16174f);
generalPath.lineTo(310.4186f, 253.3957f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_28
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(320.554f, 255.85684f);
generalPath.lineTo(321.4594f, 259.40317f);
generalPath.lineTo(318.36908f, 257.44562f);
generalPath.lineTo(315.27875f, 259.40317f);
generalPath.lineTo(316.1855f, 255.85683f);
generalPath.lineTo(313.36746f, 253.5252f);
generalPath.lineTo(317.01822f, 253.29124f);
generalPath.lineTo(318.36908f, 249.89163f);
generalPath.lineTo(319.71863f, 253.29126f);
generalPath.lineTo(323.3694f, 253.52522f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_29
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(333.17966f, 250.4481f);
generalPath.lineTo(334.24368f, 254.60248f);
generalPath.lineTo(330.61804f, 252.3092f);
generalPath.lineTo(326.99106f, 254.60248f);
generalPath.lineTo(328.05508f, 250.4481f);
generalPath.lineTo(324.75195f, 247.70674f);
generalPath.lineTo(329.03455f, 247.43446f);
generalPath.lineTo(330.61804f, 243.44533f);
generalPath.lineTo(332.20023f, 247.43446f);
generalPath.lineTo(336.48282f, 247.70674f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_30
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(339.38144f, 240.04305f);
generalPath.lineTo(340.44547f, 244.19873f);
generalPath.lineTo(336.81717f, 241.90546f);
generalPath.lineTo(333.19153f, 244.19873f);
generalPath.lineTo(334.25555f, 240.04305f);
generalPath.lineTo(330.95374f, 237.303f);
generalPath.lineTo(335.235f, 237.02939f);
generalPath.lineTo(336.81717f, 233.04158f);
generalPath.lineTo(338.40198f, 237.02939f);
generalPath.lineTo(342.68457f, 237.303f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_31
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(334.6627f, 227.3057f);
generalPath.lineTo(335.92233f, 232.23198f);
generalPath.lineTo(331.62787f, 229.51176f);
generalPath.lineTo(327.33075f, 232.23198f);
generalPath.lineTo(328.5917f, 227.3057f);
generalPath.lineTo(324.67526f, 224.06073f);
generalPath.lineTo(329.75223f, 223.73558f);
generalPath.lineTo(331.62784f, 219.0089f);
generalPath.lineTo(333.5021f, 223.73558f);
generalPath.lineTo(338.57773f, 224.06073f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_32
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(353.00638f, 228.16553f);
generalPath.lineTo(353.9118f, 231.70923f);
generalPath.lineTo(350.82147f, 229.75299f);
generalPath.lineTo(347.72983f, 231.70923f);
generalPath.lineTo(348.6379f, 228.16553f);
generalPath.lineTo(345.8212f, 225.83391f);
generalPath.lineTo(349.47064f, 225.5973f);
generalPath.lineTo(350.8215f, 222.1977f);
generalPath.lineTo(352.17105f, 225.59732f);
generalPath.lineTo(355.8205f, 225.83392f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_33
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(161.66467f, 139.57574f);
generalPath.curveTo(145.52199f, 139.57574f, 129.65863f, 140.80368f, 114.16058f, 143.17099f);
generalPath.curveTo(116.05482f, 136.68765f, 118.442795f, 130.41316f, 121.275536f, 124.38318f);
generalPath.curveTo(134.5206f, 122.76401f, 147.99469f, 121.912766f, 161.66599f, 121.912766f);
generalPath.curveTo(240.50235f, 121.912766f, 313.05368f, 149.63979f, 370.06207f, 195.81094f);
generalPath.curveTo(369.22946f, 202.52823f, 367.8885f, 209.09352f, 366.06842f, 215.46056f);
generalPath.curveTo(311.0814f, 168.20291f, 239.61682f, 139.57574f, 161.6647f, 139.57574f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_34
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(127.92281f, 132.77518f);
generalPath.curveTo(128.03261f, 134.05598f, 128.40526f, 135.02617f, 129.03442f, 135.68704f);
generalPath.curveTo(129.6636f, 136.34795f, 130.48972f, 136.63213f, 131.51277f, 136.54488f);
generalPath.curveTo(132.54507f, 136.45627f, 133.31833f, 136.03334f, 133.8325f, 135.27464f);
generalPath.curveTo(134.34668f, 134.51727f, 134.54625f, 133.49686f, 134.43655f, 132.21605f);
generalPath.curveTo(134.32677f, 130.93787f, 133.95543f, 129.9677f, 133.31967f, 129.3081f);
generalPath.curveTo(132.68388f, 128.64723f, 131.84985f, 128.36304f, 130.81754f, 128.45158f);
generalPath.curveTo(129.79579f, 128.54019f, 129.02916f, 128.9618f, 128.52159f, 129.71788f);
generalPath.curveTo(128.01404f, 130.47128f, 127.81311f, 131.49039f, 127.92282f, 132.77518f);
generalPath.closePath();
generalPath.moveTo(125.48283f, 132.984f);
generalPath.curveTo(125.32153f, 131.10443f, 125.70621f, 129.56982f, 126.635414f, 128.3789f);
generalPath.curveTo(127.56464f, 127.18797f, 128.8983f, 126.51518f, 130.63776f, 126.36715f);
generalPath.curveTo(132.37459f, 126.21784f, 133.80475f, 126.65134f, 134.93091f, 127.6691f);
generalPath.curveTo(136.05576f, 128.68687f, 136.69814f, 130.1329f, 136.85938f, 132.00719f);
generalPath.curveTo(137.02066f, 133.88278f, 136.63469f, 135.42001f, 135.69887f, 136.6136f);
generalPath.curveTo(134.76303f, 137.80717f, 133.42804f, 138.47993f, 131.69121f, 138.628f);
generalPath.curveTo(129.95174f, 138.77731f, 128.5229f, 138.34381f, 127.40335f, 137.32603f);
generalPath.curveTo(126.28512f, 136.30827f, 125.64405f, 134.86092f, 125.48281f, 132.98398f);
generalPath.closePath();
generalPath.moveTo(144.48871f, 130.80965f);
generalPath.lineTo(147.31335f, 130.71579f);
generalPath.curveTo(147.92403f, 130.69481f, 148.36946f, 130.55447f, 148.651f, 130.2981f);
generalPath.curveTo(148.92856f, 130.03905f, 149.05942f, 129.64381f, 149.04092f, 129.10983f);
generalPath.curveTo(149.02406f, 128.60359f, 148.87177f, 128.22424f, 148.58359f, 127.97046f);
generalPath.curveTo(148.29546f, 127.71801f, 147.87247f, 127.60171f, 147.31998f, 127.62021f);
generalPath.lineTo(144.38562f, 127.718f);
generalPath.lineTo(144.48866f, 130.80963f);
generalPath.closePath();
generalPath.moveTo(142.30513f, 137.45824f);
generalPath.lineTo(141.9152f, 125.784256f);
generalPath.lineTo(147.57774f, 125.59527f);
generalPath.curveTo(148.84929f, 125.55162f, 149.8023f, 125.79351f, 150.4328f, 126.316956f);
generalPath.curveTo(151.06593f, 126.840385f, 151.39902f, 127.66385f, 151.43604f, 128.7834f);
generalPath.curveTo(151.45964f, 129.49452f, 151.33037f, 130.09592f, 151.04744f, 130.58102f);
generalPath.curveTo(150.7659f, 131.06746f, 150.34558f, 131.40582f, 149.79175f, 131.5988f);
generalPath.curveTo(150.30592f, 131.76273f, 150.6747f, 132.02441f, 150.90335f, 132.38658f);
generalPath.curveTo(151.1294f, 132.75009f, 151.27084f, 133.32242f, 151.32896f, 134.10358f);
generalPath.lineTo(151.42413f, 135.47955f);
generalPath.curveTo(151.42413f, 135.4908f, 151.42413f, 135.50728f, 151.42548f, 135.52846f);
generalPath.curveTo(151.46107f, 136.22371f, 151.6277f, 136.6427f, 151.91983f, 136.77885f);
generalPath.lineTo(151.93108f, 137.13705f);
generalPath.lineTo(149.29677f, 137.22566f);
generalPath.curveTo(149.20816f, 137.06305f, 149.13809f, 136.8648f, 149.0853f, 136.62558f);
generalPath.curveTo(149.03378f, 136.3863f, 148.99406f, 136.10083f, 148.97551f, 135.76775f);
generalPath.lineTo(148.9015f, 134.54114f);
generalPath.curveTo(148.84999f, 133.81943f, 148.69928f, 133.33435f, 148.44548f, 133.09113f);
generalPath.curveTo(148.19041f, 132.84395f, 147.74228f, 132.73293f, 147.10257f, 132.75409f);
generalPath.lineTo(144.5568f, 132.83878f);
generalPath.lineTo(144.70874f, 137.37779f);
generalPath.lineTo(142.30574f, 137.45834f);
generalPath.closePath();
generalPath.moveTo(159.5425f, 134.83449f);
generalPath.lineTo(161.46965f, 134.87158f);
generalPath.curveTo(162.58522f, 134.89293f, 163.39944f, 134.61646f, 163.90834f, 134.04152f);
generalPath.curveTo(164.41988f, 133.46918f, 164.68686f, 132.52412f, 164.71198f, 131.21422f);
generalPath.curveTo(164.73709f, 129.90962f, 164.52164f, 128.94473f, 164.06958f, 128.32347f);
generalPath.curveTo(163.61488f, 127.69959f, 162.89452f, 127.38104f, 161.90451f, 127.36122f);
generalPath.lineTo(159.68526f, 127.31889f);
generalPath.lineTo(159.54251f, 134.83455f);
generalPath.closePath();
generalPath.moveTo(157.14743f, 136.89912f);
generalPath.lineTo(157.36946f, 125.22116f);
generalPath.lineTo(161.94281f, 125.307144f);
generalPath.curveTo(163.73383f, 125.341606f, 165.06223f, 125.85171f, 165.9227f, 126.833786f);
generalPath.curveTo(166.78452f, 127.8172f, 167.19821f, 129.29231f, 167.16122f, 131.25911f);
generalPath.curveTo(167.14136f, 132.3258f, 166.96033f, 133.26427f, 166.62062f, 134.07188f);
generalPath.curveTo(166.2796f, 134.8795f, 165.79318f, 135.52583f, 165.16797f, 136.00961f);
generalPath.curveTo(164.69476f, 136.37047f, 164.16342f, 136.62689f, 163.56729f, 136.77757f);
generalPath.curveTo(162.97118f, 136.92557f, 162.1411f, 136.9917f, 161.07178f, 136.97054f);
generalPath.lineTo(157.1474f, 136.89915f);
generalPath.closePath();
generalPath.moveTo(171.96724f, 137.15952f);
generalPath.lineTo(172.5964f, 125.497444f);
generalPath.lineTo(181.06638f, 125.95477f);
generalPath.lineTo(180.95659f, 127.982376f);
generalPath.lineTo(174.85262f, 127.65327f);
generalPath.lineTo(174.71904f, 130.13953f);
generalPath.lineTo(180.3009f, 130.44087f);
generalPath.lineTo(180.19243f, 132.43544f);
generalPath.lineTo(174.61057f, 132.13403f);
generalPath.lineTo(174.44795f, 135.13448f);
generalPath.lineTo(180.83743f, 135.47948f);
generalPath.lineTo(180.7211f, 137.63399f);
generalPath.lineTo(171.96695f, 137.15945f);
generalPath.closePath();
generalPath.moveTo(185.71378f, 137.9764f);
generalPath.lineTo(186.95227f, 126.36323f);
generalPath.lineTo(190.48671f, 126.73994f);
generalPath.lineTo(191.85342f, 135.82587f);
generalPath.lineTo(195.07594f, 127.22899f);
generalPath.lineTo(198.6236f, 127.608345f);
generalPath.lineTo(197.3851f, 139.22284f);
generalPath.lineTo(195.146f, 138.9836f);
generalPath.lineTo(196.14395f, 129.61745f);
generalPath.lineTo(192.78664f, 138.73247f);
generalPath.lineTo(190.34265f, 138.47076f);
generalPath.lineTo(188.9508f, 128.8495f);
generalPath.lineTo(187.95288f, 138.21564f);
generalPath.lineTo(185.71379f, 137.9764f);
generalPath.closePath();
generalPath.moveTo(216.6633f, 141.96022f);
generalPath.lineTo(219.10991f, 130.54002f);
generalPath.lineTo(227.40541f, 132.31651f);
generalPath.lineTo(226.9798f, 134.30182f);
generalPath.lineTo(221.00401f, 133.02234f);
generalPath.lineTo(220.48192f, 135.45706f);
generalPath.lineTo(225.94748f, 136.62685f);
generalPath.lineTo(225.52979f, 138.58174f);
generalPath.lineTo(220.0629f, 137.41064f);
generalPath.lineTo(219.43373f, 140.34895f);
generalPath.lineTo(225.69104f, 141.68922f);
generalPath.lineTo(225.23898f, 143.80011f);
generalPath.lineTo(216.66327f, 141.96017f);
generalPath.closePath();
generalPath.moveTo(247.57845f, 142.68193f);
generalPath.lineTo(249.8228f, 143.28731f);
generalPath.curveTo(250.42819f, 143.45123f, 250.9027f, 143.44055f, 251.25034f, 143.25845f);
generalPath.curveTo(251.59541f, 143.07335f, 251.84514f, 142.698f, 251.99849f, 142.12569f);
generalPath.curveTo(252.14236f, 141.59036f, 252.11615f, 141.14888f, 251.92168f, 140.7986f);
generalPath.curveTo(251.72346f, 140.45097f, 251.35861f, 140.20511f, 250.82327f, 140.05972f);
generalPath.lineTo(248.45728f, 139.42264f);
generalPath.lineTo(247.57832f, 142.68214f);
generalPath.closePath();
generalPath.moveTo(247.01274f, 144.72144f);
generalPath.lineTo(245.93285f, 148.73303f);
generalPath.lineTo(243.61179f, 148.10783f);
generalPath.lineTo(246.65189f, 136.83038f);
generalPath.lineTo(251.6654f, 138.18256f);
generalPath.curveTo(252.84972f, 138.50111f, 253.66394f, 139.05229f, 254.1107f, 139.83083f);
generalPath.curveTo(254.55219f, 140.60934f, 254.61693f, 141.59276f, 254.2943f, 142.785f);
generalPath.curveTo(253.9822f, 143.94817f, 253.4404f, 144.76372f, 252.6751f, 145.23294f);
generalPath.curveTo(251.90846f, 145.70216f, 250.96603f, 145.78809f, 249.86366f, 145.49069f);
generalPath.lineTo(247.01259f, 144.72142f);
generalPath.closePath();
generalPath.moveTo(261.05533f, 146.55739f);
generalPath.lineTo(263.75705f, 147.38745f);
generalPath.curveTo(264.33997f, 147.5673f, 264.8092f, 147.58044f, 265.1595f, 147.42715f);
generalPath.curveTo(265.5072f, 147.27391f, 265.7569f, 146.94206f, 265.91556f, 146.43053f);
generalPath.curveTo(266.06354f, 145.94676f, 266.04257f, 145.53833f, 265.85336f, 145.20523f);
generalPath.curveTo(265.66153f, 144.87349f, 265.2995f, 144.62497f, 264.77216f, 144.46239f);
generalPath.lineTo(261.966f, 143.59926f);
generalPath.lineTo(261.05527f, 146.55742f);
generalPath.closePath();
generalPath.moveTo(256.82697f, 152.13266f);
generalPath.lineTo(260.2596f, 140.96889f);
generalPath.lineTo(265.67496f, 142.633f);
generalPath.curveTo(266.89233f, 143.00702f, 267.71317f, 143.54503f, 268.13745f, 144.24556f);
generalPath.curveTo(268.5657f, 144.94742f, 268.61328f, 145.83301f, 268.2828f, 146.90498f);
generalPath.curveTo(268.07526f, 147.5857f, 267.7581f, 148.11177f, 267.3298f, 148.47789f);
generalPath.curveTo(266.90417f, 148.84534f, 266.39795f, 149.02907f, 265.8137f, 149.0304f);

}

private void _paint1(Graphics2D g,float origAlpha) {
generalPath.curveTo(266.24457f, 149.35292f, 266.50763f, 149.72037f, 266.6081f, 150.13806f);
generalPath.curveTo(266.70328f, 150.55573f, 266.64893f, 151.14261f, 266.45224f, 151.89998f);
generalPath.lineTo(266.09406f, 153.23102f);
generalPath.curveTo(266.08655f, 153.24226f, 266.08282f, 153.25725f, 266.07907f, 153.27728f);
generalPath.curveTo(265.88873f, 153.9461f, 265.9086f, 154.39684f, 266.13977f, 154.62154f);
generalPath.lineTo(266.03522f, 154.96388f);
generalPath.lineTo(263.51324f, 154.1893f);
generalPath.curveTo(263.48328f, 154.00684f, 263.48328f, 153.79672f, 263.5095f, 153.55353f);
generalPath.curveTo(263.53946f, 153.31033f, 263.59418f, 153.02745f, 263.6867f, 152.7076f);
generalPath.lineTo(264.01566f, 151.52194f);
generalPath.curveTo(264.20187f, 150.82404f, 264.2165f, 150.3165f, 264.05536f, 150.00322f);
generalPath.curveTo(263.89386f, 149.68732f, 263.50684f, 149.43484f, 262.8975f, 149.24849f);
generalPath.lineTo(260.46277f, 148.50037f);
generalPath.lineTo(259.12778f, 152.84242f);
generalPath.lineTo(256.8292f, 152.13263f);
generalPath.closePath();
generalPath.moveTo(274.6155f, 152.09164f);
generalPath.curveTo(274.174f, 153.29974f, 274.10004f, 154.33603f, 274.3922f, 155.20047f);
generalPath.curveTo(274.68558f, 156.06493f, 275.3122f, 156.67293f, 276.27444f, 157.02454f);
generalPath.curveTo(277.2486f, 157.38011f, 278.1289f, 157.32458f, 278.91537f, 156.85405f);
generalPath.curveTo(279.70184f, 156.38351f, 280.31512f, 155.54417f, 280.7553f, 154.3374f);
generalPath.curveTo(281.19675f, 153.13193f, 281.2695f, 152.09564f, 280.97226f, 151.22989f);
generalPath.curveTo(280.67627f, 150.36281f, 280.0391f, 149.75215f, 279.06757f, 149.39659f);
generalPath.curveTo(278.10397f, 149.04369f, 277.2303f, 149.10182f, 276.45175f, 149.57231f);
generalPath.curveTo(275.6706f, 150.04288f, 275.05728f, 150.88219f, 274.61578f, 152.09163f);
generalPath.closePath();
generalPath.moveTo(272.31564f, 151.24965f);
generalPath.curveTo(272.962f, 149.47845f, 273.96127f, 148.2492f, 275.3055f, 147.56319f);
generalPath.curveTo(276.6524f, 146.87585f, 278.1447f, 146.83093f, 279.78638f, 147.42961f);
generalPath.curveTo(281.42276f, 148.0297f, 282.53702f, 149.0263f, 283.12387f, 150.42477f);
generalPath.curveTo(283.71338f, 151.8219f, 283.68698f, 153.40407f, 283.04068f, 155.1713f);
generalPath.curveTo(282.393f, 156.94116f, 281.39374f, 158.1691f, 280.04156f, 158.85643f);
generalPath.curveTo(278.6907f, 159.54376f, 277.19577f, 159.58868f, 275.55807f, 158.99f);
generalPath.curveTo(273.9164f, 158.38992f, 272.8061f, 157.39331f, 272.2219f, 155.99748f);
generalPath.curveTo(271.63766f, 154.6017f, 271.66675f, 153.01953f, 272.31595f, 151.24966f);
generalPath.closePath();
generalPath.moveTo(293.92148f, 164.96709f);
generalPath.curveTo(293.29095f, 165.3372f, 292.6697f, 165.53943f, 292.05377f, 165.57776f);
generalPath.curveTo(291.43915f, 165.61221f, 290.7875f, 165.48521f, 290.09885f, 165.1918f);
generalPath.curveTo(288.59863f, 164.55205f, 287.60464f, 163.51183f, 287.11688f, 162.0724f);
generalPath.curveTo(286.63046f, 160.63164f, 286.74933f, 159.06137f, 287.47394f, 157.36552f);
generalPath.curveTo(288.20355f, 155.65382f, 289.25302f, 154.4814f, 290.61844f, 153.85225f);
generalPath.curveTo(291.98383f, 153.22176f, 293.44046f, 153.23761f, 294.98828f, 153.8959f);
generalPath.curveTo(296.33517f, 154.46956f, 297.2961f, 155.25867f, 297.87503f, 156.26584f);
generalPath.curveTo(298.4474f, 157.27304f, 298.57822f, 158.37672f, 298.2557f, 159.57162f);
generalPath.lineTo(296.0021f, 158.61069f);
generalPath.curveTo(296.10403f, 157.99475f, 296.0096f, 157.45149f, 295.7125f, 156.98093f);
generalPath.curveTo(295.41385f, 156.51036f, 294.92737f, 156.12837f, 294.25192f, 155.84155f);
generalPath.curveTo(293.35443f, 155.45824f, 292.50717f, 155.48598f, 291.71277f, 155.92624f);
generalPath.curveTo(290.91577f, 156.3664f, 290.2747f, 157.1674f, 289.77902f, 158.32924f);
generalPath.curveTo(289.2807f, 159.49773f, 289.15778f, 160.52473f, 289.41147f, 161.4156f);
generalPath.curveTo(289.664f, 162.30383f, 290.25873f, 162.94756f, 291.1932f, 163.3454f);
generalPath.curveTo(291.8911f, 163.64409f, 292.56656f, 163.68639f, 293.21292f, 163.47485f);
generalPath.curveTo(293.8606f, 163.26073f, 294.39722f, 162.81793f, 294.8123f, 162.14647f);
generalPath.lineTo(292.37756f, 161.11018f);
generalPath.lineTo(293.1614f, 159.2716f);
generalPath.lineTo(297.61185f, 161.16835f);
generalPath.lineTo(295.13217f, 166.98154f);
generalPath.lineTo(293.65176f, 166.34973f);
generalPath.lineTo(293.92154f, 164.96712f);
generalPath.closePath();
generalPath.moveTo(305.02707f, 164.65913f);
generalPath.lineTo(307.5649f, 165.90294f);
generalPath.curveTo(308.11475f, 166.17125f, 308.57343f, 166.25716f, 308.94354f, 166.16069f);
generalPath.curveTo(309.3096f, 166.0629f, 309.6097f, 165.77473f, 309.84628f, 165.29361f);
generalPath.curveTo(310.06958f, 164.83891f, 310.1108f, 164.43314f, 309.97855f, 164.0736f);
generalPath.curveTo(309.84103f, 163.7154f, 309.5212f, 163.41403f, 309.02554f, 163.1708f);
generalPath.lineTo(306.3899f, 161.87944f);
generalPath.lineTo(305.0271f, 164.65913f);
generalPath.closePath();
generalPath.moveTo(299.97922f, 169.5048f);
generalPath.lineTo(305.11435f, 159.01515f);
generalPath.lineTo(310.2032f, 161.50671f);
generalPath.curveTo(311.34518f, 162.06583f, 312.07486f, 162.72673f, 312.38416f, 163.48543f);
generalPath.curveTo(312.6959f, 164.24414f, 312.60483f, 165.12709f, 312.11176f, 166.13295f);
generalPath.curveTo(311.80002f, 166.7727f, 311.40195f, 167.24194f, 310.9235f, 167.53801f);
generalPath.curveTo(310.4463f, 167.8354f, 309.9176f, 167.9372f, 309.34f, 167.846f);
generalPath.curveTo(309.7167f, 168.23195f, 309.91895f, 168.63641f, 309.95065f, 169.06467f);
generalPath.curveTo(309.98062f, 169.4916f, 309.83563f, 170.06259f, 309.5211f, 170.78035f);
generalPath.lineTo(308.958f, 172.04f);
generalPath.curveTo(308.9505f, 172.04936f, 308.94678f, 172.0636f, 308.93927f, 172.08232f);
generalPath.curveTo(308.64328f, 172.71413f, 308.59155f, 173.16222f, 308.78864f, 173.41995f);
generalPath.lineTo(308.63016f, 173.7398f);
generalPath.lineTo(306.2615f, 172.58192f);
generalPath.curveTo(306.25964f, 172.39682f, 306.29147f, 172.18935f, 306.35556f, 171.95276f);
generalPath.curveTo(306.423f, 171.7175f, 306.5219f, 171.44652f, 306.6624f, 171.14383f);
generalPath.lineTo(307.1713f, 170.0256f);
generalPath.curveTo(307.4673f, 169.3647f, 307.56122f, 168.86508f, 307.44894f, 168.53065f);
generalPath.curveTo(307.34067f, 168.1949f, 306.9982f, 167.88562f, 306.42328f, 167.6041f);
generalPath.lineTo(304.1379f, 166.48323f);
generalPath.lineTo(302.13937f, 170.56224f);
generalPath.lineTo(299.98093f, 169.50482f);
generalPath.closePath();
generalPath.moveTo(313.03046f, 176.20755f);
generalPath.lineTo(318.70886f, 166.00075f);
generalPath.lineTo(326.12274f, 170.1247f);
generalPath.lineTo(325.13535f, 171.89984f);
generalPath.lineTo(319.79538f, 168.92848f);
generalPath.lineTo(318.58463f, 171.10413f);
generalPath.lineTo(323.4686f, 173.8217f);
generalPath.lineTo(322.4971f, 175.56778f);
generalPath.lineTo(317.61313f, 172.8502f);
generalPath.lineTo(316.15387f, 175.47525f);
generalPath.lineTo(321.7437f, 178.58673f);
generalPath.lineTo(320.69553f, 180.47159f);
generalPath.lineTo(313.03052f, 176.20752f);
generalPath.closePath();
generalPath.moveTo(326.20996f, 179.82791f);
generalPath.lineTo(328.24023f, 181.05186f);
generalPath.curveTo(327.9877f, 181.64536f, 327.96783f, 182.18727f, 328.17804f, 182.67633f);
generalPath.curveTo(328.38937f, 183.16406f, 328.84552f, 183.61876f, 329.54742f, 184.04173f);
generalPath.curveTo(330.14752f, 184.4039f, 330.66037f, 184.56647f, 331.09125f, 184.5374f);
generalPath.curveTo(331.5235f, 184.50967f, 331.86316f, 184.28494f, 332.11563f, 183.86858f);
generalPath.curveTo(332.47906f, 183.26056f, 331.91217f, 182.23486f, 330.4066f, 180.78485f);
generalPath.curveTo(330.38785f, 180.76613f, 330.3691f, 180.74925f, 330.35526f, 180.73465f);
generalPath.curveTo(330.31555f, 180.69756f, 330.25595f, 180.63948f, 330.17392f, 180.55893f);
generalPath.curveTo(329.36237f, 179.79097f, 328.83365f, 179.16313f, 328.59177f, 178.6701f);
generalPath.curveTo(328.37372f, 178.23128f, 328.28903f, 177.76204f, 328.33398f, 177.26901f);
generalPath.curveTo(328.37894f, 176.77466f, 328.55728f, 176.26842f, 328.87195f, 175.7463f);
generalPath.curveTo(329.4588f, 174.77347f, 330.23996f, 174.22758f, 331.2141f, 174.10599f);
generalPath.curveTo(332.18564f, 173.98703f, 333.2973f, 174.3029f, 334.54636f, 175.05634f);
generalPath.curveTo(335.71478f, 175.76083f, 336.46295f, 176.58829f, 336.7881f, 177.53601f);
generalPath.curveTo(337.11182f, 178.48637f, 336.99042f, 179.47243f, 336.42053f, 180.50209f);
generalPath.lineTo(334.44443f, 179.30984f);
generalPath.curveTo(334.6996f, 178.80229f, 334.7378f, 178.32248f, 334.55423f, 177.86908f);
generalPath.curveTo(334.37064f, 177.41307f, 333.97003f, 176.99803f, 333.35144f, 176.6253f);
generalPath.curveTo(332.81082f, 176.30011f, 332.335f, 176.15341f, 331.9133f, 176.18912f);
generalPath.curveTo(331.4943f, 176.22357f, 331.17047f, 176.43498f, 330.9339f, 176.82489f);
generalPath.curveTo(330.61542f, 177.35493f, 330.94513f, 178.09119f, 331.92126f, 179.0402f);
generalPath.curveTo(332.1858f, 179.29796f, 332.3918f, 179.50018f, 332.53854f, 179.64426f);
generalPath.curveTo(333.15714f, 180.26814f, 333.588f, 180.7281f, 333.83124f, 181.01361f);
generalPath.curveTo(334.0744f, 181.30174f, 334.2701f, 181.57935f, 334.42075f, 181.84367f);
generalPath.curveTo(334.68903f, 182.31424f, 334.81598f, 182.79932f, 334.79205f, 183.29498f);
generalPath.curveTo(334.77332f, 183.79065f, 334.6032f, 184.30217f, 334.28848f, 184.82164f);
generalPath.curveTo(333.6606f, 185.86055f, 332.82657f, 186.45534f, 331.7771f, 186.60736f);
generalPath.curveTo(330.73157f, 186.75667f, 329.57104f, 186.44868f, 328.29422f, 185.67816f);
generalPath.curveTo(327.03455f, 184.91945f, 326.2243f, 184.03253f, 325.86078f, 183.01741f);
generalPath.curveTo(325.49847f, 181.99965f, 325.615f, 180.93825f, 326.2096f, 179.82797f);
generalPath.closePath();
generalPath.moveTo(338.5197f, 187.52596f);
generalPath.lineTo(340.4839f, 188.85568f);
generalPath.curveTo(340.19836f, 189.4333f, 340.1508f, 189.9739f, 340.3359f, 190.47487f);
generalPath.curveTo(340.5225f, 190.97318f, 340.9532f, 191.449f, 341.6313f, 191.91031f);
generalPath.curveTo(342.2102f, 192.30289f, 342.71512f, 192.49321f, 343.14603f, 192.4866f);
generalPath.curveTo(343.57956f, 192.48099f, 343.92987f, 192.27646f, 344.2048f, 191.87198f);
generalPath.curveTo(344.60263f, 191.2838f, 344.08566f, 190.23032f, 342.66226f, 188.70367f);
generalPath.curveTo(342.64352f, 188.68233f, 342.6248f, 188.66658f, 342.6132f, 188.64822f);
generalPath.curveTo(342.5735f, 188.61113f, 342.5165f, 188.54912f, 342.4401f, 188.46445f);
generalPath.curveTo(341.67084f, 187.65683f, 341.17514f, 187.00253f, 340.9597f, 186.49632f);
generalPath.curveTo(340.7641f, 186.04427f, 340.70453f, 185.57372f, 340.7731f, 185.08333f);
generalPath.curveTo(340.84467f, 184.59294f, 341.05075f, 184.09596f, 341.39038f, 183.59102f);
generalPath.curveTo(342.0275f, 182.65125f, 342.83905f, 182.145f, 343.81583f, 182.07626f);
generalPath.curveTo(344.79132f, 182.00882f, 345.88574f, 182.38295f, 347.09518f, 183.19977f);
generalPath.curveTo(348.224f, 183.9664f, 348.9285f, 184.8282f, 349.20343f, 185.79309f);
generalPath.curveTo(349.47583f, 186.76064f, 349.30536f, 187.74007f, 348.68134f, 188.73802f);
generalPath.lineTo(346.77002f, 187.44398f);
generalPath.curveTo(347.0503f, 186.94963f, 347.1125f, 186.47249f, 346.95624f, 186.00984f);
generalPath.curveTo(346.79623f, 185.5459f, 346.41962f, 185.10973f, 345.8195f, 184.70656f);
generalPath.curveTo(345.2974f, 184.35104f, 344.82684f, 184.18182f, 344.40656f, 184.19238f);
generalPath.curveTo(343.98752f, 184.20737f, 343.65048f, 184.40123f, 343.3967f, 184.77528f);
generalPath.curveTo(343.04636f, 185.28815f, 343.34012f, 186.04155f, 344.2638f, 187.04214f);
generalPath.curveTo(344.5137f, 187.31177f, 344.7132f, 187.5246f, 344.84933f, 187.67528f);
generalPath.curveTo(345.43353f, 188.3322f, 345.84067f, 188.81068f, 346.06665f, 189.11206f);
generalPath.curveTo(346.2952f, 189.41211f, 346.47644f, 189.69894f, 346.61124f, 189.96858f);
generalPath.curveTo(346.8559f, 190.45502f, 346.95633f, 190.94275f, 346.90988f, 191.4384f);
generalPath.curveTo(346.8623f, 191.93275f, 346.66934f, 192.43369f, 346.32825f, 192.93732f);
generalPath.curveTo(345.64493f, 193.94319f, 344.78046f, 194.49435f, 343.72568f, 194.58823f);
generalPath.curveTo(342.67224f, 194.68208f, 341.5289f, 194.3133f, 340.29172f, 193.47662f);
generalPath.curveTo(339.07703f, 192.65446f, 338.31033f, 191.72658f, 338.00372f, 190.69295f);
generalPath.curveTo(337.6946f, 189.65533f, 337.86508f, 188.60321f, 338.5192f, 187.52594f);
generalPath.closePath();
generalPath.moveTo(353.36066f, 195.40378f);
generalPath.curveTo(352.58478f, 196.42683f, 352.21335f, 197.39967f, 352.23715f, 198.30905f);
generalPath.curveTo(352.2634f, 199.22108f, 352.68655f, 199.98639f, 353.50473f, 200.605f);
generalPath.curveTo(354.33218f, 201.2302f, 355.19f, 201.43375f, 356.07825f, 201.21565f);
generalPath.curveTo(356.9678f, 200.99492f, 357.79922f, 200.3684f, 358.5751f, 199.3427f);
generalPath.curveTo(359.34836f, 198.32228f, 359.7198f, 197.3521f, 359.69067f, 196.43478f);
generalPath.curveTo(359.6607f, 195.5201f, 359.22937f, 194.75217f, 358.40323f, 194.12695f);
generalPath.curveTo(357.5864f, 193.5057f, 356.7325f, 193.30878f, 355.8522f, 193.52951f);
generalPath.curveTo(354.968f, 193.75157f, 354.13788f, 194.37544f, 353.36066f, 195.40381f);
generalPath.closePath();
generalPath.moveTo(351.40707f, 193.92603f);
generalPath.curveTo(352.5412f, 192.42053f, 353.8577f, 191.53758f, 355.3447f, 191.27455f);
generalPath.curveTo(356.833f, 191.01022f, 358.27377f, 191.404f, 359.66693f, 192.45886f);
generalPath.curveTo(361.05878f, 193.50966f, 361.82938f, 194.78915f, 361.98538f, 196.29599f);
generalPath.curveTo(362.14124f, 197.8068f, 361.6508f, 199.31096f, 360.5182f, 200.81117f);
generalPath.curveTo(359.38013f, 202.31404f, 358.06628f, 203.19698f, 356.57404f, 203.46004f);
generalPath.curveTo(355.07907f, 203.72041f, 353.63568f, 203.32516f, 352.24783f, 202.27573f);
generalPath.curveTo(350.85336f, 201.22095f, 350.08273f, 199.9441f, 349.9321f, 198.43991f);
generalPath.curveTo(349.78146f, 196.93571f, 350.27194f, 195.42888f, 351.4072f, 193.92601f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 156, 55, 255)) : new Color(0, 156, 55, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());

}



    @SuppressWarnings("unused")
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
_paint1(g, origAlpha);


	    shape = null;
	    generalPath = null;
	    paint = null;
	    stroke = null;
	    clip = null;
        transformsStack.clear();
	}

    /**
     * Returns the X of the bounding box of the original SVG image.
     * 
     * @return The X of the bounding box of the original SVG image.
     */
    public static double getOrigX() {
        return 4.57763671875E-5;
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
		return 749.3563232421875;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 524.5494995117188;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private FlagBR() {
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
       FlagBR base = new FlagBR();
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
       FlagBR base = new FlagBR();
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
        return FlagBR::new;
    }
}

