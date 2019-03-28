package dswork.base.controller;

import java.util.ArrayList;
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
import dswork.base.service.DsBaseRoleService;
import dswork.core.util.CollectionUtil;

//角色
@Controller
@RequestMapping("/ds/base/role")
public class DsBaseRoleController extends BaseController
{
	@Autowired
	private DsBaseRoleService service;

	// 添加
	@RequestMapping("/addRole1")
	public String addRole1()
	{
		Long systemid = req().getLong("systemid");
		long pid = req().getLong("pid");
		DsBaseRole parent = null;
		if(pid > 0)
		{
			parent = service.get(pid);
		}
		else
		{
			parent = new DsBaseRole();
		}
		put("parent", parent);
		put("systemid", systemid);
		put("pid", req().getLong("pid"));
		return "/ds/base/role/addRole.jsp";
	}
	@RequestMapping("/addRole2")
	public void addRole2(DsBaseRole po)
	{
		try
		{
			if(po.getName().length() == 0)
			{
				print("0:名称不能为空");
				return;
			}
			if(0 < po.getPid())
			{
				DsBaseRole parent = service.get(po.getPid());
				if(null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
			}
			int refresh = req().getInt("refresh", 0);
			List<DsBaseRoleFunc> list = null;
			if(refresh == 1)// 需要修改功能权限
			{
				Long[] funcids = getLongArray(req().getString("funcids", "").trim());
				if(funcids.length > 0)
				{
					list = new ArrayList<DsBaseRoleFunc>();
					for(int i = 0; i < funcids.length; i++)
					{
						DsBaseRoleFunc m = new DsBaseRoleFunc();
						m.setFuncid(funcids[i]);
						if(m.getFuncid() > 0)
						{
							list.add(m);
						}
					}
				}
			}
			service.save(po, list);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 删除
	@RequestMapping("/delRole")
	public void delRole()
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
				int count = service.getCountByPid(id);
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
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updRole1")
	public String updRole1()
	{
		Long id = req().getLong("keyIndex");
		DsBaseRole po = service.get(id);
		if(null == po)
		{
			return null;// 非法访问，否则肯定存在id
		}
		DsBaseRole parent = null;
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
			parent = new DsBaseRole();
		}
		put("po", po);
		put("parent", parent);
		return "/ds/base/role/updRole.jsp";
	}
	@RequestMapping("/updRole2")
	public void updRole2(DsBaseRole po)
	{
		try
		{
			if(0 >= po.getName().length())
			{
				print("0:名称不能为空");
				return;
			}
			DsBaseRole _po = service.get(po.getId());
			if(null == _po)
			{
				print("0:操作失败，请刷新重试");
				return;
			}
			int refresh = req().getInt("refresh", 0);
			List<DsBaseRoleFunc> list = null;
			if(refresh == 1)// 需要修改功能权限
			{
				Long[] funcids = getLongArray(req().getString("funcids", "").trim());
				if(funcids.length > 0)
				{
					list = new ArrayList<DsBaseRoleFunc>();
					for(int i = 0; i < funcids.length; i++)
					{
						DsBaseRoleFunc m = new DsBaseRoleFunc();
						m.setFuncid(funcids[i]);
						if(m.getFuncid() > 0)
						{
							list.add(m);
						}
					}
				}
			}
			service.update(po, list);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 排序
	@RequestMapping("/updRoleSeq1")
	public String updRoleSeq1()
	{
		long systemid = req().getLong("systemid");
		long pid = req().getLong("pid");
		List<DsBaseRole> list = service.queryList(systemid, pid);
		put("list", list);
		return "/ds/base/role/updRoleSeq.jsp";
	}
	@RequestMapping("/updRoleSeq2")
	public void updRoleSeq2()
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
	@RequestMapping("/updRoleMove1")
	public String updRoleMove1()
	{
		DsBaseSystem po = service.getSystem(req().getLong("systemid"));
		put("po", po);
		return "/ds/base/role/updRoleMove.jsp";
	}
	@RequestMapping("/updRoleMove2")
	public void updRoleMove2()
	{
		long systemid = req().getLong("systemid");
		long pid = req().getLong("pid");
		if(pid <= 0)
		{
			pid = 0;
		}
		else
		{
			DsBaseRole m = service.get(pid);
			if(m == null || m.getSystemid().longValue() != systemid)
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的系统
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
	@RequestMapping("/getRoleTree")
	public String getRoleTree()
	{
		long systemid = req().getLong("systemid");
		put("systemid", systemid);
		return "/ds/base/role/getRoleTree.jsp";
	}
	// 获得列表
	@RequestMapping("/getRole")
	public String getRole()
	{
		long systemid = req().getLong("systemid");
		Long pid = req().getLong("pid");
		List<DsBaseRole> list = service.queryList(systemid, pid);
		put("list", list);
		put("systemid", systemid);
		put("pid", pid);
		return "/ds/base/role/getRole.jsp";
	}
	// 获得树形管理时的json数据
	@RequestMapping("/getRoleJson")// BySystemidAndPid
	public void getRoleJson()
	{
		long systemid = req().getLong("systemid");
		long pid = req().getLong("pid");
		print(service.queryList(systemid, pid));
	}

	// 明细
	@RequestMapping("/getRoleById")
	public String getRoleById()
	{
		Long id = req().getLong("keyIndex");
		DsBaseRole po = service.get(id);
		put("po", po);
		return "/ds/base/role/getRoleById.jsp";
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

	// 获得功能和被分配到角色的功能
	@RequestMapping("/getRoleFuncJson")// BySystemidAndRoleid
	public void getRoleFuncJson()
	{
		long systemid = req().getLong("systemid");
		long roleid = req().getLong("roleid");
		List<DsBaseFunc> list = service.queryFuncList(systemid);
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
			print(list);
		}
		else
		{
			print("[]");
		}
	}
}
