package dswork.http;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * HttpCommon主要供HttpUtil内部调用
 * @author skey
 * @version 2.0
 */
public class HttpCommon
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT'", Locale.US);
	private static Date convertGMT(String value)
	{
		if(value == null)
		{
			return null;
		}
		try
		{
			if(value.indexOf("-") == 7)
			{
				value = value.substring(0, 7) + " " + value.substring(8, 11) + " " + value.substring(12, value.length());
			}
			if(!value.endsWith("GMT") && value.lastIndexOf(" ") >= 23)
			{
				value = value.substring(0, value.lastIndexOf(" ")) + " GMT";
			}
			return sdf.parse(value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private static SSLSocketFactory socketFactoryForSSL;
	private static SSLSocketFactory socketFactoryForTLS;
	private static final TrustManager tm = new TM();
	static
	{
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try
		{
			javax.net.ssl.SSLContext scSSL = javax.net.ssl.SSLContext.getInstance("SSL");
			scSSL.init(null, new TrustManager[]{tm}, null);
			socketFactoryForSSL = scSSL.getSocketFactory();
			
			javax.net.ssl.SSLContext scTLS = javax.net.ssl.SSLContext.getInstance("TLS");
			scTLS.init(null, new TrustManager[]{tm}, null);
			socketFactoryForSSL = scTLS.getSocketFactory();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static class TM implements TrustManager, javax.net.ssl.X509TrustManager
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException
		{
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException
		{
			return;
		}
	}
	
	public final static HostnameVerifier HV = new HostnameVerifier()
	{
		public boolean verify(String urlHostName, SSLSession session)
		{
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
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
			if(parameter.isFormdata())
			{
				continue;
			}
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
				e.printStackTrace();
			}
		}
		return result.toString();
	}

//	private static void toByteArr(String args) throws java.io.UnsupportedEncodingException
//	{
//		System.out.print("{");
//		if(args.length() > 0)
//		{
//			byte[] arr = args.getBytes("ISO-8859-1");
//			System.out.print(arr[0]);
//			for(int i = 1; i < arr.length; i++)
//			{
//				System.out.print(", ");
//				System.out.print(arr[i]);
//			}
//		}
//		System.out.println("}");
//	}
//	
//	public static void main(String[] args) throws Exception
//	{
//		toByteArr("Content-Disposition: form-data; name=\"");
//		toByteArr("\"; filename=\"");
//		toByteArr("\"");
//		toByteArr("Content-Type: ");
//		toByteArr("\r\n");
//		toByteArr("--");
//		toByteArr("----WebKitFormBoundaryForDsworkAbcdefg");
//	}
	
	//【Content-Disposition: form-data; name=\"】
	private static byte[] B_T = {67, 111, 110, 116, 101, 110, 116, 45, 68, 105, 115, 112, 111, 115, 105, 116, 105, 111, 110, 58, 32, 102, 111, 114, 109, 45, 100, 97, 116, 97, 59, 32, 110, 97, 109, 101, 61, 34};
	//【\"; filename=\"】
	private static byte[] B_M = {34, 59, 32, 102, 105, 108, 101, 110, 97, 109, 101, 61, 34};
	//【\"】
	private static byte[] B_D = {34};
	//【Content-Type: 】
	private static byte[] B_C = {67, 111, 110, 116, 101, 110, 116, 45, 84, 121, 112, 101, 58, 32};
	//【\r\n】
	private static byte[] B_RN = {13, 10};
	//【--】
	private static byte[] B_JJ = {45, 45};
	//【----WebKitFormBoundaryForDsworkAbcdefg】对应HttpUtil中的boundaryContentType
	private static byte[] B_BOUNDARY = {45, 45, 45, 45, 87, 101, 98, 75, 105, 116, 70, 111, 114, 109, 66, 111, 117, 110, 100, 97, 114, 121, 70, 111, 114, 68, 115, 119, 111, 114, 107, 65, 98, 99, 100, 101, 102, 103};

	// Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"
	// Content-Type: %s
	
	// Content-Disposition: form-data; name=\"%s\"
	// 
	// %s
	public static byte[] formatFormdata(List<? extends NameValue> parameters, String charsetName) throws java.io.IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
		bout.write(B_JJ);
		bout.write(B_BOUNDARY);
		for(NameValue nv : parameters)
		{
			bout.write(B_RN);
			bout.write(B_T);
			bout.write(nv.getName().getBytes(charsetName));
			if(nv.isFormdata())
			{
				NameFile nf = (NameFile)nv;
				bout.write(B_M);
				bout.write(nf.getFilename().getBytes(charsetName));
				bout.write(B_D);
				bout.write(B_RN);
				
				bout.write(B_C);
				bout.write(nf.getContenttype().getBytes(charsetName));
				bout.write(B_RN);
				
				bout.write(B_RN);
				bout.write(nf.getFileobject());
			}
			else
			{
				bout.write(B_D);
				bout.write(B_RN);
				
				bout.write(B_RN);
				bout.write(nv.getValue().getBytes(charsetName));
			}
			bout.write(B_RN);
			bout.write(B_JJ);
			bout.write(B_BOUNDARY);
		}
		bout.write(B_JJ);
		bout.close();
		return bout.toByteArray();
	}

	public static String parse(List<? extends Cookie> parameters, String parameterSeparator)
	{
		StringBuilder result = new StringBuilder();
		for(Cookie parameter : parameters)
		{
			if(result.length() > 0)
			{
				result.append(parameterSeparator);
			}
			result.append(parameter.getName());
			if(parameter.getValue() != null)
			{
				result.append(NAME_VALUE_SEPARATOR);
				result.append(parameter.getValue());
			}
		}
		return result.toString();
	}

	public static List<Cookie> getHttpCookies(HttpURLConnection http)
	{
		List<Cookie> list = new ArrayList<Cookie>();
		String v = null;
		String key = null;
		Date now = new Date();
		for(int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++)
		{
			try
			{
				if(key.equalsIgnoreCase("set-cookie"))
				{
					v = http.getHeaderField(i);
					//System.out.print("\n****set-cookie===" + v);
					String[] arr = v.split(";");
					String[] _m = arr[0].split("=", 2);
					Cookie c = new Cookie(_m[0], _m[1]);
					Map<String, String> map = new HashMap<String, String>();
					List<NameValue> vlist = new ArrayList<NameValue>();
					String _t = null;
					for(int j = 1; j < arr.length; j++)
					{
						_t = arr[j].trim();
						try
						{
							_m = _t.split("=", 2);
							vlist.add(new NameValue(_m[0].toLowerCase(Locale.ROOT), _m[1]));
							map.put(_m[0].toLowerCase(Locale.ROOT), _m[1]);
						}
						catch(Exception ex)
						{
							if(_t.equalsIgnoreCase("secure"))
							{
								c.setSecure(true);
							}
							else if(_t.equalsIgnoreCase("httponly"))
							{
								c.setHttpOnly(true);
							}
						}
					}
					String t = map.get("expires");
					if(t != null)
					{
						c.setExpiryDate(HttpCommon.convertGMT(t));
					}
					t = map.get("max-age");
					if(t != null)
					{
						try
						{
							long d = Long.parseLong(t);
							c.setExpiryDate(new Date(now.getTime() + d));
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
					if(c.getExpiryDate() == null || !c.isExpired(now))
					{
						t = map.get("path");
						if(t != null)
						{
							c.setPath(t);
						}
						t = map.get("domain");
						if(t != null && c.getExpiryDate() != null)
						{
							c.setDomain(t);
						}
					}
					list.add(c);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void refreshCookies(List<Cookie> cookies)
	{
		Date date = new Date();
		for(int i = cookies.size() - 1; i >=0; i--)
		{
			if(cookies.get(i).isExpired(date))
			{
				cookies.remove(i);// 移除超时的
			}
		}
	}
	
	public static List<Cookie> getHttpCookies(List<Cookie> cookies, boolean hasSecure)
	{
		HttpCommon.refreshCookies(cookies);
		List<Cookie> list;
		if(hasSecure)
		{
			list = cookies;
		}
		else
		{
			list = new ArrayList<Cookie>();
			for(Cookie m : cookies)
			{
				if(!m.isSecure())
				{
					list.add(m.clone());
				}
			}
		}
		return list;
	}

	public static SSLSocketFactory getSocketFactoryForSSL()
	{
		return socketFactoryForSSL;
	}

	public static SSLSocketFactory getSocketFactoryForTLS()
	{
		return socketFactoryForTLS;
	}
	
	public static TrustManager getTrustManager()
	{
		return tm;
	}
}
