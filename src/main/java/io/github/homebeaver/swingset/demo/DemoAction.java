package io.github.homebeaver.swingset.demo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.demos.errorpane.ErrorPaneDemo;
import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.icon.PauseIcon;
import org.jdesktop.swingx.icon.PlayIconSolid;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.StopIcon;

import swingset.AbstractDemo;
import swingset.ButtonDemo;
import swingset.ColorChooserDemo;
import swingset.ComboBoxDemo;
import swingset.FileChooserDemo;
import swingset.HtmlDemo;
import swingset.InternalFrameDemo;
import swingset.ListDemo;
import swingset.ProgressBarDemo;
import swingset.ScrollPaneDemo;
import swingset.SliderDemo;
import swingset.SplitPaneDemo;
import swingset.StaticUtilities;
import swingset.TabbedPaneDemo;
import swingset.TableDemo;
import swingset.TextAndMnemonicUtils;
import swingset.ToolTipDemo;

/**
 * DemoAction extends <code>AbstractActionExt</code> which includes toggle or group states.
 * <p>
 * It implements <code>Action</code> and <code>ItemListener</code> interface.
 * 
 * Each DemoAction represents a demo, either SwingSet2 or SwingSet3 demo,
 * which can be loaded and started in an extra frame.
 */
//                                                                        interface ItemListener extends EventListener
//                        class AbstractActionExt extends AbstractAction implements ItemListener
public class DemoAction extends AbstractActionExt {

	private static final Logger LOG = Logger.getLogger(DemoAction.class.getName());

	/** A Category of Demos */
	public enum Category {
		CHOOSERS, 
		CONTAINERS, 
		CONTROLS, 
		DATA, 
		DECORATORS, 
		FUNCTIONALITY, 
		GRAPHICS, 
		TEXT, 
		VISUALIZATION};
	
	// category => color
	@SuppressWarnings("serial")
	public static Map<Category, Color> categoryToColor = new HashMap<Category, Color>() {{
	    put(Category.CHOOSERS, Color.CYAN);
	    put(Category.CONTAINERS, Color.PINK); // examples: ss3:JXCollapsiblePane
	    put(Category.CONTROLS, Color.RED); // examples: ButtonDemo/ss2, ss3:JXDatePicker, LoginPaneDemo
	    put(Category.DATA, Color.BLUE); // examples: TableDemo/ss2, ss3:XTableDemo, XTreeDemo
	    put(Category.DECORATORS, Color.MAGENTA); // examples: ProgressBarDemo/ss2 ss3:JXBusyLabel
	    put(Category.FUNCTIONALITY, Color.YELLOW); // examples: ss3:SearchDemo
	    put(Category.GRAPHICS, Color.ORANGE); // examples: ss3:BlendComposite
	    put(Category.TEXT, Color.GREEN);
	    put(Category.VISUALIZATION, Color.GRAY);
	}};

	/**
	 * get SmallIcon for swingSetVersion,color
	 * @param ssv swingSetVersion
	 * @param color Color of the icon
	 * @return Icon
	 */
	// swingSetVersion,color => smallIcon
	public static Icon getSmallIcon(int ssv, Color color) {
		RadianceIcon icon;
		if(ssv==2) {
			icon = StopIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
			icon.setColorFilter(iconColor -> color);
			return icon;
		}
		if(ssv==3) {
			icon = PlayIconSolid.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
			icon.setColorFilter(iconColor -> color);
			return icon;
		}
		icon = PauseIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
		icon.setColorFilter(iconColor -> color);
		return icon;
	}
	/**
	 * get SmallIcon for swingSetVersion,category
	 * @param ssv swingSetVersion
	 * @param category demo Category
	 * @return Icon
	 */
	// swingSetVersion,category => smallIcon
	public static Icon getSmallIcon(int ssv, Category category) {
		RadianceIcon icon;
		if(ssv==2) {
			icon = StopIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
			icon.setColorFilter(iconColor -> categoryToColor.get(category));
			return icon;
		}
		if(ssv==3) {
			icon = PlayIconSolid.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
			icon.setColorFilter(iconColor -> categoryToColor.get(category));
			return icon;
		}
		icon = PauseIcon.of(RadianceIcon.SMALL_ICON, RadianceIcon.SMALL_ICON);
		return icon;
	}

	Class<?> democlass = null;
	String className = null;
	AbstractButton jToggleButton = null;
	Category category = null;

	/* super (AbstractActionExt) ctors:

    public AbstractActionExt() // default
    public AbstractActionExt(AbstractActionExt action) // copy ctor
    public AbstractActionExt(String name)                            // with label
    public AbstractActionExt(String name, Icon icon)                 // with label and smallIcon
    public AbstractActionExt(String name, String command)            // with label and command
    public AbstractActionExt(String name, String command, Icon icon) // label, command and smallIcon

	 */
	
    /**
     * Creates an {@code DemoAction} with the specified name/Value({@code Action.NAME}) and icons.
     *
     * @param className full className
     * @param name the name ({@code Action.NAME}) for the action; a value of {@code null} is ignored
     * @param smallIcon the icon ({@code Action.SMALL_ICON}) for the action; a value of {@code null} is ignored
     * @param icon the large icon ({@code Action.LARGE_ICON_KEY}) for the action; a value of {@code null} is ignored
     */
	public DemoAction(String className, String name, Icon smallIcon, Icon icon) {
		super(name, smallIcon);
		this.className = className;
        if(icon!=null) super.setLargeIcon(icon);
	}
	/**
	 * ctor
	 * @param className String
	 * @param name String
	 * @param icon Icon
	 */
	public DemoAction(String className, String name, Icon icon) {
		this(className, name, null, icon);
	}
	/**
	 * ctor
	 * @param className String
	 * @param name String
	 */
	public DemoAction(String className, String name) {
		this(className, name, null, null);
	}
	/**
	 * ctor
	 * @param className String
	 * @param name String
	 * @param ssv swingSetVersion
	 * @param cat Action Category
	 */
	public DemoAction(String className, String name, int ssv, Category cat) {
		this(className, name, getSmallIcon(ssv, cat), null);
		this.category = cat;
	}
	/**
	 * ctor
	 * @param className String
	 * @param name String
	 * @param ssv swingSetVersion
	 * @param cat Action Category
	 * @param icon Icon
	 */
	public DemoAction(String className, String name, int ssv, Category cat, Icon icon) {
		this(className, name, getSmallIcon(ssv, cat), icon);
		this.category = cat;
	}

    /**
     * Creates an {@code DemoAction} with the specified name/Value({@code Action.NAME}) and icons.
     *
     * @param democlass the demo class
     * @param name the name ({@code Action.NAME}) for the action; a value of {@code null} is ignored
     * @param smallIcon the icon ({@code Action.SMALL_ICON}) for the action; a value of {@code null} is ignored
     * @param icon the large icon ({@code Action.LARGE_ICON_KEY}) for the action; a value of {@code null} is ignored
     */
	public DemoAction(Class<?> democlass, String name, Icon smallIcon, Icon icon) {
		super(name, smallIcon);
		this.democlass = democlass;
        if(icon!=null) super.setLargeIcon(icon);
        
        // SHORT_DESCRIPTION will setToolTipText in addActionToToolBar
        if(this.democlass!=null) {
            String key = this.democlass.getSimpleName() + '.' + "tooltip";
            // TODO use AbstractDemo.getBundleString(String key, String fallback) ! getBundleString not static
            String desc = null;
        	try {
            	// getTextAndMnemonicString throws MissingResourceException
        		desc = TextAndMnemonicUtils.getTextAndMnemonicString(key);
        	} catch (MissingResourceException e) {
        		desc = null;
        	}
            super.setShortDescription(desc);
        }
	}
	/**
	 * convenience ctor
     * @param democlass the demo class
	 * @param name the name ({@code Action.NAME}) for the action
	 * @param icon the large icon 
	 */
	public DemoAction(Class<?> democlass, String name, Icon icon) {
		this(democlass, name, null, icon);
	}
	/**
	 * convenience ctor
     * @param democlass the demo class
	 * @param name the name ({@code Action.NAME}) for the action
	 */
	public DemoAction(Class<?> democlass, String name) {
		this(democlass, name, null, null);
	}
	/**
	 * convenience ctor with swingSetVersion and demo Category
     * @param democlass the demo class
	 * @param name the name ({@code Action.NAME}) for the action
	 * @param ssv swingSetVersion
	 * @param cat demo Category
	 */
	public DemoAction(Class<?> democlass, String name, int ssv, Category cat) {
		this(democlass, name, getSmallIcon(ssv, cat), null);
		this.category = cat;
	}
	/**
	 * convenience ctor with swingSetVersion, demo Category and large icon
     * @param democlass the demo class
	 * @param name the name ({@code Action.NAME}) for the action
	 * @param ssv swingSetVersion
	 * @param cat demo Category
	 * @param icon the large icon 
	 */
	public DemoAction(Class<?> democlass, String name, int ssv, Category cat, Icon icon) {
		this(democlass, name, getSmallIcon(ssv, cat), icon);
		this.category = cat;
	}

    private static DemoAction root = null;
    private static DemoAction ss2 = null;
    private static DemoAction ss3 = null;
    /**
     * Returns Root Action
     * @return DemoAction
     */
    public static DemoAction getRootAction() {
    	if(root==null) {
    		root = new DemoAction((String)null, "Demo");
    	}
    	return root;
    }
    /**
     * get SwingSet2 Action
     * @return DemoAction
     */
    public static DemoAction getSS2Action() {
    	if(ss2==null) {
    		ss2 = new DemoAction((String)null, "SwingSet2", 2, null, null);
    	}
    	return ss2;
    }
    /**
     * get SwingSet3 Action
     * @return DemoAction
     */
    public static DemoAction getSS3Action() {
    	if(ss3==null) {
    		ss3 = new DemoAction((String)null, "SwingSet3", 3, null, null);
    	}
    	return ss3;
    }
	private static ArrayList<DemoAction> ss2Actions = null;
	private static ArrayList<DemoAction> ss3Actions = null;
    /**
     * get List of SwingSet2 Actions
     * @return List
     */
    public static ArrayList<DemoAction> getSS2Actions() {
    	if(ss2Actions==null) {
    		ss2Actions = new ArrayList<DemoAction>();
        	ss2Actions.add(new DemoAction(InternalFrameDemo.class, "InternalFrame", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(InternalFrameDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ButtonDemo.class, "Button", 2, Category.CONTROLS, StaticUtilities.createImageIcon(ButtonDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ComboBoxDemo.class, "ComboBox", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(ComboBoxDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ColorChooserDemo.class, "ColorChooser", 2, Category.CHOOSERS, StaticUtilities.createImageIcon(ColorChooserDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(FileChooserDemo.class, "FileChooser", 2, Category.CHOOSERS, StaticUtilities.createImageIcon(FileChooserDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(HtmlDemo.class, "Html", 2, Category.TEXT, StaticUtilities.createImageIcon(HtmlDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ListDemo.class, "List", 2, Category.DATA, StaticUtilities.createImageIcon(ListDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ErrorPaneDemo.class, "Error+OptionPane", 3, Category.CONTROLS, StaticUtilities.createImageIcon(ErrorPaneDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ProgressBarDemo.class, "ProgressBar", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ProgressBarDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ScrollPaneDemo.class, "ScrollPane", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ScrollPaneDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(SliderDemo.class, "Slider", 2, Category.DECORATORS, StaticUtilities.createImageIcon(SliderDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(SplitPaneDemo.class, "SplitPane", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(SplitPaneDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(TabbedPaneDemo.class, "TabbedPane", 2, Category.CONTAINERS, StaticUtilities.createImageIcon(TabbedPaneDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(TableDemo.class, "Table", 2, Category.DATA, StaticUtilities.createImageIcon(TableDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(ToolTipDemo.class, "ToolTip", 2, Category.DECORATORS, StaticUtilities.createImageIcon(ToolTipDemo.ICON_PATH)));
        	ss2Actions.add(new DemoAction(XTreeDemo.class, "Tree", 3, Category.DATA, StaticUtilities.createImageIcon(XTreeDemo.ICON_PATH)));
    	}
    	return ss2Actions;
    }
    /**
     * get List of SwingSet3 Actions
     * @return List
     */
    public static ArrayList<DemoAction> getSS3Actions() {
    	if(ss3Actions==null) {
    		ss3Actions = new ArrayList<DemoAction>();
        	// category FUNCTIONALITY 4/5 (Autocomplete is DECORATOR):
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.search.SearchDemo", "Search", 3, Category.FUNCTIONALITY));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.highlighter.HighlighterDemo", "Highlighter", 3, Category.FUNCTIONALITY));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.highlighterext.HighlighterExtDemo", "Highlighter (extended)", 3, Category.FUNCTIONALITY));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.prompt.PromptSupportDemo", "PromptSupport", 3, Category.FUNCTIONALITY));
        	// category CONTROLS 10/10:
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.errorpane.ErrorPaneDemo", "ErrorPane", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.imageview.ImageViewDemo", "ImageView", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.hyperlink.HyperlinkDemo", "Hyperlink", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.loginpane.LoginToDBPaneDemo", "LoginToDBPane", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.loginpane.LoginPaneDemo", "LoginPane", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.datepicker.DatePickerDemo", "JXDatePicker", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.monthview.MonthViewDemo", "JXMonthView (basic)", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.monthviewext.MonthViewExtDemo", "JXMonthView (extended)", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.xbutton.XButtonDemo", "JXButton", 3, Category.CONTROLS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.xlabel.XLabelDemo", "XLabel", 3, Category.CONTROLS));
        	// category DECORATORS 6/3: multithumbslider hierhin, da Slider auch in DECORATORS
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.autocomplete.AutoCompleteDemo", "AutoComplete", 3, Category.DECORATORS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.busylabel.BusyLabelDemo", "BusyLabel", 3, Category.DECORATORS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.titledseparator.TitledSeparatorDemo", "JXTitledSeparator", 3, Category.DECORATORS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.painter.PainterDemo", "Painter", 3, Category.DECORATORS));        	
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.multithumbslider.MultiThumbSliderDemo", "MultiThumbSlider", 3, Category.DECORATORS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.svg.MirroringIconDemo", "MirroringIcon", 3, Category.DECORATORS));
        	// category DATA 4/4:
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.xlist.XListDemo", "XList", 3, Category.DATA));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.table.XTableDemo", "XTable", 3, Category.DATA));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.tree.XTreeDemo", "XTree", 3, Category.DATA));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.treetable.TreeTableDemo", "XTreeTable",  3, Category.DATA));
        	// category CONTAINERS 6/6:
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.collapsiblepane.CollapsiblePaneDemo", "JXCollapsiblePane", 3, Category.CONTAINERS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.taskpane.TaskPaneDemo", "TaskPane", 3, Category.CONTAINERS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.multisplitpane.MultiSplitPaneDemo", "JXMultiSplitPane", 3, Category.CONTAINERS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.xpanel.XPanelDemo", "XPanel", 3, Category.CONTAINERS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.titledpanel.TitledPanelDemo", "JXTitledPanel", 3, Category.CONTAINERS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.tipoftheday.TipOfTheDayDemo", "JXTipOfTheDay", 3, Category.CONTAINERS));
        	// category GRAPHICS 1/1:
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.blendcomposite.BlendCompositeDemo", "BlendComposite", 3, Category.GRAPHICS));
        	ss3Actions.add(new DemoAction("org.jdesktop.swingx.demos.graph.GraphDemo", "JXGraph", 3, Category.GRAPHICS));
        	// TODO JXGraph is cat Visualization
    	}
    	return ss3Actions;
    }

	void setToggleButton(AbstractButton toggleButton) {
		jToggleButton = toggleButton;
	}
	AbstractButton getToggleButton() {
		return jToggleButton;
	}

	Category getCategory() {
		return category;
	}

    /* implement method public void actionPerformed(ActionEvent e)
     * defined in interface ActionListener extends EventListener
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(democlass==null) {
			if(className==null) {
				// node ohne demo klasse, root, ss2, ss3, ... ==> nix tun
				return;
			}
			democlass = loadDemo(className);
		}
		MainJXframe rf = MainJXframe.getInstance();
		
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

		DemoJXFrame frame = rf.makeFrame(rf, 1, null);
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

	// defined in interface ItemListener extends EventListener
	public void itemStateChanged(ItemEvent e) {
    	super.itemStateChanged(e); // Updates internal state
    }

    /**
     * Loads a demo from a classname, but does not instantiate
     */
    private Class<?> loadDemo(String classname) {
    	// setStatus("loading" + classname);
        Class<?> demoClass = null;
        try {
            demoClass = Class.forName(classname); // throws ClassNotFoundException
        } catch (Exception e) {
        	LOG.warning("Error occurred loading class: " + classname);
            e.printStackTrace();
        }
        return demoClass;
    }

	private AbstractDemo getInstanceOf(Class<?> demoClass, Frame frame, MainJXframe rf) 
			throws NoSuchMethodException
			, SecurityException
			, InstantiationException
			, IllegalAccessException
			, IllegalArgumentException
			, InvocationTargetException {
		
		Constructor<?> demoConstructor = demoClass.getConstructor(new Class[] { Frame.class }); // throws
																								// NoSuchMethodException,
																								// SecurityException
		AbstractDemo demo = (AbstractDemo) demoConstructor.newInstance(new Object[] { frame });
//		demo.setLocale(rf.getLocale()); // das ist zu spät
//		LOG.info("------------- demo.Locale="+demo.getLocale());
		return demo;
	}

}
