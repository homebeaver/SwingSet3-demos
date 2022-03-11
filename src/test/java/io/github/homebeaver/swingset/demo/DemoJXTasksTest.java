package io.github.homebeaver.swingset.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import org.jdesktop.test.EDTRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EDTRunner.class)
public class DemoJXTasksTest {

    private static final Logger LOG = Logger.getLogger(DemoJXTasksTest.class.getName());

    @Test
    public void testCtor() {
    	DemoJXTasks t1 = DemoJXTasks.getInstance();
    	DemoJXTasks t2 = DemoJXTasks.getInstance();
    	assertNotNull(t1); 
    	assertEquals(t1, t2); 
    }

}
