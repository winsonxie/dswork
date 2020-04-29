package dswork.core.util;

/**
 * ID生成类
 */
public class IdUtil
{
	private static long getIdDatacenter(long maxDatacenterId)
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

	private static long getIdWorker(long datacenterId, long maxWorkerId)
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
	
	private final static long idDatacenterLength = 5L;// 数据标识所占的2进制位数
	private final static long idWorkerLength     = 5L;// 机器码所占的2进制位数
	private final static long maxIdDatacenter   = -1L ^ (-1L << idDatacenterLength);// 数据标识的long值，5位是31
	private final static long maxIdWorker        = -1L ^ (-1L << idWorkerLength);// 机器码的long值，5位是31
	private final static long datacenterId = getIdDatacenter(maxIdDatacenter);// 数据标识
	private final static long workerId = getIdWorker(datacenterId, maxIdWorker);// 数据标识
	
	
	// 用于生成16位id
	private final static long datacenterIdShift = idWorkerLength;// 数据标识向左移5位(5)
	private final static long timestampShift    = idWorkerLength + idDatacenterLength;// 时间截向左移10位(5+5)
	private       static long lastTimeMillis = -1L;// 上次生成ID的时间截

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
	 * 根据时间戳和机器信息生产一个唯一的16位ID，具有防止重复机制<br>
	 * 结构：正数0 - 时间戳 - 10位机器码 <br>
	 * 长型形共64位，最高位是符号位，0正数，1负数：<br>
	 * 0 - 000 - 0000000000 0000000000 0000000000 0000000000 0000000000 - 1111111111<br>
	 * javascript中不会计算出问题的长整形最大值为9007199254740992，取二进制53位全1，即9007199254740991<br>
	 * 0 - 000 00 - 000001 11 1111111111 1111111111 1111111111 1111111111 - 1111111111<br>
	 * 除去位移的10位，其余43位最大值为：(Long.parseLong("1111111111111111111111111111111111111111111", 2)) = 8796093022207L<br>
	 * 即2248-09-26 23:10:22.207及之后的id将超出js可计算长度
	 * 10位的数据机器位，可以部署在1024个节点<br>
	 * @return long 最大16位的id
	 */
	public synchronized static long genId()
	{
		long times = timeGen();
		if(lastTimeMillis == times)
		{
			times = nextMillis(lastTimeMillis);// 毫秒内序列溢出，阻塞到下一个毫秒,获得新的时间戳
		}
		lastTimeMillis = times;// 上次生成ID的时间截
		return (times << timestampShift) //
				| (datacenterId << datacenterIdShift) //
				| workerId;// 移位并通过或运算拼到一起组成ID
	}

	/**
	 * 根据genId()获取到的id转化为java时间截
	 * @param id genId()获取到的id
	 * @return long java时间截
	 */
	public static long formatId(long id)
	{
		return id >> 10;
	}
	

	// 用于生成18位id
	private final static long     idSequenceLength    = 8L;// 序列号所占的2进制位数
	private final static long UniqueIdWorkerShift     = idSequenceLength;
	private final static long UniqueIdDatacenterShift = idSequenceLength + idWorkerLength;// 数据标识向左移13位(5+8)
	private final static long UniqueTimestampShift    = idSequenceLength + idWorkerLength + idDatacenterLength;// 时间截向左移18位(5+5+8)
	private final static long UniqueSequenceMask      = -1L ^ (-1L << idSequenceLength);// 生成序列的掩码，这里为255
	private       static long UniqueLastTimeMillis    = -1L;// 上次生成ID的时间截
	private       static long sequence                = 0L;// 毫秒内序列号

	/**
	 * 根据时间戳和机器信息生产一个唯一的18位ID，javascript不适用，需转String，具有防止重复机制，但2090-11-18 22:07:45.624后的ID将超出18位<br>
	 * 结构：正数0 - 时间戳 - 10位机器码 - 8位序列号<br>
	 * 长型形共64位，最高位是符号位，0正数，1负数：<br>
	 * 0 - 000 - 00 0000000000 0000000000 0000000000 0000000000 - 1111111111 - 11111111<br>
	 * 18位长整形最大值为（时间戳有42位）：<br>
	 * 0 - 000 - 11 0111100000 1011011010 1100111010 0111011000 - 1111111111 - 11111111<br>
	 * (Long.parseLong("110111100000101101101011001110100111011000", 2)) = 3814697265624L<br>
	 * 10位的数据机器位，可以部署在1024个节点<br>
	 * 8位序列号可支持每个节点每毫秒(同一机器，同一时间截)产生256个ID序号
	 * @return long 最大18位的id
	 */
	public synchronized static long genUniqueId()
	{
		long times = timeGen();
		// if(timestamp < lastTimestamp){}// 系统时钟回退过
		if(UniqueLastTimeMillis == times)
		{
			sequence = (sequence + 1) & UniqueSequenceMask;// 如果是同一时间生成的，则进行毫秒内序列
			if(sequence == 0)
			{
				times = nextMillis(UniqueLastTimeMillis);// 毫秒内序列溢出，阻塞到下一个毫秒,获得新的时间戳
			}
		}
		else
		{
			sequence = 0L;// 时间戳改变，毫秒内序列重置
		}
		UniqueLastTimeMillis = times;// 上次生成ID的时间截
		return (times << UniqueTimestampShift) //
				| (datacenterId << UniqueIdDatacenterShift) //
				| (workerId << UniqueIdWorkerShift) //
				| sequence;// 移位并通过或运算拼到一起组成64位的ID
	}

	/**
	 * 根据genUniqueId()获取到的id转化为java时间截
	 * @param id genId()获取到的id
	 * @return long java时间截
	 */
	public static long formatUniqueId(long id)
	{
		return id >> 18;
	}

	private static long lastTimeId = -1L;// 上次生成ID的时间截
	/**
	 * 根据时间戳产生一个唯一ID，具有防止重复机制
	 * @return long
	 */
	public synchronized static long genTimeId()
	{
		long id = 0;
		do
		{
			id = timeGen();
		}
		while(id == lastTimeId);
		lastTimeId = id;
		return id;
	}
}
