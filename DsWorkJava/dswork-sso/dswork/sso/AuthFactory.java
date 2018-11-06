/**
 * 调用webapi的工厂类
 */
package dswork.sso;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;

import dswork.sso.http.HttpUtil;
import dswork.sso.model.IFunc;
import dswork.sso.model.IOrg;
import dswork.sso.model.ISystem;
import dswork.sso.model.IUser;

public class AuthFactory
{
	static com.google.gson.Gson gson = AuthGlobal.getGson();
	static String url = "";
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}
	
	private static HttpUtil getHttp(String path)
	{
		String u = new StringBuilder(30).append(AuthGlobal.getURL()).append("/").append(path).toString();
		HttpUtil http = new HttpUtil();
		http.create(u, true)// 不进行主机名确认
		.addForm("name", AuthGlobal.getName())
		.addForm("pwd", AuthGlobal.getPwd());
		return http;
	}
	
	private static void doDebug(String url, String json)
	{
		if(log.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder("AuthFactory:url=").append("url");
			sb.append(", json=").append(json);
			log.debug(sb.toString());
		}
	}
	
	private static void doDebug(String url, String json, String name, String value)
	{
		if(log.isDebugEnabled())
		{
			StringBuilder sb = new StringBuilder("AuthFactory:url=").append("url").append("?").append(name).append("=").append(value);
			sb.append(", json=").append(json);
			log.debug(sb.toString());
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////
	// 权限相关的方法
	/////////////////////////////////////////////////////////////// ///////////////
	/**
	 * 获取子系统信息
	 * @param systemPassword 系统访问密码
	 * @return ISystem
	 */
	public static ISystem getSystem()
	{
		String v = getHttp("getSystem").connect().trim();
		doDebug("getSystem", v);
		ISystem m = gson.fromJson(v, ISystem.class);
		return m;
	}
	/**
	 * 获取用户有权限访问的子系统
	 * @param userAccount 用户帐号
	 * @return ISystem[]
	 */
	public static ISystem[] getSystemByUser(String userAccount)
	{
		String v = getHttp("getSystemByUser").addForm("userAccount", userAccount).connect().trim();
		doDebug("getSystemByUser", v, "userAccount", userAccount);
		List<ISystem> list = gson.fromJson(v, new TypeToken<List<ISystem>>(){}.getType());
		return list.toArray(new ISystem[list.size()]);
	}
	
	/**
	 * 获取系统的功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionBySystem()
	{
		String v = getHttp("getFunctionBySystem").connect().toString();
		doDebug("getFunctionBySystem", v);
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}
	
	/**
	 * 获取用户权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param userAccount 用户帐号
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByUser(String userAccount)
	{
		String v = getHttp("getFunctionByUser").addForm("userAccount", userAccount).connect().toString();
		doDebug("getFunctionByUser", v, "userAccount", userAccount);
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}
	
	/**
	 * 获取岗位权限范围内的系统功能结构
	 * @param systemAlias 系统标识
	 * @param systemPassword 系统访问密码
	 * @param orgId 单位ID、部门ID、岗位ID
	 * @return IFunc[]
	 */
	public static IFunc[] getFunctionByOrg(String orgId)
	{
		String v = getHttp("getFunctionByOrg").addForm("orgId", orgId).connect().toString();
		doDebug("getFunctionByOrg", v, "orgId", orgId);
		List<IFunc> list = gson.fromJson(v, new TypeToken<List<IFunc>>(){}.getType());
		return list.toArray(new IFunc[list.size()]);
	}

	//////////////////////////////////////////////////////////////////////////////
	// 用户相关的方法
	//////////////////////////////////////////////////////////////////////////////
	/**
	 * @note 获取组织机构
	 * @param orgId 组织机构ID
	 * @return IOrg
	 */
	public static IOrg getOrg(String orgId)
	{
		String v = getHttp("getOrg").addForm("orgId", orgId).connect().toString();
		doDebug("getOrg", v, "orgId", orgId);
		IOrg m = gson.fromJson(v, IOrg.class);
		return m;
	}
	
	/**
	 * @note 获取下级组织机构(status:2单位,1部门,0岗位)
	 * @param orgPid 组织机构ID，为0则取顶级
	 * @return IOrg[]
	 */
	public static IOrg[] queryOrgByOrgParent(String orgPid)
	{
		String v = getHttp("queryOrgByOrgParent").addForm("orgPid", orgPid).connect().toString();
		doDebug("queryOrgByOrgParent", v, "orgPid", orgPid);
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}

	/**
	 * @note 获取组织机构下的岗位
	 * @param orgId 组织机构ID
	 * @return IOrg[]
	 */
	public static IOrg[] queryPostByOrg(String orgId)
	{
		String v = getHttp("queryPostByOrg").addForm("orgId", orgId).connect().toString();
		doDebug("queryPostByOrg", v, "orgId", orgId);
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}
	
	/**
	 * @note 获取指定用户的基本信息
	 * @param userAccount 用户帐号
	 * @return IUser
	 */
	public static IUser getUser(String userAccount)
	{
		String v = getHttp("getUser").addForm("userAccount", userAccount).connect().toString();
		doDebug("getUser", v, "userAccount", userAccount);
		IUser m = gson.fromJson(v, IUser.class);
		return m;
	}
	
	/**
	 * @note 获取岗位下的所有用户
	 * @param postId 岗位ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByPost(String postId)
	{
		String v = getHttp("queryUserByPost").addForm("postId", postId).connect().toString();
		doDebug("queryUserByPost", v, "postId", postId);
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定单位下的用户，不含子单位
	 * @param orgPid 单位ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrgParent(String orgPid)
	{
		String v = getHttp("queryUserByOrgParent").addForm("orgPid", orgPid).connect().toString();
		doDebug("queryUserByOrgParent", v, "orgPid", orgPid);
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定部门下的用户，不含子部门
	 * @param orgId 部门 ID
	 * @return IUser[]
	 */
	public static IUser[] queryUserByOrg(String orgId)
	{
		String v = getHttp("queryUserByOrg").addForm("orgId", orgId).connect().toString();
		doDebug("queryUserByOrg", v, "orgId", orgId);
		List<IUser> list = gson.fromJson(v, new TypeToken<List<IUser>>(){}.getType());
		return list.toArray(new IUser[list.size()]);
	}
	
	/**
	 * @note 获取指定用户拥有的岗位
	 * @param userAccount 用户帐号
	 * @return IOrg[]
	 */
	public static IOrg[] queryPostByUser(String userAccount)
	{
		String v = getHttp("queryPostByUser").addForm("userAccount", userAccount).connect().toString();
		doDebug("queryPostByUser", v, "userAccount", userAccount);
		List<IOrg> list = gson.fromJson(v, new TypeToken<List<IOrg>>(){}.getType());
		return list.toArray(new IOrg[list.size()]);
	}
}
