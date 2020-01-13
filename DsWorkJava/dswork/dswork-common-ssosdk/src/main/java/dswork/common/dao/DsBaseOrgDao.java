package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.common.model.IOrg;
import dswork.core.db.MyBatisDao;

@Repository
@SuppressWarnings("all")
public class DsBaseOrgDao extends MyBatisDao
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
		return DsBaseOrgDao.class;
	}

	// org //////////////////////////////////////////////////////////////////
	public IOrg getOrgByOrgid(long orgid)
	{
		return (IOrg) executeSelect("getOrgByOrgid", orgid);
	}

	public List<IOrg> queryOrgByOrgPid(long orgpid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orgpid", orgpid);
		return executeSelectList("queryOrgByOrgpid", map);
	}

	// post //////////////////////////////////////////////////////////////////
	public List<IOrg> queryPostByUserId(Long userid)
	{
		return executeSelectList("queryPostByUserId", userid);
	}
}
