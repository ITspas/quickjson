package com.itspas.quickjson;

import java.util.ArrayList;

public class JSONArray extends ArrayList<Object> {

	private static final long serialVersionUID = 1L;

	public Integer getIntValue(int i) {
		return CaseUtils.intValue(get(i));
	}

	public Integer getIntValue(int i, Integer value) {
		Integer t = getIntValue(i);
		return null == t ? value : t;
	}

	public Long getLongValue(int i) {
		return CaseUtils.longValue(i);
	}

	public Long getLongValue(int i, Long value) {
		Long t = getLongValue(i);
		return null == t ? value : t;
	}

	public Short getShortValue(int i) {
		return CaseUtils.shortValue(get(i));
	}

	public Short getShortValue(int i, Short value) {
		Short t = getShortValue(i);
		return null == t ? null : t;
	}

	public Float getFloatValue(int i) {
		return CaseUtils.floatValue(get(i));
	}

	public Float getFloatValue(int i, Float value) {
		Float t = getFloatValue(i);
		return null == t ? null : t;
	}

	public Double getDoubleValue(int i) {
		return CaseUtils.doubleValue(get(i));
	}

	public Double getDoubleValue(int i, Double value) {
		Double t = getDoubleValue(i);
		return null == t ? null : t;
	}

	public Byte getByteValue(int i) {
		return CaseUtils.byteValue(get(i));
	}

	public Byte getByteValue(int i, Byte value) {
		Byte t = getByteValue(i);
		return null == t ? null : t;
	}

	public Boolean getBoolValue(int i) {
		return CaseUtils.booleanValue(get(i));
	}

	public Boolean getBoolValue(int i, Boolean value) {
		Boolean t = getBoolValue(i);
		return null == t ? null : t;
	}

	public Character getCharValue(int i) {
		return CaseUtils.charValue(get(i));
	}

	public Character getCharValue(int i, Character value) {
		Character t = getCharValue(i);
		return null == t ? null : t;
	}

	public String getStringValue(int i) {
		return CaseUtils.stringValue(get(i));
	}

	public String getStringValue(int i, String value) {
		String t = getStringValue(i);
		return null == t ? null : t;
	}

	public JSONObject getJSONObject(int i) {
		Object obj = get(i);
		return obj instanceof JSONObject ? JSONObject.class.cast(obj) : null;
	}

	public JSONObject getJSONObject(int i, JSONObject value) {
		JSONObject t = getJSONObject(i);
		return null == t ? null : t;
	}

	public JSONArray getJSONArray(int i) {
		Object obj = get(i);
		return obj instanceof JSONArray ? JSONArray.class.cast(obj) : null;
	}

	public JSONArray getJSONArray(int i, JSONArray value) {
		JSONArray t = getJSONArray(i);
		return null == t ? null : t;
	}

	public String toJSONString() {
		return JSON.toJSONString(this);
	}

	public String toString() {
		return toJSONString();
	}

}
