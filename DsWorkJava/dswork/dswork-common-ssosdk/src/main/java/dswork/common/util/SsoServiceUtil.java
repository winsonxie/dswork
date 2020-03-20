package dswork.common.util;

import java.util.List;
import java.util.Locale;

import dswork.common.dao.DsBaseSystemDao;
import dswork.common.model.IBind;
import dswork.common.model.IFunc;
import dswork.common.model.IOrg;
import dswork.common.model.ISystem;
import dswork.common.model.IUser;
import dswork.common.model.IUserBind;
import dswork.common.model.IUserBindState;
import dswork.common.model.IUserBm;
import dswork.common.service.SsoService;

public class SsoServiceUtil
{
	private SsoService service;

	public SsoServiceUtil(SsoService service)
	{
		this.service = service;
	}

	public List<IBind> queryListBind()
	{
		return service.queryListBind();
	}

	public IUser getUserByBm(String bm)
	{
		return service.getUserByBm(bm);
	}

	public int saveUser(IUser user)
	{
		return service.saveUser(user);
	}

	public IUser getUserById(long id)
	{
		return service.getUserById(id);
	}

	public IUserBind getUserBindById(long id)
	{
		return service.getUserBindById(id);
	}

	public int updateUser(IUser user)
	{
		return service.updateUser(user);
	}

	public int updateUserData(IUser user)
	{
		return service.updateUserData(user);
	}

	public int updateUserPassword(long userid, String password)
	{
		return service.updateUserPassword(userid, password);
	}

	public IUserBind saveOrUpdateUserBind(IUserBind userBind, boolean isCreateUser, IUser bindUser)
	{
		return service.saveOrUpdateUserBind(userBind, isCreateUser, bindUser);
	}
	
	public int updateUserBind(IUserBind userBind)
	{
		return service.updateUserBind(userBind);
	}

	public int updateUserid(IUser user, String oldBm, String newBm)
	{
		return service.updateUserid(user, oldBm, newBm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		return service.saveUser(user, reg_type);
	}

	public IUserBm getUserBm(String bm)
	{
		return service.getUserBm(bm.toLowerCase(Locale.ENGLISH));
	}
	
	public List<IUserBindState> getUserBindStateByUserId(long userid)
	{
		return service.getUserBindStateByUserId(userid);
	}
	
	public int updateUserBindForUnBind(long userid, String bindids)
	{
		return service.updateUserBindForUnBind(userid, bindids);
	}
	

	//////////////////////////// api////////////////////////////
	public void updateUserPassword(String account, String password)
	{
		service.updateUserPassword(account, password);
	}

	public IUser getUserByBmNotSecret(String account)
	{
		return service.getUserByBmNotSecret(account);
	}

	public IUser getUserByOpenid(long openid)
	{
		return service.getUserByOpenid(openid);
	}

	// public IUser[] queryUserByPostid(String postid)
	// {
	// return service.queryUserByPostid(postid);
	// }
	public IUser[] queryUserByOrgPid(String orgpid)
	{
		return service.queryUserByOrgPid(orgpid);
	}

	public IUser[] queryUserByOrgId(String orgid)
	{
		return service.queryUserByOrgId(orgid);
	}

	public ISystem getSystem(String systemAlias)
	{
		return DsBaseSystemDao.getISystem(systemAlias);
	}

	public ISystem[] getSystemByUser(String account)
	{
		return service.getSystemByUser(account);
	}

	public IFunc[] getFuncBySystemAlias(String systemAlias)
	{
		return service.getFuncBySystemAlias(systemAlias);
	}

	public IFunc[] getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		return service.getFuncBySystemAliasAndAccount(systemAlias, account);
	}

	public IFunc[] getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		return service.getFuncBySystemAliasAndOrgid(systemAlias, orgid);
	}

	// public IOrg[] getPostByUserId(Long userid)
	// {
	// return service.queryPostByUserId(userid);
	// }
	public IOrg getOrgByOrgId(String orgid)
	{
		return service.getOrgByOrgId(orgid);
	}

	public IOrg[] queryOrgByOrgPid(String orgpid)
	{
		return service.queryOrgByOrgPid(orgpid);
	}

	public void saveLogLogin(String appid, String ticket, String ip, String bm, String name, boolean isSuccess)
	{
		service.saveLogLogin(appid, ticket, ip, bm, name, isSuccess);
	}

	public void saveLogLogout(String ticket, boolean isTimeout, boolean isUpdatePassword)
	{
		service.saveLogLogout(ticket, isTimeout, isUpdatePassword);
	}
}
