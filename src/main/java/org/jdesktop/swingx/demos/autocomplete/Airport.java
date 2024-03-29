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
package org.jdesktop.swingx.demos.autocomplete;

/**
 * A simple data class for airport information.
 * 
 * @author Karl George Schaefer
 * @author Thomas Bierhance (original AutoCompleteDemoPanel.Airport)
 */
public class Airport {
	/** name */
	public String name;
	/** ICAO Code */
	public String icaoCode;
	/** IATA Code */
	public String iataCode;
    
	/**
	 * ctor 
	 * @param name String
	 * @param icaoCode String
	 * @param iataCode String
	 */
    public Airport(String name, String icaoCode, String iataCode) {
        this.name = name;
        this.icaoCode = icaoCode;
        this.iataCode = iataCode;
    }
    
    public String toString() {
        return this.name + " (" + icaoCode + "/" + iataCode + ")";
    }
}
