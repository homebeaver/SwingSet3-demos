package org.jdesktop.swingx.demos.highlighter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Painter;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.painter.AbstractAreaPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.WrappingIconPanel;

import com.jhlabs.image.InvertFilter;

/* Filter:
import com.jhlabs.image.BumpFilter;
import com.jhlabs.image.DiffuseFilter;
import com.jhlabs.image.EmbossFilter;
import com.jhlabs.image.EqualizeFilter;
import com.jhlabs.image.FlareFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.LensBlurFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.PointillizeFilter;
import com.jhlabs.image.SmearFilter;
import com.jhlabs.image.SparkleFilter;

 */
public class RolloverIconHighlighter extends IconHighlighter {

	private static final Logger LOG = Logger.getLogger(RolloverIconHighlighter.class.getName());
	IconValue iconValue;

    public RolloverIconHighlighter() {
        this((HighlightPredicate) null);
    }
    public RolloverIconHighlighter(HighlightPredicate predicate) {
        this(predicate, null);
    }
    public RolloverIconHighlighter(IconValue iv) {
        this(null, iv);
    }
    public RolloverIconHighlighter(HighlightPredicate predicate, IconValue iv) {
        super(predicate);
//        setIcon(icon);
        this.iconValue = iv;
    }

    @Override
    protected boolean canHighlight(Component component, ComponentAdapter adapter) {
        return component instanceof WrappingIconPanel;
    }

    @Override
    protected Component doHighlight(Component component, ComponentAdapter adapter) {
    	if(component instanceof WrappingIconPanel wip) {
    		Icon icon = iconValue==null ? wip.getIcon() : iconValue.getIcon(adapter.getValue());
    		LOG.fine("icon:"+icon);
    		PainterIcon painterIcon = new PainterIcon(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        	BufferedImage image = null;
        	if(icon instanceof RadianceIcon ri) {
        		image = ri.toImage(1);
        	} else if(icon instanceof ImageIcon ii) {
        		image = (BufferedImage)ii.getImage();
        	} else {
//        		WARNUNG: no highlighting for org.jdesktop.swingx.icon.PainterIcon@592c127c
        		LOG.warning("no highlighting for "+icon); //
        	}
        	AbstractAreaPainter<Component> delegate = new ImagePainter(image);
        	delegate.setFilters(new InvertFilter());
        	painterIcon.setPainter((Painter<? extends Component>)delegate);
        	wip.setIcon(painterIcon);
        	return wip;
    	}
        return super.doHighlight(component, adapter);
//    	in super:
//        if (getIcon() != null) {
//            if (component instanceof IconAware) {
//                ((IconAware) component).setIcon(getIcon());
//            } else if (component instanceof JLabel) {
//                ((JLabel) component).setIcon(getIcon());
//            }
//        }
//        return component;
    }

}
