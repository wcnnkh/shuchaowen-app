package scw.app.payment.controller;

import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import scw.alibaba.pay.TradeStatus;
import scw.app.payment.AlipayConfig;
import scw.app.payment.PaymentConfig;
import scw.app.payment.enums.PaymentStatus;
import scw.app.payment.event.PaymentEvent;
import scw.app.payment.pojo.Order;
import scw.app.payment.service.PaymentService;
import scw.beans.annotation.Autowired;
import scw.context.result.Result;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.MVCUtils;
import scw.mvc.annotation.Controller;

@Controller(value = PaymentConfig.ALI_PREFIX, methods = HttpMethod.POST)
public class AliPaymentController {
	private static Logger logger = LoggerFactory.getLogger(AliPaymentController.class);
	private static final String SUCCESS_TEXT = "SUCCESS";

	private PaymentConfig paymentConfig;
	@Autowired
	private PaymentService paymentService;

	public AliPaymentController(PaymentConfig paymentConfig) {
		this.paymentConfig = paymentConfig;
	}

	@Controller(value = PaymentConfig.SUCCESS_CONTROLLER)
	public String succes(ServerHttpRequest request) throws AlipayApiException {
		logger.info("收到支付宝回调-----");
		Map<String, String> params = MVCUtils.getRequestParameterAndAppendValues(request, ",");
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		logger.info(JSONUtils.toJSONString(params));

		String out_trade_no = params.get("out_trade_no");
		Order order = paymentService.getOrder(out_trade_no);
		if (order == null) {
			return "not found order";
		}

		AlipayConfig alipayConfig = paymentConfig.getAlipayConfig(order);
		boolean flag = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(), alipayConfig.getCharset(),
				alipayConfig.getSignType());
		if (!flag) {
			logger.error("微信支付验证签名错误");
			return "sign error";
		}

		String status = params.get("trade_status");

		TradeStatus tradeStatus = TradeStatus.forName(status);
		if (TradeStatus.TRADE_SUCCESS == tradeStatus) {
			Result result = paymentService.publish(new PaymentEvent(out_trade_no, PaymentStatus.SUCCESS));
			if (result.isError()) {
				return result.toString();
			}
		}
		return SUCCESS_TEXT;
	}
}
