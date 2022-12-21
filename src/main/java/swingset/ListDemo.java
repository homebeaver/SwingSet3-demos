/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JYList;

/**
 * List Demo. This demo shows that it is not always necessary to have an array of objects
 * as big as the size of the list stored.
 *
 * Indeed, in this example, there is no array kept for the list data, rather it is generated
 * on the fly as only those elements are needed.
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (cellsLayout, selectionMode)
 */
public class ListDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JList.gif";
	
	private static final long serialVersionUID = -6590141127414585946L;
	private static final String DESCRIPTION = "JList Demo";
    private static final Logger LOG = Logger.getLogger(ListDemo.class.getName());
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;

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

    JList<Object> list;

    // layout of cells
    private static String[] CELLS_LAYOUT = 
    	{ "VERTICAL - a single column" 
    	, "VERTICAL_WRAP - a \"newspaper style\" flowing vertically then horizontally" 
    	, "HORIZONTAL_WRAP - newspaper style flowing horizontally then vertically" // initial
    	};
    private JComboBox<String> cellsLayout;

    // selection mode
    private static String[] SELECTION_MODE = 
    	{ "SINGLE_SELECTION - select one list index at a time" 
    	, "SINGLE_INTERVAL_SELECTION - select one contiguous range" 
    	, "MULTIPLE_INTERVAL_SELECTION - select one or more contiguous ranges"
    	};
    private JComboBox<String> selectionMode;
    
    // drop mode
    private static String[] DROP_MODE = 
    	{ "USE_SELECTION - item moves to echo the potential drop point (not recommended)" 
    	, "ON - used to drop on top of existing list items" 
    	, "INSERT - select the space between existing list items"
    	, "ON_OR_INSERT - a combination of the ON and INSERT"
    	};
    private JComboBox<String> dropMode;
    
    JPanel prefixList;
    JPanel suffixList;

    Action prefixAction;
    Action suffixAction;

    GeneratedListModel<?> listModel;

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
        } else {
            String text = "Drag from or drop into this area.\nThe default action is MOVE;"
            		+"\nhold the Control key to COPY.";          
            JTextArea area = new JTextArea();
            area.setText(text);
            area.setDragEnabled(true);
            super.add(area, BorderLayout.NORTH);
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
        centerPanel.add(Box.createRigidArea(HGAP10));

        // Create the list
        list = new JYList<>();
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP); // default is VERTICAL
        list.setCellRenderer(new CompanyLogoListCellRenderer());
        listModel = new GeneratedListModel<>(this);
        list.setModel(listModel);

        // Set the preferred row count. This affects the preferredSize of the JList when it's in a scrollpane.
        // In HORIZONTAL_WRAP and VERTICAL_WRAP orientations affects how cells are wrapped.
        list.setVisibleRowCount(10);
        
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // default is SINGLE_SELECTION

        // Add list to a scrollpane
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);
        listPanel.add(Box.createRigidArea(VGAP10));

        if(CONTROLLER_IN_PRESENTATION_FRAME) {
            // Add the control panel (holds the prefix/suffix list and prefix/suffix checkboxes)
            centerPanel.add(createControlPanel());
            createPrefixesAndSuffixes();
        }

        LOG.info("list.DragEnabled="+list.getDragEnabled());
        list.setDragEnabled(true);
        list.setTransferHandler(new ListTransferHandler());
        /*
JList supports the following drop modes: 
• DropMode.USE_SELECTION
• DropMode.ON
• DropMode.INSERT
• DropMode.ON_OR_INSERT
The drop mode is only meaningful if this component has a TransferHandler that accepts drops.

Sets the drop mode for this component. 
For backward compatibility,the default for this property is DropMode.USE_SELECTION (not recommended).
Usage of one of the other modes is recommended, however, for an improved user experience. 
DropMode.ON, for instance, offers similar behavior of showing items as selected, 
but does so without affecting the actual selection in the list. 
         */
    	list.setDropMode(DropMode.ON);
        
    }

    private void createPrefixesAndSuffixes() {
        addPrefix("Tera", true);
        addPrefix("Micro", false);
//        addPrefix("Southern", false);
        addPrefix("Net", true);
        addPrefix("YoYo", true);
//        addPrefix("Northern", false);
        addPrefix("Tele", false);
//        addPrefix("Eastern", false);
        addPrefix("Neo", false);
        addPrefix("Digi", false);
        addPrefix("National", false);
        addPrefix("Compu", true);
        addPrefix("Meta", true);
        addPrefix("Info", false);
//        addPrefix("Western", false);
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
//        addSuffix("Telecom", true);
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
//        addSuffix("Com", false);
        addSuffix("Ware", false);
//        addSuffix("Widgets", false);
        addSuffix("Media", false);
        addSuffix("Computer", false);
        addSuffix("Hardware", false);
//        addSuffix("Gizmos", false);
        addSuffix("Concepts", false);
    }
    @Override
	public JXPanel getControlPane() {
        if(CONTROLLER_IN_PRESENTATION_FRAME) return emptyControlPane();

        JXPanel descPanel = new JXPanel(new BorderLayout());
        JXLabel description = new JXLabel(getBundleString("xdescription"));
        description.setLineWrap(true);
        descPanel.add(description, BorderLayout.NORTH);
        
        JXPanel controller = new JXPanel(new BorderLayout());
        controller.add(descPanel, BorderLayout.NORTH);
        
        JXPanel py = new JXPanel();
        py.setLayout(new BoxLayout(py, BoxLayout.Y_AXIS));
        py.add(Box.createRigidArea(VGAP10));
        descPanel.add(py, BorderLayout.CENTER);
        
        JXPanel px = new JXPanel();
        px.setLayout(new BoxLayout(px, BoxLayout.X_AXIS));
        py.add(px);
     
        // layout of cells
        px.add(Box.createRigidArea(HGAP10));
        px.add(new JLabel(getBundleString("cellsLayout")));
        cellsLayout = new JComboBox<String>(CELLS_LAYOUT);
        cellsLayout.setSelectedIndex(2);
        px.add(cellsLayout);
        px.add(Box.createRigidArea(HGAP10));
        cellsLayout.addActionListener(actionEvent -> {
        	cellsLayout.setSelectedIndex(cellsLayout.getSelectedIndex());
        	list.setLayoutOrientation(cellsLayout.getSelectedIndex());
        });
        
        // selection mode
        px = new JXPanel();
        px.setLayout(new BoxLayout(px, BoxLayout.X_AXIS));
        py.add(px);
        px.add(Box.createRigidArea(HGAP10));
        px.add(new JLabel(getBundleString("selection mode")));
        selectionMode = new JComboBox<String>(SELECTION_MODE);
        selectionMode.setSelectedIndex(ListSelectionModel.SINGLE_INTERVAL_SELECTION); // default is SINGLE_SELECTION
        px.add(selectionMode);
        px.add(Box.createRigidArea(HGAP10));
        selectionMode.addActionListener(actionEvent -> {
        	selectionMode.setSelectedIndex(selectionMode.getSelectedIndex());
        	list.setSelectionMode(selectionMode.getSelectedIndex());
        });
        
        // drop mode
        px = new JXPanel();
        px.setLayout(new BoxLayout(px, BoxLayout.X_AXIS));
        py.add(px);
        px.add(Box.createRigidArea(HGAP10));
        px.add(new JLabel(getBundleString("drop mode")));
        dropMode = new JComboBox<String>(DROP_MODE);
        dropMode.setSelectedIndex(1); // DropMode.ON
        px.add(dropMode);
        px.add(Box.createRigidArea(HGAP10));
        dropMode.addActionListener(actionEvent -> {
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
        
        // Add the control panel (holds the prefix/suffix list and prefix/suffix checkboxes)
        controller.add(createControlPanel(), BorderLayout.CENTER);
        createPrefixesAndSuffixes();

    	return controller;
    }

    private JPanel createControlPanel() {
        @SuppressWarnings("serial")
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

    @SuppressWarnings("serial")
	class UpdatePrefixListAction extends AbstractAction {
        GeneratedListModel<?> listModel;
        protected UpdatePrefixListAction(GeneratedListModel<?> listModel) {
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

    @SuppressWarnings("serial")
    class UpdateSuffixListAction extends AbstractAction {
        GeneratedListModel<?> listModel;
        protected UpdateSuffixListAction(GeneratedListModel<?> listModel) {
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


    @SuppressWarnings("serial")
	class GeneratedListModel<E> extends AbstractListModel<Object> {
        ListDemo demo;
        Permuter permuter;

        public Vector<String> prefix = new Vector<String>();
        public Vector<String> suffix = new Vector<String>();
        public Vector<String> nix = new Vector<String>();

        public GeneratedListModel (ListDemo demo) {
            this.demo = demo;
        }

        private void update() {
            permuter = new Permuter(getSize()-nix.size());
            /*
             * call this method after one or more elements of the list change.  
             * The changed elements are specified by the closed interval index0, index1.
             * @param source the <code>ListModel</code> that changed, typically "this"
             * @param index0 one end of the new interval
             * @param index1 the other end of the new interval
             */
            fireContentsChanged(this, 0, getSize());
        }

        public void add(int index, Object element) {
        	if(element instanceof String s) {
                if(!nix.contains(s)) {
                	// ignore index
//         delegate.insertElementAt(element, index);

                    nix.addElement(s);
                    update();
                }
        	}
        }
        
        public void addPrefix(String s) {
            if(!prefix.contains(s)) {
                prefix.addElement(s);
                LOG.info(s + " at prefix."+prefix.size());
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
                LOG.info(s + " at suffix."+suffix.size());
                update();
            }
        }

        public void removeSuffix(String s) {
            suffix.removeElement(s);
            update();
        }

        public int getSize() {
            return (prefix.size() * suffix.size()) + nix.size();
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

	@SuppressWarnings("serial")
	class CompanyLogoListCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value
				, int index, boolean isSelected, boolean cellHasFocus) {
			Component retValue = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setIcon(images[index % 7]);
			
			return retValue;
		}
	}
	
	class ListTransferHandler extends TransferHandler {
		
	    private int[] indices = null;
	    private int addIndex = -1; //Location where items were added
	    private int addCount = 0;  //Number of items added.
	            
	    public boolean canImport(TransferHandler.TransferSupport info) {
	        // Check for String flavor
	        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            return false;
	        }
	        return true;
	   }

	    protected Transferable createTransferable(JComponent c) {
	    	String s = exportString(c);
	    	LOG.info("exportString="+s);
	        return new StringSelection(exportString(c));
	    }
	    
	    // @param c  the component holding the data to be transferred, here JYList
	    public int getSourceActions(JComponent c) {
	    	LOG.info("-----------JComponent "+c);
//	        return TransferHandler.COPY_OR_MOVE;
	        return TransferHandler.COPY;
	    }
	    
	    public boolean importData(TransferHandler.TransferSupport info) {
	    	LOG.info("-----------TransferHandler.TransferSupport "+info);
	        if (!info.isDrop()) {
	            return false;
	        }

	        JList<Object> list = (JList<Object>)info.getComponent();
	        GeneratedListModel<Object> listModel = (GeneratedListModel<Object>)list.getModel();
	        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
	        int index = dl.getIndex();
	        boolean insert = dl.isInsert();
	        LOG.fine("DropLocation.Index="+index + " insert="+insert);
	        // Get the string that is being dropped.
	        Transferable t = info.getTransferable();
	        String data;
	        try {
	            data = (String)t.getTransferData(DataFlavor.stringFlavor);
	        } 
	        catch (Exception e) { return false; }
	                                
	        LOG.info("DropLocation.Index="+index + " insert="+insert + " data:"+data);
	        // Perform the actual import.  
	        if (insert) {
	            listModel.add(index, data);
	        } else {
	        	LOG.info("Drop! ***NOT IMPLEMENTED*** set "+data+" at "+index);
//	            listModel.set(index, data);
	        }
	        return true;
	    }

	    protected void exportDone(JComponent c, Transferable data, int action) {
	    	if(data instanceof StringSelection ss) {
	    		try {
	    			LOG.info("data:"+ss.getTransferData(DataFlavor.stringFlavor));
				} catch (UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
	    	}
	        cleanup(c, action == TransferHandler.MOVE);
	    }

	    //Bundle up the selected items in the list
	    //as a single string, for export.
	    protected String exportString(JComponent c) {
	        JList<?> list = (JList<?>)c;
	        indices = list.getSelectedIndices();
//	        Object[] values = list.getSelectedValues(); // deprecated As of JDK 1.7
	        List<?> l = list.getSelectedValuesList();
	        Object[] values = new Object[l.size()];
	        l.toArray(values); // fill the array
	        
	        StringBuffer buff = new StringBuffer();

	        for (int i = 0; i < values.length; i++) {
	            Object val = values[i];
	            buff.append(val == null ? "" : val.toString());
	            if (i != values.length - 1) {
	                buff.append("\n");
	            }
	        }
	        
	        return buff.toString();
	    }

	    //Take the incoming string and wherever there is a
	    //newline, break it into a separate item in the list.
	    protected void importString(JComponent c, String str) {
	    	LOG.info("+++ "+str);
	        JList<Object> target = (JList<Object>)c;
	        GeneratedListModel<Object> listModel = (GeneratedListModel<Object>)target.getModel();
	        int index = target.getSelectedIndex();

	        //Prevent the user from dropping data back on itself.
	        //For example, if the user is moving items #4,#5,#6 and #7 and
	        //attempts to insert the items after item #5, this would
	        //be problematic when removing the original items.
	        //So this is not allowed.
	        if (indices != null && index >= indices[0] - 1 &&
	              index <= indices[indices.length - 1]) {
	            indices = null;
	            return;
	        }

	        int max = listModel.getSize();
	        if (index < 0) {
	            index = max;
	        } else {
	            index++;
	            if (index > max) {
	                index = max;
	            }
	        }
	        addIndex = index;
	        String[] values = str.split("\n");
	        addCount = values.length;
	        for (int i = 0; i < values.length; i++) {
	            listModel.add(index++, values[i]);
	        }
	    }
	    
	    //If the remove argument is true, the drop has been
	    //successful and it's time to remove the selected items 
	    //from the list. If the remove argument is false, it
	    //was a Copy operation and the original list is left
	    //intact.
	    protected void cleanup(JComponent c, boolean remove) {
	        if(remove && indices != null) {
	            JList<Object> source = (JList<Object>)c;
	            LOG.info("indices#="+indices.length+" JComponent "+source);
	            GeneratedListModel<Object> model  = (GeneratedListModel<Object>)source.getModel();
	            //If we are moving items around in the same list, we
	            //need to adjust the indices accordingly, since those
	            //after the insertion point have moved.
	            if (addCount > 0) {
	                for (int i = 0; i < indices.length; i++) {
	                    if (indices[i] > addIndex) {
	                        indices[i] += addCount;
	                    }
	                }
	            }
	            for (int i = indices.length - 1; i >= 0; i--) {
	            	LOG.info("TODO model.remove i="+i + " indices[i]="+indices[i]); // TODO
//	                model.remove(indices[i]);
	            }
	        } else if(indices != null) {
	            JList<Object> source = (JList<Object>)c;
	            GeneratedListModel<Object> model  = (GeneratedListModel<Object>)source.getModel();
	            for (int i = indices.length - 1; i >= 0; i--) {
	            	LOG.info("***NOT IMPLEMENTED*** model.remove i="+i 
	            			+ " indices[i]="+indices[i] + " element:"+model.getElementAt(indices[i]));
//	                model.remove(indices[i]);
	            }
	        }
	        indices = null;
	        addCount = 0;
	        addIndex = -1;
	    }
	}

}
