package dswork.cms.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.cms.GsonUtil;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsCategoryEdit;
import dswork.cms.model.DsCmsPage;
import dswork.cms.model.DsCmsPageEdit;
import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsEditService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.FileUtil;
import dswork.core.util.TimeUtil;
import dswork.html.HtmlUtil;
import dswork.html.nodes.Document;
import dswork.html.nodes.Element;
import dswork.http.HttpUtil;
import dswork.web.MyFile;

@Scope("prototype")
@Controller
@RequestMapping("/cms/edit")
public class DsCmsEditController extends DsCmsBaseController
{
	@Autowired
	private DsCmsEditService service;

	@RequestMapping("getCategoryTree")
	public String getCategoryTree()
	{
		try
		{
			Long id = req.getLong("siteid", -1), siteid = -1L;
			List<DsCmsSite> siteList = service.queryListSite(getOwn());
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
				List<DsCmsCategory> categoryList = service.queryListCategory(siteid);
				categoryList = categoryAccess(categoryList, this);
				put("siteList", siteList);
				put("categoryList", categoryList);
			}
			put("siteid", siteid);
			return "/cms/edit/getCategoryTree.jsp";
		}
		catch(Exception e)
		{
			return null;
		}
	}

	// 添加页面
	@RequestMapping("/addPage1")
	public String addPage1()
	{
		put("releasetime", TimeUtil.getCurrentTime());
		DsCmsCategory c = service.getCategory(req.getLong("categoryid"));
		put("columns", GsonUtil.toBean(c.getJsontable(), List.class));
		return "/cms/edit/addPage.jsp";
	}

	@RequestMapping("/addPage2")
	public void addPage2(DsCmsPageEdit po)
	{
		try
		{
			Long categoryid = req.getLong("categoryid");
			DsCmsCategory c = service.getCategory(categoryid);
			DsCmsSite s = service.getSite(c.getSiteid());
			if(c.getScope() == 0)
			{
				if(checkEdit(s.getId(), c.getId()))
				{
					po.setSiteid(c.getSiteid());
					po.setCategoryid(c.getId());
					po.pushEditidAndEditname(getAccount(), getName());
					po.setEdittime(TimeUtil.getCurrentTime());
					po.setStatus(0);
					if(po.getReleasetime().trim().equals(""))
					{
						po.setReleasetime(TimeUtil.getCurrentTime());
					}

					Map<String, String> map = new LinkedHashMap<String, String>();
					String[] ctitleArr = req.getStringArray("ctitle", false);
					String[] cvalueArr = req.getStringArray("cvalue", false);
					for(int i = 0; i < ctitleArr.length; i++)
					{
						map.put(ctitleArr[i], cvalueArr[i]);
					}
					po.setJsondata(GsonUtil.toJson(map));
					po.setImg(changeImageToLocal(s, po.getImg()));
					po.setContent(changeContentToLocal(s, po.getContent()));
					po.setStatus(0);

					String action = req.getString("action");
					if("save".equals(action))
					{
						po.setAuditstatus(0);
						service.savePageEdit(po, false, s.isWriteLog(), getAccount(), getName());// url拼接/id.html
						print(1);
						return;
					}
					if("submit".equals(action))
					{
						if(categoryNotNeedAudit(s.getId(), c.getId()))
						{
							po.setStatus(1);
							po.setAuditstatus(0);
							service.savePageEdit(po, true, s.isWriteLog(), getAccount(), getName());// url拼接/id.html
						}
						else
						{
							po.setAuditstatus(1);
							service.savePageEdit(po, false, s.isWriteLog(), getAccount(), getName());// url拼接/id.html
						}
						print(1);
						return;
					}
					print("0:参数错误");
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

	// 拷贝
	@RequestMapping("/copyPage1")
	public String copyPage1()
	{
		try
		{
			long categoryid = req.getLong("id");
			DsCmsCategory po = service.getCategory(categoryid);
			if(po.getScope() == 0)
			{
				DsCmsSite s = service.getSite(po.getSiteid());
				if(checkOwn(s.getId()))
				{
					put("list", queryCategory(po.getSiteid(), false, categoryid));
					return "/cms/edit/copyPage.jsp";
				}
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
			if(po.getScope() == 0)
			{
				DsCmsSite s = service.getSite(po.getSiteid());
				if(checkOwn(s.getId()))
				{
					DsCmsPageEdit page = service.getPageEdit(req.getLong("keyIndex"));
					if(page.getCategoryid() != categoryid)
					{
						page.setCategoryid(categoryid);
						page.setStatus(0);// 拷贝新增
						page.setAuditstatus(0);// 草稿状态
						service.savePageEdit(page, false, s.isWriteLog(), getAccount(), getName());
						print("1:拷贝成功");
						return;
					}
				}
			}
		}
		catch(Exception e)
		{
		}
		print("0:拷贝失败");
	}

	private List<DsCmsCategory> queryCategory(long siteid, boolean exclude, long excludeId)
	{
		List<DsCmsCategory> list = service.queryListCategory(siteid);
		return queryCategory(list, exclude, excludeId);
	}

	// 获得页面分页
	@RequestMapping("/getPage")
	public String getPage()
	{
		try
		{
			Long categoryid = req.getLong("id");
			DsCmsCategory c = service.getCategory(categoryid);
			if(c.getScope() == 0)
			{
				DsCmsSite s = service.getSite(c.getSiteid());
				if(checkEditall(s.getId(), c.getId()) || checkEditown(s.getId(), c.getId()))
				{
					PageRequest pr = getPageRequest();
					pr.getFilters().remove("id");
					pr.getFilters().put("siteid", c.getSiteid());
					pr.getFilters().put("categoryid", c.getId());
					pr.getFilters().put("excluderemove", "true");
					if(checkEditown(s.getId(), c.getId()))
					{
						pr.getFilters().put("editid", "," + getAccount() + ",");
					}
					Page<DsCmsPageEdit> pageModel = service.queryPagePageEdit(pr);
					put("pageModel", pageModel);
					put("pageNav", new PageNav<DsCmsPageEdit>(request, pageModel));
					put("po", c);
					put("enablemobile", s.getEnablemobile() == 1);
					put("categoryNeedAudit", !categoryNotNeedAudit(s.getId(), c.getId()));
					return "/cms/edit/getPage.jsp";
				}
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	// 删除页面
	@RequestMapping("/delPage")
	public void delPage()
	{
		try
		{
			DsCmsCategory c = service.getCategory(req.getLong("id"));
			DsCmsSite s = service.getSite(c.getSiteid());
			if(c.getScope() == 0)
			{
				long[] idArray = req.getLongArray("keyIndex", 0);
				if(categoryNotNeedAudit(s.getId(), c.getId()))
				{
					for(long id : idArray)
					{
						DsCmsPageEdit p = service.getPageEdit(id);
						if(c.getId() == p.getCategoryid())
						{
							service.deletePageEdit(p.getId());
						}
					}
					print(1);
					return;
				}
				if(checkEditall(s.getId(), c.getId()) || checkEditown(s.getId(), c.getId()))
				{
					boolean editown = checkEditown(s.getId(), c.getId());
					for(long id : idArray)
					{
						DsCmsPageEdit p = service.getPageEdit(id);
						if(c.getId() == p.getCategoryid())
						{
							if(editown && checkEditid(p.getEditid()))
							{
								continue;
							}
							if(p.getStatus() == 0)// 新增的数据，直接删除
							{
								service.deletePageEdit(p.getId());
							}
							else// 非新增的数据，需审核后才能删除
							{
								p.setStatus(-1);
								p.setAuditstatus(DsCmsPageEdit.AUDIT);
								service.updatePageEdit(p, false, s.isWriteLog(), getAccount(), getName());
							}
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

	// 采编页面
	@SuppressWarnings("unchecked")
	@RequestMapping("/updPage1")
	public String updPage1()
	{
		try
		{
			Long id = req.getLong("keyIndex");
			DsCmsPageEdit po = service.getPageEdit(id);
			DsCmsSite s = service.getSite(po.getSiteid());
			if(
					checkEditall(s.getId(), po.getCategoryid())
				|| (checkEditown(s.getId(), po.getCategoryid()) && checkEditid(po.getEditid()))
			)
			{
				DsCmsCategory c = service.getCategory(po.getCategoryid());
				List<Map<String, String>> jsontable = GsonUtil.toBean(c.getJsontable(), List.class);
				try
				{
					Map<String, Object> jsondata = GsonUtil.toBean(po.getJsondata(), Map.class);
					for(Map<String, String> m : jsontable)
					{
						String key = m.get("ctitle");
						Object value = jsondata.get(key);
						m.put("cvalue", String.valueOf(value == null ? "" : value));
					} 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				put("columns", jsontable);

				put("po", po);
				put("enablemobile", s.getEnablemobile() == 1);
				return "/cms/edit/updPage.jsp";
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}

	@RequestMapping("/updPage2")
	public void updPage2(DsCmsPageEdit po)
	{
		try
		{
			DsCmsPageEdit p = service.getPageEdit(po.getId());
			DsCmsSite s = service.getSite(p.getSiteid());
			if(
					checkEditall(s.getId(), p.getCategoryid())
				|| (checkEditown(s.getId(), p.getCategoryid()) && checkEditid(p.getEditid()))
			)
			{
				String action = req.getString("action");
				if("revoke".equals(action))
				{
					if(p.isAudit())
					{
						p.setAuditstatus(0);
						service.updateRevokePageEdit(p, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				if("restore".equals(action))
				{
					if(p.getStatus() > 0 && (p.isEdit() || p.isNopass()))
					{
						DsCmsPage page = service.getPage(po.getId());
						p.setScope(page.getScope());
						p.setUrl(page.getUrl());
						p.setTitle(page.getTitle());
						p.setMetakeywords(page.getMetakeywords());
						p.setMetadescription(page.getMetadescription());
						p.setSummary(page.getSummary());
						p.setContent(page.getContent());
						p.setReleasetime(page.getReleasetime());
						p.setReleasesource(page.getReleasesource());
						p.setReleaseuser(page.getReleaseuser());
						p.setImg(page.getImg());
						p.setImgtop(page.getImgtop());
						p.setPagetop(page.getPagetop());
						p.setAuditstatus(DsCmsPageEdit.PASS);
						p.setJsondata(page.getJsondata());
						p.setStatus(1);
						service.updatePageEdit(p, false, s.isWriteLog(), getAccount(), getName());
					}
					print(1);
					return;
				}

				p.setScope(po.getScope());
				p.setUrl(po.getUrl());
				p.setTitle(po.getTitle());
				p.setSummary(po.getSummary());
				p.setMetakeywords(po.getMetakeywords());
				p.setMetadescription(po.getMetadescription());
				p.setReleasesource(po.getReleasesource());
				p.setReleaseuser(po.getReleaseuser());
				p.setReleasetime(po.getReleasetime());
				p.setImgtop(po.getImgtop());
				p.setPagetop(po.getPagetop());
				p.setImg(changeImageToLocal(s, po.getImg()));
				p.setContent(changeContentToLocal(s, po.getContent()));
				p.setStatus(0);

				Map<String, String> map = new LinkedHashMap<String, String>();
				String[] ctitleArr = req.getStringArray("ctitle", false);
				String[] cvalueArr = req.getStringArray("cvalue", false);
				for(int i = 0; i < ctitleArr.length; i++)
				{
					map.put(ctitleArr[i], cvalueArr[i]);
				}
				p.setJsondata(GsonUtil.toJson(map));

				if("save".equals(action))
				{
					if(p.isEdit() || p.isNopass() || p.isPass())
					{
						p.setAuditstatus(0);
						p.pushEditidAndEditname(getAccount(), getName());
						p.setEdittime(TimeUtil.getCurrentTime());
						service.updatePageEdit(p, false, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				if("submit".equals(action))
				{
					if(categoryNotNeedAudit(p.getSiteid(), p.getCategoryid()))
					{
						p.setStatus(1);
						p.setAuditstatus(0);
						p.pushEditidAndEditname(getAccount(), getName());
						p.setEdittime(TimeUtil.getCurrentTime());
						service.updatePageEdit(p, true, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					if(p.isEdit() || p.isNopass() || p.isPass())
					{
						p.setAuditstatus(1);
						p.pushEditidAndEditname(getAccount(), getName());
						p.setEdittime(TimeUtil.getCurrentTime());
						service.updatePageEdit(p, false, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				print("0:参数错误");
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

	// 采编栏目
	@SuppressWarnings("unchecked")
	@RequestMapping("/updCategory1")
	public String updCategory1()
	{
		try
		{
			long id = req.getLong("id");
			DsCmsCategoryEdit po = service.getCategoryEdit(id);
			if(po == null)
			{
				po = service.saveCategoryEdit(id);
			}
			DsCmsSite s = service.getSite(po.getSiteid());
			if(checkEdit(s.getId(), po.getId()))
			{
				DsCmsCategory c = service.getCategory(po.getId());
				List<Map<String, String>> jsontable = GsonUtil.toBean(c.getJsontable(), List.class);
				try
				{
					Map<String, Object> jsondata = GsonUtil.toBean(po.getJsondata(), Map.class);
					for(Map<String, String> m : jsontable)
					{
						String key = m.get("ctitle");
						Object value = jsondata.get(key);
						m.put("cvalue", String.valueOf(value == null ? "" : value));
					} 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				put("columns", jsontable);

				if(po.getReleasetime().isEmpty())
				{
					po.setReleasetime(TimeUtil.getCurrentTime());
				}
				put("enablemobile", s.getEnablemobile() == 1);
				put("scope", c.getScope());
				put("po", po);
				return "/cms/edit/updCategory.jsp";
			}
			return null;
		}
		catch(Exception ee)
		{
			return null;
		}
	}

	@RequestMapping("/updCategory2")
	public void updCategory2(DsCmsCategoryEdit po)
	{ 
		try
		{
			DsCmsCategory c = service.getCategory(po.getId());
			DsCmsSite s = service.getSite(c.getSiteid());
			if(checkEdit(s.getId(), c.getId()))
			{
				DsCmsCategoryEdit p = service.getCategoryEdit(po.getId());
				String action = req.getString("action");
				if("revoke".equals(action))
				{
					if(p.isAudit())
					{
						p.setAuditstatus(0);
						service.updateRevokeCategoryEdit(p, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				if("restore".equals(action))
				{
					if(p.getStatus() > 0 && (p.isEdit() || p.isNopass()))
					{
						p.setMetakeywords(c.getMetakeywords());
						p.setMetadescription(c.getMetadescription());
						p.setSummary(c.getSummary());
						p.setContent(c.getContent());
						p.setReleasetime(c.getReleasetime());
						p.setReleasesource(c.getReleasesource());
						p.setReleaseuser(c.getReleaseuser());
						p.setImg(c.getImg());
						p.setUrl(c.getUrl());
						p.setAuditstatus(DsCmsCategoryEdit.PASS);
						p.setJsondata(c.getJsondata());
						service.updateCategoryEdit(p, false, s.isWriteLog(), getAccount(), getName());
					}
					print(1);
					return;
				}

				p.setMetakeywords(po.getMetakeywords());
				p.setMetadescription(po.getMetadescription());
				p.setSummary(po.getSummary());
				p.setReleasetime(po.getReleasetime());
				p.setReleasesource(po.getReleasesource());
				p.setReleaseuser(po.getReleaseuser());
				p.setUrl(po.getUrl());
				p.pushEditidAndEditname(getAccount(), getName());
				p.setEdittime(TimeUtil.getCurrentTime());
				p.setImg(changeImageToLocal(s, po.getImg()));
				p.setContent(changeContentToLocal(s, po.getContent()));
				p.setStatus(0);

				Map<String, String> map = new LinkedHashMap<String, String>();
				String[] ctitleArr = req.getStringArray("ctitle", false);
				String[] cvalueArr = req.getStringArray("cvalue", false);
				for(int i = 0; i < ctitleArr.length; i++)
				{
					map.put(ctitleArr[i], cvalueArr[i]);
				}
				p.setJsondata(GsonUtil.toJson(map));

				if("save".equals(action))
				{
					if(p.isEdit() || p.isNopass() || p.isPass())
					{
						p.setAuditstatus(0);
						service.updateCategoryEdit(p, false, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				if("submit".equals(action))
				{
					if(categoryNotNeedAudit(p.getSiteid(), p.getId()))
					{
						p.setStatus(1);
						po.setAuditstatus(0);
						service.updateCategoryEdit(p, true, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					if(p.isEdit() || p.isNopass() || p.isPass())
					{
						p.setAuditstatus(1);
						service.updateCategoryEdit(p, false, s.isWriteLog(), getAccount(), getName());
						print(1);
						return;
					}
					print("0:状态错误");
					return;
				}
				print("0:参数错误");
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

	@RequestMapping("/uploadImage")
	public void uploadImage()
	{
		try
		{
			Long categoryid = req.getLong("categoryid");
			DsCmsCategory m = service.getCategory(categoryid);
			DsCmsSite site = service.getSite(m.getSiteid());
			if(checkOwn(site.getId()))
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
		catch(Exception e)
		{
			e.printStackTrace();
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
			if(checkOwn(site.getId()))
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
		catch(Exception e)
		{
			e.printStackTrace();
		}
		print("{\"err\":\"上传失败！\",\"msg\":\"\"}");
	}

	private String changeContentToLocal(DsCmsSite site, String content)
	{
		Document doc = HtmlUtil.parse(content);
		List<Element> imgs = doc.select("img");
		for(Element img : imgs)
		{
			String imgUrl = img.attr("src");
			String newUrl = changeImageToLocal(site, imgUrl);
			if(!imgUrl.equals(newUrl))
			{
				content = content.replace(imgUrl, newUrl);
			}
		}
		return content;
	}

	private String changeImageToLocal(DsCmsSite site, String imgUrl)
	{
		if(site.getUrl().length() == 0)
		{
			if(imgUrl.startsWith("http"))
			{
				return remoteImageToLocal(site.getUrl(), site.getFolder(), imgUrl);
			}
		}
		else
		{
			if(imgUrl.startsWith("http") && !imgUrl.startsWith(site.getUrl()))
			{
				return remoteImageToLocal(site.getUrl(), site.getFolder(), imgUrl);
			}
		}
		return imgUrl;
	}

	private String remoteImageToLocal(String siteUrl, String siteFolder, String imgUrl)
	{
		if(
			imgUrl.endsWith(".jpg") ||
			imgUrl.endsWith(".jpeg") ||
			imgUrl.endsWith(".gif") ||
			imgUrl.endsWith(".png")
		)
		{
			String[] ss = imgUrl.split("\\.");
			String extName = ss[ss.length - 1];
			String imgName = System.currentTimeMillis() + "." + extName;
			String ym = TimeUtil.getCurrentTime("yyyyMM");
			String imgPath = getCmsRoot() + "/html/" + siteFolder + "/html/f/img/" + ym + "/" + imgName;
			HttpUtil httpUtil = new HttpUtil().create(imgUrl);
			if(FileUtil.writeFile(imgPath, httpUtil.connectStream(), true))
			{
				return siteUrl + "/f/img/" + ym + "/" + imgName;
			}
		}
		return imgUrl;
	}

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/") + "/";
	}

	private boolean checkEditid(String editid)
	{
		return editid.indexOf("," + getAccount() + ",") != -1;
	}

	@Override
	public boolean checkCategory(DsCmsCategory category)
	{
		return checkEdit(category.getSiteid(), category.getId());
	}
}
