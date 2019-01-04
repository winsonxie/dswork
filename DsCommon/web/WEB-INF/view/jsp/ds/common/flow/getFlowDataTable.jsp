<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<style type="text/css">
.listLogo{position:fixed;}
.listTable{margin:30px 0 0 0;}
</style>
<script type="text/javascript">
$(function(){
var model = parent.getModel();
var datatable = ""
if(model != null && model != ""){
	paintMtr(datatable, model);
}
});

function paintMtr(datatable, model){
	eval("datatable = " + model);
	for(var i=0; i<datatable.length; i++){
		var $mtr = $("<tr></tr>");
		$mtr.attr("class", "mtr");
		
		var $mtd = $("<td></td>")
		$mtd.attr("style", "width:120px;");
		$mtd.appendTo($mtr);
		$("<div class='line'></div>").appendTo($mtd);
		$("<span>名称：</span>").appendTo($mtd);
		var $talias = $("<input />");
		$talias.attr("type", "text");
		$talias.attr("name", "talias");
		$talias.attr("style", "width:70px;");
		$talias.val(datatable[i].talias);
		$talias.appendTo($mtd);
		
		$("<div class='line'></div>").appendTo($mtd);
		$("<span>字段：</span>").appendTo($mtd);
		var $tname = $("<input />");
		$tname.attr("type", "text");
		$tname.attr("name", "tname");
		$tname.attr("style", "width:70px;");
		$tname.attr("placeholder", "只允许输入字母");
		$tname.attr("onkeyup", "this.value=this.value.replace(/[^a-zA-Z]/g,'')");
		$tname.val(datatable[i].tname);
		$tname.appendTo($mtd);
		$("<div class='line'></div>").appendTo($mtd);

		$('<td class="mtd_tuse form_input"></td>').appendTo($mtr);
		$('<td><input type="button" class="delete" onclick="$(this).parent().parent().remove();" /></td>').appendTo($mtr);
		
		$("#contactTable").append($mtr);
		paintMtdTuse($("#contactTable .mtr .mtd_tuse:last"), datatable[i]);
	}
}

function paintMtdTuse($mtd_tuse, row){
	var _tuse = row.tuse;
	var _ttype = row.ttype;
	
	var $select = $("<select></select>");
	$select.attr("name", "tuse");
	$select.attr("onchange", "tuseChange(this);");
	$("<option></option>").val("common").text("通用").appendTo($select);
	$("<option></option>").val("file").text("文件").appendTo($select);
	$("<option></option>").val("dict").text("字典").appendTo($select);
	$("<option></option>").val("extend").text("扩展").appendTo($select);
	$select.val(_tuse);
	$select.appendTo($mtd_tuse);
	
	var $hidden = $("<input />");
	$hidden.attr("type", "hidden");
	$hidden.val(JSON.stringify(row));
	$hidden.appendTo($mtd_tuse);
	
	var $span = $("<span></span>");
	$span.attr("class", "tuse_extend");
	$span.appendTo($mtd_tuse);
	
	var $btn = $("<input />");
	$btn.attr("type", "button");
	$btn.attr("style", "margin:0 10px;");
	$btn.attr("onclick", "setTypeInfo(this)");
	$btn.val("设置类型信息");
	$btn.appendTo($span);
	
	var info = setInfo(_tuse, _ttype);
	$("<span name='info'>" + info + "</span>").appendTo($span);
}

function setTypeInfo(_obj){
	var data = $(_obj).parent().parent().find("[type=hidden]").val();
	var tuse = $(_obj).parent().parent().find("[name=tuse]").val();
	if(data == "" || (data != "" && JSON.parse(data).tuse != tuse)){
		data = {};
		data.tname = "";
		data.talias = "";
		data.tuse = $(_obj).parent().parent().find("[name=tuse]").val();
		data.trwx = "400";
		data.ttype = "";
		data.tvalue = "";
		data = JSON.stringify(data);
	}
	$jskey.dialog.callback = function(){
		var result = $jskey.dialog.returnValue;
		if(result != null){
			if(result != data){
				$(_obj).parent().parent().find("[type=hidden]").val(result);
				result = JSON.parse(result);
				var _tuse = result.tuse;
				var _ttype = result.ttype;
				var info = setInfo(_tuse, _ttype);
				$(_obj).parent().find("[name=info]").html(info);
			}
		}
	};
	$jskey.dialog.showChooseKey({id:"chooseSystem", title:"设置类型信息", args:{url:"setTypeInfo.htm", data:data}, width:"500", height:"400", closable:false});
}

function tuseChange(_obj){
	var $obj = $(_obj);
	var tuse = $obj.val();
	var hidden = $obj.parent().find("[type=hidden]").val();
	var info = "";
	if(hidden != ""){
		hidden = JSON.parse(hidden);
		if(tuse == hidden.tuse){
			info = setInfo(hidden.tuse, hidden.ttype);
		}
	}
	$(_obj).parent().find("[name=info]").html(info);
}

function setInfo(tuse, ttype){
	var k = "";
	var v = "";
	var info = "";
	if(ttype != ""){
		k = ttype[0].key;
		v = ttype[0].val;
	}
	if(tuse == "common"){
		info = "校验类型：" + k + (k.indexOf("!") < 0 ? "， 必填" : "") + "，默认值：" + v;
	}
	else if(tuse == "file"){
		info = "文件类型：" + k + "，拓展名：" + v;
	}
	else if(tuse == "dict"){
		info = "字典引用名：" + k + "，根节点：" + v;
	}
	else if(tuse == "extend"){
		$.each(ttype, function(index, value){
			info += ttype[index].key + ":" + ttype[index].val + "，"
		});
	}
	return info;
}

function dataTableSave(){
	if(dataCheck()){
		var array = [];
		$("#contactTable .mtr").each(function() {
			var row = $(this).find("input[type=hidden]").val();
			if(row != ""){
				row = JSON.parse(row);
				row.tname = $(this).find("input[name=tname]").val();
				row.talias = $(this).find("input[name=talias]").val();
				array.push(row);
			}
		});
		parent.setModel(JSON.stringify(array));
		//console.log(JSON.stringify(array));
		parent.$jskey.dialog.close();
	}
}

function cancel(){parent.$jskey.dialog.close();}

function dataCheck(){
	var flag = true;
	var talias = new Array();
	var tname = new Array();
	var mtalias = "";
	var mtname = "";
	for (var i = 0; i < $(".listTable [name='talias']").length; i++) {
		talias[i] = $($(".listTable [name='talias']")[i]).val();
		tname[i] = $($(".listTable [name='tname']")[i]).val();
	}
	var taarr = talias.sort();
	var tnarr = tname.sort();
	for(var i = 0; i < talias.length; i++){
		if(taarr[i] == "" && tnarr[i] == ""){
			alert("名称，字段不能为空");
			return false;
		}
		else{
			if(taarr[i] == ""){
				alert("名称不能为空");
				return false;
			}
			if(tnarr[i] == ""){
				alert("字段不能为空");
				return false;
			}
		}
		if (taarr[i] == taarr[i+1]){
			mtalias += taarr[i] + ",";
			flag = false;
		}
		if (tnarr[i] == tnarr[i+1]){
			mtname += tnarr[i] + ",";
			flag = false;
		}
	}
	if(!flag){
		var msg = "";
		if(mtalias != ""){
			msg += "名称" + mtalias.substring(0, mtalias.length - 1) + "重复，";
		}
		if(mtname != ""){
			msg += "字段" + mtname.substring(0, mtname.length - 1) + "重复";
		}
		alert(msg);
	}
	return flag;
}

function setJsonType(){
	var data = parent.getModel();
	$jskey.dialog.callback = function(){
		var result = $jskey.dialog.returnValue;
		if(result != null){
			parent.setModel(result);
			$("#contactTable .mtr").remove();
			paintMtr(datatable="", result);
		}
	};
	$jskey.dialog.showChooseKey({id:"chooseSystem", title:"表结构配置（json）", args:{url:"setJsonType.htm", data:data}, width:"700", height:"400", closable:false});
}

$(function(){
	$(window).scrollTop(10);
	if($(window).scrollTop() > 0){
		$("#end").css({"display":""});
    }
	$(window).scrollTop(0);
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td id="focus"></td>
		<td class="menuTool">
			<a class="graph" id="graph" onclick="setJsonType();return false;" href="#">表结构配置</a>
			<a class="check" id="check" onclick="dataCheck();return false;" href="#">校验信息</a>
			<a class="save" id="dataTableSave" onclick="dataTableSave();return false;" href="#">确定修改</a>
			<a class="close" id="close" onclick="cancel()" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tbody>
	<tr class="list_title">
		<td>基本信息</td>
		<td>类型信息</td>
		<td width="5%" class="menuTool"><a class="insert" onclick="$('#contactTable').append($('#cloneTable>tbody>tr:eq(0)').clone());"></a></td>
	</tr>
	</tbody>
	<tbody id="contactTable">
	</tbody>
	<tbody>
	<tr class="list_title" id="end" style="display:none;">
		<td>基本信息</td>
		<td>类型信息</td>
		<td width="5%" class="menuTool"><a class="insert" onclick="$('#contactTable').append($('#cloneTable>tbody>tr:eq(0)').clone());"></a></td>
	</tr>
	</tbody>
</table>
<table id="cloneTable" hidden="hidden">
	<tr class="mtr">
		<td style="width:120px;">
			<div class="line"></div>
			名称：<input type="text" name="talias" style="width:70px;">
			<div class="line"></div>
			字段：<input type="text" name="tname" style="width:70px;" placeholder="请输入字母"  onkeyup="this.value=this.value.replace(/[^a-zA-Z]/g,'')">
			<div class="line"></div>
		</td>
		<td class="mtd_tuse form_input">
			<input type="hidden" value="" />
			<select name="tuse" onchange="tuseChange(this);">
				<option value="common">通用</option>
				<option value="file">文件</option>
				<option value="dict">字典</option>
				<option value="extend">扩展</option>
			</select>
			<span class="tuse_extend">
				<input type="button" style="margin:0 5px;" onclick="setTypeInfo(this)" value="设置类型信息" />
				<span name="info"></span>
			</span>
		</td>
		<td><input type="button" class="delete" onclick="$(this).parent().parent().remove();" /></td>
	</tr>
</table>
</body>
</html>