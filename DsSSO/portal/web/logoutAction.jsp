<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
session.removeAttribute(dswork.sso.WebFilter.LOGINER);
Cookie cookie = new Cookie(dswork.sso.WebFilter.SSOTICKET, "");
cookie.setMaxAge(0);
cookie.setPath("/");
cookie.setSecure(false);
cookie.setHttpOnly(true);
response.addCookie(cookie);
response.sendRedirect(request.getContextPath());
%>