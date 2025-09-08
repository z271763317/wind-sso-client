package org.wind.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wind.sso.client.bean.SSO;
import org.wind.sso.client.config.URL;
import org.wind.sso.client.proxy.SessionProxy;

/**
 * @描述 : SSO工具类
 * @数据格式 : json
 * @作者 : 胡璐璐
 * @时间 : 2020年11月30日 16:30:58
 */
@SuppressWarnings("unchecked")
public final class SSOUtil {
	
	/**将SSOService对象存储到HttpServletRequest里**/
	public static final String requestAttribute="_wind-sso-client:request_sso";
	/*****************sso里的key列表*****************/
	public static final String key_ssoId="ssoId";			//ssoId
	public static final String key_session="session";		//session数据
	public static final String key_expiry="expiry";		//过期时间
	
	/**获取 : 当前会话的ssoId（从当前cookie里获取）**/
	public static String getSsoId(HttpServletRequest request){
		Cookie cookieArr[]=request.getCookies();
		String t_cookie_name=SSO.cookie_ssoId_name;
		for(int i=0;cookieArr!=null && i<cookieArr.length;i++){
			Cookie c=cookieArr[i];
			String t_name=c.getName();
			//指定cookie名
			if(t_name!=null && t_name.equals(t_cookie_name)){
				return c.getValue();
			}
		}
		return null;
	}
	/*********************会话*********************/
	/**获取 : SSO（缓存，已生成的）**/
	public static SSO getSSO(HttpServletRequest request){
		return (SSO) request.getAttribute(requestAttribute);
	}
	/**获取 : SSO（本工具类获取客户端ip式）**/
	public static SSO getSSO(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return getSSO(request, response, _ToolUtil.getRequestIP(request));
	}
	/**获取 : SSO（ip=客户端ip）**/
	public static SSO getSSO(HttpServletRequest request,HttpServletResponse response,String ip) throws Exception{
		SSO ssoObj=(SSO) request.getAttribute(requestAttribute);
		if(ssoObj==null && ip!=null) {
			String ssoId=getSsoId(request);
			if(ssoId!=null && ssoId.length()>0) {
				String url=_SysConstant.sso_server_url+URL.user_getSSO;
				Map<Object,Object> paramMap=new HashMap<Object, Object>();
				paramMap.put("ssoId", ssoId);
				paramMap.put("ip", ip);
				//
				Map<String,Object> resultMap = null;
				try {resultMap=SSOUtil.post(url, paramMap);}catch(Exception e) {}
				if(resultMap!=null) {
					Map<String, String> ssoMap=(Map<String, String>) resultMap.get("sso");
					String newSsoId=ssoMap.get(key_ssoId);		//新的ssoId（如果有，则代表重新登录）
					String session=ssoMap.get(key_session);
					int expiry=Integer.parseInt(ssoMap.get(key_expiry));		//新的ssoId（如果有，则代表重新登录）
					//
					SessionProxy sessionProxy=null;
					Object sessionObj=null;
					//是json（伪）
					if(session.indexOf("{")==0) {
						Map<Object, Object> map_source=_JsonUtil.toObject(session, Map.class);
						//是json（真）
						if(map_source!=null) {
							sessionProxy=new SessionProxy(map_source);
							sessionObj=(Map<Object, Object>) Proxy.newProxyInstance(SessionProxy.class.getClassLoader(), map_source.getClass().getInterfaces(),sessionProxy);
						}else{
							sessionObj=session;	
						}
					}else{
						sessionObj=session;
					}
					//新ssoId
					if(newSsoId!=null) {
						SSOUtil.addCookie_ssoId(response, newSsoId, expiry);		//更新新的ssoId
						ssoObj=new SSO(newSsoId,sessionProxy, sessionObj);
					}else{
						SSOUtil.addCookie_ssoId(response, ssoId,expiry);		//延长ssoId的cookie失效时间
						ssoObj=new SSO(ssoId,sessionProxy, sessionObj);
					}
					request.setAttribute(requestAttribute, ssoObj);
				}
			}
		}
		return ssoObj;
	}
	/**保存 : 当前的会话数据到SSO中心**/
	public static void saveSession(HttpServletRequest request){
		SSO obj=getSSO(request);
		if(obj!=null) {
			SessionProxy sessionProxy=obj.getSessionProxy();
			if(sessionProxy!=null) {
				Map<Object,Map<String,Object>> handlerKeyMap=sessionProxy.getHandlerKeyMap();
				if(handlerKeyMap!=null && handlerKeyMap.size()>0) {
					String handlerSession=_JsonUtil.toJson(handlerKeyMap);
					String url=_SysConstant.sso_server_url+URL.user_saveSession;
					Map<Object,Object> paramMap=new HashMap<Object, Object>();
					paramMap.put("ssoId", obj.getSsoId());
					paramMap.put("handlerSession", handlerSession);
					//
					try {
						SSOUtil.post(url, paramMap);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	
	/*********************其他*********************/
	//通用处理
	private static <R extends Object> Map<String,R> post(String url,Map<Object,Object> paramMap) throws Exception{
		String html = _HttpUtil.post(url, paramMap, null);
		if(html!=null) {
			Map<String,Object> resultMap= _JsonUtil.toObject(html, Map.class);
			if(resultMap!=null && resultMap.size()>0) {
				Integer code=(Integer) resultMap.get("code");
				if(code!=null && code.equals(1)) {
					return (Map<String, R>) resultMap;
				}else{
					throw new RuntimeException((String) resultMap.get("message"));
				}
			}else{
				throw new RuntimeException("SSO接口异常，没有数据返回");
			}
		}else{
			throw new RuntimeException("请求SSO接口异常");
		}
	}
	/**添加 : Cookie信息——ssoId（原先的被覆盖）**/
	public static void addCookie_ssoId(HttpServletResponse response,String ssoId,int expiry) throws UnsupportedEncodingException{
		Cookie cookie=new Cookie(SSO.cookie_ssoId_name, ssoId);
		cookie.setMaxAge(expiry);
		cookie.setPath(SSO.cookie_ssoId_path);
		cookie.setDomain(SSO.domain);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	/**是否登录**/
	public static boolean isLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SSO obj=getSSO(request, response);
		if(obj!=null) {
			return true;
		}
		return false;
	}
	/**退出（HttpServletRequest式）**/
	public static void exit(HttpServletRequest request,HttpServletResponse response){
		/*删除cookie*/
		Cookie cookie = new Cookie(SSO.cookie_ssoId_name, null);
		cookie.setPath(SSO.cookie_ssoId_path);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		/*删除SSO中心的session*/
		try {
			String ssoId=SSOUtil.getSsoId(request);
			String url=_SysConstant.sso_server_url+URL.user_exit;
			Map<Object,Object> paramMap=new HashMap<Object, Object>();
			paramMap.put("ssoId", ssoId);
			SSOUtil.post(url, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**获取 : 登录页面URL**/
	public static String getLoginPageUrl() {
		String loginUrl=_SysConstant.sso_login_url;
		if(loginUrl==null || loginUrl.trim().length()>0) {
			try {
				String url=_SysConstant.sso_server_url+URL.user_getLoginUrl;
				Map<String, Object> resultMap=SSOUtil.post(url, null);
				if(resultMap!=null) {
					loginUrl=(String) resultMap.get("href");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return loginUrl;
	}
	/**获取 : 登录页面URL（带源链接URL参数）**/
	public static String getLoginPageUrlParam(HttpServletRequest request) {
		return getLoginPageUrlParam(request, null, null);
	}
	/**获取 : 登录页面URL（带源链接URL参数，可指定源链接和追加param参数）**/
	public static String getLoginPageUrlParam(HttpServletRequest request,String referer,String param) {
		StringBuffer url=new StringBuffer(getLoginPageUrl());
		if(referer==null) {
			referer=_ToolUtil.getRequestURL(request);
		}
		if(url.indexOf("?")!=-1) {
			url.append("&referer=");
		}else{
			url.append("?referer=");
		}
		url.append(referer);
		if(param!=null) {
			url.append("&").append(param);
		}
		return url.toString();
	}
	/**重定向至SSO登录页面（get式，param=url后面追加的参数串，如：t1=123&t2=abc&t3=df21d31f）**/
	public static void redirectLoginPage(HttpServletRequest request,HttpServletResponse response,String referer,String param){
		String url=getLoginPageUrlParam(request, referer, param);
		try {
			response.setCharacterEncoding("UTF-8");
			response.sendRedirect(url.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}