//jquery.js和jskey_core.js支持
if(typeof($dswork)!="object"){$dswork={};}
$dswork.date = {};
$dswork.date.callback = function(o, p){};
$dswork.date.show = function(){
	var o = arguments[0];
	var f = arguments[1] || "yyyy-MM-dd";
	var m = $(o);
	var FN = "$dswork.date.callback"
	if(m.attr("fn")){FN = m.attr("fn");}
	var p = {skin:'default', lang:0, format:f, sample:f, fn:FN, min:"1582-10-15 00:00:00", max:"9999-12-31 23:59:59"};
	if(m.attr("tomin")){
		var q = $jskey.$(m.attr("tomin"));
		if($jskey.$isDOM(q) && q.value.length > 0){p.min = $jskey.calendar.$format($jskey.calendar.$toDate(q.value, f), "yyyy-MM-dd HH:mm:ss");}
	}
	if(m.attr("tomax")){
		var q = $jskey.$(m.attr("tomax"));
		if($jskey.$isDOM(q) && q.value.length > 0){p.max = $jskey.calendar.$format($jskey.calendar.$toDate(q.value, f), "yyyy-MM-dd HH:mm:ss");}
	}
	$jskey.calendar.show(o,p);
};

$dswork.uploadURL = function(){return "/webio/io/upload." + ($dswork.dotnet ? "aspx" : "jsp");};
$dswork.ioURL     = function(){return "/webio/io/up." + ($dswork.dotnet ? "aspx" : "jsp");};
$dswork.getChoose      = function(m){m.url = "/web/js/jskey/themes/dialog/jskey_choose.html";return $jskey.dialog.show(m);};
$dswork.getChooseByKey = function(m){m.url = "/web/js/jskey/themes/dialog/jskey_choose_key.html";return $jskey.dialog.show(m);};
$dswork.getChooseDialog      = function(m){return $jskey.dialog.showChoose(m);};
$dswork.getChooseDialogByKey = function(m){return $jskey.dialog.showChooseKey(m);};

$dswork.getWebRootPath = function(){
	var p = window.location.pathname;
	var i = p.indexOf("/");
	if(i != 0){p = p.substring(0, i);return "/" + p;}
	return (p.match(/\/\w+/))[0];
};
var ctx = $dswork.getWebRootPath();//站点虚拟根目录，没有则改为"/"
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g, "").replace(/(^　*)|(　*$)/g, "");};
String.prototype.replaceAll = function(t, u){
	var i = this.indexOf(t), r = "";
	if(i == -1){return this;}
	r += this.substring(0, i) + u;
	if(i + t.length < this.length){r += (this.substring(i + t.length, this.length)).replaceAll(t, u);}
	return r;
};
function attachUrl(){
	var url = arguments[0];
	var frameId = arguments[1] || "mainFrame";
	if(!url){url = "about:blank";}
	else if(url.indexOf("/") == 0){url = ctx + url;}
	document.getElementById(frameId).src = url;
}

$dswork.result = {type:"", msg:"", data:""};
$dswork.checkResult = function(res){
	var o = $dswork.result = {code:-1, type:"", msg:"", data:null};
	try{
		var n;
		if(res){
			if(typeof(res)=="string" || typeof(res)=="number"){
				if(res.length>0 && res.indexOf("{")==0){try{eval("n="+res);}catch(e){}}
				else{
					var _d = (res + "").split(":");
					o.type = _d[0];
					try{o.code = parseInt(_d[0]);}catch(e){}
					if(_d.length > 1){
						o.msg = _d[1];
					}
					else{
						switch(_d[0]){case "0":o.msg = "操作失败";break;case "1":o.msg = "操作成功";break;}
					}
					if(_d.length>2){
						o.data = _d[2];// 这里本应该是个对象的，但为了兼容旧的项目，特殊情况还是为string
					}
				}
			}
			else if(typeof(res)=="object"){n = res;}
			if(n && typeof(n)=="object"){
				try{
					o.code = parseInt((n.code||"-1"));
					o.type = o.code+"";
				}catch(e){}
				try{o.msg = n.msg||(o.code==1?"操作成功":(o.code==0?"操作失败":o.type));}catch(e){}
				try{o.data = n.data||null;}catch(e){}
			}
		}
	}
	catch(e){}
	return $dswork.result.msg;
};

/**
 * 信息控件
 * var o=new $dswork.doAjaxControl();//初始化
 * o.show("显示提示信息");
 * o.hide();//隐藏提示信息
 * o.autoDelayHide=function(html,time)//显示提示信息，并隔time毫秒后关闭
 */
$dswork.doAjaxControl = function(){
	this.callback = null;
	var self = this;
	this.show = function(html){
		var m = $("#div_maskContainer"),w = $(window).width(),h = $(window).height();
		if(m.length == 0){
			var s = "<div id='div_maskContainer'><div id='div_maskMessage'></div><div id='div_maskBackground'></div>";
			if(document.all){s += '<iframe style="left:expression(this.previousSibling.offsetLeft);top:expression(this.previousSibling.offsetTop);width:expression(this.previousSibling.offsetWidth);height:expression(this.previousSibling.offsetHeight);position:absolute;z-index:2000;display:expression(this.previousSibling.style.display);" scrolling="no" frameborder="no"></iframe>';}
			s += "</div>";
			$("body").append(m=$(s));
		}
		var ww = $(document).width(), hh = $(document).height();
		if(ww < w){ww = w;}if(hh < h){hh = h;}
		$("#div_maskBackground").css("top", 0).css("left", 0).css("width", ww).css("height", hh);
		var tip = $("#div_maskMessage");
		tip.html(html||"");
		m.show();
		var x = (w - tip.width()) / 2, y = (h - tip.height()) / 2;
		tip.css("left", x).css("top", y + $(document).scrollTop());
	};
	this.hide = function(){var m = $("#div_maskContainer");if(m.length == 0){return;}m.remove();if(self.callback != null){self.callback();}this.callback = null;};
	this.autoDelayHide = function(html, timeOut){var m = $("#div_maskContainer");if(m.length == 0){this.show(html);}else{$("#div_maskMessage").html(html);}if(timeOut == undefined){timeOut = 3000;}window.setTimeout(this.hide, timeOut);};
};
$dswork.callback     = function(){};
$dswork.validCallBack= function(){return true;};
$dswork.beforeSubmit = function(){if(!$dswork.validCallBack()){return false;}return $jskey.validator.Validate("dataForm", $dswork.validValue || 3);};
$dswork.readySubmit  = function(){};
$dswork.doAjaxObject = new $dswork.doAjaxControl();
$dswork.show         = function(msg, times){$dswork.doAjaxObject.autoDelayHide(msg, times||2000);};
$dswork.showRequest  = function(msg){msg = msg||"正在处理，请稍候";$dswork.doAjaxObject.show(msg+"<img src='/web/js/dswork/loading.gif' />");return true;};
$dswork.showResponse = function(res, callback){$dswork.doAjaxObject.autoDelayHide($dswork.checkResult(res), 2000);$dswork.doAjaxObject.callback = callback||$dswork.callback;};
$dswork.doAjaxShow   = $dswork.showResponse;
$dswork.doAjaxOption = {beforeSubmit:function(formData, jqForm, options){return $dswork.showRequest();},success:function(res, status, xhr){$dswork.showResponse(res);}};
$dswork.doAjax       = true;
$dswork.showTree = function(p){if(typeof(p)!="object"){p={};}
	var ini = {id:"showTree"
		,title:"请选择"
		,url:function(node){return "";}
		,width:400,height:300,left:0,top:0
		,click:function(event, treeId, node){return true;}
		,dataFilter:function(id, pnode, data){return data;}
		,check:function(treeId, node){}
		,async:true,checkEnable:true,chkStyle:"radio"
		,tree:null,nodeArray:[]
	};
	p = $.extend(ini, p);
	var root = {id:"0", pid:"-1", isParent:true, name:p.title, nocheck:true};
	root = $.extend(root, p.root);
	if(p.nodeArray.length == 0){
		p.nodeArray.push(root);
	}
	p.divid = p.id + "_div";
	var $win = $('#'+p.divid);
	if($win.length){$win.css("display", "");}
	else{
		$win = $('<div id="'+p.divid+'" style="z-index:100000;position:absolute;background-color:#ffffff;border:0px;"></div>').appendTo('body');
	}
	if($('#jskey_temp_div_close').length > 0){$winsub.remove();}
	$winsub = $('<div id="jskey_temp_div_close" style="filter:alpha(opacity=1);opacity:0;z-index:99999;top:0px;left:0px;position:absolute;background-color:#ffffff;">&nbsp;</div>').appendTo('body');
	$winsub.css("width", $(document).width()).css("height", $(document).height());
	$winsub.bind("click", function(event){
		$win.css("display", "none");
		$winsub.remove();
		return true;
	});
	$win.css("width", p.width).css("height", p.height).css("left", p.left).css("top", p.top);
	var $tree = $('<ul id="'+p.id+'" class="ztree" style="border:2px solid #999999;overflow:auto;z-index:100000;background-color:#ffffff;"></ul>');
	$tree.css("width", p.width-10).css("height", p.height-10);
	$win.html($tree);
	var cfg = {view:{expandSpeed:""}
		,async:{enable:p.async,dataFilter:p.dataFilter,url:function(id, node){return p.url(node);}}
		,data:{key:{name:"name"},simpleData:{enable:true,idKey:"id",pIdKey:"pid"}}
		,check:{enable:p.checkEnable,chkStyle:p.chkStyle,chkboxType:{"Y":"ps","N":"s"},radioType:"all"}
	};
	cfg.callback = {
		"beforeCheck":p.check,
		"onCheck":function(event, treeId, node){$winsub.click();},
		"onClick":p.click
	};
	$win.css("display", "");
	p.tree = $.fn.zTree.init($tree, cfg, p.nodeArray);
	try{var _pn = p.tree.getNodeByParam("id", p.nodeArray[0].id);p.tree.selectNode(_pn);p.tree.reAsyncChildNodes(_pn, "refresh");}catch(e){}
};

//{sessionKey, fileKey, ext:file, limit:10240(KB), show:true}
$dswork.upload = function(o){
	if(typeof(o) != "object"){o = {};}
	this.sessionKey = o.sessionKey || 1;
	this.fileKey = parseInt(o.fileKey || 1);
	this.ext = o.ext || "file";
	this.count = 1;
	this.limit = o.limit || 10240;
	this.show  = o.show;
	if(this.show == null){this.show = true;}
	this.show == this.show ? true : false;
	this.image = "jpg,jpeg,gif,png";
	this.file =  "bmp,doc,docx,gif,jpeg,jpg,pdf,png,ppt,pptx,rar,rtf,txt,xls,xlsx,zip,7z";
	this.name = o.name || "";
	this.io = o.io;
	if(this.io == null){this.io = false;}
	this.io = this.io ? true : false;
	this.url = o.url || "";
};
$dswork.upload.prototype = {
	init:function(op){try{
	//{id:?,vid:*?,uploadone:,ext};ext :"file|image|***,***",uploadone:"true|false"
	if(typeof(op) != "object"){op = {};}
	this.count++;
	var defaults = {url:this.url,uploadone:"true",vid:"",sessionKey:this.sessionKey,fileKey:this.fileKey+this.count,show:this.show,limit:this.limit,ext:this.ext,name:this.name};
	var p = $.extend(defaults, op);
	p.sessionKey = parseInt(p.sessionKey);
	p.fileKey = parseInt(p.fileKey);
	p.limit = parseInt(p.limit);
	p.buttonid = p.buttonid || "";
	p.$bid = p.buttonid == "" ? p.id + "_span" : p.buttonid;
	p.$sid = p.id + "_showdiv";
	p.$input = $("#" + p.id);
	
	p.$vInput = null;
	if(p.vid != ""){p.$vInput = $("#" + p.vid);}
	if($jskey.upload.swf){
		if(p.ext == "image"){p.types = "*." + $jskey.$replace(this.image, ",", ";*.");}
		else if(p.ext == "file"){p.types = "*." + $jskey.$replace(this.file, ",", ";*.");}
		else{p.types = "*." + $jskey.$replace(p.ext, ",", ";*.");}
		if(p.buttonid == ""){
			p.$input.parent().append('<span id="' + p.$bid + '"></span>');
		}
		if(p.show){p.$input.parent().append('<div id="' + p.$sid + '" style="text-align:left;display:inline;"></div>');}
	}
	else{
		if(p.ext == "image"){p.types = this.image;}
		else if(p.ext == "file"){p.types = this.file;}
		else{p.types = p.ext;}
		if(p.buttonid == ""){
			p.$input.parent().append('<img id="' + p.$bid + '" style="cursor:pointer;width:61px;height:22px;border:none;vertical-align:middle;margin-top:-3px;" src="/web/js/jskey/themes/plupload/UploadButton.png"/>');
		}
		if(p.show){p.$input.parent().append('<div id="' + p.$sid + '" style="text-align:left;height:22px;display:inline;"></div>');}
	}
	if(p.url == ""){
		if(this.io){
			p.url = $dswork.ioURL() + ($dswork.ioURL().indexOf("?") > 0 ? "&" : "?") + "name=" + this.name + "&ext=" + p.ext;
		}
		else{
			p.url = $dswork.uploadURL() + ($dswork.uploadURL().indexOf("?") > 0 ? "&" : "?") + "sessionkey=" + p.sessionKey + "&filekey=" + p.fileKey + "&ext=" + p.ext + "&uploadone=" + (p.uploadone=="true"?"true":"false");
		}
	}
	p.success = (typeof(p.success) == "function" ? p.success : function(p){});
	p.clear = (typeof(p.clear) == "function" ? p.clear : function(p){});
	var ps = {
		url:p.url,
		browse_button : p.$bid,
		unique_names : false,
		filters : {
			max_file_size : p.limit + "kb",
			mime_types: [{title : p.types, extensions : p.types}]
		},
		debug:false,
		settings:{div:p.show?p.$sid:"",success:function(data){// {arr:[{id,name,size,state,file,type,msg}],msg:"",err:""}
			if(typeof (data.err) == 'undefined'){alert("upload error");return false;}
			if(data.err != ''){alert(data.err);}
			var o,_has = p.$input.attr("has") || "";
			if(p.vid == ""){
				var ok = false;
				for(var i = 0;i < data.arr.length;i++){o = data.arr[i];if(o.state == "1"){ok = true;}}
				p.$input.val(ok ? p.fileKey : _has);
			}
			else{
				var v = p.$vInput.val();
				if(p.uploadone == "true"){v = "";}
				for(var i = 0;i < data.arr.length;i++){o = data.arr[i];if(o.state == "1"){v += (v != "" ? "|" : "") + o.file + ":" + o.name;}}
				p.$vInput.val(v);
				p.$input.val(v == "" ? _has : p.fileKey);
			}
			var v = p.$input.val();
			if(v != "" && v != 0){p.$input.attr("msg","");if(p.show){var s = $("#" + p.$sid), btn = $('<input class="button" type="button" value="取消上传" />');s.html('&nbsp;');s.append(btn);btn.bind("click", function(){p.$input.val(p.$input.attr("has") || "");if(p.vid!=""){p.$vInput.val("");}s.html("");p.$input.attr("msg","请上传文件");p.clear(p);});}}
			p.success(p);
		}}
	};
	// 兼容swfupload
	ps.custom_settings = ps.settings;
	ps.button_placeholder_id = ps.browse_button;
	ps.file_types = p.types;
	ps.file_size_limit = p.limit;
	
	return $jskey.upload.init(ps);
	}catch(e){alert("upload init error\n" + e.name + "\n" + e.message);return null;}}
};
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
$dswork.page.menu = function(delURL, updURL, getByIdURL, page, showContext){$("#dataTable>tbody>tr>td.menuTool").each(function(){
	var o = $(this);var id = o.attr("keyIndex");
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
		$(".easyui-menu").menu("hide");$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});event.preventDefault();
	}).bind("click", function(event){
		$(".easyui-menu").menu("hide");$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});event.preventDefault();
	}).bind("mousemove", function(event){
		$(".easyui-menu").menu("hide");$(_menu).menu('show', {left: $(this).offset().left + 16, top: o.offset().top + 3});event.preventDefault();
	}));
	if(showContext == null || showContext){
		o.parent().bind("contextmenu", function(event){$(".easyui-menu").menu("hide");$(_menu).menu('show', {left: event.clientX, top: o.offset().top + 3});event.preventDefault();});
	}
	_menu.menu();
});};
$dswork.confirm = "确定保存吗？";
$(function(){
	$("input").each(function(){var o = $(this);if(o.hasClass("WebDate") && o.attr("format")){o.bind("click", function(event){$dswork.date.show(this, o.attr("format"));});o.prop("readonly", true);}});
	$("select").each(function(){var o = $(this);var v = o.attr("v");
		if(v == null || typeof(v)=="undefined"){return true;}// false为跳出each循环，true为跳出当前循环
		try{o.val(v);}catch(e){}try{if(o.val() != v){o.prop("selectedIndex", 0);}}catch(e){}
	});
	$("input[type='radio']").each(function(){var o = $(this);var v = o.attr("v");var n = o.attr("name");
		if(v == null || typeof(v)=="undefined"){return true;}
		try{var _c = false, _e = $jskey.$byName(n);for(var i = 0;i < _e.length;i++){if(_e[i].value == v){_e[i].checked = true;_c = true;}}if(!_c){_e[0].checked = true;}}catch(e){}
	});
	
	try{
	$("#_querySubmit_[type=button]").click(function(event){$("#queryForm").submit();});
	$("#queryForm").keydown(function(e){var v = e||event;var keycode = v.which||v.keyCode;if (keycode==13) {$("#_querySubmit_[type=button]").click();}});
	}catch(e){}
	
	try{
	$("#dataForm").submit(function(event){
		if($dswork.doAjax){event.preventDefault();try{$("#dataFormSave").click();}catch(e){}}
	});
	$("#dataFormSave").click(function(){
		if($dswork.beforeSubmit()){if(confirm($dswork.confirm)){
			$dswork.readySubmit();
			if($dswork.doAjax){$("#dataForm").ajaxSubmit($dswork.doAjaxOption);}
			else{$("#dataForm").submit();}
		}}return false;
	});
	}catch(e){}
	
	try{$(".form_title").css("width", "20%");}catch(e){}
	
	var hasList = $("#listForm").length > 0 || $("#dataTable").length > 0;
	if(!hasList){return;}
	try{
	$(".listTable").css({"margin-bottom":"35px"});
	var isIe6 = false;
	if(/msie/.test(navigator.userAgent.toLowerCase())){if($.browser && $.browser.version && $.browser.version == '6.0'){isIe6 = true;}else if(!$.support.leadingWhitespace){isIe6 = true;}}
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
	$(".listFormDelAll").click(function(){$("#listFormDelAll").click();});
	$("[name=keyIndex]").click(function(event){event.stopPropagation();}).dblclick(function(event){event.stopPropagation();});
	//jquery1.5-//$("#chkall").click(function(){$("input[name='keyIndex']").attr("checked", $(this).attr("checked"));});
	$("#chkall").click(function(){$("input[name='keyIndex']").prop("checked", $(this).prop("checked"));});
	$("table.listTable td a.delete").click(function(){
		if(confirm("确认删除吗？")){if($dswork.doAjax){var url = $(this).attr("href");$dswork.showRequest();$.post(url,{},function(data){$dswork.showResponse(data);});return false;}return true;}else{return false;}
	});
	if(!$dswork.tableCSS){
		$("table.listTable tr").each(function(){var t = $(this);
			if(!t.hasClass("list_title") && !t.hasClass("nolist")){t.addClass(t.index()%2 == 0 ? 'list_even' : 'list_odd');
				t.bind("mouseover", function(){$(this).removeClass("list_odd").removeClass("list_over").addClass("list_over");});
				t.bind("mouseout", function(){$(this).removeClass("list_over").addClass($(this).index()%2 == 0 ? 'list_even' : 'list_odd');});
			}
		});
	}
	}catch(e){}
});