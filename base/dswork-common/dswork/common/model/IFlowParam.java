/**
 * 字典项Model
 */
package dswork.common.model;

import java.util.List;

public class IFlowParam
{
	private boolean start = false;
	private boolean end = false;
	private IFlowPi pi;// 当前这一步
	private IFlowWaiting process;// 当前这一步
	private List<IFlowPiData> pidataList;// 当前已完成
	private List<IFlowWaiting> waitingList;// 下一步

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

	public IFlowPi getPi()
	{
		return pi;
	}

	public void setPi(IFlowPi pi)
	{
		this.pi = pi;
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
