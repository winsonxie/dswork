/**
 * DS_WEBSSO_USERService
 */
package dswork.websso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.websso.model.DsCommonUser;
import dswork.websso.dao.DsCommonUserDao;

@Service
public class DsCommonUserService
{
	@Autowired
	private DsCommonUserDao dao;

	public int save(DsCommonUser po)
	{
		return dao.save(po);
	}

	public DsCommonUser getByAccount(String account)
	{
		return dao.getByAccount(account);
	}

	public DsCommonUser getByIdcard(String idcard)
	{
		return dao.getByIdcard(idcard);
	}

	public DsCommonUser getByMobile(String mobile)
	{
		return dao.getByMobile(mobile);
	}

	public DsCommonUser getByEmail(String email)
	{
		return dao.getByEmail(email);
	}
}
