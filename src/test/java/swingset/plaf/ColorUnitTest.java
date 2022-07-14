package swingset.plaf;

import java.awt.Color;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ColorUnitTest extends junit.framework.TestCase {

    @Test
    public void testPrimaryColors() {
    	
    	// 1. in value aka hashCode ist alpha FF mit drin, am Anfang:
    	Color FF666699 = new Color(0x666699);
    	System.out.println("FF666699="+FF666699 + " .value="+FF666699.hashCode());
    	assertEquals(0xFF666699, FF666699.hashCode());
    	assertEquals("dark blue gray", ColorUnit.getName(FF666699));
    	
    	// 2. ColorUnit.darkBlueGray entspricht Color(0x666699) und liefert den gleichen Namen
    	Color darkBlueGray = ColorUnit.DARK_BLUE_GRAY;
    	System.out.println("darkBlueGray="+darkBlueGray + " .value="+darkBlueGray.hashCode());
    	assertEquals(0xFF666699, darkBlueGray.hashCode());
    	assertEquals(FF666699, darkBlueGray);
    	assertEquals("dark blue gray", ColorUnit.getName(darkBlueGray));

    	// 3. ColorUnit.Steel_primary1 entspricht auch Color(0x666699), aber liefert einen anderen Namen 
    	Color Steel_primary1 = ColorUnit.STEEL_PRIMARY1;
    	System.out.println("Steel_primary1="+Steel_primary1 + " .value="+Steel_primary1.hashCode());
    	assertEquals(0xFF666699, Steel_primary1.hashCode());
    	assertEquals(FF666699, Steel_primary1);
    	// Steel_primary1 gleicht darkBlueGray , aber hat einen anderen Namen
    	assertEquals("primary1,STEEL", ColorUnit.getName(Steel_primary1));
    	List<String> exp2 = ColorUnit.getNames(FF666699);
    	System.out.println("expected 2 different names="+exp2 + " for Color FF666699 "+FF666699);
    	assertEquals(2, exp2.size());
    	ColorUnit.printNames();
    }

}
