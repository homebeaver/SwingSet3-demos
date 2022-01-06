package swingset.borderpatch;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class CompoundBorderExt extends CompoundBorder {

	private static final Logger LOG = Logger.getLogger(CompoundBorderExt.class.getName());

//	private boolean mod = false;
	
    public CompoundBorderExt(Border outsideBorder, Border insideBorder) {
    	super(outsideBorder, insideBorder);
		if(outsideBorder instanceof MetalButtonBorder) {
//			LOG.warning("!!!modifizieren");
//			mod = true;
			// ==> insideBorder instanceof MyMarginBorder
			((BasicMarginBorder)insideBorder).setBorderInsetsPatch(true);
			// das reicht noch nicht, in outsideBorder anmelden
//			((MyMetalButtonBorder)outsideBorder).insideBorder = (MyMarginBorder)insideBorder;
		}
    }

//    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//        Insets  nextInsets;
//        int px, py, pw, ph;
//
//        px = x;
//        py = y;
//        pw = width;
//        ph = height;
//
//        if(outsideBorder != null) {
//            outsideBorder.paintBorder(c, g, px, py, pw, ph);
//
//            nextInsets = outsideBorder.getBorderInsets(c);
////            if(mod) {
////                LOG.warning("nextInsets:"+nextInsets);
////            }
//            px += nextInsets.left;
//            py += nextInsets.top;
//            pw = pw - nextInsets.right - nextInsets.left;
//            ph = ph - nextInsets.bottom - nextInsets.top;
//        }
//        if(insideBorder != null)
//            insideBorder.paintBorder(c, g, px, py, pw, ph);
//
//    }

}
