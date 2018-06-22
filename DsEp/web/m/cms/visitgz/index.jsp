<%@page language="java" pageEncoding="UTF-8"%><%
	response.setCharacterEncoding("UTF-8");
	response.setContentType("text/plain;charset=UTF-8");
	String rootpath = "http://192.168.1.10";
	String webadminpath = "http://192.168.1.10:4444";
	String contentpath = rootpath + "/visitgz";
	String dataspath =  webadminpath + "/www-visitgz/m/cms/category.jsp?siteid=7&pagesize=10";
	String datapath = webadminpath + "/www-visitgz/m/cms/list.jsp?siteid=7&pagesize=10";
	String xtpath = "http://192.168.1.10";
%>{<%
	%>"siteid":"7"<%
	%>,"siteurl":"<%=rootpath%>"<%
	%>,"contenturl":"<%=contentpath%>"<%
	%>,"datasurl":"<%=dataspath%>"<%
	%>,"dataurl":"<%=datapath%>"<%
	%>,"imgArray":["/www-visitgz/m/cms/visitgz/banner/1.jpg","/www-visitgz/m/cms/visitgz/banner/2.jpg","/www-visitgz/m/cms/visitgz/banner/3.jpg","/www-visitgz/m/cms/visitgz/banner/4.jpg","/www-visitgz/m/cms/visitgz/banner/5.jpg"]<%
	%>,"icoArray":[<%
			%> {"name":"机器人"  ,"type":"url", "img":"/www-visitgz/m/cms/visitgz/ico/jqr.png"    ,"url":"http://120.25.152.16/lyj-robotwwwa/app/appIndex.html"}<%
			%>,{"name":"广州通"  ,"type":"app", "img":"/www-visitgz/m/cms/visitgz/ico/gzt.png"    ,"url":"http://gdown.baidu.com/data/wisegame/c504507db77a1ae1/guangzhoutong_256.apk","pkgname":"com.xtownmobile.NZHGD.GZ","appname":"", "url_ios":"", "des":"地址不存在"}<%
			%>,{"name":"旅程推荐","type":"func","img":"/www-visitgz/m/cms/visitgz/ico/zl.png"     ,"url":"<%=dataspath%>&categoryids=48,48&categoryids=49,49&categoryids=50,50&categoryids=127,127&categoryids=190,190"}<%
			%>,{"name":"资讯网"  ,"type":"url", "img":"/www-visitgz/m/cms/visitgz/ico/visitgz.png","url":"http://www.gzly.gov.cn/visitgz/m/"}<%
			%>,{"name":"旅游服务","type":"func","img":"/www-visitgz/m/cms/visitgz/ico/tx.png"     ,"url":"<%=dataspath%>&categoryids=55,55&categoryids=118,118"}<%
	%>]<%
	%>,"url":"<%=dataspath%>&categoryids=13,14,15,16&categoryids=22,23,24,25,26&categoryids=28&categoryids=17,18,19,20,21&categoryids=33,34,35,36&categoryids=37,38,39,40"<%
	%>,"adverurl":""<%
	%>,"msgdataurl":"<%=webadminpath%>/gzly/category.jsp?siteid=0&pagesize=10&categoryids=59,59"<%
	%>,"login_auto":"<%=xtpath%>/lxsydb/enterprise/login/autoLoginAction.jsp"<%
	%>,"login":"<%=xtpath%>/lxsydb/enterprise/login/loginVerificationAction.jsp"<%
	%>,"hotdataurl":"<%=dataspath%>&categoryids=2002,2002"<%
%>}
