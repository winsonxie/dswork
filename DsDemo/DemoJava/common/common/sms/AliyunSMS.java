package common.sms;

public class AliyunSMS
{
	/**
	 * {"Message":"OK","RequestId":"D5992B13-F912-4307-AD8E-63427AA22CBF","BizId":"303717826362176026^0","Code":"OK"}
	 * @return json
	 */
	public static String sendSMS(String accessKeyId, String accessSecret, String signName, String templateCode, String mobile, String msgJson, String outid) throws Exception
	{
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));// 这里一定要设置GMT时区
		
		java.util.Map<String, String> paras = new java.util.HashMap<String, String>();
		// 1. 系统参数
		paras.put("SignatureMethod", "HMAC-SHA1");
		paras.put("SignatureNonce", java.util.UUID.randomUUID().toString());
		paras.put("AccessKeyId", accessKeyId);
		paras.put("SignatureVersion", "1.0");
		paras.put("Timestamp", df.format(new java.util.Date()));
		paras.put("Format", "JSON");// 返回消息是xml还是json格式
		// 2. 业务API参数
		paras.put("Action", "SendSms");
		paras.put("Version", "2017-05-25");
		paras.put("RegionId", "cn-hangzhou");
		paras.put("PhoneNumbers", mobile);
		paras.put("SignName", signName);
		paras.put("TemplateParam", msgJson);
		paras.put("TemplateCode", templateCode);
		paras.put("OutId", outid);
		// 3. 去除签名关键字Key
		if(paras.containsKey("Signature"))
			paras.remove("Signature");
		// 4. 参数KEY排序
		java.util.TreeMap<String, String> sortParas = new java.util.TreeMap<String, String>();
		sortParas.putAll(paras);
		// 5. 构造待签名的字符串
		java.util.Iterator<String> it = sortParas.keySet().iterator();
		StringBuilder sortQueryStringTmp = new StringBuilder();
		while(it.hasNext())
		{
			String key = it.next();
			sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(paras.get(key)));
		}
		String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
		StringBuilder stringToSign = new StringBuilder();
		stringToSign.append("GET").append("&");
		stringToSign.append(specialUrlEncode("/")).append("&");
		stringToSign.append(specialUrlEncode(sortedQueryString));
		String sign = sign(accessSecret + "&", stringToSign.toString());
		// 6. 签名最后也要做特殊URL编码
		String signature = specialUrlEncode(sign);
		//System.out.println(paras.get("SignatureNonce"));
		//System.out.println("\r\n=========\r\n");
		//System.out.println(paras.get("Timestamp"));
		//System.out.println("\r\n=========\r\n");
		//System.out.println(sortedQueryString);
		//System.out.println("\r\n=========\r\n");
		//System.out.println(stringToSign.toString());
		//System.out.println("\r\n=========\r\n");
		//System.out.println(sign);
		//System.out.println("\r\n=========\r\n");
		//System.out.println(signature);
		//System.out.println("\r\n=========\r\n");
		// 最终打印出合法GET请求的URL
		//System.out.println("http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp);
		dswork.http.HttpUtil http = new dswork.http.HttpUtil();
		return http.create("http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp).connect();
	}

	private static String specialUrlEncode(String value) throws Exception
	{
		return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}

	@SuppressWarnings("restriction")
	private static String sign(String accessSecret, String stringToSign) throws Exception
	{
		javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
		mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		return new sun.misc.BASE64Encoder().encode(signData);
	}
}
