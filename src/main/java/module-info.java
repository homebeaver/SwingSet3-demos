module swingx.demo {
// kein	exports 
//	exports org.jdesktop.swingx.demos;
/* 
The package org.jdesktop.swingx.util conflicts with a package accessible from another module: swingx.common
[ERROR] src/main/java/org/jdesktop/swingx/util/DecoratorFactory.java:[5,1] package exists in another module: swingx.common
=> move org.jdesktop.swingx.util.DecoratorFactory to org.jdesktop.swingx.demos.search 
wenn es nur dort benutzt wird

TODO
[ERROR] src/main/java/org/jdesktop/swingx/plaf/basic/DemoCalendarRenderingHandler.java:[20,1] package exists in another module: swingx.core
[ERROR] src/main/java/org/jdesktop/swingx/plaf/basic/DemoMonthViewUI.java:[20,1] package exists in another module: swingx.core
class org.jdesktop.swingx.plaf.basic.BasicCalendarRenderingHandler in core is not visible
kann also nicht ohne weiteres erweitert werden!!!
public class BasicMonthViewUI core , daher kann man erweitern:
public class DemoMonthViewUI extends BasicMonthViewUI ...
	
[ERROR] src/main/java/org/jdesktop/swingx/treetable/TreeTableModelAdapter.java:[5,1] package exists in another module: swingx.core
[ERROR] src/main/java/org/jdesktop/swingx/treetable/NodeModel.java:[5,1] package exists in another module: swingx.core
[ERROR] src/main/java/org/jdesktop/swingx/treetable/NodeChangedMediator.java:[5,1] package exists in another module: swingx.core

 */
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