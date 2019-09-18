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

	public int updateUserPassword(long userid, String password)
	{
		return service.updateUserPassword(userid, password);
	}

	public IUserBind saveOrUpdateUserBind(IUserBind userBind, boolean isCreateUser, String userType)
	{
		return service.saveOrUpdateUserBind(userBind, isCreateUser, userType);
	}

	public int updateUserid(String oldBm, String newBm)
	{
		return service.updateUserid(oldBm, newBm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		return service.saveUser(user, reg_type);
	}

	public IUserBm getUserBm(String bm)
	{
		return service.getUserBm(bm.toLowerCase(Locale.ENGLISH));
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
}
