/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.TreeModel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.FilteredIconValue;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
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
   
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
//        JFrame frame = new JFrame(XTreeDemo.class.getAnnotation(DemoProperties.class).value());
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(new XTreeDemo());
//        frame.setPreferredSize(new Dimension(800, 600));
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
    }


    private JXTree tree;
    
    /**
     * TreeDemo Constructor
     */
    public XTreeDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 */
        tree = new JXTree();
        
        tree.setName("componentTree");
        tree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(new JScrollPane(tree), BorderLayout.CENTER);
        LOG.info("done initComponents tree:"+tree);

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

		loadButton = new JXButton(getString("reloadTreeData"));
		loadButton.setName("loadButton");
		loadButton.addActionListener(ae -> {
			//addNotify(); // create and install the component tree model
			tree.setModel(createTreeModel());
		});
		buttons.add(loadButton);
		
		// <snip> JXTree convenience api
		expandButton = new JXButton(getString("expandAll"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(ae -> {
			tree.expandAll();
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getString("collapseAll"));
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
        
        // StringValue for lazy icon loading
        StringValue keyValue = new StringValue() {
            
            @Override
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

    
//    private void bind() {
//        // example model is component hierarchy of demo application
//        // bind in addNotify
//        tree.setModel(null);
//    }

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
//       this.getParent(); // in Component: transient Container parent;
//       Component c = this;
//       LOG.info("c.getParent():"+c.getParent());
//       for(Container p = c.getParent(); p != null; p = p.getParent()) {
//    	   LOG.info("----------Container p:"+p);
////           if (p instanceof Window) {
////               return (Window)p;
////           }
//       }
//       LOG.info("window:"+window); // TODO null
///*
//public final class org.jdesktop.swingxset.util.ComponentModels { ...
//    public static TreeTableModel getTreeTableModel(Component root) {
//
// */
       return getTreeTableModel(window != null ? window : this);
    }

    // copied from (swingset-demos-swingxset) org.jdesktop.swingxset.util.ComponentModels.getTreeTableModel
    /*
     * col 0: the node object/component (Component)
     * col 1: name of the component
     * col 2: location of the component (Point)
     * col 3: size 
     */
    public static TreeTableModel getTreeTableModel(Component root) {
    	LOG.info("Component root:"+root);
        return new AbstractTreeTableModel(root) {
        	// Returns the number of columns in the model.
            public int getColumnCount() {
                return 4;
            }
            // Returns the value for the {@code node} at {@code columnIndex}.
            public Object getValueAt(Object node, int column) {
                Component c = (Component) node;
                Object o = null;
                
                switch (column) {
                case 0:
                    o = c;
                    break;
                case 1:
                    o = c.getName();
                    break;
                case 2:
                    if (c.isShowing()) {
                        o = c.getLocationOnScreen();
                    }
                    break;
                case 3:
                    o = c.getSize();
                    break;
                default:
                    //does nothing
                    break;
                }
                
                return o;
            }
           
            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                case 0:
                    return Component.class;
                case 1:
                    return String.class;
                case 2:
                    return Point.class;
                case 3:
                    return Dimension.class;
                }    
                return super.getColumnClass(column);
            }

            public Object getChild(Object parent, int index) {
                return ((Container) parent).getComponent(index);
            }

            public int getChildCount(Object parent) {
                return parent instanceof Container ? ((Container) parent).getComponentCount() : 0;
            }

            public int getIndexOfChild(Object parent, Object child) {
                Component[] children = ((Container) parent).getComponents();
                
                for (int i = 0, len = children.length; i < len; i++) {
                    if (child == children[i]) {
                        return i;
                    }
                }
                
                return -1;
            }
            
        };
    }

}


