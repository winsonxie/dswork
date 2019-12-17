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
		sql.append(" id, account, name, createtime");
	}
	sql.append(" from DS_BASE_USER where 1=1");
	if(isNotEmpty(pr, "account")){
		sql.append(" and ACCOUNT like #{account, typeHandler=LikeTypeHandler}");
	}
	if(isNotEmpty(pr, "name")){
		sql.append(" and NAME like #{name, typeHandler=LikeTypeHandler}");
	}
	sql.append(" and STATUS=1 order by ID desc");
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
	pagesize = req.getInt("pagesize", pagesize);
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
	<script type="text/javascript" src="/web/js/jquery/jquery.qrcode.js"></script>
</head> 
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">用户二维码</td>
	</tr>
</table>
<div class="line"></div>
<form id="queryForm" method="post" action="getUser.jsp">
<table border="0" cellspacing="0" cellpadding="0" class="queryTable">
	<tr>
		<td class="input">
			&nbsp;账号:<input name="account" style="width:100px;" value="${fn:escapeXml(param.account)}" />
			姓名:<input name="name" style="width:80px;" value="${fn:escapeXml(param.name)}" />
		</td>
		<td class="query"><input id="_querySubmit_" type="submit" class="button" value="查询" /></td>
	</tr>
</table>
</form>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>用户二维码</td>
	</tr>
	<tr class="list">
		<td>
<c:forEach items="${pageModel.result}" var="d" varStatus="status">
<div class="qr">
	<div class="qrcode" id="i${d.id}" v="${d.id}"></div>
	<div class="name">${d.name}(${d.account})</div>
</div>
</c:forEach>
		</td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="bottomTable">
	<tr><td>${pageNav.page}</td></tr>
</table>
</body>
<style type="text/css">
.qr{border:2px solid #ffffff;width:180px;height:220px;float:left;margin:0px;padding:10px;text-align:center;}
.qr:hover{border:2px solid #ff0000;cursor:pointer;}
.qr .qrcode{padding:5px;width:180px;}
.qr .name{width:100%;text-align:center;font-size:16px;line-height:30px;}
</style>
<script type="text/javascript">
$(function(){
	$("div.qrcode").each(function(){
		var v = $(this).attr("v");
		var qrcode = new QRCode("i" + v, {
		    text:"http://x.99999jwzz.com/plant/i.jsp?v=" + v,
		    width:180,
		    height:180,
		    colorDark:"#000000",
		    colorLight:"#ffffff",
		    correctLevel:QRCode.CorrectLevel.L
		});
		$("#i" + v).removeAttr("title");
	});
});
</script>
</html>
