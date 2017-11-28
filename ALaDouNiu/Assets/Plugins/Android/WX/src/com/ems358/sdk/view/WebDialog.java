package com.ems358.sdk.view;

import com.ems358.R;
import com.ems358.sdk.SCG;
import com.ems358.sdk.EmsApi;
import com.ems358.sdk.util.L;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.ImageButton;

public class WebDialog extends Dialog {
	public static WebDialog $this;
	private String url;
	private WebView web;
	private ImageButton exit;
	private Context context;
	private boolean isTopUrl;

	protected WebDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WebDialog(Context context, int theme, String url) {
		super(context, theme);
		this.context = context;
		$this = this;
		this.url = url;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_webpay);
		web = (WebView) findViewById(R.id.web);
		exit = (ImageButton) findViewById(R.id.exit);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
					WebDialog.this.url = url;
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
				EmsApi.$this.sdkPay.payResult(SCG.ReqState_ok, SCG.PLATFORM_YB);
				EmsApi.$this.showPayResult("支付完成",context.getString(R.string.tip_wait));
				EmsApi.$this.payFlag = true;
				dismiss();
			}
		});
	}
	private Object getHtmlObject() {
		Object insertObj = new Object() {
			@JavascriptInterface
			public void closeWindow() {
				L.i("关闭");
				EmsApi.$this.sdkPay.payResult(SCG.ReqState_ok, SCG.PLATFORM_YB);
				EmsApi.$this.showPayResult("支付完成", context.getString(R.string.tip_wait));
				EmsApi.$this.payFlag = true;
				dismiss();
			}
		};
		return insertObj;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (web.getUrl().equals(url)) {
				EmsApi.$this.sdkPay.payResult(SCG.ReqState_ok, SCG.PLATFORM_YB);
				EmsApi.$this.showPayResult("支付完成",context.getString(R.string.tip_wait));
				EmsApi.$this.payFlag = true;
				dismiss();
				return true;
			} else {
				web.goBack();
			}
			return super.dispatchKeyEvent(event);
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}
}
