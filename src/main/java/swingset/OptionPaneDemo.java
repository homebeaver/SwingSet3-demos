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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    	frame.setTitle(getString("name"));

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

        JPanel bp = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        bp.setLayout(new BoxLayout(bp, BoxLayout.Y_AXIS));

        bp.add(Box.createRigidArea(VGAP30));
        bp.add(Box.createRigidArea(VGAP30));

        bp.add(createInputDialogButton());      bp.add(Box.createRigidArea(VGAP15));
        bp.add(createWarningDialogButton());    bp.add(Box.createRigidArea(VGAP15));
        bp.add(createMessageDialogButton());    bp.add(Box.createRigidArea(VGAP15));
        bp.add(createComponentDialogButton());  bp.add(Box.createRigidArea(VGAP15));
        bp.add(createConfirmDialogButton());    bp.add(Box.createVerticalGlue());

        demo.add(Box.createHorizontalGlue());
        demo.add(bp);
        demo.add(Box.createHorizontalGlue());
        
        return demo;
    }

    public JButton createWarningDialogButton() {
        Action a = new AbstractAction(getString("warningbutton")) {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                	OptionPaneDemo.this,
                    getString("warningtext"),
                    getString("warningtitle"),
                    JOptionPane.WARNING_MESSAGE
                );
            }
        };
        return createButton(a);
    }

    public JButton createMessageDialogButton() {
        Action a = new AbstractAction(getString("messagebutton")) {
            URL img = getClass().getResource("images/optionpane/bottle.gif");
            String imagesrc = "<img src=\"" + img + "\" width=\"284\" height=\"100\">";
            String message = getString("messagetext");
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog( OptionPaneDemo.this,
                    "<html>" + imagesrc + "<br><center>" + message + "</center><br></html>"
                );
            }
        };
        return createButton(a);
    }

    public JButton createConfirmDialogButton() {
        Action a = new AbstractAction(getString("confirmbutton")) {
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(OptionPaneDemo.this, getString("confirmquestion"));
                if(result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("confirmyes"));
                } else if(result == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("confirmno"));
                }
            }
        };
        return createButton(a);
    }

    public JButton createInputDialogButton() {
        Action a = new AbstractAction(getString("inputbutton")) {
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(OptionPaneDemo.this, getString("inputquestion"));
                if ((result != null) && (result.length() > 0)) {
                    JOptionPane.showMessageDialog(OptionPaneDemo.this,
                                    result + ": " + getString("inputresponse"));
                }
            }
        };
        return createButton(a);
    }

    public JButton createComponentDialogButton() {
        Action a = new AbstractAction(getString("componentbutton")) {
            public void actionPerformed(ActionEvent e) {
                // In a ComponentDialog, you can show as many message components and
                // as many options as you want:

                // Messages
                Object[]      message = new Object[4];
                message[0] = getString("componentmessage");
                message[1] = new JTextField(getString("componenttextfield"));

                JComboBox cb = new JComboBox();
                cb.addItem(getString("component_cb1"));
                cb.addItem(getString("component_cb2"));
                cb.addItem(getString("component_cb3"));
                message[2] = cb;

                message[3] = getString("componentmessage2");

                // Options
                String[] options = {
                    getString("component_op1"),
                    getString("component_op2"),
                    getString("component_op3"),
                    getString("component_op4"),
                    getString("component_op5")
                };
                int result = JOptionPane.showOptionDialog(
                	OptionPaneDemo.this,                        // the parent that the dialog blocks
                    message,                                    // the dialog message array
                    getString("componenttitle"),                // the title of the dialog window
                    JOptionPane.DEFAULT_OPTION,                 // option type
                    JOptionPane.INFORMATION_MESSAGE,            // message type
                    null,                                       // optional icon, use null to use the default icon
                    options,                                    // options string array, will be made into buttons
                    options[3]                                  // option that should be made into a default button
                );
                switch(result) {
                   case 0: // yes
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("component_r1"));
                     break;
                   case 1: // no
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("component_r2"));
                     break;
                   case 2: // maybe
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("component_r3"));
                     break;
                   case 3: // probably
                     JOptionPane.showMessageDialog(OptionPaneDemo.this, getString("component_r4"));
                     break;
                   default:
                     break;
                }

            }
        };
        return createButton(a);
    }

    public JButton createButton(Action a) {
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
