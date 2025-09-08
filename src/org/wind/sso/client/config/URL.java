package org.wind.sso.client.config;

/**
 * @描述 : sso的接口相对路径
 * @作者 : 胡璐璐
 * @时间 : 2020年11月24日 19:43:08
 */
public final class URL {

	/**********配置（config）**********/
	public static final String config="/config/";		//前缀
	
	/**********用户（user）**********/
	private static final String user="/user/";		//前缀
	public static final String user_getSSO=user+"getSSO";		//获取SSO
	public static final String user_saveSession=user+"saveSession";		//保存session数据
	public static final String user_exit=user+"exit";		//退出
	public static final String user_getLoginUrl=user+"getLoginUrl";		//获取登录url
	
}
