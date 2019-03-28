package dswork.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.base.model.DsBaseFunc;
import dswork.base.model.DsBaseRole;
import dswork.base.model.DsBaseRoleFunc;
import dswork.base.model.DsBaseSystem;
import dswork.base.service.DsBaseRoleChooseService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;

//角色
@Controller
@RequestMapping("/ds/base/rolechoose")
public class DsBaseRoleChooseController extends BaseController
{
	@Autowired
	private DsBaseRoleChooseService service;

	// 获得分页
	@RequestMapping("/getRoleChoose")
	public String getChoose()
	{
		Page<DsBaseSystem> pageModel = service.querySystemPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseSystem>(request(), pageModel));
		long systemid = req().getLong("systemid", -100000000L);//随便给个不会用的参数
		if(systemid != -100000000L)
		{
			put("systemid", systemid);
		}
		return "/ds/base/rolechoose/getRoleChoose.jsp";
	}

	// 树形管理
	@RequestMapping("/getRoleTree")
	public String getRoleTree()
	{
		long systemid = req().getLong("systemid", -100000000L);//随便给个不会用的参数
		if(systemid != -100000000L)
		{
			put("po", service.getSystem(systemid));
		}
		return "/ds/base/rolechoose/getRoleTree.jsp";
	}
	// 获得树形管理时的json数据
	@RequestMapping("/getRoleJson")
	public void getRoleJson()
	{
		long systemid = req().getLong("systemid");
		long pid = req().getLong("pid");
		print(service.queryRoleList(systemid, pid));
	}

	// 获得功能和被分配到角色的功能
	@RequestMapping("/getRoleById")
	public String getRoleById()
	{
		Long roleid = req().getLong("roleid");
		DsBaseRole po = service.get(roleid);
		List<DsBaseFunc> list = service.queryFuncList(po.getSystemid());
		if(null != list && 0 < list.size())
		{
			Map<Long, DsBaseFunc> m = new HashMap<Long, DsBaseFunc>();
			for(DsBaseFunc i : list)
			{
				m.put(i.getId(), i);
			}
			if(0 < roleid)
			{
				List<DsBaseRoleFunc> flist = service.queryFuncListByRoleid(roleid);
				for(DsBaseRoleFunc i : flist)
				{
					m.get(i.getFuncid()).setChecked(true);
				}
			}
			put("list", list);
		}
		else
		{
			put("list", "[]");
		}
		put("sys", service.getSystem(po.getSystemid()));
		//put("po", po);
		return "/ds/base/rolechoose/getRoleById.jsp";
	}
}
