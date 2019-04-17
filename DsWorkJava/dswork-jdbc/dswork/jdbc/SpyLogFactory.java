package dswork.jdbc;

public class SpyLogFactory
{
	private SpyLogFactory()
	{
	}
	private static SpyLogDelegator logger = new SpyLogDelegator();

	public static SpyLogDelegator getSpyLogDelegator()
	{
		return logger;
	}

	public static void refresh()
	{
		logger = new SpyLogDelegator();
	}
}
