package dswork.core.util;

public class UniqueIdTest
{
	public static void main(String[] args)
	{
		for(int i = 0; i < 10; i++)
		{
			long id = IdUtil.genId();
			System.out.println(id);
			System.out.println(id >> 14);
			System.out.println(TimeUtil.formatDate(new java.util.Date(id >> 14), "yyyy-MM-dd HH:mm:ss"));
			String x = Long.toBinaryString(id);
			String y = x.substring(0, x.length() - 14);
			System.out.println(y);
			System.out.println(Long.parseLong(y, 2));
			System.out.println(TimeUtil.formatDate(new java.util.Date(Long.parseLong(y, 2)), "yyyy-MM-dd HH:mm:ss"));
			System.out.println("");
		}
	}
}
