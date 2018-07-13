package dswork.cms.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.cms.model.DsCmsSpecial;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsCmsSpecialDao extends MyBatisDao
{
	@Override
	protected Class getEntityClass()
	{
		return DsCmsSpecialDao.class;
	}

	public DsCmsSpecial get(Long id)
	{
		return (DsCmsSpecial) executeSelect("select", id);
	}

	public int save(DsCmsSpecial po)
	{
		return executeInsert("insert", po);
	}

	public int update(DsCmsSpecial po)
	{
		return executeUpdate("update", po);
	}

	public int delete(Long id)
	{
		return executeDelete("delete", id);
	}

	public List queryList(Map<String, Object> map)
	{
		return executeSelectList("query", map);
	}
}
