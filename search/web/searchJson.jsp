<%@page pageEncoding="UTF-8"%><%!
public int getInt(HttpServletRequest request, String key, int defaultValue)
{
	try
	{
		String str = request.getParameter(key);
		return (str == null || str.trim().equals("")) ? defaultValue : Integer.parseInt(str.trim());
	}
	catch(Exception ex)
	{
		return defaultValue;
	}
}
public String getString(HttpServletRequest request, String key)
{
	String value = request.getParameter(key);
	return (value == null) ? "" : value;
}
%><%
response.addHeader("Access-Control-Allow-Origin", "*");
String search = new String(getString(request, "v").getBytes("iso-8859-1"), "UTF-8");
String type = new String(getString(request, "type").getBytes("iso-8859-1"), "UTF-8");
search = java.net.URLDecoder.decode(search, "UTF-8");
type = java.net.URLDecoder.decode(type, "UTF-8");
java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
try
{
	common.lucene.MyPage pageModel = common.lucene.LuceneUtil.search(type.split(",", -1), search, getInt(request, "page", 1), getInt(request, "pagesize", 10));
	map.put("status", "1");//success
	map.put("msg", "success");
	
	map.put("size", pageModel.getTotalSize());
	map.put("page", pageModel.getPage());
	map.put("pagesize", pageModel.getPageSize());
	
	map.put("totalpage", pageModel.getTotalPage());
	map.put("rows", pageModel.getResult());
}
catch(Exception ex)
{
	map.put("status", "0");
	map.put("msg", "error");
}
out.print(common.json.GsonUtil.toJson(map));
%>