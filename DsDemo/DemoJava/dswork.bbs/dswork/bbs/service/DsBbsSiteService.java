/**
 * 站点Service
 */
package dswork.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.db.EntityDao;
import dswork.core.db.BaseService;
import dswork.bbs.model.DsBbsSite;
import dswork.bbs.dao.DsBbsSiteDao;

@Service
@SuppressWarnings("all")
public class DsBbsSiteService extends BaseService<DsBbsSite, Long>
{
	@Autowired
	private DsBbsSiteDao dao;

	@Override
	protected EntityDao getEntityDao()
	{
		return dao;
	}
}
