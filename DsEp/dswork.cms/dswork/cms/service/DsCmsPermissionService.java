/**
 * 用户Service
 */
package dswork.cms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.cms.controller.DsCmsBaseController;
import dswork.cms.dao.DsCmsCategoryDao;
import dswork.cms.dao.DsCmsCategoryEditDao;
import dswork.cms.dao.DsCmsPageEditDao;
import dswork.cms.dao.DsCmsPermissionDao;
import dswork.cms.dao.DsCmsSiteDao;
import dswork.cms.dao.DsCmsUserDao;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsCategoryEdit;
import dswork.cms.model.DsCmsPageEdit;
import dswork.cms.model.DsCmsPermission;
import dswork.cms.model.DsCmsSite;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("all")
public class DsCmsPermissionService
{
	@Autowired
	private DsCmsPermissionDao dao;
	@Autowired
	private DsCmsSiteDao siteDao;
	@Autowired
	private DsCmsCategoryDao categoryDao;
	@Autowired
	private DsCmsUserDao userDao;
	@Autowired
	private DsCmsPageEditDao pageEditDao;
	@Autowired
	private DsCmsCategoryEditDao categoryEditDao;

	public int save(DsCmsPermission po)
	{
		if(
			po.getAudit().length() < 2 &&
			po.getEditall().length() < 2 &&
			po.getEditown().length() < 2 &&
			po.getPublish().length() < 2
		)// 没有任何权限，直接删除
		{
			dao.delete(po.getSiteid(), po.getAccount());
		}
		else if(dao.get(po.getSiteid(), po.getAccount()) != null)
		{
			dao.update(po);
		}
		else
		{
			dao.save(po);
		}
		DsCmsBaseController.refresh();
		return 1;
	}

	public DsCmsPermission get(Long siteid, String account)
	{
		return dao.get(siteid, account);
	}

	public List<DsCmsSite> queryListSite(Map<String, Object> map)
	{
		return siteDao.queryList(map);
	}

	public List<DsCmsCategory> queryListCategory(Long siteid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteid", siteid);
		map.put("publishstatus", "true");// 过滤移除的栏目
		return categoryDao.queryList(map);
	}

	public Page<Map<String, Object>> queryPageUser(PageRequest pr)
	{
		return userDao.queryPage(pr);
	}

	public List<DsCmsPermission> queryListPermission(long siteid)
	{
		return dao.queryList(siteid);
	}

	public DsCmsCategory getCategory(long id)
	{
		return (DsCmsCategory) categoryDao.get(id);
	}

	public int queryCountPageEdit(Map<String, Object> map)
	{
		return pageEditDao.queryCount(map);
	}

	public DsCmsCategoryEdit getCategoryEdit(Long id)
	{
		return (DsCmsCategoryEdit) categoryEditDao.get(id);
	}
}
