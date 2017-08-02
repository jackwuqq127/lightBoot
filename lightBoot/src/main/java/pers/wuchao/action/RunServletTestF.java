package pers.wuchao.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.wuchao.action.annotation.Action;
import pers.wuchao.action.annotation.ActionlMapping;
import pers.wuchao.action.framework.FrameAction;


@Action(name="RunServlet",urlPatterns={"/run"})
public class RunServletTestF extends FrameAction {

	@ActionlMapping(urlPatterns={"/show"})
	protected String show(HttpServletRequest req, HttpServletResponse resp) throws InterruptedException {
		String sql="select * from emp";
		List<Map<String, Object>> list=daoTool.queryForList(sql);
		req.setAttribute("list", list);
		return "show.jsp";
	}
	
	
	@ActionlMapping(urlPatterns={"/deptList"})
	protected String show2(HttpServletRequest req, HttpServletResponse resp){
		
		String sql="select * from dept";
		
		List<Map<String, Object>> list=daoTool.queryForList(sql);
		req.setAttribute("list", list);
		return "dept/list.jsp";
	}

}