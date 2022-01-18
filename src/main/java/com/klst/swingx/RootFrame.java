package com.klst.swingx;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jdesktop.swingx.JXFrame;

/*
TODO RootFrame <> JXRootPane
 */
@SuppressWarnings("serial")
public class RootFrame extends WindowFrame {

    private static final Logger LOG = Logger.getLogger(RootFrame.class.getName());

	private static final String TITLE = "Gossip";
	
	public RootFrame() {
		super(TITLE);
		super.rootFrame = this;
		// TODO ...
		frames = new ArrayList<JXFrame>();
		frames.add(this);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LOG.info(TITLE+" frame ctor. frames#="+frames.size() + " super.rootFrame:"+super.rootFrame);
	}
	
	// simple frame manager
	List<JXFrame> frames;
	boolean enable = true;
	boolean remove(JXFrame frame) {
		return frames.remove(frame);
	}
	WindowFrame makeFrame(int frameNumber, RootFrame rootFrame, int window_ID, Object object) {
		if(enable) {
    		WindowFrame frame = new WindowFrame("Frame number " + frameNumber, rootFrame, window_ID, object);
    		frames.add(frame);
    		return frame;
		}
		return null;
	}
	// ...
	// <<<

}
