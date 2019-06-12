/**
 * 流程待办事项Model
 */
package dswork.common.model;

public class IFlowWaiting
{
	// 主键ID
	private long id = 0L;
	// 实例ID
	private long piid = 0L;
	// 业务流水号
	private String ywlsh = "";
	// 申办流水号
	private String sblsh = "";
	// 流程ID
	private long flowid = 0L;
	// 流程名称
	private String flowname = "";
	// 任务标识
	private String talias = "";
	// 任务名称
	private String tname = "";
	// 合并任务个数(只有一个任务时等于1，其余大于1)
	private int tcount = 0;
	// 上级任务(从哪过来的)
	private String tprev = "";
	// 下级任务（以逗号分隔节点标识，以|线分隔分支任务）翻译成代码可理解为：[(可选1 || 可选2 || 可选3) && (可选A || 可选B)]
	private String tnext = "";
	// 任务开始时间
	private String tstart = "";
	// 经办人([会签用户|]经办用户，当为会签任务时对有中括号部分，用户前后补逗号)
	private String tuser = "";
	// 当前任务的用户ID(以逗号分隔可选用户)
	private String tusers = "";
	// 至少合并会签个数(不需要会签时值为-1)
	private int subcount = -1;
	// 已进行会签的用户ID(以逗号分隔用户，前后补逗号)
	private String subusers = "";
	// 参数
	private String tmemo = "";
	// 数据结构
	private String datatable = "";
	// 是否启用待办（-1不启用，0启用）
	private int tenable = 0;
	// 数据视图
	private String dataview = "";
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getPiid()
	{
		return piid;
	}

	public void setPiid(long piid)
	{
		this.piid = piid;
	}

	public String getYwlsh()
	{
		return ywlsh;
	}

	public void setYwlsh(String ywlsh)
	{
		this.ywlsh = ywlsh;
	}

	public String getSblsh()
	{
		return sblsh;
	}

	public void setSblsh(String sblsh)
	{
		this.sblsh = sblsh;
	}

	public long getFlowid()
	{
		return flowid;
	}

	public void setFlowid(long flowid)
	{
		this.flowid = flowid;
	}

	public String getFlowname()
	{
		return flowname;
	}

	public void setFlowname(String flowname)
	{
		this.flowname = flowname;
	}

	public String getTalias()
	{
		return talias;
	}

	public void setTalias(String talias)
	{
		this.talias = talias;
	}

	public String getTname()
	{
		return tname;
	}

	public void setTname(String tname)
	{
		this.tname = tname;
	}

	public int getTcount()
	{
		return tcount;
	}

	public void setTcount(int tcount)
	{
		this.tcount = tcount;
	}

	public String getTprev()
	{
		return tprev;
	}

	public void setTprev(String tprev)
	{
		this.tprev = tprev;
	}

	public String getTnext()
	{
		return tnext;
	}

	public void setTnext(String tnext)
	{
		this.tnext = tnext;
	}

	public String getTstart()
	{
		return tstart;
	}

	public void setTstart(String tstart)
	{
		this.tstart = tstart;
	}

	public String getTuser()
	{
		return tuser;
	}

	public void setTuser(String tuser)
	{
		this.tuser = tuser;
	}

	public String getTusers()
	{
		return tusers;
	}

	public void setTusers(String tusers)
	{
		this.tusers = tusers;
	}

	public int getSubcount()
	{
		return subcount;
	}

	public void setSubcount(int subcount)
	{
		this.subcount = subcount;
	}

	public String getSubusers()
	{
		return subusers;
	}

	public void setSubusers(String subusers)
	{
		this.subusers = subusers;
	}

	public String getTmemo()
	{
		return tmemo;
	}

	public void setTmemo(String tmemo)
	{
		this.tmemo = tmemo;
	}

	public String getDatatable()
	{
		return datatable;
	}

	public void setDatatable(String datatable)
	{
		this.datatable = datatable;
	}

	public int getTenable()
	{
		return tenable;
	}

	public void setTenable(int tenable)
	{
		this.tenable = tenable;
	}

	public String getDataview()
	{
		return dataview;
	}

	public void setDataview(String dataview)
	{
		this.dataview = dataview;
	}
}
