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

@Scope("prototype")
@Controller
@RequestMapping("/cms/template")
public class DsCmsTemplateController extends DsCmsBaseController
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
				DsCmsSite s = service.get(siteid);
				boolean enablemobile = s.getEnablemobile() == 1;
				if(s != null)
				{
					s.setFolder(String.valueOf(s.getFolder()).replace("\\", "").replace("/", ""));
				}
				if(s != null && s.getFolder().trim().length() > 0 && checkOwn(s.getId()))
				{
					String filePath = getCmsRoot() + s.getFolder() + "/templates/";
					File froot = new File(filePath);
					File finclude = new File(filePath + "include");
					File mroot = new File(filePath + "m");
					File minclude = new File(filePath + "m/include");
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录、include目录、m根目录、m下include目录
					if(f.isDirectory() && (
							f.getPath().equals(froot.getPath())
							|| f.getPath().equals(finclude.getPath())
							|| f.getPath().equals(mroot.getPath())
							|| f.getPath().equals(minclude.getPath())
					))
					{
						boolean first = true;
						if(f.getPath().equals(froot.getPath()))
						{
							sb.append("{id:1,pid:0,isParent:true,name:\"include\",path:\"include/\"}");
							if(enablemobile)
							{
								sb.append(",{id:2,pid:0,isParent:true,name:\"m\",path:\"m/\"}");
							}
							first = false;
						}
						else if(f.getPath().equals(mroot.getPath()))
						{
							sb.append("{id:21,pid:0,isParent:true,name:\"include\",path:\"m/include/\"}");
							first = false;
						}
						for(File o : f.listFiles())
						{
							if(o.isFile())
							{
								sb.append(first? "{id:" : ",{id:").append(UniqueId.genId())
									.append(",pid:").append(pid)
									.append(",name:\"").append(o.getName())
									.append("\",path:\"").append(uriPath).append(o.getName())
									.append("\"}");
								first = false;
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
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/templates/";
					File froot = new File(filePath);
					File finclude = new File(filePath + "include");
					File mroot = new File(filePath + "m");
					File minclude = new File(filePath + "m/include");
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录、include目录、m根目录、m下include目录
					if(f.isFile() && (
							f.getParent().equals(froot.getPath())
							|| f.getParent().equals(finclude.getPath())
							|| f.getParent().equals(mroot.getPath())
							|| f.getParent().equals(minclude.getPath())
					))
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
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/templates/";
					File froot = new File(filePath);
					File finclude = new File(filePath + "include");
					File mroot = new File(filePath + "m");
					File minclude = new File(filePath + "m/include");
					File bak = new File(filePath + "bak");
					bak.mkdirs();
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录、include目录、m根目录、m下include目录
					if(f.isFile() && (
							f.getParent().equals(froot.getPath())
							|| f.getParent().equals(finclude.getPath())
							|| f.getParent().equals(mroot.getPath())
							|| f.getParent().equals(minclude.getPath())
					))
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

	@RequestMapping("readme")
	public String readme()
	{
		return "/cms/template/readme.jsp";
	}
}
