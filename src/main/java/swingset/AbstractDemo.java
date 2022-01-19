package swingset;

import org.jdesktop.swingx.JXPanel;

public abstract class AbstractDemo {

    public abstract JXPanel getDemoPane();

    public abstract JXPanel getControlPane();
    
    String getString(String resourceKey) {
    	String key = this.getClass().getSimpleName() + '.' + resourceKey;
    	return StaticUtilities.getResourceAsString(key, resourceKey);
    }

}
