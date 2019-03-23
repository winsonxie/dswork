<%@page import="dswork.http.HttpUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
HttpUtil http = new HttpUtil();
%>
系统管控密码：caX76c<%--=http.create("http://127.0.0.1:8888/sso/code.jsp").connect()--%>
<%--
&nbsp;
<a href="refresh.jsp">刷新密码</a>
<br />
展厅管控密码：<%=http.create("http://127.0.0.1:8080/jxc/user/code.jsp").connect()%>
&nbsp;
<a href="refresh2.jsp">刷新密码</a>
--%>