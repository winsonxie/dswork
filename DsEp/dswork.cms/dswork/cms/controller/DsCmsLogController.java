/**
 * 功能:日志Controller
 * 开发人员:张胤
 * 创建时间:2017/10/12 17:45:58
 */
package dswork.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsLog;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsLogService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/cms/log")
public class DsCmsLogController extends BaseController
{
	@Autowired
	private DsCmsLogService service;

	// 获得分页
	@RequestMapping("/getLog")
	public String getLog()
	{
		try
		{
			Long id = req.getLong("siteid", -1), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
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
				List<DsCmsCategory> list = service.queryListCategory(siteid);
				put("list", DsCmsUtil.categorySetting(list));
			}
			put("siteid", siteid);
			Page<DsCmsLog> pageModel = service.queryPage(getPageRequest());
			put("pageModel", pageModel);
			put("pageNav", new PageNav<DsCmsLog>(request, pageModel));
			return "/cms/log/getLog.jsp";
		}
		catch(Exception e)
		{
			return null;
		}
	}

	private String getOwn()
	{
		return common.auth.AuthUtil.getLoginUser(request).getOwn();
	}
}
