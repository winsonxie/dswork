<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="dswork.flow.DsFactoryry, dswork.web.MyRequest, dswork.common.model.*, java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String msg = "";
MyRequest req = new MyRequest(request);
long wid = req.getLong("wid");
if(wid > 0)
{
	IFlowWaiting po = DsFactory.getFlow().getWaiting(wid);
	request.setAttribute("po", po);
	request.setAttribute("map", DsFactory.getUtil().getMap(po.getDatatable()));
	
	request.setAttribute("m", DsFactory.getUtil().getM(po.getDatatable()));
	/* request.setAttribute("rows", DsFactory.getUtil().getRows(po.getDatatable())); */
	request.setAttribute("html", DsFactory.getUtil().getHtml(po.getDatatable()));
	
	java.util.Map<String, String> tmap = DsFactory.getFlow().getTaskList(po.getFlowid());
	request.setAttribute("tmap", tmap);
	String[] arr = po.getTnext().split("\\|", -1);
	request.setAttribute("arr", arr);
}
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/upd.jsp"%>
<%@include file="/commons/include/datatable.jsp"%>
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
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr>
		<td class="form_title">流程名称</td>
		<td class="form_input">${fn:escapeXml(po.flowname)}</td>
	</tr>
	<tr>
		<td class="form_title">当前任务</td>
		<td class="form_input">${fn:escapeXml(po.tname)}</td>
	</tr>
	<tr>
		<td class="form_title">下一步任务</td>
		<td class="form_input">
			<c:forEach items="${arr}" var="n">
				<select name="taskList">
					<c:forEach items="${fn:split(n, ',')}" var="m">
						<option value="${fn:escapeXml(m)}">${fn:escapeXml(tmap[m])}</option>
					</c:forEach>
				</select>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="form_title">状态</td>
		<td class="form_input">
			<label><input type="radio"  name="resultType" value="1" checked="checked" />拟同意</label>
		&nbsp;<label><input type="radio"  name="resultType" value="0" />拟拒绝</label>
		&nbsp;<label><input type="radio"  name="resultType" value="-1" />拟作废</label>
		</td>
	</tr>
	<tr>
		<td class="form_title">意见</td>
		<td class="form_input">
			<textarea name="resultMsg" style="width:100%;height:100px;">无</textarea>
		</td>
	</tr>
</table>
<input type="hidden" name="wid" value="${fn:escapeXml(po.id)}" />
<input type="hidden" id="datatable" name="datatable" value="${fn:escapeXml(po.datatable)}" />
</form>
<form id="formdata">
<%-- ${html} --%>
<!-- 

 -->
</form>
<script id="tpl" type="text/tmpl">
<c:if test="${po.dataview == ''}">
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
{{# var keys = Object.keys(d).sort();}}
{{# for(var i=0,len=keys.length; i<len; i++){ var row = d[keys[i]]; }}
	<tr {{ row.trwx=='001'?'style="display:none;"':'' }}>
		<td class="form_title">{{ row.talias }}</td>
		<td class="form_input">
			{{# if(row.tuse == "common"){ }}
				{{# if(row.trwx == "420"){ }}
					<input type="{{row.trwx=="001"?"hidden":"text"}}" name="{{row.tname}}" value="{{row.tvalue}}" datatype="{{row.ttype[0].key}}" {{row.trwx=="400"?"readonly":""}} />
				{{# }else{ }}
					<input type="{{row.trwx=="001"?"hidden":"text"}}" name="{{row.tname}}" value="{{row.tvalue}}" {{row.trwx=="400"?"readonly":""}} />
				{{# } }}
			{{# }else if(row.tuse == "dict"){ }}
				{{# if(row.trwx == "420"){ }}
					<span id="{{ row.tname }}"></span>
					{{# loaddata(row.ttype[0].key, row.ttype[0].val, row.tname, "radio", row.tname); }}
				{{# }else{ }}
					<input type="{{row.trwx=="001"?"hidden":"text"}}" name="{{row.tname}}" value="{{row.tvalue}}" {{row.trwx=="400"?"readonly":""}} />
				{{# } }}
			{{# }else if(row.tuse == "file"){ }}
				{{# if(row.trwx == "420"){ }}
					<input id="id_{{row.tname}}" type="hidden" readonly="readonly" />
					<input id="vid_{{row.tname}}" name="{{row.tname}}" type="hidden" value="{{row.tvalue}}"/>
					{{# uploadFile(row);}}
				{{# }else{ }}
					<input type="{{row.trwx=="001"?"hidden":"text"}}" name="{{row.tname}}" value="{{row.tvalue}}" {{row.trwx=="400"?"readonly":""}} />
				{{# } }}
			{{# }else if(row.tuse == "extend"){ }}
				<input type="{{row.trwx=="001"?"hidden":"text"}}" name="{{row.tname}}" value="{{row.tvalue}}" {{row.trwx=="400"?"readonly":""}} />
			{{# } }}
		</td>
	</tr>
{{# } }}
</table>
</c:if>
${po.dataview}
</script>
<script type="text/javascript">
$(function(){
	var tpl = document.getElementById('tpl').innerHTML;
	var res = ${m};
	laytpl(tpl).render(res, function(render){
	   document.getElementById('formdata').innerHTML = render;
	   try{$(".form_title").css("width", "20%");}catch(e){}
	});
})
function uploadFile(row){
	var o = new $dswork.upload({io:true, name:row.ttype[0].key, ext:row.ttype[0].val});
	$(function(){
		o.init({id:"id_"+row.tname, vid:"vid_"+row.tname, ext:row.ttype[0].val});
	})
}
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
	console.log(array)
	$("#datatable").val(JSON.stringify(array));
}
function init(){
<c:forEach items="${map}" var="d">
	var row = {};
	row.tname  = "${d.value.tname}";
	row.talias = "${d.value.talias}";
	row.tuse   = "${d.value.tuse}";
	var arr = [];
	<c:forEach items="${d.value.ttype}" var="tp">
	var ttype = {};
	ttype.key  = "${tp.key}";
	ttype.val  = "${tp.val}";
	arr.push(ttype);
	</c:forEach>
	row.ttype = arr;
	row.trwx   = "${d.value.trwx}";
	row.tvalue = "${d.value.tvalue}";
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
</body>
</html>