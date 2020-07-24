package dswork.mvc;

/**
 * 自定义标记json格式
 * @author skey
 */
public class JsonResult<T>
{
	private int code = 200;
	private T data = null;
	private String msg = null;

	/**
	 * 状态码
	 * @return int
	 */
	public int getCode()
	{
		return code;
	}

	/**
	 * 状态码
	 * @param code
	 */
	public void setCode(int code)
	{
		this.code = code;
	}

	/**
	 * 自定义泛型数据
	 * @return &lt;T&gt;
	 */
	public T getData()
	{
		return data;
	}

	/**
	 * 自定义泛型数据
	 * @param data
	 */
	public void setData(T data)
	{
		this.data = data;
	}

	/**
	 * 消息
	 * @return String
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * 消息
	 * @param msg
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
