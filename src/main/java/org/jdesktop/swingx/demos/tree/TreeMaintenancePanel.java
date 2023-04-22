package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import swingset.AbstractDemo;

// wg. https://github.com/homebeaver/SwingSet/issues/54
public class TreeMaintenancePanel extends AbstractDemo {

	private static final long serialVersionUID = -566771193655673364L;
	private static final Logger LOG = Logger.getLogger(TreeMaintenancePanel.class.getName());
	private static final String DESCRIPTION = "wg. https://github.com/homebeaver/SwingSet/issues/54";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new TreeMaintenancePanel(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    private JXPanel panel = new JXPanel();
    private BorderLayout mainLayout = new BorderLayout();
    private JXComboBox treeField; // name wie in VTreeMaintenance
	private JXTree centerTree; // name wie in VTreeMaintenance, dort aber VTreePanel extends CPanel
	private GossipTreeModel treeModel; // GossipTreeModel extends DefaultTreeModel

    public TreeMaintenancePanel(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	
    	preInit(); // // name wie in VTreeMaintenance
    	this.add(treeField, BorderLayout.PAGE_START);
    	this.add(panel, BorderLayout.CENTER);
    }

    private void preInit() {
    	panel.setLayout(mainLayout);
    	treeField = new JXComboBox(supergetTreeData());
    	panel.add(treeField, BorderLayout.NORTH);
    	treeField.addActionListener( ae -> {
    		Object o = treeField.getSelectedItem();
    		String knp = (String)o;
    		LOG.info("key/treeID="+knp+" zum Test treeID=104 ID=104 Name=GardenWorld Organization");
    				//action_loadTree(104); // treeID=104 ID=104 Name=GardenWorld Organization, das führt zu NPE
    		action_loadTree(104);
    	});
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Tree root");
    	treeModel = new GossipTreeModel(root, true);
    	centerTree = new JXTree(treeModel) {
    		@Override
            public TreeCellRenderer getCellRenderer() {
            	StringValue sv = (Object value) -> {
                    if(value instanceof MusicTreeModel.MusicEntry
                    || value instanceof MusicTreeModel.Album
                    || value instanceof MusicTreeModel.Song
                    ) {
                    	return StringValues.TO_STRING.getString(value);
                    }
                    String simpleName = value.getClass().getSimpleName();
                    return simpleName + "(" + value + ")";
            	};
                return new JXTree.DelegatingRenderer(sv) {
                    @Override
                    public Component getTreeCellRendererComponent(JTree tree, Object value,
                            boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    	Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                    	LOG.info("getTreeCellRendererComponent for "+(value==null?"":value.getClass())+" value "+value
                    			+ " componentController/Provider:"+getComponentProvider()
                    			+ "\n returns "+c);
                    	return c;
                    }  	
                };
            }
    		
    	};
    	centerTree.setRootVisible(true);
    	panel.add(new JScrollPane(centerTree), BorderLayout.CENTER);
    }
    
    private void action_loadTree(int treeID) {
    	treeModel.setRoot(null);
    	MTreeNode root = // mtree.getRootNode();
    	new MTreeNode(11, 0, "HQ", "description", 0, false, null, false, null); 
//    	(int node_ID, int seqNo, String name, String description,
//    			int parent_ID, boolean isSummary, String imageIndicator, boolean onBar, Color color)

    	treeModel.setRoot(root);
    }


    public String[] supergetTreeData() {
    	return new String[] {"Org", "name1", "name2"};
    }

	@Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}

	class GossipTreeModel extends DefaultTreeModel {
		public GossipTreeModel(TreeNode root) {
			this(root, false);
		}
		public GossipTreeModel(TreeNode root, boolean asksAllowsChildren) {
			super(root, asksAllowsChildren);
		}
	}

//	class MTreeNode extends DefaultMutableTreeTableNode { // implements NodeModel {
//		
//	}
}
