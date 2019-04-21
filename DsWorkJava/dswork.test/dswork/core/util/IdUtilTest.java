package dswork.core.util;

public class IdUtilTest
{
	public static void main(String[] args)
	{
		System.out.println(Long.parseLong("111111111111111111", 2));
		
		for(int i = 0; i < 10; i++)
		{
			long id = IdUtil.genId();
			System.out.println(id);
			System.out.println(TimeUtil.formatDate(new java.util.Date(id >> 18), "yyyy-MM-dd HH:mm:ss.SSS"));
			System.out.println("");
		}
	}
}
