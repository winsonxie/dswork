package common.email;

import javax.mail.internet.MimeMultipart;

public class EmailTest
{
	public static void main(String[] args)
	{
		String[] arr =
		{
				"C:/Users/skey/Desktop/skey.png", "C:/Users/skey/Desktop/老年人综合服务门户企业产品发布操作手册.doc"
		};
		MimeMultipart mm = EmailUtil.createMimeMultipart("sss测试邮件测试\t<br />   邮<br />\t件测试邮件");
		for(String path : arr)
		{
			EmailUtil.addAttachment(mm, path);
		}
		EmailUtil.send("smtp.163.com", "paseweb@163.com", "yj2014", "skey_chen@163.com", "", "", "测试邮件", mm);
	}
}
