/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.TreeModel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.FilteredIconValue;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.AbstractDemo;

/**
 * JXTree Demo
 * 
 * PENDING JW: make editable to demonstrate terminate enhancement. 
 *
 * @author Jeanette Winzenburg, Berlin, Created on 18.04.2008
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//        value = "JXTree Demo",
//        category = "Data",
//        description = "Demonstrates JXTree, an enhanced tree component",
//        sourceFiles = {
//                "org/jdesktop/swingx/demos/tree/XTreeDemo.java",
//                "org/jdesktop/swingx/demos/tree/TreeDemoIconValues.java"
//                }
//)
public class XTreeDemo extends AbstractDemo {

	public static final String ICON_PATH = "resources/images/XTreeDemo.gif";

	private static final long serialVersionUID = 7070451442278673301L;
    private static final Logger LOG = Logger.getLogger(XTreeDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTree, an enhanced tree component";
   
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new XTreeDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    private JXTree tree;
    
    /**
     * XTreeDemo Constructor
     */
    public XTreeDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 */
        tree = new JXTree(); 
        tree.setName("componentTree");
        tree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//        tree.setBackground(null); // is white Background:javax.swing.plaf.ColorUIResource[r=255,g=255,b=255]
        add(new JScrollPane(tree), BorderLayout.CENTER);        
        LOG.info("done initComponents Background:"+tree.getBackground() + " tree:"+tree);

        configureComponents();
        // create and install the component tree model:
        addNotify();
    }

    // Controller:
    private JXButton loadButton;
    private JXButton expandButton;
    private JXButton collapseButton;
    
    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

		loadButton = new JXButton(getBundleString("reloadTreeData"));
		loadButton.setName("loadButton");
		loadButton.addActionListener(ae -> {
			//addNotify(); // create and install the component tree model
			tree.setModel(createTreeModel());
		});
		buttons.add(loadButton);
		
		// <snip> JXTree convenience api
		expandButton = new JXButton(getBundleString("expandAll.Action.text"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(ae -> {
			tree.expandAll();
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getBundleString("collapseAll.Action.text"));
		collapseButton.setName("collapseButton");
		collapseButton.addActionListener(ae -> {
			tree.collapseAll();
		});
		buttons.add(collapseButton);
		// </snip>

		return buttons;
	}

//---------------- binding/configure
    
    private void configureComponents() {
        // <snip> JXTree rendering
        // StringValue provides node text: concat several 
        StringValue sv = new StringValue() {
            
            @Override
            public String getString(Object value) {
//            	LOG.info(" ### value:"+value);
                if (value instanceof Component) {
                    Component component = (Component) value;
                    String simpleName = component.getClass().getSimpleName();
                    if (simpleName.length() == 0){
                        // anonymous class
                        simpleName = component.getClass().getSuperclass().getSimpleName();
                    }
                    return simpleName + "(" + component.getName() + ")";
                }
                return StringValues.TO_STRING.getString(value);
            }
        };
    	LOG.info("StringValue sv:"+sv);
        // </snip>
        
        // StringValue for lazy icon loading interface org.jdesktop.swingx.renderer.StringValue
        StringValue keyValue = new StringValue() {         
            @Override // simple converter to return a String representation of an object
            public String getString(Object value) {
                if (value == null) return "";
                String simpleClassName = value.getClass().getSimpleName();
                if (simpleClassName.length() == 0){
                    // anonymous class
                    simpleClassName = value.getClass().getSuperclass().getSimpleName();
                }
                return simpleClassName + ".png";
            }
        };
    	LOG.info("StringValue keyValue:"+keyValue);
    	
        // <snip> JXTree rendering   	
        // IconValue provides node icon 
        IconValue iv = new LazyLoadingIconValue(getClass(), keyValue, "fallback.png");
    	LOG.info("IconValue iv:"+iv);
    	
        // create and set a tree renderer using the custom Icon-/StringValue
        tree.setCellRenderer(new DefaultTreeRenderer(iv, sv));
        // </snip>
        
        tree.setRowHeight(-1);     
        // <snip> JXTree rollover
        // enable and register a highlighter
        tree.setRolloverEnabled(true);
        tree.addHighlighter(createRolloverIconHighlighter(iv));
        // </snip>
    }

    // <snip> JXTree rollover
    // custom implementation of Highlighter which highlights 
    // by changing the node icon on rollover
    private Highlighter createRolloverIconHighlighter(IconValue delegate) {
        // the icon look-up is left to an IconValue
        final IconValue iv = new FilteredIconValue(delegate);
        AbstractHighlighter hl = new AbstractHighlighter(HighlightPredicate.ROLLOVER_ROW) {

            /**
             * {@inheritDoc} <p>
             * 
             * Implemented to highlight by setting the node icon.
             */
            @Override
            protected Component doHighlight(Component component, ComponentAdapter adapter) {
                Icon icon = iv.getIcon(adapter.getValue());
                if (icon != null) {
                    ((WrappingIconPanel) component).setIcon(icon);
                }
                return component;
            }
            // </snip>
            
            /**
             * {@inheritDoc} <p>
             * 
             * Implementated to return true if the component is a WrappingIconPanel,
             * a panel implemenation specialized for rendering tree nodes.
             * 
             */
            @Override
            protected boolean canHighlight(Component component, ComponentAdapter adapter) {
                return component instanceof WrappingIconPanel;
            }
            
        };
        return hl;
    }

    /**
     * Overridden to create and install the component tree model.
     */
    @Override // overrides javax.swing.JComponent.addNotify
    public void addNotify() {
        super.addNotify();
        TreeModel model = tree.getModel();
        LOG.info("tree.Model:"+(model==null?"null":model.getRoot()));
        // der Vergleich mit null ist nicht sinnvoll, denn ein "leeres Modell" liefert nicht null, 
        // sondern DefaultTreeModel mit JTree: colors, sports, food
        if (model == null || "JTree".equals(model.getRoot().toString())) {
            tree.setModel(createTreeModel());
        }
    }

    private TreeTableModel createTreeModel() {
       Window window = SwingUtilities.getWindowAncestor(this);
       return TreeTableDemo.getTreeTableModel(window != null ? window : this);
    }

}


