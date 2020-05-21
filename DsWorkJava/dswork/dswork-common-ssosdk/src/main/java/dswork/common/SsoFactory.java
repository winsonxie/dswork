package dswork.common;

import dswork.common.service.SsoService;
import dswork.common.util.SsoDaoUtil;
import dswork.common.util.SsoServiceUtil;
import dswork.spring.BeanFactory;

public class SsoFactory
{
	private static SsoDaoUtil sDao = new SsoDaoUtil();
	private static SsoServiceUtil sService = null;

	/**
	 * 在service调用，相当于dao，本身没有事务支持。
	 * @return SsoDaoUtil，不带事务
	 */
	public static SsoDaoUtil getSsoDao()
	{
		return sDao;
	}

	/**
	 * 相当于service，本身提供事务支持。
	 * @return SsoServiceUtil，自带事务
	 */
	public static SsoServiceUtil getSsoService()
	{
		if(sService == null)
		{
			sService = new SsoServiceUtil((SsoService) BeanFactory.getBean("ssoService"));
		}
		return sService;
	}
}
