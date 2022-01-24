/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXPanel;

/**
 * Split Pane demo
 *
 * @author Scott Violet
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class SplitPaneDemo extends AbstractDemo {

	public static final String ICON_PATH = "toolbar/JSplitPane.gif";

	private static final long serialVersionUID = 5987956209025810711L;
    private static final String IMG_PATH = "splitpane/"; // prefix dir

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
//        SplitPaneDemo demo = new SplitPaneDemo(null);
//        demo.mainImpl();
    }

    JSplitPane splitPane = null;
    JLabel earth = null;
    JLabel moon = null;

    // Controller:
    JTextField divSize;
    JTextField earthSize;
    JTextField moonSize;

    /**
     * SplitPaneDemo Constructor
     */
    public SplitPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	earth = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"earth.jpg"));
        earth.setMinimumSize(new Dimension(20, 20));

        moon = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"moon.jpg"));
        moon.setMinimumSize(new Dimension(20, 20));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, earth, moon);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(200);

        super.add(splitPane, BorderLayout.CENTER);
        super.setBackground(Color.black);
    }

    /**
     * Creates controls to alter the JSplitPane.
     */
    @Override
	public JXPanel getControlPane() {
        JXPanel wrapper = new JXPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton button;

        Box buttonWrapper = new Box(BoxLayout.X_AXIS);

        wrapper.setLayout(new GridLayout(0, 1));

        /* Create a radio button to vertically split the split pane. */
        button = new JRadioButton(getString("vert_split"));
        button.setMnemonic(getMnemonic("vert_split_mnemonic"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);

        // Create a radio button the horizontally split the split pane.
        button = new JRadioButton(getString("horz_split"));
        button.setMnemonic(getMnemonic("horz_split_mnemonic"));
        button.setSelected(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);

        // Create a check box as to whether or not the split pane continually lays out the component when dragging.
        JCheckBox checkBox = new JCheckBox(getString("cont_layout"));
        checkBox.setMnemonic(getMnemonic("cont_layout_mnemonic"));
        checkBox.setSelected(true);

        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setContinuousLayout(((JCheckBox)e.getSource()).isSelected());
            }
        });
        buttonWrapper.add(checkBox);

        // Create a check box as to whether or not the split pane divider contains the oneTouchExpandable buttons.
        checkBox = new JCheckBox(getString("one_touch_expandable"));
        checkBox.setMnemonic(getMnemonic("one_touch_expandable_mnemonic"));
        checkBox.setSelected(true);

        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setOneTouchExpandable(((JCheckBox) e.getSource()).isSelected());
            }
        });
        buttonWrapper.add(checkBox);
        wrapper.add(buttonWrapper);

        /* Create a text field to change the divider size. */
        JPanel                   tfWrapper;
        JLabel                   label;

        divSize = new JTextField();
        divSize.setText(Integer.valueOf(splitPane.getDividerSize()).toString());
        divSize.setColumns(5);
        divSize.getAccessibleContext().setAccessibleName(getString("divider_size"));
        divSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String  value = ((JTextField)e.getSource()).getText();
                int newSize;

                try {
                    newSize = Integer.parseInt(value);
                } catch (Exception ex) {
                    newSize = -1;
                }
                if(newSize > 0) {
                    splitPane.setDividerSize(newSize);
                } else {
                    JOptionPane.showMessageDialog(splitPane,
                                                  getString("invalid_divider_size"),
                                                  getString("error"),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        label = new JLabel(getString("divider_size"));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(divSize);
        label.setLabelFor(divSize);
        label.setDisplayedMnemonic(getMnemonic("divider_size_mnemonic"));
        wrapper.add(tfWrapper);

        // Create a text field that will change the preferred/minimum size of the earth component.
        earthSize = new JTextField(String.valueOf(earth.getMinimumSize().width));
        earthSize.setColumns(5);
        earthSize.getAccessibleContext().setAccessibleName(getString("first_component_min_size"));
        earthSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String           value = ((JTextField)e.getSource()).getText();
                int              newSize;

                try {
                    newSize = Integer.parseInt(value);
                } catch (Exception ex) {
                    newSize = -1;
                }
                if(newSize > 10) {
                    earth.setMinimumSize(new Dimension(newSize, newSize));
                } else {
                    JOptionPane.showMessageDialog(splitPane,
                                                  getString("invalid_min_size") +
                                                  getString("must_be_greater_than") + 10,
                                                  getString("error"),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        label = new JLabel(getString("first_component_min_size"));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(earthSize);
        label.setLabelFor(earthSize);
        label.setDisplayedMnemonic(getMnemonic("first_component_min_size_mnemonic"));
        wrapper.add(tfWrapper);

        // Create a text field that will change the preferred/minimum size of the moon component.
        moonSize = new JTextField(String.valueOf(moon.getMinimumSize().width));
        moonSize.setColumns(5);
        moonSize.getAccessibleContext().setAccessibleName(getString("second_component_min_size"));
        moonSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String           value = ((JTextField)e.getSource()).getText();
                int              newSize;

                try {
                    newSize = Integer.parseInt(value);
                } catch (Exception ex) {
                    newSize = -1;
                }
                if(newSize > 10) {
                    moon.setMinimumSize(new Dimension(newSize, newSize));
                } else {
                    JOptionPane.showMessageDialog(splitPane,
                                                  getString("invalid_min_size") +
                                                  getString("must_be_greater_than") + 10,
                                                  getString("error"),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        label = new JLabel(getString("second_component_min_size"));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(moonSize);
        label.setLabelFor(moonSize);
        label.setDisplayedMnemonic(getMnemonic("second_component_min_size_mnemonic"));
        wrapper.add(tfWrapper);

        return wrapper;
    }

    void updateDragEnabled(boolean dragEnabled) {
        divSize.setDragEnabled(dragEnabled);
        earthSize.setDragEnabled(dragEnabled);
        moonSize.setDragEnabled(dragEnabled);
    }

}
