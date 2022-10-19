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
public class Duke implements RadianceIcon {
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -0.0f, 1.52587890625E-5f));
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
generalPath.moveTo(458.259f, 307.339f);
generalPath.curveTo(468.798f, 300.92398f, 476.217f, 289.98898f, 475.618f, 280.24298f);
generalPath.lineTo(531.94104f, 216.71198f);
generalPath.curveTo(540.91205f, 217.49597f, 549.67804f, 213.97197f, 553.95306f, 214.19098f);
generalPath.curveTo(562.53503f, 214.94897f, 561.97205f, 212.80898f, 571.4961f, 215.54898f);
generalPath.curveTo(581.0491f, 218.46498f, 579.5061f, 220.04298f, 584.7581f, 220.79999f);
generalPath.curveTo(590.2221f, 221.58398f, 598.2141f, 217.874f, 599.37714f, 214.385f);
generalPath.curveTo(602.4871f, 204.82399f, 600.9451f, 207.93599f, 598.02716f, 202.692f);
generalPath.curveTo(605.4122f, 202.692f, 612.63715f, 195.079f, 611.2632f, 187.095f);
generalPath.curveTo(609.71216f, 178.892f, 606.7872f, 175.209f, 599.7542f, 171.693f);
generalPath.curveTo(602.89124f, 169.146f, 610.28424f, 161.163f, 603.8692f, 151.82199f);
generalPath.curveTo(596.2652f, 141.10799f, 590.4142f, 148.11299f, 579.5052f, 145.19598f);
generalPath.curveTo(568.19116f, 142.27098f, 568.19116f, 145.19598f, 565.45917f, 136.01398f);
generalPath.curveTo(562.9382f, 126.86798f, 570.51715f, 130.38298f, 567.9972f, 119.06998f);
generalPath.curveTo(561.3722f, 89.45398f, 536.8222f, 94.89898f, 529.80817f, 100.55698f);
generalPath.curveTo(526.8832f, 103.101974f, 528.2402f, 106.39898f, 530.1872f, 109.516975f);
generalPath.curveTo(533.7032f, 115.95798f, 537.9862f, 139.91698f, 536.8232f, 150.44698f);
generalPath.curveTo(535.4392f, 162.73097f, 531.35016f, 160.59798f, 525.69415f, 173.45398f);
generalPath.curveTo(520.0632f, 186.49498f, 522.5831f, 199.76498f, 521.79016f, 208.12698f);
generalPath.lineTo(475.01917f, 268.95297f);
generalPath.curveTo(473.07217f, 259.99298f, 457.69617f, 259.58698f, 444.61917f, 259.39297f);
generalPath.lineTo(458.259f, 307.339f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
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
generalPath.moveTo(182.483f, 233.868f);
generalPath.curveTo(162.613f, 230.758f, 155.604f, 235.41899f, 153.84601f, 241.86f);
generalPath.curveTo(140.77f, 240.098f, 99.46101f, 252.769f, 92.046005f, 258.426f);
generalPath.curveTo(84.657005f, 264.082f, 81.54301f, 288.44598f, 80.353004f, 294.862f);
generalPath.curveTo(79.189f, 301.303f, 77.428f, 290.579f, 70.41801f, 297.593f);
generalPath.curveTo(63.598007f, 304.607f, 71.98601f, 303.849f, 67.68201f, 307.524f);
generalPath.curveTo(63.598007f, 311.233f, 64.57101f, 304.819f, 57.372005f, 310.44998f);
generalPath.curveTo(49.952003f, 315.922f, 54.446007f, 314.75897f, 48.411003f, 321.17297f);
generalPath.curveTo(42.159004f, 327.8f, 10.786003f, 313.77997f, 5.534004f, 314.75897f);
generalPath.curveTo(0.445004f, 315.54196f, -3.6439962f, 332.11f, 5.128004f, 335.41296f);
generalPath.curveTo(14.085004f, 338.70895f, 35.744003f, 343.01697f, 43.132004f, 344.36496f);
generalPath.curveTo(37.096004f, 349.64294f, 17.226004f, 356.25296f, 13.112003f, 361.33698f);
generalPath.curveTo(8.834003f, 366.589f, 4.7490034f, 374.201f, 7.075003f, 376.93396f);
generalPath.curveTo(9.212004f, 379.85895f, 18.389004f, 382.18497f, 30.082003f, 382.18497f);
generalPath.curveTo(41.779003f, 382.18497f, 49.952003f, 363.85797f, 58.345f, 368.16595f);
generalPath.curveTo(66.707f, 372.24594f, 60.292f, 399.72797f, 60.103f, 405.76495f);
generalPath.curveTo(60.103f, 411.80096f, 58.724f, 417.64294f, 68.47f, 417.64294f);
generalPath.curveTo(78.4f, 417.64294f, 78.022f, 415.91595f, 82.3f, 404.97995f);
generalPath.curveTo(86.415f, 394.07196f, 80.758f, 370.87195f, 89.341f, 364.64194f);
generalPath.curveTo(97.703f, 358.60495f, 95.37701f, 357.23093f, 102.981f, 360.55194f);
generalPath.curveTo(110.374f, 363.67194f, 103.955f, 366.78195f, 112.917f, 371.27695f);
generalPath.curveTo(136.113f, 382.96893f, 145.264f, 364.64194f, 145.643f, 348.26895f);
generalPath.curveTo(145.643f, 340.27795f, 138.254f, 339.68695f, 135.32901f, 339.68695f);
generalPath.curveTo(132.598f, 339.68695f, 130.26701f, 343.98795f, 125.58301f, 343.98795f);
generalPath.curveTo(120.71501f, 343.98795f, 116.031006f, 340.27795f, 109.017006f, 333.65094f);
generalPath.curveTo(102.19701f, 327.01593f, 98.677f, 324.68994f, 98.677f, 321.17294f);
generalPath.curveTo(98.677f, 317.86896f, 100.629005f, 322.14294f, 101.034004f, 314.15994f);
generalPath.curveTo(101.412f, 306.17593f, 95.37701f, 308.12296f, 95.94501f, 299.54193f);
generalPath.curveTo(96.351006f, 290.95892f, 103.739006f, 268.35593f, 107.07001f, 262.13693f);
generalPath.curveTo(113.701004f, 258.80493f, 145.85901f, 246.53894f, 153.65701f, 246.91794f);
generalPath.curveTo(155.60402f, 251.79094f, 156.95702f, 255.50095f, 172.55002f, 264.08295f);
generalPath.lineTo(182.483f, 233.868f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(333.532f, 9.146f);
generalPath.curveTo(299.61102f, 41.299f, 247.58401f, 108.946f, 227.50302f, 147.144f);
generalPath.curveTo(207.25302f, 185.333f, 177.80002f, 249.64899f, 165.72803f, 293.319f);
generalPath.curveTo(153.84703f, 337.16602f, 137.84903f, 395.825f, 136.87503f, 423.898f);
generalPath.curveTo(135.89703f, 451.97202f, 136.87503f, 472.028f, 156.95703f, 471.049f);
generalPath.curveTo(177.23303f, 470.079f, 172.55003f, 459.95502f, 220.67804f, 436.375f);
generalPath.curveTo(268.83804f, 412.583f, 294.73904f, 405.948f, 347.17303f, 418.241f);
generalPath.curveTo(399.61005f, 430.524f, 412.46603f, 476.52f, 432.14203f, 474.574f);
generalPath.curveTo(451.84503f, 472.617f, 464.71002f, 434.022f, 464.51703f, 366.588f);
generalPath.curveTo(464.51703f, 299.161f, 415.38303f, 192.15302f, 404.10403f, 163.89502f);
generalPath.curveTo(392.974f, 135.831f, 353.614f, 47.336f, 333.532f, 9.146f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.239f,0,0,2.613f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(333.532f, 9.146f);
generalPath.curveTo(299.61102f, 41.299f, 247.58401f, 108.946f, 227.50302f, 147.144f);
generalPath.curveTo(207.25302f, 185.333f, 177.80002f, 249.64899f, 165.72803f, 293.319f);
generalPath.curveTo(153.84703f, 337.16602f, 137.84903f, 395.825f, 136.87503f, 423.898f);
generalPath.curveTo(135.89703f, 451.97202f, 136.87503f, 472.028f, 156.95703f, 471.049f);
generalPath.curveTo(177.23303f, 470.079f, 172.55003f, 459.95502f, 220.67804f, 436.375f);
generalPath.curveTo(268.83804f, 412.583f, 294.73904f, 405.948f, 347.17303f, 418.241f);
generalPath.curveTo(399.61005f, 430.524f, 412.46603f, 476.52f, 432.14203f, 474.574f);
generalPath.curveTo(451.84503f, 472.617f, 464.71002f, 434.022f, 464.51703f, 366.588f);
generalPath.curveTo(464.51703f, 299.161f, 415.38303f, 192.15302f, 404.10403f, 163.89502f);
generalPath.curveTo(392.974f, 135.831f, 353.614f, 47.336f, 333.532f, 9.146f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(333.532f, 13.447f);
generalPath.curveTo(266.101f, 69.18f, 203.921f, 177.351f, 174.879f, 262.92f);
generalPath.curveTo(231.996f, 229.19002f, 400.773f, 240.50401f, 447.73898f, 270.903f);
generalPath.curveTo(438.777f, 231.515f, 358.671f, 49.099f, 333.532f, 13.447f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(306.841f, 165.059f);
generalPath.curveTo(258.492f, 164.09001f, 226.14401f, 196.43701f, 226.334f, 239.314f);
generalPath.curveTo(226.334f, 282.192f, 261.039f, 306.767f, 302.348f, 304.608f);
generalPath.curveTo(343.682f, 302.466f, 388.508f, 275.371f, 386.34f, 234.847f);
generalPath.curveTo(384.013f, 194.295f, 355.164f, 166.037f, 306.841f, 165.059f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(169, 5, 51, 255)) : new Color(169, 5, 51, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_1
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(274.855f, 206.896f);
generalPath.lineTo(297.545f, 206.896f);
generalPath.curveTo(297.545f, 194.356f, 287.384f, 184.18799f, 274.855f, 184.18799f);
generalPath.curveTo(262.33002f, 184.18799f, 252.16202f, 194.35599f, 252.16202f, 206.896f);
generalPath.curveTo(252.16202f, 219.41699f, 262.33002f, 229.58499f, 274.855f, 229.58499f);
generalPath.curveTo(287.385f, 229.58499f, 297.545f, 219.41699f, 297.545f, 206.896f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_5
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.745f,0,1,2.613f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(306.841f, 165.059f);
generalPath.curveTo(258.492f, 164.09001f, 226.14401f, 196.43701f, 226.334f, 239.314f);
generalPath.curveTo(226.334f, 282.192f, 261.039f, 306.767f, 302.348f, 304.608f);
generalPath.curveTo(343.682f, 302.466f, 388.508f, 275.371f, 386.34f, 234.847f);
generalPath.curveTo(384.013f, 194.295f, 355.164f, 166.037f, 306.841f, 165.059f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_6
paint = (colorFilter != null) ? colorFilter.filter(new Color(128, 130, 133, 255)) : new Color(128, 130, 133, 255);
stroke = new BasicStroke(0.498f,1,1,2.613f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(594.698f, 147.708f);
generalPath.curveTo(584.564f, 148.49199f, 553.954f, 149.09099f, 561.751f, 167.42f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_7
paint = (colorFilter != null) ? colorFilter.filter(new Color(128, 130, 133, 255)) : new Color(128, 130, 133, 255);
stroke = new BasicStroke(0.389f,1,1,2.613f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(598.214f, 171.694f);
generalPath.curveTo(566.623f, 169.147f, 553.195f, 177.545f, 564.105f, 190.587f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_8
paint = (colorFilter != null) ? colorFilter.filter(new Color(128, 130, 133, 255)) : new Color(128, 130, 133, 255);
stroke = new BasicStroke(0.444f,1,1,2.613f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(596.073f, 201.9f);
generalPath.curveTo(585.358f, 197.221f, 556.685f, 190.401f, 560.211f, 209.698f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
        return 0.0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 8.1336088180542;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 611.4310302734375;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 468.38641357421875;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private Duke() {
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
       Duke base = new Duke();
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
       Duke base = new Duke();
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
        return Duke::new;
    }
}

