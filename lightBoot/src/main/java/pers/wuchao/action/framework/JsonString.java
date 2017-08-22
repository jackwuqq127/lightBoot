package pers.wuchao.action.framework;

public class JsonString {
	private static JsonString jsonString;
	private String str;
	private JsonString(){}
	public String getStr() {
		return str;
	}
	
	static{
		jsonString=new JsonString();
	}
	
	public static JsonString str(String str){
		jsonString.str=str;
		return jsonString;
	}
}
