/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jdesktop.swingxset;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.util.GraphicsUtilities;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

//@DemoProperties(
//        value = "IntroSplash",
//        category = "Intro",
//        description = "Demonstrates an in-application splash with animation effect.",
//        sourceFiles = {                
//                "org/jdesktop/swingxset/IntroPanelDemo.java",
//                "org/jdesktop/swingxset/SwingXSet.java",
//                "org/jdesktop/swingx/appframework/SingleXFrameApplication.java", 
//                "org/jdesktop/swingx/appframework/XProperties.java"
//                
//                }
//)
/**
 * Intro panel which uses compound, animated painters to show the app image.
 * 
 * @author Amy Fowler
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class IntroPanelDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 1464028488363824142L;
	private static final Logger LOG = Logger.getLogger(IntroPanelDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				AbstractDemo demo = new IntroPanelDemo();
				JXFrame frame = new JXFrame("demo", exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setSize(680, 400);
            	frame.setVisible(true);
			}		
    	});

    }

    private SlidingPainter textImagePainter;

    private ImagePainter introImagePainter;
 
    public IntroPanelDemo() {
        setName("introPanel");
        
        // <snip> ImagePainters for intro
        introImagePainter = new ImagePainter();
        introImagePainter.setFillHorizontal(true);
        introImagePainter.setVerticalAlignment(VerticalAlignment.TOP);
        
        textImagePainter = new SlidingPainter();
        textImagePainter.setVisible(false);
        textImagePainter.setHorizontalAlignment(HorizontalAlignment.LEFT);
        textImagePainter.setVerticalAlignment(VerticalAlignment.TOP);
        
        setBackgroundPainter(new CompoundPainter<Component>(introImagePainter, textImagePainter));
        // </snip>
        
        /* props:
        introPanel.introImage=images/home_notext.png
        introPanel.textImage=images/home_text.png        
         */
        setIntroImage(getBufferedImage("resources/images/home_notext.png"));
        setTextImage(getBufferedImage("resources/images/home_text.png"));

//      bind():
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                slideTextIn();
            }
        });
     
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}
    
    private BufferedImage getBufferedImage(String resource) {
    	try {
            InputStream fis = StaticUtilities.getResourceAsStream(this.getClass(), resource);
    		BufferedImage im = ImageIO.read(fis);
    		return im;
    	} catch (IOException e) {
    		LOG.log(Level.WARNING, "cannot load resource "+resource);
    	}
    	return null;
    }

    /**
     * Configures the intro image painter with the given image, 
     * converting to a BufferedImage if necessary.
     * 
     * @param image
     */
    private void setIntroImage(Image image) {
        introImagePainter.setImage(image instanceof BufferedImage ? 
                (BufferedImage) image : GraphicsUtilities.convertToBufferedImage(image));
    }
    
    /**
     * Configures the text image painter with the given image, 
     * converting to a BufferedImage if necessary.
     *  
     * @param image
     */
    // <snip> ImagePainters for intro
    // the image is loaded via resource injection
    private void setTextImage(Image image) {
        textImagePainter.setImage(image instanceof BufferedImage ? 
                (BufferedImage) image : GraphicsUtilities.convertToBufferedImage(image));
    }
    // </snip>
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // <snip> ImagePainters for intro
        // start animation of text
        if (!textImagePainter.isVisible()) {
            slideTextIn();
            textImagePainter.setVisible(true);
        }
    }
    
    // create, configure and start an animator on the painter's horizontal location
    private void slideTextIn() {
        Animator animator = new Animator(800, new PropertySetter(textImagePainter, "x", getWidth(), 30));
        animator.setStartDelay(800);
        animator.setAcceleration(.2f);
        animator.setDeceleration(.5f);
        animator.start();
        // </snip>
    }
    
//    private void slideTextOut() {
//        Animator animator = new Animator(600, new PropertySetter(textImagePainter, "x", textImagePainter.getX(), -getWidth()));
//        animator.setStartDelay(10);
//        animator.setAcceleration(.5f);
//        animator.setDeceleration(.2f);
//        animator.start();        
//    }
    
    public class SlidingPainter extends ImagePainter {
        public SlidingPainter(BufferedImage image) {
            super(image);
            // move out of the way ;-)
            setX(2000);
        }
        
        public SlidingPainter() {
            this(null);
        }

        public void setX(int x) {
            setInsets(new Insets(110, x, 0, 0));
            // hack around an open issue in swingx:
            // CompoundPainter doesn't propagate property changes of contained painters
            repaint();
        }
        
        public int getX() {
            return getInsets() != null ? getInsets().left : 0;
        }
    }

}
