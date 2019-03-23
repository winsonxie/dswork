<%@page language="java" pageEncoding="UTF-8" import="dswork.web.*,dswork.spring.BeanFactory,dswork.base.service.DsBaseUserService,java.util.*"%><%
MyRequest req = new MyRequest(request);
Map<String, Object> map = new HashMap<String, Object>();
map.put("type", 5); //业务员
map.put("name", req.getString("name")); 
DsBaseUserService service = (DsBaseUserService)BeanFactory.getBean("DsBaseUserService");
List<dswork.base.model.DsBaseUser> list = service.queryList(map);
out.print(list);
%>