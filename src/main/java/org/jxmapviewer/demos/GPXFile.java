package org.jxmapviewer.demos;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import me.himanshusoni.gpxparser.GPXParser;
import me.himanshusoni.gpxparser.modal.GPX;
import me.himanshusoni.gpxparser.modal.Metadata;
import me.himanshusoni.gpxparser.modal.Route;
import me.himanshusoni.gpxparser.modal.Track;
import me.himanshusoni.gpxparser.modal.TrackSegment;
import me.himanshusoni.gpxparser.modal.Waypoint;
import swingset.StaticUtilities;

/*

in https://github.com/osmandapp/OsmAnd gibt es eine vollständige
public class net.osmand.gpx.GPXFile extends GPXUtilities.GPXExtensions

hier nur rudimentär
 */
public class GPXFile {

	private static final Logger LOG = Logger.getLogger(GPXFile.class.getName());

	private GPX gpx;
	
// in OsmAnd:	public GPXUtilities.Metadata metadata = new GPXUtilities.Metadata();	
	private HashSet<Route> routes;
	private HashSet<Track> tracks;
	private HashSet<Waypoint> waypoints;
	private HashMap<String, Object> extData;

	/**
	 * 
	 * @param gpxFileName example "resources/Mittelweg.gpx"
	 */
	public GPXFile(String gpxFileName) {
		this(StaticUtilities.getResourceAsStream(GPXFile.class, gpxFileName));
	}
	public GPXFile(InputStream is) {
        GPXParser p = new GPXParser();
        p.addExtensionParser(new OsmExtensionParser());
		try {
			gpx = p.parseGPX(is); // throws Exception
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Metadata getMetadata() {
		return gpx.getMetadata();
	}

	private HashMap<String, Object> getExtensionData() {
		extData = gpx.getExtensionData();
		if(extData!=null) extData.forEach((k,v) -> {
			if(v instanceof OsmExtensionHolder oeh) {
				LOG.info(k + "=> nodes#="+ oeh.getNodeList().getLength());
			}
		});
		return extData;
	}
	
	/**
	 * in GPX there are routes, tracks and waypoints.
	 * This method extracts all waypoints in all tracks.
	 * @return all Waypoints in all trackSegments
	 */
	/*

in package org.jxmapviewer.viewer ist Waypoint ein interface mit
public GeoPosition getPosition();
und GeoPosition besteht aus (zweidimensional) latitude + longitude (ohne elv)
public GeoPosition(double latitude, double longitude) {

	 */
	public ArrayList<Waypoint> getTrackWaypoints() {
		if (tracks == null)
			tracks = gpx.getTracks();
		LOG.info((getMetadata()==null ? "" : getMetadata().getName() + " : ")
			+ "routes:" + routes + " tracks:" + tracks + ",size=" + tracks.size() 
			+ " waypoints:" + waypoints + " extData:" + extData);
		ArrayList<Waypoint> allWpts = new ArrayList<Waypoint>();
		for (Track track : tracks) {
			LOG.info("track.Name:" + track.getName());
			List<TrackSegment> trackSegments = track.getTrackSegments(); // ArrayList
			for (TrackSegment segment : trackSegments) {
				ArrayList<Waypoint> wpts = segment.getWaypoints();
				LOG.info("segment:" + segment + " with "+wpts.size()+" waypoints.");
				allWpts.addAll(wpts);
			}

		}
		return allWpts;
	}
	
	public static void main(String[] args) {
		GPXFile gpxf = new GPXFile( //"resources/Mad_17_BalcoesRibeiroFrio.gpx"); 
//				"resources/Mittelweg.gpx");
				"resources/Odenwald-Weg.gpx");
//				"resources/westweg-etappe1.gpx");
		ArrayList<Waypoint> wpts = gpxf.getTrackWaypoints();
		for (Waypoint wp : wpts) {
			LOG.fine("wp:" + wp);
		}
		
		gpxf.getExtensionData();
	}
}
