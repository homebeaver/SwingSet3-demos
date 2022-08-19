package org.jdesktop.swingx.demos.formattedtext;

import java.text.NumberFormat;
import java.awt.*;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
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

        splitPane.setDividerLocation(200);

        super.add(splitPane, BorderLayout.CENTER);
        super.setBackground(Color.black);
    	
//    	createXPanelDemo();
//    	createAnimation(2000, 0.6f); // 2000ms , stop at 60%
    }

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}

    private JComponent createRightPane() {
        right = new JXPanel();
        right.setName("right");
        LOG.info("   ----------Font="+right.getFont());

		JLabel labelDatum = new JLabel("Datum");
		JLabel labelBetrag = new JLabel("Betrag");

		JXFormattedTextField tfDatum = new JXFormattedTextField();
		tfDatum.setValue(new Date()); // heute
		
		JXFormattedTextField tfBetrag = new JXFormattedTextField();
		// JXFormattedTextField Besonderheiten:
		tfBetrag.setPrompt("Euro Betrag");
		tfBetrag.setForeground(Color.BLUE);
		tfBetrag.setBackground(Color.ORANGE);
		//JXFormattedTextField tfBetrag = new JXFormattedTextField("Euro Betrag", Color.BLUE, Color.ORANGE);
		tfBetrag.setPromptFontStyle(Font.BOLD+Font.ITALIC); // ist aber PLAIN
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
		tfBetrag.setValue(betrag.format(4.3f));
		tfBetrag.setColumns(5);

		right.add(labelDatum);
		right.add(tfDatum);
		right.add(labelBetrag);
		right.add(tfBetrag);

//		tfBetrag.setFocusBehavior(PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT);
		tfBetrag.setFocusBehavior(PromptSupport.FocusBehavior.SHOW_PROMPT);
		
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
    	left = new JXPanel();
    	left.setName("left");
        
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

		left.add(labelDatum);
		left.add(tfDatum);
		left.add(labelBetrag);
		left.add(tfBetrag);

        return left;
    }
}
