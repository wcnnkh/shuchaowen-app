package scw.app.web;

import java.util.Map;

import scw.app.payment.PaymentStatus;
import scw.app.payment.service.OrderService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.logger.Logger;
import scw.logger.LoggerFactory;
import scw.mvc.annotation.Controller;
import scw.mvc.parameter.XmlMap;
import scw.result.BaseResult;
import scw.result.Result;
import scw.tencent.wx.WeiXinPay;

import com.alibaba.fastjson.JSONObject;

@Controller(value = "/payment/weixin", methods = HttpMethod.POST)
public class WeixinPaymentController {
	private static Logger logger = LoggerFactory.getLogger(WeixinPaymentController.class);
	private static final String SUCCESS_TEXT = "SUCCESS";
	private WeiXinPay weixinPay;
	@Autowired
	private OrderService orderService;

	public WeixinPaymentController(WeiXinPay weiXinPay) {
		this.weixinPay = weiXinPay;
	}

	public BaseResult check(Map<String, String> map) {
		if (!SUCCESS_TEXT.equals(map.get("return_code"))) {
			return new BaseResult(false).setMsg(map.get("return_msg"));
		}

		if (!SUCCESS_TEXT.equals(map.get("result_code"))) {
			return new BaseResult(false).setMsg(map.get("err_code") + "(" + map.get("err_code_des") + ")");
		}

		boolean success = weixinPay.checkSign(map);
		if (!success) {
			return new BaseResult(false).setMsg("签名错误");
		}

		String out_trade_no = map.get("out_trade_no");
		if (StringUtils.isEmpty(out_trade_no)) {
			return new BaseResult(false).setMsg("订单号错误");
		}
		return new BaseResult(true);
	}

	@Controller(value = "success")
	public String success(XmlMap map) {
		logger.info("收到微信支付回调:");
		logger.info(JSONObject.toJSONString(map));
		BaseResult baseResult = check(map);
		if (!baseResult.isError()) {
			logger.error("微信支付回调失败：{}", baseResult.getMsg());
			return baseResult.getMsg();
		}

		String out_trade_no = map.get("out_trade_no");
		Result result = orderService.updateStatus(out_trade_no, PaymentStatus.SUCCESS);
		if(result.isSuccess()){
			return SUCCESS_TEXT;
		}
		return result.toString();
	}
}
