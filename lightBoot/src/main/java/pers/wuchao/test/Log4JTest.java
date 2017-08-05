package pers.wuchao.test;

import org.apache.log4j.Logger;

public class Log4JTest {
	private static Logger logger = Logger.getLogger(Log4JTest.class);

	public static void main(String[] args) {
		
		System.out.println("+++++");
		
		// System.out.println("This is println message.");
		// 记录debug级别的信息
		logger.debug("This is 调试");
		// 记录info级别的信息
		logger.info("This is 信息");
		// 记录error级别的信息
		logger.error("This is 错误");
		
		try {
			int a=1/0;
		} catch (Exception e) {
			logger.error(e,e.fillInStackTrace());
		}
	}
	
	
	
}
