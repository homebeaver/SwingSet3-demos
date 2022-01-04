/*
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.RootPaneContainer;
import javax.swing.SingleSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;

import swingset.plaf.AquaTheme;
import swingset.plaf.CharcoalTheme;
import swingset.plaf.ContrastTheme;
import swingset.plaf.EmeraldTheme;
import swingset.plaf.RubyTheme;

/**
 * A demo that shows all of the Swing components.
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (removed usage of JApplet, Applet, now subclass Panel)
 */
public class SwingSet2 extends JPanel {

	private static final long serialVersionUID = 3856695829171691102L;
	private static final Logger LOG = Logger.getLogger(SwingSet2.class.getName());

	// simple lass name (without package name)
    String[] demos = {
//      "InternalFrameDemo", see preloadFirstDemo()
      "ButtonDemo",
      "ColorChooserDemo",
      "ComboBoxDemo",
      "FileChooserDemo",
      "HtmlDemo",
      "ListDemo",
      "OptionPaneDemo",
      "ProgressBarDemo",
      "ScrollPaneDemo",
      "SliderDemo",
      "SplitPaneDemo",
      "TabbedPaneDemo",
      "TableDemo",
      "ToolTipDemo",
      "TreeDemo"
    };

	void loadDemos() {
		String pkgName = SwingSet2.class.getPackage().getName();
		for (int i = 0; i < demos.length;) {
			if (isApplet() && demos[i].equals("FileChooserDemo")) {
				// don't load the file chooser demo if we are applet
			} else {
				String demoClassName = pkgName + "." + demos[i];
				loadDemo(demoClassName);
			}
			i++;
		}
	}

    // The current Look & Feel
    private static String currentLookAndFeel;

    // List of demos
    private ArrayList<DemoModule> demosList = new ArrayList<DemoModule>();

    // The preferred size of the demo
    private static final int PREFERRED_WIDTH = 720;
    private static final int PREFERRED_HEIGHT = 640;

    // A place to hold on to the visible demo
    private DemoModule currentDemo = null;
    private JPanel demoPanel = null;

    // About Box
    private JDialog aboutBox = null;

    // Status Bar
    private JTextField statusField = null;

    // Tool Bar
    private ToggleButtonToolBar toolbar = null;
    private ButtonGroup toolbarGroup = new ButtonGroup();

    // Menus
    private JMenuBar menuBar = null;
    private JMenu lafMenu = null;
    private JMenu themesMenu = null;
    private JMenu audioMenu = null;
    private JMenu optionsMenu = null;
    private ButtonGroup lafMenuGroup = new ButtonGroup();
    private ButtonGroup themesMenuGroup = new ButtonGroup();
    private ButtonGroup audioMenuGroup = new ButtonGroup();

    // Popup menu
    private JPopupMenu popupMenu = null;
    private ButtonGroup popupMenuGroup = new ButtonGroup();

    // Used only if swingset is an application
    private JFrame frame = null;

    // Used only if swingset is an applet (dummy stub)
    private SwingSet2Applet applet = null;

    // To debug or not to debug, that is the question
    private boolean DEBUG = true;
    private int debugCounter = 0;

    // The tab pane that holds the demo
    private JTabbedPane tabbedPane = null;

    private JEditorPane demoSrcPane = null;

    // contentPane cache, saved from the applet or application frame
    Container contentPane = null;

    // number of swingsets - for multiscreen
    // keep track of the number of SwingSets created - we only want to exit
    // the program when the last one has been closed.
    private static int numSSs = 0;
    private static Vector<SwingSet2> swingSets = new Vector<SwingSet2>();

    private boolean dragEnabled = false;

    // Dummy stub for deprecated Applet
    @SuppressWarnings("serial")
	public class SwingSet2Applet extends Component implements RootPaneContainer {
    
    	// used in initializeDemo()
        public void setJMenuBar(final JMenuBar menuBar) {
			LOG.warning("dummy stub menuBar"+menuBar);
            getRootPane().setJMenuBar(menuBar);
        }

		@Override
		public JRootPane getRootPane() {
			LOG.info("**TODO** methode abgeschrieben von JApplet");
	        return SwingUtilities.getRootPane(this);
		}

		@Override
		public void setContentPane(Container contentPane) {
			LOG.info("**TODO** methode abgeschrieben von JApplet param Container:"+contentPane);
	        getRootPane().setContentPane(contentPane);
		}

		@Override
		public Container getContentPane() {
			LOG.info("**TODO** methode abgeschrieben von JApplet");
	        return getRootPane().getContentPane();		
		}

		@Override
		public void setLayeredPane(JLayeredPane layeredPane) {
			LOG.info("**TODO** methode abgeschrieben von JApplet param JLayeredPane:"+layeredPane);
	        getRootPane().setLayeredPane(layeredPane);
		}

		@Override
		public JLayeredPane getLayeredPane() {
			LOG.info("**TODO** methode abgeschrieben von JApplet");
	        return getRootPane().getLayeredPane();
		}

		@Override
		public void setGlassPane(Component glassPane) {
			LOG.info("**TODO** methode abgeschrieben von JApplet param Component:"+glassPane);
	        getRootPane().setGlassPane(glassPane);
		}

		@Override
		public Component getGlassPane() {
			LOG.info("**TODO** methode abgeschrieben von JApplet");
	        return getRootPane().getGlassPane();
		}

    }

	public SwingSet2(SwingSet2Applet applet) {
		this(applet, null);
	}
    
    public SwingSet2() {
		this(null, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
    }

    public SwingSet2(SwingSet2Applet applet, GraphicsConfiguration gc) {
		this(applet, gc, true);
    }
    /**
     * SwingSet2 Constructor
     */
    public SwingSet2(SwingSet2Applet applet, GraphicsConfiguration gc, boolean initAndStart) {

        // Note that applet may be null if this is started as an application
        this.applet = applet;

        currentLookAndFeel = UIManager.getLookAndFeel().getClass().getName();

        if (!isApplet()) {
            frame = createFrame(gc);
        }

        if(initAndStart) {
            // set the layout
            setLayout(new BorderLayout());
            // set the preferred size of the demo
            setPreferredSize(new Dimension(PREFERRED_WIDTH,PREFERRED_HEIGHT));

            initializeDemo();
            preloadFirstDemo();
            // Show the demo. Must do this on the GUI thread using invokeLater.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showSwingSet2();
                }
            });

            // Start loading the rest of the demo in the background
            DemoLoadThread demoLoader = new DemoLoadThread(this);
            demoLoader.start();
        }
    }

    /**
     * Bring up the SwingSet2 demo by showing the frame (only
     * applicable if coming up as an application, not an applet);
     */
	public void showSwingSet2() {
		if (!isApplet() && getFrame() != null) {
			// put swingset in a frame and show it
			JFrame f = getFrame();
			f.setTitle(getString("Frame.title"));
			f.getContentPane().add(this, BorderLayout.CENTER);
			f.pack();

			Rectangle screenRect = f.getGraphicsConfiguration().getBounds();
			Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(f.getGraphicsConfiguration());

			// Make sure we don't place the demo off the screen.
			int centerWidth = screenRect.width < f.getSize().width ? screenRect.x
					: screenRect.x + screenRect.width / 2 - f.getSize().width / 2;
			int centerHeight = screenRect.height < f.getSize().height ? screenRect.y
					: screenRect.y + screenRect.height / 2 - f.getSize().height / 2;

			centerHeight = centerHeight < screenInsets.top ? screenInsets.top : centerHeight;

			f.setLocation(centerWidth, centerHeight);
			f.setVisible(true);
			numSSs++;
			swingSets.add(this);
		}
	}

    /**
     * SwingSet2 Main. Called only if we're an application, not an applet.
     */
    public static void main(String[] args) {
    // Create SwingSet on the default monitor
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        SwingSet2 swingset = new SwingSet2(null, GraphicsEnvironment.
                                             getLocalGraphicsEnvironment().
                                             getDefaultScreenDevice().
                                             getDefaultConfiguration());
    }

    
    // *******************************************************
    // *************** Demo Loading Methods ******************
    // *******************************************************

	public void initializeDemo() {
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);

		menuBar = createMenus();

		if (isApplet()) {
			applet.setJMenuBar(menuBar);
		} else {
			frame.setJMenuBar(menuBar);
		}

		ToolBarPanel toolbarPanel = new ToolBarPanel();
		toolbarPanel.setLayout(new BorderLayout());
		toolbar = new ToggleButtonToolBar();
		toolbarPanel.add(toolbar, BorderLayout.CENTER);
		top.add(toolbarPanel, BorderLayout.SOUTH);
		toolbarPanel.addContainerListener(toolbarPanel);

		tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.getModel().addChangeListener(e -> {
            SingleSelectionModel model = (SingleSelectionModel) e.getSource();
            boolean srcSelected = model.getSelectedIndex() == 1;
            if(currentTabDemo != currentDemo && demoSrcPane != null && srcSelected) {
                demoSrcPane.setText(getString("SourceCode.loading"));
                repaint();
            }
            if(currentTabDemo != currentDemo && srcSelected) {
                currentTabDemo = currentDemo;
                setSourceCode(currentDemo);
            }
		});

		statusField = new JTextField("");
		statusField.setEditable(false);
		add(statusField, BorderLayout.SOUTH);

		demoPanel = new JPanel();
		demoPanel.setLayout(new BorderLayout());
		demoPanel.setBorder(new EtchedBorder());
		tabbedPane.addTab("Hi There!", demoPanel);

		// Add html src code viewer
		demoSrcPane = new JEditorPane("text/html", getString("SourceCode.loading"));
		demoSrcPane.setEditable(false);

		JScrollPane scroller = new JScrollPane();
		scroller.getViewport().add(demoSrcPane);

		tabbedPane.addTab(getString("TabbedPane.src_label")
				, null
				, scroller
				, getString("TabbedPane.src_tooltip"));
		
		// creates popupMenu accessible via keyboard
		popupMenu = createPopupMenu();
		
		LOG.config("initialized.\n");
	}

    DemoModule currentTabDemo = null;

    /**
     * Loads and puts the source code text into JEditorPane in the "Source Code" tab
     */
    public void setSourceCode(DemoModule demo) {
        // do the following on the gui thread
        SwingUtilities.invokeLater(new SwingSetRunnable(this, demo) {
            public void run() {
                swingset.demoSrcPane.setText(((DemoModule)obj).getSourceCode());
                swingset.demoSrcPane.setCaretPosition(0);
            }
        });
    }

    /**
     * Create menus
     */
    public JMenuBar createMenus() {
        JMenuItem mi;
        // ***** create the menubar ****
        JMenuBar menuBar = new JMenuBar();
        menuBar.getAccessibleContext().setAccessibleName(
            getString("MenuBar.accessible_description"));

        // ***** create File menu
        JMenu fileMenu = (JMenu) menuBar.add(new JMenu(getString("FileMenu.file_label")));
        fileMenu.setMnemonic(getMnemonic("FileMenu.file_mnemonic"));
        fileMenu.getAccessibleContext().setAccessibleDescription(getString("FileMenu.accessible_description"));

        createMenuItem(fileMenu, "FileMenu.about_label", "FileMenu.about_mnemonic",
                       "FileMenu.about_accessible_description", new AboutAction(this));

        fileMenu.addSeparator();

        createMenuItem(fileMenu, "FileMenu.open_label", "FileMenu.open_mnemonic",
                       "FileMenu.open_accessible_description", null);

        createMenuItem(fileMenu, "FileMenu.save_label", "FileMenu.save_mnemonic",
                       "FileMenu.save_accessible_description", null);

        createMenuItem(fileMenu, "FileMenu.save_as_label", "FileMenu.save_as_mnemonic",
                       "FileMenu.save_as_accessible_description", null);


        if(!isApplet()) {
            fileMenu.addSeparator();

            createMenuItem(fileMenu, "FileMenu.exit_label", "FileMenu.exit_mnemonic",
                           "FileMenu.exit_accessible_description", new ExitAction(this)
            );
        }

        // Create these menu items for the first SwingSet only.
        if (numSSs == 0) {
        // ***** create laf switcher menu
        lafMenu = (JMenu) menuBar.add(new JMenu(getString("LafMenu.laf_label")));
        lafMenu.setMnemonic(getMnemonic("LafMenu.laf_mnemonic"));
        lafMenu.getAccessibleContext().setAccessibleDescription(
            getString("LafMenu.laf_accessible_description"));

        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();

        for (int counter = 0; counter < lafInfo.length; counter++) {
            String className = lafInfo[counter].getClassName();
            final boolean selected = className.equals(currentLookAndFeel);
            mi = createLafMenuItem(lafMenu, lafInfo[counter]);
            mi.setSelected(selected);
        }

        // ***** create themes menu
        themesMenu = (JMenu) menuBar.add(new JMenu(getString("ThemesMenu.themes_label")));
        themesMenu.setMnemonic(getMnemonic("ThemesMenu.themes_mnemonic"));
        themesMenu.getAccessibleContext().setAccessibleDescription(
            getString("ThemesMenu.themes_accessible_description"));

        // ***** create the audio submenu under the theme menu
        audioMenu = (JMenu) themesMenu.add(new JMenu(getString("AudioMenu.audio_label")));
        audioMenu.setMnemonic(getMnemonic("AudioMenu.audio_mnemonic"));
        audioMenu.getAccessibleContext().setAccessibleDescription(
            getString("AudioMenu.audio_accessible_description"));

        createAudioMenuItem(audioMenu, "AudioMenu.on_label",
                            "AudioMenu.on_mnemonic",
                            "AudioMenu.on_accessible_description",
                            new OnAudioAction(this));

        mi = createAudioMenuItem(audioMenu, "AudioMenu.default_label",
                                 "AudioMenu.default_mnemonic",
                                 "AudioMenu.default_accessible_description",
                                 new DefaultAudioAction(this));
        mi.setSelected(true); // This is the default feedback setting

        createAudioMenuItem(audioMenu, "AudioMenu.off_label",
                            "AudioMenu.off_mnemonic",
                            "AudioMenu.off_accessible_description",
                            new OffAudioAction(this));


        // ***** create the font submenu under the theme menu
        JMenu fontMenu = (JMenu) themesMenu.add(new JMenu(getString("FontMenu.fonts_label")));
        fontMenu.setMnemonic(getMnemonic("FontMenu.fonts_mnemonic"));
        fontMenu.getAccessibleContext().setAccessibleDescription(
            getString("FontMenu.fonts_accessible_description"));
        ButtonGroup fontButtonGroup = new ButtonGroup();
        mi = createButtonGroupMenuItem(fontMenu, "FontMenu.plain_label",
                "FontMenu.plain_mnemonic",
                "FontMenu.plain_accessible_description",
                new ChangeFontAction(this, true), fontButtonGroup);
        mi.setSelected(true);
        mi = createButtonGroupMenuItem(fontMenu, "FontMenu.bold_label",
                "FontMenu.bold_mnemonic",
                "FontMenu.bold_accessible_description",
                new ChangeFontAction(this, false), fontButtonGroup);



        // *** now back to adding color/font themes to the theme menu
        mi = createThemesMenuItem(themesMenu, "ThemesMenu.ocean_label",
                                              "ThemesMenu.ocean_mnemonic",
                                              "ThemesMenu.ocean_accessible_description",
                                              new OceanTheme());
        mi.setSelected(true); // This is the default theme

        createThemesMenuItem(themesMenu, "ThemesMenu.steel_label",
                             "ThemesMenu.steel_mnemonic",
                             "ThemesMenu.steel_accessible_description",
                             new DefaultMetalTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.aqua_label", "ThemesMenu.aqua_mnemonic",
                       "ThemesMenu.aqua_accessible_description", new AquaTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.charcoal_label", "ThemesMenu.charcoal_mnemonic",
                       "ThemesMenu.charcoal_accessible_description", new CharcoalTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.contrast_label", "ThemesMenu.contrast_mnemonic",
                       "ThemesMenu.contrast_accessible_description", new ContrastTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.emerald_label", "ThemesMenu.emerald_mnemonic",
                       "ThemesMenu.emerald_accessible_description", new EmeraldTheme());

        createThemesMenuItem(themesMenu, "ThemesMenu.ruby_label", "ThemesMenu.ruby_mnemonic",
                       "ThemesMenu.ruby_accessible_description", new RubyTheme());

        // ***** create the options menu
        optionsMenu = (JMenu)menuBar.add(
            new JMenu(getString("OptionsMenu.options_label")));
        optionsMenu.setMnemonic(getMnemonic("OptionsMenu.options_mnemonic"));
        optionsMenu.getAccessibleContext().setAccessibleDescription(
            getString("OptionsMenu.options_accessible_description"));

        // ***** create tool tip submenu item.
        mi = createCheckBoxMenuItem(optionsMenu, "OptionsMenu.tooltip_label",
                "OptionsMenu.tooltip_mnemonic",
                "OptionsMenu.tooltip_accessible_description",
                new ToolTipAction());
        mi.setSelected(true);

        // ***** create drag support submenu item.
        createCheckBoxMenuItem(optionsMenu, "OptionsMenu.dragEnabled_label",
                "OptionsMenu.dragEnabled_mnemonic",
                "OptionsMenu.dragEnabled_accessible_description",
                new DragSupportAction());

        }


        // ***** create the multiscreen menu, if we have multiple screens
    if (!isApplet()) {
        GraphicsDevice[] screens = GraphicsEnvironment.
                                    getLocalGraphicsEnvironment().
                                    getScreenDevices();
        LOG.config("number of screens is "+screens.length);
        if (screens.length > 1) {

            JMenu multiScreenMenu = (JMenu) menuBar.add(new JMenu(
                                     getString("MultiMenu.multi_label")));

            multiScreenMenu.setMnemonic(getMnemonic("MultiMenu.multi_mnemonic"));
            multiScreenMenu.getAccessibleContext().setAccessibleDescription(
             getString("MultiMenu.multi_accessible_description"));

            createMultiscreenMenuItem(multiScreenMenu, MultiScreenAction.ALL_SCREENS);
            for (int i = 0; i < screens.length; i++) {
                createMultiscreenMenuItem(multiScreenMenu, i);
            }
        }
    }

        return menuBar;
    }

    /**
     * Create a checkbox menu menu item
     */
    private JMenuItem createCheckBoxMenuItem(JMenu menu, String label,
                                             String mnemonic,
                                             String accessibleDescription,
                                             Action action) {
        JCheckBoxMenuItem mi = (JCheckBoxMenuItem)menu.add(
                new JCheckBoxMenuItem(getString(label)));
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(
                accessibleDescription));
        mi.addActionListener(action);
        return mi;
    }

    /**
     * Create a radio button menu menu item for items that are part of a
     * button group.
     */
    private JMenuItem createButtonGroupMenuItem(JMenu menu, String label,
                                                String mnemonic,
                                                String accessibleDescription,
                                                Action action,
                                                ButtonGroup buttonGroup) {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem)menu.add(
                new JRadioButtonMenuItem(getString(label)));
        buttonGroup.add(mi);
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(
                accessibleDescription));
        mi.addActionListener(action);
        return mi;
    }

    /**
     * Create the theme's audio submenu
     */
    public JMenuItem createAudioMenuItem(JMenu menu, String label,
                                         String mnemonic,
                                         String accessibleDescription,
                                         Action action) {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(getString(label)));
        audioMenuGroup.add(mi);
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(action);

        return mi;
    }

    /**
     * Creates a generic menu item
     */
    public JMenuItem createMenuItem(JMenu menu, String label, String mnemonic,
                               String accessibleDescription, Action action) {
        JMenuItem mi = (JMenuItem) menu.add(new JMenuItem(getString(label)));
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(action);
        if(action == null) {
            mi.setEnabled(false);
        }
        return mi;
    }

    /**
     * Creates a JRadioButtonMenuItem for the Themes menu
     */
    public JMenuItem createThemesMenuItem(JMenu menu, String label, String mnemonic,
                               String accessibleDescription, MetalTheme theme) {
        JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(getString(label)));
        themesMenuGroup.add(mi);
        mi.setMnemonic(getMnemonic(mnemonic));
        mi.getAccessibleContext().setAccessibleDescription(getString(accessibleDescription));
        mi.addActionListener(new ChangeThemeAction(this, theme));

        return mi;
    }

    /**
     * Creates a JRadioButtonMenuItem for the Look and Feel menu
     */
    public JMenuItem createLafMenuItem(JMenu menu, UIManager.LookAndFeelInfo lafInfo) {
        JMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(lafInfo.getName()));
        lafMenuGroup.add(mi);
        mi.addActionListener(new ChangeLookAndFeelAction(this, lafInfo.getClassName()));
        mi.setEnabled(isAvailableLookAndFeel(lafInfo.getClassName()));
        return mi;
    }
    
    /**
     * Creates a multi-screen menu item
     */
    public JMenuItem createMultiscreenMenuItem(JMenu menu, int screen) {
        JMenuItem mi = null;
        if (screen == MultiScreenAction.ALL_SCREENS) {
            mi = (JMenuItem) menu.add(new JMenuItem(getString("MultiMenu.all_label")));
            mi.setMnemonic(getMnemonic("MultiMenu.all_mnemonic"));
            mi.getAccessibleContext().setAccessibleDescription(getString(
                                                                 "MultiMenu.all_accessible_description"));
        }
        else {
            mi = (JMenuItem) menu.add(new JMenuItem(getString("MultiMenu.single_label") + " " +
                                                                                                 screen));
            mi.setMnemonic(KeyEvent.VK_0 + screen);
            mi.getAccessibleContext().setAccessibleDescription(getString(
                                               "MultiMenu.single_accessible_description") + " " + screen);

        }
        mi.addActionListener(new MultiScreenAction(this, screen));
        return mi;
    }

    /* <snip> PopupMenu
     * a small popup menu, activated via keyboard SHIFT_DOWN+F10
     * shows items with InstalledLookAndFeels
     * and a class ActivatePopupMenuAction with ActionEvent on SHIFT_DOWN+F10
     */
    public JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu("JPopupMenu demo");

        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        JMenuItem mi = null;
        for (int counter = 0; counter < lafInfo.length; counter++) {
        	String classname = lafInfo[counter].getClassName();
        	final boolean selected = classname.equals(SwingSet2.currentLookAndFeel);
        	mi = createPopupMenuItem(popupMenu, lafInfo[counter]);
        	mi.setSelected(selected);
        }

        // register key binding to activate popupMenu
        InputMap map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.SHIFT_DOWN_MASK), "postMenuAction");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_CONTEXT_MENU, 0), "postMenuAction");
        getActionMap().put("postMenuAction", new ActivatePopupMenuAction(this, popupMenu));

        return popupMenu;
    }

    /**
     * Creates a JMenuItem for the Look and Feel popupMenu
     */
    public JMenuItem createPopupMenuItem(JPopupMenu menu, UIManager.LookAndFeelInfo lafInfo) {
    	JMenuItem mi = menu.add(new JMenuItem(lafInfo.getName()));
    	popupMenuGroup.add(mi);
    	mi.addActionListener(new ChangeLookAndFeelAction(this, lafInfo.getClassName()));
    	return mi;
    }

    class ActivatePopupMenuAction extends AbstractAction {
		private static final long serialVersionUID = 3925663480989462160L;
		SwingSet2 swingset;
        JPopupMenu popup;
        
        protected ActivatePopupMenuAction(SwingSet2 swingset, JPopupMenu popupMenu) {
            super("ActivatePopupMenu"); // the name for the action
            this.swingset = swingset;
            this.popup = popupMenu;
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
        	LOG.fine("event:"+e);
            Dimension invokerSize = getSize();
            Dimension popupSize = popup.getPreferredSize();
            popup.show(swingset, 
            		(invokerSize.width - popupSize.width) / 2,
            		(invokerSize.height - popupSize.height) / 2
            	);
        }
    }
    // </snip> PopupMenu

    /**
     * Load the first demo. 
     * This is done separately from the remaining demos
     * so that we can get SwingSet2 up and available to the user quickly.
     */
    public void preloadFirstDemo() {
        DemoModule demo = addDemo(new InternalFrameDemo(this));
        setCurrentDemo(demo);
    }

    /**
     * Add a demo to the toolbar
     */
    private DemoModule addDemo(DemoModule demo) {
    	return addDemo(demo, demo==null ? null : demo.getClass());
    }
    public DemoModule addDemo(DemoModule demo, Class<?> demoClass) {
    	if(demoClass==null) LOG.severe("*** demoClass==null !!!!");
        demosList.add(demo); // TODO EUG auch null landet so in demosList
        if (dragEnabled) {
            demo.updateDragEnabled(true);
        }
        // do the following on the gui thread
        SwingUtilities.invokeLater(new SwingSetRunnable(this, demo) {
        	// overrides swingset.SwingSet2.SwingSetRunnable.run
            public void run() {
            	// add switchToDemoAction to ToolBar
                SwitchToDemoAction action = new SwitchToDemoAction(swingset, (DemoModule) obj, demoClass);
                JToggleButton tb = swingset.getToolBar().addToggleButton(action);
                swingset.getToolBarGroup().add(tb);
                if(swingset.getToolBarGroup().getSelection() == null) {
                    tb.setSelected(true);
                }
                tb.setText(null);
                tb.setToolTipText(getString(demoClass.getSimpleName() + ".tooltip"));
                
                setStatus(getString("Status.popupMenuAccessible"));
            }
        });
        return demo;
    }


    /**
     * Sets the current demo
     */
    public void setCurrentDemo(DemoModule demo) {
    	LOG.config("old:"+currentDemo + " new:"+demo);
        currentDemo = demo;

        // Ensure panel's UI is current before making visible
        JComponent currentDemoPanel = demo.getDemoPanel();
        SwingUtilities.updateComponentTreeUI(currentDemoPanel);

        demoPanel.removeAll();
        demoPanel.add(currentDemoPanel, BorderLayout.CENTER);

        tabbedPane.setSelectedIndex(0);
        tabbedPane.setTitleAt(0, demo.getName());
        tabbedPane.setToolTipTextAt(0, demo.getToolTip());
    }


    // *******************************************************
    // ****************** Utility Methods ********************
    // *******************************************************

    /**
     * Loads a demo from a classname, but does not instantiate
     */
    Class<?> loadDemo(String classname) {
        setStatus(getString("Status.loading") + classname);
        
        Class<?> demoClass = null;
        DemoModule demo = null;
        try {
            demoClass = Class.forName(classname); // throws ClassNotFoundException
            addDemo(null, demoClass);
        } catch (Exception e) {
        	LOG.warning("Error occurred loading demo: " + classname);
            e.printStackTrace();
        }
        return demoClass;
    }
    
    private DemoModule getInstanceOf(Class<?> demoClass) throws NoSuchMethodException, SecurityException
    		, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> demoConstructor = demoClass.getConstructor(new Class[]{SwingSet2.class}); // throws NoSuchMethodException, SecurityException
        DemoModule demo = (DemoModule) demoConstructor.newInstance(new Object[]{this});
        return demo;
    }
    

    /**
     * A utility function that layers on top of the LookAndFeel's
     * isSupportedLookAndFeel() method. Returns true if the LookAndFeel
     * is supported. Returns false if the LookAndFeel is not supported
     * and/or if there is any kind of error checking if the LookAndFeel
     * is supported.
     *
     * The L&F menu will use this method to detemine whether the various
     * L&F options should be active or inactive.
     *
     */
     protected boolean isAvailableLookAndFeel(String laf) {
         try {
             Class<?> lnfClass = Class.forName(laf);
             LookAndFeel newLAF = (LookAndFeel)(lnfClass.getDeclaredConstructor().newInstance());
             return newLAF.isSupportedLookAndFeel();
         } catch(Exception e) { // If ANYTHING weird happens, return false
/*
IllegalAccessException: class swingset.SwingSet2 cannot access class com.sun.java.swing.plaf.motif.MotifLookAndFeel (in module java.desktop) because module java.desktop does not export com.sun.java.swing.plaf.motif to unnamed module @35fb3008
IllegalAccessException: class swingset.SwingSet2 cannot access class com.sun.java.swing.plaf.windows.WindowsLookAndFeel (in module java.desktop) because module java.desktop does not export com.sun.java.swing.plaf.windows to unnamed module @35fb3008
IllegalAccessException: class swingset.SwingSet2 cannot access class com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel 
	(in module java.desktop) because module java.desktop 
	does not export com.sun.java.swing.plaf.windows to unnamed module @35fb3008
ABER:
via PopupMenu shift-F10 kann ich alle drei aktivieren.
 */
        	 LOG.config(laf + " liefert "+e);
             return false;
         }
     }

    /**
     * Determines if this is an applet or application
     */
    public boolean isApplet() {
    	if(applet==null) return false;
    	LOG.warning("NEVER - applet is replaced by a dummy!!!");
        return (applet != null);
    }

    /**
     * Returns the applet instance
     */
    public SwingSet2Applet getApplet() {
        return applet;
    }

    /**
     * Returns the frame instance
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Returns the menubar
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Returns the toolbar
     */
    public ToggleButtonToolBar getToolBar() {
        return toolbar;
    }

    /**
     * Returns the toolbar button group
     */
    public ButtonGroup getToolBarGroup() {
        return toolbarGroup;
    }

    /**
     * Returns the content pane wether we're in an applet
     * or application
     */
    public Container getContentPane() {
        if(contentPane == null) {
            if(getFrame() != null) {
                contentPane = getFrame().getContentPane();
            } else if (getApplet() != null) {
                contentPane = getApplet().getContentPane();
            }
        }
        return contentPane;
    }

    /**
     * Create a frame for SwingSet2 to reside in if brought up
     * as an application.
     */
    public static JFrame createFrame(GraphicsConfiguration gc) {
        JFrame frame = new JFrame(gc);
        if (numSSs == 0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            WindowListener l = new WindowAdapter() {
            	@Override
                public void windowClosing(WindowEvent e) {
                    numSSs--;
                    swingSets.remove(this);
                }
            };
            frame.addWindowListener(l);
        }
        return frame;
    }


    /**
     * Set the status
     */
    public void setStatus(String s) {
        // do the following on the gui thread
        SwingUtilities.invokeLater(new SwingSetRunnable(this, s) {
            public void run() {
                swingset.statusField.setText((String) obj);
            }
        });
    }


    /**
     * This method returns a string from the demo's resource bundle.
     */
    public String getString(String key) {
        String value = null;
        try {
            value = TextAndMnemonicUtils.getTextAndMnemonicString(key);
        } catch (MissingResourceException e) {
            System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
        }
        if(value == null) {
            value = "Could not find resource: " + key + "  ";
        }
        return value;
    }

    void setDragEnabled(boolean dragEnabled) {
        if (dragEnabled == this.dragEnabled) {
            return;
        }
        this.dragEnabled = dragEnabled;
        for (DemoModule dm : demosList) {
        	LOG.info("DemoModule #"+demosList.size()+" dm:"+dm);
        	// BUG TODO EUG: nicht alle dm sind instanziert, beim späteren instanzieren updateDragEnabled setzen
            if(dm!=null) dm.updateDragEnabled(dragEnabled);
        }
        demoSrcPane.setDragEnabled(dragEnabled);
    }

    boolean isDragEnabled() {
        return dragEnabled;
    }


    /**
     * Returns a mnemonic from the resource bundle. Typically used as
     * keyboard shortcuts in menu items.
     */
    public char getMnemonic(String key) {
        return (getString(key)).charAt(0);
    }

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public ImageIcon createImageIcon(String filename, String description) {
    	String path = "/swingset/images/" + filename; 
        return new ImageIcon(getClass().getResource(path));
    }

    /**
     * If DEBUG is defined, prints debug information out to std ouput.
     */
    public void debug(String s) {
        if(DEBUG) {
            System.out.println((debugCounter++) + ": " + s);
        }
    }

    /**
     * Stores the current L&F, and calls updateLookAndFeel, below
     */
    public void setLookAndFeel(String laf) {
        if(!currentLookAndFeel.equals(laf)) {
            currentLookAndFeel = laf;
            /* The recommended way of synchronizing state between multiple
             * controls that represent the same command is to use Actions.
             * The code below is a workaround and will be replaced in future
             * version of SwingSet2 demo.
             */
            UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
            String lafName = null;
            for(int counter = 0; counter<lafInfo.length; counter++) {
            	String classname = lafInfo[counter].getClassName();
            	if(classname.equals(laf)) {
            		lafName = lafInfo[counter].getName();
            		break;
            	}
            }
            themesMenu.setEnabled(laf.equals("javax.swing.plaf.metal.MetalLookAndFeel"));
            updateLookAndFeel();
            for(int i=0;i<lafMenu.getItemCount();i++) {
                JMenuItem item = lafMenu.getItem(i);
                item.setSelected(item.getText().equals(lafName));
            }
        }
    }

    private void updateThisSwingSet() {
        if (isApplet()) {
            SwingUtilities.updateComponentTreeUI(getApplet());
        } else {
            JFrame frame = getFrame();
            if (frame == null) {
                SwingUtilities.updateComponentTreeUI(this);
            } else {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        }

        SwingUtilities.updateComponentTreeUI(popupMenu);
        if (aboutBox != null) {
            SwingUtilities.updateComponentTreeUI(aboutBox);
        }
    }

    /**
     * Sets the current L&F on each demo module
     */
    public void updateLookAndFeel() {
        try {
            UIManager.setLookAndFeel(currentLookAndFeel);
            if (isApplet()) {
                updateThisSwingSet();
            } else {
                for (SwingSet2 ss : swingSets) {
                    ss.updateThisSwingSet();
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed loading L&F: " + currentLookAndFeel);
            System.out.println(ex);
        }
    }

    // *******************************************************
    // **************   ToggleButtonToolbar  *****************
    // *******************************************************
    static Insets zeroInsets = new Insets(1,1,1,1);
    protected class ToggleButtonToolBar extends JToolBar {
        public ToggleButtonToolBar() {
            super();
        }

        JToggleButton addToggleButton(Action a) {
            JToggleButton tb = new JToggleButton(
                (String)a.getValue(Action.NAME),
                (Icon)a.getValue(Action.SMALL_ICON)
            );
            tb.setMargin(zeroInsets);
            tb.setText(null);
            tb.setEnabled(a.isEnabled());
            tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
            tb.setAction(a);
            add(tb);
            return tb;
        }
    }

    // *******************************************************
    // *********  ToolBar Panel / Docking Listener ***********
    // *******************************************************
    class ToolBarPanel extends JPanel implements ContainerListener {

        public boolean contains(int x, int y) {
            Component c = getParent();
            if (c != null) {
                Rectangle r = c.getBounds();
                return (x >= 0) && (x < r.width) && (y >= 0) && (y < r.height);
            }
            else {
                return super.contains(x,y);
            }
        }

        public void componentAdded(ContainerEvent e) {
            Container c = e.getContainer().getParent();
            if (c != null) {
                c.getParent().validate();
                c.getParent().repaint();
            }
        }

        public void componentRemoved(ContainerEvent e) {
            Container c = e.getContainer().getParent();
            if (c != null) {
                c.getParent().validate();
                c.getParent().repaint();
            }
        }
    }

    // *******************************************************
    // ******************   Runnables  ***********************
    // *******************************************************

    /**
     * Generic SwingSet2 runnable. This is intended to run on the
     * AWT gui event thread so as not to muck things up by doing
     * gui work off the gui thread. Accepts a SwingSet2 and an Object
     * as arguments, which gives subtypes of this class the two
     * "must haves" needed in most runnables for this demo.
     */
    class SwingSetRunnable implements Runnable {
        protected SwingSet2 swingset;
        protected Object obj;

        public SwingSetRunnable(SwingSet2 swingset, Object obj) {
            this.swingset = swingset;
            this.obj = obj;
        }

        public void run() {
        	LOG.warning("run() should be overwritten by subclass!!! obj:"+obj);
        }
    }


    // *******************************************************
    // ********************   Actions  ***********************
    // *******************************************************

    //                 interface Action extends ActionListener 
    // AbstractAction implements Action, Cloneable, Serializable
    public class SwitchToDemoAction extends AbstractAction {
        SwingSet2 swingset;
        DemoModule demo;
        Class<?> demoClass;

        public SwitchToDemoAction(SwingSet2 swingset, DemoModule demo, Class<?> demoClass) {
            super(demoClass.getSimpleName(), swingset.createImageIcon(getIconPath(demoClass), null));
            this.swingset = swingset;
            this.demo = demo;
            this.demoClass = demoClass;
        }

        private static String getIconPath(Class<?> demoClass) {
        	try {
				Field field = demoClass.getField("ICON_PATH");
				Object value = field.get(null);
				return (String)value;
			} catch (NoSuchFieldException | SecurityException e) {
				// thrown by getField
				e.printStackTrace();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// thrown by get
				e.printStackTrace();
			}
        	return "toolbar/JMenu.gif"; // any default
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
        	LOG.info("make an instance of "+demoClass+" ActionEvent e:"+e);
            try {
            	this.demo = swingset.getInstanceOf(demoClass);
            	// TODO demosList aktualisieren
            } catch (Exception ex) {
            	LOG.warning("Error occurred creating instance of " + demoClass);
                ex.printStackTrace();
            }
            swingset.setCurrentDemo(demo);
        }
    }

    class ChangeLookAndFeelAction extends AbstractAction {
        SwingSet2 swingset;
        String laf;
        protected ChangeLookAndFeelAction(SwingSet2 swingset, String laf) {
            super("ChangeTheme");
            this.swingset = swingset;
            this.laf = laf;
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
            swingset.setLookAndFeel(laf);
        }
    }

    // Turns on all possible auditory feedback
    class OnAudioAction extends AbstractAction {
        SwingSet2 swingset;
        protected OnAudioAction(SwingSet2 swingset) {
            super("Audio On");
            this.swingset = swingset;
        }
        public void actionPerformed(ActionEvent e) {
            UIManager.put("AuditoryCues.playList",
                          UIManager.get("AuditoryCues.allAuditoryCues"));
            swingset.updateLookAndFeel();
        }
    }

    // Turns on the default amount of auditory feedback
    class DefaultAudioAction extends AbstractAction {
        SwingSet2 swingset;
        protected DefaultAudioAction(SwingSet2 swingset) {
            super("Audio Default");
            this.swingset = swingset;
        }
        public void actionPerformed(ActionEvent e) {
            UIManager.put("AuditoryCues.playList",
                          UIManager.get("AuditoryCues.defaultCueList"));
            swingset.updateLookAndFeel();
        }
    }

    // Turns off all possible auditory feedback
    class OffAudioAction extends AbstractAction {
        SwingSet2 swingset;
        protected OffAudioAction(SwingSet2 swingset) {
            super("Audio Off");
            this.swingset = swingset;
        }
        public void actionPerformed(ActionEvent e) {
            UIManager.put("AuditoryCues.playList",
                          UIManager.get("AuditoryCues.noAuditoryCues"));
            swingset.updateLookAndFeel();
        }
    }

    // Turns on or off the tool tips for the demo.
    class ToolTipAction extends AbstractAction {
        protected ToolTipAction() {
            super("ToolTip Control");
        }

        public void actionPerformed(ActionEvent e) {
            boolean status = ((JCheckBoxMenuItem)e.getSource()).isSelected();
            ToolTipManager.sharedInstance().setEnabled(status);
        }
    }

    class DragSupportAction extends AbstractAction {
        protected DragSupportAction() {
            super("DragSupport Control");
        }

        public void actionPerformed(ActionEvent e) {
            boolean dragEnabled = ((JCheckBoxMenuItem)e.getSource()).isSelected();
            if (isApplet()) {
                setDragEnabled(dragEnabled);
            } else {
//            	LOG.info("ActionEvent e:"+e);
//            	LOG.info("dragEnabled="+dragEnabled+" swingSets.size:"+(swingSets==null?"null":swingSets.size()));
                for (SwingSet2 ss : swingSets) {
                    ss.setDragEnabled(dragEnabled);
                }
            }
        }
    }

    class ChangeThemeAction extends AbstractAction {
        SwingSet2 swingset;
        MetalTheme theme;
        protected ChangeThemeAction(SwingSet2 swingset, MetalTheme theme) {
            super("ChangeTheme");
            this.swingset = swingset;
            this.theme = theme;
        }

        public void actionPerformed(ActionEvent e) {
            MetalLookAndFeel.setCurrentTheme(theme);
            swingset.updateLookAndFeel();
        }
    }

    class ExitAction extends AbstractAction {
        SwingSet2 swingset;
        protected ExitAction(SwingSet2 swingset) {
            super("ExitAction");
            this.swingset = swingset;
        }

        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    // used in AboutAction
    class OkAction extends AbstractAction {
        JDialog aboutBox;

        protected OkAction(JDialog aboutBox) {
            super("OkAction");
            this.aboutBox = aboutBox;
        }

        // implements interface ActionListener
        public void actionPerformed(ActionEvent e) {
            aboutBox.setVisible(false);
        }
    }

    class AboutAction extends AbstractAction {
        SwingSet2 swingset;
        protected AboutAction(SwingSet2 swingset) {
            super("AboutAction");
            this.swingset = swingset;
        }

        public void actionPerformed(ActionEvent e) {
            if(aboutBox == null) {
                // JPanel panel = new JPanel(new BorderLayout());
                JPanel panel = new AboutPanel(swingset);
                panel.setLayout(new BorderLayout());

                aboutBox = new JDialog(swingset.getFrame(), getString("AboutBox.title"), false);
                aboutBox.setResizable(false);
                aboutBox.getContentPane().add(panel, BorderLayout.CENTER);

                // JButton button = new JButton(getString("AboutBox.ok_button_text"));
                JPanel buttonpanel = new JPanel();
                buttonpanel.setBorder(new javax.swing.border.EmptyBorder(0, 0, 3, 0));
                buttonpanel.setOpaque(false);
                JButton button = (JButton) buttonpanel.add(
                    new JButton(getString("AboutBox.ok_button_text"))
                );
                panel.add(buttonpanel, BorderLayout.SOUTH);

                button.addActionListener(new OkAction(aboutBox));
            }
            aboutBox.pack();
            if (isApplet()) {
                aboutBox.setLocationRelativeTo(getApplet());
            } else {
                aboutBox.setLocationRelativeTo(getFrame());
            }
            aboutBox.setVisible(true);
        }
    }

    class MultiScreenAction extends AbstractAction {
        static final int ALL_SCREENS = -1;
        int screen;
        protected MultiScreenAction(SwingSet2 swingset, int screen) {
            super("MultiScreenAction");
            this.screen = screen;
        }

        public void actionPerformed(ActionEvent e) {
            GraphicsDevice[] gds = GraphicsEnvironment.
                                   getLocalGraphicsEnvironment().
                                   getScreenDevices();
            if (screen == ALL_SCREENS) {
                for (int i = 0; i < gds.length; i++) {
                    SwingSet2 swingset = new SwingSet2(null, gds[i].getDefaultConfiguration());
                    swingset.setDragEnabled(dragEnabled);
                }
            }
            else {
                SwingSet2 swingset = new SwingSet2(null, gds[screen].getDefaultConfiguration());
                swingset.setDragEnabled(dragEnabled);
            }
        }
    }

    // *******************************************************
    // **********************  Misc  *************************
    // *******************************************************

    class DemoLoadThread extends Thread {
        SwingSet2 swingset;

        public DemoLoadThread(SwingSet2 swingset) {
            this.swingset = swingset;
        }

        public void run() {
            swingset.loadDemos();
        }
    }

    class AboutPanel extends JPanel {
        ImageIcon aboutimage = null;
        SwingSet2 swingset = null;

        public AboutPanel(SwingSet2 swingset) {
            this.swingset = swingset;
            aboutimage = swingset.createImageIcon("About.jpg", "AboutBox.accessible_description");
            setOpaque(false);
        }

        public void paint(Graphics g) {
            aboutimage.paintIcon(this, g, 0, 0);
            super.paint(g);
        }

        public Dimension getPreferredSize() {
            return new Dimension(aboutimage.getIconWidth(), aboutimage.getIconHeight());
        }
    }


    private class ChangeFontAction extends AbstractAction {
        private SwingSet2 swingset;
        private boolean plain;

        ChangeFontAction(SwingSet2 swingset, boolean plain) {
            super("FontMenu");
            this.swingset = swingset;
            this.plain = plain;
        }

        public void actionPerformed(ActionEvent e) {
            if (plain) {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
            }
            else {
                UIManager.put("swing.boldMetal", Boolean.TRUE);
            }
            // Change the look and feel to force the settings to take effect.
            updateLookAndFeel();
        }
    }
}
