/**
 * 功能:公共Controller
 */
package dswork.cms.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.cms.model.DsCmsSite;
import dswork.cms.service.DsCmsSiteService;
import dswork.core.util.FileUtil;
import dswork.core.util.UniqueId;
import dswork.web.MyFile;

@Scope("prototype")
@Controller
@RequestMapping("/cms/file")
public class DsCmsFileController extends DsCmsBaseController
{
	@Autowired
	private DsCmsSiteService service;

	private String getCmsRoot()
	{
		return request.getSession().getServletContext().getRealPath("/html") + "/";
	}

	@RequestMapping("/getFileTree")
	public String getFileTree()
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
			return "/cms/file/getFileTree.jsp";
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文件树
	 */
	@RequestMapping("/getFileTreeJson")
	public void getFileTreeJson()
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
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String filePath = getCmsRoot() + site.getFolder() + "/html/f/res/";
					File froot = new File(filePath);
					File f = new File(filePath + uriPath);
					// 限制为只能读取根目录下的信息
					if(f.isDirectory() && (f.getPath().startsWith(froot.getPath())))
					{
						int i = 0;
						for(File o : f.listFiles())
						{
							if(o.isDirectory())
							{
								if(i > 0)
								{
									sb.append(",");
								}
								sb.append("{id:").append(UniqueId.genId()).append(",pid:").append(pid).append(",isParent:").append(o.isDirectory()).append(",name:\"").append(o.getName()).append("\",path:\"").append(uriPath).append(o.getName()).append("/\"}");
								i++;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		sb.append("]");
		print(sb.toString());
	}

	/**
	 * 获取文件树
	 */
	@RequestMapping("/getFile")
	public String getFile()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "");
			if(siteid >= 0 && uriPath.indexOf("..") == -1)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String resPath = getCmsRoot() + site.getFolder() + "/html/f/res/";
					File froot = new File(resPath);
					File f = new File(resPath + uriPath);
					// 限制为只能读取根目录下的信息
					if(f.isDirectory() && (f.getPath().startsWith(froot.getPath())))
					{
						for(File o : f.listFiles())
						{
							if(o.isFile())
							{
								Map<String, Object> m = new HashMap<String, Object>();
								m.put("name", o.getName());
								m.put("path", uriPath + o.getName());
								m.put("root", site.getUrl() + "/f/res/");
								list.add(m);
							}
						}
					}
				}
				put("list", list);
				put("path", uriPath);
				put("siteid", siteid);
				return "/cms/file/getFile.jsp";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转文件上传页面
	 * @return
	 */
	@RequestMapping("/uploadFile1")
	public String uploadFile1()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "");
			if(siteid >= 0 && uriPath.indexOf("..") == -1)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String resPath = getCmsRoot() + site.getFolder() + "/html/f/res/";
					File froot = new File(resPath);
					froot.mkdirs();
					File f = new File(resPath + uriPath);
					// 限制为只能读取根目录下的信息
					if(f.isDirectory() && (f.getPath().startsWith(froot.getPath())))
					{
						put("list", list);
						put("path", uriPath);
						put("siteid", siteid);
						put("hz", hz);
						return "/cms/file/uploadFile.jsp";
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

	@RequestMapping("/uploadFile2")
	public void uploadFile2()
	{
		try
		{
			long siteid = req.getLong("siteid", -1);
			String uriPath = req.getString("path", "");
			MyFile zipFile = null;
			if(req.getFileArray().length > 0)
			{
				zipFile = (req.getFileArray())[0];
			}
			if(siteid >= 0 && uriPath.indexOf("..") == -1 && zipFile != null && zipFile.getFileData() != null)// 防止读取上级目录
			{
				DsCmsSite site = service.get(siteid);
				if(site != null)
				{
					site.setFolder(String.valueOf(site.getFolder()).replace("\\", "").replace("/", ""));
				}
				// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if(site != null && site.getFolder().trim().length() > 0 && checkOwn(site.getId()))
				{
					String resPath = getCmsRoot() + site.getFolder() + "/html/f/res/";
					File froot = new File(resPath);
					File f = new File(resPath + uriPath);
					// 限制为只能读取根目录下的信息
					if(f.isDirectory() && (f.getPath().startsWith(froot.getPath())))
					{
						unzipFile(zipFile.getFileData(), resPath + uriPath);
					}
				}
			}
			print("{\"err\":\"\",\"msg\":\"x.zip\"}");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("{\"err\":\"上传失败\",\"msg\":\"\"}");
		}
	}
	private static String hz = "avi,bmp,css,doc,docx,flv,gif,jpeg,jpg,mp3,mp4,pdf,png,ppt,pptx,rtf,swf,txt,xls,xlsx";
	private static String cc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_0123456789.";
	private static String[] hzArr = hz.split(",");

	/**
	 * 解压文件
	 * @param zipFilePath //要解压得文件路径
	 * @param targetPath //要解压得目标路径
	 */
	public void unzipFile(byte[] dataArray, String targetPath)
	{
		try
		{
			// File zipFile = new File(zipFilePath);
			// InputStream is = new FileInputStream(zipFile);
			InputStream is = FileUtil.getToInputStream(dataArray);
			ZipInputStream zis = new ZipInputStream(is);//, java.nio.charset.Charset.forName("GBK")
			ZipEntry entry = null;
			outer:while((entry = zis.getNextEntry()) != null)
			{
				System.out.println("解压缩文件" + (entry.isDirectory() ? "夹：" : "：") + entry.getName());
				String zfilepath = entry.getName().replaceAll("\\\\", "/");
				if(zfilepath.endsWith("/"))
				{
					zfilepath = zfilepath.substring(0, zfilepath.length() - 1);
					System.out.println(zfilepath);
				}
				int zi = zfilepath.lastIndexOf("/");
				String zfilename = zi == -1 ? zfilepath : zfilepath.substring(zi + 1);
				try
				{
					int i = zfilename.lastIndexOf(".");
					if(i == 0)
					{
						continue;
					}
					// 文件名校验
					for(int j = 0; j < zfilename.length(); j++)
					{
						if(cc.indexOf(zfilename.charAt(j) + "") == -1)
						{
							continue outer;// 非法文件名
						}
					}
					if(entry.isDirectory())// 文件夹会先于文件解压缩
					{
						if(i == -1)// 文件夹不能存在小数点
						{
							File zFolder = new File(targetPath + File.separator + zfilepath);
							if(!zFolder.exists())
							{
								zFolder.mkdirs();
							}
							System.out.println("成功解压文件夹:" + zFolder.getPath());
						}
					}
					else
					{
						if(i > 1)
						{
							boolean ok = false;
							String sup = zfilename.substring(i + 1);
							for(String s : hzArr)
							{
								if(s.equals(sup))
								{
									ok = true;
									break;
								}
							}
							if(!ok)
							{
								continue;// 后缀名不匹配
							}
							File zFile = new File(targetPath + File.separator + zfilepath);
							if(zFile.getParentFile().exists())
							{
								zFile.createNewFile();
								FileOutputStream fos = new FileOutputStream(zFile);
								int bread;
								while((bread = zis.read()) != -1)
								{
									fos.write(bread);
								}
								fos.close();
								System.out.println("成功解压文件:" + zFile.getPath());
							}
							// 不存在上级目录，代表目录名不合法
						}
					}
				}
				catch(Exception e)
				{
					System.out.println("解压" + zfilepath + "失败");
					continue;
				}
			}
			zis.close();
			is.close();
			System.out.println("解压结束");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
