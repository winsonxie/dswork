package dswork.base.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseUser;
import dswork.base.service.DsBaseExUserOrgService;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/ds/base/ex/userorg")
public class DsBaseExUserOrgController extends BaseController
{
	@Autowired
	private DsBaseExUserOrgService service;

	// 树形管理
	@RequestMapping("/getOrgTree")
	public String getOrgTree()
	{
		Long rootid = getLoginUser().getOrgpid();// 作为限制根节点显示
		DsBaseOrg po = null;
		if(rootid > 0)
		{
			po = service.getOrg(rootid);
			if(null == po)
			{
				return null;// 没有此根节点
			}
			if(po.getStatus() == 0)
			{
				return null;// 不能以岗位作为根节点
			}
		}
		else
		{
			po = new DsBaseOrg();
			po.setName("组织机构");
		}
		put("po", po);
		return "/ds/base/ex/userorg/getOrgTree.jsp";
	}

	@RequestMapping("/getUserOrg")
	public String getUserOrg()
	{
		Long pid = req.getLong("pid");
		put("pid", pid);
		put("orgList", service.queryOrgList(pid));
		put("userList", service.queryUserList(pid));
		return "/ds/base/ex/userorg/getUserOrg.jsp";
	}

	@RequestMapping("/updSetUser1")
	public String updSetUser1()
	{
		Long id = req.getLong("id");
		put("list", service.queryListByUserid(id));
		return "/ds/base/ex/userorg/updSetUser.jsp";
	}
	@RequestMapping("/updSetUser2")
	public void updSetUser2()
	{
		try
		{
			Long userid = req.getLong("userid");
			String ids = req.getString("orgids", "");
			List<Long> list = new ArrayList<Long>();
			if(ids.length() > 0)
			{
				for(String tmp : ids.split(","))
				{
					list.add(new Long(tmp));
				}
			}
			service.saveByUser(userid, list);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/updSetOrg1")
	public String updSetOrg1()
	{
		Long id = req.getLong("id");
		put("list", service.queryListByOrgid(id));
		return "/ds/base/ex/userorg/updSetOrg.jsp";
	}
	@RequestMapping("/updSetOrg2")
	public void updSetOrg2()
	{
		try
		{
			Long orgid = req.getLong("orgid");
			if(orgid > 0)
			{
				String ids = req.getString("userids", "");
				List<Long> list = new ArrayList<Long>();
				if(ids.length() > 0)
				{
					for(String tmp : ids.split(","))
					{
						list.add(new Long(tmp));
					}
				}
				service.saveByOrg(orgid, list);
				print(1);
			}
			else
			{
				print(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	private DsBaseUser getLoginUser()
	{
		String account = dswork.sso.WebFilter.getAccount(session);
		return service.getUserByAccount(account);
	}

	@RequestMapping("/getUserById")
	public String getUserById()
	{
		Long id = req.getLong("id");
		put("po", service.getUser(id));
		put("list", service.queryOrgListByUserid(id));
		return "/ds/base/ex/userorg/getUserById.jsp";
	}

	@RequestMapping("/getOrgById")
	public String getOrgById()
	{
		Long id = req.getLong("id");
		put("po", service.getOrg(id));
		put("list", service.queryUserListByOrgid(id));
		return "/ds/base/ex/userorg/getOrgById.jsp";
	}

	// 授权
	@RequestMapping("/updOrgRole1")
	public String updOrgRole1()
	{
		Long id = req.getLong("keyIndex");
		DsBaseOrg po = service.getOrg(id);
		if(null == po)
		{
			return null;// 非法访问，否则肯定存在id
		}
		if(0 == po.getStatus())// 岗位才可以授权
		{
			put("po", po);
			put("list", service.queryOrgRoleList(id));
			return "/ds/base/ex/userorg/updOrgRole.jsp";
		}
		return null;
	}

	@RequestMapping("/updOrgRole2")
	public void updOrgRole2()
	{
		try
		{
			Long id = req.getLong("orgid");
			DsBaseOrg po = service.getOrg(id);
			if(null == po)
			{
				print(0);
				return;// 非法访问，否则肯定存在id
			}
			if(0 == po.getStatus())// 岗位才可以授权
			{
				String ids = req.getString("roleids", "");
				List<Long> list = new ArrayList<Long>();
				if(ids.length() > 0)
				{
					for(String tmp : ids.split(","))
					{
						list.add(new Long(tmp));
					}
				}
				service.saveOrgRole(id, list);
				print(1);
			}
			else
			{
				print(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
}
