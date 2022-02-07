package com.klst.swingx;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.icon.PauseIcon;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.StopIcon;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

public class MenuTreeTableModel extends AbstractTreeTableModel {

	public enum Category {CHOOSERS, CONTAINERS, CONTROLS, DATA, DECORATORS, FUNCTIONALITY, GRAPHICS, TEXT, VISUALIZATION}
	
	// category => color
	@SuppressWarnings("serial")
	public static Map<Category, Color> categoryToColor = new HashMap<Category, Color>() {{
	    put(Category.CHOOSERS, Color.CYAN);
	    put(Category.CONTAINERS, Color.PINK); // examples: ss3:JXCollapsiblePane
	    put(Category.CONTROLS, Color.RED); // examples: ButtonDemo/ss2, ss3:JXDatePicker, LoginPaneDemo
	    put(Category.DATA, Color.BLUE); // examples: TableDemo/ss2, ss3:XTableDemo, XTreeDemo
	    put(Category.DECORATORS, Color.MAGENTA); // examples: ProgressBarDemo/ss2 ss3:JXBusyLabel
	    put(Category.FUNCTIONALITY, Color.YELLOW); // examples: ss3:AutoComplete, Decorator ????
	    put(Category.GRAPHICS, Color.ORANGE); // examples: ss3:BlendComposite
	    put(Category.TEXT, Color.GREEN);
	    put(Category.VISUALIZATION, Color.GRAY);
	}};

	// swingSetVersion,color => smallIcon
	public static Icon getSmallIcon(int ssv, Color color) {
		if(ssv==2) return new StopIcon(SizingConstants.SMALL_ICON, color);
		if(ssv==3) return new PlayIcon(SizingConstants.SMALL_ICON, color);
		return new PauseIcon(SizingConstants.SMALL_ICON, color);		
	}
	// swingSetVersion,category => smallIcon
	public static Icon getSmallIcon(int ssv, Category category) {
		if(ssv==2) return new StopIcon(SizingConstants.SMALL_ICON, categoryToColor.get(category));
		if(ssv==3) return new PlayIcon(SizingConstants.SMALL_ICON, categoryToColor.get(category));
		return new PauseIcon(SizingConstants.SMALL_ICON);		
	}

    private static final String[] columnNameKeys = 
    	{ Action.NAME       //                 expl. ButtonDemo
    	, "className"       // full className, expl. swingset.ButtonDemo
    	, Action.SHORT_DESCRIPTION // for tooltip
    	, Action.LONG_DESCRIPTION
    	, "swingSetVersion" // 2 or 3
    	, "category"        // data, controller, ...
//    	, "ad_menu_id"      // wg. gossip ... weitere
    	};

    /*
     * static DemoMenuAction getRootAction() :
     * DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(DemoMenuAction.getRootAction());
     */
    private static final MenuTreeTableModel INSTANCE = new MenuTreeTableModel(
    		new DefaultMutableTreeNode(DemoMenuAction.getRootAction())
    		);
    /**
     * @return the singleton
     */
    public static MenuTreeTableModel getInstance() {
        return INSTANCE;
    }
    /**
     * Constructs an {@code MenuTreeTableModel} with the specified root node.
     * 
     * @param menuRoot root node
     */
    public MenuTreeTableModel(TreeNode menuRoot) {
    	super(menuRoot);
    }

	/**
	 * number of columns in the model
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnCount() {
		return columnNameKeys.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValueAt(Object node, int column) {
		DemoMenuAction ma = (DemoMenuAction) node;
        Object o = null;
        
        switch (column) {
        case 0:
        	o = ma.getValue(Action.NAME);
            break;
        case 1:
            o = ma.className;
            break;
        case 2: // SHORT_DESCRIPTION // for tooltip
            break;
        case 3: // LONG_DESCRIPTION
            break;
        case 4: // swingSetVersion
        	if (ma.className.startsWith("swingset.")) {
        		o = Integer.valueOf(2);
        	} else {
        		o = Integer.valueOf(3);
        	}
            break;
        case 5: // category TODO
            break;
        default:
            //does nothing
            break;
        }              
        return o;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getChild(Object parent, int index) {
    	if(parent==this.getRoot()) {            		
//	    	LOG.info("index="+index+", parent is root:"+parent);
    		if(index==0) return DemoMenuAction.getSS2Action();
    		if(index==1) return DemoMenuAction.getSS3Action();
    	}
//    	LOG.info("nicht root ----------- parent:"+parent);
    	if(parent==DemoMenuAction.getSS2Action()) {
//	    	LOG.info("index="+index+", parent is SS2Action:"+parent);
    		if(index>=0 && index<DemoMenuAction.ss2Actions.size()) return DemoMenuAction.ss2Actions.get(index);
    	}
    	if(parent==DemoMenuAction.getSS3Action()) {
//	    	LOG.info("index="+index+", parent is SS3Action:"+parent);
    		if(index>=0 && index<DemoMenuAction.ss3Actions.size()) return DemoMenuAction.ss3Actions.get(index);
    	}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount(Object parent) {
    	if(parent==this.getRoot()) return 2;
//    	LOG.info("nicht root ----------- parent:"+parent);
    	if(parent==DemoMenuAction.getSS2Action()) return DemoMenuAction.ss2Actions.size();
    	if(parent==DemoMenuAction.getSS3Action()) return DemoMenuAction.ss3Actions.size();
//    	LOG.info("return 0 ------ bei parent:"+parent);
		return 0;
	}

	/**
	 * index of child in parent
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
//    	LOG.info("parent:"+parent);
    	if(parent==this.getRoot() && child==DemoMenuAction.getSS2Action()) return 0;
    	if(parent==this.getRoot() && child==DemoMenuAction.getSS3Action()) return 1;
    	if(parent==DemoMenuAction.getSS2Action()) {
    		return DemoMenuAction.ss2Actions.indexOf(child);
    	}
    	if(parent==DemoMenuAction.getSS3Action()) {
    		return DemoMenuAction.ss3Actions.indexOf(child);
    	}
		return -1; // either parent or child don't belong to this tree model
	}

}
