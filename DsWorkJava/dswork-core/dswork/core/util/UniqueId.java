package dswork.core.util;

import java.util.*;

/**
 * ID生成类
 */
public class UniqueId
{
	private final static long datacenterIdLength = 4L;// 数据标识所占的2进制位数
	private final static long workerIdLength = 4L;// 机器码所占的2进制位数
	private final static long sequenceLength = 6L;// 序列号所占的2进制位数
	private final static long workerIdShift = sequenceLength;
	private final static long datacenterIdShift = sequenceLength + workerIdLength;// 数据标识向左移10位(6+4)
	private final static long timestampShift = sequenceLength + workerIdLength + datacenterIdLength;// 时间截向左移14位(6+4+4)
	private final static long sequenceMask = -1L ^ (-1L << sequenceLength);// 生成序列的掩码，这里为64
	private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdLength);// 数据标识的long值，4位是16
	private final static long maxWorkerId = -1L ^ (-1L << workerIdLength);// 机器码的long值，4位是16

	private static long getDatacenterId(long maxDatacenterId)
	{
		long id = 0L;
		try
		{
			java.net.InetAddress ip = java.net.InetAddress.getLocalHost();
			java.net.NetworkInterface network = java.net.NetworkInterface.getByInetAddress(ip);
			if(network == null)
			{
				id = 1L;
			}
			else
			{
				byte[] mac = network.getHardwareAddress();
				if(null != mac)
				{
					id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
					id = id % (maxDatacenterId + 1);
				}
			}
		}
		catch(Exception e)
		{
		}
		return id;
	}

	private static long getMaxWorkerId(long datacenterId, long maxWorkerId)
	{
		StringBuilder mpid = new StringBuilder();
		mpid.append(datacenterId);
		String name = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
		if(name != null && "".equals(name))
		{
			mpid.append(name.split("@")[0]);// GET jvmPid
		}
		return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);// MAC + PID 的 hashcode 获取16个低位
	}
	private static long datacenterId = getDatacenterId(maxDatacenterId);// 数据标识
	private static long workerId = getMaxWorkerId(datacenterId, maxWorkerId);// 数据标识
	private static long sequence = 0L;// 毫秒内序列号
	private static long lastTimeMillis = -1L;// 上次生成ID的时间截

	private static long timeGen()
	{
		return System.currentTimeMillis();
	}

	private static long nextMillis(long lastTimestamp)
	{
		long timestamp = timeGen();
		while(timestamp <= lastTimestamp)// 阻塞到下一个毫秒，直到获得新的时间戳
		{
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 根据时间戳和机器信息生产一个唯一ID，具有防止重复机制
	 * @return long 最大18位的id
	 */
	public synchronized static long genUniqueId()
	{
		long times = timeGen();
		// if(timestamp < lastTimestamp){}// 系统时钟回退过
		if(lastTimeMillis == times)
		{
			sequence = (sequence + 1) & sequenceMask;// 如果是同一时间生成的，则进行毫秒内序列
			if(sequence == 0)
			{
				times = nextMillis(lastTimeMillis);// 毫秒内序列溢出，阻塞到下一个毫秒,获得新的时间戳
			}
		}
		else
		{
			sequence = 0L;// 时间戳改变，毫秒内序列重置
		}
		lastTimeMillis = times;// 上次生成ID的时间截
		return (times << timestampShift) //
				| (datacenterId << datacenterIdShift) //
				| (workerId << workerIdShift) //
				| sequence;// 移位并通过或运算拼到一起组成64位的ID
	}

	/**
	 * 根据genUniqueId()获取到的id转化为java时间截
	 * @param uniqueId genUniqueId()获取到的id
	 * @return long java时间截
	 */
	public static long formatUniqueId(long uniqueId)
	{
		return uniqueId >> 14;
	}

	/**
	 * 根据时间戳产生一个唯一ID，具有防止重复机制
	 * @return long
	 */
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

	/**
	 * 返回GUID，格式00000000-0000-0000-0000-000000000000
	 * @return String
	 */
	public static String genGuid()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
