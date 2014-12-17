package dswork.web;

import javax.servlet.http.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展Request
 */
@SuppressWarnings("all")
public class MyRequest
{
	private HttpServletRequest request;

	/**
	 * 初始化request
	 * @param request HttpServletRequest
	 */
	public MyRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	/**
	 * 取得当前页的URL，如有参数则带参数
	 * @return String
	 */
	public String getCurrentUrl()
	{
		StringBuilder urlThisPage = new StringBuilder();
		urlThisPage.append(request.getRequestURI());
		Enumeration e = request.getParameterNames();
		String key;
		String[] values;
		urlThisPage.append("?");
		if(e.hasMoreElements())
		{
			while(e.hasMoreElements())
			{
				key = String.valueOf(e.nextElement());
				values = getStringArray(key);
				for(int j = 0; j < values.length; j++)
				{
					urlThisPage.append(key);
					urlThisPage.append("=");
					urlThisPage.append(values[j]);
					urlThisPage.append("&");
				}
			}
		}
		return urlThisPage.substring(0, urlThisPage.length() - 1);
	}

	/**
	 * 取得当前页的URL，如有参数则带参数，但多个同名参数以","合并为一个参数
	 * @return String
	 */
	public String getCurrentUrlUniteParameter()
	{
		return getCurrentUrlUniteParameter(",");
	}

	/**
	 * 取得当前页的URL，如有参数则带参数，但多个同名参数以separator合并为一个参数
	 * @param separator 分隔符
	 * @return String
	 */
	public String getCurrentUrlUniteParameter(String separator)
	{
		StringBuilder urlThisPage = new StringBuilder();
		urlThisPage.append(request.getRequestURI());
		Enumeration e = request.getParameterNames();
		String key, values;
		urlThisPage.append("?");
		if(e.hasMoreElements())
		{
			while(e.hasMoreElements())
			{
				key = String.valueOf(e.nextElement());
				values = getStringValues(key, separator);
				urlThisPage.append(key);
				urlThisPage.append("=");
				urlThisPage.append(values);
				urlThisPage.append("&");
			}
		}
		return urlThisPage.substring(0, urlThisPage.length() - 1);
	}

	/**
	 * 从Request中取得double值，如果取得的值为null，则返回0D
	 * @param key request参数名
	 * @return double
	 */
	public double getDouble(String key)
	{
		return getDouble(key, 0D);
	}

	/**
	 * 从Request中取得double值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return double
	 */
	public double getDouble(String key, double defaultValue)
	{
		try
		{
			String str = request.getParameter(key);
			return (str == null || str.trim().equals("")) ? defaultValue : Double.parseDouble(str.trim());
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}

	/**
	 * 返回数组取得多个同名参数值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return double[]
	 */
	public double[] getDoubleArray(String key, double defaultValue)
	{
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				double[] _numArr = new double[_v.length];
				for(int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Double.parseDouble(_v[i].trim());
					}
					catch(NumberFormatException e)
					{
						_numArr[i] = defaultValue;
					}
				}
				return _numArr;
			}
		}
		catch(Exception e)
		{
		}
		return new double[0];
	}

	/**
	 * 从request中获取值并自动填充到Object
	 * @param o Object
	 */
	public void getFillObject(Object o)
	{
		this.getFillObject(o, "");
	}

	/**
	 * 从request中获取值并自动填充到Object
	 * @param o Object
	 * @param clazzName request中获取类的属性key为clazzName加上属性名，即clazzName为key的前缀
	 */
	public void getFillObject(Object o, String clazzName)
	{
		try
		{
			BeanInfo info = Introspector.getBeanInfo(o.getClass());
			PropertyDescriptor[] descritors = info.getPropertyDescriptors();
			String name = "", typeName = "", _tmp, pre_name;
			for(int i = 0; i < descritors.length; i++)
			{
				try
				{
					name = descritors[i].getName();
					pre_name = clazzName + name;
					typeName = descritors[i].getReadMethod().getReturnType().getName();
					_tmp = String.valueOf(request.getParameter(pre_name)); 
					if(name.equals("class") || _tmp.equals("null"))
					{
						continue;
					}
					if(typeName.equals(String.class.getName()))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{_tmp});
					}
					else if(typeName.equals(Long.class.getName()) || typeName.equals("long"))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{Long.parseLong(_tmp)});
					}
					else if(typeName.equals(Integer.class.getName()) || typeName.equals("int"))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{Integer.parseInt(_tmp)});
					}
					else if(typeName.equals(Float.class.getName()) || typeName.equals("float"))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{Float.parseFloat(_tmp)});
					}
					else if(typeName.equals(Double.class.getName()) || typeName.equals("double"))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{Double.parseDouble(_tmp)});
					}
					else if(typeName.equals(Date.class.getName()))
					{
						descritors[i].getWriteMethod().invoke(o, new Object[]{MyRequest.toDate(_tmp)});
					}
					else
					{
						Object obj = descritors[i].getReadMethod().invoke(o);
						if(obj == null)
						{
							obj = (Class.forName(descritors[i].getReadMethod().getReturnType().getName())).newInstance();
							descritors[i].getWriteMethod().invoke(o, obj);
						}
						this.getFillObject(obj, pre_name+".");
						//descritors[i].getWriteMethod().invoke(o, new Object[]{request.getParameter(pre_name)});
					}
				}
				catch(Exception ex)
				{
					System.out.println(typeName + ":" + clazzName + name + ":" + ex);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	/**
	 * 从Request中取得float值，如果取得的值为null，则返回0F
	 * @param key request参数名
	 * @return float
	 */
	public float getFloat(String key)
	{
		return getFloat(key, 0F);
	}

	/**
	 * 从Request中取得float值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return float
	 */
	public float getFloat(String key, float defaultValue)
	{
		try
		{
			String str = request.getParameter(key);
			return (str == null || str.trim().equals("")) ? defaultValue : Float.parseFloat(str.trim());
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}

	/**
	 * 返回数组取得多个同名参数值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return float[]
	 */
	public float[] getFloatArray(String key, float defaultValue)
	{
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				float[] _numArr = new float[_v.length];
				for(int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Float.parseFloat(_v[i].trim());
					}
					catch(NumberFormatException e)
					{
						_numArr[i] = defaultValue;
					}
				}
				return _numArr;
			}
		}
		catch(Exception e)
		{
		}
		return new float[0];
	}

	/**
	 * 将取得的值中的"'替换为&amp;quot; &amp;#039;，如果取得的值为null，则返回空字符串
	 * @param key request参数名
	 * @return String
	 */
	public String getInputValue(String key)
	{
		return getString(key, "").replaceAll("\"", "&quot;").replaceAll("'", "&#039;");
	}

	/**
	 * 从request中取得int值，如果取得的值为null，则返回0
	 * @param key request参数名
	 * @return int
	 */
	public int getInt(String key)
	{
		return getInt(key, 0);
	}

	/**
	 * 从request中取得int值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return int
	 */
	public int getInt(String key, int defaultValue)
	{
		try
		{
			String str = request.getParameter(key);
			return (str == null || str.trim().equals("")) ? defaultValue : Integer.parseInt(str.trim());
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}

	/**
	 * 返回数组取得多个同名参数值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return int[]
	 */
	public int[] getIntArray(String key, int defaultValue)
	{
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				int[] _numArr = new int[_v.length];
				for(int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Integer.parseInt(_v[i].trim());
					}
					catch(NumberFormatException e)
					{
						_numArr[i] = defaultValue;
					}
				}
				return _numArr;
			}
		}
		catch(Exception e)
		{
		}
		return new int[0];
	}

	/**
	 * 从Request中取得long值，如果取得的值为null，则返回0L
	 * @param key request参数名
	 * @return long
	 */
	public long getLong(String key)
	{
		return getLong(key, 0L);
	}

	/**
	 * 从Request中取得long值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return long
	 */
	public long getLong(String key, long defaultValue)
	{
		try
		{
			String str = request.getParameter(key);
			return (str == null || str.trim().equals("")) ? defaultValue : Long.parseLong(str.trim());
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}

	/**
	 * 返回数组取得多个同名参数值，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return long[]
	 */
	public long[] getLongArray(String key, long defaultValue)
	{
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				long[] _numArr = new long[_v.length];
				for(int i = 0; i < _v.length; i++)
				{
					try
					{
						_numArr[i] = Long.parseLong(_v[i].trim());
					}
					catch(NumberFormatException e)
					{
						_numArr[i] = defaultValue;
					}
				}
				return _numArr;
			}
		}
		catch(Exception e)
		{
		}
		return new long[0];
	}

	/**
	 * 取得请求中所有的参数集合形成一个map，根据remainArray参数决定返回字符串数组或字符串
	 * @param remainArray 是否保留为数组，否则以","分隔成一个字符串
	 * @param isSecure 是否过滤为安全字符
	 * @return Map
	 */
	public Map<String, Object> getParameterValueMap(boolean remainArray, boolean isSecure)
	{
		return getParameterValueMap(remainArray, ",", isSecure);
	}

	/**
	 * 取得请求中所有的参数集合形成一个map，根据remainArray参数决定返回字符串数组或字符串
	 * @param remainArray 是否保留为数组，否则以separator分隔成一个字符串
	 * @param separator 分隔符
	 * @param isSecure 是否过滤为安全字符
	 * @return Map
	 */
	public Map<String, Object> getParameterValueMap(boolean remainArray, String separator, boolean isSecure)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String key;
		Enumeration params = request.getParameterNames();
		// 是否保留为数组
		if(remainArray)
		{
			while(params.hasMoreElements())
			{
				key = String.valueOf(params.nextElement());
				map.put(key, getStringArray(key, isSecure));
			}
		}
		else
		{
			while(params.hasMoreElements())
			{
				key = String.valueOf(params.nextElement());
				map.put(key, getStringValues(key, separator, isSecure));
			}
		}
		return map;
	}

	/**
	 * 取的前面页面的地址
	 * @return String
	 */
	public String getRefererURL()
	{
		return this.request.getHeader("referer");
	}

	/**
	 * 取得申请的URL，不包含上下文路径
	 * @return String
	 */
	public String getRequestURI()
	{
		return this.request.getRequestURI().substring(this.request.getContextPath().length());
	}

	/**
	 * 取得安全字符串，如果取得的值为null，则返回空字符串
	 * @param key request参数名
	 * @return String
	 */
	public String getSecureString(String key)
	{
		return getSecureString(key, "");
	}

	/**
	 * 取得安全字符串，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return String
	 */
	public String getSecureString(String key, String defaultValue)
	{
		String value = request.getParameter(key);
		return (value == null) ? defaultValue : filterInject(value);
	}

	/**
	 * 取字符串类型的参数，如果取得的值为null，则返回空字符串
	 * @param key request参数名
	 * @return String
	 */
	public String getString(String key)
	{
		return getString(key, "");
	}

	/**
	 * 取字符串类型的参数，如果取得的值为null，则使用默认值
	 * @param key request参数名
	 * @param defaultValue 默认值
	 * @return String
	 */
	public String getString(String key, String defaultValue)
	{
		String value = request.getParameter(key);
		return (value == null) ? defaultValue : value;
	}

	/**
	 * 返回数组取得多个同名参数值
	 * @param key request参数名
	 * @return String[]
	 */
	public String[] getStringArray(String key)
	{
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				return _v;
			}
		}
		catch(Exception e)
		{
		}
		return new String[0];
	}

	/**
	 * 返回数组取得多个同名参数值
	 * @param key request参数名
	 * @param delEmpty 是否去掉空值
	 * @return String[]
	 */
	public String[] getStringArray(String key, boolean delEmpty)
	{
		return getStringArray(key, delEmpty, false);
	}

	/**
	 * 返回数组取得多个同名参数值
	 * @param key request参数名
	 * @param delEmpty 是否去掉空值
	 * @param isSecure 是否过滤为安全字符
	 * @return String[]
	 */
	public String[] getStringArray(String key, boolean delEmpty, boolean isSecure)
	{
		if(!delEmpty)
		{
			return getStringArray(key);
		}
		try
		{
			String[] _v = request.getParameterValues(key);
			if(_v != null && _v.length > 0)
			{
				List<String> list = new ArrayList<String>();
				for(int i = 0; i < _v.length; i++)
				{
					if(_v[i] == null || _v[i].trim().length() == 0)
					{
						continue;
					}
					list.add(isSecure ? filterInject(_v[i].trim()) : _v[i].trim());
				}
				_v = new String[list.size()];
				for(int i = 0; i < list.size(); i++)
				{
					_v[i] = list.get(i);
				}
				return _v;
			}
		}
		catch(Exception e)
		{
		}
		return new String[0];
	}

	/**
	 * 以","分隔符取得多个同名参数值
	 * @param key request参数名
	 * @return String
	 */
	public String getStringValues(String key)
	{
		return getStringValues(key, ",", false);
	}

	/**
	 * 以分隔符取得多个同名参数值
	 * @param key request参数名
	 * @param separator 分隔符
	 * @return String
	 */
	public String getStringValues(String key, String separator)
	{
		return getStringValues(key, separator, false);
	}

	/**
	 * 以分隔符取得多个同名参数值
	 * @param key request参数名
	 * @param separator 分隔符
	 * @param isSecure 是否过滤为安全字符
	 * @return String
	 */
	public String getStringValues(String key, String separator, boolean isSecure)
	{
		StringBuilder value = new StringBuilder();
		String[] _v = request.getParameterValues(key);
		if(_v != null && _v.length > 0)
		{
			if(isSecure)
			{
				value.append(filterInject(String.valueOf(_v[0]).replaceAll(separator, "")));
				for(int i = 1; i < _v.length; i++)
				{
					value.append(separator);
					value.append(filterInject(String.valueOf(_v[i]).replaceAll(separator, "")));
				}
			}
			else
			{
				value.append(String.valueOf(_v[0]).replaceAll(separator, ""));
				for(int i = 1; i < _v.length; i++)
				{
					value.append(separator);
					value.append(String.valueOf(_v[i]).replaceAll(separator, ""));
				}
			}
		}
		return value.toString();
	}

	/**
	 * 去掉\\||\\&|\\*|\\?|exec\\s|drop\\s|insert\\s|select\\s|delete\\s|update\\s|truncate\\s字符，替换;&lt;&gt;()%为双字节字符，替换
	 * '为''
	 * @param str 需要过滤的String
	 * @return String
	 */
	private static String filterInject(String str)
	{
		// Pattern.CANON_EQ：如表达式"a/u030A"会匹配"?"
		// Pattern.DOTALL：在这种模式下，表达式'.'可以匹配任意字符，包括表示一行的结束符。默认情况下，表达式'.'不匹配行的结束符。
		// Pattern.CASE_INSENSITIVE：对Unicode字符进行大小不明感的匹配
		// Pattern.UNICODE_CASE：在这个模式下，如果还启用了CASE_INSENSITIVE，那么它会对Unicode字符进行大小写不明感的匹配。默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。
		String injectstr = "\\||\\&|\\*|\\?|exec\\s|drop\\s|insert\\s|select\\s|delete\\s|update\\s|truncate\\s";
		Pattern regex = Pattern.compile(injectstr, Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = regex.matcher(str);
		str = matcher.replaceAll("");
		str = str.replace("'", "''");// 过滤sql字符串
		str = str.replace(";", "；");// 过滤中断sql
		str = str.replace("<", "＜");// 过滤脚本,iframe等html标签
		str = str.replace(">", "＞");
		str = str.replace("(", "（");// 处理调用数据库函数，如mid,substring,substr,chr等
		str = str.replace(")", "）");
		str = str.replace("%", "％");
		return str;
	}
	
	private static Date toDate(String value) throws Exception 
	{
		Date obj = null;
		if(value != null)
		{
			value = value.trim();
			int ii = value.length();
			String format = "yyyy-MM-dd";
			switch(ii)
			{
				case 19:
					format = "yyyy-MM-dd HH:mm:ss";
					break;
				case 10:
					format = "yyyy-MM-dd";
					break;
				case 7:
					format = "yyyy-MM";
					break;
				case 4:
					format = "yyyy";
					break;
				case 16:
					format = "yyyy-MM-dd HH:mm";
					break;
				case 13:
					format = "yyyy-MM-dd HH";
					break;
				case 23:
					format = "yyyy-MM-dd HH:mm:ss.SSS";
					break;
			}
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
			obj = sdf.parse(value);
		}
		return obj;
	}
}
