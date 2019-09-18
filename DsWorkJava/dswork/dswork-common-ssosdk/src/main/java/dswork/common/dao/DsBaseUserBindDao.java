package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.IUserBind;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseUserBindDao extends MyBatisDao
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseUserBindDao.class;
	}

	public int saveUserBind(IUserBind userBind)
	{
		return executeInsert("insertUserBind", userBind);
	}

	public int updateUserBind(IUserBind userBind)
	{
		return executeUpdate("updateUserBind", userBind);
	}

	public IUserBind getUserBindById(long id)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return (IUserBind) executeSelect("queryUserBind", id);
	}

	public IUserBind queryUserBindByOpenid(String openid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", openid);
		return (IUserBind) executeSelect("queryUserBind", map);
	}

	public List<IUserBind> queryUserBindByUnionid(String unionid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unionid", unionid);
		return (List<IUserBind>) executeSelectList("queryUserBind", map);
	}
}
