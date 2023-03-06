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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
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
    private JXTree getTree(Component c) {
    	JScrollPane sp = (JScrollPane)c;
    	LOG.fine(" ---> getViewport:"+sp.getViewport());
    	JXTree tree = (JXTree)sp.getViewport().getView();
    	return tree;
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

        tabbedpane.add(getBundleString("music"), createMusicTree());
        tabbedpane.add(getBundleString("componentTree"), createComponentTree());
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        tabbedpane.getModel().addChangeListener( changeEvent -> {
            SingleSelectionModel model = (SingleSelectionModel) changeEvent.getSource();
        });
    }

    class Album {
    	static char SEPARATOR = ';';
        private String record; // title of the album
        private String pixUrl; // wikimedia image url of the album
        // f.i. "https://upload.wikimedia.org/wikipedia/en/a/ac/My_Name_Is_Albert_Ayler.jpg"
        
		Album(String line) {
			String recordAlbumpix = line.substring(2);
			int separator = recordAlbumpix.indexOf(SEPARATOR);
			pixUrl = null;
			if (separator == -1) {
				record = recordAlbumpix;
			} else {
				record = recordAlbumpix.substring(0, separator);
				pixUrl = recordAlbumpix.substring(separator+1);
			}
		}

		public String getHtmlSrc() {
			if(pixUrl==null) return null;
			return "<html>" + "<img src=\"" + pixUrl + "\">"+ "</html>";
		}

		public String toString() {
			return record;
		}
    }

    class TreeNodeXX implements StringValue {

    	DefaultMutableTreeNode treeNode;
    	TreeNodeXX(DefaultMutableTreeNode top) {
    		treeNode = top;
    	}
 
    	private String string;
		private void setStringAndIcon(Object value) {
//        	LOG.info(" ### value:"+value + " "+value.getClass());
            if(value instanceof Album album) {
            	//string = album.pixUrl==null ? album.record : album.pixUrl;
            	string = album.record;
            	return;
            } else if(value instanceof String stringValue) {
            	// root of Music, Catagory, Artist/Composer, Song/Composition
            	if(value==treeNode.getUserObject()) {
            		// root of Music
            		string = stringValue;
            		return;
            	}
            	Enumeration<TreeNode> children = treeNode.children();
            	// Catagory : "Rock", ...
//            	LOG.info("try Catagory for "+stringValue);
            	while(children.hasMoreElements()) {
            		TreeNode next = children.nextElement();
//                	LOG.info("----- Catagory for "+next.getClass());
                	if(next instanceof DefaultMutableTreeNode category) {
                		if(value==category.getUserObject()) {
//                			LOG.info(stringValue + " ist ----- Catagory "+category.getUserObject());
                    		string = stringValue;
                    		return;            			
                		}
                	}
            	}
            	children = treeNode.children();
            	boolean isArtist = false;
//            	LOG.info("try Artist for "+stringValue);
            	while(children.hasMoreElements()) {
            		TreeNode cat = children.nextElement();
            		Enumeration<? extends TreeNode> artists = cat.children();
            		while(artists.hasMoreElements()) {
            			TreeNode next = artists.nextElement();
            			if(next instanceof DefaultMutableTreeNode artist) {
                			if(value==artist.getUserObject()) {
                        		string = stringValue;
                            	return;
                			}
            			}
            		}
            	}
            	assert isArtist==false;
            	// Record is instanceof Album! - so we have Songs here:
//            	LOG.info("try Song/Composition for "+value);
        		string = stringValue;
        		return;
            } else if(value instanceof DefaultMutableTreeNode dmtn) {
            	Object uo = dmtn.getUserObject();
//            	TreeNode[] tn = dmtn.getPath();
//            	LOG.info(" "+uo+"### Path#:"+tn.length + " "+tn[tn.length-1] + " UserObject.Class:"+dmtn.getUserObject().getClass());
            	/* tn.length==
            	   1 : ==> root Music
            	   2 : ==> Catagory : "Rock", ...
            	   3 : ==> Artist : "Steve Miller Band", ...
            	   4 : ==> Record : "The Joker", ... class org.jdesktop.swingx.demos.tree.XTreeDemo$Album
            	           with toString()-method returns record
            	 */
            	if(uo instanceof Album album) {
                	string = album.pixUrl==null ? album.record : album.pixUrl;
            		return;
            	}
            	if(value==treeNode) {
//                	LOG.info("top UserObject:"+uo + " UserObject.Class:"+uo.getClass());
                	string = uo.toString();
                	return;
            	}
            	Enumeration<TreeNode> children = treeNode.children();
            	boolean isCategory = false;
            	while(children.hasMoreElements()) {
            		TreeNode child = children.nextElement();
            		isCategory = value==child; 
            	}
            	if(isCategory) {
//                	LOG.info("Catagory UserObject:"+uo + " UserObject.Class:"+uo.getClass());
                	string = uo.toString();
                	return;
            	}
            	assert isCategory==false;
            	children = treeNode.children();
            	boolean isArtist = false;
            	while(children.hasMoreElements()) {
            		TreeNode cat = children.nextElement();
            		Enumeration<? extends TreeNode> artists = cat.children();
            		while(artists.hasMoreElements()) {
            			TreeNode artist = artists.nextElement();
            			if(value==artist) {
//                        	LOG.info("Artist UserObject:"+uo + " UserObject.Class:"+uo.getClass());
                        	string = uo.toString();
                        	return;
            			}
            		}
            	}
            	assert isArtist==false;
            } else if(value instanceof Component) {
                Component component = (Component) value;
                String simpleName = component.getClass().getSimpleName();
                if (simpleName.length() == 0){
                    // anonymous class
                    simpleName = component.getClass().getSuperclass().getSimpleName();
                }
                string = simpleName + "(" + component.getName() + ")";
            	return;
            }
        	LOG.warning("???????????????????????? :"+value);
		}

		@Override
		public String getString(Object value) {
			setStringAndIcon(value);
			return string;
        }
		
    }

    private JComponent createMusicTree() {
    	
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getBundleString("music"));
        DefaultMutableTreeNode catagory = null ;
        DefaultMutableTreeNode artist = null;
        DefaultMutableTreeNode record = null;

        // open tree data
        URL url = getClass().getResource("resources/tree.txt");
        LOG.info("tree data url="+url);

        try {
            // convert url to buffered string
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            // read one line at a time, put into tree
            String line = reader.readLine();
            while(line != null) {
            	LOG.fine("reading in: ->" + line + "<-");
                char linetype = line.charAt(0);
                switch(linetype) {
                   case 'C':
                     catagory = new DefaultMutableTreeNode(line.substring(2));
                     top.add(catagory);
                     break;
                   case 'A':
                     if(catagory != null) {
                         catagory.add(artist = new DefaultMutableTreeNode(line.substring(2)));
                     }
                     break;
                   case 'R':
                     if(artist != null) {
                    	 artist.add(record = new DefaultMutableTreeNode(new Album(line)));
                     }
                     break;
                   case 'S':
                     if(record != null) {
                         record.add(new DefaultMutableTreeNode(line.substring(2)));
                     }
                     break;
                   default:
                     break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }

        @SuppressWarnings("serial")
		JXTree tree = new JXTree(top) {
            public Insets getInsets() {
                return new Insets(5,5,5,5);
            }
        };
        
        // rollover support: enabled to show album cover, a "live" rollover behaviour:
        tree.setRolloverEnabled(true);
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
					DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)o;	
					Album album = (Album)dmtn.getUserObject();
					source.setToolTipText(album.getHtmlSrc());
				}
			}
        });
        
        tree.setOpaque(true);
        
        LOG.config("default Tree.CellRenderer for music tree:"+tree.getCellRenderer());
        TreeNodeXX tnsv = new TreeNodeXX(top);
        DefaultTreeRenderer renderer = new DefaultTreeRenderer((StringValue)tnsv) {

            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            	Component comp = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            	if(value instanceof DefaultMutableTreeNode dmtn) {
        			if(comp instanceof WrappingIconPanel wip) {
        				if(leaf) { // Level==4
            				wip.setIcon(FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS));
        				} else if(3==dmtn.getLevel()) {
        					wip.setIcon(FeatheRdisc.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
//        				} else {       					
//            				LOG.info("---"+value+"--- row="+row + " Level="+dmtn.getLevel()
//            					+ " dmtn.UserObject="+dmtn.getUserObject()); 
        				}
//        			} else {
//        				LOG.info("---"+value+"--- row="+row + " NOT wip, comp="+comp); 
        			}
            	} else {
            		LOG.warning("value \""+value+"\" is "+value.getClass());
            	}
            	return comp;
            }

        };
        tree.setCellRenderer(renderer);
        tree.setRolloverEnabled(true);
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
				getTree(tabbedpane.getComponentAt(0)).expandAll();
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
				getTree(tabbedpane.getComponentAt(0)).collapseAll();
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
        	getTree(tabbedpane.getComponentAt(1));
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