package swingset.borderpatch;

import java.util.logging.Logger;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class CompoundBorderExt extends CompoundBorder {

	private static final Logger LOG = Logger.getLogger(CompoundBorderExt.class.getName());

    public CompoundBorderExt(Border outsideBorder, Border insideBorder) {
    	super(outsideBorder, insideBorder);
		if(outsideBorder instanceof MetalButtonBorder) {
			// ==> insideBorder instanceof BasicMarginBorder
			if(insideBorder instanceof BasicMarginBorder) {
				LOG.config("activate patch ...");
				((BasicMarginBorder)insideBorder).setBorderInsetsPatch(true);
			}
		}
    }

}
