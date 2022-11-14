/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.errorpane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.demos.svg.TangoROptionPane_error;
import org.jdesktop.swingx.demos.svg.TangoROptionPane_information;
import org.jdesktop.swingx.demos.svg.TangoROptionPane_question;
import org.jdesktop.swingx.demos.svg.TangoROptionPane_warning;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.icon.CircleIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.TrafficLightGreenIcon;
import org.jdesktop.swingx.icon.TrafficLightRedIcon;
import org.jdesktop.swingx.icon.TrafficLightYellowIcon;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXErrorPane} and swing {@code JOptionPane}.
 *
 * @author Karl George Schaefer
 * @author joshy (original JXErrorPaneDemo)
 * @author EUG (reorg+OptionPaneDemo)
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
	
	public static final String ICON_PATH = "toolbar/JOptionPane.gif";
	
	private static final long serialVersionUID = 553318600929011235L;
	private static final Logger LOG = Logger.getLogger(ErrorPaneDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXErrorPane, a control for displaying errors";

	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;
	private static final String CONTROLLER_ICON_POSITION = BorderLayout.WEST; // also good: NORTH

	private static final String OPTIONPANE_TITLETEXT = "OptionPane.titleText";
	private static final String OPTIONPANE_MESSAGE   = "OptionPane.messageDialogTitle";
	private static final String OPTIONPANE_INPUT     = "OptionPane.inputDialogTitle";
	private String getUIString(Object key) {
		return UIManager.getString(key, getLocale());
	}
	
    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
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

        bp.add(Box.createRigidArea(VGAP5));

        bp.add(createErrorMessageButton());     bp.add(Box.createRigidArea(VGAP15));
        bp.add(createBasicErrorButton());	bp.add(Box.createRigidArea(VGAP15));
        bp.add(createInputDialogButton());      bp.add(Box.createRigidArea(VGAP15));
        bp.add(createWarningDialogButton());    bp.add(Box.createRigidArea(VGAP15));
        bp.add(createOwnerWarnButton());	bp.add(Box.createRigidArea(VGAP15));
        bp.add(createNestedWarnButton());	bp.add(Box.createRigidArea(VGAP15));
        bp.add(createMessageDialogButton());    bp.add(Box.createRigidArea(VGAP15));
        bp.add(createComponentDialogButton());  bp.add(Box.createRigidArea(VGAP15));
        bp.add(createConfirmDialogButton());    bp.add(Box.createVerticalGlue());

        demo.add(bp);
        demo.add(Box.createHorizontalGlue());
        
        return demo;
    }

    private JButton createErrorMessageButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_error.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.ERROR_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("errorbutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            JOptionPane.showMessageDialog
        	( ErrorPaneDemo.this                 // parentComponent
        	, getBundleString("errormessage")    // Object message 
        	, getUIString(OPTIONPANE_MESSAGE)    // String title, nls : "Meldung" / Message
        	, JOptionPane.ERROR_MESSAGE
        	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        			null : getMessageTypeIcon(JOptionPane.ERROR_MESSAGE, RadianceIcon.BUTTON_ICON)
            );
		});
		return b;
    }

    private JButton createInputDialogButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_question.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.QUESTION_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("inputbutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            String result = (String)JOptionPane.showInputDialog
                	( ErrorPaneDemo.this
                	, getBundleString("inputquestion")      // Object message                	
                	, getUIString(OPTIONPANE_INPUT)         // String title, nls : "Eingabe" / Input
                	, JOptionPane.QUESTION_MESSAGE
                	// the Icon to display:
                	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                			null : getMessageTypeIcon(JOptionPane.QUESTION_MESSAGE, RadianceIcon.BUTTON_ICON)
                	, null, null                            // selectionValues, initialSelectionValue
                	);
                if ((result != null) && (result.length() > 0)) {
                    JOptionPane.showMessageDialog
                    	( ErrorPaneDemo.this                                // parentComponent
                    	, result + ": " + getBundleString("inputresponse")  // Object message 
                    	, getUIString(OPTIONPANE_MESSAGE)                   // String title, nls : "Meldung" / Message
                    	, JOptionPane.INFORMATION_MESSAGE
                    	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                    			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
                        );
                }
		});
    	return b;
    }

    private JButton createWarningDialogButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_warning.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.WARNING_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("warningbutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            JOptionPane.showMessageDialog
        	( ErrorPaneDemo.this
        	, getBundleString("warningtext")
        	, getBundleString("warningtitle")
        	, JOptionPane.WARNING_MESSAGE
        	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        			null : getMessageTypeIcon(JOptionPane.WARNING_MESSAGE, RadianceIcon.BUTTON_ICON)
        	);
		});
    	return b;
    }

    private String getImgSrc() {
        URL img = getClass().getResource("resources/images/bottle.gif");
        if(img==null) LOG.warning("Ressource bottle.gif not found");
        String imagesrc = "<img src=\"" + img + "\" width=\"284\" height=\"100\">";
        return imagesrc;
    }
    // Message in a Bottle:
    private JButton createMessageDialogButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_information.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("messagebutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            JOptionPane.showMessageDialog
        	( ErrorPaneDemo.this
            , "<html>" + getImgSrc() + "<br><center>" + getBundleString("messagetext") + "</center><br></html>"
            , getUIString(OPTIONPANE_MESSAGE)
            , JOptionPane.INFORMATION_MESSAGE
            , UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
            		null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
        );
		});
    	return b;
    }

    private JButton createComponentDialogButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_information.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("componentbutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            // In a ComponentDialog, you can show as many message components and
            // as many options as you want:

            // Messages
            Object[]      message = new Object[4];
            message[0] = getBundleString("componentmessage");
            message[1] = new JTextField(getBundleString("componenttextfield"));

            JComboBox<String> cb = new JComboBox<String> ();
            cb.addItem(getBundleString("component_cb1"));
            cb.addItem(getBundleString("component_cb2"));
            cb.addItem(getBundleString("component_cb3"));
            message[2] = cb;

            message[3] = getBundleString("componentmessage2");

            // Options from nls props:
            String[] options = {
                getBundleString("component_op1"),
                getBundleString("component_op2"),
                getBundleString("component_op3"),
                getBundleString("component_op4"),
                getBundleString("component_op5")
            };
            int result = JOptionPane.showOptionDialog(
            	ErrorPaneDemo.this,                         // the parent that the dialog blocks
                message,                                    // the dialog message array
                getBundleString("componenttitle"),          // the title of the dialog window
                JOptionPane.DEFAULT_OPTION,                 // option type
                JOptionPane.INFORMATION_MESSAGE,            // message type
                UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                		null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON),   
                                                            // optional icon, use null to use the default icon
                options,                                    // options string array, will be made into buttons
                options[3]                                  // option that should be made into a default button
            );
            switch(result) {
               case 0: // yes
                 JOptionPane.showMessageDialog
                 	( ErrorPaneDemo.this                   // parentComponent
                 	, getBundleString("component_r1")      // Object message 
                 	, getUIString(OPTIONPANE_MESSAGE)      // String title, nls : "Meldung" / Message
                 	, JOptionPane.INFORMATION_MESSAGE
                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
                    );
                 break;
               case 1: // no
                 JOptionPane.showMessageDialog
                 	( ErrorPaneDemo.this
                 	, getBundleString("component_r2")
                 	, getUIString(OPTIONPANE_MESSAGE)
                 	, JOptionPane.INFORMATION_MESSAGE
                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
                    );
                 break;
               case 2: // maybe
                   JOptionPane.showMessageDialog
	                 	( ErrorPaneDemo.this
	                 	, getBundleString("component_r3")
	                 	, getUIString(OPTIONPANE_MESSAGE)
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
	                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
	                    );
                 break;
               case 3: // probably
                   JOptionPane.showMessageDialog
	                 	( ErrorPaneDemo.this
	                 	, getBundleString("component_r4")
	                 	, getUIString(OPTIONPANE_MESSAGE)
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
	                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
	                    );
                 break;
               default:
                 break;
            }
		});
    	return b;
    }

    private JButton createConfirmDialogButton() {
        Icon icon = UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
        		TangoROptionPane_question.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON) : 
        		getMessageTypeIcon(JOptionPane.QUESTION_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
		JLabel clickMe = new JLabel(getBundleString("confirmbutton"), SwingConstants.CENTER);
		JButton b = new JButton();
        b.setLayout(new BorderLayout());
        b.add(iconLabel, CONTROLLER_ICON_POSITION);
        b.add(clickMe, BorderLayout.CENTER);
		b.addActionListener(event -> {
            int result = JOptionPane.showConfirmDialog
            		( ErrorPaneDemo.this
            		, getBundleString("confirmquestion")
            		, getUIString(OPTIONPANE_TITLETEXT)
            		, JOptionPane.YES_NO_CANCEL_OPTION
            		, JOptionPane.QUESTION_MESSAGE
            		, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
            				null : getMessageTypeIcon(JOptionPane.QUESTION_MESSAGE, RadianceIcon.BUTTON_ICON)
            		);
            if(result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog
                 	( ErrorPaneDemo.this
                 	, getBundleString("confirmyes")
                 	, getUIString(OPTIONPANE_MESSAGE)
                 	, JOptionPane.INFORMATION_MESSAGE
                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
                    );
            } else if(result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog
                 	( ErrorPaneDemo.this
                 	, getBundleString("confirmno")
                 	, getUIString(OPTIONPANE_MESSAGE)
                 	, JOptionPane.INFORMATION_MESSAGE
                 	, UIManager.getLookAndFeel().getClass().getName().contains("Nimbus") ?
                 			null : getMessageTypeIcon(JOptionPane.INFORMATION_MESSAGE, RadianceIcon.BUTTON_ICON)
                    );
            }
		});
    	return b;
    }

    private Icon getMessageTypeIcon(int messageType, int size) {
    	RadianceIcon icon = CircleIcon.of(size, size); // out of order ==> no color
    	switch(messageType) {
		case JOptionPane.ERROR_MESSAGE:
			icon = TrafficLightRedIcon.of(size, size);
			break;
		case JOptionPane.INFORMATION_MESSAGE:
			break;
		case JOptionPane.WARNING_MESSAGE:
			icon = TrafficLightYellowIcon.of(size, size);
			break;
		case JOptionPane.QUESTION_MESSAGE:
			icon = TrafficLightGreenIcon.of(size, size);
			break;
		default:
			break;
		}
    	return icon;
    }

    private JButton createBasicErrorButton() {
        Icon icon = getMessageTypeIcon(JOptionPane.ERROR_MESSAGE, RadianceIcon.BUTTON_ICON);
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

    private JButton createOwnerWarnButton() {
        Icon icon = getMessageTypeIcon(JOptionPane.WARNING_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
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

    private JButton createNestedWarnButton() {
        Icon icon = getMessageTypeIcon(JOptionPane.WARNING_MESSAGE, RadianceIcon.BUTTON_ICON);
		JLabel iconLabel = new JLabel(icon);
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
    	
        JXErrorPane.showDialog(new Exception("any text"));
//        JXErrorPane.showFrame(new Exception());
//        JXErrorPane.showInternalFrame(new Exception());
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
