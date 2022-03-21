/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xlabel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLabel.TextAlignment;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXLabel}.
 *
 * @author Karl George Schaefer
 * @author rah003 (original JXLabelDemo)
 * @author Richard Bair (original JXLabelDemo)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXLabel Demo",
//    category = "Controls",
//    description = "Demonstrates JXLabel, a Painter-enabled multiline label component.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/xlabel/XLabelDemo.java",
//        "org/jdesktop/swingx/demos/xlabel/resources/XLabelDemo.properties",
//        "org/jdesktop/swingx/demos/xlabel/resources/XLabelDemo.html",
//        "org/jdesktop/swingx/demos/xlabel/resources/images/XLabelDemo.png",
//        "org/jdesktop/swingx/demos/xlabel/resources/images/exit.png"
//    }
//)
//@SuppressWarnings("serial")
public class XLabelDemo extends AbstractDemo {

	private static final long serialVersionUID = -490251065325879942L;
	private static final Logger LOG = Logger.getLogger(XLabelDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXLabel, a Painter-enabled multiline label component.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new XLabelDemo(controller);
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

    private JXLabel label;
    
    /**
     * MonthViewDemo Constructor
     */
    public XLabelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        label = new JXLabel();
        label.setName("contents");
        label.setText(getBundleString("contents.text"));
        /*
         * BUG: contents.text=<html> ... ==> die controller funcs 
         *  - label.setLineWrap
         *  - label.setTextAlignment
         * tuen nicht! TODO
         *  - label.setTextRotation aber schon
         */
        label.setIcon(getResourceAsIcon(getClass(), "resources/images/exit.png"));
        add(label);
    }

    private JCheckBox lineWrap;
    private JComboBox<TextAlignment> alignments;
    private JButton rotate;

    @Override
	public JXPanel getControlPane() {
    	JXPanel controller = new JXPanel();

        lineWrap = new JCheckBox();
        lineWrap.setName("lineWrap");
        lineWrap.setText(getBundleString("lineWrap.text", lineWrap));
        lineWrap.setSelected(Boolean.valueOf(getBundleString("lineWrap.selected")));
        label.setLineWrap(lineWrap.isSelected());
        lineWrap.addActionListener( ae -> {
            label.setLineWrap(lineWrap.isSelected());
            label.repaint();
        });
        controller.add(lineWrap);
        
        alignments = new JComboBox<TextAlignment>(new EnumComboBoxModel<TextAlignment>(TextAlignment.class));
        
        alignments.setRenderer(new DefaultListRenderer(new StringValue() {
            @Override
            public String getString(Object value) {
                String s = StringValues.TO_STRING.getString(value);
                
                if (s.length() > 1) {
                    String lc = s.toLowerCase();
                    s = s.charAt(0) + lc.substring(1);
                }
                
                return s;
            }
        }));
        alignments.addActionListener( ae -> {
            LOG.info("ActionListener:"+ae);
            Object o = alignments.getSelectedItem();
            label.setTextAlignment((TextAlignment)o);
        });
        controller.add(alignments);

        rotate = new JButton();
        rotate.setName("rotate");
        rotate.setText(getBundleString("rotate.text", rotate));
        rotate.addActionListener( ae -> {
            label.setTextRotation((label.getTextRotation() + Math.PI / 16) % (2 * Math.PI));
        });
        controller.add(rotate);
        
        return controller;
    }

}