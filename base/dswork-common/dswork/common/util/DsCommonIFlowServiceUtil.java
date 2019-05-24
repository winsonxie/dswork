package dswork.common.util;

import java.util.List;
import java.util.Map;

import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowWaiting;
import dswork.common.model.IFlowInterface;
import dswork.common.service.DsCommonIFlowService;

public class DsCommonIFlowServiceUtil implements IFlowInterface
{
	private DsCommonIFlowService service;

	public DsCommonIFlowServiceUtil(DsCommonIFlowService service)
	{
		this.service = service;
	}
	
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
	{
		try
		{
			return service.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
	{
		return service.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
	}

	public void stop(Long flowid, String alias, String piid)
	{
		service.saveStop(flowid, alias, piid);
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
	public boolean process(long waitid, String replaceUser, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return service.saveProcess(waitid, replaceUser, nextTalias, nextTusers, paccount, pname, resultType, resultMsg, datatable);
	}

	/**
	 * 查询当前账号的所有待办
	 * @param account 账号
	 * @return
	 */
	public List<IFlowWaiting> queryWaiting(String account)
	{
		return service.queryFlowWaiting(account);
	}

	/**
	 * 取得待办任务
	 * @param waitid 需要取得的待办ID
	 * @param user 取得待办的用户
	 * @return
	 */
	public boolean takeWaiting(long waitid, String user)
	{
		if(user != null && user.trim().length() > 0)
		{
			service.updateFlowWaitingUser(waitid, user);
			return true;
		}
		return false;
	}

	/**
	 * 根据待办ID取得待办
	 * @param waitid 待办ID
	 * @return
	 */
	public IFlowWaiting getWaiting(long waitid)
	{
		return service.getFlowWaiting(waitid);
	}

	/**
	 * 获取流程任务
	 * @param flowid 流程ID
	 * @return
	 */
	public Map<String, String> getTaskList(long flowid)
	{
		return service.queryFlowTask(flowid);
	}

	/**
	 * 根据流程ID获取流程
	 * @param flowid 流程ID
	 * @return
	 */
	public IFlow getFlowById(long flowid)
	{
		return service.getFlowById(flowid);
	}

	/**
	 * 根据流程实例id(piid)获取流程实例
	 * @param piid 流程实例id
	 * @return
	 */
	public IFlowPi getFlowPiByPiid(String piid)
	{
		return service.getFlowPiByPiid(piid);
	}

	/**
	 * 根据业务流水号查询所有的流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		return service.queryFlowPi(ywlsh);
	}

	/**
	 * 根据申办流水号查询所有的流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		return service.queryFlowPiBySblsh(sblsh);
	}

	/**
	 * 根据业务流水号获取流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
	public IFlowPi getFlowPi(String ywlsh)
	{
		return service.getFlowPi(ywlsh);
	}

	/**
	 * 根据申办流水号获取流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		return service.getFlowPiBySblsh(sblsh);
	}

	/**
	 * 根据流程实例id(piid)查询已办数据
	 * @param piid 流程实例id
	 * @return
	 */
	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		return service.queryFlowPiData(piid);
	}

	/**
	 * 根据流程实例id(piid) 查询待办数据
	 * @param piid
	 * @return
	 */
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		return service.queryFlowWaitingByPiid(piid);
	}

	/**
	 * 根据流程实例id删除流程实例
	 * @param id
	 */
	public void deleteFlowPi(String id)
	{
		service.deleteFlowPi(id);
	}

	/**
	 * 是否启用待办
	 * @param wid 待办ID
	 * @param datatable 表单结构
	 * @return
	 */
	public boolean saveFlow(Long wid, String datatable)
	{
		return service.updateFlowWaitingTenable(wid, datatable);
	}
}
