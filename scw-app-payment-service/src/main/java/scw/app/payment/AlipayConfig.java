package scw.app.payment;

import java.io.Serializable;

public class AlipayConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	private String publicKey;
	private String charset;
	private String signType;// RSA2

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
}
