package common.web;

import javax.servlet.annotation.WebServlet;

@SuppressWarnings("serial")
@WebServlet(name="xxx", loadOnStartup=1, urlPatterns={"/authcode"})
public class AuthcodeServlet extends dswork.web.MyAuthCodeServlet
{
}
