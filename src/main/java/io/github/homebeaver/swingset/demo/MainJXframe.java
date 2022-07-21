package io.github.homebeaver.swingset.demo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.action.ActionContainerFactory;
import org.jdesktop.swingx.action.ActionManager;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingxset.IntroPanelDemo;

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
	
 */
/**
 * MainJXframe extends DemoJXFrame. This Frame contains the Demo Controller.
 */
@SuppressWarnings("serial")
public class MainJXframe extends DemoJXFrame {

	static private enum DemoSelectorStyle {
		USE_JXTREE,
		USE_JXTASKS
	}
	static DemoSelectorStyle demoSelectorStyle = DemoSelectorStyle.USE_JXTASKS;
	
	/**
	 * starts swingset demo application
	 */
	public static void main(String[] args) {
        
		DemoJXFrame gossip = MainJXframe.getInstance(); // RootFrame contains a simple frame manager
		// start with Nimbus (tut nicht so richtig)
//        try {
//			NimbusLookAndFeelAddons addon = new NimbusLookAndFeelAddons();
//			addon.initialize();
//			UIManager.setLookAndFeel(new NimbusLookAndFeel()); // throws UnsupportedLookAndFeelException
//		} catch (UnsupportedLookAndFeelException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		JMenu demoMenu = gossip.createDemosMenu();

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

Alternative 3: DemoJXTasks statt demoTree
 */
    	content = new JXPanel(new BorderLayout());
    	
    	if(demoSelectorStyle==DemoSelectorStyle.USE_JXTASKS) {
    		JComponent tpc = DemoJXTasks.getTaskPaneContainer();
    		tpc.setBackground(getBackground());
        	content.add(new JScrollPane(tpc), BorderLayout.WEST);
    	}

    	tabbedpane = new JTabbedPane();
    	tabbedpane.add("source", new JXLabel("TODO enpty")); // TODO
    	currentController = new IntroPanelDemo();
    	tabbedpane.add("controller", currentController);
    	tabbedpane.setSelectedComponent(currentController);
    	content.add(tabbedpane, BorderLayout.CENTER);
    	getContentPane().add(content);

    	// creates popupMenu accessible via keyboard
    	createPopupMenu(content);

	}
	private JXTree demoTree = null;
	private JMenu menu;

	private JMenu createMenu() {
		ActionManager manager = ActionManager.getInstance(); // ActionManager extends ActionMap
		ActionContainerFactory factory = new ActionContainerFactory(manager);
		manager.addAction(DemoAction.getRootAction().getName(), DemoAction.getRootAction());
		manager.addAction(DemoAction.getSS2Action().getName(), DemoAction.getSS2Action());
		manager.addAction(DemoAction.getSS3Action().getName(), DemoAction.getSS3Action());
		
		List<String> submenu2Ids = new ArrayList<String>(); // submenu SwingSet2
		submenu2Ids.add(DemoAction.getSS2Action().getName());
		DemoAction.getSS2Actions().forEach( a -> {
			manager.addAction(a.getName(), a);
			// ein hack, denn eigentlich sollte factory.createToolBar( ... verwendet werden
			AbstractButton ab = addActionToToolBar(this, a); // add ToggleButton to ToolBar
			
			submenu2Ids.add(a.getName());
		});
		
		List<String> submenu3Ids = new ArrayList<String>(); // submenu SwingSet3
		submenu3Ids.add(DemoAction.getSS3Action().getName());
		DemoAction.getSS3Actions().forEach( a -> {
			manager.addAction(a.getName(), a);
			submenu3Ids.add(a.getName());
		});
			
		List<Object> menuIds = new ArrayList<Object>();
		menuIds.add(DemoAction.getRootAction().getName());
		menuIds.add(submenu2Ids);
		menuIds.add(submenu3Ids);
		
		return factory.createMenu(menuIds);
	}
	
    public JMenu createDemosMenu() {
    	TreeTableModel model = DemoTreeTableModel.getInstance();
    	
    	this.menu = createMenu(); // mit add ToggleButton to ToolBar
    	
    	demoTree = new JXTree(model);

    	demoTree.setName("demoTree");
    	demoTree.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    	demoTree.expandAll(); 
    	demoTree.setEditable(false);
    	demoTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    	
    	demoTree.addTreeSelectionListener( treeSelectionEvent -> {
    		// implement valueChanged(TreeSelectionEvent e):
    		JXTree source = (JXTree)treeSelectionEvent.getSource(); // == demoTree
        	TreeSelectionModel tsm = source.getSelectionModel();
			Object o = tsm.getSelectionPath().getLastPathComponent();
//			LOG.info("selected aka LastPathComponent:"+o);
//			DefaultMutableTreeNode node = (DefaultMutableTreeNode)o;
//			if(node.isLeaf()) {
//				Object uo = node.getUserObject();
//				LOG.info("selected Leaf user Object:"+uo);
//				DemoAction action = (DemoAction)uo; 
//    			LOG.info("Selected DemoAction.NAME:"+action.getValue(Action.NAME));
//    			action.actionPerformed(null); // ohne para 
//			} else {
//				LOG.info("selected node is not Leaf user Object:"+node.getUserObject());
//			}
			DemoAction action = (DemoAction)o;
			LOG.info("Selected DemoAction.NAME:"+action.getValue(Action.NAME));
			action.actionPerformed(null); // ohne para
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
		
    	if(demoSelectorStyle==DemoSelectorStyle.USE_JXTREE) {
    		content.add(new JScrollPane(demoTree), BorderLayout.WEST);
    	}
		return menu;
    }

    // <snip> PopupMenu
    /**
     * a small popup menu, activated via keyboard SHIFT_DOWN+F10
     * shows items with InstalledLookAndFeels
     * and a class ActivatePopupMenuAction with ActionEvent on SHIFT_DOWN+F10
     * 
     * @param comp JComponent, typically JXPanel where to show the popup
     * @return JPopupMenu
     */
    public JPopupMenu createPopupMenu(JComponent comp) {
        JPopupMenu popupMenu = new JPopupMenu("JPopupMenu Laf demo");

        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        JMenuItem mi = null;
        for (int counter = 0; counter < lafInfo.length; counter++) {
//        	String classname = lafInfo[counter].getClassName();
//        	LOG.info("--->counter "+counter + " lafInfo.ClassName:"+classname);
        	mi = createLafMenuItem(lafInfo[counter]);
        	popupMenu.add(mi);
        }

        InputMap map = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK), "postMenuAction");
        comp.getActionMap().put("postMenuAction", new ActivatePopupMenuAction(comp, popupMenu));

        return popupMenu;
    }

    class ActivatePopupMenuAction extends AbstractAction {
		private static final long serialVersionUID = 3925663480989462160L;
		Component invoker;
        JPopupMenu popup;
        
        protected ActivatePopupMenuAction(Component comp, JPopupMenu popupMenu) {
            super("ActivatePopupMenu"); // the name for the action
            this.invoker = comp;
            this.popup = popupMenu;
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
        	LOG.fine("event:"+e);
            Dimension invokerSize = getSize();
            Dimension popupSize = popup.getPreferredSize();
            popup.show(invoker, 
            		(invokerSize.width - popupSize.width) / 2,
            		(invokerSize.height - popupSize.height) / 2
            	);
        }
    }
    // </snip> PopupMenu

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
    		if(current!=null) {
    			current.dispose();
    		}
    		currentDemoFrame = frame;
    		return frame;
		}
		return null;
	}

	JXPanel content = null;
	JTabbedPane tabbedpane = null;
	JXPanel currentController = null;
	// not public, used in DemoAction
	void addController(JXPanel controlPane) {
		if(currentController!=null) tabbedpane.remove(currentController);
		currentController = controlPane;
		tabbedpane.add("controller", currentController);
		tabbedpane.setSelectedComponent(currentController);
		tabbedpane.invalidate();
    	pack();
	}

}
