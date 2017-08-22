package pers.wuchao.filter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import pers.wuchao.action.framework.Dispatcher;
import pers.wuchao.action.framework.JsonString;
import pers.wuchao.action.framework.Redirect;
import pers.wuchao.filter.frame.FrameFilter;

@WebFilter(urlPatterns={"*"})
public class ServerFilter extends FrameFilter {
	private Logger log=Logger.getLogger(ServerFilter.class);
	
	@Override
	public void service() throws IOException, ServletException {
		setCharEncode(req, resp);
		Object obj=excute(req, resp);
		String path;
		
		if(obj instanceof Boolean ){
			boolean f=(boolean) obj;
			if(f){
				chain.doFilter(req, resp);
			}
		}else if(obj instanceof Dispatcher ){
			Dispatcher d=(Dispatcher) obj;
			path=d.getPath();
			req.getRequestDispatcher("/"+path).forward(req, resp);
		}else if(obj instanceof Redirect){
			Redirect r=(Redirect) obj;
			path=r.getPaht();
			String contextPath = req.getContextPath();
			resp.sendRedirect(contextPath+"/"+path);
		}else if(obj instanceof JsonString){
			JsonString jsonString=(JsonString) obj;
			PrintWriter printWriter = resp.getWriter();
			printWriter.print(jsonString.getStr());
		}
	}
	
	private Object excute(HttpServletRequest req,HttpServletResponse resp){
		String servPath = req.getServletPath(); //得到请求servlet 地址
		Object[] array=servletMap.get(servPath); //根据地址获取servlet 相关参数
		if(array==null||array.length==0){
			return true;
		}
		
		try {
			Method method=(Method)array[0]; //获得方法
			Object obj=array[1]; //获得对象
			
			//赋值req
			setSupperField("req", obj, req);
			
			//赋值resp
			setSupperField("resp", obj, resp);
			
			//赋值session
			setSupperField("session", obj, req.getSession());
			
			method.setAccessible(true);
			Object getObj= method.invoke(obj); //执行action
			return getObj;
		} catch (Exception e) {
			log.error(e, e.fillInStackTrace());
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
	
	private void setSupperField(String name,Object obj,Object value) throws Exception{
		Field reqField=obj.getClass().getSuperclass().getDeclaredField(name);
		reqField.setAccessible(true);
		reqField.set(obj, value);
	}
}
