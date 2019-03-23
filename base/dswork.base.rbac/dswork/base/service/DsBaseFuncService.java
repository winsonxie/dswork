/**
 * 功能Service
 */
package dswork.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseFuncDao;
import dswork.base.dao.DsBaseRoleDao;
import dswork.base.dao.DsBaseSystemDao;
import dswork.base.model.DsBaseFunc;
import dswork.base.model.DsBaseRes;
import dswork.base.model.DsBaseSystem;
import dswork.base.model.view.DsBaseFuncView;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseFuncService
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
	 * @param po 功能对象
	 */
	public void save(DsBaseFunc po)
	{
		po.setId(UniqueId.genUniqueId());
		funcDao.save(po);
	}

	/**
	 * 删除功能,包括其资源和被分配到角色的信息
	 * @param id 功能主键
	 */
	public int delete(Long id)
	{
		roleDao.deleteRoleFuncByFuncid(id);
		return funcDao.delete(id);
	}
	
	/**
	 * 删除系统的所有功能和被分配到角色的信息
	 * @param systemid 系统主键
	 * @return int
	 */
	public int deleteBySystem(Long systemid)
	{
		roleDao.deleteRoleFuncBySystemid(systemid);
		funcDao.deleteBySystemid(systemid);
		return 1;
	}

	/**
	 * 更新对象
	 * @param model 功能对象
	 * @return int
	 */
	public int update(DsBaseFunc po)
	{
		return funcDao.update(po);
	}

	/**
	 * 更新移动
	 * @param ids 功能主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updatePid(Long[] ids, long targetId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				funcDao.updatePid(ids[i], targetId, map);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 功能主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for(int i = 0; i < ids.length; i++)
		{
			funcDao.updateSeq(ids[i], i + 1L);
		}
	}

	/**
	 * 判断标识是否存在
	 * @param alias 标识
	 * @param systemid 所属系统主键
	 * @return boolean 存在true，不存在false
	 */
	public boolean isExistsByAlias(String alias, long systemid)
	{
		return funcDao.isExistsByAlias(alias, systemid);
	}

	/**
	 * 查询单个功能对象
	 * @param primaryKey 功能主键
	 * @return DsBaseFunc
	 */
	public DsBaseFunc get(Long primaryKey)
	{
		return (DsBaseFunc) funcDao.get(primaryKey);
	}

	/**
	 * 根据系统主键和上级功能主键取得列表数据
	 * @param systemid 系统主键
	 * @param pid 上级功能主键
	 * @return List&lt;DsBaseFunc&gt;
	 */
	public List<DsBaseFunc> queryList(Long systemid, Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("systemid", systemid);
		map.put("pid", pid);
		return funcDao.queryList(map);
	}

	/**
	 * 获得节点的子节点个数
	 * @param pid 上级功能主键
	 * @return int
	 */
	public int getCountByPid(long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		PageRequest pageRequest = new PageRequest();
		pageRequest.setFilters(map);
		return funcDao.queryCount(pageRequest);
	}
	
	/**
	 * 根据systemid查询func集合
	 * @param systemid
	 * @return
	 */
	public List<DsBaseFuncView> queryFuncBySystemid(long systemid)
	{
		return funcDao.queryFuncBySystemid(systemid);
	}
	
	/**
	 * 查询func表id集合，用于菜单导入时判断除了系统id为systemid之外的id是否存在
	 * @param systemid 需要导入菜单的系统id
	 * @return
	 */
	public List<String> queryFuncIdList(long systemid)
	{
		return funcDao.queryFuncIdList(systemid);
	}
	
	/**
	 * 查询systemid的func表id集合
	 * @param systemid 需要导入菜单的系统id
	 * @return
	 */
	public List<String> queryFuncOldIdList(long systemid)
	{
		return funcDao.queryFuncOldIdList(systemid);
	}
	
	/**
	 * DsBaseFunc的list数据入库
	 * @param list
	 * @param systemid
	 * @param oldDrIdList 数据库中系统id为systemid的已存在的菜单的id集合
	 */
	public void updateFuncList(List<DsBaseFunc> list, long systemid, List<String> oldDrIdList)
	{
		if(list != null && list.size() > 0)
		{
			List<String> newDrIdList = new ArrayList<String>();
			//数据库中已存在的菜单修改，不存在的添加
			for(DsBaseFunc o : list)
			{
				newDrIdList.add(String.valueOf(o.getId()));
				if(oldDrIdList.contains(String.valueOf(o.getId())))
				{
					funcDao.update(o);
				}
				else
				{
					funcDao.save(o);
				}
			}
			//删除多余的菜单
			for(String id : oldDrIdList)
			{
				if(!newDrIdList.contains(id))
				{
					funcDao.delete(Long.valueOf(id));
				}
			}
		}
	}
}
