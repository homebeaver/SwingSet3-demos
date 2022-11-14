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
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.icon.RadianceIcon;

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
        SwingUtilities.invokeLater( () -> {
            createAndShowGUI();
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

    String[] iconNames = {"activity", "airplay", "IconRarrowInCircle", "feather"};

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

        //Create the combo box.
        JComboBox<DisplayInfo<RadianceIcon>> iconCombo = new JComboBox<DisplayInfo<RadianceIcon>>();
        iconCombo.setModel(createCBM());
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(200, RadianceIcon.SMALL_ICON*3));
        iconCombo.setRenderer(renderer);
        iconCombo.setMaximumRowCount(3); // rows the JComboBox displays

        //Lay out the demo.
        add(iconCombo, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    private ComboBoxModel<DisplayInfo<RadianceIcon>> createCBM() {
        MutableComboBoxModel<DisplayInfo<RadianceIcon>> model = new DefaultComboBoxModel<DisplayInfo<RadianceIcon>>();
        for (int i = 0; i < iconNames.length; i++) {
            model.addElement(new DisplayInfo<RadianceIcon>(iconNames[i], createRadianceIcon(iconNames[i])));
        }
        return model;
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
			Object o = method.invoke(null, RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
			icon = (RadianceIcon)o;
		} catch (NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return icon;   	
    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer<DisplayInfo<RadianceIcon>> {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * Return a label that has been configured to display the specified combo item.
         */
		public Component getListCellRendererComponent(JList<? extends DisplayInfo<RadianceIcon>> list,
				DisplayInfo<RadianceIcon> comboItem, 
				int index, 
				boolean isSelected, 
				boolean cellHasFocus) {
			
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and its description text. If icon was null, say so.
			RadianceIcon icon = comboItem.getValue(); // icons[selectedIndex];
			String iconName = comboItem.getDescription(); // iconNames[selectedIndex];
			setIcon(icon); // Label.setIcon
			/*
			   wenn ausreichend Platz ist (renderer.setPreferredSize), dann
			   kann man den text unterhalb des icons positionieren:
			 */
			this.setVerticalTextPosition(BOTTOM); // default is CENTER. 
			this.setHorizontalTextPosition(CENTER); // LEADING, ... TRAILING is default
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
