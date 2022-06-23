/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xpanel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXPanel}.
 *
 * @author Karl George Schaefer
 * @author Richard Bair (original JXPanelTranslucencyDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXPanel Demo",
//    category = "Containers",
//    description = "Demonstrates JXPanel, a container supporting Painters and transparency.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/xpanel/XPanelDemo.java",
//        "org/jdesktop/swingx/demos/xpanel/resources/XPanelDemo.properties",
//        "org/jdesktop/swingx/demos/xpanel/resources/XPanelDemo.html",
//        "org/jdesktop/swingx/demos/xpanel/resources/images/XPanelDemo.png"
//    }
//)
public class XPanelDemo extends AbstractDemo implements ChangeListener {
	
	private static final long serialVersionUID = -7559775313510139445L;
	private static final Logger LOG = Logger.getLogger(XPanelDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXPanel, a container supporting Painters and transparency.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new XPanelDemo(controller);
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
    
	private JXPanel panel;
    private JSlider alphaSlider;
	// controller prop name
	private static final String SLIDER = "alphaSlider";
    
    /**
     * Constructor
     */
    public XPanelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	createXPanelDemo();
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel(new BorderLayout());

        alphaSlider = new JSlider();
        alphaSlider.setName(SLIDER);
/* prpos:
panel.opaque=false
alphaSlider.background=153, 153, 255
alphaSlider.paintLabels=true
alphaSlider.value=100
alphaSlider.opaque=false
 */
        alphaSlider.setOpaque(Boolean.parseBoolean(getBundleString(SLIDER+".opaque", Boolean.toString(false))));
        alphaSlider.setPaintLabels(Boolean.valueOf(getBundleString(SLIDER+".paintLabels", Boolean.toString(true))));
        alphaSlider.setValue(Integer.parseInt(getBundleString(SLIDER+".value", "50")));
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        // can we fill these labels from the properties file? Yes, we can!
        String labelTable = getBundleString(SLIDER+".labelTable");
        LOG.info("alphaSlider.labelTable:"+labelTable);
        labels.put(alphaSlider.getMinimum(), new JLabel("Transparent"));
        labels.put(alphaSlider.getMaximum(), new JLabel("Opaque"));
        alphaSlider.setLabelTable(labels);
        alphaSlider.addChangeListener(this); // see method stateChanged
        
        panel.add(alphaSlider, BorderLayout.SOUTH); // nicht CENTER, damit der Controller ganz sichtbar ist
        // TODO: besser vertikal!

    	return panel;
	}


    private void createXPanelDemo() {
        setBackground(javax.swing.UIManager.getDefaults().getColor("ScrollBar.thumb"));
        
        panel = new JXPanel();
        panel.setName("panel");
        // TODO:
//        panel.setOpaque(Boolean.parseBoolean(getBundleString("panel.opaque", Boolean.toString(false))));
        panel.add(new JSplitPane());
        add(panel);
    }
    
    /**
     * implements ChangeListener
     */
	@Override
	public void stateChanged(ChangeEvent ce) {
//		if(changeSize) return;
		synchronized (this) {
//			changeSize = true;
			int alpha = alphaSlider.getValue();
			LOG.info("XPanelDemo.Alpha old/new:"+panel.getAlpha()+"/"+alpha + ", ChangeEvent:"+ce);
			// alphaSlider liefert int 0..100, setAlpha erwartet float 0..1
			panel.setAlpha(1f/100*alpha);
			SwingUtilities.updateComponentTreeUI(this);
//			changeSize = false;
		}
	}
}
