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
public class DsBaseLoginDao extends MyBatisDao
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
		return DsBaseLoginDao.class;
	}

	public void saveLogLogin(String appid, String ticket, String ip, String bm, String name, boolean isSuccess)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String time = TimeUtil.getCurrentTime();
		map.put("id", IdUtil.genId());
		map.put("logintime", time);
		map.put("logouttime", isSuccess ? "0" : time);// 退出前标识为0
		map.put("appid", appid);
		map.put("ticket", ticket);
		map.put("ip", ip);
		map.put("bm", bm);
		map.put("name", name);
		map.put("status", isSuccess ? "1" : "0");
		executeInsert("insertLoginLog", map);
	}

	public void saveLogLogout(String ticket, boolean isTimeout, boolean isUpdatePassword)
	{
		if(ticket.length() > 0 && !ticket.equals("null"))
		{
			Map<String, Object> map = new HashMap<String, Object>();
			String time = TimeUtil.getCurrentTime();
			map.put("logouttime", time);
			map.put("timeouttime", isTimeout ? time : "");
			map.put("pwdtime", isUpdatePassword ? time : "");
			map.put("ticket", ticket);
			executeUpdate("updateLoginLog", map);
		}
	}
}
