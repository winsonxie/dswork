package dswork.sso.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.IUnit;
import dswork.common.model.ZAuthtoken;
import dswork.common.util.CodeUtil;
import dswork.common.util.ResponseUtil;
import dswork.common.util.TokenUnitUtil;
import dswork.common.util.UnitUtil;
import dswork.web.MyRequest;

@Controller
@RequestMapping("/unit")
public class UnitController
{
	/**
	 * 根据code获取token
	 * @throws IOException
	 */
	@RequestMapping("/access_token")
	public void access_token(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String appsecret = req.getString("appsecret");
		String grant_type = req.getString("grant_type");
		try
		{
			if(!"".equals(appid) && !"".equals(appsecret)&& "client_credential".equals(grant_type))
			{
				IUnit app = UnitUtil.get(appid);
				if(null != app && appid.equals(app.getAppid()) && appsecret.equals(app.getAppsecret())) // 校验appid和appsecret
				{
					ZAuthtoken token = TokenUnitUtil.setUnitToken(appid);
					ResponseUtil.printJson(response, ResponseUtil.getJsonUnitToken(token));
				}
				else
				{
					ResponseUtil.printJson(response, CodeUtil.CODE_400);
				}
			}
			else
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_400);
			}
		}
		catch(Exception e)
		{
		}
	}

	@RequestMapping("/auth_token")
	public void auth_token(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		MyRequest req = new MyRequest(request);
		String appid = req.getString("appid");
		String access_token = req.getString("access_token");
		if(!"".equals(access_token) && !"".equals(appid))
		{
			IUnit app = UnitUtil.get(appid);
			if(null != app && TokenUnitUtil.checkUnitToken(appid, access_token)) // 校验appid和appsecret
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_001);
			}
			else
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_406);
			}
		}
		else
		{
			ResponseUtil.printJson(response, CodeUtil.CODE_400);
		}
	}
//	/**
//	 * 根据code获取token
//	 * @throws IOException
//	 */
//	@RequestMapping("/refresh_token")
//	public void refresh_token(HttpServletRequest request, HttpServletResponse response)
//	{
//		MyRequest req = new MyRequest(request);
//		String appid = req.getString("appid");
//		String appsecret = req.getString("appsecret");
//		String refresh_token = req.getString("refresh_token");
//		String grant_type = req.getString("grant_type");
//		try
//		{
//			if(!"".equals(appid) && !"".equals(appsecret)&& "refresh_token".equals(grant_type))
//			{
//				IUnit app = UnitUtil.get(appid);
//				if(null != app && appid.equals(app.getAppid()) && appsecret.equals(app.getAppsecret())) // 校验appid和appsecret
//				{
//					String v = TokenUnitUtil.setUnitToken(appid);
//					ResponseUtil.printJson(response, String.format(json, v, TokenUnitUtil.timeout_second));
//				}
//				else
//				{
//					ResponseUtil.printJson(response, CodeUtil.CODE_400);
//				}
//			}
//			else
//			{
//				ResponseUtil.printJson(response, CodeUtil.CODE_400);
//			}
//		}
//		catch(Exception e)
//		{
//		}
//	}
}
