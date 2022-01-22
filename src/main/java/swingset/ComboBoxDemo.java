/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.accessibility.AccessibleRelation;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

/**
 * JComboBox Demo
 *
 * @author Jeff Dinkins (inception)
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ComboBoxDemo extends AbstractDemo implements ActionListener {

	public static final String ICON_PATH = "toolbar/JComboBox.gif";

	private static final long serialVersionUID = 6157959394784801204L;
    private static final Logger LOG = Logger.getLogger(ComboBoxDemo.class.getName());
    
    Face face;
    JXLabel xfaceLabel;
    
    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {  // TODO
    	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//    	ComboBoxDemo demo = new ComboBoxDemo(new SwingSet2(null, gc, false));
//        demo.mainImpl();
    }

    /**
     * ComboBoxDemo Constructor
     */
    public ComboBoxDemo(Frame frame) {
        super(new BorderLayout());
        super.setPreferredSize(PREFERRED_SIZE);
        super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        face = new Face();
        xfaceLabel = new JXLabel(face);
        super.add(xfaceLabel, BorderLayout.CENTER);

        getControlPane();
    }

    JXPanel comboBoxPanel = null; // Controller
    JXComboBox<String> hairCB;
    JXComboBox<String> eyesCB;
    JXComboBox<String> mouthCB;

    JXComboBox<String> presetCB;

    private Hashtable parts = new Hashtable();

    @Override
    public JXPanel getControlPane() {
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

        JXLabel l = new JXLabel(getString("presets"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        presetCB = createPresetComboBox();
        presetCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(presetCB);
        l.setLabelFor(presetCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP30));

        l = new JXLabel(getString("hair_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        hairCB = createHairComboBox();
        hairCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(hairCB);
        l.setLabelFor(hairCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = new JXLabel(getString("eyes_description"));
        l.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        eyesCB = createEyesComboBox();
        eyesCB.setAlignmentX(JXComboBox.LEFT_ALIGNMENT);
        comboBoxPanel.add(eyesCB);
        l.setLabelFor(eyesCB);
        comboBoxPanel.add(Box.createRigidArea(VGAP15));

        l = new JXLabel(getString("mouth_description"));
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
        hairCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        eyesCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        mouthCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);

        // load up the face parts
        addFace("brent",     getString("brent"));
        addFace("georges",   getString("georges"));
        addFace("hans",      getString("hans"));
        addFace("howard",    getString("howard"));
        addFace("james",     getString("james"));
        addFace("jeff",      getString("jeff"));
        addFace("jon",       getString("jon"));
        addFace("lara",      getString("lara"));
        addFace("larry",     getString("larry"));
        addFace("lisa",      getString("lisa"));
        addFace("michael",   getString("michael"));
        addFace("philip",    getString("philip"));
        addFace("scott",     getString("scott"));

        // set the default face
        presetCB.setSelectedIndex(0);
		return comboBoxPanel;
    }

    void addFace(String name, String i18n_name) {
        ImageIcon i;
        String i18n_hair = getString("hair");
        String i18n_eyes = getString("eyes");
        String i18n_mouth = getString("mouth");

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
        cb.addItem(getString("preset1"));
        cb.addItem(getString("preset2"));
        cb.addItem(getString("preset3"));
        cb.addItem(getString("preset4"));
        cb.addItem(getString("preset5"));
        cb.addItem(getString("preset6"));
        cb.addItem(getString("preset7"));
        cb.addItem(getString("preset8"));
        cb.addItem(getString("preset9"));
        cb.addItem(getString("preset10"));
        cb.addActionListener(this);
        return cb;
    }

    void fillComboBox(JXComboBox<String> cb) {
        cb.addItem(getString("brent"));
        cb.addItem(getString("georges"));
        cb.addItem(getString("hans"));
        cb.addItem(getString("howard"));
        cb.addItem(getString("james"));
        cb.addItem(getString("jeff"));
        cb.addItem(getString("jon"));
        cb.addItem(getString("lara"));
        cb.addItem(getString("larry"));
        cb.addItem(getString("lisa"));
        cb.addItem(getString("michael"));
        cb.addItem(getString("philip"));
        cb.addItem(getString("scott"));
    }

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
