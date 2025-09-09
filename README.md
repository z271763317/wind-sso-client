wind-sso的sdk（客户端），java语言开发，适合java web项目引入。

1、配置文件

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

  （1）、SSOUtil.isLogin：是否已登录

  （2）、SSOUtil.getLoginPageUrl：获取登录页面URL

  （3）、SSOUtil.getLoginPageUrlParam：获取登录页面URL（带参数）

  （4）、SSOUtil.exit：退出（清除在redis里的会话信息）

  （5）、SSOUtil.getSSO：获取SSO对象（附带：ssoId会话唯一标识、、session数据、session的代理对象等）


  
