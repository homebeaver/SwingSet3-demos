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
package org.jdesktop.swingx.demos.svg;
//package components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.jdesktop.swingx.icon.SizingConstants;
import org.pushingpixels.radiance.common.api.icon.RadianceIcon;

/*
 * original CustomComboBoxDemo.java uses the following files:
 *   images/Bird.gif
 *   images/Cat.gif
 *   images/Dog.gif
 *   images/Rabbit.gif
 *   images/Pig.gif
 * this works with Radiance Icons
 */
//see https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
@SuppressWarnings("serial")
public class CustomComboBoxDemo extends JPanel {
	
	private static final Logger LOG = Logger.getLogger(CustomComboBoxDemo.class.getName());
	
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * Create the GUI and show it.  
     * For thread safety, this method should be invoked from the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("CustomComboBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new CustomComboBoxDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	// interface RadianceIcon extends Icon
    RadianceIcon[] icons;
    String[] iconNames = {"activity", "airplay", "alert_circle", "archive", "award"};

    /*
     * Despite its use of EmptyBorder, this panel makes a fine content
     * pane because the empty border just increases the panel's size
     * and is "painted" on top of the panel's normal background.  In
     * other words, the JPanel fills its entire background if it's
     * opaque (which it is by default); adding a border doesn't change
     * that.
     */
    public CustomComboBoxDemo() {
        super(new BorderLayout());

        //Load the icons and create an array of indexes.
        icons = new RadianceIcon[iconNames.length];
        Integer[] intArray = new Integer[iconNames.length];
        for (int i = 0; i < iconNames.length; i++) {
            intArray[i] = Integer.valueOf(i);
            icons[i] = createRadianceIcon(iconNames[i]);
        }

        //Create the combo box.
        JComboBox<Integer> petList = new JComboBox<Integer>(intArray); // intArray == items ??? TODO
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(200, SizingConstants.SMALL_ICON*2));
        petList.setRenderer(renderer);
        petList.setMaximumRowCount(3); // rows the JComboBox displays

        //Lay out the demo.
        add(petList, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    private static String upperCasePrefix(String iconName) {
    	return Character.isLowerCase(iconName.charAt(0)) ? "IconR" : "";
    }
    protected static RadianceIcon createRadianceIcon(String iconName) {
    	String className = CustomComboBoxDemo.class.getPackageName()+"."+upperCasePrefix(iconName)+iconName;
    	Class<?> iconClass = null;
    	LOG.info("load class "+className);
    	try {
    		iconClass = Class.forName(className);  // throws ClassNotFoundException
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    		return null;
    	}

    	RadianceIcon icon = null;
    	try {
			Method method = iconClass.getMethod("of", int.class, int.class);
			Object o = method.invoke(null, SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON);
			icon = (RadianceIcon)o;
		} catch (NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return icon;   	
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer<Integer> {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the icon and text corresponding to the selected value and 
         * returns the label (with icon), set up to display the text and image.
         */
        public Component getListCellRendererComponent(
                                           JList<? extends Integer> list,
                                           Integer value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            RadianceIcon icon = icons[selectedIndex];
            String iconName = iconNames[selectedIndex];
            setIcon(icon); // Label.setIcon
            if (icon != null) {
                setText(iconName);
                setFont(list.getFont());
            } else {
                setUhOhText(iconName + " (no image available)", list.getFont());
            }

            return this;
        }

        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
}
