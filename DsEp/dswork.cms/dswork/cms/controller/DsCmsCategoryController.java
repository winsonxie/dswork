package dswork.cms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.cms.GsonUtil;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPageEdit;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsCategoryService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.FileUtil;

@Scope("prototype")
@Controller
@RequestMapping("/cms/category")
public class DsCmsCategoryController extends DsCmsBaseController
{
	@Autowired
	private DsCmsCategoryService service;

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
					for(DsCmsSite s : siteList)
					{
						if(s.getId() == id)
						{
							siteid = s.getId();
							put("enablemobile", s.getEnablemobile() == 1);
							put("list", queryCategory(siteid, true, 0));
							break;
						}
					}
				}
				if(siteid == -1)
				{
					DsCmsSite s = siteList.get(0);
					siteid = s.getId();
					put("enablemobile", s.getEnablemobile() == 1);
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

	// 获得栏目列表
	@RequestMapping("/getRecycledCategory")
	public String getRecycledCategory()
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
							put("list", queryRecycledCategory(siteid));
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
					put("list", queryRecycledCategory(siteid));
				}
			}
			put("siteid", siteid);
			return "/cms/category/getRecycledCategory.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	// 添加
	@RequestMapping("/addCategory1")
	public String addCategory1()
	{
		try
		{
			Long siteid = req.getLong("siteid");
			DsCmsSite s = service.getSite(siteid);
			put("enablemobile", s.getEnablemobile() == 1);
			put("templates", getTemplateName(s.getFolder(), false));
			put("mtemplates", getTemplateName(s.getFolder(), true));
			put("list", queryCategory(siteid, true, 0));
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
				if(checkOwn(s.getId()))
				{
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					Map<String, String> map = new HashMap<String, String>();
					String[] cnameArr = req.getStringArray("cname", false);
					String[] ctitleArr = req.getStringArray("ctitle", false);
					String[] cdatatypeArr = req.getStringArray("cdatatype", false);
					for(int i = 0; i < cnameArr.length; i++)
					{
						if(ctitleArr[i].length() > 0 && map.get(ctitleArr[i]) == null)
						{
							Map<String, String> m = new HashMap<String, String>();
							m.put("cname", cnameArr[i]);
							m.put("ctitle", ctitleArr[i]);
							m.put("cdatatype", cdatatypeArr[i]);
							map.put(ctitleArr[i], ctitleArr[i]);
							list.add(m);
						}
					}
					po.setJsontable(GsonUtil.toJson(list));
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

	// 删除到回收站
	@RequestMapping("/delCategory")
	public void delCategory()
	{
		try
		{
			long id = req.getLong("keyIndex", -1);
			DsCmsCategory po = service.get(id);
			DsCmsSite s = service.getSite(po.getSiteid());
			if(checkOwn(s.getId()))
			{
				List<DsCmsCategory> list = new ArrayList<DsCmsCategory>();
				Queue<DsCmsCategory> queue = new LinkedList<DsCmsCategory>();
				queue.offer(po);
				while(!queue.isEmpty())
				{
					DsCmsCategory c = queue.poll();
					List<DsCmsCategory> clist = service.queryListByPid(c.getId());
					for(DsCmsCategory e : clist)
					{
						queue.offer(e);
					}
					list.add(c);
				}
				service.updateRecycled(list);
				boolean enablemobile = s.getEnablemobile() == 1;
				for(DsCmsCategory c : list)
				{
					deleteCategoryFolder(s.getFolder(), c.getId() + "", enablemobile);
				}
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

	// 从回收站删除
	@RequestMapping("/delRecycledCategory")
	public void delRecycledCategory()
	{
		try
		{
			long[] ids = req.getLongArray("keyIndex", -1);
			if(ids.length == 0)
			{
				print("0:没有指定栏目");
				return;
			}
			List<DsCmsCategory> list = new ArrayList<DsCmsCategory>();
			String msg = "";
			for(long id : ids)
			{
				DsCmsCategory po = service.get(id);
				if(!checkOwn(po.getSiteid()))
				{
					msg += "栏目 [" + po.getId() + "] 站点不存在<br>";
					continue;
				}
				if(po.getStatus() != -1)
				{
					msg += "栏目 [" + po.getId() + "] 状态不为-1<br>";
				}
				if(po.getPid() != 0)
				{
					msg += "栏目 [" + po.getId() + "] Pid不为空<br>";
				}
				list.add(po);
			}
			if(msg.length() > 0)
			{
				print("0:" + msg);
				return;
			}
			service.deleteRecycled(list);
			print(1);
			return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 还原回收站栏目
	@RequestMapping("updRecycledCategory1")
	public String updRecycledCategory1()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			long id = req.getLong("keyIndex", -1);
			if(checkOwn(siteid))
			{
				put("id", id);
				put("siteid", siteid);
				put("list", queryCategory(siteid, true, 0));
				return "/cms/category/updRecycledCategory.jsp";
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 还原回收站栏目
	@RequestMapping("updRecycledCategory2")
	public void updRecycledCategory2()
	{
		try
		{
			long id = req.getLong("keyIndex", -1);
			DsCmsCategory po = service.get(id);
			if(checkOwn(po.getSiteid()))
			{
				po.setPid(req.getLong("pid"));
				service.updateRestore(po);
				print(1);
				return;
			}
			print("0:站点不存在");
		}
		catch(Exception e)
		{
			print("0:" + e.getMessage());
		}
	}

	// 回收站栏目明细
	@RequestMapping("getRecycledCategoryById")
	public String getRecycledCategoryById()
	{
		try
		{
			long id = req.getLong("keyIndex", -1);
			DsCmsCategory po = service.get(id);
			DsCmsSite s = service.getSite(po.getSiteid());
			if(checkOwn(s.getId()))
			{
				put("po", po);
				put("enablemobile", s.getEnablemobile() == 1);
				if(po.getScope() == 0)
				{
					Page<DsCmsPageEdit> page = service.queryPagePageEdit(getPageRequest());
					put("pageModel", page);
					put("pageNav", new PageNav<DsCmsPageEdit>(request, page));
				}
				return "/cms/category/getRecycledCategoryById.jsp";
			}
		}
		catch(Exception e)
		{
		}
		return null;
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
			if(siteid == po.getSiteid())
			{
				DsCmsSite s = service.getSite(siteid);
				if(checkOwn(s.getId()))
				{
					put("po", po);
					put("list", queryCategory(po.getSiteid(), true, id));
					put("enablemobile", s.getEnablemobile() == 1);
					put("templates", getTemplateName(s.getFolder(), false));
					put("mtemplates", getTemplateName(s.getFolder(), true));
					put("columns", GsonUtil.toBean(po.getJsontable(), List.class));
					return "/cms/category/updCategory.jsp";
				}
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
			DsCmsCategory c = service.get(po.getId());
			DsCmsSite s = service.getSite(c.getSiteid());
			if(c.getSiteid() == s.getId() && checkOwn(s.getId()))
			{
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				Map<String, String> map = new HashMap<String, String>();
				String[] cnameArr = req.getStringArray("cname", false);
				String[] ctitleArr = req.getStringArray("ctitle", false);
				String[] cdatatypeArr = req.getStringArray("cdatatype", false);
				for(int i = 0; i < cnameArr.length; i++)
				{
					if(ctitleArr[i].length() > 0 && map.get(ctitleArr[i]) == null)
					{
						Map<String, String> m = new HashMap<String, String>();
						m.put("cname", cnameArr[i]);
						m.put("ctitle", ctitleArr[i]);
						m.put("cdatatype", cdatatypeArr[i]);
						map.put(ctitleArr[i], ctitleArr[i]);
						list.add(m);
					}
				}
				po.setJsontable(GsonUtil.toJson(list));
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
				DsCmsSite s = service.getSite(siteid);
				if(checkOwn(s.getId()))
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
		filters.put("publishstatus", "true");
		List<DsCmsCategory> clist = service.queryList(filters);
		return queryCategory(clist, exclude, excludeId);
	}

	private List<DsCmsCategory> queryRecycledCategory(long siteid)
	{
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("siteid", siteid);
		filters.put("status", -1);
		return service.queryList(filters);
	}

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/html") + "/";
	}

	private void deleteCategoryFolder(String siteFolder, String categoryFolder, boolean enablemobile)
	{
		if(siteFolder != null && siteFolder.trim().length() > 0 && categoryFolder != null && categoryFolder.trim().length() > 0)
		{
			File file = new File(getCmsRoot() + "/html/" + siteFolder + "/html/a/" + categoryFolder);
			FileUtil.delete(file.getPath());
			if(enablemobile)
			{
				file = new File(getCmsRoot() + "/html/" + siteFolder + "/html/m/a/" + categoryFolder);
				FileUtil.delete(file.getPath());
			}
		}
	}

	private List<String> getTemplateName(String sitename, boolean mobile)
	{
		List<String> list = new ArrayList<String>();
		try
		{
			File file = new File(getCmsRoot() + sitename + (mobile ? "/templates/m" : "/templates"));
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
}
