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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.swing.JComponent;

/**
 * <code>TextAndMnemonicUtils</code> allows to extract text and mnemonic values
 * from the unified text &amp; mnemonic strings. For example:
 *   LafMenu.laf.labelAndMnemonic=&amp;Look &amp;&amp; Feel
 * The extracted text is "Look &amp; Feel" and the extracted mnemonic mnemonic is "L".
 *
 * There are several patterns for the text and mnemonic suffixes which are used
 * in the resource file. The patterns format is:
 * (resource key -> unified text &amp; mnemonic resource key).
 *
 * Keys that have label suffixes:
 * (xxx_label -> xxx.labelAndMnemonic)
 *
 * Keys that have mnemonic suffixes:
 * (xxx_mnemonic -> xxx.labelAndMnemonic)
 *
 * Keys that do not have definite suffixes:
 * (xxx -> xxx.labelAndMnemonic)
 *
 * @author Alexander Scherbatiy
 */
public class TextAndMnemonicUtils {
	
	private static final Logger LOG = Logger.getLogger(TextAndMnemonicUtils.class.getName());
	
    // Label suffix for the text & mnemonic resource
    private static final String LABEL_SUFFIX = ".labelAndMnemonic";

    // Resource bundle for internationalized and accessible text
    private static final String RESOURCEBUNDLE_BASENAME = "swingset.swingset";
    private static ResourceBundle bundle = null;

    // Resource properties for the mnemonic key defenition
    private static Properties properties = null;

    static { // clinit
    	Locale locale = JComponent.getDefaultLocale();
    	LOG.config("ResourceBundle.getBundle(\""+RESOURCEBUNDLE_BASENAME+"\") ... Locale:"+locale);
    	// Parameter: baseName the base name of the resource bundle, a fully qualified class name
    	// es gibt noch die Methode mit Locale:
        //public static final ResourceBundle getBundle(String baseName, Locale locale)
        bundle = ResourceBundle.getBundle(RESOURCEBUNDLE_BASENAME, locale);
        LOG.config("bundle:"+bundle);
/*
Throws:java.lang.NullPointerException - if baseName is null
MissingResourceException - if no resource bundle for the specified base name can be found
 */
        properties = new Properties();
        if(bundle==null) {
            try {
            	LOG.info("properties.load ...");
                properties.load(TextAndMnemonicUtils.class.getResourceAsStream("swingset.properties"));
            	LOG.fine("properties:"+properties);
            } catch (IOException ex) {
            	LOG.warning("------------------>"+ex.getMessage());
                System.out.println("java.io.IOException: Couldn't load swingset.properties");
            }
        } else {
        	LOG.info("bundle.BaseBundleName:"+bundle.getBaseBundleName()+"<<<<");
        	Enumeration<String> keys = bundle.getKeys();
        	Iterator<String> iter = keys.asIterator();
        	int k=0;
        	while(iter.hasNext()) {
        		String key = iter.next();
        		LOG.fine(key+"<< key "+k); // props key z.B. OptionPaneDemo.inputbutton
        		k++;
        		properties.put(key, bundle.getObject(key));
        	}
        	LOG.info("#properties="+k);
        }
/* print the Swingset2 props:
        // +"_"+locale.getLanguage()
        try {
        	LOG.info("properties.load ...");
            properties.load(TextAndMnemonicUtils.class.getResourceAsStream("swingset.properties"));
        	LOG.info("properties#="+properties.size());
        	// print sorted:
        	List<String> keys = new ArrayList<String>();
        	for(String key : properties.stringPropertyNames()) {
        		keys.add(key);
        	}
        	Collections.sort(keys);
        	keys.forEach( k -> {
        		System.out.println("Key : " + k + ", Value : " + properties.getProperty(k));
        	});
// not sorted
//        	properties.forEach(
//        			(k, v) -> System.out.println("Key : " + k + ", Value : " + v));
        } catch (IOException ex) {
        	LOG.warning("------------------>"+ex.getMessage());
            System.out.println("java.io.IOException: Couldn't load swingset.properties");
        }
 */
    	LOG.info("clinit ENDE properties - OK\n");
    }

    /**
     * Returns accessible and internationalized strings or mnemonics from the
     * resource bundle. The key is converted to the text and mnemonic key.
     *
     * The following patterns are checked:
     * Keys that have label suffixes:
     * (xxx_label -> xxx.labelAndMnemonic)
     *
     * Keys that have mnemonic suffixes:
     * (xxx_mnemonic -> xxx.labelAndMnemonic)
     *
     * Keys that do not have definite suffixes:
     * (xxx -> xxx.labelAndMnemonic)
     *
     * Properties class is used to check if a key created for mnemonic exists.
     * 
     * @param key String
     * @return strings or mnemonics from the resource bundle
     */
    public static String getTextAndMnemonicString(String key) {

        if (key.endsWith("_label")) {
            String compositeKey = composeKey(key, 6, LABEL_SUFFIX);
            String textAndMnemonic = bundle.getString(compositeKey);
            return getTextFromTextAndMnemonic(textAndMnemonic);
        }

        if (key.endsWith("_mnemonic")) {

            String compositeKey = composeKey(key, 9, LABEL_SUFFIX);
            Object value = properties.getProperty(compositeKey);

            if (value != null) {
                String textAndMnemonic = bundle.getString(compositeKey);
                return getMnemonicFromTextAndMnemonic(textAndMnemonic);
            }

        }

        String compositeKey = composeKey(key, 0, LABEL_SUFFIX);
        Object value = properties.getProperty(compositeKey);
//LOG.info("key="+key+"____________value:"+value);
        if (value != null) {
            String textAndMnemonic = bundle.getString(compositeKey);
            return getTextFromTextAndMnemonic(textAndMnemonic);
        }

        String textAndMnemonic = bundle.getString(key); // throws    MissingResourceException
//        LOG.info("key="+key+"____________textAndMnemonic:"+textAndMnemonic);
        return getTextFromTextAndMnemonic(textAndMnemonic);
    }

    /**
     * Convert the text &amp; mnemonic string to text string
     *
     * The '&amp;' symbol is treated as the mnemonic pointer
     * The double "&amp;&amp;" symbols are treated as the single '&amp;'
     *
     * For example the string "&amp;Look &amp;&amp; Feel" is converted to "Look &amp; Feel"
     * 
     * @param text String
     * @return the text String without Mnemonics
     */
    public static String getTextFromTextAndMnemonic(String text) {

        StringBuilder sb = new StringBuilder();

        int prevIndex = 0;
        int nextIndex = text.indexOf('&');
        int len = text.length();

        while (nextIndex != -1) {

            String s = text.substring(prevIndex, nextIndex);
            sb.append(s);

            nextIndex++;

            if (nextIndex != len && text.charAt(nextIndex) == '&') {
                sb.append('&');
                nextIndex++;
            }

            prevIndex = nextIndex;
            nextIndex = text.indexOf('&', nextIndex + 1);
        }

        sb.append(text.substring(prevIndex, text.length()));
        return sb.toString();
    }

    /**
     * Convert the text &amp; mnemonic string to mnemonic
     *
     * The '&amp;' symbol is treated the mnemonic pointer
     * The double "&amp;&amp;" symbols are treated as the single '&amp;'
     *
     * For example the string "&amp;Look &amp;&amp; Feel" is converted to "L"
     * 
     * @param text String
     * @return the mnemonic String
     */
    public static String getMnemonicFromTextAndMnemonic(String text) {
        int index = text.indexOf('&');

        while (0 <= index && index < text.length() - 1) {
            index++;
            if (text.charAt(index) == '&') {
                index = text.indexOf('&', index + 1);
            } else {
                char c = text.charAt(index);
                return String.valueOf(Character.toUpperCase(c));
            }
        }

        return null;
    }

    /**
     * Removes the last n characters and adds the suffix
     */
    private static String composeKey(String key, int reduce, String sufix) {
        return key.substring(0, key.length() - reduce) + sufix;
    }
}
