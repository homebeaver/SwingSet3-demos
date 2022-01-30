package com.klst.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.demos.tree.TreeDemoIconValues.LazyLoadingIconValue;
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
//	private JXTree createDemoTree(JMenu menu) {
//		DefaultMutableTreeNode top = new DefaultMutableTreeNode(menu.getAction());
//
//		Component[] compArray = menu.getMenuComponents();
//		for(int i=0; i<compArray.length; i++) {
//			JMenu comp = (JMenu)compArray[i];
//			Action action = comp.getAction();
//			LOG.info("comp "+i + ":"+(action==null?"null":action.getClass())+" - "+action);
//			if(action!=null) {
//				top.add(new DefaultMutableTreeNode(action));
//			}
//		}
//
//		JXTree tree = new JXTree(top);
//		//tree.setName("demoMenuTree");
//		DefaultTreeRenderer treeRenderer = new MenuTreeRenderer(menu); //iv, sv);
//		tree.setCellRenderer(treeRenderer);
//		return tree;
//	}
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
//                	LOG.info(" ### value:"+value);
//                    if (value instanceof Component) {
//                        Component component = (Component) value;
//                        String simpleName = component.getClass().getSimpleName();
//                        if (simpleName.length() == 0){
//                            // anonymous class
//                            simpleName = component.getClass().getSuperclass().getSimpleName();
//                        }
//                        return simpleName + "(" + component.getName() + ")";
//                    }
                    return StringValues.TO_STRING.getString(tn);
                }
            };
            LOG.info("           StringValue sv:"+sv.getString(tn));
            return sv;
        }

		public MenuTreeRenderer(TreeNode ma) {
//			LOG.info("           TreeNode:"+ma);
			// super.(IconValue iv, StringValue sv)
			super(getIconValue(ma), getStringValue(ma));
		}
	}
	public JXTree createDemoTreeXX() {
		Object userObject = this.menu; // demoTree top aka root
		userObject = new String("swing set demos");
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(userObject);
        DefaultMutableTreeNode ss2 = new DefaultMutableTreeNode("SwingSet2");
        top.add(ss2);
        DefaultMutableTreeNode catagory = null;
    	this.demoActions.forEach( action -> {
    		if(PLAY_ICON==action.getValue(Action.SMALL_ICON)) {
//            	ss2.add(new DefaultMutableTreeNode(action.getValue(Action.NAME)));
            	ss2.add(new DefaultMutableTreeNode(action));
    		} else if(action.getValue(Action.SMALL_ICON)!=null) {
//    			ss3menu.add(action);
    		}
    	});
    	// tree farbe ist weiss! ; TODO tree action ==> action
    	
        // open tree data
//        URL url = getClass().getResource("/swingset/tree.txt");
        URL url = getClass().getResource("/demotree.txt");

		JXTree tree = new JXTree(top);
		tree.setName("demoMenuTree");
		
		// TODO background ist immer weiss!!! und die icons passen auch nicht ==> renderer
//        tree.setBackground(null); // nicht weiss, ABER beim Umschalten auf Nimbus wieder weiss!!!
		
//        StringValue keyValue = new StringValue() {         
//            @Override // simple converter to return a String representation of an object
//            public String getString(Object value) {
//                if (value == null) return "";
//                return "JXTree.png";
//            }
//        };
//        IconValue iv = new LazyLoadingIconValue(JXTree.class, keyValue, "fallback.png");
//        // StringValue provides node text: concat several 
//        StringValue sv = new StringValue() {        
//            @Override
//            public String getString(Object value) {
//            	LOG.info(" ### "+value.getClass()+" value:"+value);
//                if (value instanceof Component) {
//                    Component component = (Component) value;
//                    String simpleName = component.getClass().getSimpleName();
//                    if (simpleName.length() == 0){
//                        // anonymous class
//                        simpleName = component.getClass().getSuperclass().getSimpleName();
//                    }
//                    return simpleName + "(" + component.getName() + ")";
//                }
//                return StringValues.TO_STRING.getString(value);
//            }
//        };
//    	LOG.info("-------iv, sv:"+iv +", "+sv);
//    	DefaultTreeRenderer treeRenderer = new DefaultTreeRenderer(iv, sv);
//		tree.setCellRenderer(treeRenderer);
		return tree;
	}
	
	private void initDemos() {
		// wie oben
		// oder addDemo zusammen mit demoActions
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
    	this.menu = new JMenu(new DemoAction(null, "Demos"));
    	AbstractAction ss2Action = new DemoAction(null, "SwingSet2", SS2_ICON, null); 	
    	JMenu ss2menu = (JMenu) menu.add(new JMenu(ss2Action));
    	AbstractAction ss3Action = new DemoAction(null, "SwingSet3", PLAY_ICON, null); 	
    	JMenu ss3menu = (JMenu) menu.add(new JMenu(ss3Action));
    	
    	// so kann ich menu nicht erweitern:
//    	TreeModel tm = demoTree.getModel();
//    	DefaultMutableTreeNode top = (DefaultMutableTreeNode)tm.getRoot();
//    	top.add( new DefaultMutableTreeNode(ss2menu) );
//    	top.add( new DefaultMutableTreeNode(ss3menu) );
//    	demoTree.setModel(tm);
//    	demoTree.invalidate();
    	
    	this.demoActions.forEach( action -> {
    		if(SS2_ICON==action.getValue(Action.SMALL_ICON)) {
            	ss2menu.add(action);
    		} else if(action.getValue(Action.SMALL_ICON)!=null) {
    			ss3menu.add(action);
    		}
    	});
    	
		Component[] compArray = menu.getMenuComponents();
		for(int i=0; i<compArray.length; i++) {
			JMenu comp = (JMenu)compArray[i];
			LOG.info("comp "+i + ":"+comp.getAction());
		}
//		demoTree = createDemoTree(menu);
//		content.add(new JScrollPane(demoTree), BorderLayout.WEST);
    	
    	//DemoMenuAction root = DemoMenuAction.getRootAction();
		TreeNode rootNode = DemoMenuAction.createTree();
    	TreeTableModel model = DemoMenuAction.createMenuModel(rootNode);
    	// macht new AbstractTreeTableModel(root)
    	
    	// private static class DummyTreeTableModel extends AbstractTreeTableModel
    	// DummyTreeTableModel dummyModel = new DummyTreeTableModel(createTree());
    	demoTree = new JXTree(model);
		DefaultTreeRenderer treeRenderer = new MenuTreeRenderer(rootNode);
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
