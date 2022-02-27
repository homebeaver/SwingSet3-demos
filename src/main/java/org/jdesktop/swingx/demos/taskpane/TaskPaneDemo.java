/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.taskpane;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.text.html.HTMLDocument;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.action.AbstractActionExt;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTaskPane}.
 *
 * @author Karl George Schaefer
 * @author l2fprod (original JXTaskPaneDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXTaskPane Demo",
//    category = "Containers",
//    description = "Demonstrates JXTaskPane, a container for tasks and other arbitrary components.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/taskpane/TaskPaneDemo.java",
//        "org/jdesktop/swingx/demos/taskpane/resources/TaskPaneDemo.properties",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/TaskPaneDemo.png",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/tasks-email.png",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/tasks-internet.png",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/tasks-question.png",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/tasks-recycle.png",
//        "org/jdesktop/swingx/demos/taskpane/resources/images/tasks-writedoc.png"
//    }
//)
public class TaskPaneDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 7532679655717309639L;
	private static final Logger LOG = Logger.getLogger(TaskPaneDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTaskPane, a container for tasks and other arbitrary components.";

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
				AbstractDemo demo = new TaskPaneDemo(frame);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
			}		
    	});
    }

	private JXTaskPane systemGroup;
    private JXTaskPane officeGroup;
    private JXTaskPane seeAlsoGroup;
    private JXTaskPane detailsGroup;
    
    /**
     * HyperlinkDemo Constructor
     */
    public TaskPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        createTaskPaneDemo();
        
/* bind with props
email.Action.text=Send by email
email.Action.icon=images/tasks-email.png
delete.Action.text=Delete
delete.Action.icon=images/tasks-recycle.png
write.Action.text=Write Document
write.Action.icon=images/tasks-writedoc.png
exploreInternet.Action.text=The Internet
exploreInternet.Action.icon=images/tasks-internet.png
exploreInternet.Action.shortDescription=A cool web resource
help.Action.text=Help Center
help.Action.icon=images/tasks-question.png
help.Action.shortDescription=The place where you can't find anything
 */
        AbstractActionExt email = new TaskAction(getBundleString("email.Action.text"), getResourceAsIcon(getClass(), "resources/images/tasks-email.png"));
        email.setActionCommand("email");
        systemGroup.add(email);
        LOG.info("email:"+email);
//INFORMATION: key 3:email (class java.lang.String) 
//             value:org.jdesktop.application.ApplicationAction email "Send by email"
//INFORMATION: email:[class org.jdesktop.swingx.demos.taskpane.TaskPaneDemo$TaskAction:Name=Send by email,SmallIcon=javax.swing.ImageIcon@16d0f804,ActionCommandKey=email]

        AbstractActionExt delete = new TaskAction(getBundleString("delete.Action.text"), getResourceAsIcon(getClass(), "resources/images/tasks-recycle.png"));
        delete.setActionCommand("delete");
        systemGroup.add(delete);

        AbstractActionExt write = new TaskAction(getBundleString("write.Action.text"), getResourceAsIcon(getClass(), "resources/images/tasks-writedoc.png"));
        write.setActionCommand("write");
        officeGroup.add(write);

        TaskAction exploreInternet = new TaskAction(getBundleString("exploreInternet.Action.text"), getResourceAsIcon(getClass(), "resources/images/tasks-internet.png"));
        exploreInternet.setActionCommand("exploreInternet");
        exploreInternet.setShortDescription(getBundleString("exploreInternet.Action.shortDescription"));
        seeAlsoGroup.add(exploreInternet);

        AbstractActionExt help = new TaskAction(getBundleString("help.Action.text"), getResourceAsIcon(getClass(), "resources/images/tasks-question.png"));
        help.setActionCommand("help");
        exploreInternet.setShortDescription(getBundleString("help.Action.shortDescription"));
        seeAlsoGroup.add(help);
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

    private void createTaskPaneDemo() {
        JXTaskPaneContainer tpc = new JXTaskPaneContainer();

        // set with props values
        
        // "System" GROUP
        systemGroup = new JXTaskPane(); // Alternativ: JXTaskPane(String title, Icon icon)
        systemGroup.setName("systemGroup");
        systemGroup.setTitle(getBundleString("systemGroup.title", systemGroup));
        systemGroup.setIcon(getResourceAsIcon(getClass(), "resources/images/tasks-email.png"));
        systemGroup.setToolTipText(getBundleString("systemGroup.toolTipText"));
        systemGroup.setSpecial(Boolean.valueOf(getBundleString("systemGroup.special")));
        tpc.add(systemGroup);

        // "Office" GROUP
        officeGroup = new JXTaskPane();
        officeGroup.setName("officeGroup");
        officeGroup.setTitle(getBundleString("officeGroup.title", officeGroup));
        officeGroup.setCollapsed(Boolean.valueOf(getBundleString("officeGroup.collapsed")));
        officeGroup.setScrollOnExpand(Boolean.valueOf(getBundleString("officeGroup.scrollOnExpand")));
        tpc.add(officeGroup);
        
        // "SEE ALSO" GROUP and ACTIONS
        seeAlsoGroup = new JXTaskPane();
        seeAlsoGroup.setName("seeAlsoGroup");
        seeAlsoGroup.setTitle(getBundleString("seeAlsoGroup.title", seeAlsoGroup));
        tpc.add(seeAlsoGroup);
        
        // "Details" GROUP
        detailsGroup = new JXTaskPane();
        detailsGroup.setName("detailsGroup");
        detailsGroup.setTitle(getBundleString("detailsGroup.title", detailsGroup));
        detailsGroup.setScrollOnExpand(Boolean.valueOf(getBundleString("detailsGroup.scrollOnExpand")));
        
        //TODO better injection for editor area
        JEditorPane area = new JEditorPane("text/html", "<html>");
        area.setName("detailsArea");
        area.setText(getBundleString("detailsArea.text"));
        area.setEditable(Boolean.valueOf(getBundleString("detailsArea.editable")));
        area.setOpaque(Boolean.valueOf(getBundleString("detailsArea.opaque")));
        
        area.setFont(UIManager.getFont("Label.font"));
        
        Font defaultFont = UIManager.getFont("Button.font");
        
        String stylesheet = "body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
                + defaultFont.getName()
                + "; font-size: "
                + defaultFont.getSize()
                + "pt;  }"
                + "a, p, li { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: "
                + defaultFont.getName()
                + "; font-size: "
                + defaultFont.getSize()
                + "pt;  }";
        if (area.getDocument() instanceof HTMLDocument) {
            HTMLDocument doc = (HTMLDocument)area.getDocument();
            try {
                doc.getStyleSheet().loadRules(new java.io.StringReader(stylesheet), null);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        
        detailsGroup.add(area);
        
        tpc.add(detailsGroup);
        
        add(new JScrollPane(tpc));
    }
    
    public class TaskAction extends AbstractActionExt {

    	public TaskAction(String name, Icon icon) {
    		super(name,icon);
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			LOG.info("ActionEvent "+e + "\n NOT IMPLEMENTED");
		}
    	
    }

}
