package dswork.sso.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装http请求
 * @author skey
 * @version 2.0x
 */
public class HttpUtil
{
	static Logger log = LoggerFactory.getLogger(HttpUtil.class.getName());
	private HttpURLConnection http;
	private boolean isHttps = false;
	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64)";// Gecko/20150123
	private List<NameValue> form = new ArrayList<NameValue>();// 表单项

	public HttpUtil addForm(String name, String value)
	{
		form.add(new NameValue(name, value));
		return this;
	}

	public HttpUtil create(String url, boolean isHostnameVerifier)
	{
		form.clear();
		URL c;
		try
		{
			c = new URL(url);
			isHttps = c.getProtocol().toLowerCase().equals("https");
			this.http = (HttpURLConnection) c.openConnection();
			if(isHttps)
			{
				HttpsURLConnection https = (HttpsURLConnection) this.http;
				if(isHostnameVerifier)
				{
					https.setHostnameVerifier(HV);// 不进行主机名确认
				}
			}
			this.http.setDoInput(true);
			this.http.setDoOutput(false);
			this.http.setConnectTimeout(10000);
			this.http.setReadTimeout(30000);
			this.http.setRequestProperty("User-Agent", userAgent);
			this.http.setRequestProperty("Accept-Charset", "utf-8");
			this.http.setRequestMethod("GET");
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return this;
	}
	
	/**
	 * 连接并返回网页文本
	 * @return 连接失败返回null
	 */
	public String connect()
	{
		String result = null;
		try
		{
			byte[] arr = null;
			if(this.form.size() > 0)
			{
				String data = format(form, "UTF-8");
				arr = data.getBytes("ISO-8859-1");
			}
			this.http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if(arr != null)
			{
				this.http.setDoOutput(true);
				this.http.setUseCaches(false);
				if(this.http.getRequestMethod().toUpperCase().equals("GET"))// DELETE, PUT, POST
				{
					this.http.setRequestMethod("POST");
				}
				DataOutputStream out = new DataOutputStream(this.http.getOutputStream());
				out.write(arr, 0, arr.length);
				out.flush();
				out.close();
			}
			this.http.connect();
			int responseCode = http.getResponseCode();// 设置http返回状态200（ok）还是403
			if(responseCode >= 200 && responseCode < 300)
			{
				BufferedReader in = null;
				in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
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
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		try
		{
			http.disconnect();
		}
		catch(Exception e)
		{
		}
		return result;
	}

	public final static HostnameVerifier HV = new HostnameVerifier()
	{
		public boolean verify(String urlHostName, SSLSession session)
		{
			return true;
		}
	};

	private static final String NAME_VALUE_SEPARATOR = "=";
	private static final String PARAMETER_SEPARATOR = "&";
	public static String format(List<? extends NameValue> parameters, String charsetName)
	{
		StringBuilder result = new StringBuilder();
		for(NameValue parameter : parameters)
		{
			try
			{
				String encodedName = java.net.URLEncoder.encode(parameter.getName(), charsetName);
				String encodedValue = java.net.URLEncoder.encode(parameter.getValue(), charsetName);
				if(result.length() > 0)
				{
					result.append(PARAMETER_SEPARATOR);
				}
				result.append(encodedName);
				if(encodedValue != null)
				{
					result.append(NAME_VALUE_SEPARATOR);
					result.append(encodedValue);
				}
			}
			catch(Exception e)
			{
			}
		}
		return result.toString();
	}
}
