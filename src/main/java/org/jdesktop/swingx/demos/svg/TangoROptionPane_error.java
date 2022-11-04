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
public class TangoROptionPane_error implements RadianceIcon {
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
g.setComposite(AlphaComposite.getInstance(3, 0.63f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
shape = new Ellipse2D.Double(5.739999771118164, 36.03000259399414, 37.0, 11.199999809265137);
paint = new RadialGradientPaint(new Point2D.Double(24.239999771118164, 41.630001068115234), 18.5f, new Point2D.Double(24.239999771118164, 41.630001068115234), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.302700012922287f, 0.0f, 29.030000686645508f));
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
generalPath.moveTo(32.69f, 0.49f);
generalPath.lineTo(45.51f, 13.59f);
generalPath.lineTo(45.51f, 31.48f);
generalPath.lineTo(32.86f, 43.48f);
generalPath.lineTo(15.43f, 43.48f);
generalPath.lineTo(2.5f, 30.64f);
generalPath.lineTo(2.5f, 13.439999f);
generalPath.lineTo(15.6f, 0.4799986f);
generalPath.closePath();
shape = generalPath;
paint = new LinearGradientPaint(new Point2D.Double(24.0, 18.100000381469727), new Point2D.Double(41.0, 35.959999084472656), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(204, 0, 0, 255)) : new Color(204, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(179, 0, 0, 255)) : new Color(179, 0, 0, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(136, 0, 0, 255)) : new Color(136, 0, 0, 255);
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(32.69f, 0.49f);
generalPath.lineTo(45.51f, 13.59f);
generalPath.lineTo(45.51f, 31.48f);
generalPath.lineTo(32.86f, 43.48f);
generalPath.lineTo(15.43f, 43.48f);
generalPath.lineTo(2.5f, 30.64f);
generalPath.lineTo(2.5f, 13.439999f);
generalPath.lineTo(15.6f, 0.4799986f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(25.72f, 26.57f);
generalPath.curveTo(25.848f, 26.57f, 25.955f, 26.396f, 25.955f, 26.185999f);
generalPath.lineTo(27.015f, 11.785999f);
generalPath.curveTo(27.015f, 11.577f, 26.907999f, 11.416999f, 26.779999f, 11.416999f);
generalPath.lineTo(21.095f, 11.416999f);
generalPath.curveTo(20.967f, 11.416999f, 20.859999f, 11.575999f, 20.859999f, 11.785999f);
generalPath.lineTo(21.919998f, 26.185999f);
generalPath.curveTo(21.919998f, 26.394999f, 22.026999f, 26.57f, 22.154999f, 26.57f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(361.20001220703125, 264.6000061035156), 316.9f, new Point2D.Double(361.20001220703125, 264.6000061035156), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(184, 184, 184, 255)) : new Color(184, 184, 184, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.11980000138282776f, 0.0f, 0.0f, 0.1858000010251999f, -17.479999542236328f, -34.77000045776367f));
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(15.15999984741211, 10.539999961853027), new Point2D.Double(32.59000015258789, 38.189998626708984), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 2, 2, 77)) : new Color(255, 2, 2, 77)),((colorFilter != null) ? colorFilter.filter(new Color(255, 155, 155, 204)) : new Color(255, 155, 155, 204))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.2f,0,1,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(25.72f, 26.57f);
generalPath.curveTo(25.848f, 26.57f, 25.955f, 26.396f, 25.955f, 26.185999f);
generalPath.lineTo(27.015f, 11.785999f);
generalPath.curveTo(27.015f, 11.577f, 26.907999f, 11.416999f, 26.779999f, 11.416999f);
generalPath.lineTo(21.095f, 11.416999f);
generalPath.curveTo(20.967f, 11.416999f, 20.859999f, 11.575999f, 20.859999f, 11.785999f);
generalPath.lineTo(21.919998f, 26.185999f);
generalPath.curveTo(21.919998f, 26.394999f, 22.026999f, 26.57f, 22.154999f, 26.57f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_2_0_1
shape = new Ellipse2D.Double(21.700000762939453, 29.0, 4.599999904632568, 4.599999904632568);
paint = new RadialGradientPaint(new Point2D.Double(361.20001220703125, 264.6000061035156), 316.9f, new Point2D.Double(361.20001220703125, 264.6000061035156), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(184, 184, 184, 255)) : new Color(184, 184, 184, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.11980000138282776f, 0.0f, 0.0f, 0.1858000010251999f, -17.479999542236328f, -34.77000045776367f));
g.setPaint(paint);
g.fill(shape);
paint = new LinearGradientPaint(new Point2D.Double(15.15999984741211, 10.539999961853027), new Point2D.Double(32.59000015258789, 38.189998626708984), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 2, 2, 77)) : new Color(255, 2, 2, 77)),((colorFilter != null) ? colorFilter.filter(new Color(255, 155, 155, 204)) : new Color(255, 155, 155, 204))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.2f,0,1,4.0f,null,0.0f);
shape = new Ellipse2D.Double(21.700000762939453, 29.0, 4.599999904632568, 4.599999904632568);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.8f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
paint = new LinearGradientPaint(new Point2D.Double(15.739999771118164, 10.5), new Point2D.Double(53.599998474121094, 45.400001525878906), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 139, 139, 255)) : new Color(255, 139, 139, 255)),((colorFilter != null) ? colorFilter.filter(new Color(236, 27, 27, 255)) : new Color(236, 27, 27, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(32.23f, 1.5f);
generalPath.lineTo(44.48f, 13.92f);
generalPath.lineTo(44.48f, 31.02f);
generalPath.lineTo(32.62f, 42.47f);
generalPath.lineTo(15.849998f, 42.47f);
generalPath.lineTo(3.4899988f, 30.190002f);
generalPath.lineTo(3.4899988f, 13.830002f);
generalPath.lineTo(15.999999f, 1.4900017f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_0_0
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(25.72f, 26.57f);
generalPath.curveTo(25.848f, 26.57f, 25.955f, 26.396f, 25.955f, 26.185999f);
generalPath.lineTo(27.015f, 11.785999f);
generalPath.curveTo(27.015f, 11.577f, 26.907999f, 11.416999f, 26.779999f, 11.416999f);
generalPath.lineTo(21.095f, 11.416999f);
generalPath.curveTo(20.967f, 11.416999f, 20.859999f, 11.575999f, 20.859999f, 11.785999f);
generalPath.lineTo(21.919998f, 26.185999f);
generalPath.curveTo(21.919998f, 26.394999f, 22.026999f, 26.57f, 22.154999f, 26.57f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(361.20001220703125, 264.6000061035156), 316.9f, new Point2D.Double(361.20001220703125, 264.6000061035156), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(184, 184, 184, 255)) : new Color(184, 184, 184, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.11980000138282776f, 0.0f, 0.0f, 0.1858000010251999f, -17.479999542236328f, -34.77000045776367f));
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 230)) : new Color(255, 255, 255, 230);
stroke = new BasicStroke(0.4f,0,1,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(25.72f, 26.57f);
generalPath.curveTo(25.848f, 26.57f, 25.955f, 26.396f, 25.955f, 26.185999f);
generalPath.lineTo(27.015f, 11.785999f);
generalPath.curveTo(27.015f, 11.577f, 26.907999f, 11.416999f, 26.779999f, 11.416999f);
generalPath.lineTo(21.095f, 11.416999f);
generalPath.curveTo(20.967f, 11.416999f, 20.859999f, 11.575999f, 20.859999f, 11.785999f);
generalPath.lineTo(21.919998f, 26.185999f);
generalPath.curveTo(21.919998f, 26.394999f, 22.026999f, 26.57f, 22.154999f, 26.57f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_4_0_1
shape = new Ellipse2D.Double(21.700000762939453, 29.0, 4.599999904632568, 4.599999904632568);
paint = new RadialGradientPaint(new Point2D.Double(361.20001220703125, 264.6000061035156), 316.9f, new Point2D.Double(361.20001220703125, 264.6000061035156), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(184, 184, 184, 255)) : new Color(184, 184, 184, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.11980000138282776f, 0.0f, 0.0f, 0.1858000010251999f, -17.479999542236328f, -34.77000045776367f));
g.setPaint(paint);
g.fill(shape);
paint = (colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 230)) : new Color(255, 255, 255, 230);
stroke = new BasicStroke(0.4f,0,1,4.0f,null,0.0f);
shape = new Ellipse2D.Double(21.700000762939453, 29.0, 4.599999904632568, 4.599999904632568);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.3f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_5
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(2.75f, 13.56f);
generalPath.lineTo(2.75f, 30.560001f);
generalPath.lineTo(5.69f, 33.47f);
generalPath.curveTo(22.45f, 33.530003f, 22.16f, 20.470001f, 45.25f, 21.59f);
generalPath.lineTo(45.25f, 13.690001f);
generalPath.lineTo(32.56f, 0.75000095f);
generalPath.lineTo(15.690001f, 0.75000095f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(16.75, 10.670000076293945), 21.25f, new Point2D.Double(16.75, 10.670000076293945), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 0)) : new Color(255, 255, 255, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(4.15500020980835f, 0.0f, 0.0f, 3.1989998817443848f, -52.849998474121094f, -23.510000228881836f));
g.setPaint(paint);
g.fill(shape);
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
        return 2.0;
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
		return 44.0099983215332;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 47.230003356933594;
	}

	/** The current width of this icon. */
	private int width;

    /** The current height of this icon. */
	private int height;

	/**
	 * Creates a new transcoded SVG image. This is marked as private to indicate that app
	 * code should be using the {@link #of(int, int)} method to obtain a pre-configured instance.
	 */
	private TangoROptionPane_error() {
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
       TangoROptionPane_error base = new TangoROptionPane_error();
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
       TangoROptionPane_error base = new TangoROptionPane_error();
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
        return TangoROptionPane_error::new;
    }
}

