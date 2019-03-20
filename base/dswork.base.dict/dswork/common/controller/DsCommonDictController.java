/**
 * 功能:字典Controller
 */
package dswork.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.DsCommonDict;
import dswork.common.model.DsCommonDictData;
import dswork.common.service.DsCommonDictService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/ds/common/dict")
public class DsCommonDictController extends BaseController
{
	@Autowired
	private DsCommonDictService service;

	// 添加
	@RequestMapping("/addDict1")
	public String addDict1()
	{
		return "/ds/common/dict/addDict.jsp";
	}
	@RequestMapping("/addDict2")
	public void addDict2(DsCommonDict po)
	{
		try
		{
			po.setId(UniqueId.genUniqueId());
			if(po.getName().trim().length() <= 0)
			{
				print("0:保存失败，引用名不能为空");
				return;
			}
			int count = service.getCountByName(po.getId(), po.getName());
			if(count > 0)
			{
				print("0:保存失败，引用名已存在");
			}
			else
			{
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
	@RequestMapping("/delDict")
	public void delDict()
	{
		try
		{
			service.deleteBatch(CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 修改
	@RequestMapping("/updDict1")
	public String updDict1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/common/dict/updDict.jsp";
	}
	@RequestMapping("/updDict2")
	public void updDict2(DsCommonDict po)//, String changeName
	{
		try
		{
			if(po.getName().trim().length() <= 0)
			{
				print("0:保存失败，引用名不能为空");
				return;
			}
			int count = service.getCountByName(po.getId(), po.getName());
			if(count > 0)
			{
				print("0:保存失败，引用名已存在");
			}
			else
			{
				//service.update(po, "1".equals(String.valueOf(changeName)));
				service.update(po, true);
				print(1);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 获得分页
	@RequestMapping("/getDict")
	public String getDict()
	{
		Page<DsCommonDict> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonDict>(request, pageModel));
		return "/ds/common/dict/getDict.jsp";
	}
	// 树形管理
	@RequestMapping("/getDictDataTree")
	public String getDictDataTree()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/common/dict/getDictDataTree.jsp";
	}
	// 获得树形管理时的json数据
	@RequestMapping("/getDictDataJson")//ByDictidAndPid
	public void getDictDataJson()
	{
		DsCommonDict po = service.get(req.getLong("dictid")); 
		List<DsCommonDictData> list = service.queryListData(req.getLong("pid"), po.getName(), new HashMap<String, Object>());
		print(list);
	}
	// 获得列表
	@RequestMapping("/getDictData")
	public String getDictData()
	{
		Long pid = req.getLong("pid");
		DsCommonDict po = service.get(req.getLong("dictid"));
		Map<String, Object> map = req.getParameterValueMap(false, false);
		List<DsCommonDictData> list = service.queryListData(pid, po.getName(), map);
		put("list", list);
		put("po", po);
		put("pid", pid);
		return "/ds/common/dict/getDictData.jsp";
	}

	// 添加
	@RequestMapping("/addDictData1")
	public String addDsDictData1()
	{
		Long dictid = req.getLong("dictid");
		put("pid", req.getLong("pid"));
		put("dict", service.get(dictid));
		return "/ds/common/dict/addDictData.jsp";
	}
	@RequestMapping("/addDictData2")
	public void addDictData2()
	{
		try
		{
			DsCommonDict dict = service.get(req.getLong("dictid"));
			Long pid = req.getLong("pid");
			
			String[] aliasArr = req.getStringArray("alias", false);
			String[] labelArr = req.getStringArray("label", false);
			String[] memoArr = req.getStringArray("memo", false);

			int v = 0;String s = "";
			for(int i = 0; i < aliasArr.length; i++)
			{
				DsCommonDictData po = new DsCommonDictData();
				po.setAlias(aliasArr[i]);
				po.setLabel(labelArr[i]);
				po.setMemo(memoArr[i]);
				if(po.getAlias().trim().length() <= 0)
				{
					v++;//print("0:保存失败，标识不能为空");
					continue;
				}
				po.setId(UniqueId.genUniqueId());
				po.setStatus(1);// 暂时只需要支持树叉
				po.setName(dict.getName());
				po.setPid(pid);
				int count = service.getCountDataByAlias(po.getAlias(), dict.getName(), po.getId());
				if(count > 0)
				{
					v++;//print("0:保存失败，标识已存在");
					s += "," + po.getAlias();
					continue;
				}
				service.saveData(po);
			}
			print("1" + (v > 0?":"+v+"个保存失败，标识已存在" + s:""));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
	
	// 删除
	@RequestMapping("/delDictData")
	public void delDictData()
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
				int count = service.getDataCount(id, null);
				if(0 == count)
				{
					service.deleteData(id);
				}
				else
				{
					v++;//print("0:下级节点不为空，不能删除");
				}
			}
			print("1" + (v > 0?":"+v+"个不能删除，下级节点不为空":""));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
	
	// 修改
	@RequestMapping("/updDictData1")
	public String updDictData1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getData(id));
		return "/ds/common/dict/updDictData.jsp";
	}
	@RequestMapping("/updDictData2")
	public void updDictData2(DsCommonDictData po)
	{
		DsCommonDictData m = service.getData(po.getId());
		try
		{
			if(po.getAlias().trim().length() <= 0)
			{
				print("0:保存失败，标识不能为空");
				return;
			}
			if(!m.getAlias().equals(po.getAlias()))
			{
				int count = service.getCountDataByAlias(po.getAlias(), m.getName(), po.getId());
				if(count > 0)
				{
					print("0:保存失败，标识已存在");
					return;
				}
			}
			service.updateData(po);
			print(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}
	
	// 排序
	@RequestMapping("/updDictDataSeq1")
	public String updDictDataSeq1()
	{
		Long pid = req.getLong("pid");
		DsCommonDict po = service.get(req.getLong("dictid"));
		List<DsCommonDictData> list = service.queryListData(pid, po.getName(), new HashMap<String, Object>());
		put("list", list);
		put("po", po);
		put("pid", pid);
		return "/ds/common/dict/updDictDataSeq.jsp";
	}
	@RequestMapping("/updDictDataSeq2")
	public void updDictDataSeq2()
	{
		Long[] ids = CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0));
		try
		{
			if(ids.length > 0)
			{
				service.updateDataSeq(ids);
				print(1);// 刷新列表页
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
	@RequestMapping("/updDictDataMove1")
	public String updDictDataMove1()
	{
		DsCommonDict po = service.get(req.getLong("dictid"));
		put("po", po);
		return "/ds/common/dict/updDictDataMove.jsp";
	}
	@RequestMapping("/updDictDataMove2")
	public void updDictDataMove2()
	{
		DsCommonDict po = service.get(req.getLong("dictid"));
		long pid = req.getLong("pid");
		if(pid <= 0)
		{
			pid = 0;
		}
		else
		{
			DsCommonDictData m = service.getData(pid);
			if(m == null || !m.getName().equals(po.getName()))
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的字典分类
				return;
			}
		}
		Long[] ids = getLongArray(req.getString("ids"));
		try
		{
			if(ids.length > 0)
			{
				service.updateDataPid(ids, pid);
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

	// 明细
	@RequestMapping("/getDictDataById")
	public String getDictDataById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getData(id));
		return "/ds/common/dict/getDictDataById.jsp";
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
