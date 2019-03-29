package dswork.ep.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.ep.model.DsEpEnterprise;
import dswork.ep.model.DsEpUser;
import dswork.ep.service.DsEpEnterpriseService;
import dswork.mvc.BaseController;

@Controller
@RequestMapping("/ep/enterprise")
public class DsEpEnterpriseController extends BaseController
{
	@Autowired
	private DsEpEnterpriseService service;

	// 添加
	@RequestMapping("/addEnterprise1")
	public String addEnterprise1()
	{
		return "/ep/enterprise/addEnterprise.jsp";
	}

	@RequestMapping("/addEnterprise2")
	public void addEnterprise2(DsEpEnterprise ent, DsEpUser user)
	{
		try
		{
			if(ent.getQybm().length() == 0 || service.isExists(ent.getQybm()))
			{
				print("0:添加失败，企业编码已存在");
			}
			else if(user.getAccount().length() == 0 || service.isExistsUser(user.getAccount()))
			{
				print("0:添加失败，账号已存在");
			}
			else
			{
				ent.setId(UniqueId.genId());
				user.setId(ent.getId());
				
				user.setName(req().getString("username"));
				user.setQybm(ent.getQybm());
				user.setUsertype(1);// 企业管理员
				user.setStatus(1);// 正常状态
				user.setCreatetime(TimeUtil.getCurrentTime());
				service.save(ent, user);
				print(1);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updEnterprise1")
	public String updEnterprise1()
	{
		Long id = req().getLong("keyIndex");
		DsEpEnterprise po = service.get(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qybm", po.getQybm());
		map.put("usertype", 1); // 1代表管理员
		List<DsEpUser> userlist = service.queryListUser(map);
		if(userlist.size() > 0)
		{
			put("po", po);
			put("admin", userlist.get(0));
			put("page", req().getInt("page", 1));
			return "/ep/enterprise/updEnterprise.jsp";
		}
		return null;
	}

	@RequestMapping("/updEnterprise2")
	public void updEnterprise2(DsEpEnterprise po, DsEpUser user)
	{
		try
		{
			user.setId(req().getLong("userid"));
			user.setName(req().getString("username"));
			service.update(po, user);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getEnterprise")
	public String getEnterprise()
	{
		Page<DsEpEnterprise> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsEpEnterprise>(request(), pageModel));
		return "/ep/enterprise/getEnterprise.jsp";
	}

	// 明细
	@RequestMapping("/getEnterpriseById")
	public String getEnterpriseById()
	{
		Long id = req().getLong("keyIndex");
		DsEpEnterprise po = service.get(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qybm", po.getQybm());
		map.put("usertype", 1); // 1代表管理员
		List<DsEpUser> userlist = service.queryListUser(map);
		if(userlist.size() > 0)
		{
			put("po", po);
			put("admin", userlist.get(0));
			return "/ep/enterprise/getEnterpriseById.jsp";
		}
		return null;
	}

	@RequestMapping("/updPassword1")
	public String updPassword1()
	{
		Long id = req().getLong("keyIndex");
		String qybm = service.get(id).getQybm();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("usertype", 1); // 1代表管理员
		map.put("qybm", qybm);
		List<DsEpUser> userlist = service.queryListUser(map);
		if(userlist.size() > 0)
		{
			put("po", userlist.get(0));
			return "/ep/enterprise/updPassword.jsp";
		}
		else
		{
			return null;
		}
	}

	@RequestMapping("/updPassword2")
	public void updPassword2(DsEpUser po)
	{
		try
		{
			service.updatePassword(po.getId(), 1, po.getPassword());
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
}
