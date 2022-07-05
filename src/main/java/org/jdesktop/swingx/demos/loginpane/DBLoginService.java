package org.jdesktop.swingx.demos.loginpane;

import java.util.logging.Logger;

import org.jdesktop.swingx.auth.JDBCLoginService;

/**
 * A {@code LoginService} that implements a login to a database (postgresql).
 * 
 * TODO: add another db driver
 * - implement the getUserRoles()
 * 
 * @author EUGen hb https://github.com/homebeaver
 */
public class DBLoginService extends JDBCLoginService {
	
    private Logger LOG = Logger.getLogger(DBLoginService.class.getName());

    /* db-URL 
    
    jdbc:subprotocol:subname 				// url Aufbau Allgemein
    subprotocol ::= postgresql				// example
    subname ::= //localhost:5432/ad393		// example
    subname ::= //[user[:password]@][netloc][:port][/dbname][?param1=value1&...]
    subname ::= //{host}[:{port}]/[{database}]

     */
    static String getDriverName(String url) {
    	if(url.startsWith("jdbc:postgresql:")) return "org.postgresql.Driver";
    	if(url.startsWith("jdbc:h2:")) return "org.h2.Driver";
    	return "";
    }
    
    /**
     * Constructs the default service.
     * @param url db url
     */
    /*
     * in super:
    public JDBCLoginService(String jndiContext) // Java Naming and Directory Interface
    public JDBCLoginService(String driver, String url, Properties props) 
    public JDBCLoginService(String driver, String url) {
     */
    public DBLoginService(String url) {
        super(getDriverName(url), url);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {
    	LOG.info(name + " AT " + server);
    	boolean ret = super.authenticate(name, password, server);
    	if(ret) {
        	LOG.info(name + " conn=" + super.getConnection());
        	// Properties getClientInfo() throws SQLException;
        	// String getSchema() throws SQLException;
    	} else {
        	LOG.info(name + " ret=" + ret);
    	}
    	return ret;
    }

}
