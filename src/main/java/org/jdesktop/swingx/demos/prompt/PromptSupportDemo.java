/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.prompt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.BuddySupport.Position;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ShapeUtils;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import swingset.AbstractDemo;

/**
 * A demo for {@code PromptSupport}.
 *
 * @author Karl George Schaefer
 * @author Peter Weishapl (original prompt demos)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "PromptSupport Demo",
//    category = "Functionality",
//    description = "Demonstrates PromptSupport, a prompting decoration for text components.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/prompt/PromptSupportDemo.java",
//        "org/jdesktop/swingx/demos/prompt/resources/PromptSupportDemo.properties",
//        "org/jdesktop/swingx/demos/prompt/resources/images/PromptSupportDemo.png"
//    }
//)
public class PromptSupportDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -2286660834393833047L;
	private static final Logger LOG = Logger.getLogger(PromptSupportDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates PromptSupport, a prompting decoration for text components.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new PromptSupportDemo(controller);
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

    JSplitPane splitPane = null;
    JXPanel right;

    //Values for the fields with initials
    private double xamount = 100000;
    private double xrate = 7.5;  //7.5%
    private int xnumPeriods = 30;
    
    //Fields for data entry
	private JTextField textField;
    private JXFormattedTextField amountXField;
    private JXFormattedTextField rateXField;
    private JXFormattedTextField numPeriodsXField;
    private JXFormattedTextField paymentXField;
    
	// Controller:
    private JComboBox<FocusBehavior> focusCombo;
    private JTextField promptText;
    private JComboBox<DisplayInfo<Painter<? super Component>>> backgroundPainter;
    private JComboBox<DisplayInfo<Integer>> fontStyle;

    /**
     * PromptSupportDemo Constructor
     * 
     * @param frame controller Frame
     */
    public PromptSupportDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPane(), createRightPane());
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(300);

        super.add(splitPane, BorderLayout.CENTER);
        super.setBackground(Color.black);
    }
 
    private JComponent createRightPane() {
        right = new JXPanel(new VerticalLayout(3));
        right.setName("right");
    	
        JXTitledSeparator sep = new JXTitledSeparator();
        sep.setTitle("PromptSupport example:");
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(sep);
        
        double payment = computePayment(xamount, xrate, xnumPeriods);

        //Create the text fields and set them up.
        amountXField = new JXFormattedTextField(amountFormat);
        amountXField.setValue(Double.valueOf(xamount));
        amountXField.setColumns(10);
        amountXField.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
        amountXField.setPrompt("Amount");
        amountXField.addBuddy(new JLabel("€"), Position.RIGHT);
        amountXField.setPromptForeground(Color.BLUE);
        amountXField.setPromptBackground(Color.ORANGE);
        amountXField.setPromptFontStyle(Font.BOLD+Font.ITALIC);
        amountXField.addPropertyChangeListener(pce -> {
        	LOG.info("amountXField PropertyChangeEvent:"+pce +
        			"\n Source:"+pce.getSource());
        	xamount = ((Number)amountXField.getValue()).doubleValue();
        	Double d = Double.valueOf(computePayment(xamount, xrate, xnumPeriods));
            paymentXField.setValue(d);
            paymentXField.setForeground(d<0 ? Color.red : Color.black);
        });

        rateXField = new JXFormattedTextField(percentFormat);
        rateXField.setValue(Double.valueOf(xrate));
        rateXField.setColumns(10);
        rateXField.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
        rateXField.setPrompt("Rate");
        rateXField.addBuddy(new JLabel("%"), Position.RIGHT);
        rateXField.setPromptForeground(Color.BLUE);
        rateXField.setPromptBackground(Color.ORANGE);
        rateXField.setPromptFontStyle(Font.BOLD+Font.ITALIC);
        rateXField.addPropertyChangeListener(pce -> {
        	LOG.info("rateXField PropertyChangeEvent:"+pce + 
        			"\n Source:"+pce.getSource());
            xrate = ((Number)rateXField.getValue()).doubleValue();
        	Double d = Double.valueOf(computePayment(xamount, xrate, xnumPeriods));
            paymentXField.setValue(d);
            paymentXField.setForeground(d<0 ? Color.red : Color.black);
        });

        numPeriodsXField = new JXFormattedTextField();
        numPeriodsXField.setValue(Integer.valueOf(xnumPeriods));
        numPeriodsXField.setColumns(10);
        numPeriodsXField.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
        numPeriodsXField.setPrompt("Years");
        numPeriodsXField.setPromptForeground(Color.BLUE);
        numPeriodsXField.setPromptBackground(Color.ORANGE);
        numPeriodsXField.setPromptFontStyle(Font.BOLD+Font.ITALIC);
        numPeriodsXField.addPropertyChangeListener(pce -> {
        	LOG.info("numPeriodsXField PropertyChangeEvent:"+pce);
            xnumPeriods = ((Number)numPeriodsXField.getValue()).intValue();
        	Double d = Double.valueOf(computePayment(xamount, xrate, xnumPeriods));
            paymentXField.setValue(d);
            paymentXField.setForeground(d<0 ? Color.red : Color.black);
        });

        paymentXField = new JXFormattedTextField(paymentFormat);
        paymentXField.setValue(Double.valueOf(payment));
        paymentXField.setColumns(10);
        paymentXField.setEditable(false); // read only
        paymentXField.setForeground(payment<0 ? Color.red : Color.black);

        // Lay out the labels and text fields in a panel.
        JPanel labelAndFieldPane = new JPanel(new GridLayout(0,2));
        labelAndFieldPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        labelAndFieldPane.add(new JLabel(""));
        labelAndFieldPane.add(amountXField);
        labelAndFieldPane.add(new JLabel(""));
        labelAndFieldPane.add(rateXField);
        labelAndFieldPane.add(new JLabel(""));
        labelAndFieldPane.add(numPeriodsXField);
        labelAndFieldPane.add(new JLabel(""));
        labelAndFieldPane.add(paymentXField);
        labelAndFieldPane.add(new JLabel("JTextField"));
        
        textField = new JTextField(20); // 20 columns
        labelAndFieldPane.add(textField);
        
        right.add(labelAndFieldPane, BorderLayout.CENTER);

        return right;
    }
    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel();

        panel.setBorder(new TitledBorder("Prompt Controls"));
		// jgoodies layout and builder:
        panel.setLayout(new FormLayout(  		
            new ColumnSpec[] { // 2 columns + glue
                FormFactory.GLUE_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,	// 1st column
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.PREF_COLSPEC,		// 2nd column
                FormFactory.GLUE_COLSPEC,
            },
            new RowSpec[] { // 5 rows + glue
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC 
            }
        ));
        
        CellConstraints cc = new CellConstraints();
        
        JLabel label = new JLabel("Focus Behavior:");
        panel.add(label, cc.rc(1, 2));
        
        focusCombo = new JComboBox<FocusBehavior>(new EnumComboBoxModel<FocusBehavior>(FocusBehavior.class));
        focusCombo.setRenderer(new DefaultListRenderer(DisplayValues.TITLE_WORDS_UNDERSCORE));
        panel.add(focusCombo, cc.rc(1, 4));
        
        label = new JLabel("Prompt Text:");
        panel.add(label, cc.rc(3, 2));
        
        promptText = new JTextField();
        promptText.setText("Prompt Text");
        panel.add(promptText, cc.rc(3, 4, "c f"));
        
        JButton foregroundColor = new JButton(new AbstractAction("Select Foreground") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(PromptSupportDemo.this, "Foreground Color", PromptSupport.getForeground(textField));
                
                if (color != null) {
                    PromptSupport.setForeground(color, textField);
                    PromptSupport.setForeground(color, amountXField);
                    PromptSupport.setForeground(color, rateXField);
                    PromptSupport.setForeground(color, numPeriodsXField);
                }
            }
        });
        panel.add(foregroundColor, cc.rc(5, 2));
        
        JButton backgroundColor = new JButton(new AbstractAction("Select Background") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(PromptSupportDemo.this, "Background Color", PromptSupport.getBackground(textField));
                
                if (color != null) {
                    PromptSupport.setBackground(color, textField);
                    PromptSupport.setBackground(color, amountXField);
                    PromptSupport.setBackground(color, rateXField);
                    PromptSupport.setBackground(color, numPeriodsXField);
                }
            }
        });
        panel.add(backgroundColor, cc.rc(5, 4));
        
        label = new JLabel("Painter:");
        panel.add(label, cc.rc(7, 2));
        
        backgroundPainter = new JComboBox<DisplayInfo<Painter<? super Component>>>(new ListComboBoxModel<DisplayInfo<Painter<? super Component>>>(getPainters()));
        backgroundPainter.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        panel.add(backgroundPainter, cc.rc(7, 4));
        
        label = new JLabel("Font Style:");
        panel.add(label, cc.rc(9, 2));
        
        ListComboBoxModel<DisplayInfo<Integer>> model = new ListComboBoxModel<DisplayInfo<Integer>>(getFontStyles());
        fontStyle = new JComboBox<DisplayInfo<Integer>>(model);
        fontStyle.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        panel.add(fontStyle, cc.rc(9, 4));

        bind();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                focusCombo.requestFocusInWindow();
            }
        });

        return panel;
	}

    private void bind() {
        focusCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PromptSupport.setFocusBehavior((FocusBehavior)focusCombo.getSelectedItem(), textField);
                PromptSupport.setFocusBehavior((FocusBehavior)focusCombo.getSelectedItem(), amountXField);
                PromptSupport.setFocusBehavior((FocusBehavior)focusCombo.getSelectedItem(), rateXField);
                PromptSupport.setFocusBehavior((FocusBehavior)focusCombo.getSelectedItem(), numPeriodsXField);
            }
        });
        
        promptText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
        });
        PromptSupport.setPrompt(promptText.getText(), textField);
        
        backgroundPainter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object o = backgroundPainter.getSelectedItem();
                if(o instanceof DisplayInfo) {
                    DisplayInfo<Painter<? super Component>> di = (DisplayInfo<Painter<? super Component>>)o;
                    PromptSupport.setBackgroundPainter(di.getValue(), textField);
                    PromptSupport.setBackgroundPainter(di.getValue(), amountXField);
                    PromptSupport.setBackgroundPainter(di.getValue(), rateXField);
                    PromptSupport.setBackgroundPainter(di.getValue(), numPeriodsXField);
                }
            }
        });
        
        fontStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Object o = fontStyle.getSelectedItem();
            	if(o instanceof DisplayInfo) {
            		DisplayInfo<Integer> di = (DisplayInfo<Integer>)o;
                    PromptSupport.setFontStyle(di.getValue(), textField);
            	}
            }
        });
    }
        
    private List<DisplayInfo<Painter<? super Component>>> getPainters() {
        List<DisplayInfo<Painter<? super Component>>> painters = new ArrayList<DisplayInfo<Painter<? super Component>>>();
        
        painters.add(new DisplayInfo<Painter<? super Component>>("None", null));
        painters.add(new DisplayInfo<Painter<? super Component>>("Checkered", new MattePainter(PaintUtils.getCheckerPaint(new Color(0, 0, 0, 0), new Color(33, 33, 128), 20))));
        painters.add(new DisplayInfo<Painter<? super Component>>("Gradient", new MattePainter(PaintUtils.AERITH, true)));
        painters.add(new DisplayInfo<Painter<? super Component>>("Star Shape", new ShapePainter(ShapeUtils.generatePolygon(5, 10, 5, true), Color.GREEN)));
        
        return painters;
    }
    
    private List<DisplayInfo<Integer>> getFontStyles() {
        List<DisplayInfo<Integer>> fontStyles = new ArrayList<DisplayInfo<Integer>>();
        
        fontStyles.add(new DisplayInfo<Integer>("Normal", Font.PLAIN));
        fontStyles.add(new DisplayInfo<Integer>("Bold", Font.BOLD));
        fontStyles.add(new DisplayInfo<Integer>("Italic", Font.ITALIC));
        fontStyles.add(new DisplayInfo<Integer>("Bold Italic", Font.BOLD | Font.ITALIC));
        
        return fontStyles;
    }

    // ---------------- from FormattedTextFieldDemo
    // contains example from https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
    JXPanel left;

    //Formats to format and parse numbers
    private NumberFormat amountFormat;
    private NumberFormat percentFormat;
    private NumberFormat paymentFormat;

    //Values for the fields with initials
    private double amount = 100000;
    private double rate = 7.5;  //7.5%
    private int numPeriods = 30;

    //Labels to identify the fields
    private JLabel amountLabel;
    private JLabel rateLabel;
    private JLabel numPeriodsLabel;
    private JLabel paymentLabel;

    //Strings for the labels
    private static String amountString = "Loan Amount: ";
    private static String rateString = "APR (%): ";
    private static String numPeriodsString = "Years: ";
    private static String paymentString = "Monthly Payment: ";

    //Fields for data entry
    private JFormattedTextField amountField;
    private JFormattedTextField rateField;
    private JFormattedTextField numPeriodsField;
    private JFormattedTextField paymentField;

    private JComponent createLeftPane() {
    	left = new JXPanel(new VerticalLayout(3));
    	left.setName("left");
    	
        JXTitledSeparator sep = new JXTitledSeparator();
        sep.setTitle("JFormattedTextField example:");
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(sep, BorderLayout.NORTH);

        setUpFormats();
        double payment = computePayment(amount, rate, numPeriods);

        //Create the labels.
        amountLabel = new JLabel(amountString);
        rateLabel = new JLabel(rateString);
        numPeriodsLabel = new JLabel(numPeriodsString);
        paymentLabel = new JLabel(paymentString);

        //Create the text fields and set them up.
        amountField = new JFormattedTextField(amountFormat);
        amountField.setValue(Double.valueOf(amount));
        amountField.setColumns(10);
        amountField.addPropertyChangeListener(pce -> {
        	LOG.info("amountField PropertyChangeEvent:"+pce +
        			"\n Source:"+pce.getSource());
        	amount = ((Number)amountField.getValue()).doubleValue();
            paymentField.setValue(Double.valueOf(computePayment(amount, rate, numPeriods)));
        });

        rateField = new JFormattedTextField(percentFormat);
        rateField.setValue(Double.valueOf(rate));
        rateField.setColumns(10);
        rateField.addPropertyChangeListener(pce -> {
        	LOG.info("rateField PropertyChangeEvent:"+pce + 
        			"\n Source:"+pce.getSource());
            rate = ((Number)rateField.getValue()).doubleValue();
            paymentField.setValue(Double.valueOf(computePayment(amount, rate, numPeriods)));
        });

        numPeriodsField = new JFormattedTextField();
        numPeriodsField.setValue(Integer.valueOf(numPeriods));
        numPeriodsField.setColumns(10);
        numPeriodsField.addPropertyChangeListener(pce -> {
        	LOG.info("numPeriodsField PropertyChangeEvent:"+pce);
            numPeriods = ((Number)numPeriodsField.getValue()).intValue();
            paymentField.setValue(Double.valueOf(computePayment(amount, rate, numPeriods)));
        });

        paymentField = new JFormattedTextField(paymentFormat);
        paymentField.setValue(Double.valueOf(payment));
        paymentField.setColumns(10);
        paymentField.setEditable(false); // read only
        paymentField.setForeground(Color.red);

        //Tell accessibility tools about label/textfield pairs.
        amountLabel.setLabelFor(amountField);
        rateLabel.setLabelFor(rateField);
        numPeriodsLabel.setLabelFor(numPeriodsField);
        paymentLabel.setLabelFor(paymentField);

        // Lay out the labels and text fields in a panel.
        JPanel labelAndFieldPane = new JPanel(new GridLayout(0,2));
        labelAndFieldPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        labelAndFieldPane.add(amountLabel);
        labelAndFieldPane.add(amountField);
        labelAndFieldPane.add(rateLabel);
        labelAndFieldPane.add(rateField);
        labelAndFieldPane.add(numPeriodsLabel);
        labelAndFieldPane.add(numPeriodsField);
        labelAndFieldPane.add(paymentLabel);
        labelAndFieldPane.add(paymentField);
        left.add(labelAndFieldPane, BorderLayout.CENTER);

        return left;
    }

    /**
     * Create and set up number formats. 
     * These objects also parse numbers input by user.
     */
    private void setUpFormats() {
        amountFormat = NumberFormat.getNumberInstance();

        percentFormat = NumberFormat.getNumberInstance();
        percentFormat.setMinimumFractionDigits(3);

        paymentFormat = NumberFormat.getCurrencyInstance();
    }

    /**
     * Compute the monthly payment based on the loan amount, APR, and length of loan.
     */
    double computePayment(double loanAmt, double rate, int numPeriods) {
        double I, partial1, denominator, answer;

        numPeriods *= 12;        //get number of months
        if (rate > 0.01) {
            I = rate / 100.0 / 12.0;         //get monthly rate from annual
            partial1 = Math.pow((1 + I), (0.0 - numPeriods));
            denominator = (1 - partial1) / I;
        } else { //rate ~= 0
            denominator = numPeriods;
        }

        answer = (-1 * loanAmt) / denominator;
        return answer;
    }

}