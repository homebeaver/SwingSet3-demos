package io.github.homebeaver.swingset.demo;

import java.util.Locale;

/**
 * wrapper for class Locale
 * <p>
 * class Locale is final, so cannot subclass it
 *
 */
public class DisplayLocale {

    private final Locale locale;
    
    /**
     * ctor 
     * @param lang language code for Locale to wrap
     */
    public DisplayLocale(String lang) {
        this.locale = new Locale(lang);
    }
    /**
     * ctor
     * @param item Locale to wrap
     */
    public DisplayLocale(Locale item) {
        this.locale = item;
    }
    
    /**
     * get the wrapped locale 
     * @return Locale locale
     */
    public Locale getLocale() {
        return locale;
    }

    // used in JXComboBox/JRadioButtonMenuItem
    // returns "rm-CH rumantsch/Rätoromanisch" for Locale("rm", "CH")
    public String toString() {
		return locale.toString() + " " + locale.getDisplayLanguage(locale) + "/" +locale.getDisplayLanguage();  	
    }

}
