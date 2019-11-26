package dswork.common.util;

import java.util.List;
import java.util.Locale;

import dswork.common.dao.DsBaseSystemDao;
import dswork.common.dao.SsoDao;
import dswork.common.model.IBind;
import dswork.common.model.IFunc;
import dswork.common.model.IOrg;
import dswork.common.model.ISystem;
import dswork.common.model.IUser;
import dswork.common.model.IUserBind;
import dswork.common.model.IUserBm;
import dswork.spring.BeanFactory;

public class SsoDaoUtil
{
	private static SsoDao dao = null;

	private static void init()
	{
		if(dao == null)
		{
			dao = (SsoDao) BeanFactory.getBean("ssoDao");
		}
	}

	public List<IBind> queryListBind()
	{
		init();
		return dao.queryListBind();
	}

	public IUser getUserByBm(String bm)
	{
		init();
		return dao.getUserByBm(bm);
	}

	public int saveUser(IUser user)
	{
		init();
		return dao.saveUser(user);
	}

	public IUser getUserById(long id)
	{
		init();
		return dao.getUserById(id);
	}

	public IUserBind getUserBindById(long id)
	{
		init();
		return dao.getUserBindById(id);
	}

	public int updateUser(IUser user)
	{
		init();
		return dao.updateUser(user);
	}

	public int updateUserData(IUser user)
	{
		init();
		return dao.updateUserData(user);
	}

	public int updateUserPassword(long userid, String password)
	{
		init();
		return dao.updateUserPassword(userid, password);
	}

	public IUserBind saveOrUpdateUserBind(IUserBind userBind, boolean isCreateUser, String userType)
	{
		init();
		return dao.saveOrUpdateUserBind(userBind, isCreateUser, userType);
	}

	public int updateUserid(IUser user, String oldBm, String newBm)
	{
		init();
		return dao.updateUserid(user, oldBm, newBm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		init();
		return dao.saveUser(user, reg_type);
	}

	public IUserBm getUserBm(String bm)
	{
		init();
		return dao.getUserBm(bm.toLowerCase(Locale.ENGLISH));
	}

	//////////////////////////// api////////////////////////////
	public void updateUserPassword(String account, String password)
	{
		init();
		dao.updateUserPassword(account, password);
	}

	public IUser getUserByBmNotSecret(String account)
	{
		init();
		return dao.getUserByBmNotSecret(account);
	}

	public IUser getUserByOpenid(long openid)
	{
		init();
		return dao.getUserByOpenid(openid);
	}

	// public IUser[] queryUserByPostid(String postid)
	// {
	// init();
	// return dao.queryUserByPostid(postid);
	// }
	public IUser[] queryUserByOrgPid(String orgpid)
	{
		init();
		return dao.queryUserByOrgPid(orgpid);
	}

	public IUser[] queryUserByOrgId(String orgid)
	{
		init();
		return dao.queryUserByOrgId(orgid);
	}

	public ISystem getSystem(String systemAlias)
	{
		init();
		return DsBaseSystemDao.getISystem(systemAlias);
	}

	public ISystem[] getSystemByUser(String account)
	{
		init();
		return dao.getSystemByUser(account);
	}

	public IFunc[] getFuncBySystemAlias(String systemAlias)
	{
		init();
		return dao.getFuncBySystemAlias(systemAlias);
	}

	public IFunc[] getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		init();
		return dao.getFuncBySystemAliasAndAccount(systemAlias, account);
	}

	public IFunc[] getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		init();
		return dao.getFuncBySystemAliasAndOrgid(systemAlias, orgid);
	}

	// public IOrg[] getPostByUserId(Long userid)
	// {
	// init();
	// return dao.queryPostByUserId(userid);
	// }
	public IOrg getOrgByOrgId(String orgid)
	{
		init();
		return dao.getOrgByOrgId(orgid);
	}

	public IOrg[] queryOrgByOrgPid(String orgpid)
	{
		init();
		return dao.queryOrgByOrgPid(orgpid);
	}

	public void saveLogLogin(String appid, String ticket, String ip, String bm, String name, boolean isSuccess)
	{
		init();
		dao.saveLogLogin(appid, ticket, ip, bm, name, isSuccess);
	}

	public void saveLogLogout(String ticket, boolean isTimeout, boolean isUpdatePassword)
	{
		init();
		dao.saveLogLogout(ticket, isTimeout, isUpdatePassword);
	}
}
