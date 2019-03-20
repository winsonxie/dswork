/**
 * 用户Dao
 */
package dswork.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.common.model.DsCommonUser;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsCommonUserDao extends BaseDao<DsCommonUser, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsCommonUserDao.class;
	}

	/**
	 * 修改CA证书
	 * @param id 用户对象ID
	 * @param cakey ca证书
	 */
	public void updateCAKey(long id, String cakey)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("cakey", cakey);
		executeUpdate("updateCAKey", map);
	}

	/**
	 * 修改用户组织机构
	 * @param id 用户对象ID
	 * @param orgpid 单位ID
	 * @param orgid 单位部门ID
	 */
	public void updateOrg(long id, Long orgpid, Long orgid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("orgpid", orgpid);
		map.put("orgid", orgid);
		executeUpdate("updateOrg", map);
	}

	/**
	 * 修改密码
	 * @param id 用户对象ID
	 * @param password 加密后的密码
	 */
	public void updatePassword(long id, String password)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("password", password);
		executeUpdate("updatePassword", map);
	}

	/**
	 * 修改状态
	 * @param id 用户对象ID
	 * @param status 状态
	 */
	public void updateStatus(long id, int status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		executeUpdate("updateStatus", map);
	}

	/**
	 * 判断账号是否存在
	 * @param account
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isExistsByAccount(String account)
	{
		DsCommonUser user = getByAccount(account);
		if(user != null && user.getId().longValue() != 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据用户输入的账号来获得用户对象，有则返回用户对象，无则返回null。
	 * @param account - 用户输入的账号字符串。
	 * @return DsCommonUser - 用户对象。
	 */
	public DsCommonUser getByAccount(String account)
	{
		return (DsCommonUser) executeSelect("getByAccount", account);
	}
}
