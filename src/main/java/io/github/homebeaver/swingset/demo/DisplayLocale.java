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
    
    public DisplayLocale(String lang) {
        this.locale = new Locale(lang);
    }
    public DisplayLocale(Locale item) {
        this.locale = item;
    }
    
    public Locale getLocale() {
        return locale;
    }

    // used in JXComboBox/JRadioButtonMenuItem
    // returns "rm-CH rumantsch/Rätoromanisch" for Locale("rm", "CH")
    public String toString() {
		return locale.toString() + " " + locale.getDisplayLanguage(locale) + "/" +locale.getDisplayLanguage();  	
    }

}
