package org.wind.sso.client.util;

import java.io.Closeable;

/**
 * @描述 : 系统重要工具类
 * @版权 : 胡璐璐
 * @时间 : 2020年11月24日 18:11:08
 */
public final class _SystemUtil {

	
	/**清除左右空格的数据，如果长度为0，则返回null**/
	public static String clearEmpty(String str){
		return str!=null && str.trim().length()>0?str.trim():null;
	}
	/**关闭流**/
	public static void close(Closeable... objArr){
		if(objArr!=null && objArr.length>0){
			for(Closeable t_obj:objArr){
				if(t_obj!=null){try{t_obj.close();}catch(Exception e){e.printStackTrace();}}
			}
		}
	}
	
}