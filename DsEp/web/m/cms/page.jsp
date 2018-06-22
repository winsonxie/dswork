<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page language="java" pageEncoding="UTF-8" import="
dswork.web.MyRequest,
common.cms.CmsFactory,common.cms.model.ViewSite,common.cms.model.ViewCategory,common.cms.model.ViewArticle,common.json.GsonUtil,java.util.Map,java.util.HashMap"%><%!private String getString(Object object)
{
	return object == null ? "" : String.valueOf(object);
}%><%
	Map<String, Object> map = new HashMap<String, Object>();
try
{
	MyRequest req = new MyRequest(request);

	long siteid = req.getLong("siteid", -1);
	long categoryid = req.getLong("categoryid", -1);
	long pageid = req.getLong("pageid", -1);

	CmsFactory cms = new CmsFactory(siteid);
	ViewSite s = cms.getSite();
	map.put("msg", "");
	map.put("siteid", getString(siteid));
	map.put("sitename", getString(s.getName()));

	if(pageid > 0)// 内容页
	{
		ViewArticle p = cms.get(String.valueOf(pageid));
		if(p.getCategoryid() != categoryid)
		{
	map.put("msg", "参数错误");
	out.print(GsonUtil.toJson(map));
	return;
		}
		ViewCategory c = cms.getCategory(String.valueOf(categoryid));
		map.put("categoryid", getString(categoryid));
		map.put("categoryname", getString(c.getName()));
		map.put("id", getString(p.getId()));
		map.put("title", getString(p.getTitle()));
		map.put("scope", getString(p.getScope()));
		if(p.getScope() == 2)// 外链
		{
	map.put("url", getString(p.getUrl()));
	out.print(GsonUtil.toJson(map));
	return;
		}
		if(p.getScope() == 1)// 单页
		{
	map.put("summary", getString(p.getSummary()));
	map.put("metakeywords", getString(p.getMetakeywords()));
	map.put("metadescription", getString(p.getMetadescription()));
	map.put("releasetime", getString(p.getReleasetime()));
	map.put("releasesource", getString(p.getReleasesource()));
	map.put("releaseuser", getString(p.getReleaseuser()));
	map.put("img", getString(p.getImg()));
	map.put("content", getString(p.getContent()));
	out.print(GsonUtil.toJson(map));
	return;
		}
	}
	map.put("msg", "参数错误");
	out.print(GsonUtil.toJson(map));
	return;
}
catch(Exception e)
{
	map.put("msg", e.getMessage());
	out.print(GsonUtil.toJson(map));
	return;
}
%>