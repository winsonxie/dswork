<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
dswork.sso.WebFilter.logout(session);
response.sendRedirect(dswork.sso.WebFilter.getLogoutURL(request.getContextPath() + "/admin/login.jsp"));
%>