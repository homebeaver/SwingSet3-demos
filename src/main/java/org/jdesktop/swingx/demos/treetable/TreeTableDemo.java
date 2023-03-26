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
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXTree.DelegatingRenderer;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.FilteredIconValue;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
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
 * @author EUG https://github.com/homebeaver (reorg)
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
	private static final String DESCRIPTION = "Demonstrates JXTreeTable, a tree-based, grid component.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new TreeTableDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    private boolean initialized = false;
    private JXTreeTable treeTable;
    
    /**
     * TreeTableDemo Constructor
     * 
     * @param frame controller Frame
     */
    public TreeTableDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
		JXTreeTable.TreeTableCellRenderer renderer = new JXTreeTable.TreeTableCellRenderer(getTreeTableModel(SwingUtilities.getWindowAncestor(this))) {
		    /**
		     * {@inheritDoc} <p>
		     * 
		     * Overridden to return the <code>JXTree</code> delegating renderer 
		     * with <code>StringValue</code> and <code>IconValue</code>.
		     */
		    @Override
			public TreeCellRenderer getCellRenderer() {
				StringValue sv = (Object value) -> {
					if (value instanceof Component component) {
						String simpleName = component.getClass().getSimpleName();
						if (simpleName.length() == 0) {
							// anonymous class
							simpleName = component.getClass().getSuperclass().getSimpleName();
						}
						return simpleName + "(" + component.getName() + ")";
					}
					return StringValues.TO_STRING.getString(value);
				};
				// StringValue for lazy icon loading interface
				// org.jdesktop.swingx.renderer.StringValue
				StringValue keyValue = (Object value) -> {
					if (value == null)
						return "";
					String simpleClassName = value.getClass().getSimpleName();
					if (simpleClassName.length() == 0) {
						// anonymous class
						simpleClassName = value.getClass().getSuperclass().getSimpleName();
					}
					return simpleClassName + ".png";
				};
				IconValue iv = new LazyLoadingIconValue(getClass(), keyValue, "fallback.png");
				return new JXTree.DelegatingRenderer(iv, sv);
			}

		};

        // <snip> JXTree rollover
        // enable and register a highlighter
        renderer.setRolloverEnabled(true);
//		Highlighter musicIcon = new IconHighlighter(HighlightPredicate.IS_LEAF, 
//				FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
//		renderer.addHighlighter(musicIcon);
// TODO: IS_LEAF funktioniert, aber es ist ein Test, einen rollover icon HL erstellen:
        //renderer.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
        //treeTable.addHighlighter(createRolloverIconHighlighter(iv));
        // </snip>

		treeTable = new ComponentTreeTable(renderer);
        treeTable.setName("componentTreeTable");
        add(new JScrollPane(treeTable));
        LOG.info("done initComponents treeTable:"+treeTable);

        // configureComponents:
        treeTable.setColumnControlVisible(true);
        
		// UI-Dependent Striping 
		Highlighter alternateStriping = HighlighterFactory.createAlternateStriping();
		if(alternateStriping instanceof AbstractHighlighter ah) {
    		ah.setHighlightPredicate(HighlightPredicate.ALWAYS);
		}
		treeTable.addHighlighter(alternateStriping);

        Highlighter mouseOverHighlighter = new ColorHighlighter(HighlightPredicate.ROLLOVER_CELL, PaintUtils.setSaturation(Color.MAGENTA, 0.3f), null);
        treeTable.addHighlighter(mouseOverHighlighter);

        customColumnFactory();
    }
	@SuppressWarnings("serial")
	class ComponentTreeTable extends JXTreeTable implements TableCellRenderer {

		ComponentTreeTable(JXTreeTable.TreeTableCellRenderer renderer) {
			super(renderer);
			assert ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree() == renderer;
        	StringValue locSize = (Object value) -> {
				int x;
				int y;
				if (value instanceof Dimension dim) {
					x = dim.width;
					y = dim.height;
				} else if (value instanceof Point point) {
					x = point.x;
					y = point.y;
				} else {
					return StringValues.TO_STRING.getString(value); // also for value==null
				}
				return "(" + x + ", " + y + ")";
	        };
	        setDefaultRenderer(Point.class, new DefaultTableRenderer(locSize, JLabel.CENTER));
	        setDefaultRenderer(Dimension.class, this.getDefaultRenderer(Point.class));
		}
//	    @Override // code in super: return (TreeTableModel) renderer.getModel();
//	    public TreeTableModel getTreeTableModel() {
//			TableModel tm = this.getModel();
//			if(tm instanceof TreeTableModelAdapter mttma) {
//				return mttma.getTreeTableModel();
//			}
//			return super.getTreeTableModel();
//	    }
//	    @Override
//		public int getHierarchicalColumn() {
//	    	int superret = super.getHierarchicalColumn();
//	    	assert superret==0;
//			return super.getHierarchicalColumn();
//		}
//	    @Override
//	    public TableCellRenderer getCellRenderer(int row, int column) {
//	    	ComponentAdapter ca = getComponentAdapter(row, column);
//	    	if(ca.column == getHierarchicalColumn()) {
//	    		JXTree.DelegatingRenderer renderer = (JXTree.DelegatingRenderer)getTreeCellRenderer();
////		    	LOG.info("hierarchical column "+column + " isHierarchicalColumn!!! renderer:"+renderer);
//	    		JTree tree = ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree();
//	    		JXTree xtree = (JXTree)tree;
//	    		return (JXTreeTable.TreeTableCellRenderer)xtree;
//	    	}
//	    	return super.getCellRenderer(row, column);
//	    }
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {			
        	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
//        	super.getCellRenderer(row, column)
			return null;
		}
		
	}

    // Controller:
//    private JXButton loadButton;
    private JXButton expandButton;
    private JXButton collapseButton;

    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

//		loadButton = new JXButton(getBundleString("reloadTreeData"));
//		loadButton.setName("loadButton");
//		loadButton.addActionListener(ae -> {
//			treeTable.setTreeTableModel(createTreeModel());
//		});
//		buttons.add(loadButton);
		
		// <snip> JXTree convenience api
		expandButton = new JXButton(getBundleString("expandAll.Action.text"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(ae -> {
			treeTable.expandAll();
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getBundleString("collapseAll.Action.text"));
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

    private void customColumnFactory() {
        // <snip>JXTreeTable column customization
        // configure and install a custom columnFactory, arguably data related ;-)
        ColumnFactory factory = new ColumnFactory() {
            String[] columnNameKeys = 
            	{ getBundleString("componentType")
            	, getBundleString("componentName")
            	, getBundleString("componentLocation")
            	, getBundleString("componentSize")
            	};

            @Override
            public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);
                if (columnExt.getModelIndex() < columnNameKeys.length) {
                    columnExt.setTitle(columnNameKeys[columnExt.getModelIndex()]);
                }
            }
            
        };
        treeTable.setColumnFactory(factory);
        // </snip>
    }
   

//  private AbstractInputEventDispatcher inputEventDispatcher;
//  private AbstractHighlighter mouseOverHighlighter;
  
    // <snip> Input-/FocusEvent notification 
    /**
     * update Highlighter's predicate to highlight the tree row
     * which contains the component under the current mouse position
     * @param component Component
     */
//    protected void updateHighlighter(Component component) {
//        mouseOverHighlighter.setHighlightPredicate(HighlightPredicate.NEVER);
//        if (component != null) {
//        
//            List<Component> pathList = new ArrayList<Component>();
//            while (component != null) {
//                pathList.add(0, component);
//                component = component.getParent();
//            }
//            final TreePath treePath = new TreePath(pathList.toArray());
//            treeTable.scrollPathToVisible(treePath);
//            HighlightPredicate predicate = new HighlightPredicate() {
//                
//                @Override
//                public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
//                    return adapter.row == treeTable.getRowForPath(treePath);
//                }
//            };
//            mouseOverHighlighter.setHighlightPredicate(predicate);
//            // </snip>
//
//        }
//    }

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
    /**
     * 
     * @param root Component
     * @return TreeTableModel
     */
    public static TreeTableModel getTreeTableModel(Component root) {
    	LOG.config("Component root:"+root);
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
                    o = c; // c.getClass().getSimpleName();
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

}

