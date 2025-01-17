package org.jxmapviewer.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashSet;

import org.junit.Test;
import org.jxmapviewer.demos.GPXFile;
import org.jxmapviewer.demos.RouteKey;

import me.himanshusoni.gpxparser.modal.Extension;
import me.himanshusoni.gpxparser.modal.Waypoint;
import swingset.StaticUtilities;

public class GpxParseTest {

	// Demo resource with osmand extensions
	private static final String ODENWALD = "resources/Odenwald-Weg.gpx";
	// Demo resource with two tracks
	private static final String VIAROMA = "resources/Via-di-Roma.gpx";
	// test
	// https://www.openstreetmap.org/user/fir99/traces/6110027
	private static final String FIR99_6110027 = "6110027.gpx";

	private static final double DELTA = 0.001;
	
	@Test
	public void testOsmandGPXfile() {
//		fail("Not yet implemented");
		GPXFile gpxf = new GPXFile(ODENWALD);
		/*
  <metadata>
    <name>Odenwald-Vogesen-Weg Teil 1</name>
    <time>2023-01-28T22:54:21Z</time>
  </metadata>
		 */
		String name = gpxf.getMetadata().getName();
    	assertTrue(name.startsWith("Odenwald"));
    	System.out.println("Metadata.time="+gpxf.getMetadata().getTime().toString());
    	assertTrue(gpxf.getMetadata().getTime().toString().endsWith("2023"));
    	
    	assertEquals(0, gpxf.getWaypoints().size());

    	// es gibt nur einem Track mit 4643 WPs - der Track hat keinen Namen, dieser steht in extension osmand:network_route
    	assertEquals(1, gpxf.getTracksSize());
    	assertEquals(4643, gpxf.getTrackWaypoints().size());

    	/*
    <osmand:network_route>
      <osmand:route_key type="hiking" osmc_foreground="red_bar" symbol="Roter Balken auf weißem Grund" 
      	ref="HW 7a" osmc_stub_name="." name="Odenwald-Vogesen-Weg Teil 1" 
      	osmc_waycolor="red" osmc_background="white" operator="Odenwaldklub, Club vosgien" network="nwn" />
    </osmand:network_route>
    	 */
    	Extension ext = gpxf.getOsmExtension();
		if(ext!=null) {
			ext.getExtensionData().forEach((id,data) -> {
				if(data instanceof RouteKey rk) {
					System.out.println(id + " : getRouteName=" + rk.getRouteName());
				}
				System.out.println(id + " : " + data);
			});
		}
		
		RouteKey rk = gpxf.getRouteKey();
    	assertEquals(RouteKey.RouteType.HIKING, rk.type);		
	}

	@Test
	public void testTwoTracksGPXfile() {
		GPXFile gpxf = new GPXFile(VIAROMA);
		
		// keine Metadaten
    	assertNull(gpxf.getMetadata());
    	
    	assertEquals(0, gpxf.getWaypoints().size());
    	assertEquals(2, gpxf.getTracksSize());
    	assertEquals( 779, gpxf.getTrackWaypoints(0).size());
    	assertEquals( 669, gpxf.getTrackWaypoints(1).size());
    	assertEquals(1448, gpxf.getTrackWaypoints().size());
	}

	@Test
	public void testOruxmapsGPXfile() {
		// das file ist in test/resources, daher als stream
		InputStream is = StaticUtilities.getResourceAsStream(GpxParseTest.class, FIR99_6110027);
		GPXFile gpxf = new GPXFile(is);
		
    	assertTrue(gpxf.getMetadata().getTime().toString().endsWith("2023"));
    	
		/* es gibt zwei Waypoints:
<wpt lat="47.5503637" lon="11.5668625">
<ele>1666.42</ele>
<time>2023-01-29T12:05:29.016Z</time>
<name><![CDATA[Wegweiser]]></name>
<sym>Waypoint</sym>
<type>Wegpunkt</type>
<extensions>
<om:oruxmapsextensions xmlns:om="http://www.oruxmaps.com/oruxmapsextensions/1/0">
<om:ext type="ICON" subtype="0">1</om:ext>
</om:oruxmapsextensions>
</extensions>
</wpt>
...
		 */
		HashSet<Waypoint> wps = gpxf.getWaypoints();
    	assertEquals(2, wps.size());		
		Waypoint[] wpa = wps.toArray(new Waypoint[wps.size()]);
		System.out.println(wpa[0]);
    	assertEquals(1666.42, wpa[0].getElevation(), DELTA);

    	/* es gibt nur einem Track mit 2378 WPs - der Track hat ein Datum als Name
<trk>
<name><![CDATA[2023-01-29 08:29:00]]></name>
<desc><![CDATA[<p>Typ: Unbestimmt<br/>Startzeit: 08:29 29.01.2023<br/>Zielzeit: 17:12 29.01.2023<br/>Strecke: 14,1 km (08:43)<br/>Bewegungszeit: 06:02<br/>Ø-Geschwindigkeit: 1,62 km/h<br/>Netto-Geschwindigkeit: 2,34 km/h<br/>Max. Geschwindigkeit: 7,74 km/h<br/>Minimale Höhe: 759 m<br/>Maximale Höhe: 1708 m<br/>Steig-Geschw.: 301,7 m/h<br/>Sink-Geschw.: 383,2 m/h<br/>Aufstieg: 977 m<br/>Abstieg: 991 m<br/>Steigzeit: 03:14<br/>Sinkzeit: 02:35<br/></p><hr align="center" width="480" style="height: 2px; width: 517px"/>]]></desc>
<type>Unbestimmt</type>
<extensions>
<om:oruxmapsextensions xmlns:om="http://www.oruxmaps.com/oruxmapsextensions/1/0">
<om:ext type="TYPE" subtype="0">0</om:ext>
<om:ext type="DIFFICULTY">0</om:ext>
</om:oruxmapsextensions>
</extensions>
<trkseg> ...
    	 */
    	assertEquals(1, gpxf.getTracksSize());
    	assertEquals(2378, gpxf.getTrackWaypoints().size());
    	
		assertNull(gpxf.getOsmExtension());
	}
}
