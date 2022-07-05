/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.datepicker;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.ComponentOrientationConverter;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.demos.monthviewext.MonthViewExtDemo;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXDatePicker}.
 *
 * @author Karl George Schaefer
 * @author Richard Bair (original JXDatePickerDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXDatePicker Demo",
//    category = "Controls",
//    description = "Demonstrates JXDatePicker, a control which allows the user to select a date",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/datepicker/DatePickerDemo.java",
//        "org/jdesktop/swingx/demos/datepicker/resources/DatePickerDemo.properties",
//        "org/jdesktop/swingx/binding/ComponentOrientationConverter.java"
//    }
//)
//@SuppressWarnings("serial")
public class DatePickerDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 6613908856054220091L;
	private static final Logger LOG = Logger.getLogger(MonthViewExtDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXDatePicker, a control which allows the user to select a date";

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
				AbstractDemo demo = new DatePickerDemo(controller);
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

    private JXDatePicker datePicker;
    private JFormattedTextField dateEchoField;

    // Controller:
    private JCheckBox interactivity;
    private JCheckBox editability;
    private JCheckBox orientation;
  
    /**
     * DatePickerDemo Constructor
     * 
     * @param frame controller Frame
     */
    public DatePickerDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	initComponents();
    	configureComponents();
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel painterControl = new JXPanel();
        FormLayout formLayout = new FormLayout(
            "5dlu, r:d:n, l:4dlu:n, f:d:g", // 2 columns
            "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n"
        );
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("propertySeparator");
        areaSeparator.setTitle(getBundleString("propertySeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int currentRow = 3;

        interactivity = new JCheckBox();
        interactivity.setName("interactivity");
        interactivity.setText(getBundleString("interactivity.text"));
        interactivity.setSelected(true);
        builder.add(interactivity, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;

        editability = new JCheckBox();
        editability.setName("editability");
        editability.setText(getBundleString("editability.text"));
        editability.setSelected(true);
        builder.add(editability, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        orientation = new JCheckBox();
        orientation.setName("orientation");
        orientation.setText(getBundleString("orientation.text"));
        orientation.setSelected(!datePicker.getComponentOrientation().isLeftToRight());
        builder.add(orientation, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;

        bind();
        
        return painterControl;
	}

    private void initComponents() {
        JPanel monthViewContainer = new JXPanel();
		// jgoodies layout and builder:
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:g, l:4dlu:n, f:d:g", // 2 columns
                "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n" // 4 rows
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, monthViewContainer);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("listSeparator");
        areaSeparator.setTitle(getBundleString("listSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));

        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        //creates a new picker and sets the current date to today
        datePicker = new JXDatePicker(new Date());
        datePicker.setName("datePicker");
        JLabel datePickerLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                datePicker, cc.xywh(widgetColumn, currentRow, 1, 1));
        datePickerLabel.setName("datePickerLabel");
        datePickerLabel.setText(getBundleString("datePickerLabel.text"));
        LabelHandler.bindLabelFor(datePickerLabel, datePicker);
        currentRow += 2;
        
        dateEchoField = new JFormattedTextField();
        dateEchoField.setName("dateEchoField");
        JLabel dateEchoLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                dateEchoField, cc.xywh(widgetColumn, currentRow, 1, 1));
        dateEchoLabel.setName("dateEchoLabel");
        dateEchoLabel.setText(getBundleString("dateEchoLabel.text"));
        LabelHandler.bindLabelFor(dateEchoLabel, dateEchoField);
        currentRow += 2;
        
        add(monthViewContainer, BorderLayout.CENTER);
    }

    private void configureComponents() {
        dateEchoField.setEditable(false);
        AbstractFormatter formatter = new DateFormatter(DateFormat.getDateTimeInstance());
        AbstractFormatterFactory tf = new DefaultFormatterFactory(formatter);
        dateEchoField.setFormatterFactory(tf);
    }
    
    @SuppressWarnings("unchecked")
    private void bind() {
        
        BindingGroup group = new BindingGroup();
        group.addBinding(Bindings.createAutoBinding(READ, 
                datePicker, BeanProperty.create("date"),
                dateEchoField, BeanProperty.create("value")
        ));

        group.addBinding(Bindings.createAutoBinding(READ,
            interactivity, BeanProperty.create("selected"),
            datePicker, BeanProperty.create("enabled")
        ));
        
        group.addBinding(Bindings.createAutoBinding(READ,
            editability, BeanProperty.create("selected"),
            datePicker, BeanProperty.create("editable")
        ));
        
        Binding b = Bindings.createAutoBinding(READ,
            orientation, BeanProperty.create("selected"),
            datePicker, BeanProperty.create("componentOrientation"));
        b.setConverter(new ComponentOrientationConverter());
        group.addBinding(b);
        group.bind();
    }

}
