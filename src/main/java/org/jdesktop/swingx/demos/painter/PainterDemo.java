/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.painter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.binding.DisplayInfoArrayConverter;
import org.jdesktop.swingx.binding.DisplayInfoConverter;
import org.jdesktop.swingx.binding.LabelHandler;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.painter.AbstractAreaPainter;
import org.jdesktop.swingx.painter.AbstractAreaPainter.Style;
import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.AbstractPainter.Interpolation;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.GlossPainter.GlossPosition;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.painter.TextPainter;
import org.jdesktop.swingx.painter.effects.AreaEffect;
import org.jdesktop.swingx.painter.effects.GlowPathEffect;
import org.jdesktop.swingx.painter.effects.InnerGlowPathEffect;
import org.jdesktop.swingx.painter.effects.InnerShadowPathEffect;
import org.jdesktop.swingx.painter.effects.NeonBorderEffect;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ShapeUtils;
import org.jdesktop.swingx.util.Utilities;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jhlabs.image.AverageFilter;
import com.jhlabs.image.BlockFilter;
import com.jhlabs.image.BlurFilter;
import com.jhlabs.image.BorderFilter;
import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.BumpFilter;
import com.jhlabs.image.ChromeFilter;
import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.CrystallizeFilter;
import com.jhlabs.image.DespeckleFilter;
import com.jhlabs.image.DiffuseFilter;
import com.jhlabs.image.DiffusionFilter;
import com.jhlabs.image.DilateFilter;
import com.jhlabs.image.DisplaceFilter;
import com.jhlabs.image.DitherFilter;
import com.jhlabs.image.EdgeFilter;
import com.jhlabs.image.EmbossFilter;
import com.jhlabs.image.EqualizeFilter;
import com.jhlabs.image.ErodeFilter;
import com.jhlabs.image.FlareFilter;
import com.jhlabs.image.GainFilter;
import com.jhlabs.image.GammaFilter;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GlintFilter;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.GrayFilter;
import com.jhlabs.image.InvertAlphaFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.LensBlurFilter;
import com.jhlabs.image.LevelsFilter;
import com.jhlabs.image.LifeFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.MarbleTexFilter;
import com.jhlabs.image.MedianFilter;
import com.jhlabs.image.MinimumFilter;
import com.jhlabs.image.MirrorFilter;
import com.jhlabs.image.MotionBlurFilter;
import com.jhlabs.image.NoiseFilter;
import com.jhlabs.image.OilFilter;
import com.jhlabs.image.OpacityFilter;
import com.jhlabs.image.OutlineFilter;
import com.jhlabs.image.PointillizeFilter;
import com.jhlabs.image.PosterizeFilter;
import com.jhlabs.image.QuantizeFilter;
import com.jhlabs.image.RaysFilter;
import com.jhlabs.image.ReduceNoiseFilter;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import com.jhlabs.image.SharpenFilter;
import com.jhlabs.image.SkeletonFilter;
import com.jhlabs.image.SmearFilter;
import com.jhlabs.image.SolarizeFilter;
import com.jhlabs.image.SparkleFilter;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.SwimFilter;
import com.jhlabs.image.ThresholdFilter;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.image.UnsharpFilter;
import com.jhlabs.image.WaterFilter;

import swingset.AbstractDemo;

/**
 * A demo for {@code Painter}.
 *
 * @author Karl George Schaefer
 * @author joshy (original PainterDemoSet)
 * @author EUG (BusyPainterDemos+CheckerboardPainterDemos, reorg)
 */
//@DemoProperties(
//    value = "Painter Demo",
//    category = "Decorators",
//    description = "Demonstrates Painters, a painting delegate.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/painter/PainterDemo.java",
//        "org/jdesktop/swingx/demos/painter/PainterDemoUtils.java",
//        "org/jdesktop/swingx/binding/DisplayInfo.java",
//        "org/jdesktop/swingx/binding/DisplayInfoConverter.java",
//        "org/jdesktop/swingx/demos/painter/resources/PainterDemo.properties"
//    }
//)
public class PainterDemo extends AbstractDemo {
    
	private static final long serialVersionUID = 6581588324297355927L;
	private static final Logger LOG = Logger.getLogger(PainterDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates Painters, a painting delegate.";

    /**
     * main method allows us to run as a standalone demo.
     * @param args params
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
			JXFrame controller = new JXFrame("controller", exitOnClose);
			AbstractDemo demo = new PainterDemo(controller);
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

    private JXPanel painterDisplay;
    
    // Controller:
    private JSplitPane splitPane;
    private JXTree painterDemos; // a list of painter launchers
    private PainterControl painterControl; // implements AbstractBean, instance created on bind()

    /* properties panel (painterControlPanel) made by createPainterPropertiesPanel() consists of
     * - basePainterControlPanel
     * - layoutPainterControlPanel
     * - areaPainterControlPanel
     */
    private JXCollapsiblePane basePainterControlPanel;
    private JXCollapsiblePane layoutPainterControlPanel;
    private JXCollapsiblePane areaPainterControlPanel;

    /*
     * Base Properties / basePainterControlPanel: createBasePainterControlPanel()
     */
    private JXTitledSeparator baseSeparator;
    private JXComboBox<Interpolation> interpolationBox;
    private JXComboBox<DisplayInfo<BufferedImageOp>> filterBox;
    private JCheckBox visibleBox;
    private JCheckBox antialiasBox;

    /*
     * Layout Properties / layoutPainterControlPanel: createLayoutPainterControl()
     */
    private JXTitledSeparator layoutSeparator;
    private JXComboBox<HorizontalAlignment> horizontalAlignmentBox;
    private JXComboBox<VerticalAlignment> verticalAlignmentBox;
    private JSlider insetSlider;
    private JCheckBox fillHorizontal;
    private JCheckBox fillVertical;

    /*
     * Area Properties / areaPainterControlPanel: createAreaPainterControlPanel()
     */
    private JXTitledSeparator areaSeparator;
    private JXComboBox<Style> styleBox;
    private JXComboBox<DisplayInfo<AreaEffect>> effectBox;
    private JSlider borderWidthSlider;
    private JCheckBox paintStretchedBox;
   
    /**
     * PainterDemo Constructor
     * 
     * @param frame controller Frame
     */
    public PainterDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(BorderFactory.createLoweredBevelBorder());

        painterDisplay = new JXPanel(new BorderLayout());
        painterDisplay.setBorder(BorderFactory.createEtchedBorder());
        painterDisplay.setPaintBorderInsets(false);
        add(painterDisplay, BorderLayout.CENTER);
        
        // a CheckerboardPainter as default:
        int squareSize = 128;
		GradientPaint gp = new GradientPaint(new Point2D.Double(0, 0), Color.BLACK, 
				new Point2D.Double(0, squareSize), Color.GRAY);
		CheckerboardPainter p = new CheckerboardPainter();
		p.setDarkPaint(gp);
		p.setLightPaint(Color.WHITE);
		p.setSquareSize(squareSize);
		painterDisplay.setBackgroundPainter(p);
    }

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel(new BorderLayout());

        painterDemos = new JXTree();
        painterDemos.setRootVisible(false);
        painterDemos.setModel(createPainters());
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
        		new JScrollPane(painterDemos), createPainterPropertiesPanel());
        panel.add(splitPane);

        painterDemos.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        painterDemos.addTreeSelectionListener( treeSelectionEvent -> {
        	// void valueChanged(TreeSelectionEvent e);
        	JXTree source = (JXTree)treeSelectionEvent.getSource(); // == painterDemos
        	TreeSelectionModel tsm = source.getSelectionModel();
			Object o = tsm.getSelectionPath().getLastPathComponent();
			LOG.info("selected aka LastPathComponent:"+o);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)o;
			if(node.isLeaf()) {
				Object uo = node.getUserObject();
				LOG.info("selected Leaf user Object:"+uo);
				DisplayInfo<?> di = (DisplayInfo<?>)uo;
				if(di.getValue()!=null && di.getValue() instanceof Painter) {
					LOG.fine("painter:"+di.getValue());
					Painter<Component> painter = (Painter<Component>)di.getValue();
					bind(painter);
				}
			} else {
				LOG.info("selected node is not Leaf user Object:"+node.getUserObject());
			}
        });
        
        return panel;
	}

    private void bind(Painter<Component> painter) {
		painterControl = new PainterControl();
		painterControl.setPainter(painter);
		bindSelection(painterDisplay, painterControl);
        // bindEnabled
        bindEnabled("areaEnabled", areaSeparator, styleBox, effectBox, borderWidthSlider, paintStretchedBox);
        bindEnabled("alignEnabled", layoutSeparator, horizontalAlignmentBox, verticalAlignmentBox, insetSlider, fillHorizontal, fillVertical);
        bindEnabled("baseEnabled", baseSeparator, interpolationBox, visibleBox, antialiasBox);
        // bind collapsed
        bindInversCollapsed("baseEnabled", layoutPainterControlPanel, basePainterControlPanel, areaPainterControlPanel);
        
		painterDisplay.setBackgroundPainter(painter);   	
    }
    
    private void bindSelection(JXPanel backgroundPainter, PainterControl painter) {
        painterDemos.setCellRenderer(new DefaultTreeRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        Converter<?, ?> painterConverter = new DisplayInfoConverter<Painter<Object>>();
        
        BindingGroup group = new BindingGroup();       
        BeanProperty<JXTree, String> sv = BeanProperty.create("selectedElement_UNWRAP_NODE");
        BeanProperty<JXPanel, String> tv = BeanProperty.create("backgroundPainter");
        //Binding<SS, SV, TS, TV>: 
        // <SS> type of source object, 
        // <SV> type of value that the source property represents,
        // TS, TV dto for target
        Binding b1 = Bindings.createAutoBinding(UpdateStrategy.READ, // strategy: keep target in sync with source
        		painterDemos, sv, // SS:JXTree, Property<SS,SV>
        		backgroundPainter, tv // JXPanel, Property<TS,TV>
        		);
        // Converter<SV, TV> converter
        // use painterConverter or painterConverter2
        Converter<DisplayInfo<Painter<Object>>, Painter<Object>> painterConverter2 = new DisplayInfoConverter<Painter<Object>>();
        b1.setConverter(painterConverter2);
        group.addBinding(b1);
        
        Binding b2 = Bindings.createAutoBinding(UpdateStrategy.READ, // strategy: keep target in sync with source
        		painterDemos, sv, // SS:JXTree, Property<SS,SV>
        		painter, BeanProperty.create("painter") // TS, Property<TS,TV>
        		);
        b2.setConverter(painterConverter);
        group.addBinding(b2);

        group.bind();
    }

    /**
     * @param property
     */
    /*
     * used in bind(): 
        bindEnabled("areaEnabled"
        , areaSeparator
        , styleBox
        , effectBox
        , borderWidthSlider
        , paintStretchedBox
        );
        bindEnabled("alignEnabled", layoutSeparator, horizontalAlignmentBox, verticalAlignmentBox, insetSlider, fillHorizontal, fillVertical);
        bindEnabled("baseEnabled", baseSeparator, interpolationBox, visibleBox, antialiasBox);
     */
    private void bindEnabled(String property, JComponent... components ) {
        BindingGroup group = new BindingGroup();
        for (JComponent comp : components) {
        	LOG.fine(property+":comp:"+comp);
            group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                    painterControl, BeanProperty.create(property),
                    comp, BeanProperty.create("enabled")));       
        }
        group.bind();
    }

    /**
     * @param property
     */
    private void bindInversCollapsed(String property, JComponent... components ) {
        BindingGroup group = new BindingGroup();
        for (JComponent comp : components) {
        	LOG.info(property+":comp:"+comp);
            group.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                    painterControl, ELProperty.create("${!" + property + "}"),
                    comp, BeanProperty.create("collapsed")));
        }
        group.bind();
    }

//---------------------------- Painters

    /**
     * @return
     */
    private TreeModel createPainters() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add(createImagePainterDemos());
        root.add(createShapePainterDemos());
        root.add(createTextPainterDemos());
        root.add(createRectanglePainterDemos());
        root.add(createTransformPainterDemos());
        root.add(createGlossPainterDemos());
        root.add(createPinstripePainterDemos());
        
        root.add(createBusyPainterDemos());
        root.add(createCheckerboardPainterDemos());  
        
        root.add(createMattePainterDemos());
        root.add(createCompoundPainterDemos());
        return new DefaultTreeModel(root);
    }

    private DefaultMutableTreeNode createInfoNode(String desc, MattePainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<MattePainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, ImagePainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<ImagePainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, ShapePainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<ShapePainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, TextPainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<TextPainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, RectanglePainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<RectanglePainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, PinstripePainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<PinstripePainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, CheckerboardPainter painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<CheckerboardPainter>(desc, painter));
    }
    private DefaultMutableTreeNode createInfoNode(String desc, CompoundPainter<Component> painter) {   	
        return new DefaultMutableTreeNode(new DisplayInfo<CompoundPainter<Component>>(desc, painter));
    }
    
    private MutableTreeNode createImagePainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Image Painter Demos", (ImagePainter)null);
        
        final String imgdir = "resources/images/";
        Class<?> thisClass = PainterDemo.class;
        LOG.info("getResources for "+ thisClass + " imagedir:"+imgdir);
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"border.gif");
            BufferedImage img = ImageIO.read(fis);
            node.add(createInfoNode("small image", new ImagePainter(img)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"moon.jpg");
            BufferedImage img = ImageIO.read(fis);
            node.add(createInfoNode("big image", new ImagePainter(img)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"border.gif");
            BufferedImage img = ImageIO.read(fis);
            ImagePainter ip = new ImagePainter(img);
            ip.setHorizontalRepeat(true);
            node.add(createInfoNode("horizontal repeat", ip));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"border.gif");
            BufferedImage img = ImageIO.read(fis);
            ImagePainter ip = new ImagePainter(img);
            ip.setVerticalRepeat(true);
            node.add(createInfoNode("vertical repeat", ip));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"border.gif");
            BufferedImage img = ImageIO.read(fis);
            ImagePainter ip = new ImagePainter(img);
            ip.setBorderPaint(Color.BLACK);
            node.add(createInfoNode("image with black border", ip));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"a-glyph.png");
            BufferedImage img = ImageIO.read(fis);
            node.add(createInfoNode("An image of 'A' with transparent parts", new ImagePainter(img)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        try {
        	InputStream fis = Utilities.getResourceAsStream(this.getClass(), imgdir+"a-glyph.png");
            BufferedImage img = ImageIO.read(fis);
            ImagePainter ip = new ImagePainter(img);
            ip.setFillPaint(Color.RED);
            node.add(createInfoNode("red background visible through transparent parts", ip));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //FIXME need a way to background load the image, cf. SwingX #1099
//        try {
//            ImagePainter ip = new ImagePainter();
//            ip.setImageString("http://java.sun.com/developer/techDocs/hi/repository/graphicsRepository/toolbarButtonGraphics/general/Delete24.gif");
//            
//            DefaultMutableTreeNode pin = createInfoNode("image loaded from remote URL", ip);
//            node.add(pin);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        return node;
    }
    
    private MutableTreeNode createShapePainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Shape Painter Demos", (ShapePainter)null);
        
        // build a star shape with 5 points and 30 degree angles
        Shape starShape = ShapeUtils.generatePolygon(5,30,15,true);
        ShapePainter sp = new ShapePainter(starShape, Color.RED);
        node.add(createInfoNode("Star style", sp));
        
        return node;
    }
    
    private MutableTreeNode createTextPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Text Painter Demos", (TextPainter)null);
        String textToPaint = "Neon";
        Font font = new Font("SansSerif", Font.BOLD, 80);
        
        TextPainter tp = new TextPainter(textToPaint, font, Color.RED);
        node.add(createInfoNode("Text", tp));
        
        tp = new TextPainter(textToPaint, font, Color.BLACK);
        tp.setFillPaint(PaintUtils.AERITH);
        node.add(createInfoNode("Text with gradient", tp));
        
        return node;
    }
    
    private MutableTreeNode createRectanglePainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Rectangle Painter Demos", (RectanglePainter)null);
        
        RectanglePainter rp = new RectanglePainter(20, 20, 20, 20, 30, 30, true, Color.GREEN, 3, Color.GREEN.darker());
        node.add(createInfoNode("Rectangle, green on gray", rp));
        
        rp = new RectanglePainter(50, 50, 30, Color.GREEN);
        rp.setBorderPaint(Color.GREEN.darker());
        node.add(createInfoNode("Rectangle, 100x100", rp));
       
        rp = new RectanglePainter(5, 5, 5, 5, 10, 10, true, Color.RED, 3, Color.BLACK);
        node.add(createInfoNode("Red Rectangle w/  black border", rp));

        rp = new RectanglePainter(Color.BLACK,Color.RED);
        rp.setRoundHeight(10);
        rp.setRoundWidth(10);
        rp.setRounded(true);
        node.add(createInfoNode("Plain round rect for sizing bugs", rp));
        
        return node;
    }
    
    //TODO some transforms moving painter outside of visible region
    private MutableTreeNode createTransformPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Compound Painter Transform Demos", (CompoundPainter<Component>)null);
        Font font = new Font("SansSerif", Font.BOLD, 80);
        
        TextPainter tp = new TextPainter("Text", font);
        CompoundPainter<Component> cp = new CompoundPainter<Component>(tp);
        node.add(createInfoNode("Normal Text", cp));
        
        tp = new TextPainter("Text", font);
        cp = new CompoundPainter<Component>(tp);
        cp.setTransform(AffineTransform.getRotateInstance(-Math.PI * 2 / 8, 100, 100));
        node.add(createInfoNode("Rotated Text", cp));
        
        tp = new TextPainter("Text", font);
        cp = new CompoundPainter<Component>(tp);
        cp.setTransform(AffineTransform.getShearInstance(-0.2, 0));
        node.add(createInfoNode("Sheared Text", cp));
        
        tp = new TextPainter("Text", font);
        cp = new CompoundPainter<Component>(tp);
        cp.setTransform(AffineTransform.getScaleInstance(2, 2));
        node.add(createInfoNode("Scaled Text", cp));
        
        RectanglePainter rp = new RectanglePainter(30, 30, 30, 30, 30, 30, true, Color.RED, 4f, Color.RED.darker());
        tp = new TextPainter("Text", font);
        cp = new CompoundPainter<Component>(rp, tp);
        cp.setTransform(AffineTransform.getRotateInstance(-Math.PI * 2 / 8, 100, 100));
        node.add(createInfoNode("Rotated Text w/ effects on rect", cp));
        
        return node;
    }
    
    private MutableTreeNode createGlossPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Gloss Painter Demos", (RectanglePainter)null);

        RectanglePainter rp = new RectanglePainter(20, 20, 20, 20, 20, 20);
        rp.setFillPaint(Color.RED);
        rp.setBorderPaint(Color.RED.darker());
        rp.setStyle(RectanglePainter.Style.BOTH);
        rp.setBorderWidth(5);
        rp.setAntialiasing(true);
        CompoundPainter<Component> cp = new CompoundPainter<Component>(rp, new GlossPainter());
        node.add(createInfoNode("Gloss on top of rectangle", cp));

        rp = new RectanglePainter(20, 20, 20, 20, 20, 20, true, Color.RED, 5f, Color.RED.darker());
        rp.setAntialiasing(true);
        cp = new CompoundPainter<Component>(rp, new GlossPainter(GlossPosition.BOTTOM));
        node.add(createInfoNode("Gloss on bottom of rectangle", cp));

        return node;
    }
    
    private MutableTreeNode createPinstripePainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Pinstripe Painter Demos", (PinstripePainter)null);
        MattePainter black = new MattePainter(Color.BLACK);
        
        PinstripePainter pp = new PinstripePainter(Color.WHITE, 45, 1, 10);
        CompoundPainter<Component> cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("45deg white pinstripe on black", cp));
        
        pp = new PinstripePainter(Color.WHITE, 0, 1, 10);
        pp.setAntialiasing(true);
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("vertical white pinstripe on black", cp));
        
        pp = new PinstripePainter(Color.WHITE, 90, 1, 10);
        pp.setAntialiasing(true);
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("horizontal white pinstripe on black", cp));
        
        pp = new PinstripePainter(Color.WHITE, 45, 3, 10);
        pp.setAntialiasing(true);
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("3px wide white pinstripe on black", cp));
        
        pp = new PinstripePainter(Color.WHITE, 45, 10, 2);
        pp.setAntialiasing(true);
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("10px wide pinstripe w/ 2px spacing on black", cp));
        
        pp = new PinstripePainter(Color.WHITE, 45, 3, 15);
        pp.setAntialiasing(true);
        pp.setPaint(
                new GradientPaint(new Point(0, 0), Color.WHITE, new Point(10, 10), Color.BLACK));
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("pinstripe w/ 10px gradient", cp));
        
        pp = new PinstripePainter(Color.WHITE, 45, 3, 15);
        pp.setAntialiasing(true);
        pp.setPaint(
                new GradientPaint(new Point(0, 0), Color.WHITE, new Point(200, 200), Color.BLACK));
        cp = new CompoundPainter<Component>(black, pp);
        node.add(createInfoNode("pinstripe w/ 200px gradient", cp));
        
        return node;
    }
    
    private class BusyPainterAction implements ActionListener {

    	BusyPainter busyPainter;
    	public BusyPainterAction(BusyPainter bp) {
    		busyPainter = bp;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
            int frame = busyPainter.getFrame();
            if(busyPainter.getDirection()==BusyPainter.Direction.LEFT) {
                frame = frame>0 ? frame-1 : busyPainter.getPoints()-1;
            } else {
                frame = (frame+1)%busyPainter.getPoints();
            }
            busyPainter.setFrame(frame);
		}
    	
		public void startTimer(int delay) {
			Timer timer = new Timer(delay, this);
			timer.start();
		}
    }
    
    private MutableTreeNode createBusyPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Busy Painter Demos", (ImagePainter)null);
        MattePainter black = new MattePainter(Color.BLACK);
        
        final BusyPainter bp1 = new BusyPainter(); // final wg. int frame =
        LOG.info("BusyPainter bp"+bp1);
        BusyPainterAction bpa1 = new BusyPainterAction(bp1);
        bpa1.startTimer(100);        
        CompoundPainter<Component> cp = new CompoundPainter<Component>(black, bp1);
        node.add(createInfoNode("(default)shape of circle and bounds size 26x26 points", cp));
        
        final BusyPainter bp = new BusyPainter(52); // final wg. int frame =
        bp.setCacheable(true);
        bp.setPaintCentered(true);
        bp.setBaseColor(Color.RED);
        bp.setDirection(BusyPainter.Direction.LEFT);
        LOG.info("BusyPainter bp"+bp);
        BusyPainterAction bpa = new BusyPainterAction(bp);
        bpa.startTimer(100);
        cp = new CompoundPainter<Component>(black, bp);
        node.add(createInfoNode("Centers red shape in the area, height is 52.", cp));
                
        return node;
    }
    
    private MutableTreeNode createCheckerboardPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Checkerboard Painter Demo", (CheckerboardPainter)null);
        MattePainter black = new MattePainter(Color.BLACK);
        
        CheckerboardPainter bp = new CheckerboardPainter();
        LOG.config("CheckerboardPainter bp"+bp);
        CompoundPainter<Component> cp = new CompoundPainter<Component>(black, bp);
        node.add(createInfoNode("(default) Checkerboard white+light gray, square size 8x8", cp));
        
        GradientPaint gp = new GradientPaint(
        		new Point2D.Double(0, 0), Color.BLACK, 
        		new Point2D.Double(0, 32), Color.GRAY	
        	);
        bp = new CheckerboardPainter();
        bp.setDarkPaint(gp);
        bp.setLightPaint(Color.WHITE);
        bp.setSquareSize(32);
        LOG.config("CheckerboardPainter bp"+bp);
        cp = new CompoundPainter<Component>(black, bp);
        node.add(createInfoNode("Checkerboard white+GradientPaint for the dark, square size 32", cp));
                
        return node;
    }
    
    private MutableTreeNode createMattePainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Matte Painter Gradient Demos", (MattePainter)null);
        
        Paint gradient = new GradientPaint(new Point(30, 30), Color.RED, new Point(80, 30),  Color.GREEN);
        MattePainter mp = new MattePainter(gradient);
        node.add(createInfoNode("single stop, horiz", mp));
        
        gradient = new GradientPaint(new Point(30, 30), Color.RED, new Point(30, 80), Color.GREEN);
        node.add(createInfoNode("single stop, vert", new MattePainter(gradient)));
        
        gradient = new GradientPaint(new Point(30, 30), Color.RED, new Point(80, 80), Color.GREEN);
        node.add(createInfoNode("single stop, diag", new MattePainter(gradient)));
        
        gradient = new LinearGradientPaint(new Point(30,30), new Point(80,30), 
                new float[] {0f, 0.5f, 1f},
                new Color[] {Color.RED, Color.GREEN, Color.BLUE} 
        	);
        node.add(createInfoNode("multi stop horiz", new MattePainter(gradient)));
        
        gradient = new LinearGradientPaint(new Point(30,30), new Point(30,80), 
                new float[] {0f, 0.5f, 1f},
                new Color[] {Color.RED, Color.GREEN, Color.BLUE} 
        	);
        node.add(createInfoNode("multi stop vert", new MattePainter(gradient)));
        
        gradient = new LinearGradientPaint(new Point(30,30), new Point(30,80), 
                new float[] {0f, 0.5f, 1f},
                new Color[] {Color.RED, Color.GREEN, Color.BLUE} 
        	);
        node.add(createInfoNode("multi stop diag", new MattePainter(gradient)));
        
        return node;
    }
    
    private MutableTreeNode createCompoundPainterDemos() {
        DefaultMutableTreeNode node = createInfoNode("Compound Painter Gradient Demos", (MattePainter)null);
        
        MattePainter mp = new MattePainter(Color.GREEN);
        PinstripePainter pp = new PinstripePainter(Color.BLUE);
        CompoundPainter<Component> cp = new CompoundPainter<Component>(mp, pp);
        node.add(createInfoNode("panel w/ blue pinstripe fg, green matte bg", cp));
        
        mp = new MattePainter(Color.GREEN);
        RectanglePainter rp = new RectanglePainter(new Insets(20, 20, 20, 20), 
        		50, 50, 10, 10, true, Color.RED, 5, Color.RED.darker()
        	);
        pp = new PinstripePainter(Color.BLUE);
        cp = new CompoundPainter<Component>(mp, rp, pp);
        node.add(createInfoNode("panel, blue stripe fg, green bg, red rect comp", cp));
                
        rp = new RectanglePainter(20, 20, 5, Color.BLUE);
        TextPainter tp = new TextPainter("Some Text");
        cp = new CompoundPainter<Component>(rp, tp);
        node.add(createInfoNode("text on blue", cp));
        
        mp = new MattePainter(Color.BLACK);
        tp = new TextPainter("Neon");
        tp.setFont(new Font("SansSerif", Font.BOLD, 100));
        tp.setFillPaint(Color.BLACK);
        NeonBorderEffect effect = new NeonBorderEffect(Color.BLACK, Color.RED.brighter(), 10);
        effect.setBorderPosition(NeonBorderEffect.BorderPosition.Centered);
        tp.setAreaEffects(effect);
        cp = new CompoundPainter<Component>(mp, tp);
        node.add(createInfoNode("A Cool Logo", cp));
        
        mp = new MattePainter(Color.GRAY);
        ShapePainter sp = new ShapePainter(ShapeUtils.generatePolygon(30, 50, 45, true), Color.RED);
        sp.setStyle(ShapePainter.Style.FILLED);
        sp.setBorderPaint(Color.BLUE);
        ShadowPathEffect starShadow = new ShadowPathEffect();
        starShadow.setOffset(new Point(1,1));
        starShadow.setEffectWidth(5);
        sp.setAreaEffects(starShadow);
        tp = new TextPainter("Coming Soon!", new Font("SansSerif", Font.PLAIN, 12), Color.WHITE);
        cp = new CompoundPainter<Component>(mp, sp, tp);
        node.add(createInfoNode("Coming Soon badge", cp));

        return node;
    }

    /**
     * Create and return model for filter selection combobox.
     * 
     * @return ComboBoxModel<E> aModel
     */
    // @param <E> the type of the elements of this model: DisplayInfo<BufferedImageOp>
    private ComboBoxModel<DisplayInfo<BufferedImageOp>> createFilterList() {
        DefaultComboBoxModel<DisplayInfo<BufferedImageOp>> model = new DefaultComboBoxModel<DisplayInfo<BufferedImageOp>>();
//    private ComboBoxModel<E> createFilterList() {
//        DefaultComboBoxModel<E> model = new DefaultComboBoxModel<E>();
        model.addElement(new DisplayInfo<BufferedImageOp>("- none - ", null));
        model.addElement(new DisplayInfo<BufferedImageOp>(new AverageFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new BlockFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new BlurFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new BorderFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new BoxBlurFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new BumpFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ChromeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ContrastFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new CrystallizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DespeckleFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DiffuseFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DiffusionFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DilateFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DisplaceFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new DitherFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new EdgeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new EmbossFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new EqualizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ErodeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new FlareFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GainFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GammaFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GaussianFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GlintFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GlowFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new GrayFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new InvertFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new InvertAlphaFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new LensBlurFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new LevelsFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new LifeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MarbleFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MarbleTexFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MedianFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MinimumFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MirrorFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new MotionBlurFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new NoiseFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new OilFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new OpacityFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new OutlineFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new PointillizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new PosterizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new QuantizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new RaysFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ReduceNoiseFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new RippleFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ShadowFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SharpenFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SkeletonFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SmearFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SolarizeFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SparkleFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SphereFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new SwimFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new ThresholdFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new TwirlFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new UnsharpFilter()));
        model.addElement(new DisplayInfo<BufferedImageOp>(new WaterFilter()));
        return model;
    }

    /**
     * Create and returns model for effects selection combobox
     * @return
     */
    private ComboBoxModel<DisplayInfo<AreaEffect>> createAreaEffectsList() {
        DefaultComboBoxModel<DisplayInfo<AreaEffect>> model = new DefaultComboBoxModel<DisplayInfo<AreaEffect>>();
        model.addElement(new DisplayInfo<AreaEffect>(" - none -", null));
        model.addElement(new DisplayInfo<AreaEffect>(new GlowPathEffect()));
        model.addElement(new DisplayInfo<AreaEffect>(new InnerGlowPathEffect()));
        model.addElement(new DisplayInfo<AreaEffect>(new ShadowPathEffect()));
        model.addElement(new DisplayInfo<AreaEffect>(new InnerShadowPathEffect()));
        model.addElement(new DisplayInfo<AreaEffect>(new NeonBorderEffect()));
        return model;
    }
    
    /**
     * Controller of bindings to Painter properties.
     */
    // must not be private! 
    // private leads to: java.lang.IllegalAccessException: 
    // class org.jdesktop.beansbinding.BeanProperty cannot access a member of class org.jdesktop.swingx.demos.painter.PainterDemo$PainterControl with modifiers "public"
    public class PainterControl extends AbstractBean {
        private Painter<?> painter;
        private boolean alignEnabled;
        private boolean baseEnabled;
        private boolean areaEnabled;
        private BindingGroup alignGroup;
        private BindingGroup baseGroup;
        private BindingGroup areaGroup;

        public PainterControl() {
        	LOG.fine("ctor");
            StringValue effectInfo = DisplayValues.DISPLAY_INFO_DESCRIPTION;
            
            // effects
            effectBox.setRenderer(new DefaultListRenderer<>(effectInfo));
            effectBox.setModel(createAreaEffectsList());
            styleBox.setModel(new EnumComboBoxModel<Style>(Style.class));
            
            // base
            interpolationBox.setModel(new EnumComboBoxModel<Interpolation>(Interpolation.class));
            ComboBoxModel<DisplayInfo<BufferedImageOp>> comboBoxModel = createFilterList();
            filterBox.setModel(comboBoxModel);
            filterBox.setRenderer(new DefaultListRenderer<>(effectInfo));
            
            // layout data
            horizontalAlignmentBox.setModel(new EnumComboBoxModel<HorizontalAlignment>(HorizontalAlignment.class));            
            verticalAlignmentBox.setModel(new EnumComboBoxModel<VerticalAlignment>(VerticalAlignment.class));
        }

        /**
         * @return the areaEnabled
         */
        public boolean isAreaEnabled() {
            return areaEnabled;
        }
        
        /**
         * @param areaEnabled the areaEnabled to set
         */
        public void setAreaEnabled(boolean areaEnabled) {
            boolean old = isAreaEnabled();
            this.areaEnabled = areaEnabled;
            updateAreaBindings();
            firePropertyChange("areaEnabled", old, isAreaEnabled());
        }


        public boolean isBaseEnabled() {
            return baseEnabled;
        }
        
        public void setBaseEnabled(boolean baseEnabled) {
            boolean old = isBaseEnabled();
            this.baseEnabled = baseEnabled;
            updateBaseBindings();
            firePropertyChange("baseEnabled", old, isBaseEnabled());
        }
        

        public boolean isAlignEnabled() {
            return alignEnabled;
        }

        public void setAlignEnabled(boolean alignEnabled) {
            boolean old = isAlignEnabled();
            this.alignEnabled = alignEnabled;
            updateAlignBindings();
            firePropertyChange("alignEnabled", old, isAlignEnabled());
        }

        @SuppressWarnings("unchecked")
        private void updateAreaBindings() {
            if (areaEnabled) {
                areaGroup = new BindingGroup();
                areaGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        paintStretchedBox, BeanProperty.create("selected"),
                        painter, BeanProperty.create("paintStretched")));
                areaGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        styleBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("style")));
                
                Binding effectsBinding = (Bindings.createAutoBinding(UpdateStrategy.READ, 
                        effectBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("areaEffects")));
                // Converter<SV, TV> converter
                effectsBinding.setConverter(new DisplayInfoArrayConverter<AreaEffect>(AreaEffect.class));
                areaGroup.addBinding(effectsBinding);
                
                Binding borderWidthBinding = (Bindings.createAutoBinding(UpdateStrategy.READ, 
                        borderWidthSlider, BeanProperty.create("value"),
                        painter, BeanProperty.create("borderWidth")));
                borderWidthBinding.setConverter(PainterDemoUtils.SLIDER_TO_FLOAT);
                areaGroup.addBinding(borderWidthBinding);
                
                areaGroup.bind();
            }
        }

        @SuppressWarnings("unchecked")
        private void updateBaseBindings() {
            if (baseEnabled) {
                baseGroup = new BindingGroup();
                baseGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        interpolationBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("interpolation")));
                baseGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        visibleBox, BeanProperty.create("selected"),
                        painter, BeanProperty.create("visible")));
                baseGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        antialiasBox, BeanProperty.create("selected"),
                        painter, BeanProperty.create("antialiasing")));
                
                Binding filterBinding = (Bindings.createAutoBinding(UpdateStrategy.READ, 
                        filterBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("filters")));
                filterBinding.setConverter(new DisplayInfoArrayConverter<BufferedImageOp>(BufferedImageOp.class));
                baseGroup.addBinding(filterBinding);
                
                baseGroup.bind();             
            }
        }
        
        @SuppressWarnings("unchecked")
        private void updateAlignBindings() {
            if (alignEnabled) {
                alignGroup = new BindingGroup();
                alignGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        horizontalAlignmentBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("horizontalAlignment")));
                alignGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        verticalAlignmentBox, BeanProperty.create("selectedItem"),
                        painter, BeanProperty.create("verticalAlignment")));
                alignGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        fillHorizontal, BeanProperty.create("selected"),
                        painter, BeanProperty.create("fillHorizontal")));
                alignGroup.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                        fillVertical, BeanProperty.create("selected"),
                        painter, BeanProperty.create("fillVertical")));
                
                Binding insetBinding = (Bindings.createAutoBinding(UpdateStrategy.READ, 
                        insetSlider, BeanProperty.create("value"),
                        painter, BeanProperty.create("insets")));
                insetBinding.setConverter(PainterDemoUtils.SLIDER_TO_INSETS);
                alignGroup.addBinding(insetBinding);
                
                alignGroup.bind();
            }
        }

        public Painter<?> getPainter() {
            return painter;
        }

        public void setPainter(Painter<?> painter) {
        	LOG.fine("------------- Painter<?>:"+painter);
            releaseBaseBindings();
            releaseAlignBindings();
            releaseAreaBindings();
            this.painter = painter;
            setBaseEnabled(painter instanceof AbstractPainter<?>);
            setAlignEnabled(painter instanceof AbstractLayoutPainter<?>);
            setAreaEnabled(painter instanceof AbstractAreaPainter<?>);
        }

        private void releaseAreaBindings() {
            if (areaGroup !=  null) {
                areaGroup.unbind();
            }
            styleBox.setSelectedItem(Style.BOTH);
            effectBox.setSelectedIndex(0);
            paintStretchedBox.setSelected(false);
        }

        private void releaseBaseBindings() {
            if (baseGroup != null) {
                baseGroup.unbind();
            }
            interpolationBox.setSelectedItem(Interpolation.NearestNeighbor);
            filterBox.setSelectedIndex(0);
            visibleBox.setSelected(true);
            antialiasBox.setSelected(true);
        }

        private void releaseAlignBindings() {
            if (alignGroup != null) {
                alignGroup.unbind();
            }
            // PENDING JW: reset defaults ... wrong place here?
            verticalAlignmentBox.setSelectedItem(VerticalAlignment.CENTER);
            horizontalAlignmentBox.setSelectedItem(HorizontalAlignment.CENTER);
            insetSlider.setValue(0);
            fillHorizontal.setSelected(false);
            fillHorizontal.setSelected(false);
        }
      
    }
    

    /* some notes on jgoodies layout and builder (used in create*Panel):
     * dlu   == dialog units
     * r:d:n == RIGHT:DEFAULT:NONE == [columnAlignment:] size [:resizeBehavior]
     * f:d:n == FILL:DEFAULT:NONE == [columnAlignment:] size [:resizeBehavior]
     * t:d:n == TOP:DEFAULT:NONE == rowSpec
     * c:d:n == CENTER:DEFAULT:NONE == rowSpec
     * @see jgoodies-forms whitepaper.pdf
     */

    /**
     * PainterPropertiesPanel consists of
     * 	basePainterControlPanel + layoutPainterControlPanel + areaPainterControlPanel
     * 
     * @return properties panel
     */
    private JXPanel createPainterPropertiesPanel() {
        basePainterControlPanel = createBasePainterControlPanel();
        layoutPainterControlPanel = createLayoutPainterControl();
        areaPainterControlPanel = createAreaPainterControlPanel();
        
        JXPanel properties = new JXPanel(new GridLayout(3, 1, 1, 1));
        properties.add(basePainterControlPanel);
        properties.add(layoutPainterControlPanel);
        properties.add(areaPainterControlPanel);
        return properties;
    }

    /**
     * @return control collapsiblePane
     */
    private JXCollapsiblePane createBasePainterControlPanel() {
        JXCollapsiblePane pane = new JXCollapsiblePane();
        // jgoodies layout and builder:
        FormLayout formLayout = new FormLayout(
            "5dlu, r:d:n, l:4dlu:n, f:d:n" // encodedColumnSpecs 2gaps+2cols: gap,col,gap,col
            // 5 rows (+ gaps):
            , "c:d:n" // baseSeparator
            + ", t:4dlu:n, c:d:n" // gap, interpolation: label+comboBox
            + ", t:4dlu:n, c:d:n" // gap, filter
            + ", t:4dlu:n, c:d:n" // gap, visibleBox
            + ", t:4dlu:n, c:d:n" // gap, antialiasBox
        );
        PanelBuilder builder = new PanelBuilder(formLayout, pane);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        baseSeparator = new JXTitledSeparator();
        baseSeparator.setName("basePainterSeparator");
        baseSeparator.setTitle(getBundleString("basePainterSeparator.title"));
        builder.add(baseSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        interpolationBox = new JXComboBox<Interpolation>();
        interpolationBox.setName("interpolationBox");

        JLabel interpolationLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                interpolationBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        interpolationLabel.setName("interpolationLabel");
        interpolationLabel.setText(getBundleString("interpolationLabel.text"));
        LabelHandler.bindLabelFor(interpolationLabel, interpolationBox);
        
        filterBox = new JXComboBox<DisplayInfo<BufferedImageOp>>();
        filterBox.setName("filterBox");
        
        JLabel filterLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                filterBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        filterLabel.setName("filterLabel");
        filterLabel.setText(getBundleString("filterLabel.text"));
        LabelHandler.bindLabelFor(filterLabel, filterBox);
        
        
        visibleBox = new JCheckBox();
        visibleBox.setName("visibleBox");
        visibleBox.setText(getBundleString("visibleBox.text"));
        
        antialiasBox = new JCheckBox();
        antialiasBox.setName("antialiasBox");
        antialiasBox.setText(getBundleString("antialiasBox.text"));
        
        builder.add(visibleBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        builder.add(antialiasBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow +=2;
        
        return pane;
    }

    /**
     * @return control collapsiblePane
     */
    private JXCollapsiblePane createLayoutPainterControl() {
        JXCollapsiblePane pane = new JXCollapsiblePane();//new JXPanel();
        // jgoodies layout and builder:
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:n, l:4dlu:n, f:d:n", // encodedColumnSpecs 3gaps+3cols: gap,col,gap,col,gap,col
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"
                ); // rows
        PanelBuilder builder = new PanelBuilder(formLayout, pane);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
  
        layoutSeparator = new JXTitledSeparator();
        layoutSeparator.setName("layoutPainterSeparator");
        layoutSeparator.setTitle(getBundleString("layoutPainterSeparator.title"));
        builder.add(layoutSeparator, cc.xywh(1, 1, 6, 1));

        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        
        horizontalAlignmentBox = new JXComboBox<HorizontalAlignment>();     
        verticalAlignmentBox = new JXComboBox<VerticalAlignment>();

        insetSlider = new JSlider(0, 100, 0);
        insetSlider.setPaintLabels(true);
        insetSlider.setPaintTicks(true);
        insetSlider.setMajorTickSpacing(20);
        
        fillHorizontal = new JCheckBox();
        fillHorizontal.setName("fillHorizontalBox");
        fillHorizontal.setText(getBundleString("fillHorizontalBox.text", fillHorizontal));
        
        fillVertical = new JCheckBox();
        fillVertical.setName("fillVerticalBox");
        fillVertical.setText(getBundleString("fillVerticalBox.text", fillVertical));

        JLabel horAlign = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                horizontalAlignmentBox, cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;
        horAlign.setName("horizontalAlignmentLabel");
        horAlign.setText(getBundleString("horizontalAlignmentLabel.text", horAlign));
        LabelHandler.bindLabelFor(horAlign, horizontalAlignmentBox);

        JLabel vertAlign = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                verticalAlignmentBox, cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;
        vertAlign.setName("verticalAlignmentLabel");
        vertAlign.setText(getBundleString("verticalAlignmentLabel.text", vertAlign));
        LabelHandler.bindLabelFor(vertAlign, verticalAlignmentBox);

        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), insetSlider,
                cc.xywh(widgetColumn, currentRow, 3, 1));
        currentRow += 2;
        insets.setName("insetsLabel");
        insets.setText(getBundleString("insetsLabel.text", insets));
        LabelHandler.bindLabelFor(insets, insetSlider);
        
        JLabel fillLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                fillHorizontal, cc.xywh(widgetColumn, currentRow, 1, 1));
        fillLabel.setName("fillLabel");
        fillLabel.setText(getBundleString("fillLabel.text", fillLabel));
        LabelHandler.bindLabelFor(fillLabel, fillHorizontal);
        builder.add(fillVertical, cc.xywh(widgetColumn + 2, currentRow, 1, 1));
        currentRow += 2;
        return pane;
    }

    /**
     * @return control collapsiblePane
     */
    private JXCollapsiblePane createAreaPainterControlPanel() {
        JXCollapsiblePane pane = new JXCollapsiblePane();
        // jgoodies layout and builder:
        FormLayout formLayout = new FormLayout(
                "5dlu, r:d:n, l:4dlu:n, f:d:n", // encodedColumnSpecs 2gap+2cols: gap,col,gap,col 
                "c:d:n " +
                ", t:4dlu:n, c:d:n " +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n" +
                ", t:4dlu:n, c:d:n"	// encodedRowSpecs 5rows+4gaps
        );
        PanelBuilder builder = new PanelBuilder(formLayout, pane);
        builder.setBorder(Borders.DLU4_BORDER);
        CellConstraints cl = new CellConstraints();
        CellConstraints cc = new CellConstraints();
        
        areaSeparator = new JXTitledSeparator();
        areaSeparator.setName("areaPainterSeparator");
        areaSeparator.setTitle(getBundleString("areaPainterSeparator.title"));
        builder.add(areaSeparator, cc.xywh(1, 1, 4, 1));
        
        int labelColumn = 2;
        int widgetColumn = labelColumn + 2;
        int currentRow = 3;
        styleBox = new JXComboBox<Style>();
        styleBox.setName("styleBox");

        JLabel styleBoxLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                styleBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        styleBoxLabel.setName("styleBoxLabel");
        styleBoxLabel.setText(getBundleString("styleBoxLabel.text", styleBoxLabel));
        LabelHandler.bindLabelFor(styleBoxLabel, styleBox);
        
        effectBox = new JXComboBox<DisplayInfo<AreaEffect>>();
        effectBox.setName("areaEffectBox");
        
        JLabel effectLabel = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1),
                effectBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        effectLabel.setName("effectLabel");
        effectLabel.setText(getBundleString("effectLabel.text", effectLabel));
        LabelHandler.bindLabelFor(effectLabel, effectBox);
        
        borderWidthSlider = new JSlider(0, 100, 0);
        borderWidthSlider.setPaintLabels(true);
        borderWidthSlider.setPaintTicks(true);
        borderWidthSlider.setMajorTickSpacing(50);
        
        JLabel insets = builder.addLabel("", cl.xywh(labelColumn, currentRow, 1, 1), borderWidthSlider,
                cc.xywh(widgetColumn, currentRow, 1, 1));
        insets.setName("borderWidthLabel");
        insets.setText(getBundleString("borderWidthLabel.text", insets));
        LabelHandler.bindLabelFor(insets, borderWidthSlider);
        currentRow += 2;
        
        paintStretchedBox = new JCheckBox();
        paintStretchedBox.setName("paintStretchedBox");
        paintStretchedBox.setText(getBundleString("paintStretchedBox.text", paintStretchedBox));
        builder.add(paintStretchedBox, cc.xywh(widgetColumn, currentRow, 1, 1));
        currentRow += 2;
        
        return pane;
    }

}