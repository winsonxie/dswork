<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
java.util.List<java.util.Map<String, String>> list = new java.util.ArrayList<java.util.Map<String, String>>();
java.util.Map<String, String> m = new java.util.HashMap<String, String>();m.put("id", "1");m.put("name", "1A");
list.add(m);
m = new java.util.HashMap<String, String>();m.put("id", "2");m.put("name", "2A");
list.add(m);
m = new java.util.HashMap<String, String>();m.put("id", "4");m.put("name", "4A");
list.add(m);
m = new java.util.HashMap<String, String>();m.put("id", "11");m.put("name", "11A");
list.add(m);
request.setAttribute("list", list);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0,minimal-ui"/>
<title>jskey_dialog</title>
<%@include file="/commons/include/web.jsp"%>
<script type="text/javascript">
var map = new $jskey.Map();

function domsg(type){$("#showmsg").html(type?"数据已修改，未保存":"");}
$dswork.deleteRow = function (obj,id){map.remove(id);$(obj).parent().parent().remove();domsg(true);};
function refreshBody(){
	$("#dataTable>tbody>tr:gt(0)").remove();//除首行外移除所有行
	var arr = map.getValueArray();
	for(var i = 0; i < arr.length; i++){
		var obj = arr[i],_html = "<tr class='list'>";
		_html += "<td style='text-align:left;'>&nbsp;" + obj.name + "</td>"
		_html += "<td><input type='button' class='delete' onclick='$dswork.deleteRow(this, " + obj.id + ")' /></td>";
		_html += "</tr>";
		$("#dataTable").append(_html);
	}
}
function init(){
	var o;
	map = new $jskey.Map();//初始化或还原,对象中的sname是原节点名称,因为tree修改了name属性
	<c:forEach items="${list}" var="d">
	o = {id:"${d.id}", name:"${fn:escapeXml(d.name)}"};
	map.put("${d.id}", o);
	</c:forEach>
	refreshBody();domsg(false);
}

$jskey.dialog.callback = function(){
	var result = $jskey.dialog.returnValue;
	if(result != null){
		var m = new $jskey.Map(), o;
		for(var i = 0; i < result.length; i++){o=result[i];m.put(o.id + "", o);}
		map = m;
		refreshBody();
		domsg(true);
	}
};
function mycallback(){ alert("自定义函数mycallback");
	$jskey.dialog.callback();
};

$(function(){
	init();
	$("#vchoose").click(function(){
		var data = map.getValueArray();
		$dswork.getChooseDialog({id:"chooseSystem", title:"选择信息", args:{url:"./chooseDetail.jsp", data:data}, width:"600", height:"400"});
		return false;
	});
	$("#vchoose2").click(function(){
		var data = map.getValueArray();
		$dswork.getChooseDialog({id:"chooseSystem", title:"选择信息", args:{url:"./chooseDetail.jsp", data:data}, width:"600", height:"400", callback:mycallback});
		return false;
	});
	$("#vinit").click(function(){return init();});
	$("#vsave").click(function(){if(confirm("确定保存？")){
		var ids = "",arr = map.getValueArray();
		if(arr.length > 0){
			ids = arr[0].id;
			for(var i = 1; i < arr.length; i++){ids += "," + arr[i].id}
		}
		alert(ids);
	}});
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">样例列表<span id="showmsg" style="color:red;"></span></td>
		<td class="menuTool">
			<a id="vinit" class="refresh" href="#">重置选择</a>
			<a id="vchoose" class="check" href="#">选择角色</a>
			<a id="vchoose2" class="check" href="#">选择角色(自定义回调)</a>
			<a id="vsave" class="save" href="#">保存分配</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td>已选的内容</td>
		<td style="width:15%">操作</td>
	</tr>
</table>
</body>
</html>