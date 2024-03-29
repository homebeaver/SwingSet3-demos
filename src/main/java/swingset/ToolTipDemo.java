/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

/**
 * ToolTip Demo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ToolTipDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/ToolTip.gif";

	private static final long serialVersionUID = 8981063652293220475L;
	private static final String DESCRIPTION = "Bessie The Cow ToolTips";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			// no controller
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			AbstractDemo demo = new ToolTipDemo(frame);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	});
    }

    /**
     * ToolTipDemo Constructor
     * @param frame controller Frame
     */
    public ToolTipDemo(Frame frame) {
    	super();
    	frame.setTitle(getBundleString("name", DESCRIPTION));
//    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	super.setBackground(Color.white);

        // Create a Cow to put in the center of the panel.
        Cow cow = new Cow(getResourceAsIcon(getClass(), "images/tooltip/cow.gif"));
        cow.getAccessibleContext().setAccessibleName(getBundleString("accessible_cow"));

        // Set the tooltip text. Note, for fun, we also set more tooltip text
        // descriptions for the cow down below in the Cow.contains() method.
        cow.setToolTipText(getBundleString("cow"));

        // Add the cow midway down the panel
//        super.add(Box.createRigidArea(new Dimension(1, 150)));
        super.add(cow);
    }

    @Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}

    class Cow extends JXLabel {
        Polygon cowgon = new Polygon();

        public Cow(Icon icon) {
            super(icon);
            setAlignmentX(CENTER_ALIGNMENT);

            // Set polygon points that define the outline of the cow.
            cowgon.addPoint(3,20);    cowgon.addPoint(44,4);
            cowgon.addPoint(79,15);   cowgon.addPoint(130,11);
            cowgon.addPoint(252,5);   cowgon.addPoint(181,17);
            cowgon.addPoint(301,45);  cowgon.addPoint(292,214);
            cowgon.addPoint(269,209); cowgon.addPoint(266,142);
            cowgon.addPoint(250,161); cowgon.addPoint(235,218);
            cowgon.addPoint(203,206); cowgon.addPoint(215,137);
            cowgon.addPoint(195,142); cowgon.addPoint(143,132);
            cowgon.addPoint(133,189); cowgon.addPoint(160,200);
            cowgon.addPoint(97,196);  cowgon.addPoint(107,182);
            cowgon.addPoint(118,185); cowgon.addPoint(110,144);
            cowgon.addPoint(59,77);   cowgon.addPoint(30,82);
            cowgon.addPoint(30,35);   cowgon.addPoint(15,36);
        }

        boolean moo = false;
        boolean milk = false;
        boolean tail = false;

        // Use the contains method to set the tooltip text depending
        // on where the mouse is over the cow.
        public boolean contains(int x, int y) {
            if(!cowgon.contains(new Point(x, y))) {
                return false;
            }

            if((x > 30) && (x < 60) && (y > 60) && (y < 85)) {
                if(!moo) {
                    setToolTipText("<html><center><font color=blue size=+2>" +
                                   getBundleString("moo") + "</font></center></html>");
                    moo = true;
                    milk = false;
                    tail = false;
                }
            } else if((x > 150) && (x < 260) && (y > 90) && (y < 145)) {
                if(!milk) {
                    setToolTipText("<html><center><font face=AvantGarde size=+1 color=white>" +
                                   getBundleString("got_milk") + "</font></center></html>");
                    milk = true;
                    moo = false;
                    tail = false;
                }
            } else if((x > 280) && (x < 300) && (y > 20) && (y < 175)) {
                if(!tail) {
                    setToolTipText("<html><em><b>" + getBundleString("tail") + "</b></em></html>");
                    tail = true;
                    moo = false;
                    milk = false;
                }
            } else if(moo || milk || tail) {
                setToolTipText(getBundleString("tooltip_features"));
                moo = false;
                tail = false;
                milk = false;
            }

            return true;
        }
    }

}
