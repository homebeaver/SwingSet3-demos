/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.errorpane;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.error.ErrorInfo;

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
	
	private JButton basic;
    private JButton owner;
    private JButton nested;
    
    /**
     * main method allows us to run as a standalone demo.
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
     */
    public ErrorPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());
    	
    	// TODO weitere Beispiele in JXErrorPaneIssues und JXErrorPaneVisualCheck
    	/* showXXX() , XXX could be one of Dialog, Frame, or InternalFrame
    	 * - Unterschied zwischen showDialog() und showFrame() bzw showInternalFrame()
    	 */
        JPanel panel = new JXPanel(new GridLayout(3, 1, 1, 1));
        /*
         * get the default error icon, see BasicErrorPaneUI.getDefaultErrorIcon() or getDefaultWarningIcon()
         * JXErrorPane.errorIcon or OptionPane.errorIcon
         */
        Icon errorIcon = UIManager.getIcon("JXErrorPane.errorIcon");
        if(errorIcon==null) {
            LOG.warning("JXErrorPane.errorIcon is null, use OptionPane.errorIcon.");
            errorIcon = UIManager.getIcon("OptionPane.errorIcon");
        }
        Icon warningIcon = UIManager.getIcon("OptionPane.warningIcon");
        
    	basic = new JButton(getBundleString("generateBasicDialog.Action.text"), errorIcon);
    	basic.addActionListener(event -> {
//    		basic.setSelected(true);
    		generateBasicDialog();
        });
        panel.add(basic);
        
    	owner = new JButton(getBundleString("generateDialogWithOwner.Action.text"), warningIcon);
    	owner.addActionListener(event -> {
    		generateDialogWithOwner();
        });
        panel.add(owner);

        nested = new JButton(getBundleString("generateNestedExceptions.Action.text"), warningIcon);
        nested.addActionListener(event -> {
    		generateNestedExceptions();
        });
    	panel.add(nested);

        add(panel);
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel(new BorderLayout());
    	
//        JXPanel controller = new JXPanel(new BorderLayout());
//        JPanel control = new JPanel(new GridLayout(2, 2));
//        control.add(new JLabel("Highlighter Options:"));
    	JXTree painterDemos = new JXTree();
        painterDemos.setRootVisible(false);
        //painterDemos.setModel(createPainters());
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
//        new DefaultMutableTreeNode(new DisplayInfo<ImagePainter>("desc", new ImagePainter(img))
//        root.add(createImagePainterDemos());
        root.add(new DefaultMutableTreeNode(new DisplayInfo<JXErrorPane>("desc - Show Basic Dialog", new JXErrorPane())));
        
//        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
//        		new JScrollPane(painterDemos), createPainterPropertiesPanel());
        panel.add(painterDemos);

    	// TODO
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    protected void createDemo() {
        setLayout(new VerticalLayout(5));
        
        basic = new JButton();
        add(basic);
        
        owner = new JButton();
        add(owner);
        
        nested = new JButton();
        add(nested);
    }
    
    /**
     * {@inheritDoc}
     */
//    protected void bind() {
//    	// org.jdesktop.application.ApplicationActionMap map;
//        basic.setAction(map.get("generateBasicDialog"));
//        owner.setAction(map.get("generateDialogWithOwner"));
//        nested.setAction(map.get("generateNestedExceptions"));
//    }
//    
//    @Action org.jdesktop.application.Action
    public void generateBasicDialog() {
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

    public void generateDialogWithOwner() {
        ErrorInfo info = new ErrorInfo("DialogWithOwner", "basic error message", null, 
        		"category", new Exception(), Level.ALL, null);
        JXErrorPane.showDialog(this, info);
//        JXErrorPane.showInternalFrame(this, info);
    }

    public void generateNestedExceptions() {
        Exception ex = new Exception("I'm a secondary exception", new Exception("I'm the cause"));
        ErrorInfo info = new ErrorInfo("Dialog with NestedExceptions", "basic error message", null,
        		"category", ex, Level.ALL, null);
        JXErrorPane.showDialog(this, info);
    }
}
