/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.busylabel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.geom.RoundRectangle2D;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXBusyLabel}.
 *
 * @author EUG https://github.com/homebeaver (reorg)
 * @author Karl George Schaefer
 * @author rah003 (original JXBusyLabelDemoPanel)
 */
//@DemoProperties(
//    value = "JXBusyLabel Demo",
//    category = "Decorators",
//    description = "Demonstrates JXBusyLabel, a control for identifying busy periods",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/busylabel/BusyLabelDemo.java",
//        "org/jdesktop/swingx/demos/busylabel/resources/BusyLabelDemo.properties",
//        "org/jdesktop/swingx/demos/busylabel/resources/BusyLabelDemo.html",
//        "org/jdesktop/swingx/demos/busylabel/resources/images/BusyLabelDemo.png"
//    }
//)
public class BusyLabelDemo extends AbstractDemo implements ChangeListener {
	
	private static final long serialVersionUID = 7645879039288285071L;
	private static final Logger LOG = Logger.getLogger(BusyLabelDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXBusyLabel, a control for identifying busy periods";

	// controller prop name
	private static final String SIZE_SLIDER = "sizeSlider";
	
	private JXBusyLabel label;
//	private boolean changeSize = false;
    
    // Controller:
    private JSlider sizeSlider;   
    private JSlider speedSlider;   
    private JSlider pointsSlider;
    private JSlider trailSlider;
    
    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new BusyLabelDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }
  
    /**
     * HighlighterDemo Constructor
     * 
     * @param frame controller Frame
     */
	public BusyLabelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// use GradientPaint for Background:
    	setBackgroundPainter(new MattePainter(PaintUtils.BLUE_EXPERIENCE, true));

        // uncomment when SwingX #999 ? is fixed:
//    	label = new JXBusyLabel(); // default 26 by 26 points
    	label = new JXBusyLabel(new Dimension(50, 50)); // looks smarter
    	label.setName("busyLabel");
    	LOG.info("BLUE_EXPERIENCE.Color1:"+PaintUtils.BLUE_EXPERIENCE.getColor1()); // [r=168,g=204,b=241]
    	LOG.info("BLUE_EXPERIENCE.Color2:"+PaintUtils.BLUE_EXPERIENCE.getColor2()); // [r=44,g=61,b=146]
    	LOG.info("BaseColor:"+label.getBusyPainter().getBaseColor()); // [r=192,g=192,b=192]
    	LOG.info("HighlightColor:"+label.getBusyPainter().getHighlightColor()); // [r=51,g=51,b=51]
    	label.getBusyPainter().setHighlightColor(PaintUtils.BLUE_EXPERIENCE.getColor2().darker());
    	label.getBusyPainter().setBaseColor(PaintUtils.BLUE_EXPERIENCE.getColor1().brighter());
//    	label.getBusyPainter().setHighlightColor(label.getBusyPainter().getHighlightColor().darker());
//    	label.getBusyPainter().setBaseColor(label.getBusyPainter().getBaseColor().brighter());
    	label.setBusy(true);
    	add(label, BorderLayout.EAST); // EAST, damit controls den label nicht überdeckt
    	
    	// default example:
    	JXBusyLabel busyLabel = new JXBusyLabel();
    	LOG.info("default PreferredSize:"+busyLabel.getPreferredSize()); // java.awt.Dimension[width=26,height=26]
//    	add(busyLabel, BorderLayout.WEST);
    	busyLabel.setBusy(true);
    	
    	// more complicated example:
    	JXBusyLabel mcLabel = new JXBusyLabel(new Dimension(100,84));
    	BusyPainter painter = new BusyPainter(
//    			new Rectangle2D.Float(0, 0, 13.500001f, 1),  // point shape, create using awt.geom or ShapeFactory:
    			ShapeFactory.createEllipticalPoint(13, 10),
//    			ShapeFactory.createLinearPoint(13, 10),
//    			ShapeFactory.createSquarePoint(13, 10),
//    			ShapeFactory.createRectangularPoint(13, 10),
    			new RoundRectangle2D.Float(12.5f, 12.5f, 59.0f, 59.0f, 10, 10)  // trajectory 
    			);
    	painter.setTrailLength(5);
    	painter.setPoints(31);
    	painter.setFrame(1);
    	mcLabel.setPreferredSize(new Dimension(100,84));
//    	mcLabel.setIcon(new EmptyIcon(100,84));
    	mcLabel.setBusyPainter(painter);
//     	add(mcLabel, BorderLayout.CENTER);
     	mcLabel.setBusy(true);
    	
    	JPanel p = createHorizontalPanel(false); // true == loweredBorder from super
//    	JPanel p = createVerticalPanel(false);
    	p.add(Box.createRigidArea(HGAP30));
    	p.add(busyLabel);
    	p.add(Box.createRigidArea(HGAP30));
    	p.setBorder(new TitledBorder(null, "default and more complicated example", TitledBorder.LEFT, TitledBorder.TOP));
    	p.add(mcLabel);
    	add(p, BorderLayout.SOUTH);   	
    }

	@Override
	public JXPanel getControlPane() {
    	JXPanel controls = new JXPanel(new VerticalLayout());
    	controls.setName("controlPanel");

        sizeSlider = new JSlider();
        sizeSlider.setName(SIZE_SLIDER);
        sizeSlider.setOpaque(Boolean.parseBoolean(getBundleString(SIZE_SLIDER+".opaque", Boolean.toString(false))));
        sizeSlider.setPaintLabels(Boolean.valueOf(getBundleString(SIZE_SLIDER+".paintLabels", Boolean.toString(true))));
        sizeSlider.setMinimum(Integer.parseInt(getBundleString(SIZE_SLIDER+".minimum", "10")));
        sizeSlider.setMaximum(Integer.parseInt(getBundleString(SIZE_SLIDER+".maximum", "200")));
        sizeSlider.setValue(Integer.parseInt(getBundleString(SIZE_SLIDER+".value", "50")));
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        String labelTable = getBundleString(SIZE_SLIDER+".labelTable");
        LOG.info("sizeSlider.labelTable:"+labelTable);
        labels.put(sizeSlider.getMinimum(), new JLabel("Smaller"));
        labels.put(sizeSlider.getMaximum(), new JLabel("Bigger"));
        sizeSlider.setLabelTable(labels);
        sizeSlider.addChangeListener(this); // see method stateChanged
        controls.add(sizeSlider);

        speedSlider = new JSlider();
        speedSlider.setName("speedSlider");
/* expected props:
speedSlider.opaque=false
speedSlider.paintLabels=true
speedSlider.minimum=1
speedSlider.maximum=50
speedSlider.value=41
 */
        // use Boolean.valueOf or Boolean.parseBoolean:
        speedSlider.setOpaque(Boolean.parseBoolean(getBundleString("speedSlider.opaque")));
        speedSlider.setPaintLabels(Boolean.valueOf(getBundleString("speedSlider.paintLabels")));
        speedSlider.setMinimum(Integer.parseInt(getBundleString("speedSlider.minimum")));
        speedSlider.setMaximum(Integer.parseInt(getBundleString("speedSlider.maximum")));
        speedSlider.setValue(Integer.parseInt(getBundleString("speedSlider.value")));
        labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
        // see: https://stackoverflow.com/questions/1380343/parse-map-from-properties-files
        // then use: speedSlider.labelTable=1,Faster;50,Slower
//        String labelTable = getBundleString("speedSlider.labelTable");
//        LOG.info("labelTable:"+labelTable);
        labels.put(speedSlider.getMinimum(), new JLabel("Faster"));
        labels.put(speedSlider.getMaximum(), new JLabel("Slower"));
        speedSlider.setLabelTable(labels);
        speedSlider.addChangeListener( ce -> {
        	LOG.info("BusyPainter.Delay:"+speedSlider.getValue());
        	label.setDelay(speedSlider.getValue()*10);
    	});
        controls.add(speedSlider);

        pointsSlider = new JSlider();
        pointsSlider.setName("pointsSlider");
/* expected props:
pointsSlider.opaque=false
pointsSlider.paintLabels=true
pointsSlider.minimum=1
pointsSlider.maximum=50
pointsSlider.value=12
 */
        pointsSlider.setOpaque(Boolean.parseBoolean(getBundleString("pointsSlider.opaque")));
        pointsSlider.setPaintLabels(Boolean.parseBoolean(getBundleString("pointsSlider.paintLabels")));
        pointsSlider.setMinimum(Integer.parseInt(getBundleString("pointsSlider.minimum")));
        pointsSlider.setMaximum(Integer.parseInt(getBundleString("pointsSlider.maximum")));
        pointsSlider.setValue(Integer.parseInt(getBundleString("pointsSlider.value")));
        labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
        labels.put(pointsSlider.getMinimum(), new JLabel("Fewer Points"));
        labels.put(pointsSlider.getMaximum(), new JLabel("More Points"));
        pointsSlider.setLabelTable(labels);
        pointsSlider.addChangeListener( ce -> {
        	LOG.info("BusyPainter.Points:"+pointsSlider.getValue());
        	label.getBusyPainter().setPoints(pointsSlider.getValue());
    	});
        controls.add(pointsSlider);

        trailSlider = new JSlider();
        trailSlider.setName("trailSlider");
/* expected props:
trailSlider.opaque=false
trailSlider.paintLabels=true
trailSlider.minimum=1
trailSlider.maximum=20
trailSlider.value=3
 */
        trailSlider.setOpaque(Boolean.parseBoolean(getBundleString("trailSlider.opaque")));
        trailSlider.setPaintLabels(Boolean.parseBoolean(getBundleString("trailSlider.paintLabels")));
        trailSlider.setMinimum(Integer.parseInt(getBundleString("trailSlider.minimum")));
        trailSlider.setMaximum(Integer.parseInt(getBundleString("trailSlider.maximum")));
        trailSlider.setValue(Integer.parseInt(getBundleString("trailSlider.value")));
        labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
        labels.put(trailSlider.getMinimum(), new JLabel("Short Trail"));
        labels.put(trailSlider.getMaximum(), new JLabel("Long Trail"));
        trailSlider.setLabelTable(labels);
        trailSlider.addChangeListener( ce -> {
        	LOG.info("BusyPainter.TrailLength:"+trailSlider.getValue());
        	label.getBusyPainter().setTrailLength(trailSlider.getValue());
    	});
        controls.add(trailSlider);

        return controls;
    }

    /**
     * implements ChangeListener
     */
	@Override
	public void stateChanged(ChangeEvent ce) {
//		if(changeSize) return;
		synchronized (this) {
//			changeSize = true;
			int size = sizeSlider.getValue();
			LOG.info("BusyPainter.Size aka Dimension:"+size + ", ChangeEvent:"+ce);
			remove(label);
			label = new JXBusyLabel(new Dimension(size, size));
			add(label, BorderLayout.EAST);
			label.setBusy(true);
			SwingUtilities.updateComponentTreeUI(this);
//			changeSize = false;
		}
	}

    // wie in ButtonDemo:
    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
    private JXPanel createHorizontalPanel(boolean threeD) {
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }
    private JXPanel createVerticalPanel(boolean threeD) {
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

}
