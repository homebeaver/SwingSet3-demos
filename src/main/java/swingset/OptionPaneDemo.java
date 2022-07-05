/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
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

/**
 * JOptionPaneDemo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class OptionPaneDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JOptionPane.gif";

	private static final long serialVersionUID = 3164409244554337243L;
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;

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
//        buttonPanel.add(Box.createRigidArea(VGAP30));

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

    public JButton createWarningDialogButton() {
        @SuppressWarnings("serial")
		Action a = new AbstractAction(getBundleString("warningbutton")) {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                	OptionPaneDemo.this,
                    getBundleString("warningtext"),
                    getBundleString("warningtitle"),
                    JOptionPane.WARNING_MESSAGE
                );
            }
        };
        // see protected Icon BasicOptionPaneUI.getIconForType(int messageType)
        //Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon"); // messageType == 0
        Icon icon = UIManager.getIcon("OptionPane.warningIcon"); // messageType == 2
//		System.err.println("icon:"+icon.getIconHeight()); // 32
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public JButton createMessageDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("messagebutton")) {
            URL img = getClass().getResource("images/optionpane/bottle.gif");
            String imagesrc = "<img src=\"" + img + "\" width=\"284\" height=\"100\">";
            String message = getBundleString("messagetext");
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog( OptionPaneDemo.this,
                    "<html>" + imagesrc + "<br><center>" + message + "</center><br></html>"
                );
            }
        };
        Icon icon = UIManager.getIcon("OptionPane.informationIcon"); // messageType == 1
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public JButton createConfirmDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("confirmbutton")) {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(OptionPaneDemo.this, getBundleString("confirmquestion"));
                if(result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("confirmyes"));
                } else if(result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("confirmno"));
                }
            }
        };
        Icon icon = UIManager.getIcon("OptionPane.questionIcon"); // messageType == 3
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public JButton createInputDialogButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("inputbutton")) {
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(OptionPaneDemo.this, getBundleString("inputquestion"));
                if ((result != null) && (result.length() > 0)) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this,
                                    result + ": " + getBundleString("inputresponse"));
                }
            }
        };
        Icon icon = UIManager.getIcon("OptionPane.questionIcon"); // messageType == 3
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public JButton createComponentDialogButton() {
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

                // Options
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
                    null,                                       // optional icon, use null to use the default icon
                    options,                                    // options string array, will be made into buttons
                    options[3]                                  // option that should be made into a default button
                );
                switch(result) {
                   case 0: // yes
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("component_r1"));
                     break;
                   case 1: // no
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("component_r2"));
                     break;
                   case 2: // maybe
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("component_r3"));
                     break;
                   case 3: // probably
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getBundleString("component_r4"));
                     break;
                   default:
                     break;
                }

            }
        };
        Icon icon = UIManager.getIcon("OptionPane.informationIcon"); // messageType == 1
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public JButton createButton(Action a) {
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
