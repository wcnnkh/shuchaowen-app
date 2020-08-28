package scw.app.common.model;

import java.io.Serializable;

public class TastInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String describe;
	private int condition;// 完成条件

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}
}
