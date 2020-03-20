package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.common.model.IUserBind;
import dswork.common.model.IUserBindState;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseUserBindDao extends MyBatisDao
{
	private SqlSessionTemplate sqlSessionTemplateCommon;
	private static boolean hasCommon = false;

	@Override
	protected SqlSessionTemplate getSqlSessionTemplate()
	{
		if(hasCommon)
		{
			return sqlSessionTemplateCommon;
		}
		return super.getSqlSessionTemplate();
	}

	public void setSqlSessionTemplateCommon(SqlSessionTemplate sqlSessionTemplate)
	{
		if(log.isInfoEnabled())
		{
			log.info("======== DsCommonDaoCommonIDict call setSqlSessionTemplateCommon ========");
		}
		hasCommon = true;
		this.sqlSessionTemplateCommon = sqlSessionTemplate;
	}

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
		return (IUserBind) executeSelect("queryUserBind", map);
	}

	public IUserBind queryUserBindByOpenid(String openid, long bindid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", openid);
		map.put("bindid", bindid);
		return (IUserBind) executeSelect("queryUserBind", map);
	}

	public List<IUserBind> queryUserBindByUnionid(String unionid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unionid", unionid);
		return (List<IUserBind>) executeSelectList("queryUserBind", map);
	}

	public List<IUserBindState> getUserBindStateByUserId(long userid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return (List<IUserBindState>) executeSelectList("getUserBindStateByUserId", map);
	}

	public int updateUserBindForUnBind(long userid, String bindids)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("bindids", bindids);
		return executeUpdate("updateUserBindForUnBind", map);
	}
}
