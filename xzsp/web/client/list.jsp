<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page pageEncoding="UTF-8" import="
common.gov.DsXzsp,
common.gov.DsXzspService,
dswork.spring.BeanFactory,
dswork.web.MyRequest,
dswork.core.page.PageRequest,
java.util.List,
java.util.Map,
java.util.HashMap
"%><%
MyRequest req = new MyRequest(request);
int zt = req.getInt("fszt", -1);
int size = 0;
try
{
	DsXzspService service = (DsXzspService) BeanFactory.getBean("dsXzspService");
	Map<String, Object> filters = new HashMap<String, Object>();
	filters.put("desc_id", "desc_id");
	filters.put("fszt", zt);
	dswork.core.page.Page<DsXzsp> p = service.queryPage(1, 1000, filters);
	List<DsXzsp> list = p.getResult();
	request.setAttribute("list", list);
	size = p.getTotalCount();
}
catch(Exception e)
{
	out.print(e.getMessage());
}
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

function showUpd(id){
	$("#iframe").attr("src", "upd.jsp?keyIndex=" + id);
}
$(function(){
	$("td.sptype").each(function(){
		var s = "";
		switch($(this).html())
		{
			case "0":s="ApplicationOB"; break;
			case "1":s="PreAcceptOB"; break;
			case "2":s="AcceptOB"; break;
			case "3":s="SubmitOB"; break;
			case "4":s="CompleteOB"; break;
			case "5":s="BlockOB"; break;
			case "6":s="ResumeOB"; break;
			case "7":s="SupplyOB"; break;
			case "8":s="SupplyAcceptOB"; break;
			case "9":s="ReceiveRegOB"; break;
			default:
				break;
		}
		$(this).html(s);
	});
});
</script>
<style type="text/css">td.sptype,td.TL{text-align:left;} td.menuTool{text-align:center;}span.r{color:#ff0000;}span.g{color:#00ff00;}</style>
</head>
<body>
<div style="width:50%;height:100%;float:left;overflow:scroll;background-color:#00ff00;">
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:20%">类型<select onchange="location.href='list.jsp?fszt='+this.value;" v="<%=zt%>"><option value="0">待发</option><option value="1">发送成功</option><option value="2">配置出错</option><option value="3">信息不符合要求</option><option value="4">连不到数据交换平台</option></select></td>
		<td style="width:10%;">发送<br/>次数</td>
		<td style="width:50%;"><span class="g">ID</span>、<span class="r">申办流水号</span><br />备注</td>
		<td style="width:auto;">操作(共<%=size %>条)</td>
	</tr>
<c:forEach items="${list}" var="d">
	<tr>
		<td class="sptype">${d.sptype}</td>
		<td>${d.fscs}</td>
		<td class="TL"><span class="g">${d.id}</span>、<span class="r">${d.sblsh}</span><br />${d.memo}</td>
		<td class="menuTool">
			<a class="edit" onclick="showUpd(${d.id});return false;">修改</a>
		</td>
	</tr>
</c:forEach>
</table>
</div>
<div style="width:50%;height:100%;float:left;overflow:hidden;">
	<iframe id="iframe" style="width:100%;height:100%;border:0;"></iframe>
</div>
</body>
</html>
