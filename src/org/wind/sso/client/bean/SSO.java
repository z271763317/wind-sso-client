package org.wind.sso.client.bean;

import org.wind.sso.client.proxy.SessionProxy;

/**
 * @描述 : SSO对象（存储了跟SSO相关的数据）
 * @作者 : 胡璐璐
 * @时间 : 2021年4月6日 21:48:32
 */
public class SSO {

	/******配置******/
	public static String domain;				//域名
	public static String authUrl;				//认证服务器（登录URL）
    /*************cookie*************/
    public static String cookie_ssoId_name;				//cookie的ssoId的name
    public static String cookie_ssoId_path;				//cookie的ssoId的path
	
    private String ssoId;		//sso令牌（唯一标识）
	private SessionProxy sessionProxy;			//session的代理对象
	private Object session;		//session数据
	
	public SSO(String ssoId,SessionProxy sessionProxy,Object session) {
		this.ssoId=ssoId;
		this.sessionProxy=sessionProxy;
		this.session=session;
	}
	public SessionProxy getSessionProxy() {
		return sessionProxy;
	}
	public void setSessionProxy(SessionProxy sessionProxy) {
		this.sessionProxy = sessionProxy;
	}
	public Object getSession() {
		return session;
	}
	public String getSsoId() {
		return ssoId;
	}
	
}
