package dswork.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dswork.common.model.IUnit;
import dswork.common.util.CodeUtil;
import dswork.common.util.ResponseUtil;
import dswork.common.util.TokenUnitUtil;
import dswork.common.util.UnitUtil;
import dswork.web.MyRequest;

@Controller
@RequestMapping("/sms")
public class SmsController
{
	static
	{
		UserController.useSMS = true;// 标记已开启短信模块
	}
	
	/**
	 * 应用使用短信验证码
	 */
	@RequestMapping("/code")
	public void code(HttpServletRequest request, HttpServletResponse response)
	{
		MyRequest req = new MyRequest(request);
		String mobile = req.getString("mobile");
		String appid = req.getString("appid");
		String access_token = req.getString("access_token");
		boolean userauth = "".equals(access_token);
		if(mobile.length() < 8)
		{
		}
		else
		{
			IUnit unit = UnitUtil.get(appid);
			if(unit == null)
			{
			}
			else if(!userauth && !TokenUnitUtil.checkUnitToken(appid, access_token))
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_406);
				return;
			}
			else if(SEND_MESSAGE(appid, mobile))
			{
				ResponseUtil.printJson(response, CodeUtil.CODE_001);
				return;
			}
		}
		ResponseUtil.printJson(response, CodeUtil.CODE_400);
	}
	

	/**
	 * 输入电话来发送短信
	 * @param mobile
	 * @return 发送成功 返回true,发送失败 返回false 采用默认字节utf-8 支持500字节
	 */
	public static boolean SEND_MESSAGE(String appid, String mobile)
	{
		return SEND_MESSAGE_ALIYUN(appid, mobile);
	}


	public static boolean SEND_MESSAGE_ALIYUN(String appid, String mobile)
	{
		try
		{
			int smscode = (int) ((Math.random() * 9 + 1) * 100000);
			String accessKeyId = "testId";// "testId";
			String accessSecret = "testSecret";// "testSecret";
			String signName = "";// 必填:短信签名-可在短信控制台中找到
			String templateCode = "";// 必填:短信模板-可在短信控制台中找到
			// String mobile = "";// 必填:待发送手机号
			String msgJson = "{\"code\":\"" + smscode + "\"}";// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			String outid = "dswork";// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			try
			{
				if(sendSMS(accessKeyId, accessSecret, signName, templateCode, mobile, msgJson, outid).indexOf("\"Code\":\"OK\"") != -1)
				{
					dswork.common.util.TokenSmsUtil.smscodeSet(appid + mobile, String.valueOf(smscode), dswork.common.util.TokenSmsUtil.sms_timeout);
					return true;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
		}
		return false;
	}

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
		
		// 3. 去除签名关键字Key// if(paras.containsKey("Signature")){paras.remove("Signature");}
		
		// 4. 参数KEY排序
		java.util.TreeMap<String, String> sortParas = new java.util.TreeMap<String, String>();
		sortParas.putAll(paras);
		
		// 5. 构造待签名的字符串
		java.util.Iterator<String> it = sortParas.keySet().iterator();
		StringBuilder sortQueryStringTmp = new StringBuilder();
		while(it.hasNext())
		{
			String key = it.next();
			sortQueryStringTmp.append("&").append(encodeURL(key)).append("=").append(encodeURL(paras.get(key)));
		}
		String sortedQueryString = sortQueryStringTmp.substring(1);// 去除第一个多余的&符号
		StringBuilder stringToSign = (new StringBuilder()).append("GET").append("&").append(encodeURL("/")).append("&").append(encodeURL(sortedQueryString));
		javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
		mac.init(new javax.crypto.spec.SecretKeySpec((accessSecret + "&").getBytes("UTF-8"), "HmacSHA1"));
		byte[] signData = mac.doFinal(stringToSign.toString().getBytes("UTF-8"));
		
		// 6. 签名最后也要做特殊URL编码
		String signature = encodeURL(encodeBase64(signData));
		dswork.http.HttpUtil http = new dswork.http.HttpUtil();
		return http.create("http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp).connect();
	}

	private static String encodeURL(String value) throws Exception
	{
		return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}

	@SuppressWarnings("all")
	private static String encodeBase64(byte[] str)
	{
		try
		{
			Class.forName("java.util.Base64");
			return java.util.Base64.getEncoder().encodeToString(str);
		}
		catch(ClassNotFoundException ex)
		{
		}
		return (new sun.misc.BASE64Encoder()).encode(str);
	}
}
