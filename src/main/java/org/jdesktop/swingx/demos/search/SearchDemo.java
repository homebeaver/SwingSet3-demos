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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXFindBar;
import org.jdesktop.swingx.JXFindPanel;
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
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.demos.svg.FeatheRuser;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.search.Searchable;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModelAdapter;

import swingset.AbstractDemo;

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
	private static final String DESCRIPTION = "Demonstrates base searching functionality plus custom configuration.";
	private static final String SEARCHFIELD_PROMPT = "Enter a search text here";
    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new SearchDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    private String[] keys = {"name", "date", "merits", "email"};
    private Contributors contributors; // Data
    private TreeModel treeModel;
    private JXTreeTable treeTable;
    private JXTree tree;
    private JXList<Object> list;
    private JXTable table;
    private JTabbedPane tabbedPane;
    
    private JPanel jxFindContainer;
    private JXFindPanel findPanel;
    private JXFindBar findBar;
    private Map<String, StringValue> stringValues;
    private SearchControl searchControl;

    /**
     * SearchDemo Constructor
     * 
     * @param frame controller Frame
     */
    public SearchDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        jxFindContainer = new JPanel();
        add(jxFindContainer, BorderLayout.NORTH);

        findBar = SearchFactory.getInstance().createFindBar();
        jxFindContainer.add(findBar);

        initStringRepresentation();
        contributors = new Contributors();
        treeModel = new DefaultTreeModel(contributors.getRootNode());

    	tabbedPane = initComponents(); // with init treeTable
        add(tabbedPane, BorderLayout.CENTER);
        
        // findPanel with searchField
        findPanel = new DemoFindPanel(updateSearchable(tabbedPane));
        
        installCustomSearch();
        
        table.setModel(contributors.getTableModel());
        list.setModel(contributors.getListModel());
        tree.setModel(treeModel);
        searchControl = new SearchControl();        
        
        installRenderers();

        InputMap map = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.SHIFT_DOWN_MASK), "SearchAction");
        this.getActionMap().put("SearchAction", new SearchAction(this));
    }

    @SuppressWarnings("serial")
	class SearchAction extends AbstractActionExt {
		Component invoker;
        protected SearchAction(Component comp) {
            super("SearchAction"); // the name for the action
            this.invoker = comp;
        }
		@Override
		public void actionPerformed(ActionEvent e) {
        	LOG.info("invoker:"+invoker+", event:"+e);
        	cmdFind();
		}  	
    }
    
    // Controller:
    private JCheckBox extendedMarkerBox;
    private JCheckBox painterBox;

    // aus DemoModule
    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED), new EmptyBorder(5,5,5,5));
    private JXPanel createVerticalPanel(boolean threeD) {
        JXPanel p = new JXPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel(new BorderLayout());
        
        JXPanel boxen = new JXPanel();
        extendedMarkerBox = new JCheckBox();
        extendedMarkerBox.setName("extendedMarkerBox");
        extendedMarkerBox.setText(getBundleString("extendedMarkerBox.text"));
        extendedMarkerBox.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+extendedMarkerBox.isSelected());
        	searchControl.setExtendedMarker(extendedMarkerBox.isSelected());
        });
        painterBox = new JCheckBox();
        painterBox.setName("painterBox");
        painterBox.setText(getBundleString("painterBox.text"));
        painterBox.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+painterBox.isSelected());
        	searchControl.setAnimatedPainter(painterBox.isSelected());
        });
        
        boxen.add(extendedMarkerBox);
        boxen.add(painterBox);
        controller.add(boxen, BorderLayout.NORTH);
        
        JPanel leftColumn = createVerticalPanel(false);
        controller.add(leftColumn);
        JLabel searchControlLabel = new JLabel(getBundleString("searchControlLabel.text"));
        leftColumn.add(searchControlLabel);
        ButtonGroup group = new ButtonGroup(); // JXFindBar XOR JXFindPanel 

        JRadioButton jxFindBar = new JRadioButton();
        jxFindBar.setText(getBundleString("jxFindBar.labelAndMnemonic", jxFindBar));
        jxFindBar.setToolTipText(getBundleString("jxFindBar.tooltip"));
        jxFindBar.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == jxFindBar
        	if(rb.isSelected()) {
                LOG.config("item event JRadioButton="+rb);
                SearchDemo.this.jxFindContainer.removeAll();
                SearchDemo.this.jxFindContainer.add(findBar);
                SearchDemo.this.updateUI();
        	}
        });
        group.add(jxFindBar);
        jxFindBar.setSelected(true);
        leftColumn.add(jxFindBar);

        JRadioButton jxFindPanel = new JRadioButton();
        jxFindPanel.setActionCommand("jxFindPanel");
        jxFindPanel.setText(getBundleString("jxFindPanel.labelAndMnemonic", jxFindPanel));
        jxFindPanel.setToolTipText(getBundleString("jxFindPanel.tooltip"));
        jxFindPanel.addItemListener(e -> {
        	JRadioButton rb = (JRadioButton) e.getSource(); // rb == e.getSource() == jxFindPanel
        	if(rb.isSelected()) {
                LOG.info("item event JRadioButton="+rb);
                SearchDemo.this.jxFindContainer.removeAll();
                SearchDemo.this.jxFindContainer.add(findPanel);
                SearchDemo.this.updateUI();
        	}
        });
        group.add(jxFindPanel);
        leftColumn.add(jxFindPanel);

        JButton openFindDialog = new JButton("find dialog or SHIFT_DOWN+F5");
        
        openFindDialog.addActionListener( actionEvent -> {
        	LOG.fine("actionEvent:"+actionEvent);
        	cmdFind();
        });
        controller.add(openFindDialog, BorderLayout.SOUTH);

    	return controller;
	}

    private void cmdFind() {
//    	LOG.info("---find---find---find---find---find---find---");
    	int selected = tabbedPane.getSelectedIndex();
        switch (selected) {
        case 0:
        	SearchFactory.getInstance().showFindDialog(table, table.getSearchable());
            break;
        case 1:
        	SearchFactory.getInstance().showFindDialog(list, list.getSearchable());
            break;
        case 2:
        	SearchFactory.getInstance().showFindDialog(tree, tree.getSearchable());
            break;
        case 3:
        	SearchFactory.getInstance().showFindDialog(treeTable, treeTable.getSearchable());
            break;
        default:
            //does nothing
            break;
        }
    }
    
//-------------------------- custom search logic    
    /**
     * Replaces the default findAction with one using the per-demo panel findBar.
     */
    private void installCustomSearch() {
        // <snip> Customize Search 
        // create custom find action
        @SuppressWarnings("serial")
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
        // addChangeListener shorter with Lambda Expression
        tabbed.addChangeListener(changeEvent -> {
            SwingUtilities.invokeLater( () -> {
                updateSearchable((JTabbedPane) changeEvent.getSource());
            });
        });
        // initial searchable
        updateSearchable(tabbed);
    }

    
    /**
     * Looks for a Searchable in the potential searchable provider and
     * sets it as the current searchable of the search panel.
     * 
     * @param searchableProvider a component (JXTable, ... ) which 
     * @return the Searchable for searchableProvider
     */
    protected Searchable updateSearchPanel(Object searchableProvider) {
        final Searchable s = getSearchable(searchableProvider);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                findBar.setSearchable(s);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(findBar);
            }
        });
        return s;
    }


    /**
     * Looks for a searchable provider in the selected tab of the JTabbedPane
     * and updates the searchPanel accordingly.
     * 
     * @param tabbed JTabbedPane
     * @return the searchable
     */
    protected Searchable updateSearchable(JTabbedPane tabbed) {
        Component comp = tabbed.getSelectedComponent();
        if (comp instanceof JScrollPane) {
            comp = (JComponent) ((JScrollPane) comp).getViewport().getView();
        }
        return updateSearchPanel(comp);
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
        StringValue nameValue = (Object value) -> {
            if(value instanceof Contributor c) {
                return c.getLastName() + ", " + c.getFirstName();
            }
            return StringValues.TO_STRING.getString(value);        	
        };
        stringValues.put("name", nameValue);

        // show the joined date
        StringValue dateValue = (Object value) -> {
            if(value instanceof Contributor c) {
                return StringValues.DATE_TO_STRING.getString(c.getJoinedDate());
            }
            return StringValues.TO_STRING.getString(value);   	
        };
        // </snip>
        stringValues.put("date", dateValue);
        
        // show the merits
        StringValue meritValue = (Object value) -> {
            if(value instanceof Contributor c) {
                return StringValues.NUMBER_TO_STRING.getString(c.getMerits());
            }
            return StringValues.TO_STRING.getString(value);
        };
        stringValues.put("merits", meritValue);

        // show the email
        StringValue emailValue = (Object value) -> {
            if(value instanceof Contributor c) {
                URI mail = c.getEmail();
                // strip mailto:
                String path = mail.toString();
                return path.replace("mailto:", "");
            }
            return StringValues.EMPTY.getString(value);       	
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
        tree.setCellRenderer(tree.new DelegatingRenderer(sv));
        
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

    // EUG: im Gegensetz zu PainterDemo$PainterControl ist SearchControl keine AbstractBean!
    // Weil kein bind() gemacht wird. Daher kann diese Klasse private sein
    private class SearchControl extends AbstractBean {
        private boolean extendedMarker;
        private boolean animatedPainter;
        private String[] tabs = {"table", "list", "tree", "treeTable", "xTreeTable"};
        private Map <String, MatchingTextHighlighter> matchingTextMarkers;
        private HashMap<String, ColorHighlighter> colorCellMarkers;
        
        public SearchControl() {
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
//            Color matchColor = Color.PINK;
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
    
    /**
     * creates JTabbedPane with table, list, tree and treeTable tabs
     * @return JTabbedPane
     */
    private JTabbedPane initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setName("searchTabs");
        
        table = new JXTable();
        table.setName("searchTable");
        list = new JXList<Object>(true);
        tree = new JXTree() {
			private static final long serialVersionUID = 1L;
		    @Override
            public TreeCellRenderer getCellRenderer() {
                return new JXTree.DelegatingRenderer(stringValues.get("name"));
            }
        };
		/*
		 * use small user/person icon for contributors
		 */
		Highlighter personIcon = new IconHighlighter(new HighlightPredicate.DepthHighlightPredicate(1), 
				FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
		tree.addHighlighter(personIcon);
        
        // TreeTableModelAdapter implements TreeTableModel
        TreeTableModel ttmodel = new TreeTableModelAdapter(treeModel, contributors.getContributorNodeModel());
		JXTreeTable.TreeTableCellRenderer renderer = new JXTreeTable.TreeTableCellRenderer(ttmodel) {
			private static final long serialVersionUID = 1L;
			@Override
			public TreeCellRenderer getCellRenderer() {
                return new JXTree.DelegatingRenderer(stringValues.get("name"));
		    }
		};
		renderer.addHighlighter(personIcon);
		treeTable = new ContributorTreeTable(renderer);
        
        table.setColumnControlVisible(true);
        treeTable.setColumnControlVisible(true);

        addTab(tabbedPane, table, "tableTabTitle", true);
        addTab(tabbedPane, list, "listTabTitle", true);
        addTab(tabbedPane, tree, "treeTabTitle", true);
        addTab(tabbedPane, treeTable, "treeTableTabTitle", true);
        
        return tabbedPane;
    }
    
	@SuppressWarnings("serial")
	class ContributorTreeTable extends JXTreeTable implements TableCellRenderer {

		ContributorTreeTable(JXTreeTable.TreeTableCellRenderer renderer) {
			super(renderer);
			assert ((JXTreeTable.TreeTableModelAdapter) getModel()).getTree() == renderer;			
		}
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {			
        	LOG.warning("NICHT IMPLEMENTIERT row="+row + " column="+column + " value:"+value);
//        	super.getCellRenderer(row, column)
			return null;
		}
		
	}

}