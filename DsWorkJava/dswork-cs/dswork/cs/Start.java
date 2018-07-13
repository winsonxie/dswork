package dswork.cs;

public class Start extends MyStart
{
	public static void main(String[] args)
	{
		MyStart.myLog4jLoad();
		MyStart.mySpringLoad();
		// 执行需要的应用程序
		ExecuteProject.execute(args);
		MyStart.waitForQuit();
	}
}
