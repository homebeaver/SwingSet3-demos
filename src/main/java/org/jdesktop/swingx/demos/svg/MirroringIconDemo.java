package org.jdesktop.swingx.demos.svg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.SizingConstants;
import org.pushingpixels.radiance.common.api.icon.RadianceIcon;

import swingset.AbstractDemo;

/**
 * Demonstrates how one icon class is used to render different icons.
 * <p>
 * The Class <code>IconRfeather</code> was generated from feather.svg file with Radiance SVG transcoder.
 * In CENTER you see the original svg file.
 * The icons around are rendered by rotating resp. point/axis reflection (mirroring)
 * 
 * @author homeb
 *
 */
@SuppressWarnings("serial")
public class MirroringIconDemo extends AbstractDemo {

	private static final Logger LOG = Logger.getLogger(MirroringIconDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates how one icon class is used to render different icons.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new MirroringIconDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    private JTextArea textArea;

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public MirroringIconDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
        JPanel center = new JXPanel(new BorderLayout());
        center.add(new JScrollPane(textArea));
    	add(center);
    	
//    	String iconName = "feather";
    	String iconName = "align_left";	
    	
    	// point reflection:
        center.add(createButton(iconName, -1, true, true), BorderLayout.NORTH);

        JPanel north = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
        add(north, BorderLayout.NORTH);
        north.add(createButton(iconName, SizingConstants.NORTH_WEST));
        north.add(createButton(iconName, SizingConstants.NORTH)); // original
        north.add(createButton(iconName, SizingConstants.NORTH_EAST));
        
        JPanel south = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
        add(south, BorderLayout.SOUTH);
    	south.add(createButton(iconName, SizingConstants.SOUTH_WEST));
    	south.add(createButton(iconName, -1, true, false));
    	south.add(createButton(iconName, SizingConstants.SOUTH_EAST));

    	add(createButton(iconName, -1, false, true), BorderLayout.EAST);
    	add(createButton(iconName, -1), BorderLayout.WEST);
    	
//        InputStream in = getClass().getResourceAsStream("resources/feather.svg");
        InputStream in = getClass().getResourceAsStream("resources/align-left.svg");
        try {
        	LOG.info("read svg file");
            textArea.read(new InputStreamReader(in), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private JComponent createButton(String iconName, int direction) {
    	return createButton(iconName, direction, false, false);
    }
    private String reflectionType(boolean horizontal, boolean vertical) {
    	if(horizontal && vertical) return "point reflection";
    	if(horizontal) return "horizontal mirroring (X axis)";
    	if(vertical) return "vertical mirroring (Y axis)";
    	return "nix";
    }
    /**
     * 
     * @param iconName
     * @param direction rotation
     * @param horizontal point/axis reflection (mirroring) 
     * @param vertical point/axis reflection (mirroring) 
     * @return
     */
    private JComponent createButton(String iconName, int direction, boolean horizontal, boolean vertical) {
//    	IconRfeather icon = (IconRfeather)IconRfeather.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
    	RadianceIcon icon = IconRalign_left.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
    	icon.setRotation(direction);
    	icon.setReflection(horizontal, vertical);
//    	LOG.info("rotation direction="+direction +"  >>>---------------icon.isReflection():"+icon.isReflection());
    	String orientation = icon.isReflection() ? reflectionType(horizontal, vertical) : "?";
		switch (direction) {
		case SwingConstants.NORTH: // 1
			//orientation = "N";
            break;
		case SwingConstants.NORTH_EAST:
			orientation = "45° rotation (NE)";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.EAST:
			orientation = "E";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH_EAST:
			orientation = "135° rotation";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH: // 5
			orientation = "S";
            break;
        case SwingConstants.SOUTH_WEST:
			orientation = "rotation to SW";
	    	icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.WEST:
			orientation = "W";
	    	icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.NORTH_WEST:
			orientation = "rotation to NW";
	    	icon.setColorFilter(color -> Color.blue);
            break;
		default: { /* no xform */ }
		}
		String text = "?".equals(orientation) ? iconName : orientation;
//    	return new JButton(iconName+" "+orientation, icon);
    	return new JButton(text, icon);
    }
    
    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}


}
