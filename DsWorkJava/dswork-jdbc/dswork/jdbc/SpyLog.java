package dswork.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpyLog
{
	static final long SqlTimingWarn = 10000L;
	static final long SqlTimingWarnDebug = 60000L;
	private static String nl = System.getProperty("line.separator");
	public static Logger log = LoggerFactory.getLogger("jdbc.sqlonly");

	private SpyLog()
	{
	}

	public boolean isJdbcLoggingEnabled()
	{
		return log.isErrorEnabled();
	}

	public void exceptionOccured(Spy spy, String methodCall, Exception e, String sql, long execTime)
	{
		Integer spyNo = spy.getConnectionNumber();
		if(sql == null)
		{
			String classType = spy.getClassType();
			String header = spyNo + ". " + classType + "." + methodCall;
			log.error(header, e);
		}
		else
		{
			sql = (sql == null) ? "" : sql.trim();
			log.error(" {FAILED after " + execTime + " msec}" + nl + spyNo + ". " + sql, e);
		}
	}

	public void sqlOccured(Spy spy, long execTime, String methodCall, String sql)
	{
		if(log.isErrorEnabled())
		{
			if(log.isWarnEnabled())
			{
				if(execTime >= SpyLog.SqlTimingWarn)
				{
					log.warn(buildSqlDump(spy, execTime, methodCall, sql, log.isDebugEnabled() || (execTime > SpyLog.SqlTimingWarnDebug)));
				}
				else if(log.isDebugEnabled())
				{
					log.debug(buildSqlDump(spy, execTime, methodCall, sql, true));
				}
				else if(log.isInfoEnabled())
				{
					log.info(buildSqlDump(spy, execTime, methodCall, sql, false));
				}
			}
		}
	}

	private String buildSqlDump(Spy spy, long execTime, String methodCall, String sql, boolean debugInfo)
	{
		StringBuffer out = new StringBuffer();
		if(debugInfo)
		{
			out.append("{executed in ");
			out.append(execTime);
			out.append(" msec}");
		}
		out.append(nl);
		out.append(spy.getConnectionNumber());
		out.append(". ");
		sql = (sql == null) ? "" : sql.trim();
		out.append(sql);
		return out.toString();
	}

	public void debug(String msg)
	{
		log.debug(msg);
	}

	public void connectionOpened(Spy spy)
	{
		log.info(spy.getConnectionNumber() + ". Connection opened");
	}

	public void connectionClosed(Spy spy)
	{
		log.info(spy.getConnectionNumber() + ". Connection closed ");
	}
	
	static SpyLog spylog = new SpyLog();
	
	public static SpyLog getLog()
	{
		return spylog;
	}
}
