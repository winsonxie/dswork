/**
 * 字典Service
 */
package dswork.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.base.dao.DsBaseDictDao;
import dswork.base.dao.DsBaseDictDataDao;
import dswork.base.model.DsBaseDict;
import dswork.base.model.DsBaseDictData;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsBaseDictService
{
	@Autowired
	private DsBaseDictDao DsBaseDictDao;// 字典分类dao
	@Autowired
	private DsBaseDictDataDao DsBaseDictDataDao;// 字典dao

	// private static String getSecureString(Object obj)
	// {
	// return String.valueOf(obj).replace('&', ' ').replace('-', ' ').replace('*', ' ').replace('%', ' ').replace("'",
	// "''");
	// }
	/**
	 * 新增对象
	 * @param model 字典分类对象
	 * @return int
	 */
	public int save(DsBaseDict model)
	{
		return DsBaseDictDao.save(model);
	}

	/**
	 * 删除字典分类，并不实际删除对应的字典项
	 * @param primaryKey 字典分类主键
	 * @return int
	 */
	public int delete(long primaryKey)
	{
		return DsBaseDictDao.delete(primaryKey);
	}

	/**
	 * 批量删除字典分类，并不实际删除对应的字典项
	 * @param primaryKeys 字典分类主键数组
	 */
	public void deleteBatch(Long[] primaryKeys)
	{
		if(primaryKeys != null && primaryKeys.length > 0)
		{
			for(long p : primaryKeys)
			{
				delete(p);
			}
		}
	}

	/**
	 * 更新对象
	 * @param model 字典分类对象
	 * @param updateDataName true更新字典项，false不更新
	 * @return int
	 */
	public int update(DsBaseDict model, boolean updateDataName)
	{
		if(updateDataName)
		{
			DsBaseDict n = (DsBaseDict) DsBaseDictDao.get(model.getId());
			if(!n.getName().equals(model.getName()))
			{
				DsBaseDictDataDao.updateName(model.getName(), n.getName());
			}
		}
		DsBaseDictDao.update(model);
		return 1;
	}

	/**
	 * 查询单个字典分类对象
	 * @param primaryKey 字典分类主键
	 * @return DsBaseDict
	 */
	public DsBaseDict get(long primaryKey)
	{
		return (DsBaseDict) DsBaseDictDao.get(primaryKey);
	}

	/**
	 * 默认分页方法
	 * @param pageRequest 条件类
	 * @return Page&lt;DsBaseDict&gt;
	 */
	public Page<DsBaseDict> queryPage(PageRequest pageRequest)
	{
		return DsBaseDictDao.queryPage(pageRequest);
	}

	/**
	 * 获取同名的字典分类个数(当前分类除外)
	 * @param id 排除的id
	 * @param name 字典类名
	 * @return int
	 */
	public int getCountByName(long id, String name)
	{
		return DsBaseDictDao.getCountByName(id, name);
	}

	/**
	 * 新增
	 * @param model 字典项对象
	 * @return int
	 */
	public int saveData(DsBaseDictData model)
	{
		return DsBaseDictDataDao.save(model);
	}

	/**
	 * 删除字典项，并且如果父节点下面再没有子节点，则更改父节点为叶子节点
	 * @param id 字典编码
	 * @param name 字典名
	 * @return int
	 */
	public int deleteData(String pid, String id, String name)
	{
		DsBaseDictDataDao.delete(id, name);
		int count = DsBaseDictDataDao.getCount(pid, name);
		if(count == 0)
		{
			// 更改父节点为叶子节点
			DsBaseDictDataDao.updateStatus(pid, name, 0);
		}
		return 1;
	}

	/**
	 * 更新对象
	 * @param model 字典项对象
	 * @return int
	 */
	public int updateData(DsBaseDictData model)
	{
		return DsBaseDictDataDao.update(model);
	}

	/**
	 * 更新移动
	 * @param ids 字典项主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updateDataPid(String[] ids, String targetId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			if(!ids[i].isEmpty())
			{
				DsBaseDictDataDao.updatePid(ids[i], targetId, map);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 字典项主键数组
	 */
	public void updateDataSeq(String[] ids, String name)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			DsBaseDictDataDao.updateSeq(ids[i], name, i + 1, map);
		}
	}

	/**
	 * 查询字典项对象
	 * @param primaryKey 字典项主键
	 * @return DsDictData
	 */
	public DsBaseDictData getData(String id, String name)
	{
		return (DsBaseDictData) DsBaseDictDataDao.get(id, name);
	}

	/**
	 * 获取节点字典项个数
	 * @param pid 上级字典项主键
	 * @param name 字典分类名，为null时需保证pid大于0，否则返回全部
	 * @return int
	 */
	public int getDataCount(String pid, String name)
	{
		return DsBaseDictDataDao.getCount(pid, name);
	}

	/**
	 * 获取指定节点的列表数据，当pid为null时，获取全部数据，当pid小于等于0时，获取根节点数据
	 * @param pid 上级字典项主键
	 * @param name 字典分类名
	 * @param map 查询条件
	 * @return List&lt;DsBaseDictData&gt;
	 */
	public List<DsBaseDictData> queryListData(String pid, String name, Map<String, Object> map)
	{
		return DsBaseDictDataDao.queryList(pid, name, map);
	}

	/**
	 * 更新节点的状态(1树叉,0树叶)
	 * @param id 字典项编号
	 * @param name 字典引用名
	 * @param status 状态(1树叉,0树叶)
	 * @return int
	 */
	public int updateDataStatus(String id, String name, int status)
	{
		return DsBaseDictDataDao.updateStatus(id, name, status);
	}

	/**
	 * 根据名称获取字典
	 * @param name 字典引用名
	 * @return DsBaseDict
	 */
	public DsBaseDict getByName(String name)
	{
		return DsBaseDictDao.getByName(name);
	}
}
