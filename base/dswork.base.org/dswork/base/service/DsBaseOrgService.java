/**
 * 组织机构Service
 */
package dswork.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseOrgDao;
import dswork.base.model.DsBaseOrg;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseOrgService
{
	@Autowired
	private DsBaseOrgDao orgDao;

	/**
	 * 新增对象
	 * @param entity 组织机构对象
	 */
	public void save(DsBaseOrg entity)
	{
		entity.setId(UniqueId.genUniqueId());
		orgDao.save(entity);
	}

	/**
	 * 删除组织机构
	 * @param id 组织机构主键
	 * @return int
	 */
	public int delete(Long id)
	{
		return orgDao.delete(id);
	}

	/**
	 * 修改对象
	 * @param entity 组织机构对象
	 * @return int
	 */
	public int update(DsBaseOrg entity)
	{
		return orgDao.update(entity);
	}

	/**
	 * 更新移动
	 * @param ids 组织机构主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updatePid(Long[] ids, long targetId)
	{
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				orgDao.updatePid(ids[i], targetId);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 组织机构主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for (int i = 0; i < ids.length; i++)
		{
			orgDao.updateSeq(ids[i], i + 1L);
		}
	}

	/**
	 * 查询单个组织机构对象
	 * @param primaryKey 组织机构主键
	 * @return DsBaseOrg
	 */
	public DsBaseOrg get(Long primaryKey)
	{
		return (DsBaseOrg) orgDao.get(primaryKey);
	}
	
	/**
	 * 根据上级组织机构主键取得列表数据
	 * @param pid 上级组织机构主键
	 * @return List&lt;DsBaseOrg&gt;
	 */
	public List<DsBaseOrg> queryList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		return orgDao.queryList(map);
	}

	/**
	 * 获得节点指定类型的子节点个数
	 * @param pid 上级组织机构主键
	 * @param status 指定类型的下级节点，为null则查询全部
	 * @return int
	 */
	public int getCountByPid(long pid, Integer status)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("status", status);
		return orgDao.queryCount(new PageRequest(map));
	}
}
