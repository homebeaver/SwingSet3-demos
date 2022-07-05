/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.titledseparator;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTitledSeparator}.
 *
 * @author Karl George Schaefer
 * @author rbair (original JXTitledSeparatorDemo)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXTitledSeparator Demo",
//    category = "Decorators",
//    description = "Demonstrates JXTitledSeparator, a text separator.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/titledseparator/TitledSeparatorDemo.java",
//        "org/jdesktop/swingx/demos/titledseparator/resources/TitledSeparatorDemo.properties",
//        "org/jdesktop/swingx/demos/titledseparator/resources/TitledSeparatorDemo.html",
//        "org/jdesktop/swingx/demos/titledseparator/resources/images/TitledSeparatorDemo.png"
//    }
//)
public class TitledSeparatorDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -5258557721381053649L;
	private static final Logger LOG = Logger.getLogger(TitledSeparatorDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTitledSeparator, a text separator";

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
				AbstractDemo demo = new TitledSeparatorDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }
    
    /**
     * Constructor
     * 
     * @param frame controller Frame
     */
    public TitledSeparatorDemo(Frame frame) {
    	super(new VerticalLayout(3));
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        createTitledSeparatorDemo();
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

    private void createTitledSeparatorDemo() {
    	LOG.info("TODO inject properties");
        add(new JXTitledSeparator());
        add(new JXTitledSeparator()).setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
        JXTitledSeparator s = new JXTitledSeparator();
        s.setTitle("Custom Title");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setTitle("Custom Title");
        add(s);
        
        s = new JXTitledSeparator();
        s.setFont(new Font("Times New Roman", Font.ITALIC, 16));
        s.setTitle("Custom Font");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setFont(new Font("Times New Roman", Font.ITALIC, 16));
        s.setTitle("Custom Font");
        add(s);
        
        s = new JXTitledSeparator();
        s.setForeground(Color.BLUE.darker());
        s.setTitle("Custom Foreground");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setForeground(Color.BLUE.darker());
        s.setTitle("Custom Foreground");
        add(s);
        
        s = new JXTitledSeparator();
        s.setHorizontalAlignment(SwingConstants.CENTER);
        s.setTitle("Center Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setHorizontalAlignment(SwingConstants.CENTER);
        s.setTitle("Center Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setHorizontalAlignment(SwingConstants.TRAILING);
        s.setTitle("Trailing Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setHorizontalAlignment(SwingConstants.TRAILING);
        s.setTitle("Trailing Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setHorizontalAlignment(SwingConstants.LEADING);
        s.setTitle("Leading Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setHorizontalAlignment(SwingConstants.LEADING);
        s.setTitle("Leading Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setHorizontalAlignment(SwingConstants.LEFT);
        s.setTitle("Left Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setHorizontalAlignment(SwingConstants.LEFT);
        s.setTitle("Left Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setHorizontalAlignment(SwingConstants.RIGHT);
        s.setTitle("Right Alignment");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setHorizontalAlignment(SwingConstants.RIGHT);
        s.setTitle("Right Alignment");
        add(s);
        
//        Icon icon = Application.getInstance().getContext()
//                .getResourceMap(TitledSeparatorDemo.class).getIcon("greenOrb");
        Icon greenOrb = getResourceAsIcon(this.getClass(), "resources/images/green-orb.png");
        
        s = new JXTitledSeparator();
        s.setIcon(greenOrb);
        s.setTitle("Custom Icon");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setIcon(greenOrb);
        s.setTitle("Custom Icon");
        add(s);
        
        s = new JXTitledSeparator();
        s.setIcon(greenOrb);
        s.setHorizontalTextPosition(SwingConstants.LEFT);
        s.setTitle("Custom Icon, LEFT Horizontal Text Position");
        add(s);
        
        s = new JXTitledSeparator();
        s.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        s.setIcon(greenOrb);
        s.setHorizontalTextPosition(SwingConstants.LEFT);
        s.setTitle("Custom Icon, LEFT Horizontal Text Position");
        add(s);
    }
    
}
