package com.ems358.sdk.pay;

public abstract class SdkPay {

	public abstract void getOrder(final int palatform, final OrderCallback callback);

	public abstract void payResult(int code, int platform);

}