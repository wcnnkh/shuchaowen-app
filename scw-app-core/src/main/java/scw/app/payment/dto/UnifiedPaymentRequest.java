package scw.app.payment.dto;

import scw.lang.Nullable;

/**
 * 统一支付请求
 * @author shuchaowen
 *
 */
public class UnifiedPaymentRequest extends PaymentRequest{
	private static final long serialVersionUID = 1L;
	/**
	 * 微信支付需要的用户openid
	 */
	@Nullable
	private String wx_openid;
	
	/**
	 * 发起支付的客户端ip
	 */
	@Nullable
	private String clientIp;

	public String getWx_openid() {
		return wx_openid;
	}

	public void setWx_openid(String wx_openid) {
		this.wx_openid = wx_openid;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
