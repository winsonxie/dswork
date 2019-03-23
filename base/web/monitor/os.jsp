<%@page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title></title>
	<style type="text/css">
.listTable {width:100%;background-color:#999;border:none;}/*数据表格*/
.listTable * {font-size:14px;font-weight:bold;color:#000;}

.listTable tr {height:25px;padding:0px 2px 0px 2px;}
.listTable td {vertical-align:middle;text-align:center;word-wrap:break-word;word-break:break-all;}
.listTable tr.list_title {height:30px;}
.listTable tr.list_title td{background-color:#ccc;}

td.form_title, td.form_input {height:25px;padding-left:3px;padding-right:3px;}
td.form_title {text-align:right;background-color:#eee;}
td.form_input {text-align:left;background-color:#eee;}

		td.memory {line-height:28px;vertical-align:top;}
		td.memory div {line-height:28px;height:28px;text-align:left;overflow:hidden;}
		td.memory div span {line-height:28px;height:28px;text-align:left;overflow:hidden;font-weight:bold;}
	</style>
	<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
	<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
</head>
<body style="min-width:600px;">
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td colspan="4">系统信息</td>
	</tr>
	<tr>
		<td class="form_title" style="width:20%;">操作系统：</td>
		<td class="form_input" id="osname"></td>
	</tr>
	<tr>
		<td class="form_title">内存总量：</td>
		<td class="form_input" id="os_memory"></td>
	</tr>
	<tr>
		<td class="form_title">处理器核数：</td>
		<td class="form_input" id="oscpu"></td>
	</tr>
	<tr>
		<td class="form_title">虚拟内存：</td>
		<td class="form_input">总量：<span id="os_swap"></span>&nbsp;空闲：<span id="os_swap_free"></span></td>
	</tr>
</table>
<div class="line"></div>
<table border="0" cellspacing="1" cellpadding="0" class="listTable">
	<tr class="list_title">
		<td style="width:50%;">物理内存状态</td>
		<td style="width:50%;">JAVA虚拟机运行时状态</td>
	</tr>
	<tr>
		<td class="form_input memory">
			<div>
				空闲物理内存：<span id="os_memory_free"></span>
			</div>
			<div>
				已用物理内存：<span id="os_memory_nofree"></span>
			</div>
			<div style="width:100%;position:relative;height:30px;line-height:30px;background-color:green;text-align:right;">
				<div id="memoryUseTotalDiv" style="width:10%;height:30px;line-height:30px;float:left;background-color:red;display:block;">&nbsp;</div>
				<div id="memoryUseTotal" style="position:absolute;top:0;left:0;height:30px;line-height:30px;width:100px;padding:0 0 0 10px;color:white;text-align:left;">
					0%
				</div>
				<span id="memoryTotal" style="color:white;padding:0 10px 0 0;"></span>
			</div>
			<div>JDK与操作系统位数不一致时，数据可能不准确</div>
		</td>
		<td class="form_input memory">
			<div>
				可使用最大内存：<span id="run_memory_max"></span>
			</div>
			<div>
				运行时内存总量：<span id="run_memory_total"></span>
			</div>
			<div>
				运行时空闲内存：<span id="run_memory_free"></span>
			</div>
			<div>
				可用处理器核数：<span id="run_cpu"></span>
			</div>
		</td>
	</tr>
</table>
</body>
<script type="text/javascript">
function getInfo(){$.ajax({
	url:"os_info.jsp?" + (new Date().getTime()),
	type:"post",
	data:{},
	success:function(m){
		try{
			eval("m=" + m + ";");
			$("#osname").html(m.osname + " (" + m.osarch + ") NT" + m.osversion);
			//$("#osversion").html(m.osversion);
			$("#oscpu").html(m.oscpu);
			var os_memory = parseInt(m.os_memory);
			var os_swap = parseInt(m.os_swap);
			var os_memory_free = parseInt(m.os_memory_free);
			var os_swap_free = parseInt(m.os_swap_free);
			
			
			$("#os_memory").html(os_memory + " MB");
			$("#os_swap").html(os_swap + " MB");
			$("#os_memory_free").html(os_memory_free + " MB");
			$("#os_swap_free").html(os_swap_free + " MB");

			
			$("#os_memory_nofree").html((os_memory-os_memory_free) + " MB");
			//$("#os_swap_nofree").html((os_swap-os_swap_free) + " MB");
			
			
			var memoryTotal = os_memory;// + os_swap;
			var memoryFreeTotal = os_memory_free;// + os_swap_free;
			var memoryUseTotal = memoryTotal - memoryFreeTotal;
			var percent = parseInt((memoryUseTotal*100)/memoryTotal);
			$("#memoryUseTotalDiv").css("width", percent + "%");
			$("#memoryUseTotal").html(percent + "%");
			$("#memoryTotal").html(memoryTotal + "MB");
			
			
			$("#run_memory_total").html(m.run_memory_total + " MB");
			$("#run_memory_free").html(m.run_memory_free + " MB");
			$("#run_memory_max").html(m.run_memory_max + " MB");
			
			$("#run_cpu").html(m.run_cpu);
		}
		catch(e){
			alert(e.message);
		}
		setTimeout(getInfo, 5000);
	},
	error:function(){
		alert("加载信息出错，请重试");
		setTimeout(getInfo, 5000);
	}
});}
setTimeout(getInfo, 1);
</script>
</html>
