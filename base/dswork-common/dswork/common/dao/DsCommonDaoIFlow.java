/**
 * 公共Dao
 */
package dswork.common.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.reflect.TypeToken;

import dswork.common.DsFactory;
import dswork.common.model.IFlow;
import dswork.common.model.IFlowDataRow;
import dswork.common.model.IFlowParam;
import dswork.common.model.IFlowPi;
import dswork.common.model.IFlowPiData;
import dswork.common.model.IFlowTask;
import dswork.common.model.IFlowWaiting;
import dswork.core.db.MyBatisDao;
import dswork.core.util.IdUtil;
import dswork.core.util.TimeUtil;

@Repository
@SuppressWarnings("all")
public class DsCommonDaoIFlow extends MyBatisDao
{
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

	// 万一出现2条的异常处理
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

	public IFlowWaiting saveFlowStart(String alias, String users, String ywlsh, String sblsh, String account, String name, int piDay, boolean isWorkDay, boolean tenable)
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
			pi.setId(IdUtil.genId());
			IFlowParam beforeParam = new IFlowParam();
			IFlowParam afterParam = new IFlowParam();
			beforeParam.setFlowid(flowid);
			beforeParam.setPiid(pi.getId());
			beforeParam.setAlias(alias);
			beforeParam.setStart(true);
			BeanUtils.copyProperties(beforeParam, afterParam);// 复制
			DsFactory.getUtil().handleMethod(beforeParam, true);
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
			pi.setDataview(task.getDataview());
			executeInsert("insertFlowPi", pi);
			Long piid = pi.getId();
			IFlowWaiting m = new IFlowWaiting();
			m.setId(IdUtil.genId());
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
			m.setTenable(tenable ? 0 : -1);// （-1不启用，0启用）
			m.setDataview(task.getDataview());
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
			// param.setFlowid(flowid);
			// param.setPiid(pi.getId());
			// param.setAlias(alias);
			// param.setStart(true);
			afterParam.setProcess(m);
			DsFactory.getUtil().handleMethod(afterParam, false);
			return m;
		}
		return null;
	}

	public String saveStart(String alias, String users, String ywlsh, String sblsh, String account, String name, int piDay, boolean isWorkDay, boolean tenable)
	{
		IFlowWaiting w = saveFlowStart(alias, users, ywlsh, sblsh, account, name, piDay, isWorkDay, tenable);
		if(w != null)
		{
			return String.valueOf(w.getPiid());
		}
		return "";
	}

	public void saveStop(Long flowid, String alias, Long piid)
	{
		IFlowParam beforeParam = new IFlowParam();
		IFlowParam afterParam = new IFlowParam();
		beforeParam.setFlowid(flowid);
		beforeParam.setPiid(piid);
		beforeParam.setAlias(alias);
		beforeParam.setEnd(true);
		BeanUtils.copyProperties(beforeParam, afterParam);// 复制
		DsFactory.getUtil().handleMethod(beforeParam, true);
		IFlowPi pi = this.getFlowPiByPiid(piid.longValue() + "");
		this.deleteFlowWaitingByPiid(piid);
		this.updateFlowPi(piid, 0, "", pi.getDatatable());// 结束
		DsFactory.getUtil().handleMethod(afterParam, false);
	}

	public boolean saveProcess(Long waitid, String[] nextTalias, String[] nextTusers, String account, String name, String resultType, String resultMsg, String datatable)
	{
		boolean isEnd = false;
		IFlowWaiting m = this.getFlowWaiting(waitid);
		if(m != null)
		{
			IFlowPi pi = this.getFlowPiByPiid(m.getPiid() + "");
			IFlowParam beforeParam = new IFlowParam();
			IFlowParam afterParam = new IFlowParam();
			IFlowWaiting w = new IFlowWaiting();
			BeanUtils.copyProperties(m, w);// 复制m
			beforeParam.setPiid(w.getPiid());
			beforeParam.setProcess(w);
			beforeParam.setFlowid(w.getFlowid());
			beforeParam.setAlias(pi.getAlias());
			BeanUtils.copyProperties(beforeParam, afterParam);// 复制
			DsFactory.getUtil().handleMethod(beforeParam, true);

			
			// 先初始化已办数据。除状态外
			String time = TimeUtil.getCurrentTime();
			IFlowPiData pd = new IFlowPiData();
			pd.setId(IdUtil.genId());
			pd.setPiid(m.getPiid());
			pd.setTprev(m.getTprev());
			pd.setTalias(m.getTalias());
			pd.setTname(m.getTname());
			pd.setPaccount(account);
			pd.setPname(name);
			pd.setPtime(time);
			pd.setPtype(resultType);
			pd.setMemo(resultMsg);
			pd.setDatatable(datatable);
			pd.setDataview(m.getDataview());

			String dtSet = updateDataTable(pi.getDatatable(), datatable);
			m.setDatatable(dtSet);
			
			if(nextTalias == null)// 代办或增加会签人或取得任务
			{
				Map<String, String> tusersMap = new HashMap<String, String>();
				if(nextTusers != null)
				{
					for(String us : nextTusers)
					{
						String[] tuser = us.split(",");
						for(String u : tuser)
						{
							u = u.trim();
							if(u.length() > 0)
							{
								tusersMap.put(u, u);
							}
						}
					}
				}
				List<String> tusersList = new ArrayList<String>(tusersMap.keySet());
				
				if(m.getSubcount() > -1)// 会签
				{
					m.setTusers("");
					String[] subusersArr = m.getSubusers().split(",");// 目前待办的会签人
					boolean nomy = tusersMap.get(account) == null;
					// 合并新的会签人集合
					for(String u : subusersArr)
					{
						tusersMap.put(u, u);
					}
					if(nomy)// 处理掉一次会签
					{
						tusersMap.remove(account);
					}
					if(tusersMap.size() > 0)
					{
						if(m.getSubcount() > 0)
						{
							m.setSubcount(m.getSubcount() - 1);// 新的
						}
						if(tusersList.size() > 0)// 加人
						{
							m.setSubcount(m.getSubcount() + tusersList.size());// 新的
							if(m.getSubcount() > tusersMap.size())// 没那么多人
							{
								m.setSubcount(tusersMap.size());
							}
						}
						String subusers = ",";
						for(String u : tusersMap.keySet())
						{
							subusers += u = ",";
						}
						m.setSubusers(subusers);
					}
					else
					{
						m.setSubusers("");
						m.setSubcount(0);
					}
					
					if(m.getTuser().length() == 0 && m.getSubcount() == 0)
					{
						isEnd = true;// 会签流程最后一步切没有控制人，需要结束流程
					}
					pd.setStatus(4);// 状态(0已处理,1代办,2挂起,3取消挂起,4会签)
				}
				else// 非会签，转办或取得任务或重回候选
				{
					String tuser = account;
					String tusers = "";
					if(tusersList.size() > 1)
					{
						tusers = ",";
						for(String u : tusersList)
						{
							tusers += u = ",";
						}
						tuser = "";
					}
					else if(tusersList.size() == 1)
					{
						tusers = "";
						tuser = "," + tusersList.get(0) + ",";
					}
					else
					{
						tusers = "";
						tuser = "," + account + ",";
					}
					m.setTusers(tusers);
					m.setTuser(tuser);
					pd.setStatus(1);// 状态(0已处理,1代办,2挂起,3取消挂起,4会签)
				}
				executeUpdate("updateFlowWaiting", m);// 更新此待办
			}
			else// 有下一步的
			{
				if("end".equals(m.getTnext()))
				{
					isEnd = true;
				}
				else
				{
					isEnd = exeProcess(account, nextTalias, nextTusers, m, time);
				}
				pd.setStatus(m.getSubcount() > -1 ? 4 : 0);// 状态(0已处理,1代办,2挂起,3取消挂起,4会签)
			}
			executeInsert("insertFlowPiData", pd);
			
			List<IFlowPiData> pidataList = new ArrayList<IFlowPiData>();
			pidataList.add(pd);
			
			if(isEnd)
			{
				afterParam.setEnd(true);// 标记为结束
				this.deleteFlowWaitingByPiid(m.getPiid());// 已经结束，清空所有待办事项
				this.updateFlowPi(m.getPiid(), 0, "", dtSet);// 结束
				// 记录最后一步流向
				IFlowPiData pdend = new IFlowPiData();
				pdend.setId(IdUtil.genId());
				pdend.setPiid(m.getPiid());
				pdend.setTprev(m.getTalias());
				pdend.setTalias("end");
				pdend.setTname("结束");
				pdend.setStatus(m.getSubcount() > -1 ? 4 : 0);// 状态(0已处理,1代办,2挂起,3取消挂起,4会签)
				pdend.setPaccount(account);
				pdend.setPname(name);
				pdend.setPtime(time);
				pdend.setPtype(resultType);
				pdend.setMemo(resultMsg);
				pdend.setDatatable(datatable);
				pdend.setDataview(m.getDataview());
				executeInsert("insertFlowPiData", pdend);
				
				pidataList.add(pdend);
			}
			else
			{
				List<IFlowWaiting> newWaitList = this.queryFlowWaitingByPiid(m.getPiid());
				StringBuilder sb = new StringBuilder(100);
				sb.append(newWaitList.get(0).getTalias());
				for(int i = 1; i < newWaitList.size(); i++)
				{
					sb.append(",").append(newWaitList.get(i).getTalias());
				}
				this.updateFlowPi(m.getPiid(), 2, sb.toString(), dtSet);// 处理中
				afterParam.setWaitingList(newWaitList);
			}
			afterParam.setPidataList(pidataList);
			DsFactory.getUtil().handleMethod(afterParam, false);
			return true;
		}
		return false;
	}

	private boolean exeProcess(String account, String[] nextTalias, String[] nextTusers, IFlowWaiting m, String time)
	{
		boolean isEnd = false;
		this.deleteFlowWaiting(m.getId());// 该待办事项已经处理
		if(nextTalias.length == 0)
		{
			isEnd = true;
		}
		else
		{
			int uIndexLen = nextTusers != null ? (nextTusers.length - 1) : -1;
			for(int i = 0; i < nextTalias.length; i++)
			{
				String talias = nextTalias[i];
				if(talias.equals("end"))
				{
					isEnd = true;
					break;
				}

				Map<String, String> uMap = new HashMap<String, String>();
				if(uIndexLen >= i && nextTusers[i].length() > 0)
				{
					String ux = nextTusers[i].split("\\|")[0];
					String[] uarr = ux.split(",");
					for(String u : uarr)
					{
						u = u.trim();
						if(u.length() > 0)
						{
							uMap.put(u, u);
						}
					}
				}
				
				
				
				IFlowWaiting w = this.getFlowWaitingByPiid(m.getPiid(), talias);
				String tusers = "";
				String tuser = "," + account + ",";// 暂定当前人为经办人
				String subusers = "";
				int subcount = -1;
				
				String xu = "";
				
				if(w == null)
				{
					w = new IFlowWaiting();
					w.setId(0L);
					w.setPiid(m.getPiid());
					w.setYwlsh(m.getYwlsh());
					w.setSblsh(m.getSblsh());
					w.setFlowid(m.getFlowid());
					w.setFlowname(m.getFlowname());
					w.setTstart(time);
					w.setTprev(m.getTalias());
					IFlowTask t = this.getFlowTask(m.getFlowid(), talias);
					w.setTalias(t.getTalias());
					w.setTname(t.getTname());
					w.setTcount(t.getTcount());
					w.setTnext(t.getTnext());
					w.setTenable(1);
					w.setDatatable(t.getDatatable());
					w.setDataview(t.getDataview());
					w.setTmemo(t.getTmemo());
					w.setSubcount(t.getSubcount());

					// 新增的，如果没选人，才用配置的
					if(uMap.size() == 0)
					{
						if(w.getSubcount() > -1)
						{
							xu = t.getSubusers().split("\\|")[0];
						}
						else
						{
							xu = t.getTusers().split("\\|")[0];
						}
					}
				}
				else
				{
					// 处理合并任务
					if(w.getTcount() > 1)
					{
						// 让合并数减1
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", w.getId());
						map.put("tstart", time);
						map.put("tprev", w.getTprev() + "," + m.getTalias());
						executeUpdate("updateFlowWaitingTcount", map);// 等待数减，上级节点增加一个
					}
					if(w.getSubcount() > -1)
					{
						xu = w.getSubusers();
					}
					else
					{
						xu = w.getTusers();
					}
				}
				
				if(xu.length() > 0)
				{
					String[] uarr = xu.split(",");
					for(String u : uarr)
					{
						u = u.trim();
						if(u.length() > 0)
						{
							uMap.put(u, u);
						}
					}
				}
				if(w.getSubcount() > -1)// 会签
				{
					subcount = w.getSubcount();
					if(uMap.size() > 0)
					{
						subusers = ",";
						for(String u : uMap.keySet())
						{
							subusers += u = ",";
						}
						if(subcount == 0)// 不会小于0
						{
							subcount = uMap.size();
						}
						else
						{
							subcount = subcount > uMap.size() ? uMap.size() : subcount;
						}
					}
					else
					{
						subusers = "";
						subcount = 0;
					}
				}
				else
				{
					if(uMap.size() > 0)
					{
						if(uMap.size() == 1)
						{
							tusers = "";
							
							for(String u : uMap.keySet())
							{
								tuser = "," + u + ",";
							}
						}
						else
						{
							tusers = ",";
							for(String u : uMap.keySet())
							{
								tusers += u = ",";
							}
							
							tuser = "";
						}
					}
				}
				w.setTusers(tusers);
				w.setTuser(tuser);
				w.setSubusers(subusers);
				w.setSubcount(subcount);
				
				String dtSet = updateDataTable(m.getDatatable(), w.getDatatable());
				w.setDatatable(dtSet);
				
				if(w.getId().longValue() <= 0)
				{
					w.setId(IdUtil.genId());
					this.saveFlowWaiting(w);
				}
				else
				{
					executeUpdate("updateFlowWaiting", w);
				}
			}
		}
		return isEnd;
	}
	
	public boolean updateFlowWaitingTenable(long waitid, String datatable)
	{
		IFlowWaiting w = this.getFlowWaiting(waitid);
		w.setTenable(1);
		w.setDatatable(datatable);
		return executeUpdate("updateFlowWaiting", w) > 0;
	}

	/**
	 * 表单替换
	 * @param oDataTable 源头的表单
	 * @param nDataTable 目标的表单
	 * @return
	 */
	private String updateDataTable(String oDataTable, String nDataTable)
	{
		Map<String, IFlowDataRow> oMap = null;
		Map<String, IFlowDataRow> nMap = null;
		List<IFlowDataRow> list = new ArrayList<IFlowDataRow>();
		List<IFlowDataRow> oList = null;
		List<IFlowDataRow> nList = null;
		if(oDataTable != null && !"".equals(oDataTable))
		{
			oList = DsFactory.getUtil().toBean(oDataTable, new TypeToken<List<IFlowDataRow>>()
			{
			}.getType());
			if(oList != null && oList.size() > 0)
			{
				oMap = new LinkedHashMap<String, IFlowDataRow>();
				for(int i = 0; i < oList.size(); i++)
				{
					IFlowDataRow row = oList.get(i);
					oMap.put(row.getTname(), row);
				}
			}
		}
		if(nDataTable != null && !"".equals(nDataTable))
		{
			nList = DsFactory.getUtil().toBean(nDataTable, new TypeToken<List<IFlowDataRow>>()
			{
			}.getType());
			if(nList != null && nList.size() > 0)
			{
				nMap = new LinkedHashMap<String, IFlowDataRow>();
				for(int i = 0; i < nList.size(); i++)
				{
					IFlowDataRow row = nList.get(i);
					nMap.put(row.getTname(), row);
				}
			}
		}
		if(oMap != null && nMap != null) // 旧不空，新不空
		{
			// 将oMap的val放到nMap中
			for(Entry<String, IFlowDataRow> et : nMap.entrySet())
			{
				IFlowDataRow nrow = nMap.get(et.getKey());
				if(nrow != null)
				{
					IFlowDataRow orow = oMap.get(et.getKey());
					if(orow != null)
					{
						nrow.setTvalue(orow.getTvalue());
					}
					list.add(nrow);
				}
			}
		}
		else if(oMap == null && nMap != null)// 旧空，新不空
		{
			// 将nMap直接返回
			for(Entry<String, IFlowDataRow> et : nMap.entrySet())
			{
				list.add(et.getValue());
			}
		}
		else if(oMap != null && nMap == null)// 旧不空，新空
		{
			// 将oMap中的trwx全部改为001并返回
			for(Entry<String, IFlowDataRow> et : oMap.entrySet())
			{
				IFlowDataRow v = et.getValue();
				v.setTrwx("001");
				list.add(v);
			}
		}
		return DsFactory.getUtil().toJson(list);
	}

	/**
	 * 更新已进行会签的用户和表单数据(会签数减一)
	 * @param id 待办ID
	 * @param subusers 已进行会签的用户ID(以逗号分隔用户，前后补逗号)
	 * @param datatable 表单数据
	 * @return
	 */
	private boolean updateSubFlowWaiting(Long id, String subusers, String datatable)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("subusers", subusers);
		map.put("datatable", datatable);
		return executeUpdate("updateSubFlowWaiting", map) > 0 ? true : false;
	}
}
