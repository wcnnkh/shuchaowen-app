package scw.app.user.pojo;

import java.util.HashMap;
import java.util.Map;

import scw.aop.support.FieldSetter;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.sql.orm.annotation.Column;
import scw.sql.orm.annotation.PrimaryKey;
import scw.sql.orm.annotation.Table;
import scw.sql.orm.support.generation.annotation.Generator;

@Table
public class User extends UserAttributeModel {
	private static final long serialVersionUID = 1L;
	@Generator
	@PrimaryKey
	private long uid;
	private long cts;
	@Column(length=1000)
	private Map<UnionIdType, String> unionIdMap;
	private String password;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public long getCts() {
		return cts;
	}

	public void setCts(long cts) {
		this.cts = cts;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<UnionIdType, String> getUnionIdMap() {
		return unionIdMap;
	}

	public void setUnionIdMap(Map<UnionIdType, String> unionIdMap) {
		this.unionIdMap = unionIdMap;
	}
	
	@FieldSetter("unionIdMap")
	public void putUnionId(UnionIdType unionIdType, String unionId){
		if(unionIdMap == null){
			unionIdMap = new HashMap<UnionIdType, String>(4);
		}
		unionIdMap.put(unionIdType, unionId);
	}
}
