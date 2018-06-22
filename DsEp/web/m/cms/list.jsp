<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
common.cms.CmsFactory,
common.cms.model.ViewSite,
common.cms.model.ViewCategory,
common.cms.model.ViewArticle,
common.cms.model.ViewArticleSet,
common.json.GsonUtil,
java.util.List,
java.util.ArrayList,
java.util.Map,
java.util.HashMap
"%><%!
private List<Long> splitString(String values){
	List<Long> list = new ArrayList<Long>();
	try{String[] _v = values.split(",", -1);
		if(_v.length > 0){
			for(int i = 0; i < _v.length; i++){
				try{list.add(Long.parseLong(_v[i].trim()));}
				catch(NumberFormatException e){}
			}
		}
	}
	catch(Exception e){}
	return list;
}%><%
	Map<String, Object> map = new HashMap<String, Object>();
try
{
	MyRequest req = new MyRequest(request);

	long siteid = req.getLong("siteid", -1);
	String categoryids = req.getString("categoryids", "0");
	List<Long> _ids = splitString(categoryids);
	int currentPage = req.getInt("page", 1);
	int pagesize = req.getInt("pagesize", 25);

	CmsFactory cms = new CmsFactory(siteid);
	ViewSite s = cms.getSite();

	ViewCategory c = cms.getCategory(String.valueOf(_ids.get(0)));
	if(c == null)
	{
		map.put("status", "0");
		map.put("msg", "栏目不存在");
		map.put("categoryids", categoryids);
		map.put("categoryid", String.valueOf(_ids.get(0)));
		return;
	}
	map.put("catetory", c);
	map.put("categoryids", categoryids);
	map.put("categoryid", String.valueOf(_ids.get(0)));
	if(c.getScope() == 0)// 列表
	{
		ViewArticleSet set = cms.queryPage(currentPage, pagesize, true, null, _ids.toArray(new Long[_ids.size()]));
		for(ViewArticle p : set.getRows())
		{
			p.setContent(null);
		}
		set.setCategoryids(categoryids);
		set.setCategoryid(String.valueOf(_ids.get(0)));
		set.setCatetory(c);
		/*
	{
		status:1,
		msg:"success",
		categoryid:栏目ID,
		category:{列表栏目对象},
		size:总条数,
		page:当前页,
		pagesize:每页条数,
		totalpage:总页数,
		rows:[当前页数据数组]
	}
		 */
		out.print(GsonUtil.toJson(set));
		return;
	}
	/*
		{
	status:1,
	msg:"success",
	categoryid:栏目ID,
	category:{单页或外链栏目对象}
		}
	 */
	out.print(GsonUtil.toJson(map));
	return;
}
catch(Exception e)
{
}
map.put("status", "0");
map.put("msg", "请确认站点是否存在");
out.print(GsonUtil.toJson(map));
return;
%>