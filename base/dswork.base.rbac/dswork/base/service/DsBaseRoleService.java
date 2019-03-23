/**
 * 角色Service
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
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseRoleService
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

	/**
	 * 新增对象
	 * @param po 角色对象
	 * @param list 角色功能列表
	 */
	public void save(DsBaseRole po, List<DsBaseRoleFunc> list)
	{
		po.setId(UniqueId.genUniqueId());
		roleDao.save(po);
		if(null != list)
		{
			for(DsBaseRoleFunc tmp : list)
			{
				tmp.setId(UniqueId.genUniqueId());
				tmp.setSystemid(po.getSystemid());
				tmp.setRoleid(po.getId());
				roleDao.saveRoleFunc(tmp);
			}
		}
	}

	/**
	 * 删除角色,包括其权限
	 * @param id 角色主键
	 */
	public int delete(Long id)
	{
		roleDao.deleteRoleFuncByRoleid(id);
		return roleDao.delete(id);
	}

	/**
	 * 更新对象
	 * @param model 角色对象
	 * @param list 角色功能列表
	 * @return int
	 */
	public int update(DsBaseRole po, List<DsBaseRoleFunc> list)
	{
		roleDao.update(po);
		if(null == list)
		{
			return 1;
		}
		roleDao.deleteRoleFuncByRoleid(po.getId());
		for(DsBaseRoleFunc tmp : list)
		{
			tmp.setId(UniqueId.genUniqueId());
			tmp.setRoleid(po.getId());
			tmp.setSystemid(po.getSystemid());
			roleDao.saveRoleFunc(tmp);
		}
		return 1;
	}

	/**
	 * 更新移动
	 * @param ids 角色主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updatePid(Long[] ids, long targetId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				roleDao.updatePid(ids[i], targetId, map);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 角色主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for(int i = 0; i < ids.length; i++)
		{
			roleDao.updateSeq(ids[i], i + 1L);
		}
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
	public List<DsBaseRole> queryList(Long systemid, Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", pid);
		return roleDao.queryList(map);
	}

	/**
	 * 获得节点的子节点个数
	 * @param pid 上级角色主键
	 * @return int
	 */
	public int getCountByPid(long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		PageRequest pageRequest = new PageRequest();
		pageRequest.setFilters(map);
		return roleDao.queryCount(pageRequest);
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
