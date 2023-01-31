package org.jxmapviewer.demos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

public class DemoMapLocation {

	private String name;
	private GeoPosition addressLocation;
	private int zoom;
	private List<RoutePainter> routePainter;
	
	// getter:
	GeoPosition getAddressLocation() { return addressLocation; }
	int getZoom() { return zoom; }
	List<RoutePainter> getRoutePainter() { return routePainter; }
	
	DemoMapLocation(String name, GeoPosition geoPosition, int zoom) {
		this(name, geoPosition, zoom, null);
	}
	DemoMapLocation(String name, GeoPosition geoPosition, int zoom, RoutePainter routePainter) {
		this.name = name;
		this.addressLocation = geoPosition;
		this.zoom = zoom;
		this.routePainter = new ArrayList<RoutePainter>();
		addRoutePainter(routePainter);
	}
	DemoMapLocation(String name, GPXFile gpxf, int zoom) {
		this.name = name;
		this.zoom = zoom;
		int size = gpxf.getTracksSize();
		this.routePainter = new ArrayList<RoutePainter>();
		for(int i=0; i<size; i++) {
			List<GeoPosition> track = new ArrayList<GeoPosition>();
			gpxf.getTrackWaypoints(i).forEach(wp -> {
				track.add(new GeoPosition(wp.getLatitude(), wp.getLongitude()));
			});
			addRoutePainter(new RoutePainter(Color.RED, track));
			if(addressLocation==null) addressLocation = track.get(0);
		}	
	}

	public void addRoutePainter(RoutePainter rp) {
		routePainter.add(rp);
	}
	
	public String toString() {
		return this.name;
	}
}
