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

	public static void refresh()
	{
		DsBaseDao dao = (DsBaseDao) BeanFactory.getBean("dsBaseDao");
		List<IUnit> list = dao.queryListUnit();
		ConcurrentMap<String, IUnit> _map = new ConcurrentHashMap<String, IUnit>();
		for(IUnit m : list)
		{
			_map.put(m.getAppid() + "", m);
		}
		map = _map;
	}

	public static IUnit get(String appid)
	{
		return map.get(appid);
	}
}
