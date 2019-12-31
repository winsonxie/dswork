<%@page language="java" pageEncoding="UTF-8"%><%!
static dswork.db.redis.config.RedisConfig config = new dswork.db.redis.config.RedisConfig()
.setHost("127.0.0.1").setPassword(null).setPort(6379)//
.setDatabase(3)//
.setMinIdle(1).setMaxIdle(1).setMaxTotal(1)//
.setMaxWaitMillis(10000).setTestOnBorrow(true).setTestOnReturn(true).setTestWhileIdle(true)//
.setMinEvictableIdleTimeMillis(60000).setTimeBetweenEvictionRunsMillis(30000).setNumTestsPerEvictionRun(-1)//
.setConnectTimeout(2000).setSoTimeout(2000)//
.setClientName(null);
static dswork.db.redis.RedisUtil mydb = new dswork.db.redis.RedisUtil(config);
%><%
String dbs = String.valueOf(request.getParameter("db"));
String kv = String.valueOf(request.getParameter("k"));
if(kv.equals("null") || kv.equals("")){
	kv = "";
}
if(dbs.equals("null") || dbs.equals("")){
	dbs = "3";
}
int db = 3;
try{
	db = Integer.parseInt(dbs);
}
catch(Exception ex){
	db = 3;
}
if(db > 15)
{
	db = 3;
}
redis.clients.jedis.Jedis jedis = mydb.getJedis();
if(db != 3)
{
	jedis.select(db);
}
if(kv.length() == 0)
{
	java.util.Set<String> set = jedis.keys("*");
	jedis.close();
	java.util.Set<String> sortSet = new java.util.TreeSet<String>(new java.util.Comparator<String>() {
	    public int compare(String o1, String o2) {
	        return o2.compareTo(o1);//降序排列
	    }
	});
	sortSet.addAll(set);
	%>
	<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
		<title></title>
		<style type="text/css">html,body{width:100%;height:100%;overflow:hidden;}</style>
	</head>
	<body>
		<div style="width:100%;height:50%;padding:10px;overflow-x:hidden;overflow-y:scroll; ">
		<% 
		for(String s : sortSet)
		{
			out.print("<a onclick=\"kv('" + s + "');return false;\" href='#'>" + s + "</a><br/>");
		}
		%>
		</div>
		<iframe id="iframe" style="width:100%;height:50%;overflow:hidden;" scrolling="auto" frameborder="0" src="about:blank"></iframe>
	</body>
		<script type="text/javascript">
		var iframe = document.getElementById("iframe");
		function kv(k){iframe.setAttribute("src", "k.jsp?db=<%=db%>&k=" + k);}
		</script>
	</html>
	<%
}
else
{
	java.util.Map<String, String> map = jedis.hgetAll(kv);
	String val = "";
	if(map == null)
	{
		val = jedis.get(kv);
	}
	jedis.close();
	%>
	<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui"/>
		<title></title>
	</head>
	<body>
		<%
		if(map == null)
		{
			out.print(kv + "=" + val + "<br/>");
		}
		else
		{
			for(String s : map.keySet())
			{
				out.print(s + "=" + String.valueOf(map.get(s)) + "<br/>");
			}
		}
		%>
	</body>
	</html>
<%
}
%>
