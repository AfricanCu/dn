package com.ems358.sdk;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ems358.R;
import com.ems358.sdk.bean.ALIConstants;
import com.ems358.sdk.bean.Good;
import com.ems358.sdk.bean.WXConstants;
import com.ems358.sdk.bean.YBConstants;
import com.ems358.sdk.login.SdkLogin;
import com.ems358.sdk.pay.OrderCallback;
import com.ems358.sdk.pay.PayActivity;
import com.ems358.sdk.pay.SdkPay;
import com.ems358.sdk.pay.WebPayActivity;
import com.ems358.sdk.share.SdkShare;
import com.ems358.sdk.share.WXShare;
import com.ems358.sdk.update.UpdateActivity;
import com.ems358.sdk.util.A;
import com.ems358.sdk.util.AccessTokenKeeper;
import com.ems358.sdk.util.BitmapUtil;
import com.ems358.sdk.util.HttpUtil;
import com.ems358.sdk.util.L;
import com.ems358.sdk.util.MD5;
import com.ems358.sdk.util.ParseJson;
import com.ems358.sdk.util.Util;
import com.ems358.sdk.view.PayDialog;
import com.ems358.sdk.view.WebDialog;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;



public class EmsApi {
	/**
	 * 支付宝支付所需变量
	 */

	/**
	 * 微信支付所需变量
	 */
	public IWXAPI wxApi;

	/**
	 * 通用变量
	 */
	public static EmsApi $this;
	public SdkShare sdkShare;
	public SdkPay sdkPay;
	public SdkLogin sdkLogin;
	private Good good;
	private String tran_id;
	private String notify_url;
	public Context context;
	private String user;
	private int payShowPay;
	private boolean isGoodInit;
	public boolean payFlag;
	private Bundle bundle;
	/**
	 * View
	 */
	private ProgressDialog payProgressDialog;
	private PayDialog payDialog;

	public interface tranIdBack {

	}

	public EmsApi(Context context) {
		super();
		this.context = context;
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			String msg = appInfo.metaData.getString("data_Name");

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		wxApi = WXAPIFactory.createWXAPI(context, WXConstants.APP_ID);
		// 设置默认支付显示方式
		payShowPay = SCG.Pay_None;
		$this = this;
	}
	
	public EmsApi(Context context,String APP_ID){
		this.context = context;
		WXConstants.APP_ID = APP_ID;
		wxApi = WXAPIFactory.createWXAPI(context, WXConstants.APP_ID);
		// 设置默认支付显示方式
		payShowPay = SCG.Pay_None;
		$this = this;
	}

	// 微信分享
	public void ShareWithWX(WXShare shareWx, SdkShare sdkShare) {
		this.sdkShare = sdkShare;
		if (shareWx.getShareType() == WXShare.SHARE_INVITE) {
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			String url = shareWx.getLink();
			WXWebpageObject localWXWebpageObject = new WXWebpageObject();
			localWXWebpageObject.webpageUrl = url;
			WXMediaMessage localWXMediaMessage = new WXMediaMessage(
					localWXWebpageObject);
			localWXMediaMessage.title = shareWx.getTitle();
			localWXMediaMessage.description = shareWx.getContent();
			localWXMediaMessage.thumbData = BitmapUtil.getBitmapBytes(
					shareWx.getIcon(), false);
			req.transaction = "SHARE_INVITE";
			req.message = localWXMediaMessage;
			
			// 分享到朋友圈;
			//req.scene = SendMessageToWX.Req.WXSceneTimeline;
			// 分享到好友
			req.scene = SendMessageToWX.Req.WXSceneSession;
			// 分享到收藏
			// req.scene = SendMessageToWX.Req.WXSceneFavorite;
			wxApi.registerApp(WXConstants.APP_ID);
			wxApi.sendReq(req);
		} else if (shareWx.getShareType() == WXShare.SHARE_FLAUNT) {
			File file = new File(shareWx.getImg());
			Log.i("test", shareWx.getImg());
			if (file.exists()) {
				// 分享的图片尺寸
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				Bitmap bitmap = BitmapFactory.decodeFile(shareWx.getImg());
				WXImageObject imgObj = new WXImageObject(bitmap);
				WXMediaMessage msg = new WXMediaMessage();
				msg.mediaObject = imgObj;
				Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 133, 75,
						true);
				bitmap.recycle();
				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
				req.transaction = "SHARE_FLAUNT";
				req.message = msg;
				// 分享到朋友圈
				//req.scene = SendMessageToWX.Req.WXSceneTimeline;
				// 分享到好友
			    req.scene = SendMessageToWX.Req.WXSceneSession;
				// 分享到收藏
				// req.scene = SendMessageToWX.Req.WXSceneFavorite;
				wxApi.registerApp(WXConstants.APP_ID);
				wxApi.sendReq(req);
		}
		}else if(shareWx.getShareType() == WXShare.SHARE_FREEND){
//			File file = new File(shareWx.getImg());
//			Log.i("test", shareWx.getImg());
//			if (file.exists()) {
				// 分享的图片尺寸
//				SendMessageToWX.Req req = new SendMessageToWX.Req();
//				Bitmap bitmap = BitmapFactory.decodeFile(shareWx.getImg());
//				WXImageObject imgObj = new WXImageObject(bitmap);
//				String msgtext = shareWx.getContent();
//				WXTextObject text = new WXTextObject(msgtext);
//				WXMediaMessage msg = new WXMediaMessage();
//				Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 113, 70,
//						true);
//				bitmap.recycle();
//				msg.title = msgtext;
//				msg.mediaObject = imgObj;
//				msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
//				req.transaction = "SHARE_FREEND";
//				req.message = msg;	
//				// 分享到朋友圈
//				req.scene = SendMessageToWX.Req.WXSceneTimeline;
//				// 分享到好友
//			    //req.scene = SendMessageToWX.Req.WXSceneSession;
//				// 分享到收藏
//				// req.scene = SendMessageToWX.Req.WXSceneFavorite;
//				wxApi.registerApp(WXConstants.APP_ID);
//				wxApi.sendReq(req);	
				
				
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = shareWx.getLink();
				WXMediaMessage msg = new WXMediaMessage(webpage);
				String msgtext = shareWx.getContent();
				msg.title = msgtext;
				msg.description = msgtext;
				Bitmap thumb = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
				msg.thumbData = Util.bmpToByteArray(thumb, true);
				
				msg.thumbData = BitmapUtil.getBitmapBytes(
						shareWx.getIcon(), false);
//				Bitmap thumb = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.icon);
//				msg.thumbData = Util.bmpToByteArray(thumb, true);
				
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.transaction = "SHARE_FREEND";
				req.message = msg;
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
				wxApi.registerApp(WXConstants.APP_ID);
				wxApi.sendReq(req);
//			 }
		  }
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	
	// 通用支付初始化,必须传订单号已经回调地址
	public void initPay(Good good, SdkPay sdkPay) { 
		// TODO Auto-generated method stub
		this.good = good ;
		this.sdkPay = sdkPay;
		isGoodInit = true;
		payFlag = false;
	}

	public EmsApi initLogin(SdkLogin sdkLogin) {
		this.sdkLogin = sdkLogin;
		return this;
	}

	// 显示支付弹框
	public void initPayDialog(int platform) {
		payDialog = new PayDialog(context, R.style.Theme_Transparent);
		payDialog.setCancelable(false);
	}

	// 选择登陆方式
	public void login(int platform,String state) {
		getLastUser();
		switch (platform) {
		case SCG.PLATFORM_WX:
			if (user != null) {
				WXConstants.token = AccessTokenKeeper
						.readWXAccessToken(context);
				startWXFastLogin(WXConstants.token.getRefresh_token(),state);
			} else {
				startWXLogin(state);
			}
			break;
		default:
			break;
		}
	}

	// 选择支付方式
	public void pay(int platform, String extJson) {
		switch (platform) {
		case SCG.PLATFORM_WX:
			L.i("wxpay");

			if (!wxApi.isWXAppInstalled()) {
				L.i("关闭弹框");
				closePayProgressDialog();
				L.i("显示结果");
				showPayResult("支付失败", "你还没有安装微信，请安装最新微信客户端");
				sdkPay.payResult(SCG.ReqState_faild, SCG.PLATFORM_WX);
				return;
			}

			if (isGoodInit) {
				if (TextUtils.isEmpty(WXConstants.APP_ID)
						|| TextUtils.isEmpty(WXConstants.APP_KEY)) {
					new AlertDialog.Builder(context).setTitle("警告")
							.setMessage("请配置微信支付的相关参数").show();
					return;
				} else {
					wxpay(extJson);
				}
			} else {
				new AlertDialog.Builder(context).setTitle("警告")
						.setMessage("请配置支付商品的相关参数").show();
			}
			break;
		case SCG.PLATFORM_ALI:
			L.i("alipay");
			if (isGoodInit) {
				if (TextUtils.isEmpty(ALIConstants.PARTNER)
						|| TextUtils.isEmpty(ALIConstants.RSA_PRIVATE)
						|| TextUtils.isEmpty(ALIConstants.SELLER)) {
					new AlertDialog.Builder(context).setTitle("警告")
							.setMessage("需要配置PARTNER| RSA_PRIVATE| SELLER")
							.show();
					return;
				} else {
					if (payShowPay == SCG.Pay_Dialog
							|| payShowPay == SCG.Pay_None) {
						alipay(context);
					} else if (payShowPay == SCG.Pay_Activity) {
						alipay(PayActivity.getInstence());
					}

				}
			} else {
				new AlertDialog.Builder(context).setTitle("警告")
						.setMessage("请配置支付商品的相关参数").show();
			}
			break;
		case SCG.PLATFORM_YB:
			L.i("ybpay");
			if (isGoodInit) {
				closePayProgressDialog();
				showWebView();
			}
			break;
		default:
			if (!payFlag) {
				L.i("支付取消");
				sdkPay.payResult(SCG.ReqState_cancel, SCG.PLATFORM_NONE);
			} else {
				L.i("支付完成");
			}
			if (payShowPay == SCG.Pay_Dialog) {
				L.i("关闭弹框");
				closePayDialog();
			} else if (payShowPay == SCG.Pay_Activity) {
				L.i("结束actvity");
				PayActivity.getInstence().finish();
			}
			break;
		}
	}

	// 选择支付方式
	public void showCounter(int way, int fastPayPlatform) {
		payShowPay = way;
		if (way == SCG.Pay_Dialog) {
			showpayDialog(fastPayPlatform);
		} else if (way == SCG.Pay_Activity) {
			Intent intent = new Intent(context, PayActivity.class);
			intent.putExtra("platform", fastPayPlatform);
			context.startActivity(intent);
		} else {
			if (fastPayPlatform != SCG.PLATFORM_NONE) {
				praparePay(fastPayPlatform);
			}
		}

	}

	public void startPayActivity() {
		context.startActivity(new Intent(context, PayActivity.class));
	}

	public void startUpdate() {
		context.startActivity(new Intent(context, UpdateActivity.class));
		// Update update = new Update(context);
		// update.show("http://h5.ems358.com/download/dula0202.apk");
	}

	// 获得app签名，微信必须
	@SuppressLint("DefaultLocale")
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(WXConstants.API_KEY);
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		L.i("app签名" + appSign);
		return appSign;
	}

	/**
	 * 微信支付
	 * 
	 * @author ems
	 * 
	 */

	private void wxpay(String extJson) {
		// 这里关闭对话框
		closePayProgressDialog();
		// 解析数据
		try {
			JSONObject json = new JSONObject(extJson);
			if(null != json && !json.has("retcode") ){
				PayReq req = new PayReq();
				//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
				req.appId			= json.getString("appid");
				req.partnerId		= json.getString("partnerid");
				req.prepayId		= json.getString("prepayid");
				req.nonceStr		= json.getString("noncestr");
				req.timeStamp		= json.getString("timestamp");
				req.packageValue	= json.getString("package");
				req.sign			= json.getString("sign");
				req.extData			= "app data"; // optional
				wxApi.registerApp(WXConstants.APP_ID);
				wxApi.sendReq(req);
			}else{
	        	Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * 支付宝支付
	 */

	private void alipay(final Context context) {
//		// 订单
//		String orderInfo = Tools.getOrderInfo(good, notify_url, tran_id);
//		// 对订单做RSA 签名
//		String sign = SignUtils.sign(orderInfo, ALIConstants.RSA_PRIVATE);
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		// 完整的符合支付宝参数规范的订单信息,使用rsa签名
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ "sign_type=\"RSA\"";
//
//		// 这里关闭对话框
//		closePayProgressDialog();
//
//		Runnable payRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask alipay = new PayTask((Activity) context);
//				// 调用支付接口，获取支付结果
//				String result = alipay.pay(payInfo);
//				PayResult payResult = new PayResult(result);
//				String resultStatus = payResult.getResultStatus();
//				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//				if (TextUtils.equals(resultStatus, "9000")) {
//					sdkPay.payResult(SCG.ReqState_ok, SCG.PLATFORM_ALI);
//					A.show(context, "支付成功");
//					showPayResult("支付成功", context.getString(R.string.tip_ok));
//				} else {
//					// 判断resultStatus 为非“9000”则代表可能支付失败
//					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态），6001表示取消支付
//					if (TextUtils.equals(resultStatus, "8000")) {
//						sdkPay.payResult(SCG.ReqState_wait, SCG.PLATFORM_ALI);
//						A.show(context, "支付结果确认中");
//						showPayResult("支付确认中",
//								context.getString(R.string.tip_faild));
//					} else if (TextUtils.equals(resultStatus, "6001")) {
//						sdkPay.payResult(SCG.ReqState_cancel, SCG.PLATFORM_ALI);
//						A.show(context, "支付取消");
//						showPayResult("取消支付",
//								context.getString(R.string.tip_cancel));
//					} else {
//						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//						sdkPay.payResult(SCG.ReqState_faild, SCG.PLATFORM_ALI);
//						A.show(context, "支付失败,错误码" + resultStatus);
//						showPayResult("支付失败",
//								context.getString(R.string.tip_faild));
//					}
//				}
//				// 支付完成
//				payFlag = true;
//			}
//		};
//		// 必须异步调用
//		Thread payThread = new Thread(payRunnable);
//		payThread.start();

	}

	/**
	 * 微信登陆
	 * 
	 */
	private void startWXLogin(String state) {
		// TODO Auto-generated method stub
		// send oauth request
		final SendAuth.Req req = new SendAuth.Req();
		req.transaction = "Login";
		req.scope = "snsapi_userinfo";
		req.state = state;
		wxApi.sendReq(req);
		L.i("开始尝试登陆");
	}

	private void startWXFastLogin(final String refresh_token,final String state) {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = HttpUtil
						.doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="
								+ WXConstants.APP_ID
								+ "&grant_type=refresh_token&refresh_token="
								+ refresh_token);

				WXConstants.token = ParseJson.getAccessToken(result);
				if (WXConstants.token != null) {
					Log.i("test", WXConstants.token.getRefresh_token());
					String loginResult = HttpUtil
							.doGet("https://api.weixin.qq.com/sns/userinfo?access_token="
									+ WXConstants.token.getAccess_token()
									+ "&openid="
									+ WXConstants.token.getOpenid());
					Log.i("test", "账号信息" + loginResult);
					WXConstants.user = ParseJson.getWXUserINfo(loginResult);
					// if (WXConstants.user != null) {
					// User user = new User(WXConstants.user.getOpenid(),
					// WXConstants.user.getNickname(),
					// WXConstants.user.getHeadimgurl(), Config.WX);
					// iLoginBack.loginResult(Config.ReqState_ok, Config.WX,
					// user);
					// } else {
					// startWXLogin();
					// }
				} else {
					startWXLogin(state);
				}
			}
		}).start();
	}

	/**
	 * 通用方法
	 */

	// 显示支付结果
	public void showPayResult(final String result, final String tip) {
		if (payShowPay == SCG.Pay_None)
			return;

		if (payShowPay == SCG.Pay_Dialog) {
			if (payDialog != null && payDialog.isShowing()) {
				payDialog.payResult.post(new Runnable() {
					public void run() {
						payDialog.payResult.setText(result);
						payDialog.extraText.setText(tip);
					}
				});
			}
		} else if (payShowPay == SCG.Pay_Activity) {
			PayActivity.getInstence().runOnUiThread(new Runnable() {
				public void run() {
					PayActivity.getInstence().payResult.setText(result);
					PayActivity.getInstence().extraText.setText(tip);
				}
			});
		}

	}

	// 显示支付收银台(弹框方式)
	private void showpayDialog(final int fastPayPlatform) {
		if (payShowPay == SCG.Pay_None)
			return;

		Activity activity = (Activity) context;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				initPayDialog(fastPayPlatform);
				payDialog.show();
			}
		});
	}

	// 关闭支付收银台(弹框方式)
	private void closePayDialog() {
		if (payShowPay == SCG.Pay_None)
			return;

		Activity activity = (Activity) context;
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (payDialog != null && payDialog.isShowing()) {
					payDialog.dismiss();
					payDialog = null;
				}
			}
		});
	}

	// 显示获取订单弹框
	private void showPayProgressDialog(final String tip) {
		if (payShowPay == SCG.Pay_Dialog || payShowPay == SCG.Pay_None) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					payProgressDialog = ProgressDialog.show(context, "提示", tip);
				}
			});
		} else if (payShowPay == SCG.Pay_Activity) {
			PayActivity.getInstence().runOnUiThread(new Runnable() {
				public void run() {
					payProgressDialog = ProgressDialog.show(
							PayActivity.getInstence(), "提示", tip);
				}
			});
		}

	}

	// 关闭获取订单弹框
	private void closePayProgressDialog() {
		if (payShowPay == SCG.Pay_Dialog || payShowPay == SCG.Pay_None) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (payProgressDialog != null
							&& payProgressDialog.isShowing()) {
						payProgressDialog.dismiss();
					}
				}
			});
		} else if (payShowPay == SCG.Pay_Activity) {
			PayActivity.getInstence().runOnUiThread(new Runnable() {
				public void run() {
					if (payProgressDialog != null
							&& payProgressDialog.isShowing()) {
						payProgressDialog.dismiss();
					}
				}
			});
		}

	}

	// 显示webView
	private void showWebView() {
		final String param = "?orderId=" + tran_id + "&callback_url="
				+ YBConstants.CallbackUrl + "&notify_url=" + notify_url
				+ "&uid=" + good.getUid() + "&amount=" + good.getGoodPrice()
				+ "&goodsName=" + good.getGoodName();
		L.i("支付的url" + YBConstants.PayUrl + param);
		if (payShowPay == SCG.Pay_Dialog || payShowPay == SCG.Pay_None) {
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					WebDialog webDialog = new WebDialog(context,
							R.style.Theme_Transparent, YBConstants.PayUrl
									+ param);
					webDialog.setCancelable(false);
					webDialog.show();
				}
			});
		} else if (payShowPay == SCG.Pay_Activity) {
			Intent intent = new Intent(PayActivity.getInstence(),
					WebPayActivity.class);
			intent.putExtra("url", YBConstants.PayUrl + param);
			PayActivity.getInstence().startActivity(intent);
		}

	}

	private void getLastUser() {
		// user = AccessTokenKeeper.readLastLoginUser(context);
		// if (!user.getNickname().equals("") &&
		// HttpUtil.isNetworkAvailable(context)) {
		// // 加载用户信息
		// switch (user.getPlatform()) {
		// case Config.WX:
		// WXConstants.token = AccessTokenKeeper.readWXAccessToken(context);
		// break;
		// case Config.QQ:
		//
		// break;
		// case Config.WB:
		//
		// break;
		// default:
		// break;
		// }
		// } else {
		// user = null;
		// L.i("没有用户数据");
		// }
	}

	public void praparePay(final int platform) {
		// L.i("支付平台" + platform + "准备获取订单");
		if (platform != SCG.PLATFORM_NONE) {
			if (platform == SCG.PLATFORM_WX) {
				if (!wxApi.isWXAppInstalled()) {
					A.show(context, "请安装微信");
					return;
				}
			}
			showPayProgressDialog("正在获取订单中，请稍候");
			sdkPay.getOrder(platform, new OrderCallback() {

				@Override
				public void onPayComlete(int code, int platform) {

				}

				@Override
				public void onGetOrder(int platform, String extJson) {
					L.i("获取订单成功  " + "平台" + platform + "平台信息" + extJson);
					HashMap<String, Object> map = ParseJson
							.parseOrderInfo(extJson);
					if (platform != SCG.PLATFORM_WX) {
						EmsApi.this.notify_url = (String) map.get("notify_url");
						EmsApi.this.tran_id = (String) map.get("tran_id");
					}
					pay(platform, extJson);
				}
			});
		} else {
			pay(platform, "");
		}

	}
}
