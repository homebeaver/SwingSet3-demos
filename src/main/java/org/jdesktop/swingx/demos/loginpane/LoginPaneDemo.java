/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.loginpane;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXLoginPane.Status;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.auth.DefaultUserNameStore;
import org.jdesktop.swingx.auth.KeyChain;
import org.jdesktop.swingx.auth.PasswordStore;
import org.jdesktop.swingx.auth.UserNameStore;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.PaintUtils;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXLoginPane} with custom banner.
 *
 * @author Karl George Schaefer
 * @author rah003 (original JXLoginPaneDemo)
 * @author hb https://github.com/homebeaver (locale lang selector + custom moon banner)
 */
//@DemoProperties(
//    value = "JXLoginPane Demo",
//    category = "Controls",
//    description = "Demonstrates JXLoginPane, a security login control.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/loginpane/LoginPaneDemo.java",
//        "org/jdesktop/swingx/demos/loginpane/resources/LoginPaneDemo.properties",
//        "org/jdesktop/swingx/demos/loginpane/resources/LoginPaneDemo.html",
//        "org/jdesktop/swingx/demos/loginpane/resources/images/LoginPaneDemo.png",
//        "org/jdesktop/swingx/demos/loginpane/DemoLoginService.java"
//    }
//)
public class LoginPaneDemo extends AbstractDemo {
	
	private static final long serialVersionUID = 545678580592168192L;
	private static final Logger LOG = Logger.getLogger(LoginPaneDemo.class.getName());
    private static final Font SANSSERIF16 = new Font("SansSerif", Font.PLAIN, 16);
	private static final String DESCRIPTION = "Demonstrates JXLoginPane, a security login control.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE); // turn off bold fonts in Metal
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new LoginPaneDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    private PasswordStore ps;
    private UserNameStore us = null; // not used ==> DefaultUserNameStore
    private DemoLoginService service;
    private JXLoginPane loginPane;
    private JXLabel statusLabel;
    // controler:
    private JToggleButton allowLogin;
    private JXButton loginLauncher;
    
    public LoginPaneDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));

        JXPanel n = new JXPanel(new HorizontalLayout());
        JXLabel status = new JXLabel("Status:");
        status.setFont(SANSSERIF16);
        status.setHorizontalAlignment(SwingConstants.RIGHT);
        n.add(status);
        statusLabel = new JXLabel(loginPane==null ? Status.NOT_STARTED.toString() : loginPane.getStatus().name());
        statusLabel.setFont(SANSSERIF16);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        n.add(statusLabel);
        super.add(n, BorderLayout.NORTH);
	}

    @Override
	public JXPanel getControlPane() {

        JXPanel controller = new JXPanel(new BorderLayout());

        loginLauncher = new JXButton();
        loginLauncher.setName("launcher");
        loginLauncher.setText(getBundleString("launcher.text"));
        loginLauncher.setFont(SANSSERIF16);
        final Painter<Component> orangeBgPainter = new MattePainter(PaintUtils.ORANGE_DELIGHT, true);
        loginLauncher.setBackgroundPainter(orangeBgPainter);
        loginLauncher.addMouseListener(new MouseAdapter() { // disable BG painter
            @Override
            public void mouseEntered(MouseEvent e) {
            	loginLauncher.setBackgroundPainter(null);
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	loginLauncher.setBackgroundPainter(orangeBgPainter);
            }          
        });
        bind(); // registriert den event Observer aka Listener
        
        controller.add(loginLauncher, BorderLayout.SOUTH); // bei CENTER wird es nicht angezeigt?
        
        JXPanel p = new JXPanel(new VerticalLayout());
        controller.add(p);
        
        allowLogin = new JRadioButton(); // JRadioButton extends JToggleButton
        allowLogin.setFont(SANSSERIF16);
        allowLogin.setName("allowLogin");
        allowLogin.setText(getBundleString("allowLogin.text"));
        // TODO getBundleString indiziert Mnemonic: allowLogin.text=&Allow Login - wird aber nicht gesetzt
        allowLogin.setSelected(Boolean.valueOf(getBundleString("allowLogin.selected")));
        JRadioButton disallowLogin = new JRadioButton("disallow"); // JRadioButton extends JToggleButton
        disallowLogin.setFont(SANSSERIF16);
      //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(allowLogin);
        group.add(disallowLogin);
        JPanel radioPanel = new JPanel(new java.awt.GridLayout( 1, 2 ));
        radioPanel.add(allowLogin);
        radioPanel.add(disallowLogin);
        p.add(radioPanel);

    	return controller;
    }

    protected void bind() {
        // bind source allowLogin button to target service.validLogin erst nach createLoginPaneDemo()!
		// d.h. bind() registriert nur den event Observer aka Listener
    	loginLauncher.addActionListener(event -> {
    		if(loginPane==null) {
    			createLoginPaneDemo();
    	        Bindings.createAutoBinding(READ, 
    	        		allowLogin, BeanProperty.create("selected"),
    	                service, BeanProperty.create("validLogin")
    	                ).bind();
    		}
//    		if(selectedLocale!=null) loginPane.setLocale(this.selectedLocale);
    		loginPane.setLocale(super.getLocale());
   		
    		if(statusLabel.getText().equals(Status.SUCCEEDED.toString())) {
    			LOG.info("status:SUCCEEDED!!!! - do reset ...");
    			loginPane = null;
    			statusLabel.setText(Status.NOT_STARTED.toString());
    			loginLauncher.setText("reset done, launch again.");
    			return;
    		}
    		
    		Status status = JXLoginPane.showLoginDialog(LoginPaneDemo.this, loginPane); // 1623
    		statusLabel.setText(status.toString());
    		
    		if(status==Status.SUCCEEDED) {
    			String[] userNames = loginPane.getUserNameStore().getUserNames();
    			String allNames = Arrays.toString(userNames);
    			LOG.info("User:"+loginPane.getUserName() + " of "+userNames.length + allNames
    				+ ", Server:"+service.getServer() + ", LoginService:"+loginPane.getLoginService().getServer()
					+ ", isRememberPassword? : "+loginPane.isRememberPassword());  			
    			ps.set("USER#allNames", null, allNames.toCharArray()); // userNames persistent ablegen
    			if(loginPane.isRememberPassword()) {
    				// wurde schon in loginPane gemacht:
    				//ps.set(loginPane.getUserName(), null, loginPane.getPassword());
    			}
				((InnerFilePasswordStore)ps).store(); // make ps persistent
				
    			loginPane.setVisible(false);
    			loginLauncher.setText("login "+Status.SUCCEEDED.toString());
    			// kein disable -> launch kann beliebig of wiederholt werden -> reset
    			//loginLauncher.setEnabled(false);
    		}
    	});   	
    }
    
    private void createLoginPaneDemo() {
        final String[] serverArray = { null
//        		, "sun-ds.sfbay" 
//            	, "jdbc:postgresql://localhost/demo", "jdbc:postgresql://localhost/ad393"
        		, "jdbc:h2:~/data/H2/demodata" };
        
        service = new DemoLoginService(serverArray);
    	ps = new InnerFilePasswordStore();
    	char[] allNames = ps.get("USER#allNames", null);
    	/*
    	   allNames==null => ps ist leer
    	   allNames=[a]   => ein user
    	   allNames=[a, b, beta] => mehrere user
    	 */
//    	us = new LoggingUserNameStore(); // uncomment to be verbose 
    	us = new DefaultUserNameStore();
    	LOG.info("-------------------> us:"+us + ", allNames="+(allNames==null?"null":String.valueOf(allNames)));
    	if(allNames!=null) {
    		String allNamesasString = String.valueOf(allNames);
    		String[] names = allNamesasString.substring(1, allNamesasString.length()-1).split(", ");
        	int l = names.length;
        	for(int i=0;i<names.length;i++) {
            	LOG.info("-------------------> #allNames="+l + " "+i+":"+names[i]);
        		us.addUserName(names[i]);
        	}
        	us.saveUserNames();
    	}
        
        loginPane = new JXLoginPane(service, ps, us);
        LOG.info("banner:"+loginPane.getBanner());
        List<String> servers = new ArrayList<String>(Arrays.asList(serverArray));
        loginPane.setServers(servers);

        loginPane.addPropertyChangeListener("status", new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				JXLoginPane.Status status = (JXLoginPane.Status)evt.getNewValue();
				JXLoginPane.Status old = (JXLoginPane.Status)evt.getOldValue();
				LOG.info("new status is "+status + " old:"+old);
				statusLabel.setText(status.toString());
				LoginPaneDemo.this.validate();
			}
        	
        });
        
        // customization:
//        loginPane.setBanner(null); // No banner (customization)
        loginPane.setBanner(new MoonLoginPaneUI(loginPane).getBanner());
//        loginPane.setBannerText("BannerText");
        
        // nicht notwendig: wird anhand ps+us gesetzt:
//        loginPane.setSaveMode(SaveMode.PASSWORD);
    }
   
    public class LoggingUserNameStore extends DefaultUserNameStore {
    	
    	LoggingUserNameStore() {
    		super();
        	LOG.info(">>>>>>>>>>> ctor");
    	}
    	
        @Override
        public void addUserName(String name) {
        	LOG.info(">>>>>>>>>>> name="+name);
        	super.addUserName(name);
        }

        @Override
        public void removeUserName(String name) {
        	LOG.info(">>>>>>>>>>> name="+name);
        	super.removeUserName(name);
        }
        
        @Override
        public String[] getUserNames() {
        	String[] res = super.getUserNames();
        	LOG.info(">>>>>>>>>>> " + " results to array length="+(res==null ? "nix" : res.length));
        	return res;
        }
        @Override
        public boolean containsUserName(String name) {
        	boolean res = super.containsUserName(name);
        	LOG.info(">>>>>>>>>>> name="+name + " results to "+res);
        	return res;
        }
        
        public void loadUserNames() {
        	super.loadUserNames();
        	LOG.info(">>>>>>>>>>> done.");
        }
        public void saveUserNames() {
        	super.saveUserNames();
        	LOG.info(">>>>>>>>>>> done.");
        }
    }

    /*
     * Alternative
     * https://github.com/xafero/java-keyring
     * 
     * wg. static class see
     * https://stackoverflow.com/questions/70324/java-inner-class-and-static-nested-class
     * https://stackoverflow.com/questions/253492/static-nested-class-in-java-why
     * https://stackoverflow.com/questions/16524373/why-and-when-to-use-static-inner-class-or-instance-inner-class
     */
    private static final class InnerFilePasswordStore extends PasswordStore {
    	
    	private static final String FILENAME = "KeyChainInnerStore.txt";
    	
    	KeyChain kc;
    	
    	public InnerFilePasswordStore() {
            FileInputStream fis = null;
    		try {
    	        File file = new File(FILENAME); // in eclipse ws : swingset\SwingSet3\swingx-demos
    	        if (!file.exists()) {
    	            file.createNewFile(); // throws IOException
    	            LOG.info("created "+FILENAME);
    	            fis = null;
    	        } else {
    	            fis = new FileInputStream(file); // throws FileNotFoundException
    	            LOG.info("use existing "+FILENAME);
    	        }
    		} catch (FileNotFoundException e) {
                LOG.warning("new FileInputStream throws"+e);
    		} catch (IOException e) {
                LOG.warning("file.createNewFile throws"+e);
    		}
    		
            try {
    			kc = new KeyChain("test".toCharArray(), fis);
    			store(); // store the empty DS
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    	}

    	// equals und hashCode sind in JXLoginPane.NullPasswordStore definiert. Wozu?
//    	@Override // overwrites Object.equals
//        public boolean equals(Object obj) {
//            return obj instanceof InnerFilePasswordStore;
//        }
//    	@Override // overwrites Object.hashCode
//        public int hashCode() {
//            return 17;
//        }

    	/**
    	 *  Persist the KeyChain and reflect any changes, <b>store</b> with an OutputStream.
    	 */
        public void store() {
            FileOutputStream fos = null;
            File file = new File(FILENAME);
            try {
                fos = new FileOutputStream(file); // throws FileNotFoundException
    			kc.store(fos); // throws IOException
                LOG.info("PasswordStore stored to "+FILENAME);
    		} catch (FileNotFoundException e) {
                LOG.warning("new FileOutputStream throws"+e);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }

        /**
         * Adds a password to the store for a given account/user and server.
         * 
         * {@inheritDoc}
         *
         *  @param username account/user
         *  @param server server used for authentication, can be null
         *  @param password password to save. 
         *  	Password can't be null. Use empty array for empty password.
         *  @return <code>true</code> if stored, <code>false</code> if password is null
         */
    	@Override
    	public boolean set(String username, String server, char[] password) {
    		if(password==null) return false;
    		LOG.info("username="+username + ", server="+server);
    		kc.addPassword(username, server, password);
    		return true;
    	}

        /**
         * {@inheritDoc}
         */
    	@Override
    	public char[] get(String username, String server) {
    		String pw = kc.getPassword(username, server);
    		return pw==null ? null : pw.toCharArray();
    	}

        /**
         * {@inheritDoc}
         */
    	@Override
    	public void removeUserPassword(String username) {
            LOG.info("TODO username="+username);
    		// TODO ? server
    	}

    }
}
