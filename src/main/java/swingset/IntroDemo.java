/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Intro
 */
public class IntroDemo extends DemoModule {

	private static final long serialVersionUID = 1867077915822954698L;

	public static final String ICON_PATH = "toolbar/JMenu.gif";

    private JEditorPane html;
    private JSplitPane splitPane = null;


    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        IntroDemo demo = new IntroDemo(null);
        demo.mainImpl();
    }

    public IntroDemo(SwingSet2 swingset) {
        // Set the resource name for this demo, 
    	// and an icon used to represent this demo inside the SwingSet2 tool bar.
        super(swingset, "IntroDemo", ICON_PATH);

        JPanel demo = getDemoPanel();
        JLabel splash = null;
        JLabel sunSplash = null;

        sunSplash = new JLabel(createImageIcon("SplashEpisode2.jpg", "the ancient inception sun splash"));
        sunSplash.setMinimumSize(new Dimension(20, 20));

        splash = new JLabel(createImageIcon("splash.png", "current splash"));
        splash.setMinimumSize(new Dimension(20, 20));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sunSplash, splash);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(600);

        demo.add(splitPane, BorderLayout.NORTH);
        demo.add(createSplitPaneControls(), BorderLayout.CENTER);
    }
    
	private JComponent createSplitPaneControls() {
		JPanel jPanel = new JPanel();
		try {
			URL url = null;
			String path = null;
			try {
				path = "/swingset/swingset2.html";
				url = getClass().getResource(path);
			} catch (Exception e) {
				System.err.println("Failed to open " + path);
				url = null;
			}

			if (url != null) {
				html = new JEditorPane(url);
				html.setEditable(false);
				html.addHyperlinkListener(createHyperLinkListener());

				JScrollPane scroller = new JScrollPane();
				JViewport vp = scroller.getViewport();
				vp.add(html);
				return scroller;
			}
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL: " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		return jPanel;
	}

    private HyperlinkListener createHyperLinkListener() {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument)html.getDocument()).processHTMLFrameHyperlinkEvent(
                            (HTMLFrameHyperlinkEvent)e);
                    } else {
                        try {
                            html.setPage(e.getURL());
                        } catch (IOException ioe) {
                            System.out.println("IOE: " + ioe);
                        }
                    }
                }
            }
        };
    }

    void updateDragEnabled(boolean dragEnabled) {
        html.setDragEnabled(dragEnabled);
    }

}
