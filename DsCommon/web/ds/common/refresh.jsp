<%@page import="dswork.http.HttpUtil"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
HttpUtil http = new HttpUtil();
http.create("http://127.0.0.1:8888/sso/refresh.jsp").connect();
response.sendRedirect(request.getContextPath() + "/common/code.jsp");
%>