/* created from jxmapviewer sample6_mapkit
*/ 
package org.jxmapviewer.demos;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Frame;
import java.awt.Frame;
import java.awt.Frame;
import java.awt.Frame;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.demos.svg.FeatheRactivity;
import org.jdesktop.swingx.demos.svg.FeatheRflag;
import org.jdesktop.swingx.demos.svg.FeatheRmap_pin;
import org.jdesktop.swingx.demos.svg.FeatheRminus;
import org.jdesktop.swingx.demos.svg.FeatheRplus;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jxmapviewer.JXMapKit;
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
 * A demo for the {@code JXMapKit}.
 *
 * @author Martin Steiger
 * @author EUG https://github.com/homebeaver (integrate to SwingSet3)
 */
public class MapKitDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 1811042967620854708L;
	private static final Logger LOG = Logger.getLogger(MapKitDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXMapKit that shows a map with zoom slider and a mini-map";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new MapKitDemo(controller);
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

	private static final int DEFAULT_ZOOM = 9; // OSM MAX_ZOOM is 19;
	private static final String DEFAULT_POS = "Java, Mt.Merapi";
	private TileFactoryInfo info;
	private JXMapKit mapKit;
//    private JXMapViewer mapViewer;

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
    public MapKitDemo(Frame frame) {
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

        // Setup JXMapKit
        mapKit = new JXMapKit() {
            protected Icon setZoomOutIcon() {
//            	return FeatheRminus.of(SizingConstants.XS, SizingConstants.XS);
            	return ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
            }
            protected Icon setZoomInIcon() {
            	RadianceIcon icon = ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
            	icon.setRotation(RadianceIcon.SOUTH);
            	return icon;
//            	return FeatheRplus.of(SizingConstants.XS, SizingConstants.XS);
            }
        };
        mapKit.setName("mapKit");
        mapKit.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the zoom and focus to Java - the island
        mapKit.setZoom(DEFAULT_ZOOM);
        mapKit.setAddressLocation(nameToGeoPosition.get(DEFAULT_POS));
//        mapKit.getMainMap().setDrawTileBorders(true); TODO controler
//        mapKit.getMainMap().setRestrictOutsidePanning(true); // ???
        mapKit.getMainMap().setHorizontalWrapped(false);

        // Add interactions / verschieben , zoomen , select
// "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
//        mapKit.getMainMap().addMouseMotionListener(new MouseMotionListener() {
        MouseInputListener mia = new PanMouseInputListener(mapKit.getMainMap());
        mapKit.addMouseListener(mia);
        mapKit.addMouseMotionListener(mia);

        mapKit.addMouseListener(new CenterMapListener(mapKit.getMainMap()));

        mapKit.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapKit.getMainMap()));

        mapKit.addKeyListener(new PanKeyListener(mapKit.getMainMap()));

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapKit.getMainMap());
        SelectionPainter sp = new SelectionPainter(sa);
        mapKit.addMouseListener(sa);
        mapKit.addMouseMotionListener(sa);
        CompoundPainter<JXMapViewer> cp = new CompoundPainter<JXMapViewer>();
        cp.setCacheable(false);
        cp.setPainters(addressLocationPainter, sp);
        // TODO flag is not exactly at Pos location, because the flag pole is not at icon.getIconWidth() / 2
        // is: <line x1="4" y1="22" x2="4" y2="15" />, expected icon.getIconWidth() / 2 = 12 but is 4
        addressLocationPainter.setRenderer(new DefaultWaypointRenderer(FeatheRflag.of(SizingConstants.M, SizingConstants.M)));
        mapKit.getMainMap().setOverlayPainter(cp);

        LOG.info("isAddressLocationShown():"+mapKit.isAddressLocationShown());
        
        add(mapKit);
        
        mapKit.addPropertyChangeListener("zoom", pce -> {
        	LOG.info("---------------------pce:"+pce);
        	getPosAndZoom();
        });
        mapKit.addPropertyChangeListener("center", pce -> {
        	GeoPosition pos = getPosAndZoom();
        	mapKit.setCenterPosition(pos);
        });
        getPosAndZoom();
    }
 
    // from JXMapKit
	private WaypointPainter<Waypoint> addressLocationPainter = new WaypointPainter<Waypoint>() {
		@Override
		public Set<Waypoint> getWaypoints() {
			Set<Waypoint> set = new HashSet<Waypoint>();
			if (mapKit.getMainMap().getAddressLocation() != null) {
				set.add(new DefaultWaypoint(mapKit.getMainMap().getAddressLocation()));
			} else {
				set.add(new DefaultWaypoint(0, 0));
			}
			return set;
		}
	};

    private GeoPosition getPosAndZoom() {
        double lat = mapKit.getCenterPosition().getLatitude();
        double lon = mapKit.getCenterPosition().getLongitude();
        int zoom = mapKit.getZoomSlider().getValue();
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
//		controls.add(Box.createRigidArea(VGAP15));
//    	JXPanel controls = new JXPanel(new VerticalLayout());

		JXLabel selectLabel = new JXLabel("select another location:");
		selectLabel.setName("selectLabel");
		selectLabel.setText(getBundleString("selectLabel.text"));
		selectLabel.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		controls.add(selectLabel);

        // Create the combo chooser box:
		positionChooserCombo = new JComboBox<DisplayInfo<GeoPosition>>();
		positionChooserCombo.setName("positionChooserCombo");
		positionChooserCombo.setModel(createCBM());
//		positionChooserCombo.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
//        ComboBoxRenderer renderer = new ComboBoxRenderer(); wie in MirroringIconDemo mit Flagge TODO
		
		positionChooserCombo.addActionListener(ae -> {
			int index = positionChooserCombo.getSelectedIndex();
			DisplayInfo<GeoPosition> item = (DisplayInfo<GeoPosition>)positionChooserCombo.getSelectedItem();
			LOG.info("Combo.SelectedItem=" + item.getDescription());
			mapKit.setAddressLocation(item.getValue());
			mapKit.setZoom(DEFAULT_ZOOM);
	        zoomSlider.setValue(DEFAULT_ZOOM);
			positionChooserCombo.setSelectedIndex(index);
		});
		controls.add(positionChooserCombo);
		selectLabel.setLabelFor(positionChooserCombo);
		controls.add(Box.createRigidArea(VGAP15));

//	    LOG.info("min/max/zoom:"+info.getMinimumZoomLevel()+" "+info.getMaximumZoomLevel()+" "+mapViewer.getZoom());
//	    zoomSlider = new JSlider(JSlider.HORIZONTAL, info.getMinimumZoomLevel(), info.getMaximumZoomLevel(), mapKit.getZoomSlider().getValue());
	    zoomSlider = new JSlider();
	    zoomSlider.setMinimum(info.getMinimumZoomLevel());
	    zoomSlider.setMaximum(info.getMaximumZoomLevel());
	    zoomSlider.setValue(mapKit.getZoomSlider().getValue());
	    zoomSlider.addChangeListener(changeEvent -> {
	    	//LOG.info(""+zoomSlider.getValue());
	    	mapKit.setZoom(zoomSlider.getValue());
	    });
	    zoomSlider.setPaintTicks(true);
	    zoomSlider.setMajorTickSpacing(1);
//		controls.add(zoomSlider);
//	    JPanel zoomPanel = new JPanel(new BorderLayout());
//	    zoomPanel.add(zoomSlider, BorderLayout.CENTER);
//		controls.add(zoomPanel);

	    
//        Dictionary<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
        // can we fill these labels from the properties file? Yes, we can! but I do not
//        String labelTable = getBundleString(SLIDER+".labelTable");
//        labels.put(zoomSlider.getMinimum(), new JLabel("zoom in"));
//        labels.put(zoomSlider.getMaximum(), new JLabel("zoom out"));
//        zoomSlider.setLabelTable(labels);

        // to fill up the remaining space
		JPanel fill = new JPanel();
		fill.setOpaque(false);
		fill.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
//        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
		fill.add(zoomSlider, gridBagConstraints);		
		
		gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
//        gridBagConstraints.weightx = 1.0;
//        gridBagConstraints.weighty = 1.0;
//        jPanel1.add(zoomInButton, gridBagConstraints); // plus
//		fill.add(new JLabel(getBundleString("zoomOut.text")), BorderLayout.NORTH);
        fill.add(new JLabel(getBundleString("zoomOut.text")), gridBagConstraints);

////        zoomOutButton.setAction(getZoomInAction());
////        zoomOutButton.setIcon(new ImageIcon(JXMapKit.class.getResource("images/minus.png")));
////        zoomOutButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
////        zoomOutButton.setMaximumSize(new java.awt.Dimension(20, 20));
////        zoomOutButton.setMinimumSize(new java.awt.Dimension(20, 20));
////        zoomOutButton.setOpaque(false);
////        zoomOutButton.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
//        gridBagConstraints.weighty = 1.0;
//        jPanel1.add(zoomOutButton, gridBagConstraints);
        fill.add(new JLabel(getBundleString("zoomIn.text")), gridBagConstraints);
//
////		controls.add(fill);
//		gridBagConstraints = new GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.anchor = GridBagConstraints.SOUTHWEST;
//        gridBagConstraints.weightx = 1.0;
//        gridBagConstraints.weighty = 1.0;
//        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
////        mainMap.add(jPanel1, gridBagConstraints);
		controls.add(fill, gridBagConstraints);


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
            put("Berlin",            new GeoPosition(52,31,0, 13,24,0));
            put("Darmstadt",         new GeoPosition(49,52,0,  8,39,0));
            put("Frankfurt am Main", new GeoPosition(50.11, 8.68));
            put(DEFAULT_POS,         new GeoPosition(-7.541389, 110.446111)); // default Java, Merapi
            put("Eugene Oregon",     new GeoPosition(44,3,0, -123,5,0));
            put("London",            new GeoPosition(51.5, 0));
        }
    };

}
