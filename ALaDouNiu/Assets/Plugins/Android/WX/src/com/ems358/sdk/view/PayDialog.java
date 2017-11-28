package com.ems358.sdk.view;

import com.ems358.R;
import com.ems358.sdk.SCG;
import com.ems358.sdk.EmsApi;
import com.ems358.sdk.util.L;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PayDialog extends Dialog {
	Button payBtn;
	ImageButton backBtn;
	Button backGame;
	public TextView payResult;
	public TextView extraText;
	int platform;
	RadioGroup payGroup;

	public PayDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PayDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pay);
		payBtn = (Button) findViewById(R.id.pay);
		backGame = (Button) findViewById(R.id.backGame);
		payResult = (TextView) findViewById(R.id.payResult);
		extraText = (TextView) findViewById(R.id.extra);
		payGroup = (RadioGroup) findViewById(R.id.payGroup);
		backBtn = (ImageButton) findViewById(R.id.back);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		
		platform = SCG.PLATFORM_WX;
		payGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				L.i(checkedId + "");
				if (checkedId == R.id.radio0) {
					platform = SCG.PLATFORM_WX;
				} else if (checkedId == R.id.radio1) {
					platform = SCG.PLATFORM_ALI;
				} else if (checkedId == R.id.radio2) {
					platform = SCG.PLATFORM_YB;
				}
			}
		});
		payBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EmsApi.$this.praparePay(platform);
				findViewById(R.id.counterLayout).setVisibility(View.GONE);
				findViewById(R.id.cashierLayout).setVisibility(View.VISIBLE);
				payResult.setText("正在支付中...");
				extraText.setText("支付过程中可能有延迟，请耐心等候哦！");
			}
		});
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EmsApi.$this.praparePay(SCG.PLATFORM_NONE);
			}
		});
		backGame.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EmsApi.$this.praparePay(SCG.PLATFORM_NONE);
			}
		});

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			EmsApi.$this.praparePay(SCG.PLATFORM_NONE);
			return true;
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}

}
