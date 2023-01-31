package org.jxmapviewer.demos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.viewer.GeoPosition;

public class DemoMapLocation {

	private String name;
	private GeoPosition addressLocation;
	private int zoom;
	private RoutePainter routePainter; // TODO mehrere in verschiedenen Tracks / Segmente, Farben
	
	// getter:
	GeoPosition getAddressLocation() { return addressLocation; }
	int getZoom() { return zoom; }
	RoutePainter getRoutePainter() { return routePainter; }
	
	DemoMapLocation(String name, GeoPosition geoPosition, int zoom) {
		this(name, geoPosition, zoom, null);
	}
	DemoMapLocation(String name, GeoPosition geoPosition, int zoom, RoutePainter routePainter) {
		this.name = name;
		this.addressLocation = geoPosition;
		this.zoom = zoom;
		this.routePainter = routePainter;
	}
	DemoMapLocation(String name, GPXFile gpxf, int zoom) {
		this.name = name;
		this.zoom = zoom;
		List<GeoPosition> track = new ArrayList<GeoPosition>();
		gpxf.getTrackWaypoints().forEach(wp -> {
			track.add(new GeoPosition(wp.getLatitude(), wp.getLongitude()));
		});
		routePainter = new RoutePainter(Color.RED, track);
		addressLocation = track.get(0);
	}

	public String toString() {
		return this.name;
	}
}
