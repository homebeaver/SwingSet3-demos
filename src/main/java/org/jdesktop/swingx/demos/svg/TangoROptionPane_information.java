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
public class TangoROptionPane_information implements RadianceIcon {
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
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 0.6f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0706000328063965f, 0.0f, 0.0f, 0.5249999761581421f, -0.8927599787712097f, 22.5f));
// _0_0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(41.0f, 40.0f);
generalPath.curveTo(41.027473f, 43.0714f, 37.766056f, 45.915382f, 32.450706f, 47.45506f);
generalPath.curveTo(27.13536f, 48.994736f, 20.57864f, 48.994736f, 15.263292f, 47.45506f);
generalPath.curveTo(9.947945f, 45.915382f, 6.6865287f, 43.0714f, 6.7140007f, 40.0f);
generalPath.curveTo(6.6865287f, 36.9286f, 9.947945f, 34.084618f, 15.263292f, 32.54494f);
generalPath.curveTo(20.57864f, 31.005262f, 27.13536f, 31.005262f, 32.450706f, 32.54494f);
generalPath.curveTo(37.766056f, 34.084618f, 41.027473f, 36.9286f, 41.0f, 40.0f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(23.85700035095215, 40.0), 17.143f, new Point2D.Double(23.85700035095215, 40.0), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 20.0f));
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(0.920490026473999f, 0.0f, 0.0f, 0.920490026473999f, 2.368499994277954f, 0.9740800261497498f));
// _0_1_0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(46.857f, 23.929f);
generalPath.curveTo(46.857f, 36.829002f, 36.399998f, 47.286003f, 23.499998f, 47.286003f);
generalPath.curveTo(10.599998f, 47.286003f, 0.14299774f, 36.829002f, 0.14299774f, 23.929003f);
generalPath.curveTo(0.14299774f, 11.029003f, 10.5999975f, 0.5720024f, 23.499998f, 0.5720024f);
generalPath.curveTo(36.399998f, 0.5720024f, 46.857f, 11.029002f, 46.857f, 23.929003f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(52, 101, 164, 255)) : new Color(52, 101, 164, 255);
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(32, 74, 135, 255)) : new Color(32, 74, 135, 255);
stroke = new BasicStroke(1.0864f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(46.857f, 23.929f);
generalPath.curveTo(46.857f, 36.829002f, 36.399998f, 47.286003f, 23.499998f, 47.286003f);
generalPath.curveTo(10.599998f, 47.286003f, 0.14299774f, 36.829002f, 0.14299774f, 23.929003f);
generalPath.curveTo(0.14299774f, 11.029003f, 10.5999975f, 0.5720024f, 23.499998f, 0.5720024f);
generalPath.curveTo(36.399998f, 0.5720024f, 46.857f, 11.029002f, 46.857f, 23.929003f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.34659f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(0.8544800281524658f, 0.0f, 0.0f, 0.8544800281524658f, 1.8600000143051147f, 0.24062000215053558f));
// _0_1_0_1
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(49.902f, 26.635f);
generalPath.curveTo(49.902f, 39.885002f, 39.161f, 50.626f, 25.911001f, 50.626f);
generalPath.curveTo(12.661003f, 50.626f, 1.920002f, 39.885f, 1.920002f, 26.635f);
generalPath.curveTo(1.920002f, 13.385002f, 12.661002f, 2.644001f, 25.911001f, 2.644001f);
generalPath.curveTo(39.161f, 2.644001f, 49.902f, 13.385001f, 49.902f, 26.635f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0);
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(43.93600082397461, 53.83599853515625), new Point2D.Double(20.065000534057617, -8.562700271606445), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 230, 155, 255)) : new Color(255, 230, 155, 255)),((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.1703f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(49.902f, 26.635f);
generalPath.curveTo(49.902f, 39.885002f, 39.161f, 50.626f, 25.911001f, 50.626f);
generalPath.curveTo(12.661003f, 50.626f, 1.920002f, 39.885f, 1.920002f, 26.635f);
generalPath.curveTo(1.920002f, 13.385002f, 12.661002f, 2.644001f, 25.911001f, 2.644001f);
generalPath.curveTo(39.161f, 2.644001f, 49.902f, 13.385001f, 49.902f, 26.635f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
g.setComposite(AlphaComposite.getInstance(3, 0.25f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(24.0f, 3.0f);
generalPath.curveTo(12.954f, 3.0f, 4.0f, 11.954f, 4.0f, 23.0f);
generalPath.curveTo(4.0f, 24.6861f, 4.23214f, 26.3108f, 4.625f, 27.875f);
generalPath.curveTo(7.829f, 32.6683f, 17.879f, 27.75486f, 24.844f, 22.2812f);
generalPath.curveTo(32.852f, 15.9881f, 42.685997f, 30.6405f, 43.969f, 23.7187f);
generalPath.curveTo(43.977f, 23.4797f, 44.0f, 23.2407f, 44.0f, 22.9997f);
generalPath.curveTo(44.0f, 11.953701f, 35.045998f, 2.9997005f, 24.0f, 2.9997005f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 2.895900011062622f, -3.6972999572753906f));
// _0_2_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_1_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(21.104f, 13.697f);
generalPath.curveTo(20.23977f, 13.716419f, 19.5286f, 14.00521f, 18.9704f, 14.563379f);
generalPath.curveTo(18.41221f, 15.12159f, 18.12341f, 15.832779f, 18.10402f, 16.69698f);
generalPath.curveTo(18.12341f, 17.56122f, 18.412209f, 18.272379f, 18.9704f, 18.83058f);
generalPath.curveTo(19.52858f, 19.38879f, 20.2398f, 19.67758f, 21.104f, 19.69696f);
generalPath.curveTo(21.96822f, 19.67758f, 22.6794f, 19.38879f, 23.2376f, 18.83058f);
generalPath.curveTo(23.79578f, 18.27241f, 24.08457f, 17.56118f, 24.103981f, 16.69698f);
generalPath.curveTo(24.084572f, 15.83278f, 23.79578f, 15.121579f, 23.2376f, 14.563379f);
generalPath.curveTo(22.6794f, 14.005209f, 21.9682f, 13.716419f, 21.104f, 13.697f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_1_1
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(15.104f, 21.697f);
generalPath.lineTo(15.104f, 24.697f);
generalPath.lineTo(17.104f, 24.697f);
generalPath.curveTo(17.65628f, 24.697f, 18.104f, 25.14472f, 18.104f, 25.697f);
generalPath.lineTo(18.104f, 34.697f);
generalPath.curveTo(18.104f, 35.24928f, 17.65628f, 35.697f, 17.104f, 35.697f);
generalPath.lineTo(15.104f, 35.697f);
generalPath.lineTo(15.104f, 38.697f);
generalPath.lineTo(27.104f, 38.697f);
generalPath.lineTo(27.104f, 35.697f);
generalPath.lineTo(25.104f, 35.697f);
generalPath.curveTo(24.55172f, 35.697f, 24.104f, 35.24928f, 24.104f, 34.697f);
generalPath.lineTo(24.104f, 21.696999f);
generalPath.lineTo(15.104f, 21.696999f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
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
        return 2.000117778778076;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 1.0005923509597778;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 43.99979019165039;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 46.99940872192383;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private TangoROptionPane_information() {
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
       TangoROptionPane_information base = new TangoROptionPane_information();
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
       TangoROptionPane_information base = new TangoROptionPane_information();
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
        return TangoROptionPane_information::new;
    }
}

