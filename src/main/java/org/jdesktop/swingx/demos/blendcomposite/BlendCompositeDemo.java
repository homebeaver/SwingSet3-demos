/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.blendcomposite;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.BlendComposite;
import org.jdesktop.swingx.graphics.BlendComposite.BlendingMode;
import org.jdesktop.swingx.util.GraphicsUtilities;

import swingset.AbstractDemo;

/**
 * A demo for the {@code BlendComposite}.
 *
 * @author EUG https://github.com/homebeaver (reorg)
 * @author Karl George Schaefer
 * @author Romain Guy romain.guy@mac.com (original version)
 */
//@DemoProperties(
//    value = "BlendComposite Demo",
//    category = "Graphics",
//    description = "Demonstrates BlendComposite, a Composite for defining various blending effects.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/blendcomposite/BlendCompositeDemo.java",
//        "org/jdesktop/swingx/demos/blendcomposite/resources/BlendCompositeDemo.properties",
//        "org/jdesktop/swingx/demos/blendcomposite/resources/BlendCompositeDemo.html",
//        "org/jdesktop/swingx/demos/blendcomposite/resources/images/A.jpg",
//        "org/jdesktop/swingx/demos/blendcomposite/resources/images/B.jpg"
//    }
//)
//@SuppressWarnings("serial")
public class BlendCompositeDemo extends AbstractDemo {

	private static final long serialVersionUID = -4489182800035657788L;
	private static final Logger LOG = Logger.getLogger(BlendCompositeDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates BlendComposite, a Composite for defining various blending effects";

	/**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new BlendCompositeDemo(controller);
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

    @SuppressWarnings("serial")
	private static class CompositeTestPanel extends JPanel {
        private BufferedImage image = null;
        private Composite composite = AlphaComposite.Src;
        private BufferedImage imageA;
        private BufferedImage imageB;
        private boolean repaint = false;

        public CompositeTestPanel() {
            setOpaque(false);
            try {
                imageA = GraphicsUtilities.loadCompatibleImage(getClass().getResource("resources/images/A.jpg"));
                imageB = GraphicsUtilities.loadCompatibleImage(getClass().getResource("resources/images/B.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(imageA.getWidth(), imageA.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image == null) {
                image = new BufferedImage(imageA.getWidth(),
                                          imageA.getHeight(),
                                          BufferedImage.TYPE_INT_ARGB);
                repaint = true;
            }

            if (repaint) {
                Graphics2D g2 = image.createGraphics();
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, image.getWidth(), image.getHeight());

                g2.setComposite(AlphaComposite.Src);
                g2.drawImage(imageA, 0, 0, null);
                g2.setComposite(getComposite());
                g2.drawImage(imageB, 0, 0, null);
                g2.dispose();

                repaint = false;
            }

            int x = (getWidth() - image.getWidth()) / 2;
            int y = (getHeight() - image.getHeight()) / 2;
            g.drawImage(image, x, y, null);
        }

        public void setComposite(Composite composite) {
            if (composite != null) {
                this.composite = composite;
                this.repaint = true;
                repaint();
            }
        }

        public Composite getComposite() {
            return this.composite;
        }
    }
    
    private CompositeTestPanel compositeTestPanel;
    // controller:
    private JComboBox<BlendingMode> combo;
    private JSlider slider;
    
    /**
     * BlendCompositeDemo Constructor
     * 
     * @param frame controller Frame frame.title will be set
     */
    public BlendCompositeDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	compositeTestPanel = new CompositeTestPanel();
    	compositeTestPanel.setComposite(BlendComposite.Average);
    	add(new JScrollPane(compositeTestPanel));
    }
    
	@Override
	public JXPanel getControlPane() {
    	JXPanel controls = new JXPanel(new FlowLayout(FlowLayout.LEFT));
    	
    	combo = new JComboBox<BlendingMode>(BlendComposite.BlendingMode.values());
    	combo.addActionListener( ae -> {
        	LOG.info("selected item:" + combo.getSelectedItem());
        	BlendingMode bm = (BlendingMode) combo.getSelectedItem();
        	compositeTestPanel.setComposite(BlendComposite.getInstance(bm, slider.getValue() / 100.0f));
        });
    	
    	slider = new JSlider(0, 100, 100);
    	slider.addChangeListener( ce -> {
            BlendComposite blend = (BlendComposite) compositeTestPanel.getComposite();
            blend = blend.derive(slider.getValue() / 100.0f);
            compositeTestPanel.setComposite(blend);
    	});
        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        //TODO can we fill these labels from the properties file?
/*
controlPanel.opaque=false

trailSlider.opaque=false
trailSlider.paintLabels=true
trailSlider.minimum=1
trailSlider.maximum=20
trailSlider.value=3
 */
        labels.put(0, new JLabel("0%"));
        labels.put(100, new JLabel("100%"));
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
    	
    	controls.add(combo);
    	controls.add(slider);
    	
        return controls;
    }

}
