/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.errorpane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.icon.DefaultIcons;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXErrorPane}.
 *
 * @author Karl George Schaefer
 * @author joshy (original JXErrorPaneDemo)
 * @author EUG (reorg)
 */
//@DemoProperties(
//    value = "JXErrorPane Demo",
//    category = "Controls",
//    description = "Demonstrates JXErrorPane, a control for displaying errors",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/errorpane/ErrorPaneDemo.java",
//        "org/jdesktop/swingx/demos/errorpane/resources/ErrorPaneDemo.properties",
//        "org/jdesktop/swingx/demos/errorpane/resources/ErrorPaneDemo.html",
//        "org/jdesktop/swingx/demos/errorpane/resources/images/ErrorPaneDemo.png"
//    }
//)
public class ErrorPaneDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 553318600929011235L;
	private static final Logger LOG = Logger.getLogger(ErrorPaneDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXErrorPane, a control for displaying errors";

	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;
	private static final String CONTROLLER_ICON_POSITION = BorderLayout.WEST; // also good: NORTH

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
				AbstractDemo demo = new ErrorPaneDemo(controller);
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
     * ErrorPaneDemo Constructor
     * 
     * @param frame controller Frame
     */
	// TODO weitere Beispiele in JXErrorPaneIssues und JXErrorPaneVisualCheck
	/* showXXX() , XXX could be one of Dialog, Frame, or InternalFrame
	 * - Unterschied zwischen showDialog() und showFrame() bzw showInternalFrame()
	 */
    public ErrorPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

        if(CONTROLLER_IN_PRESENTATION_FRAME) {
            super.add(getBoxPane());
        }
    }

    @Override
	public JXPanel getControlPane() {
        if(CONTROLLER_IN_PRESENTATION_FRAME) return emptyControlPane();
    	
        JXPanel controller = new JXPanel();
        controller.add(getBoxPane());
    	return controller;
    }

    private JPanel getBoxPane() {
    	JPanel demo = new JPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.X_AXIS));

        @SuppressWarnings("serial")
        JPanel bp = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));

        bp.add(Box.createRigidArea(VGAP30));

        bp.add(createBasicButton());	bp.add(Box.createRigidArea(VGAP15));
        bp.add(createOwnerButton());	bp.add(Box.createRigidArea(VGAP15));
        bp.add(createNestedButton());	bp.add(Box.createVerticalGlue());

        demo.add(Box.createHorizontalGlue());
        demo.add(bp);
        demo.add(Box.createHorizontalGlue());
        
        return demo;
    }

    private JButton createBasicButton() {
        /*
         * get the default error icon, see BasicErrorPaneUI.getDefaultErrorIcon() or getDefaultWarningIcon()
         * JXErrorPane.errorIcon or OptionPane.errorIcon
         */
        Icon icon = UIManager.getIcon("JXErrorPane.errorIcon");
        if(icon==null) {
            LOG.warning("JXErrorPane.errorIcon is null, use default UI OptionPane.errorIcon.");
            icon = DefaultIcons.getIcon(DefaultIcons.ERROR);
        }
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("generateBasicDialog.Action.text"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
			generateBasicDialog();
		});
    	return b;
    }

    private JButton createOwnerButton() {
		JLabel iconLabel = new JLabel(DefaultIcons.getIcon(DefaultIcons.WARNING));
		JLabel clickMe = new JLabel(getBundleString("generateDialogWithOwner.Action.text"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
			generateDialogWithOwner();
		});
    	return b;
    }

    private JButton createNestedButton() {
		JLabel iconLabel = new JLabel(DefaultIcons.getIcon(DefaultIcons.WARNING));
		JLabel clickMe = new JLabel(getBundleString("generateNestedExceptions.Action.text"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
			generateNestedExceptions();
		});
    	return b;
    }

    private void generateBasicDialog() {
    	// showXXX() , XXX could be one of Dialog, Frame, or InternalFrame
/*
 * ... to modify the icon shown with a particular
 * instance of a <code>JXErrorPane</code>, you might do the following:
 *      JXErrorPane pane = new JXErrorPane();
 *      pane.setErrorIcon(myErrorIcon);
 *      pane.setErrorInfo(new ErrorInfo("Fatal Error", exception));
 *      JXErrorPane.showDialog(null, pane);    	
 */
    	
        JXErrorPane.showDialog(new Exception());
//        JXErrorPane.showFrame(new Exception());
    }

    private void generateDialogWithOwner() {
        ErrorInfo info = new ErrorInfo("DialogWithOwner", "basic error message", null, 
        		"category", new Exception(), Level.ALL, null);
        JXErrorPane.showDialog(this, info);
//        JXErrorPane.showInternalFrame(this, info);
    }

    private void generateNestedExceptions() {
        Exception ex = new Exception("I'm a secondary exception", new Exception("I'm the cause"));
        ErrorInfo info = new ErrorInfo("Dialog with Nested Exceptions", "basic error message", null,
        		"category", ex, Level.ALL, null);
        JXErrorPane.showDialog(this, info);
    }
}
