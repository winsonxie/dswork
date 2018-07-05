;!function($){

var validator = {
	zhname:function(v){return /^[\u4E00-\u9FA5]{1,6}$/.test(v)}
	,mobile:function(v){return /^1[34578]\d{9}$/.test(v)}
	,idcard:function(v){return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(v)}
	,require:function(v){return '' != v}
	,english:function(v){return /^[A-Za-z]+$/.test(v)}
	,number:function(v){return /^[0-9]*$/.test(v)}
	,char:function(v){return /^[A-Za-z0-9]+$/.test(v)}
	,email:function(v){return /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(v)}
	
	,equal:function(v1,v2){return v1==v2}
};

$.fn.validate = function(){
	var pass = true;
	$(this).find('[data-type],[data-equal]').each(function(){
		var t = check(this);
		var $e = $(this);
		if(!t){
			var $m = $('#' + $e.data('show'));
			$m.css({display:'block'});
			$e.off('blur').on('blur', function(){//防止重复绑定
				$m.css({display:check(this)?'':'block'});
			});
		}
		pass = pass && t;
	});
	return pass;
}

function check(dom){
	var pass = true;
	var $e = $(dom);
	var dt = $e.data('type');
	var de = $e.data('equal');
	if(de){
		var _$e = $('#' + de);
		pass = validator.equal($e.val(), _$e.val());
	}
	if(dt){
		var fn = validator[dt];
		if(fn){
			pass = pass && fn($e.val());
		}
	}
	return pass;
}

}(jQuery);