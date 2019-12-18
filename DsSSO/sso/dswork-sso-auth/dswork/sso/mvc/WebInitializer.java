package dswork.sso.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class WebInitializer implements WebApplicationInitializer
{
	@Override
	public void onStartup(ServletContext context) throws ServletException
	{
		//context.addListener("dswork.sso.common.listener.SessionListener");
		javax.servlet.ServletRegistration.Dynamic ssoServlet = context.addServlet("ssoServlet", "org.springframework.web.servlet.DispatcherServlet");
		ssoServlet.setLoadOnStartup(0);
		ssoServlet.setInitParameter("contextConfigLocation", "classpath*:/dswork/sso/mvc/sso-servlet.xml");
		ssoServlet.addMapping(
			 "/user/access_token"   // 后端获取access_token
			,"/user/auth_token"     // 前端检查用户凭证(access_token)是否还有效
			,"/user/authorize"      // 前端授权页面
			,"/user/login"          // 前端登入认证
			,"/user/register"       // 前端注册
			,"/user/logout"         // 前端登出认证，即取消用户凭证
			,"/user/redirect"       // 授权后访问重定向地址
			,"/user/userinfo"       // 前端账户信息
			,"/user/update/userinfo"// 更新账户信息
			,"/user/update/password"// 更新账户密码
			,"/user/update/account" // 更新账户账号
			
			,"/unit/access_token"   // 获取应用凭证(access_token)
			,"/unit/auth_token"     // 后端检查应用凭证(access_token)是否还有效
			
			,"/sms/code"            // 前端授权时获取短信验证码
			
			// 组织机构及用户相关api
			,"/api/getOrg"
			,"/api/queryOrgByOrgParent"
			,"/api/getUser"
			,"/api/getUserByOpenid"
			,"/api/queryUserByOrgParent"
			,"/api/queryUserByOrg"
			
			// 门户权限相关api
			,"/api/getSystem"
			,"/api/getSystemByUser"
			,"/api/getFunctionBySystem"
			,"/api/getFunctionByUser"


			// 以下是旧的地址拦截
			,"/login"
			,"/logout"
			,"/password", "/passwordAction"
		);
	}
}
