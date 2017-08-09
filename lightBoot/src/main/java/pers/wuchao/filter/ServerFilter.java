package pers.wuchao.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.wuchao.action.framework.ConfigureProperties;
//import pers.wuchao.listener.WebAppListener;

@WebFilter(urlPatterns={"*"})
public class ServerFilter implements Filter {
	
	private Map<String, Object[]> servletMap=null;
	private Properties appPros=null;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		servletMap=ConfigureProperties.servletMap;
		appPros=ConfigureProperties.properties;
		/*WebAppListener wl=new WebAppListener();
		wl.urlMappingConfig();*/
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse resp=(HttpServletResponse)response;
		
		setCharEncode(req, resp);
		String path=service(req, resp);
		
		if(path==null||path.equals("chain")||path.length()==0){
			chain.doFilter(request, response);
			return;
		}else{
			req.getRequestDispatcher("/"+path).forward(req, resp);
		}
	}
	
	private String service(HttpServletRequest req,HttpServletResponse resp){
		String servPath = req.getServletPath(); //得到请求servlet 地址
		Object[] array=servletMap.get(servPath); //根据地址获取servlet 相关参数
		if(array==null||array.length==0){
			return "chain";
		}
		
		try {
			Method method=(Method)array[0]; //获得方法
			Object obj=array[1]; //获得对象
			method.setAccessible(true);
			Object path= method.invoke(obj, req,resp);
			return path.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void setCharEncode(HttpServletRequest req,HttpServletResponse resp) throws UnsupportedEncodingException{
		//上下文参数设置POST　请求编码　处理post 编码
		String encode=appPros.getProperty("encode");
		if(encode==null){
			encode="utf-8";
		}
		req.setCharacterEncoding(encode);
		resp.setCharacterEncoding(encode);
	}
	
	public void destroy() {}
}
