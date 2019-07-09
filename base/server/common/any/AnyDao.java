package common.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.core.db.MyBatisDao;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Repository
@SuppressWarnings("all")
public class AnyDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return AnyDao.class;
	}
	
	public static Map<String, Object> initSql(String sql)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sql", sql);
		return map;
	}

	public int executeInsert(Object parameter)
	{
		return getSqlSessionTemplate().insert(getStatementName("query"), parameter);
	}

	public int executeDelete(Object parameter)
	{
		return getSqlSessionTemplate().delete(getStatementName("query"), parameter);
	}

	public int executeUpdate(Object parameter)
	{
		return getSqlSessionTemplate().update(getStatementName("query"), parameter);
	}

	public Object executeSelect(Object parameter)
	{
		return getSqlSessionTemplate().selectOne(getStatementName("query"), parameter);
	}

	public Object executeCount(Object parameter)
	{
		return getSqlSessionTemplate().selectOne(getStatementName("queryCount"), parameter);
	}

	public List executeSelectList(Object parameter)
	{
		return getSqlSessionTemplate().selectList(getStatementName("query"), parameter);
	}
	
	public Page queryPage(PageRequest pageRequest, PageRequest pageRequestCount)
	{
		return queryPage("query", pageRequest, "queryCount", pageRequestCount);
	}
}