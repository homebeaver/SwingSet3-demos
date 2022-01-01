/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * JProgressBar Demo
 *
 * @author Jeff Dinkins
 # @author Peter Korn (accessibility support)
 */
public class ProgressBarDemo extends DemoModule {

    public static final String ICON_PATH = "toolbar/JProgressBar.gif";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        ProgressBarDemo demo = new ProgressBarDemo(null);
        demo.mainImpl();
    }

    /**
     * ProgressBarDemo Constructor
     */
    public ProgressBarDemo(SwingSet2 swingset) {
        // Set the title for this demo, and an icon used to represent this
        // demo inside the SwingSet2 app.
        super(swingset, "ProgressBarDemo", ICON_PATH);

        createProgressPanel();
    }

    javax.swing.Timer timer = new javax.swing.Timer(18, createTextLoadAction());
    Action loadAction;
    Action stopAction;
    JProgressBar progressBar;
    JTextArea progressTextArea;

    void updateDragEnabled(boolean dragEnabled) {
        progressTextArea.setDragEnabled(dragEnabled);
    }

    public void createProgressPanel() {
        getDemoPanel().setLayout(new BorderLayout());

        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        textWrapper.setAlignmentX(LEFT_ALIGNMENT);
        progressTextArea = new MyTextArea();

        progressTextArea.getAccessibleContext().setAccessibleName(getString("ProgressBarDemo.accessible_text_area_name"));
        progressTextArea.getAccessibleContext().setAccessibleName(getString("ProgressBarDemo.accessible_text_area_description"));
        textWrapper.add(new JScrollPane(progressTextArea), BorderLayout.CENTER);

        getDemoPanel().add(textWrapper, BorderLayout.CENTER);

        JPanel progressPanel = new JPanel();
        getDemoPanel().add(progressPanel, BorderLayout.SOUTH);

        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, text.length()) {
            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        progressBar.getAccessibleContext().setAccessibleName(getString("ProgressBarDemo.accessible_text_loading_progress"));

        progressPanel.add(progressBar);
        progressPanel.add(createLoadButton());
        progressPanel.add(createStopButton());
    }

    public JButton createLoadButton() {
        loadAction = new AbstractAction(getString("ProgressBarDemo.start_button")) {
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

    public JButton createStopButton() {
        stopAction = new AbstractAction(getString("ProgressBarDemo.stop_button")) {
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                loadAction.setEnabled(true);
                stopAction.setEnabled(false);
            }
        };
        return createButton(stopAction);
    }

    public JButton createButton(Action a) {
        JButton b = new JButton();
        // setting the following client property informs the button to show
        // the action text as it's name. The default is to not show the
        // action text.
        b.putClientProperty("displayActionText", Boolean.TRUE);
        b.setAction(a);
        return b;
    }


    int textLocation = 0;

    String text = getString("ProgressBarDemo.text");

    public Action createTextLoadAction() {
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
