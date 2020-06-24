package scw.app.user.model;

import java.io.Serializable;
import java.sql.Date;

import scw.app.common.enums.SexType;

public class UserAttributeModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date birthday;
	private SexType sex;
	private int age;
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public SexType getSex() {
		return sex;
	}
	public void setSex(SexType sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
