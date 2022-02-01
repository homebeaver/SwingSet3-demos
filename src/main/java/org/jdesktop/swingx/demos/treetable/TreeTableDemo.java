/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.treetable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.util.PaintUtils;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTreeTable}.
 *
 * @author Karl George Schaefer
 */
//@DemoProperties(
//    value = "JXTreeTable Demo",
//    category = "Data",
//    description = "Demonstrates JXTreeTable, a tree-based, grid component.",
//    sourceFiles = {
//            "org/jdesktop/swingx/demos/treetable/TreeTableDemo.java",
//            "org/jdesktop/swingx/demos/tree/TreeDemoIconValues.java",
//        "org/jdesktop/swingxset/JXDemoFrame.java",
//        "org/jdesktop/swingx/demos/treetable/resources/TreeTableDemo.properties"
//    }
//)
//@SuppressWarnings("serial")
public class TreeTableDemo extends AbstractDemo {
    
	public static final String ICON_PATH = "resources/images/TreeTableDemo.png";

	private static final long serialVersionUID = 5372511125598257122L;
    private static final Logger LOG = Logger.getLogger(TreeTableDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new TreeTableDemo(controller);
				JXFrame frame = new JXFrame("demo", exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    private boolean initialized = false;
    private JXTreeTable treeTable;
    
    /**
     * TreeTableDemo Constructor
     */
    public TreeTableDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
        treeTable = new JXTreeTable();
        treeTable.setName("componentTreeTable");
        add(new JScrollPane(treeTable));
        LOG.info("done initComponents treeTable:"+treeTable);

        configureComponents();
        bind();
    }

    // Controller:
//    private JXButton loadButton;
    private JXButton expandButton;
    private JXButton collapseButton;
//    private AbstractInputEventDispatcher inputEventDispatcher;
    private AbstractHighlighter mouseOverHighlighter;
    

    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

//		loadButton = new JXButton(getString("reloadTreeData"));
//		loadButton.setName("loadButton");
//		loadButton.addActionListener(ae -> {
//			treeTable.setTreeTableModel(createTreeModel());
//		});
//		buttons.add(loadButton);
		
		// <snip> JXTree convenience api
		expandButton = new JXButton(getString("expandAll"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(ae -> {
			treeTable.expandAll();
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getString("collapseAll"));
		collapseButton.setName("collapseButton");
		collapseButton.addActionListener(ae -> {
			treeTable.collapseAll();
		});
		buttons.add(collapseButton);
		// </snip>

		return buttons;
    }

    private void refreshModel() {
        treeTable.setTreeTableModel(createTreeModel());
        treeTable.expandAll();
        treeTable.packColumn(treeTable.getHierarchicalColumn(), -1);
    }

    private void bind() {
        // <snip>JXTreeTable column customization
        // configure and install a custom columnFactory, arguably data related ;-)
        ColumnFactory factory = new ColumnFactory() {
            String[] columnNameKeys = 
            	{ getString("componentType")
            	, getString("componentName")
            	, getString("componentLocation")
            	, getString("componentSize")
            	};

            @Override
            public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);
                if (columnExt.getModelIndex() < columnNameKeys.length) {
//                    columnExt.setTitle(DemoUtils.getResourceString(TreeTableDemo.class, columnNameKeys[columnExt.getModelIndex()]));
                    columnExt.setTitle(columnNameKeys[columnExt.getModelIndex()]);
                }
            }
            
        };
        treeTable.setColumnFactory(factory);
        // </snip>
    }
   
    private void configureComponents() {
        // <snip> JXTreeTable rendering
        // StringValue provides node text, used in hierarchical column
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
                    return simpleName;
                }
                return StringValues.TO_STRING.getString(value);
            }
        };
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
        // <snip> JXTreeTable rendering
        // IconValue provides node icon (same as in XTreeDemo)
        IconValue iv = new LazyLoadingIconValue(XTreeDemo.class, keyValue, "fallback.png");
        // create and set a tree renderer using the custom Icon-/StringValue
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(iv, sv));
        // string representation for use of Dimension/Point class
        StringValue locSize = new StringValue() {
            
            @Override
            public String getString(Object value) {
                int x;
                int y;
                if (value instanceof Dimension) {
                    x = ((Dimension) value).width;
                    y = ((Dimension) value).height;
                } else if (value instanceof Point) {
                    x = ((Point) value).x;
                    y = ((Point) value).y;
                } else {
                    return StringValues.TO_STRING.getString(value);
                }
                return "(" + x + ", " + y + ")";
            }
        };
        treeTable.setDefaultRenderer(Point.class, new DefaultTableRenderer(locSize, JLabel.CENTER));
        treeTable.setDefaultRenderer(Dimension.class, treeTable.getDefaultRenderer(Point.class));
        // </snip>
        
        mouseOverHighlighter = new ColorHighlighter(HighlightPredicate.NEVER, PaintUtils.setSaturation(Color.MAGENTA, 0.3f), null);
        treeTable.addHighlighter(mouseOverHighlighter);
        
        treeTable.setColumnControlVisible(true);
    }

    
    // <snip> Input-/FocusEvent notification 
    // update Highlighter's predicate to highlight the tree row
    // which contains the component under the current mouse position
    protected void updateHighlighter(Component component) {
        mouseOverHighlighter.setHighlightPredicate(HighlightPredicate.NEVER);
        if (component != null) {
        
            List<Component> pathList = new ArrayList<Component>();
            while (component != null) {
                pathList.add(0, component);
                component = component.getParent();
            }
            final TreePath treePath = new TreePath(pathList.toArray());
            treeTable.scrollPathToVisible(treePath);
            HighlightPredicate predicate = new HighlightPredicate() {
                
                @Override
                public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                    return adapter.row == treeTable.getRowForPath(treePath);
                }
            };
            mouseOverHighlighter.setHighlightPredicate(predicate);
            // </snip>

        }
    }

    /**
     * Overridden to create and install the component tree model.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (!initialized) {
            initialized = true;
            refreshModel();
        }
        installInputEventListener();
    }

    @Override
    public void removeNotify() {
        uninstallInputEventListener();
        super.removeNotify();
    }
    // <snip> Input-/FocusEvent notification 
    // install custom dispatcher with demo frame
    private void installInputEventListener() {
//        if (!(SwingUtilities.getWindowAncestor(this) instanceof JXDemoFrame)) return;
//        JXDemoFrame window = (JXDemoFrame) SwingUtilities.getWindowAncestor(this);
//        if (inputEventDispatcher == null) {
//            inputEventDispatcher = new AbstractInputEventDispatcher() {
//                // updates Highlighter for mouseEntered/mouseExited
//                // of all components in the frame's container hierarchy
//                @Override
//                protected void processMouseEvent(MouseEvent e) {
//                    if (MouseEvent.MOUSE_ENTERED == e.getID()) {
//                        updateHighlighter(e.getComponent());
//                    } else if (MouseEvent.MOUSE_EXITED == e.getID()) {
//                        updateHighlighter(null);
//                    }
//                }
//                
//            };
//        }
//        window.setInputEventDispatcher(inputEventDispatcher); 
//        // </snip>

    }

    private void uninstallInputEventListener() {
//        if (!(SwingUtilities.getWindowAncestor(this) instanceof JXDemoFrame)) return;
//        JXDemoFrame window = (JXDemoFrame) SwingUtilities.getWindowAncestor(this);
//        window.setInputEventDispatcher(null);
//        updateHighlighter(null);
//        
    }

    private TreeTableModel createTreeModel() {
       Window window = SwingUtilities.getWindowAncestor(this);
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

