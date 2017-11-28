package com.ems358.sdk.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.ems358.sdk.bean.ALIConstants;
import com.ems358.sdk.bean.Good;

public class Tools {

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	// 得到随机字符串
	public static String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	// 得到当前时间戳
	public static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}


	/**
	 * 支付宝支付提供的方法
	 */
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(Good good, String notify_url, String tran_id) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + ALIConstants.PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + ALIConstants.SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + tran_id + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + good.getGoodName() + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + good.getGoodName() + "\"";
		// 商品金额
		DecimalFormat df = new DecimalFormat("0.00");
		double price = (double)good.getGoodPrice()/100;
		 orderInfo += "&total_fee=" + "\"" + df.format(price) + "\"";
//		orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";
//		L.i("费用"+df.format(price));
		L.i("回调地址"+notify_url);
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_url + "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		return orderInfo;
	}
}
