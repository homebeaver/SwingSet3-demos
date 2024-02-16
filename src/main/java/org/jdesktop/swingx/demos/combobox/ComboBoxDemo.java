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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.ComboBoxContext;

import swingset.plaf.LaFUtils;

/* ComboBoxDemo which uses an uneditable combo box with pets TODO rename to XComboBoxPets
 * copied from https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ComboBoxDemo
 * 
 * ComboBoxDemo.java uses these additional files:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 */
@SuppressWarnings("serial")
public class ComboBoxDemo extends JPanel {
	
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
        JComponent newContentPane = new ComboBoxDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    JLabel picture;
    JXComboBox<String> cb;

    public ComboBoxDemo() {
        super(new BorderLayout());

        String[] emptyStrings = { };
        String[] petStrings = { "Rabbit", "Bird", "Pig", "Cat"
        		, "Text: Create the combo box, select the item at index 4."
//        		, "Dog", "Rabbit", "Pig" };
        };

        //Create a sorted combo box, select the item at index 4.
        //Indices start at 0, so 4 specifies the long text.
        JXComboBox<String> petList = new JXComboBox<>(petStrings, true);
        cb = petList;
        RowSorter<? extends ListModel<String>> rs = petList.getRowSorter();
        System.out.println("ComboBoxDemo petList:"+petList
        	+ "\n RowSorter:"+rs
        	+ "\n RowSorter ViewRowCount/ModelRowCount="+rs.getViewRowCount()+"/"+rs.getModelRowCount()
        	+ "\n RowSorter 0 at view ="+rs.convertRowIndexToView(0) + " expected 3"
        	+ "\n RowSorter 1 at view ="+rs.convertRowIndexToView(1) + " expected 0"
        	+ "\n RowSorter 2 at view ="+rs.convertRowIndexToView(2) + " expected 2"
        	+ "\n RowSorter 3 at view ="+rs.convertRowIndexToView(3) + " expected 1"
        	+ "\n RowSorter 4 at view ="+rs.convertRowIndexToView(4) + " expected 4"
        	+ "\n petList.PrototypeDisplayValue:"+petList.getPrototypeDisplayValue()
        );
        // JVM disables assertion validation by default: use -enableassertions to enable!
        assert(rs.convertRowIndexToView(0)==3);
        assert(rs.convertRowIndexToView(1)==0);
        assert(rs.convertRowIndexToView(2)==2);
        assert(rs.convertRowIndexToView(3)==1);
        assert(rs.convertRowIndexToView(4)==4);
        
        // is PrototypeDisplayValue kürzer als longest DisplayValue aka petStrings, so wird longest abgeschnitten!!!
//        petList.setPrototypeDisplayValue("a PrototypeDisplayValue");
        petList.setToolTipText("Choose an animal name from the combo box to view its picture");
        // nicht notwendig, da autoCreateRowSorter==true im ctor
        //petList.setRowSorter(new ListSortController<ListModel<String>>(petList.getModel()), SortOrder.ASCENDING);
        
        // determine selected item (-1 indicates no selection, 0 is the default)
        if(petList.getModel().getSize()>0) petList.setSelectedIndex(1);
        
        petList.addActionListener(ae -> {
            String petName = (String)petList.getSelectedItem();
            updateLabel(petName);
        });
        
//        petList.setEnabled(false);
        // setComboBoxIcon: 
		RadianceIcon icon = ChevronIcon.of(SizingConstants.XS, SizingConstants.XS);
		icon.setReflection(true); // for ChevronIcon same effect as icon.setRotation(RadianceIcon.SOUTH)
        petList.setComboBoxIcon(icon, ChevronIcon.of(SizingConstants.XS, SizingConstants.XS));
//        petList.setComboBoxIcon(FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
//        JXIcon personIcon = FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON);      
//        petList.setComboBoxIcon(personIcon);
        
//        petList.setEditable(true);
//        petList.setRenderer(new MyComboBoxRenderer());

        //Set up the picture.
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

    /**
     * A subclass of BasicComboBoxRenderer to override the renderer property
     * - border of the Label : EmptyNoFocusBorder,
     *          of the List : EtchedBorder
public class BasicComboBoxRenderer extends JLabel
implements ListCellRenderer<Object>, Serializable {


public class DefaultComboBoxRenderer<E> extends AbstractRenderer implements ListCellRenderer<E> {
ComponentProvider<JLabel> componentProvider = new LabelProvider();
DefaultComboBoxRenderer renderer = new DefaultComboBoxRenderer(ComponentProvider<?> componentProvider);
     */
//    class MyComboBoxRenderer extends BasicComboBoxRenderer {
    class MyComboBoxRenderer extends JLabel implements ListCellRenderer<Object>, Serializable {

        static Border etchedNoFocusBorder = BorderFactory.createEtchedBorder();

        ComboBoxContext cc;
        public MyComboBoxRenderer() {
            super();
//            CellContext cc = new ComboBoxContext();
            cc = new ComboBoxContext();
        }

        Border getEmptyNoFocusBorder() {
        	return new EmptyBorder(1, 1, 1, 1);
        }

        /*
called with index -1 in 
- BasicXComboBoxUI.getDisplaySize n times (...false, false) to calculate the dimension
            	renderer.getListCellRendererComponent(listBox, prototypeValue, -1, false, false)
                    Component c = renderer.getListCellRendererComponent(listBox, value, -1, false, false);

- BasicXComboBoxUI.getDefaultSize 1x (...false, false) to calculate the dimension
        Component comp = getDefaultListCellRenderer().getListCellRendererComponent(listBox, " ", -1, false, false);

- getBaseline ??? with (...false, false)
                Component component = renderer.getListCellRendererComponent(listBox, value, -1, false, false);

- getBaselineResizeBehavior
                Component component = renderer.getListCellRendererComponent(listBox, value, -1, false, false);

- BasicXComboBoxUI.paintCurrentValue (...isSelected, cellHasFocus)
            c = renderer.getListCellRendererComponent( listBox,
                                                       comboBox.getSelectedItem(),
                                                     -1, ...

im renderer: wird bei index == -1 
         */
        @Override
		public Component getListCellRendererComponent(JList<?> list, Object value, 
				int index, boolean isSelected, boolean cellHasFocus) {

//            public void installContext(JComboBox<?> component, Object value, int row, int column,
//                    boolean selected, boolean focused, boolean expanded, boolean leaf) {
            cc.installContext(ComboBoxDemo.this.cb, value, index, -1, isSelected, cellHasFocus, false, false);
           

    		System.out.println("ComboBoxDemo getListCellRendererComponent: list:"+list
    			+"\n value:"+value+"/"+(value==null?"null":value.getClass())
    			+"\n index="+index+" , isSelected="+isSelected+" , cellHasFocus="+cellHasFocus
    			+"\n ListCellRendererComponent hashCode=@"+Integer.toHexString(this.hashCode())
    				);
    		
			setBorder(index == -1 ? getEmptyNoFocusBorder() : etchedNoFocusBorder);

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setFont(list.getFont());

			if (value instanceof Icon) {
				setIcon((Icon) value);
			} else {
				setText((value == null) ? "" : value.toString());
			}
			
			return this;
		}

    }

}
