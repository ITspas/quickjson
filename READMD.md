QuickJson
---------
Java 操作json框架

```Java
String json = "{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"number\":123,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}";
JSONObject jsonObject =  JSON.parseJSONObject(json);
System.out.println(jsonObject);
System.out.println(jsonObject.getJSONArray("array").getIntValue(0));
System.out.println(jsonObject.getJSONObject("object").getStringValue("a"));
System.out.println(jsonObject.toJSONString());
```