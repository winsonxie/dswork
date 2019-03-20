/**
 * 角色选择Service
 */
package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonFuncDao;
import dswork.common.dao.DsCommonRoleDao;
import dswork.common.dao.DsCommonSystemDao;
import dswork.common.model.DsCommonFunc;
import dswork.common.model.DsCommonRole;
import dswork.common.model.DsCommonRoleFunc;
import dswork.common.model.DsCommonSystem;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsCommonRoleChooseService
{
	@Autowired
	private DsCommonSystemDao systemDao;
	@Autowired
	private DsCommonFuncDao funcDao;
	@Autowired
	private DsCommonRoleDao roleDao;

	public DsCommonSystem getSystem(Long primaryKey)
	{
		return (DsCommonSystem) systemDao.get(primaryKey);
	}
	
	public Page<DsCommonSystem> querySystemPage(PageRequest pageRequest)
	{
		Page<DsCommonSystem> page = systemDao.queryPage(pageRequest);
		return page;
	}

	/**
	 * 查询单个角色对象
	 * @param primaryKey 角色主键
	 * @return DsCommonRole
	 */
	public DsCommonRole get(Long primaryKey)
	{
		return (DsCommonRole) roleDao.get(primaryKey);
	}

	/**
	 * 根据系统主键和上级角色主键取得列表数据
	 * @param systemid 系统主键
	 * @param pid 上级角色主键
	 * @return List&lt;DsCommonRole&gt;
	 */
	public List<DsCommonRole> queryRoleList(Long systemid, Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", pid);
		return roleDao.queryList(map);
	}

	/**
	 * 获得当前系统下的所有功能
	 * @param systemid 系统主键
	 * @return List&lt;DsCommonFunc&gt;
	 */
	public List<DsCommonFunc> queryFuncList(Long systemid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", null);
		return funcDao.queryList(map);
	}

	/**
	 * 获得当前角色下的所有权限
	 * @param roleid 角色主键
	 * @return List&lt;DsCommonRoleFunc&gt;
	 */
	public List<DsCommonRoleFunc> queryFuncListByRoleid(long roleid)
	{
		return roleDao.queryRoleFuncByRoleid(roleid);
	}
}
