/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

/**
 * Scroll Pane Demo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ScrollPaneDemo extends AbstractDemo {
	
	public static final String ICON_PATH = "toolbar/JScrollPane.gif";

	private static final long serialVersionUID = 9199557527894145835L;
	private static final String DESCRIPTION = "JScrollPane Demo";
    private static final String IMG_PATH = "scrollpane/"; // prefix dir

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
				AbstractDemo demo = new ScrollPaneDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    /**
     * ScrollPaneDemo Constructor
     */
    public ScrollPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));
    	
    	ImageIcon crayons = StaticUtilities.createImageIcon(IMG_PATH+"crayons.jpg");
    	super.add(new ImageScroller(this, crayons), BorderLayout.CENTER);
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

    /**
     * ScrollPane class that demonstrates how to set the various column and row headers
     * and corners.
     */
    class ImageScroller extends JScrollPane {
        public ImageScroller(ScrollPaneDemo demo, Icon icon) {
            super();

            // Panel to hold the icon image
            JPanel p = new JPanel(new BorderLayout());
            p.add(new JLabel(icon), BorderLayout.CENTER);
            getViewport().add(p);

            // Create and add a column header to the scrollpane
            JLabel colHeader = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"colheader.jpg"));
            setColumnHeaderView(colHeader);

            // Create and add a row header to the scrollpane
            JLabel rowHeader = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"rowheader.jpg"));
            setRowHeaderView(rowHeader);

            // Create and add the upper left corner
            JLabel cornerUL = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"upperleft.jpg"));
            setCorner(UPPER_LEFT_CORNER, cornerUL);

            // Create and add the upper right corner
            JLabel cornerUR = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"upperright.jpg"));
            setCorner(UPPER_RIGHT_CORNER, cornerUR);

            // Create and add the lower left corner
            JLabel cornerLL = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"lowerleft.jpg"));
            setCorner(LOWER_LEFT_CORNER, cornerLL);

            JScrollBar vsb = getVerticalScrollBar();
            JScrollBar hsb = getHorizontalScrollBar();

            vsb.setValue(icon.getIconHeight());
            hsb.setValue(icon.getIconWidth()/10);
        }
    }

}