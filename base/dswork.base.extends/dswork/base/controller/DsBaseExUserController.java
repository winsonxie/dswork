package dswork.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.base.model.DsBaseOrg;
import dswork.base.model.DsBaseUser;
import dswork.base.model.DsBaseUsertype;
import dswork.base.service.DsBaseExUserService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.EncryptUtil;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

@Controller
@RequestMapping("/ds/base/ex/user")
public class DsBaseExUserController extends BaseController
{
	@Autowired
	private DsBaseExUserService service;

	// 树形管理
	@RequestMapping("/getOrgTree")
	public String getOrgTree()
	{
		long rootid = getLoginUser().getOrgpid();
		DsBaseOrg po = null;
		if(rootid > 0)
		{
			po = service.getOrg(rootid);
			if(po == null)
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
			po.setStatus(2);
		}
		put("po", po);
		return "/ds/base/ex/user/getOrgTree.jsp";
	}

	// 添加
	@RequestMapping("/addUser1")
	public String addUser1()
	{
		String xtype = req().getString("xtype", "");
		if(xtype == null || xtype.length() == 0)
		{
			xtype = null;
		}
		List<DsBaseUsertype> list = service.queryListForUsertype(xtype);
		if(list.size() == 0)
		{
			return null;
		}
		DsBaseUser user = getLoginUser();
		put("orgpid", user.getOrgpid());
		put("orgpname", user.getOrgpname());
		put("typeList", list);
		return "/ds/base/ex/user/addUser.jsp";
	}
	@RequestMapping("/addUser2")
	public void addUser2(DsBaseUser po)
	{
		if(po.getOrgpid() == null)
		{
			print("0:所属单位为空！");
			return;
		}
		if(!checkOrgid(po.getOrgpid()))
		{
			print("0:您无权将用户添加至该单位！");
			return;
		}
		try
		{
			if(po.getAccount().length() <= 0 || "null".equals(po.getAccount()))
			{
				print("0:添加失败，账号不能为空");
			}
			else
			{
				if(!"root".equals(po.getAccount()) && !"admin".equals(po.getAccount()) && !service.isExistsByAccount(po.getAccount()))
				{
					po.setId(UniqueId.genUniqueId());
					po.setCreatetime(TimeUtil.getCurrentTime());
					po.setStatus(0);//默认禁用用户
					po.setPassword(EncryptUtil.encryptMd5(po.getPassword()));
					service.save(po);
					print(1);
				}
				else
				{
					print("0:添加失败，该账号已存在");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delUser")
	public void delUser()
	{
		try
		{
			long[] ids = req().getLongArray("keyIndex", 0);
			for(long id : ids)
			{
				DsBaseUser po = service.get(id);
				if(po.getOrgpid() == null)
				{
					print("0:有用户所属单位为空！");
					return;
				}
				if(!checkOrgid(po.getOrgpid()))
				{
					print("0:您没有删除该用户的权限！");
					return;
				}
			}

			for(long id : ids)
			{
				if(id <= 0)
				{
					continue;
				}
				service.delete(id);
			}
			print("1");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updUser1")
	public String updUser1()
	{
		Long id = req().getLong("keyIndex");
		DsBaseUser po = service.get(id);
		if(!checkOrgid(po.getOrgpid()))
		{
			return null;
		}
		put("po", po);
		String xtype = req().getString("xtype", "");
		if(xtype == null || xtype.length() == 0)
		{
			xtype = null;
		}
		List<DsBaseUsertype> list = service.queryListForUsertype(xtype);
		if(list.size() == 0)
		{
			return null;
		}
		put("typeList", list);
		put("page", req().getInt("page", 1));
		return "/ds/base/ex/user/updUser.jsp";
	}

	@RequestMapping("/updUser2")
	public void updUser2(DsBaseUser po)
	{
		if(po.getOrgpid() == null)
		{
			print("0:用户所属单位为空！");
			return;
		}
		DsBaseUser u = service.get(po.getId());
		if(!checkOrgid(u.getOrgpid()))
		{
			print("0:您没有操作该用户的权限！");
			return;
		}
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

	// 修改用户状态
	@RequestMapping("/updUserStatus")
	public void updUserStatus()
	{
		long id = req().getLong("keyIndex");
		DsBaseUser po = service.get(id);
		if(!checkOrgid(po.getOrgpid()))
		{
			print("0:您没有操作该用户的权限！");
			return;
		}
		int status = req().getInt("status", -1);
		try
		{
			if(status == 0 || status == 1)
			{
				if(id <= 0)
				{
					print("0:此用户无法更改状态");
				}
				else
				{
					service.updateStatus(id, status);
					print(1);
				}
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

	@RequestMapping("/updUserOrg1")
	public String updUserOrg1()
	{
		Long id = req().getLong("keyIndex");
		if(id > 0 || id == -1)
		{
			DsBaseUser po = service.get(id);
			if(!checkOrgid(po.getOrgpid()))
			{
				return null;
			}
			DsBaseUser user = getLoginUser();
			put("orgpid", user.getOrgpid());
			put("orgpname", user.getOrgpname());
			put("po", po);
			put("page", req().getInt("page", 1));
			return "/ds/base/ex/user/updUserOrg.jsp";
		}
		return null;
	}
	@RequestMapping("/updUserOrg2")
	public void updUserOrg2()
	{
		DsBaseUser po = service.get(req().getLong("id"));
		if(!checkOrgid(po.getOrgpid()))
		{
			print("0:您无权修改的该用户的权限！");
			return;
		}
		try
		{
			long id = req().getLong("id");
			long orgpid = req().getLong("orgpid");
			long orgid = req().getLong("orgid");
			if(!checkOrgid(orgpid))
			{
				print("0:您无权将用户调动至该单位！");
				return;
			}
			service.updateOrg(id, orgpid, orgid);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	@RequestMapping("/updUserPassword1")
	public String updUserPassword1()
	{
		Long id = req().getLong("keyIndex");
		if(id > 0)
		{
			DsBaseUser po = service.get(id);
			if(!checkOrgid(po.getOrgpid()))
			{
				return null;
			}
			put("po", po);
			put("page", req().getInt("page", 1));
			return "/ds/base/ex/user/updUserPassword.jsp";
		}
		return null;
	}
	@RequestMapping("/updUserPassword2")
	public void updUserPassword2()
	{
		DsBaseUser po = service.get(req().getLong("id"));
		if(!checkOrgid(po.getOrgpid()))
		{
			print("0:您没有操作该用户的权限！");
			return;
		}
		try
		{
			long id = req().getLong("id");
			String password = req().getString("password");
			service.updatePassword(id, password);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getUser")
	public String getUser()
	{
		Page<DsBaseUser> pageModel;
		if(req().getLong("orgid") > 0)
		{
			pageModel = service.queryPage(getPageRequest());// 部门下的用户
		}
		else
		{
			pageModel = service.queryPageByOrgpid(getPageRequest(), req().getLong("orgpid"));// 单位下的用户
		}
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseUser>(request(), pageModel));
		String xtype = req().getString("xtype", "");
		if(xtype == null || xtype.length() == 0)
		{
			xtype = null;
		}
		List<DsBaseUsertype> list = service.queryListForUsertype(xtype);
		if(list.size() == 0)
		{
			return null;
		}
		put("typeList", list);
		return "/ds/base/ex/user/getUser.jsp";
	}

	// 明细
	@RequestMapping("/getUserById")
	public String getUserById()
	{
		Long id = req().getLong("keyIndex");
		DsBaseUser po = service.get(id);
		if(!checkOrgid(po.getOrgpid()))
		{
			return null;
		}
		put("po", po);
		return "/ds/base/ex/user/getUserById.jsp";
	}

	private DsBaseUser getLoginUser()
	{
		String account = dswork.sso.WebFilter.getAccount(session());
		return service.getByAccount(account);
	}

	private boolean checkOrgid(Long orgid)
	{
		Long orgpid = getLoginUser().getOrgpid();
		for(int i = 0; i < 100; i++)// 防止死循环，检查层级最多一百层
		{
			if(orgpid == null || orgpid == 0 || orgpid.equals(orgid))
			{
				return true;
			}
			if(orgid == null || orgid == 0)
			{
				return false;
			}
			orgid = service.getOrg(orgid).getPid();
		}
		return false;
	}
}
