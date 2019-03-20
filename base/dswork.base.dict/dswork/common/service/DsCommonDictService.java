/**
 * 字典Service
 */
package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonDictDao;
import dswork.common.dao.DsCommonDictDataDao;
import dswork.common.model.DsCommonDict;
import dswork.common.model.DsCommonDictData;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsCommonDictService
{
	@Autowired
	private DsCommonDictDao dsCommonDictDao;// 字典分类dao
	@Autowired
	private DsCommonDictDataDao dsCommonDictDataDao;// 字典dao
	
//	private static String getSecureString(Object obj)
//	{
//		return String.valueOf(obj).replace('&', ' ').replace('-', ' ').replace('*', ' ').replace('%', ' ').replace("'", "''");
//	}

	/**
	 * 新增对象
	 * @param model 字典分类对象
	 * @return int
	 */
	public int save(DsCommonDict model)
	{
		return dsCommonDictDao.save(model);
	}

	/**
	 * 删除字典分类，并不实际删除对应的字典项
	 * @param primaryKey 字典分类主键
	 * @return int
	 */
	public int delete(long primaryKey)
	{
		return dsCommonDictDao.delete(primaryKey);
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
	public int update(DsCommonDict model, boolean updateDataName)
	{
		if(updateDataName)
		{
			DsCommonDict n = (DsCommonDict) dsCommonDictDao.get(model.getId());
			if(!n.getName().equals(model.getName()))
			{
				dsCommonDictDataDao.updateName(model.getName(), n.getName());
			}
		}
		dsCommonDictDao.update(model);
		return 1;
	}

	/**
	 * 查询单个字典分类对象
	 * @param primaryKey 字典分类主键
	 * @return DsCommonDict
	 */
	public DsCommonDict get(long primaryKey)
	{
		return (DsCommonDict) dsCommonDictDao.get(primaryKey);
	}

	/**
	 * 默认分页方法
	 * @param pageRequest 条件类
	 * @return Page&lt;DsCommonDict&gt;
	 */
	public Page<DsCommonDict> queryPage(PageRequest pageRequest)
	{
		return dsCommonDictDao.queryPage(pageRequest);
	}
	
	/**
	 * 获取同名的字典分类个数(当前分类除外)
	 * @param id 排除的id
	 * @param name 字典类名
	 * @return int
	 */
	public int getCountByName(long id, String name)
	{
		return dsCommonDictDao.getCountByName(id, name);
	}


	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 新增
	 * @param model 字典项对象
	 * @return int
	 */
	public int saveData(DsCommonDictData model)
	{
		return dsCommonDictDataDao.save(model);
	}

	/**
	 * 删除字典项
	 * @param primaryKey 字典项主键
	 * @return int
	 */
	public int deleteData(long primaryKey)
	{
		return dsCommonDictDataDao.delete(primaryKey);
	}

	/**
	 * 批量删除字典项
	 * @param primaryKeys 字典项主键数组
	 */
	public void deleteBatchData(Long[] primaryKeys)
	{
		if(primaryKeys != null && primaryKeys.length > 0)
		{
			for(long p : primaryKeys)
			{
				deleteData(p);
			}
		}
	}

	/**
	 * 更新对象
	 * @param model 字典项对象
	 * @return int
	 */
	public int updateData(DsCommonDictData model)
	{
		return dsCommonDictDataDao.update(model);
	}

	/**
	 * 更新移动
	 * @param ids 字典项主键数组
	 * @param targetId 目标节点，为0则是根节点
	 */
	public void updateDataPid(Long[] ids, long targetId)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			if(ids[i] > 0)
			{
				dsCommonDictDataDao.updatePid(ids[i], targetId, map);
			}
		}
	}

	/**
	 * 更新排序
	 * @param ids 字典项主键数组
	 */
	public void updateDataSeq(Long[] ids)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < ids.length; i++)
		{
			dsCommonDictDataDao.updateSeq(ids[i], i + 1, map);
		}
	}

	/**
	 * 查询字典项对象
	 * @param primaryKey 字典项主键
	 * @return DsDictData
	 */
	public DsCommonDictData getData(long primaryKey)
	{
		return (DsCommonDictData) dsCommonDictDataDao.get(primaryKey);
	}
	
	/**
	 * 获取节点字典项个数
	 * @param pid 上级字典项主键
	 * @param name 字典分类名，为null时需保证pid大于0，否则返回全部
	 * @return int
	 */
	public int getDataCount(Long pid, String name)
	{
		return dsCommonDictDataDao.getCount(pid, name);
	}
	
	/**
	 * 获取指定节点的列表数据，当pid为null时，获取全部数据，当pid小于等于0时，获取根节点数据
	 * @param pid 上级字典项主键
	 * @param name 字典分类名
	 * @param map 查询条件
	 * @return List&lt;DsCommonDictData&gt;
	 */
	public List<DsCommonDictData> queryListData(Long pid, String name, Map<String, Object> map)
	{
		return dsCommonDictDataDao.queryList(pid, name, map);
	}

//	/**
//	 * 取得字典项分页数据
//	 * @param currentPage 当前页码
//	 * @param pageSize 一页显示的条数
//	 * @param map 查询参数和条件数据
//	 * @return Page&lt;DsCommonDictData&gt;
//	 */
//	public Page<DsCommonDictData> queryPageData(int currentPage, int pageSize, Map map)
//	{
//		PageRequest pr = new PageRequest();
//		pr.setCurrentPage(currentPage);
//		pr.setPageSize(pageSize);
//		pr.setFilters(map);
//		return dsCommonDictDataDao.queryPage(pr);
//	}
	
	/**
	 * 获取相同标识的字典个数
	 * @param alias 字典标识值
	 * @param name 字典分类名
	 * @param id 排除的id
	 * @return int
	 */
	public int getCountDataByAlias(String alias, String name, long id)
	{
		return dsCommonDictDataDao.getCountByAlias(alias, name, id);
	}
}
