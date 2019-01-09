if(typeof ($jskey) != "object"){$jskey = {};}


;!function(){"use strict";




var i_close = "&#xf1016;",i_open = "&#xf1017;",i_N  = "&nbsp;", i_TC, i_TO, i_LC, i_LO, i_T, i_L, i_I;
var has3 = true;

if(has3){/*三角模式*/
	i_TC = "&#xf1016;";// T close
	i_TO = "&#xf1017;";// T open
	i_LC = "&#xf1016;";// L close
	i_LO = "&#xf1017;";// L open
	i_T  = "&nbsp;";// T
	i_L  = "&nbsp;";// L
	i_I  = "&nbsp;";// I
	document.write('<style type="text/css">.jskeymenu .cell{padding:0 0 0 12px;}.jskeymenu .treenodes i{font-size:12px;margin-right:2px;width:auto;}</style>');
}
else{
	// 加减模式
	i_TC = "&#xf1046;";// T close
	i_TO = "&#xf1047;";// T open
	i_LC = "&#xf1043;";// L close
	i_LO = "&#xf1044;";// L open
	i_T  = "&#xf1045;";// T
	i_L  = "&#xf1042;";// L
	i_I  = "&#xf1041;";// I
}

$jskey.menu =
{
num:1,
list:[],
hidden:true,//隐藏所有菜单
load:function(index, id){},//异步加载菜单
root:"",
path:"",
imgPath:"",
$:function(id){return document.getElementById(id);},
//初始化操作
reset:function(){this.list = [];},
//添加一级菜单下的一组子菜单的菜单项
put:function(i, title, id, html){
	this.list[this.list.length] = {"index":i, "title":title, "id":id, "html":html};//index位置索引,id一级菜单的id,title一级菜单的name
},
//创建一层菜单内容
createCell:function(obj, c){
	var html = '<tr>';
	html += '<td id="JskeyMT_' + c + '" class="menu menu-close" onclick="$jskey.menu.clickBar(' + c + ')"' + ((c == this.list.length)?' style="display:none;"':'') + '>';
	html += '<i id="JskeyI' + 'JskeyMT_' + c + '">'+i_close+'</i>';
	html += obj.title + '</td>';
	html += '</tr><tr>';
	html += '<td id="JskeyMC_' + c + '" valign="top" class="cell cell-close" style="' + ((c == this.list.length)?'display:;height:100%':'display:none;height:') + '">';
	html += '<div class="cell-content" id="JskeyMCD_' + c + '">';
	html += ((obj.html != "")?obj.html:' ');
	html += '</div></td></tr>';
	return html;
},
//创建一层菜单内容
create:function(){
	var len = this.list.length;
	if(len < 1){return;}
	var html = '<table id="JskeyMG" class="jskeymenu" style="height:'+(this.hidden?'100%':'auto')+'" cellspacing="0" cellpadding="0">';
	for(var c = 0;c < len;c++){html += this.createCell(this.list[c], c);}
	html += this.createCell({"index":len, "title":"", "id":"", "html":""}, len);// 在最后补一个看不见的，用于收缩
	html += '</table>';
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
		try{this.click(c, this.list[c].id);this.load(c, this.list[c].id);}catch(e){}
		var isClose = this.$("JskeyMC_" + c).style.display == "";
		if(isClose){
			this.$("JskeyMT_" + c).className = "menu menu-close";
			this.$("JskeyMC_" + c).className = "cell cell-close";
			//this.$("JskeyI" + "JskeyMT_" + c).src = this.imgPath + "right.gif";
			this.$("JskeyI" + "JskeyMT_" + c).innerHTML = i_close;
			if(this.hidden == null){
				this.$("JskeyMC_" + c).style.height = "auto";
				this.$("JskeyMC_" + c).style.display = "none";
				this.$("JskeyMC_" + this.list.length).style.display = "";
			}
			else if(this.hidden){
				this.$("JskeyMC_" + c).style.height = "100%";
				this.$("JskeyMC_" + this.list.length).style.display = "";
				this.smoothMenu(this.list.length, c, 0);// 需要将最后一个展开
			}
			else{
				this.$("JskeyMC_" + c).style.height = "auto";
				this.$("JskeyMC_" + c).style.display = "none";
			}
		}
		else{
			this.$("JskeyMT_" + c).className = "menu menu-open";
			this.$("JskeyMC_" + c).className = "cell cell-open";
			this.$("JskeyI" + "JskeyMT_" + c).innerHTML = i_open;
			if(this.hidden == null){
				for(var i = 0;i < this.list.length;i++){
					if(i != c && this.$("JskeyMC_" + i).style.display == ""){// 找到有其他被打开的就直接关掉并返回
						this.$("JskeyMT_" + i).className = "menu menu-close";
						this.$("JskeyMC_" + i).className = "cell cell-close";
						this.$("JskeyMC_" + i).style.height = "auto";
						this.$("JskeyMC_" + i).style.display = "none";
						this.$("JskeyMC_" + c).style.height = "auto";
						this.$("JskeyMC_" + c).style.display = "";
						this.$("JskeyI" + "JskeyMT_" + i).innerHTML = i_close;
						return;
					}
				}
				this.$("JskeyMC_" + c).style.height = "auto";
				this.$("JskeyMC_" + c).style.display = "";
				this.$("JskeyMC_" + this.list.length).style.height = "auto";
				this.$("JskeyMC_" + this.list.length).style.display = "none";
			}
			else if(this.hidden){
				this.$("JskeyMC_" + c).style.height = "100%";
				for(var i = 0;i < this.list.length;i++){
					if(i != c && this.$("JskeyMC_" + i).style.display == ""){// 找到有其他被打开的就直接关掉并返回
						this.$("JskeyMT_" + i).className = "menu menu-close";
						this.$("JskeyMC_" + i).className = "cell cell-close";
						this.$("JskeyMC_" + i).style.height = "100%";
						this.$("JskeyI" + "JskeyMT_" + i).innerHTML = i_close;
						this.smoothMenu(c, i, 0);
						return;
					}
				}
				this.smoothMenu(c, this.list.length, 0);// 找不到被打开的，就关掉最后一个
			}
			else{
				this.$("JskeyMC_" + c).style.height = "auto";
				this.$("JskeyMC_" + c).style.display = "";
			}
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
click:function(index, id){
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
		if(obj._root != "" && url.indexOf("^") != 0 && url.indexOf("http") != 0 && url.indexOf(obj._root) != 0){
			url = obj._root + url;
		}
		var _img = ((obj.img == null || obj.img == "")?(this.path + "default.gif"):(this.path + obj.img));// 父节点的图标是否由json来决定
		var _imgOpen = ((obj.imgOpen == null || obj.imgOpen == "")?(this.path + "default.gif"):(this.path + obj.imgOpen));
		html += "<div class='treenode treenodeout' onmouseover='this.className = \"treenode treenodeover\"' onmouseout='this.className = \"treenode treenodeout\"' ondblclick=\"$jskey.menu.reChangeURL('" + pnodeName + "','" + obj.name + "','" + url + "')\" onclick=\"$jskey.menu.changeURL('" + pnodeName + "','" + obj.name + "','" + url + "')\">";
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
		html += "<div class='treenode treenodeout' onmouseover='this.className = \"treenode treenodeover\"' onmouseout='this.className = \"treenode treedivout\"' onclick='$jskey.menu.expandNode(\"JskeyI"+v+"\", \"DIV"+v+"\", \"" + _img + "\", \"" + _imgOpen + "\");'>";
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
		
		// html += "</div>";
		
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
		//子节点，无下级节点
		if(items[i].items.length > 0){
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

$jskey.menu.show = function(items, isHidden, _root){
	_root = _root || $jskey.menu.root;
	$jskey.menu.hidden = isHidden;
	if($jskey.menu.path == "") $jskey.menu.path = $jskey.menu.jsPath + "/themes/menu/ico/";
	if($jskey.menu.imgPath == "") $jskey.menu.imgPath = $jskey.menu.jsPath + "/themes/menu/img/";
	$jskey.menu.reset();
	for(var i = 0;i < items.length;i++){
		var item = items[i];
		item._root = _root;
		var html = $jskey.menu.getNodeHtml(item);
		$jskey.menu.put(i, item.name, item.id, html);
	}
	$jskey.menu.create();
};
//改变URL，可以覆盖此方法，用于自定义不同的方式
$jskey.menu.changeURL = function(parentname, nodename, url){
	if(url == null || url == "" || url == "null"){url = "";}
	if(url.indexOf("^") == 0){
		url = url.substring(1, url.length);
	}
//	else if($jskey.menu.root != ""){
//		if(url.indexOf("http") != 0 && url.indexOf($jskey.menu.root) != 0){
//			url = $jskey.menu.root + url;
//		}
//	}
	if(url != ""){try{
		var s = nodename;//parentname + '-'+nodename;
		if(parent.$('#tt').tabs('exists', s)){
			parent.$('#tt').tabs('select', s);
		}
		else{
			// 为了处理新增tab高度不适应bug
			parent.$('#tt').tabs('add',{});// 增加空白tab
			var tab = parent.$('#tt').tabs('getSelected');
			parent.$('#tt').tabs('update',{
				tab:tab,
				options:{
					title:s,
					content:'<div style="overflow:hidden;width:100%;height:100%;padding:0px;margin:0px;"><iframe scrolling="yes" frameborder="0" src="' + url + '"></iframe></div>',
					closable:true,
					selected:true
				}
			});
		}
	}catch(e){}}
};
$jskey.menu.reChangeURL = function(parentname, nodename, url){
	if(url == null || url == "" || url == "null"){url = "";}
	if(url.indexOf("^") == 0){
		url = url.substring(1, url.length);
	}
//	else if($jskey.menu.root != ""){
//		if(url.indexOf("http") != 0 && url.indexOf($jskey.menu.root) != 0){
//			url = $jskey.menu.root + url;
//		}
//	}
	if(url != ""){try{
		var s = nodename;//parentname + '-'+nodename;
		var tab = null;
		if(parent.$('#tt').tabs('exists', s)){
			parent.$('#tt').tabs('select', s);
			tab = parent.$('#tt').tabs('getTab', s);//.find('iframe')[0].src = url;
		}
		else{
			//parent.$('#tt').tabs('add',{
			//	title:s,
			//	content:'<div style="overflow:hidden;width:100%;height:100%;padding:0px;margin:0px;"><iframe scrolling="yes" frameborder="0" src="' + url + '"></iframe></div>',
			//	closable:true
			//});
			parent.$('#tt').tabs('add',{});// 增加空白tab
			tab = parent.$('#tt').tabs('getSelected');
		}
		parent.$('#tt').tabs('update',{
			tab:tab,
			options:{
				title:s,
				content:'<div style="overflow:hidden;width:100%;height:100%;padding:0px;margin:0px;"><iframe scrolling="yes" frameborder="0" src="' + url + '"></iframe></div>',
				closable:true,
				selected:true
			}
		});
	}catch(e){}}
};



}();