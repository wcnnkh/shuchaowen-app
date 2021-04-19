package scw.app.payment.adapter;

import java.io.IOException;
import java.util.Map;

import scw.app.payment.PaymentConfig;
import scw.app.payment.PaymentMethod;
import scw.app.payment.PaymentServiceAdapter;
import scw.app.payment.PaymentStatus;
import scw.app.payment.PaymentStatusEvent;
import scw.app.payment.dto.PaymentResponse;
import scw.app.payment.dto.UnifiedPaymentRequest;
import scw.beans.annotation.Autowired;
import scw.context.result.BaseResult;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.json.JSONUtils;
import scw.lang.NotSupportedException;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mapper.Copy;
import scw.mvc.HttpChannel;
import scw.mvc.parameter.XmlMap;
import scw.tencent.wx.pay.RefundRequest;
import scw.tencent.wx.pay.Unifiedorder;
import scw.tencent.wx.pay.UnifiedorderRequest;
import scw.tencent.wx.pay.WeiXinPay;
import scw.tencent.wx.pay.WeiXinPayResponse;

public class WeixinPaymentAdapter implements PaymentServiceAdapter{
	private static Logger logger = LoggerFactory.getLogger(WeixinPaymentAdapter.class);
	private static final String SUCCESS_TEXT = "SUCCESS";
	@Autowired
	private WeiXinPay weiXinPay;
	@Autowired
	private PaymentConfig paymentConfig;
	@Autowired
	private ResultFactory resultFactory;
	
	@Override
	public DataResult<PaymentResponse> payment(UnifiedPaymentRequest request)
			throws NotSupportedException {
		String type = "JSAPI";
		if(request.getPaymentMethod().equals(PaymentMethod.WX_APP)){
			type = "APP";
		}
		UnifiedorderRequest unifiedorderRequest = new UnifiedorderRequest(request.getSubject(), request.getOrderId(),
				request.getPrice(), request.getClientIp(), paymentConfig.getPaymentCallbackUrl(request.getPaymentMethod(), PaymentStatus.SUCCESS), type);
		unifiedorderRequest.setOpenid(request.getWx_openid());
		Unifiedorder unifiedorder = weiXinPay.payment(unifiedorderRequest);
		PaymentResponse response = new PaymentResponse();
		Copy.copy(response, request);
		response.setCredential(unifiedorder);
		return resultFactory.success(response);
	}

	@Override
	public Result refund(scw.app.payment.dto.RefundRequest request) throws NotSupportedException {
		RefundRequest refundRequest = new RefundRequest();
		refundRequest.setNotify_url(paymentConfig.getPaymentCallbackUrl(request.getPaymentMethod(), PaymentStatus.REFUND));
		refundRequest.setOut_trade_no(request.getOrderId());
		refundRequest.setOut_refund_no(request.getOrderId());
		refundRequest.setRefund_desc(request.getSubject());
		refundRequest.setRefund_fee(request.getPrice());
		refundRequest.setTotal_fee(request.getPrice());

		WeiXinPayResponse response = weiXinPay.refund(refundRequest);
		if (response.isSuccess()) {
			return resultFactory.error("申请退款异常(" + response.getReturnMsg() + ")");
		}
		return resultFactory.success();
	}

	@Override
	public boolean isAccept(String paymentMethod) {
		return PaymentMethod.WX_WEB.equals(paymentMethod) || PaymentMethod.WX_APP.equals(paymentMethod);
	}
	
	private void writeStatus(HttpChannel channel, String status){
		try {
			channel.getResponse().getWriter().write(status);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PaymentStatusEvent callback(String paymentMethod,
			String paymentStatus, HttpChannel httpChannel)
			throws NotSupportedException {
		logger.info("收到微信支付回调:");
		XmlMap map = httpChannel.getInstanceFactory().getInstance(XmlMap.class);
		logger.info(JSONUtils.getJsonSupport().toJSONString(map));
		BaseResult baseResult = check(map);
		if (!baseResult.isError()) {
			logger.error("微信支付回调失败：{}", baseResult.getMsg());
			writeStatus(httpChannel, baseResult.getMsg());
			return null;
		}

		String out_trade_no = map.get("out_trade_no");
		writeStatus(httpChannel, SUCCESS_TEXT);
		return new PaymentStatusEvent(System.currentTimeMillis(), paymentStatus, out_trade_no, null, paymentMethod, 0, map);
	}

	
	public BaseResult check(Map<String, String> map) {
		if (!SUCCESS_TEXT.equals(map.get("return_code"))) {
			return new BaseResult(false).setMsg(map.get("return_msg"));
		}

		if (!SUCCESS_TEXT.equals(map.get("result_code"))) {
			return new BaseResult(false).setMsg(map.get("err_code") + "(" + map.get("err_code_des") + ")");
		}

		String out_trade_no = map.get("out_trade_no");
		if (StringUtils.isEmpty(out_trade_no)) {
			return new BaseResult(false).setMsg("订单号错误");
		}

		boolean success = weiXinPay.checkSign(map);
		if (!success) {
			return new BaseResult(false).setMsg("签名错误");
		}
		return new BaseResult(true);
	}
}
