package common.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XmlUtil
{
	static Logger log = LoggerFactory.getLogger(XmlUtil.class.getName());
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	public static DocumentBuilder getDocumentBuilder()
	{
		DocumentBuilder db = null;
		try
		{
			db = dbf.newDocumentBuilder();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return db;
	}
	
	public static Document getDocument(String xml)
	{
		Document document = null;
		try
		{
			StringReader sr = new StringReader(xml);
			InputSource is = new InputSource(sr);
			document = getDocumentBuilder().parse(is);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return document;
	}
}
