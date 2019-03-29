package dswork.bbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.bbs.model.DsBbsForum;
import dswork.bbs.model.DsBbsPage;
import dswork.bbs.model.DsBbsSite;
import dswork.bbs.service.DsBbsForumService;
import dswork.bbs.service.DsBbsPageService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;

@Controller
@RequestMapping("/bbs")
public class DsBbsController extends BaseController
{
	@Autowired
	private DsBbsPageService service;
	@Autowired
	private DsBbsForumService forumService;

	// 获得栏目列表
	@RequestMapping("/index")
	public String index()
	{
		try
		{
			Long id = req().getLong("siteid", 0);
			DsBbsSite s = service.getSite(id);
			put("site", s);
			put("list", queryForum(querySiteForum(id), 0).getList());
			return "/bbs/index.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	// 获得栏目列表
	@RequestMapping("/forum-{id}-{p}")
	public String forum(@PathVariable("id") Long id, @PathVariable("p") int page)
	{
		try
		{
			DsBbsForum m = service.getForum(id);
			DsBbsSite s = service.getSite(m.getSiteid());
			m = queryForum(querySiteForum(m.getSiteid()), id);
			put("site", s);
			put("forum", m);
			put("list", m.getList());
			PageRequest rq = getPageRequest();
			rq.getFilters().put("siteid", m.getSiteid());
			rq.getFilters().put("forumid", m.getId());
			Page<DsBbsPage> pageModel = service.queryPage(rq);
			put("pageModel", pageModel);
			put("pageNav", new PageNav<DsBbsPage>(request(), pageModel));
			return "/bbs/forum.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	private List<DsBbsForum> querySiteForum(long siteid)
	{
		Map<String, Object> mapFilters = req().getParameterValueMap(false, false);
		mapFilters.put("siteid", siteid);
		List<DsBbsForum> clist = forumService.queryList(mapFilters);
		Map<Long, DsBbsForum> map = new HashMap<Long, DsBbsForum>();
		for(DsBbsForum m : clist)
		{
			map.put(m.getId(), m);
		}
		for(DsBbsForum m : clist)
		{
			if(m.getPid() > 0 && m.getStatus() == 1)
			{
				try
				{
					DsBbsForum p = map.get(m.getPid());
					p.add(m);// 放入其余节点对应的父节点
					m.setParent(p);// 设置自己的父节点
				}
				catch(Exception ex)
				{
					ex.printStackTrace();// 找不到对应的父栏目
				}
			}
		}
		return clist;
	}

	private DsBbsForum queryForum(List<DsBbsForum> clist, long pid)
	{
		DsBbsForum t = new DsBbsForum();
		if(pid == 0)
		{
			t.setId(0L);
			for(DsBbsForum m : clist)
			{
				if(m.getPid() == 0 && m.getStatus() == 1)
				{
					t.add(m);// 只把根节点放入list
				}
			}
			return t;
		}
		else
		{
			for(DsBbsForum m : clist)
			{
				if(m.getId() == pid && m.getStatus() == 1)
				{
					return m;
				}
			}
		}
		return t;
	}
}
