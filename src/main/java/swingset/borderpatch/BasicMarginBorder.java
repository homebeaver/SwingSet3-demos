package swingset.borderpatch;

import java.awt.Component;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicBorders;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class BasicMarginBorder extends BasicBorders.MarginBorder {

	private static final Logger LOG = Logger.getLogger(BasicMarginBorder.class.getName());
	private static Insets zeroInsets = new Insets(0,0,0,0);
//	private static Insets min = new Insets(2,2,2,2);
//	private static Insets min = new Insets(0,3,2,3); // gut, an button1 links unten ein Strich, bei 10 unter button 3 Punkte
	private static Insets min = new Insets(0,4,2,5); // besser, bei 10 an button1 links unten noch ein Strich
	                                                 // bei push ist der Strich weg!

	MetalButtonBorder outsideBorder = null;
    public void setOutsideBorder(Border border) {
    	if(border instanceof MetalButtonBorder) outsideBorder = (MetalButtonBorder)border;
    }

	public BasicMarginBorder() {
		LOG.fine("ctor");
	}

    public Insets getBorderInsets(Component c, Insets insets) {
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton)c;
            Insets margin = b.getMargin();
            if(outsideBorder!=null || zeroInsets.hashCode()==insets.hashCode()) {
                LOG.fine("margin:"+margin + " - patched margin.bottom: "+margin.bottom + " to 0");
                insets.top = margin != null? //margin.top-min.top : 0;
                		(margin.top>=min.top? margin.top-min.top : 0)
                		: 0;
                insets.left = margin != null? //margin.left-min.left : 0;
                		(margin.left>=min.left? margin.left-min.left : 0)
                		: 0;
                insets.bottom = margin != null? //margin.bottom-min.bottom : 0;
                		(margin.bottom>=min.bottom? margin.bottom-min.bottom : 0)
                		: 0;
                insets.right = margin != null? //margin.right-min.right : 0;
                		(margin.right>=min.right? margin.right-min.right : 0)
                		: 0;
                return insets;
            }
        } 
        return super.getBorderInsets(c, insets);
    }
}
