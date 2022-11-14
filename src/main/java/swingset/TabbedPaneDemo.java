/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXFrame.StartPosition;

/**
 * JTabbedPane Demo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class TabbedPaneDemo extends AbstractDemo implements ActionListener {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JTabbedPane.gif";

	private static final long serialVersionUID = 3060212344858037094L;
    private static final String IMG_PATH = "tabbedpane/"; // prefix dir

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new TabbedPaneDemo(controller);
			JXFrame frame = new JXFrame("demo", exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    JTabbedPane tabbedpane;
	HeadSpin spin;

    /**
     * TabbedPaneDemo Constructor
     * @param frame controller Frame
     */
    public TabbedPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));
    	
        // create tab
        tabbedpane = new JTabbedPane();
        super.add(tabbedpane, BorderLayout.CENTER);

        JLabel pix = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"laine.jpg"));
        tabbedpane.add(getBundleString("laine"), pix);

        pix = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"ewan.jpg"));
        tabbedpane.add(getBundleString("ewan"), pix);

        pix = new JLabel(StaticUtilities.createImageIcon(IMG_PATH+"hania.jpg"));
        tabbedpane.add(getBundleString("hania"), pix);

        spin = new HeadSpin();
        tabbedpane.add(getBundleString("bounce"), spin);

        tabbedpane.getModel().addChangeListener(
           new ChangeListener() {
              public void stateChanged(ChangeEvent e) {
                  SingleSelectionModel model = (SingleSelectionModel) e.getSource();
                  if(model.getSelectedIndex() == tabbedpane.getTabCount()-1) {
                      spin.go();
                  }
              }
           }
        );

    }


    ButtonGroup group;

    JRadioButton top;
    JRadioButton bottom;
    JRadioButton left;
    JRadioButton right;

    @Override
	public JXPanel getControlPane() {

        // create tab position controls
        JXPanel tabControls = new JXPanel();
        tabControls.add(new JLabel(getBundleString("label")));
        top    = (JRadioButton) tabControls.add(new JRadioButton(getBundleString("top")));
        left   = (JRadioButton) tabControls.add(new JRadioButton(getBundleString("left")));
        bottom = (JRadioButton) tabControls.add(new JRadioButton(getBundleString("bottom")));
        right  = (JRadioButton) tabControls.add(new JRadioButton(getBundleString("right")));

        group = new ButtonGroup();
        group.add(top);
        group.add(bottom);
        group.add(left);
        group.add(right);

        top.setSelected(true);

        top.addActionListener(this);
        bottom.addActionListener(this);
        left.addActionListener(this);
        right.addActionListener(this);

        return tabControls;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == top) {
            tabbedpane.setTabPlacement(JTabbedPane.TOP);
        } else if(e.getSource() == left) {
            tabbedpane.setTabPlacement(JTabbedPane.LEFT);
        } else if(e.getSource() == bottom) {
            tabbedpane.setTabPlacement(JTabbedPane.BOTTOM);
        } else if(e.getSource() == right) {
            tabbedpane.setTabPlacement(JTabbedPane.RIGHT);
        }
    }

    class HeadSpin extends JComponent implements ActionListener {
        javax.swing.Timer animator;

        ImageIcon icon[] = new ImageIcon[6];

        int tmpScale;

        final static int numImages = 6;

        double x[] = new double[numImages];
        double y[] = new double[numImages];

        int xh[] = new int[numImages];
        int yh[] = new int[numImages];

        double scale[] = new double[numImages];

        public HeadSpin() {
            setBackground(Color.black);
            icon[0] = StaticUtilities.createImageIcon(IMG_PATH+"ewan.gif");
            icon[1] = StaticUtilities.createImageIcon(IMG_PATH+"stephen.gif");
            icon[2] = StaticUtilities.createImageIcon(IMG_PATH+"david.gif");
            icon[3] = StaticUtilities.createImageIcon(IMG_PATH+"matthew.gif");
            icon[4] = StaticUtilities.createImageIcon(IMG_PATH+"blake.gif");
            icon[5] = StaticUtilities.createImageIcon(IMG_PATH+"brooke.gif");

            /*
            for(int i = 0; i < 6; i++) {
                x[i] = (double) rand.nextInt(500);
                y[i] = (double) rand.nextInt(500);
            }
            */
        }

        public void go() {
            animator = new javax.swing.Timer(22 + 22 + 22, this);
            animator.start();
        }

        public void paint(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            for(int i = 0; i < numImages; i++) {
                if(x[i] > 3*i) {
                    nudge(i);
                    squish(g, icon[i], xh[i], yh[i], scale[i]);
                } else {
                    x[i] += .05;
                    y[i] += .05;
                }
            }
        }

        Random rand = new Random();

        public void nudge(int i) {
            x[i] += (double) rand.nextInt(1000) / 8756;
            y[i] += (double) rand.nextInt(1000) / 5432;
            int tmpScale = (int) (Math.abs(Math.sin(x[i])) * 10);
            scale[i] = (double) tmpScale / 10;
            int nudgeX = (int) (((double) getWidth()/2) * .8);
            int nudgeY = (int) (((double) getHeight()/2) * .60);
            xh[i] = (int) (Math.sin(x[i]) * nudgeX) + nudgeX;
            yh[i] = (int) (Math.sin(y[i]) * nudgeY) + nudgeY;
        }

        public void squish(Graphics g, ImageIcon icon, int x, int y, double scale) {
            if(isVisible()) {
                g.drawImage(icon.getImage(), x, y,
                            (int) (icon.getIconWidth()*scale),
                            (int) (icon.getIconHeight()*scale),
                            this);
            }
        }

        public void actionPerformed(ActionEvent e) {
            if(isVisible()) {
                repaint();
            } else {
                animator.stop();
            }
        }
    }
}
