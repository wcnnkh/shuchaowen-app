package scw.app.web;

import java.util.Map;

import scw.alibaba.pay.AlipayConfig;
import scw.alibaba.pay.TradeStatus;
import scw.app.payment.PaymentStatus;
import scw.app.payment.service.OrderService;
import scw.beans.annotation.Autowired;
import scw.http.HttpMethod;
import scw.http.server.ServerHttpRequest;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.MVCUtils;
import scw.mvc.annotation.Controller;
import scw.result.Result;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

@Controller(value = "/payment/weixin", methods = HttpMethod.POST)
public class AliPaymentController {
	private static Logger logger = LoggerFactory
			.getLogger(AliPaymentController.class);
	private static final String SUCCESS_TEXT = "SUCCESS";

	private AlipayConfig alipayConfig;
	@Autowired
	private OrderService orderService;

	public AliPaymentController(AlipayConfig alipayConfig) {
		this.alipayConfig = alipayConfig;
	}

	public String succes(ServerHttpRequest request) throws AlipayApiException {
		logger.info("收到支付宝回调-----");
		Map<String, String> params = MVCUtils
				.getRequestParameterAndAppendValues(request, ",");
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		logger.info(JSONUtils.toJSONString(params));

		boolean flag = AlipaySignature.rsaCheckV1(params,
				alipayConfig.getPublicKey(), alipayConfig.getCharset(),
				alipayConfig.getSignType());
		if (!flag) {
			logger.error("微信支付验证签名错误");
			return "sign error";
		}

		String out_trade_no = params.get("out_trade_no");
		String status = params.get("trade_status");

		TradeStatus tradeStatus = TradeStatus.forName(status);
		if (TradeStatus.TRADE_SUCCESS == tradeStatus) {
			Result result = orderService.updateStatus(out_trade_no,
					PaymentStatus.SUCCESS);
			if (result.isError()) {
				return result.toString();
			}
		}
		return SUCCESS_TEXT;
	}
}
