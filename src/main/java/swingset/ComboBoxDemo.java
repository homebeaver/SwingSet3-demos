/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

/**
 * JComboBox Demo
 *
 * @author Jeff Dinkins
 */
/*

links controler:
	comboBoxPanel

rechts demo: >>>>>>>>>>>    <<<<<<<<<<<<
	facePanel mit faceLabel aus facePaintedIcon = new Face();

 */
public class ComboBoxDemo implements ActionListener {

    public static final String ICON_PATH = "toolbar/JComboBox.gif";

    private static final Logger LOG = Logger.getLogger(ComboBoxDemo.class.getName());

    JXPanel facePanel = null;
    Face facePaintedIcon;
    JXLabel xfaceLabel;

    public JXPanel getDemoPane() {
    	if(facePanel!=null) {
        	LOG.info("---------------facePanel:"+facePanel);
    		return facePanel;
    	}
        facePaintedIcon = new Face();
        facePanel = new JXPanel(new BorderLayout());
        facePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        xfaceLabel = new JXLabel(facePaintedIcon);
        facePanel.add(xfaceLabel, BorderLayout.CENTER);
        return facePanel;
    }

    JComboBox hairCB;
    JComboBox eyesCB;
    JComboBox mouthCB;

    JComboBox presetCB;

    Hashtable parts = new Hashtable();

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    	ComboBoxDemo demo = new ComboBoxDemo(new SwingSet2(null, gc, false));
        demo.mainImpl();
    }

    JPanel panel = new JPanel();
    void mainImpl() {
    	// frame ...
//    	JFrame frame = swingset.getFrame();
//    	frame.setName(getName());
//        frame.getContentPane().setLayout(new BorderLayout());
//        frame.getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(DemoModule.PREFERRED_WIDTH, DemoModule.PREFERRED_HEIGHT));
//        frame.pack();
//        frame.setVisible(true);

    }
    /**
     * ComboBoxDemo Constructor
     */
    public ComboBoxDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
//        super(swingset, "ComboBoxDemo", ICON_PATH);

        getComboBoxDemo();
    }

    JXPanel comboBoxPanel = null;
    public JXPanel getComboBoxDemo() {
    	if(comboBoxPanel!=null) {
        	LOG.info("---------------comboBoxPanel:"+comboBoxPanel);
    		return comboBoxPanel;
    	}
//        JPanel demo = getDemoPanel();
//        JPanel demoPanel = getDemoPanel();
//        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

//        demoPanel.add(Box.createRigidArea(VGAP20));
//        demoPanel.add(innerPanel);
//        demoPanel.add(Box.createRigidArea(VGAP20));
//
        innerPanel.add(Box.createRigidArea(DemoModule.HGAP20));

        // Create a panel to hold buttons
        comboBoxPanel = new JXPanel() {
                public Dimension getMaximumSize() {
                    return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
                }
        };
        comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));

        comboBoxPanel.add(Box.createRigidArea(DemoModule.VGAP15));

//        JLabel l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.presets")));
        JLabel l = new JLabel("presets");
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        comboBoxPanel.add(l);
        presetCB = (JComboBox) comboBoxPanel.add(createPresetComboBox());
        presetCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(presetCB);
        comboBoxPanel.add(Box.createRigidArea(DemoModule.VGAP30));

//        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.hair_description")));
        l = new JLabel("hair_description");
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        hairCB = (JComboBox) comboBoxPanel.add(createHairComboBox());
        hairCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(hairCB);
        comboBoxPanel.add(Box.createRigidArea(DemoModule.VGAP15));

//        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.eyes_description")));
        l = new JLabel("eyes_description");
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        eyesCB = (JComboBox) comboBoxPanel.add(createEyesComboBox());
        eyesCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(eyesCB);
        comboBoxPanel.add(Box.createRigidArea(DemoModule.VGAP15));

//        l = (JLabel) comboBoxPanel.add(new JLabel(getString("ComboBoxDemo.mouth_description")));
        l = new JLabel("mouth_description");
        l.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        mouthCB = (JComboBox) comboBoxPanel.add(createMouthComboBox());
        mouthCB.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
        l.setLabelFor(mouthCB);
        comboBoxPanel.add(Box.createRigidArea(DemoModule.VGAP15));

        // Fill up the remaining space
        comboBoxPanel.add(new JPanel(new BorderLayout()));

//        // Create and place the Face. >>>>>>>>>>>>>>>>
//
//        facePaintedIcon = new Face();
//        JPanel facePanel = new JPanel();
//        facePanel.setLayout(new BorderLayout());
//        facePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
//
//        faceLabel = new JLabel(facePaintedIcon);
//        facePanel.add(faceLabel, BorderLayout.CENTER);
////        <<<<<<<<<<<<<<<<<<<
        // Indicate that the face panel is controlled by the hair, eyes and mouth combo boxes.
        Object [] controlledByObjects = new Object[3];
        controlledByObjects[0] = hairCB;
        controlledByObjects[1] = eyesCB;
        controlledByObjects[2] = mouthCB;
        AccessibleRelation controlledByRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLED_BY_PROPERTY, controlledByObjects);
        getDemoPane().getAccessibleContext().getAccessibleRelationSet().add(controlledByRelation);

        // Indicate that the hair, eyes and mouth combo boxes are controllers for the face panel.
        AccessibleRelation controllerForRelation =
            new AccessibleRelation(AccessibleRelation.CONTROLLER_FOR_PROPERTY, getDemoPane());
        hairCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        eyesCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);
        mouthCB.getAccessibleContext().getAccessibleRelationSet().add(controllerForRelation);

        // add buttons and image panels to inner panel
        innerPanel.add(comboBoxPanel);
        innerPanel.add(Box.createRigidArea(DemoModule.HGAP30));
//        innerPanel.add(facePanel);
        innerPanel.add(Box.createRigidArea(DemoModule.HGAP20));

        // load up the face parts
        addFace("brent",     ("ComboBoxDemo.brent"));
        addFace("georges",   ("ComboBoxDemo.georges"));
        addFace("hans",      ("ComboBoxDemo.hans"));
        addFace("howard",    ("ComboBoxDemo.howard"));
        addFace("james",     ("ComboBoxDemo.james"));
        addFace("jeff",      ("ComboBoxDemo.jeff"));
        addFace("jon",       ("ComboBoxDemo.jon"));
        addFace("lara",      ("ComboBoxDemo.lara"));
        addFace("larry",     ("ComboBoxDemo.larry"));
        addFace("lisa",      ("ComboBoxDemo.lisa"));
        addFace("michael",   ("ComboBoxDemo.michael"));
        addFace("philip",    ("ComboBoxDemo.philip"));
        addFace("scott",     ("ComboBoxDemo.scott"));

        // set the default face
        presetCB.setSelectedIndex(0);
		return comboBoxPanel;
    }

    void addFace(String name, String i18n_name) {
        ImageIcon i;
//        String i18n_hair = getString("ComboBoxDemo.hair");
//        String i18n_eyes = getString("ComboBoxDemo.eyes");
//        String i18n_mouth = getString("ComboBoxDemo.mouth");
        String i18n_hair = ("ComboBoxDemo.hair");
        String i18n_eyes = ("ComboBoxDemo.eyes");
        String i18n_mouth = ("ComboBoxDemo.mouth");

        parts.put(i18n_name, name); // i18n name lookup
        parts.put(name, i18n_name); // reverse name lookup

        i = StaticUtilities.createImageIcon("combobox/" + name + "hair.jpg"); //, i18n_name + i18n_hair);
        parts.put(name +  "hair", i);

        i = StaticUtilities.createImageIcon("combobox/" + name + "eyes.jpg"); //, i18n_name + i18n_eyes);
        parts.put(name +  "eyes", i);

        i = StaticUtilities.createImageIcon("combobox/" + name + "mouth.jpg"); //, i18n_name + i18n_mouth);
        parts.put(name +  "mouth", i);
    }

    Face getFace() {
        return facePaintedIcon;
    }

    JComboBox createHairComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createEyesComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createNoseComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createMouthComboBox() {
        JComboBox cb = new JComboBox();
        fillComboBox(cb);
        cb.addActionListener(this);
        return cb;
    }

    JComboBox createPresetComboBox() {
        JComboBox cb = new JComboBox();
        cb.addItem(("ComboBoxDemo.preset1"));
        cb.addItem(("ComboBoxDemo.preset2"));
        cb.addItem(("ComboBoxDemo.preset3"));
        cb.addItem(("ComboBoxDemo.preset4"));
        cb.addItem(("ComboBoxDemo.preset5"));
        cb.addItem(("ComboBoxDemo.preset6"));
        cb.addItem(("ComboBoxDemo.preset7"));
        cb.addItem(("ComboBoxDemo.preset8"));
        cb.addItem(("ComboBoxDemo.preset9"));
        cb.addItem(("ComboBoxDemo.preset10"));
        cb.addActionListener(this);
        return cb;
    }

    void fillComboBox(JComboBox cb) {
        cb.addItem(("ComboBoxDemo.brent"));
        cb.addItem(("ComboBoxDemo.georges"));
        cb.addItem(("ComboBoxDemo.hans"));
        cb.addItem(("ComboBoxDemo.howard"));
        cb.addItem(("ComboBoxDemo.james"));
        cb.addItem(("ComboBoxDemo.jeff"));
        cb.addItem(("ComboBoxDemo.jon"));
        cb.addItem(("ComboBoxDemo.lara"));
        cb.addItem(("ComboBoxDemo.larry"));
        cb.addItem(("ComboBoxDemo.lisa"));
        cb.addItem(("ComboBoxDemo.michael"));
        cb.addItem(("ComboBoxDemo.philip"));
        cb.addItem(("ComboBoxDemo.scott"));
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == hairCB) {
            String name = (String) parts.get((String) hairCB.getSelectedItem());
            facePaintedIcon.setHair((ImageIcon) parts.get(name + "hair"));
            xfaceLabel.repaint();
        } else if(e.getSource() == eyesCB) {
            String name = (String) parts.get((String) eyesCB.getSelectedItem());
            facePaintedIcon.setEyes((ImageIcon) parts.get(name + "eyes"));
            xfaceLabel.repaint();
        } else if(e.getSource() == mouthCB) {
            String name = (String) parts.get((String) mouthCB.getSelectedItem());
            facePaintedIcon.setMouth((ImageIcon) parts.get(name + "mouth"));
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
