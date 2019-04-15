<%@page language="java" pageEncoding="utf-8" import="dswork.web.MyRequest,testwork.model.Choose"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
MyRequest req = new MyRequest(request);
int p = req.getInt("page", 1);
if(p < 1){p = 1;}
java.util.List<Choose> list = new java.util.ArrayList<Choose>();
for(long i = 5*(p-1) + 1; i <= 5*p; i++)
{
	list.add(new Choose().setId(i).setName("A"+i));
}
request.setAttribute("list", list);
request.setAttribute("page", p);
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
var map = new $jskey.Map();
function init(){
	var o;
	map = new $jskey.Map();//初始化或还原,对象中的sname是原节点名称,因为tree修改了name属性
	<c:forEach items="${list}" var="d">
	o = {id:"${d.id}", name:"${fn:escapeXml(d.name)}"};
	map.put("${d.id}", o);
	</c:forEach>
}

// 初始化状态
$(function(){
	init();
	var arr = map.getValueArray();
	for(var i = 0; i < arr.length; i++){
		if(parent.refreshModel(arr[i])){
			 $("#b"+arr[i].id).prop("checked", true);
			 $("#c"+arr[i].id).html("已选择");
		}
		else{
			 $("#b"+arr[i].id).prop("checked", false);
			 $("#c"+arr[i].id).html("<span style='color:#ff0000'>未选择</span>");
		}
	}
});

function reselect(id){
	if($("#b"+id).checked){//取消选择
		parent.setModel(map.get(id), false);
		$("#c"+id).html("<span style='color:#ff0000'>未选择</span>");
	}
	else{
		parent.setModel(map.get(id), true);
		 $("#c"+id).html("已选择");
	}
}
</script>
<style type="text/css">label{cursor:pointer;}</style>
</head>
<body>
<div id="p1"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>已选的内容</td>
		<td style="width:15%">操作</td>
	</tr>
	<c:forEach items="${list}" var="d">
	<tr class='list'>
	<td style='text-align:left;'>${d.name}</td>
	<td><label><input id="b${d.id}" onclick="reselect('${d.id}')" type="checkbox"/><span id="c${d.id}"></span></label></td>
	</tr>
	</c:forEach>
</table>
<script type="text/javascript">
function fn(e){
	if(e.page == ${page}){
		return;
	}
	location.href = "./chooseDetail.jsp?page=" + e.page;
}
$jskey.page({
	template:1,
	object:'p1',size:990,page:${page},
    fn:fn
});
</script>

</body>
</html>