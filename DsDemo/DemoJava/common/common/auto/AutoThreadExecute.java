package common.auto;

//import java.util.ArrayList;
//import java.util.Calendar;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

//import dswork.core.util.TimeUtil;
//import dswork.spring.BeanFactory;

public class AutoThreadExecute extends Thread
{
	// ################################################################################################
	// 线程任务相关
	// ################################################################################################
	private static int count = 0;
	private static synchronized int getCount() {return AutoThreadExecute.count;}
	private static synchronized void setCount(int count) {AutoThreadExecute.count = count;}// count仅用于标记是否启动任务，1为启动，0为不启动

	/**
	 * 启动线程，该线程仅负责业务的运行
	 */
	public void run()
	{
		final long index = System.currentTimeMillis();
		System.out.println("********AutomaticExecute线程启动********");
		System.out.println("标识号：" + index);
		if(AutoThreadExecute.getCount() != 0)//上次的任务还没有结束呢，不需要重复执行 了
		{
			System.out.println("********AutomaticExecute线程结束 ，因上次线程任务还在运行，本次不执行********");
			System.out.println("标识号：" + index);
			return;
		}
		AutoThreadExecute.setCount(1);//标记任务启动
		
		try
		{
			// 可以让任务启动后，等待一段时间再运行
			sleep(1000 * 60 * 60 * 1L);// 进来先休息一个小时
		}
		catch(Exception e)
		{
		}
		
		//boolean retry = false;// 用于判断是否有定时发送，但未到时间
		boolean goclose = false;
		while(!goclose)
		{
			System.out.println("--AutomaticExecute循环任务启动。--");
			System.out.println("标识号：" + index);
			try
			{
				
				//long now = Calendar.getInstance().getTimeInMillis();//取得当前时间
				////DxService dxService = (DxService)BeanFactory.getBean("dxService");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userstatus", "0");
				map.put("status", "0");// 待发送
				////List<Dx> list = dxService.queryList(map);
				////for(Dx m : list)
				////{
					//long send = TimeUtil.convertString(m.getFssj(), "yyyy-MM-dd HH:mm:ss").getTime();
					//if(send > now)// 未到时间发送
					//{
					//	retry = true;// 有未到时间暂时不发送的信息
					//}
					//else
					//{
						////dxService.updateStatus(m.getId(), 1);// 发送成功，更新状态
					//}
				////}
				//if(!retry)// 没有需要发送的
				//{
					// 本来可以直接退出的了，但这业务数量大，就再判断一次吧，说不定又有数据了呢
					map.clear();
					map.put("userstatus", "0");
					map.put("status", "0");// 待发送
					////list = dxService.queryList(map);
					////if(list == null || list.size() == 0)
					////{
						AutoThreadExecute.setCount(0);// 退出
						System.out.println("--AutomaticExecute定时任务结束，已无待发信息。--");
						System.out.println("标识号：" + index);
						
						
						goclose = true;// 可以退出了，线程可以结束了
						sleep(1000*60*60*1L);// 如果当定时任务来用，就去掉goclose=true;当线程睡一会继续执行就可以当定时器用了
						
						
						
						
					////}
					//else{}//你看，就在这么一会，就真的又出现了新的待发信息，只能先不退出了，重新进循环执行
				//}
				//else
				//{
				//	System.out.println("--还有待发信息，重新进循环执行--");
				//}
			}
			catch(Exception ex)
			{
				AutoThreadExecute.setCount(0);// 退出
				System.out.println("********AutomaticExecute线程异常结束 ********");
				System.out.println("标识号：" + index);
				ex.printStackTrace();
			}
		}
	}
	/*
	 * 调用toStart(),启动定时清理程序
	 */
	public static final void toStart()
	{
		AutoThreadExecute pj = new AutoThreadExecute();
		pj.start();//启动程序
	}
}
