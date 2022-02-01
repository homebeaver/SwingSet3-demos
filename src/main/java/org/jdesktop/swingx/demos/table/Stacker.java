package org.jdesktop.swingx.demos.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;

public class Stacker extends JLayeredPane {

    private Component master; // dictates sizing, scrolling
    private JPanel messageLayer;
    private JXPanel messageAlpha;

    public Stacker(Component master) {
        this.master = master;
        setLayout(null);
        add(master, JLayeredPane.DEFAULT_LAYER);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return master.getPreferredSize();
    }
    
    @Override
    public void doLayout() {
        // ensure all layers are sized the same
        Dimension size = getSize();
        Component layers[] = getComponents();
        for (Component layer : layers) {
            layer.setBounds(0, 0, size.width, size.height);
        }
    }
   
    /**
     * Fades in the specified message component in the top layer of this
     * layered pane.
     * @param message the component to be displayed in the message layer
     * @param finalAlpha the alpha value of the component when fade in is complete
     */
    public void showMessageLayer(JComponent message, final float finalAlpha) {
        messageLayer = new JPanel();
        messageLayer.setOpaque(false);
        GridBagLayout gridbag = new GridBagLayout();
        messageLayer.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;

        messageAlpha = new JXPanel();
        messageAlpha.setOpaque(false);
        messageAlpha.setAlpha(0.0f);
        gridbag.addLayoutComponent(messageAlpha, c);
        messageLayer.add(messageAlpha);
        messageAlpha.add(message);

        add(messageLayer, JLayeredPane.POPUP_LAYER);
        revalidate();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Animator animator = new Animator(2000, new PropertySetter(messageAlpha, "alpha", 0.0f, finalAlpha));
                animator.setStartDelay(200);
                animator.setAcceleration(.2f);
                animator.setDeceleration(.5f);
                animator.start();
            }
        });
    }

    /**
     * Fades out and removes the current message component
     */
    public void hideMessageLayer() {
        if (messageLayer != null && messageLayer.isShowing()) {
            Animator animator = new Animator(500, new PropertySetter(messageAlpha, "alpha", messageAlpha.getAlpha(), 0.0f) {
				public void end() {
					remove(messageLayer);
					revalidate();
				}
			});
            animator.setStartDelay(300);
            animator.setAcceleration(.2f);
            animator.setDeceleration(.5f);
            animator.start();
        }
    }


}
