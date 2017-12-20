package dswork.cms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPage;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsPageService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import dswork.core.util.FileUtil;
import dswork.core.util.TimeUtil;
import dswork.http.HttpUtil;
import dswork.mvc.BaseController;
import dswork.web.MyFile;

@Scope("prototype")
@Controller
@RequestMapping("/cms/page")
public class DsCmsPageController extends BaseController
{
	private HttpUtil httpUtil = new HttpUtil();// 单例模式下，用来保持session会话
	@Autowired
	private DsCmsPageService service;

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/") + "/";
	}

	private String getLocalAddr()
	{
		String addr = request.getLocalAddr();
		if(addr != null && addr.indexOf(":") != -1)
		{
			addr = "[" + addr + "]";
		}
		return addr;
	}

	// 添加
	@RequestMapping("/addPage1")
	public String addPage1()
	{
		put("releasetime", TimeUtil.getCurrentTime());
		return "/cms/page/addPage.jsp";
	}

	@RequestMapping("/addPage2")
	public void addPage2(DsCmsPage po)
	{
		try
		{
			Long categoryid = req.getLong("categoryid");
			DsCmsCategory m = service.getCategory(categoryid);
			DsCmsSite s = service.getSite(m.getSiteid());
			if(m.getScope() == 0 && checkOwn(s.getOwn()))
			{
				po.setSiteid(m.getSiteid());
				po.setCategoryid(m.getId());
				if(po.getReleasetime().trim().equals(""))
				{
					po.setReleasetime(TimeUtil.getCurrentTime());
				}
				if(po.getScope() != 2) // 不为外链
				{
					po.setUrl("/a/" + m.getFolder());
				}
				po.setStatus(0);
				service.savePage(po, s.isEnablelog(), getAccount(), getName());// url拼接/id.html
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

	// 删除
	@RequestMapping("/delPage")
	public void delPage()
	{
		try
		{
			long categoryid = req.getLong("id");
			DsCmsCategory po = service.getCategory(categoryid);
			if(po.getScope() == 0 && checkOwn(po.getSiteid()))
			{
				service.deleteBatchPage(CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
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

	// 拷贝
	@RequestMapping("/copyPage1")
	public String copyPage1()
	{
		try
		{
			long categoryid = req.getLong("id");
			DsCmsCategory po = service.getCategory(categoryid);
			if(po.getScope() == 0 && checkOwn(po.getSiteid()))
			{
				put("list", queryCategory(po.getSiteid(), false, categoryid));
				return "/cms/page/copyPage.jsp";
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/copyPage2")
	public void copyPage2()
	{
		try
		{
			long categoryid = req.getLong("id");
			DsCmsCategory po = service.getCategory(categoryid);
			DsCmsSite s = service.getSite(po.getSiteid());
			if(po.getScope() == 0 && checkOwn(s.getId()))
			{
				DsCmsPage page = service.getPage(req.getLong("keyIndex"));
				if(page.getCategoryid() != categoryid)
				{
					page.setId(0L);
					page.setCategoryid(categoryid);
					service.savePage(page, s.isEnablelog(), getAccount(), getName());
					print("1:拷贝成功");
					return;
				}
			}
		}
		catch(Exception e)
		{
		}
		print("0:拷贝失败");
	}

	// 修改
	@RequestMapping("/updPage1")
	public String updPage1()
	{
		try
		{
			Long id = req.getLong("keyIndex");
			put("po", service.getPage(id));
			put("page", req.getInt("page", 1));
			return "/cms/page/updPage.jsp";
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@RequestMapping("/updPage2")
	public void updPage2(DsCmsPage po)
	{
		try
		{
			DsCmsPage p = service.getPage(po.getId());
			DsCmsSite s = service.getSite(p.getSiteid());
			if(checkOwn(s.getOwn()))
			{
				po.setStatus(1);
				if(po.getScope() != 2) // 不为外链
				{
					DsCmsCategory m = service.getCategory(p.getCategoryid());
					po.setUrl("/a/" + m.getFolder() + "/" + po.getId() + ".html");
				}
				service.updatePage(po, s.isEnablelog(), getAccount(), getName());
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

	// 修改
	@RequestMapping("/updCategory1")
	public String updCategory1()
	{
		try
		{
			Long id = req.getLong("id");
			DsCmsCategory po = service.getCategory(id);
			if(po.getScope() == 1)// 单页栏目
			{
				if(po.getReleasetime() == null || po.getReleasetime().length() == 0)
				{
					po.setReleasetime(TimeUtil.getCurrentTime());
				}
				put("po", po);
				return "/cms/page/updCategory.jsp";
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
			DsCmsCategory m = service.getCategory(po.getId());
			DsCmsSite s = service.getSite(m.getSiteid());
			if(m.getScope() == 1 && checkOwn(m.getSiteid()))
			{
				po.setStatus(1);
				po.setSiteid(m.getSiteid());
				po.setName(m.getName());
				po.setScope(m.getScope());
				service.updateCategory(po, s.isEnablelog(), getAccount(), getName());
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
	@RequestMapping("/getCategoryTree")
	public String getCategoryTree()
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
				put("list", queryCategory(siteid));
			}
			put("siteid", siteid);
			return "/cms/page/getCategoryTree.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	// 获得分页
	@RequestMapping("/getPage")
	public String getPage()
	{
		try
		{
			Long categoryid = req.getLong("id");
			DsCmsCategory m = service.getCategory(categoryid);
			if(m.getScope() == 0 && checkOwn(m.getSiteid()))// 列表
			{
				PageRequest rq = getPageRequest();
				rq.getFilters().put("siteid", m.getSiteid());
				rq.getFilters().put("categoryid", m.getId());
				rq.getFilters().put("keyvalue", req.getString("keyvalue"));
				Page<DsCmsPage> pageModel = service.queryPagePage(rq);
				put("pageModel", pageModel);
				put("pageNav", new PageNav<DsCmsPage>(request, pageModel));
				put("po", m);
				return "/cms/page/getPage.jsp";
			} 
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/uploadImage")
	public void uploadImage()
	{
		try
		{
			Long categoryid = req.getLong("categoryid");
			DsCmsCategory m = service.getCategory(categoryid);
			DsCmsSite site = service.getSite(m.getSiteid());
			if(checkOwn(site.getOwn()))
			{
				String ext = "";
				byte[] byteArray = null;
				if(req.getFileArray().length > 0)
				{
					MyFile file = req.getFileArray()[0];
					byteArray = file.getFileData();
					ext = file.getFileExt();
				}
				if(!ext.equals("") && "jpg,jpeg,gif,png".indexOf(ext) != -1)
				{
					String zoom = req.getString("zoom", "true");
					String root = getCmsRoot();
					String ym = TimeUtil.getCurrentTime("yyyyMM");
					String path = "/html/" + site.getFolder() + "/html/f/img/" + ym + "/";
					FileUtil.createFolder(root + path);
					String webpath = site.getUrl() + "/f/img/" + ym + "/";
					String v = System.currentTimeMillis() + "." + ext.toLowerCase();
					try
					{
						if("true".equals(zoom) && "jpg,jpeg,png".indexOf(ext) != -1)
						{
							// 压缩图片使尺寸最多不超过1000*1000
							byte[] arr = dswork.core.util.ImageUtil.resize(FileUtil.getToInputStream(byteArray), 1000, 1000);
							if(arr == null)
							{
								arr = byteArray;
							}
							FileUtil.writeFile(root + path + v, FileUtil.getToInputStream(arr), true);
						}
						else// gif
						{
							FileUtil.writeFile(root + path + v, FileUtil.getToInputStream(byteArray), true);
						}
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

	@RequestMapping("/uploadFile")
	public void uploadFile()
	{
		try
		{
			Long categoryid = req.getLong("categoryid");
			DsCmsCategory m = service.getCategory(categoryid);
			DsCmsSite site = service.getSite(m.getSiteid());
			if(checkOwn(site.getOwn()))
			{
				String ext = "";
				byte[] byteArray = null;
				if(req.getFileArray().length > 0)
				{
					MyFile file = req.getFileArray()[0];
					byteArray = file.getFileData();
					ext = file.getFileExt();
				}
				if(!ext.equals("") && "bmp,doc,docx,gif,jpeg,jpg,pdf,png,ppt,pptx,rar,rtf,txt,xls,xlsx,zip,7z".indexOf(ext) != -1)
				{
					String root = getCmsRoot();
					String ym = TimeUtil.getCurrentTime("yyyyMM");
					String path = "/html/" + site.getFolder() + "/html/f/file/" + ym + "/";
					FileUtil.createFolder(root + path);
					String webpath = site.getUrl() + "/f/file/" + ym + "/";
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

	// 发布指定的信息
	@RequestMapping("/build")
	public void build()
	{
		Long siteid = req.getLong("siteid", -1);
		Long categoryid = req.getLong("categoryid", -1);
		Long pageid = req.getLong("pageid", -1);
		int pagesize = req.getInt("pagesize", 25);
		_building(true, siteid, categoryid, pageid, pagesize);
	}

	// 删除指定的信息
	@RequestMapping("/unbuild")
	public void unbuild()
	{
		Long siteid = req.getLong("siteid", -1);
		Long categoryid = req.getLong("categoryid", -1);
		Long pageid = req.getLong("pageid", -1);
		int pagesize = req.getInt("pagesize", 25);
		_building(false, siteid, categoryid, pageid, pagesize);
	}

	/**
	 * 取出当前登录用户的栏目
	 * @param exclude 是否包括非List的栏目
	 * @param excludeId 需要清空指定id的子栏目
	 * @return List
	 */
	private List<DsCmsCategory> queryCategory(long siteid, boolean exclude, long excludeId)
	{
		List<DsCmsCategory> list = service.queryListCategory(siteid);
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		for(DsCmsCategory m : list)
		{
			map.put(m.getId(), m);
		}
		List<DsCmsCategory> _list = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory m : list)
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
					catch(Exception ex)// 找不到对应的父栏目
					{
					}
				}
				else if(m.getPid() == 0)
				{
					if(m.getScope() == 0 || exclude)
					{
						_list.add(m);// 只把根节点放入list
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
		return DsCmsUtil.categorySettingList(_list);
	}

	/**
	 * 生成或删除信息
	 * @param isCreateOrDelete true生成，false删除
	 * @param siteid
	 * @param categoryid
	 * @param pageid
	 * @param pagesize
	 */
	private void _building(boolean isCreateOrDelete, long siteid, long categoryid, long pageid, int pagesize)
	{
		pagesize = (pagesize <= 0) ? 25 : pagesize;
		try
		{
			if(siteid >= 0)
			{
				DsCmsSite site = service.getSite(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getOwn()))
				{
					String path = "http://" + getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath() + "/cmsbuild/buildHTML.chtml?siteid=" + siteid;
					// 首页：categoryid==-1，pageid==-1
					// 全部栏目：categoryid==0，pageid==-1
					// 指定栏目：categoryid>0，pageid==-1
					// 全部内容：categoryid==0，pageid==0
					// 栏目内容：categoryid>0，pageid==0
					// 指定内容：pageid>0
					if(pageid > 0)// 指定内容
					{
						DsCmsPage p = service.getPage(pageid);
						if(p.getSiteid() == siteid)
						{
							try
							{
								if(p.getStatus() == -1)
								{
									_buildFile(null, p.getUrl(), site.getFolder());
									service.deletePage(p.getId());
								}
								else if(p.getScope() == 2)
								{
									DsCmsCategory c = service.getCategory(p.getCategoryid());
									_buildFile(null, "/a/" + c.getFolder() + "/" + p.getId() + ".html", site.getFolder());
								}
								else
								{
									_buildFile(isCreateOrDelete ? path + "&pageid=" + p.getId() : null, p.getUrl(), site.getFolder());
									service.updatePageStatus(p.getId(), isCreateOrDelete ? 8 : 0);
								}
								print("1");
							}
							catch(Exception e)
							{
								print("0");
							}
						}
						else
						{
							print("0:无权操作");
						}
					}
					else if(pageid == -1)// 栏目首页
					{
						List<DsCmsCategory> list = new ArrayList<DsCmsCategory>();
						if(categoryid == 0)// 全部栏目首页
						{
							list = service.queryListCategory(siteid);
						}
						else if(categoryid > 0)// 指定栏目首页
						{
							DsCmsCategory c = service.getCategory(categoryid);
							if(c.getSiteid() == siteid)
							{
								list.add(c);
							}
						}
						if(categoryid >= 0)// 栏目首页，这里不能用list.size，因为可能长度就是0
						{
							for(DsCmsCategory c : list)
							{
								try
								{
									if(c.getScope() == 2)// 外链没有东西生成的
									{
										_deleteFile(site.getFolder(), c.getFolder(), true, true);
										continue;
									}
									_deleteFile(site.getFolder(), c.getFolder(), true, false);// 删除栏目首页
									if(isCreateOrDelete)
									{
										_buildFile(path + "&categoryid=" + c.getId() + "&page=1&pagesize=" + pagesize, c.getUrl(), site.getFolder());
										Map<String, Object> map = new HashMap<String, Object>();
										map.put("siteid", site.getId());
										map.put("categoryid", c.getId());
										map.put("releasetime", TimeUtil.getCurrentTime());
										PageRequest rq = new PageRequest(map);
										rq.setPageSize(pagesize);
										rq.setCurrentPage(1);
										Page<DsCmsPage> pageModel = service.queryPagePage(rq);
										for(int i = 2; i <= pageModel.getLastPage(); i++)
										{
											_buildFile(path + "&categoryid=" + c.getId() + "&page=" + i + "&pagesize=" + pagesize, c.getUrl().replaceAll("\\.html", "_" + i + ".html"), site.getFolder());
										}
									}
									service.updateCategoryStatus(c.getId(), isCreateOrDelete ? 8 : 0);
								}
								catch(Exception e)
								{
								}
							}
						}
						else// 首页
						{
							_buildFile(isCreateOrDelete ? path : null, "/index.html", site.getFolder());
						}
						print("1");
					}
					else if(pageid == 0)// 栏目内容
					{
						List<DsCmsCategory> list = new ArrayList<DsCmsCategory>();
						if(categoryid == 0)// 全部栏目内容
						{
							list = service.queryListCategory(siteid);
						}
						else if(categoryid > 0)// 指定栏目内容
						{
							DsCmsCategory c = service.getCategory(categoryid);
							if(c.getSiteid() == siteid)
							{
								list.add(c);
							}
						}
						for(DsCmsCategory c : list)
						{
							if(c.getScope() == 2)// 外链没有东西生成的
							{
								_deleteFile(site.getFolder(), c.getFolder(), true, true);
								continue;
							}
							_deleteFile(site.getFolder(), c.getFolder(), false, true);// 删除内容
							if(isCreateOrDelete)
							{
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("siteid", site.getId());
								map.put("releasetime", TimeUtil.getCurrentTime());
								map.put("categoryid", c.getId());
								PageRequest rq = new PageRequest(1, pagesize, map);
								Page<DsCmsPage> pageModel = service.queryPagePage(rq);
								for(DsCmsPage p : pageModel.getResult())
								{
									try
									{
										if(p.getStatus() == -1)
										{
											service.deletePage(p.getId());
										}
										else if(p.getScope() != 2)
										{
											_buildFile(isCreateOrDelete ? path + "&pageid=" + p.getId() : null, p.getUrl(), site.getFolder());
											service.updatePageStatus(p.getId(), isCreateOrDelete ? 8 : 0);
										}
									}
									catch(Exception e)
									{
									}
								}
								for(int i = 2; i <= pageModel.getLastPage(); i++)
								{
									map.clear();
									map.put("siteid", site.getId());
									map.put("releasetime", TimeUtil.getCurrentTime());
									map.put("categoryid", c.getId());
									rq.setFilters(map);
									rq.setPageSize(pagesize);
									rq.setCurrentPage(i);
									List<DsCmsPage> pageList = service.queryListPage(rq);
									for(DsCmsPage p : pageList)
									{
										try
										{
											if(p.getStatus() == -1)
											{
												service.deletePage(p.getId());
											}
											else if(p.getScope() != 2)
											{
												_buildFile(isCreateOrDelete ? path + "&pageid=" + p.getId() : null, p.getUrl(), site.getFolder());
												service.updatePageStatus(p.getId(), isCreateOrDelete ? 8 : 0);
											}
										}
										catch(Exception e)
										{
										}
									}
								}
							}
						}
						print("1");
					}
					else
					{
						print("0");
					}
				}
				else
				{
					print("0");
				}
			}
			else
			{
				print("0");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print(isCreateOrDelete ? "0:生成失败" : "0:删除失败");
		}
	}

	private void _deleteFile(String siteFolder, String categoryFolder, boolean deleteCategory, boolean deletePage)
	{
		// 这部分处理不当，全把整个站点的都删除的
		if(siteFolder != null && siteFolder.trim().length() > 0 && categoryFolder != null && categoryFolder.trim().length() > 0)
		{
			java.io.File file = new java.io.File(getCmsRoot() + "/html/" + siteFolder + "/html/a/" + categoryFolder);
			if(file.exists())
			{
				for(java.io.File f : file.listFiles())
				{
					if(f.isDirectory())
					{
						FileUtil.delete(f.getPath());// 不应该存在目录
					}
					if(f.isFile())
					{
						if(f.getName().startsWith("index"))
						{
							if(deleteCategory)
							{
								f.delete();// 删除栏目首页
							}
						}
						else
						{
							if(deletePage)
							{
								f.delete();// 删除栏目内容
							}
						}
					}
				}
			}
		}
	}

	private void _buildFile(String path, String urlpath, String siteFolder)
	{
		try
		{
			String p = getCmsRoot().replaceAll("\\\\", "/") + "html/" + siteFolder + "/html/" + urlpath;
			if(path == null)
			{
				FileUtil.delete(p);
			}
			else
			{
				FileUtil.writeFile(p, httpUtil.create(path).connectStream(), true);
			}
		}
		catch(Exception e)
		{
		}
	}

	// 取出可处理数据的栏目
	private List<DsCmsCategory> queryCategory(Long siteid)
	{
		List<DsCmsCategory> clist = service.queryListCategory(siteid);
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			map.put(m.getId(), m);
		}
		List<DsCmsCategory> tlist = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			if(m.getPid() > 0)
			{
				try
				{
					if(m.getScope() == 0 || m.getScope() == 1)// 过滤外链栏目
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
				if(m.getScope() == 0 || m.getScope() == 1)// 过滤外链栏目
				{
					tlist.add(m);// 只把根节点放入list
				}
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

	private String getAccount()
	{
		return common.auth.AuthUtil.getLoginUser(request).getAccount();
	}

	private String getName()
	{
		return common.auth.AuthUtil.getLoginUser(request).getName();
	}
}
