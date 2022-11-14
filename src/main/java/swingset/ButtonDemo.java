/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.CircleIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.TrafficLightGreenIcon;
import org.jdesktop.swingx.icon.TrafficLightRedIcon;
import org.jdesktop.swingx.icon.TrafficLightYellowIcon;

/**
 * JButton, JRadioButton, (JToggleButton), JCheckBox Demos
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (Traffic Light Buttons)
 */
public class ButtonDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JButton.gif";

	private static final long serialVersionUID = -61808634982886166L;
	private static final Logger LOG = Logger.getLogger(ButtonDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new ButtonDemo(controller);
			JXFrame frame = new JXFrame("demo", exitOnClose);
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
     * ButtonDemo Constructor
     * 
     * @param frame controller Frame
     */
    public ButtonDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        JXPanel demo = new JXPanel();
        super.add(demo, BorderLayout.CENTER);
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

        demo.setLayout(new BoxLayout(demo, BoxLayout.Y_AXIS));
        demo.add(tab);

        addButtons();
        addRadioButtons();
        addCheckBoxes();
        currentControls = buttons;
    }

    // wg. https://github.com/homebeaver/SwingSet/issues/18 :
//    private Border patchedBorder(JButton button) {
//    	Border buttonBorder = button.getBorder();
//        if(buttonBorder instanceof BorderUIResource.CompoundBorderUIResource) {
//        	CompoundBorder cb = (CompoundBorder) buttonBorder; // cast OK, denn CompoundBorderUIResource subclass von CompoundBorder
//        	Border ob = cb.getOutsideBorder();
//        	Border ib = cb.getInsideBorder();
//        	LOG.info("plaf.metal CompoundBorder Button.border : "+cb.getClass().getSimpleName() 
//        			+ " "+ob.getClass().getSimpleName() + " "+ib.getClass().getSimpleName() + " for Button \""+button.getText());
//        	if(ob instanceof MetalBorders.ButtonBorder && ib instanceof BasicBorders.MarginBorder) {
//        		ob = new MetalButtonBorder();
//        		ib = new BasicMarginBorder();
//        		((MetalButtonBorder)ob).setInsideBorder(ib);
//            	return new BorderUIResource.CompoundBorderUIResource(ob, ib);
//        	}
//        	return cb;
//        }
//        return buttonBorder;
//    }

    // aus DemoModule
    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
    private JXPanel createHorizontalPanel(boolean threeD) {
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }
    private JXPanel createVerticalPanel(boolean threeD) {
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    /**
     * add Buttons
     */
    public void addButtons() {
        tab.addTab(getBundleString("buttons"), buttonPanel);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(border5);

        JXPanel verticalPane = createVerticalPanel(true);
        verticalPane.setAlignmentY(Component.TOP_ALIGNMENT);
        buttonPanel.add(verticalPane);

        // Text Buttons
        JXPanel p2 = createHorizontalPanel(false);
        verticalPane.add(p2);
        p2.setBorder(new CompoundBorder(new TitledBorder(null, getBundleString("textbuttons"),
                                                          TitledBorder.LEFT, TitledBorder.TOP), border5));

        String buttonText1 = getBundleString("button1");
        button = new JButton(buttonText1);
        // wg. https://github.com/homebeaver/SwingSet/issues/18 :
//        button.setBorder(patchedBorder(button));
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));

        button = new JButton(getBundleString("button2"));
//        button.setBorder(patchedBorder(button));
        p2.add(button);
        buttons.add(button);
        p2.add(Box.createRigidArea(HGAP10));
        
        String buttonText3 = getBundleString("button3");
        button = new JButton(buttonText3);
//        button.setBorder(patchedBorder(button));
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
    }

	RadianceIcon outoforder = CircleIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
	RadianceIcon red = TrafficLightRedIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
	RadianceIcon yellow = TrafficLightYellowIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
	RadianceIcon green = TrafficLightGreenIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
    private void createTrafficLightButtons(JComponent pane) {
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.setBorder(new TitledBorder(null, "Traffic Light Buttons", TitledBorder.LEFT, TitledBorder.TOP));
        
//        String description = "green - yellow - red";
        
        button = new JButton("<html>green<p>red when pressed</html>", green);
        button.setName("green"); // used in listener
        button.setPressedIcon(red);
        button.setRolloverIcon(yellow);
        button.setDisabledIcon(outoforder);
//        button.setBorder(patchedBorder(button));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        button = new JButton("red", red);
        button.setName("red");
        button.setPressedIcon(green);
        button.setRolloverIcon(yellow);
        button.setDisabledIcon(outoforder);
//        button.setBorder(patchedBorder(button));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        button = new JButton("switching", green);
        button.setName("flipflop"); // used in listener
//        button.setBorder(patchedBorder(button));
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
        pane.setBorder(new TitledBorder(null, getBundleString("imagebuttons"),
                                         TitledBorder.LEFT, TitledBorder.TOP));

        // phone image button mit vier icons
        String description = getBundleString("phone");
        button = new JButton(StaticUtilities.createImageIcon("buttons/b1.gif"));
        button.setPressedIcon(StaticUtilities.createImageIcon("buttons/b1p.gif"));
        button.setRolloverIcon(StaticUtilities.createImageIcon("buttons/b1r.gif"));
        button.setDisabledIcon(StaticUtilities.createImageIcon("buttons/b1d.gif"));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));
        
        // write image button
        description = getBundleString("write");
        button = new JButton(StaticUtilities.createImageIcon("buttons/b2.gif"));
        button.setPressedIcon(StaticUtilities.createImageIcon("buttons/b2p.gif"));
        button.setRolloverIcon(StaticUtilities.createImageIcon("buttons/b2r.gif"));
        button.setDisabledIcon(StaticUtilities.createImageIcon("buttons/b2d.gif"));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
        pane.add(Box.createRigidArea(HGAP10));

        // peace image button
        description = getBundleString("peace");
        button = new JButton(StaticUtilities.createImageIcon("buttons/b3.gif"));
        button.setPressedIcon(StaticUtilities.createImageIcon("buttons/b3p.gif"));
        button.setRolloverIcon(StaticUtilities.createImageIcon("buttons/b3r.gif"));
        button.setDisabledIcon(StaticUtilities.createImageIcon("buttons/b3d.gif"));
        button.setMargin(new Insets(0,0,0,0));
        pane.add(button);
        buttons.add(button);
    }
    
    /**
     * add RadioButtons
     */
    public void addRadioButtons() {
        ButtonGroup group = new ButtonGroup();

        tab.addTab(getBundleString("radiobuttons"), radioButtonPanel);
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
                        null, getBundleString("textradiobuttons"),
                        TitledBorder.LEFT, TitledBorder.TOP), border5)
        );

        radio = (JRadioButton)p2.add(new JRadioButton(getBundleString("radio1")));
        group.add(radio);
        radiobuttons.add(radio);
        p2.add(Box.createRigidArea(HGAP10));

        radio = (JRadioButton)p2.add(new JRadioButton(getBundleString("radio2")));
        group.add(radio);
        radiobuttons.add(radio);
        p2.add(Box.createRigidArea(HGAP10));

        radio = (JRadioButton)p2.add(new JRadioButton(getBundleString("radio3")));
        group.add(radio);
        radiobuttons.add(radio);

        // Image Radio Buttons
        group = new ButtonGroup();
        p1.add(Box.createRigidArea(VGAP30));
        JPanel p3 = createHorizontalPanel(false);
        p1.add(p3);
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.setBorder(new TitledBorder(null, getBundleString("imageradiobuttons"),
                                         TitledBorder.LEFT, TitledBorder.TOP));

        // image radio button 1
        String description = getBundleString("customradio");
        String text = getBundleString("radio1");
        radio = new JRadioButton(text, StaticUtilities.createImageIcon("buttons/rb.gif"));
        radio.setPressedIcon(StaticUtilities.createImageIcon("buttons/rbp.gif"));
        radio.setRolloverIcon(StaticUtilities.createImageIcon("buttons/rbr.gif"));
        radio.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/rbrs.gif"));
        radio.setSelectedIcon(StaticUtilities.createImageIcon("buttons/rbs.gif"));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        p3.add(radio);
        radiobuttons.add(radio);
        p3.add(Box.createRigidArea(HGAP20));

        // image radio button 2
        text = getBundleString("radio2");
        radio = new JRadioButton(text, StaticUtilities.createImageIcon("buttons/rb.gif"));
        radio.setPressedIcon(StaticUtilities.createImageIcon("buttons/rbp.gif"));
        radio.setRolloverIcon(StaticUtilities.createImageIcon("buttons/rbr.gif"));
        radio.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/rbrs.gif"));
        radio.setSelectedIcon(StaticUtilities.createImageIcon("buttons/rbs.gif"));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        p3.add(radio);
        radiobuttons.add(radio);
        p3.add(Box.createRigidArea(HGAP20));

        // image radio button 3
        text = getBundleString("radio3");
        radio = new JRadioButton(text, StaticUtilities.createImageIcon("buttons/rb.gif"));
        radio.setPressedIcon(StaticUtilities.createImageIcon("buttons/rbp.gif"));
        radio.setRolloverIcon(StaticUtilities.createImageIcon("buttons/rbr.gif"));
        radio.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/rbrs.gif"));
        radio.setSelectedIcon(StaticUtilities.createImageIcon("buttons/rbs.gif"));
        radio.setMargin(new Insets(0,0,0,0));
        group.add(radio);
        radiobuttons.add(radio);
        p3.add(radio);

        // verticaly glue fills out the rest of the box
        p1.add(Box.createVerticalGlue());

        radioButtonPanel.add(Box.createHorizontalGlue());
        currentControls = radiobuttons;
    }

    /**
     * add CheckBoxes
     */
    public void addCheckBoxes() {
        tab.addTab(getBundleString("checkboxes"), checkboxPanel);
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
                        null, getBundleString("textcheckboxes"),
                        TitledBorder.LEFT, TitledBorder.TOP), border5)
        );

        check = new JCheckBox(getBundleString("check1"));
        checkboxes.add(check);
        p2.add(check);
        p2.add(Box.createRigidArea(HGAP10));

        check = new JCheckBox(getBundleString("check2"));
        checkboxes.add(check);
        p2.add(check);
        p2.add(Box.createRigidArea(HGAP10));

        check = new JCheckBox(getBundleString("check3"));
        checkboxes.add(check);
        p2.add(check);

        // Image Radio Buttons
        p1.add(Box.createRigidArea(VGAP30));
        JPanel p3 = createHorizontalPanel(false);
        p1.add(p3);
        p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
        p3.setBorder(new TitledBorder(null, getBundleString("imagecheckboxes"), TitledBorder.LEFT, TitledBorder.TOP));

        // image checkbox 1
        String description = getBundleString("customcheck");
        String text = getBundleString("check1");
        check = new JCheckBox(text, StaticUtilities.createImageIcon("buttons/cb.gif"));
        check.setRolloverIcon(StaticUtilities.createImageIcon("buttons/cbr.gif"));
        check.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/cbrs.gif"));
        check.setSelectedIcon(StaticUtilities.createImageIcon("buttons/cbs.gif"));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);
        p3.add(Box.createRigidArea(HGAP20));

        // image checkbox 2
        text = getBundleString("check2");
        check = new JCheckBox(text, StaticUtilities.createImageIcon("buttons/cb.gif"));
        check.setRolloverIcon(StaticUtilities.createImageIcon("buttons/cbr.gif"));
        check.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/cbrs.gif"));
        check.setSelectedIcon(StaticUtilities.createImageIcon("buttons/cbs.gif"));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);
        p3.add(Box.createRigidArea(HGAP20));

        // image checkbox 3
        text = getBundleString("check3");
        check = new JCheckBox(text, StaticUtilities.createImageIcon("buttons/cb.gif"));
        check.setRolloverIcon(StaticUtilities.createImageIcon("buttons/cbr.gif"));
        check.setRolloverSelectedIcon(StaticUtilities.createImageIcon("buttons/cbrs.gif"));
        check.setSelectedIcon(StaticUtilities.createImageIcon("buttons/cbs.gif"));
        check.setMargin(new Insets(0,0,0,0));
        p3.add(check);
        checkboxes.add(check);

        // verticaly glue fills out the rest of the box
        p1.add(Box.createVerticalGlue());

        checkboxPanel.add(Box.createHorizontalGlue());
        currentControls = checkboxes;
    }

    /*
     * JPanel controls beinhaltet Controller für
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
    @Override
	public JXPanel getControlPane() {
        @SuppressWarnings("serial")
		JXPanel controls = new JXPanel() {
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
        JLabel l = new JLabel(getBundleString("controlpanel.labelAndMnemonic"));
        leftColumn.add(l);

        JCheckBox bordered = new JCheckBox();
        bordered.setActionCommand("PaintBorder");
        bordered.setText(getBundleString("paintborder.labelAndMnemonic", bordered));
        bordered.setToolTipText(getBundleString("paintborder_tooltip"));
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
				b.getParent().invalidate();
			}
        });
        leftColumn.add(bordered);

        JCheckBox focused = new JCheckBox();
        focused.setActionCommand("PaintFocus");
        focused.setText(getBundleString("paintfocus.labelAndMnemonic", focused));
        focused.setToolTipText(getBundleString("paintfocus_tooltip"));
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

        JCheckBox enabled = new JCheckBox();
        enabled.setActionCommand("Enabled");
        enabled.setText(getBundleString("enabled.labelAndMnemonic", enabled));
        enabled.setToolTipText(getBundleString("enabled_tooltip"));
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

        JCheckBox filled = new JCheckBox();
        filled.setActionCommand("ContentFilled");
        filled.setText(getBundleString("contentfilled.labelAndMnemonic", filled));
        filled.setToolTipText(getBundleString("contentfilled_tooltip"));
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

        l = new JLabel(getBundleString("padamount.labelAndMnemonic")); // no mnemonics
        leftColumn.add(l);
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton defaultPad = new JRadioButton();
        defaultPad.setText(getBundleString("default.labelAndMnemonic", defaultPad));
        defaultPad.setToolTipText(getBundleString("default_tooltip"));
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

        JRadioButton zeroPad = new JRadioButton();
        zeroPad.setActionCommand("ZeroPad");
        zeroPad.setText(getBundleString("zero.labelAndMnemonic", zeroPad));
        zeroPad.setToolTipText(getBundleString("zero_tooltip"));
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

        JRadioButton tenPad = new JRadioButton();
        tenPad.setActionCommand("TenPad");
        tenPad.setText(getBundleString("ten.labelAndMnemonic", tenPad));
        tenPad.setToolTipText(getBundleString("ten_tooltip"));

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

    /**
     * getter
     * @return currentControls
     */
    public Vector<AbstractButton> getCurrentControls() {
        return currentControls;
    }
}
