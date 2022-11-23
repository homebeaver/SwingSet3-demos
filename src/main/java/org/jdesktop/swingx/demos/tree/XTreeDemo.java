/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
import org.jdesktop.swingx.demos.svg.FeatheRmusic;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.FilteredIconValue;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.jdesktop.swingx.rollover.TreeRolloverProducer;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.AbstractDemo;
import swingset.plaf.ColorUnit;

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
    private TreeRolloverProducer musicRollover = null;
    private JXTree componentTree;

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
//        tabbedpane.add("a label", new JLabel(" ?????music ????"));
        tabbedpane.setTabPlacement(JTabbedPane.TOP);
        tabbedpane.getModel().addChangeListener( changeEvent -> {
            SingleSelectionModel model = (SingleSelectionModel) changeEvent.getSource();
        });
    }
    
    private JComponent createMusicTree() {
    	
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getBundleString("music"));
        DefaultMutableTreeNode catagory = null ;
        DefaultMutableTreeNode artist = null;
        DefaultMutableTreeNode record = null;

        // open tree data
        URL url = getClass().getResource("/swingset/tree.txt");
        LOG.info("tree data url="+url);

        try {
            // convert url to buffered string
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            // read one line at a time, put into tree
            String line = reader.readLine();
            while(line != null) {
//                System.out.println("reading in: ->" + line + "<-");
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
                         artist.add(record = new DefaultMutableTreeNode(line.substring(2)));
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

        JXTree tree = new JXTree(top) {
            public Insets getInsets() {
                return new Insets(5,5,5,5);
            }
            protected RolloverProducer createRolloverProducer() {
            	musicRollover = (TreeRolloverProducer)super.createRolloverProducer();
            	return musicRollover;
            }
        };
        tree.setRolloverEnabled(true);
        
        tree.setOpaque(true);
        
        LOG.info("Tree.CellRenderer for music tree:"+tree.getCellRenderer());
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        RadianceIcon icon = FeatheRmusic.of(SizingConstants.XS, SizingConstants.XS);
        /*
         * use very small XS music icon instead the default Tree.leafIcon (file/sheet/fileview)
         */
        renderer.setLeafIcon(icon);
        tree.setCellRenderer(renderer);
        
    	String currentClassName = UIManager.getLookAndFeel().getClass().getName();
    	if(currentClassName.contains("Nimbus")) {
    		renderer.setBackgroundNonSelectionColor(ColorUnit.NIMBUS_BACKGROUND);
    		tree.setBackground(ColorUnit.NIMBUS_BACKGROUND);
    	} else {
    		LOG.config("current Laf is "+currentClassName);
    		Color primary3 = MetalLookAndFeel.getCurrentTheme().getPrimaryControl();
    		Color secondary3 = MetalLookAndFeel.getCurrentTheme().getControl();
    		LOG.config("set BG to secondary3 "+secondary3);
    		renderer.setBackgroundNonSelectionColor(secondary3);
    		tree.setBackground(secondary3);
    	}

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

        configureComponents();
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
        componentTree.setCellRenderer(new DefaultTreeRenderer(iv, sv));
        // </snip>
        
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
        LOG.info("tree.Model.Root:"+(model==null?"null":model.getRoot()));
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


