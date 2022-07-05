/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

/**
 * JColorChooserDemo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ColorChooserDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JColorChooser.gif";

	private static final long serialVersionUID = 6436900967666892689L;

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new ColorChooserDemo(controller);
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

    BezierAnimationPanel bezAnim;
    JButton outerColorButton = null;
    JButton backgroundColorButton = null;
    JButton gradientAButton = null;
    JButton gradientBButton = null;

    // to store the color chosen from the JColorChooser
    private Color chosen;

    /**
     * ColorChooserDemo Constructor
     * 
     * @param frame controller Frame
     */
    public ColorChooserDemo(Frame frame) {
        super(new BorderLayout());
        super.setPreferredSize(PREFERRED_SIZE);
        super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        // Create the bezier animation panel to put in the center of the panel.
        bezAnim = new BezierAnimationPanel();

        outerColorButton = new JButton(getBundleString("outer_line"));
        outerColorButton.setIcon(new ColorSwatch("OuterLine", bezAnim));

        backgroundColorButton = new JButton(getBundleString("background"));
        backgroundColorButton.setIcon(new ColorSwatch("Background", bezAnim));

        gradientAButton = new JButton(getBundleString("grad_a"));
        gradientAButton.setIcon(new ColorSwatch("GradientA", bezAnim));

        gradientBButton = new JButton(getBundleString("grad_b"));
        gradientBButton.setIcon(new ColorSwatch("GradientB", bezAnim));

        ActionListener l = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color current = bezAnim.getOuterColor();

                if(e.getSource() == backgroundColorButton) {
                    current = bezAnim.getBackgroundColor();
                } else if(e.getSource() == gradientAButton) {
                    current = bezAnim.getGradientColorA();
                } else if(e.getSource() == gradientBButton) {
                    current = bezAnim.getGradientColorB();
                }

                final JColorChooser chooser = new JColorChooser(current != null ? current : Color.WHITE);
//                if (getSwingSet2() != null && getSwingSet2().isDragEnabled()) {
//                    chooser.setDragEnabled(true);
//                }

                chosen = null;
                ActionListener okListener = new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        chosen = chooser.getColor();
                    }
                };

                JDialog dialog = JColorChooser.createDialog(ColorChooserDemo.this,
                                                            getBundleString("chooser_title"),
                                                            true,
                                                            chooser,
                                                            okListener,
                                                            null);

//              dialog.show(); // Deprecated.  As of JDK version 1.5, replaced by setVisible(boolean).
                dialog.setVisible(true);

                if(e.getSource() == outerColorButton) {
                    bezAnim.setOuterColor(chosen);
                } else if(e.getSource() == backgroundColorButton) {
                    bezAnim.setBackgroundColor(chosen);
                } else if(e.getSource() == gradientAButton) {
                    bezAnim.setGradientColorA(chosen);
                } else {
                    bezAnim.setGradientColorB(chosen);
                }
            }
        };

        outerColorButton.addActionListener(l);
        backgroundColorButton.addActionListener(l);
        gradientAButton.addActionListener(l);
        gradientBButton.addActionListener(l);

        // Add everything to the panel
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        super.add(p, BorderLayout.CENTER);
        p.add(bezAnim);
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        // Add control buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(VGAP15));

        buttonPanel.add(backgroundColorButton);
        buttonPanel.add(Box.createRigidArea(VGAP15));

        buttonPanel.add(gradientAButton);
        buttonPanel.add(Box.createRigidArea(VGAP15));

        buttonPanel.add(gradientBButton);
        buttonPanel.add(Box.createRigidArea(VGAP15));

        buttonPanel.add(outerColorButton);
        buttonPanel.add(Box.createRigidArea(VGAP15));

        // Add the panel midway down the panel
        p.add(Box.createRigidArea(HGAP10));
        p.add(buttonPanel);
        p.add(Box.createRigidArea(HGAP10));
    	return p;
    }

    class ColorSwatch implements Icon {
        String gradient;
        BezierAnimationPanel bez;

        public ColorSwatch(String g, BezierAnimationPanel b) {
            bez = b;
            gradient = g;
        }

        public int getIconWidth() {
            return 11;
        }

        public int getIconHeight() {
            return 11;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.black);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            if(gradient.equals("GradientA")) {
                g.setColor(bez.getGradientColorA());
            } else if(gradient.equals("GradientB")) {
                g.setColor(bez.getGradientColorB());
            } else if(gradient.equals("Background")) {
                g.setColor(bez.getBackgroundColor());
            } else if(gradient.equals("OuterLine")) {
                g.setColor(bez.getOuterColor());
            }
            g.fillRect(x+2, y+2, getIconWidth()-4, getIconHeight()-4);
        }
    }

}
