/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;

/**
 * List Demo. This demo shows that it is not
 * always necessary to have an array of objects
 * as big as the size of the list stored.
 *
 * Indeed, in this example, there is no array
 * kept for the list data, rather it is generated
 * on the fly as only those elements are needed.
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ListDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JList.gif";
	
	private static final long serialVersionUID = -6590141127414585946L;
	private static final String DESCRIPTION = "JList Demo";
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = true;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			// no controller
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			AbstractDemo demo = new ListDemo(frame);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	});
    }

    JList list;

    JPanel prefixList;
    JPanel suffixList;

    Action prefixAction;
    Action suffixAction;

    GeneratedListModel listModel;

    Vector<JCheckBox> checkboxes = new Vector<JCheckBox>();

    /**
     * ListDemo Constructor
     * @param frame controller Frame
     */
    public ListDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        if(CONTROLLER_IN_PRESENTATION_FRAME) {
            JLabel description = new JLabel(getBundleString("description"));
            super.add(description, BorderLayout.NORTH);
        }

        loadImages();

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(Box.createRigidArea(HGAP10));
        super.add(centerPanel, BorderLayout.CENTER);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.add(Box.createRigidArea(VGAP10));

        centerPanel.add(listPanel);
        centerPanel.add(Box.createRigidArea(HGAP30));

        // Create the list
        list = new JList();
        list.setCellRenderer(new CompanyLogoListCellRenderer());
        listModel = new GeneratedListModel(this);
        list.setModel(listModel);

        // Set the preferred row count. This affects the preferredSize
        // of the JList when it's in a scrollpane.
        list.setVisibleRowCount(22);

        // Add list to a scrollpane
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);
        listPanel.add(Box.createRigidArea(VGAP10));

        // Add the control panel (holds the prefix/suffix list and prefix/suffix checkboxes)
        centerPanel.add(createControlPanel());

        // create prefixes and suffixes
        addPrefix("Tera", true);
        addPrefix("Micro", false);
        addPrefix("Southern", false);
        addPrefix("Net", true);
        addPrefix("YoYo", true);
        addPrefix("Northern", false);
        addPrefix("Tele", false);
        addPrefix("Eastern", false);
        addPrefix("Neo", false);
        addPrefix("Digi", false);
        addPrefix("National", false);
        addPrefix("Compu", true);
        addPrefix("Meta", true);
        addPrefix("Info", false);
        addPrefix("Western", false);
        addPrefix("Data", false);
        addPrefix("Atlantic", false);
        addPrefix("Advanced", false);
        addPrefix("Euro", false);
        addPrefix("Pacific", false);
        addPrefix("Mobile", false);
        addPrefix("In", false);
        addPrefix("Computa", false);
        addPrefix("Digital", false);
        addPrefix("Analog", false);

        addSuffix("Tech", true);
        addSuffix("Soft", true);
        addSuffix("Telecom", true);
        addSuffix("Solutions", false);
        addSuffix("Works", true);
        addSuffix("Dyne", false);
        addSuffix("Services", false);
        addSuffix("Vers", false);
        addSuffix("Devices", false);
        addSuffix("Software", false);
        addSuffix("Serv", false);
        addSuffix("Systems", true);
        addSuffix("Dynamics", true);
        addSuffix("Net", false);
        addSuffix("Sys", false);
        addSuffix("Computing", false);
        addSuffix("Scape", false);
        addSuffix("Com", false);
        addSuffix("Ware", false);
        addSuffix("Widgets", false);
        addSuffix("Media", false);
        addSuffix("Computer", false);
        addSuffix("Hardware", false);
        addSuffix("Gizmos", false);
        addSuffix("Concepts", false);
    }

    @Override
	public JXPanel getControlPane() {
        if(CONTROLLER_IN_PRESENTATION_FRAME) return emptyControlPane();

        JLabel description = new JLabel(getBundleString("description"));
        
        JXPanel controller = new JXPanel();
        controller.add(description, BorderLayout.NORTH);
    	return controller;
    }


    void updateDragEnabled(boolean dragEnabled) {
        list.setDragEnabled(dragEnabled);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel() {
            Insets insets = new Insets(0, 4, 10, 10);
            public Insets getInsets() {
                return insets;
            }
        };
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        JPanel prefixPanel = new JPanel();
        prefixPanel.setLayout(new BoxLayout(prefixPanel, BoxLayout.Y_AXIS));
        prefixPanel.add(new JLabel(getBundleString("prefixes")));

        JPanel suffixPanel = new JPanel();
        suffixPanel.setLayout(new BoxLayout(suffixPanel, BoxLayout.Y_AXIS));
        suffixPanel.add(new JLabel(getBundleString("suffixes")));

        prefixList = new JPanel() {
            Insets insets = new Insets(0, 4, 0, 0);
            public Insets getInsets() {
                return insets;
            }
        };
        prefixList.setLayout(new BoxLayout(prefixList, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(prefixList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        prefixPanel.add(scrollPane);
        prefixPanel.add(Box.createRigidArea(HGAP10));

        suffixList = new JPanel() {
            Insets insets = new Insets(0, 4, 0, 0);
            public Insets getInsets() {
                return insets;
            }
        };
        suffixList.setLayout(new BoxLayout(suffixList, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(suffixList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        suffixPanel.add(scrollPane);
        suffixPanel.add(Box.createRigidArea(HGAP10));

        controlPanel.add(prefixPanel);
        controlPanel.add(Box.createRigidArea(HGAP15));
        controlPanel.add(suffixPanel);
        return controlPanel;
    }

    private FocusListener listFocusListener = new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            JComponent c = (JComponent)e.getComponent();
            c.scrollRectToVisible(new Rectangle(0, 0, c.getWidth(), c.getHeight()));
        }
    };

    private void addPrefix(String prefix, boolean selected) {
        if(prefixAction == null) {
            prefixAction = new UpdatePrefixListAction(listModel);
        }
        final JCheckBox cb = (JCheckBox) prefixList.add(new JCheckBox(prefix));
        checkboxes.addElement(cb);
        cb.setSelected(selected);
        cb.addActionListener(prefixAction);
        if(selected) {
            listModel.addPrefix(prefix);
        }
        cb.addFocusListener(listFocusListener);
    }

    private void addSuffix(String suffix, boolean selected) {
        if(suffixAction == null) {
            suffixAction = new UpdateSuffixListAction(listModel);
        }
        final JCheckBox cb = (JCheckBox) suffixList.add(new JCheckBox(suffix));
        checkboxes.addElement(cb);
        cb.setSelected(selected);
        cb.addActionListener(suffixAction);
        if(selected) {
            listModel.addSuffix(suffix);
        }
        cb.addFocusListener(listFocusListener);
    }

    class UpdatePrefixListAction extends AbstractAction {
        GeneratedListModel listModel;
        protected UpdatePrefixListAction(GeneratedListModel listModel) {
            this.listModel = listModel;
        }

        public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox) e.getSource();
            if(cb.isSelected()) {
                listModel.addPrefix(cb.getText());
            } else {
                listModel.removePrefix(cb.getText());
            }
        }
    }

    class UpdateSuffixListAction extends AbstractAction {
        GeneratedListModel listModel;
        protected UpdateSuffixListAction(GeneratedListModel listModel) {
            this.listModel = listModel;
        }

        public void actionPerformed(ActionEvent e) {
            JCheckBox cb = (JCheckBox) e.getSource();
            if(cb.isSelected()) {
                listModel.addSuffix(cb.getText());
            } else {
                listModel.removeSuffix(cb.getText());
            }
        }
    }


    class GeneratedListModel extends AbstractListModel {
        ListDemo demo;
        Permuter permuter;

        public Vector prefix = new Vector();
        public Vector suffix = new Vector();

        public GeneratedListModel (ListDemo demo) {
            this.demo = demo;
        }

        private void update() {
            permuter = new Permuter(getSize());
            fireContentsChanged(this, 0, getSize());
        }

        public void addPrefix(String s) {
            if(!prefix.contains(s)) {
                prefix.addElement(s);
                update();
            }
        }

        public void removePrefix(String s) {
            prefix.removeElement(s);
            update();
        }

        public void addSuffix(String s) {
            if(!suffix.contains(s)) {
                suffix.addElement(s);
                update();
            }
        }

        public void removeSuffix(String s) {
            suffix.removeElement(s);
            update();
        }

        public int getSize() {
            return prefix.size() * suffix.size();
        }

        public Object getElementAt(int index) {
            if(permuter == null) {
                update();
            }
            // morph the index to another int -- this has the benefit of
            // causing the list to look random.
            int j = permuter.map(index);
            int ps = prefix.size();
            int ss = suffix.size();
            return (String) prefix.elementAt(j%ps) + (String) suffix.elementAt((j/ps)%ss);
        }
    }

    ImageIcon images[] = new ImageIcon[7];
    void loadImages() {
            images[0] = StaticUtilities.createImageIcon("list/red.gif");
            images[1] = StaticUtilities.createImageIcon("list/blue.gif");
            images[2] = StaticUtilities.createImageIcon("list/yellow.gif");
            images[3] = StaticUtilities.createImageIcon("list/green.gif");
            images[4] = StaticUtilities.createImageIcon("list/gray.gif");
            images[5] = StaticUtilities.createImageIcon("list/cyan.gif");
            images[6] = StaticUtilities.createImageIcon("list/magenta.gif");
    }

    class CompanyLogoListCellRenderer extends DefaultListCellRenderer {
       public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            Component retValue = super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus
            );
            setIcon(images[index%7]);
            return retValue;
        }
    }
}
