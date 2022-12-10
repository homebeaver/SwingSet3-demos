/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package org.jdesktop.swingx.demos.xlist;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JYList;

import swingset.plaf.LaFUtils;

/* ListDemo.java requires no other files. 
 * copied from https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ListDemo
 */
@SuppressWarnings("serial")
public class ListDemo extends JPanel implements ListSelectionListener {

    private static final Logger LOG = Logger.getLogger(ListDemo.class.getName());

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
    	LaFUtils.setLAF(args[0]);
        SwingUtilities.invokeLater( () -> {
            createAndShowGUI();
        });
    }

    /**
     * Create the GUI and show it.
     * For thread safety, this method should be invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ListDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private JList<String> list;
    private DefaultListModel<String> listModel;

    private static final String hireString = "Hire";
    private static final String fireString = "Fire";
    private JButton fireButton;
    private JTextField employeeName;

    public ListDemo() {
        super(new BorderLayout());

        listModel = new DefaultListModel<String>();
        listModel.addElement("Jane Doe");
        listModel.addElement("John Smith");
        listModel.addElement("Kathy Green");
        listModel.addElement("Mark Andrews");
        listModel.addElement("Tom Ball");
        listModel.addElement("Alan Chung");
        listModel.addElement("Jeff Dinkins");
        listModel.addElement("Amy Fowler");
        listModel.addElement("Brian Gerhold");
        listModel.addElement("James Gosling");
        listModel.addElement("David Karlton");
        listModel.addElement("Dave Kloba");
        listModel.addElement("Peter Korn");
        listModel.addElement("Phil Milne");
        listModel.addElement("Dave Moore");
        listModel.addElement("Hans Muller");
        listModel.addElement("Jane Doe"); // add this duplicate to show that JList allows it

        //Create the list and put it in a scroll pane.
        list = new JYList<String>(listModel);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP); // default is VERTICAL
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton hireButton = new JButton(hireString);
        HireListener hireListener = new HireListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(hireListener);
        hireButton.setEnabled(false);

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.addActionListener(new FireListener());

        employeeName = new JTextField(10);
        employeeName.addActionListener(hireListener);
        employeeName.getDocument().addDocumentListener(hireListener);
//        String name = listModel.getElementAt(
//                              list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(employeeName);
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                fireButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = employeeName.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            listModel.insertElementAt(employeeName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                fireButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                fireButton.setEnabled(true);
            }
        }
    }
/*
    class MyListCellRenderer extends DefaultListCellRenderer {
        public MyListCellRenderer() {
            super();
            setOpaque(true);
            setBorder(getNoFocusBorder());
            setName("List.cellRenderer");
        }
        private Border getNoFocusBorder() {
        	LOG.info("use EtchedBorder ...");
        	return new EtchedBorder();
//            Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");
//            if (System.getSecurityManager() != null) {
//                if (border != null) return border;
//                return SAFE_NO_FOCUS_BORDER;
//            } else {
//                if (border != null &&
//                        (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
//                    return border;
//                }
//                return noFocusBorder;
//            }
        }

        @Override // implements public interface ListCellRenderer<E>
		public Component getListCellRendererComponent(JList<?> list, Object value
				, int index, boolean isSelected, boolean cellHasFocus) {
			
			Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (cellHasFocus) {
				if (isSelected) {
					LOG.info("list.getBackground()"+list.getBackground()+"cellHasFocus+isSelected >>>>>TODO"); //TODO "List.focusSelectedCellHighlightBorder"
				}
				LOG.info("cellHasFocus+isNOTSelected >>>>>TODO"); //TODO "List.focusCellHighlightBorder"
			}
			setBorder(getNoFocusBorder());
			return comp;
		}
        // original in super: not accessible: sun.swing.DefaultLookup 
//			setComponentOrientation(list.getComponentOrientation());
//
//			Color bg = null;
//			Color fg = null;
//
//			JList.DropLocation dropLocation = list.getDropLocation();
//			if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
//
//				bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
//				fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");
//
//				isSelected = true;
//			}
//
//			if (isSelected) {
//				setBackground(bg == null ? list.getSelectionBackground() : bg);
//				setForeground(fg == null ? list.getSelectionForeground() : fg);
//			} else {
//				setBackground(list.getBackground());
//				setForeground(list.getForeground());
//			}
//
//			if (value instanceof Icon) {
//				setIcon((Icon) value);
//				setText("");
//			} else {
//				setIcon(null);
//				setText((value == null) ? "" : value.toString());
//			}
//
//			setEnabled(list.isEnabled());
//			setFont(list.getFont());
//
//			Border border = null;
//			if (cellHasFocus) {
//				if (isSelected) {
//					border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
//				}
//				if (border == null) {
//					border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
//				}
//			} else {
//				border = getNoFocusBorder();
//			}
//			setBorder(border);
//
//			return this;
//		}

    }
 */
}
