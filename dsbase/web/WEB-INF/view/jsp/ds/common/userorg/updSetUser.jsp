<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<script type="text/javascript">
var data = parent.$jskey.dialog.dialogArguments.args.data;
var list = data.oList;
var arr = ["0", <c:forEach items="${list}" var="d">,"${d.orgid}"</c:forEach>];
for(var i=0; i<list.length; i++){
	var o = list[i];o.check = false;
	for(var j=1; j<arr.length; j++){if(o.id == arr[j]){o.check = true;}}
}
var oids = "0";
function refreshValue(){
	var ids = "0";
	$("input[name='keyIndex']:checked").each(function(){
		ids += "," + $(this).val();
	});
	ids = (ids.length == 1) ? "" : ids.substring(2);
	parent.$jskey.dialog.returnValue = (oids == ids)?null:ids;
}
$(function(){
	t = $("#dataTable");
	for(var i = 0; i < list.length; i++){m=list[i];
		var r = $("<tr></tr>"),chk;
		if(m.check){
			chk = $("<input name='keyIndex' type='checkbox' checked='checked' value='' />");
			oids += "," + m.id;
		}
		else{chk = $("<input name='keyIndex' type='checkbox' value='' />");}
		chk.attr("id", "v" + m.id).val(m.id);
		chk.click(function(){refreshValue();});
		$("<td></td>").append(chk).appendTo(r);
		$("<td></td>").html("<label style='cursor:pointer;' for='v" + m.id + "'>" + m.name + "</label>").appendTo(r);
		t.append(r);
	}
	oids = (oids.length == 1) ? "" : oids.substring(2);
	refreshValue();
});
</script>
</head>
<body>
<table id="dataTable" border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:8%">&nbsp;</td>
		<td>岗位名称</td>
	</tr>
</table>
</body>
</html>
