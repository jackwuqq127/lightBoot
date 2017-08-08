package pers.wuchao.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static String pattern="yyyy-MM-dd";
	private static SimpleDateFormat sdf=new SimpleDateFormat(pattern);
	
	public static String getNowTimeString(){
		Date date=new Date();
		return sdf.format(date);
	}
}
