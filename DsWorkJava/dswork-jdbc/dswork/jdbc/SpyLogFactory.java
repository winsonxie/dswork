package dswork.jdbc;

import org.slf4j.LoggerFactory;

public class SpyLogFactory
{
	private SpyLogFactory()
	{
	}
	private static SpyLogDelegator logger = new SpyLogDelegator(LoggerFactory.getLogger("jdbc.sqlonly"));

	public static SpyLogDelegator getSpyLogDelegator()
	{
		return logger;
	}

	public static void refresh()
	{
		logger = new SpyLogDelegator(LoggerFactory.getLogger("jdbc.sqlonly"));
	}

	public static void refresh(org.slf4j.Logger log)
	{
		logger = new SpyLogDelegator(log);
	}
}
