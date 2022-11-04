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
public class TangoROptionPane_question implements RadianceIcon {
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
g.setComposite(AlphaComposite.getInstance(3, 0.6f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0705499649047852f, 0.0f, 0.0f, 0.5249999761581421f, -0.8927549719810486f, 22.5f));
// _0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(41.0f, 40.0f);
generalPath.curveTo(41.027473f, 43.071407f, 37.766083f, 45.9154f, 32.45078f, 47.455086f);
generalPath.curveTo(27.135475f, 48.994766f, 20.578812f, 48.994766f, 15.263508f, 47.455086f);
generalPath.curveTo(9.948204f, 45.9154f, 6.6868153f, 43.071407f, 6.714287f, 40.0f);
generalPath.curveTo(6.6868153f, 36.928593f, 9.948204f, 34.0846f, 15.263508f, 32.544914f);
generalPath.curveTo(20.578812f, 31.005232f, 27.135475f, 31.005232f, 32.45078f, 32.544914f);
generalPath.curveTo(37.766083f, 34.0846f, 41.027473f, 36.928593f, 41.0f, 40.0f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(23.85714340209961, 40.0), 17.142857f, new Point2D.Double(23.85714340209961, 40.0), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 20.0f));
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(45.49997f, 23.000044f);
generalPath.curveTo(45.49997f, 34.87415f, 35.874107f, 44.500015f, 24.0f, 44.500015f);
generalPath.curveTo(12.125893f, 44.500015f, 2.5000298f, 34.87415f, 2.5000298f, 23.000044f);
generalPath.curveTo(2.5000298f, 11.125938f, 12.125895f, 1.500073f, 24.0f, 1.500073f);
generalPath.curveTo(35.874107f, 1.500073f, 45.49997f, 11.125938f, 45.49997f, 23.000044f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(245, 121, 0, 255)) : new Color(245, 121, 0, 255);
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(143, 89, 2, 255)) : new Color(143, 89, 2, 255);
stroke = new BasicStroke(1.0000019f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(45.49997f, 23.000044f);
generalPath.curveTo(45.49997f, 34.87415f, 35.874107f, 44.500015f, 24.0f, 44.500015f);
generalPath.curveTo(12.125893f, 44.500015f, 2.5000298f, 34.87415f, 2.5000298f, 23.000044f);
generalPath.curveTo(2.5000298f, 11.125938f, 12.125895f, 1.500073f, 24.0f, 1.500073f);
generalPath.curveTo(35.874107f, 1.500073f, 45.49997f, 11.125938f, 45.49997f, 23.000044f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.35f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_1
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
stroke = new BasicStroke(0.9999958f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(44.5f, 23.0f);
generalPath.curveTo(44.532852f, 30.345785f, 40.632774f, 37.14767f, 34.276558f, 40.83008f);
generalPath.curveTo(27.92034f, 44.512486f, 20.07966f, 44.512486f, 13.723443f, 40.83008f);
generalPath.curveTo(7.367226f, 37.14767f, 3.4671485f, 30.345785f, 3.5f, 23.0f);
generalPath.curveTo(3.4671485f, 15.654216f, 7.367226f, 8.85233f, 13.723443f, 5.1699224f);
generalPath.curveTo(20.07966f, 1.4875139f, 27.92034f, 1.4875139f, 34.276558f, 5.1699224f);
generalPath.curveTo(40.632774f, 8.85233f, 44.532852f, 15.654216f, 44.5f, 23.0f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.25f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_2
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(24.0f, 3.0f);
generalPath.curveTo(12.954305f, 3.0f, 4.0f, 11.954305f, 4.0f, 23.0f);
generalPath.curveTo(4.0f, 24.686052f, 4.232138f, 26.310816f, 4.625f, 27.875f);
generalPath.curveTo(7.8289733f, 32.668262f, 17.878521f, 27.754856f, 24.84375f, 22.28125f);
generalPath.curveTo(32.8518f, 15.988144f, 42.68578f, 30.640598f, 43.96875f, 23.71875f);
generalPath.curveTo(43.97718f, 23.480131f, 44.0f, 23.240675f, 44.0f, 23.0f);
generalPath.curveTo(44.0f, 11.954305f, 35.045696f, 3.0f, 24.0f, 3.0f);
generalPath.closePath();
shape = generalPath;
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
{
    Graphics2D gText = ((Graphics2D)g.create());
            Shape shapeText = null;
            GeneralPath generalPathText = null;
gText.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
gText.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
gText.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255);
gText.setPaint(paint);
if (generalPathText == null) {
   generalPathText = new GeneralPath();
} else {
   generalPathText.reset();
}
generalPathText.moveTo(26.879883f, 27.500042f);
generalPathText.lineTo(20.52832f, 27.500042f);
generalPathText.lineTo(20.52832f, 26.632854f);
generalPathText.quadTo(20.52832f, 25.191448f, 21.10254f, 24.078167f);
generalPathText.quadTo(21.688477f, 22.964886f, 23.551758f, 21.24223f);
generalPathText.lineTo(24.676758f, 20.222698f);
generalPathText.quadTo(25.68457f, 19.308636f, 26.141602f, 18.500042f);
generalPathText.quadTo(26.610352f, 17.691448f, 26.610352f, 16.882854f);
generalPathText.quadTo(26.610352f, 15.652386f, 25.766602f, 14.960979f);
generalPathText.quadTo(24.922852f, 14.257854f, 23.411133f, 14.257854f);
generalPathText.quadTo(21.993164f, 14.257854f, 20.34082f, 14.855511f);
generalPathText.quadTo(18.688477f, 15.441448f, 16.895508f, 16.601604f);
generalPathText.lineTo(16.895508f, 11.082073f);
generalPathText.quadTo(19.016602f, 10.343792f, 20.774414f, 9.992229f);
generalPathText.quadTo(22.532227f, 9.640667f, 24.172852f, 9.640667f);
generalPathText.quadTo(28.461914f, 9.640667f, 30.711914f, 11.386761f);
generalPathText.quadTo(32.961914f, 13.132854f, 32.961914f, 16.496136f);
generalPathText.quadTo(32.961914f, 18.218792f, 32.270508f, 19.578167f);
generalPathText.quadTo(31.59082f, 20.937542f, 29.938477f, 22.507854f);
generalPathText.lineTo(28.813477f, 23.503948f);
generalPathText.quadTo(27.618164f, 24.593792f, 27.243164f, 25.26176f);
generalPathText.quadTo(26.879883f, 25.91801f, 26.879883f, 26.703167f);
generalPathText.lineTo(26.879883f, 27.500042f);
generalPathText.closePath();
generalPathText.moveTo(20.52832f, 30.101604f);
generalPathText.lineTo(26.879883f, 30.101604f);
generalPathText.lineTo(26.879883f, 36.359417f);
generalPathText.lineTo(20.52832f, 36.359417f);
generalPathText.lineTo(20.52832f, 30.101604f);
generalPathText.closePath();
shapeText = generalPath;
gText.fill(shapeText);
gText.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
    gText.dispose();
}
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
        return 2.0000288486480713;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 0.9650625586509705;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 43.999942779541016;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 47.03493881225586;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private TangoROptionPane_question() {
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
       TangoROptionPane_question base = new TangoROptionPane_question();
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
       TangoROptionPane_question base = new TangoROptionPane_question();
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
        return TangoROptionPane_question::new;
    }
}

