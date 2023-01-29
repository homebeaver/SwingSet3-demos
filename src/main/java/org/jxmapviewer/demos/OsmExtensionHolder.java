package org.jxmapviewer.demos;

import org.w3c.dom.NodeList;

public class OsmExtensionHolder {

    private NodeList myNodeList;

    public OsmExtensionHolder() {
    }

    public OsmExtensionHolder(final NodeList childNodes) {
        myNodeList = childNodes;
    }
    
    public NodeList getNodeList() {
        return myNodeList;
    }

    public void setNodeList(final NodeList nodeList) {
        myNodeList = nodeList;
    }

}
