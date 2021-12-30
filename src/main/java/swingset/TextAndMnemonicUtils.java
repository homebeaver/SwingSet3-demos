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
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * <code>TextAndMnemonicUtils</code> allows to extract text and mnemonic values
 * from the unified text & mnemonic strings. For example:
 *   LafMenu.laf.labelAndMnemonic=&Look && Feel
 * The extracted text is "Look & Feel" and the extracted mnemonic mnemonic is "L".
 *
 * There are several patterns for the text and mnemonic suffixes which are used
 * in the resource file. The patterns format is:
 * (resource key -> unified text & mnemonic resource key).
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

    static {
    	LOG.info("ResourceBundle.getBundle(\""+RESOURCEBUNDLE_BASENAME+"\") ...");
    	// Parameter: baseName the base name of the resource bundle, a fully qualified class name
        bundle = ResourceBundle.getBundle(RESOURCEBUNDLE_BASENAME);
/*
Throws:java.lang.NullPointerException - if baseName is null
MissingResourceException - if no resource bundle for the specified base name can be found
 */
    	LOG.info("bundle (Locale) :"+(bundle==null?"null":bundle.getLocale())+"<<<<");
        properties = new Properties();
        try {
        	LOG.info("properties.load ...");
            properties.load(TextAndMnemonicUtils.class.getResourceAsStream("swingset.properties"));
        	LOG.fine("properties:"+properties);
        } catch (IOException ex) {
        	LOG.warning("------------------>"+ex.getMessage());
            System.out.println("java.io.IOException: Couldn't load swingset.properties");
        }
    	LOG.info("ENDE properties - OK\n");
    }

    /**
     * Returns accessible and internationalized strings or mnemonics from the
     * resource bundle. The key is converted to the text & mnemonic key.
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

        if (value != null) {
            String textAndMnemonic = bundle.getString(compositeKey);
            return getTextFromTextAndMnemonic(textAndMnemonic);
        }

        String textAndMnemonic = bundle.getString(key);
        return getTextFromTextAndMnemonic(textAndMnemonic);
    }

    /**
     * Convert the text & mnemonic string to text string
     *
     * The '&' symbol is treated as the mnemonic pointer
     * The double "&&" symbols are treated as the single '&'
     *
     * For example the string "&Look && Feel" is converted to "Look & Feel"
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
     * Convert the text & mnemonic string to mnemonic
     *
     * The '&' symbol is treated the mnemonic pointer
     * The double "&&" symbols are treated as the single '&'
     *
     * For example the string "&Look && Feel" is converted to "L"
     */
    public static String getMnemonicFromTextAndMnemonic(String text) {
//        int len = text.length();
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
