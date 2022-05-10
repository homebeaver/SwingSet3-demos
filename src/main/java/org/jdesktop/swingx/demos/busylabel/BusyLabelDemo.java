/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.busylabel;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.binding.FactorConverter;
import org.jdesktop.swingx.graphics.BlendComposite;
import org.jdesktop.swingx.graphics.BlendComposite.BlendingMode;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;

import swingset.AbstractDemo;

//import com.sun.swingset3.DemoProperties;

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
//TODO demo incomplete
//@SuppressWarnings("serial")
public class BusyLabelDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 7645879039288285071L;
	private static final Logger LOG = Logger.getLogger(BusyLabelDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXBusyLabel, a control for identifying busy periods";

	private JXBusyLabel label;
    
    // Controller:
    private JSlider speedSlider;   
    private JSlider pointsSlider;
    private JSlider trailSlider;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JFrame frame = new JFrame(BusyLabelDemo.class.getAnnotation(DemoProperties.class).value());
//                
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.getContentPane().add(new BusyLabelDemo());
//                frame.setPreferredSize(new Dimension(800, 600));
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
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
			}		
    	});
    }
  
    /**
     * HighlighterDemo Constructor
     */
	public BusyLabelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	setBackgroundPainter(new MattePainter(PaintUtils.BLUE_EXPERIENCE, true));

        //TODO uncomment when SwingX #999 is fixed
//      label = new JXBusyLabel(); // default 26 by 26 points
    	label = new JXBusyLabel(new Dimension(50, 50));
    	label.setName("busyLabel");
    	label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
    	label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
    	label.setBusy(true);
//    	label.setDelay(ABORT);.getBusyPainter().setDe.setPoints(pointsSlider.getValue());
    	add(label, BorderLayout.EAST);
//    	add(label);

//    public BusyLabelDemo() {
//        super(new BorderLayout());
//        
//        createBusyLabelDemo();
//        
//        Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(this);
//        
//        bind();
    }

	@Override
	public JXPanel getControlPane() {
    	JXPanel controls = new JXPanel(new VerticalLayout());
    	controls.setName("controlPanel");

        speedSlider = new JSlider();
        speedSlider.setName("speedSlider");
/* expected props:
speedSlider.opaque=false
speedSlider.paintLabels=true
speedSlider.minimum=1
speedSlider.maximum=50
speedSlider.value=41
 */
        speedSlider.setOpaque(Boolean.parseBoolean(getBundleString("speedSlider.opaque")));
        speedSlider.setPaintLabels(Boolean.parseBoolean(getBundleString("speedSlider.paintLabels")));
        speedSlider.setMinimum(Integer.parseInt(getBundleString("speedSlider.minimum")));
        speedSlider.setMaximum(Integer.parseInt(getBundleString("speedSlider.maximum")));
        speedSlider.setValue(Integer.parseInt(getBundleString("speedSlider.value")));
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
        labels.put(speedSlider.getMinimum(), new JLabel("Faster"));
        labels.put(speedSlider.getMaximum(), new JLabel("Slower"));
//        speedSlider.setPaintLabels( true );
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
        labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
        labels.put(1, new JLabel("Short Trail"));
        labels.put(20, new JLabel("Long Trail"));
        trailSlider.setLabelTable(labels);
        controls.add(trailSlider);

        return controls;
    }

//    private void createBusyLabelDemo() {
//        setBackgroundPainter(new MattePainter(PaintUtils.BLUE_EXPERIENCE, true));
//        
//        //TODO uncomment when SwingX #999 is fixed
////        label = new JXBusyLabel();
//        label = new JXBusyLabel(new Dimension(50, 50));
//        label.setName("busyLabel");
//        label.getBusyPainter().setHighlightColor(new Color(44, 61, 146).darker());
//        label.getBusyPainter().setBaseColor(new Color(168, 204, 241).brighter());
//        label.setBusy(true);
//        add(label, BorderLayout.NORTH);
//        
//        JPanel control = new JPanel(new VerticalLayout());
//        control.setName("controlPanel");
//        
//        speedSlider = new JSlider();
//        speedSlider.setName("speedSlider");
//        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
//        //TODO can we fill these labels from the properties file?
//        labels.put(1, new JLabel("Faster"));
//        labels.put(50, new JLabel("Slower"));
//        speedSlider.setLabelTable(labels);
//        control.add(speedSlider);
//        
//        pointsSlider = new JSlider();
//        pointsSlider.setName("pointsSlider");
//        labels = new Hashtable<Integer, JComponent>();
//        //TODO can we fill these labels from the properties file?
//        labels.put(1, new JLabel("Fewer Points"));
//        labels.put(50, new JLabel("More Points"));
//        pointsSlider.setLabelTable(labels);
//        control.add(pointsSlider);
//        
//        trailSlider = new JSlider();
//        trailSlider.setName("trailSlider");
//        labels = new Hashtable<Integer, JComponent>();
//        //TODO can we fill these labels from the properties file?
//        labels.put(1, new JLabel("Short Trail"));
//        labels.put(20, new JLabel("Long Trail"));
//        trailSlider.setLabelTable(labels);
//        control.add(trailSlider);
//
//        add(control);
//    }
    
    @SuppressWarnings("unchecked")
    private void bind() {
        Binding b = Bindings.createAutoBinding(READ,
                speedSlider, BeanProperty.create("value"),
                label, BeanProperty.create("delay"));
        b.setConverter(new FactorConverter<Integer>(10));
        b.bind();
        
//        Bindings.createAutoBinding(READ,
//                pointsSlider, BeanProperty.create("value"),
//                label, ELProperty.create("${busyPainter.points}")).bind();
//        
//        Bindings.createAutoBinding(READ,
//                trailSlider, BeanProperty.create("value"),
//                label, ELProperty.create("${busyPainter.trailLength}")).bind();
    }
}
