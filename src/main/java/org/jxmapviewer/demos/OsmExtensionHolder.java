package org.jxmapviewer.demos;

import org.w3c.dom.NodeList;

import me.himanshusoni.gpxparser.modal.Extension;

/*

zunächst wie DummyExtensionHolder: mit myNodeList

 */
public class OsmExtensionHolder {

    private NodeList myNodeList;
    private Extension myExtension;

    public OsmExtensionHolder() {
    }

    public OsmExtensionHolder(final NodeList childNodes) {
        myNodeList = childNodes;
    }
    public OsmExtensionHolder(final Extension extension) {
    	myExtension = extension;
    }
    
    public NodeList getNodeList() {
        return myNodeList;
    }

    public Extension getExtension() {
        return myExtension;
    }

    public void setNodeList(final NodeList nodeList) {
        myNodeList = nodeList;
    }

}
