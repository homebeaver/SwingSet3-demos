/*
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package swingset;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.accessibility.Accessible;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * A generic SwingSet2 demo module
 *
 * @author Jeff Dinkins
 * @author EUG https://github.com/homebeaver (removed usage of JApplet, Applet, now subclass Panel)
 */
public class DemoModule extends JPanel implements Accessible, RootPaneContainer {

	private static final long serialVersionUID = 5010903388735156025L;
    private static final Logger LOG = Logger.getLogger(DemoModule.class.getName());
	
	// The preferred size of the demo
    static int PREFERRED_WIDTH = 680;
    static int PREFERRED_HEIGHT = 600;

    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),
                                              new EmptyBorder(5,5,5,5));

    // Premade convenience dimensions, for use wherever you need 'em.
    /** Dim for Horizontal Gap */
    public static Dimension HGAP2 = new Dimension(2,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP2 = new Dimension(1,2);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP5 = new Dimension(5,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP5 = new Dimension(1,5);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP10 = new Dimension(10,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP10 = new Dimension(1,10);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP15 = new Dimension(15,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP15 = new Dimension(1,15);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP20 = new Dimension(20,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP20 = new Dimension(1,20);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP25 = new Dimension(25,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP25 = new Dimension(1,25);

    /** Dim for Horizontal Gap */
    public static Dimension HGAP30 = new Dimension(30,1);
    /** Dim for Vertical Gap */
    public static Dimension VGAP30 = new Dimension(1,30);

    private SwingSet2 swingset = null;
    private JPanel panel = null;
    private String resourceName = null;
    private String iconPath = null;
    private String sourceCode = null;

    /**
     * ctor
     * @param swingset SwingSet2
     */
    public DemoModule(SwingSet2 swingset) {
        this(swingset, null, null);
    }

    /**
     * ctor
     * @param swingset SwingSet2
     * @param resourceName String
     * @param iconPath used to represent this demo inside the SwingSet2 tool bar
     */
    public DemoModule(SwingSet2 swingset, String resourceName, String iconPath) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        this.resourceName = resourceName;
        this.iconPath = iconPath;
        this.swingset = swingset;

        loadSourceCode();
    }

    /**
     * getter 
     * @return resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * getter for DemoPanel
     * @return panel
     */
    public JPanel getDemoPanel() {
        return panel;
    }

    /**
     * getter
     * @return SwingSet2
     */
    public SwingSet2 getSwingSet2() {
        return swingset;
    }

    /**
     * get getSwingSet2 resource
     * @param key resource name
     * @return String resource value
     */
	public String getString(String key) {
		if (getSwingSet2() != null) {
			return getSwingSet2().getString(key);
		} else {
			return "nada";
		}
	}

	/**
	 * get Mnemonic Char for String resource
	 * @param key resource name
	 * @return char
	 */
    public char getMnemonic(String key) {
        return (getString(key)).charAt(0);
    }

    /**
     * create ImageIcon from resource
     * @param filename of the resource image file
     * @param description of the resource
     * @return ImageIcon
     */
    public ImageIcon createImageIcon(String filename, String description) {
        if(getSwingSet2() != null) {
            return getSwingSet2().createImageIcon(filename, description);
        } else {
            String path = "/resources/images/" + filename;
            return new ImageIcon(getClass().getResource(path), description);
        }
    }

    /**
     * getter
     * @return sourceCode
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * loadSourceCode TODO
     */
    public void loadSourceCode() {
        if(getResourceName() == null) {
        	LOG.warning("No resource to load");
        } else {
        	String resource = getResourceName() + ".java";
        	InputStream is = StaticUtilities.getResourceAsStream(this.getClass(), resource);
        	
            sourceCode = new String("<html><body bgcolor=\"#ffffff\"><pre>");
            InputStreamReader isr = null;
            CodeViewer cv = new CodeViewer();

            try {
            	if(is!=null) { 
                    isr = new InputStreamReader(is, "UTF-8");
            	}
                BufferedReader reader = new BufferedReader(isr); // not closed!

                // Read one line at a time, htmlize using super-spiffy
                // html java code formating utility from www.CoolServlets.com
                String line = reader.readLine();
                while(line != null) {
                    sourceCode += cv.syntaxHighlight(line) + " \n ";
                    line = reader.readLine();
                }
                sourceCode += new String("</pre></body></html>");
            } catch (Exception ex) {
                sourceCode = "Could not load resource " + resource;
            } finally {
            	//isr.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getString(getResourceName() + ".name");
    };

    /**
     * create Icon from resource
     * @return Icon
     */
    public Icon getIcon() {
        return createImageIcon(iconPath, getResourceName() + ".name");
    };

    /**
     * get tooltip Resource
     * @return tooltip
     */
    public String getToolTip() {
        return getString(getResourceName() + ".tooltip");
    };

    /**
     * the main implementation
     */
    public void mainImpl() {
    	JFrame frame = swingset.getFrame();
    	frame.setName(getName());
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
        getDemoPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * create a HorizontalPanel
     * @param threeD true to set lowered Border
     * @return JPanel
     */
    public JPanel createHorizontalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    /**
     * create a VerticalPanel
     * @param threeD true to set lowered Border
     * @return JPanel
     */
    public JPanel createVerticalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(Component.TOP_ALIGNMENT);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    /**
     * the main method
     * @param args no args accepted
     */
    public static void main(String[] args) {
        DemoModule demo = new DemoModule(null);
        demo.mainImpl();
    }

    /**
     * init layout
     */
    public void init() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
    }

    void updateDragEnabled(boolean dragEnabled) {}

	@Override // interface RootPaneContainer
	public JRootPane getRootPane() {
		LOG.info("**TODO** methode abgeschrieben von JApplet");
        return SwingUtilities.getRootPane(this);
	}

	@Override
	public void setContentPane(Container contentPane) {
		LOG.info("**TODO** methode abgeschrieben von JApplet param Container:"+contentPane);
        getRootPane().setContentPane(contentPane);
	}

	@Override
	public Container getContentPane() {
		LOG.info("**TODO** methode abgeschrieben von JApplet");
        return getRootPane().getContentPane();		
	}

	@Override
	public void setLayeredPane(JLayeredPane layeredPane) {
		LOG.info("**TODO** methode abgeschrieben von JApplet param JLayeredPane:"+layeredPane);
        getRootPane().setLayeredPane(layeredPane);
	}
	
	@Override
	public JLayeredPane getLayeredPane() {
		LOG.info("**TODO** methode abgeschrieben von JApplet");
        return getRootPane().getLayeredPane();
	}

	@Override
	public void setGlassPane(Component glassPane) {
		LOG.info("**TODO** methode abgeschrieben von JApplet param Component:"+glassPane);
        getRootPane().setGlassPane(glassPane);
	}

	@Override
	public Component getGlassPane() {
		LOG.info("**TODO** methode abgeschrieben von JApplet");
        return getRootPane().getGlassPane();
	}

	// TODO aus JApplet :
    interface HasGetTransferHandler {

        /** Returns the {@code TransferHandler}.
         *
         * @return The {@code TransferHandler} or {@code null}
         */
        public TransferHandler getTransferHandler();
    }

//    @BeanProperty(hidden = true, description
//            = "Mechanism for transfer of data into the component")
    private TransferHandler transferHandler;
    public void setTransferHandler(TransferHandler newHandler) {
        TransferHandler oldHandler = transferHandler;
        transferHandler = newHandler;
        /* not visible:
    static void installSwingDropTargetAsNecessary(Component c, TransferHandler t) {
vll geht es mit introspection????
         */
//        SwingUtilities.installSwingDropTargetAsNecessary(this, transferHandler);
        firePropertyChange("transferHandler", oldHandler, newHandler);
    }

}
