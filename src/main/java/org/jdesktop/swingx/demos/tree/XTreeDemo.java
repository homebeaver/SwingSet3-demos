/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.demos.highlighter.RolloverIconHighlighter;
import org.jdesktop.swingx.demos.svg.FeatheRdisc;
import org.jdesktop.swingx.demos.svg.FeatheRmusic;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.FilteredIconValue;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.AbstractDemo;

/**
 * JXTree Demo
 * 
 * PENDING JW: make editable to demonstrate terminate enhancement. 
 *
 * @author Jeanette Winzenburg, Berlin, Created on 18.04.2008
 * @author EUG https://github.com/homebeaver (reorg, add music tree from SwingSet2)
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

	public static final String ICON_PATH = "toolbar/JTree.gif";

	private static final long serialVersionUID = 7070451442278673301L;
    private static final Logger LOG = Logger.getLogger(XTreeDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTree, an enhanced tree component";
   
    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
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
    	});
    }

    private JTabbedPane tabbedpane; // contains music tree, index 0 and component tree, index 1
    private JXTree componentTree;
    private JLabel fakeLabel = new JLabel("fake");

    /*
     * intentionally not defined music tree here.
     * You can get it from scroll pane with getTree(tabbedpane.getComponentAt(0))
     */
    private Component getTreeComp(Component c) {
    	JScrollPane sp = (JScrollPane)c;
    	LOG.fine(" ---> getViewport:"+sp.getViewport());
    	return sp.getViewport().getView();
    }

    /**
     * XTreeDemo Constructor
     * 
     * @param frame controller Frame
     */
    public XTreeDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // create tabs
        tabbedpane = new JTabbedPane();
        add(tabbedpane, BorderLayout.CENTER);

        tabbedpane.add(getBundleString("music")
//        	, createMusicTreeTable(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        	, createMusicTree(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        tabbedpane.add(getBundleString("componentTree"), createComponentTree());
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        tabbedpane.getModel().addChangeListener( changeEvent -> {
            SingleSelectionModel ssmodel = (SingleSelectionModel) changeEvent.getSource();
        });
    }

    // MusicTreeModel implements TreeTableModel
    private JComponent createMusicTreeTable(MusicTreeModel model) {
    	JXTreeTable treeTable = new JXTreeTable(model);
    	TableModel tm = treeTable.getModel();
    	if(tm instanceof JXTreeTable.TreeTableModelAdapter ttma) {
    		JTree t = ttma.getTreeCellRenderer();
    		if(t instanceof JXTreeTable.TreeTableCellRenderer tree) {
    			LOG.info("can show a \"live\" rollover behaviour:"+tree.isRolloverEnabled());
    			
//    	        tree.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {
//    	        	LOG.info(" "+propertyChangeEvent);
//    	        	JXTree source = (JXTree)propertyChangeEvent.getSource();
//    	        	source.setToolTipText(null);
//    				Point newPoint = (Point)propertyChangeEvent.getNewValue();
//    				if(newPoint!=null && newPoint.y>-1) {
//    					TreePath treePath = source.getPathForRow(newPoint.y);
//    					if(treePath.getPathCount()==4) { // Album / Record / Style 
//    						Object o = treePath.getLastPathComponent();
////    						LOG.info("PathFor newPoint.y: "+source.getPathForRow(newPoint.y) + " PropertyChangeEvent:"+propertyChangeEvent);
//    						// show https://en.wikipedia.org/wiki/File:My_Name_Is_Albert_Ayler.jpg
//    						if(o instanceof MusicTreeModel.Album album) {
//    							source.setToolTipText(album.getHtmlSrc());
//    						}
//    					} else if(treePath.getPathCount()==5) { // Song / Composition
//    						Object o = treePath.getLastPathComponent();
//    						if(o instanceof MusicTreeModel.Song song) {
//    							source.setToolTipText(song.getHtmlSrc());
//    						}
//    					}
//    				}
//    	        });
    		}
    	} else {
    		LOG.warning("tm:"+tm);    		
    	}
    	
//    	RolloverIconHighlighter roih = new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null);
//    	treeTable.addHighlighter(roih); // funktioniert nicht TODO

        return new JScrollPane(treeTable);
    }

    @SuppressWarnings("serial")
	// inner class JXTreeTable.TreeTableCellRenderer is not public, dort ca 600 Zeilen
	static class MusicTreeTableCellRenderer extends JXTree implements TableCellRenderer {

    	// ctor
        // Force user to specify TreeTableModel instead of more general TreeModel
        public MusicTreeTableCellRenderer(TreeTableModel model) {
//        public TreeTableCellRenderer(TreeTableModel model) {
//            super(model);
//            putClientProperty("JTree.lineStyle", "None");
//            setRootVisible(false); // superclass default is "true"
//            setShowsRootHandles(true); // superclass default is "false"
//                /**
//                 * TODO: Support truncated text directly in
//                 * DefaultTreeCellRenderer.
//                 */
//            // removed as fix for #769-swingx: defaults for treetable should be same as tree
////            setOverwriteRendererIcons(true);
//// setCellRenderer(new DefaultTreeRenderer());
//            setCellRenderer(new ClippedTreeCellRenderer());
        	super(model);
        }
        
        /* in JXTree gibt es ähnliches
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
wie kann man getTreeCellRendererComponent nach getTableCellRendererComponent mappen ????

 - JTree tree ==> JTable table : muss man nicht, ist bekannt
 - int column ==> getHierarchicalColumn()
 - int row muss man berechnen
 
         */
		@Override // implements TableCellRenderer
		public Component getTableCellRendererComponent(JTable table, Object value
				, boolean isSelected, boolean hasFocus, int row, int column) {
			//assert table == treeTable;
			//super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (isSelected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}

//			highlightBorder = null;
//			if (treeTable != null) {
//				if (treeTable.realEditingRow() == row && treeTable.getEditingColumn() == column) {
//				} else if (hasFocus) {
//					highlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
//				}
//			}
//
//			visibleRow = row;

			return this;
		}
    	
    }
    
    @SuppressWarnings("serial")
	class MusicTree extends JXTree implements TableCellRenderer {
    	
    	MusicTree(MusicTreeModel model) {
    		super(model);
    		LOG.info("JTree.lineStyle="+
    				getClientProperty("JTree.lineStyle") // warum ist es null? es sollte "Angled" sein
    		);
    		setRolloverEnabled(true); // to show a "live" rollover behaviour
    		setOpaque(true); // wirklich notwendig? testen
    		setCellRenderer(makeCellRenderer());
    	}
    	
        public Insets getInsets() {
            return new Insets(5,5,5,5);
        }
        
        // TreeCellRenderer is interface, DefaultTreeRenderer implements it
        private TreeCellRenderer makeCellRenderer() {
            @SuppressWarnings("serial")
			StringValue sv = new StringValue() {          
                @Override
                public String getString(Object value) {
                	LOG.fine(" ### value:"+value + " "+value.getClass());
                    if(value instanceof String
                    || value instanceof MusicTreeModel.Album
                    || value instanceof MusicTreeModel.Song
                    ) {
                    	return StringValues.TO_STRING.getString(value);
                    }
                    String simpleName = value.getClass().getSimpleName();
                    return simpleName + "(" + value + ")";
                }
            };
            @SuppressWarnings("serial")
			TreeCellRenderer renderer = new DefaultTreeRenderer(sv) {
                @Override
                public Component getTreeCellRendererComponent(JTree tree, Object value,
                        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                	Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                	if(comp instanceof WrappingIconPanel wip) {
                    	if(value instanceof String string) {
                    		// default icon for Catagory, Artist/Composer
                    	} else if(value instanceof MusicTreeModel.Song song) {
            				wip.setIcon(FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
                    	} else if(value instanceof MusicTreeModel.Album album) {
        					wip.setIcon(FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
                    	} else {
                    		LOG.warning("value \""+value+"\" is "+value.getClass());
                    	}
                	}
                	return comp;
                }
            };
            return renderer;
        	
        }

        /* in JXTree gibt es ähnliches
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
wie kann man getTreeCellRendererComponent nach getTableCellRendererComponent mappen ????

 - JTree tree ==> JTable table : muss man nicht, ist bekannt
 - int column ==> getHierarchicalColumn()
 - int row muss man berechnen
 
         */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
        	LOG.info("row="+row + " column="+column + " value:"+value);
        	boolean expanded = true;
        	boolean leaf = false;
        	Component comp = getCellRenderer().getTreeCellRendererComponent(MusicTree.this, value, isSelected, expanded, leaf, row, hasFocus);
        	
        	TreeModel m = getModel();
        	if(m instanceof TreeTableModel ttm) {
        		column = ttm.getHierarchicalColumn();
        	}
//        	updateUI();
			return comp;
		}
    }
    
    private JComponent createMusicTree(MusicTreeModel model) {
    	JXTree tree = new MusicTree(model);
    	
        tree.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {
        	JXTree source = (JXTree)propertyChangeEvent.getSource();
        	source.setToolTipText(null);
			Point newPoint = (Point)propertyChangeEvent.getNewValue();
			if(newPoint!=null && newPoint.y>-1) {
				TreePath treePath = source.getPathForRow(newPoint.y);
				if(treePath.getPathCount()==4) { // Album / Record / Style 
					Object o = treePath.getLastPathComponent();
//					LOG.info("PathFor newPoint.y: "+source.getPathForRow(newPoint.y) + " PropertyChangeEvent:"+propertyChangeEvent);
					// show https://en.wikipedia.org/wiki/File:My_Name_Is_Albert_Ayler.jpg
					if(o instanceof MusicTreeModel.Album album) {
						source.setToolTipText(album.getHtmlSrc());
					}
				} else if(treePath.getPathCount()==5) { // Song / Composition
					Object o = treePath.getLastPathComponent();
					if(o instanceof MusicTreeModel.Song song) {
						source.setToolTipText(song.getHtmlSrc());
					}
				}
			}
        });
    	RolloverIconHighlighter roih = new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null);
    	tree.addHighlighter(roih);
    	
        tree.setEditable(true);
        return new JScrollPane(tree);
    }
    
    private JComponent createComponentTree() {
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 */
        componentTree = new JXTree(); 
        componentTree.setName("componentTree");
        componentTree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JScrollPane scrollpane = new JScrollPane(componentTree);

        fakeLabel.setName("fakeLabel");
        configureComponents(fakeLabel);
        // create and install the component tree model:
        addNotify();
        
//        LOG.info("done init ComponentTree \n tree Background:"+componentTree.getBackground() 
//        + "\n tabbedpane.getAccessibleContext():"+tabbedpane.getAccessibleContext()
//        + "\n tabbedpane.getUI():"+tabbedpane.getUI()
//        + "\n tree:"+componentTree);
        return scrollpane;
    }

    // Controller:
    private JXButton loadButton;
    private JXButton expandButton;
    private JXButton collapseButton;
    
    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

		loadButton = new JXButton(getBundleString("reloadComponentTreeData"));
		loadButton.setName("loadButton");
		loadButton.addActionListener(actionEvent -> {
			if(componentTree!=null) componentTree.setModel(createTreeModel());
		});
		buttons.add(loadButton);
		
		// <snip> JXTree convenience api
		expandButton = new JXButton(getBundleString("expandAll.Action.text"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(actionEvent -> {
			if(tabbedpane.getSelectedIndex()==0) {
				// JScrollPane with music
				Component c = tabbedpane.getComponentAt(0);
				Component comp = getTreeComp(tabbedpane.getComponentAt(0));
				if(comp instanceof JXTree tree) tree.expandAll();
				if(comp instanceof JXTreeTable ttable) ttable.expandAll();
			}
			if(tabbedpane.getSelectedIndex()==1) {
				componentTree.expandAll();
			}
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getBundleString("collapseAll.Action.text"));
		collapseButton.setName("collapseButton");
		collapseButton.addActionListener(actionEvent -> {
			if(tabbedpane.getSelectedIndex()==0) {
				// JScrollPane with music
				Component comp = getTreeComp(tabbedpane.getComponentAt(0));
				if(comp instanceof JXTree tree) tree.collapseAll();
				if(comp instanceof JXTreeTable ttable) ttable.collapseAll();
			}
			if(tabbedpane.getSelectedIndex()==1) {
				componentTree.collapseAll();
			}
		});
		buttons.add(collapseButton);
		// </snip>

		return buttons;
	}

//---------------- binding/configure ComponentTree
    
    private void configureComponents(Component comp) {
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
    	LOG.info("StringValue sv:"+sv + " - sv for the fake component:"+sv.getString(comp));
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
    	LOG.info("keyValue for the fake component:"+keyValue.getString(comp));
    	
        // <snip> JXTree rendering   	
        // IconValue provides node icon 
        IconValue iv = new LazyLoadingIconValue(getClass(), keyValue, "fallback.png");
    	LOG.info("IconValue iv:"+iv);
    	
        // create and set a tree renderer using the custom Icon-/StringValue
        componentTree.setCellRenderer(new DefaultTreeRenderer(iv, sv));
        // </snip>
        
        // the current cell renderer is queried for each row's height: 
        componentTree.setRowHeight(-1);

        // <snip> JXTree rollover
        // enable and register a highlighter
        componentTree.setRolloverEnabled(true);
        componentTree.addHighlighter(createRolloverIconHighlighter(iv));
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
//        LOG.info("-------------this:"+this);
        if(componentTree==null) {
        	getTreeComp(tabbedpane.getComponentAt(1));
        	return;
        }
        TreeModel model = componentTree.getModel();
        LOG.config("tree.Model.Root:"+(model==null?"null":model.getRoot()));
        // der Vergleich mit null ist nicht sinnvoll, denn ein "leeres Modell" liefert nicht null, 
        // sondern DefaultTreeModel mit JTree: colors, sports, food
        if (model == null || "JTree".equals(model.getRoot().toString())) {
            componentTree.setModel(createTreeModel());
        }
    }

    private TreeTableModel createTreeModel() {
       Window window = SwingUtilities.getWindowAncestor(this);
       // use model from TreeTableDemo
       return TreeTableDemo.getTreeTableModel(window != null ? window : this);
    }

}