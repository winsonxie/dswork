/**
 * 功能:字典Controller
 */
package dswork.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.base.model.DsBaseDict;
import dswork.base.model.DsBaseDictData;
import dswork.base.service.DsBaseDictService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;

@Scope("prototype")
@Controller
@RequestMapping("/ds/base/dict")
public class DsBaseDictController extends BaseController
{
	@Autowired
	private DsBaseDictService service;

	// 添加
	@RequestMapping("/addDict1")
	public String addDict1()
	{
		return "/ds/base/dict/addDict.jsp";
	}

	@RequestMapping("/addDict2")
	public void addDict2(DsBaseDict po)
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
		return "/ds/base/dict/updDict.jsp";
	}

	@RequestMapping("/updDict2")
	public void updDict2(DsBaseDict po)// , String changeName
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
		Page<DsBaseDict> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseDict>(request, pageModel));
		return "/ds/base/dict/getDict.jsp";
	}

	// 树形管理
	@RequestMapping("/getDictDataTree")
	public String getDictDataTree()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/base/dict/getDictDataTree.jsp";
	}

	// 获得树形管理时的JSON数据
	@RequestMapping("/getDictDataJson") // ByDictidAndPid
	public void getDictDataJson()
	{
		DsBaseDict po = service.get(req.getLong("dictid"));
		List<DsBaseDictData> list = service.queryListData(req.getString("pid"), po.getName(), req.getParameterValueMap(false, false));
		print(list);
	}

	// 获得列表
	@RequestMapping("/getDictData")
	public String getDictData()
	{
		String pid = req.getString("pid");
		DsBaseDict po = service.get(req.getLong("dictid"));
		List<DsBaseDictData> list = service.queryListData(pid, po.getName(), req.getParameterValueMap(false, false));
		if(po.getLevel() > 1)
		{
			DsBaseDictData pNode = service.getData(pid, po.getName());
			put("level", pNode == null ? 0 : pNode.getLevel() + 1);
		}
		put("list", list);
		put("po", po);
		put("pid", pid);
		return "/ds/base/dict/getDictData.jsp";
	}

	// 添加
	@RequestMapping("/addDictData1")
	public String addDictData1()
	{
		try
		{
			Long dictid = req.getLong("dictid");
			String pid = req.getString("pid");
			DsBaseDict dict = service.get(dictid);
			put("pid", pid);
			put("dict", dict);
			if(dict.isLimitedRule())
			{
				DsBaseDictData pNode = service.getData(pid, dict.getName());
				put("rule", dict.getRules()[pNode == null ? 0 : pNode.getLevel() + 1]);
			}
			return "/ds/base/dict/addDictData.jsp";
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@RequestMapping("/addDictData2")
	public void addDictData2()
	{
		try
		{
			String pid = req.getString("pid");
			String mark = req.getString("mark");
			DsBaseDict dict = service.get(req.getLong("dictid"));
			DsBaseDictData pNode = service.getData(pid, dict.getName());
			String[] aliasArr = req.getStringArray("alias", false);
			String[] labelArr = req.getStringArray("label", false);
			String[] memoArr = req.getStringArray("memo", false);
			int v = 0;
			String s = "";
			if(dict.getLevel() > 1)
			{
				int level;
				if(pNode == null)
				{
					level = 0;
				}
				else if(pNode.getLevel() < dict.getLevel())
				{
					level = pNode.getLevel() + 1;
				}
				else
				{
					print("0:不允许继续添加子节点");
					return;
				}
				for(int i = 0; i < aliasArr.length; i++)
				{
					DsBaseDictData po = new DsBaseDictData();
					if(dict.getRules().length > 0) 
					{
						if(pNode == null)
						{
							po.setId(aliasArr[i]);
						}
						else
						{
							po.setId(pid + aliasArr[i]);
						}
						if(aliasArr[i].length() != dict.getRules()[level])
						{
							v++;
							s += "," + po.getId() + "编码长度非法";
							continue;
						}
					}
					else
					{
						po.setId(aliasArr[i]);
					}
					po.setLevel(level);
					po.setMark(mark);
					po.setLabel(labelArr[i]);
					po.setMemo(memoArr[i]);
					po.setStatus(0);
					po.setName(dict.getName());
					po.setPid(pid);
					if(service.getData(po.getId(), po.getName()) != null)
					{
						// 如果数据已存在
						v++;
						s += "," + po.getId() + "编码已存在";
						continue;
					}
					service.saveData(po);
				}
			}
			else
			{
				for(int i = 0; i < aliasArr.length; i++)
				{
					DsBaseDictData po = new DsBaseDictData();
					po.setId(aliasArr[i]);
					po.setMark(mark);
					po.setLabel(labelArr[i]);
					po.setMemo(memoArr[i]);
					po.setStatus(0);
					po.setName(dict.getName());
					po.setPid(pid);
					if(service.getData(po.getId(), po.getName()) != null)
					{
						// 如果数据已存在
						v++;
						s += "," + po.getId() + "编码已存在";
						continue;
					}
					service.saveData(po);
				}
			}
			// 如果有成功保存的节点
			if(aliasArr.length - v > 0)
			{
				if(pNode != null && pNode.getStatus() != 1)
				{
					// 确保父节点为分支状态
					service.updateDataStatus(pNode.getId(), pNode.getName(), 1);
				}
			}
			print("1" + (v > 0 ? ":" + v + "个保存失败" + s : ""));
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
			String pid = req.getString("pid");
			String[] ids = req.getStringArray("keyIndex", false);
			String name = req.getString("name");
			for(String id : ids)
			{
				if(service.getDataCount(id, name) == 0)
				{
					service.deleteData(pid, id, name);
				}
				else
				{
					v++;
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
	@RequestMapping("/updDictData1")
	public String updDictData1()
	{
		String id = req.getString("keyIndex");
		String name = req.getString("name");
		put("po", service.getData(id, name));
		return "/ds/base/dict/updDictData.jsp";
	}

	@RequestMapping("/updDictData2")
	public void updDictData2(DsBaseDictData po)
	{
		try
		{
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
		String pid = req.getString("pid");
		DsBaseDict po = service.get(req.getLong("dictid"));
		List<DsBaseDictData> list = service.queryListData(pid, po.getName(), req.getParameterValueMap(false, false));
		put("list", list);
		put("po", po);
		put("pid", pid);
		return "/ds/base/dict/updDictDataSeq.jsp";
	}

	@RequestMapping("/updDictDataSeq2")
	public void updDictDataSeq2()
	{
		String[] ids = req.getStringArray("keyIndex", false);
		String name = req.getString("name");
		try
		{
			if(ids.length > 0)
			{
				service.updateDataSeq(ids, name);
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
		DsBaseDict po = service.get(req.getLong("dictid"));
		if(po.getLevel() > 1)
		{
			return null;
		}
		put("po", po);
		return "/ds/base/dict/updDictDataMove.jsp";
	}

	/**
	 * 移动字典
	 * @param dictid 字典ID
	 * @param name 字典引用名
	 * @param pid 需要移动到的字典项父ID
	 * @param ids 被移动的字典项ID数组
	 */
	@RequestMapping("/updDictDataMove2")
	public void updDictDataMove2()
	{
		DsBaseDict po = service.get(req.getLong("dictid"));
		if(po.getLevel() > 1)
		{
			print("0:受限树形字典不支持移动");
			return;
		}
		String pid = req.getString("pid");
		String name = req.getString("name");
		if(!pid.isEmpty())
		{
			DsBaseDictData m = service.getData(pid, name);
			if(m == null || !m.getName().equals(po.getName()))
			{
				print("0:不能进行移动");// 移动的节点不存在，或者不在相同的字典分类
				return;
			}
		}
		String[] ids = req.getStringArray("ids", false);
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

	/**
	 * 查询字典明细
	 * @param keyIndex 字典编码
	 * @param name 字典引用名
	 * @return
	 */
	@RequestMapping("/getDictDataById")
	public String getDictDataById()
	{
		String id = req.getString("keyIndex");
		String name = req.getString("name");
		DsBaseDictData po = service.getData(id, name);
		DsBaseDict dict = service.getByName(po.getName());
		put("dict", dict);
		put("po", po);
		return "/ds/base/dict/getDictDataById.jsp";
	}
}
