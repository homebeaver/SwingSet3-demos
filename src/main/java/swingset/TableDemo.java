/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Vector;
import java.util.logging.Logger;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXFrame.StartPosition;

/**
 * Table demo
 *
 * @author Philip Milne
 * @author Steve Wilson
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class TableDemo extends AbstractDemo {
	
	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JTable.gif";

	private static final long serialVersionUID = -3563812140907870880L;
	private static final Logger LOG = Logger.getLogger(TableDemo.class.getName());
    private static final int INITIAL_ROWHEIGHT = 33;
    private static final String IMG_PATH = "ImageClub/food/"; // prefix dir

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new TableDemo(controller);
			JXFrame frame = new JXFrame("demo", exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }

    JScrollPane scrollpane;
    JTable      tableView;

    /**
     * TableDemo Constructor
     * @param frame controller Frame
     */
    public TableDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));
    	
        LOG.config("Create the table.");
    	tableView = createTable();
        scrollpane = new JScrollPane(tableView);
        super.add(scrollpane, BorderLayout.CENTER);
        
        super.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ctrl P"), "print");

		super.getActionMap().put("print", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				printTable();
			}
		});
    }

    private JTable createTable() {

        final String[] names = {
          getBundleString("first_name"),
          getBundleString("last_name"),
          getBundleString("favorite_color"),
          getBundleString("favorite_movie"),
          getBundleString("favorite_number"),
          getBundleString("favorite_food")
        };

        ImageIcon apple        = StaticUtilities.createImageIcon(IMG_PATH+"apple.jpg");
        ImageIcon asparagus    = StaticUtilities.createImageIcon(IMG_PATH+"asparagus.jpg");
        ImageIcon banana       = StaticUtilities.createImageIcon(IMG_PATH+"banana.jpg");
        ImageIcon broccoli     = StaticUtilities.createImageIcon(IMG_PATH+"broccoli.jpg");
        ImageIcon cantaloupe   = StaticUtilities.createImageIcon(IMG_PATH+"cantaloupe.jpg");
        ImageIcon carrot       = StaticUtilities.createImageIcon(IMG_PATH+"carrot.jpg");
        ImageIcon corn         = StaticUtilities.createImageIcon(IMG_PATH+"corn.jpg");
// intentionally added : MissingResource: Couldn't find value for: TableDemo.donut and image
        ImageIcon donut        = StaticUtilities.createImageIcon(IMG_PATH+"donut.jpg");
        ImageIcon grapes       = StaticUtilities.createImageIcon(IMG_PATH+"grapes.jpg");
        ImageIcon grapefruit   = StaticUtilities.createImageIcon(IMG_PATH+"grapefruit.jpg");
        ImageIcon kiwi         = StaticUtilities.createImageIcon(IMG_PATH+"kiwi.jpg");
        ImageIcon onion        = StaticUtilities.createImageIcon(IMG_PATH+"onion.jpg");
        ImageIcon pear         = StaticUtilities.createImageIcon(IMG_PATH+"pear.jpg");
        ImageIcon peach        = StaticUtilities.createImageIcon(IMG_PATH+"peach.jpg");
        ImageIcon pepper       = StaticUtilities.createImageIcon(IMG_PATH+"pepper.jpg");
        ImageIcon pickle       = StaticUtilities.createImageIcon(IMG_PATH+"pickle.jpg");
        ImageIcon pineapple    = StaticUtilities.createImageIcon(IMG_PATH+"pineapple.jpg");
        ImageIcon raspberry    = StaticUtilities.createImageIcon(IMG_PATH+"raspberry.jpg");
        ImageIcon sparegrass   = StaticUtilities.createImageIcon(IMG_PATH+"asparagus.jpg");
        ImageIcon strawberry   = StaticUtilities.createImageIcon(IMG_PATH+"strawberry.jpg");
        ImageIcon tomato       = StaticUtilities.createImageIcon(IMG_PATH+"tomato.jpg");
        ImageIcon watermelon   = StaticUtilities.createImageIcon(IMG_PATH+"watermelon.jpg");

        NamedColor aqua        = new NamedColor(new Color(127, 255, 212), getBundleString("aqua"));
        NamedColor beige       = new NamedColor(new Color(245, 245, 220), getBundleString("beige"));
        NamedColor black       = new NamedColor(Color.black, getBundleString("black"));
        NamedColor blue        = new NamedColor(new Color(0, 0, 222), getBundleString("blue"));
        NamedColor eblue       = new NamedColor(Color.blue, getBundleString("eblue"));
        NamedColor jfcblue     = new NamedColor(new Color(204, 204, 255), getBundleString("jfcblue"));
        NamedColor jfcblue2    = new NamedColor(new Color(153, 153, 204), getBundleString("jfcblue2"));
        NamedColor cybergreen  = new NamedColor(Color.green.darker().brighter(), getBundleString("cybergreen"));
        NamedColor darkgreen   = new NamedColor(new Color(0, 100, 75), getBundleString("darkgreen"));
        NamedColor forestgreen = new NamedColor(Color.green.darker(), getBundleString("forestgreen"));
        NamedColor gray        = new NamedColor(Color.gray, getBundleString("gray"));
        NamedColor green       = new NamedColor(Color.green, getBundleString("green"));
        NamedColor orange      = new NamedColor(new Color(255, 165, 0), getBundleString("orange"));
// intentionally added : MissingResource: Couldn't find value for: TableDemo.pink
        NamedColor pink        = new NamedColor(Color.PINK, getBundleString("pink"));
        NamedColor purple      = new NamedColor(new Color(160, 32, 240),  getBundleString("purple"));
        NamedColor red         = new NamedColor(Color.red, getBundleString("red"));
        NamedColor rustred     = new NamedColor(Color.red.darker(), getBundleString("rustred"));
        NamedColor sunpurple   = new NamedColor(new Color(100, 100, 255), getBundleString("sunpurple"));
        NamedColor suspectpink = new NamedColor(new Color(255, 105, 180), getBundleString("suspectpink"));
        NamedColor turquoise   = new NamedColor(new Color(0, 255, 255), getBundleString("turquoise"));
        NamedColor violet      = new NamedColor(new Color(238, 130, 238), getBundleString("violet"));
        NamedColor yellow      = new NamedColor(Color.yellow, getBundleString("yellow"));

        // Create the dummy data (a few rows of names)
        //  "First Name", "Last Name", "Favorite Color", Movie from props, "image", "No.", "Vegetarian" 
        final Object[][] data = {
          {"Mike", "Albers",      green,       getBundleString("brazil"), Double.valueOf(44.0), strawberry},
          {"Mark", "Andrews",     blue,        getBundleString("curse"), Double.valueOf(3), grapes},
          {"Brian", "Beck",       black,       getBundleString("bluesbros"), Double.valueOf(2.7182818285), raspberry},
          {"Lara", "Bunni",       red,         getBundleString("airplane"), Double.valueOf(15), strawberry},
          {"Roger", "Brinkley",   blue,        getBundleString("man"), Double.valueOf(13), peach},
          {"Brent", "Christian",  black,       getBundleString("bladerunner"), Double.valueOf(23), broccoli},
          {"Mark", "Davidson",    darkgreen,   getBundleString("brazil"), Double.valueOf(27), asparagus},
          {"Jeff", "Dinkins",     blue,        getBundleString("ladyvanishes"), Double.valueOf(8), kiwi},
          {"Ewan", "Dinkins",     yellow,      getBundleString("bugs"), Double.valueOf(2), strawberry},
          {"Amy", "Fowler",       violet,      getBundleString("reservoir"), Double.valueOf(3), raspberry},
          {"Hania", "Gajewska",   purple,      getBundleString("jules"), Double.valueOf(5), raspberry},
          {"David", "Geary",      blue,        getBundleString("pulpfiction"), Double.valueOf(3), watermelon},
// intentionally aktivated : MissingResource: Couldn't find value for: TableDemo.tennis, TableDemo.donut, ...
        {"James", "Gosling",    pink,        getBundleString("tennis"), Double.valueOf(21), donut},
          {"Eric", "Hawkes",      blue,        getBundleString("bladerunner"), Double.valueOf(.693), pickle},
          {"Shannon", "Hickey",   green,       getBundleString("shawshank"), Double.valueOf(2), grapes},
          {"Earl", "Johnson",     green,       getBundleString("pulpfiction"), Double.valueOf(8), carrot},
          {"Robi", "Khan",        green,       getBundleString("goodfellas"), Double.valueOf(89), apple},
          {"Robert", "Kim",       blue,        getBundleString("mohicans"), Double.valueOf(655321), strawberry},
          {"Janet", "Koenig",     turquoise,   getBundleString("lonestar"), Double.valueOf(7), peach},
          {"Jeff", "Kesselman",   blue,        getBundleString("stuntman"), Double.valueOf(17), pineapple},
          {"Onno", "Kluyt",       orange,      getBundleString("oncewest"), Double.valueOf(8), broccoli},
          {"Peter", "Korn",       sunpurple,   getBundleString("musicman"), Double.valueOf(12), sparegrass},
          {"Rick", "Levenson",    black,       getBundleString("harold"), Double.valueOf(1327), raspberry},
          {"Brian", "Lichtenwalter", jfcblue,  getBundleString("fifthelement"), Double.valueOf(22), pear},
          {"Malini", "Minasandram", beige,     getBundleString("joyluck"), Double.valueOf(9), corn},
          {"Michael", "Martak",   green,       getBundleString("city"), Double.valueOf(3), strawberry},
          {"David", "Mendenhall", forestgreen, getBundleString("schindlerslist"), Double.valueOf(7), peach},
          {"Phil", "Milne",       suspectpink, getBundleString("withnail"), Double.valueOf(3), banana},
          {"Lynn", "Monsanto",    cybergreen,  getBundleString("dasboot"), Double.valueOf(52), peach},
          {"Hans", "Muller",      rustred,     getBundleString("eraserhead"), Double.valueOf(0), pineapple},
          {"Joshua", "Outwater",  blue,        getBundleString("labyrinth"), Double.valueOf(3), pineapple},
          {"Tim", "Prinzing",     blue,        getBundleString("firstsight"), Double.valueOf(69), pepper},
          {"Raj", "Premkumar",    jfcblue2,    getBundleString("none"), Double.valueOf(7), broccoli},
          {"Howard", "Rosen",     green,       getBundleString("defending"), Double.valueOf(7), strawberry},
          {"Ray", "Ryan",         black,       getBundleString("buckaroo"),
           Double.valueOf(3.141592653589793238462643383279502884197169399375105820974944), banana},
          {"Georges", "Saab",     aqua,        getBundleString("bicycle"), Double.valueOf(290), cantaloupe},
          {"Tom", "Santos",       blue,        getBundleString("spinaltap"), Double.valueOf(241), pepper},
          {"Rich", "Schiavi",     blue,        getBundleString("repoman"), Double.valueOf(0xFF), pepper},
          {"Nancy", "Schorr",     green,       getBundleString("fifthelement"), Double.valueOf(47), watermelon},
          {"Keith", "Sprochi",    darkgreen,   getBundleString("2001"), Double.valueOf(13), watermelon},
          {"Matt", "Tucker",      eblue,       getBundleString("starwars"), Double.valueOf(2), broccoli},
          {"Dmitri", "Trembovetski", red,      getBundleString("aliens"), Double.valueOf(222), tomato},
          {"Scott", "Violet",     violet,      getBundleString("raiders"), Double.valueOf(-97), banana},
          {"Kathy", "Walrath",    darkgreen,   getBundleString("thinman"), Double.valueOf(8), pear},
          {"Nathan", "Walrath",   black,       getBundleString("chusingura"), Double.valueOf(3), grapefruit},
          {"Steve", "Wilson",     green,       getBundleString("raiders"), Double.valueOf(7), onion},
          {"Kathleen", "Zelony",  gray,        getBundleString("dog"), Double.valueOf(13), grapes}
        };

        // Create a model of the data.
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { return names.length; }
            public int getRowCount() { return data.length;}
            public Object getValueAt(int row, int col) {return data[row][col];}
            public String getColumnName(int column) {return names[column];}
            public Class<?> getColumnClass(int c) {return getValueAt(0, c).getClass();}
            public boolean isCellEditable(int row, int col) {return col != 5;}
            public void setValueAt(Object aValue, int row, int column) { data[row][column] = aValue; }
         };


        // Create the table
        tableView = new JTable(dataModel);
        TableRowSorter sorter = new TableRowSorter(dataModel);
        tableView.setRowSorter(sorter);

        // Show colors by rendering them in their own color.
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            public void setValue(Object value) {
                if (value instanceof NamedColor) {
                    NamedColor c = (NamedColor) value;
                    setBackground(c);
                    setForeground(c.getTextColor());
                    setText(c.toString());
                } else {
                    super.setValue(value);
                }
            }
        };

        // Create a combo box to show that you can use one in a table.
        JComboBox<NamedColor> comboBox = new JComboBox<NamedColor>();
        comboBox.addItem(aqua);
        comboBox.addItem(beige);
        comboBox.addItem(black);
        comboBox.addItem(blue);
        comboBox.addItem(eblue);
        comboBox.addItem(jfcblue);
        comboBox.addItem(jfcblue2);
        comboBox.addItem(cybergreen);
        comboBox.addItem(darkgreen);
        comboBox.addItem(forestgreen);
        comboBox.addItem(gray);
        comboBox.addItem(green);
        comboBox.addItem(orange);
        comboBox.addItem(purple);
        comboBox.addItem(red);
        comboBox.addItem(rustred);
        comboBox.addItem(sunpurple);
        comboBox.addItem(suspectpink);
        comboBox.addItem(turquoise);
        comboBox.addItem(violet);
        comboBox.addItem(yellow);

        TableColumn colorColumn = tableView.getColumn(getBundleString("favorite_color"));
        // Use the combo box as the editor in the "Favorite Color" column.
        colorColumn.setCellEditor(new DefaultCellEditor(comboBox));

        colorRenderer.setHorizontalAlignment(JLabel.CENTER);
        colorColumn.setCellRenderer(colorRenderer);

        tableView.setRowHeight(INITIAL_ROWHEIGHT);
        return tableView;
    }

    // Controller:
    Dimension   origin = new Dimension(0, 0);

    JCheckBox   isColumnReorderingAllowedCheckBox;
    JCheckBox   showHorizontalLinesCheckBox;
    JCheckBox   showVerticalLinesCheckBox;

    JCheckBox   isColumnSelectionAllowedCheckBox;
    JCheckBox   isRowSelectionAllowedCheckBox;

    JLabel      interCellSpacingLabel;
    JLabel      rowHeightLabel;

    JSlider     interCellSpacingSlider;
    JSlider     rowHeightSlider;

    JComboBox<String> selectionModeComboBox = null;
    JComboBox<String> resizeModeComboBox = null;

    JLabel      headerLabel;
    JLabel      footerLabel;

    JTextField  headerTextField;
    JTextField  footerTextField;

    JCheckBox   fitWidth;
    JButton     printButton;

    JXPanel      controlPanel;

    @Override
	public JXPanel getControlPane() {
        controlPanel = new JXPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        JPanel cbPanel = new JPanel(new GridLayout(3, 2));
        JPanel labelPanel = new JPanel(new GridLayout(2, 1)) {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        JPanel sliderPanel = new JPanel(new GridLayout(2, 1)) {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        JPanel comboPanel = new JPanel(new GridLayout(2, 1));
        JPanel printPanel = new JPanel(new ColumnLayout());

        Vector relatedComponents = new Vector();


        // check box panel
        isColumnReorderingAllowedCheckBox = new JCheckBox(getBundleString("reordering_allowed"), true);
        isColumnReorderingAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.getTableHeader().setReorderingAllowed(flag);
                tableView.repaint();
            }
        });

        showHorizontalLinesCheckBox = new JCheckBox(getBundleString("horz_lines"), true);
        showHorizontalLinesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setShowHorizontalLines(flag); ;
                tableView.repaint();
            }
        });

        showVerticalLinesCheckBox = new JCheckBox(getBundleString("vert_lines"), true);
        showVerticalLinesCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setShowVerticalLines(flag); ;
                tableView.repaint();
            }
        });

        LOG.config("Show that showHorizontal/Vertical controls are related");
        relatedComponents.removeAllElements();
        relatedComponents.add(showHorizontalLinesCheckBox); 
        relatedComponents.add(showVerticalLinesCheckBox);
        buildAccessibleGroup(relatedComponents);

        isRowSelectionAllowedCheckBox = new JCheckBox(getBundleString("row_selection"), true);
        isRowSelectionAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setRowSelectionAllowed(flag); ;
                tableView.repaint();
            }
        });

        isColumnSelectionAllowedCheckBox = new JCheckBox(getBundleString("column_selection"), false);
        isColumnSelectionAllowedCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = ((JCheckBox)e.getSource()).isSelected();
                tableView.setColumnSelectionAllowed(flag); ;
                tableView.repaint();
            }
        });

        // Show that row/column selections are related
        relatedComponents.removeAllElements();
        relatedComponents.add(isColumnSelectionAllowedCheckBox);
        relatedComponents.add(isRowSelectionAllowedCheckBox);
        buildAccessibleGroup(relatedComponents);

        cbPanel.add(isColumnReorderingAllowedCheckBox);
        cbPanel.add(isRowSelectionAllowedCheckBox);
        cbPanel.add(showHorizontalLinesCheckBox);
        cbPanel.add(isColumnSelectionAllowedCheckBox);
        cbPanel.add(showVerticalLinesCheckBox);


        // label panel
        interCellSpacingLabel = new JLabel(getBundleString("intercell_spacing_colon"));
        labelPanel.add(interCellSpacingLabel);

        rowHeightLabel = new JLabel(getBundleString("row_height_colon"));
        labelPanel.add(rowHeightLabel);


        // slider panel
        interCellSpacingSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 1);
        interCellSpacingSlider.getAccessibleContext().setAccessibleName(getBundleString("intercell_spacing"));
        interCellSpacingLabel.setLabelFor(interCellSpacingSlider);
        sliderPanel.add(interCellSpacingSlider);
        interCellSpacingSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int spacing = ((JSlider)e.getSource()).getValue();
                tableView.setIntercellSpacing(new Dimension(spacing, spacing));
                tableView.repaint();
            }
        });

        rowHeightSlider = new JSlider(JSlider.HORIZONTAL, 5, 100, INITIAL_ROWHEIGHT);
        rowHeightSlider.getAccessibleContext().setAccessibleName(getBundleString("row_height"));
        rowHeightLabel.setLabelFor(rowHeightSlider);
        sliderPanel.add(rowHeightSlider);
        rowHeightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int height = ((JSlider)e.getSource()).getValue();
                tableView.setRowHeight(height);
                tableView.repaint();
            }
        });

        // Show that spacing controls are related
        relatedComponents.removeAllElements();
        relatedComponents.add(interCellSpacingSlider);
        relatedComponents.add(rowHeightSlider);
        buildAccessibleGroup(relatedComponents);

        // ComboBox for selection modes.
        JPanel selectMode = new JPanel();
        selectMode.setLayout(new BoxLayout(selectMode, BoxLayout.X_AXIS));
        selectMode.setBorder(new TitledBorder(getBundleString("selection_mode")));


        selectionModeComboBox = new JComboBox<String>() {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        selectionModeComboBox.addItem(getBundleString("single"));
        selectionModeComboBox.addItem(getBundleString("one_range"));
        selectionModeComboBox.addItem(getBundleString("multiple_ranges"));
        selectionModeComboBox.setSelectedIndex(tableView.getSelectionModel().getSelectionMode());
        selectionModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox source = (JComboBox)e.getSource();
                tableView.setSelectionMode(source.getSelectedIndex());
            }
        });

        selectMode.add(Box.createHorizontalStrut(2));
        selectMode.add(selectionModeComboBox);
        selectMode.add(Box.createHorizontalGlue());
        comboPanel.add(selectMode);

        // Combo box for table resize mode.
        JPanel resizeMode = new JPanel();
        resizeMode.setLayout(new BoxLayout(resizeMode, BoxLayout.X_AXIS));
        resizeMode.setBorder(new TitledBorder(getBundleString("autoresize_mode")));


        resizeModeComboBox = new JComboBox<String>() {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        resizeModeComboBox.addItem(getBundleString("off"));
        resizeModeComboBox.addItem(getBundleString("column_boundaries"));
        resizeModeComboBox.addItem(getBundleString("subsequent_columns"));
        resizeModeComboBox.addItem(getBundleString("last_column"));
        resizeModeComboBox.addItem(getBundleString("all_columns"));
        resizeModeComboBox.setSelectedIndex(tableView.getAutoResizeMode());
        resizeModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JComboBox source = (JComboBox)e.getSource();
                tableView.setAutoResizeMode(source.getSelectedIndex());
            }
        });

        resizeMode.add(Box.createHorizontalStrut(2));
        resizeMode.add(resizeModeComboBox);
        resizeMode.add(Box.createHorizontalGlue());
        comboPanel.add(resizeMode);

        // print panel
        printPanel.setBorder(new TitledBorder(getBundleString("printing")));
        headerLabel = new JLabel(getBundleString("header"));
        footerLabel = new JLabel(getBundleString("footer"));
        headerTextField = new JTextField(getBundleString("headerText"), 15);
        footerTextField = new JTextField(getBundleString("footerText"), 15);
        fitWidth = new JCheckBox(getBundleString("fitWidth"), true);
        printButton = new JButton(getBundleString("print"));
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printTable();
            }
        });

        printPanel.add(headerLabel);
        printPanel.add(headerTextField);
        printPanel.add(footerLabel);
        printPanel.add(footerTextField);

        JPanel buttons = new JPanel();
        buttons.add(fitWidth);
        buttons.add(printButton);

        printPanel.add(buttons);

        // Show that printing controls are related
        relatedComponents.removeAllElements();
        relatedComponents.add(headerTextField);
        relatedComponents.add(footerTextField);
        relatedComponents.add(printButton);
        buildAccessibleGroup(relatedComponents);

        // wrap up the panels and add them
        JPanel sliderWrapper = new JPanel();
        sliderWrapper.setLayout(new BoxLayout(sliderWrapper, BoxLayout.X_AXIS));
        sliderWrapper.add(labelPanel);
        sliderWrapper.add(sliderPanel);
        sliderWrapper.add(Box.createHorizontalGlue());
        sliderWrapper.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        JPanel leftWrapper = new JPanel();
        leftWrapper.setLayout(new BoxLayout(leftWrapper, BoxLayout.Y_AXIS));
        leftWrapper.add(cbPanel);
        leftWrapper.add(sliderWrapper);

        // add everything
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        controlPanel.add(leftWrapper);
        controlPanel.add(comboPanel);
        controlPanel.add(printPanel);

        setTableControllers(); // Set accessibility information
        
        return controlPanel;
    }

    /**
     * Sets the Accessibility MEMBER_OF property to denote that
     * these components work together as a group. Each object
     * is set to be a MEMBER_OF an array that contains all of
     * the objects in the group, including itself.
     *
     * @param components The list of objects that are related
     */
    void buildAccessibleGroup(Vector components) {

        AccessibleContext context = null;
        int numComponents = components.size();
        Object[] group = components.toArray();
        Object object = null;
        for (int i = 0; i < numComponents; ++i) {
            object = components.elementAt(i);
            if (object instanceof Accessible) {
                context = ((Accessible)components.elementAt(i)).getAccessibleContext();
                context.getAccessibleRelationSet().add(
                    new AccessibleRelation(AccessibleRelation.MEMBER_OF, group));
            }
        }
    } // buildAccessibleGroup()

    /**
     * This sets CONTROLLER_FOR on the controls that manipulate the
     * table and CONTROLLED_BY relationships on the table to point
     * back to the controllers.
     */
    private void setTableControllers() {
        // Set up the relationships to show what controls the table
        setAccessibleController(isColumnReorderingAllowedCheckBox, scrollpane);
        setAccessibleController(showHorizontalLinesCheckBox, scrollpane);
        setAccessibleController(showVerticalLinesCheckBox, scrollpane);
        setAccessibleController(isColumnSelectionAllowedCheckBox, scrollpane);
        setAccessibleController(isRowSelectionAllowedCheckBox, scrollpane);
        setAccessibleController(interCellSpacingSlider, scrollpane);
        setAccessibleController(rowHeightSlider, scrollpane);
        setAccessibleController(selectionModeComboBox, scrollpane);
        setAccessibleController(resizeModeComboBox, scrollpane);
    }

    /**
     * Sets up accessibility relationships to denote that one
     * object controls another. The CONTROLLER_FOR property is
     * set on the controller object, and the CONTROLLED_BY
     * property is set on the target object.
     */
    private void setAccessibleController(JComponent controller, JComponent target) {
        AccessibleRelationSet controllerRelations =
            controller.getAccessibleContext().getAccessibleRelationSet();
        AccessibleRelationSet targetRelations =
            target.getAccessibleContext().getAccessibleRelationSet();

        controllerRelations.add(
            new AccessibleRelation(
                AccessibleRelation.CONTROLLER_FOR, target));
        targetRelations.add(
            new AccessibleRelation(
                AccessibleRelation.CONTROLLED_BY, controller));
    } // setAccessibleController()

    private void printTable() {
        MessageFormat headerFmt;
        MessageFormat footerFmt;
        JTable.PrintMode printMode = fitWidth.isSelected() ?
                                     JTable.PrintMode.FIT_WIDTH :
                                     JTable.PrintMode.NORMAL;

        String text;
        text = headerTextField.getText();
        if (text != null && text.length() > 0) {
            headerFmt = new MessageFormat(text);
        } else {
            headerFmt = null;
        }

        text = footerTextField.getText();
        if (text != null && text.length() > 0) {
            footerFmt = new MessageFormat(text);
        } else {
            footerFmt = null;
        }

        try {
            boolean status = tableView.print(printMode, headerFmt, footerFmt);

            if (status) {
                JOptionPane.showMessageDialog(tableView.getParent(),
                                              getBundleString("printingComplete"),
                                              getBundleString("printingResult"),
                                              JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tableView.getParent(),
                                              getBundleString("printingCancelled"),
                                              getBundleString("printingResult"),
                                              JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            String errorMessage = MessageFormat.format(getBundleString("printingFailed"),
                                                       new Object[] {pe.getMessage()});
            JOptionPane.showMessageDialog(tableView.getParent(),
                                          errorMessage,
                                          getBundleString("printingResult"),
                                          JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException se) {
            String errorMessage = MessageFormat.format(getBundleString("printingFailed"),
                                                       new Object[] {se.getMessage()});
            JOptionPane.showMessageDialog(tableView.getParent(),
                                          errorMessage,
                                          getBundleString("printingResult"),
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    class NamedColor extends Color {
        String name;
        public NamedColor(Color color, String name) {
            super(color.getRGB());
            this.name = name;
        }

        public Color getTextColor() {
            int r = getRed();
            int g = getGreen();
            int b = getBlue();
            if(r > 240 || g > 240) {
                return Color.black;
            } else {
                return Color.white;
            }
        }

        public String toString() {
            return name;
        }
    }

    class ColumnLayout implements LayoutManager {
        int xInset = 5;
        int yInset = 5;
        int yGap = 2;

        public void addLayoutComponent(String s, Component c) {}

        public void layoutContainer(Container c) {
            Insets insets = c.getInsets();
            int height = yInset + insets.top;

            Component[] children = c.getComponents();
            Dimension compSize = null;
            for (int i = 0; i < children.length; i++) {
                compSize = children[i].getPreferredSize();
                children[i].setSize(compSize.width, compSize.height);
                children[i].setLocation( xInset + insets.left, height);
                height += compSize.height + yGap;
            }

        }

        public Dimension minimumLayoutSize(Container c) {
            Insets insets = c.getInsets();
            int height = yInset + insets.top;
            int width = 0 + insets.left + insets.right;

            Component[] children = c.getComponents();
            Dimension compSize = null;
            for (int i = 0; i < children.length; i++) {
                compSize = children[i].getPreferredSize();
                height += compSize.height + yGap;
                width = Math.max(width, compSize.width + insets.left + insets.right + xInset*2);
            }
            height += insets.bottom;
            return new Dimension( width, height);
        }

        public Dimension preferredLayoutSize(Container c) {
            return minimumLayoutSize(c);
        }

        public void removeLayoutComponent(Component c) {}
    }

    void updateDragEnabled(boolean dragEnabled) {
        tableView.setDragEnabled(dragEnabled);
        headerTextField.setDragEnabled(dragEnabled);
        footerTextField.setDragEnabled(dragEnabled);
    }

}
