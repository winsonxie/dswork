/**
 * 公共Dao
 */
package dswork.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dswork.common.model.IFlow;
import dswork.common.model.IFlowDataRow;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;
import dswork.core.db.MyBatisDao;
import dswork.core.util.TimeUtil;
import dswork.core.util.UniqueId;

@Repository
@SuppressWarnings("all")
public class DsCommonDaoIFlow extends MyBatisDao
{
	com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String dtSet = "";

	public String toJson(Object object)
	{
		return builder.create().toJson(object);
	}

	public <T> T toBean(String json, Class<T> classOfT)
	{
		return builder.create().fromJson(json, classOfT);
	}
	
	// 此处这样写法是为了让流程的管理可成独立项目运行，不在同一数据库中
	// #############################################################
	@Autowired
	private DsCommonDaoCommonIFlow daoCommon;

	private IFlowTask getFlowTask(Long flowid, String talias)
	{
		return daoCommon.getFlowTask(flowid, talias);
	}

	private IFlow getFlow(String alias)
	{
		return daoCommon.getFlow(alias);
	}
	
	public IFlow getFlowById(long id)
	{
		return daoCommon.getFlowById(id);
	}

	public List<IFlowTask> queryFlowTask(Long flowid)
	{
		return daoCommon.queryFlowTask(flowid);
	}
	// #############################################################

	@Override
	protected Class getEntityClass()
	{
		return DsCommonDaoIFlow.class;
	}

	private void updateFlowPi(Long id, int status, String pialias, String datatable)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("status", status);
		map.put("pialias", pialias);
		map.put("datatable", datatable);
		if(status == 0)
		{
			map.put("piend", TimeUtil.getCurrentTime());
		}
		executeUpdate("updateFlowPi", map);
	}
	
	private void saveFlowWaiting(IFlowWaiting m)
	{
		executeInsert("insertFlowWaiting", m);
	}
	
	public void deleteFlowPi(Long id)
	{
		executeDelete("deleteFlowPi", id);
	}

	private void deleteFlowWaiting(Long id)
	{
		executeDelete("deleteFlowWaiting", id);
	}

	private void deleteFlowWaitingByPiid(Long piid)
	{
		executeDelete("deleteFlowWaitingByPiid", piid);
	}

	private void updateFlowWaiting(Long id, String tstart, String tprev)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("tstart", tstart);
		map.put("tprev", tprev);
		executeUpdate("updateFlowWaiting", map);
	}

	private IFlowWaiting getFlowWaitingByPiid(Long piid, String talias)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("piid", piid);
		map.put("talias", talias);
		return (IFlowWaiting) executeSelect("selectFlowWaitingByPiid", map);
	}

	public List<IFlowWaiting> queryFlowWaitingByPiid(Long piid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("piid", piid);
		return executeSelectList("selectFlowWaitingByPiid", map);
	}

	private List<String> queryFlowWaitingTalias(Long piid)
	{
		return executeSelectList("queryFlowWaitingTalias", piid);
	}

	public IFlowPi getFlowPiByPiid(String piid)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("piid", piid);
		return (IFlowPi) executeSelect("queryFlowPiByPiid", map);
	}

	public IFlowPi getFlowPi(String ywlsh)
	{
		List<IFlowPi> list = executeSelectList("queryFlowPi", ywlsh);
		if(list == null || list.size() == 0)
		{
			return null;
		}
		return list.get(0);
	}

	public IFlowPi getFlowPiBySblsh(String sblsh)
	{
		List<IFlowPi> list = executeSelectList("queryFlowPiBySblsh", sblsh);
		if(list == null || list.size() == 0)
		{
			return null;
		}
		return list.get(0);
	}

	public List<IFlowPi> queryFlowPi(String ywlsh)
	{
		return executeSelectList("queryFlowPi", ywlsh);
	}

	public List<IFlowPi> queryFlowPiBySblsh(String sblsh)
	{
		return executeSelectList("queryFlowPiBySblsh", sblsh);
	}

	public List<IFlowPiData> queryFlowPiData(Long piid)
	{
		return executeSelectList("queryFlowPiData", piid);
	}

	public void updateFlowWaitingUser(Long id, String tuser)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("tuser", "," + tuser + ",");
		executeUpdate("updateFlowWaitingUser", map);
	}
	
	public int saveSubFlow(IFlowWaiting subflow)
	{
		return executeUpdate("insertFlowWaiting", subflow);
	}

	public IFlowWaiting getFlowWaiting(Long id)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return (IFlowWaiting) executeSelect("selectFlowWaiting", map);
	}
	
	public List<IFlowWaiting> queryFlowWaiting(String account)
	{
		String tuser = account;
		Map<String, String> map = new HashMap<String, String>();
		map.put("account", "," + account + ",");
		map.put("tuser", "|," + tuser + ",");
		return executeSelectList("queryFlowWaiting", map);
	}

	public IFlowWaiting saveFlowStart(String alias, String users, String ywlsh, String sblsh, String account, String name, int piDay, boolean isWorkDay)
	{
		String time = TimeUtil.getCurrentTime();
		IFlow flow = this.getFlow(alias);
		long flowid = 0L;
		if(flow != null)
		{
			flowid = flow.getId();// deployidToFlowid(flow.getDeployid());//flow.getId();
		}
		if(flowid > 0)
		{
			IFlowTask task = this.getFlowTask(flowid, "start");
			IFlowPi pi = new IFlowPi();
			pi.setId(UniqueId.genUniqueId());
			pi.setYwlsh(ywlsh);
			pi.setSblsh(sblsh);
			pi.setAlias(alias);
			pi.setFlowid(flowid);
			pi.setDeployid(flow.getDeployid());
			pi.setPiday(piDay);
			pi.setPidaytype(isWorkDay ? 1 : 0);
			pi.setPistart(time);
			pi.setPiend("");
			pi.setStatus(1);// 流程状态(1,申请,2,运行,3挂起,0结束)
			pi.setCaccount(account);
			pi.setCname(name);
			pi.setPialias("start");
			pi.setDatatable(task.getDatatable());
			executeInsert("insertFlowPi", pi);
			Long piid = pi.getId();
			IFlowWaiting m = new IFlowWaiting();
			m.setId(UniqueId.genUniqueId());
			m.setPiid(piid);
			m.setYwlsh(ywlsh);
			m.setSblsh(sblsh);
			m.setFlowid(flowid);
			m.setFlowname(flow.getName());
			m.setTprev("");// 没有上级节点
			m.setTalias(task.getTalias());// "start"
			m.setTname(task.getTname());
			m.setTcount(1);// task.getTcount()，start没有上级节点，不需要等待
			m.setTnext(task.getTnext());
			m.setTstart(time);
			m.setTmemo(task.getTmemo());
			m.setSubcount(task.getSubcount());
			if(task.getSubcount() > -1 && "".equals(task.getSubusers()))
			{
				m.setSubusers(",,");
			}
			else
			{
				m.setSubusers(task.getSubusers());
			}
			m.setDatatable(task.getDatatable());
			if(users != null)
			{
				if(users.split(",", -1).length > 1)
				{
					m.setTusers("," + users + ",");// 多人，候选人
					m.setTuser("");
				}
				else
				{
					m.setTusers("");// 候选人
					m.setTuser("," + users + ",");// 单人
				}
			}
			else
			{
				if(task.getTusers().split(",", -1).length > 1)
				{
					m.setTusers("," + task.getTusers() + ",");// 多人，候选人
					m.setTuser("");
				}
				else
				{
					m.setTusers("");// 候选人
					m.setTuser("," + task.getTusers() + ",");// 单人
				}
			}
			this.saveFlowWaiting(m);
			return m;
		}
		return null;
	}

	public String saveStart(String alias, String users, String ywlsh, String sblsh, String account, String name, int piDay, boolean isWorkDay)
	{
		IFlowWaiting w = saveFlowStart(alias, users, ywlsh, sblsh, account, name, piDay, isWorkDay);
		if(w != null)
		{
			return String.valueOf(w.getPiid());
		}
		return "";
	}

	public void saveStop(Long piid)
	{
		this.deleteFlowWaitingByPiid(piid);
		this.updateFlowPi(piid, 0, "", dtSet);// 结束
	}

	public boolean saveProcess(Long waitid, String[] nextTalias, String[] nextTusers, String account, String name, String resultType, String resultMsg, String datatable)
	{
		IFlowWaiting m = this.getFlowWaiting(waitid);
		if(m != null && m.getTcount() <= 1)
		{
			String time = TimeUtil.getCurrentTime();
			IFlowPiData pd = new IFlowPiData();
			pd.setId(UniqueId.genUniqueId());
			pd.setPiid(m.getPiid());
			pd.setTprev(m.getTprev());
			pd.setTalias(m.getTalias());
			pd.setTname(m.getTname());
			pd.setStatus(0);// 状态(0已处理,1代办,2挂起,3取消挂起)
			pd.setPaccount(account);
			pd.setPname(name);
			pd.setPtime(time);
			pd.setPtype(resultType);
			pd.setMemo(resultMsg);
			pd.setDatatable(datatable);
			executeInsert("insertFlowPiData", pd);
			boolean isEnd = false;
			if(m.getSubcount() > -1)//会签任务
			{
				String subusers = m.getSubusers();
				subusers += "".equals(subusers) ? "," + account + "," : account + ",";
				if(m.getSubcount() == 0)//会签个数为0时,subcount不需要继续减
				{
					dtSet = updateDataTable(m.getDatatable(), datatable, true);
					this.updateSubFlowWaitingSubusers(m.getId(), subusers, dtSet);//更新subusers,datatable
					String cuser = "|," + account + ",";
					if(m.getTuser().indexOf(cuser) > 0)
					{
						isEnd = exeProcess(waitid, nextTalias, nextTusers, datatable, m, time, isEnd);
					}
				}
				else
				{
					dtSet = updateDataTable(m.getDatatable(), datatable, true);
					this.updateSubFlowWaiting(m.getId(), subusers, dtSet);//更新subusers,subcount,datatable
				}
				if(m.getSubcount() == 1)
				{
					//最后一个会签个数
					IFlowTask t = this.getFlowTask(m.getFlowid(), m.getTalias());
					if(!"".equals(t.getTusers()))//是否有用户来控制会签的结束
					{
						String tuser = m.getTuser() + "|," + t.getTusers() + ",";//tuser |后的用户是用来控制会签环节结束的用户
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", m.getId());
						map.put("tuser", tuser);
						executeUpdate("updateFlowUser", map);
					}
					else
					{
						if("end".equals(t.getTnext()))
						{
							isEnd = true;
						}
						else
						{
							isEnd = exeProcess(waitid, nextTalias, nextTusers, datatable, m, time, isEnd);
						}
					}
				}
			}
			else
			{
				if(nextTalias == null)
				{
					isEnd = true;// 需要结束流程
				}
				else
				{
					isEnd = exeProcess(waitid, nextTalias, nextTusers, datatable, m, time, isEnd);
				}
			}
			if(isEnd)
			{
				this.deleteFlowWaitingByPiid(m.getPiid());// 已经结束，清空所有待办事项
				this.updateFlowPi(m.getPiid(), 0, "", dtSet);// 结束

				// 记录最后一步流向
				pd.setId(UniqueId.genUniqueId());
				pd.setTprev(m.getTalias());
				pd.setTalias("end");
				executeInsert("insertFlowPiData", pd);
			}
			else
			{
				List<String> list = this.queryFlowWaitingTalias(m.getPiid());
				if(list == null || list.size() == 0)
				{
					this.updateFlowPi(m.getPiid(), 0, "", dtSet);// 结束
				}
				else
				{
					StringBuilder sb = new StringBuilder(100);
					sb.append(list.get(0));
					for(int i = 1; i < list.size(); i++)
					{
						sb.append(",").append(list.get(i));
					}
					this.updateFlowPi(m.getPiid(), 2, sb.toString(), dtSet);// 处理中
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean exeProcess(Long waitid, String[] nextTalias, String[] nextTusers, String datatable, IFlowWaiting m, String time, boolean isEnd)
	{
		this.deleteFlowWaiting(waitid);// 该待办事项已经处理
		for(int i = 0; i < nextTalias.length; i++)
		{
			String talias = nextTalias[i];
			IFlowWaiting w = this.getFlowWaitingByPiid(m.getPiid(), talias);
			if(w != null && w.getId().longValue() != 0)
			{
				this.updateFlowWaiting(w.getId(), time, w.getTprev() + "," + m.getTalias());// 等待数减1, 上经节点增加一个
			}
			else
			{
				IFlowWaiting newm = new IFlowWaiting();
				newm.setId(UniqueId.genUniqueId());
				newm.setPiid(m.getPiid());
				newm.setYwlsh(m.getYwlsh());
				newm.setSblsh(m.getSblsh());
				newm.setFlowid(m.getFlowid());
				newm.setFlowname(m.getFlowname());
				newm.setTstart(time);
				newm.setTprev(m.getTalias());
				IFlowTask t = this.getFlowTask(m.getFlowid(), talias);
				newm.setTalias(t.getTalias());
				newm.setTname(t.getTname());
				newm.setTcount(t.getTcount());
				newm.setTnext(t.getTnext());
				if(m.getSubcount() < 0)
				{
					dtSet = updateDataTable(datatable, t.getDatatable(), false);
					newm.setDatatable(dtSet);
				}
				else
				{
					newm.setDatatable(dtSet);
				}
				if(nextTusers != null)
				{
					String[] s = nextTusers[i].split(",", -1);
					if(s.length > 1)
					{
						newm.setTusers("," + nextTusers[i] + ",");// 多人，候选人
						newm.setTuser("");
					}
					else
					{
						newm.setTusers("");// 候选人
						newm.setTuser("," + nextTusers[i] + ",");// 单人
					}
				}
				else
				{
					String[] s = t.getTusers().split(",", -1);
					if(s.length > 1)
					{
						newm.setTusers("," + t.getTusers() + ",");// 多人，候选人
						newm.setTuser("");
					}
					else
					{
						newm.setTusers("");// 候选人
						newm.setTuser("," + t.getTusers() + ",");// 单人
					}
				}
				newm.setTmemo(t.getTmemo());
				newm.setSubcount(t.getSubcount());
				if(t.getSubcount() > -1)
				{
					newm.setTuser("," + t.getSubusers() + ",");
				}
				this.saveFlowWaiting(newm);
			}
			if(talias.equals("end"))
			{
				isEnd = true;
			}
		}
		return isEnd;
	}
	
	public String updateDataTable(String oDataTable, String nDataTable, boolean flag)
	{
		Map<String, IFlowDataRow> oMap = null;
		Map<String, IFlowDataRow> nMap = null;
		List<IFlowDataRow> list = new ArrayList<IFlowDataRow>();
		List<IFlowDataRow> oList = null;
		List<IFlowDataRow> nList = null;
		
		if(oDataTable != null && !"".equals(oDataTable))
		{
			oList = toBean(oDataTable,  List.class);
			oMap = new HashMap<String, IFlowDataRow>();
			for (int i = 0; i < oList.size(); i++)
			{
				IFlowDataRow row = toBean(toJson(oList.get(i)),IFlowDataRow.class);
				oMap.put(row.getTname(), row);
			}
			
		}
		if(nDataTable != null && !"".equals(nDataTable))
		{
			nList = toBean(nDataTable,  List.class);
			nMap = new HashMap<String, IFlowDataRow>();
			for (int i = 0; i < nList.size(); i++)
			{
				IFlowDataRow row = toBean(toJson(nList.get(i)),IFlowDataRow.class);
				nMap.put(row.getTname(), row);
			}
		}
		if(oMap != null && nMap != null)
		{
			//将oMap的val放到nMap中
			for (Entry<String, IFlowDataRow> et : nMap.entrySet())
			{
				IFlowDataRow nrow = nMap.get(et.getKey());
				if(nrow != null)
				{
					if(flag)
					{
						nMap.put(et.getKey(), nrow);
						list.add(nrow);
					}
					else 
					{
						IFlowDataRow orow = oMap.get(et.getKey());
						nrow.setTvalue(orow.getTvalue());
						list.add(nrow);
					}
				}
			}
		}
		else if(oMap == null  && nMap != null)
		{
			//将nMap直接返回
			for (Entry<String, IFlowDataRow> et : nMap.entrySet())
			{
				list.add(et.getValue());
			}
		}
		else if(oMap != null  && nMap == null)
		{
			//将oMap中的trwx全部改为001并返回
			for (Entry<String, IFlowDataRow> et : oMap.entrySet())
			{
				IFlowDataRow v = et.getValue();
				v.setTrwx("001");
				list.add(v);
			}
		}
		return toJson(list);
	}

	public boolean updateSubFlowWaiting(Long id, String subusers, String datatable)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("subusers", subusers);
		map.put("datatable", datatable);
		return executeUpdate("updateSubFlowWaiting", map) > 0 ? true : false;
	}
	
	public boolean updateSubFlowWaitingSubusers(Long id, String subusers, String datatable)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("subusers", subusers);
		map.put("datatable", datatable);
		return executeUpdate("updateSubFlowWaitingSubusers", map) > 0 ? true : false;
	}
	
	public boolean updateFlowUser(Long wid, String olduser, String oldname,String newuser, String newname)
	{
		IFlowWaiting m = this.getFlowWaiting(wid);
		if(m != null)
		{
			String tuser = m.getTuser();
			if(tuser.indexOf(olduser) > 0 && tuser.indexOf(newuser) < 0)
			{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", m.getId());
				tuser = tuser.replaceAll("," + olduser + ",", "," + newuser + ",");
				map.put("tuser", tuser);
				String time = TimeUtil.getCurrentTime();
				IFlowPiData pd = new IFlowPiData();
				pd.setId(UniqueId.genUniqueId());
				pd.setPiid(m.getPiid());
				pd.setTprev(m.getTprev());
				pd.setTalias(m.getTalias());
				pd.setTname(m.getTname());
				pd.setStatus(1);// 状态(0已处理,1代办,2挂起,3取消挂起)
				pd.setPaccount(olduser);
				pd.setPname(oldname);
				pd.setPtime(time);
				pd.setPtype("1");
				pd.setMemo("无");
				pd.setDatatable("[]");
				executeInsert("insertFlowPiData", pd);
				executeUpdate("updateFlowUser", map);
				return true;
			}
		}
		return false;
	}
}
