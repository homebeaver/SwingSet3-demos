/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.collapsiblepane;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXCollapsiblePane}.
 *
 * @author Karl George Schaefer
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXCollapsiblePane Demo",
//    category = "Containers",
//    description = "Demonstrates JXCollapsiblePane, a container for dynamically hiding contents",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/collapsiblepane/CollapsiblePaneDemo.java",
//        "org/jdesktop/swingx/demos/collapsiblepane/resources/CollapsiblePaneDemo.properties",
//        "org/jdesktop/swingx/demos/collapsiblepane/resources/CollapsiblePaneDemo.html",
//        "org/jdesktop/swingx/demos/collapsiblepane/resources/images/CollaspiblePaneDemo.png"
//    }
//)
public class CollapsiblePaneDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -8035437657756950018L;
	private static final Logger LOG = Logger.getLogger(CollapsiblePaneDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXCollapsiblePane, a container for dynamically hiding contents";

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
				AbstractDemo demo = new CollapsiblePaneDemo(controller);
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
    
    private JXCollapsiblePane collapsiblePane;
    private CardLayout containerStack;
    // Controller:
    private JButton previousButton;
    private JButton collapsingButton;
    private JButton nextButton;
    
    /**
     * CollapsiblePaneDemo Constructor
     * 
     * @param frame controller Frame
     */
    public CollapsiblePaneDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        collapsiblePane = new JXCollapsiblePane();
        collapsiblePane.setName("collapsiblePane");
        add(collapsiblePane, BorderLayout.NORTH);
        
        containerStack = new CardLayout();
        collapsiblePane.setLayout(containerStack);
        collapsiblePane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        collapsiblePane.add(new JTree(), "");
        collapsiblePane.add(new JTable(4, 4), "");
        collapsiblePane.add(new JXFindPanel(), "");
        
        add(new JLabel("Main Content Goes Here", JLabel.CENTER));
    }
    
    @Override
	public JXPanel getControlPane() {
    	JXPanel buttonPanel = new JXPanel();

        previousButton = new JButton();
        previousButton.setName("previousButton");
        previousButton.setText(getBundleString("previousButton.text"));
        previousButton.addActionListener( ae -> {
        	containerStack.previous(collapsiblePane.getContentPane());
        });
        buttonPanel.add(previousButton);
        
        collapsingButton = new JButton();
        collapsingButton.setName("toggleButton");
        collapsingButton.setText(getBundleString("toggleButton.text"));
        buttonPanel.add(collapsingButton);
        
        nextButton = new JButton();
        nextButton.setName("nextButton");
        nextButton.setText(getBundleString("nextButton.text"));
        nextButton.addActionListener( ae -> {
        	containerStack.next(collapsiblePane.getContentPane());
        });
        buttonPanel.add(nextButton);

        bind();
        
        return buttonPanel;
	}


    private void bind() {
        collapsingButton.addActionListener(collapsiblePane.getActionMap().get(JXCollapsiblePane.TOGGLE_ACTION));
        
//        nextButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                containerStack.next(collapsiblePane.getContentPane());
//            }
//        });
//        
//        previousButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                containerStack.previous(collapsiblePane.getContentPane());
//            }
//        });
    }
}
