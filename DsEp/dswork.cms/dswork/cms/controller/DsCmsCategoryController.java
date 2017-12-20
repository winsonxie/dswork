package dswork.cms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsCategoryService;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/cms/category")
public class DsCmsCategoryController extends BaseController
{
	@Autowired
	private DsCmsCategoryService service;

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/html") + "/";
	}

	private List<String> getTemplateName(String sitename)
	{
		List<String> list = new ArrayList<String>();
		try
		{
			File file = new File(getCmsRoot() + sitename + "/templates");
			for(File f : file.listFiles())
			{
				if(f.isFile() && !f.isHidden() && f.getPath().endsWith(".jsp"))
				{
					list.add(f.getName());
				}
			}
		}
		catch(Exception ex)
		{
		}
		return list;
	}

	// 添加
	@RequestMapping("/addCategory1")
	public String addCategory1()
	{
		try
		{
			Long siteid = req.getLong("siteid");
			DsCmsSite site = service.getSite(siteid);
			put("templates", getTemplateName(site.getFolder()));
			put("list", queryCategory(siteid, false, 0));
			return "/cms/category/addCategory.jsp";
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/addCategory2")
	public void addCategory2(DsCmsCategory po)
	{
		try
		{
			if(po.getSiteid() >= 0)
			{
				DsCmsSite s = service.getSite(po.getSiteid());
				if(checkOwn(s.getOwn()))
				{
					if(po.getScope() != 2)
					{
						po.setUrl("/a/" + po.getFolder() + "/index.html");
					}
					po.setStatus(0);// 没有新增状态，直接就是修改状态
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
	@RequestMapping("/delCategory")
	public void delCategory()
	{
		try
		{
			Long siteid = req.getLong("siteid");
			Long mid = req.getLong("keyIndex", 0);
			DsCmsCategory po = service.get(mid);
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
	@RequestMapping("/updCategory1")
	public String updCategory1()
	{
		try
		{
			Long siteid = req.getLong("siteid");
			Long id = req.getLong("keyIndex");
			DsCmsCategory po = service.get(id);
			if(siteid == po.getSiteid() && checkOwn(po.getSiteid()))
			{
				put("po", po);
				put("list", queryCategory(po.getSiteid(), false, id));
				DsCmsSite site = service.getSite(siteid);
				put("templates", getTemplateName(site.getFolder()));
				return "/cms/category/updCategory.jsp";
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/updCategory2")
	public void updCategory2(DsCmsCategory po)
	{
		try
		{
			DsCmsCategory m = service.get(po.getId());
			DsCmsSite s = service.getSite(m.getSiteid());
			if(m.getSiteid() == s.getId() && checkOwn(s.getOwn()))
			{
				if(m.getScope() == 0 && !po.getFolder().equals(m.getFolder()))// 列表栏目存在内容时，不可修改目录名称
				{
					if(service.getCountByCategoryid(m.getSiteid(), m.getId()) > 0)
					{
						print("0:存在内容时目录名称不可修改");
						return;
					}
				}
				if(m.getScope() != 2)
				{
					po.setUrl("/a/" + po.getFolder() + "/index.html");
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

	@RequestMapping("/updCategorySeq")
	public void updCategorySeq()
	{
		Long siteid = req.getLong("siteid");
		long[] idArr = req.getLongArray("keyIndex", 0);
		int[] seqArr = req.getIntArray("seq", 0);
		try
		{
			if(idArr.length == seqArr.length)
			{
				if(checkOwn(siteid))
				{
					service.updateSeq(idArr, seqArr, siteid);
					print(1);
					return;
				}
				print("0:站点不存在");
			}
			else
			{
				print("0:没有需要排序的节点");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得栏目列表
	@RequestMapping("/getCategory")
	public String getCategory()
	{
		try
		{
			Long id = req.getLong("siteid"), siteid = -1L;
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
							put("list", queryCategory(siteid, true, 0));
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
					put("list", queryCategory(siteid, true, 0));
				}
			}
			put("siteid", siteid);
			return "/cms/category/getCategory.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	/**
	 * 取出当前登录用户的栏目
	 * @param exclude 是否包括非List的栏目
	 * @param excludeId 需要清空指定id的子栏目
	 * @return List
	 */
	private List<DsCmsCategory> queryCategory(long siteid, boolean exclude, long excludeId)
	{
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("siteid", siteid);
		List<DsCmsCategory> clist = service.queryList(filters);
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			map.put(m.getId(), m);
		}
		List<DsCmsCategory> tlist = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			if(m.getId() != excludeId)
			{
				if(m.getPid() > 0)
				{
					try
					{
						if(m.getScope() == 0 || exclude)
						{
							map.get(m.getPid()).add(m);// 放入其余节点对应的父节点
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();// 找不到对应的父栏目
					}
				}
				else if(m.getPid() == 0)
				{
					if(m.getScope() == 0 || exclude)
					{
						tlist.add(m);// 只把根节点放入list
					}
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
		List<DsCmsCategory> list = DsCmsUtil.categorySettingList(tlist);
		return list;
	}

	private boolean checkOwn(Long siteid)
	{
		try
		{
			return service.getSite(siteid).getOwn().equals(getOwn());
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
		return common.auth.AuthUtil.getLoginUser(request).getOwn();
	}
}
