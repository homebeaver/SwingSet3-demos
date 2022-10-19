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
public class Duke_waving implements RadianceIcon {
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, -0.0f, -0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(48.859f, 43.518f);
generalPath.curveTo(57.283f, 61.158f, 51.595f, 184.35f, 41.731003f, 227.55f);
generalPath.curveTo(31.867002f, 270.822f, 22.003002f, 325.83002f, 19.699003f, 372.126f);
generalPath.curveTo(18.691004f, 391.854f, 21.715004f, 399.63f, 34.603004f, 399.63f);
generalPath.curveTo(57.355003f, 399.63f, 86.227005f, 351.678f, 122.443f, 352.758f);
generalPath.curveTo(158.731f, 353.83798f, 170.251f, 407.766f, 187.24301f, 407.406f);
generalPath.curveTo(204.23502f, 407.04602f, 217.91501f, 401.142f, 218.059f, 348.654f);
generalPath.curveTo(218.563f, 191.981f, 87.235f, 64.973f, 48.859f, 43.518f);
generalPath.lineTo(48.859f, 43.518f);
generalPath.lineTo(48.859f, 43.518f);
generalPath.lineTo(48.859f, 43.518f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
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
generalPath.moveTo(162.763f, 168.726f);
generalPath.curveTo(170.755f, 182.19f, 191.131f, 171.82199f, 191.995f, 156.63f);
generalPath.curveTo(192.859f, 141.438f, 190.627f, 122.286f, 180.763f, 121.56601f);
generalPath.curveTo(170.899f, 120.84601f, 163.843f, 110.98201f, 153.979f, 110.26201f);
generalPath.curveTo(144.115f, 109.54201f, 133.531f, 119.406006f, 128.923f, 106.30201f);
generalPath.curveTo(124.315f, 93.19801f, 141.307f, 87.65401f, 153.979f, 85.56601f);
generalPath.curveTo(142.675f, 74.26201f, 136.05101f, 61.73401f, 131.08301f, 48.70201f);
generalPath.curveTo(126.115005f, 35.670013f, 122.44301f, 23.718012f, 136.339f, 18.60601f);
generalPath.curveTo(150.235f, 13.494011f, 149.08301f, 39.77401f, 164.99501f, 51.79801f);
generalPath.curveTo(160.31502f, 34.086014f, 158.587f, 26.742012f, 158.947f, 16.44601f);
generalPath.curveTo(159.307f, 6.150009f, 158.587f, -1.6979885f, 173.203f, 0.31801033f);
generalPath.curveTo(187.819f, 2.3340104f, 181.483f, 32.71801f, 192.85901f, 45.102013f);
generalPath.curveTo(196.315f, 33.366013f, 198.40302f, 18.462013f, 206.755f, 12.342014f);
generalPath.curveTo(215.107f, 6.2220154f, 234.115f, 6.0780144f, 222.01901f, 31.206015f);
generalPath.curveTo(209.92302f, 56.334015f, 225.54701f, 69.94202f, 221.87502f, 89.74201f);
generalPath.curveTo(218.20302f, 109.54201f, 206.82701f, 105.94202f, 201.93102f, 118.68601f);
generalPath.curveTo(197.03502f, 131.43001f, 204.81102f, 160.44601f, 195.59502f, 173.47801f);
generalPath.curveTo(186.37901f, 186.51001f, 184.72302f, 206.52602f, 190.69902f, 222.51001f);
generalPath.curveTo(172.339f, 205.374f, 162.763f, 168.726f, 162.763f, 168.726f);
generalPath.lineTo(162.763f, 168.726f);
generalPath.lineTo(162.763f, 168.726f);
generalPath.lineTo(162.763f, 168.726f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(48.355f, 185.646f);
generalPath.curveTo(40.939f, 236.478f, 15.162998f, 242.526f, 13.867001f, 263.622f);
generalPath.curveTo(12.571001f, 284.71802f, 20.707f, 286.734f, 20.203001f, 306.246f);
generalPath.curveTo(19.699001f, 325.758f, 2.3470001f, 333.678f, 0.25900078f, 344.262f);
generalPath.curveTo(-1.8289986f, 354.84598f, 9.187001f, 358.22998f, 15.595001f, 358.22998f);
generalPath.curveTo(22.003002f, 358.22998f, 28.411001f, 330.15f, 31.003002f, 312.29398f);
generalPath.curveTo(33.595f, 294.43796f, 22.507002f, 283.92596f, 22.507002f, 271.68597f);
generalPath.curveTo(22.507002f, 259.44598f, 38.563004f, 237.05397f, 35.611f, 256.70996f);
generalPath.curveTo(48.931f, 235.686f, 55.268f, 208.901f, 48.355f, 185.646f);
generalPath.lineTo(48.355f, 185.646f);
generalPath.lineTo(48.355f, 185.646f);
generalPath.lineTo(48.355f, 185.646f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
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
generalPath.moveTo(58.292f, 205.013f);
generalPath.curveTo(52.676f, 232.517f, 17.612f, 386.09302f, 35.468002f, 388.037f);
generalPath.curveTo(53.324005f, 389.981f, 78.740005f, 340.517f, 120.356f, 340.87698f);
generalPath.curveTo(162.044f, 341.23697f, 178.46f, 396.317f, 188.612f, 395.95697f);
generalPath.curveTo(198.764f, 395.597f, 205.45999f, 399.55698f, 206.54f, 337.49298f);
generalPath.curveTo(207.62f, 275.429f, 169.74799f, 196.15698f, 147.35599f, 161.23698f);
generalPath.curveTo(116.971f, 163.181f, 84.283f, 187.518f, 58.292f, 205.013f);
generalPath.lineTo(58.292f, 205.013f);
generalPath.lineTo(58.292f, 205.013f);
generalPath.lineTo(58.292f, 205.013f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
generalPath.moveTo(147.082f, 171.533f);
generalPath.curveTo(145.42f, 154.33801f, 132.675f, 143.545f, 116.187f, 140.906f);
generalPath.curveTo(100.263f, 138.35701f, 82.927f, 145.904f, 71.77899f, 157.052f);
generalPath.curveTo(59.24099f, 169.59f, 58.73999f, 187.03f, 67.51899f, 201.88501f);
generalPath.curveTo(76.17999f, 216.542f, 95.36599f, 221.38602f, 111.081985f, 217.72002f);
generalPath.curveTo(132.588f, 212.705f, 148.48f, 193.896f, 147.082f, 171.533f);
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_2
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(139.162f, 177.941f);
generalPath.curveTo(137.669f, 193.568f, 125.215004f, 206.12299f, 110.218f, 209.765f);
generalPath.curveTo(94.348f, 213.619f, 76.825f, 207.508f, 71.122f, 191.189f);
generalPath.curveTo(68.21f, 182.857f, 68.752f, 174.31f, 73.759f, 166.991f);
generalPath.curveTo(77.982f, 160.81999f, 84.792f, 155.991f, 91.538f, 152.925f);
generalPath.curveTo(105.462006f, 146.599f, 125.37f, 145.896f, 135.069f, 160.002f);
generalPath.curveTo(138.744f, 165.348f, 139.387f, 171.641f, 139.162f, 177.941f);
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(86.62989807128906, 167.06930541992188), 54.7155f, new Point2D.Double(86.62989807128906, 167.06930541992188), new float[] {0.0f,0.0604f,0.0712f,0.1829f,0.2995f,0.4199f,0.5453f,0.6778f,0.822f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(251, 200, 180, 255)) : new Color(251, 200, 180, 255)),((colorFilter != null) ? colorFilter.filter(new Color(251, 195, 176, 255)) : new Color(251, 195, 176, 255)),((colorFilter != null) ? colorFilter.filter(new Color(247, 151, 139, 255)) : new Color(247, 151, 139, 255)),((colorFilter != null) ? colorFilter.filter(new Color(244, 113, 107, 255)) : new Color(244, 113, 107, 255)),((colorFilter != null) ? colorFilter.filter(new Color(241, 82, 81, 255)) : new Color(241, 82, 81, 255)),((colorFilter != null) ? colorFilter.filter(new Color(239, 58, 61, 255)) : new Color(239, 58, 61, 255)),((colorFilter != null) ? colorFilter.filter(new Color(238, 41, 47, 255)) : new Color(238, 41, 47, 255)),((colorFilter != null) ? colorFilter.filter(new Color(237, 31, 39, 255)) : new Color(237, 31, 39, 255)),((colorFilter != null) ? colorFilter.filter(new Color(237, 28, 36, 255)) : new Color(237, 28, 36, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(139.162f, 177.941f);
generalPath.curveTo(137.669f, 193.568f, 125.215004f, 206.12299f, 110.218f, 209.765f);
generalPath.curveTo(94.348f, 213.619f, 76.825f, 207.508f, 71.122f, 191.189f);
generalPath.curveTo(68.21f, 182.857f, 68.752f, 174.31f, 73.759f, 166.991f);
generalPath.curveTo(77.982f, 160.81999f, 84.792f, 155.991f, 91.538f, 152.925f);
generalPath.curveTo(105.462006f, 146.599f, 125.37f, 145.896f, 135.069f, 160.002f);
generalPath.curveTo(138.744f, 165.348f, 139.387f, 171.641f, 139.162f, 177.941f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
        return 0.0;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 225.93800354003906;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 407.4070129394531;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private Duke_waving() {
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
       Duke_waving base = new Duke_waving();
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
       Duke_waving base = new Duke_waving();
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
        return Duke_waving::new;
    }
}

