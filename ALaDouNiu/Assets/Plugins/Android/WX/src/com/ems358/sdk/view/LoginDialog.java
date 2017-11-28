package com.ems358.sdk.view;

import com.ems358.R;
import com.ems358.sdk.SCG;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginDialog extends Dialog {
	private Button qq;
	private Button wx;

	private clickBack iBack;

	public interface clickBack {
		void loginPlatfo(int platform);
	}

	public void setClickBack(clickBack iBack) {
		this.iBack = iBack;
	}

	public LoginDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LoginDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_login);
		qq = (Button) findViewById(R.id.qq);
		wx = (Button) findViewById(R.id.wx);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		qq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iBack.loginPlatfo(SCG.PLATFORM_QQ);
				LoginDialog.this.dismiss();
			}
		});
		wx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iBack.loginPlatfo(SCG.PLATFORM_WX);
				LoginDialog.this.dismiss();
			}
		});
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			dismiss();
			return super.dispatchKeyEvent(event);
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}

}
