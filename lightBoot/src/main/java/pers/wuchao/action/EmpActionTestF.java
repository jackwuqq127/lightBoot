package pers.wuchao.action;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import pers.wuchao.action.annotation.Action;
import pers.wuchao.action.annotation.ActionlMapping;
import pers.wuchao.action.framework.Dispatcher;
import pers.wuchao.action.framework.FrameAction;
import pers.wuchao.action.framework.JsonString;

@Action
public class EmpActionTestF extends FrameAction {
	@SuppressWarnings("unused")
	private Logger log=Logger.getLogger(this.getClass());
	
	@ActionlMapping(urlPatterns={"/emplist"})
	protected Dispatcher show() throws InterruptedException {
		
		String empno=pget("empno");
		String sql="select * from emp e where e.empno=?";
		List<Map<String, Object>> list=daoTool.queryForList(sql,empno);
		req.setAttribute("list", list);
		return Dispatcher.path("show.jsp");
	}
	
	@ActionlMapping(urlPatterns={"/empJson"})
	protected JsonString getEmpnoJson(){
		String sql="select * from emp e";
		List<Map<String, Object>> list=daoTool.queryForList(sql);
		String json=JSON.toJSONString(list);
		return JsonString.str(json);
	}
}
