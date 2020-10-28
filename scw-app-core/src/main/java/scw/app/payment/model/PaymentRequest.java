package scw.app.payment.model;

public class PaymentRequest extends OrderModel {
	private static final long serialVersionUID = 1L;
	// 数据较大，不推荐保存在数据库字段中
	private String applePayReceiptData;// apple pay receipt-data

	public String getApplePayReceiptData() {
		return applePayReceiptData;
	}

	public void setApplePayReceiptData(String applePayReceiptData) {
		this.applePayReceiptData = applePayReceiptData;
	}
}
