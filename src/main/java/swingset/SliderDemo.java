/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.SizingConstants;

/**
 * JSlider Demo
 *
 * @author Dave Kloba
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg, select nimbus thumb shape)
 */
public class SliderDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JSlider.gif";

	private static final long serialVersionUID = 2798412694764911672L;
	private static final String DESCRIPTION = "Shows an example of using the JSlider component.";
	
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new SliderDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
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

    // 5 horizontal Slider: 0..4 , 4 vertical Slider: 5..8
    JSlider hs1,hs2,hs3,hs4,hs5;
    JSlider[] slider = new JSlider[9];  
    
    /**
     * SliderDemo Constructor
     * @param frame controller Frame
     */
    public SliderDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name")); // DESCRIPTION

        JPanel hp;
        JPanel vp;
        GridLayout g;
        JPanel tp;
        JLabel tf;
        ChangeListener listener;

        tf = new JLabel(getBundleString("slidervalue"));
        super.add(tf, BorderLayout.SOUTH);

        tp = new JPanel();
        g = new GridLayout(1, 2);
        g.setHgap(5);
        g.setVgap(5);
        tp.setLayout(g);
        super.add(tp, BorderLayout.CENTER);

        listener = new SliderListener(tf);

        BevelBorder border = new BevelBorder(BevelBorder.LOWERED);

        hp = new JPanel();
        hp.setLayout(new BoxLayout(hp, BoxLayout.Y_AXIS));
        hp.setBorder(new TitledBorder(
                        border,
                        getBundleString("horizontal"),
                        TitledBorder.LEFT,
                        TitledBorder.ABOVE_TOP));
        tp.add(hp);

        vp = new JPanel();
        vp.setLayout(new BoxLayout(vp, BoxLayout.X_AXIS));
        vp.setBorder(new TitledBorder(
                        border,
                        getBundleString("vertical"),
                        TitledBorder.LEFT,
                        TitledBorder.ABOVE_TOP));
        tp.add(vp);

        // Horizontal Slider 1
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("plain")));
        slider[0] = new JSlider(-10, 100, 20);
        slider[0].getAccessibleContext().setAccessibleName(getBundleString("plain"));
        slider[0].getAccessibleContext().setAccessibleDescription(getBundleString("a_plain_slider"));
        slider[0].addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(slider[0]);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 2
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("majorticks")));
        slider[1] = new JSlider(100, 1000, 400);
        slider[1].setPaintTicks(true);
        slider[1].setMajorTickSpacing(100);
        slider[1].getAccessibleContext().setAccessibleName(getBundleString("majorticks"));
        slider[1].getAccessibleContext().setAccessibleDescription(getBundleString("majorticksdescription"));
        slider[1].addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(slider[1]);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 3
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("ticks")));
        slider[2] = new JSlider(0, 11, 6);
//        s.putClientProperty("JSlider.isFilled", Boolean.FALSE ); // XXX ?? true in Ocean

        slider[2].setPaintTicks(true);
        slider[2].setMajorTickSpacing(5);
        slider[2].setMinorTickSpacing(1);

        slider[2].setPaintLabels( true );
        slider[2].setSnapToTicks( true );

        Integer i11 = Integer.valueOf(11);
        slider[2].getLabelTable().put(i11, new JLabel(i11.toString(), JLabel.CENTER));
        slider[2].setLabelTable( slider[2].getLabelTable() );
        //Sets the localized accessible name of this object. 
        //Changing the name willcause a PropertyChangeEvent to be fired for the ACCESSIBLE_NAME_PROPERTY property.
        slider[2].getAccessibleContext().setAccessibleName(getBundleString("minorticks"));
        slider[2].getAccessibleContext().setAccessibleDescription(getBundleString("minorticksdescription"));

        slider[2].addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(slider[2]);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // SvgViewer: die ticks zwischen 56 und 88 fehlen! kann ich hier nicht nachvollziehen
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("ticks")));
        int initialSize = SizingConstants.BUTTON_ICON;
        // ctor JSlider(int min, int max, int value):
        slider[3] = new JSlider(SizingConstants.SMALL_ICON, SizingConstants.XXL, initialSize);
//        s.putClientProperty("JSlider.isFilled", Boolean.TRUE );
        slider[3].setSnapToTicks(true);
        slider[3].setPaintLabels(true);
        slider[3].setPaintTicks(true);
        slider[3].setMajorTickSpacing(32);
        slider[3].setMinorTickSpacing(8);
        slider[3].setValue(initialSize); // initialSize im ctor Slider steht auf 48!?
        // das letzte Label 128 fehlt, weil 16 + 3*32 = 112 und das nächste Label 144 wäre,
        // das kann man manuell nachtragen, aber offenbar nicht in Radiance
        Integer i = Integer.valueOf(128);
        slider[3].getLabelTable().put(i, new JLabel(i.toString(), JLabel.CENTER));
        slider[3].setLabelTable( slider[3].getLabelTable() );

        slider[3].addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(slider[3]);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 4
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("disabled")));
        BoundedRangeModel brm = new DefaultBoundedRangeModel(80, 0, 0, 100);
        slider[4] = new JSlider(brm);
        slider[4].setPaintTicks(true);
        slider[4].setMajorTickSpacing(20);
        slider[4].setMinorTickSpacing(5);
        slider[4].setEnabled(false);
        slider[4].getAccessibleContext().setAccessibleName(getBundleString("disabled"));
        slider[4].getAccessibleContext().setAccessibleDescription(getBundleString("disableddescription"));
        slider[4].addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(slider[4]);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);

        //////////////////////////////////////////////////////////////////////////////

        // Vertical Slider 1
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("plain")));
        slider[5] = new JSlider(JSlider.VERTICAL, -10, 100, 20);
        slider[5].getAccessibleContext().setAccessibleName(getBundleString("plain"));
        slider[5].getAccessibleContext().setAccessibleDescription(getBundleString("a_plain_slider"));
        slider[5].addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP10));
        p.add(slider[5]);
        p.add(Box.createRigidArea(HGAP10));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP10));

        // Vertical Slider 2
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("majorticks")));
        slider[6] = new JSlider(JSlider.VERTICAL, 100, 1000, 400);

        slider[6].putClientProperty( "JSlider.isFilled", Boolean.TRUE );

        slider[6].setPaintTicks(true);
        slider[6].setMajorTickSpacing(100);
        slider[6].getAccessibleContext().setAccessibleName(getBundleString("majorticks"));
        slider[6].getAccessibleContext().setAccessibleDescription(getBundleString("majorticksdescription"));
        slider[6].addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP25));
        p.add(slider[6]);
        p.add(Box.createRigidArea(HGAP25));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP5));

        // Vertical Slider 3
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("minorticks")));
        slider[7] = new JSlider(JSlider.VERTICAL, 0, 100, 60);
        slider[7].setPaintTicks(true);
        slider[7].setMajorTickSpacing(20);
        slider[7].setMinorTickSpacing(5);

        slider[7].setPaintLabels( true );

        slider[7].getAccessibleContext().setAccessibleName(getBundleString("minorticks"));
        slider[7].getAccessibleContext().setAccessibleDescription(getBundleString("minorticksdescription"));

        slider[7].addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP10));
        p.add(slider[7]);
        p.add(Box.createRigidArea(HGAP10));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP5));

        // Vertical Slider 4
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("disabled")));
        slider[8] = new JSlider(JSlider.VERTICAL, 0, 100, 80);
        slider[8].setPaintTicks(true);
        slider[8].setMajorTickSpacing(20);
        slider[8].setMinorTickSpacing(5);
        slider[8].setEnabled(false);
        slider[8].getAccessibleContext().setAccessibleName(getBundleString("disabled"));
        slider[8].getAccessibleContext().setAccessibleDescription(getBundleString("disableddescription"));
        slider[8].addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP20));
        p.add(slider[8]);
        p.add(Box.createRigidArea(HGAP20));
        vp.add(p);
    }

    @Override
	public JXPanel getControlPane() {

        @SuppressWarnings("serial")
		JXPanel controls = new JXPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(300, super.getMaximumSize().height);
            }
        };
        
        ButtonGroup group = new ButtonGroup();
        JRadioButton button;

        Box buttonWrapper = new Box(BoxLayout.X_AXIS);

        controls.setLayout(new GridLayout(0, 1));

        // Create radio buttons to select the thumb shape
        final String propertyName = "Slider.paintThumbArrowShape";
        button = new JRadioButton();
        button.setText(getBundleString("Circle thumb (default) in Nimbus ", button));
        button.setEnabled(UIManager.getLookAndFeel().getClass().getName().contains("Nimbus"));
        button.setSelected(true);
        button.addActionListener(ae -> {
        	for(int i=0; i<slider.length; i++) {
        		slider[i].putClientProperty(propertyName, Boolean.FALSE );
        	}
        });
        group.add(button);
        buttonWrapper.add(button);

        // Create a radio button the horizontally split the split pane.
        button = new JRadioButton();
        button.setText(getBundleString("ARROWSHAPE in Nimbus", button));
        button.setEnabled(UIManager.getLookAndFeel().getClass().getName().contains("Nimbus"));
        button.addActionListener(ae -> {
        	for(int i=0; i<slider.length; i++) {
        		slider[i].putClientProperty(propertyName, Boolean.TRUE );
        	}
        });
        group.add(button);
        buttonWrapper.add(button);
        
        controls.add(buttonWrapper);

        return controls;
	}

    class SliderListener implements ChangeListener {
        JLabel tf;
        public SliderListener(JLabel f) {
            tf = f;
        }
        public void stateChanged(ChangeEvent e) {
            JSlider s1 = (JSlider)e.getSource();
            tf.setText(getBundleString("slidervalue") + s1.getValue());
        }
    }
}
