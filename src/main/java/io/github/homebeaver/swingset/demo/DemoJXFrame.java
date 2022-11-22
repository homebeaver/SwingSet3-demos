package io.github.homebeaver.swingset.demo;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
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
import org.jdesktop.swingx.demos.svg.FlagBR;
import org.jdesktop.swingx.demos.svg.FlagCH;
import org.jdesktop.swingx.demos.svg.FlagCS;
import org.jdesktop.swingx.demos.svg.FlagDE;
import org.jdesktop.swingx.demos.svg.FlagES;
import org.jdesktop.swingx.demos.svg.FlagFR;
import org.jdesktop.swingx.demos.svg.FlagIT;
import org.jdesktop.swingx.demos.svg.FlagNL;
import org.jdesktop.swingx.demos.svg.FlagPL;
import org.jdesktop.swingx.demos.svg.FlagSE;
import org.jdesktop.swingx.demos.svg.FlagUK;
import org.jdesktop.swingx.icon.SizingConstants;

import swingset.StaticUtilities;
import swingset.TextAndMnemonicUtils;

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
	
	// Einstellungen vor Frame ctor
	static private boolean exitOnClose(int window_ID) {
        UIManager.put(UI_KEY_BOLDMETAL, Boolean.FALSE); // turn off bold fonts in Metal
        // dieser key gilt nicht für Frame Titel, das macht:
		Font font = UIManager.getFont(UI_KEY_FRAME_TITLEFONT);
		UIManager.put(UI_KEY_FRAME_TITLEFONT, new Font(font.getName(), Font.PLAIN, font.getSize()));
		LOG.info("UI key "+UI_KEY_FRAME_TITLEFONT+" changed to "+UIManager.getFont(UI_KEY_FRAME_TITLEFONT) + " was "+font);
			
//		LOG.info("InternalFrame.closeIcon:"+UIManager.getIcon("InternalFrame.closeIcon"));

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

    private static final boolean USE_METALTITLEPANE = true;
	protected void frameInit() {
		if(USE_METALTITLEPANE || !JFrame.isDefaultLookAndFeelDecorated()) {
			super.frameInit();
			return;
		}
		frameInitPatch();
	}
	// siehe https://github.com/homebeaver/SwingSet/issues/25
	private void frameInitPatch() {
		// die UI-Farben retten:
		Color activeBackground = UIManager.getColor("activeCaption");
		Color activeForeground = UIManager.getColor("activeCaptionText");
		Color activeShadow = UIManager.getColor("activeCaptionBorder");

		// Code aus JFRame:
		enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
		setLocale( JComponent.getDefaultLocale() );
		setRootPane(createRootPane());
		setBackground(UIManager.getColor("control"));
		setRootPaneCheckingEnabled(true);
        if (JFrame.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
        		UIManager.put("activeCaption", this.getBackground()); // activeBackground wie this
// statt MetalLookAndFeel.getPrimaryControl() in activeBumps ==> 
//  (UIManager.get("InternalFrame.activeTitleGradient") != null) ? null :  MetalLookAndFeel.getPrimaryControl() );

        		
                setUndecorated(true);
                getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
                
                Component[] comps = getRootPane().getLayeredPane().getComponentsInLayer(JLayeredPane.FRAME_CONTENT_LAYER);
                for(int i=0; i<comps.length;i++) {
                	// es gibt zwei: 0: org.jdesktop.swingx.JXRootPane$1
                	// und interessanter 1:javax.swing.plaf.metal.MetalTitlePane - not visible class MetalTitlePane extends JComponent
/* instanziert in MetalRootPaneUI private 
    private JComponent createTitlePane(JRootPane root) {
        return new MetalTitlePane(root, this);
    }

 */
                	Component c = comps[i];
                	JComponent jc = (JComponent)c;
                	if(jc.getClass().getName().equals("javax.swing.plaf.metal.MetalTitlePane")) {
                    	LOG.info("--!!!-- "+i +":" +jc);
                		LOG.info("BG:"+jc.getBackground());
                	}
                }
                /* private MetalTitlePane
activeBG     javax.swing.plaf.ColorUIResource[r=184,g=207,b=229]
activeShadow javax.swing.plaf.ColorUIResource[r=163,g=184,b=204]

javax.swing.plaf.metal.MetalBumps@69e0b6f6
BG:    javax.swing.plaf.ColorUIResource[r=184,g=207,b=229]
shadow javax.swing.plaf.ColorUIResource[r=99,g=130,b=191]
top:   javax.swing.plaf.ColorUIResource[r=255,g=255,b=255]

activeBumpsHighlight javax.swing.plaf.ColorUIResource[r=255,g=255,b=255]
activeBumpsShadow    javax.swing.plaf.ColorUIResource[r=99,g=130,b=191]

                 */
            }
        }
        //sun.awt.SunToolkit.checkAndSetPolicy(this);
        
		// die UI-Farben wiederherstellen:
		UIManager.put("activeCaption", activeBackground);
		UIManager.put("activeCaptionText", activeForeground);
		UIManager.put("activeCaptionBorder", activeShadow);
        LOG.info("----PATCH done------");
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
    	locales.add(new DisplayLocale(Locale.ENGLISH, FlagUK.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale("cs", FlagCS.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale("es", FlagES.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(Locale.FRENCH, FlagFR.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(Locale.GERMAN, FlagDE.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(new Locale("de", "CH"), FlagDE.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(new Locale("fr", "CH"), FlagFR.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(new Locale("it", "CH"), FlagIT.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(new Locale("rm", "CH"), FlagCH.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(Locale.ITALIAN, FlagIT.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale("nl", FlagNL.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale("pl", FlagPL.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale(new Locale("pt", "BR"), FlagBR.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	locales.add(new DisplayLocale("sv", FlagSE.of(SizingConstants.ACTION_ICON, SizingConstants.SMALL_ICON)));
    	
        JMenu menu = new JMenu("Languages");
        ButtonGroup langMenuGroup = new ButtonGroup(); // wg. mi.setSelected
        
        locales.forEach( dl -> {
        	SetLanguageAction action = new SetLanguageAction(dl, target);
        	JMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(action));
        	mi.setText(dl.toString());
        	mi.setIcon(dl.getIcon());
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
    	String memuLabel = null; // the text for the menu label
    	try {
        	// getTextAndMnemonicString throws MissingResourceException
    		memuLabel = TextAndMnemonicUtils.getTextAndMnemonicString("ThemesMenu.themes.labelAndMnemonic");
    	} catch (MissingResourceException e) {
    		memuLabel = "Themes";
    	}
        themeMenu = new JMenu(memuLabel);
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
