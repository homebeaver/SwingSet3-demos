/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.multithumbslider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
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
import org.jdesktop.swingx.color.GradientTrackRenderer;
import org.jdesktop.swingx.multislider.ThumbDataEvent;
import org.jdesktop.swingx.multislider.ThumbDataListener;
import org.jdesktop.swingx.util.PaintUtils;

import swingset.AbstractDemo;

/*
Die Implementierung des JXMultiThumbSlider wurde wahrscheinlich nicht abgeschlossen.
Es ist dokumentiert:
 * Thumbs have no default visual representation. 
Also keine Renderer!

 * To customize the look of the thumbs and the track behind the thumbs you must provide 
 * a ThumbRenderer and a TrackRenderer implementation. 
Für ThumbRenderer und TrackRenderer gibt es Interfaces
und zwei rudimentäre Implementierungen
- 1. in package org.jdesktop.swingx.color
	class GradientThumbRenderer extends JComponent implements ThumbRenderer
	class GradientTrackRenderer extends JComponent implements TrackRenderer
- 2. in package org.jdesktop.swingx.plaf.basic als inner class :
    private class BasicMultiThumbSliderUI$BasicThumbRenderer extends JComponent implements ThumbRenderer {
    private class BasicMultiThumbSliderUI$BasicTrackRenderer extends JComponent implements ThumbRenderer {

- Der Track ist eine linie
- Die Thumbs sind simple grüne Rauten (Polygon)

mit ThumbDataListener werden thumbs aus JXMultiThumbSlider mit JSlider sliders synchronisiert,
so dass man mit beiden rumspielen kann

 */
/**
 * A demo for the {@code JXMultiThumbSlider}.
 *
 * @author Karl George Schaefer  (inited)
 * @author EUG https://github.com/homebeaver (implement)
 */
//@DemoProperties(
//    value = "JXMultiThumbSlider Demo",
//    category = "Controls", ===> DECORATORS
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
public class MultiThumbSliderDemo extends AbstractDemo implements ThumbDataListener {
   
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

    private JXMultiThumbSlider<Color> slider = null;
    private List<JSlider> sliders = new ArrayList<JSlider>();
    static private final String[] LAYOUT_CONSTRAINTS = 
    	{ BorderLayout.WEST, BorderLayout.EAST 
    	, BorderLayout.NORTH, BorderLayout.SOUTH
    	, BorderLayout.CENTER };
    
    private void createSliderAt(String layoutConstraint) {
    	LOG.info("JXMultiThumbSlider at "+ layoutConstraint);
//    	slider = new JXMultiThumbSlider<Blue>();
    	// --------- wie in JXGradientChooser + JXGradientChooser.ChangeAlphaListener
        slider = new JXMultiThumbSlider<Color>();
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(100,35)); // default ist 60,16
        slider.getModel().setMinimumValue(0f);
        slider.getModel().setMaximumValue(1.0f);
        
//        slider.getModel().addThumb(0,Color.black);
//        slider.getModel().addThumb(0.5f,Color.red);
//        slider.getModel().addThumb(1.0f,Color.white);
        // calc new color and set it on thumb
        Color col = Color.BLUE;
        col = PaintUtils.setAlpha(col, 255*50/100); // 50% alpha (transparency), damit checker_paint durchscheint
//        thumb.setObject(col);
        slider.getModel().addThumb(0, col);
        slider.getModel().addThumb(1.0f, Color.RED);
        
        /*
         * EUG used indirectly in in (swingx-beaninfo) test org.jdesktop.beans.editors.PaintPickerDemo
         */
        LOG.info("ThumbRenderer and TrackRenderer ...");
        //slider.setThumbRenderer(new GradientThumbRenderer());
        slider.setTrackRenderer(new GradientTrackRenderer());
        //slider.addMultiThumbListener(new StopListener());
    	// --------- wie in JXGradientChooser<<<< 

//    	GradientTrackRenderer trackRenderer = new GradientTrackRenderer();
//    	GradientThumbRenderer thumbRenderer = new GradientThumbRenderer();
//    	slider.setTrackRenderer(trackRenderer);
//    	slider.setThumbRenderer(thumbRenderer);
//        slider.getModel().addThumb(LIGHT_GRAY, new Blue(Color.LIGHT_GRAY));
//        slider.getModel().addThumb(GRAY, new Blue(Color.GRAY));
        
        for(int c=0; c<LAYOUT_CONSTRAINTS.length; c++) {
        	String lc = LAYOUT_CONSTRAINTS[c];
        	if(lc.equals(layoutConstraint)) {
        		add(slider, lc);
            } else {
            	add(jSliderOrLabel(lc), lc);
        	}
        }
        slider.getModel().addThumbDataListener(this);
    }
    
//    private class ThumbHandler implements ThumbDataListener {
	@Override
	public void valueChanged(ThumbDataEvent e) {
		LOG.info(">>>>>>>>> Thumb[" + e.getIndex() + "]:" + e.getThumb());
	}

	@Override
	public void positionChanged(ThumbDataEvent e) {
		LOG.fine(">>>>>>>>> Thumb[" + e.getIndex() + "]:" + e.getThumb());
		int v = (int) (255 * e.getThumb().getPosition()); // float
		sliders.get(e.getIndex()).setValue(v);
	}

	@Override
	public void thumbAdded(ThumbDataEvent e) {
		LOG.info(">>>>>>>>> Thumb[" + e.getIndex() + "]:" + e.getThumb());
	}

	@Override
	public void thumbRemoved(ThumbDataEvent e) {
		LOG.info(">>>>>>>>> Thumb[" + e.getIndex() + "]:" + e.getThumb());
	}  	
//    }

    private JComponent jSliderOrLabel(String layoutConstraint) {
    	if(BorderLayout.SOUTH==layoutConstraint) {
        	JXPanel pane = new JXPanel();
        	pane.setBorder(BorderFactory.createLineBorder(Color.RED));
        	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        	Box box = Box.createVerticalBox();
        	
            final JSlider blueSlider = new JSlider(0, 255, 0); // javax.swing ( min, max, initial
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
            sliders.add(blueSlider);
            
            box.add(Box.createRigidArea(VGAP5));
            
            final JSlider redSlider = new JSlider(0, 255, 255); // javax.swing ( min, max, initial
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
            sliders.add(redSlider);
            
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

}
