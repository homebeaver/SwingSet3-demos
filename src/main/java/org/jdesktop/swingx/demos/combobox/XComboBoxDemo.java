package org.jdesktop.swingx.demos.combobox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.MutableComboBoxModel;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.demos.search.Contributor;
import org.jdesktop.swingx.demos.search.Contributors;
import org.jdesktop.swingx.demos.xlist.ListDemoConstants;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import swingset.AbstractDemo;
import swingset.plaf.LaFUtils;

/**
 * A demo to compare {@code JComboBox} with {@code JXComboBox}.
 * <p>
 * Left: JComboBox with unsorted pet Strings
 * <p>
 * Right: JXComboBox with sorted Objects (pet Strings + contributors)
 * 
 * @author EUGen https://github.com/homebeaver
 */
public class XComboBoxDemo extends AbstractDemo implements ListDemoConstants {

	private static final long serialVersionUID = 4404504405146801119L;
	private static final Logger LOG = Logger.getLogger(XComboBoxDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXComboBox, an extended combo box control";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	if(args.length>0) LaFUtils.setLAFandTheme(Arrays.asList(args));
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new XComboBoxDemo(controller);
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

	/**
	 * String representation for pets (String) and persons of type Contributors.
	 * 
	 * We need the String representation in drop-down list to display the value of the items, see MyComboBoxRenderer
	 * and for sorting the items, see Comparator.
	 * 
	 * @param o object item
	 * @return preferred String representation of o
	 */
	private static String preferredStringRepresentation(Object o) {
		if(o==null) return "";
    	return o instanceof Contributor c ? (c.getLastName() + ", " + c.getFirstName() + " (" + c.getMerits() + ")") 
    			: o.toString();		
	}

    // intentionally unsorted
    private static final String[] petStrings = { "Tyrannosaurus Rex", "Dog", "Bird", "Cat", "Rabbit", "Pig" };
    private static final String toolTipText = "Choose an animal name from the combo box to view its picture";
    
    // abgeschrieben aus MirroringIconDemo
    private static final String DEFAULT = "DEFAULT";
    @SuppressWarnings("serial")
	private static final Map<String, String> nameToClassname = new HashMap<>(){
        {
            put(DEFAULT,                "org.jdesktop.swingx.icon.EmptyIcon");
            put("arrow",                "org.jdesktop.swingx.icon.ArrowIcon");
            put("chevron",              "org.jdesktop.swingx.icon.ChevronIcon");
            put("chevrons",             "org.jdesktop.swingx.icon.ChevronsIcon");
        }
    };
    private static final String[] iconNames = { DEFAULT, "arrow", "arrowInCircle", "chevron", "chevrons" };
    private String upperCasePrefix(String iconName) {
    	return Character.isLowerCase(iconName.charAt(0)) ? "FeatheR" : "";
    }
    private RadianceIcon getRadianceIcon(Class<?> iconClass, int size) {
    	RadianceIcon icon = null;
    	try {
			Method method = iconClass.getMethod("of", int.class, int.class);
			Object o = method.invoke(null, size, size); // width, height
			icon = (RadianceIcon)o; // ClassCastException
		} catch (NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
    	return icon;
    }
    private Class<?> iconClass = null;
    private Icon getRadianceIcon(String iconName, int size) {
    	int width=size;
    	int height=size;
    	String className = nameToClassname.get(iconName);
    	if(className==null) {
        	className = "org.jdesktop.swingx.demos.svg."+upperCasePrefix(iconName)+iconName;
    	}
    	if(iconClass==null || !className.equals(iconClass.getName())) {
    		LOG.info("load class "+className);
			try {
				iconClass = Class.forName(className);  // throws ClassNotFoundException
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
    	}
    	if(iconName==DEFAULT) {
    		return new EmptyIcon(width, height);
    	}
    	return getRadianceIcon(iconClass, size);
    }

    JSplitPane splitPane = null;
    JXPanel left, right;
    JComboBox<String> cb;
    JXComboBox<Object> xcb;
    JLabel leftpic, rightpic;

    private JComponent createLeftPane() {
    	left = new JXPanel(new VerticalLayout(3));
    	left.setName("left");
    	
        JXTitledSeparator leftSeparator = new JXTitledSeparator();
        leftSeparator.setTitle(getBundleString("left.separator.title", "uneditable JComboBox:"));
        leftSeparator.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(leftSeparator);
        
        cb = new JComboBox<>(petStrings);
        cb.setToolTipText(getBundleString("cb.toolTipText", toolTipText));
        cb.addActionListener(ae -> {
        	String petName = (String)cb.getSelectedItem();
        	updateLabel(leftpic, petName);
        });
        left.add(cb, BorderLayout.PAGE_START); // NORTH
        
        //Set up the pet picture.
        leftpic = new JLabel();
        leftpic.setFont(leftpic.getFont().deriveFont(Font.ITALIC));
        leftpic.setHorizontalAlignment(JLabel.CENTER);
        updateLabel(leftpic, petStrings[cb.getSelectedIndex()]);
        leftpic.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        left.add(leftpic, BorderLayout.PAGE_END); // SOUTH
        
        left.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        return left;
    }
    
    private JComponent createRightPane() {
        right = new JXPanel(new VerticalLayout(3));
        right.setName("right");
    	
        JXTitledSeparator sep = new JXTitledSeparator();
        sep.setTitle(getBundleString("right.separator.title", "JXComboBox example:"));
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(sep);
        
        //Create a sorted combo box with Object items; define a comparator
        xcb = new JXComboBox<>(petStrings, true);
        xcb.setComparator(new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				String s1 = preferredStringRepresentation(o1);
				String s2 = preferredStringRepresentation(o2);
//            	System.out.println("Comparator compare "+s1+" with "+s2);
				return s1.compareToIgnoreCase(s2);
			}        	
        });

        // add some contributors
        ComboBoxModel<Contributor> m = Contributors.getContributorModel();
//		LOG.info("Contributor item count = "+m.getSize());
        for(int c=0; c<3; c++) {
        	xcb.addItem(m.getElementAt(c));
        }
        
        // determine selected item (-1 indicates no selection, 0 is the default)
        if(xcb.getModel().getSize()>0) xcb.setSelectedIndex(1);

        xcb.setToolTipText(getBundleString("cb.toolTipText", toolTipText));
        xcb.addActionListener(ae -> {
            Object o = xcb.getSelectedItem();
            if(o instanceof String petName) {
                updateLabel(rightpic, petName);
            } else {
//                System.out.println("SelectedItem is not a String:"+o);
                updateLabel(rightpic, o.toString());
            }
        });
        // setRenderer(ListCellRenderer<? super E> renderer)
        StringValue sv = (Object value) -> {
        	return preferredStringRepresentation(value);
        };
		IconValue iv = (Object value) -> {
			if (value instanceof Contributor c) {
				return flagIcons[(c.getMerits()) % flagIcons.length];
			}
			return IconValue.NULL_ICON;
		};
        xcb.setRenderer(new DefaultListRenderer<Object>(sv, iv));
        right.add(xcb, BorderLayout.CENTER);
        
        //Set up the item picture.
        rightpic = new JLabel();
        rightpic.setFont(rightpic.getFont().deriveFont(Font.ITALIC));
        rightpic.setHorizontalAlignment(JLabel.CENTER);
        updateLabel(rightpic, petStrings[xcb.getSelectedIndex()]);
        rightpic.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        right.add(rightpic, BorderLayout.PAGE_END);
        
        right.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        return right;
    }
    protected void updateLabel(JLabel picture, String name) {
    	String path = "resources/images/"+name+".gif";
        ImageIcon icon = createImageIcon(path);
        picture.setIcon(icon);
        picture.setToolTipText(getBundleString("picture.toolTipText", "A drawing of a " + name.toLowerCase()));
        if (icon != null) {
        	picture.setText(null);
        } else {
        	picture.setText(getBundleString("picture.text", "Image not found"));
        }
    }
    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path) {
    	URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    /**
     * ComboBoxDemo Constructor
     * 
     * @param frame controller Frame
     */
    public XComboBoxDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPane(), createRightPane());
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(300);

        super.add(splitPane, BorderLayout.CENTER);
        super.setBackground(Color.black);
    }

	// Controller:
    private JComboBox<SortOrder> sortCombo;
    private JComboBox<DisplayInfo<Icon>> iconChooserCombo;

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel();

        panel.setBorder(new TitledBorder("Combo box Controls"));
		// jgoodies layout and builder:
        panel.setLayout(new FormLayout(  		
            new ColumnSpec[] { // 2 columns + glue
                FormFactory.GLUE_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,	// 1st column
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.PREF_COLSPEC,		// 2nd column
                FormFactory.GLUE_COLSPEC,
            },
            new RowSpec[] { // 5 rows + glue
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC 
            }
        ));
        
        CellConstraints cc = new CellConstraints();
        
        JLabel slabel = new JLabel(getBundleString("sortComboLabel.text", "List Sort Order:"));
        panel.add(slabel, cc.rc(1, 2));
        sortCombo = new JComboBox<SortOrder>(new EnumComboBoxModel<SortOrder>(SortOrder.class));
        sortCombo.setName("sortCombo");
        sortCombo.setSelectedItem(SortOrder.ASCENDING);
        /*
BUG:
	anfangs ist SortOrder.ASCENDING : OK
	- ändert man in UNSORTED : OK
	- ändert nam anschliessend in ASCENDING, so wird Comparator nicht berücksichtigt
         */
        sortCombo.addActionListener(ae -> {
        	LOG.info("---"+sortCombo.getSelectedItem()+"------"+ae);
        	SortOrder so = (SortOrder)sortCombo.getSelectedItem();
        	xcb.setSortOrder(so);
        });
        panel.add(sortCombo, cc.rc(1, 4));
        
        JLabel ilabel = new JLabel(getBundleString("iconComboLabel.text", "Combo box icons:"));
        panel.add(ilabel, cc.rc(3, 2));
        iconChooserCombo = new JComboBox<DisplayInfo<Icon>>();
        iconChooserCombo.setName("iconChooserCombo");
        MutableComboBoxModel<DisplayInfo<Icon>> model = new DefaultComboBoxModel<DisplayInfo<Icon>>();
        for (int i = 0; i < iconNames.length; i++) {
        	Icon ri = getRadianceIcon(iconNames[i], RadianceIcon.SMALL_ICON);
        	if(ri==null) {
        		LOG.warning("no icon class for "+iconNames[i]);
        	} else {
                model.addElement(new DisplayInfo<Icon>(iconNames[i], ri));
        	}
        }
        iconChooserCombo.setModel(model);
//        ComboBoxRenderer renderer = new ComboBoxRenderer();
//        renderer.setPreferredSize(new Dimension(200, RadianceIcon.SMALL_ICON*3));
//        iconChooserCombo.setRenderer(renderer);
		iconChooserCombo.addActionListener(ae -> {
			@SuppressWarnings("unchecked")
			DisplayInfo<Icon> item = (DisplayInfo<Icon>)iconChooserCombo.getSelectedItem();
			Icon i = item.getValue();
			if(i instanceof RadianceIcon ri) {
				ri.setReflection(true, false); // horizontal spiegeln
				RadianceIcon icon = getRadianceIcon(ri.getClass(), RadianceIcon.SMALL_ICON); // nicht gespiegelt
				LOG.info("iconChooserCombo.SelectedItem=" + item.getDescription() + "\n "+ri + "\n "+icon);
				xcb.setComboBoxIcon(ri, icon);
				right.updateUI();
			} else {
				xcb.setComboBoxIcon(null);
				right.updateUI();
			}
		});
//		iconChooserCombo.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
//		controls.add(iconChooserCombo);
//		selectLabel.setLabelFor(iconChooserCombo);
		panel.add(iconChooserCombo, cc.rc(3, 4));

        JButton foregroundColor = new JButton(new AbstractAction("Select Foreground") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(XComboBoxDemo.this, "Foreground Color", cb.getForeground());
                
                if (color != null) {
                	cb.setForeground(color);
                	xcb.setForeground(color);
                }
            }
        });
        panel.add(foregroundColor, cc.rc(5, 2));
        
        JButton backgroundColor = new JButton(new AbstractAction("Select Background") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(XComboBoxDemo.this, "Background Color", cb.getBackground());
                
                if (color != null) {
                	cb.setBackground(color);
                	xcb.setBackground(color);
                }
            }
        });
        panel.add(backgroundColor, cc.rc(5, 4));
        
//        label = new JLabel("Painter:");
//        panel.add(label, cc.rc(7, 2));
//        
//        backgroundPainter = new JComboBox<DisplayInfo<Painter<? super Component>>>(new ListComboBoxModel<DisplayInfo<Painter<? super Component>>>(getPainters()));
//        backgroundPainter.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
//        panel.add(backgroundPainter, cc.rc(7, 4));
//        
//        label = new JLabel("Font Style:");
//        panel.add(label, cc.rc(9, 2));
//        
//        ListComboBoxModel<DisplayInfo<Integer>> model = new ListComboBoxModel<DisplayInfo<Integer>>(getFontStyles());
//        fontStyle = new JComboBox<DisplayInfo<Integer>>(model);
//        fontStyle.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
//        panel.add(fontStyle, cc.rc(9, 4));

//        bind();
//
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                focusCombo.requestFocusInWindow();
//            }
//        });

        return panel;
	}

}
