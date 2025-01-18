module swingx.demo {
// no exports 
//	exports org.jdesktop.swingx.demos;

	requires transitive java.desktop;
	requires java.logging;
	requires transitive java.compiler;
	
	requires swingx.action;
	requires swingx.autocomplete;
	requires transitive swingx.common; // requires org.kohsuke.metainf_services;
	requires swingx.core;
	requires swingx.graphics;
	requires swingx.mapviewer;
	requires swingx.painters;
	requires beansbinding; // org.jdesktop.beansbinding
	
	requires org.pushingpixels.radiance.trident;
	requires transitive org.apache.xmlgraphics.batik.brdige;
	requires org.apache.xmlgraphics.batik.util;
	requires org.apache.xmlgraphics.batik.awt.util;
	requires org.apache.xmlgraphics.batik.gvt;
	requires batik.swing;
	requires batik.transcoder;
	requires forms; // com.jgoodies.forms.*
	requires timingframework;
	requires filters; // com.jhlabs.*
}