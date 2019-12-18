/**
 * 获取认证信息
 */
package dswork.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.SsoFactory;
import dswork.common.model.IFunc;
import dswork.common.model.IOrg;
import dswork.common.model.ISystem;
import dswork.common.model.IUser;
import dswork.common.util.ResponseUtil;
import dswork.common.util.TokenUnitUtil;
import dswork.web.MyRequest;

// getFunctionByOrg、queryPostByOrg
@Controller
@RequestMapping("/api")
public class ApiController
{
	private static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	private static com.google.gson.Gson gson = builder.create();
	private static String toJson(Object object)
	{
		if(object == null)
		{
			return null;
		}
		return gson.toJson(object);
	}

	private static boolean getAppCheck(MyRequest req)
	{
		String appid = req.getString("appid");
		String access_token = req.getString("access_token");
		return TokenUnitUtil.checkUnitToken(appid, access_token);
	}

	/**
	 * @note 获取组织机构
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param orgId 组织机构ID
	 * @return IOrg
	 */
	@RequestMapping("/getOrg")
	public void getOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IOrg org = null;
		if(getAppCheck(req))
		{
			String orgId = req.getString("orgId");
			org = SsoFactory.getSsoService().getOrgByOrgId(orgId);
		}
		ResponseUtil.printJson(response, toJson(org));
	}

	/**
	 * @note 获取下级组织机构(status:2单位,1部门,0岗位)
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param orgPid 组织机构ID，为0则取顶级
	 * @return IOrg[]
	 */
	@RequestMapping("/queryOrgByOrgParent")
	public void queryOrgByOrgParent(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IOrg[] list = null;
		if(getAppCheck(req))
		{
			String orgPid = req.getString("orgPid");
			list = SsoFactory.getSsoService().queryOrgByOrgPid(orgPid);
		}
		ResponseUtil.printJson(response, toJson(list));
	}

	 /**
	 * @note 获取指定用户的基本信息
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	 @RequestMapping("/getUser")// 要改成从access_token中获取
	public void getUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IUser user = null;
		if(getAppCheck(req))
		{
			String userAccount = req.getString("userAccount");
			user = SsoFactory.getSsoService().getUserByBmNotSecret(userAccount);
		}
		ResponseUtil.printJson(response, toJson(user));
	}

	 /**
	 * @note 获取指定用户的基本信息
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param userOpenid 用户标识
	 * @return IUser
	 */
	 @RequestMapping("/getUserByOpenid")// 要改成从access_token中获取
	public void getUserByOpenid(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IUser user = null;
		if(getAppCheck(req))
		{
			long openid = req.getLong("userOpenid");
			user = SsoFactory.getSsoService().getUserByOpenid(openid);
		}
		ResponseUtil.printJson(response, toJson(user));
	}

	/**
	 * @note 获取指定单位下的用户，不含子单位
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param orgPid 单位ID
	 * @return IUser[]
	 */
	@RequestMapping("/queryUserByOrgParent")
	public void queryUserByOrgParent(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IUser[] list = null;
		if(getAppCheck(req))
		{
			String orgPid = req.getString("orgPid");
			list = SsoFactory.getSsoService().queryUserByOrgPid(orgPid);
		}
		ResponseUtil.printJson(response, toJson(list));
	}

	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	@RequestMapping("/queryUserByOrg")
	public void queryUserByOrg(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		IUser[] list = null;
		if(getAppCheck(req))
		{
			String orgId = req.getString("orgId");
			list = SsoFactory.getSsoService().queryUserByOrgId(orgId);
		}
		ResponseUtil.printJson(response, toJson(list));
	}

	//////////////////////////////////////////////////////////////////////////////
	// 权限相关的方法
	//////////////////////////////////////////////////////////////////////////////

	private ISystem getSystemCheck(MyRequest req)
	{
		if(getAppCheck(req))
		{
			String systemAlias = req.getString("systemAlias");
			String systemSecret = req.getString("systemPassword");// MD5()
			ISystem sys = SsoFactory.getSsoService().getSystem(systemAlias);
			if((sys != null && systemSecret.equals(sys.getPassword())))
			{
				return sys;
			}
		}
		return null;
	}
	
	/**
	 * 获取子系统信息，仅限systemAlias以!开头的内置系统调用
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return ISystem
	 */
	@RequestMapping("/getSystem")
	public void getSystem(HttpServletRequest request, HttpServletResponse response)
	{
		ISystem s = getSystemCheck(new MyRequest(request));
		if(s != null && s.getAlias().startsWith("!"))
		{
			ResponseUtil.printJson(response, toJson(s));
		}
	}

	/**
	 * 获取用户有权限访问的子系统，仅限systemAlias以!开头的内置系统调用
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return ISystem[]
	 */
	@RequestMapping("/getSystemByUser")
	public void getSystemByUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		ISystem[] list = null;
		ISystem s = getSystemCheck(req);
		if(s != null)
		{
			String userAccount = req.getString("userAccount");
			list = SsoFactory.getSsoService().getSystemByUser(userAccount);
			if(!s.getAlias().startsWith("!"))
			{
				for(ISystem i : list)
				{
					if(i.getId().longValue() == s.getId().longValue())
					{
						ISystem[] newArray = {s};
						ResponseUtil.printJson(response, toJson(newArray));// 只返回自己，别的无权访问
						return;
					}
				}
				list = null;
			}
		}
		ResponseUtil.printJson(response, toJson(list));
	}

	/**
	 * 获取系统的功能结构
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return IFunc[]
	 */
	@RequestMapping("/getFunctionBySystem")
	public void getFunctionBySystem(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		ISystem s = getSystemCheck(req);
		IFunc[] list = null;
		if(s != null)
		{
			list = SsoFactory.getSsoService().getFuncBySystemAlias(s.getAlias());
		}
		ResponseUtil.printJson(response, toJson(list));
	}

	/**
	 * 获取用户权限范围内的系统功能结构
	 * @param appid 应用ID
	 * @param access_token 应用凭证
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return IFunc[]
	 */
	@RequestMapping("/getFunctionByUser")
	public void getFunctionByUser(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		ISystem s = getSystemCheck(req);
		IFunc[] list = null;
		if(s != null)
		{
			String userAccount = req.getString("userAccount");
			String otherAlias;
			if(s.getAlias().startsWith("!"))
			{
				otherAlias = req.getString("otherAlias", s.getAlias());// 以!开头的系统可读任意系统的菜单
			}
			else
			{
				otherAlias = s.getAlias();
			}
			list = SsoFactory.getSsoService().getFuncBySystemAliasAndAccount(otherAlias, userAccount);
		}
		ResponseUtil.printJson(response, toJson(list));
	}

//	/**
//	 * 获取岗位权限范围内的系统功能结构
//	 * @param systemAlias 系统标识
//	 * @param systemPassword 系统访问密码
//	 * @param postId 岗位ID
//	 * @return IFunc[]
//	 */
//	@RequestMapping("/getFunctionByOrg")
//	public void getFunctionByOrg(HttpServletRequest request, HttpServletResponse response)
//	{
//		MyRequest req = new MyRequest(request);
//		ISystem s = getSystemCheck(req);
//		IFunc[] list = null;
//		if(s != null)
//		{
//			String orgId = req.getString("orgId");
//			list = SsoFactory.getSsoService().getFuncBySystemAliasAndOrgid(s.getAlias(), orgId);
//		}
//		ResponseUtil.printJson(response, toJson(list));
//	}
}
