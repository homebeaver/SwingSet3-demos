package org.jdesktop.swingx.demos.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.treetable.TreeTableModel;

import swingset.AbstractDemo;

// wg. https://github.com/homebeaver/SwingSet/issues/54
@Deprecated
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
	private GossipTreeTableModel treeTableModel; // GossipTreeTableModel extends GossipTreeModel

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
    	treeTableModel = new GossipTreeTableModel(root, true);
    	centerTree = new JXMaintananceTree(treeModel);
    	centerTree.setRootVisible(true);
//    	centerTree.setEditable(true);
    	panel.add(new JScrollPane(centerTree), BorderLayout.CENTER);
    	panel.add(createGossipTreeTable(treeTableModel), BorderLayout.EAST);
    }

	private JComponent createGossipTreeTable(GossipTreeTableModel model) {
		JXTreeTable.TreeTableCellRenderer renderer = new JXMaintananceTreeTable.JXMaintananceTreeTableCellRenderer(model);
		JXTreeTable treeTable = new JXMaintananceTreeTable(renderer);
		// XXX HierarchicalColumn wird mit icon, aber ohne text dargestellt
//		treeTable.setRootVisible(true); // OK
		treeTable.setColumnControlVisible(true); // OK
		
		// redText Highlighter funktioniert auch:
		treeTable.setRolloverEnabled(true);
		Highlighter redText = new ColorHighlighter(HighlightPredicate.ROLLOVER_CELL, null, Color.RED);
		treeTable.addHighlighter(redText);
		
		treeTable.setEditable(true);
		
		return new JScrollPane(treeTable);
	}

    private void action_loadTree(int treeID) {
//    	treeModel.setRoot(null);
    	MTreeNode root = // mtree.getRootNode();
    	new MTreeNode(0, 0, "GardenWorld Organization", "description", 0, true, null, false, null);
    	root.add(new MTreeNode(11, 0, "HQ", "description first child", 0, false, null, false, null));
    	root.add(new MTreeNode(50000, 1, "Furniture", "description child seqNo=1", 0, false, null, false, null));
//    	(int node_ID, int seqNo, String name, String description,
//    			int parent_ID, boolean isSummary, String imageIndicator, boolean onBar, Color color)
//21:17:16.649   MTreeNode.<init>: MTreeNode Node_ID=0, seqNo=0, Parent_ID=0, isSummary=true, imageIndicator=null - GardenWorld Organization [23]
//21:17:16.661   MTreeNode.<init>: MTreeNode Node_ID=11, seqNo=0, Parent_ID=0, isSummary=false, imageIndicator=null - HQ [23]
//21:17:16.661   MTreeNode.<init>: MTreeNode Node_ID=50000, seqNo=1, Parent_ID=0, isSummary=false, imageIndicator=null - Furniture [23]
//21:17:16.661   MTreeNode.<init>: MTreeNode Node_ID=50001, seqNo=2, Parent_ID=0, isSummary=false, imageIndicator=null - Fertilizer [23]
//21:17:16.661   MTreeNode.<init>: MTreeNode Node_ID=50007, seqNo=3, Parent_ID=0, isSummary=true, imageIndicator=null - Stores [23]
//21:17:16.662   MTreeNode.<init>: MTreeNode Node_ID=50006, seqNo=0, Parent_ID=50007, isSummary=false, imageIndicator=null - Store West [23]
//21:17:16.662   MTreeNode.<init>: MTreeNode Node_ID=50005, seqNo=1, Parent_ID=50007, isSummary=false, imageIndicator=null - Store East [23]
//21:17:16.662   MTreeNode.<init>: MTreeNode Node_ID=50004, seqNo=2, Parent_ID=50007, isSummary=false, imageIndicator=null - Store South [23]
//21:17:16.662   MTreeNode.<init>: MTreeNode Node_ID=50002, seqNo=3, Parent_ID=50007, isSummary=false, imageIndicator=null - Store North [23]
//21:17:16.662   MTreeNode.<init>: MTreeNode Node_ID=12, seqNo=4, Parent_ID=50007, isSummary=false, imageIndicator=null - Store Central [23]
    	treeModel.setRoot(root);
    	treeTableModel.setRoot(root);
    	LOG.info("=========treeTableModel.getColumnCount="+treeTableModel.getColumnCount() +
    		", root.ValueAt 1/"+treeTableModel.getColumnName(1)+":"+treeTableModel.getValueAt(treeTableModel.getRoot(), 1));
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

	@SuppressWarnings("serial")
	class GossipTreeTableModel extends GossipTreeModel implements TreeTableModel {

		public GossipTreeTableModel(TreeNode root) {
			this(root, false);
		}
		public GossipTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
			super(root, asksAllowsChildren);
		}

		@Override
		public int getColumnCount() {
			Object o = this.getRoot();
			if(o instanceof MTreeNode root) {
				return root.getColumnCount();
			}
			return MTreeNode.columnClass.length;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			Object o = this.getRoot();
			if(o instanceof MTreeNode root) {
				return root.getColumnClass(columnIndex);
			}
			return MTreeNode.columnClass[columnIndex];
		}

		@Override
		public String getColumnName(int column) {
			Object o = this.getRoot();
			if(o instanceof MTreeNode root) {
				return root.getColumnName(column);
			}
			return MTreeNode.columnName[column];
		}

		@Override
		public int getHierarchicalColumn() {
			return 1;
		}

		@Override
		public Object getValueAt(Object node, int column) {
			if(node instanceof MTreeNode mTreeNode) {
				return mTreeNode.getValueAt(column);
			}
			return null;
		}

		@Override
		public boolean isCellEditable(Object node, int column) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setValueAt(Object value, Object node, int column) {
			// TODO Auto-generated method stub
			
		}

	}

}
