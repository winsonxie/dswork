package dswork.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.DsCommonSystem;
import dswork.common.service.DsCommonSystemService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

//应用系统
@Controller
@RequestMapping("/ds/common/system")
public class DsCommonSystemController extends BaseController
{
	@Autowired
	private DsCommonSystemService service;

	// 添加
	@RequestMapping("/addSystem1")
	public String addSystem1()
	{
		return "/ds/common/system/addSystem.jsp";
	}
	@RequestMapping("/addSystem2")
	public void addSystem2(DsCommonSystem po)
	{
		try
		{
			if (po.getAlias().length() <= 0)
			{
				print("0:添加失败，标识不能为空");
			}
			else
			{
				if (!service.isExistsByAlias(po.getAlias()))
				{
					po.setId(UniqueId.genUniqueId());
					po.setStatus(0);// 默认禁用系统
					service.save(po);
					print(1);
				}
				else
				{
					print("0:添加失败，标识已存在");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delSystem")
	public void delSystem()
	{
		try
		{
			long systemid = req().getLong("keyIndex", 0);
			int funcCount = service.getCountFuncBySystemid(systemid);
			int roleCount = service.getCountRoleBySystemid(systemid);
			if (0 >= funcCount && 0 >= roleCount)
			{
				service.delete(systemid);
				print(1);
			}
			else
			{
				String msg = "操作失败，以下内容必须先清除：";
				int c = 0;
				if (0 < funcCount)
				{
					msg += "系统功能";
					c++;
				}
				if (0 < roleCount)
				{
					msg += ((c == 1)?"、系统角色":"系统角色");
				}
				print("0:" + msg);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updSystem1")
	public String updSystem()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/common/system/updSystem.jsp";
	}
	@RequestMapping("/updSystem2")
	public void updSystem2(DsCommonSystem po)
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

	// 修改状态
	@RequestMapping("/updSystemStatus")
	public void updSystemStatus()
	{
		long id = req().getLong("keyIndex");
		int status = req().getInt("status", -1);
		try
		{
			if (status == 0 || status == 1)
			{
				service.updateStatus(id, status);
				print(1);
			}
			else
			{
				print("0:参数错误");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getSystem")
	public String getSystem()
	{
		Page<DsCommonSystem> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonSystem>(request(), pageModel));
		return "/ds/common/system/getSystem.jsp";
	}
	
	// 明细
	@RequestMapping("/getSystemById")
	public String getSystemById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/common/system/getSystemById.jsp";
	}
	
	// 排序
	@RequestMapping("/updSystemSeq1")
	public String updSystemSeq1()
	{
		Map<String,Object> map = new HashMap<String,Object>();
		List<DsCommonSystem> list = service.queryList(map);
		put("list", list);
		return "/ds/common/system/updSystemSeq.jsp";
	}
	
	@RequestMapping("/updSystemSeq2")
	public void updSystemSeq2()
	{
		Long[] ids = CollectionUtil.toLongArray(req().getLongArray("keyIndex", 0));
		try
		{
			if(ids.length > 0)
			{
				service.updateSeq(ids);
				print(1);
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
}
