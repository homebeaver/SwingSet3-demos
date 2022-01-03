package swingset;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.plaf.UIResource;

public class CircleIcon implements Icon, UIResource {

    public static final int XS  = 10;
    public static final int  S  = 16;
    public static final int  M  = 24; // Action bar, Dialog & Tab icons
    public static final int  N  = 32;
    public static final int  L  = 48; // Launcher icons
    public static final int XL  = 64;
    public static final int XXL =128;

    public static final int  LAUNCHER_ICON  = L;
    public static final int  ACTION_ICON  = M;

    private int width = ACTION_ICON;
    private int height = ACTION_ICON;
    private Color color;

    public CircleIcon() {
    	this(ACTION_ICON, null);
    }
    public CircleIcon(int size, Color color) {
    	width = size;
    	height = size;
    	this.color = color;
    }
    
//    public String toString() {
//    	return "["+color+"]";
//    }
    
    // implements interface Icon:

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
//		Graphics2D g2 = (Graphics2D) g;
        g.setColor(c.getForeground());
        g.drawOval(x, y, width-1, height-1);

        g.setColor(this.color==null ? c.getBackground() : this.color);
        g.fillOval(x+1, y+1, width-2, height-2);		
	}

	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return height;
	}

}
