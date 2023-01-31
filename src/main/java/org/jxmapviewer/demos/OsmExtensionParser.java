package org.jxmapviewer.demos;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import me.himanshusoni.gpxparser.GPXConstants;
import me.himanshusoni.gpxparser.extension.IExtensionParser;
import me.himanshusoni.gpxparser.modal.Extension;

public class OsmExtensionParser implements IExtensionParser {

	private static final Logger LOG = Logger.getLogger(OsmExtensionParser.class.getName());
	
	public static final String OSMAND_EXTENSIONS_PREFIX = "osmand:";

    @Override
    public String getId() {
            return "OSM Extension Parser";
    }

    /*

https://osmand.net/docs/technical/osmand-file-formats/osmand-gpx :
[show_arrows]	Bool. "true" or "false". Show / hide arrows along the path line.
[width]	String. "thin", "medium", "bold" or number 1-24. Width of the track line on the map. The thin, medium, and bold are style depended values (should be defined as currentTrackWidth attribute).
[color]	String. Hex value "#AARRGGBB" or "#RRGGBB". Color of a track line on the map.
[split_type]	String. "no_split", "distance" or "time". Split type for a track.
[split_interval]	Double. Split interval for a track. Distance (meters), time (seconds).
show_start_finish - nicht dokumentiert
smoothing_threshold
coloring_type

Bsp.:
    <osmand:show_arrows>false</osmand:show_arrows>
    <osmand:show_start_finish>true</osmand:show_start_finish>
    <osmand:split_interval>0.0</osmand:split_interval>
    <osmand:split_type>no_split</osmand:split_type>
    <osmand:color>#aaa71de1</osmand:color>
    <osmand:width>bold</osmand:width>
    <osmand:coloring_type>solid</osmand:coloring_type>
    <osmand:smoothing_threshold>0.0</osmand:smoothing_threshold>
    <osmand:min_filter_speed>0.0</osmand:min_filter_speed>
    <osmand:max_filter_speed>0.0</osmand:max_filter_speed>
    <osmand:min_filter_altitude>0.0</osmand:min_filter_altitude>
    <osmand:max_filter_altitude>0.0</osmand:max_filter_altitude>
    <osmand:max_filter_hdop>0.0</osmand:max_filter_hdop>
    <osmand:network_route>
      <osmand:route_key type="hiking" osmc_foreground="red_diamond" symbol="Rote Raute mit weißem Balken auf weißem Grund" osmc_stub_name="." name="Mittelweg" osmc_textcolor="white" osmc_text="|" osmc_waycolor="red" osmc_background="white" wikipedia="de:Mittelweg (Fernwanderstrecke)" operator="Schwarzwaldverein" network="nwn" />
    </osmand:network_route>

     */
    @Override
    public Object parseExtensions(Node node) {
    	LOG.fine("node.NodeName="+node.getNodeName()); // expected extensions
    	Extension ext = new Extension();
        // store all nodes under extension in OsmExtensionHolder - if any
        if (GPXConstants.NODE_EXTENSIONS.equals(node.getNodeName()) && (node.getChildNodes().getLength() > 0)) {
        	NodeList nodes = node.getChildNodes();
        	for(int i=0; i<nodes.getLength(); i++) {
        		Node n = nodes.item(i);
        		// Types ELEMENT_NODE              = 1 , TEXT_NODE                 = 3
        		if(Node.ELEMENT_NODE==n.getNodeType()) {
        			String nodeName = n.getNodeName();
//			    	LOG.info(""+i+"/"+nodes.getLength() + ": ELEMENT_NODE, Name="+nodeName);			    	
			    	if(nodeName.startsWith(OSMAND_EXTENSIONS_PREFIX)) {
			    		String nodeValue = n.getChildNodes().item(0).getNodeValue();
			    		// Boolean:
			    		if(nodeName.endsWith("show_arrows")) {
			    			ext.addExtensionData(nodeName, Boolean.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("show_start_finish")) {
			    			ext.addExtensionData(nodeName, Boolean.valueOf(nodeValue));
			    		// String: 
			    		} else if(nodeName.endsWith("width")) {
			    			ext.addExtensionData(nodeName, nodeValue);
			    		} else if(nodeName.endsWith("color")) {
			    			ext.addExtensionData(nodeName, nodeValue);
			    		} else if(nodeName.endsWith("coloring_type")) {
			    			ext.addExtensionData(nodeName, nodeValue);
			    		} else if(nodeName.endsWith("split_type")) {
			    			ext.addExtensionData(nodeName, nodeValue);
			    		// Double:
			    		} else if(nodeName.endsWith("split_interval")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("smoothing_threshold")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("min_filter_speed")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("max_filter_speed")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("min_filter_altitude")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("max_filter_altitude")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("max_filter_hdop")) {
			    			ext.addExtensionData(nodeName, Double.valueOf(nodeValue));
			    		} else if(nodeName.endsWith("network_route")) {
			    			LOG.config("network_route.ChildNodes.Length="+n.getChildNodes().getLength()); // 3 expected
			    			Node route_keyNode = n.getChildNodes().item(1); // route_keyNode.Attributes# = 10
			    			LOG.config("Node:"+route_keyNode+" with Attributes#="+route_keyNode.getAttributes().getLength());
			    			Map<String, String> map = new HashMap<String, String>();
			    			Node attr;
			    			for(int a=0; a<route_keyNode.getAttributes().getLength(); a++) {
			    				attr = route_keyNode.getAttributes().item(a);
//			    				LOG.info("Attribute"+a+attr.getNodeName()+":"+attr.getNodeValue());
			    				map.put(attr.getNodeName(), attr.getNodeValue());
			    			}
			    			ext.addExtensionData(nodeName, RouteKey.fromGpx(map));
			    		}
			    	} else {
			    		LOG.warning(nodeName + " is not OSM!");
			    	}
        		}
        	}
        	OsmExtensionHolder eh = new OsmExtensionHolder(ext);
        	eh.setNodeList(nodes);
        	return eh;
        } else {
            return null;
        }
    }

    @Override
    public void writeExtensions(Extension e, Node node, Document doc) {
        if(e.getExtensionData(getId()) != null) {
            // add all nodes from DummyExtensionHolder to the document
            final OsmExtensionHolder holder = (OsmExtensionHolder) e.getExtensionData(getId());
            final NodeList nodes = holder.getNodeList();
            
            // https://stackoverflow.com/questions/5786936/create-xml-document-using-nodelist
            for (int i = 0; i < nodes.getLength(); i++) {
                final Node extNode = nodes.item(i);
                final Node copyNode = doc.importNode(extNode, true);
                node.appendChild(copyNode);
            }
        }
    }
	
}

