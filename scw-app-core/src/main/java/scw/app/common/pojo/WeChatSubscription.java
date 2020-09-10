package scw.app.common.pojo;

import java.io.Serializable;

import scw.mapper.MapperUtils;
import scw.oauth2.AccessToken;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;

/**
 * 微信公众号
 * 
 * @author shuchaowen
 *
 */
@Table
public class WeChatSubscription implements Serializable {
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	private String appId;
	private String appSecret;
	private AccessToken accessToken;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	
	@Override
	public String toString() {
		return MapperUtils.getMapper().toString(this);
	}
}
