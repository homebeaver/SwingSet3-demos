package com.klst.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.icon.PauseIcon;
import org.jdesktop.swingx.icon.SizingConstants;

import swingset.AbstractDemo;
import swingset.StaticUtilities;

@SuppressWarnings("serial")
public class WindowFrame extends JXFrame {

    private static final Logger LOG = Logger.getLogger(WindowFrame.class.getName());

	private static int windowCounter = 0; // für windowNo, wird pro ctor hochgezählt
	private int windowNo;
	RootFrame rootFrame; // mit FrameManager
	public RootFrame getRootFrame() {
		return rootFrame;
	}
	
	private int window_ID;
	JXPanel jPanel = new JXPanel(new BorderLayout());
	
	/*
	 * window_ID==-1 is used for RootFrame
	 */
	WindowFrame(String title, RootFrame rootFrame, int window_ID, Object object) {
		super(title
			, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			, window_ID==-1 ? true : false // exitOnClose
			);
		windowCounter++;
		this.windowNo = windowCounter-1;
		
		this.rootFrame = rootFrame;
		this.window_ID = window_ID;
		
		// bei RootFrame: setJMenuBar:
		if(this instanceof RootFrame) {
			LOG.info("\nthis:"+this);
//			JMenu jMenu = createPlafMenu(); // aus InteractiveTestCase
//			JMenuBar jMenuBar = createAndFillMenuBar(null);
			setJMenuBar(createAndFillMenuBar(null));
		}

//		AbstractAction disableFrameMgrAction = new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	LOG.info("disable FrameMgr");
//            	if(getRootFrame().enable) {
//                	getRootFrame().enable = false;
//                	// TODO for all frames:
//                    //addFrameAction.putValue(Action.LARGE_ICON_KEY, new StopIcon(SizingConstants.ACTION_ICON, Color.RED));
//            	}
//            }
//        };
//        disableFrameMgrAction.putValue(Action.NAME, "disableFrameMgr");
//        disableFrameMgrAction.putValue(Action.LARGE_ICON_KEY, new PauseIcon(SizingConstants.LAUNCHER_ICON, Color.MAGENTA));
//    	JXButton pause = new JXButton(disableFrameMgrAction);

		getRootPaneExt().setToolBar(new ToggleButtonToolBar()); // inner Class
//		super.setSize(600, 200); fex size or better ==> pack()
	}
	
	WindowFrame(String title) { // für RootFrame
		this(title, null, -1, null);
	}

	/*

in SwingSet2 gibt es eine inner Klasse ToggleButtonToolBar extends JToolBar
mit einer Methode: JToggleButton addToggleButton(Action a)

- public class javax.swing.JToggleButton extends AbstractButton
ist also "Geschwister" von
- public class javax.swing.JButton extends AbstractButton

Demnach können die JToggleButton in der ToolBar nicht JXButton sein, denn
- public class JXButton extends JButton
Es sei denn man implementiert JXToggleButton TODO

	JToolBar.add(Action a) kann man nicht überschreben, denn es liefert JButton,
	daher: (aus Swingset2)
        JToggleButton addToggleButton(Action a) {
            JToggleButton tb = new JToggleButton(
                (String)a.getValue(Action.NAME),            // Text wie Action.NAME
                (Icon)a.getValue(Action.SMALL_ICON)
            );
            tb.setMargin(zeroInsets);
            tb.setText(null);                               // Text wieder aus
            tb.setEnabled(a.isEnabled());
            tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
            tb.setAction(a);
            add(tb);
            return tb;
        }
 */
    protected class ToggleButtonToolBar extends JToolBar {
        public ToggleButtonToolBar() {
            super();
        }
        public JToggleButton addToggleButton(Action a) {
/*
aus super:        	
            JButton b = createActionComponent(a);
 */
        	JToggleButton b = createToggleComponent(a);
            b.setAction(a);
            add(b);
            return b;

        }
        protected JToggleButton createToggleComponent(Action a) {
        	JToggleButton b = new JToggleButton() {
                protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                    PropertyChangeListener pcl = null;
                    if (pcl==null) {
                        pcl = super.createActionPropertyChangeListener(a);
                    }
                    return pcl;
                }
            };
            if (a != null && (a.getValue(Action.SMALL_ICON) != null ||
                              a.getValue(Action.LARGE_ICON_KEY) != null)) {
                b.setHideActionText(true);
            }
            b.setHorizontalTextPosition(JButton.CENTER);
            b.setVerticalTextPosition(JButton.BOTTOM);
            return b;
        }

    }

	// aus InteractiveTestCase.createAndFillMenuBar:
    public AbstractButton addActionToToolBar(JXFrame frame, Action action) {
        JToolBar toolbar = frame.getRootPaneExt().getToolBar();
        if(toolbar instanceof ToggleButtonToolBar) {
        	LOG.info("action:"+action);
        	ToggleButtonToolBar tbtb = (ToggleButtonToolBar)toolbar;
            AbstractButton button = tbtb.addToggleButton(action);
            button.setToolTipText((String)action.getValue(Action.SHORT_DESCRIPTION));
            button.setFocusable(false);
            return button;
        }
        if (toolbar != null) {
            AbstractButton button = toolbar.add(action);
            button.setFocusable(false);
            return button;
        }
        return null;
    }

    /**
     * Creates, fills and returns a JMenuBar. 
     * 
     * @param component the component that was added to the frame.
     * @return a menu bar filled with actions as defined in createAndAddMenus
     * 
     * @see #createAndAddMenus
     */
	// aus InteractiveTestCase.createAndFillMenuBar
    protected JMenuBar createAndFillMenuBar(JComponent component) {
        JMenuBar bar = new JMenuBar();
//        createAndAddMenus(bar, component); // == bar.add(createPlafMenu());
        bar.add(createPlafMenu(null));
        return bar;
    }

    /**
     * Creates a menu filled with one SetPlafAction for each of the currently
     * installed LFs.
     * 
     * @param target the toplevel window to update, maybe null to indicate update
     *   of all application windows
     * @return the menu to use for plaf switching.
     */
	// aus InteractiveTestCase.createPlafMenu
    protected JMenu createPlafMenu(Window target) {
        LookAndFeelInfo[] plafs = UIManager.getInstalledLookAndFeels();
        JMenu menu = new JMenu("Set L&F");
        
        for (LookAndFeelInfo info : plafs) {
//            LOG.info(info.getName()+" "+info.getClassName()+" "+target);
            menu.add(new SetPlafAction(info.getName(), info.getClassName(), target));
        }
        return menu;
    }

    /**
     * Returns the <code>JXFrame</code>'s status bar. Lazily creates and 
     * sets an instance if necessary.
     * @param frame the target frame
     * @return the frame's statusbar
     */
	// aus InteractiveTestCase.getStatusBar
    static JXStatusBar getStatusBar(JXFrame frame) {
        JXStatusBar statusBar = frame.getRootPaneExt().getStatusBar();
        if (statusBar == null) {
            statusBar = new JXStatusBar();
            frame.setStatusBar(statusBar);
        }
        return statusBar;
    }
    public JXStatusBar getStatusBar() {
    	return getStatusBar(this);
    }

    public class DemoAction extends AbstractAction {

    	Class<?> democlass = null;

    	public DemoAction(Class<?> democlass, String name, Icon icon) {
    		this(democlass, name, null, icon);
    	}
    	public DemoAction(Class<?> democlass, String name, Icon smallIcon, Icon icon) {
    		super(name, icon);
    		this.democlass = democlass;
            super.putValue(Action.LARGE_ICON_KEY, icon);
            
            // SHORT_DESCRIPTION will setToolTipText in addActionToToolBar
            String key = this.democlass.getSimpleName() + '.' + "tooltip";
            String short_description = StaticUtilities.getResourceAsString(key, null);
            super.putValue(Action.SHORT_DESCRIPTION, short_description);
    	}
    	
        @Override
        public void actionPerformed(ActionEvent e) {
        	// frame erstellen - demo starten: TODO: controler in root registrieren
        	
        	int frameNumber = windowCounter;
        	//------------
        	//------------
        	WindowFrame frame = getRootFrame().makeFrame(frameNumber, getRootFrame(), 1, null);
        	if(frame!=null) {
        		frame.setStartPosition(StartPosition.CenterInScreen);
        		AbstractDemo demo = null;
				try {
					demo = getInstanceOf(democlass, frame); // ctor 
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	getRootFrame().getContentPane().add(demo.getControlPane()); // controler
            	getRootFrame().pack();
            	
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
        	}
        }
    
		private AbstractDemo getInstanceOf(Class<?> demoClass, Frame frame) throws NoSuchMethodException, SecurityException,
				InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			Constructor<?> demoConstructor = demoClass.getConstructor(new Class[] { Frame.class }); // throws
																									// NoSuchMethodException,
																									// SecurityException
			AbstractDemo demo = (AbstractDemo) demoConstructor.newInstance(new Object[] { frame });
			return demo;
		}

    }
    
    /**
     * Action to toggle plaf and update all toplevel windows of the
     * current application. Used to setup the plaf-menu.
     */
    private static class SetPlafAction extends AbstractAction {

        private String plaf;
        private Window toplevel;

        @SuppressWarnings("unused")
        public SetPlafAction(String name, String plaf) {
            this(name, plaf, null);
        }
        
        /**
         * Instantiates an action which updates the toplevel window to
         * the given LAF. 
         * 
         * @param name the name of the action
         * @param plaf the class name of the LAF to set
         * @param toplevel the window to update, may be null to indicate
         *   update of all application windows
         */
        public SetPlafAction(String name, String plaf, Window toplevel) {
            super(name);
            this.plaf = plaf;
            this.toplevel = toplevel;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
//        	LOG.info("plaf:"+plaf +", ActionEvent "+e);
            try {
                UIManager.setLookAndFeel(plaf);
                if (toplevel != null) {
                    SwingUtilities.updateComponentTreeUI(toplevel);
                } else {
                    SwingXUtilities.updateAllComponentTreeUIs();
                }
                
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.log(Level.FINE, "problem in setting laf: " + plaf, e1);
            } 
        }

    }

}
