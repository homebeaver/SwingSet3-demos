/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.io.IOException;
import java.net.URI;
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
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
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
import org.jdesktop.swingx.demos.svg.FeatheRuser;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.JXIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.TrafficLightRedIcon;
import org.jdesktop.swingx.icon.TrafficLightYellowIcon;
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

    private JTabbedPane tabbedpane; // contains music tree, index 0 and component tree at tabbedpane.getTabCount()
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
        	, createMusicTree(new MusicTreeModel(getBundleString("music"), getClass().getResource("resources/tree.txt"))));
        tabbedpane.add(getBundleString("default JTree"), createDefaultTree()); 
        tabbedpane.add(getBundleString("componentTree"), createComponentTree());
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        // the default model of tabbedpane implements SingleSelectionModel
        // javax.swing.DefaultSingleSelectionModel
        tabbedpane.getModel().addChangeListener( changeEvent -> {
        	Object source = changeEvent.getSource();
        	if(source instanceof SingleSelectionModel ssmodel) {
        		Component scrollPane = tabbedpane.getComponentAt(ssmodel.getSelectedIndex());
    			// componentTree ist in JScrollPane eingepackt
        		if(getTreeComp(scrollPane)==componentTree) {
        			LOG.info("componentTree selected "+changeEvent);
        			componentTree.setModel(createTreeModel()); // lazy createTreeModel
        			componentTree.expandAll();
        		} else {
        			LOG.info("selected "+getTreeComp(scrollPane));
        		}
        	} else {
        		LOG.warning(""+changeEvent);
        	}
        });
    }

    @SuppressWarnings("serial")
	class MusicTree extends JXTree /*implements TableCellRenderer*/ {
    	
    	static final String LINE_STYLE = "JTree.lineStyle";
    	static final String LEG_LINE_STYLE_STRING = "Angled";
    	
    	MusicTree(MusicTreeModel model) {
    		super(model);
    		// javax.swing.plaf.metal.MetalTreeUI.LINE_STYLE : the property name is private
    		Object lineStyle = getClientProperty(LINE_STYLE);
    		if(lineStyle==null) {
    			// warum ist es null? es sollte "Angled" sein
        		LOG.warning("JTree.lineStyle="+lineStyle + " will be set to 'Horizontal'"); 
        		// null oder LEG_LINE_STYLE_STRING ist gleichwertig!! 
        		// siehe MetalTreeUI#144: lineStyle = LEG_LINE_STYLE; // default case
        		putClientProperty(LINE_STYLE, "Horizontal"); // macht es einen Unterschied? JA
        		LOG.fine("JTree.lineStyle=" + getClientProperty(LINE_STYLE) + "\n");
    		}
    		setRolloverEnabled(true); // to show a "live" rollover behaviour
    		setCellRenderer(musicCellRenderer()); 		
    	}

        public Insets getInsets() {
            return new Insets(5,5,5,5);
        }
        
        public void setEditable(boolean editable) {
        	LOG.config("setEditable to "+editable+(isLargeModel()?", tree isLargeModel":"")+", cellEditor:"+cellEditor);
        	// cellEditor:org.jdesktop.swingx.tree.DefaultXTreeCellEditor
        	super.setEditable(editable);
        }
        
        private TreeCellRenderer musicCellRenderer() {
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

    }
    
    private JComponent createMusicTree(MusicTreeModel model) {
    	JXTree tree = new MusicTree(model);

		/*
		 * use small person icon for Composer (use And Predicate)
		 */
		Highlighter personIcon = new IconHighlighter(
				new HighlightPredicate.AndHighlightPredicate(HighlightPredicate.IS_LEAF, new HighlightPredicate.DepthHighlightPredicate(2)),
				FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
		tree.addHighlighter(personIcon);

		/*
		 * use small disc icon for records/Albums
		 */
		Highlighter discIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(3), 
				FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
		tree.addHighlighter(discIcon);
		
		/*
		 * use very small XS music icon instead the default Tree.leafIcon (file/sheet/fileview)
		 * HighlightPredicate.IS_LEAF is not good, because there is a composer (Chopin) entry,
		 * with no records ==> hence Chopin is a leaf
		 * ==> use Depth predicate
		 */
		Highlighter musicIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(4), 
				FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
		tree.addHighlighter(musicIcon);
		
		Highlighter redText = new ColorHighlighter(HighlightPredicate.ROLLOVER_CELL, null, Color.RED);
		tree.addHighlighter(redText);

		tree.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));

		/*
		 * define a ToolTip for Cursor-Rollover event.
		 * - for Albums : show the Album cover from wikipedia
		 * 
		 * addPropertyChangeListener is defined and implemented in java.awt.Component
		 * PropertyChangeListener is a SAM-interface (Single Abstract Method) 
		 * with method void propertyChange(PropertyChangeEvent evt). So I can define this method
		 * as Lambda-Expression.
		 *  
		 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
		 */
        tree.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, propertyChangeEvent -> {
        	JXTree source = (JXTree)propertyChangeEvent.getSource(); // source where event occurred
        	source.setToolTipText(null);
			Point newPoint = (Point)propertyChangeEvent.getNewValue();
			if(newPoint!=null && newPoint.y>-1) {
				TreePath treePath = source.getPathForRow(newPoint.y);
				if(treePath.getPathCount()==4) { // Album / Record / Style 
					Object o = treePath.getLastPathComponent();
					// show https://en.wikipedia.org/wiki/File:My_Name_Is_Albert_Ayler.jpg
					if(o instanceof MusicTreeModel.Album album) {
						source.setToolTipText(album.getHtmlSrc());
					}
				} else if (treePath.getPathCount() == 3) { // Artist / Composer
					Object o = treePath.getLastPathComponent();
					if (o instanceof MusicTreeModel.Artist artist) {
						URI uri = artist.getURI();
						if(uri!=null) {
							source.setToolTipText("click to browse wikipedia");
						}
					}
				} else if(treePath.getPathCount()==5) { // Song / Composition
					Object o = treePath.getLastPathComponent();
					if(o instanceof MusicTreeModel.Song song) {
						URI uri = song.getURI();
						if(uri!=null) {
							source.setToolTipText("click to open a browser player");
						}
					}
				}
			}
        });
        tree.addPropertyChangeListener(RolloverProducer.CLICKED_KEY, propertyChangeEvent -> {
        	JXTree source = (JXTree)propertyChangeEvent.getSource();
        	source.setToolTipText(null);
			Point newPoint = (Point)propertyChangeEvent.getNewValue();
			if(newPoint!=null && newPoint.y>-1) {
				TreePath treePath = source.getPathForRow(newPoint.y);
				if(treePath.getPathCount()==3) { // Artist / Composer
					Object o = treePath.getLastPathComponent();
					if (o instanceof MusicTreeModel.Artist artist) {
						URI uri = artist.getURI();
						if(uri!=null) try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if(treePath.getPathCount()==5) { // Song / Composition
					Object o = treePath.getLastPathComponent();
					if(o instanceof MusicTreeModel.Song song) {
						URI uri = song.getURI();
						if(uri!=null) try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
        });
    	
        tree.setEditable(true);
        return new JScrollPane(tree);
    }
    
	@SuppressWarnings("serial")
	private JComponent createDefaultTree() {
    	/*
    	 * no param: use the DefaultMutableTreeNode JTree with colors, sports, food
    	 * defined in protected static TreeModel JTree.getDefaultTreeModel()
    	 */
		JXTree dtree = new JXTree() {
			@Override
			public TreeCellRenderer getCellRenderer() {
				StringValue sv = (Object value) -> {
					if (value instanceof String) {
						return StringValues.TO_STRING.getString(value);
					}
					// UserObject is wrapped in DefaultMutableTreeNode instance
					// auto-unwrap in WrappingProvider is switched off
					if (value instanceof javax.swing.tree.DefaultMutableTreeNode dmtn) {
						return StringValues.TO_STRING.getString(dmtn.getUserObject());
					}
					String simpleName = value.getClass().getSimpleName();
					return simpleName + "(" + value + ")";
				};
	        	IconValue iv = (Object value) -> {
					if (value instanceof String s) {
						if(s.equals("red")) {
//							return UIManager.getIcon("Tree.leafIcon");
							return TrafficLightRedIcon.of(JXIcon.SMALL_ICON, JXIcon.SMALL_ICON);
						}
						if(s.equals("yellow")) {
							return TrafficLightYellowIcon.of(JXIcon.SMALL_ICON, JXIcon.SMALL_ICON);
						}
					}
					if (value instanceof javax.swing.tree.DefaultMutableTreeNode dmtn) {
						if("red".equals(dmtn.getUserObject())) {
							return TrafficLightRedIcon.of(JXIcon.SMALL_ICON, JXIcon.SMALL_ICON);
						}
						if("yellow".equals(dmtn.getUserObject())) {
							return TrafficLightYellowIcon.of(JXIcon.SMALL_ICON, JXIcon.SMALL_ICON);
						}
					}
					return null;
	        	};
				return new JXTree.DelegatingRenderer(iv, sv);
			}
		};
    	dtree.setCellRenderer(dtree.getCellRenderer());
    	dtree.setRolloverEnabled(true);
    	dtree.addHighlighter(new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null));
    	dtree.setRowHeight(-1);
    	dtree.setName("dtree");
    	dtree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    	JScrollPane scrollpane = new JScrollPane(dtree);
    	dtree.expandAll();
    	dtree.updateUI();
    	dtree.setEditable(true);
    	return scrollpane;
    }
	
	@SuppressWarnings("serial")
	private JComponent createComponentTree() {
    	/*
    	 * no param: in JTree there is a dafault DefaultMutableTreeNode JTree with colors, sports, food.
    	 * the TreeModel is set later when componentTree is selected
    	 */
        componentTree = new JXTree() {
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

    // Controller:
    private JXButton expandButton;
    private JXButton collapseButton;
    
    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

		// <snip> JXTree convenience api
		expandButton = new JXButton(getBundleString("expandAll.Action.text"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(actionEvent -> {
			if(tabbedpane.getSelectedIndex()==tabbedpane.getTabCount()) {
				componentTree.expandAll();
			} else if(tabbedpane.getSelectedIndex()>=0) {
				// 0:JScrollPane with music , 1:JScrollPane with default JTree
//				Component c = tabbedpane.getComponentAt(tabbedpane.getSelectedIndex());
				Component comp = getTreeComp(tabbedpane.getComponentAt(tabbedpane.getSelectedIndex()));
				if(comp instanceof JXTree tree) tree.expandAll();
				if(comp instanceof JXTreeTable ttable) ttable.expandAll();
			}
		});
		buttons.add(expandButton);

		collapseButton = new JXButton(getBundleString("collapseAll.Action.text"));
		collapseButton.setName("collapseButton");
		collapseButton.addActionListener(actionEvent -> {
			if(tabbedpane.getSelectedIndex()==tabbedpane.getTabCount()) {
				componentTree.collapseAll();
			} else if(tabbedpane.getSelectedIndex()>=0) {
				Component comp = getTreeComp(tabbedpane.getComponentAt(tabbedpane.getSelectedIndex()));
				if(comp instanceof JXTree tree) tree.collapseAll();
				if(comp instanceof JXTreeTable ttable) ttable.collapseAll();
			}
		});
		buttons.add(collapseButton);
		// </snip>

		return buttons;
	}

    private TreeTableModel createTreeModel() {
       Window window = SwingUtilities.getWindowAncestor(this);
       // use model from TreeTableDemo
       return TreeTableDemo.getTreeTableModel(window != null ? window : this);
    }

}