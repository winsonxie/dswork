package dswork.common.dao;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import dswork.common.model.IFunc;
import dswork.common.model.ISystem;
import dswork.core.db.MyBatisDao;
import dswork.spring.BeanFactory;

@Repository
@SuppressWarnings("all")
public class DsBaseSystemDao extends MyBatisDao
{
	// system//////////////////////////////////////////////////////////////////
	private static ConcurrentMap<String, ISystem> map = new ConcurrentHashMap<String, ISystem>();
	// 1000(1秒)|60000(1分钟)|3600000(1小时)|86400000(1天)
	public static final int refresh_timeout = 10 * 60000;// 10分钟
	private static long refreshTime = 0L;


	private static String Md5(String v)
	{
		if(v != null)
		{
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] digest = md.digest(v.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString().toUpperCase(Locale.ENGLISH);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sb = null;
			}
		}
		return "";
	}
	private static synchronized void refresh()
	{
		// 刷新全部系统配置信息
		refreshTime += System.currentTimeMillis() + refresh_timeout;// 先刷新，免得同一时刻都要刷新
		DsBaseSystemDao dao = (DsBaseSystemDao) BeanFactory.getBean("dsBaseSystemDao");
		List<ISystem> list = dao.querySystem();// 获取全部系统配置信息
		if(list != null)
		{
			ConcurrentMap<String, ISystem> newmap = new ConcurrentHashMap<String, ISystem>();
			for(ISystem i : list)
			{
				i.setPassword(Md5(i.getPassword()));
				newmap.put(i.getAlias(), i);
			}
			map = newmap;
		}
	}

	public static ISystem getISystem(String systemAlias)
	{
		if(System.currentTimeMillis() > refreshTime)// 初始时refreshPermissionTime为0，肯定会执行
		{
			refresh();
		}
		return map.get(systemAlias);// 没有则返回null
	}

	public static String getSystemId(String systemAlias)
	{
		ISystem sys = getISystem(systemAlias);
		if(sys != null)
		{
			return String.valueOf(sys.getId());
		}
		return "0";// 该id主要作为sql查询条件，没有值时返回0，DS_COMMON_SYSTEM表没有0的记录
	}
	
	@Override
	public Class getEntityClass()
	{
		return DsBaseSystemDao.class;
	}

	private List<ISystem> querySystem()
	{
		return executeSelectList("querySystem", null);
	}

	public List<ISystem> querySystemByAccount(String account)
	{
		List<ISystem> result = new ArrayList<ISystem>();
		List<ISystem> list = executeSelectList("querySystemByAccount", account);
		if(list != null && list.size() > 0)
		{
			for(ISystem s : list)
			{
				ISystem ss = getISystem(s.getAlias());
				if(ss == null)
				{
					s.setPassword(Md5(s.getPassword()));
					ss = map.put(s.getAlias(), s);// 顺便更新一下缓存
				}
				result.add(ss);
			}
		}
		return result;
	}

	public List<IFunc> queryFuncBySystemAlias(String systemAlias)
	{
		return executeSelectList("queryFuncBySystemid", DsBaseSystemDao.getSystemId(systemAlias));
	}

	public List<IFunc> getFuncBySystemAliasAndAccount(String systemAlias, String account)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("systemid", DsBaseSystemDao.getSystemId(systemAlias));
		map.put("account", account);
		return executeSelectList("getFuncBySystemidAndAccount", map);
	}

	public List<IFunc> getFuncBySystemAliasAndOrgid(String systemAlias, String orgid)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("systemid", DsBaseSystemDao.getSystemId(systemAlias));
		map.put("orgid", orgid);
		return executeSelectList("getFuncBySystemidAndOrgid", map);
	}
}
