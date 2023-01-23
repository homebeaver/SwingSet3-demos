/* created from jxmapviewer sample1_basics + sample3_interaction
*/ 
package org.jxmapviewer.demos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.demos.svg.FeatheRmap_pin;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

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

	private static final int DEFAULT_ZOOM = 13; // OSM MAX_ZOOM is 19;
	private TileFactoryInfo info;
    private JXMapViewer mapViewer;

    // controller:
    private JComboBox<DisplayInfo<GeoPosition>> positionChooserCombo;
    private JSlider zoomSlider;
    // controller prop name
//	private static final String SLIDER = "zoomSlider";

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

        // Create a TileFactoryInfo for OpenStreetMap
        info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        mapViewer = new JXMapViewer();
        mapViewer.setName("mapViewer");
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the zoom and focus to Java - the island
        mapViewer.setZoom(DEFAULT_ZOOM);
        mapViewer.setAddressLocation(nameToGeoPosition.get("Java"));

        // Add interactions / verschieben , zoomen , select
// "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter selectionPainter = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        CompoundPainter<JXMapViewer> cp = new CompoundPainter<JXMapViewer>();
        cp.setCacheable(false);
        cp.setPainters(addressLocationPainter, selectionPainter);
        addressLocationPainter.setRenderer(new DefaultWaypointRenderer(FeatheRmap_pin.of(SizingConstants.M, SizingConstants.M)));
        mapViewer.setOverlayPainter(cp);

        add(mapViewer);
        
        mapViewer.addPropertyChangeListener("zoom", pce -> {
        	LOG.info("---------------------pce:"+pce);
        	getPosAndZoom();
        });
        mapViewer.addPropertyChangeListener("center", pce -> {
        	GeoPosition pos = getPosAndZoom();
        	mapViewer.setCenterPosition(pos);
        });
        getPosAndZoom();
    }

    // from JXMapKit
	private WaypointPainter<Waypoint> addressLocationPainter = new WaypointPainter<Waypoint>() {
		@Override
		public Set<Waypoint> getWaypoints() {
			Set<Waypoint> set = new HashSet<Waypoint>();
			if (mapViewer.getAddressLocation() != null) {
				set.add(new DefaultWaypoint(mapViewer.getAddressLocation()));
			} else {
				set.add(new DefaultWaypoint(0, 0));
			}
			return set;
		}
	};

    private GeoPosition getPosAndZoom() {
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();
        if(zoomSlider!=null) zoomSlider.setValue(zoom);

        LOG.info(String.format("Lat/Lon=(%.2f / %.2f) - Zoom: %d", lat, lon, zoom));
        return new GeoPosition(lat, lon);
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
		positionChooserCombo.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
//        ComboBoxRenderer renderer = new ComboBoxRenderer(); wie in MirroringIconDemo mit Flagge TODO
		
		positionChooserCombo.addActionListener(ae -> {
			int index = positionChooserCombo.getSelectedIndex();
			DisplayInfo<GeoPosition> item = (DisplayInfo<GeoPosition>)positionChooserCombo.getSelectedItem();
			LOG.info("Combo.SelectedItem=" + item.getDescription());
			mapViewer.setAddressLocation(item.getValue());
	        mapViewer.setZoom(DEFAULT_ZOOM);
	        zoomSlider.setValue(DEFAULT_ZOOM);
			positionChooserCombo.setSelectedIndex(index);
		});
		controls.add(positionChooserCombo);
		selectLabel.setLabelFor(positionChooserCombo);

//	    LOG.info("min/max/zoom:"+info.getMinimumZoomLevel()+" "+info.getMaximumZoomLevel()+" "+mapViewer.getZoom());
	    zoomSlider = new JSlider(JSlider.VERTICAL, info.getMinimumZoomLevel(), info.getMaximumZoomLevel(), mapViewer.getZoom());
	    zoomSlider.addChangeListener(changeEvent -> {
	    	//LOG.info(""+zoomSlider.getValue());
	    	mapViewer.setZoom(zoomSlider.getValue());
	    });
	    zoomSlider.setPaintTicks(true);
	    zoomSlider.setMajorTickSpacing(1);
//        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        // can we fill these labels from the properties file? Yes, we can! but I do not
//        String labelTable = getBundleString(SLIDER+".labelTable");
//        labels.put(zoomSlider.getMinimum(), new JLabel("zoom in"));
//        labels.put(zoomSlider.getMaximum(), new JLabel("zoom out"));
//        zoomSlider.setLabelTable(labels);

        // to fill up the remaining space
		JPanel fill = new JPanel(new BorderLayout());
		fill.add(new JLabel(getBundleString("zoomOut.text")), BorderLayout.NORTH);		
		fill.add(zoomSlider, BorderLayout.WEST);		
		fill.add(new JLabel(getBundleString("zoomIn.text")), BorderLayout.SOUTH);		
		controls.add(fill);

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
            put("Berlin",               new GeoPosition(52,31,0, 13,24,0));
            put("Darmstadt",            new GeoPosition(49,52,0,  8,39,0));
            put("Frankfurt am Main",    new GeoPosition(50.11, 8.68));
//            GeoPosition frankfurt = new GeoPosition(50,  7, 0, 8, 41, 0);
//            GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
//            GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
//            GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);
            put("Java",                 new GeoPosition(-7.502778, 111.263056)); // default
            put("Eugene Oregon",        new GeoPosition(44,3,0, -123,5,0));
            put("London",               new GeoPosition(51.5, 0));
        }
    };

}
