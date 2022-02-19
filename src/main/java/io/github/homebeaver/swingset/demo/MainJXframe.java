package io.github.homebeaver.swingset.demo;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeelAddons;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModel;

/*
TODO RootFrame <> JXRootPane extends JRootPane (A lightweight container used behind the scenes by JFrame, JDialog, JWindow, JApplet, and JInternalFrame.)
de-Beschreibung in https://runebook.dev/de/docs/openjdk/java.desktop/javax/swing/jrootpane
	, https://www.java-tutorial.org/jframe.html
	
Die vier schwergewichtigen JFC/Swing-Container: ( JFrame , JDialog , JWindow und JApplet )
in SwingSet3:
	class JXFrame  extends JFrame
	class JXDialog extends JDialog
	      JXWindow gibt es nicht
	class JXApplet extends JApplet(deprecated)
	
	class JXRootPane extends JRootPane
	
es gibt viele WindowFrames, aber nur einen RootFrame ==> Singleton
TODO andere Namen: WindowFrame ==> DemoJXFrame  
                   RootFrame   ==> MainJXframe

 */
@SuppressWarnings("serial")
public class MainJXframe extends DemoJXFrame {

	/**
	 * starts swingset demo application
	 */
	public static void main(String[] args) {
		DemoJXFrame gossip = MainJXframe.getInstance(); // RootFrame contains a simple frame manager
		JMenu demoMenu = gossip.createDemosMenu();
		
        try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
			NimbusLookAndFeelAddons addon = new NimbusLookAndFeelAddons();
			addon.initialize();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal

		@SuppressWarnings("unused")
		JXStatusBar statusBar = gossip.getStatusBar(); // just to paint it

		JMenu plafMenu = gossip.createPlafMenu(gossip);
		if(plafMenu != null) gossip.getJMenuBar().add(plafMenu);
		JMenu themeMenu = gossip.createThemeMenu(gossip);
		if(themeMenu != null) gossip.getJMenuBar().add(themeMenu);
		JMenu langMenu = gossip.createLanguageMenu(gossip);
		if(langMenu != null) gossip.getJMenuBar().add(langMenu);

		if(demoMenu != null) gossip.getJMenuBar().add(demoMenu);
        		
		gossip.pack(); // auto or fix:
		//gossip.setSize(680, 200);
		gossip.setVisible(true);
	}

    private static final Logger LOG = Logger.getLogger(MainJXframe.class.getName());

	private static final String TITLE = "SwingSet Demos";

    private static final MainJXframe INSTANCE = new MainJXframe();
    
    /**
     * @return the singleton
     */
    public static MainJXframe getInstance() {
        return INSTANCE;
    }

	DemoJXFrame currentDemoFrame = null;
	
	private MainJXframe() {
		super(TITLE);
		frames = new ArrayList<JXFrame>();
		frames.add(this);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LOG.info(TITLE+" frame ctor. frames#="+frames.size());

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
	JMenu menu;
	// TODO use ActionContainerFactory.createMenu( ... )
	/*
	 
ActionManager manager = ActionManager.getInstance();	 // ActionManager extends ActionMap
    ActionManager am = new ActionManager();
    ActionContainerFactory acf = new ActionContainerFactory(am);
    JToolBar bar = null;
        initActionManager(); // am befüllen

        bar = acf.createToolBar(Arrays.asList(new String[] { "ba", "bb" }));
	 
	 */
    public JMenu createDemosMenu() {
    	TreeTableModel model = DemoTreeTableModel.getInstance();
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot(); // DefaultMutableTreeNode root
    	this.menu = new JMenu((Action)root.getUserObject());

    	Object o2 = model.getChild(root,0);
    	JMenu ss2 = (JMenu) menu.add(new JMenu((Action)o2));
    	for(int i=0;i<model.getChildCount(o2);i++) {
    		Object o = model.getChild(o2,i);
    		AbstractButton ab = addActionToToolBar(this, (DemoAction)o); // add ToggleButton to ToolBar
    		LOG.config("AbstractButton:"+ab +" menu add:"+o);
    		ss2.add((DemoAction)o); // add menuitem DemoMenuAction to ss2 submenu
    	}
    	
    	Object o3 = model.getChild(root,1);
    	JMenu ss3 = (JMenu) menu.add(new JMenu((Action)o3));
    	for(int i=0;i<model.getChildCount(o3);i++) {
    		Object o = model.getChild(o3,i);
    		LOG.config("menuitem add:"+(DemoAction)o);
    		ss3.add((DemoAction)o); // add menuitem DemoMenuAction to ss3 submenu
    	}
    	
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
    			DemoAction action = (DemoAction)o; // root ist DefaultMutableTreeNode! ClassCastException TODO
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

    	IconValue iv = new IconValue() {
       		@Override
			public Icon getIcon(Object value) {
                if (value instanceof DemoAction) {
                	DemoAction dma = (DemoAction)value;
                	return (Icon)dma.getValue(Action.SMALL_ICON);
                }
    			return null;
    		}
    	};
        StringValue sv = new StringValue() {
       		@Override
            public String getString(Object value) {
                if (value instanceof DemoAction) {
                	DemoAction dma = (DemoAction)value;
                	return (String)dma.getValue(Action.NAME);
                }
                return StringValues.TO_STRING.getString(value);
            }            
        };
		DefaultTreeRenderer treeRenderer = new DefaultTreeRenderer(iv, sv);
		
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
	DemoJXFrame makeFrame(MainJXframe rootFrame, int window_ID, Object object) {
		if(enable) {
			int frameNumber = getWindowCounter();
    		DemoJXFrame frame = new DemoJXFrame("Frame number " + frameNumber, window_ID, object);
    		frames.add(frame);
    		// close/dispose current and make frame current:
    		DemoJXFrame current = currentDemoFrame;
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
