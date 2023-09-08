package org.jdesktop.swingx.demos.tree;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;

/*
 *  Mutable Tree Node (not a PO).
 *
 *  Ersetzt (base) org.compiere.model.MTreeNode , das von javax.swing.tree.DefaultMutableTreeNode ableitet
 *                                         und MutableTreeNode implementiert : ... implements Cloneable, MutableTreeNode, Serializable
 *                                         
 *  Ich leite ab von jdesktop DefaultMutableTreeTableNode extends AbstractMutableTreeTableNode 
 *  AbstractMutableTreeTableNode implements MutableTreeTableNode extends TreeTableNode extends TreeNode
 *
 *  DefaultMutableTreeTableNode is designed mainly for testing
 */
@Deprecated
public class MTreeNode extends AbstractMutableTreeTableNode { // interface MutableTreeTableNode extends TreeTableNode

	private static final Logger LOG = Logger.getLogger(MTreeNode.class.getName());

	@Override
	public int getColumnCount() {	
		return 4; // == columnName.length oder kleiner, default ist 1 in super
	}

	// ---- ab hier wg. implements NodeModel mit @Override, ohne @Override, damit es in MenuTreeModel aufrufbar wird
//	@Override
	public Class<?> getColumnClass(int column) {
		return columnClass[column];
	}

//	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}

	@Override // interface TreeTableNode extends TreeNode
	public Object getValueAt(int column) {
		LOG.info("column="+column+"----------UserObject:"+this.getUserObject()+ " this:"+this );
//		return getValueAt(super.getUserObject(), column);
		return getValueAt(this, column);
	}

//	@Override
	public int getHierarchicalColumn() {
		return 0;
	}

//	@Override
	public Object getValueAt(Object node, int column) {
		MTreeNode c = (MTreeNode) node;
		Object o = null;
		
		switch (column) {
		case 0:
			o = c.getName();
			break;
		case 1:
			o = Integer.valueOf(c.getNode_ID());
			break;
		case 2:
			o = c.getImageIndicator();
			break;
		case 3:
			o = Integer.valueOf(c.getSeqNo());
			break;
//		case 4:
//			o = c.getImageIcon();
//			break;
		default:
			// does nothing
			break;
		}
		return o;
	}

//	@Override
//	public boolean isCellEditable(Object node, int column) {
//		return false;
//	}
//
//	@Override
//	public void setValueAt(Object value, Object node, int column) {
//		// noop - not editable	
//	}

	public static final String[] columnName = 
		{ "Name"
		, "Node_Id"
		, "II" // II == m_imageIndicator
		, "Seq"
		, "Icon"
		};
	
	public static final Class<?>[] columnClass = 
		{ String.class
		, Integer.class
		, String.class
		, Integer.class
		, ImageIcon.class
		};

	/**
	 *  Construct Model TreeNode
	 *  @param node_ID	node
	 *  @param seqNo sequence
	 *  @param name name
	 *  @param description description
	 *  @param parent_ID parent
	 *  @param isSummary summary                    aka allowsChildren
	 *  @param imageIndicator image indicator
	 *  @param onBar on bar
	 *  @param color color
	 */
	public MTreeNode (int node_ID, int seqNo, String name, String description,
		int parent_ID, boolean isSummary, String imageIndicator, boolean onBar, Color color)
	{
		super();
		LOG.fine( "MTreeNode Node_ID="+node_ID + ", seqNo="+seqNo + ", Parent_ID="+parent_ID + ", isSummary="+isSummary + ", imageIndicator="+imageIndicator+ " - " + name);
		m_node_ID = node_ID;
		m_seqNo = seqNo;
		m_name = name;
		m_description = description;
		if (m_description == null)
			m_description = "";
		m_parent_ID = parent_ID;
		setSummary(isSummary);
//		setImageIndicator(imageIndicator);
		m_onBar = onBar;
		m_color = color;
	}   //  MTreeNode

	private int     	m_node_ID;
	private int     	m_seqNo;
	private String  	m_name;
	private String  	m_description;
	private int     	m_parent_ID;
	private boolean 	m_isSummary;
	private String      m_imageIndicator; // image indicator (W/X/R/P/F/T/B) MWFNode.ACTION_
	private int 		m_imageIndex = 0; // Index to Icon
	private boolean 	m_onBar;
	private Color 		m_color;
//	private int			m_menu_ID;
//	private boolean		m_iscollapsible;

	public String toString() {
		return "["+getClass() + " node_ID/parent_ID:" +m_node_ID + "/" + m_parent_ID // + "/" + m_onBar + "/" + m_color 
				+ " imageIndicator+seqNo:" +m_imageIndicator + m_seqNo + " name=" + m_name +"]"; 
	}

	public int getNode_ID() {
		return m_node_ID;
	}

	public int getSeqNo() {
		return m_seqNo;
	}

	public String getName() {
		return m_name;
	}

	public int getParent_ID() {
		return m_parent_ID;
	}
	
	public String getImageIndicator() {
		return m_imageIndicator;
	}

/* zur INFO aus X_AD_Menu :
	public static final int ACTION_AD_Reference_ID=104;
	public static final String ACTION_Form        = "X";
	public static final String ACTION_Process     = "P";
	public static final String ACTION_Report      = "R";
	public static final String ACTION_SmartBrowse = "S";
	public static final String ACTION_Task        = "T";
	public static final String ACTION_Window      = "W";
	public static final String ACTION_WorkFlow    = "F";
	public static final String ACTION_Workbench   = "B";
 */
//	public boolean isProcess() {
//		return X_AD_Menu.ACTION_Process.equals(m_imageIndicator);
//	}
//	public boolean isWindow() {
//		return X_AD_Menu.ACTION_Window.equals(m_imageIndicator);
//	}
//	public boolean isForm() {
//		return X_AD_Menu.ACTION_Form.equals(m_imageIndicator);
//	}

	public int getImageIndex() {
		return m_imageIndex;
	}

	/**
	 *  Allow children to be added to this node
	 *  @return true if summary node
	 */
	public boolean isSummary() {
		return m_isSummary;
	}

	public boolean getAllowsChildren() {
		return super.getAllowsChildren();
	}
	
	/**************************************************************************
	 *  Set Summary (allow children)
	 *  @param isSummary summary node
	 */
	public void setSummary(boolean isSummary) {
		m_isSummary = isSummary;
		super.setAllowsChildren(isSummary);
	}

	public static final int TYPE_WINDOW = 1;
	public static final int TYPE_REPORT = 2;
	public static final int TYPE_PROCESS = 3;
	public static final int TYPE_WORKFLOW = 4;
	public static final int TYPE_WORKBENCH = 5;
	public static final int TYPE_SETVARIABLE = 6;
	public static final int TYPE_USERCHOICE = 7;
	public static final int TYPE_DOCACTION = 8;

	/*************************************************************************/

	@Override
    protected List<MutableTreeTableNode> createChildrenList() {
        return new LinkedList<MutableTreeTableNode>(); // statt new ArrayList<MutableTreeTableNode>();
    }

	MutableTreeTableNode findChildAfter(MTreeNode c) {
		MTreeNode ca = null;
		for (MutableTreeTableNode child : children) {
			MTreeNode nd = (MTreeNode)child;
			if(nd.getSeqNo()>c.getSeqNo()) {
				// gefunden ca soll nach c angezeigt werden
				return nd;
			} else {
				ca = nd;
			}
		}
		return ca;
	}
	
	MutableTreeTableNode findChildBefore(MTreeNode c) {
		MTreeNode cb = null;
		for (MutableTreeTableNode child : children) {
			MTreeNode nd = (MTreeNode)child;
			if(nd.getSeqNo()<c.getSeqNo()) {
				cb = nd;
			} else {
				// gefunden cb soll vor c angezeigt werden
			}
		}
		return cb;
	}
	
	/* die Reihenfolge der childs wird bestimmt durch m_seqNo m_node_ID;
	 * allerdings kann es zwei childs mit gleicher seq geben, dann bestimmt m_node_ID die Reihenfolge
	 */
	public void add(MutableTreeTableNode child) {
		if(child instanceof MTreeNode) {
			MutableTreeTableNode ca = findChildAfter((MTreeNode)child);
			if(ca==null) {
				super.add(child);
			} else {
				super.insert(child, 1+this.getIndex(ca));
			}
		} else {
			super.add(child);
		}
	}

	/**
	 *	Return the Node with ID in list of children
	 *  @param ID id
	 *  @return VTreeNode with ID or null
	 */
	public MTreeNode findNode(int ID) {
		if (m_node_ID == ID) return this;
		for (MutableTreeTableNode child : children) {
			MTreeNode nd = (MTreeNode)child;
			if(nd.getParent_ID()==ID) {
				return nd; // gefunden
			} else {
				// weitersuchen
				// rekursiv:
				//LOG.config("rekursiv weitersuchen in "+nd);
				MTreeNode gefunden = nd.findNode(ID);
				if(gefunden!=null) return gefunden;
			}
		}
		return null;
	}
	
}
