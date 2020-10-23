package scw.app.payment.controller;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import scw.app.payment.PaymentConfig;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.event.PaymentEvent;
import scw.app.payment.pojo.Order;
import scw.app.payment.service.PaymentService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.annotation.Controller;
import scw.mvc.parameter.XmlMap;
import scw.result.BaseResult;
import scw.result.Result;
import scw.tencent.wx.pay.WeiXinPay;

@Controller(value = PaymentConfig.WEIXIN_PREFIX, methods = HttpMethod.POST)
public class WeixinPaymentController {
	private static Logger logger = LoggerFactory.getLogger(WeixinPaymentController.class);
	private static final String SUCCESS_TEXT = "SUCCESS";
	private PaymentConfig paymentConfig;
	@Autowired
	private PaymentService paymentService;

	public WeixinPaymentController(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
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

		Order order = paymentService.getOrder(out_trade_no);
		if (order == null) {
			return new BaseResult(false).setMsg("订单不存在");
		}

		WeiXinPay weiXinPay = paymentConfig.getWeiXinPay(order);
		boolean success = weiXinPay.checkSign(map);
		if (!success) {
			return new BaseResult(false).setMsg("签名错误");
		}
		return new BaseResult(true);
	}

	@Controller(value = PaymentConfig.SUCCESS_CONTROLLER)
	public String success(XmlMap map) {
		logger.info("收到微信支付回调:");
		logger.info(JSONObject.toJSONString(map));
		BaseResult baseResult = check(map);
		if (!baseResult.isError()) {
			logger.error("微信支付回调失败：{}", baseResult.getMsg());
			return baseResult.getMsg();
		}

		String out_trade_no = map.get("out_trade_no");
		Result result = paymentService.publish(new PaymentEvent(out_trade_no, PaymentStatus.SUCCESS));
		if (result.isSuccess()) {
			return SUCCESS_TEXT;
		}
		return result.toString();
	}

	@Controller(value = PaymentConfig.REFUND_CONTROLLER)
	public String refund(XmlMap map) {
		logger.info("收到微信退款回调:");
		logger.info(JSONObject.toJSONString(map));
		BaseResult baseResult = check(map);
		if (!baseResult.isError()) {
			logger.error("微信退款回调失败：{}", baseResult.getMsg());
			return baseResult.getMsg();
		}

		String out_trade_no = map.get("out_trade_no");
		PaymentEvent paymentEvent = new PaymentEvent(out_trade_no, PaymentStatus.REFUND);
		paymentEvent.setRefundOrderId(map.get("out_refund_no"));
		Result result = paymentService.publish(paymentEvent);
		if (result.isSuccess()) {
			return SUCCESS_TEXT;
		}
		return result.toString();
	}
}
