/**
 * 用户类型Service
 */
package dswork.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.common.model.DsCommonUsertype;
import dswork.common.dao.DsCommonUsertypeDao;

@Service
@SuppressWarnings("all")
public class DsCommonUsertypeService extends BaseService<DsCommonUsertype, Long>
{
	@Autowired
	private DsCommonUsertypeDao userTypeDao;

	@Override
	protected EntityDao getEntityDao()
	{
		return userTypeDao;
	}

	/**
	 * 更新排序
	 * @param ids 用户类型主键数组
	 */
	public void updateSeq(Long[] ids)
	{
		for(int i = 0; i < ids.length; i++)
		{
			userTypeDao.updateSeq(ids[i], i + 1L);
		}
	}

	/**
	 * 判断用户类型是否存在
	 * @param alias - 用户输入的标识字符串。
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isExistsByAlias(String alias)
	{
		return userTypeDao.isExistsByAlias(alias);
	}

	/**
	 * 根据用户输入的标识来获得对象，有则返回用户类型对象，无则返回null。
	 * @param alias - 用户输入的标识字符串。
	 * @return DsCommonUsertype - 用户类型对象。
	 */
	public DsCommonUsertype getByAlias(String alias)
	{
		return userTypeDao.getByAlias(alias);
	}
}
