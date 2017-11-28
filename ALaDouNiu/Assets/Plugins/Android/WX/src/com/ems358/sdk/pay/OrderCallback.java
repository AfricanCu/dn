package com.ems358.sdk.pay;

public interface OrderCallback {
	void onGetOrder(int platform,String extJson);
	
	void onPayComlete(int code ,int platform);
}
