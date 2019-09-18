package dswork.common.util;

import dswork.core.util.EnvironmentUtil;

public class CodeUtil
{
	public static final String publicKey = EnvironmentUtil.getToString("sso.key.public", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmABR8u7uvytZd8vJpMd2uK4cEwt+SFJhU7dtPk71Tj4M8WDR6ZvdzHe02cp+Sm6x6QR5WxjgSwqcCKru3vsEITzJs3DZD3JRXmfFCNJG9C52pIzSIEFnwlLSu7vNgKoxWBn+ufZG3mjbSLdFKfwLfRR2KouWy2EEfVVDvEgmSFsUaYLXZ8GfsGr9kAvf5cVsZ7cxpywsdXLFp6vardbTgzMUA8+SIfoH60XAT36CQa2JF929h4+2MFmuqTpPHz0NoJ1dCy+s+czHr+z6wZ3Labfjg2elK15BKB+uLr2SFLeUm8liLNPvOgEVOEso3ubY1WNP7XXKZt+zYn5EE4VRYwIDAQAB");
	public static final String privateKey = EnvironmentUtil.getToString("sso.key.private", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCYAFHy7u6/K1l3y8mkx3a4rhwTC35IUmFTt20+TvVOPgzxYNHpm93Md7TZyn5KbrHpBHlbGOBLCpwIqu7e+wQhPMmzcNkPclFeZ8UI0kb0LnakjNIgQWfCUtK7u82AqjFYGf659kbeaNtIt0Up/At9FHYqi5bLYQR9VUO8SCZIWxRpgtdnwZ+wav2QC9/lxWxntzGnLCx1csWnq9qt1tODMxQDz5Ih+gfrRcBPfoJBrYkX3b2Hj7YwWa6pOk8fPQ2gnV0LL6z5zMev7PrBnctpt+ODZ6UrXkEoH64uvZIUt5SbyWIs0+86ARU4Syje5tjVY0/tdcpm37NifkQThVFjAgMBAAECggEAXe/ktVQAbFioBCcHSTSUJHJFm2eA9cUjrQ8xDk7RvzcN3KbPU6YmHtTkCazg+7MJdE51iV0IUJgP8jNhSKCs3rWomaLmImut6cALvvXlL8b/P5Zfzdtd+nQhFDBuQlPsBtyZ1edjyNPYoUnSDq5bk0Nv+78kxDADDEoDgHq+Xfwn60+YvPDh2vDknrOjbpqiJYz5ExVQUwZrufjtqcI2r80RmD/W4rvd7FU4svvDgHfCKaNADdDlRSVgVk1mmquHp/UYwl0JrEpUeFIUORSADsmYiYFu383Vqa6HWPgsdAfHeRLjVf5JFDQBmINJCkLzyLNH+8C9gWNsv8erd4EVYQKBgQDKj4db7LpIfpimDY7AwiRRZFq4YuTHPISW6q521BnKmYI+5pD2kNC5Z/djlpJ8dFXRuRuFGrB5a7lQMJIA6q8MPsma4S1wQogWhzhohVRKbsfvOfWDokyPJjpvl7oEFeZJyiRTNGQWh3TLrDMhKiDEnoZnP3786+Gj8XUXQdl76wKBgQDAGiCsQpmfDJaQTBdlVcby8Fqst3bkiAKOQPlY3GGY3B2Qhs2s4c70bewepurx8OuKTruv0Owcrx6SJrSJXco6Cz3nQdoDHQ50sPnEPMNPBq7ucIuzuY6OrYndxeDe1eVh7AMoeaJHWBefHP8AyBO/REGK2VG/zTsuLX2OXWr6aQKBgH1M49J9UY5eoVa2tEOQ1J50kZnVsud8DBi++0UOVxX4d5UpUyry0eqe4W19SwgpfpzJALkJlXjGpk9wfCCNC9NrU5K3EECNJ1moM8CUGVwhwxO8qp7O2sUCRMua87prQZULsgS2N+OnzxveXZJ6WOLHnSRpJsiVsXKUfnXwifQZAoGBAJKK2+XMoHfuGGVXRnmtJ6lnpSQgiHfpMVnnQs+AvornTz5TN07sqv1XZUo0twF9cZD+YrrKO3PeOwnusMYhYAVLUhu55l99qOtnngEPAUalqIiIHmop4RACXitpqhMTRHqgmgAkWaDnGI3uhrDH8Bh2LzGGepBANScS2LbjmJoRAoGBAJjO7Jjy3elcZgxJq4aeP7C2XPNFItIJKdSwMFGpgKAT52PmMAiYWY8jOxpQZbnWr+4An94BSHzEi7FzYgMnZZKZOeB8cCrUMGDNlIoLWlCYmopdy8YjKNF50mvmsb5jcAyCwmshymYXLWdhmu/OQ4XDH29hLEXpyo+zehe/4HbU");
	/**
	 * 1: 成功
	 */
	public static String CODE_001 = "{\"code\":1}";
	/**
	 * 400: 请求失败或参数不正确
	 */
	public static String CODE_400 = "{\"code\":400}";
	/**
	 * 400: appid参数不正确
	 */
	public static String CODE_400_APPID = "{\"code\":400,\"msg\":\"appid\"}";
	/// **
	// * 400: code参数不正确
	// */
	// public static String CODE_400_CODE = "{\"code\":400,\"msg\":\"code\"}";
	/**
	 * 400: redirect_uri参数不正确
	 */
	public static String CODE_400_REDIRECT_URI = "{\"code\":400,\"msg\":\"redirect_uri\"}";
	/**
	 * 400: access_key参数不正确
	 */
	public static String CODE_400_AUTHCODE = "{\"code\":400,\"msg\":\"authcode\"}";
	/**
	 * 400: response_type参数不正确
	 */
	public static String CODE_400_RESPONSE_TYPE = "{\"code\":400,\"msg\":\"response_type\"}";
	/**
	 * 400: response_type参数不正确
	 */
	public static String CODE_400_GRANT_TYPE = "{\"code\":400,\"msg\":\"grant_type\"}";
	/**
	 * 401：用户未激活
	 */
	public static String CODE_401 = "{\"code\":401}";
	/**
	 * 402：密码不正确
	 */
	public static String CODE_402 = "{\"code\":402}";
	/**
	 * 403：用户被禁用
	 */
	public static String CODE_403 = "{\"code\":403}";
	/**
	 * 404：用户不存在
	 */
	public static String CODE_404 = "{\"code\":404}";
	/**
	 * 406：验证码无效，access_token无效， CODE无效
	 */
	public static String CODE_406 = "{\"code\":406}";
	/**
	 * 407：APP权限不足
	 */
	public static String CODE_407 = "{\"code\":407}";
	/**
	 * 410：用户已存在
	 */
	public static String CODE_410 = "{\"code\":410}";
	/**
	 * 410：账号已存在
	 */
	public static String CODE_410_ACCOUNT = "{\"code\":410,\"msg\":\"account already exists\"}";
	/**
	 * 410：手机号已存在
	 */
	public static String CODE_410_MOBILE = "{\"code\":410,\"msg\":\"mobile already exists\"}";
	/**
	 * 410：身份证件号已存在
	 */
	public static String CODE_410_CARDNUM = "{\"code\":410,\"msg\":\"cardnum already exists\"}";
	/**
	 * 423：用户被锁定
	 */
	public static String CODE_423 = "{\"code\":423}";
}
