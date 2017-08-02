package pers.wuchao.action.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigureProperties {
	public static Map<String, Object[]> servletMap=null;
	public static Properties properties=null;
	
	static{
		servletMap=new HashMap<String, Object[]>();
		properties=new Properties();
		InputStream in=ConfigureProperties.class.getClassLoader().getResourceAsStream("app.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
