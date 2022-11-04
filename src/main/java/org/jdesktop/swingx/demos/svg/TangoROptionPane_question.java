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
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_0
paint = new LinearGradientPaint(new Point2D.Double(31.5, 59.22999954223633), new Point2D.Double(9.125, 10.489999771118164), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(26, 59, 110, 255)) : new Color(26, 59, 110, 255)),((colorFilter != null) ? colorFilter.filter(new Color(32, 74, 135, 255)) : new Color(32, 74, 135, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(3.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(22.08f, 27.93f);
generalPath.curveTo(22.08f, 25.7f, 22.5f, 24.25f, 23.93f, 21.45f);
generalPath.curveTo(25.89f, 17.640001f, 26.42f, 15.85f, 26.42f, 13.090001f);
generalPath.curveTo(26.42f, 9.520001f, 25.28f, 7.2000012f, 23.07f, 6.270001f);
generalPath.curveTo(21.74f, 5.7200007f, 20.09f, 5.9700007f, 19.11f, 6.870001f);
generalPath.curveTo(18.390001f, 7.5300007f, 18.42f, 7.9700007f, 19.300001f, 10.02f);
generalPath.curveTo(20.490002f, 12.780001f, 20.230001f, 14.67f, 18.560001f, 15.51f);
generalPath.curveTo(16.930002f, 16.33f, 14.890001f, 15.72f, 13.990002f, 14.14f);
generalPath.curveTo(13.600001f, 13.450001f, 13.510002f, 13.01f, 13.540002f, 11.900001f);
generalPath.curveTo(13.620002f, 8.39f, 16.590002f, 5.3300004f, 20.730001f, 4.4900007f);
generalPath.curveTo(22.310001f, 4.1700006f, 25.600002f, 4.1700006f, 27.170002f, 4.4900007f);
generalPath.curveTo(31.430002f, 5.370001f, 34.29f, 8.380001f, 34.47f, 12.17f);
generalPath.curveTo(34.550003f, 13.95f, 34.16f, 15.63f, 33.33f, 17.04f);
generalPath.curveTo(32.620003f, 18.25f, 30.330002f, 20.37f, 28.04f, 21.900002f);
generalPath.curveTo(24.550001f, 24.250002f, 23.52f, 25.630001f, 23.19f, 28.430002f);
generalPath.curveTo(23.08f, 29.400002f, 23.01f, 29.520002f, 22.57f, 29.520002f);
generalPath.curveTo(22.1f, 29.520002f, 22.08f, 29.460003f, 22.08f, 27.910002f);
generalPath.closePath();
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_0_0_1
paint = new LinearGradientPaint(new Point2D.Double(31.5, 59.22999954223633), new Point2D.Double(9.125, 10.489999771118164), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(26, 59, 110, 255)) : new Color(26, 59, 110, 255)),((colorFilter != null) ? colorFilter.filter(new Color(32, 74, 135, 255)) : new Color(32, 74, 135, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(3.0f,0,0,4.0f,null,0.0f);
shape = new Ellipse2D.Double(18.489999771118164, 33.650001525878906, 8.5600004196167, 8.5600004196167);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.2f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_0
shape = new Ellipse2D.Double(16.149999618530273, 39.6400032043457, 13.699999809265137, 5.519999980926514);
paint = new RadialGradientPaint(new Point2D.Double(25.0, 42.0), 13.0f, new Point2D.Double(25.0, 42.0), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(0.5270000100135803f, 0.0f, 0.0f, 0.21199999749660492f, 9.729999542236328f, 33.5099983215332f));
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_1_1
shape = new Ellipse2D.Double(12.0, 38.0, 26.0, 9.0);
paint = new RadialGradientPaint(new Point2D.Double(25.0, 42.0), 13.0f, new Point2D.Double(25.0, 42.0), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 255)) : new Color(0, 0, 0, 255)),((colorFilter != null) ? colorFilter.filter(new Color(0, 0, 0, 0)) : new Color(0, 0, 0, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 0.34599998593330383f, 0.0f, 27.989999771118164f));
g.setPaint(paint);
g.fill(shape);
g.setTransform(transformsStack.pop());
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
generalPath.moveTo(22.08f, 27.93f);
generalPath.curveTo(22.08f, 25.7f, 22.5f, 24.25f, 23.93f, 21.45f);
generalPath.curveTo(25.89f, 17.640001f, 26.42f, 15.85f, 26.42f, 13.090001f);
generalPath.curveTo(26.42f, 9.520001f, 25.28f, 7.2000012f, 23.07f, 6.270001f);
generalPath.curveTo(21.74f, 5.7200007f, 20.09f, 5.9700007f, 19.11f, 6.870001f);
generalPath.curveTo(18.390001f, 7.5300007f, 18.42f, 7.9700007f, 19.300001f, 10.02f);
generalPath.curveTo(20.490002f, 12.780001f, 20.230001f, 14.67f, 18.560001f, 15.51f);
generalPath.curveTo(16.930002f, 16.33f, 14.890001f, 15.72f, 13.990002f, 14.14f);
generalPath.curveTo(13.600001f, 13.450001f, 13.510002f, 13.01f, 13.540002f, 11.900001f);
generalPath.curveTo(13.620002f, 8.39f, 16.590002f, 5.3300004f, 20.730001f, 4.4900007f);
generalPath.curveTo(22.310001f, 4.1700006f, 25.600002f, 4.1700006f, 27.170002f, 4.4900007f);
generalPath.curveTo(31.430002f, 5.370001f, 34.29f, 8.380001f, 34.47f, 12.17f);
generalPath.curveTo(34.550003f, 13.95f, 34.16f, 15.63f, 33.33f, 17.04f);
generalPath.curveTo(32.620003f, 18.25f, 30.330002f, 20.37f, 28.04f, 21.900002f);
generalPath.curveTo(24.550001f, 24.250002f, 23.52f, 25.630001f, 23.19f, 28.430002f);
generalPath.curveTo(23.08f, 29.400002f, 23.01f, 29.520002f, 22.57f, 29.520002f);
generalPath.curveTo(22.1f, 29.520002f, 22.08f, 29.460003f, 22.08f, 27.910002f);
generalPath.closePath();
shape = generalPath;
paint = new RadialGradientPaint(new Point2D.Double(26.033000946044922, 26.165000915527344), 10.97f, new Point2D.Double(26.033000946044922, 26.165000915527344), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(42, 83, 135, 255)) : new Color(42, 83, 135, 255)),((colorFilter != null) ? colorFilter.filter(new Color(73, 127, 198, 255)) : new Color(73, 127, 198, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.8454999923706055f, -1.223800063598901E-4f, -19.878999710083008f));
g.setPaint(paint);
g.fill(shape);
paint = new RadialGradientPaint(new Point2D.Double(26.033000946044922, 26.165000915527344), 10.97f, new Point2D.Double(26.033000946044922, 26.165000915527344), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(42, 83, 135, 255)) : new Color(42, 83, 135, 255)),((colorFilter != null) ? colorFilter.filter(new Color(73, 127, 198, 255)) : new Color(73, 127, 198, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.8454999923706055f, -1.223800063598901E-4f, -19.878999710083008f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(22.08f, 27.93f);
generalPath.curveTo(22.08f, 25.7f, 22.5f, 24.25f, 23.93f, 21.45f);
generalPath.curveTo(25.89f, 17.640001f, 26.42f, 15.85f, 26.42f, 13.090001f);
generalPath.curveTo(26.42f, 9.520001f, 25.28f, 7.2000012f, 23.07f, 6.270001f);
generalPath.curveTo(21.74f, 5.7200007f, 20.09f, 5.9700007f, 19.11f, 6.870001f);
generalPath.curveTo(18.390001f, 7.5300007f, 18.42f, 7.9700007f, 19.300001f, 10.02f);
generalPath.curveTo(20.490002f, 12.780001f, 20.230001f, 14.67f, 18.560001f, 15.51f);
generalPath.curveTo(16.930002f, 16.33f, 14.890001f, 15.72f, 13.990002f, 14.14f);
generalPath.curveTo(13.600001f, 13.450001f, 13.510002f, 13.01f, 13.540002f, 11.900001f);
generalPath.curveTo(13.620002f, 8.39f, 16.590002f, 5.3300004f, 20.730001f, 4.4900007f);
generalPath.curveTo(22.310001f, 4.1700006f, 25.600002f, 4.1700006f, 27.170002f, 4.4900007f);
generalPath.curveTo(31.430002f, 5.370001f, 34.29f, 8.380001f, 34.47f, 12.17f);
generalPath.curveTo(34.550003f, 13.95f, 34.16f, 15.63f, 33.33f, 17.04f);
generalPath.curveTo(32.620003f, 18.25f, 30.330002f, 20.37f, 28.04f, 21.900002f);
generalPath.curveTo(24.550001f, 24.250002f, 23.52f, 25.630001f, 23.19f, 28.430002f);
generalPath.curveTo(23.08f, 29.400002f, 23.01f, 29.520002f, 22.57f, 29.520002f);
generalPath.curveTo(22.1f, 29.520002f, 22.08f, 29.460003f, 22.08f, 27.910002f);
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
shape = new Ellipse2D.Double(18.489999771118164, 33.650001525878906, 8.5600004196167, 8.5600004196167);
paint = new RadialGradientPaint(new Point2D.Double(26.033000946044922, 26.165000915527344), 10.97f, new Point2D.Double(26.033000946044922, 26.165000915527344), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(42, 83, 135, 255)) : new Color(42, 83, 135, 255)),((colorFilter != null) ? colorFilter.filter(new Color(73, 127, 198, 255)) : new Color(73, 127, 198, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.8454999923706055f, -1.223800063598901E-4f, -19.878999710083008f));
g.setPaint(paint);
g.fill(shape);
paint = new RadialGradientPaint(new Point2D.Double(26.033000946044922, 26.165000915527344), 10.97f, new Point2D.Double(26.033000946044922, 26.165000915527344), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(42, 83, 135, 255)) : new Color(42, 83, 135, 255)),((colorFilter != null) ? colorFilter.filter(new Color(73, 127, 198, 255)) : new Color(73, 127, 198, 255))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.8454999923706055f, -1.223800063598901E-4f, -19.878999710083008f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new Ellipse2D.Double(18.489999771118164, 33.650001525878906, 8.5600004196167, 8.5600004196167);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 0.7f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_0
paint = new LinearGradientPaint(new Point2D.Double(20.573999404907227, 34.62099838256836), new Point2D.Double(26.243000030517578, 47.09400177001953), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(136, 138, 133, 0)) : new Color(136, 138, 133, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
shape = new Ellipse2D.Double(18.489999771118164, 33.650001525878906, 8.5600004196167, 8.5600004196167);
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_1
paint = new LinearGradientPaint(new Point2D.Double(13.770000457763672, 13.699999809265137), new Point2D.Double(24.100000381469727, 21.469999313354492), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(179, 179, 179, 0)) : new Color(179, 179, 179, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(18.56f, 15.53f);
generalPath.curveTo(16.93f, 16.35f, 14.714999f, 15.875f, 13.99f, 14.16f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_2
paint = new LinearGradientPaint(new Point2D.Double(23.329999923706055, 6.239999771118164), new Point2D.Double(38.7400016784668, 29.8700008392334), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(179, 179, 179, 0)) : new Color(179, 179, 179, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(34.5f, 12.19f);
generalPath.curveTo(34.58f, 13.969999f, 34.19f, 15.639999f, 33.36f, 17.06f);
generalPath.curveTo(32.65f, 18.27f, 30.36f, 20.39f, 28.07f, 21.92f);
generalPath.curveTo(24.58f, 24.27f, 23.55f, 25.65f, 23.22f, 28.45f);
generalPath.curveTo(23.109999f, 29.42f, 23.01f, 29.54f, 22.57f, 29.54f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_3
paint = new LinearGradientPaint(new Point2D.Double(20.100000381469727, 21.75), new Point2D.Double(31.389999389648438, 41.75), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(179, 179, 179, 0)) : new Color(179, 179, 179, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(22.57f, 29.54f);
generalPath.curveTo(22.1f, 29.54f, 22.08f, 29.480001f, 22.08f, 27.93f);
generalPath.curveTo(22.08f, 25.7f, 22.5f, 24.2279f, 23.93f, 21.45f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_4
paint = new LinearGradientPaint(new Point2D.Double(14.819999694824219, 12.399999618530273), new Point2D.Double(29.309999465942383, 36.5), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(179, 179, 179, 0)) : new Color(179, 179, 179, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(13.99f, 14.16f);
generalPath.curveTo(13.179999f, 12.29f, 13.519f, 10.079f, 14.62f, 8.4f);
generalPath.curveTo(16.089f, 6.1589994f, 18.619999f, 4.8999996f, 21.21f, 4.41f);
generalPath.curveTo(23.169998f, 4.0396f, 25.21f, 4.0681996f, 27.175f, 4.515f);
generalPath.curveTo(31.63f, 5.528f, 34.32f, 8.4f, 34.5f, 12.190001f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
g.setTransform(transformsStack.pop());
g.setComposite(AlphaComposite.getInstance(3, 1.0f * origAlpha));
transformsStack.push(g.getTransform());
g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
// _0_3_5
paint = new LinearGradientPaint(new Point2D.Double(23.540000915527344, 23.219999313354492), new Point2D.Double(17.799999237060547, 4.794000148773193), new float[] {0.0f,1.0f}, new Color[] {((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 255)) : new Color(255, 255, 255, 255)),((colorFilter != null) ? colorFilter.filter(new Color(255, 255, 255, 0)) : new Color(255, 255, 255, 0))}, MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
stroke = new BasicStroke(1.0f,0,0,4.0f,null,0.0f);
if (generalPath == null) {
   generalPath = new GeneralPath();
} else {
   generalPath.reset();
}
generalPath.moveTo(23.93f, 21.45f);
generalPath.curveTo(25.89f, 17.640001f, 26.42f, 15.85f, 26.42f, 13.090001f);
generalPath.curveTo(26.42f, 9.520001f, 25.28f, 7.2000012f, 23.07f, 6.270001f);
generalPath.curveTo(21.74f, 5.7200007f, 20.09f, 5.9700007f, 19.11f, 6.870001f);
generalPath.curveTo(18.390001f, 7.5300007f, 18.42f, 7.9700007f, 19.300001f, 10.02f);
generalPath.curveTo(20.490002f, 12.780001f, 20.230001f, 14.690001f, 18.560001f, 15.530001f);
shape = generalPath;
g.setPaint(paint);
g.setStroke(stroke);
g.draw(shape);
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
        return 12.0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     * 
     * @return The Y of the bounding box of the original SVG image.
     */
    public static double getOrigY() {
        return 2.6598002910614014;
    }

	/**
	 * Returns the width of the bounding box of the original SVG image.
	 * 
	 * @return The width of the bounding box of the original SVG image.
	 */
	public static double getOrigWidth() {
		return 26.0;
	}

	/**
	 * Returns the height of the bounding box of the original SVG image.
	 * 
	 * @return The height of the bounding box of the original SVG image.
	 */
	public static double getOrigHeight() {
		return 44.3401985168457;
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

