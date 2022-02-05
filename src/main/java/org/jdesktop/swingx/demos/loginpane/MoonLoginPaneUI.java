package org.jdesktop.swingx.demos.loginpane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI;

import swingset.StaticUtilities;

public class MoonLoginPaneUI extends BasicLoginPaneUI {

    private static final Logger LOG = Logger.getLogger(MoonLoginPaneUI.class.getName());
    
    public MoonLoginPaneUI(JXLoginPane dlg) {
        super(dlg);
    }

	private static final String FILENAME = "resources/images/moon.jpg";

	/**
     * the original (super) default 400x60 banner is replaced by part of the moon
     */
    @Override
    public Image getBanner() {
    	try {
            InputStream fis = StaticUtilities.getResourceAsStream(this.getClass(), FILENAME);
    		BufferedImage im = ImageIO.read(fis);
    		return im.getSubimage(100, 300, 400, 60);
    	} catch (IOException e) {
    		LOG.log(Level.WARNING, "cannot load resource "+FILENAME);
    	}
    	return super.getBanner();
    }

}
