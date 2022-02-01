/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.table;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTable}. This demo displays the same functionality as
 * {@link com.sun.swingset3.demos.table.TableDemo}, using SwingX components and methodologies.
 * <p>
 * It is not possible to extend {@code TableDemo}, since the display components are private. This
 * class replicates contents and behavior in that class and may fall out of sync.
 * 
 * @author Karl George Schaefer
 * @author Jeanette Winzenberg (Devoxx '08 version)
 * @author aim (original TableDemo)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//        value = "JXTable Demo",
//        category = "Data",
//        description = "Demonstrates JXTable, an enhanced data grid display.",
//        sourceFiles = {
//                "org/jdesktop/swingx/demos/table/XTableDemo.java",
//                "org/jdesktop/swingx/demos/table/OscarRendering.java",
//            "org/jdesktop/swingx/demos/table/CustomColumnFactory.java",
//            "org/jdesktop/swingx/demos/table/OscarFiltering.java",
//            "org/jdesktop/swingx/demos/table/resources/XTableDemo.properties"
//        }
//    )
/*
Grob ok: TODO s:
Col Überschriften nicht aus props
Bei Nominee fehlen Sterne: props: winnerIcon = images/goldstar.png
Status läuft nicht und verschwindet nicht
Beschriftungsn beim Controller fehlen
Intro nicht da
 */
public class XTableDemo extends AbstractDemo {

	private static final long serialVersionUID = -2616149327587528185L;
	static final Logger LOG = Logger.getLogger(XTableDemo.class.getName());

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new XTableDemo(controller);
				JXFrame frame = new JXFrame("demo", exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
				((XTableDemo)demo).start();
			}		
    	});
    }


    private OscarTableModel oscarModel;

    private Stacker dataPanel; // com.sun.swingset3.demos.Stacker extends JLayeredPane TODO mit timing.Animator
    private JXTable oscarTable;
    private JComponent statusBarLeft;
    private JLabel actionStatus;
    private JLabel tableStatus;
    private JLabel tableRows;

    private JProgressBar progressBar;

    private OscarFiltering filterController;

    /**
     * XTableDemo Constructor
     */
    public XTableDemo(Frame frame) {
    	super(new BorderLayout());
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
//        setLayout(new BorderLayout());

//        controlPanel = createControlPanel();
//        add(controlPanel, BorderLayout.NORTH);
        oscarTable = createXTable();
        oscarTable.setName("oscarTable");
        
        JScrollPane scrollpane = new JScrollPane(oscarTable);
        dataPanel = new Stacker(scrollpane);
        add(dataPanel, BorderLayout.CENTER);

        add(createStatusBar(), BorderLayout.SOUTH);

//    public XTableDemo() {
//        initComponents();
        configureDisplayProperties();
//        DemoUtils.injectResources(this);
        bind();
        start();
    }

    // Controller:
    private JXPanel controlPanel;
    private JTextField filterField;
    private JCheckBox winnersCheckbox;

    @Override
	public JXPanel getControlPane() {
    	controlPanel = new JXPanel(); // ====> controlPanel = createControlPanel():
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        controlPanel.setLayout(gridbag);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.insets = new Insets(20, 10, 0, 10);
        c.anchor = GridBagConstraints.SOUTHWEST;
        JLabel searchLabel = new JLabel();
        searchLabel.setName("searchLabel");
        controlPanel.add(searchLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.insets.top = 0;
        c.insets.bottom = 12;
        c.anchor = GridBagConstraints.SOUTHWEST;
        //c.fill = GridBagConstraints.HORIZONTAL;
        filterField = new JTextField(24);
        filterField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
//                LOG.info("--------e:"+e.getSource());
//                JTextField f = (JTextField)e.getSource();
//                e.getActionCommand();
                //public void setFilterString(String filterString) {
                filterController.setFilterString(e.getActionCommand());
        	}
        });
        controlPanel.add(filterField, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        //c.insets.right = 24;
        //c.insets.left = 12;
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        winnersCheckbox = new JCheckBox();
        winnersCheckbox.setName("winnersLabel");
        winnersCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean showOnlyWinners = ((JCheckBox)e.getSource()).isSelected();
//                tableView.getTableHeader().setReorderingAllowed(flag);
//                tableView.repaint();
                // TODO filterController, BeanProperty.create("showOnlyWinners")));
                filterController.setShowOnlyWinners(showOnlyWinners);
            }
        });
        controlPanel.add(winnersCheckbox, c);

        return controlPanel;
    }
    /**
     * Customizes display properties of contained components.
     * This is data-unrelated.
     */
    private void configureDisplayProperties() {
        //<snip> JXTable display properties
        // show column control
        oscarTable.setColumnControlVisible(true);
        // replace grid lines with striping 
        oscarTable.setShowGrid(false, false);
        oscarTable.addHighlighter(HighlighterFactory.createSimpleStriping());
        // initialize preferred size for table's viewable area
        oscarTable.setVisibleRowCount(10);
//        </snip>
        
        //<snip> JXTable column properties
        // create and configure a custom column factory
        CustomColumnFactory factory = new CustomColumnFactory();
        OscarRendering.configureColumnFactory(factory, getClass());
        // set the factory before setting the table model
        oscarTable.setColumnFactory(factory);
//        </snip>

//        DemoUtils.setSnippet("JXTable display properties", oscarTable);
//        DemoUtils.setSnippet("JXTable column properties", oscarTable.getTableHeader());
//        DemoUtils.setSnippet("Filter control", filterField, winnersCheckbox, tableStatus, tableRows);
//        DemoUtils.setSnippet("Use SwingWorker to asynchronously load the data", statusBarLeft,
//                (JComponent) statusBarLeft.getParent());
    }

    /**
     * Binds components to data and user interaction.
     */
    protected void bind() {
        
        //<snip> JXTable data properties
        oscarModel = new OscarTableModel();
        // set the table model after setting the factory
        oscarTable.setModel(oscarModel);
//        </snip>
        
        // <snip> Filter control
        // create the controller
        filterController = new OscarFiltering(oscarTable);
        // bind controller properties to input components
//        BindingGroup filterGroup = new BindingGroup();
//        filterGroup.addBinding(Bindings.createAutoBinding(READ, 
//                winnersCheckbox, BeanProperty.create("selected"),
//                filterController, BeanProperty.create("showOnlyWinners")));
//        filterGroup.addBinding(Bindings.createAutoBinding(READ, 
//                filterField, BeanProperty.create("text"),
//                filterController, BeanProperty.create("filterString")));
// EUG TODO controller winnersCheckbox   
        //public void setFilterString(String filterString) {
  
        // PENDING JW: crude hack to update the statusbar - fake property
        // how-to do cleanly?
//        filterGroup.addBinding(Bindings.createAutoBinding(READ, 
//                filterController, BeanProperty.create("showOnlyWinners"),
//                this, BeanProperty.create("statusContent")));
//        filterGroup.addBinding(Bindings.createAutoBinding(READ, 
//                filterController, BeanProperty.create("filterString"),
//                this, BeanProperty.create("statusContent")));
//        filterGroup.bind();
// EUG TODO controller filterController
        
//        </snip>
        oscarModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateStatusBar();
            }
        });

        //<snip> JXTable column properties
        // some display properties can be configured only after the model has been set, here:
        // configure the view sequence of columns to be different from the model
        oscarTable.setColumnSequence(new Object[] {"yearColumn", "categoryColumn", "movieTitleColumn", "nomineesColumn"});
        // </snip>
    }

    /**
     * Binding artefact method: 
     * crude hack to update the status bar on state changes from the controller. 
     */
    public void setStatusContent(Object dummy) {
        updateStatusBar();
    }

    /**
     * Updates status labels. Called during loading and on 
     * changes to the filter controller state.
     */
    protected void updateStatusBar() {
        tableStatus.setName(filterController.isFilteringByString() ? "searchCountLabel" : "rowCountLabel");
        //DemoUtils.injectResources(this, tableStatus); // JLabel tableStatus
// macht:
//  Application.getInstance().getContext().getResourceMap(parent.getClass()).injectComponents(child);
// also tableStatus und alle subcomp zu this
        tableRows.setText("" + oscarTable.getRowCount());
    }
    
    /**
     * Callback method for demo loader. 
     */
    public void start() {
        if (oscarModel.getRowCount() != 0) return;
        //<snip>Use SwingWorker to asynchronously load the data
        // create SwingWorker which will load the data on a separate thread
        SwingWorker<?, ?> loader = new OscarDataLoader(
                XTableDemo.class.getResource("resources/oscars.xml"), oscarModel);
        
        // display progress bar while data loads
        progressBar = new JProgressBar();
        statusBarLeft.add(progressBar);
        // bind the worker's progress notification to the progressBar
        // and the worker's state notification to this
//        BindingGroup group = new BindingGroup();
//        group.addBinding(Bindings.createAutoBinding(READ, 
//                loader, BeanProperty.create("progress"),
//                progressBar, BeanProperty.create("value")));
//        group.addBinding(Bindings.createAutoBinding(READ, 
//                loader, BeanProperty.create("state"),
//                this, BeanProperty.create("loadState")));
//        group.bind();
        // TODO
        loader.execute();
//        </snip>
    }

    /**
     * Callback for worker's state notification: cleanup if done.
     * @param state
     */
    public void setLoadState(StateValue state) {
        //<snip>Use SwingWorker to asynchronously load the data
        // remove progressbar if done loading
        if (state != StateValue.DONE) return;
        statusBarLeft.remove(progressBar);
        statusBarLeft.remove(actionStatus);
        revalidate();
        repaint();
//        </snip>
    }
    
    //<snip>Use SwingWorker to asynchronously load the data specialized on OscarCandidate
/*
 * @param <T> the result type returned by this {@code SwingWorker's}
 *        {@code doInBackground} and {@code get} methods
 * @param <V> the type used for carrying out intermediate results by this
 *        {@code SwingWorker's} {@code publish} and {@code process} methods
 */
    private class OscarDataLoader extends SwingWorker<List<OscarCandidate>, OscarCandidate> {
    	
        private final URL oscarData;
        private final OscarTableModel oscarModel;
        private final List<OscarCandidate> candidates = new ArrayList<OscarCandidate>();
//        </snip>
        
        private JLabel credits;
        
        // ctor:
        private OscarDataLoader(URL oscarURL, OscarTableModel oscarTableModel) {
            this.oscarData = oscarURL;
            this.oscarModel = oscarTableModel;
        }

        //<snip>Use SwingWorker to asynchronously load the data
        // background task let a parser do its stuff and update a progress bar
        @Override
        public List<OscarCandidate> doInBackground() {
            OscarDataParser parser = new OscarDataParser() {
                @Override
                protected void addCandidate(OscarCandidate candidate) {
                    candidates.add(candidate);
                    if (candidates.size() % 3 == 0) {
                        try { // slow it down so we can see progress :-)
                            Thread.sleep(1);
                        } catch (Exception ex) {
                        }
                    }
                    publish(candidate);
                    setProgress(100 * candidates.size() / 8545);
                }
            };
            parser.parseDocument(oscarData);
            return candidates;
        }
//        </snip>

        @Override
        protected void process(List<OscarCandidate> moreCandidates) {
            if (credits == null) {
                showCredits();
            }
            oscarModel.add(moreCandidates);
        }

        // For older Java 6 on OS X
        @SuppressWarnings("unused")
        protected void process(OscarCandidate... moreCandidates) {
            for (OscarCandidate candidate : moreCandidates) {
                oscarModel.add(candidate);
            }
        }
        
        //<snip>Use SwingWorker to asynchronously load the data
        // show a transparent overlay on start loading
        private void showCredits() {
            credits = new JLabel(); 
            credits.setName("credits");
            credits.setFont(UIManager.getFont("Table.font").deriveFont(24f));
            credits.setHorizontalAlignment(JLabel.CENTER);
            credits.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(20,20,20,20)));

            dataPanel.showMessageLayer(credits, .75f);
//            DemoUtils.injectResources(XTableDemo.this, dataPanel);
        }
//        </snip>
        
        @Override
        //<snip>Use SwingWorker to asynchronously load the data
        // hide transparend overlay on end loading
        protected void done() {
            setProgress(100);
            dataPanel.hideMessageLayer();
        }
//        </snip>
    }
    
//------------------ init ui    
    //<snip> JXTable display properties
    // center column header text
    private JXTable createXTable() {
        JXTable table = new JXTable() {

            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JXTableHeader(columnModel) {

                    @Override
                    public void updateUI() {
                        super.updateUI();
                        // need to do in updateUI to survive toggling of LAF
                        if (getDefaultRenderer() instanceof JLabel) {
                            ((JLabel) getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
                            
                        }
                    }
//                    </snip>
                    
                };
            }
            
        };
        return table;
    }
   
//    protected void initComponents() {
//        setLayout(new BorderLayout());
//
//        controlPanel = createControlPanel();
//        add(controlPanel, BorderLayout.NORTH);
//        oscarTable = createXTable();
//        oscarTable.setName("oscarTable");
//        
//        JScrollPane scrollpane = new JScrollPane(oscarTable);
//        dataPanel = new Stacker(scrollpane);
//        add(dataPanel, BorderLayout.CENTER);
//
//        add(createStatusBar(), BorderLayout.SOUTH);
//    }

//    protected JPanel createControlPanel() {
//        JPanel controlPanel = new JPanel();
//        GridBagLayout gridbag = new GridBagLayout();
//        GridBagConstraints c = new GridBagConstraints();
//        controlPanel.setLayout(gridbag);
//
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridheight = 1;
//        c.insets = new Insets(20, 10, 0, 10);
//        c.anchor = GridBagConstraints.SOUTHWEST;
//        JLabel searchLabel = new JLabel();
//        searchLabel.setName("searchLabel");
//        controlPanel.add(searchLabel, c);
//
//        c.gridx = 0;
//        c.gridy = 2;
//        c.weightx = 1.0;
//        c.insets.top = 0;
//        c.insets.bottom = 12;
//        c.anchor = GridBagConstraints.SOUTHWEST;
//        //c.fill = GridBagConstraints.HORIZONTAL;
//        filterField = new JTextField(24);
//        controlPanel.add(filterField, c);
//
//        c.gridx = 1;
//        c.gridy = 2;
//        c.gridwidth = GridBagConstraints.REMAINDER;
//        //c.insets.right = 24;
//        //c.insets.left = 12;
//        c.weightx = 0.0;
//        c.anchor = GridBagConstraints.EAST;
//        c.fill = GridBagConstraints.NONE;
//        winnersCheckbox = new JCheckBox();
//        winnersCheckbox.setName("winnersLabel");
//        controlPanel.add(winnersCheckbox, c);
//
//        return controlPanel;
//    }

    protected Container createStatusBar() {

        JXStatusBar statusBar = new JXStatusBar();
        statusBar.putClientProperty("auto-add-separator", Boolean.FALSE);
        // Left status area
        statusBar.add(Box.createRigidArea(new Dimension(10, 22)));
        statusBarLeft = Box.createHorizontalBox();
        statusBar.add(statusBarLeft, JXStatusBar.Constraint.ResizeBehavior.FILL);
        actionStatus = new JLabel();
        actionStatus.setName("loadingStatusLabel");
        actionStatus.setHorizontalAlignment(JLabel.LEADING);
        statusBarLeft.add(actionStatus);

        // Middle (should stretch)
//        statusBar.add(Box.createHorizontalGlue());
//        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(Box.createVerticalGlue());
        statusBar.add(Box.createRigidArea(new Dimension(50, 0)));

        // Right status area
        tableStatus = new JLabel(); 
        tableStatus.setName("rowCountLabel");
        JComponent bar = Box.createHorizontalBox();
        bar.add(tableStatus);
        tableRows = new JLabel("0");
        bar.add(tableRows);
        
        statusBar.add(bar);
        statusBar.add(Box.createHorizontalStrut(12));
        return statusBar;
    }

//    public static void main(String args[]) {
//
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JXFrame frame = new JXFrame("JXTable Demo", true);
//                XTableDemo demo = new XTableDemo();
//                frame.add(demo);
//                frame.setSize(700, 400);
//                frame.setVisible(true);
//                demo.start();
//            }
//        });
//    }
    
//-----do nothing methods (keep beansbinding happy)
    
    public Object getStatusContent() {
        return null;
    }
    
    public StateValue getLoadState() {
        return null;
    }

}