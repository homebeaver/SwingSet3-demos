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
		STEEL}

    private Context context = null;

    // ctor
	public ColorUnit(int rgb, Context context) {
		super(rgb);
		this.context = context;
	}
	public ColorUnit(Color c, Context context, String name) {
		super(c.getRed(), c.getGreen(), c.getBlue());
		this.context = context;
//		getValueToKeyword().put(getRGBwithoutAlpha(c), name+","+Context.STEEL);
/*
java.lang.ExceptionInInitializerError
	at swingset.plaf.ColorUnitTest.testPrimaryColors(ColorUnitTest.java:19)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:59)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:56)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.BlockJUnit4ClassRunner$1.evaluate(BlockJUnit4ClassRunner.java:100)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:366)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:103)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:63)
	at org.junit.runners.ParentRunner$4.run(ParentRunner.java:331)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:79)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:329)
	at org.junit.runners.ParentRunner.access$100(ParentRunner.java:66)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:293)
	at org.junit.runners.ParentRunner$3.evaluate(ParentRunner.java:306)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:413)
	at org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:93)
	at org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:40)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:529)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:756)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:452)
	at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:210)
Caused by: java.lang.NullPointerException: Cannot invoke "java.awt.Color.getRed()" because "c" is null
	at swingset.plaf.ColorUnit.getRGBwithoutAlpha(ColorUnit.java:145)
	at swingset.plaf.ColorUnit.getValueToKeyword(ColorUnit.java:154)
	at swingset.plaf.ColorUnit.<init>(ColorUnit.java:77)
	at swingset.plaf.ColorUnit.<clinit>(ColorUnit.java:123)
	... 27 more

 */
	}

	private static final String PRIMARY1 = "primary1";
	private static final String PRIMARY2 = "primary2";
	private static final String PRIMARY3 = "primary3";
	private static final String SECONDARY1 = "secondary1";
	private static final String SECONDARY2 = "secondary2";
	private static final String SECONDARY3 = "secondary3";
    public static final Color DARK_BLUE_GRAY = new Color(0x666699);
    public static final ColorUnit STEEL_PRIMARY1 = new ColorUnit(DARK_BLUE_GRAY, Context.STEEL, PRIMARY1);
    public static final Color BLUE_BELL = new Color(0x9999CC);
    public static final ColorUnit STEEL_PRIMARY2 = new ColorUnit(BLUE_BELL, Context.STEEL, PRIMARY2);
    public static final Color LEVANDER_BLUE = new Color(0xCCCCFF);
    public static final ColorUnit STEEL_PRIMARY3 = new ColorUnit(LEVANDER_BLUE, Context.STEEL, PRIMARY3);
    public static final Color CURSED_GRAY = new Color(0x666666);
    public static final ColorUnit STEEL_SECONDARY1 = new ColorUnit(CURSED_GRAY, Context.STEEL, SECONDARY1);
    public static final Color NOBEL = new Color(0x999999);
    public static final ColorUnit STEEL_SECONDARY2 = new ColorUnit(NOBEL, Context.STEEL, SECONDARY2);
    public static final Color NEON_SILVER = new Color(0xCCCCCC);
    public static final ColorUnit STEEL_SECONDARY3 = new ColorUnit(NEON_SILVER, Context.STEEL, SECONDARY3);
   
    public static final Color CADET_BLUE = new Color(0x669999);
    public static final ColorUnit AQUA_PRIMARY1 = new ColorUnit(CADET_BLUE, Context.AQUA, PRIMARY1);
    public static final Color GLACIER = new Color(0x80C0C0);
    public static final ColorUnit AQUA_PRIMARY2 = new ColorUnit(GLACIER, Context.AQUA, PRIMARY2);
    public static final Color BLIZZARD_BLUE = new Color(0x9FEBEB);
    public static final ColorUnit AQUA_PRIMARY3 = new ColorUnit(BLIZZARD_BLUE, Context.AQUA, PRIMARY3);

    private static int getRGBwithoutAlpha(Color c) {
    	int context = c instanceof ColorUnit ? ((ColorUnit)(c)).context.ordinal() : 0;
    	int value = ((context & 0xFF) << 24)
    	| ((c.getRed() & 0xFF) << 16)
    	| ((c.getGreen() & 0xFF) << 8)
		| ((c.getBlue() & 0xFF) << 0);
        return value;
    }
//	private static Map<Integer, String> valueToKeyword = null;
//	private static Map<Integer, String> getValueToKeyword() {
//		if(valueToKeyword==null) valueToKeyword = new HashMap<Integer, String>();
//		valueToKeyword.put(getRGBwithoutAlpha(DARK_BLUE_GRAY), "dark blue gray");
//		valueToKeyword.put(getRGBwithoutAlpha(BLUE_BELL), "blue bell");
//		valueToKeyword.put(getRGBwithoutAlpha(LEVANDER_BLUE), "lavender blue");
//		valueToKeyword.put(getRGBwithoutAlpha(CURSED_GRAY), "cursed gray");
//		valueToKeyword.put(getRGBwithoutAlpha(NOBEL), "nobel");
//		valueToKeyword.put(getRGBwithoutAlpha(NEON_SILVER), "neon silver");
//		
//		valueToKeyword.put(getRGBwithoutAlpha(CADET_BLUE), "cadet blue");
//		valueToKeyword.put(getRGBwithoutAlpha(GLACIER), "glacier");
//		valueToKeyword.put(getRGBwithoutAlpha(BLIZZARD_BLUE), "blizzard blue");
//		return valueToKeyword;
//	}
		
	private static Map<Integer, String> valueToKeyword = new HashMap<Integer, String>() {{
		put(getRGBwithoutAlpha(DARK_BLUE_GRAY), "dark blue gray");
		put(getRGBwithoutAlpha(BLUE_BELL), "blue bell");
		put(getRGBwithoutAlpha(LEVANDER_BLUE), "lavender blue");
		put(getRGBwithoutAlpha(CURSED_GRAY), "cursed gray");
		put(getRGBwithoutAlpha(NOBEL), "nobel");
		put(getRGBwithoutAlpha(NEON_SILVER), "neon silver");
		
		put(getRGBwithoutAlpha(CADET_BLUE), "cadet blue");
		put(getRGBwithoutAlpha(GLACIER), "glacier");
		put(getRGBwithoutAlpha(BLIZZARD_BLUE), "blizzard blue");
		
		// TODO diese nur im ctor einfügen!! exception wie oben beim Test
		put(getRGBwithoutAlpha(STEEL_PRIMARY1), PRIMARY1+","+Context.STEEL);
		put(getRGBwithoutAlpha(STEEL_PRIMARY2), PRIMARY2+","+Context.STEEL);
		put(getRGBwithoutAlpha(STEEL_PRIMARY2), PRIMARY2+","+Context.STEEL);
	}};

	public static String getName(Color c) {
//		Map<Integer, String> vtk = getValueToKeyword();
		return valueToKeyword.get(getRGBwithoutAlpha(c));
	}

	public static List<String> getNames(Color c) {
		List<String> res = new ArrayList<String>();
		int rgb = getRGBwithoutAlpha(c);
		String rgbs = Integer.toHexString(rgb).toUpperCase();
		valueToKeyword.forEach( (k,v) -> {
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
	public static void printNames() {
		valueToKeyword.forEach( (k,v) -> {
			// ohne Context
			String hexk = Integer.toHexString(k).toUpperCase();
	    	System.out.println("[#"+(hexk.length()==6 ? hexk : hexk.substring(1)) + ","+v + "]");
		});
	}
}
