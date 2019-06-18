$(function(){
$dswork.confirm = "确定保存吗？";
$("#dataFormSave").click(function(){
	if($dswork.beforeSubmit()){if(confirm($dswork.confirm)){
		$dswork.readySubmit();
		if($dswork.doAjax){$("#dataForm").ajaxSubmit($dswork.doAjaxOption);}
		else{$("#dataForm").submit();}
	}}
	return false;
});
$("#dataForm").submit(function(event){
	if($dswork.doAjax){event.preventDefault();try{$("#dataFormSave").click();}catch(e){}}
});
try{$(".form_title").css("width", "20%");}catch(e){}
});