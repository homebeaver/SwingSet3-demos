/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.hyperlink;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.action.OpenBrowserAction;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.hyperlink.HyperlinkAction;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.HyperlinkProvider;
import org.jdesktop.swingx.renderer.WrappingProvider;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXHyperlink}.
 *
 * @author Karl George Schaefer
 * @author Richard Bair (original JXHyperlinkDemoPanel)
 * @author Jeanette Winzenburg (original JXHyperlinkDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXHyperlink Demo",
//    category = "Controls",
//    description = "Demonstrates JXHyperlink, a button providing a hyperlink feel.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/hyperlink/HyperlinkDemo.java",
//        "org/jdesktop/swingx/demos/hyperlink/resources/HyperlinkDemo.properties"
//    }
//)
public class HyperlinkDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 2847724784701465650L;
	private static final Logger LOG = Logger.getLogger(HyperlinkDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXHyperlink, a button providing a hyperlink feel.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				// no controller
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				AbstractDemo demo = new HyperlinkDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

    private JXHyperlink plainBrowse;
    private JXHyperlink customBrowse;
    private JXHyperlink plainMail;
    private JXHyperlink customLink;
    private JXList<URI> linkList;
    private JXTable linkTable;
    private JXTree linkTree;
    
    /**
     * HyperlinkDemo Constructor
     */
    public HyperlinkDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        createHyperlinkDemo();
        try {
            bind();
        } catch (URISyntaxException e) {
            // will not happen ... we did type correctly, didn't we ;-)
        }
    }
   
    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}
    
    private void createHyperlinkDemo() {
        JXTitledSeparator simple = new JXTitledSeparator();
        simple.setName("simpleSeparator");
        simple.setTitle(getBundleString("simpleSeparator.title"));
        
        // wg. BUG in JXHyperlink ctor mit OpenBrowserAction:
        plainBrowse = new JXHyperlink(new OpenBrowserAction("https://swingx.dev.java.net"));
        /*
BUG in 
	plainBrowse.setURI(new URI("https://swingx.dev.java.net"));
	// default browser action, text defaults to url
	setText(uri) wird nicht gemacht !!!
         */
        plainBrowse.setName("plainBrowse");
        plainBrowse.setText("swingx.dev.java.net");
        plainBrowse.setToolTipText(getBundleString("plainBrowse.toolTipText"));
        
        plainMail = new JXHyperlink();
        plainMail.setName("plainMail"); // TODO Desktop.Action.MAIL OpenMailAction, braucht man es?
        
        OpenBrowserAction oba = new OpenBrowserAction("https://github.com/homebeaver/SwingSet/wiki");
        customBrowse = new JXHyperlink(oba);
        customBrowse.setName("customBrowse");
        /* props:
customBrowse.text = SwingX - Swing Extension, Experiment, Excitement
customBrowse.icon = images/workerduke.png
customBrowse.toolTipText = Default browser action, custom icon and text injected
         */
        customBrowse.setText(getBundleString("customBrowse.text"));
        customBrowse.setIcon(getResourceAsIcon(getClass(), "resources/images/workerduke.png"));
        customBrowse.setToolTipText(getBundleString("customBrowse.toolTipText"));
        LOG.config("customBrowse:"+customBrowse);
        LOG.config("customBrowse.Action:"+customBrowse.getAction());
        
        JXTitledSeparator custom = new JXTitledSeparator();
        custom.setName("customSeparator");
        custom.setTitle(getBundleString("customSeparator.title"));
        customLink = new JXHyperlink();
        customLink.setName("customLink");
        
        JComponent standaloneLinks = new JXPanel(new VerticalLayout(20));
        standaloneLinks.add(simple);
        standaloneLinks.add(plainBrowse);
        standaloneLinks.add(plainMail);
        standaloneLinks.add(customBrowse);
        standaloneLinks.add(custom);
        standaloneLinks.add(customLink);
        standaloneLinks.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComponent renderedLinks = new JXPanel(new BorderLayout());
        linkList = new JXList<URI>();
        
        linkTable = new JXTable();
        linkTable.setVisibleRowCount(10);

        linkTree = new JXTree();
        
        JXPanel top = new JXPanel(new GridLayout(1, 2, 20, 10));
        top.add(new JScrollPane(linkList));
        top.add(new JScrollPane(linkTree));
        renderedLinks.add(top);
        renderedLinks.setBorder(standaloneLinks.getBorder());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setName("hyperlinkTabs");
        // super.addTab(JTabbedPane tab, JComponent comp, String propName, boolean createScroll) :
        // durch super.addTab ist tabbedPane.setTitleAt(0, "tab title"); nicht notwendig
        addTab(tabbedPane, standaloneLinks, "standaloneTab", false);
        addTab(tabbedPane, renderedLinks, "renderedTab", false);       

        add(tabbedPane);
    }
    
    private void bind() throws URISyntaxException {
        // <snip> Hyperlink with desktop action
        // default mailer action, text defaults to uri
    	URI mailto = new URI("mailto:nobody@dev.java.net");
        plainMail.setURI(mailto);
        plainMail.setText(mailto.toString());
        plainMail.setToolTipText(getBundleString("plainMail.toolTipText"));
        // </snip>       
//        DemoUtils.setSnippet("Hyperlink with desktop action", plainBrowse, plainMail, customBrowse);
        
        // <snip> Hyperlink with custom action
        customLink.setAction(createLinkAction());
        // </snip>
//        DemoUtils.setSnippet("Hyperlink with custom action", customLink);
        
        // <snip> List with hyperlink renderer
        // model containing URIs
        linkList.setModel(new DefaultComboBoxModel<URI>(new URI[] {
                new URI("https://swingx.dev.java.net"),
                new URI("http://wiki.java.net/bin/view/Javadesktop/SwingLabsSwingX"),
                new URI("http://forums.java.net/jive/forum.jspa?forumID=73")
        }));
        // enable rollover
        linkList.setRolloverEnabled(true);
        // set a renderer delegating to a HyperlinkProvider configured
        // with raw HyperlinkAction
        linkList.setCellRenderer(new DefaultListRenderer(new HyperlinkProvider(new HyperlinkAction())));
        //</snip>
//        DemoUtils.setSnippet("List with hyperlink renderer", linkList);
        
        // <snip> Tree with hyperlink renderer
        // model containing URIs wrapped in treeNodes
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new URI("http://java.net"));
        for (int i = 0; i < linkList.getElementCount(); i++) {
            root.add(new DefaultMutableTreeNode(linkList.getElementAt(i)));
        }
        linkTree.setModel(new DefaultTreeModel(root));
        linkTree.expandAll();
        // enable rollover
        linkTree.setRolloverEnabled(true);
        // set a renderer delegating to wrapper around a HyperlinkProvider
        // configured with a raw HyperlinkAction 
        linkTree.setCellRenderer(new DefaultTreeRenderer(new WrappingProvider(
                new HyperlinkProvider(new HyperlinkAction()))));
        // </snip>
//        DemoUtils.setSnippet("Tree with hyperlink renderer", linkTree);        
    }

    private Action createLinkAction() {
        // <snip> Hyperlink with custom action
        // custom implementation of AbstractHyperlinkAction
        // updates visited property and related state
        AbstractHyperlinkAction<?> action = new AbstractHyperlinkAction<Object>() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(null, 
                		getBundleString("customAction.message"),
                		getBundleString("customAction.title"), 
                        JOptionPane.YES_NO_OPTION);
                setVisited(option == JOptionPane.YES_OPTION);
            }

            @Override
            public void setVisited(boolean visited) {
                super.setVisited(visited);
                /* props:
                 * customAction.unverifiedIcon = images/earth_day.gif
                 * customAction.verifiedIcon = images/earth_night.gif
                 */
                Icon unverifiedIcon = getResourceAsIcon(getClass(), "resources/images/earth_day.gif");
                Icon verifiedIcon = getResourceAsIcon(getClass(), "resources/images/earth_night.gif");
                setSmallIcon(isVisited() ? verifiedIcon : unverifiedIcon);
                setName(isVisited() ? getBundleString("customAction.verifiedText") : getBundleString("customAction.unverifiedText"));
            }
            // </snip>
        };
        action.setVisited(false);
        return action;
    }

}