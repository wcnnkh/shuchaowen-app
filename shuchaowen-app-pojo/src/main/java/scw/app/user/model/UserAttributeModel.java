package scw.app.user.model;

import java.io.Serializable;
import java.sql.Date;

import scw.app.common.enums.SexType;

public class UserAttributeModel implements Serializable{
	private static final long serialVersionUID = 1L;
	private Date birthday;
	private SexType sex;
	private int age;
	private String nickname;
	private String headImg;
	
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
}
