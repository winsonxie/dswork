package dswork.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import dswork.core.db.MyBatisDao;
import dswork.core.util.IdUtil;
import dswork.core.util.TimeUtil;

@Repository
@SuppressWarnings("all")
public class DsBaseUserLogDao extends MyBatisDao
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
		return DsBaseUserLogDao.class;
	}
	
	public void saveUserLog(String appid, String atype, String acode, int optype, String opread, boolean isSuccess, String ip, Long userid, String bm, String name)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", IdUtil.genId());
		map.put("appid", appid);
		map.put("atype", atype);
		map.put("acode", acode);
		map.put("optype", optype);
		map.put("optime", TimeUtil.getCurrentTime());
		map.put("opread", opread);
		map.put("status", isSuccess ? "1" : "0");
		map.put("ip", ip);
		map.put("userid", userid);
		map.put("bm", bm);
		map.put("name", name);
		executeInsert("insertUserLog", map);
	}
}
