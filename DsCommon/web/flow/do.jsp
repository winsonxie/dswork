<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="dswork.common.DsFactory, dswork.web.MyRequest, dswork.common.model.*, java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<script type="text/javascript" src="/web/js/jskey/jskey_upload.js"></script>
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
	com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	List<IFlowDataRow> dt = gson.fromJson(po.getDatatable(), List.class);
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
			<input type="text" name="${fn:escapeXml(dt.tname)}" datatype="${fn:escapeXml(dt.ttype[0].key)}" ${fn:escapeXml(dt.trwx=='400'?'readonly':'')} value="${fn:escapeXml(dt.tvalue)}" /><br />
		</c:if>
		<c:if test="${dt.tuse == 'file'}">
			<c:if test="${empty dt.tvalue && dt.trwx=='420'}">
				<script type="text/javascript">
					var o = new $dswork.upload({io:true, name:"${fn:escapeXml(dt.ttype[0].key)}".toUpperCase(), ext:"${fn:escapeXml(dt.ttype[0].val)}"});
					$(function(){
						o.init({id:"id_${fn:escapeXml(dt.tname)}", vid:"vid_${fn:escapeXml(dt.tname)}", ext:"${fn:escapeXml(dt.ttype[0].val)}"});
					});
				</script>
				<input id="id_${fn:escapeXml(dt.tname)}" type="text" readonly="readonly" />
				<input id="vid_${fn:escapeXml(dt.tname)}" name="${fn:escapeXml(dt.tname)}" type="hidden" value=""/>
			</c:if>
			<c:if test="${not empty dt.tvalue || dt.trwx=='400'}">
				<div>
				<script type="text/javascript">
					var name = "${fn:split(dt.tvalue,':')[0]}";
					if(name.indexOf(".pdf") > 0){
						var url = "show.jsp?filename=" + name + "&name=" + "${fn:escapeXml(dt.ttype[0].key)}".toUpperCase()
						document.write('<iframe name="childframe" src="'+url+'" frameBorder=0 style="width:100%;height:500px;" scrolling="no"></iframe>');
					}
					else if(name.indexOf(".png") > 0 || name.indexOf(".jpg") > 0){
						document.write('<img src="/webio/io/down.jsp?name=' + ("${fn:escapeXml(dt.ttype[0].key)}".toUpperCase() + "&f=" + name) + '">');
					}
					else{
						document.write(name);
					}
					document.write('<input name="${fn:escapeXml(dt.tname)}" type="hidden" value="${fn:escapeXml(dt.tvalue)}" />')
				</script>
				</div>
			</c:if>
		</c:if>
		<c:if test="${dt.tuse == 'dict'}">
			<span id="chk1"></span>
			<script type="text/javascript">
				<c:if test="${dt.trwx=='420'}">
				loaddata("${fn:escapeXml(dt.ttype[0].key)}", ${fn:escapeXml(dt.ttype[0].val)}, "chk1", "radio", "${fn:escapeXml(dt.tname)}");
				</c:if>
				<c:if test="${dt.trwx=='400'}">
				loaddata("${fn:escapeXml(dt.ttype[0].key)}", ${fn:escapeXml(dt.ttype[0].val)}, "chk1", "", "${fn:escapeXml(dt.tname)}");
				document.write('<input name="${fn:escapeXml(dt.tname)}" type="hidden" value="${fn:escapeXml(dt.tvalue)}" />')
				</c:if>
				<c:if test="${dt.trwx=='001'}">
				document.write('<input name="${fn:escapeXml(dt.tname)}" type="hidden" value="${fn:escapeXml(dt.tvalue)}" />')
				</c:if>
			</script>
		</c:if>
		<c:if test="${dt.tuse == 'extend'}">
			<c:forEach items="${dt.ttype}" var="tp" > 
				<script type="text/javascript">document.write("${tp.key}" + ":" + "${tp.val}" + "，");</script>
			</c:forEach> 
			<input name="${fn:escapeXml(dt.tname)}" type="hidden" value="${fn:escapeXml(dt.tvalue)}" />
		</c:if>
	</div>
</c:forEach>
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
		if(m.trwx == "420"){
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
	var arr = [];
	<c:forEach items="${d.ttype}" var="tp">
	var ttype = {};
	ttype.key  = "${tp.key}";
	ttype.val  = "${tp.val}";
	arr.push(ttype);
	</c:forEach>
	row.ttype = arr;
	row.trwx   = "${d.trwx}";
	row.tvalue = "${d.tvalue}";
	map.put(row.tname, row);
</c:forEach>
}
$(function(){
	init();
});

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