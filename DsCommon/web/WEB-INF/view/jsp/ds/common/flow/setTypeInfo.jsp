<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$(function(){
var model = parent.getModel();
var row = ""
if(model != null && model != ""){
	eval("row = " + model);
	var tuse = row.tuse;
	var ttype = row.ttype;
	
	$("#titile").html((tuse=="common"?"通用":tuse=="file"?"文件":tuse=="dict"?"字典":"自定义") + "类型<input type='hidden' id='row' value='" + model + "' />");
	
	var $mtd_f = "";
	var $mtd_l = "";
	if(tuse == "common" || tuse == "file" || tuse == "dict"){
		$("#contactTable .menuTool").remove();
		$(".listLogo .menuTool .check").remove();
		
		var $mtr = $("<tr></tr>");
		$mtr.attr("class", "mtr");
		$("#contactTable").append($mtr);
		
		var $mtd = $("<td></td><td></td>");
		$mtd.attr("class", "mtd");
		$mtd.appendTo($mtr);
		$mtd_f = $mtr.find(".mtd:first");
		$mtd_l = $mtr.find(".mtd:last");
	}
	
	var k = "";
	var v = "";
	if(ttype != ""){
		k = ttype[0].key;
		v = ttype[0].val;
	}
	
	if(row.tuse == "common"){
		paintCommon($mtd_f, $mtd_l, k, v);
	}
	else if(row.tuse == "file"){
		paintFile($mtd_f, $mtd_l, k, v);
	}
	else if(row.tuse == "dict"){
		paintDict($mtd_f, $mtd_l, k, v);
	}
	else if(row.tuse == "extend"){
		if(ttype != ""){
			$.each(ttype, function(index, value){
				paintExtend(ttype[index].key, ttype[index].val);
			});
		}
	}
}

});

function paintExtend(k, v){
	var $mtr = $("<tr></tr>");
	$mtr.attr("class", "mtr");
	$("#contactTable").append($mtr);
	var $mtd = $("<td></td><td></td><td></td>");
	$mtd.attr("class", "mtd");
	$mtd.appendTo($mtr);
	$mtd_f = $mtr.find(".mtd:first");
	$mtd_s = $mtr.find(".mtd:eq(1)");
	$mtd_l = $mtr.find(".mtd:last");
	
	$("<span>key：</span>").appendTo($mtd_f);
	var $k = $("<input />");
	$k.attr("type", "text");
	$k.attr("name", "k");
	$k.attr("placeholder", "请输入字母或数字");
	$k.attr("onkeyup", "this.value=this.value.replace(/[^\\w\\.\/]/ig,'')");
	$k.val(k);
	$k.appendTo($mtd_f);
	
	$("<span>val：</span>").appendTo($mtd_s);
	var $v = $("<input />");
	$v.attr("type", "text");
	$v.attr("name", "v");
	$v.val(v);
	$v.appendTo($mtd_s);
	
	var $input = $("<input />");
	$input.attr("type", "button");
	$input.attr("class", "delete");
	$input.attr("onclick", "$(this).parent().parent().remove();");
	$input.appendTo($mtd_l);
}

function paintDict($mtd_f, $mtd_l, k, v){
	$("<span>引用名：</span>").appendTo($mtd_f);
	var $k = $("<input />");
	$k.attr("type", "text");
	$k.attr("name", "k");
	$k.attr("placeholder", "字典引用名");
	$k.attr("onkeyup", "this.value=this.value.replace(/[^a-zA-Z]/g,'')");
	$k.val(k);
	$k.appendTo($mtd_f);
	
	$("<span>&nbsp;根节点：</span>").appendTo($mtd_l);
	var $v = $("<input />");
	$v.attr("type", "text");
	$v.attr("name", "v");
	$v.attr("placeholder", "字典根节点");
	$v.val(v);
	$v.appendTo($mtd_l);
}

function paintFile($mtd_f, $mtd_l, k, v){
	$("<span>文件类型：</span>").appendTo($mtd_f);
	var $k = $("<select></select>");
	$k.attr("name", "k");
	$("<option></option>").val("file").text("文件").appendTo($k);
	$("<option></option>").val("img").text("图片").appendTo($k);
	if(k != ""){
		$k.val(k);
	}
	$k.appendTo($mtd_f);
	
	$("<span>&nbsp;拓展名：</span>").appendTo($mtd_l);
	var $v = $("<input />");
	$v.attr("type", "text");
	$v.attr("name", "v");
	$v.attr("style", "width:150px;");
	$v.attr("placeholder", "如：pdf,png,jpg...");
	$v.attr("onkeyup", "this.value=this.value.replace(/[^a-z,A-Z]/g,'')");
	$v.val(v);
	$v.appendTo($mtd_l);
}

function paintCommon($mtd_f, $mtd_l, k, v){
	$("<span>格式校验：</span>").appendTo($mtd_f);
	var $k = $("<select></select>");
	$k.attr("name", "k");
	$("<option></option>").val("!Require").text("字符串").appendTo($k);
	$("<option></option>").val("!Chinese").text("中文").appendTo($k);
	$("<option></option>").val("!WebDate").text("日期").appendTo($k);
	$("<option></option>").val("!Char").text("英、数、下划线").appendTo($k);
	$("<option></option>").val("!IdCard").text("身份证号码").appendTo($k);
	$("<option></option>").val("!Mobile").text("手机号码").appendTo($k);
	$("<option></option>").val("!Money").text("金额").appendTo($k);
	$("<option></option>").val("!Email").text("邮件格式").appendTo($k);
	$("<option></option>").val("!Integer").text("纯数字").appendTo($k);
	$("<option></option>").val("!UnitCode").text("统一社会信用代码").appendTo($k);
	if(k != ""){
		$k.val(k.indexOf("!") < 0 ? "!" + k : k);
	}
	$k.appendTo($mtd_f);
	
	$("<span>&nbsp;必填：</span>").appendTo($mtd_f);
	var $required = $("<input />");
	$required.attr("type", "checkbox");
	if(k != "" && k.indexOf("!") < 0){
		$required.attr("checked", "checked");
	}
	$required.appendTo($mtd_f);
	
	$("<span>&nbsp;默认值：</span>").appendTo($mtd_l);
	var $v = $("<input />");
	$v.attr("type", "text");
	$v.attr("name", "v");
	$v.val(v);
	$v.appendTo($mtd_l);
}

function dataTableSave(){
	var row = $("#row").val();
	row = JSON.parse(row);
	var tuse = row.tuse;
	var k = $("#contactTable .mtr").find("[name=k]").val();
	var v = $("#contactTable .mtr").find("[name=v]").val();
	var tarr = [];
	if(tuse == "common" || tuse == "file" || tuse == "dict"){
		if(tuse == "common"){
			if($("#contactTable .mtr").find("[type=checkbox]").is(':checked')){
				k = k.replace("!", "");
			}
			row.tvalue = v;
		}
		var ttype = {};
		ttype.key = k;
		ttype.val = v;
		tarr.push(ttype);
	}
	else if(tuse == "extend"){
		tarr = []
		if(dataCheck()){
			$("#contactTable .mtr").find("[name=k]").each(function(){
				var ttype = {};
				ttype.key = $(this).val();
				ttype.val = $(this).parent().parent().find("[name=v]").val();
				tarr.push(ttype);
			});
		}
		else{
			return;
		}
	}
	row.ttype = tarr;
	parent.setModel(JSON.stringify(row));
	//console.log(JSON.stringify(row));
	parent.$jskey.dialog.close();
}
function cancel(){parent.$jskey.dialog.close();}

function dataCheck(){
	var flag = true;
	var karr = new Array();
	var msg = "";
	if($(".listTable [name=k]").length <= 0){
		alert("请添加key和val值");
		return false;
	}
	for (var i = 0; i < $(".listTable [name=k]").length; i++) {
		karr[i] = $($(".listTable [name=k]")[i]).val();
	}
	var arr = karr.sort();
	for(var i = 0; i < karr.length; i++){
		if(arr[i] == "" && arr[i] == ""){
			alert("key值不能为空");
			return false;
		}
		if (arr[i] == arr[i+1]){
			msg += arr[i] + ",";
			flag = false;
		}
	}
	if(!flag){
		if(msg != ""){
			msg = "key值" + msg.substring(0, msg.length - 1) + "重复";
			alert(msg);
		}
	}
	return flag;
}
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td id="titile"></td>
		<td class="menuTool">
			<a class="check" id="check" onclick="dataCheck();return false;" href="#">校验信息</a>
			<a class="save" id="dataTableSave" onclick="dataTableSave();return false;" href="#">确定修改</a>
			<a class="close" id="close" onclick="cancel()" href="#">取消修改</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="contactTable">
	<tr class="list_title">
		<td>key</td>
		<td>val</td>
		<td width="5%" class="menuTool"><a class="insert" onclick="$('#contactTable>tbody').append($('#cloneTable>tbody>tr:eq(0)').clone());"></a></td>
	</tr>
</table>
<table id="cloneTable" hidden="hidden">
	<tr class="mtr">
		<td>
			key：<input type="text" name="k" onkeyup="this.value=this.value.replace(/[^\w\.\/]/ig,'')" placeholder="请输入字母或数字" >
		</td>
		<td>
			val：<input type="text" name="v">
		</td>
		<td><input type="button" class="delete" onclick="$(this).parent().parent().remove();" /></td>
	</tr>
</table>
</body>
</html>