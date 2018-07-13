import dswork.ee.MyTomcat;

public class Tomcat
{
	public static void main(String[] args) throws Exception
	{
		MyTomcat.class.newInstance().setPort(8080).setContextDir("context.xml").setBaseDir("/WorkServer/TomcatEmbed").addWebapp().start();
	}
}
