package pers.wuchao.action.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pers.wuchao.daobase.DaoTool;

public class FrameAction {
	protected DaoTool daoTool=new DaoTool();
	
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected HttpSession session;
	
	protected String pget(String name){
		return req.getParameter(name);
	}
	
	protected String[] pgetValues(String name){
		return req.getParameterValues(name);
	}
}
