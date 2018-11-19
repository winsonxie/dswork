<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dswork.common.DsFactory, dswork.web.MyRequest, dswork.common.model.*, java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<style type="text/css">
body {line-height:2em;}
</style>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">流程测试</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="waiting.jsp">返回待办列表</a>
		</td>
	</tr>
</table>
<form id="dataForm" method="post" action="doAction.jsp">
<%
String msg = "";
MyRequest req = new MyRequest(request);
long wid = req.getLong("wid");
try
{
  if(wid > 0){
	IFlowWaiting po = DsFactory.getFlow().getWaiting(wid);
	request.setAttribute("po", po);
	java.util.Map<String, String> map = DsFactory.getFlow().getTaskList(po.getFlowid());
	map.get(po.getTalias());
	String datatable = po.getDatatable().replaceAll("\\\\", "");
	List<IFlowDataRow> dt = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(datatable, List.class);
	request.setAttribute("dt", dt);
%>
	流程名称：${po.flowname}<br />
	当前任务：${po.talias}&nbsp;${po.tname}<br />
	下级任务：
	<%
	String[] arr = po.getTnext().split("\\|", -1);
	for(String s : arr)
	{
		%><select name="taskList"><%
		for(String m : s.split(",", -1)){%><option value="<%=m%>"><%=map.get(m)%></option><%}
		%></select>&nbsp;<%
	}
	%>
	<br />
	状态：<label><input type="radio"  name="resultType" value="1" checked="checked" />拟同意</label>
		&nbsp;<label><input type="radio"  name="resultType" value="0" />拟拒绝</label>
		&nbsp;<label><input type="radio"  name="resultType" value="-1" />拟作废</label>
	<br />
	意见：<textarea name="resultMsg" style="width:400px;">无</textarea><br />
<input type="hidden" name="wid" value="<%=wid%>" />
<input type="hidden" id="datatable" name="datatable" value="" />
</form>
<form id="formdata" method="post" action="">
<script type="text/javascript">
var fileArr = [];
function loaddata(name, value, objectid, type, ename){
	$.post("${ctx}/common/share/getJsonDict.htm",{name:name, value:value},function(data){
		var a = eval(data);
		var s = $("#" + objectid);
		if(type == "checkbox" || type == "radio"){
			for(var i=0; i<a.length; i++){
				s.append("<label><input name=\"" + ename + "\" type=\"" + type + "\" value=\"" + a[i].id + "\" " + (i==0?"checked":"") + " />"+a[i].name+"</label>");
			}
			s.append("<input name=\"" + ename + "\" type=\"" + type + "\" datatype=\"Group\" msg=\"必选\" value=\"\" style=\"display:none;\" />");
		}else{
			for(var i=0; i<a.length; i++){
				var o = $("<option></option>");
				o.text(a[i].name);
				o.attr("value", a[i].id);
				if(i == 0){o.prop("selected", true);}// 当下拉框size大于1时，默认不会有选中的值
				s.append(o);
			}
			s.change();
		}
	});
}
</script>
<c:forEach items="${dt}" var="dt">
	<div ${fn:escapeXml(dt.trwx=='001'?'style=display:none;':'')} name="tb">
		<%-- ${fn:escapeXml(dt.talias)}：<input name="${fn:escapeXml(dt.tname)}" datatype="${fn:escapeXml(dt.datatype)}" ${fn:escapeXml(dt.rwx=='400'?'readonly':'')} value="${fn:escapeXml(dt.value)}" /><br /> --%>
		${fn:escapeXml(dt.talias)}：
		<c:if test="${dt.tuse == 'common'}">
			<input name="${fn:escapeXml(dt.tname)}" datatype="${fn:escapeXml(dt.ttype)}" ${fn:escapeXml(dt.trwx=='400'?'readonly':'')} value="" /><br />
			<script type="text/javascript">
			var tp = "${fn:escapeXml(dt.ttype)}";
			var tv = "${fn:escapeXml(dt.tvalue)}";
			var k = "";
			var v = "";
			if(tp.indexOf(",") > 0){
				k = tp.split(",")[0].split(":")[1];
				v = tp.split(",")[1].split(":")[1];
			}
			if(tv == ""){
				$("[name=tb]").find("[name=${fn:escapeXml(dt.tname)}]").val(v);
			}
			else{
				$("[name=tb]").find("[name=${fn:escapeXml(dt.tname)}]").val("${fn:escapeXml(dt.tvalue)}");
			}
			</script>
		</c:if>
		<c:if test="${dt.tuse == 'file'}">
			<script type="text/javascript">
				var f = {};
				f.tuse  = "${fn:escapeXml(dt.tuse)}";
				f.ttype = "${fn:escapeXml(dt.ttype)}";
				f.tname = "${fn:escapeXml(dt.tname)}";
				fileArr.push(f);
			</script>
			<input id="id_${fn:escapeXml(dt.tname)}" name="id_${fn:escapeXml(dt.tname)}" type="text" readonly="readonly" />
			<input id="vid_${fn:escapeXml(dt.tname)}" name="vid_${fn:escapeXml(dt.tname)}" type="hidden"/>
		</c:if>
		<c:if test="${dt.tuse == 'dict'}">
			<span id="chk1"></span>
			<script type="text/javascript">
			var tp = "${fn:escapeXml(dt.ttype)}";
			var k = "";
			var v = "";
			if(tp.indexOf(",") > 0){
				k = tp.split(",")[0].split(":")[1];
				v = tp.split(",")[1].split(":")[1];
			}
			loaddata(k, v, "chk1", "checkbox", "chk_hello");
			</script>
		</c:if>
		<c:if test="${dt.tuse == 'extend'}">
			<script type="text/javascript">
				var tp = "${fn:escapeXml(dt.ttype)}";
				var k = "";
				var v = "";
				if(tp.indexOf(",") > 0){
					k = tp.split(",")[0].split(":")[1];
					v = tp.split(",")[1].split(":")[1];
				}
				var karr = k.split("|");
				var varr = v.split("|");
				$.each(karr, function(index, value){
					document.write(karr[index] + ":" + varr[index] + "，");
				});
			</script>
		</c:if>
	</div>
</c:forEach>
<script type="text/javascript">
if(fileArr.length > 0){
	var script=document.createElement("script");
	script.type="text/javascript";
	script.src="/web/js/jskey/jskey_upload.js";
	document.getElementsByTagName('head')[0].appendChild(script); 
	var oarr = [];
	for(var i = 0; i < fileArr.length; i++){
		var ttype = fileArr[i].ttype;
		var k = "";
		var v = "";
		if(ttype.indexOf(",") > 0){
			k = ttype.split(",")[0].split(":")[1];
			v = ttype.split(",")[1].split(":")[1];
		}
		oarr[i] = new $dswork.upload({io:true, name:k.toUpperCase(), ext: k});
	}
	window.onload = function()
	{
		for(var i = 0; i < oarr.length; i++){
			var ttype = fileArr[i].ttype;
			var k = "";
			var v = "";
			if(ttype.indexOf(",") > 0){
				k = ttype.split(",")[0].split(":")[1];
				v = ttype.split(",")[1].split(":")[1];
			}
			oarr[i].init({id:"id_" + fileArr[i].tname, vid:"vid_" + fileArr[i].tname, ext:v});
		}
	};
}


</script>
</form>
<script type="text/javascript">
var map = new $jskey.Map();
var array = [];
function getFormData(){
	array = [];
	var d = {};
	var formdata = $("#formdata").serializeArray();
	$.each(formdata, function(){
		var m = map.get(this.name);
		if(m.rwx == "420"){
			m.tvalue = this.value.replace(/\"/g,"&quot;");
		}
		map.put(this.name, m);
		array.push(m);
	});
	$("#datatable").val(JSON.stringify(array));
}
function init(){
<c:forEach items="${dt}" var="d">
	var row = {};
	row.tname  = "${d.tname}";
	row.talias = "${d.talias}";
	row.tuse   = "${d.tuse}";
	row.ttype  = "${d.ttype}";
	row.trwx   = "${d.trwx}";
	row.tvalue = "";
	map.put(row.tname, row);
</c:forEach>
}
$(function(){
	init();
})

$dswork.readySubmit = function(){
	getFormData();
}
</script>

<%
	}else{msg = "处理失败";}
}catch(Exception ex){
	ex.printStackTrace();
	msg = "处理失败";
}%>
<%=msg%>
<br />
</body>
</html>