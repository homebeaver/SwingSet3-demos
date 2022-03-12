/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.multisplitpane;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.MultiSplitLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXMultiSplitPane}.
 *
 * @author Karl George Schaefer, Luan O'Carroll
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXMultiSplitPane Demo",
//    category = "Containers",
//    description = "Demonstrates JXMultiSplitPane, a container that allows arbitrary resizing children.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/multisplitpane/MultiSplitPaneDemo.java",
//        "org/jdesktop/swingx/demos/multisplitpane/resources/MultiSplitPaneDemo.properties",
//        "org/jdesktop/swingx/demos/multisplitpane/resources/MultiSplitPaneDemo.html",
//        "org/jdesktop/swingx/demos/multisplitpane/resources/images/MultiSplitPaneDemo.png"
//    }
//)
public class MultiSplitPaneDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -5205296232962160682L;
	private static final Logger LOG = Logger.getLogger(MultiSplitPaneDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXMultiSplitPane, a container that allows arbitrary resizing children.";

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
				AbstractDemo demo = new MultiSplitPaneDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }
    
    /**
     * MultiSplitPaneDemo Constructor
     */
    public MultiSplitPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);

    	LOG.config("createMultiSplitPaneDemo:");
    	createMultiSplitPaneDemo();
    }

    @Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}


    private void createMultiSplitPaneDemo() {

      JXMultiSplitPane msp = new JXMultiSplitPane();

      String layoutDef 
      = "(COLUMN " 
      +		"(ROW weight=0.8 " 
      + 		"(COLUMN weight=0.25 "
      + 			"(LEAF name=left.top weight=0.5) (LEAF name=left.middle weight=0.5) "
      + 		") "
      + 		"(LEAF name=editor weight=0.75) "
      +		") " 
      +		"(LEAF name=bottom weight=0.2) " 
      +	")" ;
      MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel( layoutDef );
      msp.getMultiSplitLayout().setModel( modelRoot );

      msp.add( new JButton( "Left Top" ), "left.top" );
      msp.add( new JButton( "Left Middle" ), "left.middle" );
      msp.add( new JButton( "Editor" ), "editor" );
      msp.add( new JButton( "Bottom" ), "bottom" );

      // ADDING A BORDER TO THE MULTISPLITPANE CAUSES ALL SORTS OF ISSUES
      msp.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

      add( msp, BorderLayout.CENTER );
    }
    
}
