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
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import swingset.plaf.LaFUtils;

/*
 * copied from https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#CustomComboBoxDemo
 * CustomComboBoxDemo.java uses the files like ComboBoxDemo.java:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 */
@SuppressWarnings("serial")
public class CustomComboBoxDemo extends JPanel {
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	if(args.length>0) LaFUtils.setLAF(args[0]);
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
        JFrame frame = new JFrame("Provides a custom renderer for a combo box.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new CustomComboBoxDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    ImageIcon[] images;
    // add Oops without image
    String[] petStrings = {"Bird", "Cat", "Dog", "Rabbit", "Pig", "Oops"};

    /*
     * Despite its use of EmptyBorder, this panel makes a fine content pane 
     * because the empty border just increases the panel's size
     * and is "painted" on top of the panel's normal background.  
     * In other words, the JPanel fills its entire background if it's opaque (which it is by default); 
     * adding a border doesn't change that.
     */
    public CustomComboBoxDemo() {
        super(new BorderLayout());

        //Load the pet images and create an array of indexes.
        images = new ImageIcon[petStrings.length];
        Integer[] intArray = new Integer[petStrings.length];
        for (int i = 0; i < petStrings.length; i++) {
            intArray[i] = Integer.valueOf(i);
            images[i] = createImageIcon("resources/images/" + petStrings[i] + ".gif");
            if (images[i] != null) {
                images[i].setDescription(petStrings[i]);
            }
        }

        // Create the combo box, modeled with Integers! The renderer maps the ints to petStrings and images
        JComboBox<Integer> petList = new JComboBox<>(intArray);
        //Indices start at 0, so 4 specifies the pig.
        petList.setSelectedIndex(4);
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(200, 130));
        petList.setRenderer(renderer);
        petList.setMaximumRowCount(3);

        //Lay out the demo.
        add(petList, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path) {
    	URL imgURL = ComboBoxDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer<Integer> {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding to the selected value 
         * and returns the label, set up to display the text and image.
         */
		@Override
		public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value, 
				int index, boolean isSelected, boolean cellHasFocus) {
			
            // Get the selected index. (The index param isn't always valid, so just use the value.)
            int selectedIndex = value.intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            // Set the icon and text. If icon was null, say so.
            ImageIcon icon = images[selectedIndex];
            String pet = petStrings[selectedIndex];
            setIcon(icon);
            if (icon != null) {
                setText(pet);
                setFont(list.getFont());
            } else {
                setUhOhText(pet + " (no image available)", list.getFont());
            }

            return this;
		}

        // Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }

    }
}
