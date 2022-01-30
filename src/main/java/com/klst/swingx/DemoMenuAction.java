package com.klst.swingx;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToolBar;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;
import org.jdesktop.swingx.icon.PlayIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.StopIcon;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.StaticUtilities;

public class DemoMenuAction extends AbstractAction {

	private static final long serialVersionUID = 4948734814912303059L;
	private static final Logger LOG = Logger.getLogger(DemoMenuAction.class.getName());

	Class<?> democlass = null;
	String className = null;
	AbstractButton jToggleButton = null;

/* super ctors:

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

    private static String[] columnNameKeys = 
    	{ Action.NAME       //                 expl. ButtonDemo
    	, "className"       // full className, expl. swingset.ButtonDemo
    	, Action.SHORT_DESCRIPTION // for tooltip
    	, Action.LONG_DESCRIPTION
    	, "swingSetVersion" // 2 or 3
    	, "category"        // data, controller, ...
//    	, "ad_menu_id"      // wg. gossip ... weitere
    	};

    private ColumnFactory getColumnFactory() {
        // <snip>JXTreeTable column customization
        // configure and install a custom columnFactory, arguably data related ;-)
        ColumnFactory factory = new ColumnFactory() {
//            String[] columnNameKeys = { "componentType", "componentName", "componentLocation", "componentSize" };

            @Override
            public void configureTableColumn(TableModel model, TableColumnExt columnExt) {
                super.configureTableColumn(model, columnExt);
                if (columnExt.getModelIndex() < columnNameKeys.length) {
//                    columnExt.setTitle(DemoUtils.getResourceString(TreeTableDemo.class, columnNameKeys[columnExt.getModelIndex()]));
                    columnExt.setTitle(columnNameKeys[columnExt.getModelIndex()]);
                }
            }
            
        };
        return factory;
        // </snip>
    }

	static Icon SS2_ICON = new StopIcon(SizingConstants.SMALL_ICON);
	static Icon GO2_ICON = new StopIcon(SizingConstants.SMALL_ICON, Color.GREEN);
	static Icon SS3_ICON = new PlayIcon(SizingConstants.SMALL_ICON);
	static Icon GO3_ICON = new PlayIcon(SizingConstants.SMALL_ICON, Color.GREEN);
	static Icon SS3DATA_ICON = new PlayIcon(SizingConstants.SMALL_ICON, Color.BLUE);
	static ArrayList<DemoMenuAction> ss3Actions = new ArrayList<DemoMenuAction>();
    public static TreeNode createTree() {
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode(getRootAction());
    	
    	DefaultMutableTreeNode ss2 = new DefaultMutableTreeNode(getSS2Action());
    	DefaultMutableTreeNode ss3 = new DefaultMutableTreeNode(getSS3Action());
    	root.add(ss2);
    	root.add(ss3);

    	ss3Actions.add(new DemoMenuAction(XTreeDemo.class, "XTree", SS3DATA_ICON, StaticUtilities.createImageIcon(XTreeDemo.class, XTreeDemo.ICON_PATH)));
    	ss3Actions.add(new DemoMenuAction(TreeTableDemo.class, "XTreeTable", SS3DATA_ICON, StaticUtilities.createImageIcon(TreeTableDemo.class, TreeTableDemo.ICON_PATH)));
    	for(int i=0;i<ss3Actions.size();i++) {
    		LOG.info("ss3Actions "+i + ":"+ss3Actions.get(i));
    		ss3.add(new DefaultMutableTreeNode(ss3Actions.get(i)));
    	}
//    	ss3Actions.forEach( action -> {
//        	ss3.add(new DefaultMutableTreeNode(action));
//    	});

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
    		ss2 = new DemoMenuAction((String)null, "SwingSet2", SS2_ICON);
    	}
    	return ss2;
    }
    public static DemoMenuAction getSS3Action() {
    	if(ss3==null) {
    		ss3 = new DemoMenuAction((String)null, "SwingSet3", SS3_ICON);
    	}
    	return ss3;
    }
    public static TreeTableModel createMenuModel(TreeNode root) {
    	LOG.info("TreeNode root:"+root);
    	return new AbstractTreeTableModel(root) {

			@Override
			public int getColumnCount() {
				return columnNameKeys.length;
			}

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
                case 4: // swingSetVersion TODO
                	if (ma.className.startsWith("swingset.")) {
                		o = Integer.valueOf(2);
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

			@Override
			public Object getChild(Object parent, int index) {
            	if(parent==this.getRoot()) {            		
    		    	LOG.info("index="+index+", parent is root:"+parent);
            		if(index==0) return getSS2Action();
            		if(index==1) return getSS3Action();
            	}
//		    	LOG.info("nicht root ----------- parent:"+parent);
            	if(parent==getSS3Action()) {
    		    	LOG.info("index="+index+", parent is SS3Action:"+parent);
            		if(index>=0 && index<ss3Actions.size()) return ss3Actions.get(index);
            	}
				return null;
			}

			@Override
			public int getChildCount(Object parent) {
//		    	LOG.info("parent:"+parent);
            	if(parent==this.getRoot()) return 2;
		    	LOG.info("nicht root ----------- parent:"+parent);
//            	if(parent==getRootAction()) return 2;
            	if(parent==getSS3Action()) return ss3Actions.size();
		    	LOG.info("return 0 ------ bei parent:"+parent);
				return 0;
			}

			@Override
			public int getIndexOfChild(Object parent, Object child) {
		    	LOG.info("parent:"+parent);
            	if(parent==getRootAction() && child==getSS2Action()) return 0;
            	if(parent==getRootAction() && child==getSS3Action()) return 1;
            	if(parent==getSS3Action()) {
            		return ss3Actions.indexOf(child);
            	}
				// TODO getSS2Action Auto-generated method stub
				return -1;
			}
    		
    	};
    }
    

    /* implement method public void actionPerformed(ActionEvent e)
     * defined in interface ActionListener extends EventListener
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		LOG.info("Source:"+o);
		if(o instanceof JToolBar) {
			JToolBar tb= (JToolBar)o;
		}
//		JToolBar tb= (JToolBar) e.getSource(); // TODO cast nicht möglich !!!!!!!!!!!!!!!!!!!!
//        	class com.klst.swingx.WindowFrame$ToggleButtonToolBar$1 cannot be cast to class javax.swing.JToolBar 
//        	(com.klst.swingx.WindowFrame$ToggleButtonToolBar$1 is in unnamed module of loader 'app'; javax.swing.JToolBar is in module java.desktop of loader 'bootstrap')
		// TODO Auto-generated method stub		
	}

}
