/**
 * 采编审核Service
 */
package dswork.cms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.cms.dao.DsCmsCategoryEditDao;
import dswork.cms.dao.DsCmsPageEditDao;
import dswork.cms.dao.DsCmsCategoryDao;
import dswork.cms.dao.DsCmsLogDao;
import dswork.cms.dao.DsCmsPageDao;
import dswork.cms.dao.DsCmsSiteDao;
import dswork.cms.model.DsCmsCategoryEdit;
import dswork.cms.model.DsCmsPageEdit;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPage;
import dswork.cms.model.DsCmsSite;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;

@Service
@SuppressWarnings("unchecked")
public class DsCmsEditService
{
	@Autowired
	private DsCmsPageEditDao pageEditDao;
	@Autowired
	private DsCmsCategoryEditDao categoryEditDao;
	@Autowired
	private DsCmsLogDao logDao;
	@Autowired
	private DsCmsCategoryDao categoryDao;
	@Autowired
	private DsCmsSiteDao siteDao;
	@Autowired
	private DsCmsPageDao pageDao;

	public void savePageEdit(DsCmsPageEdit po, boolean writePage, boolean enablelog, String editid, String editname)
	{
		pageEditDao.save(po);
		if(po.getScope() != 2) // 不为外链
		{
			po.setUrl("/a/" + po.getCategoryid() + "/" + po.getId() + ".html");
		}
		if(writePage)
		{
			DsCmsPage p = new DsCmsPage();
			p.setId(po.getId());
			p.setSiteid(po.getSiteid());
			p.setCategoryid(po.getCategoryid());
			p.setStatus(po.getStatus());
			p.setTitle(po.getTitle());
			p.setMetakeywords(po.getMetakeywords());
			p.setMetadescription(po.getMetadescription());
			p.setSummary(po.getSummary());
			p.setContent(po.getContent());
			p.setReleasetime(po.getReleasetime());
			p.setReleasesource(po.getReleasesource());
			p.setReleaseuser(po.getReleaseuser());
			p.setImg(po.getImg());
			p.setImgtop(po.getImgtop());
			p.setPagetop(po.getPagetop());
			p.setScope(po.getScope());
			p.setStatus(0);// page设置为新建未发布状态
			p.setUrl(po.getUrl());
			p.setJsondata(po.getJsondata());
			pageDao.save(p);
		}
		pageEditDao.update(po);
		if((po.isAudit() || po.isPass()) && enablelog)// 只记录提交时的日志
		{
			logDao.saveForEditPage(po, 0, editid, editname);
		}
	}

	public int deletePageEdit(Long id)
	{
		pageEditDao.delete(id);
		pageDao.updateStatus(id, -1);
		return 1;
	}

	public void updateCategoryEdit(DsCmsCategoryEdit po, boolean writeCategory, boolean enablelog, String editid, String editname)
	{
		if(writeCategory)
		{
			DsCmsCategory c = (DsCmsCategory) categoryDao.get(po.getId());
			c.setSummary(po.getSummary());
			c.setReleasesource(po.getReleasesource());
			c.setReleaseuser(po.getReleasetime());
			c.setImg(po.getImg());
			if(c.getScope() == 0 || c.getScope() == 1)
			{
				c.setMetakeywords(po.getMetakeywords());
				c.setMetadescription(po.getMetadescription());
				c.setContent(po.getContent());
				c.setReleasetime(po.getReleasetime());
				if(c.getStatus() != 0)
				{
					c.setStatus(1);
				}
				c.setJsondata(po.getJsondata());
				categoryDao.updateContent(c);
			}
			else if(c.getScope() == 2)
			{
				c.setUrl(po.getUrl());
				c.setStatus(8);// Category设置为已发布状态（因为外链不需要发布操作）
				categoryDao.updateURL(c);
			}
		}
		categoryEditDao.update(po);
		if((po.isAudit() || po.isPass()) && enablelog)// 只记录提交时的日志
		{
			logDao.saveForEditCategory(po, 1, editid, editname);
		}
	}

	public void updateRevokeCategoryEdit(DsCmsCategoryEdit po, boolean enablelog, String editid, String editname)
	{
		if(po.getStatus() == -1)
		{
			po.setStatus(1); // 修改
		}
		categoryEditDao.update(po);
		if(enablelog)
		{
			logDao.saveForEditCategory(po, 3, editid, editname);
		}
	}

	public void updatePageEdit(DsCmsPageEdit po, boolean writePage, boolean enablelog, String editid, String editname)
	{
		if(po.getScope() != 2)
		{
			po.setUrl("/a/" + po.getCategoryid() + "/" + po.getId() + ".html");
		}
		if(writePage)
		{
			DsCmsPage p = (DsCmsPage) pageDao.get(po.getId());
			boolean save = false;
			if(p == null)
			{
				p = new DsCmsPage();
				p.setId(po.getId());
				p.setSiteid(po.getSiteid());
				p.setCategoryid(po.getCategoryid());
				save = true;
			}
			p.setStatus(po.getStatus());
			p.setTitle(po.getTitle());
			p.setMetakeywords(po.getMetakeywords());
			p.setMetadescription(po.getMetadescription());
			p.setSummary(po.getSummary());
			p.setContent(po.getContent());
			p.setReleasetime(po.getReleasetime());
			p.setReleasesource(po.getReleasesource());
			p.setReleaseuser(po.getReleaseuser());
			p.setImg(po.getImg());
			p.setImgtop(po.getImgtop());
			p.setPagetop(po.getPagetop());
			p.setScope(po.getScope());
			p.setUrl(po.getUrl());
			p.setJsondata(po.getJsondata());
			if(save)
			{
				p.setStatus(0);// page设置为新建未发布状态
				pageDao.save(p);
			}
			else
			{
				if(p.getStatus() != 0)
				{
					p.setStatus(1);// page设置为更新未发布状态
				}
				pageDao.update(p);
			}
		}
		pageEditDao.update(po);
		if((po.isAudit() || po.isPass()) && enablelog)// 只记录提交时的日志
		{
			logDao.saveForEditPage(po, po.getStatus() == -1 ? 2 : 1, editid, editname);
		}
	}

	public void updateRevokePageEdit(DsCmsPageEdit po, boolean enablelog, String editid, String editname)
	{
		if(po.getStatus() == -1)
		{
			po.setStatus(1); // 删除撤销为修改
		}
		pageEditDao.update(po);
		if(enablelog)
		{
			logDao.saveForEditPage(po, 3, editid, editname);
		}
	}

	public DsCmsSite getSite(Long siteid)
	{
		return (DsCmsSite) siteDao.get(siteid);
	}

	public DsCmsCategory getCategory(Long categoryid)
	{
		return (DsCmsCategory) categoryDao.get(categoryid);
	}

	public DsCmsCategoryEdit saveCategoryEdit(Long id)
	{
		DsCmsCategoryEdit po = new DsCmsCategoryEdit();
		DsCmsCategory c = (DsCmsCategory) categoryDao.get(id);
		po.setId(c.getId());
		po.setSiteid(c.getSiteid());
		po.setSummary(c.getSummary());
		po.setMetakeywords(c.getMetakeywords());
		po.setMetadescription(c.getMetadescription());
		po.setReleasesource(c.getReleasesource());
		po.setReleaseuser(c.getReleaseuser());
		po.setImg(c.getImg());
		po.setContent(c.getContent());
		po.setReleasetime(c.getReleasetime());
		po.setUrl(c.getUrl());
		po.setAuditstatus(DsCmsCategoryEdit.EDIT);// 编辑状态
		po.setStatus(0);// 初始设置为新增状态
		categoryEditDao.save(po);
		return po;
	}

	public DsCmsCategoryEdit getCategoryEdit(Long id)
	{
		return (DsCmsCategoryEdit) categoryEditDao.get(id);
	}

	public DsCmsPageEdit getPageEdit(Long id)
	{
		return (DsCmsPageEdit) pageEditDao.get(id);
	}

	public DsCmsPage getPage(Long id)
	{
		return (DsCmsPage) pageDao.get(id);
	}

	public List<DsCmsSite> queryListSite(String own)
	{
		return siteDao.queryList(own);
	}

	public Page<DsCmsPageEdit> queryPagePageEdit(PageRequest pr)
	{
		return pageEditDao.queryPage(pr);
	}

	public List<DsCmsCategory> queryListCategory(long siteid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteid", siteid);
		map.put("publishstatus", "true");
		return categoryDao.queryList(map);
	}
}
