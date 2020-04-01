package dswork.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class StatementSpy implements Statement, Spy
{
	static SpyLog log = SpyLog.getLog();
	protected ConnectionSpy connectionSpy;
	protected Statement realStatement;
	
	private static String rightJustify(int fieldSize, String field)
	{
		if(field == null)
		{
			field = "";
		}
		StringBuffer output = new StringBuffer();
		for(int i = 0, j = fieldSize - field.length(); i < j; i++)
		{
			output.append(' ');
		}
		output.append(field);
		return output.toString();
	}

	public Statement getRealStatement()
	{
		return realStatement;
	}

	public StatementSpy(ConnectionSpy connectionSpy, Statement realStatement)
	{
		if(realStatement == null)
		{
			throw new IllegalArgumentException("Must pass in a non null real Statement");
		}
		if(connectionSpy == null)
		{
			throw new IllegalArgumentException("Must pass in a non null ConnectionSpy");
		}
		this.realStatement = realStatement;
		this.connectionSpy = connectionSpy;
	}

	public String getClassType()
	{
		return "StatementSpy";
	}

	public Integer getConnectionNumber()
	{
		return connectionSpy.getConnectionNumber();
	}

	protected void reportException(String methodCall, SQLException exception, String sql, long execTime)
	{
		log.exceptionOccured(this, methodCall, exception, sql, execTime);
	}

	protected void reportSql(long execTime, String sql, String methodCall)
	{
		log.sqlOccured(this, execTime, methodCall, sql);
	}

	public SQLWarning getWarnings() throws SQLException
	{
		return realStatement.getWarnings();
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException
	{
		String methodCall = "executeUpdate(" + sql + ", " + columnNames + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			int result = realStatement.executeUpdate(sql, columnNames);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException
	{
		String methodCall = "execute(" + sql + ", " + columnNames + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			boolean result = realStatement.execute(sql, columnNames);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public void setMaxRows(int max) throws SQLException
	{
		realStatement.setMaxRows(max);
	}

	public boolean getMoreResults() throws SQLException
	{
		return realStatement.getMoreResults();
	}

	public void clearWarnings() throws SQLException
	{
		realStatement.clearWarnings();
	}
	
	@SuppressWarnings("all")
	protected List currentBatch = new ArrayList();
	
	@SuppressWarnings("all")
	public void addBatch(String sql) throws SQLException
	{
		currentBatch.add(sql);
		realStatement.addBatch(sql);
	}

	public int getResultSetType() throws SQLException
	{
		return realStatement.getResultSetType();
	}

	public void clearBatch() throws SQLException
	{
		realStatement.clearBatch();
		currentBatch.clear();
	}

	public void setFetchDirection(int direction) throws SQLException
	{
		realStatement.setFetchDirection(direction);
	}

	public int[] executeBatch() throws SQLException
	{
		String methodCall = "executeBatch()";
		int j = currentBatch.size();
		StringBuffer batchReport = new StringBuffer("batching " + j + " statements:");
		int fieldSize = ("" + j).length();
		String sql;
		for(int i = 0; i < j;)
		{
			sql = (String) currentBatch.get(i);
			batchReport.append("\n");
			batchReport.append(StatementSpy.rightJustify(fieldSize, "" + (++i)));
			batchReport.append(":  ");
			batchReport.append(sql);
		}
		sql = batchReport.toString();
		long tstart = System.currentTimeMillis();
		int[] updateResults;
		try
		{
			updateResults = realStatement.executeBatch();
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
		currentBatch.clear();
		return updateResults;
	}

	public void setFetchSize(int rows) throws SQLException
	{
		realStatement.setFetchSize(rows);
	}

	public int getQueryTimeout() throws SQLException
	{
		return realStatement.getQueryTimeout();
	}

	public Connection getConnection() throws SQLException
	{
		return connectionSpy;
	}

	public ResultSet getGeneratedKeys() throws SQLException
	{
		return realStatement.getGeneratedKeys();
	}

	public void setEscapeProcessing(boolean enable) throws SQLException
	{
		realStatement.setEscapeProcessing(enable);
	}

	public int getFetchDirection() throws SQLException
	{
		return realStatement.getFetchDirection();
	}

	public void setQueryTimeout(int seconds) throws SQLException
	{
		realStatement.setQueryTimeout(seconds);
	}

	public boolean getMoreResults(int current) throws SQLException
	{
		return realStatement.getMoreResults(current);
	}

	public ResultSet executeQuery(String sql) throws SQLException
	{
		String methodCall = "executeQuery(" + sql + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			ResultSet result = realStatement.executeQuery(sql);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public int getMaxFieldSize() throws SQLException
	{
		return realStatement.getMaxFieldSize();
	}

	public int executeUpdate(String sql) throws SQLException
	{
		String methodCall = "executeUpdate(" + sql + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			int result = realStatement.executeUpdate(sql);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public void cancel() throws SQLException
	{
		realStatement.cancel();
	}

	public void setCursorName(String name) throws SQLException
	{
		realStatement.setCursorName(name);
	}

	public int getFetchSize() throws SQLException
	{
		return realStatement.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException
	{
		return realStatement.getResultSetConcurrency();
	}

	public int getResultSetHoldability() throws SQLException
	{
		return realStatement.getResultSetHoldability();
	}

	// jdbc4
	public boolean isClosed() throws SQLException
	{
		return realStatement.isClosed();
	}

	// jdbc4
	public void setPoolable(boolean poolable) throws SQLException
	{
		realStatement.setPoolable(poolable);
	}

	// jdbc4
	public boolean isPoolable() throws SQLException
	{
		return realStatement.isPoolable();
	}

	public void setMaxFieldSize(int max) throws SQLException
	{
		realStatement.setMaxFieldSize(max);
	}

	public boolean execute(String sql) throws SQLException
	{
		String methodCall = "execute(" + sql + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			boolean result = realStatement.execute(sql);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
	{
		String methodCall = "executeUpdate(" + sql + ", " + autoGeneratedKeys + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			int result = realStatement.executeUpdate(sql, autoGeneratedKeys);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
	{
		String methodCall = "execute(" + sql + ", " + autoGeneratedKeys + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			boolean result = realStatement.execute(sql, autoGeneratedKeys);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
	{
		String methodCall = "executeUpdate(" + sql + ", " + columnIndexes + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			int result = realStatement.executeUpdate(sql, columnIndexes);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException
	{
		String methodCall = "execute(" + sql + ", " + columnIndexes + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			boolean result = realStatement.execute(sql, columnIndexes);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	public ResultSet getResultSet() throws SQLException
	{
		return realStatement.getResultSet();
	}

	public int getMaxRows() throws SQLException
	{
		return realStatement.getMaxRows();
	}

	public void close() throws SQLException
	{
		realStatement.close();
	}

	public int getUpdateCount() throws SQLException
	{
		return realStatement.getUpdateCount();
	}

	// jdbc4
	@SuppressWarnings("all")
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		return (iface != null && (iface == Connection.class || iface == Spy.class)) ? (T) this : realStatement.unwrap(iface);
	}

	// jdbc4
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return (iface != null && (iface == Statement.class || iface == Spy.class)) || realStatement.isWrapperFor(iface);
	}
	
	// jdbc 4.1
	public void closeOnCompletion() throws SQLException
	{
		realStatement.closeOnCompletion();
	}
	
	// jdbc 4.1
	public boolean isCloseOnCompletion() throws SQLException
	{
		return realStatement.isCloseOnCompletion();
	}

	// jdbc 4.2
	public long getLargeUpdateCount() throws SQLException
	{
		return realStatement.getLargeUpdateCount();
	}

	// jdbc 4.2
	public void setLargeMaxRows(long max) throws SQLException
	{
		realStatement.setLargeMaxRows(max);
	}

	// jdbc 4.2
	public long getLargeMaxRows() throws SQLException
	{
		return realStatement.getLargeMaxRows();
	}

	// jdbc 4.2
	public long[] executeLargeBatch() throws SQLException
	{
		return realStatement.executeLargeBatch();
	}

	// jdbc 4.2
	public long executeLargeUpdate(String sql) throws SQLException
	{
		String methodCall = "executeLargeUpdate(" + sql + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			long result = realStatement.executeLargeUpdate(sql);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	// jdbc 4.2
	public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException
	{
		String methodCall = "executeLargeUpdate(" + sql + ", " + autoGeneratedKeys + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			long result = realStatement.executeLargeUpdate(sql, autoGeneratedKeys);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	// jdbc 4.2
	public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException
	{
		String methodCall = "executeLargeUpdate(" + sql + ", " + columnIndexes + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			long result = realStatement.executeLargeUpdate(sql, columnIndexes);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}

	// jdbc 4.2
	public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException
	{
		String methodCall = "executeLargeUpdate(" + sql + ", " + columnNames + ")";
		long tstart = System.currentTimeMillis();
		try
		{
			long result = realStatement.executeLargeUpdate(sql, columnNames);
			reportSql(System.currentTimeMillis() - tstart, sql, methodCall);
			return result;
		}
		catch(SQLException s)
		{
			reportException(methodCall, s, sql, System.currentTimeMillis() - tstart);
			throw s;
		}
	}
}
