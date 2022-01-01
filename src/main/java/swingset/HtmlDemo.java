/* Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package swingset;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * Html Demo
 *
 * @author Jeff Dinkins
 */
public class HtmlDemo extends DemoModule {

	private static final long serialVersionUID = 1867077915822954698L;

	public static final String ICON_PATH = "toolbar/JEditorPane.gif";

    JEditorPane html;

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        HtmlDemo demo = new HtmlDemo(null);
        demo.mainImpl();
    }

    /**
     * HtmlDemo Constructor
     */
    public HtmlDemo(SwingSet2 swingset) {
        // Set the title for this demo, and 
    	// an icon used to represent this demo inside the SwingSet2 app.
        super(swingset, "HtmlDemo", ICON_PATH);

        try {
            URL url = null;
            // System.getProperty("user.dir") +
            // System.getProperty("file.separator");
            String path = null;
            try {
                path = "/swingset/index.html";
                url = getClass().getResource(path);
            } catch (Exception e) {
                System.err.println("Failed to open " + path);
                url = null;
            }

            if(url != null) {
                html = new JEditorPane(url);
                html.setEditable(false);
                html.addHyperlinkListener(createHyperLinkListener());

                JScrollPane scroller = new JScrollPane();
                JViewport vp = scroller.getViewport();
                vp.add(html);
                getDemoPanel().add(scroller, BorderLayout.CENTER);
            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public HyperlinkListener createHyperLinkListener() {
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
