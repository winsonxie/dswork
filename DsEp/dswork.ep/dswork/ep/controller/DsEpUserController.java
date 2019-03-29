package dswork.ep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import common.auth.Auth;
import common.auth.AuthUtil;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import dswork.core.util.EncryptUtil;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.ep.model.DsEpUser;
import dswork.ep.service.DsEpUserService;
import dswork.mvc.BaseController;

@Controller
@RequestMapping("/ep/user")
public class DsEpUserController extends BaseController
{
	@Autowired
	private DsEpUserService service;

	// 添加
	@RequestMapping("/addUser1")
	public String addUser1()
	{
		String qybm = req().getString("qybm");
		put("qybm", qybm);
		return "/ep/user/addUser.jsp";
	}

	@RequestMapping("/addUser2")
	public void addUser2(DsEpUser po)
	{
		try
		{
			if(po.getAccount().length() == 0 || service.isExists(po.getAccount()))
			{
				print("0:添加失败，账号已存在");
			}
			else
			{
				Auth user = AuthUtil.getLoginUser(request());
				po.setId(UniqueId.genId());
				po.setQybm(user.getQybm());
				po.setUsertype(0);// 非企业管理员
				po.setStatus(1);// 正常状态
				po.setCreatetime(TimeUtil.getCurrentTime());
				service.save(po);
				print(1);
			}
		}
		catch(Exception e)
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
			service.deleteBatch(CollectionUtil.toLongArray(req().getLongArray("keyIndex", 0)));
			print(1);
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
		put("po", service.get(id));
		put("page", req().getInt("page", 1));
		return "/ep/user/updUser.jsp";
	}

	@RequestMapping("/updUser2")
	public void updUser2(DsEpUser po)
	{
		try
		{
			Long id = req().getLong("id");
			DsEpUser user = service.get(id);
			if(user != null && checkUser(user.getId()))
			{
				po.setUsertype(user.getUsertype());// 用户类型不可改，虽然xml没有配置sql也照写
				po.setPassword(user.getPassword());
				po.setQybm(user.getQybm());
				service.update(po);
				print(1);
			}
			else
			{
				print("0");
			}
		}
		catch(Exception e)
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

	@RequestMapping("/updUserPassword1")
	public String updUserPwd1()
	{
		Long id = req().getLong("keyIndex");
		DsEpUser user = service.get(id);
		put("po", user);
		return "/ep/user/updUserPassword.jsp";
	}

	@RequestMapping("/updUserPassword2")
	public void updUserPassword2(DsEpUser po)
	{
		try
		{
			String oldpassword = req().getString("oldpassword");
			DsEpUser user = service.get(po.getId());
			if(user != null && EncryptUtil.encryptMd5(oldpassword).equals(user.getPassword()) && checkUser(user.getId()))
			{
				service.updatePassword(user.getId(), 0, po.getPassword());// 此模块只能改非企业管理员
				print(1);
			}
			else
			{
				print("0");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getUser")
	public String getUser()
	{
		Auth user = AuthUtil.getLoginUser(request());
		PageRequest rq = getPageRequest();
		rq.getFilters().put("qybm", user.getQybm());
		Page<DsEpUser> pageModel = service.queryPage(rq);
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsEpUser>(request(), pageModel));
		return "/ep/user/getUser.jsp";
	}

	// 明细
	@RequestMapping("/getUserById")
	public String getUserById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/ep/user/getUserById.jsp";
	}

	private boolean checkUser(Long userid)
	{
		try
		{
			return service.get(userid).getQybm().equals(common.auth.AuthUtil.getLoginUser(request()).getQybm());
		}
		catch(Exception ex)
		{
		}
		return false;
	}
}
