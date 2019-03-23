/**
 * 角色选择Service
 */
package dswork.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseFuncDao;
import dswork.base.dao.DsBaseRoleDao;
import dswork.base.dao.DsBaseSystemDao;
import dswork.base.model.DsBaseFunc;
import dswork.base.model.DsBaseRole;
import dswork.base.model.DsBaseRoleFunc;
import dswork.base.model.DsBaseSystem;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsBaseRoleChooseService
{
	@Autowired
	private DsBaseSystemDao systemDao;
	@Autowired
	private DsBaseFuncDao funcDao;
	@Autowired
	private DsBaseRoleDao roleDao;

	public DsBaseSystem getSystem(Long primaryKey)
	{
		return (DsBaseSystem) systemDao.get(primaryKey);
	}
	
	public Page<DsBaseSystem> querySystemPage(PageRequest pageRequest)
	{
		Page<DsBaseSystem> page = systemDao.queryPage(pageRequest);
		return page;
	}

	/**
	 * 查询单个角色对象
	 * @param primaryKey 角色主键
	 * @return DsBaseRole
	 */
	public DsBaseRole get(Long primaryKey)
	{
		return (DsBaseRole) roleDao.get(primaryKey);
	}

	/**
	 * 根据系统主键和上级角色主键取得列表数据
	 * @param systemid 系统主键
	 * @param pid 上级角色主键
	 * @return List&lt;DsBaseRole&gt;
	 */
	public List<DsBaseRole> queryRoleList(Long systemid, Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", pid);
		return roleDao.queryList(map);
	}

	/**
	 * 获得当前系统下的所有功能
	 * @param systemid 系统主键
	 * @return List&lt;DsBaseFunc&gt;
	 */
	public List<DsBaseFunc> queryFuncList(Long systemid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", null);
		return funcDao.queryList(map);
	}

	/**
	 * 获得当前角色下的所有权限
	 * @param roleid 角色主键
	 * @return List&lt;DsBaseRoleFunc&gt;
	 */
	public List<DsBaseRoleFunc> queryFuncListByRoleid(long roleid)
	{
		return roleDao.queryRoleFuncByRoleid(roleid);
	}
}
