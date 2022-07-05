/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

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
     * @param args params
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
    
	private JXPanel xpanel;
	private boolean opaque = false;
	
	// controller:
    private JSlider alphaSlider;
	// controller prop name
	private static final String SLIDER = "alphaSlider";
    
    /**
     * Constructor
     * 
     * @param controllerFrame controller Frame
     */
    public XPanelDemo(Frame controllerFrame) {
    	super(new BorderLayout());
    	controllerFrame.setTitle(getBundleString("frame.title", DESCRIPTION));
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
        // name Cobalite in https://icolorpalette.com/color/9999ff
        // in https://colornames.org/color/9999ff : Star Dust Purple  
//        alphaSlider.setBackground(new Color(153, 153, 255)); 
        alphaSlider.setOpaque(Boolean.parseBoolean(getBundleString(SLIDER+".opaque", Boolean.toString(false))));
        alphaSlider.setPaintLabels(Boolean.valueOf(getBundleString(SLIDER+".paintLabels", Boolean.toString(true))));
        alphaSlider.setValue(Integer.parseInt(getBundleString(SLIDER+".value", "50")));
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        // can we fill these labels from the properties file? Yes, we can!
        String labelTable = getBundleString(SLIDER+".labelTable");
        LOG.info("alphaSlider.labelTable:"+labelTable);
        // TODO: Muss die Beschriftung nicht andersrum sein?!
        labels.put(alphaSlider.getMinimum(), new JLabel("Transparent"));
        labels.put(alphaSlider.getMaximum(), new JLabel("Opaque"));
        alphaSlider.setLabelTable(labels);
        alphaSlider.setValue(20); xpanel.setAlpha(0.2f);
        alphaSlider.addChangeListener(this); // see method stateChanged
        
        // slider : WEST + VERTICAL damit er nicht von panel verdeckt wird
        panel.add(new JLabel(" "), BorderLayout.NORTH);
        panel.add(alphaSlider, BorderLayout.WEST);
        alphaSlider.setOrientation(JSlider.VERTICAL);

    	return panel;
	}

    private static final String IMG_PATH = "splitpane/"; // prefix dir
    private void createXPanelDemo() {
        xpanel = new JXPanel();
        xpanel.setName("panel");
        
    	// malt anfangs mit thumbColor (grau/OceanTheme.PRIMARY2 [r=163,g=184,b=204] "#A3B8CC")
    	// Northern Pond , Mood Cloud Blue 
//        setBackground(UIManager.getDefaults().getColor("ScrollBar.thumb"));
        
        opaque = Boolean.parseBoolean(getBundleString("panel.opaque", Boolean.toString(false)));
        xpanel.setOpaque(opaque);
        
        //panel.add(new JSplitPane());
        JLabel earth = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"earth.jpg"));
        earth.setMinimumSize(new Dimension(20, 20));

//        JLabel moon = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"moon.jpg"));
        JLabel moon = new JLabel(StaticUtilities.createImageIcon("images/XPanelDemo.png"));
        moon.setMinimumSize(new Dimension(20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, moon, earth);
//        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200); // BUG?, nein ausserhalb vom xpanel, wenn moon.jpg verwedet
        xpanel.add(splitPane);
        add(xpanel);
    }
    
    /**
     * implements ChangeListener
     */
	@Override
	public void stateChanged(ChangeEvent ce) {
//		if(changeSize) return;
		synchronized (this) {
//			changeSize = true;
//	        panel.setOpaque(opaque);
			int alpha = alphaSlider.getValue();
			LOG.fine("XPanelDemo.Opaque="+xpanel.isOpaque()+", alpha old/new:"+xpanel.getAlpha()+"/"+alpha + ", ChangeEvent:"+ce);
			// alphaSlider liefert int 0..100, setAlpha erwartet float 0..1
			if(alpha==1) {
		        xpanel.setOpaque(opaque);
//			} else {
//				panel.setAlpha(1f*alpha/100);
			}
			xpanel.setAlpha(1f*alpha/100);
			SwingUtilities.updateComponentTreeUI(this);
//			changeSize = false;
		}
	}
}
