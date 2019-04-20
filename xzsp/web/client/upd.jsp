<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page pageEncoding="UTF-8" import="
MQAPI.*,
common.gov.JSONUtil,
common.gov.DsXzsp,
common.gov.DsXzspService,
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.core.page.PageRequest,
java.util.List
"%><%
	MyRequest req = new MyRequest(request);
	DsXzspService service = (DsXzspService) BeanFactory.getBean("dsXzspService");
	DsXzsp po = service.get(req.getLong("keyIndex"));
	
	request.setAttribute("po", po);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="/web/themes/default/frame.css" />
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/web/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="/web/js/jskey/jskey_core.js"></script>
<script type="text/javascript" src="/web/js/dswork/dswork.js"></script>
<script type="text/javascript" src="/web/js/dswork/form.js"></script>
<script type="text/javascript">
$dswork.doAjax = true;
$(function(){
	$("#mySave")    .click(function(){$("#resend").val("0");$("#dataFormSave").click();return false;});
	$("#mySend")    .click(function(){$("#resend").val("1");$("#dataFormSave").click();return false;});
	$("#mySaveSend").click(function(){$("#resend").val("2");$("#dataFormSave").click();return false;});
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">修改[${po.sptype}]</td>
		<td class="menuTool">
			<a style="display:none;" id="dataFormSave" href="#">保存</a>
			<a class="save" id="mySave" href="#">保存</a>
			<a class="submit" id="mySend" href="#">重发</a>
			<a class="submit" id="mySaveSend" href="#">保存并重发</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="updAction.jsp">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td style="width:18%;">字段名</td>
		<td style="width:18%;">注释</td>
		<td>内容</td>
	</tr>
<%
try
{
	Object ob = null;
	switch(po.getSptype())
	{
		case 0: ob = new ApplicationOB(); break;
		case 1: ob = new PreAcceptOB(); break;
		case 2: ob = new AcceptOB(); break;
		case 3: ob = new SubmitOB(); break;
		case 4: ob = new CompleteOB(); break;
		case 5: ob = new BlockOB(); break;
		case 6: ob = new ResumeOB(); break;
		case 7: ob = new SupplyOB(); break;
		case 8: ob = new SupplyAcceptOB(); break;
		case 9: ob = new ReceiveRegOB(); break;
	}
	request.setAttribute("ob", JSONUtil.toBean(po.getSpobject(), ob.getClass()));
%>
	<tr><td>SXBM</td> <td>事项编码</td>  <td><input style="width:90%;" type="text" name="SXBM" value="${ob.SXBM}" /></td></tr>
	<tr><td>YWLSH</td><td>业务流水号</td><td><input style="width:90%;" type="text" name="YWLSH" value="${ob.YWLSH}" /></td></tr>
	<tr><td>SBLSH</td><td>申办流水号</td><td><input style="width:90%;" type="text" name="SBLSH" value="${ob.SBLSH}" /></td></tr>
	<tr><td>SJBBH</td><td>数据版本号</td><td><input style="width:90%;" type="text" name="SJBBH" value="${ob.SJBBH}" /></td></tr>
<%
	switch(po.getSptype())
	{
		case 0: %><%@include file="0.jsp" %><%; break;
		case 1: %><%@include file="1.jsp" %><%; break;
		case 2: %><%@include file="2.jsp" %><%; break;
		case 3: %><%@include file="3.jsp" %><%; break;
		case 4: %><%@include file="4.jsp" %><%; break;
		case 5: %><%@include file="5.jsp" %><%; break;
		case 6: %><%@include file="6.jsp" %><%; break;
		case 7: %><%@include file="7.jsp" %><%; break;
		case 8: %><%@include file="8.jsp" %><%; break;
		case 9: %><%@include file="9.jsp" %><%; break;
	}
%>
	<tr><td>BYZDA</td><td>备用字段A</td><td><input style="width:90%;" type="text" name="BYZDA" value="${ob.BYZDA}" /></td></tr>
	<tr><td>BYZDB</td><td>备用字段B</td><td><input style="width:90%;" type="text" name="BYZDB" value="${ob.BYZDB}" /></td></tr>
	<tr><td>BYZDC</td><td>备用字段C</td><td><input style="width:90%;" type="text" name="BYZDC" value="${ob.BYZDC}" /></td></tr>
	<tr><td>BYZDD</td><td>备用字段D</td><td><input style="width:90%;" type="text" name="BYZDD" value="<fmt:formatDate value='${ob.BYZDD}' pattern='yyyy-MM-dd HH:mm:ss'/>" /></td></tr>
	<tr><td>BZ</td><td>备注</td><td><input style="width:90%;" type="text" name="BZ" value="${ob.BZ}" /></td></tr>
<%
}
catch(Exception e)
{
	out.print(e.getMessage());
}
%>
</table>
<input type="hidden" name="keyIndex" value="${po.id}" />
<input type="hidden" id="resend" name="resend" value="0" />
</form>
</body>
</html>
