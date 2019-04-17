public class Tomcat
{
	public static void main(String[] args) throws Exception
	{// .setContextDir("context.xml")
		dswork.ee.MyTomcat.class.newInstance().setPort(8080).setBaseDir("/WorkServer/TomcatEmbed").addWebapp().addWebapp("web", "E:\\WorkServer\\web").start();
	}
}
