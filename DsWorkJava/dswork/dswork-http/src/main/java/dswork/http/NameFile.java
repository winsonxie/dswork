package dswork.http;

/**
 * 扩展自定义NameFile
 * @author skey
 * @version 2.0
 */
public class NameFile extends NameValue
{
	private String filename = "";
	private String contenttype = "";
	private byte[] fileobject = null;

	/**
	 * 构造方法
	 * @param name String
	 * @param filename String
	 * @param fileobject byte[]
	 */
	public NameFile(String name, String filename, byte[] fileobject)
	{
		this(name, filename, "application/octet-stream", fileobject);
	}

	/**
	 * 构造方法
	 * @param name String
	 * @param filename String
	 * @param contenttype String
	 * @param fileobject byte[]
	 */
	public NameFile(String name, String filename, String contenttype, byte[] fileobject)
	{
		super(name, "");
		this.setFormdata(true);
		this.filename = filename;
		this.contenttype = contenttype;
		this.fileobject = fileobject;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getContenttype()
	{
		return contenttype;
	}

	public void setContenttype(String contenttype)
	{
		this.contenttype = contenttype;
	}

	public byte[] getFileobject()
	{
		return fileobject;
	}

	public void setFileobject(byte[] fileobject)
	{
		this.fileobject = fileobject;
	}
}
