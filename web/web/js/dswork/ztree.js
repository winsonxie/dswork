//ztree
if(typeof($dswork)!="object"){$dswork={};}
$dswork.ztree = {
	id:"mytree", menuName:"divMenu", tree:null, menu:null, dataName:"divTree",
	tid:-1, tpid:-1//异步加载前选中的节点id和父节点id
};
$dswork.ztree.root = {id:0, pid:-1, gid:-1, status:0, state:0, code:"", isParent:true, name:""};//默认根节点
$dswork.ztree.nodeArray = [$dswork.ztree.root];//初始化

$dswork.ztree.showMenu = function(type, x, y){};
$dswork.ztree.hideMenu = function(){try{$("#" + $dswork.ztree.menuName).menu('hide');}catch(e){}};
$dswork.ztree.getSelectedNode = function(){//当前选中节点，单选时
	var _arr = $dswork.ztree.tree.getSelectedNodes();
	if(_arr.length > 0){return _arr[0];}
	else{
		var _root = $dswork.ztree.tree.getNodeByParam("id", 0);
		$dswork.ztree.tree.selectNode(_root);//选中
		return _root;
	}
};
$dswork.ztree.beforeClick = function(treeId, treeNode, clickFlag){};//点击节点前函数
$dswork.ztree.click = function(event, treeId, treeNode, clickFlag){};//点击节点函数
$dswork.ztree.beforeDblClick = function(treeId, treeNode){return treeNode != null};//双击节点前函数
$dswork.ztree.dblClick = function(event, treeId, treeNode){};//双击节点函数，$dswork.ztree.beforeDblClick必须返回非false

$dswork.ztree.rightClick = function(event, treeId, treeNode){//右击节点函数
	if(treeNode && !treeNode.noR){
		$dswork.ztree.tree.selectNode(treeNode);
		$dswork.ztree.click();
		$dswork.ztree.showMenu("node", event.clientX, event.clientY);
	}
};
$dswork.ztree.beforeCheck = function(treeId, treeNode){};//点击节点按钮前函数
$dswork.ztree.check = function(event, treeId, treeNode){};//点击节点按钮函数
$dswork.ztree.refreshNode = function(re){
	re = re?true:false;//是否需要刷新父节点，不传默认刷新当前节点
	var z = $dswork.ztree;
	var _c = z.getSelectedNode();//当前选中节点
	if(_c == null){return false;}
	if(_c.pid == null || _c.pid < 0){re = false;}//根节点
	var _id = parseInt(re?_c.pid:_c.id);//需要刷新的节点id
	if(_id >= 0){
		$dswork.ztree.tid = _c.id;//记录选中节点id
		$dswork.ztree.tpid = _c.pid;//记录选中节点pid
		var _t = z.getNodeByParam("id", _id);
		z.selectNode(_t);//选中
		z.reAsyncChildNodes(_t, "refresh");//重新加载
	}
};
$dswork.ztree.reAsyncChildNodes = function(parentNode, reloadType, isSilent){//仅仅重新加载
	$dswork.ztree.tree.reAsyncChildNodes(parentNode, reloadType, isSilent);
};
$dswork.ztree.asyncSuccess = function(event, treeId, treeNode, msg){//异步获取数据后加载到树
	try{if($dswork.ztree.tid != -1){//选中刷新前选择的节点，并执行单击事件
		var z = $dswork.ztree;
		var _c = z.getNodeByParam("id", $dswork.ztree.tid);
		var _p = z.getNodeByParam("id", $dswork.ztree.tpid);
		if(_c == null){
			if(_p == null){_p = z.getNodeByParam("id", 0);}
			z.selectNode(_p);//选中
			z.expandNode(_p, true);//展开
		}
		else{
			z.selectNode(_c);//选中
			z.expandNode(_c, true);//展开
		}
		z.click();
	}}catch(e){}
	$dswork.ztree.tid = -1;//还原
	$dswork.ztree.tpid = -1;//还原
};
$dswork.ztree.moveUpdate = function(fromId, toId){// 异步树移动后需要刷新时调用
	var z = $dswork.ztree;
	var from = z.getNodeByParam("id", fromId);
	var to = z.getNodeByParam("id", toId);
	if(to != null){//目标是否已加载
		z.selectNode(to);
		//判断目标是不是当前子节点
		var node = to, p = to.pid, hasParent = false;
		while(p != null && p >= 0){
			if(node.pid == from.id){hasParent = true;p = -1;break;}
			else{node = z.getNodeByParam("id", p);if(node == null){p = -1;break;}p = node.pid;}
		}
		if(!hasParent){z.reAsyncChildNodes(to, "refresh");}//目标是子节点时不刷新
	}
	if(from.pid + "" != toId + ""){//目标不是父节点
		from = z.getNodeByParam("id", fromId);//刷新目标后，from不一定存在
		if(from != null){
			$dswork.ztree.tid = from.id;
			$dswork.ztree.tpid = from.pid;
			z.selectNode(from);
			z.reAsyncChildNodes(from, "refresh");
		}
	}
	z.click();
};
$dswork.ztree.dataFilter = function (treeId, parentNode, responseData){//异步获取数据后未加载到树
//	if(responseData){for(var i =0; i < responseData.length; i++){responseData[i]}}
	return responseData;
};
$dswork.ztree.setFontCss = function(treeId, treeNode){return {};};
$dswork.ztree.url = function(treeNode){};//必须修改，针对根进行异步加载时，treeNode = null
$dswork.ztree.config = {
	callback:null,//必须修改
	view:{
		expandSpeed:"",
		nameIsHTML:true,
		fontCss:function(treeId, treeNode){return $dswork.ztree.setFontCss(treeId, treeNode);}
	},
	async:{
		enable:true,
		dataFilter:function (treeId, parentNode, responseData){return $dswork.ztree.dataFilter(treeId, parentNode, responseData);},
		url:function(treeId, treeNode){return $dswork.ztree.url(treeNode);}
	},
	data:{
		key:{
			name: "name"
		},
		simpleData:{
			//rootPid:"-1",
			enable:true,
			idKey:"id",
			pIdKey:"pid"
		}
	},
	check:{
		enable: false,
		chkStyle: "checkbox",
		chkboxType: {"Y":"ps","N":"s"},
		radioType: "all"
	}
};
$dswork.ztree.load = function(){//默认加载页面管理
	$dswork.ztree.config.callback = {
		"beforeCheck":$dswork.ztree.beforeCheck,
		"onCheck":$dswork.ztree.check,
		"beforeClick":$dswork.ztree.beforeClick,
		"onClick":$dswork.ztree.click,
		"beforeDblClick":$dswork.ztree.beforeDblClick,
		"onDblClick":$dswork.ztree.dblClick,
		"onRightClick":$dswork.ztree.rightClick,
		"onAsyncSuccess":$dswork.ztree.asyncSuccess
	};
	$dswork.ztree.tree = $.fn.zTree.init($("#"+$dswork.ztree.id), $dswork.ztree.config, $dswork.ztree.nodeArray);
};
$dswork.ztree.expandRoot = function(){
	try{
		var tree = $dswork.ztree.tree;
		var _p = tree.getNodeByParam("id", $dswork.ztree.root.id);//根节点
		if(_p && $dswork.ztree.refreshNode){
			tree.selectNode(_p);tree.expandNode(_p, true);$dswork.ztree.refreshNode();
		}
	}catch(e){}
};
$dswork.ztree.selectNode = function(treeNode, addFlag){
	return $dswork.ztree.tree.selectNode(treeNode, addFlag);
};
$dswork.ztree.expandNode = function(treeNode, expandFlag, sonSign, focus, callbackFlag){
	return $dswork.ztree.tree.expandNode(treeNode, expandFlag, sonSign, focus, callbackFlag);
};
$dswork.ztree.updateNode = function(treeNode, checkTypeFlag){
	return $dswork.ztree.tree.updateNode(treeNode, checkTypeFlag);
};
$dswork.ztree.expandAll = function(expandFlag){
	return $dswork.ztree.tree.expandAll(expandFlag);
};
$dswork.ztree.getCheckedNodes = function(checked){
	return $dswork.ztree.tree.getCheckedNodes(checked);
};
$dswork.ztree.getNodeByParam = function(key, value, parentNode){
	return $dswork.ztree.tree.getNodeByParam(key, value, parentNode);
};
$dswork.ztree.getNodesByParam = function(key, value, parentNode){
	return $dswork.ztree.tree.getNodesByParam(key, value, parentNode);
};
