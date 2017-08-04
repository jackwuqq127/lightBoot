package pers.wuchao.test;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class MainTest {
	@Test
	public void test(){
		URL url=this.getClass().getResource("/pers");
		String prptocol=url.getProtocol(); //获取协议名称
		File f=new File(url.getPath());
		System.out.println(f.getParentFile().getPath());
		if(prptocol.equals("file")){
			scanPath(url.getPath(),f.getParentFile().getPath());
		}
	}
	
	public void scanPath(String path,String urlPath){
		File f=new File(path);
		if(f.isFile()&&f.getPath().indexOf("$")==-1){
			String classNmae=f.getPath().substring(urlPath.length()+1,f.getPath().length()-6);
			System.out.println(classNmae);
		}else{
			File[] listFile=f.listFiles();
			if(listFile!=null&&listFile.length>0){
				for (File file : listFile) {
					scanPath(file.getPath(),urlPath);
				}
			}
		}
	}
}
