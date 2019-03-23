package dswork.base;

import dswork.base.service.DsBaseIFlowService;
import dswork.base.util.DsBaseUtil;
import dswork.base.util.DsBaseIFlowServiceUtil;
import dswork.spring.BeanFactory;

/**
 * 在控制器调用，相当于service，本身提供事务支持。
 */
public class DsFactory
{
	public static DsBaseFactoryDict getDict()
	{
		return DsBaseFactoryDict.getInstance();
	}
	public static DsBaseFactoryOrg getOrg()
	{
		return DsBaseFactoryOrg.getInstance();
	}
	
	private static DsBaseIFlowServiceUtil factory = null;
	
	private static final DsBaseUtil util = new DsBaseUtil();
	
	/**
	 * 相当于service，本身提供事务支持。
	 */
	public static DsBaseIFlowServiceUtil getFlow()
	{
		if(factory == null)
		{
			factory = new DsBaseIFlowServiceUtil((DsBaseIFlowService) BeanFactory.getBean("DsBaseIFlowService"));
		}
		return factory;
	}
	
	public static DsBaseUtil getUtil()
	{
		return util;
	}
}
