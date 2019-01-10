package dswork.common;

import dswork.common.model.IFlowParam;

/**
 * 处理方法
 */
public abstract class IFlowHandle
{
	/**
	 * 执行之前
	 * @param param
	 */
	public void exeBefore(IFlowParam param){}
	
	/**
	 * 执行之后
	 * @param param
	 */
	public void exeAfter(IFlowParam param){}

}
