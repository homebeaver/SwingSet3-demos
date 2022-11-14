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
import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.icon.RadianceIcon;

import swingset.AbstractDemo;

/**
 * Demonstrates how one icon class is used to render different icons.
 * <p>
 * The Class <code>IconRarrow</code> was generated from arrow-up.svg file with Radiance SVG transcoder.
 * In CENTER you see the original svg file.
 * The icons around are rendered by rotating.
 * 
 * @author homeb
 *
 */
/*
 * - Quelle: arrow-up.svg file
 * - der Generator kann (noch) kein rotate generieren
 * - daher das file umbenannt IconRarrow_up.java in IconRarrow.java und rotate manuell reinkodiert
 * - auch in Radiance interface RadianceIcon ist rotate und direction nicht vorgesehen
 * - feature 407 bei kirill vorgeschlagen 
 * - kirill will den PR nicht haben, also mache ich einen branch
 */
public class RotatingIconDemo extends AbstractDemo {

	private static final long serialVersionUID = -1196350478441930803L;
	private static final Logger LOG = Logger.getLogger(RotatingIconDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates how one icon class is used to render different icons.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new RotatingIconDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    private JTextArea textArea;

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public RotatingIconDemo(Frame frame) {
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
    	add(new JScrollPane(textArea));
    	
//    	String iconName = "arrow";
    	String iconName = "arrowInCircle";
//    	String iconName = "chevron";
//    	String iconName = "chevrons";

        JPanel north = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
        add(north, BorderLayout.NORTH);
        north.add(createButton(iconName, SizingConstants.NORTH_WEST));
        north.add(createButton(iconName, SizingConstants.NORTH));
        north.add(createButton(iconName, SizingConstants.NORTH_EAST));
        
        JPanel south = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
        add(south, BorderLayout.SOUTH);
    	south.add(createButton(iconName, SizingConstants.SOUTH_WEST));
    	south.add(createButton(iconName, SizingConstants.SOUTH));
    	south.add(createButton(iconName, SizingConstants.SOUTH_EAST));

    	add(createButton(iconName, SizingConstants.EAST), BorderLayout.EAST);
    	add(createButton(iconName, SizingConstants.WEST), BorderLayout.WEST);
    	
//        InputStream in = getClass().getResourceAsStream("resources/arrow-up.svg");
        InputStream in = getClass().getResourceAsStream("resources/"+iconName+".svg");
//        InputStream in = getClass().getResourceAsStream("resources/chevron-up.svg");
//    	InputStream in = getClass().getResourceAsStream("resources/chevrons-up.svg");
        try {
        	LOG.info("read svg file");
            textArea.read(new InputStreamReader(in), null); // NPE if no resources
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private JComponent createButton(String iconName, int direction) {
    	RadianceIcon icon = ArrowIcon.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
//    	RadianceIcon icon = IconRarrowInCircle.of(SizingConstants.LAUNCHER_ICON, SizingConstants.LAUNCHER_ICON);
//    	RadianceIcon icon = IconRchevron.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
//    	RadianceIcon icon = IconRchevrons.of(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
    	icon.setRotation(direction);
//    	LOG.info("direction="+direction +"  >>>---------------icon.getRotation():"+icon.getRotation());
    	String orientation = "?";
		switch (direction) {
		case SwingConstants.NORTH: // 1
			//orientation = "N";
            break;
		case SwingConstants.NORTH_EAST:
			orientation = "45° rotation (NE)";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.EAST:
			orientation = "90° (E)";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH_EAST:
			orientation = "135° rotation (SE)";
	    	icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH: // 5
			orientation = "180° rotation (S)";
            break;
        case SwingConstants.SOUTH_WEST:
			orientation = "rotation to SW";
	    	icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.WEST:
			orientation = "to W";
	    	icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.NORTH_WEST:
			orientation = "rotation to NW";
	    	icon.setColorFilter(color -> Color.blue);
            break;
		default: { /* no xform */ }
		}
		String text = "?".equals(orientation) ? iconName : orientation;
    	return new JButton(text, icon);
    }
    
    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}


}
