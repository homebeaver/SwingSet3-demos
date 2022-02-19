package io.github.homebeaver.swingset.demo;

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

//import com.klst.swingx.DemoMenuAction;

//import com.klst.swingx.DemoMenuAction;
//import com.klst.swingx.MenuTreeTableModel;
//import com.klst.swingx.MenuTreeTableModel.Category;

public class DemoTreeTableModel extends AbstractTreeTableModel {

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
     * static DemoAction getRootAction() :
     * DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(DemoAction.getRootAction());
     */
    private static final DemoTreeTableModel INSTANCE = new DemoTreeTableModel(
    		new DefaultMutableTreeNode(new DemoAction((String)null, "Demos"))
    		);
    /**
     * @return the singleton
     */
    public static DemoTreeTableModel getInstance() {
        return INSTANCE;
    }
    /**
     * Constructs an {@code MenuTreeTableModel} with the specified root node.
     * 
     * @param menuRoot root node
     */
    public DemoTreeTableModel(TreeNode menuRoot) {
    	super(menuRoot);
    	((DefaultMutableTreeNode)getRoot()).add(new DefaultMutableTreeNode(DemoAction.getSS2Action()));
    	((DefaultMutableTreeNode)getRoot()).add(new DefaultMutableTreeNode(DemoAction.getSS3Action()));
    	// die arrays ss2Actions und ss3Actions initialisieren
    	DemoAction.getSS2Actions();
    	DemoAction.getSS3Actions();
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
		DemoAction ma = (DemoAction) node;
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
    		if(index==0) return DemoAction.getSS2Action();
    		if(index==1) return DemoAction.getSS3Action();
    	}
//    	LOG.info("nicht root ----------- parent:"+parent);
    	if(parent==DemoAction.getSS2Action()) {
//	    	LOG.info("index="+index+", parent is SS2Action:"+parent);
//    		if(index>=0 && index<DemoAction.ss2Actions.size()) 
    		return DemoAction.ss2Actions.get(index);
    	}
    	if(parent==DemoAction.getSS3Action()) {
//	    	LOG.info("index="+index+", parent is SS3Action:"+parent);
//    		if(index>=0 && index<DemoAction.ss3Actions.size()) return 
    		DemoAction.ss3Actions.get(index);
    	}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount(Object parent) {
    	if(parent==this.getRoot()) return 2;
    	if(parent==DemoAction.getSS2Action()) return DemoAction.ss2Actions.size();
    	if(parent==DemoAction.getSS3Action()) return DemoAction.ss3Actions.size();
		return 0;
	}

	/**
	 * index of child in parent
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public int getIndexOfChild(Object parent, Object child) {
    	if(parent==this.getRoot() && child==DemoAction.getSS2Action()) return 0;
    	if(parent==this.getRoot() && child==DemoAction.getSS3Action()) return 1;
    	if(parent==DemoAction.getSS2Action()) {
    		return DemoAction.ss2Actions.indexOf(child);
    	}
    	if(parent==DemoAction.getSS3Action()) {
    		return DemoAction.ss3Actions.indexOf(child);
    	}
		return -1; // either parent or child don't belong to this tree model
	}

}
