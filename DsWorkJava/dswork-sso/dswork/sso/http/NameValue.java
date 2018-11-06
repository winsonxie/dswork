package dswork.sso.http;

/**
 * 自定义NameValue
 * @author skey
 * @version 2.0x
 */
public class NameValue
{
	private String name;
	private String value;

	public NameValue(String name, String value)
	{
		this.name = String.valueOf(name).trim();
		this.value = value;
	}

	public String getName()
	{
		return this.name;
	}

	public String getValue()
	{
		return this.value;
	}
}
