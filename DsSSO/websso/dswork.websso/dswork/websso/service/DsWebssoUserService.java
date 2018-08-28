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
		DsWebssoUser u = dao.getByOpenid(po);// 一定是跟sso用户绑定的
		if(u == null)
		{
			po.setId(UniqueId.genId());
			po.setUseraccount(getAccount(po.getId()));// 全小写
			po.setSsoaccount(po.getUseraccount());
			dao.save(po);
			
			DsCommonUser c = new DsCommonUser();
			c.setId(po.getId());
			c.setAccount(po.getSsoaccount());
			c.setName(po.getName());
			// c.setIdcard(idcard);
			//c.setCakey(cakey);
			//c.setWorkcard(workcard);
			c.setEmail(po.getEmail());
			c.setMobile(po.getMobile());
			c.setPhone(po.getPhone());
			c.setStatus(po.getStatus());
			c.setCreatetime(TimeUtil.getCurrentTime());
			
			//c.setOrgpid(orgpid);
			//c.setOrgid(orgid);
			//c.setOrgpname(orgpname);
			//c.setOrgname(orgname);
			
			c.setType(po.getType());
			c.setTypename(po.getTypename());
			c.setExalias(po.getExalias());
			c.setExname(po.getExname());
			
			commonUserDao.save(c);
		}
		else
		{
			throw new RuntimeException("该"+(po.getOpenidqq().length() > 0 ? "QQ" : (po.getOpenidwechat().length() > 0 ? "微信" : (po.getOpenidalipay().length() > 0 ? "支付宝" : "")))+"已注册用户");
		}
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
