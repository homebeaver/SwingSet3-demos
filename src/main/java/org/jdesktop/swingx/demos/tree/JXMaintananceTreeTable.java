package org.jdesktop.swingx.demos.tree;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.treetable.TreeTableModel;

@Deprecated
public class JXMaintananceTreeTable extends JXTreeTable implements TableCellRenderer {

	private static final long serialVersionUID = 1303514904251437739L;
	private static final Logger LOG = Logger.getLogger(JXMaintananceTreeTable.class.getName());

	// inner in JXTreeTable
	// public static class TreeTableCellRenderer extends JXTree implements TableCellRenderer
	public static class JXMaintananceTreeTableCellRenderer extends JXTreeTable.TreeTableCellRenderer {
		JXMaintananceTreeTableCellRenderer(TreeTableModel model) {
			super(model);
			if(isOverwriteRendererIcons()) {
				// OK
			} else {
				LOG.warning("isOverwriteRendererIcons NOT set");
				setOverwriteRendererIcons(true);
			}
		}
		// Returns the component used for drawing the cell.
	    @Override // to log
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
	    	Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        	LOG.info("----- r/c:"+row+"/"+column +" value:"+value + " " + (value==null?"":value.getClass())
        			+ " "+comp);
        	if(comp instanceof JXTree xtree) {
//        		LOG.info("----- CellRenderer:"+
//        		xtree.getCellRenderer());
//        	    Component getTreeCellRendererComponent(JTree tree, Object value,
//                        boolean selected, boolean expanded,
//                        boolean leaf, int row, boolean hasFocus);
        		return xtree.getCellRenderer().getTreeCellRendererComponent(xtree, value, isSelected, true, false, row, hasFocus);
        	}
        	return comp;
        }
	}

	JXMaintananceTreeTable(JXTreeTable.TreeTableCellRenderer renderer) {
		super(renderer);
//		renderer.bind(this); // renderer already bound
		assert ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree() == renderer;		
	}
	
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
    	ComponentAdapter ca = getComponentAdapter(row, column);
    	if(ca.column == getHierarchicalColumn()) {
    		JXTree.DelegatingRenderer renderer = (JXTree.DelegatingRenderer)getTreeCellRenderer();
	    	LOG.info("hierarchical column "+column + " isHierarchicalColumn!!! renderer:"+renderer);
    		JTree tree = ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree();
    		JXTree xtree = (JXTree)tree;
    		return (JXTreeTable.TreeTableCellRenderer)xtree;
    	}
    	return super.getCellRenderer(row, column);
    }
	
    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, 
			boolean isSelected, boolean hasFocus, int row, int column) {			
    	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
//    	super.getCellRenderer(row, column)
		return null;
	}
}
