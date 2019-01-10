package dswork.common;

import java.util.ArrayList;

import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowWaiting;

/**
 * 处理方法
 */
public abstract class IFlowHandle
{
	/**
	 * 执行之前
	 * @param wait
	 */
	public void exeBefore(IFlowPi pi, IFlowWaiting wait){}
	
	/**
	 * 执行之后
	 * @param wait
	 */
	public void exeAfter(IFlowPi pi, ArrayList<IFlowWaiting> wlist, IFlowPiData data){}

}
