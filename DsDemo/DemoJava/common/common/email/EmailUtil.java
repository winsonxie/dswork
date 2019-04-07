package common.email;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil
{
	public static int count = 0;
	public static boolean send(String smtp, /* SMTP主机地址 */
			String from, /* 发信人 */
			String password, /* 发信人密码 */
			String to, /* 收信人 */
			String cc, /* 抄送人 */
			String bcc, /* 暗送人 */
			String subject, /* 主题 */
			MimeMultipart mm
			)
	{
		try
		{
			if(smtp == null)
			{
				smtp = dswork.core.util.EnvironmentUtil.getToString("email.smtp", "");
			}
			if(from == null)
			{
				from = dswork.core.util.EnvironmentUtil.getToString("email.account", "");
			}
			if(password == null)
			{
				password = dswork.core.util.EnvironmentUtil.getToString("email.password", "");
			}
			java.util.Properties props = new java.util.Properties();
			props.put("mail.smtp.host", smtp);
			props.put("mail.smtp.auth", "true");
			Session s = Session.getInstance(props);
			s.setDebug(false);
			MimeMessage message = new MimeMessage(s);
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setSentDate(new java.util.Date());
			
			if(mm == null)
			{
				mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放多个BodyPart对象
			}
			
			message.setContent(mm);
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			transport.connect(smtp, from, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
	
	public static MimeMultipart createMimeMultipart()
	{
		return new MimeMultipart();
	}
	
	public static MimeMultipart createMimeMultipart(String body)
	{
		MimeMultipart mm = new MimeMultipart();
		try
		{
			BodyPart bp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
			bp.setContent("<html><body><pre>" + body + "</pre></body></html>", "text/html;charset=utf-8");// 给BodyPart对象设置内容和格式/编码方式
			mm.addBodyPart(bp);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return mm;
	}
	
	public static void addBody(MimeMultipart mm, String body)
	{
		try
		{
			BodyPart bp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
			bp.setContent("<html><body><pre>" + body + "</pre></body></html>", "text/html;charset=utf-8");// 给BodyPart对象设置内容和格式/编码方式
			mm.addBodyPart(bp);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	public static String addAttachment(MimeMultipart mm, String path)
	{
		if(count == Integer.MAX_VALUE)
		{
			count = 0;
		}
		int cid = count++;
        try
		{
    		java.io.File file = new java.io.File(path);
    		MimeBodyPart mbp = new MimeBodyPart();
    		mbp.setDisposition(MimeBodyPart.INLINE);
    		mbp.setContent(new MimeMultipart("mixed"));
    		mbp.setHeader("Content-ID", "<" + cid + ">");
			mbp.setDataHandler(new DataHandler(new FileDataSource(file)));
	        mbp.setFileName(new String(file.getName().getBytes("GBK"), "ISO-8859-1"));
	        mm.addBodyPart(mbp);
	        return String.valueOf(cid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        return "";
	}
}
