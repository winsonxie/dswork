<%@ page language="java" pageEncoding="UTF-8" import="
dswork.spring.BeanFactory,
dswork.base.model.DsBaseFunc,
dswork.base.dao.DsBaseSingleUserFuncDao,
common.auth.AuthUtil,
common.auth.Auth,
java.util.List
"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%!
private void todo(HttpServletRequest request)
{
	Auth auth = AuthUtil.getLoginUser(request);
	if(auth != null)
	{
		try
		{
			DsBaseSingleUserFuncDao dao = (DsBaseSingleUserFuncDao)BeanFactory.getBean("DsBaseSingleUserFuncDao");
			List<DsBaseFunc> list = dao.getFuncBySystemidAndAccount(1L, auth.getAccount());// TODO 这里要设置系统的ID
			request.setAttribute("list", list);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
%><%
todo(request);
%>var treedata = [<c:forEach items="${list}" var="d" varStatus="status">
<c:if test="${status.first==true}">{id:${d.id},pid:${d.pid},name:"${d.name}",img:"",imgOpen:"",url:"${d.uri}"}</c:if>
<c:if test="${status.first==false}">,{id:${d.id},pid:${d.pid},name:"${d.name}",img:"",imgOpen:"",url:"${d.uri}"}</c:if>
</c:forEach>];