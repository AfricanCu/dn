package com.wk.gm.bean;

import java.util.Date;

public class MinusCreditsBean {
	private String username;
	private String uid;
	private int type;
	private int credits;
	private String log;
	private Date operaTime;

	public MinusCreditsBean() {
		super();
	}

	public MinusCreditsBean(String username, String uid, int type, int credits,
			String log, Date operaTime) {
		super();
		this.username = username;
		this.uid = uid;
		this.type = type;
		this.credits = credits;
		this.log = log;
		this.operaTime = operaTime;
	}

	public String getUsername() {
		return username;
	}

	public String getUid() {
		return uid;
	}

	public String getLog() {
		return log;
	}

	public int getCredits() {
		return credits;
	}

	public Date getOperaTime() {
		return operaTime;
	}

}
