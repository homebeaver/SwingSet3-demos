package io.github.homebeaver.swingset.demo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.action.AbstractActionExt;

import swingset.StaticUtilities;

/**
 * multi window Frame used for Demos
 */
@SuppressWarnings("serial")
public class DemoJXFrame extends JXFrame {

    private static final Logger LOG = Logger.getLogger(DemoJXFrame.class.getName());
    private static final String METAL = "javax.swing.plaf.metal.MetalLookAndFeel";
    private static final String STEEL = "javax.swing.plaf.metal.DefaultMetalTheme";
    private static final String OCEAN = "javax.swing.plaf.metal.OceanTheme";
    // some UI object keys
    private static final String UI_KEY_BOLDMETAL = "swing.boldMetal"; // Boolean
    private static final String UI_KEY_FRAME_TITLEFONT = "InternalFrame.titleFont"; // Font

    public static boolean isMETAL() {
    	return "Metal".equals(UIManager.getLookAndFeel().getName()); // oder String getID()
    }
    public static void setDefaultLookAndFeelDecorated(boolean defaultLookAndFeelDecorated) {
        JFrame.setDefaultLookAndFeelDecorated(defaultLookAndFeelDecorated);    	
    }
    
	private static int windowCounter = 0; // für windowNo, wird pro ctor hochgezählt
	int getWindowCounter() {
		return windowCounter;
	}
	private int windowNo;
	/**
	 * get singleton instance of MainJXframe
	 * @return MainJXframe singleton
	 */
	public MainJXframe getRootFrame() {
		return MainJXframe.getInstance();
	}
	
    ButtonGroup lafMenuGroup;
	private ButtonGroup lafGroup = null; // wg. mi.setSelected
	ButtonGroup getLaFGroup() {
		if(lafGroup==null) lafGroup = new ButtonGroup();
		return lafGroup;
	}
	
	private int window_ID;
	JXPanel jPanel = new JXPanel(new BorderLayout());
	
	@Deprecated // not used
	static private void initialTheme(String themeClassName) {
		String plaf = METAL;
        try {
            UIManager.setLookAndFeel(plaf);
        } catch (Exception e1) {
            e1.printStackTrace();
            LOG.log(Level.FINE, "problem in setting laf: " + plaf, e1);
        }

		Class<?> themeClass = null;
		MetalTheme theme = null;
		try {
			themeClass = Class.forName(themeClassName); // throws ClassNotFoundException
		} catch (Exception e) {
			LOG.warning("Error occurred loading class: "+themeClassName);
			e.printStackTrace();
		}
		try {
			theme = (MetalTheme)themeClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info(">>>>>>>>>>>>>>>>>theme:"+theme.getName());
		MetalLookAndFeel.setCurrentTheme(theme);
		LOG.info(">>>>>>>>>>>>>>>>>current:"+MetalLookAndFeel.getCurrentTheme());
	}
	// Einstellungen vor Frame ctor
	static private boolean exitOnClose(int window_ID) {
        UIManager.put(UI_KEY_BOLDMETAL, Boolean.FALSE); // turn off bold fonts in Metal
        // dieser key gilt nicht für Frame Titel, das macht:
		Font font = UIManager.getFont(UI_KEY_FRAME_TITLEFONT);
		Object newFont = UIManager.put(UI_KEY_FRAME_TITLEFONT, new Font(font.getName(), Font.PLAIN, font.getSize()));
		LOG.info(UI_KEY_FRAME_TITLEFONT+" changed to "+newFont + " was "+font);
			
		LOG.info("InternalFrame.closeIcon:"+UIManager.getIcon("InternalFrame.closeIcon"));
//		initialTheme(STEEL); // uncomment to start explicitly with STEEL
		if(isMETAL() && window_ID!=-1) { // root Window always OS controlled ID==-1
			// decorate Demo Frame Title with STEEL when LaF is METAL
			// otherwise no decoration aka OS controlled
	        setDefaultLookAndFeelDecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
		}
		return window_ID==-1 ? true : false;
	}
	/*
	 * window_ID==-1 is used for RootFrame
	 */
	DemoJXFrame(String title, int window_ID, Object object) {
		super(title
			, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
			, exitOnClose(window_ID)
			);
        
        /*
         * Frame Title Background Color cannot be set. It is controlled by OS.
         * see: https://stackoverflow.com/questions/2482971/how-to-change-the-color-of-titlebar-in-jframe
         * default: isUndecorated=false WindowDecorationStyle=0 (NONE)
         * activeCaption:ColorUIResource[r=184,g=207,b=229] #B8CFE5 / TROPICAL_BLUE
         * activeCaptionText:sun.swing.PrintColorUIResource[r=51,g=51,b=51] #333333 / NIGHT_RIDER
UIDefaults uiDefaults = UIManager.getDefaults();
uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.gray));
uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.white));
JFrame.setDefaultLookAndFeelDecorated(true);
         */
        // in XXX JFrame setBackground(UIManager.getColor("control")); [r=238,g=238,b=238] == EEEEEE
        LOG.info(">>>>>>>>>>> UIManager.getColor(\"control\":"+UIManager.getColor("control")
    		+" getBackground:"+super.getBackground() // ColorUIResource[r=238,g=238,b=238]
        	+" activeCaption:"+UIManager.get("activeCaption")
        	+" activeCaptionText:"+UIManager.get("activeCaptionText")
        	+" JFrame.isDefaultLookAndFeelDecorated="+JFrame.isDefaultLookAndFeelDecorated() // false
        	+"\n isUndecorated="+isUndecorated()+" WindowDecorationStyle="+getRootPane().getWindowDecorationStyle());
        /*
         * DefaultLookAndFeelDecorated ist false, ausser bei Metal.
         * d.h. OS hat die Kontrolle über Frame Title Background.
         * 
         * Es sei denn, mann setzt es explizit:
        JFrame.setDefaultLookAndFeelDecorated(true);
         */

//		super.setUndecorated(true);
//		super.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
       
		windowCounter++;
		this.windowNo = windowCounter-1;
		this.window_ID = window_ID;
		
		getRootPaneExt().setToolBar(new ToggleButtonToolBar()); // inner Class
		
		// bei RootFrame: setJMenuBar:
		if(this instanceof MainJXframe) {
			LOG.info("\nthis:"+this);
			setJMenuBar(new JMenuBar());
		}
	}
	
	DemoJXFrame(String title) { // für RootFrame
		this(title, -1, null);
	}

	public void setTitle(String title) {
		super.setTitle("#"+this.windowNo+":"+title);
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
	/**
	 * a special ToolBar with toggle Buttons
	 */
    protected class ToggleButtonToolBar extends JToolBar {
    	/**
    	 * ctor
    	 */
        public ToggleButtonToolBar() {
            super();
        }
        /**
         * add ToggleButton for Action a
         * @param a the Action
         * @return JToggleButton
         */
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
        /**
         * create ToggleComponent (JToggleButton) for an Action
         * @param a the Action
         * @return JToggleButton
         */
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

    /**
     * add Action To ToolBar
     * @param frame JXFrame with ToolBar
     * @param action to add
     * @return Button added to ToolBar
     */
	// aus InteractiveTestCase.createAndFillMenuBar:
    public AbstractButton addActionToToolBar(JXFrame frame, Action action) {
        JToolBar toolbar = frame.getRootPaneExt().getToolBar();
        if(toolbar instanceof ToggleButtonToolBar) {
//        	LOG.info("action:"+action);
        	ToggleButtonToolBar tbtb = (ToggleButtonToolBar)toolbar;
            AbstractButton button = tbtb.addToggleButton(action);
            button.setToolTipText((String)action.getValue(Action.SHORT_DESCRIPTION));
            button.setFocusable(false);
            if(action instanceof DemoAction) {
            	((DemoAction)action).setToggleButton(button);
            }
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
     * TODO
     * @return JMenu
     */
    public JMenu createDemosMenu() {
    	LOG.warning("**** nix ****");
    	return null;
    }

    /**
     * Creates a menu filled with one SetPlafAction for each of the currently installed LFs.
     * 
     * @param target the toplevel window to update, maybe null to indicate update
     *   of all application windows
     * @return the menu to use for plaf switching.
     */
    protected JMenu createPlafMenu(Window target) {
    	UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        JMenu menu = new JMenu("Set L&F");
        lafMenuGroup = new ButtonGroup(); // wg. mi.setSelected
        for (LookAndFeelInfo info : lafInfo) {
            JMenuItem mi = createLafMenuItem(info);
            LOG.fine("JMenuItem mi.Action:"+mi.getAction() + " ClassName="+info.getClassName());
            lafMenuGroup.add(mi);
            if(info.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
            	mi.setSelected(true);
            }
            menu.add(mi);
        }
        return menu;
    }
    
    /**
     * Creates a LaF JMenuItem for the Look and Feel Menu
     * @param info LookAndFeelInfo
     * @return JMenuItem
     */
    protected JMenuItem createLafMenuItem(UIManager.LookAndFeelInfo info) {
    	SetPlafAction action = new SetPlafAction(info.getName(), info.getClassName(), getLaFGroup(), this);
    	JMenuItem mi = new JRadioButtonMenuItem(action);
/*
    	// bei der Reihenfolge getLaFGroup().add(mi); dann mi.setSelected(true) ist JMenu setSelected gesetzt
    	// nicht aber das JPopupMenu
    	getLaFGroup().add(mi);
    	if(info.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
    		mi.setSelected(true);
    	}
    	// dreht man es um, so wird JPopupMenu korrekt angezeigt!
    	 */
    	if(info.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
    		mi.setSelected(true);
    	}
    	getLaFGroup().add(mi);
    	return mi;
    }

    /**
     * Creates a LaF JMenu for languages
     * @param target Window
     * @return JMenu
     */
    protected JMenu createLanguageMenu(Window target) {
    	Locale defaultLocale = JComponent.getDefaultLocale();
    	List<DisplayLocale> locales = new ArrayList<DisplayLocale>();
    	locales.add(new DisplayLocale(Locale.ENGLISH));
    	locales.add(new DisplayLocale("cs"));
    	locales.add(new DisplayLocale("es"));
    	locales.add(new DisplayLocale(Locale.FRENCH));
    	locales.add(new DisplayLocale(Locale.GERMAN));
    	locales.add(new DisplayLocale(new Locale("de", "CH")));
    	locales.add(new DisplayLocale(new Locale("fr", "CH")));
    	locales.add(new DisplayLocale(new Locale("it", "CH")));
    	locales.add(new DisplayLocale(new Locale("rm", "CH")));
    	locales.add(new DisplayLocale(Locale.ITALIAN));
    	locales.add(new DisplayLocale("nl"));
    	locales.add(new DisplayLocale("pl"));
    	locales.add(new DisplayLocale(new Locale("pt", "BR")));
    	locales.add(new DisplayLocale("sv"));
    	
        JMenu menu = new JMenu("Languages");
        ButtonGroup langMenuGroup = new ButtonGroup(); // wg. mi.setSelected
        
        locales.forEach( dl -> {
        	SetLanguageAction action = new SetLanguageAction(dl, target);
        	JMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(action));
        	mi.setText(dl.toString());
        	if(defaultLocale!=null && defaultLocale.toString().startsWith(dl.getLocale().toString())) mi.setSelected(true);
        	langMenuGroup.add(mi);
        });

        return menu;
    }

    JMenu themeMenu;
    /**
     * Creates a LaF JMenu for metal Themes Menu
     * @param target Window
     * @return JMenu
     */
    protected JMenu createThemeMenu(Window target) {
    	String[] themeInfo = { OCEAN , STEEL
    		, "swingset.plaf.AquaTheme"
    		, "swingset.plaf.CharcoalTheme"
    		, "swingset.plaf.ContrastTheme"
    		, "swingset.plaf.EmeraldTheme"
    		, "swingset.plaf.RubyTheme"
    		};
        themeMenu = new JMenu(StaticUtilities.getResourceAsString("ThemesMenu.themes.labelAndMnemonic", "Themes"));
        ButtonGroup themeMenuGroup = new ButtonGroup(); // wg. mi.setSelected
        for (String info : themeInfo) {
            JMenuItem mi = (JRadioButtonMenuItem) themeMenu.add(new JRadioButtonMenuItem(info));
            themeMenuGroup.add(mi);
            SetThemeAction action = new SetThemeAction(info, target);
        	mi.setAction(action);
        }
        // setEnabled when LAF is Metal
        themeMenu.setEnabled(isMETAL());
        if(isMETAL()) {
        	// set inital selected default: 
        	themeMenu.getItem("Steel".equals(MetalLookAndFeel.getCurrentTheme().getName()) ? 1 : 0).setSelected(true);
        }
        return themeMenu;
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

    /**
     * Action to toggle plaf and update all toplevel windows of the
     * current application. Used to setup the plaf-menu.
     */
    private static class SetPlafAction extends AbstractActionExt {

        private String plaf;
        private Window toplevel;

        @SuppressWarnings("unused")
        public SetPlafAction(String name, String plaf) {
            this(name, plaf, null, null);
        }
        
        /**
         * Instantiates an action which updates the toplevel window to the given LAF. 
         * 
         * @param name the name of the action
         * @param plaf the class name of the LAF to set
         * @param group identity of the state action
         * @param toplevel the window to update, may be null to indicate
         *   update of all application windows
         */
        public SetPlafAction(String name, String plaf, ButtonGroup group, Window toplevel) {
            super(name);
            super.setGroup(group);
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
            
            if(toplevel instanceof MainJXframe) {
            	MainJXframe rf = (MainJXframe)toplevel;
//            	// themeMenu setEnabled when LAF is Metal
            	rf.themeMenu.setEnabled(isMETAL());
//            	if(isMETAL()) {
//            		rf.themeMenu.setEnabled(true);
//        		LOG.info(">>> TODO setEnabled in Theme >>>>>>>>>>>>>>current:"+MetalLookAndFeel.getCurrentTheme());
////        	        setDefaultLookAndFeelDecorated(UIManager.getLookAndFeel().getSupportsWindowDecorations());
//            	} else {
//            		rf.themeMenu.setEnabled(false);
////        	        setDefaultLookAndFeelDecorated(false);
//            	}
            	
                // JPopupMenu Group (in AbstractActionExt) und JMenu Group synchronisieren:
            	Enumeration<AbstractButton> abEnum = rf.lafMenuGroup.getElements();
            	abEnum.asIterator().forEachRemaining(ab -> { 
            		if(e.getActionCommand().equals(ab.getText())) {
            			ab.setSelected(true);
            		}
            	});
            	abEnum = rf.getLaFGroup().getElements();
            	abEnum.asIterator().forEachRemaining(ab -> { 
            		if(e.getActionCommand().equals(ab.getText())) {
            			ab.setSelected(true);
            		}
            	});
            }
        }

    }

    private static class SetThemeAction extends AbstractAction {

    	/*
abstract class MetalTheme
class DefaultMetalTheme extends MetalTheme
class OceanTheme extends DefaultMetalTheme
       AquaTheme extends DefaultMetalTheme
       ...
    	 */
        Class<?> themeClass = null;
        private MetalTheme theme;
        private Window toplevel;

        public SetThemeAction(Class<?> themeClass, Window toplevel) {
        	this(themeClass.getName(), toplevel);
        	this.themeClass = themeClass;
        }
        /**
         * 
         * @param name == themeClass name
         * @param theme
         * @param toplevel
         */
        public SetThemeAction(String name, Window toplevel) {
            super(name);
            this.theme = null;
            this.toplevel = toplevel;
        }

		@Override
		public void actionPerformed(ActionEvent event) {
            if(!isMETAL()) return;
            		
			if (themeClass == null) {
				String className = (String) super.getValue(Action.NAME);
				try {
					themeClass = Class.forName(className); // throws ClassNotFoundException
				} catch (Exception e) {
					LOG.warning("Error occurred loading class: " + className);
					e.printStackTrace();
				}
			}
			try {
				theme = (MetalTheme)themeClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOG.info("theme:"+theme.getName() +", toplevel "+toplevel);
			try {
				MetalLookAndFeel.setCurrentTheme(theme);
				UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
	            if (toplevel != null) {
	                SwingUtilities.updateComponentTreeUI(toplevel);
	                if(toplevel instanceof MainJXframe) {
	                	MainJXframe rf = (MainJXframe)toplevel;
	                	Window w = rf.currentDemoFrame;
	                	if (w != null) SwingUtilities.updateComponentTreeUI(w);
	                }
	                // WindowFrame currentDemoFrame = null;
	            } else {
	                SwingXUtilities.updateAllComponentTreeUIs();
	            }
            } catch (Exception e1) {
                e1.printStackTrace();
                LOG.log(Level.FINE, "problem in setting MetalTheme", e1);
			}
		}
    	
    }

    private static class SetLanguageAction extends AbstractAction {
    	
    	private DisplayLocale dl;
        private Window toplevel;

        public SetLanguageAction(DisplayLocale dl, Window toplevel) {
            super(dl.getLocale().toString());
            this.dl = dl;
            this.toplevel = toplevel;
        }
        
		@Override
		public void actionPerformed(ActionEvent e) {
			if (toplevel != null) {
				LOG.info("Locale selected:"+dl + ", do setComponentTreeLocale for "+toplevel);
				SwingXUtilities.setComponentTreeLocale(toplevel, dl.getLocale());
                if(toplevel instanceof MainJXframe) {
                	MainJXframe rf = (MainJXframe)toplevel;
                	Window w = rf.currentDemoFrame;
                	if (w != null) SwingXUtilities.setComponentTreeLocale(w, dl.getLocale());
                }
			}
		}
    	
    }

}
