package scw.app.payment;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import scw.core.Assert;
import scw.lang.AlreadyExistsException;

public class PaymentChannelSupports {
	private static final HashSet<PaymentChannel> CHANNELS = new HashSet<PaymentChannel>();

	public static final PaymentChannel ALI_APP = new PaymentChannel("ali_app",
			"支付定APP支付");
	public static final PaymentChannel WX_APP = new PaymentChannel("wx_app",
			"微信APP支付");

	static {
		for (Field field : PaymentChannelSupports.class.getFields()) {
			Object value = null;
			try {
				value = field.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (value == null) {
				continue;
			}

			if (value instanceof PaymentChannel) {
				addSupportChannels((PaymentChannel) value);
			}
		}
	}

	public static synchronized void addSupportChannels(
			PaymentChannel paymentChannel) {
		Assert.requiredArgument(paymentChannel != null, "paymentChannel");
		if (CHANNELS.contains(paymentChannel)) {
			throw new AlreadyExistsException("存在相同的支付渠道:" + paymentChannel);
		}
		CHANNELS.add(paymentChannel);
	}

	public static Collection<PaymentChannel> getSupportChannels() {
		return Collections.unmodifiableCollection(CHANNELS);
	}
}
