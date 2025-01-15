/*
 * Created on 05.12.2008
 *
 */
package org.jdesktop.swingx.demos.search;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Painter;
import javax.swing.Timer;

import org.jdesktop.swingx.painter.AlphaPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;

/**
 * a simple DecoratorFactory
 * used in SearchDemo
 *
 */
public class DecoratorFactory {

	/** Color */
    public static final Color MATCH_COLOR = Color.YELLOW;
	/** Color */
    public static final Color PINSTRIPE_COLOR = Color.GREEN;
    
    /**
     * Factory for PlainPainter
     * @return MattePainter
     */
    public static Painter<Component> createPlainPainter() {
        return new MattePainter(MATCH_COLOR);
    }
    
    /**
     * create AnimatedPainter
     * @return Painter
     */
    public static Painter<Component> createAnimatedPainter() {
        final AlphaPainter<Component> alpha = new AlphaPainter<Component>();
        alpha.setAlpha(1f);
        final PinstripePainter pinstripePainter = new PinstripePainter(PINSTRIPE_COLOR,45,3,3);
        alpha.setPainters(new MattePainter(MATCH_COLOR), pinstripePainter);
        ActionListener l = new ActionListener() {
            boolean add;
            public void actionPerformed(ActionEvent e) {
                float a = add ? (alpha.getAlpha() + 0.1f) : (alpha.getAlpha() - 0.1f);
                if (a > 1.0) {
                    a = 1f;
                    add = false;
                } else if (a < 0) {
                    a = 0;
                    add = true;
                }
                alpha.setAlpha(a);
                pinstripePainter.setAngle(pinstripePainter.getAngle()+10);
            }
            
        };
        new Timer(100, l).start();
        return alpha;
    }
    
}    

