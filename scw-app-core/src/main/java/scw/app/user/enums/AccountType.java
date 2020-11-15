package scw.app.user.enums;

import scw.app.user.pojo.User;
import scw.mapper.Field;
import scw.mapper.FilterFeature;
import scw.mapper.MapperUtils;

public enum AccountType {
	USERNAME("username"), 
	PHONE("phone"),
	EMAIL("email"), 
	WX_OPENID("openidForWX"),
	WX_XCX_OPENID("openidForWXCXC"),
	WX_UNIONID("unionidForWX"),
	QQ_OPENID("openidForQQ");
	
	private final String fieldName;
	
	AccountType(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public Field getField(){
		return MapperUtils.getMapper().getFields(User.class, FilterFeature.EXISTING_GETTER_FIELD).find(fieldName, String.class);
	}

	public String getAccount(User user) {
		return (String) getField().getGetter().get(user);
	}

	public void setAccount(User user, String account) {
		getField().getSetter().set(user, account);
	}
}
