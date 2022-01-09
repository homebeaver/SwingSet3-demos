/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SingleSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.BorderUIResource;

import swingset.borderpatch.BasicMarginBorder;
import swingset.borderpatch.MetalButtonBorder;

/**
 * JButton, JRadioButton, (JToggleButton), JCheckBox Demos
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (Traffic Light Buttons)
 */
public class ButtonDemo extends DemoModule {

	public static final String ICON_PATH = "toolbar/JButton.gif";

	private static final long serialVersionUID = -61808634982886166L;
	private static final Logger LOG = Logger.getLogger(ButtonDemo.class.getName());

    JTabbedPane tab;

    JPanel buttonPanel = new JPanel();
    JPanel checkboxPanel = new JPanel();
    JPanel radioButtonPanel = new JPanel();

    Vector<AbstractButton> buttons = new Vector<AbstractButton>();
    Vector<AbstractButton> checkboxes = new Vector<AbstractButton>();
    Vector<AbstractButton> radiobuttons = new Vector<AbstractButton>();
    private Vector<AbstractButton> togglebuttons = new Vector<AbstractButton>(); // not used

    // radiobuttons
    Vector<AbstractButton> currentControls = buttons;

    JButton button;
    JCheckBox check;
    JRadioButton radio;
//    private JToggleButton toggle;

    EmptyBorder border5 = new EmptyBorder(5,5,5,5);
    EmptyBorder border10 = new EmptyBorder(10,10,10,10);

    Insets insets0 = new Insets(0,0,0,0);
    Insets insets10 = new Insets(10,10,10,10);

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        ButtonDemo demo = new ButtonDemo(new SwingSet2(null, gc, false));
        demo.mainImpl();
    }

    /**
     * ButtonDemo Constructor
     */
    public ButtonDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this demo inside SwingSet2.
        super(swingset, "ButtonDemo", ICON_PATH); 

        tab = new JTabbedPane();
        tab.getModel().addChangeListener(e -> {
            SingleSelectionModel model = (SingleSelectionModel) e.getSource();
            if(model.getSelectedIndex() == 0) {
                currentControls = buttons;
            } else if(model.getSelectedIndex() == 1) {
                currentControls = radiobuttons;
            } else if(model.getSelectedIndex() == 2) {
                currentControls = checkboxes;
            } else {
                currentControls = togglebuttons;
            }
        });

        JPanel demo = getDemoPanel();
        demo.setLayout(new BoxLayout(demo, BoxLayout.Y_AXIS));
        demo.add(tab);

        addButtons();
        addRadioButtons();
        addCheckBoxes();
        currentControls = buttons;
    }

    public void addButtons() {
        tab.addTab(getString("ButtonDemo.buttons"), buttonPanel);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(border5);

        JPanel verticalPane = createVerticalPanel(true);
        verticalPane.setAlignmentY(Component.TOP_ALIGNMENT);
        buttonPanel.add(verticalPane);

        // Text Buttons
        JPanel p2 = createHorizontalPanel(false);
        verticalPane.add(p2);
        p2.setBorder(new CompoundBorder(new TitledBorder(null, getString("ButtonDemo.textbuttons"),
                                                          TitledBorder.LEFT, TitledBorder.TOP), border5));

        String buttonText1 = getString("ButtonDemo.button1");
        button = new JButton(buttonText1);
        // wg. https://github.com/homebeaver/SwingSet/issues/18 :
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));

        button = new JButton(getString("ButtonDemo.button2"));
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));
        
        String buttonText3 = getString("ButtonDemo.button3");
        button = new JButton(buttonText3);
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        p2.add(button);
        buttons.add(button);

        // Image Buttons
        verticalPane.add(Box.createRigidArea(VGAP30));
        JPanel p3 = createHorizontalPanel(false); // true == loweredBorder from super
        verticalPane.add(p3);
        createImageButtons(p3);
        
        // traffic lights buttons
        verticalPane.add(Box.createRigidArea(VGAP30));
        JPanel p4 = createHorizontalPanel(true);
        verticalPane.add(p4);
        createTrafficLightButtons(p4);

        verticalPane.add(Box.createVerticalGlue());
        
        buttonPanel.add(Box.createHorizontalGlue());
        currentControls = buttons; // currentControls is global para for createControls!
        buttonPanel.add(createControls());       
    }

    CircleIcon outoforder = new CircleIcon(CircleIcon.ACTION_ICON, null);
    CircleIcon red = new CircleIcon(CircleIcon.ACTION_ICON, Color.RED);
    CircleIcon yellow = new CircleIcon(CircleIcon.ACTION_ICON, Color.YELLOW);
    CircleIcon green = new CircleIcon(CircleIcon.ACTION_ICON, Color.GREEN);
    private void createTrafficLightButtons(JComponent pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.setBorder(new TitledBorder(null, "Traffic Light Buttons",
                                         TitledBorder.LEFT, TitledBorder.TOP));
        
//        String description = "green - yellow - red";
        
        button = new JButton("<html>green<p>red when pressed</html>", green);
        button.setName("green"); // used in listener
        button.setPressedIcon(red);
        button.setRolloverIcon(yellow);
        button.setDisabledIcon(outoforder);
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        button = new JButton("red", red);
        button.setName("red");
        button.setPressedIcon(green);
        button.setRolloverIcon(yellow);
        button.setDisabledIcon(outoforder);
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        button = new JButton("switching", green);
        button.setName("flipflop"); // used in listener
        if(button.getBorder() instanceof BorderUIResource.CompoundBorderUIResource) {
        	BasicMarginBorder bmb = new BasicMarginBorder();
        	button.setBorder(new CompoundBorder(new MetalButtonBorder(bmb), bmb));
        }
        JButton b = button;
        b.addMouseListener(new MouseAdapter() {
        	@SuppressWarnings("unused")
			Icon old = null;
        	Icon rem = null;
        	// (pressed and released)
            public void mouseClicked(MouseEvent e) {
            	LOG.info("Icon:"+b.getIcon());
            	if(b.getIcon()==outoforder) {
            		// ready
                	old = b.getIcon();
            	} else if(b.getIcon()==green) {
                	b.setIcon(red);
            	} else if(b.getIcon()==red) {
                	b.setIcon(green);
            	} else if(b.getIcon()==yellow && rem==green) {
                	b.setIcon(red);
                	b.setSelected(true);
            	} else if(b.getIcon()==yellow && rem==red) {
                	b.setIcon(green);
                	b.setSelected(false);
            	}
            }
            
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseEntered(MouseEvent e) {
            	rem = b.getIcon();
            	b.setIcon(yellow);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseExited(MouseEvent e) {
            	b.setIcon(b.isSelected() ? red : green);
            }          
        });

        pane.add(button);
        buttons.add(button);
//        pane.add(Box.createRigidArea(HGAP10)); no gap at the end
    }
    
    // global 	Vector<Component> buttons
    //		-	aus super: HGAP10 / sollten final sein, sind sie aber nicht
    private void createImageButtons(JComponent pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.setBorder(new TitledBorder(null, getString("ButtonDemo.imagebuttons"),
                                         TitledBorder.LEFT, TitledBorder.TOP));

        // phone image button mit vier icons
        String description = getString("ButtonDemo.phone");
        button = new JButton(createImageIcon("buttons/b1.gif", description));
        button.setPressedIcon(createImageIcon("buttons/b1p.gif", description));
        button.setRolloverIcon(createImageIcon("buttons/b1r.gif", description));
        button.setDisabledIcon(createImageIcon("buttons/b1d.gif", description));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        // write image button
        description = getString("ButtonDemo.write");
        button = new JButton(createImageIcon("buttons/b2.gif", description));
        button.setPressedIcon(createImageIcon("buttons/b2p.gif", description));
        button.setRolloverIcon(createImageIcon("buttons/b2r.gif", description));
        button.setDisabledIcon(createImageIcon("buttons/b2d.gif", description));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));

        // peace image button
        description = getString("ButtonDemo.peace");
        button = new JButton(createImageIcon("buttons/b3.gif", description));
        button.setPressedIcon(createImageIcon("buttons/b3p.gif", description));
        button.setRolloverIcon(createImageIcon("buttons/b3r.gif", description));
        button.setDisabledIcon(createImageIcon("buttons/b3d.gif", description));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
    }
    
    public void addRadioButtons() {
        ButtonGroup group = new ButtonGroup();

        tab.addTab(getString("ButtonDemo.radiobuttons"), radioButtonPanel);
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.X_AXIS));
        radioButtonPanel.setBorder(border5);

        JPanel p1 = createVerticalPanel(true);
        p1.setAlignmentY(Component.TOP_ALIGNMENT);
        radioButtonPanel.add(p1);

        // Text Radio Buttons
        JPanel p2 = createHorizontalPanel(false);
        p1.add(p2);
        p2.setBorder(new CompoundBorder(
                      new TitledBorder(
                        null, getString("ButtonDemo.textradiobuttons"),
                        TitledBorder.LEFT, TitledBorder.TOP), border5)
        );

        radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio1")));
        group.add(radio);
        radiobuttons.add(radio);
        p2.add(Box.createRigidArea(HGAP10));

        radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio2")));
        group.add(radio);
        radiobuttons.add(radio);
        p2.add(Box.createRigidArea(HGAP10));

        radio = (JRadioButton)p2.add(
                new JRadioButton(getString("ButtonDemo.radio3")));
        group.add(radio);
        radiobuttons.add(radio);

        // Image Radio Buttons
        group = new ButtonGroup();
        p1.add(Box.createRigidArea(VGAP30));
        JPanel p3 = createHorizontalPanel(false);
        p1.add(p3);
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.setBorder(new TitledBorder(null, getString("ButtonDemo.imageradiobuttons"),
                                         TitledBorder.LEFT, TitledBorder.TOP));

        // image radio button 1
        String description = getString("ButtonDemo.customradio");
        String text = getString("ButtonDemo.radio1");
        radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
        radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        p3.add(radio);
        radiobuttons.add(radio);
        p3.add(Box.createRigidArea(HGAP20));

        // image radio button 2
        text = getString("ButtonDemo.radio2");
        radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
        radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        p3.add(radio);
        radiobuttons.add(radio);
        p3.add(Box.createRigidArea(HGAP20));

        // image radio button 3
        text = getString("ButtonDemo.radio3");
        radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
        radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
        radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
        radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
        radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        radiobuttons.add(radio);
        p3.add(radio);

        // verticaly glue fills out the rest of the box
        p1.add(Box.createVerticalGlue());

        radioButtonPanel.add(Box.createHorizontalGlue());
        currentControls = radiobuttons;
        radioButtonPanel.add(createControls());
    }


    public void addCheckBoxes() {
        tab.addTab(getString("ButtonDemo.checkboxes"), checkboxPanel);
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.X_AXIS));
        checkboxPanel.setBorder(border5);

        JPanel p1 = createVerticalPanel(true);
        p1.setAlignmentY(Component.TOP_ALIGNMENT);
        checkboxPanel.add(p1);

        // Text Radio Buttons
        JPanel p2 = createHorizontalPanel(false);
        p1.add(p2);
        p2.setBorder(new CompoundBorder(
                      new TitledBorder(
                        null, getString("ButtonDemo.textcheckboxes"),
                        TitledBorder.LEFT, TitledBorder.TOP), border5)
        );

        check = new JCheckBox(getString("ButtonDemo.check1"));
        checkboxes.add(check);
        p2.add(check);
        p2.add(Box.createRigidArea(HGAP10));

        check = new JCheckBox(getString("ButtonDemo.check2"));
        checkboxes.add(check);
        p2.add(check);
        p2.add(Box.createRigidArea(HGAP10));

        check = new JCheckBox(getString("ButtonDemo.check3"));
        checkboxes.add(check);
        p2.add(check);

        // Image Radio Buttons
        p1.add(Box.createRigidArea(VGAP30));
        JPanel p3 = createHorizontalPanel(false);
        p1.add(p3);
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.setBorder(new TitledBorder(null, getString("ButtonDemo.imagecheckboxes"),
                                         TitledBorder.LEFT, TitledBorder.TOP));

        // image checkbox 1
        String description = getString("ButtonDemo.customcheck");
        String text = getString("ButtonDemo.check1");
        check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
        check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
        check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
        check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);
        p3.add(Box.createRigidArea(HGAP20));

        // image checkbox 2
        text = getString("ButtonDemo.check2");
        check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
        check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
        check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
        check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);
        p3.add(Box.createRigidArea(HGAP20));

        // image checkbox 3
        text = getString("ButtonDemo.check3");
        check = new JCheckBox(text, createImageIcon("buttons/cb.gif", description));
        check.setRolloverIcon(createImageIcon("buttons/cbr.gif", description));
        check.setRolloverSelectedIcon(createImageIcon("buttons/cbrs.gif", description));
        check.setSelectedIcon(createImageIcon("buttons/cbs.gif", description));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);

        // verticaly glue fills out the rest of the box
        p1.add(Box.createVerticalGlue());

        checkboxPanel.add(Box.createHorizontalGlue());
        currentControls = checkboxes;
        checkboxPanel.add(createControls());
    }

    /*
     * JPanel controls aka Controler ist im rechten Teil der Panel buttonPanel, radioButtonPanel, checkboxPanel positioniert.
     * Es beinhaltet Controler für
     * - JCheckBox'es : Display Options
     * - JRadioButton's : Pad Amount
     * - LayoutControlPanel+DirectionPanel mit 
     * - - Text Position
     * - - Content Alignment
     */
    /**
     * creates controler panel with checkBoxes for Display Options and radioButtons for Pad Amount
     * and LayoutControlPanel for Text Position and Content Alignment
     * 
     * @see LayoutControlPanel
     * @see DirectionPanel
     */
    public JPanel createControls() {
        @SuppressWarnings("serial")
		JPanel controls = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(300, super.getMaximumSize().height);
            }
        };
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setAlignmentY(Component.TOP_ALIGNMENT);
        controls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonControls = createHorizontalPanel(true);
        buttonControls.setAlignmentY(Component.TOP_ALIGNMENT);
        buttonControls.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel leftColumn = createVerticalPanel(false);
        leftColumn.setAlignmentY(Component.TOP_ALIGNMENT);
        leftColumn.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel rightColumn = new LayoutControlPanel(this);

        buttonControls.add(leftColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));
        buttonControls.add(rightColumn);
        buttonControls.add(Box.createRigidArea(HGAP20));

        controls.add(buttonControls);

        // Display Options
        JLabel l = new JLabel(getString("ButtonDemo.controlpanel_label"));
        leftColumn.add(l);

        JCheckBox bordered = new JCheckBox(getString("ButtonDemo.paintborder"));
        bordered.setActionCommand("PaintBorder");
        bordered.setMnemonic(getMnemonic("ButtonDemo.paintborder_mnemonic"));
        bordered.setToolTipText(getString("ButtonDemo.paintborder_tooltip"));
        if (currentControls == buttons) {
                bordered.setSelected(true); // initial
        }

        bordered.addItemListener(e -> {
			JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == bordered
			boolean borderPainted = cb.isSelected();
			AbstractButton b;
			LOG.config("bordered currentControls.size()=" + currentControls.size());
			for (int i = 0; i < currentControls.size(); i++) {
				b = (AbstractButton) currentControls.elementAt(i);
				b.setBorderPainted(borderPainted);
				b.invalidate();
			}
        });
        leftColumn.add(bordered);

        JCheckBox focused = new JCheckBox(getString("ButtonDemo.paintfocus"));
        focused.setActionCommand("PaintFocus");
        focused.setToolTipText(getString("ButtonDemo.paintfocus_tooltip"));
        focused.setMnemonic(getMnemonic("ButtonDemo.paintfocus_mnemonic"));
        focused.setSelected(true);

        focused.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == focused
        	boolean focusPainted = cb.isSelected();
            AbstractButton b;
			LOG.config("focused currentControls.size()=" + currentControls.size());
            for(int i = 0; i < currentControls.size(); i++) {
                b = (AbstractButton) currentControls.elementAt(i);
                b.setFocusPainted(focusPainted);
                b.invalidate();
            }
        });
        leftColumn.add(focused);

        JCheckBox enabled = new JCheckBox(getString("ButtonDemo.enabled"));
        enabled.setActionCommand("Enabled");
        enabled.setMnemonic(getMnemonic("ButtonDemo.enabled_mnemonic"));
        enabled.setToolTipText(getString("ButtonDemo.enabled_tooltip"));
        enabled.setSelected(true);

        enabled.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == enabled
        	boolean enable = cb.isSelected();
            Component c;
            for(int i = 0; i < currentControls.size(); i++) {
                c = (Component) currentControls.elementAt(i);
                if(c instanceof JButton) {
                	JButton b = (JButton)c;
                	if("green".equals(b.getName())) { // set the green light out of order
                		b.setIcon(enable ? green : outoforder);
                	} else if("flipflop".equals(b.getName())) { // set the flipflop light out of order
                		b.setIcon(enable ? green : outoforder);
                	} else if("red".equals(b.getName())) { 
                		// red light remains red!
                	} else {
                		c.setEnabled(enable);
                	}
                }
                c.invalidate();
            }
        });
        leftColumn.add(enabled);

        JCheckBox filled = new JCheckBox(getString("ButtonDemo.contentfilled"));
        filled.setActionCommand("ContentFilled");
        filled.setMnemonic(getMnemonic("ButtonDemo.contentfilled_mnemonic"));
        filled.setToolTipText(getString("ButtonDemo.contentfilled_tooltip"));
        filled.setSelected(true);

        filled.addItemListener(e -> {
        	JCheckBox cb = (JCheckBox) e.getSource(); // cb == e.getSource() == filled
        	boolean caFilled = cb.isSelected();
            AbstractButton b;
            for(int i = 0; i < currentControls.size(); i++) {
                b = (AbstractButton) currentControls.elementAt(i);
                b.setContentAreaFilled(caFilled);
                b.invalidate();
            }
        });
        leftColumn.add(filled);

        leftColumn.add(Box.createRigidArea(VGAP20));

        l = new JLabel(getString("ButtonDemo.padamount_label"));
        leftColumn.add(l);
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton defaultPad = new JRadioButton(getString("ButtonDemo.default"));
        defaultPad.setMnemonic(getMnemonic("ButtonDemo.default_mnemonic"));
        defaultPad.setToolTipText(getString("ButtonDemo.default_tooltip"));
        defaultPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == defaultPad
        	if(rb.isSelected()) {
                AbstractButton b;
                LOG.config("defaultPad currentControls.size()="+currentControls.size());
                for(int i = 0; i < currentControls.size(); i++) {
//                    LOG.info("i="+i + "currentControl:"+currentControls.elementAt(i));
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(null);
                    b.invalidate();
                }
        	}
        });
        group.add(defaultPad);
        defaultPad.setSelected(true);
        leftColumn.add(defaultPad);

        JRadioButton zeroPad = new JRadioButton(getString("ButtonDemo.zero"));
        zeroPad.setActionCommand("ZeroPad");
        zeroPad.setMnemonic(getMnemonic("ButtonDemo.zero_mnemonic"));
        zeroPad.setToolTipText(getString("ButtonDemo.zero_tooltip"));
        zeroPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == zeroPad
        	if(rb.isSelected()) {
                AbstractButton b;
                LOG.config("zeroPad currentControls.size()="+currentControls.size());
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(insets0);
                    b.invalidate();
                }
        	}
        });
        group.add(zeroPad);
        leftColumn.add(zeroPad);

        JRadioButton tenPad = new JRadioButton(getString("ButtonDemo.ten"));
        tenPad.setActionCommand("TenPad");
        tenPad.setMnemonic(getMnemonic("ButtonDemo.ten_mnemonic"));
        tenPad.setToolTipText(getString("ButtonDemo.ten_tooltip"));

        tenPad.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == tenPad
        	if(rb.isSelected()) {
                AbstractButton b;
                for(int i = 0; i < currentControls.size(); i++) {
                    b = (AbstractButton) currentControls.elementAt(i);
                    b.setMargin(insets10);
                    b.invalidate();
                }
        	}
        });
        group.add(tenPad);
        leftColumn.add(tenPad);

//        leftColumn.add(Box.createRigidArea(VGAP20));
        return controls;
    }

    public Vector<AbstractButton> getCurrentControls() {
        return currentControls;
    }
}
