/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.xlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
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
 * @author EUG https://github.com/homebeaver (reorg + controller for cellsLayout and selection mode)
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
public class XListDemo extends AbstractDemo implements ListDemoConstants {
    
	private static final long serialVersionUID = -1398533665658062231L;
    private static final Logger LOG = Logger.getLogger(XListDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXList, an enhanced list component.";

	/**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	// invokeLater method can be invoked from any thread
    	SwingUtilities.invokeLater( () -> {
    		// ...create UI here...
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new XListDemo(controller);
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

    private JXList<Contributor> list;
    
    // Controller:
    // layout of cells , listLayoutOrientation
    private JComboBox<String> cellsLayout;
    
    // selection mode
    private JComboBox<String> selectionMode;

    // drop mode
    private JComboBox<String> dropMode;

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
     * 
     * @param frame controller Frame
     */
    public XListDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
        list = new JXList<Contributor>();
        list.setName("list");

        JPanel listContainer = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "5dlu, f:d:g ", // l:4dlu:n, f:d:g", // columns
                "c:d:n " +
                ", t:4dlu:n, f:d:g " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, listContainer);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("listSeparator");
        areaSeparator.setTitle(getBundleString("listSeparator.title"));
        
        builder.add(areaSeparator, cc.xywh(1, 1, 2, 1));
        builder.add(new JScrollPane(list), cc.xywh(2, 3, 1, 1));
        
        add(listContainer, BorderLayout.CENTER);
//        add(list, BorderLayout.CENTER);
        
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
        
        // Set the preferred row count. This affects the preferredSize of the JList when it's in a scrollpane.
        // In HORIZONTAL_WRAP and VERTICAL_WRAP orientations affects how cells are wrapped.
        list.setVisibleRowCount(20);

        // </snip>
        
        // PENDING JW: add visual clue to current sort order
        list.setAutoCreateRowSorter(true);
        list.setModel(Contributors.getContributorListModel());
        
        list.setDragEnabled(true);
        list.setDropMode(DropMode.ON);
        list.setTransferHandler(new ListTransferHandler() {
// ---------------------
    	    protected Transferable createTransferable(JComponent c) {
    	    	String s = exportString(c);
    	    	LOG.info("exportString="+s);
    	        return new StringSelection(s);
    	    }
    	    protected String exportString(JComponent c) {
//    	    	LOG.info("JComponent type="+c.getClass());
    	    	StringBuilder buff = new StringBuilder();
    	    	if(c instanceof JXList<?> xlist) {
    	    		setIndizes(list.getSelectedIndices());
    	    		List<?> l = xlist.getSelectedValuesList();
    	    		Object[] values = new Object[l.size()];
    	    		l.toArray(values); // fill the array
    	    		for (int i = 0; i < values.length; i++) {
    	                Object val = values[i];
    	                if(val instanceof Contributor contributor) {
        	    	    	LOG.info("values["+i+"]="+contributor.getFirstName()
    	    	    			+" "+contributor.getLastName()
        	    	    		+" ("+contributor.getMerits()+")");
        	                buff.append(val == null ? "" : contributor.getFirstName()
        	                	+" "+contributor.getLastName()
        	                	+" ("+contributor.getMerits()+")");
    	                } else {
        	    	    	LOG.info("values["+i+"]="+val);
        	                buff.append(val == null ? "" : val.toString());
    	                }
    	                if (i != values.length - 1) {
    	                    buff.append("\n");
    	                }
    	    		}
    	    	}
    	        return buff.toString();
    	    }
    	    public int getSourceActions(JComponent c) {
//    	    	LOG.info("return 2:MOVE -unused JComponent: "+c);
//    	        return TransferHandler.MOVE;
    	    	LOG.config("return 3:COPY_OR_MOVE -unused JComponent "+c);
    	        return TransferHandler.COPY_OR_MOVE;
    	    }
    	    public boolean importData(TransferHandler.TransferSupport info) {
//    	    	LOG.info("-----------TransferHandler.TransferSupport "+info);
//    	    	return super.importData(info);
    	        if (!info.isDrop()) {
    	            return false;
    	        }

    	        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
    	        int index = dl.getIndex();
    	        boolean insert = dl.isInsert();

    	        // Get the string that is being dropped.
    	        Transferable t = info.getTransferable();
    	        String data;
    	        try {
    	            data = (String)t.getTransferData(DataFlavor.stringFlavor);
    	        } 
    	        catch (Exception e) { return false; }

    	        LOG.info("DropLocation:"+dl + " data:"+data);
    	        Component comp = info.getComponent();
    	        if(comp instanceof JList<?> list) {
    	        	ListModel<?> listModel = list.getModel();
    	        	if(listModel instanceof DefaultComboBoxModel<?>) {
    	        		DefaultComboBoxModel<Contributor> model = (DefaultComboBoxModel<Contributor>)listModel;
    	        		// Perform the actual import. Split data at nl to contributor
    	        		StringTokenizer tokenizer = new StringTokenizer(data, "\n\r\f", false);
    	        		while(tokenizer.hasMoreElements()) {
    	        			String contributor = tokenizer.nextToken();
    	        			if (insert || index<0) {
    	        				model.addElement(new Contributor(contributor));
    	        			} else {
    	        				model.insertElementAt(new Contributor(contributor), index);
    	        			}
    	        		}
    	                return true;
    	        	}
    	        }
    	        return false;
    	    }
    	    protected void exportDone(JComponent c, Transferable data, int action) {
    	    	// 0:NONE
    	    	// 1:COPY
    	    	// 2:TransferHandler.MOVE
    	    	LOG.info("action="+action+", Transferable:"+data);
    	        super.exportDone(listContainer, data, action);
    	    }
// ---------------------
        });
        
    }

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
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
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
        
        JXTitledSeparator jListSeparator = new JXTitledSeparator();
        jListSeparator.setName("jListSeparator");
        jListSeparator.setTitle(getBundleString("jListSeparator.title"));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 1;
        builder.add(jListSeparator, cc.xywh(1, currentRow, 4, 1));
        currentRow += 2;

        cellsLayout = new JComboBox<String>(LIST_LAYOUT_ORIENTATION);
        cellsLayout.setName("cellsLayout");

		// set default cells Layout
        cellsLayout.setSelectedIndex(VERTICAL_WRAP);
    	list.setLayoutOrientation(VERTICAL_WRAP);

    	cellsLayout.addActionListener(ae -> {
        	cellsLayout.setSelectedIndex(cellsLayout.getSelectedIndex());
        	list.setLayoutOrientation(cellsLayout.getSelectedIndex());
        });
     
        JLabel cellsLayoutLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                cellsLayout, cc.xywh(widgetColumn, currentRow, 1, 1));
        cellsLayoutLabel.setName("cellsLayoutLabel");
        cellsLayoutLabel.setText(getBundleString("cellsLayoutLabel.text"));
        currentRow += 2;

        selectionMode = new JComboBox<String>(SELECTION_MODE);
        selectionMode.setName("selectionMode");

		// set default selection mode
        selectionMode.setSelectedIndex(SINGLE_INTERVAL_SELECTION); // default is SINGLE_SELECTION
    	list.setSelectionMode(SINGLE_INTERVAL_SELECTION);

    	selectionMode.addActionListener(ae -> {
    		selectionMode.setSelectedIndex(selectionMode.getSelectedIndex());
        	list.setSelectionMode(selectionMode.getSelectedIndex());
        });
     
        JLabel selectionModeLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                selectionMode, cc.xywh(widgetColumn, currentRow, 1, 1));
        selectionModeLabel.setName("selectionModeLabel");
        selectionModeLabel.setText(getBundleString("selectionModeLabel.text"));
        currentRow += 2;

        dropMode = new JComboBox<String>(DROP_MODE);
        dropMode.setName("dropMode");

		// set default drop mode
        dropMode.setSelectedIndex(DropMode.ON.ordinal()); // DropMode.ON , default is USE_SELECTION
//    	list.setDropMode(DropMode.ON);

        dropMode.addActionListener(ae -> {
        	int i = dropMode.getSelectedIndex();
        	dropMode.setSelectedIndex(i);
            switch (i) {
            case 0:
            	list.setDropMode(DropMode.USE_SELECTION);
            	break;
            case 1:
            	list.setDropMode(DropMode.ON);
            	break;
            case 2:
            	list.setDropMode(DropMode.INSERT);
            	break;
            case 3:
            	list.setDropMode(DropMode.ON_OR_INSERT);
            	break;
            default:
            }
        });
     
        JLabel dropModeLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                dropMode, cc.xywh(widgetColumn, currentRow, 1, 1));
        dropModeLabel.setName("dropModeLabel");
        dropModeLabel.setText(getBundleString("dropModeLabel.text"));
        currentRow += 2;

        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("extendedSeparator");
        areaSeparator.setTitle(getBundleString("extendedSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, currentRow, 4, 1));
        currentRow += 2;

        toggleSortOrder = new JButton();
        toggleSortOrder.setName("toggleSortOrder");
        toggleSortOrder.setText(getBundleString("toggleSortOrder.Action.text")); 
        toggleSortOrder.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+toggleSortOrder.isSelected());
        	list.toggleSortOrder(); // Delegates to the SortController, defined by the SortController's toggleSortOrder implementation
        });
        builder.add(toggleSortOrder, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        resetSortOrder = new JButton();
        resetSortOrder.setName("resetSortOrder");
        resetSortOrder.setText(getBundleString("resetSortOrder.Action.text")); 
        resetSortOrder.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+resetSortOrder.isSelected());
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
        	list.toggleSortOrder();
        });
     
        JLabel comparatorComboLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                comparatorCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
        comparatorComboLabel.setName("comparatorComboLabel");
        comparatorComboLabel.setText(getBundleString("comparatorComboLabel.text"));
        currentRow += 2;
        
        currentRow += 2;
        JXTitledSeparator rolloverSeparator = new JXTitledSeparator();
        rolloverSeparator.setName("rolloverSeparator");
        rolloverSeparator.setTitle(getBundleString("rolloverSeparator.title"));
        builder.add(rolloverSeparator, cc.xywh(1, currentRow, 4, 1));
        currentRow += 2;

		rolloverEnabledBox = new JCheckBox();
		rolloverEnabledBox.setName("rolloverBox");
		rolloverEnabledBox.setText(getBundleString("rolloverBox.text"));
		rolloverEnabledBox.addActionListener( ae -> {
        	LOG.fine("actionEvent:"+ae + " selected="+rolloverEnabledBox.isSelected());
            list.setRolloverEnabled(rolloverEnabledBox.isSelected());
            list.setToolTipText(list.isRolloverEnabled() ? getBundleString("stickyRolloverToolTip") : null);
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
        
		JLabel highlighterComboLabel = builder.addLabel("&Highlighter" // label textWithMnemonic
			, cl.xywh(labelColumn, currentRow, 1, 1), highlighterCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
		highlighterComboLabel.setName("highlighterComboLabel");
//		highlighterComboLabel.setText(getBundleString("highlighterComboLabel.text")); // no Mnemonics in props
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
