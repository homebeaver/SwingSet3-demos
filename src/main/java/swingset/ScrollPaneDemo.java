/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Scroll Pane Demo
 *
 * @author Jeff Dinkins
 */
public class ScrollPaneDemo extends DemoModule {

	private static final long serialVersionUID = 9199557527894145835L;
	
	public static final String ICON_PATH = "toolbar/JScrollPane.gif";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        ScrollPaneDemo demo = new ScrollPaneDemo(null);
        demo.mainImpl();
    }

    /**
     * ScrollPaneDemo Constructor
     */
    public ScrollPaneDemo(SwingSet2 swingset) {
        super(swingset, "ScrollPaneDemo", ICON_PATH);

        ImageIcon crayons = createImageIcon("scrollpane/crayons.jpg",  getString("ScrollPaneDemo.crayons"));
        getDemoPanel().add(new ImageScroller(this, crayons), BorderLayout.CENTER);
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
            JLabel colHeader = new JLabel(
                demo.createImageIcon("scrollpane/colheader.jpg", getString("ScrollPaneDemo.colheader")));
            setColumnHeaderView(colHeader);

            // Create and add a row header to the scrollpane
            JLabel rowHeader = new JLabel(
                demo.createImageIcon("scrollpane/rowheader.jpg", getString("ScrollPaneDemo.rowheader")));
            setRowHeaderView(rowHeader);

            // Create and add the upper left corner
            JLabel cornerUL = new JLabel(
                demo.createImageIcon("scrollpane/upperleft.jpg", getString("ScrollPaneDemo.upperleft")));
            setCorner(UPPER_LEFT_CORNER, cornerUL);

            // Create and add the upper right corner
            JLabel cornerUR = new JLabel(
                demo.createImageIcon("scrollpane/upperright.jpg", getString("ScrollPaneDemo.upperright")));
            setCorner(UPPER_RIGHT_CORNER, cornerUR);

            // Create and add the lower left corner
            JLabel cornerLL = new JLabel(
                demo.createImageIcon("scrollpane/lowerleft.jpg", getString("ScrollPaneDemo.lowerleft")));
            setCorner(LOWER_LEFT_CORNER, cornerLL);

            JScrollBar vsb = getVerticalScrollBar();
            JScrollBar hsb = getHorizontalScrollBar();

            vsb.setValue(icon.getIconHeight());
            hsb.setValue(icon.getIconWidth()/10);
        }
    }

}
