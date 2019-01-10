package dswork.common;

import java.util.ArrayList;

import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowWaiting;

/**
 * 处理方法接口
 */
public abstract class IFlowHandle
{
	/**
	 * 流程启动之前
	 * @param flow
	 */
	public void startFlowBefore(IFlow flow){}

	/**
	 * 流程启动之后
	 * @param pi
	 * @param wait
	 */
	public void startFlowAfter(IFlowPi pi, IFlowWaiting wait){}

	/**
	 * 流程处理之前
	 * @param wait
	 */
	public void processFlowBefore(IFlowWaiting wait){}

	/**
	 * 流程节点处理之后
	 * @param owait
	 * @param nwait
	 */
	public void processFlowPointAfter(IFlowWaiting owait, IFlowWaiting nwait){}
	
	/**
	 * 流程处理之后
	 * @param wait
	 */
	public void processFlowAfter(IFlowPi pi, ArrayList<IFlowWaiting> wlist){}

	/**
	 * 流程结束之前
	 * @param pi
	 */
	public void endFlowBefore(IFlowPi pi){}

	/**
	 * 流程结束之后
	 * @param pi
	 * @param data
	 */
	public void endFlowAfter(IFlowPi pi, IFlowPiData data){}

}
