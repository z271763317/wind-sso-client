wind-sso的sdk（客户端），java语言开发，适合java web项目引入。

1、配置文件（在【src/wind-sso-client.properties】）

    #配置来源（1=本地【wind-sso-client.properties】；2=远程sso服务【默认。url为sso_server_url值】）
    config_source=2
  
    #服务端根路径（如：http://127.0.0.1:8090/wind-sso）
    sso_server_url=http://127.0.0.1:8080/wind-sso
  
    #登录页面URL（若为空，则取sso_server_url值）
    #sso_login_url=http://127.0.0.1:8080/wind-sso
  
  
    ########## 【config_source为1】时生效 ##########
  
    #域名（cookie用）
    domain=127.0.0.1

    #认证服务器（URL。从auth_login的url获取）
    authUrl=http://127.0.0.1:8080/wind-bg
  
    #cooke_ssoId的name（默认：_wind-sso_ssoId）
    cookie_ssoId_name=_wind-sso_ssoId
  
    #cooke_ssoId的path（默认：/）
    cookie_ssoId_path=/


2、常用方法介绍：

    org.wind.sso.client.util.SSOUtil

  （1）、isLogin(HttpServletRequest request,HttpServletResponse response)：是否已登录

      返回：boolean。true=已登录；false=未登录

  （2）、getLoginPageUrl()：获取登录页面URL

      返回：登录页面URL

  （3）、getLoginPageUrlParam(HttpServletRequest request,String referer,String param)：获取 : 登录页面URL（带源链接URL参数，可指定源链接和追加param参数）

      参数：referer = 源链接（当前页面的URL。若为null，则会自动获取）；param=追加的参数（非必填）

      返回：获取带源链接和参数的登录页面URL

  （4）、exit(HttpServletRequest request,HttpServletResponse response)：退出（HttpServletRequest式）
  
  （5）、getSSO(HttpServletRequest request,HttpServletResponse response,String ip)：获取 : SSO（ip=客户端ip）

      参数：ip = 已登录的用户IP

      返回：SSO对象，结构：

        /******配置******/
      	public static String domain;				//域名
      	public static String authUrl;				//认证服务器（登录URL）
       
        /*************cookie*************/
        public static String cookie_ssoId_name;				//cookie的ssoId的name
        public static String cookie_ssoId_path;				//cookie的ssoId的path
      	
        private String ssoId;		//sso令牌（唯一标识）
      	private SessionProxy sessionProxy;			//session的代理对象
      	private Object session;		//session数据

  （6）、更多方法请查看该类
  
