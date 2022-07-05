/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.multithumbslider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXMultiThumbSlider;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.multislider.MultiThumbModel;
import org.jdesktop.swingx.multislider.Thumb;

import swingset.AbstractDemo;

/*
Die Implementierung des JXMultiThumbSlider wurde wahrscheinlich nicht abgeschlossen.
Es ist dokumentiert:
 * Thumbs have no default visual representation. 
Also keine Renderer!

 * To customize the look of the thumbs and the track behind the thumbs you must provide 
 * a ThumbRenderer and a TrackRenderer implementation. 
Für ThumbRenderer und TrackRenderer gibt es nur Interfaces

In org.jdesktop.swingx.plaf.basic.BasicMultiThumbSliderUI gibt es dann doch sehr rudimantäre Renderer.

- Der Treck ist eine linie
- Die Thumbs sind simple grüne Rauten (Polygon)

 */
/**
 * A demo for the {@code JXMultiThumbSlider}.
 *
 * @author Karl George Schaefer  (inited)
 * @author EUG https://github.com/homebeaver (implement)
 */
//@DemoProperties(
//    value = "JXMultiThumbSlider Demo",
//    category = "Controls",
//    description = "Demonstrates JXMultiThumbSlider, a control containing one or more thumbs on the same slider.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/multithumbslider/MultiThumbSliderDemo.java",
//        "org/jdesktop/swingx/demos/multithumbslider/resources/MultiThumbSliderDemo.properties",
//        "org/jdesktop/swingx/demos/multithumbslider/resources/MultiThumbSliderDemo.html",
//        "org/jdesktop/swingx/demos/multithumbslider/resources/images/MultiThumbSliderDemo.png"
//    }
//)
//@SuppressWarnings("serial")
////TODO complete the demo
public class MultiThumbSliderDemo extends AbstractDemo {
   
	private static final long serialVersionUID = 1291497702141918848L;
	private static final Logger LOG = Logger.getLogger(MultiThumbSliderDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXMultiThumbSlider, a control containing one or more thumbs on the same slider.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new MultiThumbSliderDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    /**
     * MultiThumbSliderDemo Constructor
     * 
     * @param frame controller Frame
     */
    public MultiThumbSliderDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);

    	// create labeled layout
//    	labeledLayout(SwingConstants.CENTER);
    	
    	createSliderAt(BorderLayout.NORTH);
    }

    private JXMultiThumbSlider<Blue> slider = null;
    static private final String[] LAYOUT_CONSTRAINTS = { BorderLayout.WEST, BorderLayout.EAST 
    		, BorderLayout.NORTH, BorderLayout.SOUTH
    		, BorderLayout.CENTER };
    
    private void createSliderAt(String layoutConstraint) {
    	LOG.info("JXMultiThumbSlider at "+ layoutConstraint);
    	slider = new JXMultiThumbSlider<Blue>();
        slider.getModel().addThumb(Blue.LIGHT_GRAY, new Blue(Color.LIGHT_GRAY));
        slider.getModel().addThumb(Blue.GRAY, new Blue(Color.GRAY));
        
        for(int c=0; c<LAYOUT_CONSTRAINTS.length; c++) {
        	String lc = LAYOUT_CONSTRAINTS[c];
        	if(lc.equals(layoutConstraint)) {
        		add(slider, lc);
            } else {
            	add(jSliderOrLabel(lc), lc);
        	}
        }
    }
    private JComponent jSliderOrLabel(String layoutConstraint) {
    	if(BorderLayout.SOUTH==layoutConstraint) {
        	JXPanel pane = new JXPanel();
        	pane.setBorder(BorderFactory.createLineBorder(Color.RED));
        	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        	Box box = Box.createVerticalBox();
        	
            final JSlider blueSlider = new JSlider(0, 255, 192); // javax.swing ( min, max, initial
            blueSlider.setExtent(10); // thumb width
            blueSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE );
            blueSlider.setForeground(Color.BLUE);
            blueSlider.setPaintTicks(true);
            blueSlider.setMajorTickSpacing(50);
//            coreSlider.setMinimum(0);
//            coreSlider.setMaximum(255);
            blueSlider.setPaintLabels( true );
            blueSlider.addChangeListener( ae -> {
            	slider.getModel().getThumbAt(0).setPosition(blueSlider.getValue()/255f);
            });
            box.add(blueSlider);
            
            box.add(Box.createRigidArea(VGAP5));
            
            final JSlider redSlider = new JSlider(0, 255, 128); // javax.swing ( min, max, initial
            redSlider.setExtent(10); // thumb width
            redSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE );
//            LOG.info(" setCreatedDoubleBuffer ============== "+redSlider.isDoubleBuffered());
//            redSlider.setDoubleBuffered(true);
            redSlider.setForeground(Color.RED);
            redSlider.setPaintTicks(true);
            redSlider.setMajorTickSpacing(50);
            redSlider.setPaintLabels( true );
            redSlider.addChangeListener( ae -> {
            	slider.getModel().getThumbAt(1).setPosition(redSlider.getValue()/255f);
            });
            box.add(redSlider);
            
        	pane.add(box);
        	return pane;
        	//add(pane, layoutConstraint);
    	} else {
        	JXLabel l = new JXLabel(layoutConstraint, SwingConstants.CENTER);
        	l.setBorder(BorderFactory.createLineBorder(Color.RED));
        	return l;
//        	add(l, layoutConstraint);
    	}
    }

    private void labeledLayout(int horizontalAlignment) {
    	add(new JXLabel("WEST", horizontalAlignment), BorderLayout.WEST);
    	add(new JXLabel("EAST", horizontalAlignment), BorderLayout.EAST);
    	
    	add(new JXLabel("NORTH", horizontalAlignment), BorderLayout.NORTH);
        add(new JXLabel("CENTER", horizontalAlignment), BorderLayout.CENTER);
        add(new JXLabel("SOUTH", horizontalAlignment), BorderLayout.SOUTH);
    }
    
    @Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}

    // inner classes:
    private class Blue extends Color { 
    	
    	static public final float LIGHT_GRAY = (float)(Color.LIGHT_GRAY.getBlue())/255f; // 192/255
    	static public final float GRAY = (float)(Color.GRAY.getBlue())/255f; // 128/255
    	static public final float BLUE = (float)(Color.BLUE.getBlue())/255f; // 255/255
    	
    	public Blue(Color c) {
    		super(c.getRGB());
    	}
    	public Blue(int rgb) {
    		super(rgb);
    	}
    	
        /**
         * Returns the blue components of the {@code Color}.
         */
    	float blue() {
    		return ((float)super.getBlue())/255f;
    	}
    }
    
    private class BlueThumb extends Thumb<Blue> {

		public BlueThumb(Blue thumb, MultiThumbModel<Blue> model) {
			super(model);
			super.setObject(thumb);
			super.setPosition(thumb.blue());
		}
    	
    }
    
}
