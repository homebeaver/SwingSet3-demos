package swingset.plaf;

import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

public class LaFUtils {

    private static final Logger LOG = Logger.getLogger(LaFUtils.class.getName());

    private LaFUtils() {}
    
    public static boolean setLAF(String nameSnippet) {
    	String currentClassName = UIManager.getLookAndFeel().getClass().getName();
    	if(currentClassName.contains(nameSnippet)) {
    		LOG.warning("current Laf is "+currentClassName);
    	}
    	UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
    	for (LookAndFeelInfo info : lafInfo) {
    		String lafClassName = info.getClassName();
    		if(lafClassName.contains(nameSnippet)) {
    			try {
    	    		LOG.info("switch to laf ClassName="+lafClassName + " from "+UIManager.getLookAndFeel());
					UIManager.setLookAndFeel(lafClassName);
					return !lafClassName.equals(currentClassName);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	LOG.warning("not found: "+nameSnippet);
    	return false;
    }

}
