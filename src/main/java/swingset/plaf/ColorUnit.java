package swingset.plaf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

In awt.Color sind ein paar sRGB Namen definiert,:
WHITE, LIGHT_GRAY, ...

https://en.wikipedia.org/wiki/X11_color_names
 - X11 vs W3C : Color names with clashing definitions: 
                Gray 	#BEBEBE vs #808080 -- in awt.Color wie W3C
                Green 	#00FF00 vs #008000 -- in awt.Color wie X11
                Maroon 	#B03060 vs #800000 -- in awt.Color nicht definiert
                Purple 	#A020F0 vs #800080 -- in awt.Color nicht definiert
                
 - X11 vs W3C : Colors with multiple names: awt.Color wie X11
                Green    #00FF00  Lime
                Magenta  #FF00FF  Fuchsia
                Cyan     #00FFFF  Aqua

in w3.org Basic color keywords sind die Namen anders
		black 	#000000 	0,0,0
		silver 	#C0C0C0 	192,192,192 == LIGHT_GRAY
		gray 	#808080 	128,128,128   in X11-Gray :	#BEBEBE
		white 	#FFFFFF 	255,255,255
		maroon 	#800000 	128,0,0    -- nicht in awt.Color
		red 	#FF0000 	255,0,0
		purple 	#800080 	128,0,128  -- nicht in awt.Color
		fuchsia #FF00FF 	255,0,255  == MAGENTA
		green 	#008000 	0,128,0    !! green ist in awt.Color 0,255,0
		lime 	#00FF00 	0,255,0    !! GREEN
		olive 	#808000 	128,128,0
		yellow 	#FFFF00 	255,255,0
		navy 	#000080 	0,0,128
		blue 	#0000FF 	0,0,255
		teal 	#008080 	0,128,128
		aqua 	#00FFFF 	0,255,255 == CYAN
 */
/**
 * A <code>color</code> is either a keyword or a numerical specification. 
 * The keywords aka color names are ASCII case-insensitive. 
 * <p>
 * See <A href="https://www.w3.org/TR/css-color-3/#color">
 * www.w3.org/TR/css-color-3
 * </A>.
 * 
 * @author homeb
 * 
 */
@SuppressWarnings("serial")
public class ColorUnit extends Color {

	// awt Colors:
//    public static final Color BLACK = Color.BLACK; // black 	#000000 	0,0,0
//    public static final Color WHITE = Color.WHITE; // white 	#FFFFFF 	255,255,255
    
	/*
	 * manche Namen sind nur in ihem Context eindeutig, Bsp. (AWT)Color.GREEN <> W3C.green
	 */
	private enum Context {
		AWT, 
		X11, 
		W3C, 
		NIMBUS, 
		AQUA,  // AquaTheme extends DefaultMetalTheme
		OCEAN, // OceanTheme extends DefaultMetalTheme in swingx
		STEEL} // the DefaultMetalTheme in swingx

    private Context context = null;

    /**
     * Creates an opaque sRGB colorUnit with the specified combined RGB value
     * and a Context.
     * 
     * @param rgb the combined RGB components
     * @param context the Context, null == AWT context
     */
	public ColorUnit(int rgb, Context context) {
		super(rgb);
		this.context = context;
	}
	private ColorUnit(int r, int g, int b, Context context) {
		super(r, g, b);
		this.context = context;
	}
	/**
     * Creates a colorUnit from a Color with name and a Context.
     * The AWT color 00FF00 has the name "green",
     * but in W3C context "green" has the RGB 008000.
     * 
	 * @param c a Color, f.i. Color.BLACK
	 * @param context, f.i. STEEL - the DefaultMetalTheme in swingx
	 * @param name in STEEL the name is "black"
	 */
	public ColorUnit(Color c, Context context, String name) {
		this(c.getRed(), c.getGreen(), c.getBlue(), context);
		ColorUnit cu = new ColorUnit(c.getRed(), c.getGreen(), c.getBlue(), context);
		NameManager.add(getRGBwithoutAlpha(cu), name+","+context);
	}
	
	// Steel Colors:
	/** DARK_BLUE_GRAY is primary control dark shadow color in STEEL, the DefaultMetalTheme in swingx */
    public static final Color DARK_BLUE_GRAY = new Color(0x666699);
	/** BLUE_BELL is primary control shadow color in STEEL, the DefaultMetalTheme in swingx */
    public static final Color BLUE_BELL = new Color(0x9999CC);
	/** LEVANDER_BLUE is primary control color in STEEL, the DefaultMetalTheme in swingx */
    public static final Color LEVANDER_BLUE = new Color(0xCCCCFF);
	/** CURSED_GRAY is control dark shadow color aka secondary1 in STEEL, the DefaultMetalTheme in swingx */
    public static final Color CURSED_GRAY = new Color(0x666666);
	/** NOBEL is control shadow color aka secondary2 in STEEL, the DefaultMetalTheme in swingx */
    public static final Color NOBEL = new Color(0x999999);
	/** NEON_SILVER is control color aka secondary3 in STEEL, the DefaultMetalTheme in swingx */
    public static final Color NEON_SILVER = new Color(0xCCCCCC);
    
	// Ocean Colors:
	/** NIGHT_RIDER is control info color (not BLACK) in OCEAN Theme in swingx */
    public static final Color NIGHT_RIDER = new Color(0x333333);
	/** HAVELOCK_BLUE is primary control dark shadow color in OCEAN Theme in swingx */
    public static final Color HAVELOCK_BLUE = new Color(0x6382BF);
	/** HEATHER is primary control shadow color in OCEAN Theme in swingx */
    public static final Color HEATHER = new Color(0xA3B8CC);
	/** TROPICAL_BLUE is primary control color in OCEAN Theme in swingx */
    public static final Color TROPICAL_BLUE = new Color(0xB8CFE5);
	/** LIGHT_SLATE_GRAY is control dark shadow color aka secondary1 in OCEAN Theme in swingx */
    public static final Color LIGHT_SLATE_GRAY = new Color(0x7A8A99);
	/** WHISPER is control color aka secondary3 in OCEAN Theme in swingx */
    public static final Color WHISPER = new Color(0xEEEEEE);
    
	/** CADET_BLUE is primary control dark shadow color in AQUA Theme */
    public static final Color CADET_BLUE = new Color(0x669999);
	/** GLACIER is primary control shadow color in AQUA Theme */
    public static final Color GLACIER = new Color(0x80C0C0);
	/** BLIZZARD_BLUE is primary control color in AQUA Theme */
    public static final Color BLIZZARD_BLUE = new Color(0x9FEBEB);
    
	// Color names taken from https://www.htmlcsscolor.com/hex/29598B (ENDEAVOUR) 
    /*
    static final Color ASTRAL             = new Color(0x376F89);
    static final Color BROWN              = new Color(0xA52A2A);
    static final Color CERULEAN_BLUE      = new Color(0x2A52BE);
    static final Color CUMULUS            = new Color(0xF5F4C1);
    static final Color EARLS_GREEN        = new Color(0xB8A722);
    static final Color ENDEAVOUR          = new Color(0x29598B);
    static final Color HAWKES_BLUE        = new Color(0xD2DAED);
    static final Color JORDY_BLUE         = new Color(0x7AAAE0);
    static final Color MANATEE            = new Color(0x8D90A1);
    static final Color MISCHKA            = new Color(0xA5A9B2);
    static final Color TENNE              = new Color(0xCD5700);
    static final Color TURBO              = new Color(0xF5CC23);
    */
	// Nimbus Colors see https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html:
    // Primary Color Names, awt color names (BLACK,WHITE) not listed
	/** approx. ASTRAL color is in Nimbus primary key NIMBUSSELECTIONBACKGROUND 
	 * and also in secondary keys NIMBUSSELECTION, TEXTBACKGROUND, TEXTHIGHLIGHT
	 * ! approx. ASTRAL is not same as ASTRAL in htmlcsscolor.com !
	 */
    public static final Color ASTRAL        = new Color(0x39698A); // Astral -> NIMBUSSELECTIONBACKGROUND
    public static final Color HAWKES_BLUE   = new Color(0xD6D9DF); // Hawkes Blue -> CONTROL
    public static final Color CUMULUS       = new Color(0xF2F2BD); // Cumulus -> INFO
    public static final Color TURBO         = new Color(0xFFDC23); // Turbo -> NIMBUSALERTYELLOW
    public static final Color ENDEAVOUR     = new Color(0x33628C); // Endeavour -> NIMBUSBASE
    public static final Color MANATEE       = new Color(0x8E8F91); // Manatee -> NIMBUSDISABLEDTEXT
    public static final Color JORDY_BLUE    = new Color(0x73A4D1); // Jordy Blue -> NIMBUSFOCUS
    public static final Color EARLS_GREEN   = new Color(0xB0B332); // Earls Green -> NIMBUSGREEN
    public static final Color CERULEAN_BLUE = new Color(0x2F5CB4); // Cerulean Blue -> NIMBUSINFOBLUE
    public static final Color TENNE         = new Color(0xBF6204); // Tenne (Tawny, Crema) -> NIMBUSORANGE
    public static final Color BROWN         = new Color(0xA92E22); // Brown -> NIMBUSRED
    
    // Secondary Colors Names, some are same to primary names, exp. Manatee, Astral
    public static final Color SPINDLE       = new Color(0xBABEC6); // Spindle
    public static final Color MISCHKA       = new Color(0xA4ABB8); // Mischka
    public static final Color SOLITUDE      = new Color(0xE9ECF2); // Solitude
    public static final Color ALICE_BLUE    = new Color(0xF7F8FA); // Alice Blue
    public static final Color LINKWATER     = new Color(0xCCD3E0); // Link Water
    public static final Color MATISSE       = new Color(0x3D6079); // Matisse
    public static final Color BLUETOYOU     = new Color(0xBDC1C8); // approx Spindle
    public static final Color INTANGIBLE    = new Color(0xEDEFF2); // approx Solitude
	/** BLUEGRAY color is used as a secondary Nimbus key NIMBUSBLUEGREY. 
	 * <p> An alternative name is <A href="https://icolorpalette.com/color/A9B0BE">Eclectic</A>.
	 */
    public static final Color BLUEGRAY      = new Color(0xA9B0BE); // approx Mischka / alt.name Eclectic 
    public static final Color CLASSICCLOUD  = new Color(0x9297A1); // Classic Cloud, approx Manatee
    public static final Color GHOSTLYSKY    = new Color(0xCDD0D5); // approx Link Water
    
    /**
     * COBALITE is used in XPanelDemo.properties
     */
    // https://icolorpalette.com/color/9999ff Cobalite Color
    // https://colornames.org/color/9999ff  Star Dust Purple , Cobalite
    // https://www.htmlcsscolor.com/hex/9999FF Portage
    public static final Color COBALITE = new Color(0x9999FF);
    
	private static final String BLACK = "black";
	private static final String WHITE = "white";
	private static final String PRIMARY1 = "primary1";
	private static final String PRIMARY2 = "primary2";
	private static final String PRIMARY3 = "primary3";
	private static final String SECONDARY1 = "secondary1";
	private static final String SECONDARY2 = "secondary2";
	private static final String SECONDARY3 = "secondary3";

	/** STEEL_BLACK aka control info color */
    public static final ColorUnit STEEL_BLACK      = new ColorUnit(Color.BLACK, Context.STEEL, BLACK);
	/** STEEL_WHITE aka control highlight color */
    public static final ColorUnit STEEL_WHITE      = new ColorUnit(Color.WHITE, Context.STEEL, WHITE);
	/** STEEL_PRIMARY1 is primary control dark shadow color in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_PRIMARY1   = new ColorUnit(DARK_BLUE_GRAY, Context.STEEL, PRIMARY1);
	/** STEEL_PRIMARY2 is primary control shadow color in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_PRIMARY2   = new ColorUnit(BLUE_BELL, Context.STEEL, PRIMARY2);
	/** STEEL_PRIMARY3 is primary control color in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_PRIMARY3   = new ColorUnit(LEVANDER_BLUE, Context.STEEL, PRIMARY3);
	/** CURSED_GRAY is control dark shadow color aka secondary1 in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_SECONDARY1 = new ColorUnit(CURSED_GRAY, Context.STEEL, SECONDARY1);
	/** NOBEL is control shadow color aka secondary2 in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_SECONDARY2 = new ColorUnit(NOBEL, Context.STEEL, SECONDARY2);
	/** NEON_SILVER is control color aka secondary3 in STEEL, the DefaultMetalTheme in swingx */
    public static final ColorUnit STEEL_SECONDARY3 = new ColorUnit(NEON_SILVER, Context.STEEL, SECONDARY3);

	/** OCEAN_BLACK aka control info color */
    public static final ColorUnit OCEAN_BLACK      = new ColorUnit(NIGHT_RIDER, Context.OCEAN, BLACK);
	/** OCEAN_WHITE aka control highlight color */
    public static final ColorUnit OCEAN_WHITE      = new ColorUnit(Color.WHITE, Context.OCEAN, WHITE);
	/** HAVELOCK_BLUE is primary control dark shadow color in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_PRIMARY1   = new ColorUnit(HAVELOCK_BLUE, Context.OCEAN, PRIMARY1);
	/** HEATHER is primary control shadow color in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_PRIMARY2   = new ColorUnit(HEATHER, Context.OCEAN, PRIMARY2);
	/** TROPICAL_BLUE is primary control color in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_PRIMARY3   = new ColorUnit(TROPICAL_BLUE, Context.OCEAN, PRIMARY3);
	/** LIGHT_SLATE_GRAY is control dark shadow color aka secondary1 in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_SECONDARY1 = new ColorUnit(LIGHT_SLATE_GRAY, Context.OCEAN, SECONDARY1);
	/** TROPICAL_BLUE control shadow color aka secondary2 in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_SECONDARY2 = new ColorUnit(TROPICAL_BLUE, Context.OCEAN, SECONDARY2);
	/** WHISPER is control color aka secondary3 in OCEAN Theme in swingx */
    public static final ColorUnit OCEAN_SECONDARY3 = new ColorUnit(WHISPER, Context.OCEAN, SECONDARY3);

	/** CADET_BLUE is primary control dark shadow color in AQUA Theme */
    public static final ColorUnit AQUA_PRIMARY1 = new ColorUnit(CADET_BLUE, Context.AQUA, PRIMARY1);
	/** GLACIER is primary control shadow color in AQUA Theme */
    public static final ColorUnit AQUA_PRIMARY2 = new ColorUnit(GLACIER, Context.AQUA, PRIMARY2);
	/** BLIZZARD_BLUE is primary control color in AQUA Theme */
    public static final ColorUnit AQUA_PRIMARY3 = new ColorUnit(BLIZZARD_BLUE, Context.AQUA, PRIMARY3);

    // Nimbus Primary Colors defined in javax.swing.plaf.nimbus.initializeDefaults
    public static final ColorUnit NIMBUS_CONTROL         = new ColorUnit(HAWKES_BLUE, Context.NIMBUS, "CONTROL");
    public static final ColorUnit NIMBUS_INFO            = new ColorUnit(CUMULUS, Context.NIMBUS, "INFO");
    public static final ColorUnit NIMBUS_ALERTYELLOW     = new ColorUnit(TURBO, Context.NIMBUS, "NIMBUSALERTYELLOW");
    public static final ColorUnit NIMBUS_BASE            = new ColorUnit(ENDEAVOUR, Context.NIMBUS, "NIMBUSBASE");
    public static final ColorUnit NIMBUS_DISABLEDTEXT    = new ColorUnit(MANATEE, Context.NIMBUS, "NIMBUSDISABLEDTEXT");
    public static final ColorUnit NIMBUS_FOCUS           = new ColorUnit(JORDY_BLUE, Context.NIMBUS, "NIMBUSFOCUS");
    public static final ColorUnit NIMBUS_GREEN           = new ColorUnit(EARLS_GREEN, Context.NIMBUS, "NIMBUSGREEN");
    public static final ColorUnit NIMBUS_INFOBLUE        = new ColorUnit(CERULEAN_BLUE, Context.NIMBUS, "NIMBUSINFOBLUE");
    public static final ColorUnit NIMBUS_LIGHTBACKGROUND = new ColorUnit(Color.WHITE, Context.NIMBUS, "NIMBUSLIGHTBACKGROUND");
    public static final ColorUnit NIMBUS_ORANGE          = new ColorUnit(TENNE, Context.NIMBUS, "NIMBUSORANGE");
    public static final ColorUnit NIMBUS_RED             = new ColorUnit(BROWN, Context.NIMBUS, "NIMBUSRED");
    public static final ColorUnit NIMBUS_SELECTEDTEXT    = new ColorUnit(Color.WHITE, Context.NIMBUS, "NIMBUSSELECTEDTEXT");
    public static final ColorUnit NIMBUS_SELECTIONBACKGROUND = new ColorUnit(ASTRAL, Context.NIMBUS, "NIMBUSSELECTIONBACKGROUND");     
    public static final ColorUnit NIMBUS_TEXT            = new ColorUnit(Color.BLACK, Context.NIMBUS, "TEXT");
    // Nimbus Secondary Colors
    public static final ColorUnit NIMBUS_ACTIVECAPTION     = new ColorUnit(SPINDLE, Context.NIMBUS, "ACTIVECAPTION");
    public static final ColorUnit NIMBUS_BACKGROUND        = new ColorUnit(HAWKES_BLUE, Context.NIMBUS, "BACKGROUND");
    public static final ColorUnit NIMBUS_CONTROLDKSHADOW   = new ColorUnit(MISCHKA, Context.NIMBUS, "CONTROLDKSHADOW");
    public static final ColorUnit NIMBUS_CONTROLHIGHLIGHT  = new ColorUnit(SOLITUDE, Context.NIMBUS, "CONTROLHIGHLIGHT");
    public static final ColorUnit NIMBUS_CONTROLLHIGHLIGHT = new ColorUnit(ALICE_BLUE, Context.NIMBUS, "CONTROLLHIGHLIGHT");
    public static final ColorUnit NIMBUS_CONTROLSHADOW     = new ColorUnit(LINKWATER, Context.NIMBUS, "CONTROLSHADOW");
    public static final ColorUnit NIMBUS_CONTROLTEXT       = new ColorUnit(Color.BLACK, Context.NIMBUS, "CONTROLTEXT");
    public static final ColorUnit NIMBUS_DESKTOP           = new ColorUnit(MATISSE, Context.NIMBUS, "DESKTOP");
    public static final ColorUnit NIMBUS_INACTIVECAPTION   = new ColorUnit(BLUETOYOU, Context.NIMBUS, "INACTIVECAPTION");
    public static final ColorUnit NIMBUS_INFOTEXT          = new ColorUnit(Color.BLACK, Context.NIMBUS, "INFOTEXT");
    public static final ColorUnit NIMBUS_MENU              = new ColorUnit(INTANGIBLE, Context.NIMBUS, "MENU");
    public static final ColorUnit NIMBUS_MENUTEXT          = new ColorUnit(Color.BLACK, Context.NIMBUS, "MENUTEXT");
    public static final ColorUnit NIMBUS_BLUEGREY          = new ColorUnit(BLUEGRAY, Context.NIMBUS, "NIMBUSBLUEGREY");
    public static final ColorUnit NIMBUS_BORDER            = new ColorUnit(CLASSICCLOUD, Context.NIMBUS, "NIMBUSBORDER");
    public static final ColorUnit NIMBUS_SELECTION         = new ColorUnit(ASTRAL, Context.NIMBUS, "NIMBUSSELECTION");
    public static final ColorUnit NIMBUS_SCROLLBAR         = new ColorUnit(GHOSTLYSKY, Context.NIMBUS, "SCROLLBAR");
    public static final ColorUnit NIMBUS_TEXTBACKGROUND    = new ColorUnit(ASTRAL, Context.NIMBUS, "TEXTBACKGROUND");
    public static final ColorUnit NIMBUS_TEXTFOREGROUND    = new ColorUnit(Color.BLACK, Context.NIMBUS, "TEXTFOREGROUND");
    public static final ColorUnit NIMBUS_TEXTHIGHLIGHT     = new ColorUnit(ASTRAL, Context.NIMBUS, "TEXTHIGHLIGHT");
    public static final ColorUnit NIMBUS_TEXTHIGHLIGHTTEXT = new ColorUnit(Color.WHITE, Context.NIMBUS, "TEXTHIGHLIGHTTEXT");
    public static final ColorUnit NIMBUS_TEXTINACTIVETEXT  = new ColorUnit(MANATEE, Context.NIMBUS, "TEXTINACTIVETEXT");

    private static int getRGBwithoutAlpha(Color c) {
//    	System.out.println("Color c="+c.getClass() + " .value="+c);   	
    	int context = c instanceof ColorUnit ? ((ColorUnit)(c)).context.ordinal() : 0;
    	int value = ((context & 0xFF) << 24)
    	| ((c.getRed() & 0xFF) << 16)
    	| ((c.getGreen() & 0xFF) << 8)
		| ((c.getBlue() & 0xFF) << 0);
        return value;
    }
    
    /**
     * For a given Color c returns the registered name without a context
     * @param c the Color
     * @return the registered name or null
     */
	public static String getName(Color c) {
		return NameManager.getInstance().valueToKeyword.get(getRGBwithoutAlpha(c));
	}

	/**
     * For a given Color c returns the registered names 
	 * @param c the given Color
	 * @return list of registered names
	 */
	public static List<String> getNames(Color c) {
		List<String> res = new ArrayList<String>();
		int rgb = getRGBwithoutAlpha(c);
		String rgbs = Integer.toHexString(rgb).toUpperCase();
		NameManager.getInstance().valueToKeyword.forEach( (k,v) -> {
			// ohne Context
			String hexk = Integer.toHexString(k).toUpperCase();
			String rgbk = hexk.length()==6 ? hexk : hexk.substring(1);
			if(rgbs.equals(rgbk)) {
		    	System.out.println("[#"+rgbk + ","+v + "]");
		    	res.add(v);
			}
		});
		return res;
	}
	/**
	 * Print the registered names.
	 */
	public static void printNames() {
		NameManager.getInstance().valueToKeyword.forEach( (k,v) -> {
			String hexk = "00000"+Integer.toHexString(k).toUpperCase();
//	    	System.out.println("[#"+hexk + ","+v + "]");
			// ohne Context
	    	System.out.println("[#"+hexk.substring(hexk.length()-6) + ","+v + "]");
		});
	}
	
	/**
	 * maps RGB value of a Color, without Alpha but with optionally Context info to a Name
	 */
	private static class NameManager {
	    private static NameManager INSTANCE;
	    private Map<Integer, String> valueToKeyword; //valueToKeyword*/ = new HashMap<Integer, String>();
	    private NameManager() {
	    	valueToKeyword = new HashMap<Integer, String>();
			valueToKeyword.put(getRGBwithoutAlpha(Color.BLACK), "black");
			valueToKeyword.put(getRGBwithoutAlpha(Color.WHITE), "white");
			
			valueToKeyword.put(getRGBwithoutAlpha(DARK_BLUE_GRAY), "dark blue gray");
			valueToKeyword.put(getRGBwithoutAlpha(BLUE_BELL), "blue bell");
			valueToKeyword.put(getRGBwithoutAlpha(LEVANDER_BLUE), "lavender blue");
			valueToKeyword.put(getRGBwithoutAlpha(CURSED_GRAY), "cursed gray");
			valueToKeyword.put(getRGBwithoutAlpha(NOBEL), "nobel");
			valueToKeyword.put(getRGBwithoutAlpha(NEON_SILVER), "neon silver");
			
			valueToKeyword.put(getRGBwithoutAlpha(NIGHT_RIDER), "night rider");
			valueToKeyword.put(getRGBwithoutAlpha(HAVELOCK_BLUE), "havelock blue");
			valueToKeyword.put(getRGBwithoutAlpha(HEATHER), "heather");
			valueToKeyword.put(getRGBwithoutAlpha(TROPICAL_BLUE), "tropical blue");
			valueToKeyword.put(getRGBwithoutAlpha(LIGHT_SLATE_GRAY), "light slate grey");
			valueToKeyword.put(getRGBwithoutAlpha(WHISPER), "whisper");
			
			valueToKeyword.put(getRGBwithoutAlpha(CADET_BLUE), "cadet blue");
			valueToKeyword.put(getRGBwithoutAlpha(GLACIER), "glacier");
			valueToKeyword.put(getRGBwithoutAlpha(BLIZZARD_BLUE), "blizzard blue");
	    }
	    private static NameManager getInstance() {
	        if (INSTANCE == null) {
	            INSTANCE = new NameManager();
	        }
	        return INSTANCE;
	    }
	    private static void add(int rgb, String keyword) {
	    	getInstance().valueToKeyword.put(rgb, keyword);
	    }
	}
	
}
