/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.monthview;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.util.Contract;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXMonthView}.
 *
 * @author Karl George Schaefer
 * @author Joshua Outwater (original JXMonthViewDemoPanel)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "JXMonthView (basic)",
//    category = "Controls",
//    description = "Demonstrates JXMonthView, a monthly calendar display.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/monthview/MonthViewDemo.java",
//        "org/jdesktop/swingx/demos/monthview/MonthViewDemoUtils.java",
//        "org/jdesktop/swingx/demos/monthview/resources/MonthViewDemo.properties",
//        "org/jdesktop/swingx/demos/monthview/resources/MonthViewDemo.html",
//        "org/jdesktop/swingx/demos/monthview/resources/images/MonthViewDemo.png"
//    }
//)
//@SuppressWarnings("serial")
public class MonthViewDemo extends AbstractDemo {
    
	private static final long serialVersionUID = -5961902554362481875L;
	private static final Logger LOG = Logger.getLogger(MonthViewDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXMonthView, a monthly calendar display.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new MonthViewDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
				//frame.setLocationRelativeTo(controller);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }
    

    private JXMonthView monthView;

    private JComboBox<SelectionMode> selectionModes;
    private JCheckBox traversable;
    private JComboBox<Date> dayOfWeekComboBox;

    private JCheckBox leadingDaysBox;
    private JCheckBox trailingDaysBox;
    private JSpinner prefColumnSlider;
    private JCheckBox weekNumberBox;
    private JSpinner prefRowSlider;
    private JXDatePicker flaggedDates;

    private JXDatePicker unselectableDates;

    private JXDatePicker upperBound;

    private JXDatePicker lowerBound;
    
    /**
     * MonthViewDemo Constructor
     * 
     * @param frame controller Frame
     */
    public MonthViewDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        monthView = new JXMonthView();
        monthView.setName("monthView");
        
        // add to container which doesn't grow the size beyond the pref
        JComponent monthViewContainer = new JXPanel();
        monthViewContainer.add(monthView);
        add(monthViewContainer, BorderLayout.CENTER);       
    }

    /**
     * the controller contains two pane's in jgoodies layout,
     * 	a BoxPropertiesPanel and
     *  a ConfigPanel
     */
    /* some notes on jgoodies layout and builder (used in create*Panel):
     * dlu   == dialog units
     * f:m:g == FILL:MIN:GROW == [columnAlignment:] componentSize [:resizeBehavior]
     * r:d:n == RIGHT:DEFAULT:NONE == [columnAlignment:] size [:resizeBehavior]
     * f:d:n == FILL:DEFAULT:NONE == [columnAlignment:] size [:resizeBehavior]
     * t:d:n == TOP:DEFAULT:NONE == rowSpec
     * c:d:n == CENTER:DEFAULT:NONE == rowSpec
     * @see jgoodies-forms whitepaper.pdf
     */
    @Override
	public JXPanel getControlPane() {
    	JXPanel monthViewControlPanel = new JXPanel();

		// jgoodies layout and builder:
		FormLayout formLayout = new FormLayout(
				"f:m:g, l:4dlu:n, f:m:g", // encodedColumnSpecs 2cols + gap
				"t:d:n "                  // encodedRowSpecs: one row
		);
		PanelBuilder builder = new PanelBuilder(formLayout, monthViewControlPanel);
		builder.setBorder(Borders.DLU4_BORDER);
		CellConstraints cc = new CellConstraints();

		builder.add(createBoxPropertiesPanel(), cc.xywh(1, 1, 1, 1));
		builder.add(createConfigPanel(), cc.xywh(3, 1, 1, 1));
        
        LOG.config("create and bind MonthViewDemoControl");
        new MonthViewDemoControl();
        
        // PENDING JW: re-visit distribution of binding control ...
        // this is quite arbitrary (time of coding ;-)
        BindingGroup group = new BindingGroup();
        group.addBinding(Bindings.createAutoBinding(READ, 
                leadingDaysBox, BeanProperty.create("selected"),
                monthView, BeanProperty.create("showingLeadingDays")));
        group.addBinding(Bindings.createAutoBinding(READ, 
                trailingDaysBox, BeanProperty.create("selected"),
                monthView, BeanProperty.create("showingTrailingDays")));
        
        group.addBinding(Bindings.createAutoBinding(READ, 
                weekNumberBox, BeanProperty.create("selected"),
                monthView, BeanProperty.create("showingWeekNumber")));

        group.addBinding(Bindings.createAutoBinding(READ, 
                prefColumnSlider, BeanProperty.create("value"),
                monthView, BeanProperty.create("preferredColumnCount")));
        
        group.addBinding(Bindings.createAutoBinding(READ, 
                prefRowSlider, BeanProperty.create("value"),
                monthView, BeanProperty.create("preferredRowCount")));
        
        group.bind();

        return monthViewControlPanel;
	}

    /**
     * @return control pane BoxPropertiesPanel
     */
    private JComponent createBoxPropertiesPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        FormLayout formLayout = new FormLayout(
            "5dlu, r:d:n, l:4dlu:n, f:d:n, l:4dlu:n, f:d:n", // encodedColumnSpecs 3 cols + 2 gaps
            "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n" // 6 rows
        );
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();

        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("monthBoxSeparator");
        areaSeparator.setTitle(getBundleString("monthBoxSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 6, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        prefColumnSlider = createSpinner(1, 3, 2);
        prefColumnSlider.setName("preferredColumnSlider");
        
        prefRowSlider = createSpinner(1, 2, 1);
        prefRowSlider.setName("preferredRowSlider");
        
        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), prefRowSlider,
                cc.xywh(widgetColumn, currentRow, 1, 1));
        insets.setName("preferredColumnLabel");
        insets.setText(getBundleString("preferredColumnLabel.text"));
        LabelHandler.bindLabelFor(insets, prefRowSlider);
        builder.add(prefColumnSlider, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;
        
        leadingDaysBox = new JCheckBox();
        leadingDaysBox.setName("leadingDaysBox");
        leadingDaysBox.setText(getBundleString("leadingDaysBox.text"));
        
        JLabel leadingLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                leadingDaysBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        leadingLabel.setName("leadingDaysLabel");
        leadingLabel.setText(getBundleString("leadingDaysLabel.text"));
        LabelHandler.bindLabelFor(leadingLabel, leadingDaysBox);
        
        trailingDaysBox = new JCheckBox();
        trailingDaysBox.setName("trailingDaysBox");
        trailingDaysBox.setText(getBundleString("trailingDaysBox.text"));
        builder.add(trailingDaysBox, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;
      
        weekNumberBox = new JCheckBox();
        weekNumberBox.setName("weekNumberBox");
        weekNumberBox.setText(getBundleString("weekNumberBox.text"));
        builder.add(weekNumberBox, cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;

        traversable = new JCheckBox();
        traversable.setName("traversable"); // traversable.text=Tra&versable
        traversable.setText(getBundleString("traversable.text", traversable));
        builder.add(traversable, cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;

        return painterControl;
    }

    private JSpinner createSpinner(int min, int max, int value) {
        SpinnerModel model = new SpinnerNumberModel(value, min, max, 1);
        JSpinner spinner = new JSpinner(model);
        ((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        return spinner;      
    }

    /**
     * @return control pane ConfigPanel
     */
    private JComponent createConfigPanel() {
        JXCollapsiblePane painterControl = new JXCollapsiblePane();
        // jgoodies layout and builder:
        FormLayout formLayout = new FormLayout(
            "5dlu, r:d:n, l:4dlu:n, f:m:g, l:4dlu:n, f:m:g", // encodedColumnSpecs 3 cols + 2 gaps
            "c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n, t:4dlu:n, c:d:n" // 5 rows
        ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, painterControl);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();

        JXTitledSeparator areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("configurationSeparator");
        areaSeparator.setTitle(getBundleString("configurationSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 6, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;

        dayOfWeekComboBox = new JComboBox<Date>();
        JLabel dayOfWeekLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                dayOfWeekComboBox, cc.xywh(widgetColumn, currentRow, 3, 1));
        dayOfWeekLabel.setName("dayOfWeekLabel");
        dayOfWeekLabel.setText(getBundleString("dayOfWeekLabel.text", dayOfWeekLabel));
        LabelHandler.bindLabelFor(dayOfWeekLabel, dayOfWeekComboBox); // calls dayOfWeekLabel.setLabelFor(dayOfWeekComboBox);
        currentRow += 2;

        selectionModes = new JComboBox<SelectionMode>();
        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                selectionModes, cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;
        insets.setName("selectionModesLabel");
        insets.setText(getBundleString("selectionModesLabel.text", insets));
        LabelHandler.bindLabelFor(insets, selectionModes); // calls insets.setLabelFor(selectionModes);
        
        unselectableDates = new JXDatePicker();
        JLabel unselectables = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                unselectableDates, cc.xywh(widgetColumn, currentRow, 1, 1));
        unselectables.setName("unselectableDatesLabel");
        unselectables.setText(getBundleString("unselectableDatesLabel.text"));
        LabelHandler.bindLabelFor(unselectables, unselectableDates);
        flaggedDates = new JXDatePicker();
        builder.add(flaggedDates, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;

        upperBound = new JXDatePicker();
        lowerBound = new JXDatePicker();
        
        JLabel lower = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), 
                lowerBound, cc.xywh(widgetColumn, currentRow, 1, 1));
        lower.setName("lowerBoundsLabel");
        lower.setText(getBundleString("lowerBoundsLabel.text"));
        LabelHandler.bindLabelFor(lower, lowerBound);
        
        builder.add(upperBound, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;

        return painterControl;
    }


//--------------------- MonthViewDemoControl
    // must not be private! 
    // private leads to: java.lang.IllegalAccessException: 
    // class org.jdesktop.beansbinding.BeanProperty cannot access a member of class org.jdesktop.swingx.demos.monthview.MonthViewDemo$MonthViewDemoControl with modifiers "public"
    public class MonthViewDemoControl extends AbstractBean {
        
        private Date lastFlagged;
        private Date lastUnselectable;
        private Date upper;
        private Date lower;
        
        
        @SuppressWarnings("unchecked")
        public MonthViewDemoControl() {
            selectionModes.setModel(new EnumComboBoxModel<SelectionMode>(SelectionMode.class));
            selectionModes.setRenderer(new DefaultListRenderer(DisplayValues.TITLE_WORDS_UNDERSCORE));

            // PENDING JW: this does not survive a change in Locale - 
            // revisit if we add changing Locale to the demo
            Calendar calendar = monthView.getCalendar();
            // start of week == first day of week in the calendar's coordinate space
            CalendarUtils.startOfWeek(calendar);
            DefaultComboBoxModel<Date> model = new DefaultComboBoxModel<Date>();
            for (int i = 0; i < 7; i++) {
                model.addElement(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
            dayOfWeekComboBox.setModel(model);
            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            dayOfWeekComboBox.setRenderer(new DefaultListRenderer(new FormatStringValue(format)));
            Converter<?, ?> days = new DayOfWeekConverter(calendar);
            
            BindingGroup group = new BindingGroup();
            group.addBinding(Bindings.createAutoBinding(READ, 
                    selectionModes, BeanProperty.create("selectedItem"),
                    monthView, BeanProperty.create("selectionMode")));
            
            group.addBinding(Bindings.createAutoBinding(READ, 
                    traversable, BeanProperty.create("selected"),
                    monthView, BeanProperty.create("traversable")));
            
            Binding dayOfWeek = Bindings.createAutoBinding(READ, 
                    dayOfWeekComboBox, BeanProperty.create("selectedItem"),
                    monthView, BeanProperty.create("firstDayOfWeek"));
            dayOfWeek.setConverter(days);
            group.addBinding(dayOfWeek);
            
            Binding flagged = Bindings.createAutoBinding(READ, 
                    flaggedDates, BeanProperty.create("date"),
                    this, BeanProperty.create("lastFlagged"));
            group.addBinding(flagged);
            
            Binding unselectable = Bindings.createAutoBinding(READ, 
                    unselectableDates, BeanProperty.create("date"),
                    this, BeanProperty.create("lastUnselectable"));
            group.addBinding(unselectable);
            
            group.addBinding(Bindings.createAutoBinding(READ, 
                    upperBound, BeanProperty.create("date"),
                    this, BeanProperty.create("upperBound")));

            group.addBinding(Bindings.createAutoBinding(READ, 
                    lowerBound, BeanProperty.create("date"),
                    this, BeanProperty.create("lowerBound")));
            
            group.bind();
/* TODO remove this log
Aug. 13, 2022 9:12:31 PM org.jdesktop.swingx.JXDatePicker initMonthView
INFO: monthView.SelectionModel/Mode:org.jdesktop.swingx.calendar.DaySelectionModel@5c413ee5/SINGLE_SELECTION
Exception in thread "AWT-EventQueue-0" org.jdesktop.beansbinding.PropertyResolutionException: Exception invoking method public void org.jdesktop.swingx.demos.monthview.MonthViewDemo$MonthViewDemoControl.setLastFlagged(java.util.Date) on org.jdesktop.swingx.demos.monthview.MonthViewDemo$MonthViewDemoControl@1fc99a44
	at org.jdesktop.beansbinding.BeanProperty.invokeMethod(BeanProperty.java:791)
	at org.jdesktop.beansbinding.BeanProperty.write(BeanProperty.java:891)
	at org.jdesktop.beansbinding.BeanProperty.setProperty(BeanProperty.java:909)
	at org.jdesktop.beansbinding.BeanProperty.setValue(BeanProperty.java:580)
	at org.jdesktop.beansbinding.Binding.refreshUnmanaged(Binding.java:1229)
	at org.jdesktop.beansbinding.Binding.refresh(Binding.java:1207)
	at org.jdesktop.beansbinding.Binding.refreshAndNotify(Binding.java:1143)
	at org.jdesktop.beansbinding.AutoBinding.bindImpl(AutoBinding.java:197)
	at org.jdesktop.beansbinding.Binding.bindUnmanaged(Binding.java:959)
	at org.jdesktop.beansbinding.Binding.bind(Binding.java:944)
	at org.jdesktop.beansbinding.BindingGroup.bind(BindingGroup.java:143)
	at org.jdesktop.swingx.demos.monthview.MonthViewDemo$MonthViewDemoControl.<init>(MonthViewDemo.java:412) <===
	at org.jdesktop.swingx.demos.monthview.MonthViewDemo.getControlPane(MonthViewDemo.java:181)
	at io.github.homebeaver.swingset.demo.DemoAction.actionPerformed(DemoAction.java:402)
	at io.github.homebeaver.swingset.demo.DemoJXTasks$1.actionPerformed(DemoJXTasks.java:91)
	at java.desktop/javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:1972)
	at org.jdesktop.swingx.JXHyperlink.fireActionPerformed(JXHyperlink.java:264)
	at java.desktop/javax.swing.AbstractButton$Handler.actionPerformed(AbstractButton.java:2313)
	at java.desktop/javax.swing.DefaultButtonModel.fireActionPerformed(DefaultButtonModel.java:405)
	at java.desktop/javax.swing.DefaultButtonModel.setPressed(DefaultButtonModel.java:262)
	at java.desktop/javax.swing.plaf.basic.BasicButtonListener.mouseReleased(BasicButtonListener.java:279)
	at java.desktop/java.awt.Component.processMouseEvent(Component.java:6617)
	at java.desktop/javax.swing.JComponent.processMouseEvent(JComponent.java:3342)
	at java.desktop/java.awt.Component.processEvent(Component.java:6382)
	at java.desktop/java.awt.Container.processEvent(Container.java:2264)
	at java.desktop/java.awt.Component.dispatchEventImpl(Component.java:4993)
	at java.desktop/java.awt.Container.dispatchEventImpl(Container.java:2322)
	at java.desktop/java.awt.Component.dispatchEvent(Component.java:4825)
	at java.desktop/java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4934)
	at java.desktop/java.awt.LightweightDispatcher.processMouseEvent(Container.java:4563)
	at java.desktop/java.awt.LightweightDispatcher.dispatchEvent(Container.java:4504)
	at java.desktop/java.awt.Container.dispatchEventImpl(Container.java:2308)
	at java.desktop/java.awt.Window.dispatchEventImpl(Window.java:2773)
	at java.desktop/java.awt.Component.dispatchEvent(Component.java:4825)
	at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:772)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:721)
	at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:715)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:391)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:85)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:95)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:745)
	at java.desktop/java.awt.EventQueue$5.run(EventQueue.java:743)
	at java.base/java.security.AccessController.doPrivileged(AccessController.java:391)
	at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:85)
	at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:742)
	at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:124)
	at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:113)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:109)
	at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
	at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:90)
Caused by: java.lang.IllegalAccessException: class org.jdesktop.beansbinding.BeanProperty cannot access a member of class org.jdesktop.swingx.demos.monthview.MonthViewDemo$MonthViewDemoControl with modifiers "public"
	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:385)
	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:687)
	at java.base/java.lang.reflect.Method.invoke(Method.java:559)
	at org.jdesktop.beansbinding.BeanProperty.invokeMethod(BeanProperty.java:782)
	... 50 more
           
 */
            // PENDING JW: removed the color selection stuff for now
            // future will be to use highlighters anyway - revisit then
        }
        /**
         * @return the lastFlagged
         */
        public Date getLastFlagged() {
            return lastFlagged;
        }
        /**
         * @param lastFlagged the lastFlagged to set
         */
        public void setLastFlagged(Date lastFlagged) {
            Date old = getLastFlagged();
            this.lastFlagged = lastFlagged;
            updateFlaggedDates();
            firePropertyChange("lastFlagged", old, getLastFlagged());
        }
        
        /**
         * 
         */
        private void updateFlaggedDates() {
            // PENDING JW: should be handled by converter
            // not working - "flaggedDates" is not a real property because different
            // types in getter/setter
            if (getLastFlagged() == null) {
                monthView.setFlaggedDates();
                return;
            }
            Set<Date> old = monthView.getFlaggedDates();
            Date[] flagged = new Date[old.size() + 1];
            int index = 0;
            for (Date d : old) {
                flagged[index++] = d;
            }
            flagged[index] = getLastFlagged();
            monthView.setFlaggedDates(flagged);
        }
        
        
        /**
         * @return the lastUnselectable
         */
        public Date getLastUnselectable() {
            return lastUnselectable;
        }
        /**
         * @param lastUnselectable the lastUnselectable to set
         */
        public void setLastUnselectable(Date lastUnselectable) {
            Date old = getLastUnselectable();
            this.lastUnselectable = lastUnselectable;
            updateLastUnselectable();
            firePropertyChange("lastUnselectable", old, getLastUnselectable());
        }
        
        /**
         * 
         */
        private void updateLastUnselectable() {
            // JW: can't bind directly - it's not a property
            if (getLastUnselectable() == null) {
                monthView.setUnselectableDates();
                return;
            }
            Set<Date> old = monthView.getSelectionModel().getUnselectableDates();
            SortedSet<Date> result = new TreeSet<Date>(old);
            result.add(getLastUnselectable());
            monthView.getSelectionModel().setUnselectableDates(result);
        }
        /**
         * @param lower the lower to set
         */
        public void setLowerBound(Date lower) {
            Date old = getLowerBound();
            this.lower = lower;
            monthView.setLowerBound(lower);
            firePropertyChange("lowerBound", old, getLowerBound());
        }
        /**
         * @return the lower
         */
        public Date getLowerBound() {
            return lower;
        }
        /**
         * @param upper the upper to set
         */
        public void setUpperBound(Date upper) {
            Date old = getUpperBound();
            this.upper = upper;
            monthView.setUpperBound(upper);
            firePropertyChange("upperBound", old, getUpperBound());
        }
        /**
         * @return the upper
         */
        public Date getUpperBound() {
            return upper;
        }
        
        
    }

    // TODO move it to package org.jdesktop.swingx.binding
    private static class DayOfWeekConverter extends Converter<Date, Integer> {

        Calendar calendar;
        
        public DayOfWeekConverter(Calendar calendar) {
            this.calendar = Contract.asNotNull(calendar, "calendar must not be null");
        }
        
        @Override
        public Integer convertForward(Date date) {
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_WEEK);
        }

        @Override
        public Date convertReverse(Integer arg0) {
            return null;
        }
        
    }

}
