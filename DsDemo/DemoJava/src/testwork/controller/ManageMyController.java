/**
 * 功能:MyController测试类，请忽略
 */
package testwork.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.data.MyService;
import dswork.core.page.Page;
import dswork.core.page.PageRequest;
import dswork.core.util.CollectionUtil;

@Scope("prototype")
@Controller
@RequestMapping("/manage/my")
public class ManageMyController extends BaseController
{
	@Autowired
	private MyService service;
	private static String namespaceDot = "testwork.model.";// 配置单表model类命名空间前缀

	@RequestMapping("/save/{model}")
	public void save(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			Object obj;
			obj = Class.forName(namespaceDot + model).newInstance();
			req.getFillObject(obj);
			try
			{
				obj.getClass().getMethod("setId", String.class).invoke(obj, dswork.core.util.UniqueId.genGuid());
			}
			catch(Exception ex1)
			{
				try
				{
					obj.getClass().getMethod("setId", Long.class).invoke(obj, dswork.core.util.UniqueId.genUniqueId());
				}
				catch(Exception ex2){}
			}
			service.save(namespaceDot + model, obj);
			map.put("status", 1);
			map.put("msg", obj.getClass().getMethod("getId").invoke(obj));
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}

	@RequestMapping("/delete/{model}")
	public void delete(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			service.deleteBatch(namespaceDot + model, CollectionUtil.toLongArray(req.getLongArray("keyIndex", 0)));
			map.put("status", 1);
			map.put("msg", "");
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}

	@RequestMapping("/update/{model}")
	public void update(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			Object obj;
			obj = (Class.forName(namespaceDot + model)).newInstance();
			req.getFillObject(obj);
			service.update(namespaceDot + model, obj);
			map.put("status", 1);
			map.put("msg", "");
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}

	@RequestMapping("/page/{model}")
	public void page(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			PageRequest pr = getPageRequest();
			Page<Object> page = service.queryPage(namespaceDot + model, pr);
			map.put("status", 1);
			map.put("msg", "");
			map.put("size", page.getTotalsize());
			map.put("page", page.getPage());
			map.put("pagesize", page.getPagesize());
			map.put("totalpage", page.getTotalpage());
			List<Object> list = page.getResult();
			map.put("rows", (list != null && list.size() > 0) ? list : new ArrayList<Object>());
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}

	@RequestMapping("/list/{model}")
	public void list(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			PageRequest pr = getPageRequest();
			List<Object> list = service.queryList(namespaceDot + model, pr);
			map.put("status", 1);
			map.put("msg", "");
			map.put("rows", (list != null && list.size() > 0) ? list : new ArrayList<Object>());
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}

	@RequestMapping("/get/{model}")
	public void get(@PathVariable String model)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			Long id = req.getLong("keyIndex");
			Object po = service.get(namespaceDot + model, id);
			map.put("status", 1);
			map.put("msg", "");
			map.put("po", po);
		}
		catch(Exception e)
		{
			map.put("status", 0);
			map.put("msg", e.getMessage());
		}
		print(toJson(map));
	}
	
	
	static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	static com.google.gson.Gson gson = builder.create();
	//static com.alibaba.fastjson.serializer.SerializeConfig mapping = new com.alibaba.fastjson.serializer.SerializeConfig();
	//static{mapping.put(java.util.Date.class, new com.alibaba.fastjson.serializer.SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));}
	public static String toJson(Object object)
	{
		if(object != null)
		{
			return gson.toJson(object);
		}
		else
		{
			return "{}";
		}
		//return com.alibaba.fastjson.JSON.toJSONString(object, mapping);
	}
	
	public static <T> T toBean(String json, Class<T> classOfT)
	{
		return gson.fromJson(json, classOfT);
		//return com.alibaba.fastjson.JSON.parseObject(json, classOfT);
	}
}
