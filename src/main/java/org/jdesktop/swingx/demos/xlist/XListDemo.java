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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
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
////TODO implement
//@DemoProperties(
//    value = "JXList Demo",
//    category = "Data",
//    description = "Demonstrates JXList, an enhanced list component.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/xlist/XListDemo.java",
//        "org/jdesktop/swingx/demos/xlist/resources/XListDemo.properties"
//    }
//)
//@SuppressWarnings("serial")
public class XListDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -1398533665658062231L;

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
    private JComboBox comparatorCombo;
    private JButton toggleSortOrder;
    private JButton resetSortOrder;
    private JCheckBox rolloverEnabledBox;
    private JComboBox highlighterCombo;

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
        // TODO prop listSeparator.title = JXList

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
        areaSeparator.setName("listSeparator");
        
        builder.add(areaSeparator, cc.xywh(1, 1, 2, 1));
        builder.add(new JScrollPane(list), cc.xywh(2, 3, 1, 1));
        
        
        add(monthViewContainer, BorderLayout.CENTER);
        
//        JComponent extended = createExtendedConfigPanel(); // Controller
//        add(extended, BorderLayout.EAST);
        
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
        // PENDING JW: add icon (see demos in swingx)
        // set a renderer configured with the custom string converter
        list.setCellRenderer(new DefaultListRenderer(sv));
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

//
//    public XListDemo() {
//        super(new BorderLayout());
//        initComponents();
//        configureComponents();
//        DemoUtils.injectResources(this);
//        bind():
        list.setAutoCreateRowSorter(true);
        list.setModel(Contributors.getContributorListModel());        
    }

    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel();

        JComponent extended = createExtendedConfigPanel(); // Controller
        comparatorCombo.setModel(createComparators());
        highlighterCombo.setModel(createRolloverHighlighters());

        controller.add(extended);
    	return controller;
	}

//---------------- public api for Binding/Action control
    
//    @Action
//    // <snip> JXList sorting
//    //  api to toggle sorts
//    public void toggleSortOrder() {
//        list.toggleSortOrder();
//    }
//    // </snip>
//
//    @Action
//    public void resetSortOrder() {
//        list.resetSortOrder();
//    }
    
    public void setComparator(Comparator<?> comparator) {
        // <snip> JXList sorting
        //  configure the comparator to use in sorting
        list.setComparator(comparator);
        if (list.getSortOrder() != SortOrder.UNSORTED) {
            // PENDING missing refresh api?
            ((DefaultSortController<?>) list.getRowSorter()).sort();
        }
        // </snip>
    }
    
    
    public void setRolloverHighlighter(Highlighter hl) {
        list.setHighlighters(hl);
    }
 
    public void setRolloverEnabled(boolean enabled) {
        list.setRolloverEnabled(enabled);
        list.setToolTipText(list.isRolloverEnabled() ? getString("stickyRolloverToolTip") : null);
    }
//------------------- ui configuration    
//    private void configureComponents() {
//        
//        // <snip> JXList rendering
//        // custom String representation: concat various element fields
//        StringValue sv = new StringValue() {
//
//            @Override
//            public String getString(Object value) {
//                if (value instanceof Contributor) {
//                    Contributor c = (Contributor) value;
//                    return c.getFirstName() + " " + c.getLastName() + " (" + c.getMerits() + ")";
//                }
//                return StringValues.TO_STRING.getString(value);
//            }
//            
//        };
//        // PENDING JW: add icon (see demos in swingx)
//        // set a renderer configured with the custom string converter
//        list.setCellRenderer(new DefaultListRenderer(sv));
//        // </snip>
//        
//        // PENDING JW: add visual clue to currentl sortorder
//        toggleSortOrder.setAction(DemoUtils.getAction(this, "toggleSortOrder"));
//        resetSortOrder.setAction(DemoUtils.getAction(this, "resetSortOrder"));
//        
//        comparatorCombo.setRenderer(
//                new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
//        highlighterCombo.setRenderer(
//                new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
//        
//        // demo specific config
//        DemoUtils.setSnippet("JXList sorting", toggleSortOrder, resetSortOrder, comparatorCombo);
//        DemoUtils.setSnippet("JXList rollover support", rolloverEnabledBox, highlighterCombo);
//        DemoUtils.setSnippet("JXList rendering", list);
//    }

    @SuppressWarnings("unchecked")
    private void bind() {
        // list properties
        // <snip> JXlist sorting
        // enable auto-create RowSorter
        list.setAutoCreateRowSorter(true);
        list.setModel(Contributors.getContributorListModel());
        //</snip>
        
        // control combos
        comparatorCombo.setModel(createComparators());
        highlighterCombo.setModel(createRolloverHighlighters());
        
//        BindingGroup group = new BindingGroup();
//        group.addBinding(Bindings.createAutoBinding(READ, 
//                rolloverEnabledBox, BeanProperty.create("selected"),
//                this, BeanProperty.create("rolloverEnabled")));
//        Binding comparatorBinding = Bindings.createAutoBinding(READ, 
//                comparatorCombo, BeanProperty.create("selectedItem"),
//                this, BeanProperty.create("comparator"));
//        comparatorBinding.setConverter(new DisplayInfoConverter<Comparator<?>>());
//        group.addBinding(comparatorBinding);
//    
//        Binding rolloverBinding = Bindings.createAutoBinding(READ, 
//                highlighterCombo, BeanProperty.create("selectedItem"),
//                this, BeanProperty.create("rolloverHighlighter"));
//        rolloverBinding.setConverter(new DisplayInfoConverter<Highlighter>());
//        group.addBinding(rolloverBinding);
//        
//        group.bind();
    }

    /*
     * interface ComboBoxModel<E> extends ListModel<E>
     *                                                             interface MutableComboBoxModel<E> extends ComboBoxModel<E>
     * class DefaultComboBoxModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>
     * 
     * die Elemente von DefaultComboBoxModel sind von Typ Highlighter
     * damit kann das Ergebnis ComboBoxModel<Highlighter> sein
     */
    private ComboBoxModel<Highlighter> createRolloverHighlighters() {
        DefaultComboBoxModel<Highlighter> model = new DefaultComboBoxModel<Highlighter>();
        // <snip> JXList rollover support
        // simple decorations of rollover row 
/* aus /swingx-demos/swingx-demos-swingxset/
org.jdesktop.swingx.binding.DisplayInfo<T> kapselt ein Objekt/item T mit zugehöriger String description

also item: new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.MAGENTA, null)
     desc: "Background Color"
 */
//        model.addElement(new DisplayInfo<Highlighter>("Background Color",
//                new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.MAGENTA, null)));
//        model.addElement(new DisplayInfo<Highlighter>("Foreground Color",
//                new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.MAGENTA)));
//        // </snip>
//        model.addElement(new DisplayInfo<Highlighter>("Related Merit", 
//                createExtendedRolloverDecoration()));
        model.addElement(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, Color.MAGENTA, null)); // Background
        model.addElement(new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, null, Color.MAGENTA)); // Foreground
        model.addElement(createExtendedRolloverDecoration()); // Related Merit
        return model;
    }

    /*
     * die 3 Elemente von DefaultComboBoxModel sind:
     *  - null
     *  - Comparator DefaultSortController<M> extends DefaultRowSorter<M, Integer>
     *  - Comparator<Contributor> meritComparator
     * Also alles scheinbar verschiedene Objekte
     */
    private ComboBoxModel createComparators() {
        DefaultComboBoxModel<Comparator> model = new DefaultComboBoxModel<Comparator>();
        // <snip> JXList sorting
        //  null comparator defaults to comparing by the display string
//        model.addElement(new DisplayInfo<Comparator<?>>("None (by display string)", null));
        model.addElement((Comparator)null);
        // compare by Comparable as implemented by the elements
//        model.addElement(new DisplayInfo<Comparator<?>>("Comparable (by lastname)", DefaultSortController.COMPARABLE_COMPARATOR));
        model.addElement(DefaultSortController.COMPARABLE_COMPARATOR);
        // custom comparator
        Comparator<Contributor> meritComparator = new Comparator<Contributor>() {

            @Override
            public int compare(Contributor o1, Contributor o2) {
                return o1.getMerits() - o2.getMerits();
            }
        };
        // </snip>
//        model.addElement(new DisplayInfo<Comparator<?>>("Custom (by merits)", meritComparator));
        model.addElement(meritComparator);
        return model;
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
        // against a fixed value and returns true if "near"
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
    
//-------------------- init ui
    
//    private void initComponents() {
//        list = new JXList();
//        list.setName("list");
//
//        JPanel monthViewContainer = new JXPanel();
//        FormLayout formLayout = new FormLayout(
//                "5dlu, f:d:g ", // l:4dlu:n, f:d:g", // columns
//                "c:d:n " +
//                ", t:4dlu:n, f:d:g " +
//                ", t:4dlu:n, c:d:n" +
//                ", t:4dlu:n, c:d:n" +
//                ", t:4dlu:n, c:d:n"
//        ); // rows
//        PanelBuilder builder = new PanelBuilder(formLayout, monthViewContainer);
//        builder.setBorder(Borders.DLU4_BORDER);
////        CellConstraints cl = new CellConstraints();
//        CellConstraints cc = new CellConstraints();
//        
//        JXTitledSeparator areaSeparator = new JXTitledSeparator();
//        areaSeparator.setName("listSeparator");
//        builder.add(areaSeparator, cc.xywh(1, 1, 2, 1));
//        builder.add(new JScrollPane(list), cc.xywh(2, 3, 1, 1));
//        
//        
//        add(monthViewContainer, BorderLayout.CENTER);
//        
//        JComponent extended = createExtendedConfigPanel();
//        add(extended, BorderLayout.EAST);
//    }

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
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        toggleSortOrder = new JButton();
        toggleSortOrder.setName("toggleSortOrder");
        builder.add(toggleSortOrder, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        resetSortOrder = new JButton();
        resetSortOrder.setName("resetSortOrder");
        builder.add(resetSortOrder, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        comparatorCombo = new JComboBox();
        comparatorCombo.setName("comparatorCombo");
        JLabel comparatorComboLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                comparatorCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
        comparatorComboLabel.setName("comparatorComboLabel");
//        LabelHandler.bindLabelFor(comparatorComboLabel, comparatorCombo); // JComboBox comparatorCombo
        currentRow += 2;
        
        currentRow += 2;
        JXTitledSeparator rolloverSeparator = new JXTitledSeparator();
        rolloverSeparator.setName("rolloverSeparator");
        builder.add(rolloverSeparator, cc.xywh(1, currentRow, 4, 1));
        currentRow += 2;

          rolloverEnabledBox = new JCheckBox();
          rolloverEnabledBox.setName("rolloverBox");
          builder.add(rolloverEnabledBox, cc.xywh(labelColumn, currentRow, 3, 1));
          currentRow += 2;
          
          highlighterCombo = new JComboBox();
          highlighterCombo.setName("highlighterCombo");
          JLabel highlighterComboLabel = builder.addLabel(
                  "", cl.xywh(labelColumn, currentRow, 1, 1),
                  highlighterCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
          highlighterComboLabel.setName("highlighterComboLabel");
//          LabelHandler.bindLabelFor(highlighterComboLabel, highlighterCombo); // JComboBox highlighterCombo
          currentRow += 2;

        return painterControl;
    }

//--------------------- dummy api (to keep bbb happy)
    
    public Comparator<?> getComparator() {
        return null;
    }
    
    public Highlighter getRolloverHighlighter() {
        return null;
    }
    
    public boolean isRolloverEnabeld() {
        return list.isRolloverEnabled();
    }
}
