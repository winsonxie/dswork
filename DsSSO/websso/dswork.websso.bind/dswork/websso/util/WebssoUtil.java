package dswork.websso.util;

import dswork.core.util.EnvironmentUtil;

public class WebssoUtil
{
	private WebssoUtil()
	{
	}

	public static final boolean HAS_ALIPAY = EnvironmentUtil.getToBoolean("websso.alipay", false);
	public static final boolean HAS_WECHAT = EnvironmentUtil.getToBoolean("websso.wechat", false);
	public static final boolean HAS_QQ = EnvironmentUtil.getToBoolean("websso.qq", false);
}
