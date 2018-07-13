/**
 * CMSService
 */
package common.cms;

public class CmsFactoryPreview extends CmsFactory
{
	public CmsFactoryPreview(Long siteid)
	{
		super(siteid);
	}

	@Override
	protected DsCmsDao getDao()
	{
		if(dao == null)
		{
			dao = (DsCmsDao) dswork.spring.BeanFactory.getBean("dsCmsPreviewDao");
		}
		return dao;
	}
}
