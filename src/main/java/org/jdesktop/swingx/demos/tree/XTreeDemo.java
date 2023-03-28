/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
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
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.demos.highlighter.RolloverIconHighlighter;
import org.jdesktop.swingx.demos.svg.FeatheRdisc;
import org.jdesktop.swingx.demos.svg.FeatheRmusic;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
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
//        	, createMusicTable(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
//        	, createMusicTreeTable(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        	, createMusicTree(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        tabbedpane.add(getBundleString("componentTree"), createComponentTree());
//        tabbedpane.add(getBundleString("componentTreeTable"), createComponentTreeTable()); // experimental
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        tabbedpane.getModel().addChangeListener( changeEvent -> {
            SingleSelectionModel ssmodel = (SingleSelectionModel) changeEvent.getSource();
        });
    }

    @SuppressWarnings("serial")
	class MusicTree extends JXTree implements TableCellRenderer {
    	
    	MusicTree(MusicTreeModel model) {
    		super(model);
    		LOG.info("JTree.lineStyle="+
    				getClientProperty("JTree.lineStyle") // warum ist es null? es sollte "Angled" sein
    		);
    		setRolloverEnabled(true); // to show a "live" rollover behaviour
    		setCellRenderer(getCellRenderer());
    		
			/*
			 * use small disc icon for records/Albums
			 */
			Highlighter discIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(3), 
					FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
			addHighlighter(discIcon);
			
			/*
			 * use very small XS music icon instead the default Tree.leafIcon (file/sheet/fileview)
			 * HighlightPredicate.IS_LEAF is not good, because there is a composer (Chopin) entry,
			 * with no records ==> hence Chopin is a leaf
			 * ==> use Depth predicate
			 */
			Highlighter musicIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(4), 
					FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
			addHighlighter(musicIcon);
			
			Highlighter redText = new ColorHighlighter(HighlightPredicate.ROLLOVER_CELL, null, Color.RED);
			addHighlighter(redText);

			addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
			ComponentAdapter ca = getComponentAdapter();
			LOG.info("ComponentAdapter.ValueAt(3, 1):"+ca.getValueAt(3, 1));
    	}

        public Insets getInsets() {
            return new Insets(5,5,5,5);
        }
        
        // TreeCellRenderer is interface, DelegatingRenderer extends DefaultTreeRenderer implements it
        public TreeCellRenderer getCellRenderer() {
        	StringValue sv = (Object value) -> {
                if(value instanceof MusicTreeModel.MusicEntry
                || value instanceof MusicTreeModel.Album
                || value instanceof MusicTreeModel.Song
                ) {
                	return StringValues.TO_STRING.getString(value);
                }
                String simpleName = value.getClass().getSimpleName();
                return simpleName + "(" + value + ")";
        	};
            return new JXTree.DelegatingRenderer(sv);
        }

        /*  
 !!!!!!!!!!!!!!!! wg. implements TableCellRenderer // wird aber für tree nicht benötigt
         */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {			
        	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
			return null;
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
//    	RolloverIconHighlighter roih = new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null);
//    	tree.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
    	
        tree.setEditable(true);
        return new JScrollPane(tree);
    }
    
	@SuppressWarnings("serial")
	private JComponent createComponentTree() {
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 */
        componentTree = new JXTree((TreeModel)null) {
        	@Override
            public TreeCellRenderer getCellRenderer() {
            	StringValue sv = (Object value) -> {
                    if (value instanceof Component component) {
                        String simpleName = component.getClass().getSimpleName();
                        if (simpleName.length() == 0){
                            // anonymous class
                            simpleName = component.getClass().getSuperclass().getSimpleName();
                        }
                        return simpleName + "(" + component.getName() + ")";
                    }
                    return StringValues.TO_STRING.getString(value);
            	};
                // StringValue for lazy icon loading interface org.jdesktop.swingx.renderer.StringValue
            	StringValue keyValue = (Object value) -> {
                    if (value == null) return "";
                    String simpleClassName = value.getClass().getSimpleName();
                    if (simpleClassName.length() == 0){
                        // anonymous class
                        simpleClassName = value.getClass().getSuperclass().getSimpleName();
                    }
                    return simpleClassName + ".png";
            	};
                IconValue iv = new LazyLoadingIconValue(getClass(), keyValue, "fallback.png");
                return new JXTree.DelegatingRenderer(iv, sv);
            }
        };
        componentTree.setCellRenderer(componentTree.getCellRenderer());
        // <snip> JXTree rollover
        // enable and register a highlighter
        componentTree.setRolloverEnabled(true);
        componentTree.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
        // </snip>
        
        // the current cell renderer is queried for each row's height: 
        componentTree.setRowHeight(-1);

        componentTree.setName("componentTree");
        componentTree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JScrollPane scrollpane = new JScrollPane(componentTree);

        // create and install the component tree model:
        addNotify();
        componentTree.expandAll();
        
//        LOG.info("done init ComponentTree \n tree Background:"+componentTree.getBackground() 
//        + "\n tabbedpane.getAccessibleContext():"+tabbedpane.getAccessibleContext()
//        + "\n tabbedpane.getUI():"+tabbedpane.getUI()
//        + "\n tree:"+componentTree);
        return scrollpane;
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
	    @Override // code in super: return (TreeTableModel) renderer.getModel();
	    public TreeTableModel getTreeTableModel() {
			TableModel tm = this.getModel();
			if(tm instanceof TreeTableModelAdapter mttma) {
				return mttma.getTreeTableModel();
			}
			return super.getTreeTableModel();
	    }
	    @Override
		public int getHierarchicalColumn() {
	    	int superret = super.getHierarchicalColumn();
	    	assert superret==0;
			return super.getHierarchicalColumn();
		}
	    @Override
	    public TableCellRenderer getCellRenderer(int row, int column) {
	    	ComponentAdapter ca = getComponentAdapter(row, column);
	    	if(ca.column == getHierarchicalColumn()) {
	    		JXTree.DelegatingRenderer renderer = (JXTree.DelegatingRenderer)getTreeCellRenderer();
		    	LOG.info("hierarchical column "+column + " isHierarchicalColumn!!! renderer:"+renderer);
	    		JTree tree = ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree();
	    		JXTree xtree = (JXTree)tree;
	    		return (JXTreeTable.TreeTableCellRenderer)xtree;
	    	}
	    	return super.getCellRenderer(row, column);
	    }
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {			
        	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
//        	super.getCellRenderer(row, column)
			return null;
		}
		
	}
	private JComponent createComponentTreeTable() {
		JXTreeTable.TreeTableCellRenderer componentTreeRenderer 
			= new JXTreeTable.TreeTableCellRenderer(createTreeModel()) {
	        public TreeCellRenderer getCellRenderer() {
            	StringValue sv = (Object value) -> {
                    if (value instanceof Component component) {
                        String simpleName = component.getClass().getSimpleName();
                        if (simpleName.length() == 0){
                            // anonymous class
                            simpleName = component.getClass().getSuperclass().getSimpleName();
                        }
                        return simpleName + "(" + component.getName() + ")";
                    }
                    return StringValues.TO_STRING.getString(value);
            	};
                // StringValue for lazy icon loading interface org.jdesktop.swingx.renderer.StringValue
            	StringValue keyValue = (Object value) -> {
                    if (value == null) return "";
                    String simpleClassName = value.getClass().getSimpleName();
                    if (simpleClassName.length() == 0){
                        // anonymous class
                        simpleClassName = value.getClass().getSuperclass().getSimpleName();
                    }
                    return simpleClassName + ".png";
            	};
                IconValue iv = new LazyLoadingIconValue(getClass(), keyValue, "fallback.png");
                return new JXTree.DelegatingRenderer(iv, sv);
	        }

		};
		JXTreeTable componentTreeTable = new ComponentTreeTable(componentTreeRenderer);
        JScrollPane scrollpane = new JScrollPane(componentTreeTable);

        // create and install the component tree model:
        addNotify();
        componentTreeTable.expandAll();
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

// experimental ----------------------------------------------------------------------------------------
    private JComponent createMusicTable(TableModel dModel) {
    	JXTable xTable = new JXTable(dModel);
        return new JScrollPane(xTable);
    }
    
    // class JXTreeTable.TreeTableCellRenderer extends JXTree implements TableCellRenderer
    class MusicTreeTableCellRenderer extends JXTreeTable.TreeTableCellRenderer {
    	MusicTreeTableCellRenderer(TreeTableModel model) {
    		super(model);
    		
//    		setCellRenderer(getCellRenderer());
    		
			/*
			 * use small disc icon for records/Albums
			 */
			Highlighter discIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(3), 
					FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
			addHighlighter(discIcon);
			
			/*
			 * use very small XS music icon instead the default for songs/compositions
			 */
			Highlighter musicIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(4),  
					FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
			addHighlighter(musicIcon);
			
			addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
    	}
	    @Override // to log
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
        	//LOG.info("----- r/c:"+row+"/"+column +" value:"+value + " " + value.getClass());
        	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

	class MusicTreeTable extends JXTreeTable implements TableCellRenderer {

		MusicTreeTable(JXTreeTable.TreeTableCellRenderer renderer) {
			super(renderer);
			assert ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree() == renderer;
			  		
    		// UI-Dependent Striping 
    		Highlighter alternateStriping = HighlighterFactory.createAlternateStriping();
    		if(alternateStriping instanceof AbstractHighlighter ah) {
        		ah.setHighlightPredicate(HighlightPredicate.ALWAYS);
    		}
    		addHighlighter(alternateStriping);
    		
			// IS_LEAF AND Column 1:
//			 new HighlightPredicate.ColumnHighlightPredicate(1);
//			 new HighlightPredicate.AndHighlightPredicate(HighlightPredicate.IS_LEAF, new HighlightPredicate.ColumnHighlightPredicate(1));
			Highlighter musicIcon = new IconHighlighter(
				//new HighlightPredicate.AndHighlightPredicate(HighlightPredicate.IS_LEAF, new HighlightPredicate.ColumnHighlightPredicate(1)),
				//new HighlightPredicate.ColumnHighlightPredicate(0), // in Spalten 0,2 funktioniert es
				new HighlightPredicate.ColumnHighlightPredicate(1), // in hierarchical Spalte 1 funktioniert es NICHT
				//HighlightPredicate.IS_LEAF, // in Spalte 0
				FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
//			addHighlighter(musicIcon);
			
			Highlighter redText = new ColorHighlighter(HighlightPredicate.ROLLOVER_CELL, null, Color.RED);
			addHighlighter(redText);

//			addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
			ComponentAdapter ca = getComponentAdapter();
			LOG.info("\"Rock\" expected st ComponentAdapter.ValueAt(2, 1):"+ca.getValueAt(2, 1)
				+ " ComponentAdapter:"+ca
			);

		}

	    @Override // code in super: return (TreeTableModel) renderer.getModel();
	    public TreeTableModel getTreeTableModel() {
			TableModel tm = this.getModel();
			if(tm instanceof TreeTableModelAdapter mttma) {
				return mttma.getTreeTableModel();
			}
			return super.getTreeTableModel();
	    }
	    @Override
		public int getHierarchicalColumn() {
			TableModel tm = this.getModel();
			if(tm instanceof TreeTableModelAdapter mttma) {
//				return mttma.getHierarchicalColumn(); // XXX ???
				TreeTableModel ttm = mttma.getTreeTableModel();
				if(ttm instanceof MusicTreeModel mtm) {
					return mtm.getHierarchicalColumn();
				}
			}
			return super.getHierarchicalColumn();
		}
	    @Override
	    public TableCellRenderer getCellRenderer(int row, int column) {
	    	ComponentAdapter ca = getComponentAdapter(row, column);
	    	if(ca.column == getHierarchicalColumn()) {
	    		JXTree.DelegatingRenderer renderer = (JXTree.DelegatingRenderer)getTreeCellRenderer();
//		    	LOG.info("hierarchical column "+column + " isHierarchicalColumn!!! renderer:"+renderer);
	    		JTree tree = ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree();
	    		JXTree xtree = (JXTree)tree;
	    		return (JXTreeTable.TreeTableCellRenderer)xtree;
	    	}
	    	return super.getCellRenderer(row, column);
	    }
//	    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//	    	LOG.info("??? TableCellRenderer:"+renderer);
//	    	return super.prepareRenderer(renderer, row, column);
//	    }
//	    Component getTreeCellRendererComponent(JTree tree, Object value,
//                boolean selected, boolean expanded,
//                boolean leaf, int row, boolean hasFocus) {
//					return tree;
//	    	
//	    }
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {			
        	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
//        	super.getCellRenderer(row, column)
			return null;
		}

	}

    // MusicTreeModel implements TreeTableModel
    private JComponent createMusicTreeTable(MusicTreeModel model) {
//    	MusicTree musicTree = new MusicTree(model);
    	JXTreeTable.TreeTableCellRenderer renderer = new MusicTreeTableCellRenderer(model);
    	JXTreeTable treeTable = new MusicTreeTable(renderer);
    	
    	treeTable.setShowGrid(false, true);
    	
    	treeTable.setRolloverEnabled(true); // to show a "live" rollover behaviour
    	
    	/*
    	 * rollover a row shows the Album Cover as ToolTip, f.i.
    	 * from https://en.wikipedia.org/wiki/File:My_Name_Is_Albert_Ayler.jpg
    	 */
    	treeTable.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {
			JXTreeTable source = (JXTreeTable) propertyChangeEvent.getSource();
			source.setToolTipText(null);
			Point newPoint = (Point) propertyChangeEvent.getNewValue();
			if (newPoint != null && newPoint.y > -1) {
				TreePath treePath = source.getPathForRow(newPoint.y);
				if (treePath.getPathCount() == 4) { // Album / Record / Style
					Object o = treePath.getLastPathComponent();
					LOG.fine("PathFor newPoint.y: " + source.getPathForRow(newPoint.y) + " PropertyChangeEvent:"
							+ propertyChangeEvent + " o:" + o);
					if (o instanceof MusicTreeModel.Album album) {
						source.setToolTipText(album.getHtmlSrc());
					}
				} else if (treePath.getPathCount() == 5) { // Song / Composition
					Object o = treePath.getLastPathComponent();
					if (o instanceof MusicTreeModel.Song song) {
						source.setToolTipText(song.getHtmlSrc());
					}
				}
			}
		});

// TODO    	treeTable.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {
//    	treeTable.addHighlighter(null);
//    	tree.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));

        treeTable.setColumnControlVisible(true);

        return new JScrollPane(treeTable);
    }

}