package com.ems358.sdk.login;

import com.ems358.sdk.bean.User;

public abstract class SdkLogin {

	public abstract void loginResult(int state, int platform, User user,String exStr);
}
