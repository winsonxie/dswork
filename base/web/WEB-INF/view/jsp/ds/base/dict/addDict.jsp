<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<%@include file="/commons/include/addAjax.jsp"%>
<script type="text/javascript">
$dswork.callback = function(){if($dswork.result.type == 1){
	location.href = "getDict.htm?mark=${fn:escapeXml(param.mark)}&islist=${fn:escapeXml(param.islist)}&page=${fn:escapeXml(parem.page)}";
}};
$(function(){
	if("${fn:escapeXml(param.islist)}" == "0"){document.getElementById("status0").checked = true;}
});
</script>
</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="listLogo">
	<tr>
		<td class="title">添加</td>
		<td class="menuTool">
			<a class="save" id="dataFormSave" href="#">保存</a>
			<a class="back" href="getDict.htm?mark=${fn:escapeXml(param.mark)}&islist=${fn:escapeXml(param.islist)}&page=${fn:escapeXml(param.page)}">返回</a>
		</td>
	</tr>
</table>
<div class="line"></div>
<form id="dataForm" method="post" action="addDict2.htm">
<table border="0" cellspacing="1" cellpadding="0" class="listTable" id="el">
	<tr>
		<td class="form_title">引用名</td>
		<td class="form_input"><input type="text" name="name" datatype="Char" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">名称</td>
		<td class="form_input"><input type="text" name="label" datatype="Require" maxlength="100" value="" /></td>
	</tr>
	<tr>
		<td class="form_title">分类</td>
		<td class="form_input">
			<label style="cursor: pointer;"><input type="radio" name="status0" @click="choseLevel(1)" />列表集合</label>
			<label style="cursor: pointer;"><input type="radio" name="status0" @click="choseLevel(0)" checked="checked"/>树形集合</label>
			<label style="cursor: pointer;"><input type="radio" name="status0" @click="choseLevel()" />受限树形集合</label>
			<input type="text" name="status" placeholder="请填写限制层级" dataType="IntegerPlus" v-model.number="status" v-show="show" />
			<span style="font-weight:bold;">保存后不可修改</span>
		</td>
	</tr>
	<tr v-if="show">
		<td class="form_title">限制规则</td>
		<td class="form_input">
			<input v-for="v in rules" :key="v.key" type="text" name="rules" placeholder="长度" dataType="IntegerPlus" size="2" />
			<span style="font-weight:bold;">可选</span>
		</td>
	</tr>
</table>
</form>
<script src="https://cdn.bootcss.com/vue/2.6.6/vue.min.js"></script>
<script type="text/javascript">
new Vue({
	el: '#el',
	data: {
		show: false,
		status: 1
	},
	watch: {
		status(val) {
			this.status = val > 20 ? 20 : val;
		}
	},
	computed: {
		rules() {
			return Array.from(Array(this.status)).map((_, i) => ({key: i}));
		}
	},
	methods: {
		choseLevel(v) {
			this.show = (v == 0 || v == 1) ? false : true;
			this.status = (v == 0 || v == 1) ? v : null;
		}
	}
});
</script>
</body>
</html>
