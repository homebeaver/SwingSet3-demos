package org.jdesktop.swingx.demos.xlist;

import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.demos.svg.CircleFlagCA;
import org.jdesktop.swingx.demos.svg.CircleFlagCH;
import org.jdesktop.swingx.demos.svg.CircleFlagCZ;
import org.jdesktop.swingx.demos.svg.CircleFlagDE;
import org.jdesktop.swingx.demos.svg.CircleFlagES;
import org.jdesktop.swingx.demos.svg.CircleFlagFR;
import org.jdesktop.swingx.demos.svg.CircleFlagIT;
import org.jdesktop.swingx.demos.svg.CircleFlagNL;
import org.jdesktop.swingx.demos.svg.CircleFlagPL;
import org.jdesktop.swingx.demos.svg.CircleFlagPT;
import org.jdesktop.swingx.demos.svg.CircleFlagSE;
import org.jdesktop.swingx.demos.svg.CircleFlagUA;
import org.jdesktop.swingx.demos.svg.CircleFlagUS;
import org.jdesktop.swingx.demos.svg.CircleFlagZA;
import org.jdesktop.swingx.icon.SizingConstants;

public interface ListDemoConstants {

    public static Icon flagIcons[] = new Icon[] {
    		CircleFlagCA.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagCH.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagCZ.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagDE.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagES.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagFR.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagIT.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagNL.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagPL.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagPT.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagSE.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagUA.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagUS.of(SizingConstants.S, SizingConstants.S),
    		CircleFlagZA.of(SizingConstants.S, SizingConstants.S),
    };
    
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
