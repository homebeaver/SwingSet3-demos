package org.jxmapviewer.demos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

// in https://github.com/osmandapp/OsmAnd/blob/998a7642d813907c33004d97e149a5c4e238a3ea/OsmAnd-java/src/main/java/net/osmand/router/network/NetworkRouteSelector.java ab 713
// inner public static class RouteKey { ...
public class RouteKey {
	
	public static final String ROUTE_KEY_VALUE_SEPARATOR = "__";
	public static final String NETWORK_ROUTE_TYPE = "type";
	
	public enum RouteType {

		HIKING("hiking"),
		BICYCLE("bicycle"),
		MTB("mtb"),
		HORSE("horse");

		private final String tag;
		private final String tagPrefix;

		RouteType(String tag) {
			this.tag = tag;
			this.tagPrefix = "route_" + tag + "_";
		}

		public String getTag() {
			return tag;
		}

		public static RouteType getByTag(String tag) {
			for (RouteType routeType : values()) {
				if (routeType.tag.equals(tag)) {
					return routeType;
				}
			}
			return null;
		}

//		public static List<RouteKey> getRouteKeys(RouteDataObject obj) {
//			Map<String, String> tags = new TreeMap<>();
//			for (int i = 0; obj.nameIds != null && i < obj.nameIds.length; i++) {
//				int nameId = obj.nameIds[i];
//				String value = obj.names.get(nameId);
//				RouteTypeRule rt = obj.region.quickGetEncodingRule(nameId);
//				if (rt != null) {
//					tags.put(rt.getTag(), value);
//				}
//			}
//			for (int i = 0; obj.types != null && i < obj.types.length; i++) {
//				RouteTypeRule rt = obj.region.quickGetEncodingRule(obj.types[i]);
//				if (rt != null) {
//					tags.put(rt.getTag(), rt.getValue());
//				}
//			}
//			return getRouteKeys(tags);
//		}
//
//
//		public static List<RouteKey> getRouteKeys(RenderedObject renderedObject) {
//			return getRouteKeys(renderedObject.getTags());
//		}
//
//		public static List<RouteKey> getRouteKeys(BinaryMapDataObject bMdo) {
//			Map<String, String> tags = new TreeMap<>();
//			for (int i = 0; i < bMdo.getObjectNames().keys().length; i++) {
//				int keyInd = bMdo.getObjectNames().keys()[i];
//				TagValuePair tp = bMdo.getMapIndex().decodeType(keyInd);
//				String value = bMdo.getObjectNames().get(keyInd);
//				if (tp != null) {
//					tags.put(tp.tag, value);
//				}
//			}
//			int[] tps = bMdo.getAdditionalTypes();
//			for (int i = 0; i < tps.length; i++) {
//				TagValuePair tp = bMdo.getMapIndex().decodeType(tps[i]);
//				if (tp != null) {
//					tags.put(tp.tag, tp.value);
//				}
//			}
//			tps = bMdo.getTypes();
//			for (int i = 0; i < tps.length; i++) {
//				TagValuePair tp = bMdo.getMapIndex().decodeType(tps[i]);
//				if (tp != null) {
//					tags.put(tp.tag, tp.value);
//				}
//			}
//			return getRouteKeys(tags);
//		}
//
//		private static int getRouteQuantity(Map<String, String> tags, RouteType rType) {
//			int q = 0;
//			for (String tag : tags.keySet()) {
//				if (tag.startsWith(rType.tagPrefix)) {
//					int num = Algorithms.extractIntegerNumber(tag);
//					if (num > 0 && tag.equals(rType.tagPrefix + num)) {
//						q = Math.max(q, num);
//					}
//				}
//			}
//			return q;
//		}
//
//		public static List<RouteKey> getRouteKeys(Map<String, String> tags) {
//			List<RouteKey> lst = new ArrayList<RouteKey>();
//			for (RouteType routeType : RouteType.values()) {
//				int rq = getRouteQuantity(tags, routeType);
//				for (int routeIdx = 1; routeIdx <= rq; routeIdx++) {
//					String prefix = routeType.tagPrefix + routeIdx;
//					RouteKey routeKey = new RouteKey(routeType);
//					for (Map.Entry<String, String> e : tags.entrySet()) {
//						String tag = e.getKey();
//						if (tag.startsWith(prefix) && tag.length() > prefix.length()) {
//							String key = tag.substring(prefix.length() + 1);
//							routeKey.addTag(key, e.getValue());
//		
//				}
//					}
//					lst.add(routeKey);
//				}
//			}
//			return lst;
//		}
	}
	
	public final RouteType type;
	public final Set<String> tags = new TreeSet<>();

	public RouteKey(RouteType routeType) {
		this.type = routeType;
	}

	public String getValue(String key) {
		key = ROUTE_KEY_VALUE_SEPARATOR + key + ROUTE_KEY_VALUE_SEPARATOR;
		for (String tag : tags) {
			int i = tag.indexOf(key);
			if (i > 0) {
				return tag.substring(i + key.length());
			}
		}
		return "";
	}

	public String getKeyFromTag(String tag) {
		String prefix = "route_" + type.tag + ROUTE_KEY_VALUE_SEPARATOR;
		if (tag.startsWith(prefix) && tag.length() > prefix.length()) {
			int endIdx = tag.indexOf(ROUTE_KEY_VALUE_SEPARATOR, prefix.length());
			return tag.substring(prefix.length(), endIdx);
		}
		return "";
	}

	// aus https://github.com/osmandapp/OsmAnd/blob/998a7642d813907c33004d97e149a5c4e238a3ea/OsmAnd-java/src/main/java/net/osmand/util/Algorithms.java
	public class Algorithms {
		public static boolean isEmpty(CharSequence s) {
			return s == null || s.length() == 0;
		}
	}
	public void addTag(String key, String value) {
		value = Algorithms.isEmpty(value) ? "" : ROUTE_KEY_VALUE_SEPARATOR + value;
		tags.add("route_" + type.tag + ROUTE_KEY_VALUE_SEPARATOR + key + value);
	}

	public String getRouteName() {
		String name = getValue("name");
		if (name.isEmpty()) {
			name = getValue("ref");
		}
		return name;
	}

	public String getNetwork() {
		return getValue("network");
	}

	public String getOperator() {
		return getValue("operator");
	}

	public String getSymbol() {
		return getValue("symbol");
	}

	public String getWebsite() {
		return getValue("website");
	}

	public String getWikipedia() {
		return getValue("wikipedia");
	}

	public static RouteKey fromGpx(Map<String, String> networkRouteKeyTags) {
		String type = networkRouteKeyTags.get(NETWORK_ROUTE_TYPE);
		if (!Algorithms.isEmpty(type)) {
			RouteType routeType = RouteType.getByTag(type);
			if (routeType != null) {
				RouteKey routeKey = new RouteKey(routeType);
				for (Map.Entry<String, String> tag : networkRouteKeyTags.entrySet()) {
					routeKey.addTag(tag.getKey(), tag.getValue());
				}
				return routeKey;
			}
		}
		return null;
	}

	public Map<String, String> tagsToGpx() {
		Map<String, String> networkRouteKey = new HashMap<>();
		networkRouteKey.put(NETWORK_ROUTE_TYPE, type.tag);
		for (String tag : tags) {
			String key = getKeyFromTag(tag);
			String value = getValue(key);
			if (!Algorithms.isEmpty(value)) {
				networkRouteKey.put(key, value);
			}
		}
		return networkRouteKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tags.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteKey other = (RouteKey) obj;
		if (!tags.equals(other.tags))
			return false;
		return type == other.type;
	}


@Override
	public String toString() {
		return "Route [type=" + type + ", set=" + tags + "]";
	}
}
