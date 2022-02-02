package swingset;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

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

    public String getString(String resourceKey) {
    	String key = this.getClass().getSimpleName() + '.' + resourceKey;
    	return StaticUtilities.getResourceAsString(key, resourceKey);
    }

    char getMnemonic(String key) {
        return (getString(key)).charAt(0);
    }

}
