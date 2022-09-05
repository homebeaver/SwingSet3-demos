package org.jdesktop.swingx.demos.svg;

import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.WeakReference;
import java.util.Stack;

import javax.swing.Icon;

/**
 * This class has been automatically generated using
 * <a href="https://github.com/kirill-grouchnikov/radiance">Radiance SVG
 * transcoder</a>.

generiert aus

<svg
  xmlns="http://www.w3.org/2000/svg"
  width="24"
  height="24"
  viewBox="0 0 24 24"
  fill="none"
  stroke="currentColor"
  stroke-width="2"
  stroke-linecap="round"
  stroke-linejoin="round"
>
  <polyline points="22 12 18 12 15 21 9 3 6 12 2 12" />
</svg> 

 */
public class IconTactivity 
											implements Icon // fehlte
{

	private static Shape shape = null;
	private static Paint paint = null;
	private static Stroke stroke = null;
	private static Shape clip = null;
	private static Stack<AffineTransform> transformsStack = new Stack<>();
	// fehlte:
	private GeneralPath generalPath = null;
	// private RadianceIcon.ColorFilter colorFilter = null;

	private void _paint0(Graphics2D g, float origAlpha) {
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
//		paint = (colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255);
		stroke = new BasicStroke(2.0f, 1, 1, 4.0f, null, 0.0f);
		if (generalPath == null) {
			generalPath = new GeneralPath();
		} else {
			generalPath.reset();
		}
		generalPath.moveTo(22.0f, 12.0f);
		generalPath.lineTo(18.0f, 12.0f);
		generalPath.lineTo(15.0f, 21.0f);
		generalPath.lineTo(9.0f, 3.0f);
		generalPath.lineTo(6.0f, 12.0f);
		generalPath.lineTo(2.0f, 12.0f);
		shape = generalPath;
		g.setPaint(paint);
		g.setStroke(stroke);
		g.draw(shape);
		g.setTransform(transformsStack.pop());
		g.setTransform(transformsStack.pop());
		g.setTransform(transformsStack.pop());

	}

	/**
	 * Paints the transcoded SVG image on the specified graphics context. You can
	 * install a custom transformation on the graphics context to scale the image.
	 * 
	 * @param g Graphics context.
	 */
	@SuppressWarnings("unused")
//	public static void paint(Graphics2D g) {
	public void paint(Graphics2D g) {
		float origAlpha = 1.0f;
		Composite origComposite = g.getComposite();
		if (origComposite instanceof AlphaComposite) {
			AlphaComposite origAlphaComposite = (AlphaComposite) origComposite;
			if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
				origAlpha = origAlphaComposite.getAlpha();
			}
		}

		_paint0(g, origAlpha);

		shape = null;
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
		return 1.0;
	}

	/**
	 * Returns the Y of the bounding box of the original SVG image.
	 * 
	 * @return The Y of the bounding box of the original SVG image.
	 */
	public static double getOrigY() {
		return 2.0;
	}

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 22.0;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 20.0;
	}

// manuell:
    private Color color;
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(color==null ? c.getForeground() : color);
		paint(g2d);
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return (int)(getOrigWidth()-getOrigX());
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return (int)(getOrigHeight()-getOrigY());
	}
}
