package dswork.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dswork.common.model.IFlowDataRow;

public class DsCommonTableUtil
{
	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	
	public static String getM(String datatable)
	{
		Map<String, IFlowDataRow> map = new HashMap<String, IFlowDataRow>();
		if (datatable != null && !"".equals(datatable))
		{
			List<IFlowDataRow> list = gson.fromJson(datatable, new TypeToken<List<IFlowDataRow>>(){}.getType());
			for (IFlowDataRow row : list)
			{
				map.put(row.getTname(), row);
			}
		}
		return gson.toJson(map);
	}

	/*public static String getRows(String datatable)
	{
		List<IFlowDataRow> list = new ArrayList<IFlowDataRow>();
		if (datatable != null && !"".equals(datatable))
		{
			list = gson.fromJson(datatable, new TypeToken<List<IFlowDataRow>>(){}.getType());
		}
		return gson.toJson(list);
	}*/
	
	public static String getHtml(String datatable)
	{
		List<IFlowDataRow> list = new ArrayList<IFlowDataRow>();
		StringBuffer sb = new StringBuffer();
		if (datatable != null && !"".equals(datatable))
		{
			list = gson.fromJson(datatable, new TypeToken<List<IFlowDataRow>>(){}.getType());
			sb.append("<table border=\"0\" cellspacing=\"1\" cellpadding=\"0\" class=\"listTable\"> \t\n");
			for (IFlowDataRow row : list)
			{
				if("001".equals(row.getTrwx())){sb.append("<tr style=\"display:none;\">");}
				else{sb.append("<tr>");}
				sb.append("<td class=\"form_title\">" + row.getTalias() + "</td>");
				sb.append("<td class=\"form_input\">" + getDom(row) + "</td>");
				sb.append("</tr> \t\n");
			}
			sb.append("</table> \t\n");
		}
		return  sb.toString();
	}
	
	private static String getDom(IFlowDataRow row){
		StringBuffer sb = new StringBuffer();
		String c = row.getTrwx();
		if("420".equals(row.getTrwx()))
		{
			c = row.getTuse() + "_" + row.getTrwx();
		}
		switch (c)
		{
			case "common_420":
				sb.append("<input type=\"text\" name=\""+row.getTname()+"\" datatype=\""+row.getTtype()[0].get("key")+"\" value=\""+row.getTvalue()+"\" />");
				break;
			case "dict_420":
				sb.append("<span id=\""+row.getTname()+"\"></span>");
				sb.append("<script>$(function(){loaddata(\""+row.getTtype()[0].get("key")+"\", \""+row.getTtype()[0].get("val")+"\", \""+row.getTname()+"\", \"radio\", \""+row.getTname()+"\");});</script>");
				break;
			case "file_420":
				sb.append("<input id=\"id_"+row.getTname()+"\" type=\"hidden\" readonly=\"readonly\" />");
				sb.append("<input id=\"vid_"+row.getTname()+"\" name=\""+row.getTname()+"\" type=\"hidden\" value=\""+row.getTvalue()+"\" />");
				sb.append("\t\n<script>"
						+ "var o = new $dswork.upload({io:true, name:\""+row.getTtype()[0].get("key").toString().toUpperCase()+"\", ext:\""+row.getTtype()[0].get("val")+"\"});"
						+ "$(function(){o.init({id:\"id_"+row.getTname()+"\", vid:\"vid_"+row.getTname()+"\", ext:\""+row.getTtype()[0].get("val")+"\"});});</script>");
				break;
			case "extend_420":
				sb.append("<input type=\"text\" name=\""+row.getTname()+"\" value=\""+row.getTvalue()+"\" />");
				break;
			case "400":
				sb.append("<input type=\"text\" name=\""+row.getTname()+"\" value=\""+row.getTvalue()+"\" readonly />");
				break;
			case "001":
				sb.append("<input type=\"hidden\" name=\""+row.getTname()+"\" value=\""+row.getTvalue()+"\" />");
				break;
		}
		return sb.toString();
	}
	
}
