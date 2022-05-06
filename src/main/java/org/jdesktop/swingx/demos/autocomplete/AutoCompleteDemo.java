/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.autocomplete;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import swingset.AbstractDemo;

/**
 * A demo for the {@code AutoCompleteDecorator}.
 * 
 * @author Karl George Schaefer
 * @author Thomas Bierhance (original AutoCompleteDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "AutoComplete Demo",
//    category = "Functionality", ===> Decorator
//    description = "Demonstrates AutoComplete, a decorator that automatically completes selections",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/autocomplete/AutoCompleteDemo.java",
//        "org/jdesktop/swingx/demos/autocomplete/Airport.java",
//        "org/jdesktop/swingx/demos/autocomplete/Airports.java",
//        "org/jdesktop/swingx/demos/autocomplete/AirportConverter.java",
//        "org/jdesktop/swingx/demos/autocomplete/resources/AutoCompleteDemo.properties",
//        "org/jdesktop/swingx/demos/autocomplete/resources/AutoCompleteDemo.html"
//    }
//)
public class AutoCompleteDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -5135820727419304840L;
	private static final Logger LOG = Logger.getLogger(AutoCompleteDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates AutoComplete, a decorator that automatically completes selections";

	/**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new AutoCompleteDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    private List<String> names;
    
    private JComboBox<Airport> airportComboBox;
    private JList<String> list;
    private JComboBox<String> nonStrictComboBox;
    private JTextField nonStrictTextField;
    private JComboBox<String> strictComboBox;
    private JTextField strictTextField;
    private JTextField textFieldForList;
    
    /**
     * Constructor
     */
    public AutoCompleteDemo(Frame frame) {
    	super(new GridBagLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	//LOG.info("getBundleString for key names...");
    	String namesString = getBundleString("names", "empty");
    	LOG.info("getBundleString for key names="+namesString);
    	names = Collections.unmodifiableList(Arrays.asList(namesString.split(",")));
    	createDemo();
/* in createDemo() bind with props 
strictComboBoxLabel.text = JComboBox (strict)
nonStrictComboBoxLabel.text = JComboBox (non-strict)
strictTextFieldLabel.text = JTextField (strict)
nonStrictTextFieldLabel.text = JTextField (non-strict)
textFieldForListLabel.text = JTextField w/ JList (strict)
listLabel.text = JList
airportLabel.text = JComboBox w/ multiple strings
 */
        strictComboBox.setModel(new ListComboBoxModel<String>(names));
        nonStrictComboBox.setModel(new ListComboBoxModel<String>(names));
        airportComboBox.setModel(new ListComboBoxModel<Airport>(Airports.ALL_AIRPORTS));
        airportComboBox.addActionListener( ae -> {
        	LOG.info("selected Airport:" + airportComboBox.getSelectedItem());
        });

        //use the combo box model because it's SwingX
        list.setModel(new ListComboBoxModel<String>(names));
   	
        decorate();
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

    private void createDemo() {
        GridBagConstraints gridBagConstraints;

        strictComboBox = new JComboBox<String>();
        nonStrictComboBox = new JComboBox<String>();
        strictTextField = new JTextField();
        nonStrictTextField = new JTextField();
        textFieldForList = new JTextField();
        list = new JList<String>();
        airportComboBox = new JComboBox<Airport>();

        JLabel strictComboBoxLabel = new JLabel();
        strictComboBoxLabel.setName("strictComboBoxLabel");
        // bind with prop:
        strictComboBoxLabel.setText(getBundleString("strictComboBoxLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(strictComboBoxLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(strictComboBox, gridBagConstraints);

        JLabel nonStrictComboBoxLabel = new JLabel();
        nonStrictComboBoxLabel.setName("nonStrictComboBoxLabel");
        // bind with prop:
        nonStrictComboBoxLabel.setText(getBundleString("nonStrictComboBoxLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(nonStrictComboBoxLabel, gridBagConstraints);

//        nonStrictComboBox.setEditable(true);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(nonStrictComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        add(new JSeparator(), gridBagConstraints);

        JLabel strictTextFieldLabel = new JLabel();
        strictTextFieldLabel.setName("strictTextFieldLabel");
        // bind with prop:
        strictTextFieldLabel.setText(getBundleString("strictTextFieldLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(strictTextFieldLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(strictTextField, gridBagConstraints);

        JLabel nonStrictTextFieldLabel = new JLabel();
        nonStrictTextFieldLabel.setName("nonStrictTextFieldLabel");
        // bind with prop:
        nonStrictTextFieldLabel.setText(getBundleString("nonStrictTextFieldLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(nonStrictTextFieldLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(nonStrictTextField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        add(new JSeparator(), gridBagConstraints);

        JLabel textFieldForListLabel = new JLabel();
        textFieldForListLabel.setName("textFieldForListLabel");
        // bind with prop:
        textFieldForListLabel.setText(getBundleString("textFieldForListLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(textFieldForListLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(textFieldForList, gridBagConstraints);

        JLabel listLabel = new JLabel();
        listLabel.setName("listLabel");
        // bind with prop:
        listLabel.setText(getBundleString("listLabel.text"));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(listLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(new JScrollPane(list), gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        add(new JSeparator(), gridBagConstraints);

        JLabel airportLabel = new JLabel();
        airportLabel.setName("airportLabel");
        // bind with prop:
        airportLabel.setText(getBundleString("airportLabel.text"));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(airportLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(airportComboBox, gridBagConstraints);
    }
    
    private void decorate() {
    	LOG.info("strictComboBox "+ (strictComboBox.isEditable() ? "is editable" : "is NOT editable"));
    	// strictComboBox is NOT editable ==> only items from the combo box can be selected :
        AutoCompleteDecorator.decorate(strictComboBox);    
    	LOG.warning("AutoCompleteDecorator.decorate sets strictComboBox.isEditable flag to "+strictComboBox.isEditable());
        
    	// nonStrictComboBox is editable : any items from can be added :
        nonStrictComboBox.setEditable(true);
        AutoCompleteDecorator.decorate(nonStrictComboBox);
        
        AutoCompleteDecorator.decorate(strictTextField, names, true);
        AutoCompleteDecorator.decorate(nonStrictTextField, names, false);
        
        AutoCompleteDecorator.decorate(list, textFieldForList);
        
    	LOG.info("airportComboBox "+ (airportComboBox.isEditable() ? "is editable" : "is NOT editable"));
    	// strictComboBox is NOT editable ==> only items from the combo box can be selected :
    	// ObjectToStringConverter used to create an AutoCompleteDocument
        AutoCompleteDecorator.decorate(airportComboBox, new AirportConverter());
    }
}