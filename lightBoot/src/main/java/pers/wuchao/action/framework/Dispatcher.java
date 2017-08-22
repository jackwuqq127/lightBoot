package pers.wuchao.action.framework;

public class Dispatcher { //内部转发
	private String path;
	
	private static Dispatcher dispatcher;
	private Dispatcher(){}
	static{
		dispatcher=new Dispatcher();
	}
	public static Dispatcher path(String path){
		dispatcher.path=path;
		return dispatcher;
	}
	public String getPath() {
		return path;
	}
}
