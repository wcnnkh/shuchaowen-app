package scw.app.payment;

import java.io.Serializable;

public class PaymentChannel implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String descriptor;

	public PaymentChannel(String name, String descriptor) {
		this.name = name;
		this.descriptor = descriptor;
	}

	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj == this) {
			return true;
		}

		if (obj instanceof PaymentChannel) {
			return getName().equals(((PaymentChannel) obj).getName());
		}
		return false;
	}

	@Override
	public String toString() {
		return "channelName=" + getName() + ", descriptor=" + getDescriptor();
	}
}
