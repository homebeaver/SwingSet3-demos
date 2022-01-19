package swingset;

import java.awt.Dimension;

import org.jdesktop.swingx.JXPanel;

public abstract class AbstractDemo {

	// The preferred size of the demo
    static int PREFERRED_WIDTH = 680;
    static int PREFERRED_HEIGHT = 600;
    static final Dimension PREFERRED_SIZE = new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);

    public abstract JXPanel getDemoPane();

    public abstract JXPanel getControlPane();
    
    String getString(String resourceKey) {
    	String key = this.getClass().getSimpleName() + '.' + resourceKey;
    	return StaticUtilities.getResourceAsString(key, resourceKey);
    }

}
