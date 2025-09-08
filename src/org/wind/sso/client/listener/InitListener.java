package org.wind.sso.client.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.wind.sso.client.bean.SSO;
import org.wind.sso.client.time.TimeTask_config;
import org.wind.sso.client.util._SysConstant;

/**
 * @描述：web项目监听器（启动前、后的处理）
 * @版权：胡璐璐
 * @时间：2020年10月31日 13:49:41
 */
@WebListener
public class InitListener implements ServletContextListener {
	
	/**启动前：InitServlet首先执行它，启动前的处理*/
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			this.initTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**关闭前：关闭前的处理**/
	public void contextDestroyed(ServletContextEvent arg0) {
	
	}
	//初始化 : 定时任务
	private void initTime() throws Exception {
		String config_source_str=null;
		switch(_SysConstant.config_source) {
			case 1 : config_source_str="本地";break;
			case 2 : config_source_str="远程SSO服务器";break;
			default:throw new RuntimeException("参数【config_source】不支持的类型："+_SysConstant.config_source);
		}
		System.out.println("-----获取SSO配置（配置来源："+config_source_str+"）-----");
		TimeTask_config.getConfig();
		System.out.println("domain："+SSO.domain);
		System.out.println("cookie_ssoId：name="+SSO.cookie_ssoId_name+"；path="+SSO.cookie_ssoId_path);
		System.out.println("authUrl："+SSO.authUrl);
		
	}
}