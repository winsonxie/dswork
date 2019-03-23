package dswork.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.base.dao.DsBaseDaoIFlow;
import dswork.base.model.IFlow;
import dswork.base.model.IFlowPi;
import dswork.base.model.IFlowPiData;
import dswork.base.model.IFlowTask;
import dswork.base.model.IFlowWaiting;
import dswork.spring.BeanFactory;

public class DsBaseIFlowUtil
{
	private static DsBaseDaoIFlow dao = null;

	private static void init()
	{
		if(dao == null)
		{
			dao = (DsBaseDaoIFlow) BeanFactory.getBean("DsBaseDaoIFlow");
		}
	}

	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例ID
	 */
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			init();
			return dao.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例ID
	 */
	public String start(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return start(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}
	
	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @param enable 是否启用待办（-1不启用，0启用）
	 * @return 流程实例ID
	 */
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, int tenable)
	{
		try
		{
			init();
			return dao.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @param enable 是否启用待办（-1不启用，0启用）
	 * @return 流程实例ID
	 */
	public String start(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, int tenable)
	{
		return start(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
	}

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		try
		{
			init();
			return dao.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param ywlsh 业务流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay)
	{
		return startFlow(alias, null, ywlsh, sblsh, caccount, cname, piDay, isWorkDay);
	}
	
	public void stop(Long flowid, String alias, String piid)
	{
		try
		{
			init();
			dao.saveStop(flowid, alias, Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param nextTusers 下级任务处理人列表，如果为null，则使用流程配置中的处理人信息
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @return true|false
	 */
	public boolean process(long waitid, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String formdata)
	{
		try
		{
			init();
			return dao.saveProcess(waitid, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, formdata);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param nextTusers 下级任务处理人列表，如果为null，则使用流程配置中的处理人信息
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @return true|false
	 */
	public boolean process(long waitid, String[] nextTalias, String paccount, String pname, String resultType, String resultMsg, String formdata)
	{
		return process(waitid, nextTalias, null, paccount, pname, resultType, resultMsg, formdata);
	}

	public List<IFlowWaiting> queryWaiting(String account)
	{
		try
		{
			init();
			return dao.queryFlowWaiting(account);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean takeWaiting(long waitid, String user)
	{
		try
		{
			if(user != null && user.trim().length() > 0)
			{
				init();
				dao.updateFlowWaitingUser(waitid, user);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public IFlowWaiting getWaiting(long waitid)
	{
		try
		{
			init();
			return dao.getFlowWaiting(waitid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getTaskList(long flowid)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			init();
			List<IFlowTask> list = dao.queryFlowTask(flowid);
			if(list != null)
			{
				for(IFlowTask m : list)
				{
					map.put(m.getTalias(), m.getTname());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public IFlow getFlowById(long flowid)
	{
		try
		{
			init();
			return dao.getFlowById(flowid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		try
		{
			init();
			return dao.getFlowPiByPiid(piid);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		try
		{
			init();
			return dao.queryFlowPi(ywlsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		try
		{
			init();
			return dao.queryFlowPiBySblsh(sblsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		try
		{
			init();
			return dao.getFlowPi(ywlsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		try
		{
			init();
			return dao.getFlowPiBySblsh(sblsh);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		try
		{
			init();
			return dao.queryFlowPiData(Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		try
		{
			init();
			return dao.queryFlowWaitingByPiid(Long.parseLong(piid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void deleteFlowPi(String id)
	{
		try
		{
			init();
			dao.deleteFlowPi(Long.parseLong(id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean updateFlowTuser(Long wid, String olduser, String oldname,String newuser, String newname)
	{
		try
		{
			init();
			return dao.updateFlowTuser(wid, olduser, oldname, newuser, newname);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean updateFlowTuser(Long wid, String tuser, int subcount)
	{
		try
		{
			init();
			return dao.updateFlowTuser(wid, tuser, subcount);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateFlowTusers(Long wid, String tusers)
	{
		try
		{
			init();
			return dao.updateFlowTusers(wid, tusers);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean saveFlow(Long wid, String datatable)
	{
		try
		{
			init();
			return dao.updateFlowWaitingTenable(wid, datatable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
