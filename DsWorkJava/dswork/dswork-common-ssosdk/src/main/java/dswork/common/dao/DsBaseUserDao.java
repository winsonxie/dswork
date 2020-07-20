package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.common.model.IUser;
import dswork.common.model.IUserBm;
import dswork.core.db.MyBatisDao;
import dswork.core.util.IdUtil;
import dswork.core.util.TimeUtil;

@Repository
@SuppressWarnings("all")
public class DsBaseUserDao extends MyBatisDao
{
	private SqlSessionTemplate sqlSessionTemplateCommon;
	private static boolean hasCommon = false;

	@Override
	protected SqlSessionTemplate getSqlSessionTemplate()
	{
		if(hasCommon)
		{
			return sqlSessionTemplateCommon;
		}
		return super.getSqlSessionTemplate();
	}
	
	public void setSqlSessionTemplateCommon(SqlSessionTemplate sqlSessionTemplate)
	{
		if(log.isInfoEnabled())
		{
			log.info("======== DsCommonDaoCommonIDict call setSqlSessionTemplateCommon ========");
		}
		hasCommon = true;
		this.sqlSessionTemplateCommon = sqlSessionTemplate;
	}
	
	@Override
	public Class getEntityClass()
	{
		return DsBaseUserDao.class;
	}

	public int saveUserBm(IUserBm bm)
	{
		return executeInsert("insertUserBm", bm);
	}
	
	public int deleteUser(long id)
	{
		return executeDelete("deleteUser", id);
	}

	public int updateUserid(IUser user, String oldBm, String newBm)
	{
		oldBm = oldBm.toLowerCase(Locale.ENGLISH);
		IUserBm o = getUserBm(oldBm);// bm的userid有可能为空或不是自己
		if(o != null && user != null)
		{
			if(o.getUserid() == 0 || user.getId() != o.getUserid())
			{
				o.setUserid(user.getId());
			}
			newBm = newBm.toLowerCase(Locale.ENGLISH);
			IUserBm bm = getUserBm(newBm);
			if(bm == null)
			{
				bm = new IUserBm();
				bm.setBm(newBm);
				bm.setUserid(o.getUserid());
				bm.setType(o.getType());
				executeInsert("insertUserBm", bm);
			}
			else
			{
				bm.setUserid(o.getUserid());
				bm.setType(o.getType());
				executeUpdate("updateUserBm", bm);
			}
			if(!oldBm.equals(newBm))
			{
				o.setUserid(0L);
			}
			IUser u = new IUser();
			u.setId(user.getId());
			u.setMobile(newBm);
			executeUpdate("updateUserMobile", u);
			return executeUpdate("updateUserBm", o);
		}
		return 0;
	}

	public IUserBm getUserBm(String bm)
	{
		return (IUserBm) executeSelect("getUserBm", bm);
	}

	public int saveUser(IUser user, String reg_type)
	{
		user.setAccount(user.getAccount().trim().toLowerCase(Locale.ENGLISH));// 数据库本质使用小写
		user.setPassword(user.getPassword());
		user.setMobile(user.getMobile().trim().toLowerCase(Locale.ENGLISH));
		user.setIdcard(user.getIdcard().trim().toUpperCase(Locale.ENGLISH));
		// 防止外面给过id
		if(user.getId() == null || user.getId().longValue() == 0L)
		{
			user.setId(IdUtil.genId());
		}
		if(user.getAccount().length() <= 0)
		{
			user.setAccount(getAccount(user.getId()));
		}
		user.setName("".equals(user.getName()) ? user.getAccount() : user.getName());
		user.setCreatetime(TimeUtil.getCurrentTime());
		user.setLasttime(1);
		user.setStatus(1);
		IUserBm bm = new IUserBm();
		IUserBm o = getUserBm(user.getAccount());
		if(o == null)
		{
			executeInsert("insertUser", user);
			bm.setBm(user.getAccount());
			bm.setUserid(user.getId());
			bm.setType(0);
			executeInsert("insertUserBm", bm);
		}
		else
		{
			return 0;
		}
		if(reg_type != null && reg_type.length() > 0)
		{
			if(reg_type.indexOf("m") >= 0)
			{
				bm.setBm(user.getMobile());
				bm.setType(1);
				try
				{
					executeInsert("insertUserBm", bm);
				}
				catch(Exception e)
				{
					executeUpdate("updateUserBm", bm);
				}
			}
			if(reg_type.indexOf("u") >= 0)
			{
				bm.setBm(user.getIdcard().toLowerCase(Locale.ENGLISH));
				bm.setType(2);
				try
				{
					executeInsert("insertUserBm", bm);
				}
				catch(Exception e)
				{
					executeUpdate("updateUserBm", bm);
				}
			}
		}
		return 1;
	}

	public int delete(long id)
	{
		return executeDelete("delete", id);
	}

	public int updateUser(IUser user)
	{
		return executeUpdate("updateUser", user);
	}
	
	public int updateUserData(IUser user)
	{
		return executeUpdate("updateUserData", user);
	}

	public int updateUserPassword(long id, String password)
	{
		if(id != 0)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("password", password);
			return executeUpdate("updateUserPassword", map);
		}
		return 0;
	}

	public int updateUserAccount(long userid, String account)
	{
		if(userid > 0)
		{
			String newAccount = account.trim().toLowerCase(Locale.ENGLISH);
			IUser user = getUserById(userid);
			if(user != null)
			{
				String oldAccount = user.getAccount();
				boolean isChangeBm = false;
				IUserBm oldUserBm = getUserBm(oldAccount);
				if(oldUserBm != null && oldUserBm.getUserid() > 0)
				{
					IUserBm newUserBm = getUserBm(newAccount);
					if(newUserBm == null)
					{
						isChangeBm = true;
					}
					else
					{
						if(newUserBm.getUserid() == 0l)
						{
							isChangeBm = true;
						}
					}
				}
				if(isChangeBm)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", userid);
					map.put("account", account);
					int row = executeUpdate("updateUserAccount", map);
					if(row > 0)
					{
						IUserBm newUserBm = new IUserBm();
						newUserBm.setBm(newAccount);
						newUserBm.setUserid(userid);
						newUserBm.setType(0);
						try
						{
							executeInsert("insertUserBm", newUserBm);
						}
						catch(Exception e)
						{
							executeUpdate("updateUserBm", newUserBm);
						}
						executeDelete("deleteUserBm", oldUserBm);
						return 1;
					}
				}
			}
		}
		return 0;
	}

	public IUser getUserById(long id)
	{
		return (IUser) executeSelect("getUserById", id);
	}

	public IUser getUserByBm(String bm)
	{
		IUserBm o = getUserBm(bm.toLowerCase(Locale.ENGLISH));
		if(o != null && o.getUserid() != 0)
		{
			return getUserById(o.getUserid());
		}
		return null;
	}

	public IUser getUserByOpenid(String openid)
	{
		return (IUser) executeSelect("getUserByOpenid", openid);
	}

	// 用于api
	public List<IUser> queryUserByOrgPid(long orgpid)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("orgpid", orgpid);
		return executeSelectList("queryUserByOrgPid", param);
	}

	// 用于api
	public List<IUser> queryUserByOrgId(long orgid)
	{
		return executeSelectList("queryUserByOrgId", orgid);
	}

	private String getAccount(long id)
	{
		String account = "u" + longToString(id);
		IUserBm u = getUserBm(account);
		for(int i = 1; u != null; i++)
		{
			account = "u" + longToString(id + i);
			u = getUserBm(account);
		}
		return account;
	}

	private String longToString(long id)
	{
		java.util.Stack<Integer> s = new java.util.Stack<Integer>();
		String str = "abcdefghijklmnopqrstuvwxyz0123456789";
		long a = id;
		while(a != 0)
		{
			Long b = a % str.length();
			a = a / str.length();
			s.push(b.intValue());
		}
		StringBuilder sb = new StringBuilder();
		while(!s.isEmpty())
		{
			sb.append(str.charAt(s.pop()));
		}
		return sb.toString();
	}
}
