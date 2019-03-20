/**
 * 功能资源Model
 */
package dswork.common.model;

public class DsCommonRes
{
	// 资源对应的URL
	private String url = "";
	// 参数(格式:name1=value2,name2=value2...)
	private String param = "";

	public void setUrl(String url)
	{
		this.url = String.valueOf(url).trim();
	}

	public String getUrl()
	{
		return this.url;
	}

	public void setParam(String param)
	{
		this.param = String.valueOf(param).trim();
	}

	public String getParam()
	{
		return this.param;
	}
}
