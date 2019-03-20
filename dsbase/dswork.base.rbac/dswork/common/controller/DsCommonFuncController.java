package dswork.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dswork.common.model.DsCommonFunc;
import dswork.common.model.DsCommonRes;
import dswork.common.model.DsCommonSystem;
import dswork.common.model.view.DsCommonFuncView;
import dswork.common.service.DsCommonFuncService;
import dswork.core.util.CollectionUtil;
import dswork.mvc.BaseController;

// 功能
@Scope("prototype")
@Controller
@RequestMapping("/ds/common/func")
public class DsCommonFuncController extends BaseController
{
	@Autowired
	private DsCommonFuncService service;

	// 添加
	@RequestMapping("/addFunc1")
	public String addFunc1()
	{
		Long systemid = req.getLong("systemid");
		long pid = req.getLong("pid");
		DsCommonFunc parent = null;
		if(pid > 0)
		{
			parent = service.get(pid);
			if(parent.getStatus() == 0)// 不是菜单
			{
				return "";// 非法访问
			}
		}
		else
		{
			parent = new DsCommonFunc();
		}
		put("parent", parent);
		put("systemid", systemid);
		put("pid", req.getLong("pid"));
		return "/ds/common/func/addFunc.jsp";
	}

	@RequestMapping("/addFunc2")
	public void addFunc2(DsCommonFunc po)
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
				DsCommonFunc parent = service.get(po.getPid());
				if(null == parent)
				{
					print("0:参数错误，请刷新重试");
					return;
				}
				if(parent.getStatus() == 0)// 上级节点不是菜单
				{
					return;// 非法访问
				}
			}
			if(po.getAlias().length() != 0)
			{
				// 判断是否在该系统下唯一
				if(service.isExistsByAlias(po.getAlias(), po.getSystemid()))
				{
					print("0:操作失败，标识已存在");
					return;
				}
			}
			List<DsCommonRes> list = null;
			// 权限资源清单
			String arr_url[] = req.getStringArray("rurl");
			if(0 < arr_url.length)
			{
				list = new ArrayList<DsCommonRes>();
				String arr_param[] = req.getStringArray("rparam");
				if(arr_url.length == arr_param.length)
				{
					for(int i = 0; i < arr_url.length; i++)
					{
						DsCommonRes m = new DsCommonRes();
						m.setUrl(arr_url[i]);
						m.setParam(arr_param[i]);
						if(0 < m.getUrl().length())// 为空的不添加，直接忽略
						{
							list.add(m);
						}
					}
				}
				else
				{
					print("0:操作失败，请刷新重试");// url和param的个数不一致
					return;
				}
			}
			po.setResourcesList(list);
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
	@RequestMapping("/delFunc")
	public void delFunc()
	{
		try
		{
			int v = 0;
			long[] ids = req.getLongArray("keyIndex", 0);
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

	// 删除系统全部功能
	@RequestMapping("/delFuncBySystem")
	public void delFuncBySystem()
	{
		try
		{
			long id = req.getLong("systemid", 0);
			if(0 < id)
			{
				service.deleteBySystem(id);
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
	@RequestMapping("/updFunc1")
	public String updFunc1()
	{
		Long id = req.getLong("keyIndex");
		DsCommonFunc po = service.get(id);
		if(null == po)
		{
			return null;// 非法访问，否则肯定存在id
		}
		DsCommonFunc parent = null;
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
			parent = new DsCommonFunc();
		}
		int count = service.getCountByPid(id);// 是否有子节点
		List<DsCommonRes> list = po.getResourcesList();
		put("po", po);
		put("parent", parent);
		put("list", list);
		put("count", count);
		return "/ds/common/func/updFunc.jsp";
	}

	@RequestMapping("/updFunc2")
	public void updFunc2(DsCommonFunc po)
	{
		try
		{
			if(0 >= po.getName().length())
			{
				print("0:名称不能为空");
				return;
			}
			DsCommonFunc _po = service.get(po.getId());
			if(null == _po)
			{
				print("0:操作失败，请刷新重试");
				return;
			}
			if(po.getAlias().length() != 0)
			{
				po.setAlias(po.getAlias().toLowerCase());
				if(!po.getAlias().equals(_po.getAlias()))// 标识被修改
				{
					// 判断是否在该系统下唯一
					if(service.isExistsByAlias(po.getAlias(), po.getSystemid()))
					{
						print("0:操作失败，标识已存在");
						return;
					}
				}
			}
			if(0 == po.getStatus())// 修改为不是菜单
			{
				int count = service.getCountByPid(po.getId());// 是否有子节点
				if(count > 0)
				{
					print("0:操作失败，必须删除子节点才能设置为不是菜单");
					return;
				}
			}
			List<DsCommonRes> list = null;
			if(true)// 权限资源清单
			{
				list = new ArrayList<DsCommonRes>();
				String arr_url[] = req.getStringArray("rurl");
				if(0 < arr_url.length)
				{
					String arr_param[] = req.getStringArray("rparam");
					if(arr_url.length == arr_param.length)
					{
						for(int i = 0; i < arr_url.length; i++)
						{
							DsCommonRes m = new DsCommonRes();
							m.setUrl(arr_url[i]);
							m.setParam(arr_param[i]);
							if(0 < m.getUrl().length())// 为空的不添加，直接忽略
							{
								list.add(m);
							}
						}
					}
					else
					{
						print("0:操作失败，请刷新重试");// url和param的个数不一致
						return;
					}
				}
				if(0 == po.getStatus() && 0 >= list.size())
				{
					print("0:操作失败，不作为菜单时，权限资源不能为空");
					return;
				}
			}
			po.setResourcesList(list);
			service.update(po);// systemid和seq不修改
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 排序
	@RequestMapping("/updFuncSeq1")
	public String updFuncSeq1()
	{
		long systemid = req.getLong("systemid");
		long pid = req.getLong("pid");
		List<DsCommonFunc> list = service.queryList(systemid, pid);
		put("list", list);
		return "/ds/common/func/updFuncSeq.jsp";
	}

	@RequestMapping("/updFuncSeq2")
	public void updFuncSeq2()
	{
		Long[] ids = CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0));
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
	@RequestMapping("/updFuncMove1")
	public String updFuncMove1()
	{
		DsCommonSystem po = service.getSystem(req.getLong("systemid"));
		put("po", po);
		return "/ds/common/func/updFuncMove.jsp";
	}

	@RequestMapping("/updFuncMove2")
	public void updFuncMove2()
	{
		long systemid = req.getLong("systemid");
		long pid = req.getLong("pid");
		if(pid <= 0)
		{
			pid = 0;
		}
		else
		{
			DsCommonFunc m = service.get(pid);
			if(m == null || m.getSystemid().longValue() != systemid)
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的系统
				return;
			}
		}
		Long[] ids = getLongArray(req.getString("ids"));
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
	@RequestMapping("/getFuncTree")
	public String getFuncTree()
	{
		long systemid = req.getLong("systemid");
		put("systemid", systemid);
		return "/ds/common/func/getFuncTree.jsp";
	}

	// 获得列表
	@RequestMapping("/getFunc")
	public String getFunc()
	{
		long systemid = req.getLong("systemid");
		Long pid = req.getLong("pid");
		List<DsCommonFunc> list = service.queryList(systemid, pid);
		put("list", list);
		put("systemid", systemid);
		put("pid", pid);
		return "/ds/common/func/getFunc.jsp";
	}

	// 获得树形管理时的json数据
	@RequestMapping("/getFuncJson") // BySystemidAndPid
	public void getFuncJson()
	{
		long systemid = req.getLong("systemid");
		long pid = req.getLong("pid");
		print(service.queryList(systemid, pid));
	}

	// 明细
	@RequestMapping("/getFuncById")
	public String getFuncById()
	{
		Long id = req.getLong("keyIndex");
		DsCommonFunc po = service.get(id);
		List<DsCommonRes> list = po.getResourcesList();
		put("po", po);
		put("list", list);
		return "/ds/common/func/getFuncById.jsp";
	}

	// 菜单导入页面
	@RequestMapping("/getFuncEdit1")
	public String getFuncEdit1()
	{
		long systemid = req.getLong("systemid");
		List<DsCommonFuncView> list = service.queryFuncBySystemid(systemid);
		Map<String, DsCommonFuncView> map = new HashMap<String, DsCommonFuncView>();
		List<DsCommonFuncView> rootList = new ArrayList<DsCommonFuncView>();
		for(DsCommonFuncView m : list)
		{
			m.setIndex(0);
			map.put(m.getId() + "", m);
			if(m.getPid() == 0)
			{
				rootList.add(m);// 只把根节点放入list
			}
		}
		for(DsCommonFuncView m : list)
		{
			if(m.getPid() > 0)
			{
				map.get(m.getPid() + "").add(m);// 放入其余节点对应的父节点
			}
		}
		put("json", rootList.toString());
		return "/ds/common/func/getFuncEdit.jsp";
	}

	// 菜单导入
	@RequestMapping("/getFuncEdit2")
	public void getFuncEdit2()
	{
		try
		{
			long systemid = req.getLong("systemid");
			String menujson = req.getString("menujson");
			menujson = menujson.replace("\n", "");// 去除换行
			menujson = menujson.replace(" ", "");// 去除空格
			menujson = menujson.replace("\t", "");// 去除制表符
			menujson = menujson.replace("'", "\"");// 单引号替换成双引号
			List<String> idList = service.queryFuncIdList(systemid);//除了需要导入的systemid外的其他id集合
			List<String> oldDrIdList = service.queryFuncOldIdList(systemid);//以往导入菜单id集合
			List<DsCommonFunc> list = new ArrayList<DsCommonFunc>();
			boolean isSuccess = doFuncList(list, idList, menujson, 0, systemid);
			if(isSuccess)
			{
				// list数据入库
				service.updateFuncList(list, systemid, oldDrIdList);
				print("1");
			}
			else
			{
				print("0:失败！存在id冲突。");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:失败！请联系管理员。" + e.getMessage());
		}
	}

	/**
	 * 组装DsCommonFunc的list
	 * @param list 需要组装的list
	 * @param jsonStr json字符串
	 * @param pid 父id
	 * @param systemid 导入菜单的系统id
	 * @param seq 排序号
	 */
	private static boolean doFuncList(List<DsCommonFunc> list, List<String> idList, String jsonStr, long pid, long systemid)
	{
		// Json的解析类对象
		JsonParser parser = new JsonParser();
		// 将JSON的String 转成一个JsonArray对象
		JsonArray jsonArray = parser.parse(jsonStr).getAsJsonArray();
		int seq = 1;
		// 加强for循环遍历JsonArray
		for(JsonElement el : jsonArray)
		{
			if(el.isJsonObject())
			{
				JsonObject jsonObject = el.getAsJsonObject();
				long id = jsonObject.get("id").getAsLong();
				if(idList.contains(id + ""))
				{
					return false;
				}
				// 封装DsCommonFunc
				DsCommonFunc po = new DsCommonFunc();
				po.setId(id);
				po.setPid(pid);
				po.setSystemid(systemid);
				po.setName(jsonObject.get("name").getAsString());
				po.setUri("".equals(jsonObject.get("url").getAsString()) ? "#" : jsonObject.get("url").getAsString());
				po.setImg(jsonObject.get("img").getAsString());
				String status = jsonObject.get("status")==null ? "1" : jsonObject.get("status").getAsString();
				po.setStatus("0".equals(status) ? 0 : 1);
				po.setSeq(seq++);
				list.add(po);
				// System.err.println(jsonObject.get("items").isJsonArray());
				String items = jsonObject.get("items").toString();
				if(!"[]".equals(items.trim()))
				{
					doFuncList(list, idList, items, id, systemid);
				}
			}
		}
		return true;
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
}
