package org.jdesktop.swingx.demos.formattedtext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.prompt.BuddySupport.Position;
import org.jdesktop.swingx.prompt.PromptSupport;

import swingset.AbstractDemo;

/**
 * A demo to compare JFormattedTextField and {@code JXFormattedTextField}.
 *
 * @author EUG https://github.com/homebeaver
 */
public class FormattedTextDemo extends AbstractDemo {

	private static final long serialVersionUID = 1027611120004098504L;
	private static final Logger LOG = Logger.getLogger(FormattedTextDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates JFormattedTextField and JXFormattedTextField.";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new FormattedTextDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    JSplitPane splitPane = null;
    JXPanel left;
    JXPanel right;
    JXFormattedTextField tfDatum;
    JXFormattedTextField tfBetrag;
    
    NumberFormat nf = null;
    private Number parse(String source) {
    	if(nf==null) {
        	nf = NumberFormat.getNumberInstance();
    	}
    	try {
    		DecimalFormat df = (DecimalFormat)nf;
    		df.setParseIntegerOnly(false);
    		df.setParseBigDecimal(true);
    		Number num = df.parse(source);
        	LOG.info("result "+num.getClass() +":"+num + " :"+source); // TODO BUG
        	// wg. Locale DE wird "22,3" zu 22.3 geparst, was korrekt ist, ABER "22.3" als Long 223 ist ein BUG
    	} catch (ParseException pe) {
    		LOG.warning(pe.getMessage()+" at "+pe.getErrorOffset());
    	}
    	return null;
    }
    
    private Number parseXXX(String source) {
    	if(nf==null) {
        	nf = NumberFormat.getNumberInstance();
//        	nf = NumberFormat.getCurrencyInstance(); // TODO parst nichts !!!!
//        	This is equivalent to calling 
//        	getCurrencyInstance(Locale.getDefault(Locale.Category.FORMAT));
        	LOG.info("+++current value of the default locale:"+Locale.getDefault(Locale.Category.FORMAT)
        		+ " CurrencyInstance="+nf + " Currency="+nf.getCurrency()
        	);     	
    	}
//    	try {
    		DecimalFormat df = (DecimalFormat)nf;
    		df.setParseIntegerOnly(false);
    		//df.setMaximumFractionDigits(3);
    		ParsePosition pp = new ParsePosition(0);
    		Number num = df.parse(source, pp);
//    		Number num = df.parse(source);
        	LOG.info("result Number:"+num + " ParsePosition="+pp + " :"+source);
//    	} catch (ParseException pe) {
//    		LOG.warning(pe.getMessage()+" at "+pe.getErrorOffset());
//    	}
    	return null;
    }

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public FormattedTextDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPane(), createRightPane());
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        splitPane.setDividerLocation(300);

        super.add(splitPane, BorderLayout.CENTER);
        super.setBackground(Color.black);
    }

    @Override
	public JXPanel getControlPane() {
//		return emptyControlPane();
    	JXPanel panel = new JXPanel(new BorderLayout());
    	
        JButton foregroundColor = new JButton(new AbstractAction("Open as a dialog") {
            @Override
            public void actionPerformed(ActionEvent e) {
            	LOG.info("ActionEvent e:"+e);
//            	JXPanel panel = new JXPanel();
//            	panel.add(tfBetrag);
//            	tfBetrag.setValue("nix");
//            	PromptSupport.init("promptText", Color.YELLOW, Color.BLACK, tfBetrag);
//            	PromptSupport.setFontStyle(Font.BOLD+Font.ITALIC, tfBetrag);
            	tfBetrag.setPromptFontStyle(Font.ITALIC);
            	tfBetrag.addBuddy(new JLabel("rechts"), Position.RIGHT);
            	LOG.info("??? tfBetrag.getLayout():"+tfBetrag.getLayout());
//            	tfBetrag.getLayout().getClass() muss BuddyLayoutAndBorder sein
//                Color color = JColorChooser.showDialog(PromptSupportDemo.this, "Foreground Color", PromptSupport.getForeground(textField));
//                
//                if (color != null) {
//                    PromptSupport.setForeground(color, textField);
//                    JXTipOfTheDay dialog = new JXTipOfTheDay(model);
//                    dialog.setCurrentTip(0);
//                    dialog.showDialog(totd);
//               }
//            	panel.getUI().c.createDialog(parentComponent, choice);
            	//panel.show ???
            	
            	tfDatum.setPrompt("Datum");
            	tfDatum.setForeground(Color.BLUE);
            	tfDatum.setBackground(Color.ORANGE);
            	tfDatum.addBuddy(new JLabel("links"), Position.LEFT);
            	
//            	right.revalidate();
            }
        });
        panel.add(foregroundColor);

    	return panel;
	}

    private JComponent createRightPane() {
        right = new JXPanel(new VerticalLayout(3));
        right.setName("right");
        
        JXTitledSeparator sep = new JXTitledSeparator();
        sep.setTitle("JXFormattedTextField:");
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(sep);
        sep = new JXTitledSeparator();
        sep.setTitle("HIGHLIGHT_PROMPT");
    	/* FocusBehavior:
    	 * Determines how the {@link JTextComponent} is rendered 
    	 * when focused and no text is present.
    	 */
       right.add(sep);

        JXPanel rightHP = new JXPanel();
        right.add(rightHP);
        
		JLabel labelDatum = new JLabel("Datum");
		JLabel labelBetrag = new JLabel("Betrag");

		tfDatum = new JXFormattedTextField();
		tfDatum.setValue(new Date()); // heute
		// Highlight the prompt text as it would be selected:
		tfDatum.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
		// Keep the prompt text visible:
//		tfDatum.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT);
		tfDatum.setColumns(12);
		tfDatum.setPrompt("Datum hier eingeben");
		tfDatum.setPromptForeground(Color.BLUE);
		tfDatum.setPromptBackground(Color.ORANGE);
		tfDatum.setPromptFontStyle(Font.BOLD+Font.ITALIC);
		
		/* JXFormattedTextField Besonderheiten: prompt as tooltip, mit Foreground-, Background-Color
		   setzten mit setter wie oben:
		tfBetrag = new JXFormattedTextField();
		tfBetrag.setPrompt("Euro Betrag");
		tfBetrag.setPromptForeground(Color.BLUE);
		tfBetrag.setPromptBackground(Color.ORANGE);
		   // die weiteren PromptFontStyle, OuterMargin, Buddy, Buddy-Gap
		   oder direkt im ctor:
		tfBetrag = new JXFormattedTextField("Euro Betrag", Color.BLUE, Color.ORANGE);
		 */
		tfBetrag = new JXFormattedTextField(NumberFormat.getCurrencyInstance());
		tfBetrag.setPrompt("Euro Betrag");
		tfBetrag.setPromptFontStyle(Font.BOLD+Font.ITALIC);
		tfBetrag.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
//		tfBetrag.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT);	
//		tfBetrag.setOuterMargin(Insets margin)
		LOG.info("tfBetrag.getFocusBehavior()="+tfBetrag.getFocusBehavior());
		
		NumberFormat betrag = NumberFormat.getCurrencyInstance();
//		if(betrag instanceof DecimalFormat) {
//			DecimalFormat df = (DecimalFormat)betrag; // DecimalFormat extends NumberFormat
//			LOG.info("DecimalFormat betrag:"+df.format(0l)); // 0,00 €	
//			tfBetrag.setValue(df.format(0l)); 
//		}
		LOG.info("betrag:"+betrag.format(4.3f)); // 4,30 €
//		tfBetrag.setValue(betrag); // exception Cannot format given Object as a Number
/*
Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Cannot format given Object as a Number
	at java.base/java.text.DecimalFormat.format(DecimalFormat.java:515)
	at java.base/java.text.Format.format(Format.java:159)
	at java.desktop/javax.swing.text.InternationalFormatter.valueToString(InternationalFormatter.java:309)
	at java.desktop/javax.swing.JFormattedTextField$AbstractFormatter.install(JFormattedTextField.java:992)
	at java.desktop/javax.swing.text.DefaultFormatter.install(DefaultFormatter.java:125)
	at java.desktop/javax.swing.text.InternationalFormatter.install(InternationalFormatter.java:286)
	at java.desktop/javax.swing.JFormattedTextField.setFormatter(JFormattedTextField.java:498)
	at java.desktop/javax.swing.JFormattedTextField.setValue(JFormattedTextField.java:822)
	at java.desktop/javax.swing.JFormattedTextField.setValue(JFormattedTextField.java:535)
	at org.jdesktop.swingx.demos.formattedtext.FormattedTextDemo.createRightPane(FormattedTextDemo.java:105)
 */
		//tfBetrag.setValue(betrag, true, true); // not visible
		//betrag.format(4.3f); ist aber String!!!!!!!!
//		tfBetrag.setValue(betrag.format(4.3f));
		tfBetrag.setColumns(10);
		tfBetrag.addBuddy(new JLabel("links"), Position.LEFT);
		tfBetrag.addBuddy(new JLabel("€"), Position.RIGHT);
//		tfBetrag.addActionListener(ae -> {
//			LOG.info("ActionEvent "+ae);
//		});
//		tfBetrag.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ae) {
//            	LOG.info("ActionEvent "+ae);
//                String  value = ((JTextField)ae.getSource()).getText();
//                int newSize;
//
////                try {
////                    newSize = Integer.parseInt(value);
////                } catch (Exception ex) {
////                    newSize = -1;
////                }
////                if(newSize > 0) {
////                    splitPane.setDividerSize(newSize);
////                    setMinimumSize();
////                } else {
////                    JOptionPane.showMessageDialog(splitPane,
////                                                  getBundleString("invalid_divider_size"),
////                                                  getBundleString("error"),
////                                                  JOptionPane.ERROR_MESSAGE);
////                }
//            }
//        });

/*

The following code snippetshows a potential implementation of such an InputVerifier: 
 public class FormattedTextFieldVerifier extends InputVerifier {
     public boolean verify(JComponent input) {
         if (input instanceof JFormattedTextField) {
             JFormattedTextField ftf = (JFormattedTextField)input;
             AbstractFormatter formatter = ftf.getFormatter();
             if (formatter != null) {
                 String text = ftf.getText();
                 try {
                      formatter.stringToValue(text);
                      return true;
                  } catch (ParseException pe) {
                      return false;
                  }
              }
          }
          return true;
      }
      public boolean shouldYieldFocus(JComponent input) {
          return verify(input);
      }
  }



 */
		tfBetrag.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
            	String text = tfBetrag.getText();
            	LOG.info("DocumentEvent "+e + " :"+text);
            	parse(text);
            }
            
            @Override
            // there was an insert into the document
            public void insertUpdate(DocumentEvent e) {
//            	DecimalFormat df;
//            	NumberFormat nf = NumberFormat.getCurrencyInstance();
////            	This is equivalent to calling 
////            	getCurrencyInstance(Locale.getDefault(Locale.Category.FORMAT)).
//            	LOG.info(" current value of the default locale:"+Locale.getDefault(Locale.Category.FORMAT));
////            	AbstractFormatter formatter = tfBetrag.getFormatter();
//            	String text = tfBetrag.getText();
//            	LOG.info("DocumentEvent "+e + " :"+text);
//            	try {
//            		Number num = nf.parse(text);
//            		//nf.setParseIntegerOnly(false);
//            		//nf.setMaximumFractionDigits(3);
////            		Object o = formatter.stringToValue(text);
//                	LOG.info("DocumentEvent Number:"+num + " :"+text);
//            	} catch (ParseException pe) {
//            		LOG.warning(pe.getMessage()+" at "+pe.getErrorOffset());
//            	}
            	String text = tfBetrag.getText();
            	LOG.info("DocumentEvent "+e + " :"+text);
            	parse(text);
            }
            
            @Override
            // attribute or set of attributes changed
            public void changedUpdate(DocumentEvent e) {
            	String text = tfBetrag.getText();
            	LOG.info("DocumentEvent "+e + " :"+text);
            	parse(text);
            }
        });


		rightHP.add(labelDatum);
		rightHP.add(tfDatum);
		rightHP.add(labelBetrag);
		rightHP.add(tfBetrag);

        return right;
    }

	/*
 aus https://java-tutorial.org/jformattedtextfield.html
 
 Wir haben in unserem Beispiel zwei JFormattedTextFields erzeugt.
  
 Das erste dient zur Eingabe eines Datums. 
 Dem Konstruktor wird als Wert ein Objekt der Klasse Date übergeben. 
 Da keine anderen Werte vorgegeben wurden, wird das Date-Objekt mit dem aktuellen Datum initialisiert. 
 Die zuständige DefaultFormatterFactory ermittelt anhand des Datentyps die passende Formatierungsklasse, 
 in diesem Fall DateFormatter und setzt eine Instanz dieser Klasse als zuständigen Formatter für das Textfeld.

 Das zweite Textfeld soll zur Eingabe eines Wertes im Währungsformat dienen. 
 Dem Konstruktor wird eine Instanz der Formatklasse NumberFormat übergeben.

 welche Formate werden erkannt? ctor JFormattedTextField(Object type)
	type instanceof DateFormat ...
	type instanceof NumberFormat ...  // Beispiel
	type instanceof Format ...
	type instanceof Date ... // Beispiel
	type instanceof Number ...

	 */
    private JComponent createLeftPane() {
    	left = new JXPanel(new VerticalLayout(3));
    	left.setName("left");
    	
        JXTitledSeparator sep = new JXTitledSeparator();
        sep.setTitle("JFormattedTextField:");
        sep.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(sep);
        
        JXPanel leftBsp = new JXPanel();
        left.add(leftBsp);
		// Labels für die Textfelder werden erzeugt
		JLabel labelDatum = new JLabel("Datum");
		JLabel labelBetrag = new JLabel("Betrag");
		
		/* Textfeld für die Eingabe eines Datums

 Dem Konstruktor wird als Wert ein Objekt der Klasse Date übergeben. 
 Da keine anderen Werte vorgegeben wurden, wird das Date-Objekt mit dem aktuellen Datum initialisiert. 
 Die zuständige DefaultFormatterFactory ermittelt anhand des Datentyps die passende Formatierungsklasse, 
 in diesem Fall DateFormatter und setzt eine Instanz dieser Klasse als zuständigen Formatter für das Textfeld.

		 */
		JFormattedTextField tfDatum = new JFormattedTextField(new Date());

		/* Textfeld für die Eingabe von Zahlen im Währungsformat

 Dem Konstruktor wird eine Instanz der Formatklasse NumberFormat übergeben.

		 */
		JFormattedTextField tfBetrag = new JFormattedTextField(NumberFormat.getCurrencyInstance());

		// Wert für das Textfeld wird gesetzt
		tfBetrag.setValue(4.30);

		tfBetrag.setColumns(5);

		leftBsp.add(labelDatum);
		leftBsp.add(tfDatum);
		leftBsp.add(labelBetrag);
		leftBsp.add(tfBetrag);

        return left;
    }
}
