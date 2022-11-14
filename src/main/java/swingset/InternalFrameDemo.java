/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

/**
 * Internal Frames Demo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class InternalFrameDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JDesktop.gif";
	
	private static final long serialVersionUID = 8207106273505201473L;
	private static final Logger LOG = Logger.getLogger(InternalFrameDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new InternalFrameDemo(controller);
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

    int windowCount = 0;
    JDesktopPane desktop = null;

    ImageIcon icon1, icon2, icon3, icon4;
    ImageIcon smIcon1, smIcon2, smIcon3, smIcon4;

    private Integer FIRST_FRAME_LAYER  = Integer.valueOf(1);
    private Integer DEMO_FRAME_LAYER   = Integer.valueOf(2);
//    private Integer PALETTE_LAYER      = Integer.valueOf(3);

    private int FRAME0_X        = 15;
    private int FRAME0_Y        = 280;

    private int FRAME0_WIDTH    = 320;
    private int FRAME0_HEIGHT   = 230;

    private int FRAME_WIDTH     = 225;
    private int FRAME_HEIGHT    = 150;

    private int PALETTE_X      = 375;
    private int PALETTE_Y      = 20;

    private int PALETTE_WIDTH  = 260;
    private int PALETTE_HEIGHT = 260;

    JCheckBox windowResizable   = null;
    JCheckBox windowClosable    = null;
    JCheckBox windowIconifiable = null;
    JCheckBox windowMaximizable = null;

    JTextField windowTitleField = null;
    JLabel windowTitleLabel = null;

    /**
     * InternalFrameDemo Constructor
     * 
     * @param frame controller Frame
     */
    public InternalFrameDemo(Frame frame) {
        super(new BorderLayout());
        super.setPreferredSize(PREFERRED_SIZE);
        super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        // preload all the icons we need for this demo
    	icon1 = getResourceAsIcon(getClass(), "images/ImageClub/misc/fish.gif");
    	icon2 = getResourceAsIcon(getClass(), "images/ImageClub/misc/moon.gif");
    	icon3 = getResourceAsIcon(getClass(), "images/ImageClub/misc/sun.gif");
    	icon4 = getResourceAsIcon(getClass(), "images/ImageClub/misc/cab.gif");

    	smIcon1 = getResourceAsIcon(getClass(), "images/ImageClub/misc/fish_small.gif");
    	smIcon2 = getResourceAsIcon(getClass(), "images/ImageClub/misc/moon_small.gif");
    	smIcon3 = getResourceAsIcon(getClass(), "images/ImageClub/misc/sun_small.gif");
    	smIcon4 = getResourceAsIcon(getClass(), "images/ImageClub/misc/cab_small.gif");

        windowTitleField = new JTextField(getBundleString("frame.labelAndMnemonic"));
        windowTitleLabel = new JLabel(getBundleString("title_text_field.labelAndMnemonic"));

        windowResizable   = new JCheckBox(getBundleString("resizable.labelAndMnemonic"), true);
        windowIconifiable = new JCheckBox(getBundleString("iconifiable.labelAndMnemonic"), true);
        windowClosable    = new JCheckBox(getBundleString("closable.labelAndMnemonic"), true);
        windowMaximizable = new JCheckBox(getBundleString("maximizable.labelAndMnemonic"), true);

        // Create the desktop pane
        desktop = new JDesktopPane();
        super.add(desktop, BorderLayout.CENTER);

        // Create an initial internal frame to show
        JInternalFrame frame1 = createInternalFrame(icon1, FIRST_FRAME_LAYER, 1, 1);
        frame1.setBounds(FRAME0_X, FRAME0_Y, FRAME0_WIDTH, FRAME0_HEIGHT);

        // Create four more starter windows
        createInternalFrame(icon1, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon3, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon4, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
        createInternalFrame(icon2, DEMO_FRAME_LAYER, FRAME_WIDTH, FRAME_HEIGHT);
    }

    /**
     * Create an internal frame and add a scrollable imageicon to it
     */
    private JInternalFrame createInternalFrame(Icon icon, Integer layer, int width, int height) {
        JInternalFrame jif = new JInternalFrame();

//        
        if(!windowTitleField.getText().equals(getBundleString("frame.labelAndMnemonic"))) {
            jif.setTitle(windowTitleField.getText() + "  ");
            LOG.finest("-------------- >>> " +jif);
        } else {
            jif = new JInternalFrame(getBundleString("frame.labelAndMnemonic") + " " + windowCount + "  ");
        }

        // set properties
        jif.setClosable(windowClosable.isSelected());
        jif.setMaximizable(windowMaximizable.isSelected());
        jif.setIconifiable(windowIconifiable.isSelected());
        jif.setResizable(windowResizable.isSelected());

        jif.setBounds(20*(windowCount%10), 20*(windowCount%10), width, height);
        jif.setContentPane(new ImageScroller(this, icon, 0, windowCount));

        windowCount++;

        desktop.add(jif, layer);

        // Set this internal frame to be selected

        try {
            jif.setSelected(true);
        } catch (java.beans.PropertyVetoException e2) {
        }

        jif.show();

        return jif;
    }

    @Override
	public JXPanel getControlPane() {

        JXPanel controller = new JXPanel(new BorderLayout());
        // use controller:
        createInternalFramePalette(controller);
        // or use Internal Frame:
//        JInternalFrame ifp = createInternalFramePalette(null);
//        controller.add(ifp, BorderLayout.CENTER);
//        ifp.show();

    	return controller;
    }

    /**
     * create an internalFrame and fill a JXPanel controller with
     *  frame maker buttons at NORTH
     *  frame property checkboxes at CENTER
     *  and
     *  frame title textfield at SOUTH
     *  
     * @param controller a border layouted Panel (optional)
     * @return InternalFrame
     */
    public JInternalFrame createInternalFramePalette(JXPanel controller) {
        JInternalFrame palette = new JInternalFrame(getBundleString("palette.labelAndMnemonic"));
        palette.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        palette.getContentPane().setLayout(new BorderLayout());
        palette.setBounds(PALETTE_X, PALETTE_Y, PALETTE_WIDTH, PALETTE_HEIGHT);
        palette.setResizable(true);
        palette.setIconifiable(true);

        // *************************************
        // * Create create frame maker buttons
        // *************************************
        JButton b1 = new JButton(smIcon1);
        JButton b2 = new JButton(smIcon2);
        JButton b3 = new JButton(smIcon3);
        JButton b4 = new JButton(smIcon4);

        // add frame maker actions
        b1.addActionListener(new ShowFrameAction(this, icon1));
        b2.addActionListener(new ShowFrameAction(this, icon2));
        b3.addActionListener(new ShowFrameAction(this, icon3));
        b4.addActionListener(new ShowFrameAction(this, icon4));

        // add frame maker buttons to panel
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JPanel buttons1 = new JPanel();
        buttons1.setLayout(new BoxLayout(buttons1, BoxLayout.X_AXIS));

        JPanel buttons2 = new JPanel();
        buttons2.setLayout(new BoxLayout(buttons2, BoxLayout.X_AXIS));

        buttons1.add(b1);
        buttons1.add(Box.createRigidArea(HGAP15));
        buttons1.add(b2);

        buttons2.add(b3);
        buttons2.add(Box.createRigidArea(HGAP15));
        buttons2.add(b4);

        p.add(Box.createRigidArea(VGAP10));
        p.add(buttons1);
        p.add(Box.createRigidArea(VGAP15));
        p.add(buttons2);
        p.add(Box.createRigidArea(VGAP10));

        palette.getContentPane().add(p, BorderLayout.NORTH);
        if(controller!=null) controller.add(p, BorderLayout.NORTH);

        // ************************************
        // * Create frame property checkboxes
        // ************************************
        p = new JPanel() {
            Insets insets = new Insets(10,15,10,5);
            public Insets getInsets() {
                return insets;
            }
        };
        p.setLayout(new GridLayout(1,2));

        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createGlue());
        box.add(windowResizable);
        box.add(windowIconifiable);
        box.add(Box.createGlue());
        p.add(box);

        box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createGlue());
        box.add(windowClosable);
        box.add(windowMaximizable);
        box.add(Box.createGlue());
        p.add(box);

        palette.getContentPane().add(p, BorderLayout.CENTER);
        if(controller!=null) controller.add(p, BorderLayout.CENTER);

        // ************************************
        // *   Create frame title textfield
        // ************************************
        p = new JPanel() {
            Insets insets = new Insets(0,0,10,0);
            public Insets getInsets() {
                return insets;
            }
        };

        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleLabel, BorderLayout.WEST);
        p.add(Box.createRigidArea(HGAP5));
        p.add(windowTitleField, BorderLayout.CENTER);
        p.add(Box.createRigidArea(HGAP5));

        palette.getContentPane().add(p, BorderLayout.SOUTH);
        if(controller!=null) controller.add(p, BorderLayout.SOUTH);

        return palette;
    }

    class ShowFrameAction extends AbstractAction {
    	
        InternalFrameDemo demo;
        Icon icon;

        public ShowFrameAction(InternalFrameDemo demo, Icon icon) {
            this.demo = demo;
            this.icon = icon;
        }

        public void actionPerformed(ActionEvent e) {
            demo.createInternalFrame(icon, getDemoFrameLayer(), getFrameWidth(), getFrameHeight() );
        }
    }

    int getFrameWidth() {
        return FRAME_WIDTH;
    }

    int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    Integer getDemoFrameLayer() {
        return DEMO_FRAME_LAYER;
    }

    class ImageScroller extends JScrollPane {

        public ImageScroller(InternalFrameDemo demo, Icon icon, int layer, int count) {
            super();
            JPanel p = new JPanel();
            p.setBackground(Color.white);
            p.setLayout(new BorderLayout() );

            p.add(new JLabel(icon), BorderLayout.CENTER);

            getViewport().add(p);
            getHorizontalScrollBar().setUnitIncrement(10);
            getVerticalScrollBar().setUnitIncrement(10);
        }

        public Dimension getMinimumSize() {
            return new Dimension(25, 25);
        }

    }

    void updateDragEnabled(boolean dragEnabled) {
        windowTitleField.setDragEnabled(dragEnabled);
    }

}
