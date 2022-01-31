package com.klst.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.StopIcon;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModel;

/*
TODO RootFrame <> JXRootPane
 */
@SuppressWarnings("serial")
public class RootFrame extends WindowFrame {

    private static final Logger LOG = Logger.getLogger(RootFrame.class.getName());

	private static final String TITLE = "Gossip";
	
//	Map<Class<?>, WindowFrame> demos;
	WindowFrame currentDemoFrame = null;
	
	public RootFrame() {
		super(TITLE);
		super.rootFrame = this;
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
		frames = new ArrayList<JXFrame>();
		frames.add(this);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LOG.info(TITLE+" frame ctor. frames#="+frames.size() + " super.rootFrame:"+super.rootFrame);

    	// TODO
/* Alternative 1:
JSplitPane splitPane = null; ???
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, demotree, source+controller);

Alternative 2: hier implementiert
getContentPane(): BorderLayout ==> content
WEST:   demoTree
CENTER: JXPanel currentController
 - tabbedpane = new JTabbedPane(); mit
 - source
 - controller : currentController

 */
    	content = new JXPanel(new BorderLayout());
    	content.add(new JScrollPane(demoTree), BorderLayout.WEST);
    	tabbedpane = new JTabbedPane();
    	tabbedpane.add("source", new JXLabel("TODO enpty"));
    	currentController = new JXPanel(); //empty
    	tabbedpane.add("controller", currentController);
    	content.add(tabbedpane, BorderLayout.CENTER);
    	getContentPane().add(content);
	}
	JXTree demoTree = null;
	public class MenuTreeRenderer extends DefaultTreeRenderer {
        static IconValue getIconValue(TreeNode tn) {
        	IconValue iv = new IconValue() {
        		@Override
				public Icon getIcon(Object value) {
        			if(tn==null) return null;
        			if(tn instanceof DemoMenuAction) {
        				DemoMenuAction ma = (DemoMenuAction)tn;
        				return (Icon)ma.getValue(Action.SMALL_ICON);
        			}
        			return null;
        		}
        	};
            LOG.info("           IconValue iv:"+iv);
        	return iv;
        }

        static StringValue getStringValue(TreeNode tn) {
            StringValue sv = new StringValue() {            
                @Override
                public String getString(Object value) {
                	if(tn instanceof DemoMenuAction) {
                		DemoMenuAction ma = (DemoMenuAction)tn;
                		return StringValues.TO_STRING.getString((String)ma.getValue(Action.NAME));
                	}
                    return StringValues.TO_STRING.getString(tn);
                }
            };
            LOG.info("           StringValue sv:"+sv.getString(tn));
            return sv;
        }

		public MenuTreeRenderer(TreeNode tn) {
			super(getIconValue(tn), getStringValue(tn));
		}
	}

	static Icon SS2_ICON = new StopIcon(SizingConstants.SMALL_ICON, Color.GREEN);
	static Icon PLAY_ICON = new PlayIcon(SizingConstants.SMALL_ICON, Color.GREEN);
	// SwingSet3 Data: JXTable, JXList, JXTree, JXTreeTable
	static Icon SS3DATA_ICON = new PlayIcon(SizingConstants.SMALL_ICON, Color.BLUE);
	List<AbstractAction> demoActions = new ArrayList<AbstractAction>();

	JMenu menu;
    public JMenu createDemosMenu() {
    	//DemoMenuAction root = DemoMenuAction.getRootAction();
		TreeNode rootNode = DemoMenuAction.createTree(this);
    	TreeTableModel model = DemoMenuAction.createMenuModel(rootNode);
    	// --------------
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot(); // DefaultMutableTreeNode root
    	this.menu = new JMenu((Action)root.getUserObject());

    	Object o2 = model.getChild(root,0);
    	JMenu ss2 = (JMenu) menu.add(new JMenu((Action)o2));
    	for(int i=0;i<model.getChildCount(o2);i++) {
    		Object o = model.getChild(o2,i);
    		AbstractButton ab = addActionToToolBar(this, (DemoMenuAction)o); // add ToggleButton to ToolBar
    		LOG.info("AbstractButton:"+ab +" menu add:"+o);
    		ss2.add((DemoMenuAction)o);
    	}
    	
    	Object o3 = model.getChild(root,1);
    	JMenu ss3 = (JMenu) menu.add(new JMenu((Action)o3));
    	for(int i=0;i<model.getChildCount(o3);i++) {
    		Object o = model.getChild(o3,i);
    		LOG.info("------------ add:"+o);
    		ss3.add((DemoMenuAction)o);
    	}
    	
    	// --------------
    	demoTree = new JXTree(model);

    	demoTree.setName("demoTree");
    	demoTree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    	// white Background:javax.swing.plaf.ColorUIResource[r=255,g=255,b=255]
    	demoTree.setBackground(null); // so sind nur die texte weiss
    	demoTree.expandAll();
    	demoTree.setEditable(false);
    	
//    	demoTree.setComponentPopupMenu(JPopupMenu popup); ????
//    	demoTree.setSelectionModel(...);
    	
    	demoTree.addTreeSelectionListener( treeSelectionEvent -> {
    		JXTree source = (JXTree)(JXTree)treeSelectionEvent.getSource();
    		LOG.info("treeSelectionEvent +.Source: Foreground:"+((JXTree)treeSelectionEvent.getSource()).getForeground()
                +"\n SelectionModel:"+source.getSelectionModel()
                +"\n SelectionModel.SelectionMode:"+source.getSelectionModel().getSelectionMode()
    			//	public int[] getSelectionRows() :
            	+"\n"+((JXTree)treeSelectionEvent.getSource()).getSelectionRows()[0]
        		+"\n"+treeSelectionEvent
    			+"\n"+treeSelectionEvent.getSource());
    		TreeSelectionModel tsm = source.getSelectionModel();
    		if(tsm.getSelectionMode()==TreeSelectionModel.SINGLE_TREE_SELECTION) {
    			LOG.info("SINGLE_TREE_SELECTION SelectionPath:"+tsm.getSelectionPath());
    		} else if(tsm.getSelectionMode()==TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION) {
    			LOG.info("DISCONTIGUOUS_TREE_SELECTION SelectionPath:"+tsm.getSelectionPath());
    			Object o = tsm.getSelectionPath().getLastPathComponent();
    			LOG.info("LastPathComponent:"+o);
    			DemoMenuAction action = (DemoMenuAction)o; // root ist DefaultMutableTreeNode! ClassCastException TODO
    			LOG.info("LastPathComponent.NAME:"+action.getValue(Action.NAME));
//    			Object[] path = tsm.getSelectionPath().getPath(); // The first element is the root
//    			for(int i=0;i<path.length;i++) {
//    				LOG.info(""+i+":"+path[i]);
////    				DemoMenuAction action = (DemoMenuAction)path[i];
////    				LOG.info(""+i+":"+action.getValue(Action.NAME));
//    			}
    			action.actionPerformed(null); // ohne para  			
    		}
    	});

    	// nicht wie gewünscht: TODO
//		DefaultTreeRenderer treeRenderer = new MenuTreeRenderer(rootNode);
    	
/* aus TreeRendererTest:
        StringValue format = new StringValue() {

            public String getString(Object value) {
                if (value instanceof File) {
                    return ((File) value).getName();
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };

   	        StringValue converter = new StringValue() {

            public String getString(Object value) {
                if (value instanceof Component) {
                    return "Name: " + ((Component) value).getName();
                }
                return StringValues.TO_STRING.getString(value);
            }

 */
    	
//		DefaultTreeRenderer treeRenderer = new DefaultTreeRenderer(format); // StringValue format
        StringValue sv = new StringValue() {

            public String getString(Object value) {
//            	LOG.info("?????????????????? value:"+value);
                if (value instanceof DemoMenuAction) {
                	DemoMenuAction dma = (DemoMenuAction)value;
                	return (String)dma.getValue(Action.NAME);
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
		DefaultTreeRenderer treeRenderer = new DefaultTreeRenderer(sv);
		
		demoTree.setCellRenderer(treeRenderer);
		
		content.add(new JScrollPane(demoTree), BorderLayout.WEST);
       return menu;
    }

	// simple frame manager
	List<JXFrame> frames;
	boolean enable = true;
	boolean remove(JXFrame frame) {
		return frames.remove(frame);
	}
	WindowFrame makeFrame(RootFrame rootFrame, int window_ID, Object object) {
		if(enable) {
			int frameNumber = getWindowCounter();
    		WindowFrame frame = new WindowFrame("Frame number " + frameNumber, rootFrame, window_ID, object);
    		frames.add(frame);
    		// close/dispose current and make frame current:
    		WindowFrame current = currentDemoFrame;
    		LOG.info("------------ close/dispose "+current);
    		if(current!=null) {
    			current.dispose();
    		}
    		currentDemoFrame = frame;
    		
    		return frame;
		}
		return null;
	}
	// ...
	// <<<

	JXPanel content = null;
	JTabbedPane tabbedpane = null;
	JXPanel currentController = null;
	public void addController(JXPanel controlPane) {
		if(currentController!=null) tabbedpane.remove(currentController);
		currentController = controlPane;
		tabbedpane.add("controller", currentController);
		tabbedpane.setSelectedComponent(currentController);
		tabbedpane.invalidate();
    	pack();
	}

}
