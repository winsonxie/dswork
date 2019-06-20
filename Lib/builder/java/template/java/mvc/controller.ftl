/**
 * 功能:${table.comment}Controller
 * 开发人员:${developer}
 * 创建时间:${.now}
 */
package ${namespace}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${frame}.mvc.BaseController;
import ${frame}.core.page.Page;
import ${frame}.core.page.PageNav;
import ${frame}.core.util.CollectionUtil;
import ${namespace}.model.${model};
import ${namespace}.service.${moduleUpperCamel}Service;

@Controller
@RequestMapping("${module}")
public class ${moduleUpperCamel}Controller extends BaseController
{
	@Autowired
	private ${moduleUpperCamel}Service service;

	//添加
	@RequestMapping
	public String add1()
	{
		return "${module}/add1.jsp";
	}

	@RequestMapping
	public void add2(${model} po)
	{
		try
		{
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
	@RequestMapping
	public void del()
	{
		try
		{
			service.deleteBatch(CollectionUtil.toLongArray(req().getLongArray("keyIndex", 0)));
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	//修改
	@RequestMapping
	public String upd1()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		put("page", req().getInt("page", 1));
		return "${module}/upd1.jsp";
	}

	@RequestMapping
	public void upd2(${model} po)
	{
		try
		{
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
	@RequestMapping
	public String list()
	{
		Page<${model}> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<${model}>(request(), pageModel));
		return "${module}/list.jsp";
	}

	//明细
	@RequestMapping
	public String get()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "${module}/get.jsp";
	}
}
