package com.ems358.sdk.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ems358.sdk.EmsApi;
import com.ems358.sdk.SCG;
import com.ems358.sdk.bean.WXConstants;
import com.ems358.sdk.util.A;
import com.ems358.sdk.util.L;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	// IWXAPI 鏄涓夋柟app鍜屽井淇￠�氫俊鐨刼penapi鎺ュ彛
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID, false);
		// new intent
		setIntent(getIntent());
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
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		L.i(resp.errCode + "");
		checkWhatReq(resp);
		finish();
	}

	private void checkWhatReq(BaseResp resp) {
		// TODO Auto-generated method stub
		L.i("收到回调");
		if (resp.transaction.equals("SHARE_INVITE")
			|| resp.transaction.equals("SHARE_FLAUNT") || resp.transaction.equals("SHARE_FREEND")) {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				L.i("分享成功");
				EmsApi.$this.sdkShare.shareResult(SCG.ReqState_ok, SCG.PLATFORM_WX);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				L.i("分享取消");
				EmsApi.$this.sdkShare.shareResult(SCG.ReqState_cancel, SCG.PLATFORM_WX);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Log.i("test", "分享错误" + BaseResp.ErrCode.ERR_AUTH_DENIED);
				EmsApi.$this.sdkShare.shareResult(SCG.ReqState_faild, SCG.PLATFORM_WX);
				break;
			default:
				break;
			}
		} else if (resp.transaction.equals("Login")) {
			SendAuth.Resp loginResp = (SendAuth.Resp) resp;// 这里做一下转型就是
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				WXConstants.state = loginResp.state;
				WXConstants.code = loginResp.code;
				EmsApi.$this.sdkLogin.loginResult(SCG.ReqState_ok, SCG.PLATFORM_WX, null, loginResp.code);
				A.show(this, "授权成功");
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				EmsApi.$this.sdkLogin.loginResult(SCG.ReqState_faild, SCG.PLATFORM_WX, null, null);
				A.show(this, "授权取消");
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				EmsApi.$this.sdkLogin.loginResult(SCG.ReqState_faild, SCG.PLATFORM_WX, null, null);
				A.show(this, "授权失败");
				break;
			default:
				break;
			}
		}
	}

	private void goToGetMsg() {
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {

	}
}