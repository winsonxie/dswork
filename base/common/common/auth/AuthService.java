/**
 * AuthService
 */
package common.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("authService")
public class AuthService
{
	@Autowired
	private AuthDao dao;

	public Auth getUserByAccount(String account)
	{
		return dao.getUserByAccount(account);
	}

	public int updateUserPassword(String account, String password)
	{
		return dao.updateUserPassword(account, password);
	}

	public Auth getEpByAccount(String account)
	{
		return dao.getEpByAccount(account);
	}

	public List<Auth> queryEpList(String account, String email)
	{
		account = (account == null || account.length() == 0) ? null : account;
		email = (email == null || email.length() == 0) ? null : email;
		if(account == null && email == null)
		{
			return new ArrayList<Auth>();
		}
		return dao.queryEpList(account, email);
	}

	public int updateEpPassword(String account, String password)
	{
		return dao.updateEpPassword(account, password);
	}

	public Auth getPersonByAccount(String key)
	{
		if(key == null || key.trim().length() == 0)
		{
			return null;
		}
		key = key.trim();
		String account = null, idcard = null;
		try
		{
			int i = Integer.parseInt(key.charAt(0) + "");
			if(i > 0 && key.length() == 18)
			{
				idcard = key;
			}
			else
			{
				account = key;
			}
		}
		catch(Exception e)
		{
			account = key;
		}
		return dao.getPersonByAccount(account, idcard);
	}

	public List<Auth> queryPersonList(String account, String email)
	{
		account = (account == null || account.length() == 0) ? null : account;
		email = (email == null || email.length() == 0) ? null : email;
		if(account == null && email == null)
		{
			return new ArrayList<Auth>();
		}
		String idcard = null;
		if(account != null && account.length() == 18)
		{
			try
			{
				int i = Integer.parseInt(account.charAt(0) + "");
				if(i > 0 )
				{
					idcard = account;
					account = null;
				}
			}
			catch(Exception e)
			{
			}
		}
		return dao.queryPersonList(account, idcard, email);
	}

	public int updatePersonPassword(String account, String password)
	{
		return dao.updatePersonPassword(account, password);
	}
}
