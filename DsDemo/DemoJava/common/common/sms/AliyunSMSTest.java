package common.sms;

public class AliyunSMSTest
{
	public static void main(String[] args)
	{
		String accessKeyId = "testId";// "testId";
		String accessSecret = "testSecret";// "testSecret";
		String signName = "智慧旅游系统";// 必填:短信签名-可在短信控制台中找到
		String templateCode = "SMS_109480154";// 必填:短信模板-可在短信控制台中找到
		String mobile = "18565428672";// 必填:待发送手机号
		String msgJson = "{\"name\":\"" + "开开心心" + "\", \"type\":\"" + "1" + "\", \"time\":\"" + "2018-05-15" + "\"}";// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		String outid = "dswork";// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		try
		{
			if(sendSMS(accessKeyId, accessSecret, signName, templateCode, mobile, msgJson, outid))
			{
				System.out.println("发送成功");
			}
			else
			{
				System.out.println("发送失败");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean sendSMS(String accessKeyId, String accessSecret, String signName, String templateCode, String mobile, String msgJson, String outid) throws Exception
	{
		String json = AliyunSMS.sendSMS(accessKeyId, accessSecret, signName, templateCode, mobile, msgJson, outid);
		System.out.println(json);
		if(json.indexOf("\"Code\":\"OK\"") != -1)
		{
			return true;
		}
		return false;
	}
}
