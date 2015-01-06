package com.itspas.quickjson;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSON {
	public static Object parse(String text) {
		return _isEmtry(text) ? null : _parseJSON(text);
	}

	public static <T extends Object> T parse(String text, Class<T> clazz) {
		if (null == text || text.isEmpty() || null == clazz || clazz.isInterface() || !_isJSONObject(text))
			return null;
		return CaseUtils.objectValue(parseJSONObject(text), clazz);
	}

	public static JSONArray parseJSONArray(String text) {
		return (_isEmtry(text) || !_isJSONArray(text)) ? null : JSONArray.class.cast(_parseJSON(text));
	}

	public static JSONObject parseJSONObject(String text) {
		return (_isEmtry(text) || !_isJSONObject(text)) ? null : JSONObject.class.cast(_parseJSON(text));
	}

	public static String toJSONString(Object object) {
		if (null == object)
			return null;
		if (CaseUtils.isPrimitive(object.getClass())) {
			return String.class.isInstance(object) ? String.format("\"%s\"", object.toString()) : object.toString();
		} else if (object.getClass().isArray() || Collection.class.isInstance(object)) {
			return _toJSONStringArray(object);
		} else if (Map.class.isInstance(object)) {
			return _toJSONStringMap(object);
		} else {
			return _toJSONStringPojo(object);
		}
	}

	private static String _toJSONStringMap(Object object) {
		StringBuilder builder = new StringBuilder("{");
		Map<?, ?> map = Map.class.cast(object);
		Iterator<?> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Object k = iterator.next();
			if (CaseUtils.isPrimitive(k.getClass())) {
				Object v = map.get(k);
				String newK = toJSONString(k);
				builder.append(String.format("%s:%s,", newK.startsWith("\"") ? newK : String.format("\"%s\"", newK), toJSONString(v)));
			}
		}
		if (builder.charAt(builder.length() - 1) == ',')
			builder.setLength(builder.length() - 1);
		builder.append("}");
		return builder.toString();
	}

	private static String _toJSONStringArray(Object object) {
		if (Collection.class.isInstance(object))
			object = List.class.cast(object).toArray();
		int count = Array.getLength(object);
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < count; i++) {
			builder.append(String.format("%s,", toJSONString(Array.get(object, i))));
		}
		if (builder.charAt(builder.length() - 1) == ',')
			builder.setLength(builder.length() - 1);
		builder.append("]");
		return builder.toString();
	}

	private static String _toJSONStringPojo(Object object) {
		StringBuilder builder = new StringBuilder("{");
		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			if (!method.getName().startsWith("get") || method.getParameterTypes().length > 0 || method.getName().equals("getClass"))
				continue;
			String name = String.format("%s%s", Character.toLowerCase(method.getName().charAt(3)), method.getName().substring(4));
			try {
				Object result = method.invoke(object);
				if (null != result) {
					builder.append(String.format("\"%s\":%s,", name, toJSONString(result)));
				}
			} catch (Exception e) {
				continue;
			}
		}
		if (builder.charAt(builder.length() - 1) == ',')
			builder.setLength(builder.length() - 1);
		builder.append("}");
		return builder.toString();
	}

	private static boolean _isJSONObject(String text) {
		text = text.trim();
		return text.startsWith("{") && text.endsWith("}");
	}

	private static boolean _isJSONArray(String text) {
		text = text.trim();
		return text.startsWith("[") && text.endsWith("]");
	}

	private static boolean _isEmtry(String text) {
		return null == text || text.isEmpty();
	}

	private static Object _parseJSON(String text) {
		text.trim();
		Stack<String> stack = new Stack<String>();
		Stack<Object> objects = new Stack<Object>();
		Stack<String> keys = new Stack<String>();
		Stack<Boolean> types = new Stack<Boolean>();
		int idx = 0;
		int pos = 0;
		Object obj = null;
		do {
			switch (text.charAt(idx++)) {
			case '[':
			case '{':
				boolean isMap = text.charAt(idx - 1) == '{';
				if (stack.size() != 0 && stack.lastElement().equals("\"")) {
					break;
				}
				stack.push("}");
				types.push(isMap);
				if (null != obj)
					objects.push(obj);
				obj = _obtain(isMap ? JSONObject.class : JSONArray.class);
				pos = idx;
				break;
			case ']':
			case '}':
				if (pos != idx) {
					String value = text.substring(pos, idx - (text.charAt(idx - 2) == '"' ? 2 : 1)).trim();
					if (obj instanceof HashMap) {
						JSONObject.class.cast(obj).put(keys.pop(), value);
					} else {
						JSONArray.class.cast(obj).add(value);
					}
				}
				types.pop();
				stack.pop();
				if (objects.size() > 0) {
					if (objects.lastElement() instanceof HashMap) {
						JSONObject.class.cast(objects.lastElement()).put(keys.pop(), obj);
					} else {
						JSONArray.class.cast(objects.lastElement()).add(obj);
					}
					obj = objects.pop();
				}
				pos = idx + 1;
				break;
			case '"':
				if (stack.lastElement().equals("\"")) {
					stack.pop();
				} else {
					stack.push("\"");
					pos = idx;
				}
				break;
			case ',':
				if (pos != idx) {
					String value = text.substring(pos, idx - (text.charAt(idx - 2) == '"' ? 2 : 1)).trim();
					if (types.lastElement()) {
						JSONObject.class.cast(obj).put(keys.pop(), value);
					} else {
						JSONArray.class.cast(obj).add(value);
					}
				}
				pos = idx;
				break;
			case ':':
				keys.push(text.substring(pos, (pos = idx) - 2));
				break;
			}
		} while (idx < text.length());
		return obj;
	}

	private static <T extends Object> T _obtain(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
