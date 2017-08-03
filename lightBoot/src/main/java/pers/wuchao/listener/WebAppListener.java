package pers.wuchao.listener;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import pers.wuchao.action.annotation.Action;
import pers.wuchao.action.annotation.ActionlMapping;
import pers.wuchao.action.framework.ConfigureProperties;
@WebListener
public class WebAppListener implements ServletContextListener {

	private Properties appPro=null;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent app) {
		System.out.println("demo");
		appPro=ConfigureProperties.properties;
		String scanPackageConfig=appPro.getProperty("scanBasePackage");
		
		String baseStr="/"+scanPackageConfig.replaceAll("\\.","/");
		URL url=this.getClass().getResource(baseStr);
		if(url==null){
			try {
				throw new Exception("基础扫描包=>"+scanPackageConfig+"不存在，请检查：scanBasePackage 属性");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		};
		File f=new File(url.getPath());
		
		if(url.getProtocol().equals("file")){
			String basepath=getBasePath(f);
			scanPath(url.getPath(),basepath);
		}
	}
	
	//得到基础路径
	private String getBasePath(File file){
		if(file.getPath().endsWith("classes")){
			return file.getPath();
		}
		return getBasePath(file.getParentFile());
	}
	
	//扫描路径，配置访问
	private void scanPath(String path,String urlPath){
		File f=new File(path);
		if(f.isFile()&&f.getPath().indexOf("$")==-1){
			String className=f.getPath().substring(urlPath.length()+1,f.getPath().length()-6).replace("\\",".");
			configMap(className);
		}else{
			File[] listFile=f.listFiles();
			if(listFile!=null&&listFile.length>0){
				for (File file : listFile) {
					if(file.getName().endsWith("annotation")||file.getName().endsWith("framework")){
						continue;
					}
					System.out.println(file.getName());
					scanPath(file.getPath(),urlPath);
				}
			}
		}
	}
	
	private void configMap(String className){
		try {
			Class<?> c = Class.forName(className);
			boolean haveInstance=false;
			Object obj=null;
			Action w=c.getAnnotation(Action.class); //获取类注解
			Method[] methods = c.getDeclaredMethods(); //获取该类方法
			if(w!=null){
				for (Method method : methods) {
					ActionlMapping[] abt = method.getAnnotationsByType(ActionlMapping.class); //获取方法注解
					if(abt==null||abt.length==0){
						continue;
					}
					if(!haveInstance){
						obj=c.newInstance();
						haveInstance=true;
					}
					
					for (String c_url:w.urlPatterns() ) { //类的URL
						if(abt==null||abt.length==0){
							continue;
						}
						for (String m_url : abt[0].urlPatterns()) { //方法URL
							setUrlMapping(method, obj, c_url, m_url);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void setUrlMapping(Method method,Object obj,String c_url,String m_url){
		Object[] array=new Object[2];
		array[0]=method;
		array[1]=obj;
		ConfigureProperties.servletMap.put(c_url+m_url,array);
	}
	
}
