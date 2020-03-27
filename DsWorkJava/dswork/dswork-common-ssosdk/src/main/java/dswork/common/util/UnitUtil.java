package dswork.common.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dswork.common.dao.DsBaseDao;
import dswork.common.model.IUnit;
import dswork.spring.BeanFactory;

public class UnitUtil
{
	private UnitUtil()
	{
	}
	private static ConcurrentMap<String, IUnit> map = new ConcurrentHashMap<String, IUnit>();
	private static String UU = "UU";

	public static void refresh()
	{
		DsBaseDao dao = (DsBaseDao) BeanFactory.getBean("dsBaseDao");
		List<IUnit> list = dao.queryListUnit();
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			for(IUnit m : list)
			{
				db.hset(UU, m.getAppid() + "", ResponseUtil.toJson(m));
			}
			db.close();
		}
		else
		{
			ConcurrentMap<String, IUnit> _map = new ConcurrentHashMap<String, IUnit>();
			for(IUnit m : list)
			{
				_map.put(m.getAppid() + "", m);
			}
			map = _map;
		}
	}

	public static IUnit get(String appid)
	{
		IUnit unit = null;
		if(ResponseUtil.USE_REDIS)
		{
			redis.clients.jedis.Jedis db = RedisUtil.db.getJedis();
			String json = db.hget(UU, appid);
			db.close();
			unit = ResponseUtil.toBean(json, IUnit.class);
		}
		else
		{
			unit = map.get(appid);
		}
		return unit;
	}
}
