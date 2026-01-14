/* created from jxmapviewer sample6_mapkit
*/ 
package org.jxmapviewer.demos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.demos.svg.FeatheRflag;
import org.jdesktop.swingx.demos.svg.FeatheRmap;
import org.jdesktop.swingx.icon.ChevronIcon;
import org.jdesktop.swingx.icon.PlayIcon;
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
import org.pushingpixels.trident.api.Timeline;

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

	private static final int DEFAULT_ZOOM = 5; // OSM MAX_ZOOM is 19;
	private static final String DEFAULT_POS = "Madeira (Trail)";
	private TileFactoryInfo info;
	private JXMapKit mapKit;
    private RoutePainter routePainter = new RoutePainter(Color.RED);

    // controller:
    private JXComboBox<DisplayInfo<GeoPosition>> positionChooserCombo;
    private JCheckBox drawTileBorder;
    private JCheckBox miniMapVisible;
    private JSlider zoomSlider; // JSlider extends JComponent
    private JButton zoomOut;
    private JButton zoomIn;
    private JSlider trackSlider;
    // Animation
    private JButton animation;
    Timeline timeline;
    public void setTrackProp(float newValue) {
    	trackSlider.setValue((int)(newValue*routePainter.getTrackSize()+0.5));
    }

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
        info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapKit
        mapKit = new JXMapKit() {
            protected Icon setZoomOutIcon() {
//            	return FeatheRminus.of(SizingConstants.XS, SizingConstants.XS);
            	// use "v" instead of "-" 
            	return ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
            }
            protected Icon setZoomInIcon() {
            	RadianceIcon icon = ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
            	icon.setRotation(RadianceIcon.SOUTH);
            	return icon;
//            	return FeatheRplus.of(SizingConstants.XS, SizingConstants.XS);
            }
        };
        
        // sync zoomSlider:
        mapKit.getZoomSlider().addChangeListener(changeEvent -> {
        	if(zoomSlider!=null) zoomSlider.setValue(mapKit.getZoomSlider().getValue());
        });
        mapKit.getZoomOutButton().addChangeListener(changeEvent -> {
        	if(zoomSlider!=null) zoomSlider.setValue(mapKit.getZoomSlider().getValue());
        });
        mapKit.getZoomInButton().addChangeListener(changeEvent -> {
        	if(zoomSlider!=null) zoomSlider.setValue(mapKit.getZoomSlider().getValue());
        });
        
        mapKit.setName("mapKit");
        mapKit.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the zoom and focus to Merapi, Java - the island
        mapKit.setZoom(DEFAULT_ZOOM);
        mapKit.setAddressLocation(nameToGeoPosition.get(DEFAULT_POS));
//        mapKit.getMainMap().setRestrictOutsidePanning(true); // ???
//        mapKit.getMainMap().setHorizontalWrapped(false);

        // Add interactions / verschieben , zoomen , select
// "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
//        mapKit.getMainMap().addMouseMotionListener(new MouseMotionListener() {
        MouseInputListener mia = new PanMouseInputListener(mapKit.getMainMap());
        mapKit.addMouseListener(mia);
        mapKit.addMouseMotionListener(mia);

        mapKit.addMouseListener(new CenterMapListener(mapKit.getMainMap()));

        mapKit.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapKit.getMainMap()));

        mapKit.addKeyListener(new PanKeyListener(mapKit.getMainMap()));

        // Add painter
        SelectionAdapter sa = new SelectionAdapter(mapKit.getMainMap());
        SelectionPainter selectionPainter = new SelectionPainter(sa);
        mapKit.getMainMap().addMouseListener(sa);
        mapKit.getMainMap().addMouseMotionListener(sa);
        CompoundPainter<JXMapViewer> cp = new CompoundPainter<JXMapViewer>();
        cp.setCacheable(false);
        cp.setPainters(addressLocationPainter, selectionPainter, routePainter);
        addressLocationPainter.setRenderer(new DefaultWaypointRenderer(4*SizingConstants.M/SizingConstants.M, SizingConstants.M
        		, FeatheRflag.of(SizingConstants.M, SizingConstants.M)));
        mapKit.getMainMap().setOverlayPainter(cp);

        LOG.info("isAddressLocationShown():"+mapKit.isAddressLocationShown());
        
        add(mapKit);
        
        mapKit.getMainMap().addPropertyChangeListener("zoom", pce -> {
        	getPosAndZoom();
        });
        mapKit.getMainMap().addPropertyChangeListener("center", pce -> {
        	GeoPosition pos = getPosAndZoom();
        	mapKit.setCenterPosition(pos);
        });
        getPosAndZoom();
        
        createAnimation(4500, 0.5f); // 4,5sec , stop at 100% 
    }
 
    public void createAnimation(long duration, float to) {
    	Timeline.builder(this)
			.addPropertyToInterpolate("trackProp", 0.0f, to)
			.setDuration(duration)
			.play(); // show track animated
    	
    	timeline = Timeline.builder(this)
			.addPropertyToInterpolate("trackProp", 0.0f, 1.0f)
			.setDuration(duration)
			.build();
    	LOG.info("Animation Duration = " + timeline.getDuration());
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

		JXLabel selectLabel = new JXLabel("select another location:");
		selectLabel.setName("selectLabel");
		selectLabel.setText(getBundleString("selectLabel.text"));
		selectLabel.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		controls.add(selectLabel);

        // Create the combo chooser box:
		positionChooserCombo = new JXComboBox<DisplayInfo<GeoPosition>>();
		positionChooserCombo.setName("positionChooserCombo");
		positionChooserCombo.setModel(createCBM());
		positionChooserCombo.setComboBoxIcon(FeatheRmap.of(SizingConstants.M, SizingConstants.M));
		
		positionChooserCombo.addActionListener(ae -> {
			int index = positionChooserCombo.getSelectedIndex();
			Object o = positionChooserCombo.getSelectedItem();
			if (o instanceof DisplayInfo<?> item) {
				LOG.info("positionChooserCombo.SelectedItem=" + item.getDescription());
				Object v = item.getValue();
				if (v instanceof GeoPosition geoPos) {
					mapKit.setAddressLocation(geoPos);
				}
			}
			mapKit.setZoom(DEFAULT_ZOOM);
	        zoomSlider.setValue(DEFAULT_ZOOM);
			positionChooserCombo.setSelectedIndex(index);
		});
		controls.add(positionChooserCombo);
		selectLabel.setLabelFor(positionChooserCombo);
		controls.add(Box.createRigidArea(VGAP15));

        drawTileBorder = new JCheckBox(); // JCheckBox extends JToggleButton, JToggleButton extends AbstractButton
        drawTileBorder.setSelected(true);
        mapKit.getMainMap().setDrawTileBorders(drawTileBorder.isSelected());
        drawTileBorder.setName("drawTileBorder");
        drawTileBorder.setText(getBundleString("drawTileBorder.text"));
        drawTileBorder.addActionListener( ae -> {
        	mapKit.getMainMap().setDrawTileBorders(drawTileBorder.isSelected());
        });
        controls.add(drawTileBorder);

        miniMapVisible = new JCheckBox(); // JCheckBox extends JToggleButton, JToggleButton extends AbstractButton
        miniMapVisible.setSelected(true);       
        mapKit.setMiniMapVisible(miniMapVisible.isSelected());
        miniMapVisible.setName("miniMapVisible");
        miniMapVisible.setText(getBundleString("miniMapVisible.text"));
        miniMapVisible.addActionListener( ae -> {
            mapKit.setMiniMapVisible(miniMapVisible.isSelected());
        });
        controls.add(miniMapVisible);

        // to fill up the remaining space
		JPanel fill = new JPanel();
		fill.setOpaque(false);
		fill.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
		fill.add(makeZoomSlider(), gridBagConstraints);		
		
		gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        zoomOut = new JButton();
        zoomOut.setName("zoomOut");
        zoomOut.setText(getBundleString("zoomOut.text"));
        zoomOut.setIcon(ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS));
        zoomOut.addActionListener( ae -> {
	    	mapKit.setZoom(zoomSlider.getValue()+1);
		    zoomSlider.setValue(mapKit.getZoomSlider().getValue());
        });
    	fill.add(zoomOut, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    	zoomIn = new JButton();
    	zoomIn.setName("zoomIn");
    	zoomIn.setText(getBundleString("zoomIn.text"));
    	zoomIn.setHorizontalTextPosition(SwingConstants.LEFT);
    	RadianceIcon icon = ChevronIcon.of(RadianceIcon.XS, RadianceIcon.XS);
    	icon.setRotation(RadianceIcon.SOUTH);
    	zoomIn.setIcon(icon);
    	zoomIn.addActionListener( ae -> {
	    	mapKit.setZoom(zoomSlider.getValue()-1);
		    zoomSlider.setValue(mapKit.getZoomSlider().getValue());
        });
    	fill.add(zoomIn, gridBagConstraints);

		controls.add(fill, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
		JXLabel trackLabel = new JXLabel("show track:");
		trackLabel.setName("trackLabel");
		trackLabel.setText(getBundleString("trackLabel.text"));
		fill.add(trackLabel, gridBagConstraints);		

		gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
		fill.add(makeTrackSlider(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        animation = new JButton();
        animation.setName("animation");
        animation.setText(getBundleString("animation.text"));
        animation.setIcon(PlayIcon.of(RadianceIcon.XS, RadianceIcon.XS));
        animation.addActionListener( ae -> {
        	setTrackProp(0f);
        	timeline.play(); // show track animated
        });
    	fill.add(animation, gridBagConstraints);

		return controls;
	}

    private JComponent makeTrackSlider() {  	
    	if(trackSlider!=null) {
    		LOG.warning("already instantiated "+trackSlider);
    		return trackSlider;
    	}
    	trackSlider = new JSlider();
    	trackSlider.setName("trackSlider");
    	trackSlider.setOpaque(false);
	    //zoomSlider.setPaintLabels(true);
    	trackSlider.setMinimum(0);
    	trackSlider.setMaximum(routePainter.getTrackSize());
    	trackSlider.setValue(trackSlider.getMaximum()/2);
    	trackSlider.addChangeListener(changeEvent -> {
    		routePainter.setMaxSize(trackSlider.getValue());
    		repaint();
	    });
    	trackSlider.setPaintTicks(false);
//    	trackSlider.setMajorTickSpacing(1);
		return trackSlider;
    }

    private JComponent makeZoomSlider() {  	
    	if(zoomSlider!=null) {
    		LOG.warning("already instantiated "+zoomSlider);
    		return zoomSlider;
    	}
    	// HORIZONTAL
	    zoomSlider = new JSlider();
	    zoomSlider.setName("zoomSlider");
	    zoomSlider.setOpaque(false);
	    zoomSlider.setMinimum(info.getMinimumZoomLevel());
	    zoomSlider.setMaximum(info.getMaximumZoomLevel());
	    zoomSlider.setValue(mapKit.getZoomSlider().getValue());
	    zoomSlider.addChangeListener(changeEvent -> {
	    	mapKit.setZoom(zoomSlider.getValue());
	    });
	    zoomSlider.setPaintTicks(true);
	    zoomSlider.setMajorTickSpacing(1);
		return zoomSlider;
    }
    
    private ComboBoxModel<DisplayInfo<GeoPosition>> createCBM() {
        MutableComboBoxModel<DisplayInfo<GeoPosition>> model = new DefaultComboBoxModel<DisplayInfo<GeoPosition>>();
        nameToGeoPosition.forEach((k,v) -> {
        	model.addElement(new DisplayInfo<GeoPosition>(k, v));
        });
        return model;
    }

    private static final Map<String, GeoPosition> nameToGeoPosition = new HashMap<>(){
        {
            put("Berlin",            new GeoPosition(52,31,0, 13,24,0));
            put("Darmstadt",         new GeoPosition(49,52,0,  8,39,0));
            put("Frankfurt am Main", new GeoPosition(50.11, 8.68));
            put("Java, Mt.Merapi",   new GeoPosition(-7.541389, 110.446111));
            put("Eugene Oregon",     new GeoPosition(44.058333, -123.068611));
            put("London",            new GeoPosition(51.5, 0));
            put(DEFAULT_POS,         new GeoPosition(32.81, -17.141)); // default with track
        }
    };

}
