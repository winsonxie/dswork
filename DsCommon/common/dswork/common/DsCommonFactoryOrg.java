package dswork.common;

import dswork.common.dao.DsCommonDao;
import dswork.spring.BeanFactory;

public class DsCommonFactoryOrg
{
	private static DsCommonDao dao;
	private static void init()
	{
		if(dao == null)
		{
			dao = (DsCommonDao) BeanFactory.getBean("dsCommonDao");
		}
	}
	
	/**
	 * 根据上级组织机构主键取得列表数据
	 * @param pid 上级组织机构主键
	 * @param status 0-2为指定分类（2单位，1部门，0岗位），超出0-2范围则不过滤
	 * @return String
	 */
	public String getOrgJson(Long pid, Integer status)
	{
		init();
		return dao.queryListOrg(pid, status).toString();
	}
}
