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
import org.jdesktop.swingx.color.GradientThumbRenderer;
import org.jdesktop.swingx.color.GradientTrackRenderer;
import org.jdesktop.swingx.multislider.Thumb;
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
    	
    	createMultiSliderAt(BorderLayout.NORTH);
    }

    static private final String[] LAYOUT_CONSTRAINTS = 
    	{ BorderLayout.WEST, BorderLayout.EAST 
    	, BorderLayout.NORTH, BorderLayout.SOUTH
    	, BorderLayout.CENTER };
    
    // NORTH: one multiSlider initialy with two thumbs:
    private JXMultiThumbSlider<Color> multiSlider = null;
    // SOUTH: two JSlider:
    private List<JSlider> sliders = new ArrayList<JSlider>();
    
    private void createMultiSliderAt(String layoutConstraint) {
    	LOG.info("JXMultiThumbSlider at "+ layoutConstraint);
//    	slider = new JXMultiThumbSlider<Blue>();
    	// --------- wie in JXGradientChooser + JXGradientChooser.ChangeAlphaListener
        multiSlider = new JXMultiThumbSlider<Color>();
        multiSlider.setOpaque(false);
        multiSlider.setPreferredSize(new Dimension(100,35)); // default ist 60,16
        multiSlider.getModel().setMinimumValue(0f);
        multiSlider.getModel().setMaximumValue(1.0f);
        
//        slider.getModel().addThumb(0,Color.black);
//        slider.getModel().addThumb(0.5f,Color.red);
//        slider.getModel().addThumb(1.0f,Color.white);
        // calc new color and set it on thumb
        Color col = Color.RED;
        col = PaintUtils.setAlpha(col, 255*50/100); // 50% alpha (transparency), damit checker_paint durchscheint
//        thumb.setObject(col);
        multiSlider.getModel().addThumb(0, col);
        multiSlider.getModel().addThumb(1.0f, Color.BLUE);
        
        /*
         * EUG used indirectly in in (swingx-beaninfo) test org.jdesktop.beans.editors.PaintPickerDemo
         */
        LOG.info("ThumbRenderer and TrackRenderer ...");
        multiSlider.setThumbRenderer(new GradientThumbRenderer());
        multiSlider.setTrackRenderer(new GradientTrackRenderer());
        //slider.addMultiThumbListener(new StopListener());
    	// --------- wie in JXGradientChooser<<<< 

        for(int c=0; c<LAYOUT_CONSTRAINTS.length; c++) {
        	String lc = LAYOUT_CONSTRAINTS[c];
        	if(lc.equals(layoutConstraint)) {
        		add(multiSlider, lc);
            } else {
            	add(createSliderOrLabelAt(lc), lc);
        	}
        }
        multiSlider.getModel().addThumbDataListener(new ThumbDataListener() {
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
        });
    }
    
    private JComponent createSliderOrLabelAt(String layoutConstraint) {
    	if(BorderLayout.SOUTH==layoutConstraint) {
        	JXPanel pane = new JXPanel();
        	pane.setBorder(BorderFactory.createLineBorder(Color.RED));
        	pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        	
        	this.multiSlider.getModel().forEach( element -> {
        		Thumb<Color> thumb = (Thumb<Color>)element;
        		Float initial = Float.valueOf(255*thumb.getPosition());
        		JSlider slider = new JSlider(0, 255, initial.intValue()); // from javax.swing (min, max, initial
                slider.setExtent(10); // thumb width ??? 
                slider.putClientProperty("JSlider.isFilled", Boolean.TRUE );
                slider.setForeground(thumb.getObject());
                slider.setPaintTicks(true);
                slider.setMajorTickSpacing(50);
                slider.setPaintLabels( true );
                slider.addChangeListener( ae -> {
                	thumb.setPosition(slider.getValue()/255f);
                });
                sliders.add(slider);
                // JSlider in Box verpacken:
            	Box box = Box.createVerticalBox();
                box.add(slider);
                box.add(Box.createRigidArea(VGAP5));
                pane.add(box);
        	});
        	return pane;
    	} else {
        	JXLabel l = new JXLabel(layoutConstraint, SwingConstants.CENTER);
        	l.setBorder(BorderFactory.createLineBorder(Color.RED));
        	return l;
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
