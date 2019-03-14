<%!
private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
%><%
java.io.InputStream in = null;
try
{
	String s_name = String.valueOf(request.getParameter("name")).replace("/", "").replace("\\", "").replace(".", "");//经由get方式传递过来的文件名
	String f = String.valueOf(request.getParameter("f")).replace("/", "").replace("\\", "");//经由get方式传递过来的文件名
	String t = String.valueOf(request.getParameter("t"));//经由get方式传递过来的下载文件名
	if(s_name.length() > 0 && !s_name.equals("null") && f.length() > 33)// f=****.***
	{
		StringBuilder sb = new StringBuilder();
		String path = sb.append(WebioUtil.PATH)
				.append("/").append(s_name)
				.append("/").append(f.substring( 0,  4))//1679616=36^4
				.append("/").append(f.substring( 4,  8))
				.append("/").append(f.substring( 8, 12))
				.append("/").append(f.substring(12, 16))
				.append("/").append(f.substring(16, 20))
				.append("/").append(f.substring(20, 24))
				.append("/").append(f.substring(24, 28))
				.append("/").append(f.substring(28, 32))
				.append("/").append(f)
				.toString();
		ServletOutputStream sos = response.getOutputStream();
		response.setContentType("application/octet-stream");
		
		sb.setLength(0);
		
		sb.append("attachment;filename=\"")
		.append(!(t.equals("null")) ? new String(java.net.URLDecoder.decode(t, "UTF-8").getBytes("GBK"), "ISO-8859-1") : f)
		.append("\"");
		
		response.addHeader("Content-Disposition", sb.toString());
		in = new java.io.FileInputStream(path);
		int b;
		while((b = in.read()) != -1)
		{
			sos.write(b);
		}
		in.close();
		sos.flush();
		sos.close();
		return;
	}
}
catch(Exception ex)
{
	ex.printStackTrace();
}
try
{
	if(in != null)
	{
		in.close();
	}
}
catch(Exception ex)
{
	ex.printStackTrace();
}
%><%@page language="java" pageEncoding="UTF-8"
import="dswork.webio.WebioUtil"%>