package dswork.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.common.model.DsCommonFunc;
import dswork.common.model.DsCommonRole;
import dswork.common.model.DsCommonRoleFunc;
import dswork.common.model.DsCommonSystem;
import dswork.common.service.DsCommonRoleChooseService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;

//角色
@Scope("prototype")
@Controller
@RequestMapping("/ds/common/rolechoose")
public class DsCommonRoleChooseController extends BaseController
{
	@Autowired
	private DsCommonRoleChooseService service;

	// 获得分页
	@RequestMapping("/getRoleChoose")
	public String getChoose()
	{
		Page<DsCommonSystem> pageModel = service.querySystemPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonSystem>(request, pageModel));
		long systemid = req.getLong("systemid", -100000000L);//随便给个不会用的参数
		if(systemid != -100000000L)
		{
			put("systemid", systemid);
		}
		return "/ds/common/rolechoose/getRoleChoose.jsp";
	}

	// 树形管理
	@RequestMapping("/getRoleTree")
	public String getRoleTree()
	{
		long systemid = req.getLong("systemid", -100000000L);//随便给个不会用的参数
		if(systemid != -100000000L)
		{
			put("po", service.getSystem(systemid));
		}
		return "/ds/common/rolechoose/getRoleTree.jsp";
	}
	// 获得树形管理时的json数据
	@RequestMapping("/getRoleJson")
	public void getRoleJson()
	{
		long systemid = req.getLong("systemid");
		long pid = req.getLong("pid");
		print(service.queryRoleList(systemid, pid));
	}

	// 获得功能和被分配到角色的功能
	@RequestMapping("/getRoleById")
	public String getRoleById()
	{
		Long roleid = req.getLong("roleid");
		DsCommonRole po = service.get(roleid);
		List<DsCommonFunc> list = service.queryFuncList(po.getSystemid());
		if(null != list && 0 < list.size())
		{
			Map<Long, DsCommonFunc> m = new HashMap<Long, DsCommonFunc>();
			for(DsCommonFunc i : list)
			{
				m.put(i.getId(), i);
			}
			if(0 < roleid)
			{
				List<DsCommonRoleFunc> flist = service.queryFuncListByRoleid(roleid);
				for(DsCommonRoleFunc i : flist)
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
		return "/ds/common/rolechoose/getRoleById.jsp";
	}
}
