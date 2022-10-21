package org.pushingpixels.radiance.theming.internal.svg;

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
public class ic_palette_black_24px implements RadianceIcon {
    private Shape shape = null;
    private GeneralPath generalPath = null;
    private Paint paint = null;
    private Stroke stroke = null;
    private Shape clip = null;
    private RadianceIcon.ColorFilter colorFilter = null;
    private Stack<AffineTransform> transformsStack = new Stack<>();

    

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
generalPath.moveTo(12.0f, 3.0f);
generalPath.curveTo(7.03f, 3.0f, 3.0f, 7.03f, 3.0f, 12.0f);
generalPath.curveTo(3.0f, 16.97f, 7.03f, 21.0f, 12.0f, 21.0f);
generalPath.curveTo(12.83f, 21.0f, 13.5f, 20.33f, 13.5f, 19.5f);
generalPath.curveTo(13.5f, 19.11f, 13.35f, 18.76f, 13.11f, 18.49f);
generalPath.curveTo(12.88f, 18.23f, 12.73f, 17.88f, 12.73f, 17.5f);
generalPath.curveTo(12.73f, 16.67f, 13.4f, 16.0f, 14.23f, 16.0f);
generalPath.lineTo(16.0f, 16.0f);
generalPath.curveTo(18.76f, 16.0f, 21.0f, 13.76f, 21.0f, 11.0f);
generalPath.curveTo(21.0f, 6.58f, 16.97f, 3.0f, 12.0f, 3.0f);
generalPath.closePath();
generalPath.moveTo(6.5f, 12.0f);
generalPath.curveTo(5.67f, 12.0f, 5.0f, 11.33f, 5.0f, 10.5f);
generalPath.curveTo(5.0f, 9.67f, 5.67f, 9.0f, 6.5f, 9.0f);
generalPath.curveTo(7.33f, 9.0f, 8.0f, 9.67f, 8.0f, 10.5f);
generalPath.curveTo(8.0f, 11.33f, 7.33f, 12.0f, 6.5f, 12.0f);
generalPath.closePath();
generalPath.moveTo(9.5f, 8.0f);
generalPath.curveTo(8.67f, 8.0f, 8.0f, 7.33f, 8.0f, 6.5f);
generalPath.curveTo(8.0f, 5.67f, 8.67f, 5.0f, 9.5f, 5.0f);
generalPath.curveTo(10.33f, 5.0f, 11.0f, 5.67f, 11.0f, 6.5f);
generalPath.curveTo(11.0f, 7.33f, 10.33f, 8.0f, 9.5f, 8.0f);
generalPath.closePath();
generalPath.moveTo(14.5f, 8.0f);
generalPath.curveTo(13.67f, 8.0f, 13.0f, 7.33f, 13.0f, 6.5f);
generalPath.curveTo(13.0f, 5.67f, 13.67f, 5.0f, 14.5f, 5.0f);
generalPath.curveTo(15.33f, 5.0f, 16.0f, 5.67f, 16.0f, 6.5f);
generalPath.curveTo(16.0f, 7.33f, 15.33f, 8.0f, 14.5f, 8.0f);
generalPath.closePath();
generalPath.moveTo(17.5f, 12.0f);
generalPath.curveTo(16.67f, 12.0f, 16.0f, 11.33f, 16.0f, 10.5f);
generalPath.curveTo(16.0f, 9.67f, 16.67f, 9.0f, 17.5f, 9.0f);
generalPath.curveTo(18.33f, 9.0f, 19.0f, 9.67f, 19.0f, 10.5f);
generalPath.curveTo(19.0f, 11.33f, 18.33f, 12.0f, 17.5f, 12.0f);
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
        return 3.0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 3.0;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 18.0;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 18.0;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private ic_palette_black_24px() {
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
       ic_palette_black_24px base = new ic_palette_black_24px();
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
       ic_palette_black_24px base = new ic_palette_black_24px();
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
        return ic_palette_black_24px::new;
    }
}

