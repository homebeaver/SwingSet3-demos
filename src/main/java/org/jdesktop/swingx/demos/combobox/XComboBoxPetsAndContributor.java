/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package org.jdesktop.swingx.demos.combobox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.demos.search.Contributor;
import org.jdesktop.swingx.demos.search.Contributors;
import org.jdesktop.swingx.demos.xlist.ListDemoConstants;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;

import swingset.plaf.LaFUtils;

/* XComboBox which uses an uneditable combo box with pets (see XComboBoxPets) and contributors
 * 
 * The challenge is the mix of object items, pets are of type String, persons of type Contributors.
 * Contributors does not have a default String representation.
 * The drop-down list of the combo box is sorted.
 * 
 * uses these additional files:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 */
@SuppressWarnings("serial")
public class XComboBoxPetsAndContributor extends JPanel implements ListDemoConstants {
	
	/**
	 * String representation for pets (String) and persons of type Contributors.
	 * 
	 * We need the String representation in drop-down list to display the value of the items, see MyComboBoxRenderer
	 * and for sorting the items, see Comparator.
	 * 
	 * @param o object item
	 * @return preferred String representation of o
	 */
	private static String preferredStringRepresentation(Object o) {
		if(o==null) return "";
    	return o instanceof Contributor c ? (c.getLastName() + ", " + c.getFirstName() + " (" + c.getMerits() + ")") 
    			: o.toString();		
	}
	
    public static void main(String[] args) {
    	if(args.length>0) LaFUtils.setLAFandTheme(Arrays.asList(args));
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater( () -> {
            createAndShowGUI();
        });
    }

    /**
     * Create the GUI and show it.
     * For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
        //Create and set up the window.
        JFrame frame = new JFrame("Shows an uneditable combo box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new XComboBoxPetsAndContributor();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    JLabel picture; // pet pix
    JXComboBox<Object> cb;

    public XComboBoxPetsAndContributor() {
        super(new BorderLayout());

        // intentionally not sorted, no pix for tyrannosaurus
        String[] petStrings = { "Rabbit", "Bird", "Pig", "Cat"
        		, "Tyrannosaurus Rex", "Dog"
        };

        //Create a sorted combo box, select the item at index 4.
        //Indices start at 0, so 4 specifies the long text.
//        JXComboBox<Object> petList = new JXComboBox<>(petStrings, true);
// autoCreateRowSorter==true creates a RowSorter with SortOrder.ASCENDING ,
// I intentionally start without a RowSorter to do some assertions.
        JXComboBox<Object> petList = new JXComboBox<>(petStrings);
        // JVM disables assertion validation by default: use -enableassertions to enable!
        assert !petList.hasRowSorter(); // NO RowSorter present
        petList.setSortOrder(null); // ==> SortOrder.UNSORTED expected
        assert petList.hasRowSorter() : "JXComboBox petList expected to have a RowSorter";
        assert petList.getSortOrder()==SortOrder.UNSORTED;
        
        petList.setSortOrder(SortOrder.ASCENDING);
        cb = petList;
        RowSorter<? extends ListModel<Object>> rs = petList.getRowSorter();
        System.out.println("ComboBoxDemo petList:"+petList
        	+ "\n RowSorter:"+rs
        	+ "\n RowSorter ViewRowCount/ModelRowCount="+rs.getViewRowCount()+"/"+rs.getModelRowCount()
        	+ "\n RowSorter 0 at view ="+rs.convertRowIndexToView(0) + " expected 4"
        	+ "\n RowSorter 1 at view ="+rs.convertRowIndexToView(1) + " expected 0"
        	+ "\n RowSorter 2 at view ="+rs.convertRowIndexToView(2) + " expected 3"
        	+ "\n RowSorter 3 at view ="+rs.convertRowIndexToView(3) + " expected 1"
        	+ "\n RowSorter 4 at view ="+rs.convertRowIndexToView(4) + " expected 5"
        	+ "\n RowSorter 5 at view ="+rs.convertRowIndexToView(5) + " expected 2"
        	+ "\n petList.PrototypeDisplayValue:"+petList.getPrototypeDisplayValue()
        );
        // JVM disables assertion validation by default: use -enableassertions to enable!
        assert rs.convertRowIndexToView(0)==4;
        assert(rs.convertRowIndexToView(1)==0);
        assert(rs.convertRowIndexToView(2)==3);
        assert(rs.convertRowIndexToView(3)==1);
        assert(rs.convertRowIndexToView(4)==5);
        assert(rs.convertRowIndexToView(5)==2);
        assert(petList.getPrototypeDisplayValue()==null);

        cb.setComparator(new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				String s1 = preferredStringRepresentation(o1);
				String s2 = preferredStringRepresentation(o2);
            	System.out.println("Comparator compare "+s1+" with "+s2);
				return s1.compareToIgnoreCase(s2);
			}        	
        });
        
        ComboBoxModel<Contributor> m = Contributors.getContributorModel();
        System.out.println("Contributor item count = "+m.getSize());
        cb.addItem(m.getElementAt(0)); // add one person aka contributor

        petList.setToolTipText("Choose an animal name from the combo box to view its picture");
        
        // determine selected item (-1 indicates no selection, 0 is the default)
        if(petList.getModel().getSize()>0) petList.setSelectedIndex(4); // ==> pig
        
        petList.addActionListener(ae -> {
            Object o = petList.getSelectedItem();
            if(o instanceof String petName) {
                updateLabel(petName);
            } else {
                System.out.println("SelectedItem is not a String:"+o);
                updateLabel(o.toString());
            }
        });
        
        // setComboBoxIcon: 
		RadianceIcon icon = ChevronIcon.of(SizingConstants.XS, SizingConstants.XS);
		icon.setReflection(true); // for ChevronIcon same effect as icon.setRotation(RadianceIcon.SOUTH)
        petList.setComboBoxIcon(icon, ChevronIcon.of(SizingConstants.XS, SizingConstants.XS));
        
        // setRenderer(ListCellRenderer<? super E> renderer)
        StringValue sv = (Object value) -> {
        	return preferredStringRepresentation(value);
        };
		IconValue iv = (Object value) -> {
			if (value instanceof Contributor c) {
				return flagIcons[(c.getMerits()) % flagIcons.length];
			}
			return IconValue.NULL_ICON;
		};
        petList.setRenderer(new DefaultListRenderer<Object>(sv, iv));

        //Set up the pet picture.
        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);
        int selIndex = petList.getSelectedIndex();
        updateLabel(selIndex>-1 ? petStrings[selIndex] : "not defined");
        picture.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //The preferred size is hard-coded to be the width of the
        //widest image and the height of the tallest image + the border.
        //A real program would compute this.
        picture.setPreferredSize(new Dimension(177, 122+10));

        //Lay out the demo.
        add(petList, BorderLayout.PAGE_START);
        add(picture, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    protected void updateLabel(String name) {
    	String path = "resources/images/"+name+".gif";
        ImageIcon icon = createImageIcon(path);
        picture.setIcon(icon);
        picture.setToolTipText("A drawing of a " + name.toLowerCase());
        if (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path) {
    	URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
