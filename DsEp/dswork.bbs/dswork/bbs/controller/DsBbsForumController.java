package dswork.bbs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.bbs.model.DsBbsForum;
import dswork.bbs.model.DsBbsSite;
import dswork.bbs.service.DsBbsForumService;
import dswork.core.page.PageRequest;

@Controller
@RequestMapping("/bbs/admin/forum")
public class DsBbsForumController extends BaseController
{
	@Autowired
	private DsBbsForumService service;

	// 添加
	@RequestMapping("/addForum1")
	public String addForum1()
	{
		Long siteid = req().getLong("siteid");
		put("list", queryForum(siteid, 0));
		return "/bbs/admin/forum/addForum.jsp";
	}

	@RequestMapping("/addForum2")
	public void addForum2(DsBbsForum po)
	{
		try
		{
			if(po.getSiteid() >= 0)
			{
				DsBbsSite s = service.getSite(po.getSiteid());
				if(checkOwn(s.getOwn()))
				{
					service.save(po);
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

	// 删除
	@RequestMapping("/delForum")
	public void delForum()
	{
		try
		{
			Long siteid = req().getLong("siteid");
			Long mid = req().getLong("keyIndex", 0);
			DsBbsForum po = service.get(mid);
			if(siteid == po.getSiteid())
			{
				int count = service.getCountByPid(mid);
				if(0 < count)
				{
					print("0:下级节点不为空");
					return;
				}
				if(checkOwn(po.getSiteid()))
				{
					service.delete(mid);
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

	// 修改
	@RequestMapping("/updForum1")
	public String updForum1()
	{
		Long siteid = req().getLong("siteid");
		Long id = req().getLong("keyIndex");
		DsBbsForum po = service.get(id);
		if(siteid == po.getSiteid())
		{
			put("po", po);
			put("list", queryForum(po.getSiteid(), id));
			return "/bbs/admin/forum/updForum.jsp";
		}
		else
		{
			return null;
		}
	}

	@RequestMapping("/updForum2")
	public void updForum2(DsBbsForum po)
	{
		try
		{
			DsBbsForum m = service.get(po.getId());
			DsBbsSite s = service.getSite(m.getSiteid());
			if(m.getSiteid() == s.getId() && checkOwn(s.getOwn()))
			{
				po.setSiteid(s.getId());
				if(m.getPid() == 0)
				{
					po.setPid(0L);
				}
				service.update(po);
				print(1);
				return;
			}
			print("0:站点不存在");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/updForumBatch")
	public void updForumBatch()
	{
		Long siteid = req().getLong("siteid");
		long[] idArr = req().getLongArray("keyIndex", 0);
		String[] nameArr = req().getStringArray("name", false);
		int[] seqArr = req().getIntArray("seq", 0);
		try
		{
			if(checkOwn(siteid))
			{
				service.updateBatch(idArr, nameArr, seqArr, siteid);
				print(1);
				return;
			}
			print("0:站点不存在");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得栏目列表
	@RequestMapping("/getForum")
	public String getForum()
	{
		try
		{
			Long id = req().getLong("siteid", -1), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
			PageRequest rq = new PageRequest(map);
			List<DsBbsSite> siteList = service.queryListSite(rq.getFilters());
			if(siteList != null && siteList.size() > 0)
			{
				put("siteList", siteList);
				if(id >= 0)
				{
					for(DsBbsSite m : siteList)
					{
						if(m.getId().longValue() == id)
						{
							siteid = m.getId();
							put("list", queryForum(siteid, 0));
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
					put("list", queryForum(siteid, 0));
				}
			}
			put("siteid", siteid);
			return "/bbs/admin/forum/getForum.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	private List<DsBbsForum> queryForum(long siteid, long excludeId)
	{
		Map<String, Object> mapFilters = req().getParameterValueMap(false, false);
		mapFilters.put("siteid", siteid);
		List<DsBbsForum> clist = service.queryList(mapFilters);
		Map<Long, DsBbsForum> map = new HashMap<Long, DsBbsForum>();
		for(DsBbsForum m : clist)
		{
			map.put(m.getId(), m);
		}
		List<DsBbsForum> tlist = new ArrayList<DsBbsForum>();
		for(DsBbsForum m : clist)
		{
			if(m.getId() != excludeId)
			{
				if(m.getPid() > 0 && m.getStatus() == 1)
				{
					try
					{
						map.get(m.getPid()).add(m);// 放入其余节点对应的父节点
					}
					catch(Exception ex)
					{
						ex.printStackTrace();// 找不到对应的父栏目
					}
				}
				else if(m.getPid() == 0)
				{
					tlist.add(m);// 只把根节点放入list
				}
			}
		}
		if(excludeId > 0)
		{
			try
			{
				map.get(excludeId).clearList();
			}
			catch(Exception e)
			{
				e.printStackTrace();// 找不到对应的栏目
			}
		}
		List<DsBbsForum> list = new ArrayList<DsBbsForum>();// 按顺序放入
		for(int i = 0; i < tlist.size(); i++)
		{
			DsBbsForum m = tlist.get(i);
			m.setLevel(0);
			m.setLabel("");
			list.add(m);
			settingList(m, list);
		}
		return list;
	}

	private void settingList(DsBbsForum m, List<DsBbsForum> list)
	{
		int size = m.getList().size();
		for(int i = 0; i < size; i++)
		{
			DsBbsForum n = m.getList().get(i);
			n.setLevel(m.getLevel() + 1);
			n.setLabel(m.getLabel());
			if(m.getLabel().endsWith("├"))
			{
				n.setLabel(m.getLabel().substring(0, m.getLabel().length() - 2) + "│");
			}
			else if(m.getLabel().endsWith("└"))
			{
				n.setLabel(m.getLabel().substring(0, m.getLabel().length() - 2) + "　");
			}
			n.setLabel(n.getLabel() + "&nbsp;&nbsp;");
			n.setLabel(n.getLabel() + (i == size - 1 ? "└" : "├"));
			list.add(n);
			settingList(n, list);
		}
	}

	private boolean checkOwn(Long siteid)
	{
		try
		{
			return checkOwn(service.getSite(siteid).getOwn());
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	private boolean checkOwn(String own)
	{
		try
		{
			return own.equals(getOwn());
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	private String getOwn()
	{
		return common.authown.AuthOwnUtil.getUser(request()).getOwn();
	}
}
