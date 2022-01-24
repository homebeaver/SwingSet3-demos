/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;

/**
 * JTree Demo
 *
 * @author Jeff Dinkins (inception)
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class TreeDemo extends AbstractDemo {

	public static final String ICON_PATH = "toolbar/JTree.gif";

	private static final long serialVersionUID = -932066695707626601L;

    JXTree tree;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) { // TODO
        TreeDemo demo = new TreeDemo(null);
//        demo.mainImpl();
    }

    /**
     * TreeDemo Constructor
     */
    public TreeDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(new Dimension(PREFERRED_WIDTH/2, PREFERRED_HEIGHT));
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
        super.add(new JScrollPane(createTree()), BorderLayout.CENTER);
    }

    private JXButton expandButton;
    private JXButton collapseButton;

    @Override
	public JXPanel getControlPane() {
		JXPanel buttons = new JXPanel();

		// <snip> JXTree convenience api
		expandButton = new JXButton(getString("expandAll"));
		expandButton.setName("expandButton");
		expandButton.addActionListener(ae -> {
			tree.expandAll();
		});
		buttons.add(expandButton);
		// </snip>

		collapseButton = new JXButton(getString("collapseAll"));
		collapseButton.setName("collapseButton");
		collapseButton.addActionListener(ae -> {
			tree.collapseAll();
		});
		buttons.add(collapseButton);

		return buttons;
	}

    private JXTree createTree() {
    	
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getString("music"));
        DefaultMutableTreeNode catagory = null ;
        DefaultMutableTreeNode artist = null;
        DefaultMutableTreeNode record = null;

        // open tree data
        URL url = getClass().getResource("/swingset/tree.txt");

        try {
            // convert url to buffered string
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);

            // read one line at a time, put into tree
            String line = reader.readLine();
            while(line != null) {
                // System.out.println("reading in: ->" + line + "<-");
                char linetype = line.charAt(0);
                switch(linetype) {
                   case 'C':
                     catagory = new DefaultMutableTreeNode(line.substring(2));
                     top.add(catagory);
                     break;
                   case 'A':
                     if(catagory != null) {
                         catagory.add(artist = new DefaultMutableTreeNode(line.substring(2)));
                     }
                     break;
                   case 'R':
                     if(artist != null) {
                         artist.add(record = new DefaultMutableTreeNode(line.substring(2)));
                     }
                     break;
                   case 'S':
                     if(record != null) {
                         record.add(new DefaultMutableTreeNode(line.substring(2)));
                     }
                     break;
                   default:
                     break;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }

        tree = new JXTree(top) {
            public Insets getInsets() {
                return new Insets(5,5,5,5);
            }
        };

        tree.setEditable(true);
        return tree;
    }

    void updateDragEnabled(boolean dragEnabled) {
        tree.setDragEnabled(dragEnabled);
    }

}
