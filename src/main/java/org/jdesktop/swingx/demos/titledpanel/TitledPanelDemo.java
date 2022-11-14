/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.titledpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.plaf.PainterUIResource;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import swingset.AbstractDemo;

/**
 * A demo for the {@code JXTitledPanel}.
 * 
 * @author Karl George Schaefer
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(value = "JXTitledPanel Demo", 
//        category = "Containers", 
//        description = "Demonstrates JXTitledPanel, a container with a title display.", 
//        sourceFiles = {
//        "org/jdesktop/swingx/demos/titledpanel/TitledPanelDemo.java",
//        "org/jdesktop/swingx/demos/titledpanel/resources/TitledPanelDemo.properties"
//        })
public class TitledPanelDemo extends AbstractDemo {
	
    // PENDING JW: removed extending DefaultDemoPanel because fell into the pit
    // of this.fields not yet initialized in overridden methods
    // plus: should consider to not extend a panel at all - this class is-not-a view
    // just a provider of a view :-)
    
	private static final long serialVersionUID = -2689327901464190656L;
	private static final Logger LOG = Logger.getLogger(TitledPanelDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JXTitledPanel, a container with a title display";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new TitledPanelDemo(controller);
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

	/**
	 * A special type of Panel that has a Title section and a Content section.
	 * <br>
	 * The following properties can be set with the UIManager to change the look and feel of the JXTitledPanel:
	 * <ul>
	 * <li>JXTitledPanel.titleForeground</li>
	 * <li>JXTitledPanel.titleBackground</li>
	 * <li>JXTitledPanel.titleFont ==> fontChooserCombo</li>
	 * <li>JXTitledPanel.titlePainter ==> backgroundChooserCombo </li> 
	 * <li>JXTitledPanel.captionInsets</li>
	 * <li>JXTitledPanel.rightDecoration ==> setNavigatorVisible</li>
	 * <li>JXTitledPanel.leftDecoration ==> setNavigatorVisible</li>
	 * </ul>
	 */
	private JXTitledPanel titledPanel; // with 3 cards, the first holds a controller
    private List<JComponent> cards;
    private JButton prevButton;
    private JButton nextButton;
    
    // controller:
    private JXTitledSeparator controlSeparator;
    private JTextField titleField;
    private JComboBox<DisplayInfo<Font>> fontChooserCombo;
    private JComboBox<DisplayInfo<Painter<?>>> backgroundChooserCombo;
    private JCheckBox visibleBox;
    
    /**
     * Constructor
     * 
     * @param frame controller Frame
     */
    public TitledPanelDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	/* initComponents
    	 * the only component is a titledPanel with 3 cards.
    	 * titledPanel is decorated left and right with buttons for card navigation
    	 */
        titledPanel = new JXTitledPanel() {

            /** 
             * @inherited <p>
             * 
             * Overridden to adjust to size requirements of invisible cards.
             */
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                Dimension child = getContentContainer().getPreferredSize();
                int width = child.width;
                for (JComponent card : cards) {
                    if (card != getContentContainer()) {
                        Dimension cardDim = card.getPreferredSize();
                        width = Math.max(width, cardDim.width);
                    }
                }
                if (width > child.width) {
                    dim.width += width - child.width;
                }
                return dim;
            }
            
        };
        titledPanel.setName("titledPanel");
        titledPanel.setTitle(getBundleString("titledPanel.title"));

        prevButton = new JButton();
        prevButton.setName("previousButton");
        nextButton = new JButton(); 
        nextButton.setName("nextButton");

        
        JComponent firstCard = createFirstCard();
        firstCard.setName("firstCard");
        
        titledPanel.setContentContainer(firstCard);
        
        JComponent secondCard = createSecondCard();
        secondCard.setName("secondCard");
        
        JComponent thirdCard = createThirdCard();
        thirdCard.setName("thirdCard");
        
        cards = new ArrayList<JComponent>();
        cards.add(firstCard);
        cards.add(secondCard);
        cards.add(thirdCard);
        
        add(titledPanel);

//        bind();
        applyNavigationDefaults();
        installUIListener();
    }

    private JComponent createFirstCard() { // formerly holds controller
        JComponent firstCard = new JXPanel();
        firstCard.add(new JXLabel("first card formerly holds controller\n which are now in extra frame"));
        return firstCard;
    }
    
    
    private JXPanel createSecondCard() {
        JXPanel content = new JXPanel();
        content.add(new JButton("Button 1"));
        content.add(new JButton("Button 2"));
        content.add(new JButton("Button 3"));
        return content;
    }

    private JComponent createThirdCard() {
        JComponent thirdCard = new JXPanel();
        thirdCard.add(new JXLabel("The quick brown fox\n jumped over the lazy dog."));
        return thirdCard;
    }

    @Override
	public JXPanel getControlPane() {
        JXPanel control = new JXPanel();
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:g", // columns
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
                ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, control);
        builder.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        controlSeparator = new JXTitledSeparator();
        controlSeparator.setName("controlSeparator");
        controlSeparator.setTitle(getBundleString("controlSeparator.title"));
        builder.add(controlSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        titleField = new JTextField(20);
        titleField.setName("titleField");
        titleField.setText(getBundleString("titleField.text"));
        JLabel titleLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                titleField, cc.xywh(widgetColumn, currentRow, 1, 1));
        titleLabel.setName("titleLabel");
        titleLabel.setText(getBundleString("titleLabel.text", titleLabel));
//        LabelHandler.bindLabelFor(titleLabel, titleField); // calls titleLabel.setLabelFor(titleField);
//        titleLabel.setLabelFor(titleField);
        titleField.addActionListener(ae -> {
//        	LOG.info("actionEvent:"+ae + " titleField.Text="+titleField.getText());
        	titledPanel.setTitle(titleField.getText());
        });        
        currentRow += 2;
        
        fontChooserCombo = new JComboBox<DisplayInfo<Font>>();
        fontChooserCombo.setName("fontChooserCombo");       
        JLabel fontLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                fontChooserCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
        fontLabel.setName("fontChooserLabel");
        fontLabel.setText(getBundleString("fontChooserLabel.text", fontLabel));
        LabelHandler.bindLabelFor(fontLabel, fontChooserCombo);
        fontChooserCombo.addActionListener(ae -> {
//        	LOG.info("actionEvent:"+ae + " fontChooserCombo.SelectedIndex="+fontChooserCombo.getSelectedIndex());
        	DisplayInfo<Font> item = (DisplayInfo<Font>)fontChooserCombo.getSelectedItem();
        	LOG.info(" fontChooserCombo.SelectedItem (Font)="+item.getValue());
        	titledPanel.setTitleFont(item.getValue());
        });        
        currentRow += 2;
        
        backgroundChooserCombo = new JComboBox<DisplayInfo<Painter<?>>>();
        backgroundChooserCombo.setName("backgroundChooserCombo");       
        JLabel backgroundLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                backgroundChooserCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
        backgroundLabel.setName("backgroundChooserLabel");
        backgroundLabel.setText(getBundleString("backgroundChooserLabel.text", backgroundLabel));
        LabelHandler.bindLabelFor(backgroundLabel, backgroundChooserCombo);
        backgroundChooserCombo.addActionListener(ae -> {
//        	LOG.info("actionEvent:"+ae + " backgroundChooserCombo.SelectedIndex="+backgroundChooserCombo.getSelectedIndex());
        	DisplayInfo<Painter<Component>> item = (DisplayInfo<Painter<Component>>)backgroundChooserCombo.getSelectedItem();
        	LOG.info(" backgroundChooserCombo.SelectedItem (Painter)="+item.getValue());
        	titledPanel.setTitlePainter(item.getValue());
        });        
        currentRow += 2;
        
        updateUIProperties();
        
        visibleBox = new JCheckBox(); // JCheckBox extends JToggleButton, JToggleButton extends AbstractButton
        visibleBox.setSelected(true);
        setNavigatorVisible(visibleBox.isSelected());
        visibleBox.setName("visibleBox");
        visibleBox.setText(getBundleString("toggleNavigatorVisible.Action.text")); // NOT "visibleBox.text"!
        visibleBox.addActionListener( ae -> {
        	LOG.info("visibleBox.isSelected/setNavigatorVisible="+visibleBox.isSelected());
        	setNavigatorVisible(visibleBox.isSelected());
        });
        
        builder.add(visibleBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;

		return control;
	}

    private boolean isTrailing() {
        return titledPanel.getContentContainer() != cards.get(0);
    }
    
    private boolean isLeading() {
        return titledPanel.getContentContainer() != cards.get(cards.size() - 1);
    }
    
//    @Action (selectedProperty = "navigatorVisible") 
//    public void toggleNavigatorVisible() {
//        // do nothing - action happens in setter of selectedProperty
//    }
    
    private boolean isNavigatorVisible() {
        return titledPanel.getLeftDecoration() != null;
    }
    
    private void setNavigatorVisible(boolean visible) {
        // <snip> JXTitledPanel configure title properties
        // Show/Hide left and right decoration options
        if (isNavigatorVisible() == visible) return;
        boolean old = isNavigatorVisible();
        titledPanel.setLeftDecoration(visible ? prevButton : null);
        titledPanel.setRightDecoration(visible ? nextButton : null);
        // </snip>

        titledPanel.revalidate();
        titledPanel.repaint();
        firePropertyChange("navigatorVisible", old, isNavigatorVisible());
    }
    
    private ComboBoxModel<DisplayInfo<Font>> createFontModel() {
        MutableComboBoxModel<DisplayInfo<Font>> model = new DefaultComboBoxModel<DisplayInfo<Font>>();
        // <snip> JXTitledPanel configure title properties
        // Font options (based on default)
        Font baseFont = UIManager.getFont("JXTitledPanel.titleFont");
        model.addElement(new DisplayInfo<Font>("Default ", baseFont));
        Font italicFont = new FontUIResource(baseFont.deriveFont(Font.ITALIC));
        model.addElement(new DisplayInfo<Font>("Derived (Italic)" , italicFont));
        Font bigFont = new FontUIResource(baseFont.deriveFont(baseFont.getSize2D() * 2));
        model.addElement(new DisplayInfo<Font>("Derived (Doubled Size) ", bigFont));
        // </snip>
        return model;
    }
    
    private ComboBoxModel<DisplayInfo<Painter<?>>> createBackgroundModel() {
        MutableComboBoxModel<DisplayInfo<Painter<?>>> model = new DefaultComboBoxModel<DisplayInfo<Painter<?>>>();
        // <snip> JXTitledPanel configure title properties
        // Background Painter options 
        Painter<?> bgPainter =  (Painter<?>) UIManager.get("JXTitledPanel.titlePainter");
        model.addElement(new DisplayInfo<Painter<?>>("Default ", bgPainter));
        model.addElement(new DisplayInfo<Painter<?>>("Checkerboard", new PainterUIResource<JComponent>(new CheckerboardPainter())));
        // PENDING JW: add more options - image, gradient, animated... 
        // add Matte Painter Gradient "single stop, horiz", see PainterDemo
        Paint gradient = new GradientPaint(new Point(30, 30), Color.RED, new Point(80, 30),  Color.GREEN);
        model.addElement(new DisplayInfo<Painter<?>>("GradientPaint", new PainterUIResource<JComponent>(new MattePainter(gradient))));
        // </snip>
        return model;
    }
    
//    @SuppressWarnings("unchecked")
//    private void bind() {
//        // set actions
//        prevButton.setAction(DemoUtils.getAction(this, "previousCard"));
//        nextButton.setAction(DemoUtils.getAction(this, "nextCard"));
//        // re-set action-independent Icons
//        applyNavigationDefaults();
//        visibleBox.setAction(DemoUtils.getAction(this, "toggleNavigatorVisible"));
//        
//        DefaultListRenderer renderer = new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION);
//        fontChooserCombo.setRenderer(renderer);
//        backgroundChooserCombo.setRenderer(renderer);
//        
//        BindingGroup group = new BindingGroup();
//        // <snip> JXTitledPanel configure title properties
//        // edit title text
//        group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
//                titleField, BeanProperty.create("text"),
//                titledPanel, BeanProperty.create("title")));
//        // </snip>
//        Binding fontBinding = Bindings.createAutoBinding(UpdateStrategy.READ, 
//                fontChooserCombo, BeanProperty.create("selectedItem"),
//                titledPanel, BeanProperty.create("titleFont"));
//        fontBinding.setConverter(new DisplayInfoConverter<Font>());
//        group.addBinding(fontBinding);
//        
//        Binding backgroundBinding = Bindings.createAutoBinding(UpdateStrategy.READ, 
//                backgroundChooserCombo, BeanProperty.create("selectedItem"),
//                titledPanel, BeanProperty.create("titlePainter"));
//        backgroundBinding.setConverter(new DisplayInfoConverter<Painter>());
//        group.addBinding(backgroundBinding);
//        
//        group.bind();
//        
//        updateUIProperties();
//        installUIListener();
//    }

    // <snip> JXTitledPanel configure title properties
    // reset to ui-defaults on changing LAF
    private void updateUIProperties() {
        fontChooserCombo.setModel(createFontModel());
        backgroundChooserCombo.setModel(createBackgroundModel());
//        applyNavigationDefaults();
    }

    // fired whenever a bean changes a "bound"property
    private void installUIListener() {
        PropertyChangeListener listener = new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
// java.beans.PropertyChangeEvent[propertyName=lookAndFeel; oldValue=[The Java(tm) Look and Feel - javax.swing.plaf.metal.MetalLookAndFeel]; newValue=[Nimbus Look and Feel - javax.swing.plaf.nimbus.NimbusLookAndFeel]; propagationId=null; source=class javax.swing.UIManager]          	
            	LOG.info("PropertyChangeEvent "+evt);
                updateUIProperties();
            	if(evt.getPropertyName().equals("lookAndFeel")) {
            		// update UI:
            		titledPanel.updateUI();
            		prevButton.updateUI();
            		nextButton.updateUI();
            		setNavigatorVisible(isNavigatorVisible());
            	}
            }
        };
        UIManager.addPropertyChangeListener(listener);
    }
    // </snip>

    
//------------------- init view
    
//    private JXPanel createFirstCard() {
//        JXPanel control = new JXPanel();
//        FormLayout formLayout = new FormLayout(
//                "5dlu, r:d:n, l:4dlu:n, f:d:g", // columns
//                "c:d:n " +
//                ", t:4dlu:n, c:d:n " +
//                ", t:4dlu:n, c:d:n" +
//                ", t:4dlu:n, c:d:n" +
//                ", t:4dlu:n, c:d:n" +
//                ", t:4dlu:n, c:d:n"
//                ); // rows
//        PanelBuilder builder = new PanelBuilder(formLayout, control);
//        builder.setBorder(Borders.DIALOG_BORDER);
//        CellConstraints cl = new CellConstraints();
//        CellConstraints cc = new CellConstraints();
//        
//        controlSeparator = new JXTitledSeparator();
//        controlSeparator.setName("controlSeparator");
//        builder.add(controlSeparator, cc.xywh(1, 1, 4, 1));
//        
//        int labelColumn = 2;
//        int widgetColumn = labelColumn + 2;
//        int currentRow = 3;
//        titleField = new JTextField(20);
//        titleField.setName("titleField");
//        
//        JLabel titleLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
//                titleField, cc.xywh(widgetColumn, currentRow, 1, 1));
//        currentRow += 2;
//        
//        titleLabel.setName("titleLabel");
//        LabelHandler.bindLabelFor(titleLabel, titleField);
//        
//        fontChooserCombo = new JComboBox();
//        fontChooserCombo.setName("fontChooserCombo");
//        
//        JLabel fontLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
//                fontChooserCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
//        currentRow += 2;
//        fontLabel.setName("fontChooserLabel");
//        LabelHandler.bindLabelFor(fontLabel, fontChooserCombo);
//        
//        backgroundChooserCombo = new JComboBox();
//        backgroundChooserCombo.setName("backgroundChooserCombo");
//        
//        JLabel backgroundLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
//                backgroundChooserCombo, cc.xywh(widgetColumn, currentRow, 1, 1));
//        currentRow += 2;
//        backgroundLabel.setName("backgroundChooserLabel");
//        LabelHandler.bindLabelFor(backgroundLabel, backgroundChooserCombo);
//        
//        
//        visibleBox = new JCheckBox();
//        visibleBox.setName("visibleBox");
//        
//        builder.add(visibleBox, cc.xywh(widgetColumn, currentRow, 1, 1));
//        currentRow += 2;
//        return control;
//    }
    
    
    /**
     * @param button
     * @param direction of nutton icon Compass-direction, use SwingConstants.WEST or SwingConstants.EAST
     */
    private void applyNavigationDefaults(JButton button, int direction) {
        int arrowSize = SizingConstants.SMALL_ICON; // 16
        int buttonSize = 22;
        Color arrowColor = UIManager.getColor("Label.foreground");
        Color inactiveColor = Color.LIGHT_GRAY; //UIManager.getColor("Label.disabledText");
        Dimension buttonDim = new Dimension(buttonSize, buttonSize);

    	RadianceIcon activArrow = ArrowIcon.of(arrowSize, arrowSize);
    	activArrow.setRotation(direction);
    	activArrow.setColorFilter(color -> arrowColor);
    	button.setIcon(activArrow);
    	RadianceIcon inactivArrow = ArrowIcon.of(arrowSize, arrowSize);
    	inactivArrow.setRotation(direction);
    	inactivArrow.setColorFilter(color -> inactiveColor);
    	button.setDisabledIcon(inactivArrow);
        button.setPreferredSize(buttonDim);
        button.setFocusable(false);
    }

    private void applyNavigationDefaults() {
        applyNavigationDefaults(prevButton, SwingConstants.WEST);
        prevButton.setEnabled(isTrailing());
        applyNavigationDefaults(nextButton, SwingConstants.EAST);
        nextButton.setEnabled(isLeading());
        //prevButton.setAction:
        prevButton.addActionListener(ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+prevButton.isSelected() + " isTrailing()="+isTrailing());
        	if(isTrailing()) {
        		// replace content with previous
        		int card = cards.indexOf(titledPanel.getContentContainer());
        		titledPanel.setContentContainer(cards.get(card - 1));
        		prevButton.setEnabled(isTrailing());
                nextButton.setEnabled(isLeading());
        		titledPanel.revalidate();
        		titledPanel.repaint();
        	}
        });
        nextButton.addActionListener(ae -> {
        	LOG.info("actionEvent:"+ae + " selected="+nextButton.isSelected() + " isLeading()="+isLeading());
        	if(isLeading()) {
        		// replace content with next
        		int card = cards.indexOf(titledPanel.getContentContainer());
        		titledPanel.setContentContainer(cards.get(card + 1));
        		prevButton.setEnabled(isTrailing());
                nextButton.setEnabled(isLeading());
        		titledPanel.revalidate();
        		titledPanel.repaint();
        	}
        });
    }

}
