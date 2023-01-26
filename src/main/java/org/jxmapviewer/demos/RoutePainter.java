/* created from jxmapviewer sample2_waypoints
*/ 
package org.jxmapviewer.demos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Painter;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class RoutePainter implements Painter<JXMapViewer> {
	
	private Color color = Color.RED;
	private boolean antiAlias = true;
	private int max = 0; // max track waypoints to show

	private List<GeoPosition> track = DEFAULT_TRACK;

	public RoutePainter(Color color) {
		this.color = color;
		setMaxSize(getTrackSize());
	}

	/**
	 * @param track the track
	 */
	public RoutePainter(List<GeoPosition> track) {
		// copy the list so that changes in the
		// original list do not have an effect here
		this.track = new ArrayList<GeoPosition>(track);
	}

	public int getTrackSize() {
		return track==null ? -1 : track.size();
	}
	
	public void setMaxSize(int maxTrackSizeToShow) {
		max = maxTrackSizeToShow;
	}
	public int getMaxSize() {
		return track==null ? -1 : max;
	}
	
	@Override
	public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
		g = (Graphics2D) g.create();

		// convert from viewport to world bitmap
		Rectangle rect = map.getViewportBounds();
		g.translate(-rect.x, -rect.y);

		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// do the drawing
//		g.setColor(Color.BLACK);
//		g.setStroke(new BasicStroke(4));
//		drawRoute(g, map);

		// do the drawing again with Color color
		g.setColor(color);
		g.setStroke(new BasicStroke(2));
		drawRoute(g, map);

		g.dispose();
	}

	/**
	 * @param g   the graphics object
	 * @param map the map
	 */
	private void drawRoute(Graphics2D g, JXMapViewer map) {
		int lastX = 0;
		int lastY = 0;

		boolean first = true;

		for(int i=0; i<track.size() &&i<getMaxSize(); i++) {
			GeoPosition gp = track.get(i);
//		for (GeoPosition gp : track) {
			// convert geo-coordinate to world bitmap pixel
			Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

			if (first) {
				first = false;
			} else {
				g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
			}

			lastX = (int) pt.getX();
			lastY = (int) pt.getY();
		}
	}
	
	// a hiking track on Madeira island
    private static final List<GeoPosition> DEFAULT_TRACK = Arrays.asList(
    		new GeoPosition(32.809156011790037, -17.141426000744104),
    		new GeoPosition(32.809156011790037, -17.141426000744104),
    		new GeoPosition(32.809156011790037, -17.141426000744104),
    		new GeoPosition(32.809156011790037, -17.141426000744104),
    		new GeoPosition(32.809309987351298, -17.141276970505714),
    		new GeoPosition(32.809432027861476, -17.141194995492697),
    		new GeoPosition(32.809449965134263, -17.141182003542781),
    		new GeoPosition(32.80955096706748, -17.1410739608109),
    		new GeoPosition(32.809626990929246, -17.140998020768166),
    		new GeoPosition(32.809696979820728, -17.140909004956484),
    		new GeoPosition(32.8098089620471, -17.140843039378524),
    		new GeoPosition(32.809855984523892, -17.140804985538125),
    		new GeoPosition(32.8099350258708, -17.140710018575191),
    		new GeoPosition(32.809943994507194, -17.140699960291386),
    		new GeoPosition(32.809991016983986, -17.140628965571523),
    		new GeoPosition(32.809961009770632, -17.140629971399903),
    		new GeoPosition(32.809987999498844, -17.140712030231953),
    		new GeoPosition(32.810052037239075, -17.140659978613257),
    		new GeoPosition(32.810139963403344, -17.140541961416602),
    		new GeoPosition(32.810199977830052, -17.140516983345151),
    		new GeoPosition(32.810284970328212, -17.140351021662354),
    		new GeoPosition(32.810260998085141, -17.140297964215279),
    		new GeoPosition(32.810278013348579, -17.140188999474049),
    		new GeoPosition(32.810280025005341, -17.140182964503765),
    		new GeoPosition(32.810357976704836, -17.140056984499097),
    		new GeoPosition(32.810364011675119, -17.140038963407278),
    		new GeoPosition(32.810338027775288, -17.139907032251358),
    		new GeoPosition(32.810338027775288, -17.139780968427658),
    		new GeoPosition(32.810348002240062, -17.139702010899782),
    		new GeoPosition(32.810327969491482, -17.139658005908132),
    		new GeoPosition(32.810313971713185, -17.139615006744862),
    		new GeoPosition(32.810306008905172, -17.139562033116817),
    		new GeoPosition(32.810308020561934, -17.139488020911813),
    		new GeoPosition(32.810320006683469, -17.139401016756892),
    		new GeoPosition(32.810342973098159, -17.139317030087113),
    		new GeoPosition(32.810383038595319, -17.139175040647388),
    		new GeoPosition(32.810399970039725, -17.139090970158577),
    		new GeoPosition(32.810328975319862, -17.138978987932205),
    		new GeoPosition(32.810326041653752, -17.138947974890471),
    		new GeoPosition(32.810296034440398, -17.138864994049072),
    		new GeoPosition(32.810291005298495, -17.138853007927537),
    		new GeoPosition(32.810225039720535, -17.138627031818032),
    		new GeoPosition(32.810225961729884, -17.13858000934124),
    		new GeoPosition(32.810210958123207, -17.138496022671461),
    		new GeoPosition(32.810192015022039, -17.13840801268816),
    		new GeoPosition(32.810186985880136, -17.138326959684491),
    		new GeoPosition(32.810142980888486, -17.138162003830075),
    		new GeoPosition(32.810133006423712, -17.138025965541601),
    		new GeoPosition(32.810145998373628, -17.137997969985008),
    		new GeoPosition(32.810099981725216, -17.137821028009057),
    		new GeoPosition(32.810097970068455, -17.137809963896871),
    		new GeoPosition(32.810082966461778, -17.137760007753968),
    		new GeoPosition(32.810101993381977, -17.137693958356977),
    		new GeoPosition(32.810100987553596, -17.13767996057868),
    		new GeoPosition(32.810102999210358, -17.137627992779016),
    		new GeoPosition(32.810102999210358, -17.137518022209406),
    		new GeoPosition(32.810101993381977, -17.137412996962667),
    		new GeoPosition(32.810101993381977, -17.137373015284538),
    		new GeoPosition(32.810099981725216, -17.137346025556326),
    		new GeoPosition(32.810101993381977, -17.137258015573025),
    		new GeoPosition(32.810092018917203, -17.137211998924613),
    		new GeoPosition(32.81010702252388, -17.137134969234467),
    		new GeoPosition(32.810097970068455, -17.137067997828126),
    		new GeoPosition(32.810095036402345, -17.137044025585055),
    		new GeoPosition(32.810090007260442, -17.136802040040493),
    		new GeoPosition(32.810079026967287, -17.136765997856855),
    		new GeoPosition(32.81002102419734, -17.136516971513629),
    		new GeoPosition(32.809980036690831, -17.136414041742682),
    		new GeoPosition(32.809931002557278, -17.136256964877248),
    		new GeoPosition(32.809907030314207, -17.136154035106301),
    		new GeoPosition(32.809903007000685, -17.13610801845789),
    		new GeoPosition(32.809919016435742, -17.135998969897628),
    		new GeoPosition(32.809967966750264, -17.135883970186114),
    		new GeoPosition(32.809971990063787, -17.135875001549721),
    		new GeoPosition(32.810076009482145, -17.135699987411499),
    		new GeoPosition(32.810093024745584, -17.135605020448565),
    		new GeoPosition(32.810093024745584, -17.135556992143393),
    		new GeoPosition(32.810164019465446, -17.135355994105339),
    		new GeoPosition(32.810155972838402, -17.135329004377127),
    		new GeoPosition(32.810176005586982, -17.13524803519249),
    		new GeoPosition(32.81025898642838, -17.135132029652596),
    		new GeoPosition(32.810259992256761, -17.135060029104352),
    		new GeoPosition(32.810235014185309, -17.13499004021287),
    		new GeoPosition(32.810235014185309, -17.13497101329267),
    		new GeoPosition(32.810253035277128, -17.134875040501356),
    		new GeoPosition(32.810210958123207, -17.134646968916059),
    		new GeoPosition(32.810201989486814, -17.134625008329749),
    		new GeoPosition(32.810178017243743, -17.134544039145112),
    		new GeoPosition(32.810162007808685, -17.134410012513399),
    		new GeoPosition(32.810188997536898, -17.134246984496713),
    		new GeoPosition(32.810194026678801, -17.134181018918753),
    		new GeoPosition(32.810182962566614, -17.134118992835283),
    		new GeoPosition(32.810159996151924, -17.134084040299058),
    		new GeoPosition(32.810156978666782, -17.134053027257323),
    		new GeoPosition(32.810109034180641, -17.133882958441973),
    		new GeoPosition(32.810110040009022, -17.133870972320437),
    		new GeoPosition(32.810062011703849, -17.13376997038722),
    		new GeoPosition(32.810055976733565, -17.133748009800911),
    		new GeoPosition(32.810050025582314, -17.133620018139482),
    		new GeoPosition(32.810035021975636, -17.133442992344499),
    		new GeoPosition(32.810027981176972, -17.133409967646003),
    		new GeoPosition(32.809980036690831, -17.133258003741503),
    		new GeoPosition(32.809976013377309, -17.13320997543633),
    		new GeoPosition(32.809998979791999, -17.133063040673733),
    		new GeoPosition(32.810020018368959, -17.132982993498445),
    		new GeoPosition(32.810011971741915, -17.132799010723829),
    		new GeoPosition(32.809996968135238, -17.132733967155218),
    		new GeoPosition(32.809972995892167, -17.132625002413988),
    		new GeoPosition(32.809990011155605, -17.132501034066081),
    		new GeoPosition(32.810009038075805, -17.132362984120846),
    		new GeoPosition(32.810010965913534, -17.132303975522518),
    		new GeoPosition(32.810036027804017, -17.132125021889806),
    		new GeoPosition(32.810008032247424, -17.132061989977956),
    		new GeoPosition(32.810003003105521, -17.132032988592982),
    		new GeoPosition(32.809855984523892, -17.131857974454761),
    		new GeoPosition(32.809851039201021, -17.131851017475128),
    		new GeoPosition(32.809795970097184, -17.131771976128221),
    		new GeoPosition(32.809695973992348, -17.131640966981649),
    		new GeoPosition(32.809688011184335, -17.131621018052101),
    		new GeoPosition(32.809648029506207, -17.131541976705194),
    		new GeoPosition(32.809567982330918, -17.131335027515888),
    		new GeoPosition(32.809565970674157, -17.131144003942609),
    		new GeoPosition(32.809581980109215, -17.131121959537268),
    		new GeoPosition(32.809641994535923, -17.131031015887856),
    		new GeoPosition(32.809649035334587, -17.131022969260812),
    		new GeoPosition(32.809705026447773, -17.130938982591033),
    		new GeoPosition(32.809782978147268, -17.130791964009404),
    		new GeoPosition(32.809810973703861, -17.130753993988037),
    		new GeoPosition(32.809924967586994, -17.130604041740298),
    		new GeoPosition(32.809924967586994, -17.130604041740298),
    		new GeoPosition(32.809783983975649, -17.130483007058501),
    		new GeoPosition(32.80976596288383, -17.130475966259837),
    		new GeoPosition(32.809579968452454, -17.130427015945315),
    		new GeoPosition(32.809420041739941, -17.130303969606757),
    		new GeoPosition(32.809405038133264, -17.1302879601717),
    		new GeoPosition(32.80922600068152, -17.130106994882226),
    		new GeoPosition(32.80920404009521, -17.130091991275549),
    		new GeoPosition(32.809118963778019, -17.12994103319943),
    		new GeoPosition(32.809047969058156, -17.129876995459199),
    		new GeoPosition(32.808898016810417, -17.129712961614132),
    		new GeoPosition(32.808898016810417, -17.129712961614132),
    		new GeoPosition(32.80885904096067, -17.129699969664216),
    		new GeoPosition(32.808864992111921, -17.129703992977738),
    		new GeoPosition(32.808961970731616, -17.129669040441513),
    		new GeoPosition(32.808987032622099, -17.129656970500946),
    		new GeoPosition(32.809003964066505, -17.129549011588097),
    		new GeoPosition(32.808973034843802, -17.129393024370074),
    		new GeoPosition(32.80894797295332, -17.129353964701295),
    		new GeoPosition(32.808906985446811, -17.129198983311653),
    		new GeoPosition(32.808902962133288, -17.129047019407153),
    		new GeoPosition(32.808850994333625, -17.128964960575104),
    		new GeoPosition(32.808842025697231, -17.128780977800488),
    		new GeoPosition(32.808800032362342, -17.128681987524033),
    		new GeoPosition(32.808811012655497, -17.12850303389132),
    		new GeoPosition(32.808798020705581, -17.128433967009187),
    		new GeoPosition(32.808779999613762, -17.128341011703014),
    		new GeoPosition(32.808743035420775, -17.128331959247589),
    		new GeoPosition(32.808747980743647, -17.12833103723824),
    		new GeoPosition(32.808743035420775, -17.128317039459944),
    		new GeoPosition(32.808750998228788, -17.128292983397841),
    		new GeoPosition(32.808773964643478, -17.128184018656611),
    		new GeoPosition(32.80879600904882, -17.128047980368137),
    		new GeoPosition(32.808830961585045, -17.12790003977716),
    		new GeoPosition(32.808842025697231, -17.127830972895026),
    		new GeoPosition(32.808900028467178, -17.12762301787734),
    		new GeoPosition(32.808907991275191, -17.127508018165827),
    		new GeoPosition(32.808921989053488, -17.127369968220592),
    		new GeoPosition(32.808934981003404, -17.127234013751149),
    		new GeoPosition(32.8089489787817, -17.127151032909751),
    		new GeoPosition(32.808946967124939, -17.12702102959156),
    		new GeoPosition(32.808963982388377, -17.126919021829963),
    		new GeoPosition(32.808946967124939, -17.1268480271101),
    		new GeoPosition(32.80894797295332, -17.126846015453339),
    		new GeoPosition(32.808945961296558, -17.126846015453339),
    		new GeoPosition(32.808945961296558, -17.126846015453339),
    		new GeoPosition(32.808931963518262, -17.126620961353183),
    		new GeoPosition(32.808941015973687, -17.12650403380394),
    		new GeoPosition(32.808944033458829, -17.126456005498767),
    		new GeoPosition(32.808942021802068, -17.126297000795603),
    		new GeoPosition(32.808929029852152, -17.126212008297443),
    		new GeoPosition(32.808881001546979, -17.12607697583735),
    		new GeoPosition(32.80886197462678, -17.125934986397624),
    		new GeoPosition(32.808856023475528, -17.125900033861399),
    		new GeoPosition(32.808844037353992, -17.125784028321505),
    		new GeoPosition(32.808844037353992, -17.125744968652725),
    		new GeoPosition(32.808835990726948, -17.125682020559907),
    		new GeoPosition(32.808858035132289, -17.125613959506154),
    		new GeoPosition(32.808876978233457, -17.125496026128531),
    		new GeoPosition(32.80892601236701, -17.125427965074778),
    		new GeoPosition(32.80892500653863, -17.125420002266765),
    		new GeoPosition(32.80892500653863, -17.125420002266765),
    		new GeoPosition(32.80892500653863, -17.125420002266765),
    		new GeoPosition(32.80892500653863, -17.125420002266765),
    		new GeoPosition(32.80892500653863, -17.125420002266765),
    		new GeoPosition(32.808924000710249, -17.125421008095145),
    		new GeoPosition(32.808924000710249, -17.125421008095145),
    		new GeoPosition(32.808915032073855, -17.125422013923526),
    		new GeoPosition(32.808913020417094, -17.125420002266765),
    		new GeoPosition(32.808912014588714, -17.125424025580287),
    		new GeoPosition(32.808912014588714, -17.125424025580287),
    		new GeoPosition(32.808912014588714, -17.125424025580287),
    		new GeoPosition(32.808912014588714, -17.125424025580287),
    		new GeoPosition(32.808912014588714, -17.125424025580287),
    		new GeoPosition(32.80890597961843, -17.125426959246397),
    		new GeoPosition(32.808900028467178, -17.125436011701822),
    		new GeoPosition(32.808848982676864, -17.125596022233367),
    		new GeoPosition(32.808838002383709, -17.12568998336792),
    		new GeoPosition(32.80884001404047, -17.125877989456058),
    		new GeoPosition(32.808853005990386, -17.125949990004301),
    		new GeoPosition(32.808875972405076, -17.126099020242691),
    		new GeoPosition(32.808898016810417, -17.126199016347528),
    		new GeoPosition(32.808922994881868, -17.126317033544183),
    		new GeoPosition(32.808922994881868, -17.126317033544183),
    		new GeoPosition(32.808930035680532, -17.126447036862373),
    		new GeoPosition(32.808932969346642, -17.12659103795886),
    		new GeoPosition(32.808929029852152, -17.12672296911478),
    		new GeoPosition(32.808933975175023, -17.1268480271101),
    		new GeoPosition(32.808941015973687, -17.12691399268806),
    		new GeoPosition(32.808949984610081, -17.127137035131454),
    		new GeoPosition(32.8089489787817, -17.127262009307742),
    		new GeoPosition(32.80892500653863, -17.127378014847636),
    		new GeoPosition(32.808908997103572, -17.127523021772504),
    		new GeoPosition(32.808873038738966, -17.127694012597203),
    		new GeoPosition(32.808844959363341, -17.127827033400536),
    		new GeoPosition(32.808802966028452, -17.1279670111835),
    		new GeoPosition(32.808774970471859, -17.128102965652943),
    		new GeoPosition(32.808747980743647, -17.128248978406191),
    		new GeoPosition(32.808730965480208, -17.128302035853267),
    		new GeoPosition(32.8086899779737, -17.12850596755743),
    		new GeoPosition(32.808663994073868, -17.128607975319028),
    		new GeoPosition(32.80860397964716, -17.128776032477617),
    		new GeoPosition(32.808545976877213, -17.128918021917343),
    		new GeoPosition(32.808484034612775, -17.129034027457237),
    		new GeoPosition(32.808433994650841, -17.129106028005481),
    		new GeoPosition(32.808368029072881, -17.129184985533357),
    		new GeoPosition(32.808320000767708, -17.129251034930348),
    		new GeoPosition(32.808226039633155, -17.129351031035185),
    		new GeoPosition(32.808146998286247, -17.12938598357141),
    		new GeoPosition(32.80806502327323, -17.129465024918318),
    		new GeoPosition(32.808060999959707, -17.129469970241189),
    		new GeoPosition(32.807976007461548, -17.129582036286592),
    		new GeoPosition(32.807962009683251, -17.129613971337676),
    		new GeoPosition(32.807841980829835, -17.129683960229158),
    		new GeoPosition(32.807746008038521, -17.129744980484247),
    		new GeoPosition(32.807619022205472, -17.129800971597433),
    		new GeoPosition(32.807526988908648, -17.129816981032491),
    		new GeoPosition(32.807397991418839, -17.129848999902606),
    		new GeoPosition(32.80731400474906, -17.129872972145677),
    		new GeoPosition(32.807199005037546, -17.129932986572385),
    		new GeoPosition(32.807176038622856, -17.129948996007442),
    		new GeoPosition(32.807070007547736, -17.130038011819124),
    		new GeoPosition(32.806966993957758, -17.130129961296916),
    		new GeoPosition(32.806966993957758, -17.130129961296916),
    		new GeoPosition(32.806871021166444, -17.130233980715275),
    		new GeoPosition(32.806865992024541, -17.130246972665191),
    		new GeoPosition(32.806785022839904, -17.130369013175368),
    		new GeoPosition(32.806770019233227, -17.130418969318271),
    		new GeoPosition(32.806673040613532, -17.130610998719931),
    		new GeoPosition(32.806650996208191, -17.130631031468511),
    		new GeoPosition(32.806646972894669, -17.130706971511245),
    		new GeoPosition(32.806526022031903, -17.1308249887079),
    		new GeoPosition(32.806466007605195, -17.130937976762652),
    		new GeoPosition(32.806436000391841, -17.131002014502883),
    		new GeoPosition(32.806339021772146, -17.131166970357299),
    		new GeoPosition(32.80632896348834, -17.131180968135595),
    		new GeoPosition(32.806315971538424, -17.131193038076162),
    		new GeoPosition(32.806322006508708, -17.131196977570653),
    		new GeoPosition(32.806322006508708, -17.131196977570653),
    		new GeoPosition(32.806322006508708, -17.131196977570653),
    		new GeoPosition(32.806322006508708, -17.131196977570653),
    		new GeoPosition(32.806254029273987, -17.131214998662472),
    		new GeoPosition(32.80615302734077, -17.131276018917561),
    		new GeoPosition(32.806043978780508, -17.131345001980662),
    		new GeoPosition(32.806016989052296, -17.131368974223733),
    		new GeoPosition(32.805885979905725, -17.131401998922229),
    		new GeoPosition(32.805879022926092, -17.131405016407371),
    		new GeoPosition(32.805870976299047, -17.131407028064132),
    		new GeoPosition(32.80576997436583, -17.13142697699368),
    		new GeoPosition(32.805656986311078, -17.131466958671808),
    		new GeoPosition(32.805649023503065, -17.13146997615695),
    		new GeoPosition(32.805550033226609, -17.1315330080688),
    		new GeoPosition(32.805466968566179, -17.131651025265455),
    		new GeoPosition(32.805364960804582, -17.131715985015035),
    		new GeoPosition(32.805334031581879, -17.13172503747046),
    		new GeoPosition(32.805158011615276, -17.131723025813699),
    		new GeoPosition(32.805082993581891, -17.131739035248756),
    		new GeoPosition(32.804987020790577, -17.131746998056769),
    		new GeoPosition(32.804877972230315, -17.131754960864782),
    		new GeoPosition(32.804808989167213, -17.131762001663446),
    		new GeoPosition(32.804720979183912, -17.131740041077137),
    		new GeoPosition(32.804636992514133, -17.131745992228389),
    		new GeoPosition(32.804572032764554, -17.131750015541911),
    		new GeoPosition(32.804476981982589, -17.131754038855433),
    		new GeoPosition(32.804425014182925, -17.13174800388515),
    		new GeoPosition(32.804403975605965, -17.131745992228389),
    		new GeoPosition(32.804298028349876, -17.131604002788663),
    		new GeoPosition(32.804277995601296, -17.13155304081738),
    		new GeoPosition(32.804194008931518, -17.131458995863795),
    		new GeoPosition(32.804164001718163, -17.131434017792344),
    		new GeoPosition(32.804070040583611, -17.131356988102198),
    		new GeoPosition(32.803949005901814, -17.131257997825742),
    		new GeoPosition(32.803896032273769, -17.131221033632755),
    		new GeoPosition(32.803802993148565, -17.131159007549286),
    		new GeoPosition(32.803785977885127, -17.131149033084512),
    		new GeoPosition(32.803573999553919, -17.131001008674502),
    		new GeoPosition(32.803508033975959, -17.130952980369329),
    		new GeoPosition(32.803334025666118, -17.130877040326595),
    		new GeoPosition(32.803311981260777, -17.130846027284861),
    		new GeoPosition(32.803186001256108, -17.130730021744967),
    		new GeoPosition(32.803036971017718, -17.130612004548311),
    		new GeoPosition(32.802997995167971, -17.130579985678196),
    		new GeoPosition(32.802933035418391, -17.1305469609797),
    		new GeoPosition(32.802790040150285, -17.13046096265316),
    		new GeoPosition(32.802769001573324, -17.130436990410089),
    		new GeoPosition(32.802688032388687, -17.130379993468523),
    		new GeoPosition(32.802646961063147, -17.130359038710594),
    		new GeoPosition(32.802562974393368, -17.13028603233397),
    		new GeoPosition(32.80254302546382, -17.130177989602089),
    		new GeoPosition(32.802572026848793, -17.130100959911942),
    		new GeoPosition(32.802640004083514, -17.130022002384067),
    		new GeoPosition(32.802640004083514, -17.130022002384067),
    		new GeoPosition(32.80263002961874, -17.130032982677221),
    		new GeoPosition(32.80263002961874, -17.130031976848841),
    		new GeoPosition(32.80263002961874, -17.130031976848841),
    		new GeoPosition(32.80263002961874, -17.130031976848841),
    		new GeoPosition(32.80263002961874, -17.130031976848841),
    		new GeoPosition(32.802625000476837, -17.130038011819124),
    		new GeoPosition(32.802605973556638, -17.130068019032478),
    		new GeoPosition(32.802594993263483, -17.130084028467536),
    		new GeoPosition(32.802537996321917, -17.130172038450837),
    		new GeoPosition(32.802514024078846, -17.130215037614107),
    		new GeoPosition(32.802714016288519, -17.130396002903581),
    		new GeoPosition(32.802724996581674, -17.130400026217103),
    		new GeoPosition(32.802858017385006, -17.130497004836798),
    		new GeoPosition(32.802977962419391, -17.130570011213422),
    		new GeoPosition(32.803021967411041, -17.130594989284873),
    		new GeoPosition(32.803106959909201, -17.130661960691214),
    		new GeoPosition(32.803251966834068, -17.130786012858152),
    		new GeoPosition(32.803281974047422, -17.130807973444462),
    		new GeoPosition(32.803438967093825, -17.130883997306228),
    		new GeoPosition(32.803558995947242, -17.130980975925922),
    		new GeoPosition(32.803752031177282, -17.131093963980675),
    		new GeoPosition(32.803816990926862, -17.131140986457467),
    		new GeoPosition(32.803989993408322, -17.131251040846109),
    		new GeoPosition(32.804171964526176, -17.131363023072481),
    		new GeoPosition(32.804269026964903, -17.131489003077149),
    		new GeoPosition(32.804311020299792, -17.131613977253437),
    		new GeoPosition(32.804381009191275, -17.131716990843415),
    		new GeoPosition(32.804503971710801, -17.131753033027053),
    		new GeoPosition(32.804597010836005, -17.131745992228389),
    		new GeoPosition(32.80460799112916, -17.131754038855433),
    		new GeoPosition(32.80473698861897, -17.131749009713531),
    		new GeoPosition(32.804746963083744, -17.131750015541911),
    		new GeoPosition(32.804926000535488, -17.131742974743247),
    		new GeoPosition(32.805090034380555, -17.131728976964951),
    		new GeoPosition(32.805243004113436, -17.131741968914866),
    		new GeoPosition(32.805315004661679, -17.131726965308189),
    		new GeoPosition(32.805470991879702, -17.131630992516875),
    		new GeoPosition(32.805627984926105, -17.131546000018716),
    		new GeoPosition(32.805693028494716, -17.131524961441755),
    		new GeoPosition(32.805787995457649, -17.131495038047433),
    		new GeoPosition(32.805913975462317, -17.131441980600357),
    		new GeoPosition(32.806026041507721, -17.131439968943596),
    		new GeoPosition(32.806052025407553, -17.131432006135583),
    		new GeoPosition(32.80626299791038, -17.131312983110547),
    		new GeoPosition(32.806351007893682, -17.131307031959295),
    		new GeoPosition(32.806362994015217, -17.131298985332251),
    		new GeoPosition(32.806322006508708, -17.131196977570653),
    		new GeoPosition(32.80623902566731, -17.131441980600357),
    		new GeoPosition(32.80619703233242, -17.131483973935246),
    		new GeoPosition(32.806074991822243, -17.131606014445424),
    		new GeoPosition(32.80602696351707, -17.131638033315539),
    		new GeoPosition(32.805967032909393, -17.131798965856433),
    		new GeoPosition(32.805965021252632, -17.131815981119871),
    		new GeoPosition(32.805936019867659, -17.131957970559597),
    		new GeoPosition(32.805925039574504, -17.132154023274779),
    		new GeoPosition(32.805929984897375, -17.132355021312833),
    		new GeoPosition(32.805968038737774, -17.132571022957563),
    		new GeoPosition(32.805970972403884, -17.132584014907479),
    		new GeoPosition(32.806026041507721, -17.132734972983599),
    		new GeoPosition(32.806039033457637, -17.132802028208971),
    		new GeoPosition(32.806086977943778, -17.132991040125489),
    		new GeoPosition(32.806126959621906, -17.133181979879737),
    		new GeoPosition(32.806170964613557, -17.133389012888074),
    		new GeoPosition(32.806210024282336, -17.133584981784225),
    		new GeoPosition(32.806213963776827, -17.133655976504087),
    		new GeoPosition(32.806259980425239, -17.133952025324106),
    		new GeoPosition(32.806280013173819, -17.134125027805567),
    		new GeoPosition(32.806288981810212, -17.134151011705399),
    		new GeoPosition(32.806299040094018, -17.134258970618248),
    		new GeoPosition(32.806302979588509, -17.134270034730434),
    		new GeoPosition(32.806393001228571, -17.134548984467983),
    		new GeoPosition(32.806463995948434, -17.134721986949444),
    		new GeoPosition(32.806486962363124, -17.13477797806263),
    		new GeoPosition(32.806515041738749, -17.134870011359453),
    		new GeoPosition(32.806539013981819, -17.134922984987497),
    		new GeoPosition(32.806548988446593, -17.134960032999516),
    		new GeoPosition(32.806554017588496, -17.134974030777812),
    		new GeoPosition(32.806558962911367, -17.135001020506024),
    		new GeoPosition(32.806570027023554, -17.1352509688586),
    		new GeoPosition(32.806585030630231, -17.135354988276958),
    		new GeoPosition(32.806587964296341, -17.135366974398494),
    		new GeoPosition(32.806635992601514, -17.13550703600049),
    		new GeoPosition(32.80669298954308, -17.135603008791804),
    		new GeoPosition(32.806705981492996, -17.135632010176778),
    		new GeoPosition(32.806762978434563, -17.135738041251898),
    		new GeoPosition(32.806796003133059, -17.135777017101645),
    		new GeoPosition(32.806834978982806, -17.135835019871593),
    		new GeoPosition(32.806903040036559, -17.135969968512654),
    		new GeoPosition(32.806937992572784, -17.13604599237442),
    		new GeoPosition(32.807026002556086, -17.136197034269571),
    		new GeoPosition(32.807032037526369, -17.136299964040518),
    		new GeoPosition(32.807019967585802, -17.136436002328992),
    		new GeoPosition(32.807033965364099, -17.13658100925386),
    		new GeoPosition(32.807058021426201, -17.136631971225142),
    		new GeoPosition(32.807148965075612, -17.136806985363364),
    		new GeoPosition(32.807225994765759, -17.13703396730125),
    		new GeoPosition(32.807302018627524, -17.137291040271521),
    		new GeoPosition(32.807349041104317, -17.137460019439459),
    		new GeoPosition(32.807363960891962, -17.137688007205725),
    		new GeoPosition(32.807369995862246, -17.137770988047123),
    		new GeoPosition(32.807362033054233, -17.137994030490518),
    		new GeoPosition(32.80735800974071, -17.138195028528571),
    		new GeoPosition(32.807371001690626, -17.138394014909863),
    		new GeoPosition(32.807424981147051, -17.138585038483143),
    		new GeoPosition(32.807521037757397, -17.138811014592648),
    		new GeoPosition(32.807601001113653, -17.139000026509166),
    		new GeoPosition(32.807662021368742, -17.139107985422015),
    		new GeoPosition(32.807760005816817, -17.139244023710489),
    		new GeoPosition(32.807847009971738, -17.13933396153152),
    		new GeoPosition(32.807969972491264, -17.13941995985806),
    		new GeoPosition(32.808015989139676, -17.139434041455388),
    		new GeoPosition(32.808120008558035, -17.139519033953547),
    		new GeoPosition(32.808205001056194, -17.139603020623326),
    		new GeoPosition(32.80823802575469, -17.139736041426659),
    		new GeoPosition(32.808229979127645, -17.139897979795933),
    		new GeoPosition(32.808234002441168, -17.139965035021305),
    		new GeoPosition(32.808229979127645, -17.139984983950853),
    		new GeoPosition(32.808234002441168, -17.139994036406279),
    		new GeoPosition(32.80828395858407, -17.140104006975889),
    		new GeoPosition(32.808298040181398, -17.140165027230978),
    		new GeoPosition(32.808358976617455, -17.140323026105762),
    		new GeoPosition(32.808523010462523, -17.140507008880377),
    		new GeoPosition(32.808683020994067, -17.14066400192678),
    		new GeoPosition(32.8087970148772, -17.140861982479692),
    		new GeoPosition(32.808875972405076, -17.141043031588197),
    		new GeoPosition(32.808992983773351, -17.14111796580255),
    		new GeoPosition(32.809171015396714, -17.141105979681015),
    		new GeoPosition(32.809176966547966, -17.141104973852634),
    		new GeoPosition(32.809309987351298, -17.141102040186524),
    		new GeoPosition(32.80941903591156, -17.141089970245957),
    		new GeoPosition(32.809482989832759, -17.141125006601214),
    		new GeoPosition(32.809478966519237, -17.14113799855113),
    		new GeoPosition(32.809382993727922, -17.141280993819237),
    		new GeoPosition(32.809260031208396, -17.141347965225577),
    		new GeoPosition(32.809257013723254, -17.141350982710719),
    		new GeoPosition(32.809156011790037, -17.141426000744104)
    		);

}