package io.github.homebeaver.swingset.demo;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.treetable.TreeTableModel;

import io.github.homebeaver.swingset.demo.DemoAction.Category;

public class DemoJXTasks extends JXTaskPane {

	private static final long serialVersionUID = -5696726866113684525L;
	private static final Logger LOG = Logger.getLogger(DemoJXTasks.class.getName());
    private static DemoJXTasks INSTANCE = null;
    /**
     * @return the singleton
     */
    public static DemoJXTasks getInstance() {
    	if(INSTANCE==null) INSTANCE = new DemoJXTasks();
        return INSTANCE;
    }
    
    public static JXTaskPaneContainer getTaskPaneContainer() {
    	return getInstance().tpc;
    }
    
    private TreeTableModel model = DemoTreeTableModel.getInstance();
    private JXTaskPaneContainer tpc = new JXTaskPaneContainer();
    private Map<Category, JXTaskPane> categoryToTask = new HashMap<Category, JXTaskPane>();
    
    private DemoJXTasks() {
    	super();
    	Object r = model.getRoot();
    	int n = model.getChildCount(r);
    	LOG.info("------------------- "+r + " with "+n+" childs.");
    	for(int i=0; i<n; i++) {
    		Object c = model.getChild(r, i);
        	LOG.info("------------------- "+c + " with "+model.getChildCount(c)+" childs.");
        	initGroups((DemoAction)c);
    	}
    }
    
    private void initGroups(DemoAction da) {
    	int n = model.getChildCount(da);
    	int cols = model.getColumnCount();
    	for(int c=0; c<cols; c++) {
    		String colName = model.getColumnName(c);
        	Object colValue = model.getValueAt(da, c);
        	LOG.info(colName+"/"+c + " : "+colValue);
    	}
    	for(int i=0; i<n; i++) {
    		Object o = model.getChild(da, i);
    		DemoAction ca = (DemoAction)o;
//        	LOG.info(" "+ca + " with "+model.getChildCount(ca)+" childs.");
//        	LOG.info(model.getValueAt(ca, 1) + " "+ca + " category="+model.getValueAt(ca, 5));
    		Category cat = ca.getCategory();
//        	LOG.info(ca.getName() + " "+ca + " category="+cat);
        	if(!categoryToTask.containsKey(cat)) {
        		JXTaskPane group = new JXTaskPane(cat.toString());
        		group.setName(cat.toString());
        		// set all groups collapsed but DATA:
        		group.setCollapsed(cat!=DemoAction.Category.DATA);
        		categoryToTask.put(cat, group);
        		tpc.add(group);
        	}
        	JXTaskPane group = categoryToTask.get(cat);

            // in AbstractButton.setIconFromAction ist die Strategie:
            // zuerst Action.LARGE_ICON_KEY, wenn keine da dann Action.SMALL_ICON
            // daher:
            //group.add(ca); // Action ca, shows SwingLargeIconKey
            AbstractActionExt aae = new AbstractActionExt(ca.getName(), ca.getSmallIcon()) {

				@Override
				public void actionPerformed(ActionEvent e) {
					ca.actionPerformed(e);
				}
            	
            };
            aae.setActionCommand(ca.getActionCommand());
            aae.setShortDescription(ca.getShortDescription());
            group.add(aae);
    	}
    }

}
