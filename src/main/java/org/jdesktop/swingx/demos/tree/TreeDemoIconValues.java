/*
 * Created on 14.12.2009
 *
 */
package org.jdesktop.swingx.demos.tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Painter;

import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.StopIcon;
import org.jdesktop.swingx.painter.AbstractAreaPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.GraphicsUtilities;

import com.jhlabs.image.InvertFilter;

/**
 * Custom icon values used in TreeDemo.
 */
public class TreeDemoIconValues {
    
    private static final Logger LOG = Logger.getLogger(TreeDemoIconValues.class.getName());
    private static Map<Object, Icon> iconCache;
    
    /**
     * An IconValue which maps cell value to an icon. The value is mapped
     * to a filename using a StringValue. The icons are loaded lazyly.
     */
    public static class LazyLoadingIconValue implements IconValue {
    	
        private Class<?> baseClass;
        private StringValue keyToFileName;
        private Icon fallbackIcon;
        
        
        // <snip> JXTree rendering
        // IconValue based on node value
        /**
         * {@inheritDoc} <p>
         * 
         * Implemented to return a Icon appropriate for the given node value. The icon is
         * loaded (and later cached) as a resource, using a lookup key created by a StringValue. 
         * 
         */
        @Override // implements IconValue
        public Icon getIcon(Object value) {
/*
value ist String und kann auch die nodes des leeren tree's enthalten, also
INFORMATION: ---------------- for JTree class java.lang.String
INFORMATION: ---------------- for colors class java.lang.String
INFORMATION: ---------------- for sports class java.lang.String
INFORMATION: ---------------- for food class java.lang.String

        	LOG.info("---------------- for "+value + " "+value.getClass());

 */
        	Icon icon = iconCache.get(value);
        	if(icon!=null) {
        		LOG.info("cached icon "+icon + " for "+value);
        		return icon;
        	}
        	String key = keyToFileName.getString(value);
        	icon = loadIcon(key);
        	if(icon!=null) {
        		LOG.fine("key "+key + "... icon "+icon + " for "+value);
        		return icon;
        	}
        	return fallbackIcon;
        }
        // </snip>

        /**
         * 
         * @param baseClass Class
         * @param sv StringValue
         * @param fallbackName fallbackName
         */
        public LazyLoadingIconValue(Class<?> baseClass, StringValue sv, String fallbackName) {
           this.baseClass = baseClass;
           if(iconCache==null) iconCache = new HashMap<Object, Icon>(); 
           this.keyToFileName = sv;
           // use a RadianceIcon as fallback
           fallbackIcon = StopIcon.of(SizingConstants.SMALL_ICON, SizingConstants.SMALL_ICON);
           LOG.fine("-------- baseClass:"+baseClass + ", sv:"+sv
        	+ (fallbackIcon==null ? ", null fallbackIcon" : ", fallbackIcon is "+fallbackName));
        }
        
        private Icon loadIcon(String key) {
            Icon icon = loadFromResource(key);
            if (icon != null) {
                iconCache.put(key, icon);
            }
            return icon;
        }
        
        /**
         * 
         * @param name String
         * @return Icon
         */
        protected Icon loadFromResource(String name) {
            URL url = baseClass.getResource("resources/images/" + name );
            if (url == null) return null;
            try {
                BufferedImage image = ImageIO.read(url);
                if (image.getHeight() > 30) {
                    image = GraphicsUtilities.createThumbnail(image, 16);
                }
                return new ImageIcon(image);
            } catch (IOException e) {
            }
            return null;
        }

        
    }

    /**
     * A IconValue which delegates icon lookup to another IconValue and returns
     * a manipulated icon.
     */
    @SuppressWarnings("serial")
	public static class FilteredIconValue implements IconValue {

        private IconValue delegate;
        private Map<Object, Icon> iconCache;

        /**
         * ctor
         * @param delegate IconValue
         */
        public FilteredIconValue(IconValue delegate) {
            iconCache = new HashMap<Object, Icon>();
            this.delegate = delegate;
        }

        /**
         * {@inheritDoc} <p>
         * 
         * Looks up the default icon in the delegate and 
         * returns a manipulated version.
         */
        @Override
        public Icon getIcon(Object value) {
            Icon icon = delegate.getIcon(value);
            Icon xicon = iconCache.get(icon);
            if (xicon == null) {
                xicon = manipulatedIcon(icon);
                iconCache.put(icon, xicon);
            }
            return xicon;
        }

        // <snip> JXTree rollover
        // wraps the given icon into an ImagePainter with a filter effect
        private Icon manipulatedIcon(Icon icon) {
            PainterIcon painterIcon = new PainterIcon(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        	BufferedImage image = null;
        	if(icon instanceof RadianceIcon ri) {
        		image = ri.toImage(1);
        	} else if(icon instanceof ImageIcon ii) {
        		image = (BufferedImage)ii.getImage();
        	} else {
        		LOG.warning("no manipulated Icon for "+icon);
        	}
            
            // ImagePainter extends AbstractAreaPainter<Component>
            //                      AbstractAreaPainter<T> extends AbstractLayoutPainter<T>
            //                                                     AbstractPainter<T> implements Painter<T>
            // ergo delegate can be casted to Painter<Component>
            AbstractAreaPainter<Component> delegate = new ImagePainter(image);
            delegate.setFilters(new InvertFilter());
            painterIcon.setPainter((Painter<Component>)delegate);
            return painterIcon;
        }
        // </snip>

    }
    
}
