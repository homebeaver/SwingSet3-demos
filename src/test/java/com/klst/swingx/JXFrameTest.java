package com.klst.swingx;

import javax.swing.JMenu;

import org.jdesktop.swingx.JXStatusBar;

import io.github.homebeaver.swingset.demo.DemoJXFrame;
import io.github.homebeaver.swingset.demo.MainJXframe;

public class JXFrameTest {

	public static void main(String args[]) {
//		setSystemLF(true);
//      Locale.setDefault(new Locale("es"));
		JXFrameTest test = new JXFrameTest();
		try {
			test.interactiveMultipleFrames();
//            test.runInteractiveTests("interactive.*Compare.*");
		} catch (Exception e) {
			System.err.println("exception when executing interactive tests:");
			e.printStackTrace();
		}
	}

    /**
     * interactiveMultipleFrames shows control frame (gossip)
     * <p>
     * - the gossip RootFrame is a Subclass of WindowFrame;  
     * 		it has a JMenuBar at top to control the L&F and an empty JXStatusBar at bottom 
     * 		and contains a very simple frame manager to create new WindowFrame's
     * - to keep it simple all WindowFrame's including gossip contains same JXPanel in BorderLayout
     * 		two Buttons at WEST and EAST, and empty CENTER
     * 		the WEST button can be used to create new frames, the action is done by RootFrame's frame manager
     * <p>
     * the pause button stops the frame manager to create new windows
     * <p>
     * closing behavior :
     * - closing gossip RootFrame do EXIT_ON_CLOSE - closes all Windows
     * - closing other WindowFrame closes only this window
     */
    public void interactiveMultipleFrames() {
    	DemoJXFrame gossip = MainJXframe.getInstance(); // MainJXframe contains a simple frame manager
		JXStatusBar statusBar = gossip.getStatusBar(); // just to paint it

        JMenu demoMenu = gossip.createDemosMenu();
        if(demoMenu!=null) gossip.getJMenuBar().add(demoMenu);

        gossip.pack(); // auto or fix:
//        gossip.setSize(680, 200);
    	gossip.setVisible(true);
    }

}
