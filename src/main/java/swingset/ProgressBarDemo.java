/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXFrame.StartPosition;

/**
 * JProgressBar Demo
 *
 * @author Jeff Dinkins
 * @author Peter Korn (accessibility support)
 * @author EUG https://github.com/homebeaver (reorg)
 */
public class ProgressBarDemo extends AbstractDemo {

	/**
	 * this is used in DemoAction to build the demo toolbar
	 */
	public static final String ICON_PATH = "toolbar/JProgressBar.gif";

	private static final long serialVersionUID = -5132422584566294096L;
	private static final String DESCRIPTION = "Shows an example of using the JProgressBar component.";
	private static final boolean CONTROLLER_IN_PRESENTATION_FRAME = true;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			// no controller
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			AbstractDemo demo = new ProgressBarDemo(frame);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	});
    }

    javax.swing.Timer timer = new javax.swing.Timer(18, createTextLoadAction());
    Action loadAction;
    Action stopAction;
    JProgressBar progressBar;
    JTextArea progressTextArea;

    /**
     * ProgressBarDemo Constructor
     * @param frame controller Frame
     */
    public ProgressBarDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("name"));

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        textWrapper.setAlignmentX(LEFT_ALIGNMENT);
        progressTextArea = new MyTextArea();

        progressTextArea.getAccessibleContext().setAccessibleName(getBundleString("ProgressBarDemo.accessible_text_area_name"));
        progressTextArea.getAccessibleContext().setAccessibleName(getBundleString("ProgressBarDemo.accessible_text_area_description"));
        textWrapper.add(new JScrollPane(progressTextArea), BorderLayout.CENTER);

        super.add(textWrapper, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        super.add(progressPanel, BorderLayout.SOUTH);

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, text.length()) {
            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        progressBar.getAccessibleContext().setAccessibleName(getBundleString("ProgressBarDemo.accessible_text_loading_progress"));

        progressPanel.add(progressBar);
        if(CONTROLLER_IN_PRESENTATION_FRAME) {
        	progressPanel.add(createLoadButton());
        	progressPanel.add(createStopButton());
        }
    }

    @Override
	public JXPanel getControlPane() {
        if(CONTROLLER_IN_PRESENTATION_FRAME) return emptyControlPane();
        	
        JXPanel controller = new JXPanel();
        controller.add(createLoadButton());
        controller.add(createStopButton());
    	return controller;
    }

    private void updateDragEnabled(boolean dragEnabled) {
        progressTextArea.setDragEnabled(dragEnabled);
    }

    private JButton createLoadButton() {
        loadAction = new AbstractAction(getBundleString("start_button")) {
            public void actionPerformed(ActionEvent e) {
                loadAction.setEnabled(false);
                stopAction.setEnabled(true);
                if (progressBar.getValue() == progressBar.getMaximum()) {
                    progressBar.setValue(0);
                    textLocation = 0;
                    progressTextArea.setText("");
                }
                timer.start();
            }
        };
        return createButton(loadAction);
    }

    private JButton createStopButton() {
        stopAction = new AbstractAction(getBundleString("stop_button")) {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                loadAction.setEnabled(true);
                stopAction.setEnabled(false);
            }
        };
        return createButton(stopAction);
    }

    private JButton createButton(Action a) {
        JButton b = new JButton();
        // setting the following client property informs the button to show
        // the action text as it's name. The default is to not show the
        // action text.
        b.putClientProperty("displayActionText", Boolean.TRUE);
        b.setAction(a);
        return b;
    }


    int textLocation = 0;

    String text = getBundleString("text");

    private Action createTextLoadAction() {
        return new AbstractAction("text load action") {
            public void actionPerformed (ActionEvent e) {
                if(progressBar.getValue() < progressBar.getMaximum()) {
                    progressBar.setValue(progressBar.getValue() + 1);
                    progressTextArea.append(text.substring(textLocation, textLocation+1));
                    textLocation++;
                } else {
                        timer.stop();
                        loadAction.setEnabled(true);
                        stopAction.setEnabled(false);
                }
            }
        };
    }


    class MyTextArea extends JTextArea {
        public MyTextArea() {
            super(null, 0, 0);
            setEditable(false);
            setText("");
        }

        public float getAlignmentX () {
            return LEFT_ALIGNMENT;
        }

        public float getAlignmentY () {
            return TOP_ALIGNMENT;
        }
    }
}
