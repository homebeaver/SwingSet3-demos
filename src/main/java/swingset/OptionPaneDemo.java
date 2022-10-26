/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.RadianceIcon;

/**
 * The OptionPane Demo shows examples of using JOptionPane to generate different common option dialog boxes
 * <p>
 * It shows usage of
 * <ul>
 * <li>{@code JOptionPane.showInputDialog} </li>
 * <li>{@code JOptionPane.showMessageDialog} </li>
 * <li>{@code JOptionPane.showConfirmDialog}</li>
 * <li>{@code JOptionPane.showOptionDialog}</li>
 * </ul>
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
/*

class JOptionPane extends JComponent
- there are some Message types. 
  Used by the UI to determine what icon to display, and possibly what behavior to give based on the type.
  Used in protected Icon javax.swing.plaf.basic.BasicOptionPaneUI#getIconForType(int messageType)
 0:ERROR_MESSAGE : "OptionPane.errorIcon"
 1:INFORMATION_MESSAGE : "OptionPane.informationIcon"
 2:WARNING_MESSAGE : "OptionPane.warningIcon"
 3:QUESTION_MESSAGE : "OptionPane.questionIcon"
 -1:PLAIN_MESSAGE : No icon is used.

- there are three nls props
  UIManager.getString("OptionPane.titleText")          // nls : "Option auswählen" / Select an Option
  UIManager.getString("OptionPane.messageDialogTitle") // nls : "Meldung" / Message
  UIManager.getString("OptionPane.inputDialogTitle")   // nls : "Eingabe" / Input

 */
public class OptionPaneDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JOptionPane.gif";

	private static final long serialVersionUID = 3164409244554337243L;
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;
	private static final boolean USE_OPTIONPANE_ICONS = false;

	private static final String OPTIONPANE_TITLETEXT = "OptionPane.titleText";
	private static final String OPTIONPANE_MESSAGE   = "OptionPane.messageDialogTitle";
	private static final String OPTIONPANE_INPUT     = "OptionPane.inputDialogTitle";
	private String getUIString(Object key) {
		return UIManager.getString(key, getLocale());
	}
	
	private static final String OPTIONPANE_ICON[] = new String[] 
		{ "OptionPane.errorIcon"       // for JOptionPane.ERROR_MESSAGE , red octagon with x
		, "OptionPane.informationIcon" // ... , blue circle with i
		, "OptionPane.warningIcon"     // ... , yellow triangle with exclamation mark
		, "OptionPane.questionIcon"    // ... , green square with question mark
		};
	// alternative monochrome icons with colors:
	private static final String FEATHER_ICON_CLASS[] = new String[] 
		{ "org.jdesktop.swingx.demos.svg.FeatheRalert_octagon"
		, "org.jdesktop.swingx.demos.svg.FeatheRinfo"
		, "org.jdesktop.swingx.demos.svg.FeatheRalert_triangle"
		, "org.jdesktop.swingx.demos.svg.FeatheRhelp_circle"
		};
	private static final Color COLOR_OF_ICON[] = new Color[] 
		{ Color.RED
		, Color.BLUE
		, Color.ORANGE
		, Color.GREEN
		};
	
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new OptionPaneDemo(controller);
				JXFrame frame = new JXFrame("demo", exitOnClose);
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
     * OptionPaneDemo Constructor
     * @param frame controller Frame
     */
    public OptionPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

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
    	JPanel boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.X_AXIS));

        // Create a panel to hold buttons
        @SuppressWarnings("serial")
        JPanel buttonPanel = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(Box.createRigidArea(VGAP30));

        buttonPanel.add(createErrorMessageButton());     buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createInputDialogButton());      buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createWarningDialogButton());    buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createMessageDialogButton());    buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createComponentDialogButton());  buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createConfirmDialogButton());    buttonPanel.add(Box.createVerticalGlue());

        boxPane.add(Box.createHorizontalGlue());
        boxPane.add(buttonPanel);
        boxPane.add(Box.createHorizontalGlue());
        
        return boxPane;
    }

    private JButton createErrorMessageButton() {
        @SuppressWarnings("serial")
		Action a = new AbstractAction(getBundleString("errorbutton")) {
        	@Override	
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog
            	( OptionPaneDemo.this                  // parentComponent
            	, getBundleString("errormessage")      // Object message 
            	, getUIString(OPTIONPANE_MESSAGE)      // String title, nls : "Meldung" / Message
            	, JOptionPane.ERROR_MESSAGE
            	, getIcon(JOptionPane.ERROR_MESSAGE)
                );
            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.ERROR_MESSAGE));
        return createButton(a);
    }

    private JButton createInputDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("inputbutton")) {
            public void actionPerformed(ActionEvent e) {
            	/* BUG TODO
während getUIString(key) locale berüchsichtigt (auch nach Änderung in demo bar), wenn aus jar gestartet
liefert getBundleString immer dieselbe Sprache!!!
            	 */
                String result = (String)JOptionPane.showInputDialog
                	( OptionPaneDemo.this
                	, getBundleString("inputquestion")      // Object message                	
                	, getUIString(OPTIONPANE_INPUT)         // String title, nls : "Eingabe" / Input
                	, JOptionPane.QUESTION_MESSAGE
                	, getIcon(JOptionPane.QUESTION_MESSAGE) // the Icon to display
                	, null, null                            // selectionValues, initialSelectionValue
                	);
                if ((result != null) && (result.length() > 0)) {
                    JOptionPane.showMessageDialog
                    	( OptionPaneDemo.this                               // parentComponent
                    	, result + ": " + getBundleString("inputresponse")  // Object message 
                    	, getUIString(OPTIONPANE_MESSAGE)                   // String title, nls : "Meldung" / Message
                    	, JOptionPane.INFORMATION_MESSAGE
                    	, getIcon(JOptionPane.INFORMATION_MESSAGE)
                        );
                }
            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.QUESTION_MESSAGE));
        return createButton(a);
    }

    private JButton createWarningDialogButton() {
        @SuppressWarnings("serial")
		Action a = new AbstractAction(getBundleString("warningbutton")) {
        	@Override	
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog
                	( OptionPaneDemo.this
                	, getBundleString("warningtext")
                	, getBundleString("warningtitle")
                	, JOptionPane.WARNING_MESSAGE
                	, getIcon(JOptionPane.WARNING_MESSAGE
                	)
                );
            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.WARNING_MESSAGE));
        return createButton(a);
    }

    private JButton createMessageDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("messagebutton")) {
            URL img = getClass().getResource("images/optionpane/bottle.gif");
            String imagesrc = "<img src=\"" + img + "\" width=\"284\" height=\"100\">";
            String message = getBundleString("messagetext");
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog
                	( OptionPaneDemo.this
                    , "<html>" + imagesrc + "<br><center>" + message + "</center><br></html>"
//  not visible:    UIManager.getString("OptionPane.messageDialogTitle", OptionPaneDemo.this),
                    , getUIString(OPTIONPANE_MESSAGE)
                    , JOptionPane.INFORMATION_MESSAGE
                    , getIcon(JOptionPane.INFORMATION_MESSAGE)
                );
            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.INFORMATION_MESSAGE));
        return createButton(a);
    }

    private JButton createComponentDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("componentbutton")) {
            public void actionPerformed(ActionEvent e) {
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
                	OptionPaneDemo.this,                        // the parent that the dialog blocks
                    message,                                    // the dialog message array
                    getBundleString("componenttitle"),          // the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,                 // option type
                    JOptionPane.INFORMATION_MESSAGE,            // message type
                    getIcon(JOptionPane.INFORMATION_MESSAGE),   // optional icon, use null to use the default icon
                    options,                                    // options string array, will be made into buttons
                    options[3]                                  // option that should be made into a default button
                );
                switch(result) {
                   case 0: // yes
                     JOptionPane.showMessageDialog
	                 	( OptionPaneDemo.this                  // parentComponent
	                 	, getBundleString("component_r1")      // Object message 
	                 	, getUIString(OPTIONPANE_MESSAGE)      // String title, nls : "Meldung" / Message
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
	                    );
                     break;
                   case 1: // no
                     JOptionPane.showMessageDialog
	                 	( OptionPaneDemo.this
	                 	, getBundleString("component_r2")
	                 	, getUIString(OPTIONPANE_MESSAGE)
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
	                    );
                     break;
                   case 2: // maybe
                       JOptionPane.showMessageDialog
  	                 	( OptionPaneDemo.this
  	                 	, getBundleString("component_r3")
  	                 	, getUIString(OPTIONPANE_MESSAGE)
  	                 	, JOptionPane.INFORMATION_MESSAGE
  	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
  	                    );
                     break;
                   case 3: // probably
                       JOptionPane.showMessageDialog
  	                 	( OptionPaneDemo.this
  	                 	, getBundleString("component_r4")
  	                 	, getUIString(OPTIONPANE_MESSAGE)
  	                 	, JOptionPane.INFORMATION_MESSAGE
  	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
  	                    );
                     break;
                   default:
                     break;
                }

            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.INFORMATION_MESSAGE));
        return createButton(a);
    }

    private JButton createConfirmDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("confirmbutton")) {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog
                		( OptionPaneDemo.this
                		, getBundleString("confirmquestion")
                		, getUIString(OPTIONPANE_TITLETEXT)
                		, JOptionPane.YES_NO_CANCEL_OPTION
                		, JOptionPane.QUESTION_MESSAGE
                		, getIcon(JOptionPane.QUESTION_MESSAGE)
                		);
                if(result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog
	                 	( OptionPaneDemo.this
	                 	, getBundleString("confirmyes")
	                 	, getUIString(OPTIONPANE_MESSAGE)
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
	                    );
                } else if(result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog
	                 	( OptionPaneDemo.this
	                 	, getBundleString("confirmno")
	                 	, getUIString(OPTIONPANE_MESSAGE)
	                 	, JOptionPane.INFORMATION_MESSAGE
	                 	, getIcon(JOptionPane.INFORMATION_MESSAGE)
	                    );
                }
            }
        };
        a.putValue(Action.LARGE_ICON_KEY, getIcon(JOptionPane.QUESTION_MESSAGE));
        return createButton(a);
    }

    private Icon getIcon(int messageType) {
    	if(USE_OPTIONPANE_ICONS) return UIManager.getIcon(OPTIONPANE_ICON[messageType]);
        // alternative icon in ControlPane, 
    	// in presentation pane JOptionPane.showInputDialog, .showMessageDialog, ...  is used
    	Class<?> iconClass = null;
		try {
			iconClass = Class.forName(FEATHER_ICON_CLASS[messageType]);  // throws ClassNotFoundException
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return UIManager.getIcon(OPTIONPANE_ICON[messageType]);
		}
    	RadianceIcon icon = null;
    	try {
			Method method = iconClass.getMethod("of", int.class, int.class);
			Object o = method.invoke(null, RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON);
			icon = (RadianceIcon)o; // ClassCastException
		} catch (NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return UIManager.getIcon(OPTIONPANE_ICON[messageType]);
		}
    	icon.setColorFilter(color -> COLOR_OF_ICON[messageType]);
	    return icon;
    }
    
    /**
     * createButton for Action
     * @param a Action
     * @return JButton
     */
    private JButton createButton(Action a) {
        @SuppressWarnings("serial")
        JButton b = new JButton() {
            public Dimension getMaximumSize() {
                int width = Short.MAX_VALUE;
                int height = super.getMaximumSize().height;
                return new Dimension(width, height);
            }
        };
        // setting the following client property informs the button to show
        // the action text as it's name. The default is to not show the
        // action text.
        b.putClientProperty("displayActionText", Boolean.TRUE);
        b.setAction(a);
        // b.setAlignmentX(JButton.CENTER_ALIGNMENT);
        return b;
    }

}
