package org.jdesktop.swingx.demos.svg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.icon.SizingConstants;
import org.pushingpixels.radiance.common.api.icon.RadianceIcon;

import swingset.AbstractDemo;

/**
 * Demonstrates how one icon class is used to render different icons.
 * <p>
 * The Class <code>IconRfeather</code> was generated from feather.svg file with Radiance SVG transcoder.
 * In CENTER you see the original svg file.
 * The icons around are rendered by rotating resp. point/axis reflection (mirroring)
 * 
 * @author homeb
 *
 */
@SuppressWarnings("serial")
public class MirroringIconDemo extends AbstractDemo {

	private static final Logger LOG = Logger.getLogger(MirroringIconDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates mirroring, rotating and coloring an icon while rendering.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new MirroringIconDemo(controller);
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

    // holds the svg code the icon is created from:
    private JTextArea textArea;

    // controller:
    private JComboBox<DisplayInfo<RadianceIcon>> iconChooserCombo;

    JPanel center;
    JPanel north;
    JPanel south;
    JComponent original = null;
    JComponent original2 = null; // duplicate
    JComponent pointReflection = null;
    JComponent horizontalMirroring = null;
    JComponent verticalMirroring = null;
    JComponent neRotation = null;
    JComponent nwRotation = null;
    JComponent seRotation = null;
    JComponent swRotation = null;
    private void initComponents(String iconName) {
    	LOG.info("iconName="+iconName);
    	
    	original = createButton(original, iconName, SizingConstants.NORTH);
    	nwRotation = createButton(nwRotation, iconName, SizingConstants.NORTH_WEST);
    	neRotation = createButton(neRotation, iconName, SizingConstants.NORTH_EAST);
    	if(north==null) {
            north = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
            north.add(nwRotation);
            north.add(original);
            north.add(neRotation);
            add(north, BorderLayout.NORTH);
    	}
        
    	pointReflection = createButton(pointReflection, iconName, -1, true, true);
    	if(center==null) {
            center = new JXPanel(new BorderLayout());
            center.add(new JScrollPane(textArea));
        	add(center);
        	// point reflection under original:
            center.add(pointReflection, BorderLayout.NORTH);
    	}

    	horizontalMirroring = createButton(horizontalMirroring, iconName, -1, true, false);
    	swRotation = createButton(swRotation, iconName, SizingConstants.SOUTH_WEST);
    	seRotation = createButton(seRotation, iconName, SizingConstants.SOUTH_EAST);
    	if(south==null) {
            south = new JXPanel(new GridLayout(0, 3, 1, 1)); // zero meaning any number of rows
            south.add(swRotation);
            // horizontal mirroring opposite of original
            south.add(horizontalMirroring);
            south.add(seRotation);
            add(south, BorderLayout.SOUTH);
    	}

    	// vertical mirroring:
    	boolean addToEastAndWest = verticalMirroring==null ? true : false;
    	original2 = createButton(original2, iconName, SizingConstants.NORTH);
    	verticalMirroring = createButton(verticalMirroring, iconName, -1, false, true);
    	if(addToEastAndWest) {
    		// add original duplicate to WEST and verticalMirroring in opposite of it
        	add(original2, BorderLayout.WEST);
        	add(verticalMirroring, BorderLayout.EAST);
    	}
    	
        try {
            InputStream in = getClass().getResourceAsStream(getSvgResourceName(iconName));
        	LOG.info("read svg file");
            textArea.read(new InputStreamReader(in), null); // NPE if no resources
        } catch (NullPointerException e) {
            textArea.setText("no svg source fuond for "+iconName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public MirroringIconDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		initComponents("feather");
    }

    // Heuristic: gilt nicht allgemein!!!
    private boolean canApplyColorFilter(String iconName) {
    	return Character.isLowerCase(iconName.charAt(0));
    }
    private String upperCasePrefix(String iconName) {
    	return Character.isLowerCase(iconName.charAt(0)) ? "IconR" : "";
    }
    
    private String getSvgResourceName(String iconName) {
    	return "resources/" + iconName.replace('_', '-') + ".svg";
    }
    Class<?> iconClass = null;
    private RadianceIcon getRadianceIcon(String iconName, int width, int height) {
    	String className = getClass().getPackageName()+"."+upperCasePrefix(iconName)+iconName;
    	if(iconClass==null || !className.equals(iconClass.getName())) {
    		LOG.info("load class "+className);
			try {
				iconClass = Class.forName(className);  // throws ClassNotFoundException
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    	}
    	RadianceIcon icon = null;
    	try {
//			Method factory = iconClass.getMethod("factory");
//			RadianceIcon.Factory f = (RadianceIcon.Factory) factory.invoke(null);
			Method method = iconClass.getMethod("of", int.class, int.class);
			Object o = method.invoke(null, width, height);
			icon = (RadianceIcon)o;
		} catch (NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return icon;
    }
    private JComponent createButton(JComponent comp, String iconName, int direction) {
    	return createButton(comp, iconName, direction, false, false);
    }
    private String reflectionType(boolean horizontal, boolean vertical) {
    	if(horizontal && vertical) return "point reflection";
    	if(horizontal) return "horizontal mirroring (X axis)";
    	if(vertical) return "vertical mirroring (Y axis)";
    	return "nix";
    }
    /**
     * 
     * @param iconName
     * @param direction rotation
     * @param horizontal point/axis reflection (mirroring) 
     * @param vertical point/axis reflection (mirroring) 
     * @return
     */
    private JComponent createButton(JComponent comp, String iconName, int direction, boolean horizontal, boolean vertical) {
    	RadianceIcon icon = getRadianceIcon(iconName, SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON);
    	icon.setRotation(direction);
    	icon.setReflection(horizontal, vertical);
    	String orientation = icon.isReflection() ? reflectionType(horizontal, vertical) : "?";
    	LOG.info(iconName+ " rotation direction="+direction + 
    		" /Reflection horizontal="+horizontal + " vertical="+vertical +" String orientation="+orientation);
		switch (direction) {
		case SwingConstants.NORTH: // 1
			//orientation = "N";
            break;
		case SwingConstants.NORTH_EAST:
			orientation = "45° rotation (NE)";
	    	if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.EAST:
			orientation = "E";
			if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH_EAST:
			orientation = "135° rotation";
			if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.red);
			break;
		case SwingConstants.SOUTH: // 5
			orientation = "S";
            break;
        case SwingConstants.SOUTH_WEST:
			orientation = "rotation to SW";
			if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.WEST:
			orientation = "W";
			if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.blue);
            break;
        case SwingConstants.NORTH_WEST:
			orientation = "rotation to NW";
			if(canApplyColorFilter(iconName)) icon.setColorFilter(color -> Color.blue);
            break;
		default: { /* no xform */ }
		}
		String text = "?".equals(orientation) ? iconName : orientation;
		if(comp==null) return new JButton(text, icon);
		if(comp instanceof JButton jb) {
			jb.setIcon(icon);
			jb.setText(text);
		}
    	return comp;
    }
    
	@Override
	public JXPanel getControlPane() {
		JXPanel controls = new JXPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
			}
		};
		controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
		controls.add(Box.createRigidArea(VGAP15));

		JXLabel l = new JXLabel("select another icon:");
		l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		controls.add(l);

        // Create the combo chooser box:
		iconChooserCombo = new JComboBox<DisplayInfo<RadianceIcon>>();
		iconChooserCombo.setName("iconChooserCombo");
		iconChooserCombo.setModel(createCBM());
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(200, SizingConstants.SMALL_ICON*3));
        iconChooserCombo.setRenderer(renderer);
        iconChooserCombo.setMaximumRowCount(7); // rows the JComboBox displays
		iconChooserCombo.addActionListener(ae -> {
			@SuppressWarnings("unchecked")
			DisplayInfo<RadianceIcon> item = (DisplayInfo<RadianceIcon>)iconChooserCombo.getSelectedItem();
			LOG.info("Combo.SelectedItem=" + item.getDescription());
			initComponents(item.getDescription());
		});
		iconChooserCombo.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
		controls.add(iconChooserCombo);
		l.setLabelFor(iconChooserCombo);

        // Fill up the remaining space
		controls.add(new JPanel(new BorderLayout()));

		return controls;
	}

    private ComboBoxModel<DisplayInfo<RadianceIcon>> createCBM() {
        MutableComboBoxModel<DisplayInfo<RadianceIcon>> model = new DefaultComboBoxModel<DisplayInfo<RadianceIcon>>();
        for (int i = 0; i < iconNames.length; i++) {
//            model.addElement(new DisplayInfo<RadianceIcon>(iconNames[i], createRadianceIcon(iconNames[i])));
            model.addElement(new DisplayInfo<RadianceIcon>(iconNames[i], 
            		getRadianceIcon(iconNames[i], SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON)));
        }
        return model;
    }
    private static final String[] iconNames = {"activity", "airplay", "alert_circle"
    		// ...
    		, "archive", "award"
    		// with svg resource:
    		, "arrow", "arrowInCircle", "chevron", "chevrons", "feather"
    		// colored svgs (do not apply setColorFilter!):
    		, "Yellow_Light_Icon"
    		// without svg resource:
    		, "Red_Light_Icon", "Green_Light_Icon"
    		// not feather:
    		, "Duke" // with svg resource
    		, "Duke_waving" // without svg resource
    		, "FlagBR" // Flag of Brazil
    		, "FlagCH"
    		, "FlagCS"
    		, "FlagDE"
    		, "FlagES"
    		, "FlagFR"
    		, "FlagIT"
    		, "FlagNL"
    		, "FlagPL"
    		, "FlagSE"
    		, "FlagUK"
    		};

    class ComboBoxRenderer extends JLabel implements ListCellRenderer<DisplayInfo<RadianceIcon>> {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * Return a label that has been configured to display the specified combo item.
         */
		public Component getListCellRendererComponent(JList<? extends DisplayInfo<RadianceIcon>> list,
				DisplayInfo<RadianceIcon> comboItem, 
				int index, 
				boolean isSelected, 
				boolean cellHasFocus) {
			
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and its description text. If icon was null, say so.
			RadianceIcon icon = comboItem.getValue(); // icons[selectedIndex];
			String iconName = comboItem.getDescription(); // iconNames[selectedIndex];
			setIcon(icon); // Label.setIcon
			/*
			   wenn ausreichend Platz ist (renderer.setPreferredSize), dann
			   kann man den text unterhalb des icons positionieren:
			 */
			this.setVerticalTextPosition(BOTTOM); // default is CENTER. 
			this.setHorizontalTextPosition(CENTER); // LEADING, ... TRAILING is default
			if (icon != null) {
				setText(iconName);
				setFont(list.getFont());
			} else {
				setUhOhText(iconName + " (no image available)", list.getFont());
			}

			return this;
		}

        //Set the font and text when no image was found.
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { //lazily create this font
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }

}
