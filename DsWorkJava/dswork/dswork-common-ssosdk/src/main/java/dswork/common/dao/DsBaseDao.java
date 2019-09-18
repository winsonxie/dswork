package dswork.common.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import dswork.common.model.IBind;
import dswork.common.model.IUnit;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseDao.class;
	}

	public List<IUnit> queryListUnit()
	{
		return (List<IUnit>) executeSelectList("queryUnit", new HashMap<String, Object>());
	}
	public List<IBind> queryListBind()
	{
		return (List<IBind>) executeSelectList("queryBind", new HashMap<String, Object>());
	}
}
