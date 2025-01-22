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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.TreeCellRenderer;

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
import org.jdesktop.swingx.decorator.AbstractHighlighter;
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
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ShapeUtils;
import org.jdesktop.swingxset.util.ComponentModels;

import swingset.AbstractDemo;

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
/*
HighlightPredicate   | list   | table | tree | treeTable | comboBox
               NEVER : ok     : ok    : ok   : ER1       : ok
              ALWAYS : ok     : ok    : ok   : ok        : ok
           HAS_FOCUS : ok     : ok    : ok   : ER2       : ER3
Non-Leaf,  IS_FOLDER : nA 1   : nA    : ok   : ???       : nA
Leaf Node,   IS_LEAF : ALWAYS : alle  : ok   : ???       : alle
        ROLLOVER_ROW : OKnow  : ok    : OKnow: ok ???    : ???
"Columns 0 and 3"    : alle   : ok    : alle : ok        : alle
"Node Depth Columns.": nix    : nix   : ok   :
"JButton Type"       : ok     : ok    : ok   :

1: List row is always leaf
ER1 : lässt sich nicht ausschalten für hierarchical Column
ER2 : ganze Spalte ist highlighted
ER3 : kein highlight wenn ausgeklappt
??? : ERROR 

 */

public class HighlighterDemo extends AbstractDemo {

	private static final long serialVersionUID = -7205284335967801438L;
    private static final Logger LOG = Logger.getLogger(HighlighterDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates Highlighters, a lightweight, reusable, visual decorator.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new HighlighterDemo(controller);
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

	private JXList<Component> list;
    private JXTable table;
    private JXTree tree;
    private JXTreeTable treeTable;
    private JXComboBox<Component> comboBox;
    
    // Controller:
    private JXComboBox<HighlighterInfo> stripingOptions;
    private JXComboBox<HighlighterInfo> highlighters;
    private JXComboBox<HighlightPredicateInfo> predicates;
    
    /**
     * HighlighterDemo Constructor
     * 
     * @param frame controller Frame
     */
	public HighlighterDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        list = new JXList<Component>();
        list.setName("list");
        
		StringValue sv = (Object value) -> {
            if (value instanceof Component c) {
                return value.getClass().getSimpleName() + (c.getName()==null ? " with no name" : " (name=" + c.getName() + ")");
            }               
            return StringValues.TO_STRING.getString(value);
		};
        list.setCellRenderer(new DefaultListRenderer<Component>(sv));
        tabbedPane.addTab("JXList", new JScrollPane(list));
        
        table = new JXTable();
        table.setName("table");
        table.setShowGrid(true, true);
        table.setColumnControlVisible(true);
        tabbedPane.addTab("JXTable", new JScrollPane(table));
        
        tree = new JXTree() {
			private static final long serialVersionUID = 1L;
            public TreeCellRenderer getCellRenderer() {
				StringValue sv = (Object value) -> {
					if (value instanceof Component component) {
						String simpleName = component.getClass().getSimpleName();
						if (simpleName.length() == 0) {
							// anonymous class
							simpleName = component.getClass().getSuperclass().getSimpleName();
						}
						return simpleName + "(" + component.getName() + ")";
					}
					return StringValues.TO_STRING.getString(value);
				};
                return new JXTree.DelegatingRenderer(sv);
            }
        };
        tree.setName("tree");
        tree.setCellRenderer(tree.getCellRenderer());
        tabbedPane.addTab("JXTree", new JScrollPane(tree));
        
		@SuppressWarnings("serial")
		JXTreeTable.TreeTableCellRenderer renderer = new JXTreeTable.TreeTableCellRenderer(ComponentModels.getTreeTableModel(this)) {
		    @Override
			public TreeCellRenderer getCellRenderer() {
				StringValue sv = (Object value) -> {
					if (value instanceof Component component) {
						String simpleName = component.getClass().getSimpleName();
						if (simpleName.length() == 0) {
							// anonymous class
							simpleName = component.getClass().getSuperclass().getSimpleName();
						}
						return simpleName + (component.getName()==null ? "" : "(" + component.getName() + ")");
					}
					return StringValues.TO_STRING.getString(value);
				};
				return new JXTree.DelegatingRenderer(sv);
			}
			
		};
		treeTable = new ComponentTreeTable(renderer);
        treeTable.setName("treeTable");
        treeTable.setColumnControlVisible(true);
        treeTable.setShowGrid(true, false);
        tabbedPane.addTab("JXTreeTable", new JScrollPane(treeTable));
        
        comboBox = new JXComboBox<Component>();
        comboBox.setName("comboBox");
        JPanel panel = new JPanel();
        panel.add(comboBox);
        tabbedPane.addTab("JXComboBox", panel);
        
        add(tabbedPane);
        LOG.config("tabbedPane with "+tabbedPane.getTabCount()+" tabs.");
        
        ListModel<Component> listModel = ComponentModels.getListModel(this);
        list.setModel(listModel);
        
        table.setModel(ComponentModels.getTableModel(this));
        // now define the TableRenderer per column:
        StringValue simpleName = (Object value) -> {
            if (value instanceof Component) {
                return value.getClass().getSimpleName();
            }              
            return StringValues.TO_STRING.getString(value);
        };
        table.getColumn(0).setCellRenderer(new DefaultTableRenderer(simpleName));
        
        // Column 1 is Name ==> no converter
        
        StringValue pointConverter = (Object value) -> {
            if(value instanceof Point p) {
            	return "[x=" + p.x + ",y=" + p.y + "]";
            } 
            return "not showing";
        };
        table.getColumn(2).setCellRenderer(new DefaultTableRenderer(pointConverter));
        
        tree.setModel(ComponentModels.getTreeModel(this));
        tree.expandAll();
        
        treeTable.expandAll();     
        treeTable.getColumn(2).setCellRenderer(new DefaultTableRenderer(pointConverter));
/*
	- No enclosing instance of type JXTree is accessible. 
	Must qualify the allocation with an enclosing instance of type JXTree 
	(e.g. x.new A() where x is an instance of JXTree).
	
		treeTable.setTreeCellRenderer(new JXTree.DelegatingRenderer(new StringValue() {
			private static final long serialVersionUID = 1L;
			public String getString(Object value) {
				if (value instanceof Component && value.getClass() != Component.class) {
					return value.getClass().getSimpleName();
				}
				return StringValues.TO_STRING.getString(value);
			}
		}));
 */
      
        // ComponentModels.getComboBoxModel(Component root) liefert ComboBoxModel<Component>:
        ComboBoxModel<Component> comboBoxModel = ComponentModels.getComboBoxModel(this);
        comboBox.setModel(comboBoxModel);
        
        StringValue svConverter = (Object value) -> {
            if (value instanceof Component) {
                return value.getClass().getSimpleName() + " (" + ((Component) value).getName() + ")";
            }              
            return StringValues.TO_STRING.getString(value);
        };
        // DefaultListRenderer<E> extends AbstractRenderer implements ListCellRenderer<E>
        ListCellRenderer<? super Component> comboBoxRenderer = new DefaultListRenderer<Component>(svConverter);
        comboBox.setRenderer(comboBoxRenderer);
    }

	@SuppressWarnings("serial")
	class ComponentTreeTable extends JXTreeTable /*implements TableCellRenderer*/ {
		ComponentTreeTable(JXTreeTable.TreeTableCellRenderer renderer) {
			super(renderer);
			assert ((JXTreeTable.InternalTreeTableModelAdapter) getModel()).getTree() == renderer;
		}
	    public void setTreeCellRenderer(TreeCellRenderer cellRenderer) {
	    	super.setTreeCellRenderer(cellRenderer);
	    }
	}

    @Override
	public JXPanel getControlPane() {
        JXPanel controller = new JXPanel(new BorderLayout());
        JPanel control = new JPanel(new GridLayout(2, 2));
        control.add(new JLabel("Highlighter Options:"));
        
        stripingOptions = new JXComboBox<HighlighterInfo>(getStripingOptionsModel());
        stripingOptions.setRenderer(new DefaultListRenderer<HighlighterInfo>(new StringValue() {
			private static final long serialVersionUID = 1L;
            public String getString(Object value) {
                if (value instanceof HighlighterInfo) {
                    return ((HighlighterInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(stripingOptions);
        
        highlighters = new JXComboBox<HighlighterInfo>(getHighlighterOptionsModel());
        highlighters.setRenderer(new DefaultListRenderer<HighlighterInfo>(new StringValue() {
			private static final long serialVersionUID = 1L;
            public String getString(Object value) {
                if (value instanceof HighlighterInfo) {
                    return ((HighlighterInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(highlighters);
        
        predicates = new JXComboBox<HighlightPredicateInfo>(getPredicateOptionsModel());
        predicates.setRenderer(new DefaultListRenderer<HighlightPredicateInfo>(new StringValue() {
			private static final long serialVersionUID = 1L;
            public String getString(Object value) {
                if (value instanceof HighlightPredicateInfo) {
                    return ((HighlightPredicateInfo) value).getDescription();
                }
                
                return StringValues.TO_STRING.getString(value);
            }
        }));
        control.add(predicates);
        controller.add(control, BorderLayout.NORTH);

        predicates.addActionListener( ae -> {
        	HighlightPredicateInfo hpi = (HighlightPredicateInfo)predicates.getSelectedItem();
        	HighlighterInfo hi = (HighlighterInfo)highlighters.getSelectedItem();
        	
        	Highlighter highlighter = hi.getHighlighter();
        	if(highlighter==HighlighterInfo.EMPTY) {
        		LOG.warning(hi.getDescription()+" with predicate "+hpi.getDescription() + " ignored.");
        	} else if(highlighter instanceof AbstractHighlighter ah) {
            	LOG.info(hi.getDescription()+" Predicate:"+hpi.getDescription());
            	ah.setHighlightPredicate(hpi.getPredicate());
            	if(hpi.getPredicate()==HighlightPredicate.ROLLOVER_ROW) {
            		
            		// enable Rollover and add highlighter
            		list.setRolloverEnabled(true);
            		list.addHighlighter(ah);
            		
            		tree.setRolloverEnabled(true);
            		tree.addHighlighter(ah);
            		// for testing: add RolloverIconHighlighter
//                	RolloverIconHighlighter roih = new RolloverIconHighlighter(HighlightPredicate.ROLLOVER_ROW, null);
//                	tree.addHighlighter(roih);
//                	treeTable.addHighlighter(roih);
                	
            		// Rollover prop is true per default for JXTable , JXTreeTable
            		LOG.fine(" treeTable.isRolloverEnabled() ===== "+treeTable.isRolloverEnabled());
            		
            		// no setRolloverEnabled for comboBox
            	}
        	} else {
            	LOG.warning(hi.getDescription()+" Highlighter:"+highlighter);          	
       	}
        });
        bind();

        return controller;
	}


    private ComboBoxModel<HighlighterInfo> getStripingOptionsModel() {
        List<HighlighterInfo> info = new ArrayList<HighlighterInfo>();
        info.add(new HighlighterInfo("No Striping", null));
        info.add(new HighlighterInfo("Alternating UI-Dependent",  HighlighterFactory.createAlternateStriping()));
        info.add(new HighlighterInfo("Alternating Groups (2) UI-Dependent", HighlighterFactory.createAlternateStriping(2)));
        info.add(new HighlighterInfo("Alternating Groups (3) UI-Dependent", HighlighterFactory.createAlternateStriping(3)));
        
        Color lightBlue = new Color(0xC0D9D9); // Haint Blue https://icolorpalette.com/color/C0D9D9
        Color gold = new Color(0xDBDB70); // Moist Gold
        info.add(new HighlighterInfo("Alternating Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold)));
        info.add(new HighlighterInfo("Alternating Groups (2) Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold, 2)));
        info.add(new HighlighterInfo("Alternating Groups (3) Blue-Gold", HighlighterFactory.createAlternateStriping(lightBlue, gold, 3)));
        
        return new ListComboBoxModel<HighlighterInfo>(info);
    }
    
    private ComboBoxModel<HighlighterInfo> getHighlighterOptionsModel() {
        List<HighlighterInfo> info = new ArrayList<HighlighterInfo>();
        info.add(new HighlighterInfo("No Additional Highlighter", null));
        info.add(new HighlighterInfo("Green Border", new BorderHighlighter(BorderFactory.createLineBorder(Color.GREEN, 1))));
        info.add(new HighlighterInfo("Blue Border", new BorderHighlighter(BorderFactory.createLineBorder(Color.BLUE, 1))));
        info.add(new HighlighterInfo("Red Text",  new ColorHighlighter(null, Color.RED)));
        info.add(new HighlighterInfo("Purple Text", new ColorHighlighter(null, new Color(0x80, 0x00, 0x80))));
        info.add(new HighlighterInfo("Blended Red Background", new ColorHighlighter(new Color(255, 0, 0, 127), null)));
        info.add(new HighlighterInfo("Blended Green Background", new ColorHighlighter(new Color(0, 180, 0, 80), null)));

        Icon greenOrb = getResourceAsIcon(this.getClass(), "resources/images/green-orb.png");
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
    
    private ComboBoxModel<HighlightPredicateInfo> getPredicateOptionsModel() {
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
        Binding<JComboBox<HighlightPredicateInfo>
        	, Property<JComboBox<HighlighterInfo>, Highlighter>
        	, JComboBox<HighlighterInfo>
        	, Property<JComboBox<HighlighterInfo>, Highlighter>> b = Bindings.createAutoBinding(READ,
                predicates, ELProperty.create("${selectedItem.predicate}"),
                highlighters, ELProperty.create("${selectedItem.highlighter.highlightPredicate}"));
        b.addBindingListener(new BindingAdapter() {
        	@SuppressWarnings("rawtypes")
			@Override // BindingAdapter.targetChanged(Binding binding, PropertyStateEvent event)
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                binding.refresh();
            }
        });
        b.bind();
        
        // converts source properties into an array
        ArrayAggregator<Highlighter> activeHighlighters = new ArrayAggregator<Highlighter>(Highlighter.class);
        Property<HighlighterInfo, Highlighter> propHighlighter = ELProperty.create("${highlighter}");
//         public <S> void addSource(S object, Property<S, SV> property) {
        activeHighlighters.addSource(new HighlighterInfo("Tooltip for truncated text",
                new ToolTipHighlighter(new HighlightPredicate.AndHighlightPredicate(
                        new HighlightPredicate() {
                            @Override // implements org.jdesktop.swingx.decorator.HighlightPredicate.isHighlighted
                            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                                return adapter.getComponent() instanceof JTable;
                            }
                        }, HighlightPredicate.IS_TEXT_TRUNCATED))),
        		propHighlighter);
        
        // type of stripingOptions, highlighters is JComboBox<HighlighterInfo>:
        Property<JComboBox<HighlighterInfo>, Highlighter> selectedHighlighter = ELProperty.create("${selectedItem.highlighter}");
        activeHighlighters.addSource(stripingOptions, selectedHighlighter);
        activeHighlighters.addSource(highlighters, selectedHighlighter);
        
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