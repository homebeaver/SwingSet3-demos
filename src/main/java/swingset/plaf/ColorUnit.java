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

	/*
	 * manche Namen sind nur in ihem Context eindeutig, Bsp. (AWT)Color.GREEN <> W3C.green
	 */
	private enum Context {
		AWT, 
		X11, 
		W3C, 
		AQUA, // AquaTheme extends DefaultMetalTheme
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
