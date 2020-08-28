package scw.app.common.service;

import java.util.List;

import scw.app.common.pojo.UserItem;
import scw.result.Result;

public interface UserItemService {
	UserItem getUserItem(long uid, int itemId);

	List<UserItem> getUserItemList(long uid);

	Result use(long uid, int itemId);
}
