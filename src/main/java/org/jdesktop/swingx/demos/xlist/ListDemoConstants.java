package org.jdesktop.swingx.demos.xlist;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

public interface ListDemoConstants {

    public static final int VERTICAL_WRAP = JList.VERTICAL_WRAP;
    public static final int HORIZONTAL_WRAP = JList.HORIZONTAL_WRAP;
    public static String[] LIST_LAYOUT_ORIENTATION = 
    	{ "VERTICAL - a single column" 
    	, "VERTICAL_WRAP - flowing vertically then horizontally" 
    	, "HORIZONTAL_WRAP - flowing horizontally then vertically"
    	};

    public static final int SINGLE_INTERVAL_SELECTION = ListSelectionModel.SINGLE_INTERVAL_SELECTION;
    public static String[] SELECTION_MODE = 
    	{ "SINGLE_SELECTION - one list index at a time" 
    	, "SINGLE_INTERVAL_SELECTION - one contiguous range" 
    	, "MULTIPLE_INTERVAL_SELECTION - one or more contiguous ranges"
    	};

    // see enum DropMode.DropMode
    public static String[] DROP_MODE = 
    	{ "USE_SELECTION - item moves to echo the potential drop point (not recommended)" 
    	, "ON - used to drop on top of existing list items" 
    	, "INSERT - select the space between existing list items"
    	, "ON_OR_INSERT - a combination of the ON and INSERT"
    	};

}
