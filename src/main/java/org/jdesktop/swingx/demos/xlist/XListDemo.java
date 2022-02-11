/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.demos.search.Contributor;
import org.jdesktop.swingx.demos.search.Contributors;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.rollover.RolloverProducer;
import org.jdesktop.swingx.sort.DefaultSortController;
import org.jdesktop.swingx.util.PaintUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXList}.
 *
 * @author Karl George Schaefer
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXList Demo",
//    category = "Data",
//    description = "Demonstrates JXList, an enhanced list component.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/xlist/XListDemo.java",
//        "org/jdesktop/swingx/demos/xlist/resources/XListDemo.properties"
//    }
//)
public class XListDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -1398533665658062231L;
    private static final Logger LOG = Logger.getLogger(XListDemo.class.getName());

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
				AbstractDemo demo = new XListDemo(controller);
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

    private JXList<Contributor> list;
    
    // Controller:
    private JButton toggleSortOrder;
    private JButton resetSortOrder;
    private JXComboBox<DisplayInfo<Comparator<?>>> comparatorCombo;
    
    /**
     * to enable rollover support
     */
    private JCheckBox rolloverEnabledBox;
    /**
     * 3 Highlighter are implemented / createRolloverHighlighters :
     * <br> Background Color / MAGENTA (default, when rollover support enabled)
     * <br> Foreground Color / MAGENTA
     * <br> Related Merit / YELLOW - highlight items "near" (+-5) to current rollover item
     */
    private JXComboBox<DisplayInfo<Highlighter>> highlighterCombo;

    /**
     * XListDemo Constructor
     */
    public XListDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getString("name"));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
        list = new JXList<Contributor>();
        list.setName("list");

        JPanel monthViewContainer = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "5dlu, f:d:g ", // l:4dlu:n, f:d:g", // columns
                "c:d:n " +
                ", t:4dlu:n, f:d:g " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, monthViewContainer);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("listSeparator"); // prop listSeparator.title = JXList :
        areaSeparator.setTitle("JXList");
        
        builder.add(areaSeparator, cc.xywh(1, 1, 2, 1));
        builder.add(new JScrollPane(list), cc.xywh(2, 3, 1, 1));
        
        
        add(monthViewContainer, BorderLayout.CENTER);
        
        // configureComponents:
        // <snip> JXList rendering
        // custom String representation: concat various element fields
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                if (value instanceof Contributor) {
                    Contributor c = (Contributor) value;
                    return c.getFirstName() + " " + c.getLastName() + " (" + c.getMerits() + ")";
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        list.setCellRenderer(new DefaultListRenderer<Contributor>(sv));
        // </snip>
        
        // PENDING JW: add visual clue to current sortorder
//        toggleSortOrder.setAction(DemoUtils.getAction(this, "toggleSortOrder")); // @Action public void toggleSortOrder() {...
//        resetSortOrder.setAction(DemoUtils.getAction(this, "resetSortOrder"));
        
        // TODO
//        comparatorCombo.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
//        highlighterCombo.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        
        // demo specific config
//        DemoUtils.setSnippet("JXList sorting", toggleSortOrder, resetSortOrder, comparatorCombo);
//        DemoUtils.setSnippet("JXList rollover support", rolloverEnabledBox, highlighterCombo);
//        DemoUtils.setSnippet("JXList rendering", list);

//        bind():
        list.setAutoCreateRowSorter(true);
        list.setModel(Contributors.getContributorListModel());        
    }

//    private void setComparator(Comparator<?> comparator) {
//        // <snip> JXList sorting
//        //  configure the comparator to use in sorting
//        list.setComparator(comparator);
//        if (list.getSortOrder() != SortOrder.UNSORTED) {
//            // PENDING missing refresh api?
//            ((DefaultSortController<?>) list.getRowSorter()).sort();
//        }
//        // </snip>
//    }
    
    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel();

        JComponent extended = createExtendedConfigPanel(); // Controller
        controller.add(extended);
    	return controller;
	}

    private JComponent createExtendedConfigPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:g", // , l:4dlu:n, f:d:g", // columns
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("extendedSeparator");
        areaSeparator.setTitle("Sort Control"); // prop extendedSeparator.title = Sort Control

        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        toggleSortOrder = new JButton();
        toggleSortOrder.setName("toggleSortOrder");
        toggleSortOrder.setText("Toggle Sort Order"); // prop toggleSortOrder.Action.text = Toggle Sort Order
        toggleSortOrder.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+toggleSortOrder.isSelected());
        	list.toggleSortOrder(); // Delegates to the SortController, defined by the SortController's toggleSortOrder implementation
        });
        builder.add(toggleSortOrder, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        resetSortOrder = new JButton();
        resetSortOrder.setName("resetSortOrder");
        resetSortOrder.setText("Reset Sort Order");// prop resetSortOrder.Action.text = Reset Sort Order
        resetSortOrder.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+resetSortOrder.isSelected());
        	list.resetSortOrder(); // Delegates to the SortController, defined by the SortController's toggleSortOrder implementation
        	//LOG.info("actionEvent:"+list.getSortController() is protected);
        });
        builder.add(resetSortOrder, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        comparatorCombo = new JXComboBox<DisplayInfo<Comparator<?>>>();
        comparatorCombo.setName("comparatorCombo");
        comparatorCombo.setModel(createComparators());

		// set default Comparator, by display string:
        DisplayInfo<Comparator<?>> defaultComp = (DisplayInfo<Comparator<?>> )comparatorCombo.getItemAt(0);
    	list.setComparator(defaultComp.getValue());

        comparatorCombo.addActionListener(ae -> {
        	LOG.info("actionEvent:"+ae 
        			+ " ActionCommand="+comparatorCombo.getActionCommand() // comboBoxChanged
        			+ " SelectedItem="+comparatorCombo.getSelectedItem()
        			+ " getAction="+comparatorCombo.getAction());
        	DisplayInfo<Comparator<?>> di = (DisplayInfo<Comparator<?>>)comparatorCombo.getSelectedItem();
        	list.setComparator(di.getValue());
        });
     
        JLabel comparatorComboLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                comparatorCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
        comparatorComboLabel.setName("comparatorComboLabel");
        comparatorComboLabel.setText("Comparator"); // comparatorComboLabel.text = Comparator
        currentRow += 2;
        
        currentRow += 2;
        JXTitledSeparator rolloverSeparator = new JXTitledSeparator();
        rolloverSeparator.setName("rolloverSeparator");
        rolloverSeparator.setTitle("Rollover Control"); // rolloverSeparator.title = Rollover Control
        builder.add(rolloverSeparator, cc.xywh(1, currentRow, 4, 1));
        currentRow += 2;

		rolloverEnabledBox = new JCheckBox();
		rolloverEnabledBox.setName("rolloverBox");
		rolloverEnabledBox.setText("Rollover Enabled"); // rolloverBox.text = Rollover Enabled
		rolloverEnabledBox.addActionListener( ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+rolloverEnabledBox.isSelected());
        	setRolloverEnabled(rolloverEnabledBox.isSelected());
        });
		builder.add(rolloverEnabledBox, cc.xywh(labelColumn, currentRow, 3, 1));
		currentRow += 2;

		highlighterCombo = new JXComboBox<DisplayInfo<Highlighter>>(createRolloverHighlighters());
		highlighterCombo.setName("highlighterCombo");
		highlighterCombo.setModel(createRolloverHighlighters());
		
		// set default Highlighter:
		DisplayInfo<Highlighter> defaultHl = (DisplayInfo<Highlighter>)highlighterCombo.getItemAt(0);
		list.addHighlighter(defaultHl.getValue());
		
		highlighterCombo.addActionListener(ae -> {
        	LOG.info("actionEvent:"+ae 
        			+ " ActionCommand="+highlighterCombo.getActionCommand() // comboBoxChanged
        			+ " SelectedItem="+highlighterCombo.getSelectedItem()
        			+ " getAction="+highlighterCombo.getAction());
        	// remove all Highlighter's:
        	for(int i=0; i<highlighterCombo.getItemCount(); i++) {
        		DisplayInfo<Highlighter> di = (DisplayInfo<Highlighter>)highlighterCombo.getItemAt(i);
            	list.removeHighlighter(di.getValue());
        	}
        	// add selected Highlighter
        	DisplayInfo<Highlighter> di = (DisplayInfo<Highlighter>)highlighterCombo.getSelectedItem();
        	list.addHighlighter(di.getValue());
        });
        
		JLabel highlighterComboLabel = builder.addLabel(
				"", cl.xywh(labelColumn, currentRow, 1, 1), 
				highlighterCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
		highlighterComboLabel.setName("highlighterComboLabel");
		currentRow += 2;

        return painterControl;
    }

    /*
     * die 3 Elemente von DefaultComboBoxModel sind:
     *  - null
     *  - Comparator DefaultSortController<M> extends DefaultRowSorter<M, Integer>
     *  - Comparator<Contributor> meritComparator
     * Also alles scheinbar verschiedene Objekte, nein alle implementieren Comparator interface
     * DisplayInfo verwenden, damit description angezeigt wird. 
     */
    private ComboBoxModel<DisplayInfo<Comparator<?>>> createComparators() {
        DefaultComboBoxModel<DisplayInfo<Comparator<?>>> model = new DefaultComboBoxModel<DisplayInfo<Comparator<?>>>();
        // <snip> JXList sorting
        // null comparator defaults to comparing by the display string
        model.addElement(new DisplayInfo<Comparator<?>>("None (by display string)", null));
        // compare by Comparable as implemented by the elements
        model.addElement(new DisplayInfo<Comparator<?>>("Comparable (by lastname)", DefaultSortController.COMPARABLE_COMPARATOR));
        // custom comparator
        Comparator<Contributor> meritComparator = new Comparator<Contributor>() {

            @Override
            public int compare(Contributor o1, Contributor o2) {
                return o1.getMerits() - o2.getMerits();
            }
        };
        // </snip>
        model.addElement(new DisplayInfo<Comparator<?>>("Custom (by merits)", meritComparator));
        return model;
    }

    /*
     * interface ComboBoxModel<E> extends ListModel<E>
     *                                                             interface MutableComboBoxModel<E> extends ComboBoxModel<E>
     * class DefaultComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>
     * 
     * die Elemente von DefaultComboBoxModel sind von Typ Highlighter,
     * bzw DisplayInfo<Highlighter>, damit description angezeigt wird
     * 
     * aus /swingx-demos/swingx-demos-swingxset/ org.jdesktop.swingx.binding.DisplayInfo<T> :
     * kapselt ein Objekt/item T mit zugehöriger String description
     * Also z.B. 
     *  item: new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.MAGENTA, null)
     *  desc: "Background Color"
     */
    private ComboBoxModel<DisplayInfo<Highlighter>> createRolloverHighlighters() {	
        DefaultComboBoxModel<DisplayInfo<Highlighter>> model = new DefaultComboBoxModel<DisplayInfo<Highlighter>>();
        
        // <snip> JXList rollover support
        // simple decorations of rollover row 
        DisplayInfo<Highlighter> background = new DisplayInfo<Highlighter>("Background Color"
        		, new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.MAGENTA, null)
        		);
        model.addElement(background);
        
        DisplayInfo<Highlighter> foreground = new DisplayInfo<Highlighter>("Foreground Color"
        		, new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.MAGENTA)
        		);
        model.addElement(foreground);
        // </snip>
        
        DisplayInfo<Highlighter> merit = new DisplayInfo<Highlighter>("Related Merit"
        		, createExtendedRolloverDecoration()
        		);
        model.addElement(merit);
        return model;
    }

    public void setRolloverEnabled(boolean enabled) {
        list.setRolloverEnabled(enabled);
        list.setToolTipText(list.isRolloverEnabled() ? getString("stickyRolloverToolTip") : null);
    }
    
    private Highlighter createExtendedRolloverDecoration() {
        Color color = PaintUtils.setAlpha(Color.YELLOW, 100);
        final PainterHighlighter hl = new PainterHighlighter(HighlightPredicate.NEVER, new MattePainter(color));
        // <snip> JXList rollover support
        // listen to changes of cell-rollover property and set a Highlighters custom predicate accordingly
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Point location = (Point) evt.getNewValue();
                int row = -1;
                if (location != null) {
                    row = location.y;
                }
                hl.setHighlightPredicate(new MeritRangeHighlightPredicate(
                        row < 0 ? null : list.getElementAt(row))); 
            }
            
        };
        list.addPropertyChangeListener(RolloverProducer.ROLLOVER_KEY, l);
        // </snip>
        return hl;
    }

    public static class MeritRangeHighlightPredicate implements HighlightPredicate {

        private Contributor compare;

        public MeritRangeHighlightPredicate(Object object) {
            this.compare = object instanceof Contributor ? (Contributor) object : null;
        }

        @Override
        // <snip> JXList rollover support
        // custom HighlightPredicate which compare the current value
        // against a fixed value and returns true if "near" (+-5)
        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (compare == null) return false;
            if (!(adapter.getValue() instanceof Contributor)) return false;
            Contributor contributor = (Contributor) adapter.getValue();
            return contributor.getMerits() >= compare.getMerits() - 5 && 
                contributor.getMerits() <= compare.getMerits() + 5;
        }
        // </snip>
        
    }
    
}
