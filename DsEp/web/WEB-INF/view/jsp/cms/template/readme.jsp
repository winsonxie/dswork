<%@page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<style type="text/css">
p{margin-top:5px;margin-bottom:5px;}
p.title{font-size:20px;padding-bottom:5px;border-bottom:solid 1px #ccc;}
.box{border:solid 1px #bbb;margin-top:10px;margin-left:10px;padding:5px;}
.box p{position:relative;}
.box p .v{padding-left:10px;font-weight:bold;}
.box p .c{position:absolute;left:300px;}
</style>
</head>
<body style="background-color:#fafafa;">
	<div style="text-align:center;font-size:30px;">模板编写说明</div>
	<hr style="width:100%;"></hr>
	<div class="box">
		<p class="title">通用变量</p>
		<p><span class="v">cms</span><span class="c">cms变量，可用来获取数据</span></p>
		<p><span class="v">year</span><span class="c">当前年</span></p>
		<p><span class="v">site</span><span class="c">site变量，可获取站点信息</span></p>
		<p><span class="v">categorylist</span><span class="c">顶级栏目列表</span></p>
		<p><span class="v">ctx</span><span class="c">站点路径前缀</span></p>
		<div class="box">
			<p class="title">文章页变量</p>
			<p><span class="v">category</span><span class="c">文章所属的栏目</span></p>
			<p><span class="v">vo</span><span class="c">文章的自定义字段</span></p>
			<p><span class="v">id</span><span class="c">文章的编号</span></p>
			<p><span class="v">categoryid</span><span class="c">文章所属栏目的编号</span></p>
			<p><span class="v">title</span><span class="c">文章标题</span></p>
			<p><span class="v">summary</span><span class="c">文章摘要</span></p>
			<p><span class="v">metakeywords</span><span class="c">文章页的metakeywords</span></p>
			<p><span class="v">metadescription</span><span class="c">文章页的metadescription</span></p>
			<p><span class="v">releasetime</span><span class="c">文章页的发布时间</span></p>
			<p><span class="v">releasesource</span><span class="c">文章来源</span></p>
			<p><span class="v">releaseuser</span><span class="c">文章作者</span></p>
			<p><span class="v">img</span><span class="c">文章图片封面</span></p>
			<p><span class="v">url</span><span class="c">文章URL</span></p>
			<p><span class="v">content</span><span class="c">文章内容</span></p>
		</div>
		<div class="box">
			<p class="title">栏目页变量</p>
			<p><span class="v"><del>categoryparent</del></span><span class="c">栏目的父栏目（不推荐使用，可用category.parent代替）</span></p>
			<p><span class="v">categoryid</span><span class="c">栏目编号</span></p>
			<p><span class="v">category</span><span class="c">当前栏目</span></p>
			<p><span class="v">vo</span><span class="c">栏目的自定义字段</span></p>
			<p><span class="v">datalist</span><span class="c">栏目下的文章列表</span></p>
			<p><span class="v">datapageview</span><span class="c">栏目下文章列表的默认网页分页控件，直接输出即可</span></p>
			<p><span class="v">datauri</span><span class="c">栏目下文章列表的URL地址前缀（即相同的部分），页数大于1时以“_页数”为中缀，后缀为“.html”</span></p>
			<p><span class="v">datapage</span><span class="c">栏目下文章列表的分页信息</span></p>
		</div>
	</div>
</body>
</html>
