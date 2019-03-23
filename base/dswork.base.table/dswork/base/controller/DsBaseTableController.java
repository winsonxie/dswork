/**
 * 功能:数据表定义Controller
 * 开发人员:
 * 创建时间:2018-10-10 18:05:45
 */
package dswork.base.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.base.model.DsBaseTable;
import dswork.base.model.DsBaseTableCategory;
import dswork.base.service.DsBaseTableService;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.mvc.BaseController;
import dswork.web.MyRequest;

@Scope("prototype")
@Controller
@RequestMapping("/ds/base/table")
public class DsBaseTableController extends BaseController
{
	@Autowired
	private DsBaseTableService service;
	
	//添加
	@RequestMapping("/addTable1")
	public String addTable1()
	{
		put("categoryList",service.getCategoryList(null));
		return "/ds/base/table/addTable.jsp";
	}

	@RequestMapping("/addTable2")
	public void addTable2(DsBaseTable po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", po.getName());
			map.put("categoryid",po.getCategoryid());
			List<DsBaseTable> list = service.queryList(map);
			if(list.size()>0) 
			{
				throw new Exception("该类型下的\""+po.getName()+"\"表单已经存在！");
			}
			po.setId(UniqueId.genId());
			po.setDatatable(resolve(req));//解析成json字符串
			service.save(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//删除
	@RequestMapping("/delTable")
	public void delTable()
	{
		try
		{
			service.deleteBatch(CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping("/updTable1")
	public String updTable1()
	{
		Long id = req.getLong("keyIndex");
		DsBaseTable po = service.get(id);
		put("po", po);
		put("typeList", service.getCategoryList(null));//所有表单类别
		put("map",DsBaseTableService.toBean(po.getDatatable(), Map.class));
		put("page", req.getInt("page", 1));
		return "/ds/base/table/updTable.jsp";
	}

	@RequestMapping("/updTable2")
	public void updTable2(DsBaseTable po)
	{
		try
		{
			po.setDatatable(resolve(req));//解析表单json
			service.update(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//获得分页
	@RequestMapping("/getTable")
	public String getTable()
	{
		put("categoryList",service.getCategoryList(null));
		Page<DsBaseTable> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseTable>(request, pageModel));
		return "/ds/base/table/getTable.jsp";
	}

	//明细
	@RequestMapping("/getTableById")
	public String getTableById()
	{
		Long id = req.getLong("keyIndex");
		DsBaseTable po = service.get(id);
		String string = po.getDatatable();
		Map<?, ?> map = DsBaseTableService.toBean(string, Map.class);
		put("po", po);
		put("map",map);
		return "/ds/base/table/getTableById.jsp";
	}

	//解析表单自定义字段成json字符串
	private String resolve(MyRequest req) {
		String[] name = req.getStringArray("tname");//自定义字段名称数组
		String[] info = req.getStringArray("tinfo");//自定义字段说明数组
		String[] datatype = req.getStringArray("ttype");//自定义字段类型数组
		Map<String,Map<String, String>> map = new LinkedHashMap<String,Map<String, String>>();
		if(name!=null&&name.length==info.length&&name.length==datatype.length) {
			for (int i=0;i<name.length;i++) 
			{
				Map<String, String> map2 = new HashMap<String,String>();
				map2.put("name", name[i]);
				map2.put("info", info[i]);
				map2.put("datatype", datatype[i]);
				map.put(name[i], map2);
			}
		}else {
			throw new RuntimeException("json 数据解析错误");
		}
		return DsBaseTableService.toJson(map);
	}
	
	
	//######
	//添加
	@RequestMapping("/addTableCategory1")
	public String addTableCategory1()
	{
		return "/ds/base/table/addTableCategory.jsp";
	}

	@RequestMapping("/addTableCategory2")
	public void addTableCategory2(DsBaseTableCategory po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", po.getName());
			List<DsBaseTableCategory> list = service.queryListCategory(map);
			if(list.size()>0) 
			{
				throw new Exception("该类型\""+po.getName()+"\"已经存在！");
			}
			po.setId(UniqueId.genId());
			service.saveCategory(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//删除
	@RequestMapping("/delTableCategory")
	public void delTableCategory()
	{
		try
		{
			//判断此类别下是否由表单,有，不允许删除
			Long[] array = CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0));
			for (Long id: array) 
			{
				int count = service.queryFromCountByFormTypeId(id);
				if(count>0) 
				{
					DsBaseTableCategory formType = service.getCategory(id);
					throw new Exception(formType.getName()+" 类别正在被使用");
				}
			}
			service.deleteBatchCategory(array);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping("/updTableCategory1")
	public String updTableCategory1()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getCategory(id));
		put("page", req.getInt("page", 1));
		return "/ds/base/table/updTableCategory.jsp";
	}

	@RequestMapping("/updTableCategory2")
	public void updTableCategory2(DsBaseTableCategory po)
	{
		try
		{
			//判断该名称是否已经被使用
			HashMap<String, Object> map = new HashMap<String, Object>();
			DsBaseTableCategory category = service.getCategory(po.getId());

			if(!category.getName().equals(po.getName())) {//如果新名字和当前名字不一样，
				map.put("name", po.getName());
				List<DsBaseTableCategory> list = service.queryListCategory(map);
				if(list.size()>0) 
				{
					throw new Exception("该类型\""+po.getName()+"\"已经存在！");
				}
			}
			service.updateCategory(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//获得分页
	@RequestMapping("/getTableCategory")
	public String getTableCategory()
	{
		Page<DsBaseTableCategory> pageModel = service.queryPageCategory(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseTableCategory>(request, pageModel));
		return "/ds/base/table/getTableCategory.jsp";
	}

	//明细
	@RequestMapping("/getTableCategoryById")
	public String getTableCategoryById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.getCategory(id));
		return "/ds/base/table/getTableCategoryById.jsp";
	}
}
