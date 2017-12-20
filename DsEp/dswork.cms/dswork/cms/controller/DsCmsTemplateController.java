/**
 * 功能:公共Controller
 */
package dswork.cms.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsSiteService;
import dswork.core.util.FileUtil;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/cms/template")
public class DsCmsTemplateController extends BaseController
{
	@Autowired
	private DsCmsSiteService service;

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/html") + "/";
	}

	// 模块
	@RequestMapping("/getTemplateTree")
	public String getTemplateTree()
	{
		try
		{
			Long id = req.getLong("siteid"), siteid = -1L;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("own", getOwn());
			List<DsCmsSite> siteList = service.queryList(map);
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
							put("site", m);
							break;
						}
					}
				}
				if(siteid == -1)
				{
					siteid = siteList.get(0).getId();
					put("site", siteList.get(0));
				}
			}
			put("siteid", siteid);
			return "/cms/template/getTemplateTree.jsp";
		}
		catch(Exception ex)
		{
		}
		return null;
	}

	@RequestMapping("/getTemplateTreeJson")
	public void getTemplateTreeJson()
	{
		StringBuilder sb = new StringBuilder(2);
		sb.append("[");
		try
		{
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "");
			long pid = req.getLong("pid", 0);
			if(siteid >= 0 && uriPath.indexOf("..") == -1)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getOwn()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/templates/";
					File froot = new File(filePath);
					File finclude = new File(filePath + "include");
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录和include目录
					if(f.isDirectory() && (f.getPath().equals(froot.getPath()) || f.getPath().equals(finclude.getPath())))
					{
						int i = 0;
						boolean b = f.getName().equals("include");
						if(!b)
						{
							sb.append("{id:1,pid:0,isParent:true,name:\"include\",path:\"include/\"}");
							i++;
						}
						for(File o : f.listFiles())
						{
							if(o.isFile())
							{
								if(i > 0)
								{
									sb.append(",");
								}
								sb.append("{id:").append(UniqueId.genId()).append(",pid:").append(pid).append(",name:\"").append(o.getName()).append("\",path:\"").append(uriPath).append(o.getName()).append("\"}");
								i++;
							}
						}
					}
				}
			}
		}
		catch(Exception ex)
		{
		}
		sb.append("]");
		System.out.println(sb.toString());
		print(sb.toString());
	}

	// 内容编辑
	@RequestMapping("/editTemplate1")
	public String editTemplate1()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "/");
			if(siteid >= 0 && uriPath.indexOf("..") == -1)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getOwn()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/templates/";
					File froot = new File(filePath);
					File finclude = new File(filePath + "include");
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录和include目录下的文件
					if(f.isFile() && (f.getParent().equals(froot.getPath()) || f.getParent().equals(finclude.getPath())))
					{
						put("content", FileUtil.readFile(f.getPath(), "UTF-8"));
						put("path", uriPath);
						put("siteid", siteid);
						return "/cms/template/editTemplate.jsp";
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping("/editTemplate2")
	public void editTemplate2()
	{
		try
		{
			String content = req.getString("content");
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "/");
			if(siteid >= 0 && uriPath.indexOf("..") == -1)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getOwn()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/templates/";
					File froot = new File(filePath);
					File bak = new File(filePath + "bak");
					bak.mkdirs();
					File finclude = new File(filePath + "include");
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录和include目录下的文件
					if(f.isFile() && (f.getParent().equals(froot.getPath()) || f.getParent().equals(finclude.getPath())))
					{
						try
						{
							FileUtil.copy(f.getPath(), bak.getPath() + "/" + TimeUtil.getCurrentTime("yyyyMMddHHmmss") + f.getName());
						}
						catch(Exception e)
						{
							print("0:文件备份失败，请重试");
							return;
						}
						FileUtil.writeFile(f.getPath(), content, "UTF-8", true);
						print("1");
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:文件读写失败，请重试");
		}
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
