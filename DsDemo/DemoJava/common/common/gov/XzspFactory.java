package common.gov;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import MQAPI.AcceptOB;
import MQAPI.ApplicationOB;
import MQAPI.BlockOB;
import MQAPI.CompleteOB;
import MQAPI.PreAcceptOB;
import MQAPI.ReceiveRegOB;
import MQAPI.ResumeOB;
import MQAPI.SubmitOB;
import MQAPI.SupplyAcceptOB;
import MQAPI.SupplyOB;

public class XzspFactory
{
	/**
	 * 申办0
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param SXMC 事项名称
	 * @param FSXBM 父事项编码
	 * @param FSXMC 父事项名称
	 * @param SQRLX 申请人类型
	 * @param SQRMC 申请人名称
	 * @param SQRZJLX 申请人证件类型
	 * @param SQRZJHM 申请人证件号码
	 * @param LXRXM 联系人姓名
	 * @param LXRZJLX 联系人证件类型
	 * @param LXRSFZJHM 联系人身份证件号码
	 * @param LXRSJ 联系人手机
	 * @param LXRYX 联系人邮箱
	 * @param SBXMMC 申办项目名称
	 * @param SBCLQD 申办材料清单
	 * @param TJFS 提交方式
	 * @param SBHZH 申办回执号
	 * @param SBSJ 申办时间
	 * @param SBJTWD 申办具体网点
	 * @param XZQHDM 业务发生所在行政区划代码
	 * @param YSBLSH 原申办流水号
	 * @param SJBBH 数据版本号
	 * @param SQRDH 申请人电话
	 * @param XMHGCBH 项目/工程编号
	 * @param LXWH 立项批复文号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int applicationOB(String SBLSH, String SXBM, String SXMC, String FSXBM, String FSXMC, String SQRLX, String SQRMC, String SQRZJLX, String SQRZJHM, String LXRXM, String LXRZJLX, String LXRSFZJHM, String LXRSJ, String LXRYX, String SBXMMC, String SBCLQD, String TJFS, String SBHZH,
			Date SBSJ, String SBJTWD, String XZQHDM, String YSBLSH, int SJBBH, String SQRDH, String XMHGCBH, String LXWH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		ApplicationOB entity = new ApplicationOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setSXMC(SXMC);
		entity.setFSXBM(FSXBM);
		entity.setFSXMC(FSXMC);
		entity.setSQRLX(SQRLX);
		entity.setSQRMC(SQRMC);
		entity.setSQRZJLX(SQRZJLX);
		entity.setSQRZJHM(SQRZJHM);
		entity.setLXRXM(LXRXM);
		entity.setLXRZJLX(LXRZJLX);
		entity.setLXRSFZJHM(LXRSFZJHM);
		entity.setLXRSJ(LXRSJ);
		entity.setLXRYX(LXRYX);
		entity.setSBXMMC(SBXMMC);
		entity.setSBCLQD(SBCLQD);
		entity.setTJFS(TJFS);
		entity.setSBHZH(SBHZH);
		entity.setSBSJ(SBSJ);
		entity.setSBJTWD(SBJTWD);
		entity.setXZQHDM(XZQHDM);
		entity.setYSBLSH(YSBLSH);
		entity.setSJBBH(SJBBH);
		entity.setSQRDH(SQRDH);
		entity.setXMHGCBH(XMHGCBH);
		entity.setLXWH(LXWH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 预受理1
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param YWLSH 业务流水号
	 * @param YSLBMMC 预受理部门名称
	 * @param YSLBMZZJGDM 预受理部门组织机构代码
	 * @param XZQHDM 预受理部门所在地行政区划代码
	 * @param BLRXM 办理人姓名
	 * @param BLRGH 办理人工号
	 * @param YSLZTDM 预受理状态代码
	 * @param BSLYY 不受理原因
	 * @param BJBZSM 补交补正说明
	 * @param YSLSJ 预受理时间
	 * @param YSLJTDD 预受理具体地点
	 * @param SJBBH 数据版本号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int preAcceptOB(String SBLSH, String SXBM, String YWLSH, String YSLBMMC, String YSLBMZZJGDM, String XZQHDM, String BLRXM, String BLRGH, String YSLZTDM, String BSLYY, String BJBZSM, Date YSLSJ, String YSLJTDD, int SJBBH, String BZ, String BYZDA, String BYZDB, String BYZDC,
			Date BYZDD)
	{
		PreAcceptOB entity = new PreAcceptOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setYWLSH(YWLSH);
		entity.setYSLBMMC(YSLBMMC);
		entity.setYSLBMZZJGDM(YSLBMZZJGDM);
		entity.setXZQHDM(XZQHDM);
		entity.setBLRXM(BLRXM);
		entity.setBLRGH(BLRGH);
		entity.setYSLZTDM(YSLZTDM);
		entity.setBSLYY(BSLYY);
		entity.setBJBZSM(BJBZSM);
		entity.setYSLSJ(YSLSJ);
		entity.setYSLJTDD(YSLJTDD);
		entity.setSJBBH(SJBBH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 受理2
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param YWLSH 业务流水号
	 * @param SLBMMC 受理部门名称
	 * @param SLBMZZJDDM 受理部门组织机构代码
	 * @param XZQHDM 受理部门所在行政区划代码
	 * @param BLRXM 办理人姓名
	 * @param BLRGH 办理人工号
	 * @param SLZTDM 受理状态代码
	 * @param BSLYY 不受理原因
	 * @param SLHZH 受理回执号
	 * @param SLSJ 受理时间
	 * @param GXDXZQHDM 发生业务管辖地行政区划代码
	 * @param CXMM 查询密码
	 * @param SJBBH 数据版本号
	 * @param SPSXMC 审批事项名称
	 * @param GDBLSX 规定办理时限
	 * @param GDBLSXDDW 规定办理时限的单位
	 * @param GDSF 规定收费
	 * @param XMMC 项目名称
	 * @param SQDWHSQRXM 申请单位或申请人名称
	 * @param SQDWJBRXM 申请单位经办人姓名
	 * @param SQDWLXDH 申请单位联系电话
	 * @param SQDWJBRSJ 申请单位经办人手机
	 * @param SQDWJBRYJ 申请单位经办人邮件
	 * @param SLJTDD 受理具体地点
	 * @param SLZLQD 受理资料清单
	 * @param TJFS 提交方式
	 * @param PDH 排队号
	 * @param XMHGCBH 项目/工程编号
	 * @param LXWH 立项批复文号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int aceeptOB(String SBLSH, String SXBM, String YWLSH, String SLBMMC, String SLBMZZJDDM, String XZQHDM, String BLRXM, String BLRGH, String SLZTDM, String BSLYY, String SLHZH, Date SLSJ, String GXDXZQHDM, String CXMM, int SJBBH, String SPSXMC, int GDBLSX, String GDBLSXDDW,
			String GDSF, String XMMC, String SQDWHSQRXM, String SQDWJBRXM, String SQDWLXDH, String SQDWJBRSJ, String SQDWJBRYJ, String SLJTDD, String SLZLQD, String TJFS, String PDH, String XMHGCBH, String LXWH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		AcceptOB entity = new AcceptOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setYWLSH(YWLSH);
		entity.setSLBMMC(SLBMMC);
		entity.setSLBMZZJDDM(SLBMZZJDDM);
		entity.setXZQHDM(XZQHDM);
		entity.setBLRXM(BLRXM);
		entity.setBLRGH(BLRGH);
		entity.setSLZTDM(SLZTDM);
		entity.setBSLYY(BSLYY);
		entity.setSLHZH(SLHZH);
		entity.setSLSJ(SLSJ);
		entity.setGXDXZQHDM(GXDXZQHDM);
		entity.setCXMM(CXMM);
		entity.setSJBBH(SJBBH);
		entity.setSPSXMC(SPSXMC);
		entity.setGDBLSX(GDBLSX);
		entity.setGDBLSXDDW(GDBLSXDDW);
		entity.setGDSF(GDSF);
		entity.setXMMC(XMMC);
		entity.setSQDWHSQRXM(SQDWHSQRXM);
		entity.setSQDWJBRXM(SQDWJBRXM);
		entity.setSQDWLXDH(SQDWLXDH);
		entity.setSQDWJBRSJ(SQDWJBRSJ);
		entity.setSQDWJBRYJ(SQDWJBRYJ);
		entity.setSLJTDD(SLJTDD);
		entity.setSLZLQD(SLZLQD);
		entity.setTJFS(TJFS);
		entity.setPDH(PDH);
		entity.setXMHGCBH(XMHGCBH);
		entity.setLXWH(LXWH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 审批3
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param SPHJDM 审批环节代码
	 * @param SPHJMC 审批环节名称
	 * @param SPBMMC 审批部门名称
	 * @param SPBMZZJDMD 审批部门组织机构代码
	 * @param XZQHDM 审批部门所在地行政区域代码
	 * @param SPRXM 审批人姓名
	 * @param SPRZWDM 审批人职务代码
	 * @param SPRZWMC 审批人职务名称
	 * @param SPYJ 审批意见
	 * @param SPSJ 审批时间
	 * @param SPHJZTDM 审批环节状态代码
	 * @param SJBBH 数据版本号
	 * @param YWLSH 业务流水号
	 * @param SPBZH 审批步骤号
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int submitOB(String SBLSH, String SXBM, String SPHJDM, String SPHJMC, String SPBMMC, String SPBMZZJDMD, String XZQHDM, String SPRXM, String SPRZWDM, String SPRZWMC, String SPYJ, Date SPSJ, String SPHJZTDM, int SJBBH, String YWLSH, int SPBZH, String PDH, String BZ, String BYZDA,
			String BYZDB, String BYZDC, Date BYZDD)
	{
		SubmitOB entity = new SubmitOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setSPHJDM(SPHJDM);
		entity.setSPHJMC(SPHJMC);
		entity.setSPBMMC(SPBMMC);
		entity.setSPBMZZJDMD(SPBMZZJDMD);
		entity.setXZQHDM(XZQHDM);
		entity.setSPRXM(SPRXM);
		entity.setSPRZWDM(SPRZWDM);
		entity.setSPRZWMC(SPRZWMC);
		entity.setSPYJ(SPYJ);
		entity.setSPSJ(SPSJ);
		entity.setSPHJZTDM(SPHJZTDM);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setSPBZH(SPBZH);
		entity.setPDH(PDH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 办结4
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param BJBMMC 办结部门名称
	 * @param BJBMZZJDDM 办结部门组织机构代码
	 * @param XZQHDM 办结部门行政区划代码
	 * @param BJJGDM 办结结果代码
	 * @param BJJGMS 办结结果描述
	 * @param ZFHTHYY 作废或退回原因
	 * @param ZJGZMC 证件/盖章名称
	 * @param ZJBH 证件编号
	 * @param ZJYXQX 证件有效期限
	 * @param FZGZDW 发证/盖章单位
	 * @param SFJE 收费金额
	 * @param JEDWDM 金额单位代码
	 * @param BJSJ 办结时间
	 * @param SJBBH 数据版本号
	 * @param YWLSH 业务流水号
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int completeOB(String SBLSH, String SXBM, String BJBMMC, String BJBMZZJDDM, String XZQHDM, String BJJGDM, String BJJGMS, String ZFHTHYY, String ZJGZMC, String ZJBH, String ZJYXQX, String FZGZDW, int SFJE, String JEDWDM, Date BJSJ, int SJBBH, String YWLSH, String PDH, String LXWH,
			String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		CompleteOB entity = new CompleteOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setBJBMMC(BJBMMC);
		entity.setBJBMZZJDDM(BJBMZZJDDM);
		entity.setXZQHDM(XZQHDM);
		entity.setBJJGDM(BJJGDM);
		entity.setBJJGMS(BJJGMS);
		entity.setZFHTHYY(ZFHTHYY);
		entity.setZJGZMC(ZJGZMC);
		entity.setZJBH(ZJBH);
		entity.setZJYXQX(ZJYXQX);
		entity.setFZGZDW(FZGZDW);
		entity.setSFJE(SFJE);
		entity.setJEDWDM(JEDWDM);
		entity.setBJSJ(BJSJ);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setPDH(PDH);
		entity.setLXWH(LXWH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 特别程序申请5
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param XH 序号
	 * @param TBCXZL 特别程序种类
	 * @param TBCXKSRQ 特别程序开始日期
	 * @param TBCXPZR 特别程序批准人
	 * @param TBCXQDLY 特别程序启动理由或依据
	 * @param SQNR 申请内容
	 * @param TBCXSX 特别程序时限
	 * @param TBCXSXDW 特别程序时限单位
	 * @param XZQHDM 特别程序申请部门所在地行政区划代码
	 * @param SJBBH 数据版本号
	 * @param YWLSH 业务流水号
	 * @param TBCXZLMC 特别程序种类名称
	 * @param TBCXPZRDH 特别程序批准人电话
	 * @param TBCXPZRSJ 特别程序批准人手机
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int blockOB(String SBLSH, String SXBM, String XH, String TBCXZL, Date TBCXKSRQ, String TBCXPZR, String TBCXQDLY, String SQNR, int TBCXSX, String TBCXSXDW, String XZQHDM, int SJBBH, String YWLSH, String TBCXZLMC, String TBCXPZRDH, String TBCXPZRSJ, String PDH, String BZ,
			String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		BlockOB entity = new BlockOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setXH(XH);
		entity.setTBCXZL(TBCXZLMC);
		entity.setTBCXKSRQ(TBCXKSRQ);
		entity.setTBCXPZR(TBCXPZRSJ);
		entity.setTBCXQDLYHYJ(TBCXQDLY);
		entity.setSQNR(SQNR);
		entity.setTBCXSX(TBCXSX);
		entity.setTBCXSXDW(TBCXSXDW);
		entity.setXZQHDM(XZQHDM);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setTBCXZLMC(TBCXZLMC);
		entity.setTBCXPZRDH(TBCXPZRDH);
		entity.setTBCXPZRSJ(TBCXPZRSJ);
		entity.setPDH(PDH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 特别程序结果6
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param XH 序号
	 * @param TBCXJG 特别程序结果
	 * @param JGCSRQ 结果产生日期
	 * @param TBCXJSRQ 特别程序结束日期
	 * @param TBCXSFJE 特别程序收费金额
	 * @param JEDWDM 金额单位代码
	 * @param XZQHDM 特别程序结果部门所在地行政区划代码
	 * @param SJBBH 数据版本号
	 * @param YWLSH 业务流水号
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int resumeOB(String SBLSH, String SXBM, String XH, String TBCXJG, Date JGCSRQ, Date TBCXJSRQ, String TBCXSFJE, String JEDWDM, String XZQHDM, int SJBBH, String YWLSH, String PDH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		ResumeOB entity = new ResumeOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setXH(XH);
		entity.setTBCXJG(TBCXJG);
		entity.setJGCSRQ(JGCSRQ);
		entity.setTBCXJSRQ(TBCXJSRQ);
		entity.setTBCXSFJE(TBCXSFJE);
		entity.setJEDWDM(JEDWDM);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setPDH(PDH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 补交告知7
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param BZGZFCRXM 补正告知发出人姓名
	 * @param BZGZYY 补正告知原因
	 * @param BZCLQD 补正材料清单
	 * @param BZGZSJ 补正告知时间
	 * @param XZQHDM 补正告知部门所在地行政区划代码
	 * @param SJBBH 数据版本号
	 * @param YWLSH 业务流水号
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int supplyOB(String SBLSH, String SXBM, String BZGZFCRXM, String BZGZYY, String BZCLQD, Date BZGZSJ, String XZQHDM, int SJBBH, String YWLSH, String PDH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		SupplyOB entity = new SupplyOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setBZGZFCRXM(BZGZFCRXM);
		entity.setBZGZYY(BZGZYY);
		entity.setBZCLQD(BZCLQD);
		entity.setBZGZSJ(BZGZSJ);
		entity.setXZQHDM(XZQHDM);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setPDH(PDH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 补交受理8
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param BZSLBLRXM 补正受理办理人姓名
	 * @param BZCLQD 补正材料清单
	 * @param BZSJ 补正时间
	 * @param XZQHDM 补正受理部门所在地行政区划代码
	 * @param BZSLJTDD 补正受理具体地点
	 * @param SJBBH 数据版本号
	 * @param YYWLSH 业务流水号
	 * @param PDH 排队号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int supplyAcceptOB(String SBLSH, String SXBM, String BZSLBLRXM, String BZCLQD, Date BZSJ, String XZQHDM, String BZSLJTDD, int SJBBH, String YWLSH, String PDH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		SupplyAcceptOB entity = new SupplyAcceptOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setBZSLBLRXM(BZSLBLRXM);
		entity.setBZCLQD(BZCLQD);
		entity.setBZSJ(BZSJ);
		entity.setXZQHDM(XZQHDM);
		entity.setBZSLJTDD(BZSLJTDD);
		entity.setSJBBH(SJBBH);
		entity.setYWLSH(YWLSH);
		entity.setPDH(PDH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}

	/**
	 * 领取登记9
	 * @param SBLSH 申办流水号
	 * @param SXBM 事项编码
	 * @param LQRXM 领取人姓名
	 * @param LQRZJLX 领取人证件类型
	 * @param LQRSFZJHM 领取人身份证件号码
	 * @param LQFS 领取方式
	 * @param LQSJ 领取时间
	 * @param XZQHDM 领取登记部门行政区划代码
	 * @param SJBBH 数据版本号
	 * @param BZ 备注
	 * @param BYZDA 备用字段A
	 * @param BYZDB 备用字段B
	 * @param BYZDC 备用字段C
	 * @param BYZDD 备用字段D
	 */
	public static int receiveRegOB(String SBLSH, String SXBM, String LQRXM, String LQRZJLX, String LQRSFZJHM, String LQFS, Date LQSJ, String XZQHDM, int SJBBH, String BZ, String BYZDA, String BYZDB, String BYZDC, Date BYZDD)
	{
		ReceiveRegOB entity = new ReceiveRegOB();
		entity.setSBLSH(SBLSH);
		entity.setSXBM(SXBM);
		entity.setLQRXM(LQRXM);
		entity.setLQRZJLX(LQRZJLX);
		entity.setLQRSFZJHM(LQRSFZJHM);
		entity.setLQFS(LQFS);
		entity.setLQSJ(LQSJ);
		entity.setXZQHDM(XZQHDM);
		entity.setSJBBH(SJBBH);
		entity.setBZ(BZ);
		entity.setBYZDA(BYZDA);
		entity.setBYZDB(BYZDB);
		entity.setBYZDC(BYZDC);
		entity.setBYZDD(BYZDD);
		return saveObject(entity, SBLSH);// 发送对象
	}
//	private static CommonGovXzspDao dao;
//
//	private static void init()
//	{
//		if(dao == null)
//		{
//			dao = (CommonGovXzspDao) dswork.spring.BeanFactory.getBean("commonGovXzspDao");
//		}
//	}
	
	private static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	private static com.google.gson.Gson gson = builder.create();
	
	private static final String GovXzspLxhURL = dswork.core.util.EnvironmentUtil.getToString("gov.xzsp.lxhurl", "");
	private static final String GovXzspSendURL = dswork.core.util.EnvironmentUtil.getToString("gov.xzsp.sendurl", "");
	
	protected static final Logger log = LoggerFactory.getLogger(XzspFactory.class);
	/**
	 * 取得当前最新的申办流水号
	 * @return
	 */
	public static String getLsh() throws Exception
	{
		dswork.http.HttpUtil http = new dswork.http.HttpUtil();
		return http.create(GovXzspLxhURL).connect();
	}

	@SuppressWarnings("all")
	private static int saveObject(Object obj, String SBLSH)
	{
		try
		{
			String v = gson.toJson(obj);
			int i = 0;
			if(obj instanceof ApplicationOB){i = 0;}// ShenBan
			else if(obj instanceof PreAcceptOB){i = 1;}// YuShouLi
			else if(obj instanceof AcceptOB){i = 2;}// ShouLi
			else if(obj instanceof SubmitOB){i = 3;}// ShenPi
			else if(obj instanceof CompleteOB){i = 4;}// BanJie
			else if(obj instanceof BlockOB){i = 5;}// TeBieChengXuQiDong
			else if(obj instanceof ResumeOB){i = 6;}// TeBieChengXuBanJie
			else if(obj instanceof SupplyOB){i = 7;}// BuJiaoGaoZhi
			else if(obj instanceof SupplyAcceptOB){i = 8;}// BuJiaoShouLi
			else if(obj instanceof ReceiveRegOB){i = 9;}// LingQuDengJi
			
			dswork.http.HttpUtil http = new dswork.http.HttpUtil();
			http.create(GovXzspSendURL)
			.addForm("sblsh", SBLSH)
			.addForm("sptype", String.valueOf(i))
			.addForm("spobject", v)
			.setUseCaches(false);
			
			try
			{
				return String.valueOf(http.connect()).trim().equals("1")? 1 : 0;
			}
			catch(Exception e2)
			{
				Thread.sleep(1000);
				log.warn("数据第一次提交失败：" + v);
				try
				{
					return String.valueOf(http.connect()).trim().equals("1")? 1 : 0;
				}
				catch(Exception e3)
				{
					Thread.sleep(1000);
					log.warn("数据第二次提交失败：" + v);
					return String.valueOf(http.connect()).trim().equals("1")? 1 : 0;
				}
			}
		}
		catch(Exception e)
		{
			log.error("数据第三次提交失败，" + e.getMessage());
			return 0;
		}
	}
}
