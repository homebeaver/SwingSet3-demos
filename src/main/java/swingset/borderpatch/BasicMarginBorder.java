package swingset.borderpatch;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class BasicMarginBorder extends BasicBorders.MarginBorder {

	private static final Logger LOG = Logger.getLogger(BasicMarginBorder.class.getName());

	private boolean mod = false;

	public BasicMarginBorder() {
//		LOG.warning("ctor");
	}

	void setBorderInsetsPatch(boolean patch) {
        if(patch) LOG.warning("method getBorderInsets() patched for buttons on margin.bottom.");
		mod = patch;
	}
	
    public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton)c;
            Insets margin = b.getMargin();
            if(mod) {
                LOG.config("margin:"+margin + " - patched margin.bottom!");
                insets.top = margin != null? margin.top : 0;
                insets.left = margin != null? margin.left : 0;
                insets.bottom = margin != null? margin.bottom -1 : 0;
                insets.right = margin != null? margin.right : 0;
                return insets;
            }
        } 
        return super.getBorderInsets(c, insets);
    }
    
//    public Insets getBorderInsetsXX(Component c, Insets insets)       {
//        Insets margin = null;
//        //
//        // Ideally we'd have an interface defined for classes which
//        // support margins (to avoid this hackery), but we've
//        // decided against it for simplicity
//        //
//       if (c instanceof AbstractButton) {
//           AbstractButton b = (AbstractButton)c;
//           margin = b.getMargin();
//           LOG.info("margin:"+margin + " !!! Patch="+mod);
//           if(mod) {
//               insets.top = margin != null? margin.top : 0;
//               insets.left = margin != null? margin.left : 0;
//               insets.bottom = margin != null? margin.bottom -1 : 0;
//               insets.right = margin != null? margin.right : 0;
//               return insets;
//           }
//       } else if (c instanceof JToolBar) {
//           JToolBar t = (JToolBar)c;
//           margin = t.getMargin();
//       } else if (c instanceof JTextComponent) {
//           JTextComponent t = (JTextComponent)c;
//           margin = t.getMargin();
//       }
//       insets.top = margin != null? margin.top : 0;
//       insets.left = margin != null? margin.left : 0;
//       insets.bottom = margin != null? margin.bottom : 0;
//       insets.right = margin != null? margin.right : 0;
//       
////       LOG.info("insets:"+insets);
//
//       return insets;
//    }
    
//    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
//        if (c instanceof JButton) {
//        	//JButton b = (JButton)c;
//        	Border b = ((JButton)c).getBorder();
//        	if(b instanceof CompoundBorder) {
//                LOG.info("c:"+c);
//        		Border ob = ((CompoundBorder)b).getOutsideBorder();
//        		if(ob instanceof MyMetalButtonBorder) {
//        			LOG.warning("!!!: x="+x + ", y="+y + ", w="+w + ", h="+h);
//        		}
//        	}
//        }
//    	super.paintBorder(c, g, x, y, w, h);
//    }

}
