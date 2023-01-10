/* created from jxmapviewer sample1_basics
*/ 
package org.jdesktop.jxmapviewer.demos;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.jxmapviewer.JXMapViewer;
import org.jdesktop.jxmapviewer.OSMTileFactoryInfo;
import org.jdesktop.jxmapviewer.viewer.DefaultTileFactory;
import org.jdesktop.jxmapviewer.viewer.GeoPosition;
import org.jdesktop.jxmapviewer.viewer.TileFactoryInfo;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXMapViewer}.
 *
 * @author Martin Steiger
 * @author EUG https://github.com/homebeaver (integrate to SwingSet3)
 */
public class MapViewerDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -4946197162374262488L;
	private static final Logger LOG = Logger.getLogger(MapViewerDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXMapViewer, a simple application that shows a map of Europe";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			// no controller
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			AbstractDemo demo = new MapViewerDemo(frame);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	});
    }

    private JXMapViewer mapViewer;
	
    /**
     * Demo Constructor
     * 
     * @param frame controller Frame
     */
    public MapViewerDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	
        mapViewer = new JXMapViewer();
        mapViewer.setName("mapViewer");
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);
        
        add(mapViewer);
    }
    
    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

}
