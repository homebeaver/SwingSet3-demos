/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.monthviewext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.binding.DisplayInfoConverter;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.demos.monthviewext.MonthViewExtDemoUtils.DayOfWeekHighlightPredicate;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.plaf.basic.CalendarHeaderHandler;
import org.jdesktop.swingx.plaf.basic.CalendarRenderingHandler;
import org.jdesktop.swingx.plaf.basic.DemoCalendarRenderingHandler;
import org.jdesktop.swingx.plaf.basic.DemoMonthViewUI;
import org.jdesktop.swingx.plaf.basic.SpinningCalendarHeaderHandler;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jhlabs.image.OpacityFilter;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXMonthView}.
 *
 * @author Karl George Schaefer
 * @author Joshua Outwater (original JXMonthViewDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXMonthView (extended)",
//    category = "Controls",
//    description = "Demonstrates extended JXMonthView features (not yet public)",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/monthviewext/MonthViewExtDemo.java",
//        "org/jdesktop/swingx/plaf/basic/DemoMonthViewUI.java",
//        "org/jdesktop/swingx/plaf/basic/DemoCalendarRenderingHandler.java",
//        "org/jdesktop/swingx/demos/monthviewext/MonthViewExtDemoUtils.java",
//        "org/jdesktop/swingx/demos/monthviewext/resources/MonthViewExtDemo.properties"
//    }
//)
//@SuppressWarnings("serial")
public class MonthViewExtDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -7354019100269447099L;
	private static final Logger LOG = Logger.getLogger(MonthViewExtDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates extended JXMonthView features";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new MonthViewExtDemo(controller);
			JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
			frame.setStartPosition(StartPosition.CenterInScreen);
			//frame.setLocationRelativeTo(controller);
        	frame.getContentPane().add(demo);
        	frame.pack();
        	frame.setVisible(true);
			
			controller.getContentPane().add(demo.getControlPane());
			controller.pack();
			controller.setVisible(true);
    	});
    }
    
    private JXMonthView monthView;

    private MonthViewDemoControl monthViewDemoControl;

    private JComponent extended;  // JXCollapsiblePane
    private JXFrame calendarFrame;
    private JCheckBox calendarBox;
    private JCheckBox zoomableBox;

    private JComboBox customHeaderBox;

    /**
     * MonthViewExtDemo Constructor
     * 
     * @param frame controller Frame
     */
    public MonthViewExtDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        // <snip> Custom MonthViewUI
        // register a custom monthView ui which provides support for custom
        // CalendarRenderingHandler and CalendarHeaderHandler
        UIManager.put(JXMonthView.uiClassID, "org.jdesktop.swingx.plaf.basic.DemoMonthViewUI");
        // </snip>
        
        createMonthViewDemo();
//        bind():      
        monthViewDemoControl = new MonthViewDemoControl();
    }

    //--------------------- create ui: current month
    private void createMonthViewDemo() {
        monthView = new JXMonthView(){

            @Override
            public void setZoomable(boolean zoomable) {
                super.setZoomable(zoomable);
                ((JComponent) getParent()).revalidate();
            }
            
        };
        monthView.setName("monthView");
        monthView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel monthViewContainer = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "5dlu, c:d:n, l:4dlu:n, f:d:n", // columns
                "c:d:n " +
                ", t:4dlu:n, t:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, monthViewContainer);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("monthViewSeparator");
        areaSeparator.setTitle(getBundleString("monthViewSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        builder.add(monthView, cc.xywh(2, 3, 1, 1));
                
        add(monthViewContainer, BorderLayout.EAST);
        
        // Controller:
        extended = createExtendedConfigPanel(); // JXCollapsiblePane
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel monthViewControlPanel = new JXPanel();
        monthViewControlPanel.add(extended);
        return monthViewControlPanel;
	}
  
    private JComponent createExtendedConfigPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:n, l:4dlu:n, f:d:n", // columns
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("extendedSeparator");
        areaSeparator.setTitle(getBundleString("extendedSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        
        calendarBox = new JCheckBox();
        calendarBox.setName("calendarBox");
        // öffnet Kalender für Jahr 2022 im separatem Frame
        calendarBox.setText(getBundleString("calendarBox.text"));
        builder.add(calendarBox, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;
        
        zoomableBox = new JCheckBox();
        zoomableBox.setName("zoomableBox");
        zoomableBox.setText(getBundleString("zoomableBox.text"));
        builder.add(zoomableBox, cc.xywh(labelColumn, currentRow, 3, 1));
        currentRow += 2;

        customHeaderBox = new JComboBox();
        customHeaderBox.setName("customHeaderBox");
        JLabel headerBoxLabel = builder.addLabel(
                "", cl.xywh(labelColumn, currentRow, 1, 1),
                customHeaderBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        headerBoxLabel.setName("customHeaderBoxLabel");
        headerBoxLabel.setText(getBundleString("customHeaderBoxLabel.text"));
        LabelHandler.bindLabelFor(headerBoxLabel, customHeaderBox);
        currentRow += 2;
        
        return painterControl;
    }
    

    private void hideCalendarFrame() {
        if (calendarFrame != null) {
// TODO           DemoUtils.fadeOutAndDispose(calendarFrame, 1000);
            LOG.warning("TODO           DemoUtils.fadeOutAndDispose(calendarFrame, 1000); use dispose()");
            calendarFrame.dispose();
        }
        calendarFrame = null;
        monthViewDemoControl.setCalendarVisible(false);
    }
    private void showCalendarFrame() {
        calendarFrame = new JXFrame("Calendar 2022");
        calendarFrame.setName("calendar2022");
        calendarFrame.setDefaultCloseOperation(JXFrame.DO_NOTHING_ON_CLOSE);
        
        WindowListener l = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                calendarFrame.removeWindowListener(this);
                hideCalendarFrame();
            }
            
        };
        calendarFrame.addWindowListener(l);
        
        JXPanel calendar = new JXPanel();
        calendar.setBackground(Color.WHITE);
        Painter<Component> painter = createBackgroundPainter();
        calendar.setBackgroundPainter(painter);

        JXMonthView monthView = new JXMonthView();
        Calendar cal = monthView.getCalendar();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        monthView.setFirstDisplayedDay(cal.getTime());
        monthView.setOpaque(false);
        monthView.setPreferredColumnCount(3);
        monthView.setPreferredRowCount(4);
        // old style: set visual property with JXMonthView api
        monthView.setDayForeground(Calendar.SUNDAY, Color.MAGENTA);
        
        // <snip> Custom CalendarRenderingHandler
        // new style: install a custom renderingHandler 
        // (as client property, because no public api support yet) which allows to add Highlighters
        monthView.putClientProperty(DemoMonthViewUI.RENDERING_HANDLER_KEY, createRenderingHandler());
        // </snip>
        
//        DemoUtils.setSnippet("Custom CalendarRenderingHandler", monthView);
        calendar.add(monthView);
        calendarFrame.add(calendar);
        calendarFrame.pack();
        calendarFrame.setLocationRelativeTo(this);
        calendarFrame.setVisible(true);
    }

    /**
     * creates an ImagePainter aka BackgroundPainter which implements Painter<T>
     * 
     * @return ImagePainter object that implements Painter<T>
     */
    private Painter<Component> createBackgroundPainter() {
        ImagePainter painter = null;
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("resources/images/demo_duke.png"));
            /*             abstract class AbstractBean
             *             abstract class AbstractPainter<T> extends AbstractBean implements Painter<T>
             *             abstract class AbstractLayoutPainter<T> extends AbstractPainter<T>
             *                                  |
             *                                  +------------------------------|
             *             abstract class AbstractAreaPainter<T> extends AbstractLayoutPainter<T>
             *                                  |
             * class ImagePainter extends AbstractAreaPainter<Component>
             */
            painter = new ImagePainter(img);
            painter.setFilters(new OpacityFilter(10));
            painter.setHorizontalRepeat(true);
            painter.setVerticalRepeat(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return painter;
    }

    /**
     * Creates and returns a RenderingHandler which supports adding Highlighters.
     *  
     * @return CalendarRenderingHandler
     */
    private CalendarRenderingHandler createRenderingHandler() {
        // <snip> Custom CalendarRenderingHandler
        // use a RenderingHandler which supports adding Highlighters.
        DemoCalendarRenderingHandler handler = new DemoCalendarRenderingHandler();
        
        // new style: use highlighter for color config
        handler.addHighlighters(
        		new ColorHighlighter(new DayOfWeekHighlightPredicate(Calendar.SATURDAY), null, Color.BLUE)
        		);
        // highlight property is setting opacity to true
        Highlighter transparent = new AbstractHighlighter(MonthViewExtDemoUtils.SELECTED) {
            
            @Override
            public Component highlight(Component component, ComponentAdapter adapter) {
                // opacity is not one of the properties which are
                // guaranteed to be reset, so we have to do it here
                ((JComponent) component).setOpaque(adapter.getComponent().isOpaque());
                // call super to apply the highight - which is to
                // set the component's opacity to true
                return super.highlight(component, adapter);
            }
            
            @Override
            protected Component doHighlight(Component component, ComponentAdapter adapter) {
                ((JComponent) component).setOpaque(true);
                return component;
            }
            
        };
        handler.addHighlighters(transparent);
        // </snip>
        return handler;
    }

    private ComboBoxModel createHeaderInfos() {
        // <snip> Custom CalendarHeaderHandler
        // create combo model containing handlers to choose
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(new DisplayInfo<CalendarHeaderHandler>("base (does nothing)", null));
        model.addElement(
        		new DisplayInfo<CalendarHeaderHandler>("default", new SpinningCalendarHeaderHandler())
        		);
        model.addElement(
        		new DisplayInfo<CalendarHeaderHandler>("default (customized)", new DemoCalendarHeaderHandler(true, true))
        		);
        // </snip>
        return model;
        
    }
    
    //--------------------- MonthViewDemoControl
    // must not be private! 
    // private leads to: java.lang.IllegalAccessException: 
    // class org.jdesktop.beansbinding.BeanProperty cannot access a member of class org.jdesktop.swingx.demos.monthviewext.MonthViewExtDemo$MonthViewDemoControl with modifiers "public"
    public class MonthViewDemoControl extends AbstractBean {
        
        private boolean calendarVisible;
        
        @SuppressWarnings("unchecked")
        public MonthViewDemoControl() {
//            DemoUtils.setSnippet("Custom CalendarRenderingHandler", calendarBox);
            
            // <snip> Custom CalendarHeaderHandler
            // configure the comboBox
            customHeaderBox.setModel(createHeaderInfos());
            customHeaderBox.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
            // </snip>
            
//            DemoUtils.setSnippet("Custom CalendarHeaderHandler", customHeaderBox, zoomableBox);
            
            BindingGroup group = new BindingGroup();
            
            // zeigt Kalender für Jahr 2022 im separatem Frame
            group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, 
                    calendarBox, BeanProperty.create("selected"),
                    this, BeanProperty.create("calendarVisible")));
            
            group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                    zoomableBox, BeanProperty.create("selected"),
                    monthView, BeanProperty.create("zoomable")));
            
            // <snip> Custom CalendarHeaderHandler
            // bind the combo box
            group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                    monthView, BeanProperty.create("zoomable"),
                    customHeaderBox, BeanProperty.create("enabled")
                    ));
            Binding handlerBinding = Bindings.createAutoBinding(UpdateStrategy.READ,
                    customHeaderBox, BeanProperty.create("selectedItem"),
                    this, BeanProperty.create("calendarHeaderHandler"));
            handlerBinding.setConverter(new DisplayInfoConverter<CalendarHeaderHandler>());
            // </snip>
            group.addBinding(handlerBinding);
            group.bind();
        }
        
        /**
         * @param handler the handler to set
         */
        // <snip> Custom CalendarHeaderHandler
        // wrapper around not yet public api: property on control for binding
        public void setCalendarHeaderHandler(CalendarHeaderHandler handler) {
            Object old = getCalendarHeaderHandler();
            monthView.putClientProperty(CalendarHeaderHandler.uiControllerID, handler);
            firePropertyChange("calendarHeaderHandler", old, getCalendarHeaderHandler());
        }
        // </snip>

        /**
         * @return the handler
         */
        public CalendarHeaderHandler getCalendarHeaderHandler() {
            return (CalendarHeaderHandler)monthView.getClientProperty(CalendarHeaderHandler.uiControllerID);
        }

        /**
         * @param calendarVisible the calendarVisible to set
         */
        /*
         * updateCalendar : ja nach private boolean calendarVisible
         * wird 2022 Kalender-frame gezeigt/show oder versteckt/hide
         */
        public void setCalendarVisible(boolean calendarVisible) {
            boolean old = isCalendarVisible();
            if (old == calendarVisible) return;
            this.calendarVisible = calendarVisible;
            updateCalendar();
            firePropertyChange("calendarVisible", old, isCalendarVisible());
        }
        
        /**
         * @return the calendarVisible
         */
        public boolean isCalendarVisible() {
            return calendarVisible;
        }
        
        private void updateCalendar() {
            if (isCalendarVisible()) {
                showCalendarFrame();
            } else {
                hideCalendarFrame();
            }
        }
        
    }

}