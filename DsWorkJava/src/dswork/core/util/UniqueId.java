package dswork.core.util;

import java.util.*;

/**
 * ID生成类
 */
public class UniqueId
{
	private static long lastTimeMillis = -1L;// 上次生成ID的时间截

	/**
	 * 根据时间戳产生一个唯一ID，具有防止重复机制
	 * @return long
	 */
	@Deprecated
	public synchronized static long genId()
	{
		long id = 0;
		do
		{
			id = Calendar.getInstance().getTimeInMillis();
		}
		while(id == lastTimeMillis);
		lastTimeMillis = id;
		return id;
	}
}
