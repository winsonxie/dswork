package dswork.base;

import dswork.base.util.DsBaseIFlowUtil;

/**
 * 在service调用，相当于dao，本身没有事务支持。
 */
public class DsFactoryUtil
{
	private static DsBaseIFlowUtil d = new DsBaseIFlowUtil();
	public static DsBaseIFlowUtil getFlow()
	{
		return d;
	}
}
