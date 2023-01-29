package org.jxmapviewer.demos;

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

    @Override
    public Object parseExtensions(Node node) {
    	LOG.info("node.NodeName="+node.getNodeName());
        // store all nodes under extension in DummyExtensionHolder - if any
        if (GPXConstants.NODE_EXTENSIONS.equals(node.getNodeName()) && (node.getChildNodes().getLength() > 0)) {
        	NodeList nodes = node.getChildNodes();
        	for(int i=0; i<nodes.getLength(); i++) {
        		Node n = nodes.item(i);
        		// Types ELEMENT_NODE              = 1 , TEXT_NODE                 = 3
        		if(Node.ELEMENT_NODE==n.getNodeType()) {
        			String nodeName = n.getNodeName();
			    	LOG.info(""+i+"/"+nodes.getLength() + ": ELEMENT_NODE, Name="+nodeName);			    	
			    	if(nodeName.startsWith(OSMAND_EXTENSIONS_PREFIX)) {
			    		if(nodeName.endsWith("show_arrows")) {
//			    			LOG.info(">>>>>"+n.getAttributes().getLength());
//			    			LOG.info(">>>>>"+n.getChildNodes().item(0)); // [#text: false]
			    			LOG.info(
			    			n.getChildNodes().item(0).getNodeValue() );
			    		}
			    	} else {
			    		LOG.warning(nodeName + " is not OSM!");
			    	}
        		}
        	}
        	return new OsmExtensionHolder(nodes);
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

