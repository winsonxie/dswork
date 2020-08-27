package dswork.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * 封装http请求
 * @author skey
 * @version 8.8.9
 */
public class HttpUtil
{
	private static String boundaryContentType = "multipart/form-data; boundary=----WebKitFormBoundaryForDsworkAbcdefg";
	
	private SSLSocketFactory sslSocketFactory;
	
	private HttpURLConnection http = null;
	
	private boolean connectd = false;
	private Map<String, List<String>> requestPropertyMap = new LinkedHashMap<String, List<String>>();
	
	private int connectTimeout = 10000;
	private String contentType = null;
	private int readTimeout = 30000;
	private int responseCode = 0;
	private String requestMethod = null;
	private String url = "";
	private boolean useCaches = false;
	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0";
	private boolean isHostnameVerifier = true;

	/**
	 * 设置超时时间毫秒
	 * @param connectTimeout int
	 * @return HttpUtil
	 */
	public HttpUtil setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
		return this;
	}

	/**
	 * 设置contentType
	 * @param contentType String
	 * @return HttpUtil
	 */
	public HttpUtil setContentType(String contentType)
	{
		this.contentType = contentType;
		return this;
	}

	/**
	 * 设置读取超时时间毫秒
	 * @param readTimeout int
	 * @return HttpUtil
	 */
	public HttpUtil setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
		return this;
	}

	/**
	 * 设置requestMethod
	 * @param requestMethod String
	 * @return HttpUtil
	 */
	public HttpUtil setRequestMethod(String requestMethod)
	{
		this.requestMethod = requestMethod.toUpperCase(Locale.ENGLISH);
		return this;
	}

	/**
	 * 设置requestProperty
	 * @param key String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil setRequestProperty(String key, String value)
	{
		try
		{
			if("Content-Type".equalsIgnoreCase(key))
			{
				setContentType(value);
			}
			else
			{
				if(value == null)
				{
					requestPropertyMap.remove(key);
				}
				else
				{
					List<String> list = requestPropertyMap.get(key);
					if(list == null)
					{
						list = new ArrayList<String>();
						requestPropertyMap.put(key, list);
					}
					list.clear();
					list.add(value);
				}
			}
		}
		catch(Exception e)
		{
		}
		return this;
	}

	/**
	 * 设置requestProperty
	 * @param key String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil addRequestProperty(String key, String value)
	{
		try
		{
			if("Content-Type".equalsIgnoreCase(key))
			{
				setContentType(value);
			}
			else
			{
				if(value != null)
				{
					List<String> list = requestPropertyMap.get(key);
					if(list == null)
					{
						list = new ArrayList<String>();
						requestPropertyMap.put(key, list);
					}
					list.add(value);
				}
			}
		}
		catch(Exception e)
		{
		}
		return this;
	}

	/**
	 * 获得HeaderField
	 * @param key String
	 * @return String
	 */
	public String getHeaderField(String key)
	{
		return connectd ? this.http.getHeaderField(key) : null;
	}

	/**
	 * 获得HeaderFields
	 * @return Map&lt;String, List&lt;String&gt;&gt;
	 */
	public Map<String, List<String>> getHeaderFields()
	{
		return connectd ? this.http.getHeaderFields() : null;
	}

	/**
	 * 设置sslSocketFactory
	 * @param sslSocketFactory SslSocketFactory
	 * @return HttpUtil
	 */
	public HttpUtil setSslSocketFactory(SSLSocketFactory sslSocketFactory)
	{
		this.sslSocketFactory = sslSocketFactory;
		return this;
	}

	/**
	 * 设置useCaches
	 * @param useCaches boolean
	 * @return HttpUtil
	 */
	public HttpUtil setUseCaches(boolean useCaches)
	{
		this.useCaches = useCaches;
		return this;
	}

	/**
	 * 设置userAgent
	 * @param userAgent String
	 * @return HttpUtil
	 */
	public HttpUtil setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
		return this;
	}

	/**
	 * 创建新的http(s)请求，重置除cookie外的所有设置
	 * @param url url地址请求
	 * @return HttpUtil
	 */
	public HttpUtil create(String url)
	{
		return create(url, true);
	}

	/**
	 * 创建新的http(s)请求，重置除cookie、connectTimeout、readTimeout、userAgent外的所有设置
	 * @param url url地址请求
	 * @param isHostnameVerifier 是否不确认主机名
	 * @return HttpUtil
	 */
	public HttpUtil create(String url, boolean isHostnameVerifier)
	{
		connectClose();
		this.url = url;
		
		// 还原
		this.connectd = false;
		this.clearForm();
		this.responseCode = 0;
		this.requestMethod = null;
		this.requestPropertyMap.clear();
		this.isHostnameVerifier = isHostnameVerifier;

		return this;
	}

	private void connectDoing(String charsetName) throws IOException
	{
		this.connectd = true;// 标记连接开始
		boolean isHttps = false;
		URL c;
		try
		{
			c = new URL(url);
			isHttps = c.getProtocol().toLowerCase().equals("https");
			this.http = (HttpURLConnection) c.openConnection();
			if(isHttps)
			{
				HttpsURLConnection https = (HttpsURLConnection) this.http;
				if(this.sslSocketFactory != null)
				{
					https.setSSLSocketFactory(this.sslSocketFactory);
				}
				if(this.isHostnameVerifier)
				{
					https.setHostnameVerifier(HttpCommon.HV);// 不进行主机名确认
				}
			}
		}
		catch(Exception e)
		{
		}
		
		this.http.setDoInput(true);// 可以使用conn.getInputStream().read()，默认值为 true
		// this.http.setDoOutput(false);
		this.http.setUseCaches(useCaches);
		this.http.setConnectTimeout(connectTimeout);
		this.http.setReadTimeout(readTimeout);
		this.http.setRequestProperty("User-Agent", userAgent);
		this.http.setRequestProperty("Accept-Charset", "utf-8");
		// GET DELETE, PUT, POST
		this.http.setRequestMethod(this.requestMethod == null ? "GET" : this.requestMethod);
		
		int len;
		for(String key : requestPropertyMap.keySet())
		{
			List<String> list = requestPropertyMap.get(key);
			this.http.setRequestProperty(key, list.get(0));
			len = list.size();
			if(len > 1)
			{
				for(int i = 1; i < len; i++)
				{
					this.http.addRequestProperty(key, list.get(i));
				}
			}
		}
		
		if(this.cookies.size() > 0)
		{
			String _c = HttpCommon.parse(HttpCommon.getHttpCookies(this.cookies, isHttps), "; ");
			http.setRequestProperty("Cookie", _c);
		}
		byte[] arr = null;
		if(this.form.size() > 0)
		{
			if(formdata)
			{
				this.contentType = boundaryContentType;
				arr = HttpCommon.formatFormdata(form, charsetName);
			}
			else
			{
				String data = HttpCommon.format(form, charsetName);
				arr = data.getBytes("ISO-8859-1");
			}
		}
		else if(databody != null)
		{
			arr = databody;
			if(this.contentType == null)
			{
				this.contentType = "application/octet-stream";
			}
		}
		java.util.List<String> list = this.http.getRequestProperties().get("Content-Type");
		if(list != null && list.size() > 0)
		{
			if(!list.contains(this.contentType))
			{
				this.http.addRequestProperty("Content-Type", this.contentType);
			}
		}
		else
		{
			this.http.setRequestProperty("Content-Type", this.contentType != null ? this.contentType : "application/x-www-form-urlencoded");
		}
		if(arr != null)
		{
			this.http.setDoOutput(true);// 可以使用conn.getOutputStream().write()，默认值为 false
			if(this.http.getRequestMethod().toUpperCase().equals("GET"))// DELETE, PUT, POST
			{
				this.http.setRequestMethod("POST");
			}
			// this.http.setRequestProperty("Content-Length", String.valueOf(data.length()));
			DataOutputStream out = new DataOutputStream(this.http.getOutputStream());
			out.write(arr, 0, arr.length);
			// out.writeBytes(data);
			out.flush();
			out.close();
		}
		this.responseCode = 0;// 还原
		this.http.connect();
		this.responseCode = http.getResponseCode();// 设置http返回状态200（ok）还是403
	}

	private InputStream connectAfter() throws IOException
	{
		Date date = new Date();
		List<Cookie> list = HttpCommon.getHttpCookies(http);
		for(Cookie m : list)
		{
			if(m.getExpiryDate() == null)
			{
				this.addCookie(m.getName(), m.getValue());// 会话cookie
			}
			else
			{
				if(!m.isExpired(date))
				{
					this.addCookie(m.getName(), m.getValue());
				}
			}
		}
		String encoding = http.getContentEncoding();
		if("gzip".equals(encoding))
		{
			return new java.util.zip.GZIPInputStream(http.getInputStream());
		}
		else
		{
			return http.getInputStream();
		}
	}

	/**
	 * 调用connectStream后，需要手工关闭
	 */
	public void connectClose()
	{
		try
		{
			if(connectd && http != null)
			{
				http.disconnect();
			}
		}
		catch(Exception e)
		{
		}
	}

	/**
	 * 连接并返回网页文本
	 * @return 连接失败返回null
	 */
	public String connect()
	{
		return connect("UTF-8", "UTF-8");
	}

	/**
	 * 连接并返回网页文本
	 * @param charsetName 对封装的表单、获取的网页内容进行的编码设置
	 * @return 连接失败返回null
	 */
	public String connect(String charsetName)
	{
		return connect(charsetName, charsetName);
	}

	/**
	 * 连接并返回网页文本
	 * @param upCharsetName 对封装的表单的编码设置
	 * @param downCharsetName 对获取的网页内容进行的编码设置
	 * @return 连接失败返回null
	 */
	public String connect(String upCharsetName, String downCharsetName)
	{
		String result = null;
		try
		{
			connectDoing(upCharsetName);
			InputStream instream = connectAfter();
			BufferedReader in = new BufferedReader(new InputStreamReader(instream, downCharsetName));
			String temp = in.readLine();
			while(temp != null)
			{
				if(result != null)
				{
					result += temp;
				}
				else
				{
					result = temp;
				}
				temp = in.readLine();
			}
			in.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		connectClose();
		return result;
	}

	/**
	 * 连接并返回网页流
	 * @return 连接失败返回null
	 */
	public InputStream connectStream()
	{
		return connectStream("UTF-8");
	}

	/**
	 * 连接并返回网页流
	 * @param upCharsetName 对封装的表单的编码设置
	 * @return 连接失败返回null
	 */
	public InputStream connectStream(String upCharsetName)
	{
		try
		{
			connectDoing(upCharsetName);
			InputStream instream = connectAfter();
			return instream;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		connectClose();
		return null;
	}

	public void initSocketFactoryForSSL()
	{
		this.sslSocketFactory = HttpCommon.getSocketFactoryForSSL();
	}

	public void initSocketFactoryForTLS()
	{
		this.sslSocketFactory = HttpCommon.getSocketFactoryForTLS();
	}
	// post的数据流，与Form数据冲突
	private byte[] databody = null;

	/**
	 * 设置数据流，优先使用form值
	 * @param arr byte[]
	 * @return HttpUtil
	 */
	public HttpUtil setDataBody(byte[] arr)
	{
		databody = arr;
		return this;
	}
	// 表单项
	private List<NameValue> form = new ArrayList<NameValue>();
	private boolean formdata = false;

	/**
	 * 清除已清加的表单项
	 * @return HttpUtil
	 */
	public HttpUtil clearForm()
	{
		form.clear();
		formdata = false;
		return this;
	}

	/**
	 * 添加表单项
	 * @param name String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil addForm(String name, String value)
	{
		NameValue c = new NameValue(name, value);
		if(c.getName().length() > 0)
		{
			form.add(c);
		}
		return this;
	}

	/**
	 * 添加文件表单项
	 * @param name String
	 * @param filename String
	 * @param fileobject byte[]
	 * @return HttpUtil
	 */
	public HttpUtil addForm(String name, String filename, byte[] fileobject)
	{
		NameFile c = new NameFile(name, filename, fileobject);
		if(c.getName().length() > 0)
		{
			form.add(c);
			formdata = true;
		}
		return this;
	}

	/**
	 * 添加文件表单项
	 * @param name String
	 * @param filename String
	 * @param contenttype String
	 * @param fileobject byte[]
	 * @return HttpUtil
	 */
	public HttpUtil addForm(String name, String filename, String contenttype, byte[] fileobject)
	{
		NameFile c = new NameFile(name, filename, contenttype, fileobject);
		if(c.getName().length() > 0)
		{
			form.add(c);
			formdata = true;
		}
		return this;
	}

	/**
	 * 批量添加表单项
	 * @param array NameValue[]
	 * @return HttpUtil
	 */
	public HttpUtil addForms(NameValue[] array)
	{
		for(NameValue c : array)
		{
			if(c.getName().length() > 0)
			{
				if(c.isFormdata())
				{
					formdata = true;
				}
				form.add(c);
			}
		}
		return this;
	}
	// cookie
	private List<Cookie> cookies = new ArrayList<Cookie>();

	/**
	 * 清除已清加的cookie
	 * @return HttpUtil
	 */
	public HttpUtil clearCookies()
	{
		cookies.clear();
		return this;
	}

	/**
	 * 添加cookie
	 * @param name String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil addCookie(String name, String value)
	{
		cookies.add(new Cookie(name, value));
		return this;
	}

	/**
	 * 批量添加cookie
	 * @param array Cookie[]
	 * @return HttpUtil
	 */
	public HttpUtil addCookies(Cookie[] array)
	{
		for(Cookie c : array)
		{
			cookies.add(c);
		}
		return this;
	}

	/**
	 * 复制cookie
	 * @param onlySessionCookie true：仅复制会话cookie false：复制非会话cookie null:全部cookie
	 * @return List&lt;Cookie&gt;
	 */
	public List<Cookie> getCloneCookies(Boolean onlySessionCookie)
	{
		List<Cookie> lists = HttpCommon.getHttpCookies(this.cookies, true);
		List<Cookie> list = new ArrayList<Cookie>();
		if(onlySessionCookie == null)
		{
			for(Cookie m : lists)
			{
				list.add(m.clone());
			}
		}
		else if(onlySessionCookie)
		{
			for(Cookie m : lists)
			{
				if(m.getExpiryDate() == null)
				{
					list.add(m.clone());
				}
			}
		}
		else
		{
			for(Cookie m : lists)
			{
				if(m.getExpiryDate() != null)
				{
					list.add(m.clone());
				}
			}
		}
		return list;
	}

	/**
	 * 返回http连接状态码
	 * @return int
	 */
	public int getResponseCode()
	{
		return this.responseCode;
	}

	/**
	 * 返回create中的url地址
	 * @return String
	 */
	public String getUrl()
	{
		return url;
	}
}
