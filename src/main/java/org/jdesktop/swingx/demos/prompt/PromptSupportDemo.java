/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.prompt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.binding.DisplayInfo;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.ShapePainter;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jdesktop.swingx.prompt.PromptSupport.FocusBehavior;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ShapeUtils;
import org.jdesktop.swingxset.util.DisplayValues;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import swingset.AbstractDemo;

/**
 * A demo for {@code PromptSupport}.
 *
 * @author Karl George Schaefer
 * @author Peter Weishapl (original prompt demos)
 * @author EUG https://github.com/homebeaver (reorg)
 */
//@DemoProperties(
//    value = "PromptSupport Demo",
//    category = "Functionality",
//    description = "Demonstrates PromptSupport, a prompting decoration for text components.",
//    sourceFiles = {
//        "org/jdesktop/swingx/demos/prompt/PromptSupportDemo.java",
//        "org/jdesktop/swingx/demos/prompt/resources/PromptSupportDemo.properties",
//        "org/jdesktop/swingx/demos/prompt/resources/images/PromptSupportDemo.png"
//    }
//)
public class PromptSupportDemo extends AbstractDemo {
	
	private static final long serialVersionUID = -2286660834393833047L;
	private static final Logger LOG = Logger.getLogger(PromptSupportDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates PromptSupport, a prompting decoration for text components.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new PromptSupportDemo(controller);
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

	private JTextField textField;
	// Controller:
    private JComboBox focusCombo;
    private JTextField promptText;
    private JComboBox backgroundPainter;
    private JComboBox fontStyle;

    /**
     * PromptSupportDemo Constructor
     */
    public PromptSupportDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        textField = new JTextField(20); // 20 columns
        JPanel p = new JPanel();
        p.add(textField);
        add(p);
    }
 

    @Override
	public JXPanel getControlPane() {
    	JXPanel panel = new JXPanel();

        panel.setBorder(new TitledBorder("Prompt Controls"));
		// jgoodies layout and builder:
        panel.setLayout(new FormLayout(  		
            new ColumnSpec[] { // 2 columns + glue
                FormFactory.GLUE_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,	// 1st column
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                FormFactory.PREF_COLSPEC,		// 2nd column
                FormFactory.GLUE_COLSPEC,
            },
            new RowSpec[] { // 5 rows + glue
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, 
                FormFactory.UNRELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC 
            }
        ));
        
        CellConstraints cc = new CellConstraints();
        
        JLabel label = new JLabel("Focus Behavior:");
        panel.add(label, cc.rc(1, 2));
        
        focusCombo = new JComboBox(new EnumComboBoxModel<FocusBehavior>(FocusBehavior.class));
        focusCombo.setRenderer(new DefaultListRenderer(DisplayValues.TITLE_WORDS_UNDERSCORE));
        panel.add(focusCombo, cc.rc(1, 4));
        
        label = new JLabel("Prompt Text:");
        panel.add(label, cc.rc(3, 2));
        
        promptText = new JTextField();
        promptText.setText("Prompt Text");
        panel.add(promptText, cc.rc(3, 4, "c f"));
        
        JButton foregroundColor = new JButton(new AbstractAction("Select Foreground") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(PromptSupportDemo.this, "Foreground Color", PromptSupport.getForeground(textField));
                
                if (color != null) {
                    PromptSupport.setForeground(color, textField);
                }
            }
        });
        panel.add(foregroundColor, cc.rc(5, 2));
        
        JButton backgroundColor = new JButton(new AbstractAction("Select Background") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(PromptSupportDemo.this, "Background Color", PromptSupport.getBackground(textField));
                
                if (color != null) {
                    PromptSupport.setBackground(color, textField);
                }
            }
        });
        panel.add(backgroundColor, cc.rc(5, 4));
        
        label = new JLabel("Painter:");
        panel.add(label, cc.rc(7, 2));
        
        backgroundPainter = new JComboBox(new ListComboBoxModel<DisplayInfo<Painter<?>>>(getPainters()));
        backgroundPainter.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        panel.add(backgroundPainter, cc.rc(7, 4));
        
        label = new JLabel("Font Style:");
        panel.add(label, cc.rc(9, 2));
        
        fontStyle = new JComboBox(new ListComboBoxModel<DisplayInfo<Integer>>(getFontStyles()));
        fontStyle.setRenderer(new DefaultListRenderer(DisplayValues.DISPLAY_INFO_DESCRIPTION));
        panel.add(fontStyle, cc.rc(9, 4));

        bind();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                focusCombo.requestFocusInWindow();
            }
        });

        return panel;
	}

    private void bind() {
        focusCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PromptSupport.setFocusBehavior((FocusBehavior) focusCombo.getSelectedItem(), textField);
            }
        });
        
        promptText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                PromptSupport.setPrompt(promptText.getText(), textField);
            }
        });
        PromptSupport.setPrompt(promptText.getText(), textField);
        
        backgroundPainter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PromptSupport.setBackgroundPainter(((DisplayInfo<Painter>) backgroundPainter.getSelectedItem()).getValue(), textField);
            }
        });
        
        fontStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PromptSupport.setFontStyle(((DisplayInfo<Integer>) fontStyle.getSelectedItem()).getValue(), textField);
            }
        });
    }
        
    private List<DisplayInfo<Painter<?>>> getPainters() {
        List<DisplayInfo<Painter<?>>> painters = new ArrayList<DisplayInfo<Painter<?>>>();
        
        painters.add(new DisplayInfo<Painter<?>>("None", null));
        painters.add(new DisplayInfo<Painter<?>>("Checkered", new MattePainter(PaintUtils.getCheckerPaint(new Color(0, 0, 0, 0), new Color(33, 33, 128), 20))));
        painters.add(new DisplayInfo<Painter<?>>("Gradient", new MattePainter(PaintUtils.AERITH, true)));
        painters.add(new DisplayInfo<Painter<?>>("Star Shape", new ShapePainter(ShapeUtils.generatePolygon(5, 10, 5, true), Color.GREEN)));
        
        return painters;
    }
    
    private List<DisplayInfo<Integer>> getFontStyles() {
        List<DisplayInfo<Integer>> fontStyles = new ArrayList<DisplayInfo<Integer>>();
        
        fontStyles.add(new DisplayInfo<Integer>("Normal", Font.PLAIN));
        fontStyles.add(new DisplayInfo<Integer>("Bold", Font.BOLD));
        fontStyles.add(new DisplayInfo<Integer>("Italic", Font.ITALIC));
        fontStyles.add(new DisplayInfo<Integer>("Bold Italic", Font.BOLD | Font.ITALIC));
        
        return fontStyles;
    }
    
}