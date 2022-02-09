/*
 * Created on 06.12.2008
 * 
 */
package org.jdesktop.swingx.demos.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXFindBar;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.search.Searchable;
import org.jdesktop.swingx.treetable.TreeTableModelAdapter;
import org.jdesktop.swingx.util.DecoratorFactory;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

//@DemoProperties(
//        value = "Search Demo",
//        category = "Functionality",
//        description = "Demonstrates base searching functionality plus custom configuration.",
//        sourceFiles = {
//                "org/jdesktop/swingx/demos/search/SearchDemo.java",
//                "org/jdesktop/swingx/demos/search/MatchingTextHighlighter.java",
//                "org/jdesktop/swingx/demos/search/Contributor.java",
//                "org/jdesktop/swingx/demos/search/Contributors.java",
//                "org/jdesktop/swingx/demos/search/resources/SearchDemo.properties"
//                }
//)
/**
 * SearchDemo
 * 
 * @author Karl George Schaefer: Commit demo content.  Copied from SwingLabs-Demos
 * @author EUG https://github.com/homebeaver (reorg)
 *
 */
public class SearchDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 3818043478721123293L;
    private static final Logger LOG = Logger.getLogger(SearchDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new SearchDemo(controller);
				JXFrame frame = new JXFrame("demo", exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }


    private Contributors contributors; // Data
    private String[] keys = {"name", "date", "merits", "email"};
    private JXTreeTable treeTable;
    private JXTree tree;
    private JXList list;
    private JXTable table;
    
    private JXFindBar searchPanel;
    private Map<String, StringValue> stringValues;
    private SearchControl searchControl;

    /**
     * SearchDemo Constructor
     */
    public SearchDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getString("name"));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        initComponents();
        initStringRepresentation();
        installCustomSearch();
        
//        bind(); :
        contributors = new Contributors();
        table.setModel(contributors.getTableModel());
        list.setModel(contributors.getListModel());
        tree.setModel(new DefaultTreeModel(contributors.getRootNode()));
        treeTable.setTreeTableModel(new TreeTableModelAdapter(tree.getModel(), contributors.getContributorNodeModel()));
        searchControl = new SearchControl();        
        
        installRenderers();
    }

    // Controller:
    private JCheckBox extendedMarkerBox;
    private JCheckBox painterBox;

    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel();
        
        extendedMarkerBox = new JCheckBox();
        extendedMarkerBox.setName("extendedMarkerBox");
        extendedMarkerBox.setText("Matching Text Marker"); // TODO prop: extendedMarkerBox.text = Matching Text Marker
        extendedMarkerBox.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+extendedMarkerBox.isSelected());
        	searchControl.setExtendedMarker(extendedMarkerBox.isSelected());
        });
        painterBox = new JCheckBox();
        painterBox.setName("painterBox");
        painterBox.setText("Animated Marker"); // TODO prop
        painterBox.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+painterBox.isSelected());
        	searchControl.setAnimatedPainter(painterBox.isSelected());
        });
        
        controller.add(extendedMarkerBox);
        controller.add(painterBox);
    	return controller;
	}

//-------------------------- custom search logic    
    /**
     * Replaces the default findAction with one using the per-demo panel findBar.
     */
    private void installCustomSearch() {
        // <snip> Customize Search 
        // create custom find action
        Action find = new AbstractActionExt() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSearchPanel(e != null ? e.getSource() : null);
            }
            
        };
        // install custom find actions on all collection components of the search demo
        installCustomFindAction(find, table);
        installCustomFindAction(find, list);
        installCustomFindAction(find, tree);
        installCustomFindAction(find, treeTable);
        // </snip>
//        DemoUtils.setSnippet("Customize Search", table, list, tree, treeTable);
        // wire the update on tab changed
        JTabbedPane tabbed = SwingXUtilities.getAncestor(JTabbedPane.class, table);
        ChangeListener l  = new ChangeListener() {
            
            @Override
            public void stateChanged(final ChangeEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        updateSearchable((JTabbedPane) e.getSource());
                    }

                });
            }
        };
        tabbed.addChangeListener(l);
        // initial searchable
        updateSearchable(tabbed);
    }

    
    /**
     * Looks for a Searchable in the potential searchable provider and
     * sets it as the current searchable of the search panel.
     * 
     * @param searchableProvider a component which 
     */
    protected void updateSearchPanel(Object searchableProvider) {
        final Searchable s = getSearchable(searchableProvider);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                searchPanel.setSearchable(s);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(searchPanel);
            }
        });
    }


    /**
     * Looks for a searchable provider in the selected tab of the JTabbedPane
     * and updates the searchPanel accordingly.
     * 
     * @param tabbed
     */
    protected void updateSearchable(JTabbedPane tabbed) {
        Component comp = tabbed.getSelectedComponent();
        if (comp instanceof JScrollPane) {
            comp = (JComponent) ((JScrollPane) comp).getViewport().getView();
        }
        updateSearchPanel(comp);
    }


    /**
     * Registers a custom find action in the target's actionMap and
     * enable incremental search on it.
     * 
     * @param find the custom find action
     * @param target the component to install the custom find action on
     */
    // <snip> Customize Search 
    private void installCustomFindAction(Action find, JComponent target) {
        // install the custom action
        target.getActionMap().put("find", find);
        // force incremental search mode 
        target.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        // </snip>
    }

//---------------------- renderers
    
    /**
     * Prepare different String representations.
     */
    private void initStringRepresentation() {
        stringValues = new HashMap<String, StringValue>();
        
        // <snip> Custom String Representation
        // Note: the content of each cell is always of type Contributor
        // its string representation as-seen is defined here in the StringValue
        // default: show contributor's first and last name
        StringValue nameValue = new StringValue() {

            public String getString(Object value) {
                if (value instanceof Contributor) {
                    Contributor c = (Contributor) value;
                    return c.getLastName() + ", " + c.getFirstName();
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        stringValues.put("name", nameValue);

        // show the joined date
        StringValue dateValue = new StringValue() {

            @Override
            public String getString(Object value) {
                if (value instanceof Contributor) {
                    return StringValues.DATE_TO_STRING.getString(((Contributor) value).getJoinedDate());
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        // </snip>
        stringValues.put("date", dateValue);
        
        // show the merits
        StringValue meritValue = new StringValue() {

            @Override
            public String getString(Object value) {
                if (value instanceof Contributor) {
                    return StringValues.NUMBER_TO_STRING.getString(
                            ((Contributor) value).getMerits());
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        stringValues.put("merits", meritValue);

        // show the email
        StringValue emailValue = new StringValue() {

            @Override
            public String getString(Object value) {
                if (value instanceof Contributor) {
                    URI mail = ((Contributor) value).getEmail();
                    // strip mailto:
                    String path = mail.toString();
                    return path.replace("mailto:", "");
                }
                return StringValues.EMPTY.getString(value);
            }
            
        };
        stringValues.put("email", emailValue);

    }

    /**
     * Install renderers which use the prepared string representations.
     * Note: this method is called after the binding (aka: attach models)
     * because it installs per-column renderers which in this setup can be done only 
     * after the columns are created. 
     */
    // <snip> Custom String Representation
    // install SwingX renderers configured with the appropriate String converter
    private void installRenderers() {
        StringValue sv = stringValues.get("name");
        table.setDefaultRenderer(Contributor.class, new DefaultTableRenderer(sv));
        list.setCellRenderer(new DefaultListRenderer<Object>(sv));
        tree.setCellRenderer(new DefaultTreeRenderer(sv));
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(sv));
        
        for (int i = 1; i < keys.length; i++) {
            installColumnRenderers(i, new DefaultTableRenderer(stringValues.get(keys[i])));
        }
        // </snip>
        // PENDING JW: make the email column use a hyperlinkRenderer once the
        // MatchingTextHighlighter can handle buttons
    }

    private void installColumnRenderers(int column, TableCellRenderer renderer) {
        if (column >= table.getColumnCount()) return;
        table.getColumn(column).setCellRenderer(renderer);
        treeTable.getColumn(column).setCellRenderer(renderer);
    }

    public class SearchControl extends AbstractBean {
        private boolean extendedMarker;
        private boolean animatedPainter;
        private String[] tabs = {"table", "list", "tree", "treeTable", "xTreeTable"};
        private Map <String, MatchingTextHighlighter> matchingTextMarkers;
        private HashMap<String, ColorHighlighter> colorCellMarkers;
        
        public SearchControl() {
//            initMatchMarkers(); :
            createMatchingTextMarkers();
            createColorCellMarkers();
            installMatchMarkers(colorCellMarkers);
        }

        public boolean isExtendedMarker() {
            return extendedMarker;
        }
        
        public void setExtendedMarker(boolean extendedMarker) {
            boolean old = isExtendedMarker();
            LOG.info("extendedMarker="+extendedMarker + " old="+old);
            // <snip> Customize Search 
            // toggle between cell- and text markers
            this.extendedMarker = extendedMarker;
            if (isExtendedMarker()) {
                installMatchMarkers(matchingTextMarkers);
            } else{
                installMatchMarkers(colorCellMarkers);
            }
            SearchDemo.this.painterBox.setEnabled(extendedMarker);
            // </snip>
            firePropertyChange("extendedMarker", old, isExtendedMarker());
        }
        
        public boolean isAnimatedPainter() {
            return animatedPainter;
        }
        
        public void setAnimatedPainter(boolean animatedPainter) {
            boolean old = isAnimatedPainter();
            this.animatedPainter = animatedPainter;
            updateXMatchMarkers();
            firePropertyChange("animatedPainter", old, isAnimatedPainter());
        }
        
        // <snip> Customize Search 
        // install match marker as given in the map
        private void installMatchMarkers(Map<String, ? extends AbstractHighlighter> markers) {
            ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(markers.get("table"));
            ((AbstractSearchable) list.getSearchable()).setMatchHighlighter(markers.get("list"));
            ((AbstractSearchable) tree.getSearchable()).setMatchHighlighter(markers.get("tree"));
            // </snip>
            ((AbstractSearchable) treeTable.getSearchable()).setMatchHighlighter(markers.get("treeTable"));
        }
        

        @SuppressWarnings("unchecked")
        private void updateXMatchMarkers() {
            // <snip> Customize Search 
            // toggle text marker painter between plain and animated
            for (MatchingTextHighlighter hl : matchingTextMarkers.values()) {
                if (isAnimatedPainter()) {
                    hl.setPainter(DecoratorFactory.createAnimatedPainter());
                } else {
                    hl.setPainter(DecoratorFactory.createPlainPainter());
                }
            }
            // </snip>
        }

        private void createColorCellMarkers() {
            colorCellMarkers = new HashMap<String, ColorHighlighter>();
            Color matchColor = HighlighterFactory.LINE_PRINTER;
            for (String string : tabs) {
                colorCellMarkers.put(string, new ColorHighlighter(matchColor, null, matchColor, Color.BLACK));
            }
        }

        private void createMatchingTextMarkers() {
            matchingTextMarkers = new HashMap<String, MatchingTextHighlighter>();
            for (String string : tabs) {
                matchingTextMarkers.put(string, new XMatchingTextHighlighter()); //DecoratorFactory.createMatchingTextHighlighter());
            }
        }
        
    }

    // hack around collection comps not being searchable
    private Searchable getSearchable(Object target) {
        if (target == table) {
            return table.getSearchable();
        } 
        if (target == list) {
            return list.getSearchable();
        }
        if (target == tree) {
            return tree.getSearchable();
        }
        if (target == treeTable) {
            return treeTable.getSearchable();
        }
        return null;
    }

//------------------ init ui
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        searchPanel = SearchFactory.getInstance().createFindBar();
        add(searchPanel, BorderLayout.NORTH);
        
        table = new JXTable();
        table.setName("searchTable");
        list = new JXList(true);
        tree = new JXTree();
        treeTable = new JXTreeTable();
        
        table.setColumnControlVisible(true);
        treeTable.setColumnControlVisible(true);

        JTabbedPane tab = new JTabbedPane();
        tab.setName("searchTabs");
        addTab(tab, table, "tableTabTitle", true);
        addTab(tab, list, "listTabTitle", true);
        addTab(tab, tree, "treeTabTitle", true);
        addTab(tab, treeTable, "treeTableTabTitle", true);
        add(tab);
    }

    private void addTab(JTabbedPane tab, JComponent comp, String string, boolean createScroll) {
//        String name = DemoUtils.getResourceString(getClass(), string);
    	String name = getResourceString(string);
        tab.addTab(name, createScroll ? new JScrollPane(comp) : comp);
    }
    private String getResourceString(String resourceKey) {
    	String key = this.getClass().getSimpleName() + '.' + resourceKey;
    	return StaticUtilities.getResourceAsString(key, resourceKey);
    }
    
}
