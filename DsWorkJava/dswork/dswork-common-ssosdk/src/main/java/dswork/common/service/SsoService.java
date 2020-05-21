package dswork.common.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsBaseSystemDao;
import dswork.common.dao.SsoDao;
import dswork.common.model.IBind;
import dswork.common.model.IFunc;
import dswork.common.model.IOrg;
import dswork.common.model.ISystem;
import dswork.common.model.IUser;
import dswork.common.model.IUserBind;
import dswork.common.model.IUserBindState;
import dswork.common.model.IUserBm;

@Service
public class SsoService
{
	@Autowired
	private SsoDao dao;

	public List<IBind> queryListBind()
	{
		return dao.queryListBind();
	}

	public IUser getUserByBm(String bm)
	{
		return dao.getUserByBm(bm);
	}

	public int saveUser(IUser user)
	{
		return dao.saveUser(user);
	}

	public int deleteUser(long id)
	{
		return dao.deleteUser(id);
	}

	public IUser getUserById(long id)
	{
		return dao.getUserById(id);
	}

	public IUserBind getUserBindById(long id)
	{
		return dao.getUserBindById(id);
	}

	public int updateUser(IUser user)
	{
		return dao.updateUser(user);
	}
	
	public int updateUserData(IUser user)
	{
		return dao.updateUserData(user);
	}

	public int updateUserPassword(long userid, String password)
	{
		return dao.updateUserPassword(userid, password);
	}
	
	public int updateUserAccount(long userid, String account)
	{
		return dao.updateUserAccount(userid, account);
	}

	public IUserBind saveOrUpdateUserBind(IUserBind userBind, boolean isCreateUser, IUser bindUser)
	{
		return dao.saveOrUpdateUserBind(userBind, isCreateUser, bindUser);
	}

	public int updateUserBind(IUserBind userBind)
	{
		return dao.updateUserBind(userBind);
	}

	public int updateUserid(IUser user, String oldBm, String newBm)
	{
		return dao.updateUserid(user, oldBm, newBm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		return dao.saveUser(user, reg_type);
	}

	public int saveUserBm(IUserBm userBm)
	{
		return dao.saveUserBm(userBm);
	}

	public IUserBm getUserBm(String bm)
	{
		return dao.getUserBm(bm.toLowerCase(Locale.ENGLISH));
	}

	public List<IUserBindState> getUserBindStateByUserId(long userid)
	{
		return dao.getUserBindStateByUserId(userid);
	}

	public int updateUserBindForUnBind(long userid, String bindids)
	{
		return dao.updateUserBindForUnBind(userid, bindids);
	}
	//////////////////////////// api////////////////////////////

	public void updateUserPassword(String account, String password)
	{
		dao.updateUserPassword(account, password);
	}

	public IUser getUserByBmNotSecret(String account)
	{
		return dao.getUserByBmNotSecret(account);
	}

	public IUser getUserByOpenid(long openid)
	{
		return dao.getUserByOpenid(openid);
	}

	// public IUser[] queryUserByPostid(String postid)
	// {
	// return dao.queryUserByPostid(postid);
	// }
	public IUser[] queryUserByOrgPid(String orgpid)
	{
		return dao.queryUserByOrgPid(orgpid);
	}

	public IUser[] queryUserByOrgId(String orgid)
	{
		return dao.queryUserByOrgId(orgid);
	}

	public ISystem getSystem(String systemAlias)
	{
		return DsBaseSystemDao.getISystem(systemAlias);
	}

	public ISystem[] getSystemByUser(String account)
	{
		return dao.getSystemByUser(account);
	}

	public IFunc[] getFuncBySystemAlias(String systemAlias)
	{
		return dao.getFuncBySystemAlias(systemAlias);
	}

	public IFunc[] getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		return dao.getFuncBySystemAliasAndAccount(systemAlias, account);
	}

	public IFunc[] getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		return dao.getFuncBySystemAliasAndOrgid(systemAlias, orgid);
	}

	// public IOrg[] getPostByUserId(Long userid)
	// {
	// return dao.queryPostByUserId(userid);
	// }
	public IOrg getOrgByOrgId(String orgid)
	{
		return dao.getOrgByOrgId(orgid);
	}

	public IOrg[] queryOrgByOrgPid(String orgpid)
	{
		return dao.queryOrgByOrgPid(orgpid);
	}
	
	public void saveUserLog(String appid, String atype, String acode, int optype, String opread, boolean isSuccess, String ip, Long userid, String bm, String name)
	{
		dao.saveUserLog(appid, atype, acode, optype, opread, isSuccess, ip, userid, bm, name);
	}
}
