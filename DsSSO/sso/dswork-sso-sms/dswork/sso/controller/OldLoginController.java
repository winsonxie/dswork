package dswork.sso.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OldLoginController
{
	/**
	 * 应用使用短信验证码
	 */
	@RequestMapping("/login")
	public void login(HttpServletResponse response)
	{
		try
		{
			response.sendRedirect("/portal");
		}
		catch(Exception e)
		{
		}
	}
}
