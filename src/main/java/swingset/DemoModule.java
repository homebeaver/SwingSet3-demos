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
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
public class DemoModule extends Panel implements Accessible, RootPaneContainer {

	private static final long serialVersionUID = 5010903388735156025L;
    private static final Logger LOG = Logger.getLogger(DemoModule.class.getName());
	
	// The preferred size of the demo
    private int PREFERRED_WIDTH = 680;
    private int PREFERRED_HEIGHT = 600;

    Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),
                                              new EmptyBorder(5,5,5,5));

    // Premade convenience dimensions, for use wherever you need 'em.
    public static Dimension HGAP2 = new Dimension(2,1);
    public static Dimension VGAP2 = new Dimension(1,2);

    public static Dimension HGAP5 = new Dimension(5,1);
    public static Dimension VGAP5 = new Dimension(1,5);

    public static Dimension HGAP10 = new Dimension(10,1);
    public static Dimension VGAP10 = new Dimension(1,10);

    public static Dimension HGAP15 = new Dimension(15,1);
    public static Dimension VGAP15 = new Dimension(1,15);

    public static Dimension HGAP20 = new Dimension(20,1);
    public static Dimension VGAP20 = new Dimension(1,20);

    public static Dimension HGAP25 = new Dimension(25,1);
    public static Dimension VGAP25 = new Dimension(1,25);

    public static Dimension HGAP30 = new Dimension(30,1);
    public static Dimension VGAP30 = new Dimension(1,30);

    private SwingSet2 swingset = null;
    private JPanel panel = null;
    private String resourceName = null;
    private String iconPath = null;
    private String sourceCode = null;

    public DemoModule(SwingSet2 swingset) {
        this(swingset, null, null);
    }

    public DemoModule(SwingSet2 swingset, String resourceName, String iconPath) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        this.resourceName = resourceName;
        this.iconPath = iconPath;
        this.swingset = swingset;

        loadSourceCode();
    }

    public String getResourceName() {
        return resourceName;
    }

    public JPanel getDemoPanel() {
        return panel;
    }

    public SwingSet2 getSwingSet2() {
        return swingset;
    }


	public String getString(String key) {
		if (getSwingSet2() != null) {
			return getSwingSet2().getString(key);
		} else {
			return "nada";
		}
	}

    public char getMnemonic(String key) {
        return (getString(key)).charAt(0);
    }

    public ImageIcon createImageIcon(String filename, String description) {
        if(getSwingSet2() != null) {
            return getSwingSet2().createImageIcon(filename, description);
        } else {
            String path = "/resources/images/" + filename;
            return new ImageIcon(getClass().getResource(path), description);
        }
    }


    public String getSourceCode() {
        return sourceCode;
    }

    public void loadSourceCode() {
    	LOG.config("ResourceName:"+getResourceName());
        if(getResourceName() != null) {
        	String packagename = "swingset/";
            String filename = packagename + getResourceName() + ".java";
        	String dir = "src/main/java/"; // m2e folder (ohne package)  
            File f = new File(dir+filename);
            InputStream is = null;
        	if(f.canRead()) {
            	LOG.info("AbsolutePath:"+f.getAbsolutePath());
        	} else {
        		LOG.warning("cannot read "+f + " >>> try default eclipse source folder ...");
        		dir = "src/"; // this is default eclipse folder
        		f = new File(dir+filename);
        		if(!f.canRead()) {
            		f = new File(getResourceName() + ".java"); 
            		// get resource from jar:
            		java.net.URL url = getClass().getResource(getResourceName() + ".java");
            		try {
            			is = url.openStream();
						LOG.warning("cannot find eclipse source "+filename + " / " + is);
					} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}
            sourceCode = new String("<html><body bgcolor=\"#ffffff\"><pre>");
            InputStreamReader isr = null;
            CodeViewer cv = new CodeViewer();

            try {
            	if(is==null) is = new FileInputStream(f);
                isr = new InputStreamReader(is, "UTF-8");
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
                sourceCode = "Could not load file: " + filename;
            } finally {
            	//isr.close();
            }
        }
    }

    public String getName() {
        return getString(getResourceName() + ".name");
    };

    public Icon getIcon() {
        return createImageIcon(iconPath, getResourceName() + ".name");
    };

    public String getToolTip() {
        return getString(getResourceName() + ".tooltip");
    };

    public void mainImpl() {
        JFrame frame = new JFrame(getName());
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(getDemoPanel(), BorderLayout.CENTER);
        getDemoPanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createHorizontalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setAlignmentY(TOP_ALIGNMENT);
        p.setAlignmentX(LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    public JPanel createVerticalPanel(boolean threeD) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentY(TOP_ALIGNMENT);
        p.setAlignmentX(LEFT_ALIGNMENT);
        if(threeD) {
            p.setBorder(loweredBorder);
        }
        return p;
    }

    public static void main(String[] args) {
        DemoModule demo = new DemoModule(null);
        demo.mainImpl();
    }

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
