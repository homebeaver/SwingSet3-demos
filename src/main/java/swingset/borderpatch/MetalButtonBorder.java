package swingset.borderpatch;

import java.awt.Component;
import java.awt.Graphics;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIClientPropertyKey;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

@SuppressWarnings("serial") // Superclass is not serializable across versions
public class MetalButtonBorder extends MetalBorders.ButtonBorder {

	// copy of sun.swing.StringUIClientPropertyKey; // not accessible
	static class MyStringUIClientPropertyKey implements UIClientPropertyKey {

	    private final String key;

	    public MyStringUIClientPropertyKey(String key) {
	        this.key = key;
	    }

	    public String toString() {
	        return key;
	    }
	}

	// need this to implement paintOceanBorder method
    static Object NO_BUTTON_ROLLOVER =
            new MyStringUIClientPropertyKey("NoButtonRollover");

	private static final Logger LOG = Logger.getLogger(MetalButtonBorder.class.getName());

    public MetalButtonBorder() {
    	super();
    }
    public MetalButtonBorder(BasicMarginBorder myMarginBorder) {
    	super();
    	LOG.config("----------------- ctor ---------------------");
    	insideBorder = myMarginBorder;
    }

    BasicMarginBorder insideBorder = null;
    
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        if (!(c instanceof AbstractButton)) {
            return;
        }

//      if (MetalLookAndFeel.usingOcean()) { // is not visible, but equivalent to:
        if(MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme) {
            paintOceanBorder(c, g, x, y, w, h);
            return;
        }
        
        super.paintBorder(c, g, x, y, w, h);
        return;
    }

    // copied from private super method to activate Patch
    private void paintOceanBorder(Component c, Graphics g, int x, int y, int w, int h) {
        AbstractButton button = (AbstractButton)c;
        ButtonModel model = ((AbstractButton)c).getModel();

        g.translate(x, y);
//      if (MetalUtils.isToolBarButton(button)) { // javax.swing.plaf.metal.MetalUtils is not visible, but equivalent to:
        if (button.getParent() instanceof JToolBar) {
            if (model.isEnabled()) {
                if (model.isPressed()) {
                    g.setColor(MetalLookAndFeel.getWhite());
                    g.fillRect(1, h - 1, w - 1, 1);
                    g.fillRect(w - 1, 1, 1, h - 1);
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.drawRect(0, 0, w - 2, h - 2);
                    g.fillRect(1, 1, w - 3, 1);
                }
                else if (model.isSelected() || model.isRollover()) {
                    g.setColor(MetalLookAndFeel.getWhite());
                    g.fillRect(1, h - 1, w - 1, 1);
                    g.fillRect(w - 1, 1, 1, h - 1);
                    g.setColor(MetalLookAndFeel.getControlDarkShadow());
                    g.drawRect(0, 0, w - 2, h - 2);
                }
                else {
                    g.setColor(MetalLookAndFeel.getWhite());
                    g.drawRect(1, 1, w - 2, h - 2);
                    g.setColor(UIManager.getColor(
                            "Button.toolBarBorderBackground"));
                    g.drawRect(0, 0, w - 2, h - 2);
                }
            }
            else {
               g.setColor(UIManager.getColor(
                       "Button.disabledToolBarBorderBackground"));
               g.drawRect(0, 0, w - 2, h - 2);
            }
        }
        else if (model.isEnabled()) {
            boolean pressed = model.isPressed();
//            boolean armed = model.isArmed();

            if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
//            	LOG.info("isDefaultButton");
                g.setColor(MetalLookAndFeel.getControlDarkShadow());
                g.drawRect(0, 0, w - 1, h - 1);
                g.drawRect(1, 1, w - 3, h - 3);
            }
            else if (pressed) {
                g.setColor(MetalLookAndFeel.getControlDarkShadow());
                g.fillRect(0, 0, w, 2);
                g.fillRect(0, 2, 2, h - 2);
                g.fillRect(w - 1, 1, 1, h - 1);
                g.fillRect(1, h - 1, w - 2, 1);
            }
            else if (model.isRollover() && button.getClientProperty(NO_BUTTON_ROLLOVER) == null) {
//            	LOG.info("isRollover");
                g.setColor(MetalLookAndFeel.getPrimaryControl());
                g.drawRect(0, 0, w - 1, h - 1);
                g.drawRect(2, 2, w - 5, h - 5);
                g.setColor(MetalLookAndFeel.getControlDarkShadow());
                g.drawRect(1, 1, w - 3, h - 3);
            }
            else {
//            	LOG.info(" else ...");
                g.setColor(MetalLookAndFeel.getControlDarkShadow());
                g.drawRect(0, 0, w - 1, h - 1);
                // EUG: activate Patch
                if(insideBorder!=null) insideBorder.setBorderInsetsPatch(true);
            }
        }
        else {
            g.setColor(MetalLookAndFeel.getInactiveControlTextColor());
            g.drawRect(0, 0, w - 1, h - 1);
            if ((c instanceof JButton) && ((JButton)c).isDefaultButton()) {
                g.drawRect(1, 1, w - 3, h - 3);
            }
        }
    }

}
