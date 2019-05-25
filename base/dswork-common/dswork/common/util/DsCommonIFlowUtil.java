package dswork.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.common.dao.DsCommonDaoIFlow;
import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;
import dswork.common.model.IFlowInterface;
import dswork.spring.BeanFactory;

public class DsCommonIFlowUtil implements IFlowInterface
{
	private static DsCommonDaoIFlow dao = null;

	private static void init()
	{
		if(dao == null)
		{
			dao = (DsCommonDaoIFlow) BeanFactory.getBean("dsCommonDaoIFlow");
		}
	}

	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
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
	
	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
	{
		try
		{
			init();
			return dao.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
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

	public boolean process(long waitid, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		try
		{
			init();
			return dao.saveProcess(waitid, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, datatable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询当前账号的所有待办
	 * @param account 账号
	 * @return
	 */
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

	/**
	 * 根据待办ID取得待办
	 * @param waitid 待办ID
	 * @return
	 */
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

	/**
	 * 获取流程任务
	 * @param flowid 流程ID
	 * @return
	 */
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

	/**
	 * 根据流程ID获取流程
	 * @param flowid 流程ID
	 * @return
	 */
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

	/**
	 * 根据流程实例id(piid)获取流程实例
	 * @param piid 流程实例id
	 * @return
	 */
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

	/**
	 * 根据业务流水号查询所有的流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
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

	/**
	 * 根据申办流水号查询所有的流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
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

	/**
	 * 根据业务流水号获取流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
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

	/**
	 * 根据申办流水号获取流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
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

	/**
	 * 根据流程实例id(piid)查询已办数据
	 * @param piid 流程实例id
	 * @return
	 */
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

	/**
	 * 根据流程实例id(piid) 查询待办数据
	 * @param piid
	 * @return
	 */
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

	/**
	 * 根据流程实例id删除流程实例
	 * @param id
	 */
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

	/**
	 * 是否启用待办
	 * @param wid 待办ID
	 * @param datatable 表单结构
	 * @return
	 */
	public boolean saveFlow(long wid, String datatable)
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
