/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tipoftheday;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.icon.ChevronsIcon;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
import org.jdesktop.swingx.tips.DefaultTip;
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel;
import org.jdesktop.swingx.tips.TipOfTheDayModel;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.callback.TimelineCallback;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTipOfTheDay}.
 * 
 * @author Karl George Schaefer
 * @author l2fprod (original JXTipOfTheDayDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXTipOfTheDay Demo",
//    category = "Containers",
//    description = "Demonstrates JXTipOfTheDay, a container for tips and other arbitrary components.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/tipoftheday/TipOfTheDayDemo.java",
//        "org/jdesktop/swingx/demos/tipoftheday/resources/TipOfTheDayDemo.properties",
//        "org/jdesktop/swingx/demos/tipoftheday/resources/TipOfTheDayDemo.html",
//        "org/jdesktop/swingx/demos/tipoftheday/resources/images/TipOfTheDayDemo.png",
//        "org/jdesktop/swingx/plaf/basic/resources/TipOfTheDay24.gif"
//})
//@SuppressWarnings("serial")
public class TipOfTheDayDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 798075929474735684L;
	private static final Logger LOG = Logger.getLogger(TipOfTheDayDemo.class.getName());
	private static final boolean exitOnClose = true;
	private static final String DESCRIPTION = "Demonstrates JXTipOfTheDay, a container for tips and other arbitrary components.";

	private TipOfTheDayModel model;
    private JXTipOfTheDay totd;
    
    private JXHyperlink nextTipLink;
    private JXHyperlink dialogLink;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
    		JXFrame controller = new JXFrame("controller", exitOnClose);
    		AbstractDemo demo = new TipOfTheDayDemo(controller);
    		JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
    		frame.setStartPosition(StartPosition.CenterInScreen);
    		frame.getContentPane().add(demo);
    		frame.pack();
    		frame.setVisible(true);
    		
    		controller.getContentPane().add(demo.getControlPane());
    		controller.pack();
    		controller.setVisible(true);
    	});
    }

    // Animation
    Timeline timeline0;
    Timeline timeline;
	// controller:
    private JButton fadeIn;
    private JButton nextTip;
    private JSlider alphaSlider;
	// controller prop name
	private static final String SLIDER = "alphaSlider";
    public void setAlphaProp(float newValue) {
        LOG.fine("timeline pulse " + totd.getAlpha() + " -> " + newValue);
    	totd.setAlpha(newValue);
        alphaSlider.setValue((int)(newValue*255+0.5));
    }
    public void createAnimation(long duration, float to) {
    	timeline0 = new Timeline(this);
    	timeline0.addPropertyToInterpolate("alphaProp", 0.0f, to);
        timeline0.setDuration(duration);
    	LOG.info("Animation Duration at init = " + timeline0.getDuration());
    	timeline0.play(); // fade in
    	
    	timeline = new Timeline(this);
        timeline.addPropertyToInterpolate("alphaProp", 0.0f, 1.0f);
        timeline.setDuration(duration);
    	LOG.info("Animation Duration = " + timeline.getDuration());
    	
    	// implement interface TimelineCallback -- 2 methods
    	timeline.addCallback(new TimelineCallback() {

			@Override
			public void onTimelineStateChanged(TimelineState oldState, TimelineState newState, 
					float durationFraction,	float timelinePosition) {
		        LOG.info("oldState "+ oldState + " -> " + newState + " , durationFraction="+durationFraction + " , timelinePosition="+timelinePosition);
		        if(oldState==TimelineState.PLAYING_REVERSE && newState==TimelineState.DONE) {
		        	totd.nextTip();
		        	timeline.play(); // fade in
		        }
			}

			@Override
			public void onTimelinePulse(float durationFraction, float timelinePosition) {
				//LOG.info("pulseno "+ "?" + ": " + durationFraction + " => "+timelinePosition);
		        // no onTimelinePulse callback on last pulse at 1.0f !!!
			}
    		
    	});
    }

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public TipOfTheDayDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        model = createTipOfTheDayModel();

        totd = new JXTipOfTheDay(model);
        totd.setName("totd");
//      totd.setAlpha(0.8f); // !!! Ursache für BUG https://github.com/homebeaver/SwingSet/issues/27 !
        add(totd, BorderLayout.NORTH);

        nextTipLink = new JXHyperlink(); // JXHyperlink extends JButton
        nextTipLink.setName("nextTipLink");
        nextTipLink.setText(getBundleString("nextTipLink.text"));
        nextTipLink.addActionListener(totd.getActionMap().get("nextTip"));
        add(nextTipLink, BorderLayout.CENTER);
        // Do not use props:
        nextTipLink.setVerticalAlignment(SwingConstants.TOP);
        nextTipLink.setHorizontalAlignment(SwingConstants.CENTER);
        nextTipLink.setIcon(new PlayIcon());
        nextTipLink.setFocusPainted(false);
        
        createAnimation(1500, 1.0f); // 1,5sec , stop at 100% 
        // während des fade in ist der font schlecht gerendert
        // ??? TODO Zum Ende "rastet" es ein und der font ist OK, das passiert nicht bei XPanel !!!
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel(new BorderLayout());

/*
dialogLink.text=Open the Tip Of The Day as a dialog.
#dialogLink.verticalAlignment=CENTER
dialogLink.verticalAlignment=0
#dialogLink.horizontalAlignment=CENTER
dialogLink.horizontalAlignment=0
dialogLink.focusPainted=false
 */
        dialogLink = new JXHyperlink();
        dialogLink.setName("dialogLink");
        dialogLink.setText(getBundleString("dialogLink.text"));
        dialogLink.addActionListener(ae -> {
        	LOG.info("ActionEvent "+ae);
        	// props for dialog, which is a new Instance 
        	UIManager.put("TipOfTheDay.tipFont", new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        	UIManager.put("TipOfTheDay.font", new Font(Font.SERIF, Font.PLAIN, 24));
            JXTipOfTheDay dialog = new JXTipOfTheDay(model);
            dialog.setCurrentTip(0);
            dialog.showDialog(totd);
        });
        panel.add(dialogLink, BorderLayout.CENTER);

        Border emptyBorder = new EmptyBorder(5,5,5,5);
    	JXPanel north = new JXPanel();
    	north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
    	north.setAlignmentX(Component.LEFT_ALIGNMENT);
    	north.setBorder(emptyBorder);

    	nextTip = new JButton();
    	nextTip.setName("fadeOut");
    	nextTip.setText("fade out and show next tip");
    	nextTip.setHorizontalTextPosition(SwingConstants.LEFT);
        ChevronsIcon fadeOutIcon = new ChevronsIcon(ChevronsIcon.SMALL_ICON);
        fadeOutIcon.setDirection(ChevronsIcon.WEST);
        nextTip.setIcon(fadeOutIcon);
    	nextTip.addActionListener( ae -> {
        	setAlphaProp(1f);
        	timeline.playReverse(); // fade out, nextTip + fade in see TimelineCallback
        });
        north.add(nextTip);
        
        alphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
        alphaSlider.setName(SLIDER);
        alphaSlider.setOpaque(false);
        alphaSlider.setPaintLabels(true);
        alphaSlider.setValue(50);
        north.add(alphaSlider);
        
        fadeIn = new JButton();
        fadeIn.setName("fadeIn");
        fadeIn.setText("fade in");
        ChevronsIcon fadeInIcon = new ChevronsIcon(ChevronsIcon.SMALL_ICON);
        fadeInIcon.setDirection(ChevronsIcon.EAST);
        fadeIn.setIcon(fadeInIcon);
        fadeIn.addActionListener( ae -> {
        	setAlphaProp(0f);
        	timeline.play(); // fade in
        });
        north.add(fadeIn);
        panel.add(north, BorderLayout.NORTH);

    	return panel;
    }    

    protected TipOfTheDayModel createTipOfTheDayModel() {
        // Create a tip model with some tips
        DefaultTipOfTheDayModel tips = new DefaultTipOfTheDayModel();

        // plain text
        tips.add(new DefaultTip("Plain Text Tip", "This is the first tip " + "This is the first tip "
                + "This is the first tip " + "This is the first tip " + "This is the first tip "
                + "This is the first tip\n" + "This is the first tip " + "This is the first tip"));

        // html text
        tips.add(new DefaultTip("HTML Text Tip", "<html>This is an html <b>TIP</b><br><center>"
                + "<table border=\"1\">" + "<tr><td>1</td><td>entry 1</td></tr>"
                + "<tr><td>2</td><td>entry 2</td></tr>" + "<tr><td>3</td><td>entry 3</td></tr>"
                + "</table>"));

        // a Component
        tips.add(new DefaultTip("Component Tip", new JTree()));

        // an Icon
        ImageIcon ii = new ImageIcon(BasicTipOfTheDayUI.class.getResource("resources/TipOfTheDay24.gif"));
        tips.add(new DefaultTip("Icon Tip", ii));

        LOG.info("no of tips in model "+tips.getTipCount());   
        return tips;
    }
    
}
