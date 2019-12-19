package dswork.common.dao;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dswork.common.model.IBind;
import dswork.common.model.IFunc;
import dswork.common.model.IOrg;
import dswork.common.model.ISystem;
import dswork.common.model.IUser;
import dswork.common.model.IUserBind;
import dswork.common.model.IUserBm;
import dswork.core.util.EncryptUtil;
import dswork.core.util.TimeUtil;

@Repository
public class SsoDao
{
	@Autowired
	private DsBaseUserDao userDao;
	@Autowired
	private DsBaseDao baseDao;
	@Autowired
	private DsBaseUserBindDao userBindDao;
	@Autowired
	private DsBaseSystemDao systemDao;
	@Autowired
	private DsBaseOrgDao orgDao;
	@Autowired
	private DsBaseLoginDao logDao;

	public List<IBind> queryListBind()
	{
		return baseDao.queryListBind();
	}

	public IUser getUserByBm(String bm)
	{
		if(bm == null || "".equals(bm))
		{
			return null;
		}
		return (IUser) userDao.getUserByBm(bm);
	}

	public int saveUser(IUser user)
	{
		return userDao.saveUser(user, null);
	}

	public IUser getUserById(long id)
	{
		return userDao.getUserById(id);
	}

	public IUserBind getUserBindById(long id)
	{
		return userBindDao.getUserBindById(id);
	}

	public int updateUser(IUser user)
	{
		return userDao.updateUser(user);
	}

	public int updateUserData(IUser user)
	{
		return userDao.updateUserData(user);
	}

	public int updateUserPassword(long userid, String password)
	{
		return userDao.updateUserPassword(userid, password);
	}

	public IUserBind saveOrUpdateUserBind(IUserBind userBind, boolean isCreateUser, String userType)
	{
		IUserBind temp = userBindDao.queryUserBindByOpenid(userBind.getOpenid());
		if(userBind.getUnionid().length() > 0 && (temp == null || temp.getUnionid().length() <= 0))
		{
			List<IUserBind> list = userBindDao.queryUserBindByUnionid(userBind.getUnionid());
			if(list != null && list.size() > 0)
			{
				if(temp != null)
				{
					temp.setUnionid(userBind.getUnionid());
					temp.setUserid(list.get(0).getUserid());
				}
				else
				{
					userBind.setUserid(list.get(0).getUserid());
				}
			}
		}
		if(temp != null)
		{
			temp.setName(userBind.getName());
			temp.setSex(userBind.getSex());
			temp.setCountry(userBind.getCountry());
			temp.setProvince(userBind.getProvince());
			temp.setCity(userBind.getCity());
			temp.setAvatar(userBind.getAvatar());
			userBind = temp;
			temp = null;
		}
		if(isCreateUser && userBind.getUserid() == 0)
		{
			// 注册用户并绑定
			IUser user = new IUser();
			user.setStatus(1);
			user.setName(userBind.getName());
			user.setSex(userBind.getSex());
			user.setAvatar(userBind.getAvatar());
			user.setType(userType);
			userDao.saveUser(user, null);
			userBind.setUserid(user.getId());
		}
		userBind.setLasttime(System.currentTimeMillis());
		if(userBind.getId() <= 0)
		{
			userBind.setId(dswork.core.util.IdUtil.genId());
			userBind.setCreatetime(TimeUtil.getCurrentTime());
			userBindDao.saveUserBind(userBind);
		}
		else
		{
			userBindDao.updateUserBind(userBind);// 如果没变不需要update
		}
		return userBind;
	}

	public int updateUserid(IUser user, String oldBm, String newBm)
	{
		return userDao.updateUserid(user, oldBm, newBm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		return userDao.saveUser(user, reg_type);
	}

	public IUserBm getUserBm(String bm)
	{
		return userDao.getUserBm(bm.toLowerCase(Locale.ENGLISH));
	}
	//////////////////////////// api////////////////////////////

	public void updateUserPassword(String account, String password)
	{
		IUserBm bm = userDao.getUserBm(account.toLowerCase(Locale.ENGLISH));
		if(bm != null && bm.getUserid() != 0)
		{
			userDao.updateUserPassword(bm.getUserid(), EncryptUtil.encryptMd5(password));
		}
	}

	public IUser getUserByBmNotSecret(String account)
	{
		if(account == null || "".equals(account))
		{
			return null;
		}
		IUser user = userDao.getUserByBm(account);
		if(user != null)
		{
			user.clearSecret();
		}
		return user;
	}

	public IUser getUserByOpenid(long openid)
	{
		if(openid < 0)
		{
			return null;
		}
		IUser user = userDao.getUserById(openid);
		if(user != null)
		{
			user.clearSecret();
		}
		return user;
	}

	// public IUser[] queryUserByPostid(String postid)
	// {
	// List<IUser> list = userDao.queryUserByPostid(postid);
	// if (list != null)
	// {
	// return list.toArray(new IUser[list.size()]);
	// }
	// return null;
	// }
	public IUser[] queryUserByOrgPid(String orgpid)
	{
		List<IUser> list = userDao.queryUserByOrgPid(orgpid);
		if(list != null)
		{
			for(IUser u : list)
			{
				u.clearSecret();
			}
			return list.toArray(new IUser[list.size()]);
		}
		return null;
	}

	public IUser[] queryUserByOrgId(String orgid)
	{
		List<IUser> list = userDao.queryUserByOrgId(orgid);
		if(list != null)
		{
			for(IUser u : list)
			{
				u.clearSecret();
			}
			return list.toArray(new IUser[list.size()]);
		}
		return null;
	}

	public ISystem getSystem(String systemAlias)
	{
		return DsBaseSystemDao.getISystem(systemAlias);
	}

	public ISystem[] getSystemByUser(String account)
	{
		List<ISystem> list = systemDao.querySystemByAccount(account);
		if(list != null)
		{
			return list.toArray(new ISystem[list.size()]);
		}
		return null;
	}

	public IFunc[] getFuncBySystemAlias(String systemAlias)
	{
		List<IFunc> list = systemDao.queryFuncBySystemAlias(systemAlias);
		if(list != null)
		{
			return list.toArray(new IFunc[list.size()]);
		}
		return null;
	}

	public IFunc[] getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		List<IFunc> list = systemDao.getFuncBySystemAliasAndAccount(systemAlias, account);
		IFunc[] arr = list.toArray(new IFunc[list.size()]);
		return arr;
	}

	public IFunc[] getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		List<IFunc> list = systemDao.getFuncBySystemAliasAndOrgid(systemAlias, orgid);
		return list.toArray(new IFunc[list.size()]);
	}

	// public IOrg[] getPostByUserId(Long userid)
	// {
	// List<IOrg> list = orgDao.queryPostByUserId(userid);
	// if (list != null)
	// {
	// return list.toArray(new IOrg[list.size()]);
	// }
	// return null;
	// }
	public IOrg getOrgByOrgId(String orgid)
	{
		if(orgid == null || "".equals(orgid))
		{
			return null;
		}
		return orgDao.getOrgByOrgid(orgid);
	}

	public IOrg[] queryOrgByOrgPid(String orgpid)
	{
		try
		{
			List<IOrg> list = orgDao.queryOrgByOrgPid(Long.parseLong(orgpid));
			if(list != null)
			{
				return list.toArray(new IOrg[list.size()]);
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	public void saveLogLogin(String appid, String ticket, String ip, String bm, String name, boolean isSuccess)
	{
		logDao.saveLogLogin(appid, ticket, ip, bm, name, isSuccess);
	}

	public void saveLogLogout(String ticket, boolean isTimeout, boolean isUpdatePassword)
	{
		logDao.saveLogLogout(ticket, isTimeout, isUpdatePassword);
	}
}
