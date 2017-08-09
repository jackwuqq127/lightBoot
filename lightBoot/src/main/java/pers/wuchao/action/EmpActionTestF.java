package pers.wuchao.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import pers.wuchao.action.annotation.Action;
import pers.wuchao.action.annotation.ActionlMapping;
import pers.wuchao.action.framework.FrameAction;

@Action
public class EmpActionTestF extends FrameAction {
	private Logger log=Logger.getLogger(this.getClass());
	
	@ActionlMapping(urlPatterns={"/emplist"})
	protected String show(HttpServletRequest req, HttpServletResponse resp) throws InterruptedException {
		String sql="select * from emp";
		List<Map<String, Object>> list=daoTool.queryForList(sql);
		req.setAttribute("list", list);
		return "show.jsp";
	}
}
