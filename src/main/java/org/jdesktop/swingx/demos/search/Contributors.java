/*
 * Created on 06.12.2008
 *
 */
package org.jdesktop.swingx.demos.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.NodeModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import swingset.StaticUtilities;

/**
 * Class Contributors
 * used in SearchDemo, XListDemo, HighlighterExtDemo, XComboBoxDemo
 */
public class Contributors {

    private static final Logger LOG = Logger.getLogger(Contributors.class.getName());

    private String dataSource = "resources/contributors.txt";
    private List<Contributor> contributors;
    private ListModel<Object> listModel;
    private TableModel tableModel;
    private DefaultMutableTreeNode rootNode;
    private NodeModel contributorNodeModel;
    private int columnCount;

    /**
     * ctor
     */
    public Contributors() {
        this(4);
    }
    
    /**
     * ctor
     * @param columnCount number of columns
     */
    public Contributors(int columnCount) {
        this.columnCount = columnCount;
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns Contributor TableModel
     * @return TableModel
     */
    public TableModel getTableModel() {
        return tableModel;
    }
    
    /**
     * Returns Contributor ListModel
     * @return ListModel
     */
    public ListModel<Object> getListModel() {
        return listModel;
    }
    
    /**
     * Returns RootNode of the Tree
     * @return rootNode
     */
    public TreeNode getRootNode() {
        return rootNode;
    }
    
    /**
     * Returns Contributor List
     * @return List
     */
    public static List<Contributor> getContributors() {
        return new Contributors().contributors;
    }
    
    /**
     * Returns Contributor Model which is an extended ListModel
     * @return ComboBoxModel an extended ListModel
     */
    public static ComboBoxModel<Contributor> getContributorModel() {
        List<Contributor> list = getContributors();
        DefaultComboBoxModel<Contributor> model = new DefaultComboBoxModel<Contributor>();
        for (Contributor contributor : list) {
            model.addElement(contributor);
        }
        return model;
    }
    
    /**
     * Create and fill a list of contributors from a resource and 
     * wrap view models around.
     * @throws IOException 
     * 
     */
    @SuppressWarnings("serial")
	private void initData() throws IOException {
        contributors = new ArrayList<Contributor>();
        // fill the list from the resources
        readDataSource(contributors);
        LOG.fine("contributors size="+contributors.size());
        // wrap a listModel around , no serialVersionUID: Same-version serialization only
        listModel = new AbstractListModel<Object>() {
            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return null; // "-- Contributors --";
                }
                return contributors.get(index - 1);
            }
            @Override
            public int getSize() {
                return contributors.size() + 1;
            }
            
        };
        LOG.fine("listModel size="+listModel.getSize());
        
        // create NodeModel: returns a Contributor for each column
        contributorNodeModel = new NodeModel() {
            public int getColumnCount() {
                // defined on init
                return columnCount;
            }
            public Class<?> getColumnClass(int columnIndex) {
                return Contributor.class;
            }
            
            public String getColumnName(int column) {
                return "Display " + column;
            }
            
            public boolean isCellEditable(Object node, int columnIndex) {
                return false;
            }
            
            public Object getValueAt(Object node, int columnIndex) {
                return getContributor(node);
            }
            
            private Contributor getContributor(Object node) {
                if (node instanceof DefaultMutableTreeNode) {
                    node = ((DefaultMutableTreeNode) node).getUserObject();
                } else if (node instanceof TreeTableNode) {
                    node =  ((TreeTableNode) node).getUserObject();
                }
                return (node instanceof Contributor) ? (Contributor) node : null;
            }
            
            public int getHierarchicalColumn() {
                return 0;
            }
            public void setValueAt(Object value, Object node, int column) {
                // noop - not editable
            }

        };
        LOG.info("contributorNodeModel ColumnCount="+contributorNodeModel.getColumnCount());
        
        // wrap a TableModel around
        tableModel = new AbstractTableModel() {
            
            public int getColumnCount() {
                return contributorNodeModel.getColumnCount();
            }
            
            public int getRowCount() {
                return contributors.size();
            }
            
            public Object getValueAt(int rowIndex, int columnIndex) {
                return contributorNodeModel.getValueAt(contributors.get(rowIndex), columnIndex);
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return contributorNodeModel.getColumnClass(columnIndex);
            }
            
            @Override
            public String getColumnName(int column) {
                return contributorNodeModel.getColumnName(column);
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
                        
        };
        LOG.info("tableModel RowCount="+tableModel.getRowCount());

        // fill DefaultTreeNodes with the elements 
        rootNode = new DefaultMutableTreeNode("Contributors");
        for (int i = 0; i < contributors.size(); i++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(contributors.get(i));
            rootNode.add(node);
        }
        
    }
    
    private void readDataSource(List<Contributor> list) throws IOException {
//        InputStream is = getClass().getResourceAsStream(dataSource);
    	InputStream is = StaticUtilities.getResourceAsStream(SearchDemo.class, dataSource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
            	//LOG.info(line);
                list.add(new Contributor(line));
            }
        } finally {
            // do our best to close
            reader.close();
        }
    }

    /**
     * Returns Contributor NodeModel
     * @return NodeModel
     */
    public NodeModel getContributorNodeModel() {
        return contributorNodeModel;
        
    }

}
