package com.wk.server.logic.login;

public class LoginOuterCallImpl {

	private static final LoginOuterCallImpl instance = new LoginOuterCallImpl();

	public static LoginOuterCallImpl getInstance() {
		return instance;
	}
}
