/*
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.demos.loginpane;

import java.util.Arrays;
import java.util.List;

import org.jdesktop.swingx.auth.LoginService;

/**
 * A {@code LoginService} that can be modified to allow or disallow logins. 
 * Only useful for demonstration purposes.
 * 
 * @author Karl George Schaefer
 * @author EUG : ctor DemoLoginService(String[] serverArray), DemoLoginService(List serverList)
 */
public class DemoLoginService extends LoginService {
    private boolean validLogin;
    List<String> servers;
    
    /**
     * Constructs the default service.
     */
    public DemoLoginService() {
        this(new String[]{null});
    }
    /**
     * ctor
     * @param serverArray servers
     */
    public DemoLoginService(String[] serverArray) {
    	this(Arrays.asList(serverArray));
    }
    /**
     * ctor
     * @param serverList List of servers
     */
    public DemoLoginService(List<String> serverList) {
        setSynchronous(true);
        servers = serverList;
        if(servers.size()>0) setServer(servers.get(0));
    }
 
    /**
     * get Servers
     * @return List of Servers
     */
    public List<String> getServers() {
        return servers;
    }

    /**
     * {@inheritDoc}
     */
   public String getServer() {
        return super.getServer();
    }

   /**
    * {@inheritDoc}
    */
    public void setServer(String server) {
    	if(servers.contains(server)) {
    		super.setServer(server);
    	}
    }

    /**
     * @return the validLogin
     */
    public boolean isValidLogin() {
        return validLogin;
    }

    /**
     * @param validLogin the validLogin to set
     */
    public void setValidLogin(boolean validLogin) {
        this.validLogin = validLogin;
    }

    /**
     * Implements abstract method defined in LoginService.
     * Only useful for demonstration purposes.
     * 
     * @param name
     *            username
     * @param password
     *            password
     * @param server
     *            server (optional)
     * 
     * @return <code>validLogin</code> prop for the first server otherwise false
     * @throws Exception TODO explain
     */
    @Override
    public boolean authenticate(String name, char[] password, String server) throws Exception {
    	if(server==servers.get(0)) return isValidLogin();
    	return false;
    }

}
