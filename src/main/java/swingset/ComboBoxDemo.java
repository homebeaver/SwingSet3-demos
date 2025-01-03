/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.accessibility.AccessibleRelation;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.demos.svg.FeatheRuser;
import org.jdesktop.swingx.icon.SizingConstants;

/**
 * JComboBox Demo
 *
 * @author Jeff Dinkins (inception)
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ComboBoxDemo extends AbstractDemo implements ActionListener {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JComboBox.gif";

	private static final long serialVersionUID = 6157959394784801204L;
    private static final Logger LOG = Logger.getLogger(ComboBoxDemo.class.getName());
    
    Face face;
    JXLabel xfaceLabel;
    
    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new ComboBoxDemo(controller);
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

    /**
     * A subclass of BasicComboBoxRenderer to override the renderer property
     * - border of the Label : EmptyNoFocusBorder,
     *          of the List : EtchedBorder
     */
    @SuppressWarnings("serial")
	class MyComboBoxRenderer extends BasicComboBoxRenderer {

        static Border etchedNoFocusBorder = BorderFactory.createEtchedBorder();

        public MyComboBoxRenderer() {
            super();
            // Alternativ via IconHighlighter
//          setIcon(FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
        }

        Border getEmptyNoFocusBorder() {
        	return BasicComboBoxRenderer.noFocusBorder;
        }

        @Override
		public Component getListCellRendererComponent(JList<?> list, Object value, 
				int index, boolean isSelected, boolean cellHasFocus) {
        	
        	// index -1 when called in BasicComboBoxUI.getDisplaySize()
        	//      >=0 when called in BasicListUI.updateLayoutState()
			setBorder(index == -1 ? getEmptyNoFocusBorder() : etchedNoFocusBorder);

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			setFont(list.getFont());

			if (value instanceof Icon) {
				setIcon((Icon) value);
			} else {
				setText((value == null) ? "" : value.toString());
			}
			
			return this;
		}

    }

    /**
     * ComboBoxDemo Constructor
     * 
     * @param controllerFrame controller Frame
     */
    public ComboBoxDemo(Frame controllerFrame) {
        super(new BorderLayout());
        super.setPreferredSize(PREFERRED_SIZE);
        super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	controllerFrame.setTitle(getBundleString("name"));

        face = new Face();
        xfaceLabel = new JXLabel(face);
        super.add(xfaceLabel, BorderLayout.CENTER);
        
		getComboPane(); // creates ComboBoxes hairCB, ... and presetCB
		JXPanel presetsPanel = new JXPanel();
		presetsPanel.setLayout(new BoxLayout(presetsPanel, BoxLayout.Y_AXIS));
		JXLabel l = new JXLabel(getBundleString("presets"));
		l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		presetsPanel.add(l);
		presetsPanel.add(presetCB);
		presetsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		presetCB.setRenderer(new MyComboBoxRenderer());
		presetCB.setComboBoxIcon(FeatheRuser.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON));
		
		l.setLabelFor(presetCB);
		super.add(presetsPanel, BorderLayout.NORTH);
        
		// to hold hairCB, eyesCB, mouthCB
		@SuppressWarnings("serial")
		JXPanel sidePanel = new JXPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
            public Dimension getPreferredSize() {
                return new Dimension(PREFERRED_SIZE.width/3, super.getMaximumSize().height);
            }
		};
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		
        l = new JXLabel(getBundleString("hair_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        sidePanel.add(l);
        hairCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        hairCB.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.NOTEPAD));
        sidePanel.add(hairCB);
        l.setLabelFor(hairCB);
        sidePanel.add(Box.createRigidArea(VGAP15));

        l = new JXLabel(getBundleString("eyes_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        sidePanel.add(l);
        eyesCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        eyesCB.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.FLORAL_WHITE));
        sidePanel.add(eyesCB);
        l.setLabelFor(eyesCB);
        sidePanel.add(Box.createRigidArea(VGAP15));
        
        l = new JXLabel(getBundleString("mouth_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        sidePanel.add(l);
        mouthCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        mouthCB.addHighlighter(HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));
        sidePanel.add(mouthCB);
        l.setLabelFor(mouthCB);
        sidePanel.add(Box.createRigidArea(VGAP15));

        // Fill up the remaining space
		sidePanel.add(new JPanel(new BorderLayout()));
		sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
		super.add(sidePanel, BorderLayout.EAST);
		
        getControlPane();
    }

    JXPanel comboBoxPanel = null;
    JXComboBox<String> hairCB;
    JXComboBox<String> eyesCB;
    JXComboBox<String> mouthCB;

    JXComboBox<String> presetCB;

//    @SuppressWarnings("rawtypes") // parts can be <String, ImageIcon> or reverse or <String, String>
	private Hashtable<Object, Object> parts = new Hashtable<Object, Object>();

    @Override
	public JXPanel getControlPane() {
		// no controller
    	return emptyControlPane();
	}
    
    @SuppressWarnings("serial")
	private JXPanel getComboPane() {
    	if(comboBoxPanel!=null) {
        	LOG.fine("---------------comboBoxPanel:"+comboBoxPanel);
    		return comboBoxPanel;
    	}

        // Create a panel to hold buttons
		comboBoxPanel = new JXPanel() {
			public Dimension getMaximumSize() {
				return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
			}
		};
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));

        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        JXLabel l = new JXLabel(getBundleString("presets"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        presetCB = createPresetComboBox();
        presetCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(presetCB);
        l.setLabelFor(presetCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP30));

        l = new JXLabel(getBundleString("hair_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        hairCB = createHairComboBox();
        hairCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(hairCB);
        l.setLabelFor(hairCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = new JXLabel(getBundleString("eyes_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        eyesCB = createEyesComboBox();
        eyesCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(eyesCB);
        l.setLabelFor(eyesCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = new JXLabel(getBundleString("mouth_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        mouthCB = createMouthComboBox();
        mouthCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(mouthCB);
        l.setLabelFor(mouthCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        // Fill up the remaining space
        comboBoxPanel.add(new JPanel(new BorderLayout()));

        // Indicate that the face panel is controlled by the hair, eyes and mouth combo boxes.
        Object [] controlledByObjects = new Object[3];
        controlledByObjects[0] = hairCB;
        controlledByObjects[1] = eyesCB;
        controlledByObjects[2] = mouthCB;
        AccessibleRelation controlledByRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLED_BY_PROPERTY, controlledByObjects);
        this.getAccessibleContext().getAccessibleRelationSet().add(controlledByRelation);

        // Indicate that the hair, eyes and mouth combo boxes are controllers for the face panel.
        AccessibleRelation controllerForRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR_PROPERTY, this);
        try {
            hairCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
            eyesCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
            mouthCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        } catch (Exception e) {
        	e.printStackTrace();
        	LOG.warning(e.toString());
        } finally {
        }

        // load up the face parts
        addFace("brent",     getBundleString("brent"));
        addFace("georges",   getBundleString("georges"));
        addFace("hans",      getBundleString("hans"));
        addFace("howard",    getBundleString("howard"));
        addFace("james",     getBundleString("james"));
        addFace("jeff",      getBundleString("jeff"));
        addFace("jon",       getBundleString("jon"));
        addFace("lara",      getBundleString("lara"));
        addFace("larry",     getBundleString("larry"));
        addFace("lisa",      getBundleString("lisa"));
        addFace("michael",   getBundleString("michael"));
        addFace("philip",    getBundleString("philip"));
        addFace("scott",     getBundleString("scott"));

        // set the default face
        presetCB.setSelectedIndex(0);
		return comboBoxPanel;
    }

    void addFace(String name, String i18n_name) {
        ImageIcon i;
        String i18n_hair = getBundleString("hair");
        String i18n_eyes = getBundleString("eyes");
        String i18n_mouth = getBundleString("mouth");

        parts.put(i18n_name, name); // i18n name lookup
        parts.put(name, i18n_name); // reverse name lookup

        i = StaticUtilities.createImageIcon("combobox/" + name + "hair.jpg"); //, i18n_name + i18n_hair);
        parts.put(name +  "hair", i);

        i = StaticUtilities.createImageIcon("combobox/" + name + "eyes.jpg"); //, i18n_name + i18n_eyes);
        parts.put(name +  "eyes", i);

        i = StaticUtilities.createImageIcon("combobox/" + name + "mouth.jpg"); //, i18n_name + i18n_mouth);
        parts.put(name +  "mouth", i);
    }

    JXComboBox<String> createHairComboBox() {
    	JXComboBox<String> cb = new JXComboBox<String>();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JXComboBox<String> createEyesComboBox() {
    	JXComboBox<String> cb = new JXComboBox<String>();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JXComboBox<String> createNoseComboBox() {
    	JXComboBox<String> cb = new JXComboBox<String>();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JXComboBox<String> createMouthComboBox() {
    	JXComboBox<String> cb = new JXComboBox<String>();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JXComboBox<String> createPresetComboBox() {
    	JXComboBox<String> cb = new JXComboBox<String>();
        cb.addItem(getBundleString("preset1"));
        cb.addItem(getBundleString("preset2"));
        cb.addItem(getBundleString("preset3"));
        cb.addItem(getBundleString("preset4"));
        cb.addItem(getBundleString("preset5"));
        cb.addItem(getBundleString("preset6"));
        cb.addItem(getBundleString("preset7"));
        cb.addItem(getBundleString("preset8"));
        cb.addItem(getBundleString("preset9"));
        cb.addItem(getBundleString("preset10"));
        cb.addActionListener(this);
        return cb;
    }

    void fillComboBox(JXComboBox<String> cb) {
        cb.addItem(getBundleString("brent"));
        cb.addItem(getBundleString("georges"));
        cb.addItem(getBundleString("hans"));
        cb.addItem(getBundleString("howard"));
        cb.addItem(getBundleString("james"));
        cb.addItem(getBundleString("jeff"));
        cb.addItem(getBundleString("jon"));
        cb.addItem(getBundleString("lara"));
        cb.addItem(getBundleString("larry"));
        cb.addItem(getBundleString("lisa"));
        cb.addItem(getBundleString("michael"));
        cb.addItem(getBundleString("philip"));
        cb.addItem(getBundleString("scott"));
    }

    @Override // implements ActionListener
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == hairCB) {
            String name = (String) parts.get((String) hairCB.getSelectedItem());
            face.setHair((ImageIcon) parts.get(name + "hair"));
            xfaceLabel.repaint();
        } else if(e.getSource() == eyesCB) {
            String name = (String) parts.get((String) eyesCB.getSelectedItem());
            face.setEyes((ImageIcon) parts.get(name + "eyes"));
            xfaceLabel.repaint();
        } else if(e.getSource() == mouthCB) {
            String name = (String) parts.get((String) mouthCB.getSelectedItem());
            face.setMouth((ImageIcon) parts.get(name + "mouth"));
            xfaceLabel.repaint();
        } else if(e.getSource() == presetCB) {
            String hair = null;
            String eyes = null;
            String mouth = null;
            switch(presetCB.getSelectedIndex()) {
               case 0:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("howard");
                   mouth = (String) parts.get("jeff");
                   break;
               case 1:
                   hair = (String) parts.get("jeff");
                   eyes = (String) parts.get("larry");
                   mouth = (String) parts.get("philip");
                   break;
               case 2:
                   hair = (String) parts.get("howard");
                   eyes = (String) parts.get("scott");
                   mouth = (String) parts.get("hans");
                   break;
               case 3:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("jeff");
                   mouth = (String) parts.get("hans");
                   break;
               case 4:
                   hair = (String) parts.get("brent");
                   eyes = (String) parts.get("jon");
                   mouth = (String) parts.get("scott");
                   break;
               case 5:
                   hair = (String) parts.get("lara");
                   eyes = (String) parts.get("larry");
                   mouth = (String) parts.get("lisa");
                   break;
               case 6:
                   hair = (String) parts.get("james");
                   eyes = (String) parts.get("philip");
                   mouth = (String) parts.get("michael");
                   break;
               case 7:
                   hair = (String) parts.get("philip");
                   eyes = (String) parts.get("lisa");
                   mouth = (String) parts.get("brent");
                   break;
               case 8:
                   hair = (String) parts.get("james");
                   eyes = (String) parts.get("philip");
                   mouth = (String) parts.get("jon");
                   break;
               case 9:
                   hair = (String) parts.get("lara");
                   eyes = (String) parts.get("jon");
                   mouth = (String) parts.get("scott");
                   break;
            }
            if(hair != null) {
                hairCB.setSelectedItem(hair);
                eyesCB.setSelectedItem(eyes);
                mouthCB.setSelectedItem(mouth);
                xfaceLabel.repaint();
            }
        }
    }

    class Face implements Icon {
        ImageIcon hair;
        ImageIcon eyes;
        ImageIcon mouth;

        void setHair(ImageIcon i) {
            hair = i;
        }

        void setEyes(ImageIcon i) {
            eyes = i;
        }

        void setMouth(ImageIcon i) {
            mouth = i;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int height = y;
            x = c.getWidth()/2 - getIconWidth()/2;

            if(hair != null) {
                hair.paintIcon(c, g, x, height);   height += hair.getIconHeight();
            }

            if(eyes != null) {
                eyes.paintIcon(c, g, x, height);   height += eyes.getIconHeight();
            }

            if(mouth != null) {
                mouth.paintIcon(c, g, x, height);
            }
        }

        public int getIconWidth() {
            return 344;
        }

        public int getIconHeight() {
            return 455;
        }
    }
}
