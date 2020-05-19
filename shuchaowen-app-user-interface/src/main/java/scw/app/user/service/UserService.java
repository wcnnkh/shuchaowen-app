package scw.app.user.service;

import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.pojo.enums.UnionIdType;
import scw.result.DataResult;

public interface UserService {
	User getUser(long uid);
	
	UnionId getUnionId(UnionIdType unionIdType, String unionId);

	User getUser(UnionIdType unionIdType, String unionId);
	
	DataResult<User> register(UnionIdType unionIdType, String unionId, String password);
}
