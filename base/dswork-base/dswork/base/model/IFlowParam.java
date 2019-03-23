/**
 * 字典项Model
 */
package dswork.base.model;

import java.util.List;

public class IFlowParam
{
	private long flowid;
	private long piid;
	private String alias;
	private boolean start = false;
	private boolean end = false;
	private IFlowWaiting process;// 当前这一步
	private List<IFlowPiData> pidataList;// 当前已完成
	private List<IFlowWaiting> waitingList;// 下一步

	public long getFlowid()
	{
		return flowid;
	}

	public void setFlowid(long flowid)
	{
		this.flowid = flowid;
	}

	public long getPiid()
	{
		return piid;
	}

	public void setPiid(long piid)
	{
		this.piid = piid;
	}

	public boolean isStart()
	{
		return start;
	}

	public void setStart(boolean start)
	{
		this.start = start;
	}

	public boolean isEnd()
	{
		return end;
	}

	public void setEnd(boolean end)
	{
		this.end = end;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public IFlowWaiting getProcess()
	{
		return process;
	}

	public void setProcess(IFlowWaiting process)
	{
		this.process = process;
	}

	public List<IFlowPiData> getPidataList()
	{
		return pidataList;
	}

	public void setPidataList(List<IFlowPiData> pidataList)
	{
		this.pidataList = pidataList;
	}

	public List<IFlowWaiting> getWaitingList()
	{
		return waitingList;
	}

	public void setWaitingList(List<IFlowWaiting> waitingList)
	{
		this.waitingList = waitingList;
	}
}
