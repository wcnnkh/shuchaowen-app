package scw.app.common.service;

import scw.oauth2.AccessToken;
import scw.result.Result;

public interface WeChatSubscriptionService {
	AccessToken getAccessToken(String appId);

	Result saveOrUpdateWeChatSubscription(String appId, String appSecret);
}
