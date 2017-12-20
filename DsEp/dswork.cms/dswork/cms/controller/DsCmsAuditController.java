package dswork.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsAuditCategory;
import dswork.cms.model.DsCmsAuditPage;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPermission;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsAuditService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.TimeUtil;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/cms/audit")
public class DsCmsAuditController extends BaseController
{
	@Autowired
	private DsCmsAuditService service;

	@RequestMapping("getCategoryTree")
	public String getCategoryTree()
	{
		try
		{
			Long id = req.getLong("siteid", -1), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
			map.put("account", getAccount());
			List<DsCmsSite> siteList = service.queryListSite(map);
			if(siteList != null && siteList.size() > 0)
			{
				put("siteList", siteList);
				if(id >= 0)
				{
					for(DsCmsSite m : siteList)
					{
						if(m.getId() == id)
						{
							siteid = m.getId();
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
				}
			}
			if(siteid >= 0)
			{
				DsCmsPermission permission = service.getPermission(siteid, getAccount());
				List<DsCmsCategory> categoryList = new ArrayList<DsCmsCategory>();
				if(permission != null)
				{
					List<DsCmsCategory> _categoryList = service.queryListCategory(siteid);
					categoryList = DsCmsUtil.categoryAccess(_categoryList, permission.getAudit());
				}
				put("siteList", siteList);
				put("categoryList", categoryList);
			}
			put("siteid", siteid);
			return "/cms/audit/getCategoryTree.jsp";
		}
		catch(Exception e)
		{
			return null;
		}
	}

	// 修改
	@RequestMapping("/auditCategory1")
	public String updCategory1()
	{
		try
		{
			long id = req.getLong("id");
			DsCmsAuditCategory po = service.getAuditCategory(id);
			if(po == null)
			{
				po = service.saveAuditCategory(id);
			}
			if(checkOwn(po.getSiteid()))
			{
				if(checkAudit(po.getSiteid(), po.getId()))
				{
					DsCmsCategory m = service.getCategory(po.getId());
					put("scope", m.getScope());
					put("po", po);
					return "/cms/audit/auditCategory.jsp";
				}
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/auditCategory2")
	public void updCategory2(DsCmsAuditCategory po)
	{
		try
		{
			DsCmsCategory m = service.getCategory(po.getId());
			DsCmsSite s = service.getSite(m.getSiteid());
			if(m.getScope() != 0 && checkOwn(s.getOwn()))
			{
				if(checkAudit(s.getId(), m.getId()))
				{
					DsCmsAuditCategory _po = service.getAuditCategory(po.getId());
					if(_po.isAudit())
					{
						String action = req.getString("action");
						if("pass".equals(action))
						{
							_po.setAuditstatus(4);
						}
						else if("nopass".equals(action))
						{
							_po.setAuditstatus(2);
						}
						else
						{
							print("0:参数错误");
							return;
						}
						_po.setMsg(po.getMsg());
						_po.setAuditid(getAccount());
						_po.setAuditname(getName());
						_po.setAudittime(TimeUtil.getCurrentTime());
						service.updateAuditCategory(_po, m, s.isEnablelog());
					}
					print(1);
					return;
				}
			}
			print("0:站点不存在");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getPage")
	public String getPage()
	{
		try
		{
			Long categoryid = req.getLong("id");
			DsCmsCategory m = service.getCategory(categoryid);
			DsCmsSite s = service.getSite(m.getSiteid());
			if(checkOwn(s.getOwn()))
			{
				if(checkAudit(s.getId(), m.getId()))
				{
					if(m.getScope() == 0 && checkOwn(m.getSiteid()))// 列表
					{
						PageRequest pr = getPageRequest();
						pr.getFilters().remove("id");
						pr.getFilters().put("siteid", m.getSiteid());
						pr.getFilters().put("categoryid", m.getId());
						pr.getFilters().put("auditstatus", DsCmsAuditPage.AUDIT);
						Page<DsCmsAuditPage> pageModel = service.queryPageAuditPage(pr);
						put("pageModel", pageModel);
						put("pageNav", new PageNav<DsCmsAuditPage>(request, pageModel));
						put("po", m);
						return "/cms/audit/getPage.jsp";
					}
				}
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 审核
	@RequestMapping("/auditPage1")
	public String auditPage1()
	{
		try
		{
			Long id = req.getLong("keyIndex");
			DsCmsAuditPage po = service.getAuditPage(id);
			DsCmsSite s = service.getSite(po.getSiteid());
			if(checkOwn(s.getOwn()))
			{
				if(checkAudit(s.getId(), po.getCategoryid()))
				{
					put("po", po);
					put("page", req.getInt("page", 1));
					return "/cms/audit/auditPage.jsp";
				}
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/auditPage2")
	public void auditPage2(DsCmsAuditPage po)
	{
		try
		{
			DsCmsAuditPage _po = service.getAuditPage(po.getId());
			DsCmsSite s = service.getSite(_po.getSiteid());
			if(checkOwn(s.getOwn()))
			{
				if(checkAudit(s.getId(), _po.getCategoryid()))
				{
					if(_po.isAudit())
					{
						String action = req.getString("action");
						if("pass".equals(action))
						{
							_po.setAuditstatus(4);
						}
						else if("nopass".equals(action))
						{
							_po.setAuditstatus(2);
						}
						else
						{
							print("0:参数错误");
							return;
						}
						_po.setMsg(po.getMsg());
						_po.setAuditid(getAccount());
						_po.setAuditname(getName());
						_po.setAudittime(TimeUtil.getCurrentTime());
						if(_po.getStatus() == -1)
						{
							service.deleteAuditPage(_po, s.isEnablelog());
						}
						else
						{
							service.updateAuditPage(_po, s.isEnablelog());
						}
					}
					print(1);
					return;
				}
			}
			print("0:站点不存在");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	private boolean checkOwn(Long siteid)
	{
		try
		{
			return service.getSite(siteid).getOwn().equals(getOwn());
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private boolean checkOwn(String own)
	{
		try
		{
			return own.equals(getOwn());
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private boolean checkAudit(long siteid, long categoryid)
	{
		try
		{
			return service.getPermission(siteid, getAccount()).checkAudit(categoryid);
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private String getOwn()
	{
		return common.auth.AuthUtil.getLoginUser(request).getOwn();
	}

	private String getAccount()
	{
		return common.auth.AuthUtil.getLoginUser(request).getAccount();
	}

	private String getName()
	{
		return common.auth.AuthUtil.getLoginUser(request).getName();
	}
}
