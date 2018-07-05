/**
 * DS_WEBSSO_USERService
 */
package dswork.websso.service;

import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;
import dswork.websso.model.DsCommonUser;
import dswork.websso.model.DsWebssoUser;
import dswork.websso.dao.DsCommonUserDao;
import dswork.websso.dao.DsWebssoUserDao;

@Service
public class DsWebssoUserService
{
	@Autowired
	private DsWebssoUserDao dao;
	@Autowired
	private DsCommonUserDao commonUserDao;

	public void save(DsWebssoUser po)
	{
		DsWebssoUser u = dao.getByOpenid(po);
		if(u == null)
		{
			po.setId(UniqueId.genId());
			po.setUseraccount(getAccount(po.getId()));
			dao.save(po);
		}
		else
		{
			// WEBSSO_USER和COMMON_USER通过ID相关联，如果ID相等，则认为两者是同一个用户
			if(commonUserDao.get(u.getId()) != null)
			{
				throw new RuntimeException("用户已存在");
			}
			po.setId(u.getId());
			po.setUseraccount(getAccount(po.getId()));
		}

		DsCommonUser c = new DsCommonUser();
		c.setId(po.getId());
		c.setAccount(po.getUseraccount());
		c.setName(po.getName());
		c.setCreatetime(TimeUtil.getCurrentTime());
		c.setEmail(po.getEmail());
		c.setPhone(po.getPhone());
		commonUserDao.save(c);
	}

	public void update(DsWebssoUser po)
	{
		dao.update(po);
	}

	public DsWebssoUser getByOpendid(DsWebssoUser po)
	{
		return dao.getByOpenid(po);
	}

	public DsWebssoUser getByUseraccount(String useraccount)
	{
		return dao.getByUseraccount(useraccount);
	}

	private String getAccount(long id)
	{
		String account = "u" + longToString(id);
		DsCommonUser u = commonUserDao.getByAccount(account);
		for(int i = 1; u != null; i++)
		{
			account = "u" + longToString(id + i);
			u = commonUserDao.getByAccount(account);
		}
		return account;
	}

	private String longToString(long id)
	{
		Stack<Integer> s = new Stack<Integer>();
		String str = "abcdefghijklmnopqrstuvwxyz0123456789";
		long a = id;
		while(a != 0)
		{
			Long b = a % str.length();
			a = a / str.length();
			s.push(b.intValue());
		}
		StringBuilder sb = new StringBuilder();
		while(!s.isEmpty())
		{
			sb.append(str.charAt(s.pop()));
		}
		return sb.toString();
	}
}
