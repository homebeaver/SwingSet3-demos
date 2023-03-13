/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.demos.highlighter.RolloverIconHighlighter;
import org.jdesktop.swingx.demos.svg.FeatheRdisc;
import org.jdesktop.swingx.demos.svg.FeatheRmusic;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.SizingConstants;
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
//          , createMusicTable(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        	, createMusicTreeTable(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
//        	, createMusicTree(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        tabbedpane.add(getBundleString("componentTree"), createComponentTree());
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
    		
//    		// UI-Dependent Striping 
//    		Highlighter alternateStriping = HighlighterFactory.createAlternateStriping();
//    		if(alternateStriping instanceof AbstractHighlighter ah) {
//        		ah.setHighlightPredicate(HighlightPredicate.ALWAYS);
//    		}
    		// auskommentiert - (sieht nicht besonders gut aus)
//    		addHighlighter(alternateStriping);
    		
			/*
			 * use small disc icon for records/Albums
			 */
			Highlighter discIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(3), 
					FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
			addHighlighter(discIcon);
			
			/*
			 * use very small XS music icon instead the default Tree.leafIcon (file/sheet/fileview)
			 */
			Highlighter musicIcon = new IconHighlighter(HighlightPredicate.IS_LEAF, 
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
			StringValue sv = new StringValue() {          
                @Override
                public String getString(Object value) {
                	LOG.fine(" ### value:"+value + " "+value.getClass());
                    if(value instanceof MusicTreeModel.MusicEntry
                    || value instanceof MusicTreeModel.Album
                    || value instanceof MusicTreeModel.Song
                    ) {
                    	return StringValues.TO_STRING.getString(value);
                    }
                    String simpleName = value.getClass().getSimpleName();
                    return simpleName + "(" + value + ")";
                }
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
    
    private JComponent createComponentTree() {
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 */
        componentTree = new JXTree((TreeModel)null) {
        	@Override
            public TreeCellRenderer getCellRenderer() {
    			StringValue sv = new StringValue() {          
                    @Override
                    public String getString(Object value) {
//                    	LOG.info(" ### value:"+value + value.getClass());
                        if (value instanceof Component component) {
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

    // MusicTreeModel implements TreeTableModel
    private JComponent createMusicTreeTable(TreeTableModel model) {
    	JXTreeTable treeTable = new JXTreeTable(model);
    	treeTable.setShowGrid(false, true);

// TODO    	treeTable.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {

        return new JScrollPane(treeTable);
    }

}