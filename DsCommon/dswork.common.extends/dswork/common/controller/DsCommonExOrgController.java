package dswork.common.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.common.model.DsCommonOrg;
import dswork.common.model.DsCommonUser;
import dswork.common.service.DsCommonExOrgService;
import dswork.core.util.CollectionUtil;

@Controller
@RequestMapping("/ds/common/ex/org")
public class DsCommonExOrgController extends BaseController
{
	@Autowired
	private DsCommonExOrgService service;

	// 添加
	@RequestMapping("/addOrg1")
	public String addOrg1()
	{
		long pid = req().getLong("pid");
		DsCommonOrg parent = null;
		if(pid > 0)
		{
			parent = service.get(pid);
			if(parent == null || parent.getStatus() == 0)// 岗位不能添加子节点
			{
				return null;
			}
		}
		else
		{
			parent = new DsCommonOrg();
		}
		put("parent", parent);
		put("pid", pid);
		return "/ds/common/ex/org/addOrg.jsp";
	}

	@RequestMapping("/addOrg2")
	public void addOrg2(DsCommonOrg po)
	{
		try
		{
			if(po.getName().length() == 0)
			{
				print("0:名称不能为空");
				return;
			}
			if(0 < po.getPid().longValue())// 存在上级节点时
			{
				DsCommonOrg parent = service.get(po.getPid());
				if(null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
				if(0 == parent.getStatus())// 上级是岗位
				{
					print("0:岗位无法添加下级");
					return;
				}
				if(1 == parent.getStatus() && po.getStatus() == 2)// 上级是部门
				{
					print("0:部门无法设置下级单位");
					return;
				}
				if(2 == parent.getStatus() && po.getStatus() == 0)// 上级不是部门
				{
					print("0:单位无法设置岗位");
					return;
				}
			}
			else
			{
				po.setStatus(2);// 没有上级时必须为单位
			}
			service.save(po);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delOrg")
	public void delOrg()
	{
		try
		{
			int v = 0;
			long[] ids = req().getLongArray("keyIndex", 0);
			for(long id : ids)
			{
				if(id <= 0)
				{
					continue;
				}
				int count = service.getCountByPid(id, null);
				if(0 >= count)
				{
					service.delete(id);
				}
				else
				{
					v++;// print("0:下级节点不为空，不能删除");
				}
			}
			print("1" + (v > 0 ? ":" + v + "个不能删除，下级节点不为空" : ""));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:下级节点或用户不为空");
		}
	}

	// 修改
	@RequestMapping("/updOrg1")
	public String updOrg1()
	{
		Long id = req().getLong("keyIndex");
		DsCommonOrg po = service.get(id);
		if(null == po)
		{
			return null;// 非法访问，否则肯定存在id
		}
		DsCommonOrg parent = null;
		if(0 < po.getPid())
		{
			parent = service.get(po.getPid());
			if(null == parent)
			{
				return null;// 非法数据，否则肯定存在parent
			}
		}
		else
		{
			parent = new DsCommonOrg();
		}
		put("po", po);
		put("parent", parent);
		return "/ds/common/ex/org/updOrg.jsp";
	}

	@RequestMapping("/updOrg2")
	public void updOrg2(DsCommonOrg po)
	{
		try
		{
			if(0 >= po.getName().length())
			{
				print("0:名称不能为空");
				return;
			}
			DsCommonOrg old = service.get(po.getId());
			if(null == old)
			{
				print("0:操作失败，请刷新重试");
				return;
			}
			if(po.getStatus() != 0 && old.getStatus() == 0)
			{
				po.setStatus(0);// 岗位类型不能被更改
			}
			if(old.getPid() <= 0)
			{
				po.setStatus(2);// 没有上级则为单位
			}
			else
			// 存在上级节点时
			{
				DsCommonOrg parent = service.get(old.getPid());
				if(null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
				if(1 == parent.getStatus() && po.getStatus() == 2)// 上级是部门
				{
					print("0:部门无法设置下级单位");
					return;
				}
			}
			if(old.getStatus() == 2 && po.getStatus() == 1)// 降级
			{
				int count = service.getCountByPid(po.getId(), 2);// 下级不能有单位
				if(0 < count)
				{
					print("0:下级存在单位，不能降级为部门");
					return;
				}
			}
			else if(old.getStatus() == 1 && po.getStatus() == 2)// 升级
			{
				int count = service.getCountByPid(po.getId(), 0);// 下级不能有岗位
				if(0 < count)
				{
					print("0:下级存在岗位，不能升级为单位");
					return;
				}
			}
			service.update(po);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 排序
	@RequestMapping("/updOrgSeq1")
	public String updOrgSeq1()
	{
		long pid = req().getLong("pid");
		List<DsCommonOrg> list = service.queryList(pid);
		put("list", list);
		return "/ds/common/ex/org/updOrgSeq.jsp";
	}

	@RequestMapping("/updOrgSeq2")
	public void updOrgSeq2()
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

	// 移动
	@RequestMapping("/updOrgMove1")
	public String updOrgMove1()
	{
		Long rootid = getLoginUser().getOrgpid();// 作为限制根节点显示
		put("po", (rootid > 0) ? service.get(rootid) : null);
		put("rootid", rootid);
		return "/ds/common/ex/org/updOrgMove.jsp";
	}

	@RequestMapping("/updOrgMove2")
	public void updOrgMove2()
	{
		long pid = req().getLong("pid");
		if(pid <= 0)
		{
			pid = 0;
		}
		else
		{
			DsCommonOrg m = service.get(pid);
			if(m == null)
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的系统
				return;
			}
			if(m.getStatus() == 0)
			{
				print("0:不能移动到岗位节点");// 移动的节点不存在，或者不在相同的系统
				return;
			}
		}
		Long[] ids = getLongArray(req().getString("ids"));
		try
		{
			if(ids.length > 0)
			{
				service.updatePid(ids, pid);
				print(1);
			}
			else
			{
				print("0:没有需要移动的节点");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 树形管理
	@RequestMapping("/getOrgTree")
	public String getOrgTree()
	{
		long rootid = getLoginUser().getOrgpid();
		DsCommonOrg po = null;
		if(rootid > 0)
		{
			po = service.get(rootid);
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
			po = new DsCommonOrg();
		}
		put("po", po);
		return "/ds/common/ex/org/getOrgTree.jsp";
	}

	// 获得列表
	@RequestMapping("/getOrg")
	public String getOrg()
	{
		Long rootid = getLoginUser().getOrgpid();// 作为限制根节点显示
		Long pid = req().getLong("pid");
		List<DsCommonOrg> list = service.queryList(pid);
		put("list", list);
		put("rootid", rootid);
		put("pid", pid);
		return "/ds/common/ex/org/getOrg.jsp";
	}

	// 获得树形管理时的json数据
	@RequestMapping("/getOrgJson")
	// ByPid
	public void getOrgJson()
	{
		long pid = req().getLong("pid");
		print(service.queryList(pid));
	}

	// 明细
	@RequestMapping("/getOrgById")
	public String getOrgById()
	{
		Long id = req().getLong("keyIndex");
		DsCommonOrg po = service.get(id);
		put("po", po);
		return "/ds/common/ex/org/getOrgById.jsp";
	}

	private Long[] getLongArray(String value)
	{
		try
		{
			String[] _v = value.split(",");
			if(_v != null && _v.length > 0)
			{
				Long[] _numArr = new Long[_v.length];
				for(int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Long.parseLong(_v[i].trim());
					}
					catch(NumberFormatException e)
					{
						_numArr[i] = 0L;
					}
				}
				return _numArr;
			}
		}
		catch(Exception e)
		{
		}
		return new Long[0];
	}

	private DsCommonUser getLoginUser()
	{
		String account = dswork.sso.WebFilter.getAccount(session());
		return service.getUserByAccount(account);
	}
}
