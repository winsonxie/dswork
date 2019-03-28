/**
 * 功能:用户类型Controller
 * 开发人员:无名
 * 创建时间:2018/03/05 17:29:00
 */
package dswork.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.base.model.DsBaseUsertype;
import dswork.base.model.DsBaseUsertypeRes;
import dswork.base.service.DsBaseUsertypeService;

@Controller
@RequestMapping("/ds/base/usertype")
public class DsBaseUsertypeController extends BaseController
{
	@Autowired
	private DsBaseUsertypeService service;

	//添加
	@RequestMapping("/addUsertype1")
	public String addUsertype1()
	{
		return "/ds/base/usertype/addUsertype.jsp";
	}
	
	@RequestMapping("/addUsertype2")
	public void addUsertype2(DsBaseUsertype po)
	{
		try
		{
			if(po.getAlias().length() == 0)
			{
				print("0:标识不能为空");
				return;
			}
			po.setAlias(po.getAlias().toLowerCase());
			// 判断是否唯一
			if(service.isExistsByAlias(po.getAlias()))
			{
				print("0:操作失败，标识已存在");
				return;
			}
			List<DsBaseUsertypeRes> list = null;
			// 权限资源清单
			String arr_alias[] = req().getStringArray("ralias");
			if(0 < arr_alias.length)
			{
				list = new ArrayList<DsBaseUsertypeRes>();
				String arr_name[] = req().getStringArray("rname");
				if(arr_alias.length == arr_name.length)
				{
					for(int i = 0; i < arr_alias.length; i++)
					{
						DsBaseUsertypeRes m = new DsBaseUsertypeRes();
						m.setAlias(arr_alias[i]);
						m.setName(arr_name[i]);
						if(0 < m.getAlias().length())// 为空的不添加，直接忽略
						{
							list.add(m);
						}
					}
				}
				else
				{
					print("0:操作失败，请刷新重试");// 个数不一致
					return;
				}
			}
			po.setId(UniqueId.genUniqueId());
			po.setSeq(po.getId());
			po.setResourcesList(list);
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
	@RequestMapping("/delUsertype")
	public void delUsertype()
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
	@RequestMapping("/updUsertype1")
	public String updUsertype1()
	{
		Long id = req().getLong("keyIndex");
		DsBaseUsertype po = service.get(id);
		put("po", po);
		put("page", req().getInt("page", 1));
		List<DsBaseUsertypeRes> list = po.getResourcesList();
		put("list", list);
		return "/ds/base/usertype/updUsertype.jsp";
	}
	
	@RequestMapping("/updUsertype2")
	public void updUsertype2(DsBaseUsertype po)
	{
		try
		{
			if(po.getAlias().length() == 0)
			{
				print("0:标识不能为空");
				return;
			}
			DsBaseUsertype _po = service.get(po.getId());
			if(null == _po)
			{
				print("0:操作失败，请刷新重试");
				return;
			}
			po.setAlias(po.getAlias().toLowerCase());
			if(!po.getAlias().equals(_po.getAlias()))// 标识被修改
			{
				// 判断是否唯一
				if(service.isExistsByAlias(po.getAlias()))
				{
					print("0:操作失败，标识已存在");
					return;
				}
			}
			List<DsBaseUsertypeRes> list = null;
			// 权限资源清单
			String arr_alias[] = req().getStringArray("ralias");
			if(0 < arr_alias.length)
			{
				list = new ArrayList<DsBaseUsertypeRes>();
				String arr_name[] = req().getStringArray("rname");
				if(arr_alias.length == arr_name.length)
				{
					for(int i = 0; i < arr_alias.length; i++)
					{
						DsBaseUsertypeRes m = new DsBaseUsertypeRes();
						m.setAlias(arr_alias[i]);
						m.setName(arr_name[i]);
						if(0 < m.getAlias().length())// 为空的不添加，直接忽略
						{
							list.add(m);
						}
					}
				}
				else
				{
					print("0:操作失败，请刷新重试");// 个数不一致
					return;
				}
			}
			po.setResourcesList(list);
			service.update(po);
			print(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			print("0:" + e.getMessage());
		}
	}

	// 排序
	@RequestMapping("/updUsertypeSeq1")
	public String updUsertypeSeq1()
	{
		List<DsBaseUsertype> list = service.queryList(new HashMap<String, Object>());
		put("list", list);
		return "/ds/base/usertype/updUsertypeSeq.jsp";
	}

	@RequestMapping("/updUsertypeSeq2")
	public void updUsertypeSeq2()
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

	//获得分页
	@RequestMapping("/getUsertype")
	public String getUsertype()
	{
		Page<DsBaseUsertype> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsBaseUsertype>(request(), pageModel));
		return "/ds/base/usertype/getUsertype.jsp";
	}

	//明细
	@RequestMapping("/getUsertypeById")
	public String getUsertypeById()
	{
		Long id = req().getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/base/usertype/getUsertypeById.jsp";
	}
}
