package pers.wuchao.action.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigureProperties {
	private static Logger log=Logger.getLogger(ConfigureProperties.class);
	public static Map<String, Object[]> servletMap=null;
	public static Properties properties=null;
	
	static{
		servletMap=new HashMap<String, Object[]>();
		properties=new Properties();
		InputStream in=ConfigureProperties.class.getClassLoader().getResourceAsStream("app.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error(e,e.fillInStackTrace());
		}
	}
}
