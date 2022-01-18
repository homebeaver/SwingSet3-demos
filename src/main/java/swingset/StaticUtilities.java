package swingset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

// copied from package (swingx-core test) org.jdesktop.swingx.TestUtilities
public class StaticUtilities {

	private static final Logger LOG = Logger.getLogger(StaticUtilities.class.getName());

	private StaticUtilities() {}

    /**
     * This method returns a string from the demo's resource bundle.
     */
	// kopiert von SwingSet2.getString(String)
    public static String getResourceAsString(String key, String def) {
        try {
            return TextAndMnemonicUtils.getTextAndMnemonicString(key);
        } catch (MissingResourceException e) {
        	LOG.warning(e.toString());
        	return def;
        } catch (Exception e) {
			e.printStackTrace();
			return null;
        }
    }

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public static ImageIcon createImageIcon(String filename) { //, String description) {
    	String path = "/swingset/images/" + filename; 
    	InputStream is = StaticUtilities.getResourceAsStream(StaticUtilities.class, path);
    	if(is==null) return null;
        return new ImageIcon(StaticUtilities.class.getResource(path));
    }

	public static InputStream getResourceAsStream(Class<?> clazz, String resourceName) {
		
		if(clazz==null) return getFileAsStream(resourceName);
		
		InputStream is = clazz.getResourceAsStream(resourceName); // Throws NullPointerException
		LOG.finer("InputStream is:"+is);
		if(is==null) {
			LOG.log(Level.WARNING, "cannot find resource "+clazz.getName() + '#' + resourceName + " try FileAsStream ...");
			// try FileInputStream:
			
			// first PackageName as dir
			String dir = clazz.getPackageName().replace('.', '/')+'/';
			is = getFileAsStream(dir, resourceName);
			if(is!=null) return is;
			
			// this package org.jdesktop.swingx as dir
			dir = StaticUtilities.class.getPackageName().replace('.', '/')+'/';
			is = getFileAsStream(dir, resourceName);
			if(is!=null) return is;
			
			// try default eclipse maven main resource folder ...
			String src = "src/main/resources/";
			is = getFileAsStream(src+dir, resourceName);
			if(is!=null) return is;

			// try default eclipse test folder ...
			src = "src/test/resources/";
			is = getFileAsStream(src+dir, resourceName);
			if(is!=null) return is;

			if(is==null) return getFileAsStream(resourceName);
		}
		return is;
	}

	public static InputStream getFileAsStream(String dir, String resourceName) {
		File path = new File(dir);
		if (!path.exists()) {
			LOG.fine("(package)/path not found:"+path);
			return null;
		}
		return getFileAsStream(dir+resourceName);
	}

	/**
	 * try to load a resource from file with log info
	 * 
	 * @param resourceName fileName
	 * @return (File)InputStream
	 * 
	 * @throws  NullPointerException
     *          If the {@code resourceName} argument is {@code null}
	 */
	public static InputStream getFileAsStream(String resourceName) {
        FileInputStream fis = null;
		try {
	        File file = new File(resourceName); // Throws NullPointerException - If the pathname argument is null
	        if (!file.exists()) {
	        	LOG.log(Level.WARNING, "cannot find resource "+file);
	        } else {
				LOG.info("found:"+file);
	        	fis = new FileInputStream(file);
	        }
		} catch (NullPointerException e) {
			throw e;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fis;
	}

}
