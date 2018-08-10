public class Tomcat
{
	public static void main(String[] args) throws Exception
	{
		dswork.ee.MyTomcat.class.newInstance().setPort(1234).addWebapp().start();
	}
}
