/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXFrame.StartPosition;

/**
 * JSlider Demo
 *
 * @author Dave Kloba
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
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
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new SliderDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    /**
     * SliderDemo Constructor
     */
    public SliderDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        JSlider s;
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
        s = new JSlider(-10, 100, 20);
        s.getAccessibleContext().setAccessibleName(getBundleString("plain"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("a_plain_slider"));
        s.addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(s);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 2
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("majorticks")));
        s = new JSlider(100, 1000, 400);
        s.setPaintTicks(true);
        s.setMajorTickSpacing(100);
        s.getAccessibleContext().setAccessibleName(getBundleString("majorticks"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("majorticksdescription"));
        s.addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(s);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 3
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("ticks")));
        s = new JSlider(0, 11, 6);

        s.putClientProperty("JSlider.isFilled", Boolean.TRUE );

        s.setPaintTicks(true);
        s.setMajorTickSpacing(5);
        s.setMinorTickSpacing(1);

        s.setPaintLabels( true );
        s.setSnapToTicks( true );

        Integer i11 = Integer.valueOf(11);
//        Dictionary<Integer, JLabel> dict = s.getLabelTable();
//        dict.put(i11, new JLabel(i11.toString(), JLabel.CENTER));
        s.getLabelTable().put(i11, new JLabel(i11.toString(), JLabel.CENTER));
        s.setLabelTable( s.getLabelTable() );

        s.getAccessibleContext().setAccessibleName(getBundleString("minorticks"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("minorticksdescription"));

        s.addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(s);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);
        hp.add(Box.createRigidArea(VGAP10));

        // Horizontal Slider 4
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new TitledBorder(getBundleString("disabled")));
        BoundedRangeModel brm = new DefaultBoundedRangeModel(80, 0, 0, 100);
        s = new JSlider(brm);
        s.setPaintTicks(true);
        s.setMajorTickSpacing(20);
        s.setMinorTickSpacing(5);
        s.setEnabled(false);
        s.getAccessibleContext().setAccessibleName(getBundleString("disabled"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("disableddescription"));
        s.addChangeListener(listener);

        p.add(Box.createRigidArea(VGAP5));
        p.add(s);
        p.add(Box.createRigidArea(VGAP5));
        hp.add(p);

        //////////////////////////////////////////////////////////////////////////////

        // Vertical Slider 1
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("plain")));
        s = new JSlider(JSlider.VERTICAL, -10, 100, 20);
        s.getAccessibleContext().setAccessibleName(getBundleString("plain"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("a_plain_slider"));
        s.addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP10));
        p.add(s);
        p.add(Box.createRigidArea(HGAP10));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP10));

        // Vertical Slider 2
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("majorticks")));
        s = new JSlider(JSlider.VERTICAL, 100, 1000, 400);

        s.putClientProperty( "JSlider.isFilled", Boolean.TRUE );

        s.setPaintTicks(true);
        s.setMajorTickSpacing(100);
        s.getAccessibleContext().setAccessibleName(getBundleString("majorticks"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("majorticksdescription"));
        s.addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP25));
        p.add(s);
        p.add(Box.createRigidArea(HGAP25));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP5));

        // Vertical Slider 3
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("minorticks")));
        s = new JSlider(JSlider.VERTICAL, 0, 100, 60);
        s.setPaintTicks(true);
        s.setMajorTickSpacing(20);
        s.setMinorTickSpacing(5);

        s.setPaintLabels( true );

        s.getAccessibleContext().setAccessibleName(getBundleString("minorticks"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("minorticksdescription"));

        s.addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP10));
        p.add(s);
        p.add(Box.createRigidArea(HGAP10));
        vp.add(p);
        vp.add(Box.createRigidArea(HGAP5));

        // Vertical Slider 4
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setBorder(new TitledBorder(getBundleString("disabled")));
        s = new JSlider(JSlider.VERTICAL, 0, 100, 80);
        s.setPaintTicks(true);
        s.setMajorTickSpacing(20);
        s.setMinorTickSpacing(5);
        s.setEnabled(false);
        s.getAccessibleContext().setAccessibleName(getBundleString("disabled"));
        s.getAccessibleContext().setAccessibleDescription(getBundleString("disableddescription"));
        s.addChangeListener(listener);
        p.add(Box.createRigidArea(HGAP20));
        p.add(s);
        p.add(Box.createRigidArea(HGAP20));
        vp.add(p);
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
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
