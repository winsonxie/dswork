/**
 * 应用系统Service
 */
package dswork.common.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonFuncDao;
import dswork.common.dao.DsCommonRoleDao;
import dswork.common.dao.DsCommonSystemDao;
import dswork.common.model.DsCommonRole;
import dswork.common.model.DsCommonSystem;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsCommonSystemService extends BaseService<DsCommonSystem, java.lang.Long>
{
	@Autowired
	private DsCommonSystemDao systemDao;
	@Autowired
	private DsCommonFuncDao funcDao;
	@Autowired
	private DsCommonRoleDao roleDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return systemDao;
	}
	

	@Override
	public int save(DsCommonSystem po)
	{
		systemDao.save(po);
		DsCommonRole role = new DsCommonRole();
		role.setId(dswork.core.util.UniqueId.genUniqueId());
		role.setSystemid(po.getId());
		role.setName("系统管理员");
		roleDao.save(role);
		return 1;
	}

	// 不能批量删除
	@Override
	@Deprecated
	public void deleteBatch(Long[] primaryKeys)
	{
		return;
	}

	/**
	 * 修改状态
	 * @param id 系统主键
	 * @param status 状态
	 */
	public void updateStatus(long id, int status)
	{
		systemDao.updateStatus(id, status);
	}

	/**
	 * 判断标识是否存在
	 * @param alias 标识
	 * @return boolean 存在true，不存在false
	 */
	public boolean isExistsByAlias(String alias)
	{
		return systemDao.isExistsByAlias(alias);
	}

	/**
	 * 获得systemid下的Func个数
	 * @param systemid 系统主键
	 * @return int
	 */
	public int getCountFuncBySystemid(long systemid)
	{
		Map map = new HashMap();
		map.put("systemid", systemid);
		PageRequest pageRequest = new PageRequest();
		pageRequest.setFilters(map);
		return funcDao.queryCount(pageRequest);
	}

	/**
	 * 获得systemid下的Role个数
	 * @param systemid 系统主键
	 * @return int
	 */
	public int getCountRoleBySystemid(long systemid)
	{
		Map map = new HashMap();
		map.put("systemid", systemid);
		PageRequest pageRequest = new PageRequest();
		pageRequest.setFilters(map);
		return roleDao.queryCount(pageRequest);
	}
	
	/**
	 * 更新排序
	 * @param ids 功能主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for(int i = 0; i < ids.length; i++)
		{
			systemDao.updateSeq(ids[i], i + 1L);
		}
	}
}
