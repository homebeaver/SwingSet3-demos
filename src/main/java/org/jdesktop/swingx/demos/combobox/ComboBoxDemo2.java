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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;

import swingset.plaf.LaFUtils;

/*
 * copied from https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ComboBoxDemo2
 * ComboBoxDemo2.java requires no other files.
 */
@SuppressWarnings("serial")
public class ComboBoxDemo2 extends JPanel {
	
    private static final Logger LOG = Logger.getLogger(ComboBoxDemo2.class.getName());

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
        JFrame frame = new JFrame("an editable combo box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ComboBoxDemo2();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    static JFrame frame;
    JLabel result;
    String currentPattern;

    public ComboBoxDemo2() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String[] patternExamples = {
                 "dd MMMMM yyyy",
                 "dd.MM.yy",
                 "MM/dd/yy",
                 "yyyy.MM.dd G 'at' hh:mm:ss z",
                 "EEE, MMM d, ''yy",
                 "h:mm a",
                 "H:mm:ss:SSS",
                 "K:mm a,z",
                 "yyyy.MMMMM.dd GGG hh:mm aaa"
                 };

        currentPattern = patternExamples[0];

        //Set up the UI for selecting a pattern.
        JLabel patternLabel1 = new JLabel("Enter the pattern string or");
        JLabel patternLabel2 = new JLabel("select one from the list:");

        JXComboBox<String> patternList = new JXComboBox<>(patternExamples);
        patternList.setEditable(true);
        patternList.addActionListener(ae -> {
        	LOG.fine("Action Event:"+ae);
            String newSelection = (String)patternList.getSelectedItem();
            currentPattern = newSelection;
        	LOG.info("Action : pattern chenged to \""+newSelection+"\"");
            reformat();
        });
        // ab v1.7.8 ist setRenderer für JXComboBox<String> nicht notwendig, 
        // denn für Strings wird der Renderer in JXComboBox#init gesetzt
//        Jan. 12, 2025 3:12:58 PM org.jdesktop.swingx.JXComboBox init
//        INFORMATION: set DefaultListRenderer for class java.lang.String StringValue
        StringValue sv = (Object value) -> {
        	return preferredStringRepresentation(value);
        };
        patternList.setRenderer(new DefaultListRenderer<Object>(sv));

        //Create the UI for displaying result.
        JLabel resultLabel = new JLabel("Current Date/Time",
                                        JLabel.LEADING); //== LEFT
        result = new JLabel(" ");
        result.setForeground(Color.black);
        result.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.black),
             BorderFactory.createEmptyBorder(5,5,5,5)
        ));

        //Lay out everything.
        JPanel patternPanel = new JPanel();
        patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.PAGE_AXIS));
        patternPanel.add(patternLabel1);
        patternPanel.add(patternLabel2);
        patternList.setAlignmentX(Component.LEFT_ALIGNMENT);
        patternPanel.add(patternList);

        JPanel resultPanel = new JPanel(new GridLayout(0, 1));
        resultPanel.add(resultLabel);
        resultPanel.add(result);

        patternPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(patternPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(resultPanel);

        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        reformat();
    }

    /** Formats and displays today's date. */
    public void reformat() {
        Date today = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(currentPattern); // throws IllegalArgumentException
            String dateString = formatter.format(today);
            result.setForeground(Color.black);
            result.setText(dateString);
        } catch (IllegalArgumentException iae) {
            result.setForeground(Color.red);
            result.setText("Error: " + iae.getMessage());
        }
    }

	private static String preferredStringRepresentation(Object o) {
		if(o==null) return "";
    	return o.toString();		
	}

}
