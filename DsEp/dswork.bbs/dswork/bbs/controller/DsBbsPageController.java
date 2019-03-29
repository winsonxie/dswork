/**
 * 功能:主题Controller
 * 开发人员:白云区
 * 创建时间:2015/4/2 12:54:49
 */
package dswork.bbs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import dswork.core.util.FileUtil;
import dswork.core.util.TimeUtil;
import dswork.bbs.model.DsBbsForum;
import dswork.bbs.model.DsBbsPage;
import dswork.bbs.model.DsBbsSite;
import dswork.bbs.service.DsBbsPageService;

@Controller
@RequestMapping("/bbs/admin/page")
public class DsBbsPageController extends BaseController
{
	@Autowired
	private DsBbsPageService service;

	//添加
	@RequestMapping("/addPage1")
	public String addPage1()
	{
		return "/bbs/admin/page/addPage.jsp";
	}
	
	@RequestMapping("/addPage2")
	public void addPage2(DsBbsPage po)
	{
		try
		{
			Long forumid = req().getLong("forumid");
			DsBbsForum m = service.getForum(forumid);
			DsBbsSite s = service.getSite(m.getSiteid());
			if(m.getStatus() == 1 && checkOwn(s.getOwn()))
			{
				po.setSiteid(m.getSiteid());
				po.setForumid(m.getId());
				po.setUserid("");
				po.setReleasetime(TimeUtil.getCurrentTime());
				po.setLastuser("");
				po.setLasttime(po.getReleasetime());
				service.save(po);
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

	//删除
	@RequestMapping("/delPage")
	public void delPage()
	{
		try
		{
			Long categoryid = req().getLong("id");
			DsBbsForum po = service.getForum(categoryid);
			if(po.getStatus() == 1 && checkOwn(po.getSiteid()))
			{
				service.deleteBatch(CollectionUtil.toLongArray(req().getLongArray("keyIndex", 0)));
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

	//修改
	@RequestMapping("/updPage1")
	public String updPage1()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		put("page", req().getInt("page", 1));
		return "/bbs/admin/page/updPage.jsp";
	}
	
	@RequestMapping("/updPage2")
	public void updPage2(DsBbsPage po)
	{
		try
		{
			service.update(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得栏目列表
	@RequestMapping("/getPageTree")
	public String getPageTree()
	{
		try
		{
			Long id = req().getLong("siteid", -1), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
			List<DsBbsSite> siteList = service.queryListSite(map);
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
				put("list", queryListForum(siteid));
			}
			put("siteid", siteid);
			return "/bbs/admin/page/getPageTree.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	//获得分页
	@RequestMapping("/getPage")
	public String getPage()
	{
		Long forumid = req().getLong("id");
		DsBbsForum m = service.getForum(forumid);
		if(m.getStatus() == 1 && checkOwn(m.getSiteid()))// 列表
		{
			PageRequest rq = getPageRequest();
			rq.getFilters().put("siteid", m.getSiteid());
			rq.getFilters().put("forumid", m.getId());
			rq.getFilters().put("keyvalue", req().getString("keyvalue"));
			Page<DsBbsPage> pageModel = service.queryPage(rq);
			put("pageModel", pageModel);
			put("pageNav", new PageNav<DsBbsPage>(request(), pageModel));
			put("po", m);
			return "/bbs/admin/page/getPage.jsp";
		}
		return null;
	}

	//明细
	@RequestMapping("/getPageById")
	public String getPageById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/bbs/admin/page/getPageById.jsp";
	}


	private String getRoot()
	{
		return session().getServletContext().getRealPath("/") + "/";
	}
	
	@RequestMapping("/uploadImage")
	public void uploadImage()
	{
		try
		{
			Long forumid = req().getLong("forumid");
			DsBbsForum m = service.getForum(forumid);
			DsBbsSite site = service.getSite(m.getSiteid());
			if(checkOwn(site.getOwn()))
			{
				String ext = "";
				boolean isHTML5 = "application/octet-stream".equals(request().getContentType());
				byte[] byteArray = null;
				if(isHTML5)
				{
					String header = request().getHeader("Content-Disposition");
					int iStart = header.indexOf("filename=\"") + 10;
					int iEnd = header.indexOf("\"", iStart);
					String fileName = header.substring(iStart, iEnd);
					int len = fileName.lastIndexOf(".");
					ext = (len != -1) ? fileName.substring(len + 1) : "";
					int i = request().getContentLength();
					byteArray = new byte[i];
					int j = 0;
					while(j < i)// 获取表单的上传文件
					{
						int k = request().getInputStream().read(byteArray, j, i - j);
						j += k;
					}
				}
				else
				{
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request();
					MultipartFile file = multipartRequest.getFile("filedata");
					String fileName = file.getOriginalFilename();
					int len = fileName.lastIndexOf(".");
					ext = (len != -1) ? fileName.substring(len + 1) : "";
					byteArray = file.getBytes();
				}
				if(!ext.equals("") && "jpg,jpeg,gif,png".indexOf(ext) != -1)
				{
					String root = getRoot();
					String path = "/bbsfile/" + forumid + "/" + TimeUtil.getCurrentTime("yyyyMM") + "/";
					FileUtil.createFolder(root + path);
					String webpath = request().getContextPath() + path;
					String v = System.currentTimeMillis() + "." + ext.toLowerCase();
					try
					{
						FileUtil.writeFile(root + path + v, FileUtil.getToInputStream(byteArray), true);
						print("{\"err\":\"\",\"msg\":\"!" + webpath + v + "\"}");
						return;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						print("{\"err\":\"上传失败\",\"msg\":\"\"}");
						return;
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		print("{\"err\":\"上传失败！\",\"msg\":\"\"}");
	}

	private List<DsBbsForum> queryListForum(long siteid)
	{
		List<DsBbsForum> clist = service.queryListForum(siteid);
		Map<Long, DsBbsForum> map = new HashMap<Long, DsBbsForum>();
		for(DsBbsForum m : clist)
		{
			map.put(m.getId(), m);
		}
		List<DsBbsForum> tlist = new ArrayList<DsBbsForum>();
		for(DsBbsForum m : clist)
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
		List<DsBbsForum> my = new ArrayList<DsBbsForum>();
		// 处理成完整的树形结构，递归再放回list
		for(DsBbsForum m : tlist)
		{
			settingForum(my, m);
		}
		return my;
	}
	private void settingForum(List<DsBbsForum> list, DsBbsForum o)
	{
		list.add(o);
		if(o.getList().size() > 0)
		{
			List<DsBbsForum> mlist = o.getList();
			for(DsBbsForum m : mlist)
			{
				settingForum(list, m);
			}
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
