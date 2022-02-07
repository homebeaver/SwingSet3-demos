package com.klst.swingx;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXFrame.StartPosition;

import com.klst.swingx.MenuTreeTableModel.Category;

import swingset.AbstractDemo;
import swingset.ButtonDemo;
import swingset.ColorChooserDemo;
import swingset.ComboBoxDemo;
import swingset.FileChooserDemo;
import swingset.HtmlDemo;
import swingset.InternalFrameDemo;
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

public class DemoMenuAction extends AbstractAction {

	private static final long serialVersionUID = 4948734814912303059L;
	private static final Logger LOG = Logger.getLogger(DemoMenuAction.class.getName());

	Class<?> democlass = null;
	String className = null;
	AbstractButton jToggleButton = null;

/* super (AbstractAction) ctors:

    public AbstractAction() {
    public AbstractAction(String name) {    putValue(Action.NAME, name);
>>  public AbstractAction(String name, Icon icon) {        this(name);        putValue(Action.SMALL_ICON, icon);

 */
    /**
     * Creates an {@code DemoMenuAction} with the specified name/Value({@code Action.NAME}) and icons.
     *
     * @param className full className
     * @param name the name ({@code Action.NAME}) for the action; a
     *        value of {@code null} is ignored
     * @param smallIcon the icon ({@code Action.SMALL_ICON}) for the action; a
     *        value of {@code null} is ignored
     * @param icon the large icon ({@code Action.LARGE_ICON_KEY}) for the action; a
     *        value of {@code null} is ignored
     */
	public DemoMenuAction(String className, String name, Icon smallIcon, Icon icon) {
		super(name, smallIcon);
		this.className = className;
        if(icon!=null) super.putValue(Action.LARGE_ICON_KEY, icon);
	}
	public DemoMenuAction(String className, String name, int ssv, MenuTreeTableModel.Category cat, Icon icon) {
		this(className, name, MenuTreeTableModel.getSmallIcon(ssv, cat), icon);
	}
	public DemoMenuAction(String className, String name, int ssv, MenuTreeTableModel.Category cat) {
		this(className, name, MenuTreeTableModel.getSmallIcon(ssv, cat), null);
	}
	public DemoMenuAction(String className, String name) {
		this(className, name, null, null);
	}
	public DemoMenuAction(String className, String name, Icon icon) {
		this(className, name, null, icon);
	}
    /**
     * Creates an {@code DemoMenuAction} with the specified name/Value({@code Action.NAME}) and icons.
     *
     * @param democlass the demo class
     * @param name the name ({@code Action.NAME}) for the action; a
     *        value of {@code null} is ignored
     * @param smallIcon the icon ({@code Action.SMALL_ICON}) for the action; a
     *        value of {@code null} is ignored
     * @param icon the large icon ({@code Action.LARGE_ICON_KEY}) for the action; a
     *        value of {@code null} is ignored
     */
	public DemoMenuAction(Class<?> democlass, String name, Icon smallIcon, Icon icon) {
		super(name, smallIcon);
		this.democlass = democlass;
        if(icon!=null) super.putValue(Action.LARGE_ICON_KEY, icon);
        
        // SHORT_DESCRIPTION will setToolTipText in addActionToToolBar
        if(this.democlass!=null) {
            String key = this.democlass.getSimpleName() + '.' + "tooltip";
            String short_description = StaticUtilities.getResourceAsString(key, null);
            super.putValue(Action.SHORT_DESCRIPTION, short_description);
        }
	}
	public DemoMenuAction(Class<?> democlass, String name, int ssv, MenuTreeTableModel.Category cat, Icon icon) {
		this(democlass, name, MenuTreeTableModel.getSmallIcon(ssv, cat), icon);
	}
	public DemoMenuAction(Class<?> democlass, String name) {
		this(democlass, name, null, null);
	}
	public DemoMenuAction(Class<?> democlass, String name, Icon icon) {
		this(democlass, name, null, icon);
	}

	void setToggleButton(AbstractButton toggleButton) {
		jToggleButton = toggleButton;
	}
	AbstractButton getToggleButton() {
		return jToggleButton;
	}

//    @Override
//    public String toString() {
//    	return (String)super.getValue(Action.NAME);
//    }

	// never used
//    private ColumnFactory getColumnFactory() {
//        // <snip>JXTreeTable column customization
//        // configure and install a custom columnFactory, arguably data related ;-)
//        ColumnFactory factory = new ColumnFactory() {
////            String[] columnNameKeys = { "componentType", "componentName", "componentLocation", "componentSize" };
//
//            @Override
//            public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
//                super.configureTableColumn(model, columnExt);
//                if (columnExt.getModelIndex() < columnNameKeys.length) {
////                    columnExt.setTitle(DemoUtils.getResourceString(TreeTableDemo.class, columnNameKeys[columnExt.getModelIndex()]));
//                    columnExt.setTitle(columnNameKeys[columnExt.getModelIndex()]);
//                }
//            }
//            
//        };
//        return factory;
//        // </snip>
//    }

	static ArrayList<DemoMenuAction> ss2Actions = new ArrayList<DemoMenuAction>();
	static ArrayList<DemoMenuAction> ss3Actions = new ArrayList<DemoMenuAction>();
    public static TreeNode createTree() {
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode(getRootAction());
    	
    	DefaultMutableTreeNode ss2 = new DefaultMutableTreeNode(getSS2Action());
    	DefaultMutableTreeNode ss3 = new DefaultMutableTreeNode(getSS3Action());
    	root.add(ss2);
    	root.add(ss3);

    	ss2Actions.add(new DemoMenuAction(InternalFrameDemo.class, "InternalFrame", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(InternalFrameDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ButtonDemo.class, "Button", 2, Category.CONTROLS, StaticUtilities.createImageIcon(ButtonDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ComboBoxDemo.class, "ComboBox", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(ComboBoxDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ColorChooserDemo.class, "ColorChooser", 2, Category.CHOOSERS, StaticUtilities.createImageIcon(ColorChooserDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(FileChooserDemo.class, "FileChooser", 2, Category.CHOOSERS, StaticUtilities.createImageIcon(FileChooserDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(HtmlDemo.class, "Html", 2, Category.TEXT, StaticUtilities.createImageIcon(HtmlDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ListDemo.class, "List", 2, Category.DATA, StaticUtilities.createImageIcon(ListDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(OptionPaneDemo.class, "OptionPane", 2, Category.CONTROLS, StaticUtilities.createImageIcon(OptionPaneDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ProgressBarDemo.class, "ProgressBar", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ProgressBarDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ScrollPaneDemo.class, "ScrollPane", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ScrollPaneDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(SliderDemo.class, "Slider", 2, Category.DECORATORS, StaticUtilities.createImageIcon(SliderDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(SplitPaneDemo.class, "SplitPane", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(SplitPaneDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(TabbedPaneDemo.class, "TabbedPane", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(TabbedPaneDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(TableDemo.class, "Table", 2, Category.DATA, StaticUtilities.createImageIcon(TableDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(ToolTipDemo.class, "ToolTip", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ToolTipDemo.ICON_PATH)));
    	ss2Actions.add(new DemoMenuAction(TreeDemo.class, "Tree", 2, Category.DATA, StaticUtilities.createImageIcon(TreeDemo.ICON_PATH)));
    	ss2Actions.forEach( action -> {
        	ss2.add(new DefaultMutableTreeNode(action));
    	});

    	// category CONTROLS:
//		ss2 = new DemoMenuAction((String)null, "SwingSet2", 2, null, null);
    	ss3Actions.add(new DemoMenuAction("org.jdesktop.swingx.demos.loginpane.LoginToDBPaneDemo", "LoginToDBPane", 3, Category.CONTROLS));
    	ss3Actions.add(new DemoMenuAction("org.jdesktop.swingx.demos.loginpane.LoginPaneDemo", "LoginPane", 3, Category.CONTROLS));
    	// category DATA:
    	ss3Actions.add(new DemoMenuAction("org.jdesktop.swingx.demos.table.XTableDemo", "XTable", 3, Category.DATA));
    	ss3Actions.add(new DemoMenuAction("org.jdesktop.swingx.demos.tree.XTreeDemo", "XTree", 3, Category.DATA));
    	ss3Actions.add(new DemoMenuAction("org.jdesktop.swingx.demos.treetable.TreeTableDemo", "XTreeTable",  3, Category.DATA));
    	ss3Actions.forEach( action -> {
        	ss3.add(new DefaultMutableTreeNode(action));
    	});

        return root;
    }
    static DemoMenuAction root = null;
    static DemoMenuAction ss2 = null;
    static DemoMenuAction ss3 = null;
    public static DemoMenuAction getRootAction() {
    	if(root==null) {
    		root = new DemoMenuAction((String)null, "Demo");
    	}
    	return root;
    }
    public static DemoMenuAction getSS2Action() {
    	if(ss2==null) {
    		ss2 = new DemoMenuAction((String)null, "SwingSet2", 2, null, null);
    	}
    	return ss2;
    }
    public static DemoMenuAction getSS3Action() {
    	if(ss3==null) {
    		ss3 = new DemoMenuAction((String)null, "SwingSet3", 3, null, null);
    	}
    	return ss3;
    }

    /* implement method public void actionPerformed(ActionEvent e)
     * defined in interface ActionListener extends EventListener
     */
	@Override
	public void actionPerformed(ActionEvent e) {
//		Object o = e.getSource();
//		LOG.info("Source:"+o + "\n "+this);
//		if(o instanceof WindowFrame.ToggleButtonToolBar) { // ist nie der Fall????????
//			JToolBar tb= (JToolBar)o;
//			LOG.info("JToolBar:\n "+tb);
//		}
//		if(o instanceof JMenu) {
//			JMenu menu = (JMenu)o;
//			LOG.info("Action:"+menu.getAction());
//		}
//		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor((Component)o);
//		LOG.info("topFrame:"+topFrame); //null
//		JFrame f1 = (JFrame) SwingUtilities.windowForComponent((Component)o);
//		LOG.info("topFrame f1:"+f1); //null
//		JFrame f2 = (JFrame) SwingUtilities.getWindowAncestor((Component)o);
//		LOG.info("topFrame f2:"+f2); //null
//		MenuElement me= (MenuElement)o;
//		Component comp = me.getComponent();
//		topFrame = (JFrame) SwingUtilities.getWindowAncestor(comp);
//		LOG.info("topFrame:"+topFrame + ", comp:"+comp); //null
//		JFrame f3 = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, comp);
//		JFrame f4 = (JFrame) SwingUtilities.getRoot(comp);
////		JFrame f5 = (JFrame) 
//				SwingUtilities.getRootPane(comp); //.getParent();
//		LOG.info("topFrame f3:"+f3); //null
//		LOG.info("topFrame f4:"+f4); //null
//		LOG.info("topFrame JRootPane:"+SwingUtilities.getRootPane(comp)); //null
		if(democlass==null) {
			if(className==null) {
				// node ohne demo klasse, root, ss2, ss3, ... ==> nix tun
				return;
			}
			democlass = loadDemo(className);
		}
		RootFrame rf = RootFrame.getInstance();
		
    	// ss2: deselect all toolbar buttons except this action button 
    	JToolBar tb = rf.getToolBar();
    	Component[] cs = tb.getComponents();
    	for(int i=0;i<cs.length;i++) {
			JToggleButton b = (JToggleButton)cs[i];
			if(b==this.getToggleButton()) {
    			LOG.info("swingset2 button "+i + " selected, "+super.getValue(Action.NAME));        		
				b.setSelected(true);
			} else {
				b.setSelected(false);
			}
    	}

		WindowFrame frame = rf.makeFrame(rf, 1, null);
    	if(frame!=null) {
    		frame.setStartPosition(StartPosition.CenterInScreen);
    		AbstractDemo demo = null;
			try {
				demo = getInstanceOf(democlass, frame, rf); // ctor 
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			rf.addController(demo.getControlPane()); // remove and add a controller
        	
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	}
	}

    /**
     * Loads a demo from a classname, but does not instantiate
     */
    private Class<?> loadDemo(String classname) {
    	// setStatus("loading" + classname);
        Class<?> demoClass = null;
        try {
            demoClass = Class.forName(classname); // throws ClassNotFoundException
//            LOG.info("_____________ getBundle ...");
//            ResourceBundle bundle = ResourceBundle.getBundle(classname);
//            LOG.info("_____________ Bundle:"+bundle);
/* findet TreeTableDemo.properties nicht
 * auch nicht TreeTableDemo_de_DE.properties
 * auch nicht TreeTableDemo_de.properties
 * auch nicht TreeTableDemo_de.properties mit getBundle(classname, Locale.GERMAN);
WARNUNG: Error occurred loading class: org.jdesktop.swingx.demos.treetable.TreeTableDemo
java.util.MissingResourceException: Can't find bundle for base name org.jdesktop.swingx.demos.treetable.TreeTableDemo, locale de_DE
	at java.base/java.util.ResourceBundle.throwMissingResourceException(ResourceBundle.java:2045)
	at java.base/java.util.ResourceBundle.getBundleImpl(ResourceBundle.java:1683): baseBundle == null
	at java.base/java.util.ResourceBundle.getBundleImpl(ResourceBundle.java:1586): return getBundleImpl(callerModule, unnamedModule, baseName, locale, control);
	at java.base/java.util.ResourceBundle.getBundleImpl(ResourceBundle.java:1549): return getBundleImpl(baseName, locale, caller, caller.getClassLoader(), control);
	at java.base/java.util.ResourceBundle.getBundle(ResourceBundle.java:858) : return getBundleImpl(baseName, Locale.getDefault(), caller, getDefaultControl(caller, baseName));
	at com.klst.swingx.DemoMenuAction.loadDemo(DemoMenuAction.java:400)

 */
        } catch (Exception e) {
        	LOG.warning("Error occurred loading class: " + classname);
            e.printStackTrace();
        }
        return demoClass;
    }

	private AbstractDemo getInstanceOf(Class<?> demoClass, Frame frame, RootFrame rf) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<?> demoConstructor = demoClass.getConstructor(new Class[] { Frame.class }); // throws
																								// NoSuchMethodException,
																								// SecurityException
		AbstractDemo demo = (AbstractDemo) demoConstructor.newInstance(new Object[] { frame });
		demo.setLocale(rf.getLocale());
		return demo;
	}

}
