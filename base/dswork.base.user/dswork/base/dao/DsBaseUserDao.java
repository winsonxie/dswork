/**
 * 用户Dao
 */
package dswork.base.dao;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Repository;

import dswork.base.model.DsBaseUser;
import dswork.base.model.DsBaseUserBm;
import dswork.core.db.BaseDao;

@Repository
@SuppressWarnings("all")
public class DsBaseUserDao extends BaseDao<DsBaseUser, Long>
{
	@Override
	public Class getEntityClass()
	{
		return DsBaseUserDao.class;
	}

	/**
	 * 新增用户
	 * @param entity 需要新增的对象模型
	 * @return int
	 */
	@Override
	public int save(DsBaseUser entity)
	{
		entity.setAccount(entity.getAccount().toLowerCase(Locale.ENGLISH));
		executeInsert("insert", entity);
		DsBaseUserBm bm = new DsBaseUserBm();
		bm.setBm(entity.getAccount());
		bm.setUserid(entity.getId());
		bm.setType(0);
		executeInsert("insertBm", bm);
		return 1;
	}

	/**
	 * 删除用户
	 * @param primaryKey 用户ID
	 * @return int
	 */
	@Override
	public int delete(Long primaryKey)
	{
		executeDelete("deleteBmByUserid", primaryKey);
		return executeDelete("delete", primaryKey);
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
		DsBaseUser user = getByAccount(account);
		if(user != null && user.getId().longValue() != 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * 根据用户输入的账号来获得用户对象，有则返回用户对象，无则返回null。
	 * @param account - 用户输入的账号字符串。
	 * @return DsBaseUser - 用户对象。
	 */
	public DsBaseUser getByAccount(String account)
	{
		return (DsBaseUser) executeSelect("getByAccount", account);
	}
}
