package com.ems358.sdk.bean;

public class User {
	String userid;
	String nickname;
	String headimgurl;
	int platform;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public User(String userid, String nickname, String headimgurl, int platform) {
		super();
		this.userid = userid;
		this.nickname = nickname;
		this.headimgurl = headimgurl;
		this.platform = platform;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

}
