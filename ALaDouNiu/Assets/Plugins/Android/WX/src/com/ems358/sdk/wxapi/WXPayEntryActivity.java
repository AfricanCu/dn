package com.ems358.sdk.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ems358.R;
import com.ems358.sdk.EmsApi;
import com.ems358.sdk.SCG;
import com.ems358.sdk.bean.WXConstants;
import com.ems358.sdk.util.A;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
//		L.i("test", "onPayFinish, errCode = " + resp.errCode);
		if (resp.errCode == 0) {
			EmsApi.$this.sdkPay.payResult(SCG.ReqState_ok, SCG.PLATFORM_WX);
			A.show(WXPayEntryActivity.this, "支付成功");
			EmsApi.$this.showPayResult("支付成功",getString(R.string.tip_ok));
		}else if (resp.errCode == -1){
			EmsApi.$this.sdkPay.payResult(SCG.ReqState_faild, SCG.PLATFORM_WX);
			A.show(WXPayEntryActivity.this, "支付失败");
			EmsApi.$this.showPayResult("支付失败",getString(R.string.tip_faild));
		}else {
			EmsApi.$this.sdkPay.payResult(SCG.ReqState_cancel, SCG.PLATFORM_WX);
			A.show(WXPayEntryActivity.this, "支付取消");
			EmsApi.$this.showPayResult("支付取消",getString(R.string.tip_faild));
		}
		EmsApi.$this.payFlag = true;
		finish();
	}
}