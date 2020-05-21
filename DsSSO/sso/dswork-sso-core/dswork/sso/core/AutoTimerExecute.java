package dswork.sso.core;

import java.util.Timer;
import java.util.TimerTask;

import dswork.common.util.UnitUtil;

//import dswork.core.util.TimeUtil;
//import dswork.spring.BeanFactory;
public class AutoTimerExecute extends Thread
{
	// ################################################################################################
	// 定时任务相关
	// ################################################################################################
	// boolean retry = false;// 用于判断是否有定时发送，但未到时间
	private Timer _timer = null;

	/**
	 * 启动线程，该线程仅负责定时器的运行
	 */
	public void run()
	{
		try
		{
			TimerTask _timerTask = new TimerTask()
			{
				public void run()
				{
					try
					{
						UnitUtil.refresh();
					}
					catch(Exception ex)
					{
					}
					try
					{
						Class.forName("dswork.sso.util.websso.WebssoUtil");
						dswork.sso.util.websso.WebssoUtil.refresh();
					}
					catch(ClassNotFoundException ex)
					{
					}
					catch(Exception ex)
					{
					}
				}
			};
			_timer = new Timer(true);
			// Timer.schedule(TimerTask task, Date date, long period)// 从date开始,每period毫秒执行task.
			_timer.schedule(_timerTask, 1000, 7200000);// 从服务器启动开始运行,每period毫秒执行
		}
		catch(Exception ex)
		{
		}
	}

	/*
	 * 调用toStart(),启动定时清理程序
	 */
	public static final void toStart()
	{
		AutoTimerExecute pj = new AutoTimerExecute();
		pj.start();// 启动程序
	}
}
