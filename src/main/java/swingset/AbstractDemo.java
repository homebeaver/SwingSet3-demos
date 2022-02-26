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
	
	// The preferred size of the demo
    static int PREFERRED_WIDTH = 680;
    static int PREFERRED_HEIGHT = 600;
    public static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);

    // Premade convenience dimensions, for use wherever you need 'em.
    public static Dimension HGAP2 = new Dimension(2,1);
    public static Dimension VGAP2 = new Dimension(1,2);

    public static Dimension HGAP5 = new Dimension(5,1);
    public static Dimension VGAP5 = new Dimension(1,5);

    public static Dimension HGAP10 = new Dimension(10,1);
    public static Dimension VGAP10 = new Dimension(1,10);

    public static Dimension HGAP15 = new Dimension(15,1);
    public static Dimension VGAP15 = new Dimension(1,15);

    public static Dimension HGAP20 = new Dimension(20,1);
    public static Dimension VGAP20 = new Dimension(1,20);

    public static Dimension HGAP25 = new Dimension(25,1);
    public static Dimension VGAP25 = new Dimension(1,25);

    public static Dimension HGAP30 = new Dimension(30,1);
    public static Dimension VGAP30 = new Dimension(1,30);

    public AbstractDemo() {
    	super();
    }
    public AbstractDemo(LayoutManager layout) {
        super(layout);
    	Window w = (Window)MainJXframe.getInstance();
    	super.setDefaultLocale(w.getLocale());
    }

    public abstract JXPanel getControlPane();

    protected JXPanel emptyControlPane() {
    	JXPanel pane = new JXPanel();
    	pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
    	pane.add(Box.createRigidArea(HGAP30));
    	pane.add(new JXLabel("no controller for this demo", JXLabel.CENTER));
    	pane.add(Box.createRigidArea(HGAP30));
    	return pane;
    }

    @Deprecated
    protected char getMnemonic(String key) {
        return (getString(key)).charAt(0);
    }

    private ResourceBundle bundle; 
    private static final String SWINGSET2_PACKAGE_NAME = "swingset";
    private static final String SWINGSET2_RESOURCEBUNDLE_BASENAME = SWINGSET2_PACKAGE_NAME+".swingset";
    protected String getBundleString(String key, JLabel comp) {
    	if(SWINGSET2_PACKAGE_NAME.equals(getClass().getPackage().getName())) {
    		// die Props haben prefix "class SimpleName."
    		String string = getBundleString(getClass().getSimpleName()+"."+key, key);
    		String mnemonicString = TextAndMnemonicUtils.getMnemonicFromTextAndMnemonic(string);
    		if(mnemonicString!=null && mnemonicString.length()==1) {
    			comp.setDisplayedMnemonic(Integer.valueOf(mnemonicString.charAt(0)));
    		}
    		return TextAndMnemonicUtils.getTextFromTextAndMnemonic(string);
    	}
    	String string = getBundleString(key, key);
		String mnemonicString = TextAndMnemonicUtils.getMnemonicFromTextAndMnemonic(string);
		if(mnemonicString!=null && mnemonicString.length()==1) {
			comp.setDisplayedMnemonic(Integer.valueOf(mnemonicString.charAt(0)));
		}
    	return TextAndMnemonicUtils.getTextFromTextAndMnemonic(string);
    }
    protected String getBundleString(String key, AbstractButton comp) {
    	if(SWINGSET2_PACKAGE_NAME.equals(getClass().getPackage().getName())) {
    		// die Props haben prefix "class SimpleName."
    		String string = getBundleString(getClass().getSimpleName()+"."+key, key);
    		String mnemonicString = TextAndMnemonicUtils.getMnemonicFromTextAndMnemonic(string);
    		if(mnemonicString!=null && mnemonicString.length()==1) {
    			comp.setMnemonic(Integer.valueOf(mnemonicString.charAt(0)));
    		}
    		return TextAndMnemonicUtils.getTextFromTextAndMnemonic(string);
    	}
    	String string = getBundleString(key, key);
		String mnemonicString = TextAndMnemonicUtils.getMnemonicFromTextAndMnemonic(string);
		if(mnemonicString!=null && mnemonicString.length()==1) {
			comp.setMnemonic(Integer.valueOf(mnemonicString.charAt(0)));
		}
    	return TextAndMnemonicUtils.getTextFromTextAndMnemonic(string);
    }
    protected String getBundleString(String key) {
    	if(SWINGSET2_PACKAGE_NAME.equals(getClass().getPackage().getName())) {
    		// die Props haben prefix "class SimpleName."
    		return TextAndMnemonicUtils.getTextFromTextAndMnemonic(getBundleString(getClass().getSimpleName()+"."+key, key));
    	}
    	return TextAndMnemonicUtils.getTextFromTextAndMnemonic(getBundleString(key, key));
    }
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

    @Deprecated
    protected String getString(String resourceKey) {
    	String key = this.getClass().getSimpleName() + '.' + resourceKey;
    	return StaticUtilities.getResourceAsString(key, resourceKey);
    }

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
