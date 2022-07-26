/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tipoftheday;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
import org.jdesktop.swingx.tips.DefaultTip;
import org.jdesktop.swingx.tips.DefaultTipOfTheDayModel;
import org.jdesktop.swingx.tips.TipOfTheDayModel;

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
	private static final String DESCRIPTION = "Demonstrates JXTipOfTheDay, a container for tips and other arbitrary components.";

	private TipOfTheDayModel model;
    private JXTipOfTheDay totd;
    
    private JXHyperlink nextTipLink;
    private JXHyperlink dialogLink;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new TipOfTheDayDemo(controller);
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
        totd.setAlpha(0.8f);
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
            JXTipOfTheDay dialog = new JXTipOfTheDay(model);
            dialog.setCurrentTip(0);
            dialog.showDialog(totd);
        });
        panel.add(dialogLink, BorderLayout.NORTH);
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
