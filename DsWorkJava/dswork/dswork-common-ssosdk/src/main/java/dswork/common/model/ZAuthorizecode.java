package dswork.common.model;

public class ZAuthorizecode
{
	private String code = "";
	private long expires_in;// 单位毫秒
	private String redirect_uri = "";
	private String value = "";

	public ZAuthorizecode(String code, long expires_in, String redirect_uri, String value)
	{
		this.code = code;
		this.expires_in = expires_in;
		this.redirect_uri = redirect_uri;
		this.value = value;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public long getExpires_in()
	{
		return expires_in;
	}

	public void setExpires_in(long expires_in)
	{
		this.expires_in = expires_in;
	}

	public String getRedirect_uri()
	{
		return redirect_uri;
	}

	public void setRedirect_uri(String redirect_uri)
	{
		this.redirect_uri = redirect_uri;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
