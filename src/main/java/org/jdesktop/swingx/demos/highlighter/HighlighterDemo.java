/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.highlighter;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.binding.ArrayAggregator;
import org.jdesktop.swingx.binding.BindingAdapter;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.decorator.AlignmentHighlighter;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.EnabledHighlighter;
import org.jdesktop.swingx.decorator.FontHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.decorator.ToolTipHighlighter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ShapeUtils;
import org.jdesktop.swingxset.util.ComponentModels;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

/**
 * A demo for {@code Highlighter}.
 *
 * @author Karl George Schaefer
 * @author Jeanette Winzenburg (original JXTableDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "Highlighter Demo",
//    category = "Functionality",
//    description = "Demonstrates Highlighters, a lightweight, reusable, visual decorator.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/highlighter/HighlighterDemo.java",
//        "org/jdesktop/swingx/demos/highlighter/resources/HighlighterDemo.properties",
//        "org/jdesktop/swingx/demos/highlighter/resources/HighlighterDemo.html",
//        "org/jdesktop/swingx/demos/highlighter/resources/images/HighlighterDemo.png"
//    }
//)
public class HighlighterDemo extends AbstractDemo {

	private static final long serialVersionUID = -7205284335967801438L;

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
				AbstractDemo demo = new HighlighterDemo(controller);
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

	private JXList list;
    private JXTable table;
    private JXTree tree;
    private JXTreeTable treeTable;
    private JXComboBox comboBox;
    
    // Controller:
    private JComboBox stripingOptions;
    private JComboBox highlighters;
    private JComboBox predicates;
    
    /**
     * HighlighterDemo Constructor
     */
    public HighlighterDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getString("name"));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        list = new JXList();
        list.setName("list");
        list.setCellRenderer(new DefaultListRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof Component) {
                    return value.getClass().getSimpleName() + " (" + ((Component) value).getName() + ")";
                }
                    
                return StringValues.TO_STRING.getString(value);
            }
        }));
        tabbedPane.addTab("JXList", new JScrollPane(list));
        
        table = new JXTable();
        tabbedPane.addTab("JXTable", new JScrollPane(table));
        
        tree = new JXTree();
        tree.setName("tree");
        tree.setCellRenderer(new DefaultTreeRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof Component) {
                    return value.getClass().getSimpleName() + " (" + ((Component) value).getName() + ")";
                }
                    
                return StringValues.TO_STRING.getString(value);
            }
        }));
        tabbedPane.addTab("JXTree", new JScrollPane(tree));
        
        treeTable = new JXTreeTable();
        treeTable.setName("treeTable");
        treeTable.setShowGrid(true, true);
        treeTable.setTreeCellRenderer(new DefaultTreeRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof Component) {
                    return value.getClass().getSimpleName() + " (" + ((Component) value).getName() + ")";
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        tabbedPane.addTab("JXTreeTable", new JScrollPane(treeTable));
        
        comboBox = new JXComboBox();
        comboBox.setName("comboBox");
        comboBox.setRenderer(new DefaultListRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof Component) {
                    return value.getClass().getSimpleName() + " (" + ((Component) value).getName() + ")";
                }
                    
                return StringValues.TO_STRING.getString(value);
            }
        }));
        JPanel panel = new JPanel();
        panel.add(comboBox);
        tabbedPane.addTab("JXComboBox", panel);
        
        add(tabbedPane);
        
        list.setModel(ComponentModels.getListModel(this));
        table.setModel(ComponentModels.getTableModel(this));
        tree.setModel(ComponentModels.getTreeModel(this));
        treeTable.setTreeTableModel(ComponentModels.getTreeTableModel(this));
        comboBox.setModel(ComponentModels.getComboBoxModel(this));
    }
    
    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel(new BorderLayout());
        JPanel control = new JPanel(new GridLayout(2, 2));
        control.add(new JLabel("Highlighter Options:"));
        
        stripingOptions = new JComboBox(getStripingOptionsModel());
        stripingOptions.setRenderer(new DefaultListRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof HighlighterInfo) {
                    return ((HighlighterInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(stripingOptions);
        
        highlighters = new JComboBox(getHighlighterOptionsModel());
        highlighters.setRenderer(new DefaultListRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof HighlighterInfo) {
                    return ((HighlighterInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(highlighters);
        
        predicates = new JComboBox(getPredicateOptionsModel());
        predicates.setRenderer(new DefaultListRenderer(new StringValue() {
            public String getString(Object value) {
                if (value instanceof HighlightPredicateInfo) {
                    return ((HighlightPredicateInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(predicates);
        controller.add(control, BorderLayout.NORTH);

        bind();

        return controller;
	}

    private ComboBoxModel getStripingOptionsModel() {
        List<HighlighterInfo> info = new ArrayList<HighlighterInfo>();
        info.add(new HighlighterInfo("No Striping", null));
        info.add(new HighlighterInfo("Alternating UI-Dependent",  HighlighterFactory.createAlternateStriping()));
        info.add(new HighlighterInfo("Alternating Groups (2) UI-Dependent", HighlighterFactory.createAlternateStriping(2)));
        info.add(new HighlighterInfo("Alternating Groups (3) UI-Dependent", HighlighterFactory.createAlternateStriping(3)));
        
        Color lightBlue = new Color(0xC0D9D9);
        Color gold = new Color(0xDBDB70);
        info.add(new HighlighterInfo("Alternating Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold)));
        info.add(new HighlighterInfo("Alternating Groups (2) Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold, 2)));
        info.add(new HighlighterInfo("Alternating Groups (3) Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold, 3)));
        
        return new ListComboBoxModel<HighlighterInfo>(info);
    }
    
    private ComboBoxModel getHighlighterOptionsModel() {
        List<HighlighterInfo> info = new ArrayList<HighlighterInfo>();
        info.add(new HighlighterInfo("No Additional Highlighter", null));
        info.add(new HighlighterInfo("Green Border", new BorderHighlighter(BorderFactory.createLineBorder(Color.GREEN, 1))));
        info.add(new HighlighterInfo("Blue Border", new BorderHighlighter(BorderFactory.createLineBorder(Color.BLUE, 1))));
        info.add(new HighlighterInfo("Red Text",  new ColorHighlighter(null, Color.RED)));
        info.add(new HighlighterInfo("Purple Text", new ColorHighlighter(null, new Color(0x80, 0x00, 0x80))));
        info.add(new HighlighterInfo("Blended Red Background", new ColorHighlighter(new Color(255, 0, 0, 127), null)));
        info.add(new HighlighterInfo("Blended Green Background", new ColorHighlighter(new Color(0, 180, 0, 80), null)));
//        info.add(new HighlighterInfo("Green Orb Icon",
//                new IconHighlighter(Application.getInstance().getContext()
//                        .getResourceMap(HighlighterDemo.class).getIcon("greenOrb"))));
        Icon greenOrb = StaticUtilities.createImageIcon(this.getClass(), "resources/images/green-orb.png");
        info.add(new HighlighterInfo("Green Orb Icon", new IconHighlighter(greenOrb)));
        info.add(new HighlighterInfo("Aerith Gradient Painter", new PainterHighlighter(new MattePainter(PaintUtils.AERITH, true))));
        info.add(new HighlighterInfo("Star Shape Painter",
                new PainterHighlighter(new ShapePainter(ShapeUtils.generatePolygon(5, 10, 5, true), PaintUtils.NIGHT_GRAY_LIGHT))));
        info.add(new HighlighterInfo("10pt. Bold Dialog Font", new FontHighlighter(new Font("Dialog", Font.BOLD, 10))));
        info.add(new HighlighterInfo("Italic Font", new DerivedFontHighlighter(Font.ITALIC)));
        info.add(new HighlighterInfo("Trailing Alignment", new AlignmentHighlighter(SwingConstants.TRAILING)));
        info.add(new HighlighterInfo("Show As Disabled", new EnabledHighlighter(false)));
        
        return new ListComboBoxModel<HighlighterInfo>(info);
    }
    
    private ComboBoxModel getPredicateOptionsModel() {
        List<HighlightPredicateInfo> info = new ArrayList<HighlightPredicateInfo>();
        info.add(new HighlightPredicateInfo("Always Off", HighlightPredicate.NEVER));
        info.add(new HighlightPredicateInfo("Always On", HighlightPredicate.ALWAYS));
        info.add(new HighlightPredicateInfo("Focused Cell", HighlightPredicate.HAS_FOCUS));
        info.add(new HighlightPredicateInfo("Non-Leaf Node", HighlightPredicate.IS_FOLDER));
        info.add(new HighlightPredicateInfo("Leaf Node", HighlightPredicate.IS_LEAF));
        info.add(new HighlightPredicateInfo("Rollover Row", HighlightPredicate.ROLLOVER_ROW));
        info.add(new HighlightPredicateInfo("Columns 0 and 3", new HighlightPredicate.ColumnHighlightPredicate(0, 3)));
        info.add(new HighlightPredicateInfo("Node Depth Columns 0 and 3", new HighlightPredicate.DepthHighlightPredicate(0, 3)));
        info.add(new HighlightPredicateInfo("JButton Type", new HighlightPredicate.TypeHighlightPredicate(JButton.class)));
        
        return new ListComboBoxModel<HighlightPredicateInfo>(info);
    }
    
    private void bind() {
        
        Binding b = Bindings.createAutoBinding(READ,
                predicates, ELProperty.create("${selectedItem.predicate}"),
                highlighters, ELProperty.create("${selectedItem.highlighter.highlightPredicate}"));
        b.addBindingListener(new BindingAdapter() {
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                binding.refresh();
            }
        });
        b.bind();
        
        ArrayAggregator<Highlighter> activeHighlighters
                = new ArrayAggregator<Highlighter>(Highlighter.class);
        activeHighlighters.addSource(new HighlighterInfo("Tooltip for truncated text",
                new ToolTipHighlighter(new HighlightPredicate.AndHighlightPredicate(
                        new HighlightPredicate() {
                            @Override
                            public boolean isHighlighted(Component renderer,
                                    ComponentAdapter adapter) {
                                return adapter.getComponent() instanceof JTable;
                            }
                        }, HighlightPredicate.IS_TEXT_TRUNCATED))),
                (Property) ELProperty.create("${highlighter}"));
        activeHighlighters.addSource(stripingOptions,
                (Property) ELProperty.create("${selectedItem.highlighter}"));
        activeHighlighters.addSource(highlighters,
                (Property) ELProperty.create("${selectedItem.highlighter}"));
        
        Bindings.createAutoBinding(READ,
                activeHighlighters, BeanProperty.create("value"),
                list, BeanProperty.create("highlighters")).bind();
        
        Bindings.createAutoBinding(READ,
                activeHighlighters, BeanProperty.create("value"),
                table, BeanProperty.create("highlighters")).bind();
        
        Bindings.createAutoBinding(READ,
                activeHighlighters, BeanProperty.create("value"),
                tree, BeanProperty.create("highlighters")).bind();
        
        Bindings.createAutoBinding(READ,
                activeHighlighters, BeanProperty.create("value"),
                treeTable, BeanProperty.create("highlighters")).bind();
        
        Bindings.createAutoBinding(READ,
                activeHighlighters, BeanProperty.create("value"),
                comboBox, BeanProperty.create("highlighters")).bind();
    }

}