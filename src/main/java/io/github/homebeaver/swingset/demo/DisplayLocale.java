package io.github.homebeaver.swingset.demo;

import java.util.Locale;

import org.pushingpixels.radiance.common.api.icon.RadianceIcon;

/**
 * wrapper for class Locale
 * <p>
 * class Locale is final, so cannot subclass it
 *
 */
public class DisplayLocale {

    private final Locale locale;
    private RadianceIcon flag = null; // optional flag
    
    public DisplayLocale(String lang, RadianceIcon flag) {
        this.locale = new Locale(lang);
        this.flag = flag;
    }
    /**
     * ctor 
     * @param lang language code for Locale to wrap
     */
    public DisplayLocale(String lang) {
    	this(lang, null);
    }
    
    public DisplayLocale(Locale item, RadianceIcon flag) {
        this.locale = item;
        this.flag = flag;
    }
    /**
     * ctor
     * @param item Locale to wrap
     */
    public DisplayLocale(Locale item) {
        this(item, null);
    }
    
    /**
     * get the wrapped locale 
     * @return Locale locale
     */
    public Locale getLocale() {
        return locale;
    }

    public String getCountry() {
    	return locale.getCountry();
    }
    
    public RadianceIcon getIcon() {
    	return flag;
    }
    
    // used in JXComboBox/JRadioButtonMenuItem
    // returns "rm-CH rumantsch/Rätoromanisch" for Locale("rm", "CH")
    public String toString() {
		return locale.toString() + " " + locale.getDisplayLanguage(locale) + "/" +locale.getDisplayLanguage();  	
    }

}
