package dswork.common.model;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface IFlowInterface
{
	/**
	 * 流程启动
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param sblsh 申办流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @param tenable 当前流程实例是否有效（false不启用，true启用），false：保存未提交的情况
	 * @return 流程实例ID
	 */
	public String start(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable);

	/**
	 * 流程启动并返回开始节点待办信息
	 * @param alias 启动流程的标识
	 * @param users 启动节点任务处理人，如果为null，则使用流程配置中的处理人信息
	 * @param ywlsh 业务流水号
	 * @param sblsh 申办流水号
	 * @param caccount 提交人账号
	 * @param cname 提交人姓名
	 * @param piDay 时限天数
	 * @param isWorkDay 时限天数类型(false日历日,true工作日)
	 * @param tenable 当前流程实例是否有效（false不启用，true启用），false：保存未提交的情况
	 * @return 流程实例的start待办信息或null
	 */
	public IFlowWaiting startFlow(String alias, String users, String ywlsh, String sblsh, String caccount, String cname, int piDay, boolean isWorkDay, boolean tenable);

	
	
	public void stop(Long flowid, String alias, String piid);

	/**
	 * 流程处理
	 * @param waitid 待办事项ID
	 * @param nextTalias 下级任务列表，如果为null，处理当前任务后，会结束流程
	 * @param nextTusers 下级任务处理人列表，如果为null，则使用流程配置中的处理人信息
	 * @param paccount 当前处理人账号
	 * @param pname 当前处理人姓名
	 * @param resultType 处理类型
	 * @param resultMsg 处理意见
	 * @param datatable 当前数据集
	 * @return true|false
	 */
	public boolean process(long waitid, String replaceUser, String[] nextTalias, String[] nextTusers, String paccount, String pname, String resultType, String resultMsg, String datatable);

	/**
	 * 查询当前账号的所有待办
	 * @param account 账号
	 * @return
	 */
	public List<IFlowWaiting> queryWaiting(String account);

	/**
	 * 取得待办任务
	 * @param waitid 需要取得的待办ID
	 * @param user 取得待办的用户
	 * @return
	 */
	public boolean takeWaiting(long waitid, String user);

	/**
	 * 根据待办ID取得待办
	 * @param waitid 待办ID
	 * @return
	 */
	public IFlowWaiting getWaiting(long waitid);

	/**
	 * 获取流程任务
	 * @param flowid 流程ID
	 * @return
	 */
	public Map<String, String> getTaskList(long flowid);

	/**
	 * 根据流程ID获取流程
	 * @param flowid 流程ID
	 * @return
	 */
	public IFlow getFlowById(long flowid);

	/**
	 * 根据流程实例id(piid)获取流程实例
	 * @param piid 流程实例id
	 * @return
	 */
	public IFlowPi getFlowPiByPiid(String piid);

	/**
	 * 根据业务流水号查询所有的流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
	public List<IFlowPi> queryFlowPi(String ywlsh);

	/**
	 * 根据申办流水号查询所有的流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
	public List<IFlowPi> queryFlowPiBySblsh(String sblsh);

	/**
	 * 根据业务流水号获取流程实例
	 * @param ywlsh 业务流水号
	 * @return
	 */
	public IFlowPi getFlowPi(String ywlsh);

	/**
	 * 根据申办流水号获取流程实例
	 * @param sblsh 申办流水号
	 * @return
	 */
	public IFlowPi getFlowPiBySblsh(String sblsh);

	/**
	 * 根据流程实例id(piid)查询已办数据
	 * @param piid 流程实例id
	 * @return
	 */
	public List<IFlowPiData> queryFlowPiData(String piid);

	/**
	 * 根据流程实例id(piid) 查询待办数据
	 * @param piid
	 * @return
	 */
	public List<IFlowWaiting> queryFlowWaitingByPiid(String piid);

	/**
	 * 根据流程实例id删除流程实例
	 * @param id
	 */
	public void deleteFlowPi(String id);

	/**
	 * 是否启用待办
	 * @param wid 待办ID
	 * @param datatable 表单结构
	 * @return
	 */
	public boolean saveFlow(Long wid, String datatable);
}
