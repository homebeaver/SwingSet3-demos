package com.klst.swingx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.demos.tree.XTreeDemo;
import org.jdesktop.swingx.demos.treetable.TreeTableDemo;

import swingset.ButtonDemo;
import swingset.ColorChooserDemo;
import swingset.ComboBoxDemo;
import swingset.FileChooserDemo;
import swingset.HtmlDemo;
import swingset.ListDemo;
import swingset.OptionPaneDemo;
import swingset.ProgressBarDemo;
import swingset.ScrollPaneDemo;
import swingset.SliderDemo;
import swingset.SplitPaneDemo;
import swingset.StaticUtilities;
import swingset.TabbedPaneDemo;
import swingset.TableDemo;
import swingset.ToolTipDemo;
import swingset.TreeDemo;

/*
TODO RootFrame <> JXRootPane
 */
@SuppressWarnings("serial")
public class RootFrame extends WindowFrame {

    private static final Logger LOG = Logger.getLogger(RootFrame.class.getName());

	private static final String TITLE = "Gossip";
	
	Map<Class<?>, WindowFrame> demos;
	public RootFrame() {
		super(TITLE);
		super.rootFrame = this;
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
		demos = new HashMap<Class<?>, WindowFrame>();
		// TODO ... so initialisieren geht angeblich auch für static var
//		demos = Map.ofEntries(
//			Map.entry(InternalFrameDemo.class, null), // NullPointerException at java.base/java.util.Objects.requireNonNull(Objects.java:208)
//			Map.entry(ButtonDemo.class       , null),
//			Map.entry(ColorChooserDemo.class , null),
//			Map.entry(ComboBoxDemo.class     , null),
//			Map.entry(FileChooserDemo.class  , null),
//			Map.entry(HtmlDemo.class         , null),
//			Map.entry(ListDemo.class         , null),
//			Map.entry(OptionPaneDemo.class   , null),
//			Map.entry(ProgressBarDemo.class  , null),
//			Map.entry(ScrollPaneDemo.class   , null),
//			Map.entry(SliderDemo.class       , null),
//			Map.entry(SplitPaneDemo.class    , null),
//			Map.entry(TabbedPaneDemo.class   , null),
//			Map.entry(TableDemo.class        , null),
//			Map.entry(ToolTipDemo.class      , null),
//			Map.entry(TreeDemo.class         , null)
//			);
		addDemos();
		frames = new ArrayList<JXFrame>();
		frames.add(this);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LOG.info(TITLE+" frame ctor. frames#="+frames.size() + " super.rootFrame:"+super.rootFrame);
    	getRootFrame().demoActions.forEach( a -> {
    		AbstractButton ab = addActionToToolBar(this, a);
    	});

	}

	private void initDemos() {
		// wie oben
		// oder addDemo zusammen mit demoActions
	}
	List<AbstractAction> demoActions = new ArrayList<AbstractAction>();
	private void addDemos() {
		demos.put(null, null); // current presentation frame
		demoActions.add(new DemoAction(ButtonDemo.class, "runDemo", StaticUtilities.createImageIcon(ButtonDemo.ICON_PATH)));
		demos.put(ButtonDemo.class, null);
		demoActions.add(new DemoAction(ColorChooserDemo.class, "runDemo", StaticUtilities.createImageIcon(ColorChooserDemo.ICON_PATH)));
		demos.put(ColorChooserDemo.class, null);
		demoActions.add(new DemoAction(ComboBoxDemo.class, "runDemo", StaticUtilities.createImageIcon(ComboBoxDemo.ICON_PATH)));
		demos.put(ComboBoxDemo.class, null);
		demoActions.add(new DemoAction(FileChooserDemo.class, "runDemo", StaticUtilities.createImageIcon(FileChooserDemo.ICON_PATH)));
		demos.put(FileChooserDemo.class, null);
		demoActions.add(new DemoAction(HtmlDemo.class, "runDemo", StaticUtilities.createImageIcon(HtmlDemo.ICON_PATH)));
		demos.put(HtmlDemo.class, null);
		demoActions.add(new DemoAction(ListDemo.class, "runDemo", StaticUtilities.createImageIcon(ListDemo.ICON_PATH)));
		demos.put(ListDemo.class, null);
		demoActions.add(new DemoAction(OptionPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(OptionPaneDemo.ICON_PATH)));
		demos.put(OptionPaneDemo.class, null);		
		demoActions.add(new DemoAction(ProgressBarDemo.class, "runDemo", StaticUtilities.createImageIcon(ProgressBarDemo.ICON_PATH)));
		demos.put(ProgressBarDemo.class, null);
		demoActions.add(new DemoAction(ScrollPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(ScrollPaneDemo.ICON_PATH)));
		demos.put(ScrollPaneDemo.class, null);
		demoActions.add(new DemoAction(SliderDemo.class, "runDemo", StaticUtilities.createImageIcon(SliderDemo.ICON_PATH)));
		demos.put(SliderDemo.class, null);
		demoActions.add(new DemoAction(SplitPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(SplitPaneDemo.ICON_PATH)));
		demos.put(SplitPaneDemo.class, null);
		demoActions.add(new DemoAction(TabbedPaneDemo.class, "runDemo", StaticUtilities.createImageIcon(TabbedPaneDemo.ICON_PATH)));
		demos.put(TabbedPaneDemo.class, null);
		demoActions.add(new DemoAction(TableDemo.class, "runDemo", StaticUtilities.createImageIcon(TableDemo.ICON_PATH)));
		demos.put(TableDemo.class, null);
		demoActions.add(new DemoAction(ToolTipDemo.class, "runDemo", StaticUtilities.createImageIcon(ToolTipDemo.ICON_PATH)));
		demos.put(ToolTipDemo.class, null);
		demoActions.add(new DemoAction(TreeDemo.class, "runDemo", StaticUtilities.createImageIcon(TreeDemo.ICON_PATH)));
		demos.put(TreeDemo.class, null);
		// swingset 3:
		demoActions.add(new DemoAction(XTreeDemo.class, "runDemo", StaticUtilities.createImageIcon(XTreeDemo.class, XTreeDemo.ICON_PATH)));
		demos.put(XTreeDemo.class, null);
		demoActions.add(new DemoAction(TreeTableDemo.class, "runDemo", StaticUtilities.createImageIcon(TreeTableDemo.class, TreeTableDemo.ICON_PATH)));
		demos.put(TreeTableDemo.class, null);
	}

	// simple frame manager
	List<JXFrame> frames;
	boolean enable = true;
	boolean remove(JXFrame frame) {
		return frames.remove(frame);
	}
	WindowFrame makeFrame(RootFrame rootFrame, int window_ID, Object object) {
		if(enable) {
			int frameNumber = getWindowCounter();
    		WindowFrame frame = new WindowFrame("Frame number " + frameNumber, rootFrame, window_ID, object);
    		frames.add(frame);
    		// close/dispose current and make frame current:
    		WindowFrame current = demos.get(null);
    		LOG.info("------------ close/dispose "+current);
    		if(current!=null) {
    			current.dispose();
    		}
    		demos.put(null, frame);
    		
    		return frame;
		}
		return null;
	}
	// ...
	// <<<

	JXPanel currentController = null;
	public void addControler(JXPanel controlPane) {
		if(currentController!=null) getContentPane().remove(currentController);
		currentController = controlPane;
		getContentPane().add(currentController);
		pack();
	}

}
