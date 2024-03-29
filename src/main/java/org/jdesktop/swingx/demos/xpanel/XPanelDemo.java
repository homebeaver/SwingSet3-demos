/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xpanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.ChevronsIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.pushingpixels.trident.api.Timeline;

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
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
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
    	});
    }
    
	private JXPanel xpanel;
	private boolean opaque = false;
	
    // Animation
    Timeline timeline;
    public void setAlphaProp(float newValue) {
//        LOG.info("timeline pulse " + xpanel.getAlpha() + " -> " + newValue);
        xpanel.setAlpha(newValue);
        alphaSlider.setValue((int)(newValue*255+0.5));
    }
    public void createAnimation(long duration, float to) {
    	Timeline.builder(this)
    		.addPropertyToInterpolate("alphaProp", 0.0f, to)
    		.setDuration(duration)
    		.play(); // fade in
    	
    	timeline = Timeline.builder(this)
			.addPropertyToInterpolate("alphaProp", 0.0f, 1.0f)
			.setDuration(duration)
			.build();
    	LOG.info("Animation Duration = " + timeline.getDuration());
    }
	
	// controller:
    private JButton fadeIn;
    private JButton fadeOut;
    private JSlider alphaSlider;
	// controller prop name
	private static final String SLIDER = "alphaSlider";
    
    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public XPanelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	createXPanelDemo();
    	createAnimation(2000, 0.6f); // 2000ms , stop at 60%
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel(new BorderLayout());

        alphaSlider = new JSlider(JSlider.VERTICAL, 0, 255, 0);
//        xpanel.setAlpha(1f*40/255);
        alphaSlider.setName(SLIDER);

/* prop alphaSlider.background COBALITE not used
        alphaSlider.setBackground(new Color(153, 153, 255)); 
 */
        alphaSlider.setOpaque(Boolean.parseBoolean(getBundleString(SLIDER+".opaque", Boolean.toString(false))));
        alphaSlider.setPaintLabels(Boolean.valueOf(getBundleString(SLIDER+".paintLabels", Boolean.toString(true))));
        alphaSlider.setValue(Integer.parseInt(getBundleString(SLIDER+".value", "50")));
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        // can we fill these labels from the properties file? Yes, we can! but I do not
        String labelTable = getBundleString(SLIDER+".labelTable");
        LOG.info("alphaSlider.labelTable:"+labelTable);
        labels.put(alphaSlider.getMinimum(), new JLabel("fade out"));
        labels.put(alphaSlider.getMaximum(), new JLabel("fade in"));
        alphaSlider.setLabelTable(labels);
        alphaSlider.addChangeListener(this); // see method stateChanged
        
        // slider : WEST + VERTICAL damit er nicht von panel verdeckt wird
        panel.add(new JLabel(" "), BorderLayout.NORTH);
        panel.add(alphaSlider, BorderLayout.WEST);

        Border emptyBorder = new EmptyBorder(5,5,5,5);
    	JXPanel north = new JXPanel();
    	north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
    	north.setAlignmentX(Component.LEFT_ALIGNMENT);
    	north.setBorder(emptyBorder);
        fadeIn = new JButton();
        fadeIn.setName("fadeIn");
        fadeIn.setText(getBundleString("fadeIn.text"));
        fadeIn.setIcon(ChevronsIcon.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
        fadeIn.addActionListener( ae -> {
        	setAlphaProp(0f);
        	timeline.play(); // fade in
        });
        north.add(fadeIn);
        panel.add(north, BorderLayout.NORTH);

    	JXPanel south = new JXPanel();
    	south.setLayout(new BoxLayout(south, BoxLayout.X_AXIS));
    	south.setAlignmentX(Component.LEFT_ALIGNMENT);
    	south.setBorder(emptyBorder);
        fadeOut = new JButton();
        fadeOut.setName("fadeOut");
        fadeOut.setText(getBundleString("fadeOut.text"));
        RadianceIcon chevrons = ChevronsIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
        chevrons.setRotation(RadianceIcon.SOUTH);
        fadeOut.setIcon(chevrons);
        fadeOut.addActionListener( ae -> {
        	setAlphaProp(1f);
        	timeline.playReverse(); // fade out
        });
        south.add(fadeOut);
        panel.add(south, BorderLayout.SOUTH);

    	return panel;
	}

    private static final String IMG_PATH = "splitpane/"; // prefix dir
    private void createXPanelDemo() {
        xpanel = new JXPanel();
        xpanel.setName("panel");
        LOG.info("   ----------Font="+xpanel.getFont());
        
    	// malt anfangs mit thumbColor (grau/OceanTheme.PRIMARY2 [r=163,g=184,b=204] "#A3B8CC")
    	// Northern Pond , Mood Cloud Blue 
//        setBackground(UIManager.getDefaults().getColor("ScrollBar.thumb"));
        
    	// best practice is to use either alpha support or opacity support, but not both.
        // so turn off Opaque:
        LOG.info("               initial xpanel.isOpaque()="+xpanel.isOpaque()); // true
        opaque = Boolean.parseBoolean(getBundleString("panel.opaque", Boolean.toString(false)));
        xpanel.setOpaque(opaque);
        LOG.info("               initial xpanel.getAlpha()="+xpanel.getAlpha());
        
        //panel.add(new JSplitPane());
        JLabel earth = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"earth.jpg"));
        earth.setMinimumSize(new Dimension(20, 20));

        JLabel ourEarth = new JLabel("   our earth:");
        ourEarth.setMinimumSize(new Dimension(20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, ourEarth, earth);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        xpanel.add(splitPane);
        add(xpanel);
    }
    
    /**
     * implements ChangeListener
     */
	@Override
	public void stateChanged(ChangeEvent ce) {
		synchronized (this) {
			int alpha = alphaSlider.getValue();
			LOG.fine("XPanelDemo.Opaque="+xpanel.isOpaque()+", alpha old/new:"+xpanel.getAlpha()+"/"+alpha + ", ChangeEvent:"+ce);
			// alphaSlider int 0..255 to float 0..1
			xpanel.setAlpha(1f*alpha/255);
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}
