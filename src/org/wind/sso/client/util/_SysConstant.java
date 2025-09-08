package org.wind.sso.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @描述 : 系统常量类
 * @作者 : 胡璐璐
 * @时间 : 2019年11月26日 19:26:10
 */
public final class _SysConstant{
	
    public static final String SYS_PROPERTIES = "wind-sso-client.properties";		//文件路径（默认工程根目录下）
    /*配置*/
    public static final int conTimeout=Integer.valueOf(getProperty("conTimeout",(10*1000)+""));			//http连接超时
    public static final int readTimeout=Integer.valueOf(getProperty("readTimeout",(60*1000)+""));		//http读取超时
    
    /*************sso配置*************/
    public static final int config_source=Integer.valueOf(getProperty("config_source","2"));				//配置来源（1=本地；2=远程sso服务器）
    /*config_source=1时生效*/
    public static final String domain=getProperty("domain");			//域名
    public static final String authUrl=getProperty("authUrl");			//认证服务器（URL。从auth_login的url获取）
    public static final String cookie_ssoId_name=getProperty("cookie_ssoId_name","_wind-sso_ssoId");			//cooke_ssoId的name（默认：_wind-sso_ssoId）
    public static final String cookie_ssoId_path=getProperty("cookie_ssoId_path","/");			//cooke_ssoId的path（默认：/）
    
    /*config_source=2时生效*/
    public static final String sso_server_url=getProperty("sso_server_url");			//服务端URL根路径
    public static final String sso_login_url=getProperty("sso_login_url");			//登录页面URL（若为空，则取sso_server_url值）

	public static final String getProperty(String name, String defaultValue) {
		return getPropertyFromFile(SYS_PROPERTIES, name,defaultValue);
	}
	public static final String getProperty(String name) {
		return getPropertyFromFile(SYS_PROPERTIES, name);
	}
	public static String getPropertyFromFile(String fullFileName,String propertyName, String defaultValue) {
		Properties p = new Properties();
		InputStream in = null;
		try {
			in = getFileInputStremByFullName(fullFileName);
			p.load(in);
			return p.getProperty(propertyName, defaultValue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static InputStream getFileInputStremByFullName(String fullName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(fullName);
	}
	public static String getPropertyFromFile(String fullFileName,String propertyName) {
		return getPropertyFromFile(fullFileName, propertyName, null);
	}
}