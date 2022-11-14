package swingset;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.util.Utilities;

import io.github.homebeaver.swingset.demo.MainJXframe;

/**
 * a super class for all (swingset 2 and swingset 3) demos.
 * Each demo has a control pane (can be empty, but not null)
 *
 * @author EUG https://github.com/homebeaver
 */
public abstract class AbstractDemo extends JXPanel {

	private static final long serialVersionUID = -6208597812505361313L;
	
	protected static final boolean exitOnClose = true; // used in JXFrame of the demo
	
	// The preferred size of the demo
    static int PREFERRED_WIDTH = 680;
    static int PREFERRED_HEIGHT = 600;
    public static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);

    // Premade convenience dimensions, for use wherever you need 'em.
    /** convenience dimension for horizontal gap */
    public static Dimension HGAP2 = new Dimension(2,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP2 = new Dimension(1,2);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP5 = new Dimension(5,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP5 = new Dimension(1,5);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP10 = new Dimension(10,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP10 = new Dimension(1,10);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP15 = new Dimension(15,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP15 = new Dimension(1,15);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP20 = new Dimension(20,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP20 = new Dimension(1,20);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP25 = new Dimension(25,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP25 = new Dimension(1,25);

    /** convenience dimension for horizontal gap */
    public static Dimension HGAP30 = new Dimension(30,1);
    /** convenience dimension for vertical gap */
    public static Dimension VGAP30 = new Dimension(1,30);

    /**
     * ctor
     */
    public AbstractDemo() {
    	super();
    }
    /**
     * ctor with layout
     * @param layout LayoutManager
     */
    public AbstractDemo(LayoutManager layout) {
        super(layout);
    	Window w = (Window)MainJXframe.getInstance();
    	super.setDefaultLocale(w.getLocale());
    }

    /**
     * get ControlPane - to be implemented in subclass
     * @return JXPanel
     */
    public abstract JXPanel getControlPane();

    /**
     * 
     * @return empty ControlPane
     */
    protected JXPanel emptyControlPane() {
    	JXPanel pane = new JXPanel();
    	pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
    	pane.add(Box.createRigidArea(HGAP30));
    	pane.add(new JXLabel("no controller for this demo", JXLabel.CENTER));
    	pane.add(Box.createRigidArea(HGAP30));
    	return pane;
    }

    private ResourceBundle bundle; 
    private static final String SWINGSET2_PACKAGE_NAME = "swingset";
    private static final String SWINGSET2_RESOURCEBUNDLE_BASENAME = SWINGSET2_PACKAGE_NAME+".swingset";
    
    /*
     * für AbstractButton, JLabel, JXTaskPane implements Mnemonicable
     * alle 3 sind JComponent
     * TODO JXLabel implements Mnemonicable
     *      AbstractActionExt implements Mnemonicable, da es auch xetMnemonic hat
     *  AbstractButton ist in package javax.swing - in jdesktop gibt es JXAbstractButton nicht
     */
    /**
     * get a String property with Mnemonicable for comp
     * @param key property name
     * @param comp AbstractButton, JLabel or JXTaskPane
     * @return property value
     */
    protected String getBundleString(String key, JComponent comp) {
    	String s = 
    		SWINGSET2_PACKAGE_NAME.equals(getClass().getPackage().getName()) 
    			? getBundleString(getClass().getSimpleName()+"."+key, key)
    			: getBundleString(key, key);
    	String mnemonic = TextAndMnemonicUtils.getMnemonicFromTextAndMnemonic(s);
    	if(mnemonic!=null && mnemonic.length()==1) {
    		if(comp instanceof AbstractButton) {
    			AbstractButton c = (AbstractButton)comp;
    			c.setMnemonic(Integer.valueOf(mnemonic.charAt(0)));
    		} else if(comp instanceof JLabel) {
    			JLabel c = (JLabel)comp;
    			c.setDisplayedMnemonic(Integer.valueOf(mnemonic.charAt(0)));
    		} else if(comp instanceof JXTaskPane) {
    			JXTaskPane c = (JXTaskPane)comp;
    			c.setMnemonic(Integer.valueOf(mnemonic.charAt(0)));
    		}
    	}
    	return TextAndMnemonicUtils.getTextFromTextAndMnemonic(s);    	
    }
    /**
     * get a String property
     * @param key property name
     * @return property value
     */
    protected String getBundleString(String key) {
    	if(SWINGSET2_PACKAGE_NAME.equals(getClass().getPackage().getName())) {
    		// die Props haben prefix "class SimpleName."
    		return TextAndMnemonicUtils.getTextFromTextAndMnemonic(getBundleString(getClass().getSimpleName()+"."+key, key));
    	}
    	return TextAndMnemonicUtils.getTextFromTextAndMnemonic(getBundleString(key, key));
    }
    /**
     * get a String property
     * @param key property name
     * @param fallback String
     * @return property value
     */
    protected String getBundleString(String key, String fallback) {
        String value = fallback;
        if (bundle == null) {
        	/* bundleName
        	 * in SwingSet2: bundleName :== <package name>.swingset // one ResourceBundle for all classes
        	 * in SwingSet3: bundleName :== <package name>.resources.<class SimpleName> // one ResourceBundle per class
        	 */
        	bundle = ResourceBundle.getBundle(SWINGSET2_RESOURCEBUNDLE_BASENAME);
        	String bundleName = null;
        	try {
            	// in SwingSet3: bundleName :== <package name>.resources.<class SimpleName>
                bundleName = getClass().getPackage().getName()+".resources."+getClass().getSimpleName();
                bundle = ResourceBundle.getBundle(bundleName, JComponent.getDefaultLocale());
                //Throws:NullPointerException,, MissingResourceException
                
                Logger.getAnonymousLogger().config("this.getLocale()=" + this.getLocale() 
    	            + "; getDefaultLocale()" +JComponent.getDefaultLocale() 
    	            + " bundle.Locale:"+bundle.getLocale()
    	            );
                if(bundle.getLocale()!=JComponent.getDefaultLocale()) {
                	if(bundle.getLocale().toString().length()>=2 && bundle.getLocale().getDisplayLanguage().substring(0, 2).
                			equals(JComponent.getDefaultLocale().getDisplayLanguage().substring(0, 2))) {
                		// de_CH soll auch mit de_XX zufrieden sein!
                	} else {
                    	// fallback:
                    	//bundle = ResourceBundle.getBundle(bundleName, (Locale)null);
                    	// liefert NPE at java.base/java.util.ResourceBundle.getBundleImpl(ResourceBundle.java:1612)
                    	bundle = ResourceBundle.getBundle(bundleName);
                	}
                }
            } catch (MissingResourceException e) {
                Logger.getAnonymousLogger().warning("missing resource "+key + " - " 
                		+ (bundle==null ? e.getMessage() : " will use bundle for "+SWINGSET2_RESOURCEBUNDLE_BASENAME)
                		+ (fallback==null ? "" : " - there is a fallback:"+fallback)
                		);
        	}
        }
        try {
            value = bundle != null ? bundle.getString(key) : fallback;
        
        } catch (MissingResourceException e) {
            Logger.getAnonymousLogger().warning("missing String resource " + key + "; using fallback \"" +fallback + "\"");
        }
        return value;
    } 

    /**
     * Adds a component to TabbedPane represented by a title and no icon
     * @param tab JTabbedPane
     * @param comp JComponent
     * @param name title
     * @param createScroll to create a new ScrollPane
     */
    protected void addTab(JTabbedPane tab, JComponent comp, String name, boolean createScroll) {
		tab.addTab(getBundleString(name), createScroll ? new JScrollPane(comp) : comp);
	}

    /**
     * get an icon from resource
     * @param clazz the class to find the resource for
     * @param imageName, the resource i.e. "resources/images/workerduke.png"
     * @return ImageIcon or null
     */
    protected ImageIcon getResourceAsIcon(Class<?> clazz, String imageName) {
		try {
	        InputStream is = Utilities.getResourceAsStream(clazz, imageName);
			return new ImageIcon(is.readAllBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}
