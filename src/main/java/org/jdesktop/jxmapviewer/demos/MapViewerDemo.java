/* created from jxmapviewer sample1_basics
*/ 
package org.jdesktop.jxmapviewer.demos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.jxmapviewer.JXMapViewer;
import org.jdesktop.jxmapviewer.OSMTileFactoryInfo;
import org.jdesktop.jxmapviewer.viewer.DefaultTileFactory;
import org.jdesktop.jxmapviewer.viewer.GeoPosition;
import org.jdesktop.jxmapviewer.viewer.TileFactoryInfo;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.DisplayInfo;

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
	private static final String DESCRIPTION = "Demonstrates JXMapViewer, a simple application that shows a map of Europe and the World";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new MapViewerDemo(controller);
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

    private JXMapViewer mapViewer;

    // controller:
    private JComboBox<DisplayInfo<GeoPosition>> positionChooserCombo;

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

        // Set the zoom and focus to Java - the island
        mapViewer.setZoom(9); // OSM MAX_ZOOM is 19
        mapViewer.setAddressLocation(nameToGeoPosition.get("Java"));
        
        add(mapViewer);
    }
    
    @Override
	public JXPanel getControlPane() {
		JXPanel controls = new JXPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
			}
		};
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.add(Box.createRigidArea(VGAP15));

		JXLabel selectLabel = new JXLabel("select another location:");
		selectLabel.setName("selectLabel");
		selectLabel.setText(getBundleString("selectLabel.text"));
		selectLabel.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		controls.add(selectLabel);

        // Create the combo chooser box:
		positionChooserCombo = new JComboBox<DisplayInfo<GeoPosition>>();
		positionChooserCombo.setName("positionChooserCombo");
		positionChooserCombo.setModel(createCBM());
//        ComboBoxRenderer renderer = new ComboBoxRenderer(); wie in MirroringIconDemo mit Flagge TODO
		
		positionChooserCombo.addActionListener(ae -> {
			int index = positionChooserCombo.getSelectedIndex();
			DisplayInfo<GeoPosition> item = (DisplayInfo<GeoPosition>)positionChooserCombo.getSelectedItem();
			LOG.info("Combo.SelectedItem=" + item.getDescription());
			mapViewer.setAddressLocation(item.getValue());
			positionChooserCombo.setSelectedIndex(index);
		});
		controls.add(positionChooserCombo);
		selectLabel.setLabelFor(positionChooserCombo);

        // Fill up the remaining space
		controls.add(new JPanel(new BorderLayout()));

		return controls;
	}

    private ComboBoxModel<DisplayInfo<GeoPosition>> createCBM() {
        MutableComboBoxModel<DisplayInfo<GeoPosition>> model = new DefaultComboBoxModel<DisplayInfo<GeoPosition>>();
        nameToGeoPosition.forEach((k,v) -> {
        	model.addElement(new DisplayInfo<GeoPosition>(k, v));
        });
        return model;
    }

    @SuppressWarnings("serial")
	private static final Map<String, GeoPosition> nameToGeoPosition = new HashMap<>(){
        {
            put("Frankfurt",            new GeoPosition(50.11, 8.68));
//            GeoPosition frankfurt = new GeoPosition(50,  7, 0, 8, 41, 0);
//            GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
//            GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
//            GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
//            GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);
            put("Java",                 new GeoPosition(-7.502778, 111.263056)); // default
            put("Darmstadt",            new GeoPosition(49, 52, 0, 8, 39, 0));
        }
    };

}
