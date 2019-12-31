<%@page language="java" pageEncoding="UTF-8"%><meta charset="UTF-8" /><%
redis.clients.jedis.Jedis jedis = ghapp.util.RedisMsgUtil.getDB().getJedis();
dswork.web.MyRequest req = new dswork.web.MyRequest(request);
String key = req.getString("k");
java.util.Map<String, String> map = jedis.hgetAll(key);
for(String s : map.keySet())
{
	out.print(s + "=" + map.get(s) + "<br/>");
}
jedis.close();
%>