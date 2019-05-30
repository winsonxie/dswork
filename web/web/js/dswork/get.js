$(function(){
	var isIe6 = false;
	if (/msie/.test(navigator.userAgent.toLowerCase())){
		if($.browser && $.browser.version && $.browser.version == '6.0'){
			isIe6 = true;
		}
		else if(!$.support.leadingWhitespace){
			isIe6 = true;
		}
	}
	$(".listTable").css({"margin-bottom":"35px"});
	$(".bottomTable").removeClass("bottomTable").addClass("bottomTableFix");/*({padding:"0",margin:"0",width:"100%",position:"fixed",left:"0",bottom:"0","border-top":"#c2c2c2 solid 1px"});*/
	if(isIe6){
		try{$(".bottomTable").before("<div style='visibility:hidden;height:0px;padding:0;margin:0;'></div>");}catch(ie6ex){}
		$(".bottomTable").css({position:"absolute",top:"expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-parseInt(this.currentStyle.marginTop)-parseInt(this.currentStyle.marginBottom)))"});
	}
	$("#listFormDelAll").click(function(){
		var a = $("input[name='keyIndex']:checked");
		var _c = 0;
		for(var i = 0;i < a.length;i++){_c++;}
		if(_c > 0){if(confirm("确认删除吗？")){
			if($dswork.doAjax){$("#listForm").ajaxSubmit($dswork.doAjaxOption);}
			else{$("#listForm").submit();}
			return true;
		}}else{alert("请选择记录！");}
		return false;
	});
	$("[name=keyIndex]").click(function(event){event.stopPropagation();}).dblclick(function(event){event.stopPropagation();});
	//jquery1.5-//$("#chkall").click(function(){$("input[name='keyIndex']").attr("checked", $(this).attr("checked"));});
	$("#chkall").click(function(){$("input[name='keyIndex']").prop("checked", $(this).prop("checked"));});
	$("table.listTable td a.del").click(function(){	alert("请把样式修改为delete");});
	$("table.listTable td a.delete").click(function(){
		if(confirm("确认删除吗？")){
			if($dswork.doAjax){
				var url = $(this).attr("href");
				$dswork.showRequest();
				$.post(url,{},function(data){
					$dswork.showResponse(data);
				});
				return false;
			}
			return true;
		}else{return false;}
	});
	if(!$dswork.tableCSS){
		$("table.listTable tr").each(function(){
			var t = $(this);
			if(!t.hasClass("list_title") && !t.hasClass("nolist")){
				t.addClass(t.index()%2 == 0 ? 'list_even' : 'list_odd');
				t.bind("mouseover", function(){
					$(this).removeClass("list_odd").removeClass("list_over").addClass("list_over");
				});
				t.bind("mouseout", function(){
					$(this).removeClass("list_over").addClass($(this).index()%2 == 0 ? 'list_even' : 'list_odd');
				});
			}
		});
	}
	$("#_querySubmit_[type=button]").click(function(event){$("#queryForm").submit();});
	$("#queryForm").keydown(function(e){
		var v = e || event;
		var keycode = v.which || v.keyCode;
		if (keycode==13) {$("#_querySubmit_[type=button]").click();}
	});
});
$dswork.page = {};
// del,upd,getById在默认时均调用ini方法
$dswork.page.ini = function(url, id, page){location.href = url + (url.indexOf("?")==-1?"?":"&") + "keyIndex=" + id + "&page=" + page;};
// tdObject用于扩展函数时方便从中获取新增的参数
// 可覆盖以下三个方法改为自定义实现
$dswork.page.del = function(event, url, id, page, tdObject){
	if($dswork.doAjax){
		$dswork.showRequest();
		$.post(url,{keyIndex:id, page:page},function(data){
			$dswork.showResponse(data);
		});
	}
	else{$dswork.page.ini(url, id, page);}
};
$dswork.page.upd = function(event, url, id, page, tdObject){$dswork.page.ini(url, id, page);};
$dswork.page.getById = function(event, url, id, page, tdObject){$dswork.page.ini(url, id, page);};
$dswork.page.join = function(td, menu, id){};
$dswork.page.menu = function(delURL, updURL, getByIdURL, page, showContext){
	$("#dataTable>tbody>tr>td.menuTool").each(function(){
		var o = $(this);
		var id = o.attr("keyIndex");
		if(id == null || typeof(id)=="undefined"){return true;}
		var _menu = $('<div class="easyui-menu" style="width:150px;"></div>');
		$dswork.page.join(o, _menu, id);
		if(updURL != null && updURL.length > 0)
		{_menu.append($('<div iconCls="menuTool-update">修改</div>').bind("click", function(event){
			$dswork.page.upd(event, updURL, id, page, o);
		}));}
		if(delURL != null && delURL.length > 0)
		{_menu.append($('<div iconCls="menuTool-delete">删除</div>').bind("click", function(event){
			if(confirm("确认删除吗？")){$dswork.page.del(event, delURL, id, page, o);}
		}));}
		if(getByIdURL != null && getByIdURL.length > 0)
		{_menu.append($('<div iconCls="menuTool-select">明细</div>').bind("click", function(event){
			$dswork.page.getById(event, getByIdURL, id, page, o);
		}));o.parent().css("cursor", "pointer").bind("dblclick", function(event){$dswork.page.getById(event, getByIdURL, id, page, o);});}
		o.append(_menu).append($('<a class="menuTool-rightarrow" href="#">&nbsp;</a>').bind("mouseover", function(event){
			$(".easyui-menu").menu("hide");
			$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});
			event.preventDefault();
		}).bind("click", function(event){
			$(".easyui-menu").menu("hide");
			$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});
			event.preventDefault();
		}).bind("mousemove", function(event){
			$(".easyui-menu").menu("hide");
			$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});
			event.preventDefault();
		}));
		if(showContext == null || showContext){
			o.parent().bind("contextmenu", function(event){
				$(".easyui-menu").menu("hide");
				$(_menu).menu('show', {left: event.clientX, top: o.offset().top + 3});
				event.preventDefault();
			});
		}
		_menu.menu();
	});
};
