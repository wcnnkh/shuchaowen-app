package scw.app.payment.adapter;

import java.io.IOException;
import java.util.Map;

import scw.alibaba.pay.TradeStatus;
import scw.app.payment.PaymentConfig;
import scw.app.payment.PaymentMethod;
import scw.app.payment.PaymentServiceAdapter;
import scw.app.payment.PaymentStatus;
import scw.app.payment.PaymentStatusEvent;
import scw.app.payment.dto.PaymentResponse;
import scw.app.payment.dto.RefundRequest;
import scw.app.payment.dto.UnifiedPaymentRequest;
import scw.beans.annotation.Autowired;
import scw.beans.annotation.InitMethod;
import scw.context.annotation.Provider;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.Ordered;
import scw.json.JSONUtils;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mapper.Copy;
import scw.mvc.HttpChannel;
import scw.mvc.MVCUtils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class AlipayAdapter implements PaymentServiceAdapter {
	private static Logger logger = LoggerFactory.getLogger(AlipayAdapter.class);
	private static final String SUCCESS_TEXT = "SUCCESS";
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private PaymentConfig paymentConfig;
	@Autowired
	private ResultFactory resultFactory;

	private AlipayClient alipayClient;

	@InitMethod
	private void init() {
		alipayClient = new DefaultAlipayClient("https://openapi.alipay.com",
				alipayConfig.getAppId(), alipayConfig.getPrivateKey(),
				alipayConfig.getDataType(), alipayConfig.getCharset(),
				alipayConfig.getPublicKey(), alipayConfig.getSignType());
	}

	@Override
	public DataResult<PaymentResponse> payment(UnifiedPaymentRequest request) {
		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setSubject(request.getSubject());
		model.setOutTradeNo(request.getOrderId());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(request.getPriceDescribe());
		model.setProductCode("QUICK_MSECURITY_PAY");
		alipayRequest.setBizModel(model);
		alipayRequest.setNotifyUrl(paymentConfig.getPaymentCallbackUrl(
				request.getPaymentMethod(), PaymentStatus.SUCCESS));
		try {
			AlipayTradeAppPayResponse response = alipayClient
					.sdkExecute(alipayRequest);

			PaymentResponse paymentResponse = new PaymentResponse();
			Copy.copy(paymentResponse, response);
			paymentResponse.setCredential(response.getBody());
			return resultFactory.success(paymentResponse);
		} catch (AlipayApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Result refund(RefundRequest request) {
		AlipayTradeRefundModel model = new AlipayTradeRefundModel();
		model.setOutTradeNo(request.getOrderId());
		model.setRefundAmount(request.getPriceDescribe());
		model.setRefundReason(request.getSubject());
		model.setOutRequestNo(request.getPlatformOrderId());
		AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
		alipayRequest.setBizModel(model);
		alipayRequest.setNotifyUrl(paymentConfig.getPaymentCallbackUrl(
				request.getPaymentMethod(), PaymentStatus.REFUND));
		try {
			AlipayTradeRefundResponse response = alipayClient
					.sdkExecute(alipayRequest);
			if (!response.isSuccess()) {
				return resultFactory.error("退款失败");
			}
			return resultFactory.success();
		} catch (AlipayApiException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isAccept(String paymentMethod) {
		return PaymentMethod.ALI_APP.equals(paymentMethod);
	}
	
	@Override
	public PaymentStatusEvent callback(String paymentMethod,
			String paymentStatus, HttpChannel httpChannel) {
		logger.info("收到支付宝回调-----");
		Map<String, String> params = MVCUtils
				.getRequestParameterAndAppendValues(httpChannel.getRequest(), ",");
		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		// boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String
		// publicKey, String charset, String sign_type)
		logger.info(JSONUtils.getJsonSupport().toJSONString(params));

		String out_trade_no = params.get("out_trade_no");
		boolean flag;
		try {
			flag = AlipaySignature.rsaCheckV1(params,
					alipayConfig.getPublicKey(), alipayConfig.getCharset(),
					alipayConfig.getSignType());
		} catch (AlipayApiException e) {
			logger.error(e, e.getMessage());
			writeStatus(httpChannel, "check error");
			return null;
		}
		
		if (!flag) {
			logger.error("支付验证签名错误");
			writeStatus(httpChannel, "sign error");
			return null;
		}

		String status = params.get("trade_status");
		TradeStatus tradeStatus = TradeStatus.forName(status);
		if (TradeStatus.TRADE_SUCCESS == tradeStatus) {
			return new PaymentStatusEvent(System.currentTimeMillis(), paymentStatus, out_trade_no, null, paymentMethod, 0, params);
		}
		writeStatus(httpChannel, SUCCESS_TEXT);
		return null;
	}

	private void writeStatus(HttpChannel httpChannel, String status){
		try {
			httpChannel.getResponse().getWriter().write(status);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
