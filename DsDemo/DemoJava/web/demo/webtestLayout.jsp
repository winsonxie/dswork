<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<meta charset="UTF-8"/>
<link rel="stylesheet" type="text/css" href="/web/js/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="/web/themes/default/frame.css"/>
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/web/js/easyui/jquery.easyui.js"></script>
<script type="text/javascript" src="/web/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/webtest/jskey_calendar.js"></script>
</head> 
<body class="easyui-layout" style="font-size:12px;">
<div region="center" data-options="border:false">
<pre>
/**
 * 调用方法：$jskey.calendar
 * 调用方式：$jskey.calendar.show(args);
 * 参数args
 *     目前支持参数：
 *     其中日历类中的参数与其默认值：{skin:"default",min:"yyyy-MM-dd HH:mm:ss",max:"yyyy-MM-dd HH:mm:ss",lang:0,left:0,top:0,format:"yyyy-MM-dd HH:mm:ss",show:"yyyy-MM-dd HH:mm:ss",fn:function(args){}}
 *     日期选择显示格式：yyyy→年，MM→月，dd→天，HH→24小时制，mm→分钟，ss→秒
 *     @param target {target:input对象}
 *     @param skin String "default" 值为themes/calendar目录中对应的样式文件夹
 *     @param min String "yyyy-MM-dd HH:mm:ss"，可选最小值，默认"1582-10-15 00:00:00"，默认的初始化年份为格里高利历实施日，中国从1912年开始实施(民国元年)
 *     @param max String "yyyy-MM-dd HH:mm:ss"，可选最大值，默认"9999-12-31 23:59:59"
 *     @param lang Integer "zh-CN"(简体中文)|"en-US"(英语) 可自由扩充，参考源码中的$jskey.$CalendarLang["zh-CN"]属性
 *     @param left Integer 相对X坐标,相对于文本框的横向偏移量
 *     @param top Integer 相对Y坐标,相对于文本框的纵向偏移量
 *     @param format String "yyyy-MM-dd HH:mm:ss w W" 格式（区分大小写）：yyyy→年，MM→月，dd→天，HH→24小时制，mm→分钟，ss→秒，w→周几，W→当年的第几周(第一周不足七天时也当第一周，计算方式目前为1月1日为第一周开始，且周日为新的一周开始，即一年首尾两周可能不是全周)
 *     @param show String 取值 "yyyy-MM-dd HH:mm:ss" 或 "yyyy-MM-dd HH:mm" 或 "yyyy-MM-dd HH" 或 "yyyy-MM-dd" 或 "yyyy-MM" 或 "yyyy"
 *     @param fn Function 回调函数fn(args)，此args比原始传入的args多出value属性，值为选中后经过show格式化的日期文本
 */
</pre>
<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
<form id="form1" name="form1" method="post" action="" onsubmit="return false;">
<table border="0" width="1000">
	<tr>
		<td>
调用方式：onclick="$jskey.calendar.show({target:this})"<br />
日期图片：class="WebDate"<br />
引用对象的方法<br />
<input type="text" value="" class="WebDate" onclick="$jskey.calendar.show({target:this})" readonly="true" title="" /><br />
<input type="text" value="" id="MyText1" readonly="true" title="" /> <button onclick="$jskey.calendar.show({target:'MyText1'})" class="WebDate" style="width:22px;height:18px;border:none;">&nbsp;</button><br />
<input type="text" value="" id="MyText2" readonly="true" title="" /> <button onclick="$jskey.calendar.show({target:this,fn:function(p){document.getElementById('MyText2').value=p.value;document.getElementById('MyText3').innerHTML=p.value;}})" class="WebDate" style="width:22px;height:18px;border:none;">&nbsp;</button><span id="MyText3"></span><br />
<input type="text" value="1582-10-15 15:58:35" class="WebDate" onclick="$jskey.calendar.show({target:this,lang:'zh-TW',format:'yyyy-MM-dd HH:mm:ss',show:'yyyy-MM-dd HH:mm.ss'})" readonly="true" title="yyyy-MM-dd HH:mm.ss" /><br />
<input type="text" value="2014-11-11 11:59:59" class="WebDate" onclick="$jskey.calendar.show({target:this,min:'2014-10-28 15:47:24',max:'2015-02-12 09:10:11',lang:'zh-CN',format:'yyyy-MM-dd HH:mm:ss',show:'yyyy-MM-dd HH:mm.ss'})" readonly="true" title="yyyy-MM-dd HH:mm.ss" /><br />
</form>
<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
<div id="tt" class="easyui-tabs" style="width:600px;height:400px;">
	<div title="首页" closable="false">
		<div style="overflow:hidden;width:100%;height:100%;">
			<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
			<input type="text" value="jsjs" class="WebDate" onclick="$jskey.calendar.show({target:this,format:'yyyy-MM-dd W',show:'yyyy'})" readonly="true" title="yyyy" /><br />
			
		</div>
	</div>
	<div title="Test" closable="false">
		<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
		<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
		<br />
		<div style="width:400px;height:200px;padding:0 0 0 400px;border:1px solid red;">
			<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
			<input type="text" value="jsjs" class="WebDate" onclick="$jskey.calendar.show({target:this,format:'yyyy-MM-dd W',show:'yyyy'})" readonly="true" title="yyyy" /><br />
			
		</div>
		<input type="text" value="jsjs" class="WebDate" onclick="$jskey.calendar.show({target:this,format:'yyyy-MM-dd W',show:'yyyy'})" readonly="true" title="yyyy" /><br />
		<input type="text" value="2004-06" class="WebDate" onclick="$jskey.calendar.show({target:this,lang:'en-US',format:'yyyy-MM',show:'yyyy-MM'})" readonly="true" title="yyyy-MM" /><br />
		<input type="text" value="2007-06-11" class="WebDate" onclick="$jskey.calendar.show({target:this,skin:'gray',format:'yyyy/MM/dd 周w 第W周',show:'yyyy-MM-dd',left:5,top:-10})" readonly="true" title="yyyy/MM/dd 周w 第W周" /><br />
		
		<input type="text" value="9999-06-11 15:58:35" class="WebDate" onclick="$jskey.calendar.show({target:this,skin:'lightGreen',lang:'en-US',format:'yyyy-MM-dd HH:mm:ss',show:'yyyy-MM-dd HH:mm.ss',fn:function(p){alert(p.show + ' = ' + p.value);}})" readonly="true" title="yyyy-MM-dd HH:mm.ss" /><br />
		</div>
		<select><option>ddddddddddddddddddddddd</option></select>
				</td>
			</tr>
		</table>
		<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
		<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
	</div>
</div>

<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
</body>
</html>
