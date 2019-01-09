$jskey.menu.load = function(index, id){
	var obj, _o = {};
	for(var i = 0; i < sys.length; i++){
		_o = obj = sys[i];
		if(i == index){// || obj.id == id
			if(obj.data == null || obj.data.length == 0){
				menuload(_o);
				return;
			}
			break;
		}
	}
};
//$jskey.menu.click = function(index, id){
//	if(sys.length > index){
//		$jskey.menu.root = sys[index].domainurl + sys[index].rooturl;
//	}
//};
function init(){
	var treedata = [];
	for(var i=0; i<sys.length; i++){
		treedata[i] = {index:i, id:sys[i].id, name:sys[i].name, img:"", imgOpen:"", url:"", items:[]};
	}
	// true置底仅打开一个，false不置底可打开多个，null不置底仅打开一个
	$jskey.menu.show(treedata, false, "");
	$jskey.menu.clickBar(0);
}
function showSystem(){
	alert("please load leftone.js");
}