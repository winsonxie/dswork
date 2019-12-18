﻿if(typeof ($jskey) != "object"){$jskey = {};}


;!function(){"use strict";




var i_close = "&#xf1016;",i_open = "&#xf1017;",i_N  = "&nbsp;", i_TC, i_TO, i_LC, i_LO, i_T, i_L, i_I;

i_TC = "&#xf1016;";// T close
i_TO = "&#xf1017;";// T open
i_LC = "&#xf1016;";// L close
i_LO = "&#xf1017;";// L open
i_T  = "&nbsp;";// T
i_L  = "&nbsp;";// L
i_I  = "&nbsp;";// I

$jskey.menu =
{
num:1,
list:[],
load:function(index, id){},//异步加载菜单
root:"",
path:"",
imgPath:"",
isTabs:true,// 是否开启选项卡,
$:function(id){return document.getElementById(id);},
//初始化操作
reset:function(){this.list = [];},
//添加一级菜单下的一组子菜单的菜单项
put:function(title, id, html){
	this.list[this.list.length] = {"title":title, "id":id, "html":html};
},
//创建一层菜单内容
createCell:function(o, c, last){
	var html = '';//<tr>
	html += '<div id="JskeyMT_' + c + '" class="menu menu-close" onclick="$jskey.menu.clickBar(' + c + ')"' + (last?' style="display:none;"':'') + '>';
	html += '<i id="JskeyI' + 'JskeyMT_' + c + '">'+i_close+'</i>';
	html += o.title + '</div>';
	html += '';//</tr><tr>
	html += '<div id="JskeyMC_' + c + '" valign="top" class="cell cell-close" style="' + (last?'display:;height:50px;':'display:none;height:') + '">';
	html += '<div class="cell-content" id="JskeyMCD_' + c + '">';
	html += ((o.html != "")?o.html:' ');
	html += '</div></div>';//</tr>
	return html;
},
//创建一层菜单内容
create:function(){
	var len = this.list.length;
	if(len < 1){return;}
	var html = '<div id="JskeyMG" class="jskeymenu">';
	for(var c = 0;c < len;c++){html += this.createCell(this.list[c], c, false);}
	html += this.createCell({"title":"", "id":"", "html":""}, len, true);// 在最后补一个看不见的，用于收缩
	html += '</div>';// </table>
	try{if(this.$("JskeyMG") != null){document.body.removeChild(this.$("JskeyMG"));}}catch(e){}
	document.body.innerHTML += html;
},
//滑动菜单
smoothMenu:function(cOpen, cClose, stat){
	var timeDelay = 20;
	var moveHight = 20;
	if(stat == 0){
		this.$("JskeyMC_" + cOpen).style.display = "";
		setTimeout("$jskey.menu.smoothMenu(" + cOpen + "," + cClose + "," + moveHight + ")", timeDelay);
	}
	else{
		if(stat > 100){stat = 100;}
		this.$("JskeyMC_" + cOpen).style.height = stat + "%";
		this.$("JskeyMC_" + cClose).style.height = (100 - stat) + "%";
		if(stat < 100){
			stat += moveHight;
			setTimeout("$jskey.menu.smoothMenu(" + cOpen + "," + cClose + "," + stat + ")", timeDelay);
		}
		else{
			this.$("JskeyMC_" + cClose).style.display = "none";
		}
	}
},
//判断点了对应的菜单块
clickBar:function(c){
	if(c >= 0){
		try{this.load(c, this.list[c].id);}catch(e){}
		var isClose = this.$("JskeyMC_" + c).style.display == "";
		if(isClose){
			this.$("JskeyMT_" + c).className = "menu menu-close";
			this.$("JskeyMC_" + c).className = "cell cell-close";
			this.$("JskeyI" + "JskeyMT_" + c).innerHTML = i_close;
			this.$("JskeyMC_" + c).style.display = "none";
		}
		else{
			this.$("JskeyMT_" + c).className = "menu menu-open";
			this.$("JskeyMC_" + c).className = "cell cell-open";
			this.$("JskeyI" + "JskeyMT_" + c).innerHTML = i_open;
			this.$("JskeyMC_" + c).style.display = "";
		}
	}
},
//用于异步回调，或刷新指定层次菜单
showNode:function(index, data, url){
	url = url || "";
	try{this.showNodeHTML(index, $jskey.menu.getNodeHtml({name:this.list[index].title, items:data, _root:url}));}catch(e){};
},
showNodeHTML:function(index, html){
	try{this.list[index].html = html;this.$("JskeyMCD_" + index).innerHTML = html;}catch(e){};
},
// 单击
click:function(itemid){
},// 单击
before:function(id, url, pname, name){
	return {"itemid":id, "url":url, "parentname":pname, "nodename":name};
},
beforeClick:function(p){
	return p;
},
//鼠标移过
imgMouse:function(obj, v, img, imgOpen, over){
	if(over){
		this.$("JskeyI" + v).src = imgOpen;
	}
	else{
		this.$("JskeyI" + v).src = img;
	}
},
//树父级节点
expandNode:function(imgid, oid, img, imgOpen){
	var m = this.$(imgid);
	if(this.$(oid).style.display == 'none'){
		this.$(oid).style.display = '';
		m.innerHTML = m.getAttribute("x")=="0" ? i_LO : i_TO;
		this.$(imgid + "_node").src = imgOpen;
	}
	else{
		this.$(oid).style.display = 'none';
		m.innerHTML = m.getAttribute("x")=="0" ? i_LC : i_TC;
		this.$(imgid + "_node").src = img;
	}
},
//取得一个菜单单元HTML代码
//icoString:0和1的字符串
getCellHTML:function(obj, pnodeName, icoString){
	var html = "";
	var v = this.num++;
	var len = icoString.length;
	var items = obj.items;
	if(items.length == 0){
		var url = obj.url;
		if(url == null || url == "" || url == "null"){url = "";}
		if(obj._root != "" && url.indexOf("^") != 0 && url.indexOf("http") != 0 && url.indexOf(obj._root) != 0){
			url = obj._root + url;
		}
		if(url.indexOf("^") == 0){
			url = url.substring(1, url.length);
		}
		var _img = ((obj.img == null || obj.img == "")?(this.path + "default.gif"):(this.path + obj.img));// 父节点的图标是否由json来决定
		var _imgOpen = ((obj.imgOpen == null || obj.imgOpen == "")?(this.path + "default.gif"):(this.path + obj.imgOpen));
		html += "<div id='JskeyItem" + obj.id + "' class='treenode treenodeout' onmouseover='this.className = \"treenode treenodeover\"' onmouseout='this.className = \"treenode treenodeout\"' ondblclick=\"$jskey.menu.reChangeURL($jskey.menu.before('" + obj.id + "','" + url + "','" + pnodeName + "','" + obj.name + "'))\" onclick=\"$jskey.menu.changeURL($jskey.menu.before('" + obj.id + "','" + url + "','" + pnodeName + "','" + obj.name + "'))\">";
		if(len > 0){
			for(var c = 0;c < len - 1;c++){
				html += "<i>" + (icoString.charAt(c)=='0' ? i_I : i_N) + "</i>";
			}
			html += "<i>" + (icoString.charAt(len - 1)=='1' ? i_L : i_T) + "</i>";
		}
		else{
			html += "<i>" + i_N + "</i>";
		}
		html += "<img id='JskeyI" + obj.id + "_node' class='img' align='absmiddle' src='" + _img + "' onmouseover='this.src = \"" + _imgOpen + "\"' onmouseout='this.src = \"" + _img + "\"' />";
		html += obj.name;
		html += "</div>";
	}
	else{
		var _img = ((obj.img == null || obj.img == "")?(this.imgPath + "close.gif"):(this.path + obj.img));// 父节点的图标是否由json来决定
		var _imgOpen = ((obj.imgOpen == null || obj.imgOpen == "")?(this.imgPath + "open.gif"):(this.path + obj.imgOpen));
		html += "<div id='JskeyItem" + obj.id + "' class='treenode treenodeout' onmouseover='this.className = \"treenode treenodeover\"' onmouseout='this.className = \"treenode treedivout\"' onclick='$jskey.menu.expandNode(\"JskeyI"+v+"\", \"DIV"+v+"\", \"" + _img + "\", \"" + _imgOpen + "\");'>";
		for(var i = 0;i < len - 1;i++){
			html += "<i>" + (icoString.charAt(i)=='0' ? i_I : i_N) + "</i>";
		}
		if(icoString && icoString.charAt(len - 1) == '0'){
			html += "<i id='JskeyI"+v+"' x='1'>"+i_TO+"</i>";
		}
		else{
			html += "<i id='JskeyI"+v+"' x='0'>"+i_LO+"</i>";
		}
		html += "<img id='JskeyI"+v+"_node' class='img' align='absmiddle' src='" + _imgOpen + "' onclick='$jskey.menu.expandNode(\"JskeyI"+v+"\", \"DIV"+v+"\", \"" + _img + "\", \"" + _imgOpen + "\");'/>";
		html += obj.name;
		html += "</div>";
		html += "<div class='treenodes' id='DIV"+v+"' style='display:;'>";
		var last = items.length - 1;
		var item;
		var tmpHTML = "";//保存子节点HTML
		for(var i = 0;i < items.length;i++){
			item = items[i];
			item._root = obj._root;// 向下传递
			var icoStr = icoString + ((i == last) ? "1" : "0");
			tmpHTML += this.getCellHTML(item, pnodeName, icoStr);//这里的pnodeName不变成name
		}
		html += tmpHTML;
		html += "</div>";
	}
	return html;
},
//获得OutLook菜单子菜单中的html代码
getNodeHtml:function(obj){//{name:"",items:[]}
	var html = "";
	var items = obj.items;
	var childrenHTML = "";//保存功能节点HTML
	var item;
	var isTree = false;
	for(var i = 0;i < items.length;i++){
		if(items[i].items.length > 0){//子节点，无下级节点
			isTree = true;
			break;
		}
	}
	var v = this.num++;
	html += "<div class='treenodes' id='DIV"+v+"' style='display:;'>";
	for(var i = 0;i < items.length;i++){
		item = items[i];
		item._root = obj._root;// 向下传递
		childrenHTML += this.getCellHTML(item, obj.name, isTree ? ((i == items.length-1) ? "1" : "0") : "", this.num++);
		// 增加此功能树节点
		html += childrenHTML;
		childrenHTML = "";// 清空
	}
	html += "</div>";
	return html;
}

};

$jskey.menu.Map = function(){this.data = [];};
$jskey.menu.Map.prototype ={
	put:function(key, value){if(key.length > 0){for(var i = 0; i < this.data.length; i++){if(key == this.data[i][0]){this.data[i][1] = value;return;}}this.data[this.data.length] = [key, value];}},
	get:function(key){if(key.length > 0){for(var i = 0; i < this.data.length; i++){if(key == this.data[i][0]){return this.data[i][1];}}}return null;}
};
$jskey.menu.format = function(){
	try{
		var data = arguments[0]||[];
		var v = arguments[1]||"0";
		var m = new $jskey.menu.Map(),root = [],po;
		for(var i = 0; i < data.length; i++){
			data[i].img = (data[i].img == null)?"":data[i].img;
			data[i].imgOpen = (data[i].imgOpen == null)?"":data[i].imgOpen;
			data[i].items = [];
			m.put(data[i].id + "", data[i]);// 把obj放入map
		}
		for(var i = 0; i < data.length; i++){
			if(data[i].pid != v){
				po = m.get(data[i].pid + "");
				if(po != null){
					var len = po.items.length;
					po.items[len] = data[i];// 把子节点放入父节点的items数组中
				}
			}
			else{root[root.length] = data[i];}
		}
		return root;
	}catch(e){alert(e.message);return [];}
};

$jskey.menu.jsSrc = "" + document.getElementsByTagName("script")[document.getElementsByTagName("script").length - 1].src;
//当前js的引用路径
$jskey.menu.jsPath = $jskey.menu.jsSrc.substring(0, $jskey.menu.jsSrc.lastIndexOf("/"));

$jskey.menu.show = function(items, _root, _root2){
	_root = _root2 || _root || $jskey.menu.root;
	if($jskey.menu.path == "") $jskey.menu.path = $jskey.menu.jsPath + "/themes/menu/ico/";
	if($jskey.menu.imgPath == "") $jskey.menu.imgPath = $jskey.menu.jsPath + "/themes/menu/img/";
	$jskey.menu.reset();
	for(var i = 0;i < items.length;i++){
		var item = items[i];
		item._root = _root;
		var html = $jskey.menu.getNodeHtml(item);
		$jskey.menu.put(item.name, item.id, html);
	}
	$jskey.menu.create();
};
//改变URL，可以覆盖此方法，用于自定义不同的方式
$jskey.menu.changeURL = function(p){
	p = $jskey.menu.beforeClick(p);
	var url = p.url;
	if(url != ""){try{
		if($jskey.menu.isTabs){
			var s = p.nodename;
			if(parent.$('#tt').tabs('exists', s)){
				parent.$('#tt').tabs('select', s);
			}
			else{
				parent.$('#tt').tabs('add',{});// 增加空白tab
				var tab = parent.$('#tt').tabs('getSelected');// 为了处理新增tab高度不适应bug
				parent.$('#tt').tabs('update',{tab:tab, options:{title:s,closable:true,selected:true,
					content:'<div style="overflow:hidden;width:100%;height:100%;padding:0px;margin:0px;"><iframe scrolling="yes" frameborder="0" src="' + url + '"></iframe></div>'
				}});
			}
		}
		else{
			parent.$("#tt").attr("src", url);
		}
	}catch(e){}}
	$jskey.menu.click(p.itemid);
};
$jskey.menu.reChangeURL = function(p){
	p = $jskey.menu.beforeClick(p);
	if(!$jskey.menu.isTabs){
		return;
	}
	var url = p.url;
	if(url != ""){try{
		var s = p.nodename;
		var tab = null;
		if(parent.$('#tt').tabs('exists', s)){
			parent.$('#tt').tabs('select', s);
			tab = parent.$('#tt').tabs('getTab', s);//.find('iframe')[0].src = url;
		}
		else{
			parent.$('#tt').tabs('add',{});// 增加空白tab
			tab = parent.$('#tt').tabs('getSelected');
		}
		parent.$('#tt').tabs('update',{tab:tab, options:{title:s,closable:true,selected:true,
			content:'<div style="overflow:hidden;width:100%;height:100%;padding:0px;margin:0px;"><iframe scrolling="yes" frameborder="0" src="' + url + '"></iframe></div>'
		}});
	}catch(e){}}
	$jskey.menu.click(p.itemid);
};



}();



$jskey.menu.click = function(itemid){
	if(!$jskey.menu.isTabs){
		try{
			$(".treenodeselected").each(function(){
				$(this).removeClass("treenodeselected");
				$(this).isselected = false;
			});
			var x = $("JskeyItem" + itemid);
			if(x.length > 0){
				x.attr("class", "treenode treenodeselected");
				x.isselected = true;
				if(!x.isclicked){
					x.isclicked = true;
					x.get(0).removeAttribute("onmouseover");
					x.get(0).removeAttribute("onmouseout");
					x.on("mouseover"), function(){
						var z = $(this);
						z.attr("class", "treenode treenodeover" + (z.isselected?" treenodeselected" : ""));
					};
					x.on("onmouseout"), function(){
						var z = $(this);
						z.attr("class", "treenode treenodeout" + (z.isselected?" treenodeselected" : ""));
					};
				}
			}
			parent.$("#tt").attr("src", url);
		}catch(ee){}
		return;
	}
};
