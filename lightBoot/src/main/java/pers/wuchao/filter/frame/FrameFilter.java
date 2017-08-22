package pers.wuchao.filter.frame;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pers.wuchao.action.framework.ConfigureProperties;

public abstract class FrameFilter implements Filter {
	
	protected FilterConfig filterConfig;
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected HttpSession session;
	protected FilterChain chain;
	
	protected Map<String, Object[]> servletMap=null;
	protected Properties appPros=null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig=filterConfig;
		servletMap=ConfigureProperties.servletMap;
		appPros=ConfigureProperties.properties;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.req=(HttpServletRequest) request;
		this.resp=(HttpServletResponse) response;
		this.session=req.getSession();
		this.chain=chain;
		service();
	}
	
	protected abstract void service() throws IOException, ServletException;

	@Override
	public void destroy() {}

}
