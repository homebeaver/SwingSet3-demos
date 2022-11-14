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
import javax.swing.table.JTableHeader;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTable}. This demo displays the same functionality as
 * {@link swingset.TableDemo TableDemo}, using SwingX components and methodologies.
 * <p>
 * It is not possible to extend {@code TableDemo}, since the display components are private. 
 * This class replicates contents and behavior in that class and may fall out of sync.
 * 
 * @author Karl George Schaefer
 * @author Jeanette Winzenberg (Devoxx '08 version)
 * @author aim (original TableDemo)
 * @author EUG https://github.com/homebeaver (reorg+simplify)
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
public class XTableDemo extends AbstractDemo {

	private static final long serialVersionUID = -2616149327587528185L;
	static final Logger LOG = Logger.getLogger(XTableDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTable, an enhanced data grid display.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			// no controller
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			AbstractDemo demo = new XTableDemo(frame);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
    	});
    }


    private OscarTableModel oscarModel;

    // Controller/NORTH:
    private JXPanel controlPanel;
    private JTextField filterField;
    private JCheckBox winnersCheckbox;
    private OscarFiltering filterController;

    private Stacker dataPanel; // com.sun.swingset3.demos.Stacker extends JLayeredPane mit timing.Animator
    private JXTable oscarTable;
    
    // SOUTH:
    private JComponent statusBarLeft;
    private JLabel actionStatus;
    private JLabel tableStatus;
    private JLabel tableRows;
    private JProgressBar progressBar;

    /**
     * XTableDemo Constructor
     * 
     * @param frame controller Frame
     */
    public XTableDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	// initComponents:
        add(getControlPanel(), BorderLayout.NORTH);
    	
        oscarTable = createXTable();
        oscarTable.setName("oscarTable");
        
        JScrollPane scrollpane = new JScrollPane(oscarTable);
        dataPanel = new Stacker(scrollpane);
        add(dataPanel, BorderLayout.CENTER);

        add(createStatusBar(), BorderLayout.SOUTH); // Alternativ JXStatusBar im frame

        configureDisplayProperties();
        
        // Filter control create the controller
        filterController = new OscarFiltering(oscarTable);
        
        //<snip> JXTable data properties
        oscarModel = new OscarTableModel();
        // set the table model after setting the factory
        oscarTable.setModel(oscarModel);
        //</snip>
        oscarModel.addTableModelListener( tableModelEvent -> {
        	updateStatusBar();
        });

        //<snip> JXTable column properties
        // some display properties can be configured only after the model has been set, 
        // here: configure the view sequence of columns to be different from the model
        oscarTable.setColumnSequence(new Object[] 
        		{ "yearColumn"
        		, "categoryColumn"
        		, "movieTitleColumn"
        		, "nomineesColumn"
        		});
        //</snip>
        
        start();
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}
    
	private JXPanel getControlPanel() {
        GridBagLayout gridbag = new GridBagLayout();
    	controlPanel = new JXPanel(gridbag);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 1;
        c.insets = new Insets(20, 10, 0, 10);
        c.anchor = GridBagConstraints.SOUTHWEST;
        JLabel searchLabel = new JLabel();
        searchLabel.setName("searchLabel");
        searchLabel.setText(getBundleString("searchLabel.text"));
        controlPanel.add(searchLabel, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.insets.top = 0;
        c.insets.bottom = 12;
        c.anchor = GridBagConstraints.SOUTHWEST;
        //c.fill = GridBagConstraints.HORIZONTAL;
        
        filterField = new JTextField(24);
        filterField.addActionListener( event -> {
            filterController.setFilterString(event.getActionCommand());
            updateStatusBar();
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
        winnersCheckbox.setText(getBundleString("winnersLabel.text"));        
        winnersCheckbox.addActionListener( event -> {
            boolean showOnlyWinners = ((JCheckBox)event.getSource()).isSelected();
            filterController.setShowOnlyWinners(showOnlyWinners);
            updateStatusBar();
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
        CustomColumnFactory factory = new CustomColumnFactory() {
        	// titles from props
        	protected void configureTitle(TableColumnExt columnExt) {
        		columnExt.setTitle(getBundleString(OscarTableModel.columnIds[columnExt.getModelIndex()]));
        	}
        };
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
     * Updates status labels. 
     * Called during loading and on changes to the filter controller state.
     */
    protected void updateStatusBar() {
    	if(filterController.isFilteringByString()) {
    		tableStatus.setName("searchCountLabel");
    		tableStatus.setText(getBundleString("searchCountLabel.text"));
    	} else {
    		tableStatus.setName("rowCountLabel");
    		tableStatus.setText(getBundleString("rowCountLabel.text"));
    	}
        tableRows.setText("" + oscarTable.getRowCount());
    }
    
    /**
     * Callback method for demo loader. 
     */
    public void start() {
        if (oscarModel.getRowCount() != 0) return;
        //<snip>Use SwingWorker to asynchronously load the data
        // create SwingWorker which will load the data on a separate thread
        SwingWorker<?, ?> loader = new OscarDataLoader(XTableDemo.class.getResource("resources/oscars.xml"), oscarModel);
        
        // ohne bind:
        loader.addPropertyChangeListener( propertyChangeEvent -> {
        	if ("state".equals(propertyChangeEvent.getPropertyName())) {
        		StateValue state = (StateValue)propertyChangeEvent.getNewValue();
                LOG.info("loader StateValue:" + state); // damit ampel steuern
                updateStatusBar();
                if (state == StateValue.DONE) {
                	statusBarLeft.remove(progressBar);
                	statusBarLeft.remove(actionStatus);
                	revalidate();
                	repaint();
                }
        	}
        	if ("progress".equals(propertyChangeEvent.getPropertyName())) {
        		int progress = (Integer)propertyChangeEvent.getNewValue();
                //LOG.info("loader progress:" + progress);
        		progressBar.setValue(progress);
        		updateStatusBar();
        	}
        });
        
        // alternativly / needs artifact/org.jdesktop/beansbinding
        // bind the worker's progress notification to the progressBar
        // and the worker's state notification to this
//        BindingGroup group = new BindingGroup();
//        group.addBinding(Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ
//        		, loader, BeanProperty.create("progress")
//        		, progressBar, BeanProperty.create("value")
//        		));
//        group.addBinding(Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ
//        		, loader, BeanProperty.create("state")
//        		, this, BeanProperty.create("loadState")
//        		));
//        group.bind();
        StateValue state = loader.getState(); // PENDING - STARTED - DONE
        LOG.info("loader StateValue:" + state);
        loader.execute();
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
        private final int expectedNumber = 8540;
        
        // ctor:
        private OscarDataLoader(URL oscarURL, OscarTableModel oscarTableModel) {
            this.oscarData = oscarURL;
            this.oscarModel = oscarTableModel;
        }

/* nach dem Laden: swingset3 8522/8545 (BUG dort) , hier: 8540
 * showOnlyWinners: beide 1758
 */
        //<snip>Use SwingWorker to asynchronously load the data
        // background task let a parser do its stuff and update a progress bar
        @Override
        public List<OscarCandidate> doInBackground() {
            OscarDataParser parser = new OscarDataParser() {
                @Override // implement abstract addCandidate
                protected void addCandidate(OscarCandidate candidate) {
                    candidates.add(candidate);
                    if (candidates.size() % 3 == 0) {
                        try { // slow it down so we can see progress :-)
                            Thread.sleep(1);
                        } catch (Exception ex) {
                        }
                    }
                    publish(candidate);
                    setProgress(100 * candidates.size() / expectedNumber);
                }
            };
            parser.parseDocument(oscarData);
            
            if(super.isCancelled()) {
    			LOG.warning("cancelled "+candidates.size()+"/"+expectedNumber + " "+100 * candidates.size() / expectedNumber + "%");
    			super.firePropertyChange("cancelled", false, true);
            } else {
    			LOG.info("DONE "+candidates.size()+"/"+expectedNumber + " "+100 * candidates.size() / expectedNumber + "%");
            }
            return candidates;
        }
//        </snip>

        /**
         * Receives data chunks asynchronously on the EDT
         * 
         * @param chunks intermediate results to process
         */
		@Override
		protected void process(List<OscarCandidate> moreCandidates) {
			//LOG.info("chunks#:"+moreCandidates.size());
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
            credits.setText(getBundleString("credits.text"));
            credits.setFont(UIManager.getFont("Table.font").deriveFont(24f));
            credits.setHorizontalAlignment(JLabel.CENTER);
            credits.setBorder(new CompoundBorder(new TitledBorder(""), new EmptyBorder(20,20,20,20)));

            dataPanel.showMessageLayer(credits, .75f);
//            DemoUtils.injectResources(XTableDemo.this, dataPanel); // wahscheinlich macht es credits.setText(...
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
   
    protected Container createStatusBar() {

        JXStatusBar statusBar = new JXStatusBar();
        statusBar.putClientProperty("auto-add-separator", Boolean.FALSE);
        // Left status area
        statusBar.add(Box.createRigidArea(new Dimension(10, 22)));
        statusBarLeft = Box.createHorizontalBox();
        statusBar.add(statusBarLeft, JXStatusBar.Constraint.ResizeBehavior.FILL);
        actionStatus = new JLabel();
        actionStatus.setName("loadingStatusLabel");
        actionStatus.setText(getBundleString("loadingStatusLabel.text"));
        actionStatus.setHorizontalAlignment(JLabel.LEADING);
        statusBarLeft.add(actionStatus);
        // display progress bar while data loads
        progressBar = new JProgressBar();
        statusBarLeft.add(progressBar);   

        // Middle (should stretch)
//        statusBar.add(Box.createHorizontalGlue());
//        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(Box.createVerticalGlue());
        statusBar.add(Box.createRigidArea(new Dimension(50, 0)));

        // Right status area
        tableStatus = new JLabel(); 
        tableStatus.setName("rowCountLabel");
        tableStatus.setText(getBundleString("rowCountLabel.text"));
        JComponent bar = Box.createHorizontalBox();
        bar.add(tableStatus);
        tableRows = new JLabel("0");
        bar.add(tableRows);
        
        statusBar.add(bar);
        statusBar.add(Box.createHorizontalStrut(12));
        return statusBar;
    }

}