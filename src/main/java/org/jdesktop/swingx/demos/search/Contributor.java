/*
 * Created on 06.12.2008
 *
 */
package org.jdesktop.swingx.demos.search;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * used in SearchDemo, XListDemo, HighlighterExtDemo
 */
public class Contributor implements Comparable<Contributor> {
	
    private String firstName;
    private String lastName;
    private String userID;
    private URI devnetMail;
    private int merits;
    private Date since;
    
    /**
     * ctor
     * @param rawData a String with raw data
     */
    public Contributor(String rawData) {
        setData(rawData);
        merits = createRandomMerits();
        since = createRandomJoinedDate();
    }

    /**
     * merits getter
     * @return merits
     */
    public int getMerits() {
        return merits;
    }
    
    /**
     * since Date getter
     * @return since
     */
    public Date getJoinedDate() {
        return since;
    }
    
    /**
     * firstName getter
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * lastName getter
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * userID getter
     * @return userID
     */
    public Object getID() {
        return userID;
    }
    
    /**
     * Returns Email URI
     * @return URI
     */
    public URI getEmail() {
        return devnetMail;
    }
    
    public String toString() {
		return getFirstName() + " " + getLastName() + " (" + getMerits() + ")";
    }
    
    /**
     * @param rawData
     */
    private void setData(String rawData) {
        if (rawData == null) {
            lastName = " <unknown> ";
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(rawData);
        try {
           firstName = tokenizer.nextToken();
           lastName = tokenizer.nextToken();
           userID = tokenizer.nextToken();
           devnetMail = new URI("mailto:" + userID + "@dev.java.net");
        } catch (Exception ex) {
            // don't care ...
        }
        
    }

    private Date createRandomJoinedDate() {
        Calendar sinceBase = Calendar.getInstance();
        sinceBase.add(Calendar.YEAR, -5);
        long max = new Date().getTime() - sinceBase.getTimeInMillis();
        Date entry = new Date(sinceBase.getTimeInMillis() + Double.valueOf(Math.random() * max).longValue());
        return entry;
    }

    private int createRandomMerits() {
    	return Double.valueOf(Math.random() * 100).intValue();
    }

    @Override // implements Comparable<Contributor> interface
    public int compareTo(Contributor o) {
        if (!(o instanceof Contributor)) return -1;
        return lastName.compareTo(((Contributor) o).lastName);
    }
}