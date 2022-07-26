/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.graph;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.logging.Logger;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXGraph;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.NumberConverter;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXGraph}.
 * 
 * @author Karl George Schaefer
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(value = "JXGraph Demo", 
//        category = "Visualization", 
//        description = "Demonstrates JXGraph, a graphing display.", sourceFiles = {
//        "org/jdesktop/swingx/demos/graph/GraphDemo.java",
//        "org/jdesktop/swingx/demos/graph/resources/GraphDemo.properties",
//        "org/jdesktop/swingx/demos/graph/resources/GraphDemo.html",
//        "org/jdesktop/swingx/demos/graph/resources/images/GraphDemo.png"
//})
//@SuppressWarnings("serial")
public class GraphDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -9153405642361302142L;
	private static final Logger LOG = Logger.getLogger(GraphDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXGraph, a graphing display.";

	private SimpleLinePlot plot;
    private JFormattedTextField formula;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new GraphDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public GraphDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        createGraphDemo();
        bind();
    }

    @Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}

    //TODO inject properties
    private void createGraphDemo() {
        JPanel controlPanel = new JPanel();
        JLabel label = new JLabel("y = ");
        controlPanel.add(label);

        formula = new JFormattedTextField(NumberFormat.getNumberInstance());
        formula.setName("formula");
        formula.setColumns(3); // TODO get from prop
        formula.setValue(1.0);
        controlPanel.add(formula);

        label = new JLabel("x");
        controlPanel.add(label);
        add(controlPanel, BorderLayout.NORTH);

        Point2D origin = new Point2D.Double(0.0d, 0.0d);
        Rectangle2D view = new Rectangle2D.Double(-10.0d, -10.0d, 20.0d, 20.0d);
        JXGraph graph = new JXGraph(origin, view, 5, 5, 5, 5);
        plot = new SimpleLinePlot();
        graph.addPlots(Color.RED, plot);
        add(graph);
    }

    private void bind() {
        Binding b = Bindings.createAutoBinding(READ,
                formula, BeanProperty.create("value"),
                plot, BeanProperty.create("coefficient"));
        b.setConverter(new NumberConverter());
        b.bind();
    }
}
