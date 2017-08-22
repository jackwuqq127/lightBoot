package pers.wuchao.action.framework;

public class Redirect {
	private String paht;
	private static Redirect redirect;
	private Redirect(){}
	
	static{
		redirect=new Redirect();
	}
	
	public static Redirect path(String path){
		redirect.paht=path;
		return redirect;
	}

	public String getPaht() {
		return paht;
	}
	
}
