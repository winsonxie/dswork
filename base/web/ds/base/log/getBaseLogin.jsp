<%@page language="java" pageEncoding="UTF-8" import="dswork.spring.BeanFactory,dswork.core.page.*,dswork.web.*,common.any.AnyDao"%><%!
public boolean isNotEmpty(PageRequest pr, String key){
	Object o = pr.getFilters().get(key);
	return o != null && String.valueOf(o).length() > 0;
}
public PageRequest genSQL(PageRequest pr, boolean isCount){
	StringBuilder sql = new StringBuilder(50);
	sql.append("select");
	if(isCount){
		sql.append(" count(1)");
	}
	else{
		sql.append(" ID, APPID, ATYPE, ACODE, TICKET, OPTYPE, OPTIME, OPREAD, STATUS, IP, USERID, BM, NAME");
	}
	sql.append(" from DS_BASE_USER_LOG where 1=1");
	if(isNotEmpty(pr, "optime_begin")){
		sql.append(" and OPTIME>=#{optime_begin}");
	}
	if(isNotEmpty(pr, "optime_end")){
		sql.append(" and OPTIME<=#{optime_end}");
	}
	if(isNotEmpty(pr, "bm")){
		sql.append(" and BM like #{bm, typeHandler=LikeTypeHandler}");
	}
	if(isNotEmpty(pr, "name")){
		sql.append(" and NAME like #{name, typeHandler=LikeTypeHandler}");
	}
	if(isNotEmpty(pr, "status")){
		sql.append(" and STATUS=#{status}");
	}
	sql.append(" order by ID desc");
	pr.getFilters().put("sql", sql.toString());
	return pr;
}
public PageRequest getPageRequest(HttpServletRequest request)
{
	int pagesize = 10;
	MyRequest req = new MyRequest(request);
	PageRequest pr = new PageRequest();
	pr.setFilters(req.getParameterValueMap(false, false));
	pr.setPage(req.getInt("page", 1));
	try
	{
		pagesize = Integer.parseInt(String.valueOf(request.getSession().getAttribute("dswork_session_pagesize")).trim());
	}
	catch(Exception ex)
	{
		pagesize = 10;
	}
	pagesize = req.getInt("pageSize", pagesize);
	request.getSession().setAttribute("dswork_session_pagesize", pagesize);
	pr.setPagesize(pagesize);
	return pr;
}
%><%
AnyDao dao = (AnyDao)BeanFactory.getBean("anyDao");
PageRequest pr = getPageRequest(request);
PageRequest prcount = getPageRequest(request);
Page pageModel = dao.queryPage(genSQL(pr, false), genSQL(prcount, true));
request.setAttribute("pageModel", pageModel);
request.setAttribute("pageNav", new PageNav<java.util.Map<String, Object>>(request, pageModel));
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title></title>
	<%@include file="/commons/include/web.jsp"%>
	<style type="text/css">
		td.L {text-align:left;padding-left:2px;}
	</style>
</head> 
<body>
<table class="listLogo">
	<tr>
		<td class="title">用户日志</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getBaseLogin.jsp">
<table class="queryTable">
	<tr>
		<td class="input">
			&nbsp;账号:<input name="bm" style="width:100px;" value="${fn:escapeXml(param.bm)}" />
			姓名:<input name="name" style="width:80px;" value="${fn:escapeXml(param.name)}" />
			状态：<select name="status" style="width:60px;" v="${fn:escapeXml(param.status)}"><option value="">全部</option><option value="1">成功</option><option value="0">失败</option></select>
			操作时间：<input type="text" name="optime_begin" class="WebDate" format="yyyy-MM-dd HH:mm:ss" style="width:145px;" value="${fn:escapeXml(param.optime_begin)}" />
			至&nbsp;<input type="text" name="optime_end" class="WebDate" format="yyyy-MM-dd HH:mm:ss" style="width:145px;" value="${fn:escapeXml(param.optime_end)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="submit" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table class="listTable">
	<tr class="list_title">
		<td style="width:8%;">用户ID</td>
		<td style="width:12%;">姓名(账号)</td>
		<td style="width:10%;">操作时间</td>
		<td style="width:10%;">标识</td>
		<td style="width:5%;">状态</td>
		<td style="width:10%;">访问来源</td>
		<td>操作(0登出,1登入,2修改密码,3更换账号,4注销)</td>
		<td>操作说明</td>
	</tr>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
	<tr class="${status.index%2==0?'list_even':'list_odd'}">
		<td>${d.userid}</td>
		<td class="L">${d.name}(${d.bm})</td>
		<td>${d.optime}</td>
		<td>${d.atype}<br>${d.acode}</td>
		<td>${d.status == 1 ? '成功' : '失败'}</td>
		<td>${d.ip}</td>
		<td>${d.optype}</td>
		<td>${d.opread}</td>
	</tr>
</c:forEach>
</table>
<table class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
</html>
