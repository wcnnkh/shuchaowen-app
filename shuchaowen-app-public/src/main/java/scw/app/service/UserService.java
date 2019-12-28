package scw.app.service;

import scw.app.enums.UnionIdType;
import scw.app.pojo.UnionId;
import scw.app.pojo.User;

public interface UserService {
	User getUser(long uid);
	
	UnionId getUnionId(UnionIdType unionIdType, String unionId);

	UnionId getUnionId(String unionIdType, String unionId);
	
	User getUser(UnionIdType unionIdType, String unionId);
	
	User getUser(String unionIdType, String unionId);
}
