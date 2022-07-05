package org.jdesktop.swingx.demos.loginpane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.jdesktop.swingx.auth.KeyChain;
import org.jdesktop.swingx.auth.PasswordStore;

/**
 * implements a <b>KeyChain</b> based password store
 * 
 * @author homeb
 *
 */
public class FilePasswordStore extends PasswordStore {

    private static final Logger LOG = Logger.getLogger(FilePasswordStore.class.getName());
	private static final String FILENAME = "KeyChainStore.txt";
	
	KeyChain kc;
	
	/**
	 * ctor
	 */
	public FilePasswordStore() {
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
