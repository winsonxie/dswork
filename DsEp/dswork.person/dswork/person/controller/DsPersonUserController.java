package dswork.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;
import dswork.core.util.EncryptUtil;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.person.model.DsPersonUser;
import dswork.person.service.DsPersonUserService;
import dswork.mvc.BaseController;

@Controller
@RequestMapping("/person/user")
public class DsPersonUserController extends BaseController
{
	@Autowired
	private DsPersonUserService service;

	// 添加
	@RequestMapping("/addUser1")
	public String addUser1()
	{
		return "/person/user/addUser.jsp";
	}

	@RequestMapping("/addUser2")
	public void addUser2(DsPersonUser po)
	{
		try
		{
			if(po.getAccount().length() == 0 || service.isExistsAccount(po.getAccount()) ||
					po.getIdcard().length() == 0 || service.isExistsIdcard(po.getIdcard()))
			{
				print("0:添加失败，账号已存在");
			}
			else
			{
				po.setId(UniqueId.genId());
				po.setStatus(0);
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
		return "/person/user/updUser.jsp";
	}

	@RequestMapping("/updUser2")
	public void updUser2(DsPersonUser po)
	{
		try
		{
			Long id = req().getLong("id");
			DsPersonUser user = service.get(id);
			if(user != null)
			{
				po.setStatus(user.getStatus());
				po.setPassword(user.getPassword());
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
		DsPersonUser user = service.get(id);
		put("po", user);
		return "/person/user/updUserPassword.jsp";
	}

	@RequestMapping("/updUserPassword2")
	public void updUserPassword2(DsPersonUser po)
	{
		try
		{
			String oldpassword = req().getString("oldpassword");
			DsPersonUser user = service.get(po.getId());
			if(user != null && EncryptUtil.encryptMd5(oldpassword).equals(user.getPassword()))
			{
				service.updatePassword(user.getId(), po.getPassword());
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
		PageRequest rq = getPageRequest();
		Page<DsPersonUser> pageModel = service.queryPage(rq);
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsPersonUser>(request(), pageModel));
		return "/person/user/getUser.jsp";
	}

	// 明细
	@RequestMapping("/getUserById")
	public String getUserById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/person/user/getUserById.jsp";
	}
}
