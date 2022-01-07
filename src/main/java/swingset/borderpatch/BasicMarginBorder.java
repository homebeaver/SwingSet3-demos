package swingset.borderpatch;

import java.awt.Component;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.plaf.basic.BasicBorders;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class BasicMarginBorder extends BasicBorders.MarginBorder {

	private static final Logger LOG = Logger.getLogger(BasicMarginBorder.class.getName());
	private static Insets zeroInsets = new Insets(0,0,0,0);
	private boolean mod = false;

	public BasicMarginBorder() {
//		LOG.fine("ctor");
	}

	void setBorderInsetsPatch(boolean patch) {
        if(patch) LOG.config("method getBorderInsets() patched for buttons on margin.bottom.");
		mod = patch;
	}
	
    public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton)c;
            Insets margin = b.getMargin();
            if(mod || zeroInsets.hashCode()==insets.hashCode()) {
                LOG.config("margin:"+margin + " - patched margin.bottom: "+margin.bottom + " to 0");
                insets.top = margin != null? margin.top : 0;
                insets.left = margin != null? margin.left : 0;
                insets.bottom = 0;
                insets.right = margin != null? margin.right : 0;
                return insets;
            }
        } 
        return super.getBorderInsets(c, insets);
    }

}
