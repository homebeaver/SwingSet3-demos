/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

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
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

/**
 * Split Pane demo
 *
 * @author Scott Violet
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class SplitPaneDemo extends AbstractDemo implements ComponentListener {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JSplitPane.gif";

	private static final long serialVersionUID = 5987956209025810711L;
	private static final Logger LOG = Logger.getLogger(SplitPaneDemo.class.getName());
	private static final String DESCRIPTION = "JSplitPane Demo";
    private static final String IMG_PATH = "splitpane/"; // prefix dir

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				controller.setName("controller");
				AbstractDemo demo = new SplitPaneDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
        		LOG.info("frame isResizable="+frame.isResizable()+" size/min "+frame.getSize() + " / "+frame.getMinimumSize());
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
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
     * @param controllerFrame controller Frame
     */
    public SplitPaneDemo(Frame controllerFrame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	controllerFrame.setTitle(getBundleString("name"));

    	this.addComponentListener(this);
    	
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
        JXPanel controller = new JXPanel();
        ButtonGroup group = new ButtonGroup();
        JRadioButton button;

        Box buttonWrapper = new Box(BoxLayout.X_AXIS);

        controller.setLayout(new GridLayout(0, 1));

        /* Create a radio button to vertically split the split pane. */
        button = new JRadioButton();
        button.setText(getBundleString("vert_split.labelAndMnemonic", button));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);

        // Create a radio button the horizontally split the split pane.
        button = new JRadioButton();
        button.setText(getBundleString("horz_split.labelAndMnemonic", button));
        button.setSelected(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            }
        });
        group.add(button);
        buttonWrapper.add(button);

        // Create a check box as to whether or not the split pane continually lays out the component when dragging.
        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(getBundleString("cont_layout.labelAndMnemonic", checkBox));
        checkBox.setSelected(true);

        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setContinuousLayout(((JCheckBox)e.getSource()).isSelected());
            }
        });
        buttonWrapper.add(checkBox);

        // Create a check box as to whether or not the split pane divider contains the oneTouchExpandable buttons.
        checkBox = new JCheckBox();
        checkBox.setText(getBundleString("one_touch_expandable.labelAndMnemonic", checkBox));
        checkBox.setSelected(true);

        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                splitPane.setOneTouchExpandable(((JCheckBox) e.getSource()).isSelected());
            }
        });
        buttonWrapper.add(checkBox);
        controller.add(buttonWrapper);

        /* Create a text field to change the divider size. */
        JPanel tfWrapper; // with label and JTextField
        JLabel label;

        divSize = new JTextField();
        divSize.setText(Integer.valueOf(splitPane.getDividerSize()).toString());
        divSize.setColumns(5);
        divSize.getAccessibleContext().setAccessibleName("divider_size");
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
                    setMinimumSize();
                } else {
                    JOptionPane.showMessageDialog(splitPane,
                                                  getBundleString("invalid_divider_size"),
                                                  getBundleString("error"),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        label = new JLabel();
        label.setText(getBundleString("divider_size.labelAndMnemonic", label));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(divSize);
        label.setLabelFor(divSize);
        controller.add(tfWrapper);

        // Create a text field that will change the preferred/minimum size of the earth component.
        earthSize = new JTextField(String.valueOf(earth.getMinimumSize().width));
        earthSize.setColumns(5);
        earthSize.getAccessibleContext().setAccessibleName("first_component_min_size");
        earthSize.addActionListener( ae -> {
        	String value = ((JTextField)ae.getSource()).getText();
        	int newSize;
            try {
                newSize = Integer.parseInt(value);
            } catch (Exception ex) {
                newSize = -1;
            }
            if(newSize > 10) {
            	// this is a demo, so square Size is enough
            	earth.setMinimumSize(new Dimension(newSize, newSize));
            	setMinimumSize();
            } else {
                JOptionPane.showMessageDialog(splitPane,
                        getBundleString("invalid_min_size") +
                        getBundleString("must_be_greater_than") + 10,
                        getBundleString("error"),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        label = new JLabel();
        label.setText(getBundleString("first_component_min_size.labelAndMnemonic", label));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(earthSize);
        label.setLabelFor(earthSize);
        controller.add(tfWrapper);

        // Create a text field that will change the preferred/minimum size of the moon component.
        moonSize = new JTextField(String.valueOf(moon.getMinimumSize().width));
        moonSize.setColumns(5);
        moonSize.getAccessibleContext().setAccessibleName("second_component_min_size");
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
                	// this is a demo, so square Size is enough
                    moon.setMinimumSize(new Dimension(newSize, newSize));
                    setMinimumSize();
                } else {
                    JOptionPane.showMessageDialog(splitPane,
                                                  getBundleString("invalid_min_size") +
                                                  getBundleString("must_be_greater_than") + 10,
                                                  getBundleString("error"),
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        label = new JLabel();
        label.setText(getBundleString("second_component_min_size.labelAndMnemonic", label));
        tfWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tfWrapper.add(label);
        tfWrapper.add(moonSize);
        label.setLabelFor(moonSize);
        controller.add(tfWrapper);

        return controller;
    }

    /**
     * setMinimumSize of the frame 
     */
    void setMinimumSize() {
    	int width = earth.getMinimumSize().width + moon.getMinimumSize().width + splitPane.getDividerSize();
    	int height = earth.getMinimumSize().height + moon.getMinimumSize().height;
    	LOG.info("---------sets MinimumSize of frame to:"+width + "x"+height);
    	// Resizing operation may be restricted if the user tries to resize window below the minimumSize value. 
    	// This window behaviour is platform-dependent. 
    	JXFrame frame = (JXFrame) SwingUtilities.windowForComponent(this);
    	frame.setMinimumSize(new Dimension(width, height));   	
    }
    void updateDragEnabled(boolean dragEnabled) {
        divSize.setDragEnabled(dragEnabled);
        earthSize.setDragEnabled(dragEnabled);
        moonSize.setDragEnabled(dragEnabled);
    }

    // implements ComponentListener :
    
    /**
     * show a warning, when componentResized is below limits
     */
	@Override
	public void componentResized(ComponentEvent e) {
		Component comp = (Component)(e.getSource());
		if(getMinimumSize().getWidth()>getSize().getWidth() || getMinimumSize().getHeight()>getSize().getHeight()) {
			LOG.warning("!!!!!! comp.size "+comp.getSize()+ " < "+comp.getMinimumSize());
//			comp.setSize(comp.getMinimumSize());
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {		
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

}
