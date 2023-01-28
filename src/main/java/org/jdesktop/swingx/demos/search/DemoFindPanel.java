package org.jdesktop.swingx.demos.search;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.demos.svg.FeatheRinfo;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.search.Searchable;

/**
 * A FindPanel which uses JXSearchField.
 * 
 * @author homeb
 */
@SuppressWarnings("serial")
public class DemoFindPanel extends JXFindPanel {

    private static final Logger LOG = Logger.getLogger(DemoFindPanel.class.getName());

    protected JButton findNext;
    protected JButton findPrevious;

    public DemoFindPanel() {
        this(null);
    }
    
    public DemoFindPanel(Searchable searchable) {
    	super(searchable);
    }

    /**
     * {@inheritDoc} <p>
     * Overridden to replace informationIcon in <code>JOptionPane.showMessageDialog</code>
     * and use message containing bold searchString
     */
    /* code in super JXFindPanel:
        JOptionPane.showMessageDialog(this, getUIString("notFound"));
		uses: "OptionPane.informationIcon" // ... , blue circle with i
		replace with "org.jdesktop.swingx.demos.svg.FeatheRinfo"
     */
    @Override
    protected void showNotFoundMessage() {
    	RadianceIcon informationIcon = FeatheRinfo.of(RadianceIcon.BUTTON_ICON, RadianceIcon.BUTTON_ICON);
    	informationIcon.setColorFilter(color -> Color.BLUE);
    	// BUG, wenn searchField.getText() html Zeichen enthält zB <
    	// "a<xx" ==> Nachricht: "Ausgruck nicht gefinden: a" TODO
    	LOG.info("----BUG------searchField.getText():"+searchField.getText());
    	String msg = "<html>" + getUIString("notFound") + ":<font color=red><b> "+searchField.getText()+"</b></font></html>";
        JOptionPane.showMessageDialog
    	( this                                                              // parentComponent
//    	, getUIString("notFound")                                           // message 
    	, msg
    	, UIManager.getString("OptionPane.messageDialogTitle", getLocale()) // String title, nls : "Meldung" / Message
    	, JOptionPane.INFORMATION_MESSAGE
    	, informationIcon
        );
    }

    @Override
    // wie in JXFindBar;
    protected void bind() {
        super.bind();
        searchField.addActionListener(getAction(JXDialog.EXECUTE_ACTION_COMMAND));
        findNext.setAction(getAction(FIND_NEXT_ACTION_COMMAND));
        findPrevious.setAction(getAction(FIND_PREVIOUS_ACTION_COMMAND));
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke, JXDialog.CLOSE_ACTION_COMMAND);
    }

    @Override
    // wie in JXFindBar;
    protected void build() {
        setLayout(new FlowLayout(SwingConstants.LEADING));
        add(searchLabel);
        add(new JLabel(":"));
        add(new JLabel("  "));
        add(searchField);
        add(findNext);
        add(findPrevious);
        
        add(matchCheck); // Case sensitiv / Groß/Kleinschreibung
        add(wrapCheck);  // Wrap Seach
//        add(backCheck); // Backward / Rückwärts
    }

	private static final String SEARCHFIELD_PROMPT = "Enter a search text here";
    /**
     * {@inheritDoc} <p>
     * Overridden to use <code>JXSearchField</code> with "textHighlight" Background.
     * This color is defined in BasicLookAndFeel.initSystemColorDefaults : Text background color when selected
     */
    @Override
    protected void initComponents() {
/* in AbstractPatternPanel
        searchLabel = new JLabel();
        searchField = new JTextField(getSearchFieldWidth()) { <==== das will ich überschreiben
            @Override
            public Dimension getMaximumSize() {
                Dimension superMax = super.getMaximumSize();
                superMax.height = getPreferredSize().height;
                return superMax;
            }
        };
        matchCheck = new JCheckBox();
 */
/* in JXFindPanel:
        super.initComponents();
        wrapCheck = new JCheckBox();
        backCheck = new JCheckBox();
 */
        searchLabel = new JLabel();
        searchField = new JXSearchField(SEARCHFIELD_PROMPT);
        searchField.setColumns(super.getSearchFieldWidth());
//        LOG.info(">>>>>>>>>>>searchField:"+searchField);
//        LOG.info("searchField.Background:"+searchField.getBackground());
//        if(searchField.getBackground().getRGB()==-16777216) { // BLACK
        	searchField.setBackground(UIManager.getColor("textHighlight"));
            LOG.info("searchField.Background:"+searchField.getBackground());
//        }
        matchCheck = new JCheckBox();
        
        wrapCheck = new JCheckBox();
        backCheck = new JCheckBox();
        // wie in JXFindBar;
        findNext = new JButton();
        findPrevious = new JButton();
    }

}
