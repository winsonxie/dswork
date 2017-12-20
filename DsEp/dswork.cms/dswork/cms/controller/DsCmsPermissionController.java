package dswork.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.auth.Auth;
import common.auth.AuthUtil;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPermission;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsPermissionService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/cms/permission")
public class DsCmsPermissionController extends BaseController
{
	@Autowired
	private DsCmsPermissionService service;

	// 获取系统用户分页
	@RequestMapping("getCommonUser")
	public String getCommonUser()
	{
		try
		{
			Page<Map<String, Object>> pageModel = service.queryPageCommonUser(getPageRequest());
			put("pageModel", pageModel);
			put("pageNav", new PageNav<Map<String, Object>>(request, pageModel));
			return "/cms/permission/getCommonUser.jsp";
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 获取企业用户分页
	@RequestMapping("getEpUser")
	public String getEpUser()
	{
		try
		{
			Auth user = AuthUtil.getLoginUser(request);
			PageRequest pr = getPageRequest();
			pr.getFilters().put("qybm", user.getQybm());
			Page<Map<String, Object>> pageModel = service.queryPageEpUser(pr);
			put("pageModel", pageModel);
			put("pageNav", new PageNav<Map<String, Object>>(request, pageModel));
			return "/cms/permission/getEpUser.jsp";
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 获取个人用户分页
	@RequestMapping("getPersonUser")
	public String getPersonUser()
	{
		try
		{
			Page<Map<String, Object>> pageModel = service.queryPagePersonUser(getPageRequest());
			put("pageModel", pageModel);
			put("pageNav", new PageNav<Map<String, Object>>(request, pageModel));
			return "/cms/permission/getPersonUser.jsp";
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 用户权限
	@RequestMapping("/updPermission1")
	public String updPermission1()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			String account = req.getString("account", null);
			if(account == null)
			{
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("siteid", siteid);
			map.put("own", getOwn());
			List<DsCmsSite> siteList = service.queryListSite(map);
			List<DsCmsCategory> categoryList = null;
			if(siteList != null && siteList.size() > 0)
			{
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
				}
				categoryList = service.queryListCategory(siteid);
			}
			DsCmsPermission permission = service.get(siteid, account);
			if(permission == null)
			{
				permission = new DsCmsPermission();
			}
			put("permission", permission);
			put("siteid", siteid);
			put("siteList", siteList == null ? new ArrayList<DsCmsSite>() : siteList);
			put("categoryList", DsCmsUtil.categorySetting(categoryList == null ? new ArrayList<DsCmsCategory>() : categoryList));
			return "/cms/permission/updPermission.jsp";
		}
		catch(Exception ex)
		{
			return null;
		}
	}

	@RequestMapping("/updPermission2")
	public void updPermission2()
	{
		try
		{
			String account = req.getString("account", null);
			Long siteid = req.getLong("siteid", -1);
			if(account == null || siteid == -1)
			{
				throw new Exception("参数错误");
			}
			boolean isSave = false;
			DsCmsPermission permission = service.get(siteid, account);
			if(permission == null)
			{
				isSave = true;
				permission = new DsCmsPermission();
				permission.setId(UniqueId.genId());
				permission.setSiteid(siteid);
				permission.setAccount(account);
			}
			permission.setEditall(req.getString("editall", ""));
			permission.setEditown(req.getString("editown", ""));
			permission.setAudit(req.getString("audit", ""));
			permission.setPublish(req.getString("publish", ""));
			if(isSave)
			{
				service.save(permission);
			}
			else
			{
				service.update(permission);
			}
			print(1);
		}
		catch(Exception e)
		{
			print("0:" + e.getMessage());
		}
	}

	private String getOwn()
	{
		return AuthUtil.getLoginUser(request).getOwn();
	}
}
