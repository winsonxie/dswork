package dswork.authown;

public class AuthOwn
{
	private String id;// ID
	private String account;// 账号
	private String name;// 姓名
	private String own;// 网站所有者
	
	public AuthOwn(String id, String account, String name, String own)
	{
		this.id = id;
		this.account = account;
		this.name = name;
		this.own = own;
	}

	public String getId()
	{
		return id;
	}

	public String getAccount()
	{
		return account;
	}

	public String getName()
	{
		return name;
	}

	public String getOwn()
	{
		return own;
	}
}
