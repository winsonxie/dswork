package dswork.flow.model;

import java.util.Map;

public class DsFlowDataRow
{
	private String tname = "";// 字段名
	private String talias = "";// 名称
	private String tuse = "";// 类型， common通用，file文件，dict字典，extend扩展
	private Map<String, Object>[] ttype;// 类型信息，格式：[{"key":"xxx","val":"xxx"}]
	private String trwx = "";// 字段权限， 400 只读， 420 编辑，001隐藏
	private String tvalue = "";// 值

	public String getTname()
	{
		return tname;
	}

	public void setTname(String tname)
	{
		this.tname = tname;
	}

	public String getTalias()
	{
		return talias;
	}

	public void setTalias(String talias)
	{
		this.talias = talias;
	}

	public String getTuse()
	{
		return tuse;
	}

	public void setTuse(String tuse)
	{
		this.tuse = tuse;
	}

	public Map<String, Object>[] getTtype()
	{
		return ttype;
	}

	public void setTtype(Map<String, Object>[] ttype)
	{
		this.ttype = ttype;
	}

	public String getTrwx()
	{
		return trwx;
	}

	public void setTrwx(String trwx)
	{
		this.trwx = trwx;
	}

	public String getTvalue()
	{
		return tvalue;
	}

	public void setTvalue(String tvalue)
	{
		this.tvalue = tvalue;
	}
}