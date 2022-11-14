package org.jdesktop.swingx.demos.formattedtext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.text.DateFormatter;

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
        SwingUtilities.invokeLater( () -> {
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
    	});
    }

    JSplitPane splitPane = null;
    JXPanel left;
    JXPanel right;
    JXFormattedTextField tfDatum;
    JXFormattedTextField tfBetrag;
    double betrag = 0;
    
    NumberFormat nf = null;

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
		return emptyControlPane();
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

        JXPanel rightHP = new JXPanel(); // HP == HIGHLIGHT_PROMPT
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
		
//		NumberFormat betrag = NumberFormat.getCurrencyInstance();
//		if(betrag instanceof DecimalFormat) {
//			DecimalFormat df = (DecimalFormat)betrag; // DecimalFormat extends NumberFormat
//			LOG.info("DecimalFormat betrag:"+df.format(0l)); // 0,00 €	
//			tfBetrag.setValue(df.format(0l)); 
//		}
//		LOG.info("betrag:"+betrag.format(4.3f)); // 4,30 €
//		tfBetrag.setValue(betrag); // exception Cannot format given Object as a Number
		tfBetrag.setValue(betrag);
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
		tfBetrag.addPropertyChangeListener(pce -> {
        	LOG.info("tfBetrag PropertyChangeEvent:"+pce);
        	String s =tfBetrag.getText(); // String javax.swing.text.JTextComponent.getText()
        	Object o = tfBetrag.getValue(); // Object javax.swing.JFormattedTextField.getValue()
        	LOG.info("tfBetrag Value:"+o + " String:"+s);
        	if(nf==null) {
//        		nf = NumberFormat.getNumberInstance();
        		nf = NumberFormat.getCurrencyInstance();
        	}
    		DecimalFormat df = (DecimalFormat)nf;
        	if(s.length()>0) try {
				Number n = df.parse(s);
				LOG.info("tfBetrag Number:"+n + " String:"+s);
//	    		Number num = df.parse(source);
//	        	LOG.info("result "+num.getClass() +":"+num + " :"+source); // TODO BUG, denn
//	        	// wg. Locale DE wird "22,3" zu 22.3 geparst, was korrekt ist, 
				// ABER "22.3" als Long 223 ist ein BUG
				tfBetrag.setValue(n);
			} catch (ParseException e) {
				int eo = e.getErrorOffset();
				LOG.warning("ErrorOffset="+eo);
				e.printStackTrace();
				tfBetrag.setValue(0);
				if(eo>0) {
					df = (DecimalFormat)NumberFormat.getNumberInstance();
					Number num = df.parse(s.substring(0, eo), new ParsePosition(0));
					LOG.info("result "+num.getClass() +":"+num + " :"+s);
					tfBetrag.setValue(num);
				}
			}
        });

		rightHP.add(labelDatum);
		rightHP.add(tfDatum);
		rightHP.add(labelBetrag);
		rightHP.add(tfBetrag);
       
        sep = new JXTitledSeparator();
        sep.setTitle("SHOW_PROMPT");
        right.add(sep);
        // SHOW_PROMPT : Keep the prompt text visible:
        right.add(createRightFB(PromptSupport.FocusBehavior.SHOW_PROMPT, Locale.US));
        
        sep = new JXTitledSeparator();
        sep.setTitle("HIDE_PROMPT");
        right.add(sep);
        // HIDE_PROMPT : Hide the prompt text: TODO BUG, denn Prompt wird gezeigt
        right.add(createRightFB(PromptSupport.FocusBehavior.HIDE_PROMPT, Locale.UK));
        
        return right;
    }
    
    private JComponent createRightFB(PromptSupport.FocusBehavior focusBehavior, Locale l) {
        JXPanel panel = new JXPanel();

		String localcurrencysymbol = "$";
		String localcurrencycode = "USD";

		JLabel labelDatum = new JLabel("Datum");
		JLabel labelBetrag = new JLabel("Betrag");

		JXFormattedTextField tfDatum = new JXFormattedTextField(new Date()); // heute
		tfDatum.setFocusBehavior(focusBehavior);
		tfDatum.setColumns(12);
		tfDatum.setPrompt("Datum hier eingeben");
		tfDatum.setPromptForeground(Color.BLUE);
		tfDatum.setPromptBackground(Color.ORANGE);
		tfDatum.setPromptFontStyle(Font.BOLD+Font.ITALIC);
		tfDatum.setValue(null);
		tfDatum.addPropertyChangeListener(pce -> {
        	LOG.info("tfDatum PropertyChangeEvent:"+pce);
        	
        	/* String javax.swing.text.JTextComponent.getText()
        	 * Returns the text contained in this TextComponent.
        	 * If the underlying document is null, will give a NullPointerException.
        	 * Note that text is not a bound property, so no PropertyChangeEvent is fired when it changes. 
        	 * To listen for changes to the text, use DocumentListener.
        	 */
        	String s = tfDatum.getText();
        	
        	/* Object javax.swing.JFormattedTextField.getValue()
        	 * Returns the last valid value. 
        	 * Based on the editing policy oft he AbstractFormatter this may not return the current value. 
        	 * The currently edited value can be obtained by invoking commitEdit followed by getValue.
        	 */
        	Object o = tfDatum.getValue();
        	LOG.info("tfDatum Value:"+o + ", Text String:"+s + (s.isEmpty() ? "(empty) " : ", ") 
        			+ "formatter:"+tfDatum.getFormatter());
        	
        	if(!s.isEmpty()) {
        		AbstractFormatter f = tfDatum.getFormatter(); // TextFormatter, responsible for formatting the current value
        		DateFormatter df = (DateFormatter)f;
        		try {
					Object datum = df.stringToValue(s);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		});

		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(l);
        if(numberFormat instanceof java.text.DecimalFormat df) {
        	localcurrencysymbol = df.getCurrency().getSymbol();
        	localcurrencycode = df.getCurrency().getCurrencyCode();
        	//df.getCurrency().getDisplayName()
        	LOG.info("localcurrencysymbol/code="+localcurrencysymbol+"7"+localcurrencycode);
        }
		JXFormattedTextField tfBetrag = new JXFormattedTextField(numberFormat);
		tfBetrag.setFocusBehavior(focusBehavior);
		tfBetrag.setColumns(12);
		tfBetrag.setPrompt(localcurrencycode+" Betrag");
		tfBetrag.setPromptForeground(Color.BLUE);
		tfBetrag.setPromptBackground(Color.ORANGE);
		tfBetrag.setPromptFontStyle(Font.BOLD+Font.ITALIC);
		tfBetrag.addBuddy(new JLabel(localcurrencysymbol), Position.RIGHT);
		tfBetrag.addPropertyChangeListener(pce -> {
        	String s = tfBetrag.getText();
        	Object o = tfBetrag.getValue();
        	LOG.info("tfBetrag Value:"+o + ", Text String:"+s + (s.isEmpty() ? "(empty) " : ", ") 
        			+ "formatter:"+tfBetrag.getFormatter());
        	if(!s.isEmpty()) {
        		DecimalFormat df = (DecimalFormat)numberFormat;
        		try {
					tfBetrag.setValue(df.parse(s));
				} catch (ParseException e) {
					int eo = e.getErrorOffset();
					LOG.warning(e.getMessage() + ", ErrorOffset="+eo);
//					e.printStackTrace();
					tfBetrag.setValue(0); // fallback
					tfBetrag.removeAllBuddies();
					if(eo>0) {
						df = (DecimalFormat)NumberFormat.getNumberInstance();
						Number num = df.parse(s.substring(0, eo), new ParsePosition(0));
						LOG.info("result "+num.getClass() +":"+num + " :"+s);
						tfBetrag.setValue(num);
					}
				}
        	}
			
		});

		panel.add(labelDatum);
		panel.add(tfDatum);
		panel.add(labelBetrag);
		panel.add(tfBetrag);

		return panel;
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
