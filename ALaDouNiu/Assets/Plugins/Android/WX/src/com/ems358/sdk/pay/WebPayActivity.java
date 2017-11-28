package com.ems358.sdk.pay;

import com.ems358.R;
import com.ems358.sdk.SCG;
import com.ems358.sdk.EmsApi;
import com.ems358.sdk.util.L;
import com.ems358.sdk.view.WebDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.ImageButton;

public class WebPayActivity extends Activity {
	public static WebDialog $this;
	private String url;
	private WebView web;
	private ImageButton exit;
	private boolean isTopUrl;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_webpay);
		web = (WebView) findViewById(R.id.web);
		exit = (ImageButton) findViewById(R.id.exit);
		// 配置url
		url = getIntent().getStringExtra("url");
		// 配置webview
		WebSettings webSetting = web.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		web.setWebViewClient(new android.webkit.WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				HitTestResult hit = view.getHitTestResult();
				if (hit != null && !isTopUrl) {
					isTopUrl = true;
					WebPayActivity.this.url = url;
				}
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
		web.loadUrl(url);
		// web.loadUrl("http://www.baidu.com");
		// web.loadUrl("file:///android_asset/back.html");
		web.addJavascriptInterface(getHtmlObject(), "pay");
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (web.getUrl().contains("https://ok.yeepay.com/paymobile/")) {
					EmsApi.$this.sdkPay.payResult(SCG.ReqState_wait, SCG.PLATFORM_YB);
					EmsApi.$this.showPayResult("支付完成", getString(R.string.tip_wait));
					EmsApi.$this.payFlag = true;
					finish();
				} else {
					EmsApi.$this.sdkPay.payResult(SCG.ReqState_cancel, SCG.PLATFORM_YB);
					EmsApi.$this.showPayResult("支付取消", getString(R.string.tip_cancel));
					EmsApi.$this.payFlag = true;
					finish();
				}
			}
		});
	}

	private Object getHtmlObject() {
		Object insertObj = new Object() {
			@JavascriptInterface
			public void closeWindow() {
				L.i("关闭");
				EmsApi.$this.sdkPay.payResult(SCG.ReqState_wait, SCG.PLATFORM_YB);
				EmsApi.$this.showPayResult("支付完成", getString(R.string.tip_wait));
				EmsApi.$this.payFlag = true;
				finish();
			}
		};
		return insertObj;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (web.getUrl().contains("https://ok.yeepay.com/paymobile/")) {
				EmsApi.$this.sdkPay.payResult(SCG.ReqState_wait, SCG.PLATFORM_YB);
				EmsApi.$this.showPayResult("支付完成", getString(R.string.tip_wait));
				EmsApi.$this.payFlag = true;
				finish();
			} else {
				web.goBack();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
