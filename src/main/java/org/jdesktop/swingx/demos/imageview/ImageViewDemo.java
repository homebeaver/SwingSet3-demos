/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.imageview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.error.ErrorSupport;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

/**
 * A demo for the {@code JXImageView}.
 *
 * @author Karl George Schaefer
 * @author jm158417 (original JXImageViewDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXImageView Demo",
//    category = "Controls",
//    description = "Demonstrates JXImageView, a control for displaying and manipulating images.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/imageview/ImageViewDemo.java",
//        "org/jdesktop/swingx/demos/imageview/resources/ImageViewDemo.properties",
//        "org/jdesktop/swingx/demos/imageview/resources/ImageViewDemo.html",
//        "org/jdesktop/swingx/demos/imageview/resources/images/ImageViewDemo.png",
//        "org/jdesktop/swingx/demos/imageview/resources/images/flower.thumbnail.jpg"
//    }
//)
//@SuppressWarnings("serial")
public class ImageViewDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -163337099642065783L;
	private static final Logger LOG = Logger.getLogger(ImageViewDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXImageView, a control for displaying and manipulating images.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new ImageViewDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

	private JXImageView imageView;
    private JButton openButton;
    private JButton saveButton;
    private JButton rotateCWButton;
    private JButton rotateCCWButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JToggleButton dragEnabledButton;
    
//    /**
//     * main method allows us to run as a standalone demo.
//     */
//    public static void main(String[] args) {
//        runDemo(ImageViewDemo.class);
//    }

    private static FileDialog getSafeFileDialog(Component comp) {
        Window win = SwingUtilities.windowForComponent(comp);
        if(win instanceof Dialog) {
            return new FileDialog((Dialog)win);
        }
        if(win instanceof Frame) {
            return new FileDialog((Frame)win);
        }
        return null;
    }
    
	private static final String FILENAME = "resources/images/flower.thumbnail.jpg";
	
    /**
     * ImageViewDemo Constructor
     * 
     * @param frame controller Frame
     */
    public ImageViewDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	
        imageView = new JXImageView();
        imageView.setName("imageView");
    	try {
            InputStream fis = StaticUtilities.getResourceAsStream(this.getClass(), FILENAME);
            BufferedImage im = ImageIO.read(fis);
            imageView.setImage(im);
    	} catch (IOException e) {
    		LOG.log(Level.WARNING, "cannot load resource "+FILENAME);    		
    	}
        add(imageView);
        
        JPanel sidebar = new JPanel(new VerticalLayout());
        openButton = new JButton();
        openButton.setName("openButton");
        openButton.setText(getBundleString("openButton.text"));
        // openButton.addActionListener(imageView.getOpenAction());
        openButton.addActionListener( ae -> {
        	LOG.info("ActionEvent "+ae);
        	//imageView.getOpenAction();
//            FileDialog fd = JXImageView.getSafeFileDialog(imageView); // not visible: getSafeFileDialog
        	FileDialog fd = getSafeFileDialog(imageView);
            fd.setMode(FileDialog.LOAD);
            fd.setVisible(true);
            if(fd.getFile() != null) {
                try {
                	imageView.setImage(new File(fd.getDirectory(), fd.getFile()));
                } catch (IOException ex) {
                    //fireError(ex);
                	// private ErrorSupport errorSupport = new ErrorSupport(this);
                	ErrorSupport errorSupport = new ErrorSupport(imageView);
                    errorSupport.fireErrorEvent(ex);
                }
            }
        });
        sidebar.add(openButton);
        
        saveButton = new JButton();
        saveButton.setName("saveButton");
        saveButton.setText(getBundleString("saveButton.text"));
        // saveButton.addActionListener(imageView.getSaveAction()); TODO
        sidebar.add(saveButton);
        
        rotateCWButton = new JButton(); // CW = Clockwise
        rotateCWButton.setName("rotateCW");
        rotateCWButton.setText(getBundleString("rotateCW.text"));
//        rotateCWButton.addActionListener(imageView.getRotateClockwiseAction());
        rotateCWButton.addActionListener( ae -> {
        	LOG.info("ActionEvent "+ae);
        	//imageView.getRotateClockwiseAction();
        	Image img = imageView.getImage();
        	BufferedImage src = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        	BufferedImage dst = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        	Graphics2D g = (Graphics2D)src.getGraphics();
        	try {
        		g.drawImage(img, 0, 0, null); // smooth scaling
        	} finally {
        		g.dispose();
        	}
        	AffineTransform trans = AffineTransform.getRotateInstance(Math.PI/2,0,0);
        	trans.translate(0,-src.getHeight());
        	BufferedImageOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        	op.filter(src,dst);
        	imageView.setImage(dst);
        });
        sidebar.add(rotateCWButton);
        
        rotateCCWButton = new JButton();
        rotateCCWButton.setName("rotateCCW");
        rotateCCWButton.setText(getBundleString("rotateCCW.text"));
        sidebar.add(rotateCCWButton);
        
        zoomInButton = new JButton();
        zoomInButton.setName("zoomIn");
        zoomInButton.setText(getBundleString("zoomIn.text"));
        sidebar.add(zoomInButton);
        
        zoomOutButton = new JButton();
        zoomOutButton.setName("zoomOut");
        zoomOutButton.setText(getBundleString("zoomOut.text"));
        sidebar.add(zoomOutButton);
        
        dragEnabledButton = new JToggleButton();
        dragEnabledButton.setName("dragEnabled");
        dragEnabledButton.setText(getBundleString("dragEnabled.text"));
        sidebar.add(dragEnabledButton);
        
        add(sidebar, BorderLayout.LINE_START);
    }
    
    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

//    /**
//     * {@inheritDoc}
//     */
//    protected void createDemo() {
//        setLayout(new BorderLayout());
//        
//        imageView = new JXImageView();
//        imageView.setName("imageView");
//        add(imageView);
//        
//        JPanel sidebar = new JPanel(new VerticalLayout());
//        openButton = new JButton();
//        openButton.setName("openButton");
//        sidebar.add(openButton);
//        
//        saveButton = new JButton();
//        saveButton.setName("saveButton");
//        sidebar.add(saveButton);
//        
//        rotateCWButton = new JButton();
//        rotateCWButton.setName("rotateCW");
//        sidebar.add(rotateCWButton);
//        
//        rotateCCWButton = new JButton();
//        rotateCCWButton.setName("rotateCCW");
//        sidebar.add(rotateCCWButton);
//        
//        zoomInButton = new JButton();
//        zoomInButton.setName("zoomIn");
//        sidebar.add(zoomInButton);
//        
//        zoomOutButton = new JButton();
//        zoomOutButton.setName("zoomOut");
//        sidebar.add(zoomOutButton);
//        
//        dragEnabledButton = new JToggleButton();
//        dragEnabledButton.setName("dragEnabled");
//        sidebar.add(dragEnabledButton);
//        
//        add(sidebar, BorderLayout.LINE_START);
//    }
    
    /**
     * {@inheritDoc}
     */
//    protected void bind() {
//        openButton.addActionListener(imageView.getOpenAction());
//        saveButton.addActionListener(imageView.getSaveAction());
//        rotateCWButton.addActionListener(imageView.getRotateClockwiseAction());
//        rotateCCWButton.addActionListener(imageView.getRotateCounterClockwiseAction());
//        zoomInButton.addActionListener(imageView.getZoomInAction());
//        zoomOutButton.addActionListener(imageView.getZoomOutAction());
//        Bindings.createAutoBinding(READ,
//                dragEnabledButton, BeanProperty.create("selected"),
//                imageView, BeanProperty.create("dragEnabled")).bind();
     //	imageView.setDragEnabled(isOptimizedDrawingEnabled()); TODO wg
//    }
}
