/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXFrame.StartPosition;

/**
 * JFileChooserDemo
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class FileChooserDemo extends AbstractDemo {

	/*
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JFileChooser.gif";
	
	private static final long serialVersionUID = 3758119595406064166L;
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = false;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new FileChooserDemo(controller);
				JXFrame frame = new JXFrame("demo", exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    JLabel theImage;

    /**
     * FileChooserDemo Constructor
     */
    public FileChooserDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        jpgIcon = StaticUtilities.createImageIcon("filechooser/jpgIcon.jpg");
        gifIcon = StaticUtilities.createImageIcon("filechooser/gifIcon.gif");
        pngIcon = StaticUtilities.createImageIcon("filechooser/pngIcon.gif");
        
        if(CONTROLLER_IN_PRESENTATION_FRAME) {
            super.add(getBoxPane(), BorderLayout.NORTH);
        }

        theImage = new JLabel("");

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));

        super.add(innerPanel, BorderLayout.CENTER);

        // Create a panel to hold the image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JScrollPane scroller = new JScrollPane(theImage);
        scroller.getVerticalScrollBar().setUnitIncrement(10);
        scroller.getHorizontalScrollBar().setUnitIncrement(10);
        imagePanel.add(scroller, BorderLayout.CENTER);

        innerPanel.add(imagePanel);
    }
    
    JDialog dialog;
    JFileChooser fc;

    Icon jpgIcon;
    Icon gifIcon;
    Icon pngIcon;

    @Override
	public JXPanel getControlPane() {
        if(CONTROLLER_IN_PRESENTATION_FRAME) return emptyControlPane();
    	
        JXPanel controller = new JXPanel();
        controller.add(getBoxPane());
    	return controller;
    }

    private JPanel getBoxPane() {
    	JPanel boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.X_AXIS));

        // Create a panel to hold buttons
        @SuppressWarnings("serial")
        JPanel buttonPanel = new JPanel() {
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(Box.createRigidArea(VGAP30));

        buttonPanel.add(createPlainFileChooserButton());	buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createPreviewFileChooserButton());	buttonPanel.add(Box.createRigidArea(VGAP15));
        buttonPanel.add(createCustomFileChooserButton());	buttonPanel.add(Box.createRigidArea(VGAP30));
        buttonPanel.add(Box.createVerticalGlue());

        boxPane.add(Box.createHorizontalGlue());
        boxPane.add(buttonPanel);
        boxPane.add(Box.createHorizontalGlue());
        
        return boxPane;
    }

    public JFileChooser createFileChooser() {
        // create a filechooser
        JFileChooser fc = new JFileChooser();
//        if (getSwingSet2() != null && getSwingSet2().isDragEnabled()) {
//            fc.setDragEnabled(true);
//        }

        // set the current directory to be the images directory
        File swingFile = new File("resources/images/About.jpg");
        if(swingFile.exists()) {
            fc.setCurrentDirectory(swingFile);
            fc.setSelectedFile(swingFile);
        }

        return fc;
    }

    // controller
    private JButton createPlainFileChooserButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("plainbutton")) {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();

                // show the filechooser
                int result = fc.showOpenDialog(FileChooserDemo.this);

                // if we selected an image, load the image
                if(result == JFileChooser.APPROVE_OPTION) {
                    loadImage(fc.getSelectedFile().getPath());
                }
            }
        };
        // See protected void BasicFileChooserUI#installIcons(JFileChooser fc)
        Icon icon = UIManager.getIcon("FileView.fileIcon");
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    // controller
    private JButton createPreviewFileChooserButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("previewbutton")) {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();

                // Add filefilter & fileview
                javax.swing.filechooser.FileFilter filter = createFileFilter(
                    getBundleString("filterdescription"), "jpg", "gif", "png");
                ExampleFileView fileView = new ExampleFileView();
                fileView.putIcon("jpg", jpgIcon);
                fileView.putIcon("gif", gifIcon);
                fileView.putIcon("png", pngIcon);
                fc.setFileView(fileView);
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);

                // add preview accessory
                fc.setAccessory(new FilePreviewer(fc));

                // show the filechooser
                int result = fc.showOpenDialog(FileChooserDemo.this);

                // if we selected an image, load the image
                if(result == JFileChooser.APPROVE_OPTION) {
                    loadImage(fc.getSelectedFile().getPath());
                }
            }
        };
//        Icon icon = UIManager.getIcon("FileChooser.viewMenuIcon"); // null ? detailsViewIcon
//        Icon icon = UIManager.getIcon("FileChooser.listViewIcon"); 
//        Icon icon = UIManager.getIcon("FileChooser.detailsViewIcon"); 
        Icon icon = StaticUtilities.createImageIcon("filechooser/gifIcon.gif");
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    private javax.swing.filechooser.FileFilter createFileFilter(String description, String...extensions) {
        description = createFileNameFilterDescriptionFromExtensions(description, extensions);
        return new FileNameExtensionFilter(description, extensions);
    }

    private String createFileNameFilterDescriptionFromExtensions(
            String description, String[] extensions) {
        String fullDescription = (description == null) ?
                "(" : description + " (";
        // build the description from the extension list
        fullDescription += "." + extensions[0];
        for (int i = 1; i < extensions.length; i++) {
            fullDescription += ", .";
            fullDescription += extensions[i];
        }
        fullDescription += ")";
        return fullDescription;
    }

    // controller
    private JButton createCustomFileChooserButton() {
        @SuppressWarnings("serial")
        Action a = new AbstractAction(getBundleString("custombutton")) {
            public void actionPerformed(ActionEvent e) {
                fc = createFileChooser();

                // Add filefilter & fileview
                javax.swing.filechooser.FileFilter filter = createFileFilter(
                    getBundleString("filterdescription"), "jpg", "gif", "png");
                ExampleFileView fileView = new ExampleFileView();
                fileView.putIcon("jpg", jpgIcon);
                fileView.putIcon("gif", gifIcon);
                fileView.putIcon("png", pngIcon);
                fc.setFileView(fileView);
                fc.addChoosableFileFilter(filter);

                // add preview accessory
                fc.setAccessory(new FilePreviewer(fc));

                // remove the approve/cancel buttons
                fc.setControlButtonsAreShown(false);

                // make custom controls
                //wokka
                JPanel custom = new JPanel();
                custom.setLayout(new BoxLayout(custom, BoxLayout.Y_AXIS));
                custom.add(Box.createRigidArea(VGAP10));
                JLabel description = new JLabel(getBundleString("description"));
                description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                custom.add(description);
                custom.add(Box.createRigidArea(VGAP10));
                custom.add(fc);

                Action okAction = createOKAction();
                fc.addActionListener(okAction);

                JPanel buttons = new JPanel();
                buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
                buttons.add(Box.createRigidArea(HGAP10));
                buttons.add(createImageButton(createFindAction()));
                buttons.add(Box.createRigidArea(HGAP10));
                buttons.add(createButton(createAboutAction()));
                buttons.add(Box.createRigidArea(HGAP10));
                buttons.add(createButton(okAction));
                buttons.add(Box.createRigidArea(HGAP10));
                buttons.add(createButton(createCancelAction()));
                buttons.add(Box.createRigidArea(HGAP10));
                buttons.add(createImageButton(createHelpAction()));
                buttons.add(Box.createRigidArea(HGAP10));

                custom.add(buttons);
                custom.add(Box.createRigidArea(VGAP10));

                // show the filechooser
                Frame parent = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, FileChooserDemo.this);
                dialog = new JDialog(parent, getBundleString("dialogtitle"), true);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(custom, BorderLayout.CENTER);
                dialog.pack();
                dialog.setLocationRelativeTo(FileChooserDemo.this);
//              dialog.show(); // Deprecated.  As of JDK version 1.5, replaced by setVisible(boolean).
                dialog.setVisible(true);
            }
        };
        Icon icon = StaticUtilities.createImageIcon("filechooser/find.gif");
        a.putValue(Action.LARGE_ICON_KEY, icon);
        return createButton(a);
    }

    public Action createAboutAction() {
        return new AbstractAction(getBundleString("about")) {
            public void actionPerformed(ActionEvent e) {
                File file = fc.getSelectedFile();
                String text;
                if(file == null) {
                    text = getBundleString("nofileselected");
                } else {
                    text = "<html>" + getBundleString("thefile");
                    text += "<br><font color=green>" + file.getName() + "</font><br>";
                    text += getBundleString("isprobably") + "</html>";
                }
                JOptionPane.showMessageDialog(FileChooserDemo.this, text);
            }
        };
    }

    public Action createOKAction() {
        return new AbstractAction(getBundleString("ok")) {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                if (!e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)
                    && fc.getSelectedFile() != null) {

                    loadImage(fc.getSelectedFile().getPath());
                }
            }
        };
    }

    public Action createCancelAction() {
        return new AbstractAction(getBundleString("cancel")) {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };
    }

    public Action createFindAction() {
        Icon icon = StaticUtilities.createImageIcon("filechooser/find.gif");
        return new AbstractAction("", icon) {
            public void actionPerformed(ActionEvent e) {
                String result = JOptionPane.showInputDialog(FileChooserDemo.this, getBundleString("findquestion"));
                if (result != null) {
                    JOptionPane.showMessageDialog(FileChooserDemo.this, getBundleString("findresponse"));
                }
            }
        };
    }

    public Action createHelpAction() {
        Icon icon = StaticUtilities.createImageIcon("filechooser/help.gif");
        return new AbstractAction("", icon) {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(FileChooserDemo.this, getBundleString("helptext"));
            }
        };
    }

    class MyImageIcon extends ImageIcon {
        public MyImageIcon(String filename) {
            super(filename);
        };
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.white);
            g.fillRect(0,0, c.getWidth(), c.getHeight());
            if(getImageObserver() == null) {
                g.drawImage(
                    getImage(),
                    c.getWidth()/2 - getIconWidth()/2,
                    c.getHeight()/2 - getIconHeight()/2,
                    c
                );
            } else {
                g.drawImage(
                    getImage(),
                    c.getWidth()/2 - getIconWidth()/2,
                    c.getHeight()/2 - getIconHeight()/2,
                    getImageObserver()
                );
            }
        }
    }

    public void loadImage(String filename) {
        theImage.setIcon(new MyImageIcon(filename));
    }

    public JButton createButton(Action a) {
        JButton b = new JButton(a) {
            public Dimension getMaximumSize() {
                int width = Short.MAX_VALUE;
                int height = super.getMaximumSize().height;
                return new Dimension(width, height);
            }
        };
        return b;
    }

    public JButton createImageButton(Action a) {
        JButton b = new JButton(a);
        b.setMargin(new Insets(0,0,0,0));
        return b;
    }
}

class FilePreviewer extends JComponent implements PropertyChangeListener {
    ImageIcon thumbnail = null;

    public FilePreviewer(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

    public void loadImage(File f) {
        if (f == null) {
            thumbnail = null;
        } else {
            ImageIcon tmpIcon = new ImageIcon(f.getPath());
            if(tmpIcon.getIconWidth() > 90) {
                thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
            } else {
                thumbnail = tmpIcon;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
            if(isShowing()) {
                loadImage((File) e.getNewValue());
                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if(thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;
            if(y < 0) {
                y = 0;
            }

            if(x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}
