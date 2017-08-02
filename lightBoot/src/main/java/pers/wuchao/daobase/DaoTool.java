package pers.wuchao.daobase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import pers.wuchao.datasource.DBHelper;

public class DaoTool extends DBHelper {

	//返回行
	public Map<String, Object> queryForMap(String sql,Object...params){
		return map(sql, params);
	}
	
	//返回多行
	public List<Map<String, Object>> queryForList(String sql, Object... params) {
		return query(sql, params);
	}
	
	
	private List<Map<String, Object>> query(String sql, Object[] params) {
		List<Map<String, Object>> list = null;
		try {
			list = queryRunner.query(sql, new MapListHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private Map<String, Object> map(String sql, Object[] params){
		try {
			return queryRunner.query(sql, new MapHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
