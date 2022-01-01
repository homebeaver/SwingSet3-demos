/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * JTree Demo
 *
 * @author Jeff Dinkins
 */
public class TreeDemo extends DemoModule {

	private static final long serialVersionUID = -932066695707626601L;

	public static final String ICON_PATH = "toolbar/JTree.gif";

    JTree tree;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        TreeDemo demo = new TreeDemo(null);
        demo.mainImpl();
    }

    /**
     * TreeDemo Constructor
     */
    public TreeDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
        super(swingset, "TreeDemo", ICON_PATH);

        getDemoPanel().add(createTree(), BorderLayout.CENTER);
    }

    public JScrollPane createTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(getString("TreeDemo.music"));
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

        tree = new JTree(top) {
            public Insets getInsets() {
                return new Insets(5,5,5,5);
            }
        };

        tree.setEditable(true);

        return new JScrollPane(tree);
    }

    void updateDragEnabled(boolean dragEnabled) {
        tree.setDragEnabled(dragEnabled);
    }

}
