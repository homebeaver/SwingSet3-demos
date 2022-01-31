package com.klst.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
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
import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.StopIcon;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.ButtonDemo;
import swingset.ColorChooserDemo;
import swingset.ComboBoxDemo;
import swingset.FileChooserDemo;
import swingset.HtmlDemo;
import swingset.ListDemo;
import swingset.OptionPaneDemo;
import swingset.ProgressBarDemo;
import swingset.ScrollPaneDemo;
import swingset.SliderDemo;
import swingset.SplitPaneDemo;
import swingset.StaticUtilities;
import swingset.TabbedPaneDemo;
import swingset.TableDemo;
import swingset.ToolTipDemo;
import swingset.TreeDemo;

/*
TODO RootFrame <> JXRootPane
 */
@SuppressWarnings("serial")
public class RootFrame extends WindowFrame {

    private static final Logger LOG = Logger.getLogger(RootFrame.class.getName());

	private static final String TITLE = "Gossip";
	
	Map<Class<?>, WindowFrame> demos;
	public RootFrame() {
		super(TITLE);
		super.rootFrame = this;
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
		demos = new HashMap<Class<?>, WindowFrame>();
		// TODO ... so initialisieren geht angeblich auch für static var
//		demos = Map.ofEntries(
//			Map.entry(InternalFrameDemo.class, null), // NullPointerException at java.base/java.util.Objects.requireNonNull(Objects.java:208)
//			Map.entry(ButtonDemo.class       , null),
//			Map.entry(ColorChooserDemo.class , null),
//			Map.entry(ComboBoxDemo.class     , null),
//			Map.entry(FileChooserDemo.class  , null),
//			Map.entry(HtmlDemo.class         , null),
//			Map.entry(ListDemo.class         , null),
//			Map.entry(OptionPaneDemo.class   , null),
//			Map.entry(ProgressBarDemo.class  , null),
//			Map.entry(ScrollPaneDemo.class   , null),
//			Map.entry(SliderDemo.class       , null),
//			Map.entry(SplitPaneDemo.class    , null),
//			Map.entry(TabbedPaneDemo.class   , null),
//			Map.entry(TableDemo.class        , null),
//			Map.entry(ToolTipDemo.class      , null),
//			Map.entry(TreeDemo.class         , null)
//			);
		addDemos();
		frames = new ArrayList<JXFrame>();
		frames.add(this);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LOG.info(TITLE+" frame ctor. frames#="+frames.size() + " super.rootFrame:"+super.rootFrame);
    	getRootFrame().demoActions.forEach( a -> {
    		AbstractButton ab = addActionToToolBar(this, a);
    	});

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
//    	demoTree = createDemoTree();
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
	private void addDemos() {
		demos.put(null, null); // current presentation frame
		demoActions.add(new DemoAction(ButtonDemo.class, "runDemo", StaticUtilities.createImageIcon(ButtonDemo.ICON_PATH)));
		demos.put(ButtonDemo.class, null);
		demoActions.add(new DemoAction(ColorChooserDemo.class, "runDemo", StaticUtilities.createImageIcon(ColorChooserDemo.ICON_PATH)));
		demos.put(ColorChooserDemo.class, null);
		demoActions.add(new DemoAction(ComboBoxDemo.class, "runDemo", StaticUtilities.createImageIcon(ComboBoxDemo.ICON_PATH)));
		demos.put(ComboBoxDemo.class, null);
		demoActions.add(new DemoAction(FileChooserDemo.class, "runDemo", StaticUtilities.createImageIcon(FileChooserDemo.ICON_PATH)));
		demos.put(FileChooserDemo.class, null);
		demoActions.add(new DemoAction(HtmlDemo.class, "runDemo", StaticUtilities.createImageIcon(HtmlDemo.ICON_PATH)));
		demos.put(HtmlDemo.class, null);
		demoActions.add(new DemoAction(ListDemo.class, "runDemo", StaticUtilities.createImageIcon(ListDemo.ICON_PATH)));
		demos.put(ListDemo.class, null);
		demoActions.add(new DemoAction(OptionPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(OptionPaneDemo.ICON_PATH)));
		demos.put(OptionPaneDemo.class, null);		
		demoActions.add(new DemoAction(ProgressBarDemo.class, "runDemo", StaticUtilities.createImageIcon(ProgressBarDemo.ICON_PATH)));
		demos.put(ProgressBarDemo.class, null);
		demoActions.add(new DemoAction(ScrollPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(ScrollPaneDemo.ICON_PATH)));
		demos.put(ScrollPaneDemo.class, null);
		demoActions.add(new DemoAction(SliderDemo.class, "Slider", SS2_ICON, StaticUtilities.createImageIcon(SliderDemo.ICON_PATH)));
		demos.put(SliderDemo.class, null);
		demoActions.add(new DemoAction(SplitPaneDemo.class, "SplitPane", SS2_ICON, StaticUtilities.createImageIcon(SplitPaneDemo.ICON_PATH)));
		demos.put(SplitPaneDemo.class, null);
		demoActions.add(new DemoAction(TabbedPaneDemo.class, "TabbedPane", SS2_ICON, StaticUtilities.createImageIcon(TabbedPaneDemo.ICON_PATH)));
		demos.put(TabbedPaneDemo.class, null);
		demoActions.add(new DemoAction(TableDemo.class, "Table", SS2_ICON, StaticUtilities.createImageIcon(TableDemo.ICON_PATH)));
		demos.put(TableDemo.class, null);
		demoActions.add(new DemoAction(ToolTipDemo.class, "ToolTip", SS2_ICON, StaticUtilities.createImageIcon(ToolTipDemo.ICON_PATH)));
		demos.put(ToolTipDemo.class, null);
		demoActions.add(new DemoAction(TreeDemo.class, "Tree", SS2_ICON, StaticUtilities.createImageIcon(TreeDemo.ICON_PATH)));
		demos.put(TreeDemo.class, null);
		// swingset 3:
//		demoActions.add(new DemoAction(XTreeDemo.class, "XTree", SS3DATA_ICON, StaticUtilities.createImageIcon(XTreeDemo.class, XTreeDemo.ICON_PATH)));
		demos.put(XTreeDemo.class, null);
//		demoActions.add(new DemoAction(TreeTableDemo.class, "XTreeTable", SS3DATA_ICON, StaticUtilities.createImageIcon(TreeTableDemo.class, TreeTableDemo.ICON_PATH)));
		demos.put(TreeTableDemo.class, null);
	}

	JMenu menu;
    public JMenu createDemosMenu() {
//    	this.menu = new JMenu(new DemoAction(null, "Demos"));
//    	AbstractAction ss2Action = new DemoAction(null, "SwingSet2", SS2_ICON, null); 	
//    	JMenu ss2menu = (JMenu) menu.add(new JMenu(ss2Action));
//    	AbstractAction ss3Action = new DemoAction(null, "SwingSet3", PLAY_ICON, null); 	
//    	JMenu ss3menu = (JMenu) menu.add(new JMenu(ss3Action));
//    	
//    	this.demoActions.forEach( action -> {
//    		if(SS2_ICON==action.getValue(Action.SMALL_ICON)) {
//            	ss2menu.add(action);
//    		} else if(action.getValue(Action.SMALL_ICON)!=null) {
//    			ss3menu.add(action);
//    		}
//    	});
//    	
//		Component[] compArray = menu.getMenuComponents();
//		for(int i=0; i<compArray.length; i++) {
//			JMenu comp = (JMenu)compArray[i];
//			LOG.info("comp "+i + ":"+comp.getAction());
//		}
    	
    	//DemoMenuAction root = DemoMenuAction.getRootAction();
		TreeNode rootNode = DemoMenuAction.createTree(this);
    	TreeTableModel model = DemoMenuAction.createMenuModel(rootNode);
    	// --------------
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot(); // DefaultMutableTreeNode root
    	this.menu = new JMenu((Action)root.getUserObject());
//    	for(int i=0;i<model.getChildCount(root);i++) {
//    		Object o = model.getChild(root,i);
//    		menu.add(new JMenu((Action)o));
//    	}
    	Object o2 = model.getChild(root,0);
    	JMenu ss2 = (JMenu) menu.add(new JMenu((Action)o2));
    	this.demoActions.forEach( action -> {
    		if(SS2_ICON==action.getValue(Action.SMALL_ICON)) {
            	ss2.add(action);
    		}
    	});
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
		DefaultTreeRenderer treeRenderer = new MenuTreeRenderer(rootNode);
//		demoTree.setCellRenderer(treeRenderer);
		
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
    		WindowFrame current = demos.get(null);
    		LOG.info("------------ close/dispose "+current);
    		if(current!=null) {
    			current.dispose();
    		}
    		demos.put(null, frame);
    		
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
