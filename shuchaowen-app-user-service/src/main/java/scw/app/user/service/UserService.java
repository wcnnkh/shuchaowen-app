package scw.app.user.service;

import scw.app.user.pojo.UnionId;
import scw.app.user.pojo.User;
import scw.app.user.pojo.enums.UnionIdType;

public interface UserService {
	User getUser(long uid);
	
	UnionId getUnionId(UnionIdType unionIdType, String unionId);

	UnionId getUnionId(String unionIdType, String unionId);
	
	User getUser(UnionIdType unionIdType, String unionId);
	
	User getUser(String unionIdType, String unionId);
}
