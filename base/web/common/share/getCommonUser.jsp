<%@page language="java" pageEncoding="UTF-8" import="dswork.web.*,dswork.spring.BeanFactory,dswork.common.service.DsCommonUserService,java.util.*"%><%
MyRequest req = new MyRequest(request);
Map<String, Object> map = new HashMap<String, Object>();
map.put("type", 5); //业务员
map.put("name", req.getString("name")); 
DsCommonUserService service = (DsCommonUserService)BeanFactory.getBean("dsCommonUserService");
List<dswork.common.model.DsCommonUser> list = service.queryList(map);
out.print(list);
%>