package dswork.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dswork.common.dao.DsCommonDaoIFlow;
import dswork.common.model.IFlow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;

@Service
public class DsCommonIFlowService
{
	@Autowired
	private DsCommonDaoIFlow dao;
	
	public String saveStart(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
	{
		try
		{
			return dao.saveStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public IFlowWaiting saveFlowStart(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable)
	{
		return dao.saveFlowStart(alias, users, ywlsh, sblsh, caccount, cname, piDay, isWorkDay, tenable);
	}
	
	public void saveStop(Long flowid, String alias, String piid)
	{
		dao.saveStop(flowid, alias, Long.parseLong(piid));
	}

	public boolean saveProcess(long waitid, String[] nextTalias, String[] nextTusers, Integer customSubusers, String paccount, String pname, String resultType, String resultMsg, String datatable)
	{
		return dao.saveProcess(waitid, nextTalias, nextTusers, customSubusers, paccount, pname, resultType, resultMsg, datatable);
	}

	public List<IFlowWaiting> queryFlowWaiting(String account)
	{
		return dao.queryFlowWaiting(account);
	}

	public IFlowWaiting getFlowWaiting(long waitid)
	{
		return dao.getFlowWaiting(waitid);
	}

	public Map<String, String> queryFlowTask(long flowid)
	{
		Map<String, String> map = new HashMap<String, String>();
		List<IFlowTask> list = dao.queryFlowTask(flowid);
		if(list != null)
		{
			for(IFlowTask m : list)
			{
				map.put(m.getTalias(), m.getTname());
			}
		}
		return map;
	}

	public IFlow getFlowById(long flowid)
	{
		return dao.getFlowById(flowid);
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		return dao.getFlowPiByPiid(piid);
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		return dao.queryFlowPi(ywlsh);
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		return dao.queryFlowPiBySblsh(sblsh);
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		return dao.getFlowPi(ywlsh);
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		return dao.getFlowPiBySblsh(sblsh);
	}

	public List<IFlowPiData> queryFlowPiData(String piid)
	{
		return dao.queryFlowPiData(Long.parseLong(piid));
	}
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid)
	{
		return dao.queryFlowWaitingByPiid(Long.parseLong(piid));
	}
	public void deleteFlowPi(String id)
	{
		dao.deleteFlowPi(Long.parseLong(id));
	}

	public boolean updateFlowWaitingTenable(long wid, String datatable)
	{
		return dao.updateFlowWaitingTenable(wid, datatable);
	}
}
