package org.wind.sso.client.time;

import java.util.Map;
import java.util.TimerTask;

import org.wind.sso.client.bean.SSO;
import org.wind.sso.client.config.URL;
import org.wind.sso.client.util._HttpUtil;
import org.wind.sso.client.util._JsonUtil;
import org.wind.sso.client.util._SysConstant;

/**
 * @描述 : 定时任务——获取配置
 * @规则 : 重复执行。默认：2分钟1次
 * @作者 : 胡璐璐
 * @时间 : 2020年11月26日 08:24:49
 */
@SuppressWarnings("unchecked")
public class TimeTask_config extends TimerTask{

	public void run() {
		getConfig();
	}
	//获取SSO数据
	public static void getConfig() {
		switch(_SysConstant.config_source) {
			//本地
			case 1:{
				SSO.domain=_SysConstant.domain;
				SSO.cookie_ssoId_name=_SysConstant.cookie_ssoId_name;
				SSO.cookie_ssoId_path=_SysConstant.cookie_ssoId_path;
				SSO.authUrl=_SysConstant.authUrl;;
				break;
			}
			//远程SSO服务器
			case 2:{
				String url=_SysConstant.sso_server_url+URL.config;
				try {
					String html=_HttpUtil.post(url, null, null);
					if(html!=null) {
						Map<String,Object> resultMap=_JsonUtil.toObject(html, Map.class);
						String domain=(String)resultMap.get("domain");
						String authUrl=(String)resultMap.get("authUrl");
						Map<Object,Object> cookieMap=(Map<Object, Object>) resultMap.get("cookie");
						Map<Object,Object> cookieMap_ssoId=(Map<Object, Object>) cookieMap.get("ssoId");
						String path=(String) cookieMap_ssoId.get("path");
						String name=(String) cookieMap_ssoId.get("name");
						//
						SSO.domain=domain;
						SSO.authUrl=authUrl;
						SSO.cookie_ssoId_name=name;
						SSO.cookie_ssoId_path=path;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			default:{throw new RuntimeException("参数【config_source】不支持的类型："+_SysConstant.config_source);}
		}
		
	}
}
