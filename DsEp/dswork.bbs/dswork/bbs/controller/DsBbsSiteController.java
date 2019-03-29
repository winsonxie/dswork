/**
 * 功能:站点Controller
 * 开发人员:白云区
 * 创建时间:2015/3/30 15:37:26
 */
package dswork.bbs.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.bbs.model.DsBbsSite;
import dswork.bbs.service.DsBbsSiteService;
import dswork.mvc.BaseController;

@Controller
@RequestMapping("/bbs/admin/site")
public class DsBbsSiteController extends BaseController
{
	@Autowired
	private DsBbsSiteService service;

	//添加
	@RequestMapping("/addSite1")
	public String addSite1()
	{
		return "/bbs/admin/site/addSite.jsp";
	}
	
	@RequestMapping("/addSite2")
	public void addSite2(DsBbsSite po)
	{
		try
		{
			po.setOwn(getOwn());
			service.save(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//删除
	@RequestMapping("/delSite")
	public void delSite()
	{
		try
		{
			Long id = req().getLong("keyIndex", 0);
			DsBbsSite po = service.get(id);
			if(po != null && checkOwn(po.getOwn()))
			{
				service.delete(id);
				print(1);
				return;
			}
			print("0:站点不存在");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping("/updSite1")
	public String updSite1()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/bbs/admin/site/updSite.jsp";
	}
	
	@RequestMapping("/updSite2")
	public void updSite2(DsBbsSite po)
	{
		try
		{
			DsBbsSite m = service.get(po.getId());
			if(m != null && checkOwn(m.getOwn()))
			{
				service.update(po);
				print(1);
				return;
			}
			print("0:站点不存在");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//获得分页
	@RequestMapping("/getSite")
	public String getSite()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("own", getOwn());
		put("list", service.queryList(map));
		return "/bbs/admin/site/getSite.jsp";
	}

	//明细
	@RequestMapping("/getSiteById")
	public String getSiteById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/bbs/admin/site/getSiteById.jsp";
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
