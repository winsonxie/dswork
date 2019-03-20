/**
 * 功能:用户类型Controller
 * 开发人员:无名
 * 创建时间:2018/03/05 17:29:00
 */
package dswork.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.mvc.BaseController;
import dswork.core.page.Page;
import dswork.core.page.PageNav;
import dswork.core.util.CollectionUtil;
import dswork.core.util.UniqueId;
import dswork.common.model.DsCommonUsertype;
import dswork.common.model.DsCommonUsertypeRes;
import dswork.common.service.DsCommonUsertypeService;

@Scope("prototype")
@Controller
@RequestMapping("/ds/common/usertype")
public class DsCommonUsertypeController extends BaseController
{
	@Autowired
	private DsCommonUsertypeService service;

	//添加
	@RequestMapping("/addUsertype1")
	public String addUsertype1()
	{
		return "/ds/common/usertype/addUsertype.jsp";
	}
	
	@RequestMapping("/addUsertype2")
	public void addUsertype2(DsCommonUsertype po)
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
			List<DsCommonUsertypeRes> list = null;
			// 权限资源清单
			String arr_alias[] = req.getStringArray("ralias");
			if(0 < arr_alias.length)
			{
				list = new ArrayList<DsCommonUsertypeRes>();
				String arr_name[] = req.getStringArray("rname");
				if(arr_alias.length == arr_name.length)
				{
					for(int i = 0; i < arr_alias.length; i++)
					{
						DsCommonUsertypeRes m = new DsCommonUsertypeRes();
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
	@RequestMapping("/updUsertype1")
	public String updUsertype1()
	{
		Long id = req.getLong("keyIndex");
		DsCommonUsertype po = service.get(id);
		put("po", po);
		put("page", req.getInt("page", 1));
		List<DsCommonUsertypeRes> list = po.getResourcesList();
		put("list", list);
		return "/ds/common/usertype/updUsertype.jsp";
	}
	
	@RequestMapping("/updUsertype2")
	public void updUsertype2(DsCommonUsertype po)
	{
		try
		{
			if(po.getAlias().length() == 0)
			{
				print("0:标识不能为空");
				return;
			}
			DsCommonUsertype _po = service.get(po.getId());
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
			List<DsCommonUsertypeRes> list = null;
			// 权限资源清单
			String arr_alias[] = req.getStringArray("ralias");
			if(0 < arr_alias.length)
			{
				list = new ArrayList<DsCommonUsertypeRes>();
				String arr_name[] = req.getStringArray("rname");
				if(arr_alias.length == arr_name.length)
				{
					for(int i = 0; i < arr_alias.length; i++)
					{
						DsCommonUsertypeRes m = new DsCommonUsertypeRes();
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
		List<DsCommonUsertype> list = service.queryList(new HashMap<String, Object>());
		put("list", list);
		return "/ds/common/usertype/updUsertypeSeq.jsp";
	}

	@RequestMapping("/updUsertypeSeq2")
	public void updUsertypeSeq2()
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

	//获得分页
	@RequestMapping("/getUsertype")
	public String getUsertype()
	{
		Page<DsCommonUsertype> pageModel = service.queryPage(getPageRequest());
		put("pageModel", pageModel);
		put("pageNav", new PageNav<DsCommonUsertype>(request, pageModel));
		return "/ds/common/usertype/getUsertype.jsp";
	}

	//明细
	@RequestMapping("/getUsertypeById")
	public String getUsertypeById()
	{
		Long id = req.getLong("keyIndex");
		put("po", service.get(id));
		return "/ds/common/usertype/getUsertypeById.jsp";
	}
}
