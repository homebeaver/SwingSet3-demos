/*
 * Created on 06.12.2008
 *
 */
package org.jdesktop.swingx.demos.highlighterext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.demos.search.Contributor;
import org.jdesktop.swingx.demos.search.Contributors;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.HyperlinkProvider;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModelAdapter;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingxset.util.RelativePainterHighlighter;
import org.jdesktop.swingxset.util.RelativePainterHighlighter.NumberRelativizer;
import org.pushingpixels.trident.api.Timeline;
import org.pushingpixels.trident.api.ease.Spline;

import swingset.AbstractDemo;

//@DemoProperties(
//        value = "Highlighter (extended)",
//        category = "Functionality",
//        description = "Demonstrates value based highlighting.",
//        sourceFiles = {
//                "org/jdesktop/swingx/demos/highlighterext/HighlighterExtDemo.java",
//                "org/jdesktop/swingxset/util/RelativePainterHighlighter.java"
//                }
//)
/**
 * HighlighterExtDemo
 *
 * @author Karl George Schaefer: Commit demo content.  Copied from SwingLabs-Demos
 * @author EUG https://github.com/homebeaver (reorg)
 *
 */
public class HighlighterExtDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 2114065461965096403L;
	private static final Logger LOG = Logger.getLogger(HighlighterExtDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates value based highlighting.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new HighlighterExtDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
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
    private int meritColumn = 2;
    private JXTreeTable treeTable;
    private JXTree tree;
    private JXList list;
    private JXTable table;
    
    // Controller:
    private JCheckBox extendedMarkerBox;
    private JButton raceButton;
    private Map<String, StringValue> stringValues;
    private HighlighterControl highlighterControl;
    private JButton fadeInButton;

    /**
     * HighlighterExtDemo Constructor
     * 
     * @param frame controller Frame
     */
    public HighlighterExtDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        initComponents();
        
//      bind() :
      contributors = new Contributors();
      table.setModel(contributors.getTableModel());
      list.setModel(contributors.getListModel());
      tree.setModel(new DefaultTreeModel(contributors.getRootNode()));
      treeTable.setTreeTableModel(new TreeTableModelAdapter(tree.getModel(), contributors.getContributorNodeModel()));

      // simple setup of per-column renderers, so can do only after binding
      installRenderers();
    }

    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel();
        
        extendedMarkerBox = new JCheckBox();
        extendedMarkerBox.setName("extendedMarkerBox");
        extendedMarkerBox.setText(getBundleString("extendedMarkerBox.text"));
        extendedMarkerBox.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+extendedMarkerBox.isSelected());
        	highlighterControl.setSpreadColumns(extendedMarkerBox.isSelected());
        });

        raceButton = new JButton();
        raceButton.setName("playButton");
        raceButton.setText(getBundleString("race.Action.text"));
        fadeInButton = new JButton();
        fadeInButton.setName("fadeInButton");
        fadeInButton.setText(getBundleString("fadeIn.Action.text"));

        // init highlighter control
        highlighterControl = new HighlighterControl(); 
//        raceButton.setAction(getAction("race")); so geht es direkter:
        raceButton.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+raceButton.isSelected());
        	highlighterControl.race(); // Delegates to the highlighterControl
        });
//        fadeInButton.setAction(getAction("fadeIn"));
        fadeInButton.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+fadeInButton.isSelected());
        	highlighterControl.fadeIn(); // Delegates to the highlighterControl
        });
    
        controller.add(extendedMarkerBox);
        controller.add(raceButton);
        controller.add(fadeInButton);
    	return controller;
	}

    //------------------ init ui
    private void initComponents() {
        table = new JXTable();
        list = new JXList(true); // autoCreateRowSorter
        tree = new JXTree();
        treeTable = new JXTreeTable();
        
        table.setColumnControlVisible(true);
        treeTable.setColumnControlVisible(true);

        JTabbedPane tab = new JTabbedPane();
        tab.setName("highlightTabs");
        addTab(tab, table, "tableTabTitle", true);
        addTab(tab, list, "listTabTitle", true);
//        addTab(tab, tree, "treeTabTitle", true);
//        addTab(tab, treeTable, "treeTableTabTitle", true);
        add(tab);
    }

    /**
     * implement custom Relativizer class
     */
    // <snip> Relativizer
    public static class MeritRelativizer extends NumberRelativizer {

        public MeritRelativizer(int column, boolean spreadColumns, Number max, Number current) {
            super(column, spreadColumns, max, current);
        }

        // custom mapping of content to a Number
        @Override
        protected Number getNumber(ComponentAdapter adapter) {
            if (!(adapter.getValue(getValueColumn()) instanceof Contributor)) {
                return null;
            }
            return ((Contributor) adapter.getValue(getValueColumn())).getMerits();
        }
        // </snip>
        
    }
    
    public class HighlighterControl extends AbstractBean {
        
        private RelativePainterHighlighter tableValueBasedHighlighter;
        private boolean spreadColumns;
        private RelativePainterHighlighter valueBasedHighlighter;
        
        private Timeline raceTimeline; // aus lib trident jar
        private Timeline fadeInTimeline;
        private MattePainter matte;
        private Color base = PaintUtils.setSaturation(Color.MAGENTA, .7f);
        
        public HighlighterControl() {
            matte = new MattePainter(PaintUtils.setAlpha(base, 125));
            tableValueBasedHighlighter = new RelativePainterHighlighter(matte);
            table.addHighlighter(tableValueBasedHighlighter);
            treeTable.addHighlighter(tableValueBasedHighlighter);

            valueBasedHighlighter = new RelativePainterHighlighter(matte);
            list.addHighlighter(valueBasedHighlighter);
            tree.addHighlighter(valueBasedHighlighter);
            
            setSpreadColumns(false);

/*
            BindingGroup group = new BindingGroup();
            group.addBinding(Bindings.createAutoBinding(READ, 
                    extendedMarkerBox, BeanProperty.create("selected"),
                    this, BeanProperty.create("spreadColumns")));
            group.bind();
            // equivalent to
        extendedMarkerBox.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+extendedMarkerBox.isSelected());
        	highlighterControl.setSpreadColumns(extendedMarkerBox.isSelected());
        });

*/
        }

        // PENDING JW: how-to find the resource of this action for injection?
//        @Action
		public void race() {
			if (raceTimeline == null) {
				raceTimeline = Timeline.builder(this)
					.addPropertyToInterpolate("currentMerit", 0, 100)
					.build();
			}
			raceTimeline.replay();
		}
        
//        @Action
        public void fadeIn() {
            if (fadeInTimeline == null) {
                fadeInTimeline = Timeline.builder(this)
                		.addPropertyToInterpolate("background", 
                			PaintUtils.setAlpha(base, 0), PaintUtils.setAlpha(base, 125))
                		.setDuration(2000)
                		.setEase(new Spline(0.7f))
                		.build();
            }
            fadeInTimeline.replay();
        }
        
        public void setBackground(Color color) {
            matte.setFillPaint(color);
        }
        
        public void setCurrentMerit(int merit) {
            MeritRelativizer relativizer = createMeritRelativizer(merit);
            tableValueBasedHighlighter.setRelativizer(relativizer);
            valueBasedHighlighter.setRelativizer(relativizer);
        }


        /**
         * Creates and returns a relativizer with the given intermediate value.
         * 
         */
        private MeritRelativizer createMeritRelativizer(int intermediate) {
            return new MeritRelativizer(meritColumn, isSpreadColumns(), 100, intermediate);
        }
        
        private void updateTableHighlighter() {
            tableValueBasedHighlighter.setRelativizer(createMeritRelativizer(100));
            valueBasedHighlighter.setRelativizer(tableValueBasedHighlighter.getRelativizer());
            if (isSpreadColumns()) {
                tableValueBasedHighlighter.setHighlightPredicate(HighlightPredicate.ALWAYS);
            } else {
                tableValueBasedHighlighter.setHighlightPredicate(new HighlightPredicate.ColumnHighlightPredicate(meritColumn));
            }    
        }

        public boolean isSpreadColumns() {
            return spreadColumns;
        }
        
        public void setSpreadColumns(boolean extendedMarker) {
            boolean old = isSpreadColumns();
            this.spreadColumns = extendedMarker;
            updateTableHighlighter();
            firePropertyChange("spreadColumns", old, isSpreadColumns());
        }

    }

//---------------------- renderers
    

    /**
     * Install renderers which use the prepared string representations.
     * Note: this method is called after the binding (aka: attach models)
     * because it installs per-column renderers which in this setup can be done only 
     * after the columns are created. 
     */
    private void installRenderers() {
        initStringRepresentation();
        StringValue sv = stringValues.get("name");
        table.setDefaultRenderer(Contributor.class, new DefaultTableRenderer(sv));
        list.setCellRenderer(new DefaultListRenderer(sv));
        tree.setCellRenderer(new DefaultTreeRenderer(sv));
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(sv));
        
        for (int i = 1; i < keys.length; i++) {
            installColumnRenderers(i, new DefaultTableRenderer(stringValues.get(keys[i])));
        }
        // <snip> Unrelated, just for fun: Hyperlink 
        // Use a hyperlinkRenderer for the email column
        HyperlinkProvider provider = new HyperlinkProvider(new ContributorMailAction(
                stringValues.get("email")));
        installColumnRenderers(keys.length - 1, new DefaultTableRenderer(provider));
        table.getColumnExt(keys.length - 1).setToolTipText(
                "Note: the mail-to action will do nothing in security restricted environments");
        // </snip>
        table.packAll();
    }

    private void installColumnRenderers(int column, TableCellRenderer renderer) {
        if (column >= table.getColumnCount()) return;
        table.getColumn(column).setCellRenderer(renderer);
        treeTable.getColumn(column).setCellRenderer(renderer);
    }

    /**
     * Prepare different String representations.
     */
    // wie in SearchDemo!
    private void initStringRepresentation() {
        stringValues = new HashMap<String, StringValue>();
        StringValue nameValue = new StringValue() {

            @Override
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
                    return StringValues.DATE_TO_STRING.getString(
                            ((Contributor) value).getJoinedDate());
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
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
        // <snip> Unrelated, just for fun: Hyperlink 
        // string representation of contributor's email
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
        // </snip>
        stringValues.put("email", emailValue);

    }

    // <snip> Unrelated, just for fun: Hyperlink 
    // custom hyperlink action which delegates to Desktop 
    public static class ContributorMailAction extends AbstractHyperlinkAction<Contributor> {
        HyperlinkAction browse = HyperlinkAction.createHyperlinkAction(null,
                java.awt.Desktop.Action.MAIL);

        StringValue sv;
        
        public ContributorMailAction(StringValue sv) {
            this.sv = sv;
        }
        
        @Override
        protected void installTarget() {
            if (sv == null) return;
            // configure the name based on the StringValue
            setName(sv.getString(getTarget()));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (target == null) return;
            browse.setTarget(target.getEmail());
            browse.actionPerformed(null);
        }
        // </snip>
    }
        
}
