package dswork.cms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import common.authown.AuthOwn;
import common.authown.AuthOwnUtil;
import dswork.cms.dao.DsCmsPermissionDao;
import dswork.cms.model.DsCmsCategory;
import dswork.cms.model.DsCmsPermission;
import dswork.cms.model.DsCmsSite;
import dswork.mvc.BaseController;
import dswork.spring.BeanFactory;

public class DsCmsBaseController extends BaseController
{
	private static Map<Long, Map<Long, Map<String, Set<String>>>> siteMap = new HashMap<Long, Map<Long, Map<String, Set<String>>>>();
	private static Map<Long, String> ownMap = new HashMap<Long, String>();

	static
	{
		refresh();
	}

	public static void refresh()
	{
		Map<Long, Map<Long, Map<String, Set<String>>>> map = new HashMap<Long, Map<Long, Map<String, Set<String>>>>();
		DsCmsPermissionDao dao = (DsCmsPermissionDao) BeanFactory.getBean(DsCmsPermissionDao.class);
		List<DsCmsSite> siteList = dao.queryListSite();
		for(DsCmsSite site : siteList)
		{
			ownMap.put(site.getId(), site.getOwn());
//			List<DsCmsCategory> categoryList = dao.queryListCategory(site.getId());
			List<DsCmsPermission> permissionList = dao.queryList(site.getId());
			Map<Long, Map<String, Set<String>>> map1 = new HashMap<Long, Map<String, Set<String>>>();
			for(DsCmsPermission permission : permissionList)
			{
				refreshMap2(map1, Arrays.asList(permission.getEditall().split(",")), "editall", permission);
				refreshMap2(map1, Arrays.asList(permission.getEditown().split(",")), "editown", permission);
				refreshMap2(map1, Arrays.asList(permission.getPublish().split(",")), "publish", permission);
				refreshMap2(map1, Arrays.asList(permission.getAudit().split(",")), "audit", permission);
			}
			if(map1.size() > 0)
			{
				map.put(site.getId(), map1);
			}
		}
		siteMap = map;
	}

	private static void refreshMap2(Map<Long, Map<String, Set<String>>> map1, List<String> idList, String key, DsCmsPermission permission)
	{
		for(String id : idList)
		{
			try
			{
				Long _id = Long.parseLong(id);
				Map<String, Set<String>> map2 = map1.get(_id);
				if(map2 == null)
				{
					map2 = new HashMap<String, Set<String>>();
					map1.put(_id, map2);
				}
				Set<String> set = map2.get(key);
				if(set == null)
				{
					set = new HashSet<String>();
					map2.put(key, set);
				}
				set.add(permission.getAccount());
			}
			catch(NumberFormatException e)
			{
			}
		}
	}

	public Map<Long, Map<String, Set<String>>> getSitePermission(long siteid)
	{
		return siteMap.get(siteid);
	}

	public boolean siteNotSetPermission(long siteid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) == null;
	}

	public boolean categoryNotNeedAudit(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) == null
			|| siteMap.get(siteid).get(categoryid) == null
			|| siteMap.get(siteid).get(categoryid).get("audit") == null;
	}

	public boolean checkEditall(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) == null
			||(siteMap.get(siteid).get(categoryid) != null
			&& siteMap.get(siteid).get(categoryid).get("editall") != null
			&& siteMap.get(siteid).get(categoryid).get("editall").contains(getAccount()));
	}

	public boolean checkEditown(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) != null
			&& siteMap.get(siteid).get(categoryid) != null
			&& siteMap.get(siteid).get(categoryid).get("editown") != null
			&& siteMap.get(siteid).get(categoryid).get("editown").contains(getAccount());
	}

	public boolean checkEdit(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return checkEditown(siteid, categoryid)
			|| checkEditall(siteid, categoryid);
	}

	public boolean checkAudit(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) != null
			&& siteMap.get(siteid).get(categoryid) != null
			&& siteMap.get(siteid).get(categoryid).get("audit") != null
			&& siteMap.get(siteid).get(categoryid).get("audit").contains(getAccount());
	}

	public boolean checkPublish(long siteid, long categoryid)
	{
		if(!checkOwn(siteid))
		{
			return false;
		}
		return siteMap.get(siteid) == null
			||(siteMap.get(siteid).get(categoryid) != null
			&& siteMap.get(siteid).get(categoryid).get("publish") != null
			&& siteMap.get(siteid).get(categoryid).get("publish").contains(getAccount()));
	}

	public boolean checkOwn(long siteid)
	{
		return getOwn().equals(ownMap.get(siteid));
	}

	private static void categorySettingList(DsCmsCategory m, List<DsCmsCategory> list)
	{
		int size = m.getList().size();
		for(int i = 0; i < size; i++)
		{
			DsCmsCategory n = m.getList().get(i);
			n.setLabel(m.getLabel());
			if(m.getLabel().endsWith("├"))
			{
				n.setLabel(m.getLabel().substring(0, m.getLabel().length() - 2) + "│");
			}
			else if(m.getLabel().endsWith("└"))
			{
				n.setLabel(m.getLabel().substring(0, m.getLabel().length() - 2) + "　");
			}
			n.setLabel(n.getLabel() + "&nbsp;&nbsp;");
			n.setLabel(n.getLabel() + (i == size - 1 ? "└" : "├"));
			list.add(n);
			categorySettingList(n, list);
		}
	}

	/**
	 *  接受整理成树形的栏目list
	 * @param list
	 * @return
	 */
	public static List<DsCmsCategory> categorySettingList(List<DsCmsCategory> list)
	{
		List<DsCmsCategory> _list = new ArrayList<DsCmsCategory>();// 按顺序放入
		for(int i = 0; i < list.size(); i++)
		{
			DsCmsCategory m = list.get(i);
			m.setLabel("");
			_list.add(m);
			categorySettingList(m, _list);
		}
		return _list;
	}

	/**
	 *  接受关系完整的栏目list
	 * @param list
	 * @return
	 */
	public static List<DsCmsCategory> categorySetting(List<DsCmsCategory> list)
	{
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		for(DsCmsCategory m : list)
		{
			map.put(m.getId(), m);
		}
		List<DsCmsCategory> _list = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory m : list)
		{
			if(m.getPid() > 0)
			{
				if(map.get(m.getPid()) != null)
				{
					map.get(m.getPid()).add(m);// 放入节点对应的父节点
				}
			}
			else if(m.getPid() == 0)
			{
				_list.add(m);// 只把根节点放入list
			}
		}
		return categorySettingList(_list);
	}

	/**
	 *  接受关系完整的栏目list和权限字符串
	 * @param list
	 * @param ids
	 * @return
	 */
	public static List<DsCmsCategory> categoryAccess(List<DsCmsCategory> list, DsCmsBaseController controller)
	{
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		List<DsCmsCategory> _list = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory c : list)
		{
			if(controller.checkCategory(c))
			{
				c.setEnable(true);
				_list.add(c);
			}
			else
			{
				map.put(c.getId(), c);
			}
		}
		List<DsCmsCategory> __list = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory c : _list)
		{
			__list.add(c);
			DsCmsCategory p = null;
			while(c.getPid() != 0 && (p = map.get(c.getPid())) != null)
			{
				__list.add(p);
				map.remove(p.getId());
				c = p;
			}
		}
		return categorySetting(__list);
	}

	public static List<DsCmsCategory> queryCategory(List<DsCmsCategory> clist, boolean exclude, long excludeId)
	{
		Map<Long, DsCmsCategory> map = new HashMap<Long, DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			map.put(m.getId(), m);
		}
		List<DsCmsCategory> tlist = new ArrayList<DsCmsCategory>();
		for(DsCmsCategory m : clist)
		{
			if(m.getId() != excludeId)
			{
				if(m.getPid() > 0)
				{
					try
					{
						if(m.getScope() == 0 || exclude)
						{
							map.get(m.getPid()).add(m);// 放入其余节点对应的父节点
						}
					}
					catch(Exception ex)// 找不到对应的父栏目
					{
					}
				}
				else if(m.getPid() == 0)
				{
					if(m.getScope() == 0 || exclude)
					{
						tlist.add(m);// 只把根节点放入list
					}
				}
			}
		}
		if(excludeId > 0)
		{
			try
			{
				map.get(excludeId).clearList();
			}
			catch(Exception e)
			{
				e.printStackTrace();// 找不到对应的栏目
			}
		}
		List<DsCmsCategory> list = categorySettingList(tlist);
		return list;
	}

	private String id;
	private String own;
	private String account;
	private String name;

	@Override
	@ModelAttribute
	public void BaseInitialization(HttpServletRequest request, HttpServletResponse response)
	{
		super.BaseInitialization(request, response);
		AuthOwn m = AuthOwnUtil.getUser(request);
		id = m.getId();
		own = m.getOwn();
		account = m.getAccount();
		name = m.getName();
	}

	public boolean checkCategory(DsCmsCategory category)
	{
		return false;
	}

	protected String getId()
	{
		return id;
	}

	protected String getAccount()
	{
		return account;
	}

	protected String getName()
	{
		return name;
	}

	protected String getOwn()
	{
		return own;
	}
}
