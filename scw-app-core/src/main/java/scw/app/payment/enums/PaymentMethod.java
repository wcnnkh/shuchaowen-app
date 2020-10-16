package scw.app.payment.enums;

public enum PaymentMethod {
	WX_APP(100, "微信APP支付"),
	
	WX_WEB(101, "微信WEB支付"),
	
	ALI_APP(200, "支付宝APP支付"),
	
	APPLE(300, "Apple支付"),
	;

	private final int channel;
	private final String describe;

	private PaymentMethod(int channel, String describe) {
		this.channel = channel;
		this.describe = describe;
	}

	public int getChannel() {
		return channel;
	}

	public String getDescribe() {
		return describe;
	}

	public static PaymentMethod forChannel(int channel) {
		for (PaymentMethod method : values()) {
			if (method.channel == channel) {
				return method;
			}
		}
		return null;
	}
}
