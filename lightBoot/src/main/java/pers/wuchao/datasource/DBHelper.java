package pers.wuchao.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import pers.wuchao.action.framework.ConfigureProperties;



public class DBHelper {
	private static Logger log=Logger.getLogger(DBHelper.class);
	private static Properties info=new Properties();
	
	protected Connection con=null;
	protected PreparedStatement ps=null;
	protected ResultSet rs=null;
	
	public static DataSource dataSource;
	protected static QueryRunner queryRunner;
	
	static{
		try {
			info.putAll(ConfigureProperties.properties);
			dataSource=DruidDataSourceFactory.createDataSource(info);
			queryRunner=new QueryRunner(dataSource);
		} catch (Exception e) {
			log.error(e,e.fillInStackTrace());
		}
	}
	
	protected Connection getCon(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e,e.fillInStackTrace());
		}
		return null;
	}
	
	protected void close(){
		try {
			if(rs!=null){
				rs.close();rs=null;
			}
			if(ps!=null){
				ps.close();ps=null;
			}
			if(con!=null){
				con.close();con=null;
			}
		} catch (SQLException e) {
			log.error(e,e.fillInStackTrace());
		}
	}
}
