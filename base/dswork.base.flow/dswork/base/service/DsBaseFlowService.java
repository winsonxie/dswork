/**
 * 流程管理Service
 */
package dswork.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseFlowCategoryDao;
import dswork.base.dao.DsBaseFlowDao;
import dswork.base.dao.DsBaseFlowTaskDao;
import dswork.base.model.DsBaseFlow;
import dswork.base.model.DsBaseFlowCategory;
import dswork.base.model.DsBaseFlowTask;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;

@Service
@SuppressWarnings("all")
public class DsBaseFlowService
{
	@Autowired
	private DsBaseFlowCategoryDao categoryDao;
	@Autowired
	private DsBaseFlowDao flowDao;
	@Autowired
	private DsBaseFlowTaskDao taskDao;

	public int saveFlow(DsBaseFlow flow, List<DsBaseFlowTask> list)
	{
		flow.setId(UniqueId.genUniqueId());
		flow.setDeployid("");
		flow.setStatus(0);
		flow.setVnum(0);
		flowDao.save(flow);
		for(DsBaseFlowTask m : list)
		{
			m.setId(UniqueId.genUniqueId());
			m.setFlowid(flow.getId());
			m.setDeployid("");
			taskDao.save(m);
		}
		return 1;
	}

	public int deleteFlow(Long flowid)
	{
		DsBaseFlow f = (DsBaseFlow) flowDao.get(flowid);
		if(f.getDeployid().length() == 0)
		{
			taskDao.deleteByFlowid(flowid);
			flowDao.delete(flowid);
		}
		return 1;
	}

	public int updateFlow(DsBaseFlow flow, List<DsBaseFlowTask> list)
	{
		flowDao.update(flow);
		taskDao.deleteByFlowid(flow.getId());
		for(DsBaseFlowTask task : list)
		{
			task.setId(UniqueId.genUniqueId());
			task.setFlowid(flow.getId());
			taskDao.save(task);
		}
		return 1;
	}

	public List<DsBaseFlow> queryListFlow(Map<String, Object> map)
	{
		return flowDao.queryList(map);
	}

	public void updateStatus(Long id, int status)
	{
		flowDao.updateStatus(id, status);
	}

	public DsBaseFlow getFlow(Long id)
	{
		DsBaseFlow flow = (DsBaseFlow) flowDao.get(id);
		flow.setTaskList(taskDao.queryList(id));
		return flow;
	}

	public int deployFlow(Long flowid)
	{
		DsBaseFlow flow = (DsBaseFlow) flowDao.get(flowid);
		if(flow.getVnum() == 0)
		{
			flow.setId(UniqueId.genUniqueId());
			String deployid = flow.getAlias() + "-" + flow.getId();
			// 把旧的数据处理为发布版本
			flow.setDeployid(deployid);
			flow.setVnum(1);
			flow.setStatus(1);
			flowDao.save(flow);// 新的流程
			flowDao.updateDeployid(flowid, deployid);// 更新VNUM为0的deployid为最新发布的id
			List<DsBaseFlowTask> list = taskDao.queryList(flowid);
			if(list != null && list.size() > 0)
			{
				for(DsBaseFlowTask m : list)
				{
					m.setId(UniqueId.genUniqueId());
					m.setFlowid(flow.getId());
					m.setDeployid(deployid);
					taskDao.save(m);
				}
			}
			return 1;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 判断标识是否存在
	 * @param alias 标识
	 * @return boolean 存在true，不存在false
	 */
	public boolean isExistsByAlias(String alias)
	{
		return flowDao.isExistsByAlias(alias);
	}

	/**
	 * 新增对象
	 * @param entity 流程管理对象
	 */
	public void save(DsBaseFlowCategory entity)
	{
		entity.setId(UniqueId.genUniqueId());
		categoryDao.save(entity);
	}

	/**
	 * 删除流程管理
	 * @param id 流程管理主键
	 * @return int
	 */
	public int delete(Long id)
	{
		return categoryDao.delete(id);
	}

	/**
	 * 修改对象
	 * @param entity 流程管理对象
	 * @return int
	 */
	public int update(DsBaseFlowCategory entity)
	{
		return categoryDao.update(entity);
	}

	/**
	 * 更新移动
	 * @param ids 流程管理主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updatePid(Long[] ids, long targetId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				categoryDao.updatePid(ids[i], targetId, map);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 流程管理主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for(int i = 0; i < ids.length; i++)
		{
			categoryDao.updateSeq(ids[i], i + 1L);
		}
	}

	/**
	 * 查询单个流程管理对象
	 * @param primaryKey 流程管理主键
	 * @return DsBaseOrg
	 */
	public DsBaseFlowCategory get(Long primaryKey)
	{
		return (DsBaseFlowCategory) categoryDao.get(primaryKey);
	}

	/**
	 * 根据上级流程管理主键取得列表数据
	 * @param pid 上级流程管理主键
	 * @return List&lt;DsBaseOrg&gt;
	 */
	public List<DsBaseFlowCategory> queryList(Long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		return categoryDao.queryList(map);
	}

	/**
	 * 获得节点指定类型的子节点个数
	 * @param pid 上级流程管理主键
	 * @return int
	 */
	public int getCountByPid(long pid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		return categoryDao.queryCount(new PageRequest(map));
	}
}
