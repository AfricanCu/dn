package com.ems358.sdk.pay;

import com.ems358.R;
import com.ems358.sdk.SCG;
import com.ems358.sdk.EmsApi;
import com.ems358.sdk.util.L;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PayActivity extends Activity {
	Button payBtn;
	ImageButton backBtn;
	Button backGame;
	public TextView payResult;
	public TextView extraText;
	RadioGroup payGroup;
	int platform;
	private static PayActivity $this;

	public static PayActivity getInstence() {
		return $this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_pay);
		$this = this;
		payBtn = (Button) findViewById(R.id.pay);
		backGame = (Button) findViewById(R.id.backGame);
		payResult = (TextView) findViewById(R.id.payResult);
		extraText = (TextView) findViewById(R.id.extra);
		payGroup = (RadioGroup) findViewById(R.id.payGroup);
		backBtn = (ImageButton) findViewById(R.id.back);
		platform = SCG.PLATFORM_WX;
		payGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.radio0) {
					platform = SCG.PLATFORM_WX;
					L.d("wx");
				} else if (checkedId == R.id.radio1) {
					platform = SCG.PLATFORM_ALI;
					L.d("al");
				} else if (checkedId == R.id.radio2) {
					platform = SCG.PLATFORM_YB;
					L.d("yb");
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
		platform = getIntent().getIntExtra("platform", 0);
		// 安装了微信的用户直接使用微信支付
		if(platform!=SCG.PLATFORM_NONE){
			L.i("快速支付");
			EmsApi.$this.praparePay(platform);
			findViewById(R.id.counterLayout).setVisibility(View.GONE);
			findViewById(R.id.cashierLayout).setVisibility(View.VISIBLE);
			payResult.setText("正在支付中...");
			extraText.setText("支付过程中可能有延迟，请耐心等候哦！");
			
		} else {
			//设置默认值
			platform = SCG.PLATFORM_WX;
		}
//		if (EmsApi.$this.wxApi.isWXAppInstalled()) {
//			EmsApi.$this.praparePay(platform);
//			findViewById(R.id.counterLayout).setVisibility(View.GONE);
//			findViewById(R.id.cashierLayout).setVisibility(View.VISIBLE);
//			payResult.setText("正在支付中...");
//			extraText.setText("支付过程中可能有延迟，请耐心等候哦！");
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			EmsApi.$this.praparePay(SCG.PLATFORM_NONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
