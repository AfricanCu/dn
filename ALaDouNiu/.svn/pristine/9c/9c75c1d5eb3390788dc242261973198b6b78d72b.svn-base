package com.games.cc;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 获取手机电量、信号展示类
 * 
 * @author ems
 *
 */
public class BatterySignalListener {
	// 静态的Activity实例
	// private static BatterySignalListener instance;
	private static Activity batteryActivity;

	public void setActivity(Activity activity) {
		BatterySignalListener.batteryActivity = activity;
	}

	// 获取电量
	static int level; // 1、当前电量
	static int scale; // 2、总电量
	static int status; // 3、是否在充电（2：手机正在充电状态 ----- 4：手机没有在充电状态）
	static int netWorkType; // 4、 网络类型
	static int netWorkMobileStrength;// 5、手机卡运营商网络信号强度
	// 6、wifi网络信号强度,速度
	static Map<String, Object> netWorkWifiStrength = new HashMap<String, Object>();

	// 1、 lua调用获取手机电量
	public String battery_Unity() throws JSONException {
		Log.d("lua调用获取手机电量:", "lua调用了");
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("level", level);            		//1、当前电量
		jsonobj.put("scale", scale);					//2、总电量
		jsonobj.put("status", status);					//3、是否在充电（2：手机正在充电状态 ----- 4：手机没有在充电状态）
		jsonobj.put("netWorkType", netWorkType);		//4、 网络类型(没有网络：0、wifi:1、手机网络数据连接类型：2、3、4、5)
		jsonobj.put("netWorkMobileStrength", netWorkMobileStrength);//5、手机卡运营商网络信号强度
		//jsonobj.put("netWorkWifiStrength", netWorkWifiStrength);//6、wifi网络信号强度,速度 (map第一个参数：signalLevel(网络信号强度),第二个参数：speed(网络速度))
		
		String signalLevel = netWorkWifiStrength.get("signalLevel").toString();//signalLevel(网络信号强度)
		String speed = netWorkWifiStrength.get("speed").toString();//speed(网络速度)
		jsonobj.put("signalLevel",signalLevel);
		jsonobj.put("speed",speed);
		
		final String jsonStr = jsonobj.toString();
		/*((Cocos2dxActivity) batteryActivity).runOnGLThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//Log.d("lua回调:", jsonStr);
				Cocos2dxLuaJavaBridge.callLuaFunctionWithString(classId, jsonStr);
			}
		});*/
		return jsonStr;
	}

	/**
	 * 获取手机电量，判断手机是否在充电。
	 */
	public static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
				int statusInt = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
				String status1 = String.valueOf(statusInt);
				if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
					Log.i("手机是否在充电：", status1);
					// 1、当前电量
					level = intent.getIntExtra("level", 0);
					// 2、总电量
					scale = intent.getIntExtra("scale", 100);
					// 3、是否在充电（2：手机正在充电状态 ----- 4：手机没有在充电状态）
					status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_CHARGING);
					Log.i("手机电池信息", "当前电量:" + level + "-" + "总电量:" + scale + "-" + "手机是否在充电:" + status);
					// 4、网络类型
					netWorkType = NetWorkStrength.GetNetworkType(context);
					Log.i("网络类型:", String.valueOf(netWorkType));
					// 5、监控wifi信号质量
					netWorkWifiStrength = NetWorkStrength.getWifiStrength(context);
					Log.i("wifi信号强度1:", netWorkWifiStrength.toString());
					netWorkMobileStrength = NetWorkStrength.getMobileDbm(context);
				} else {
					Log.i("手机是否在充电：", status1);
					level = intent.getIntExtra("level", 0);
					scale = intent.getIntExtra("scale", 100);
					status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_NOT_CHARGING);
					netWorkType = NetWorkStrength.GetNetworkType(context);
					Log.i("网络类型:", String.valueOf(netWorkType));
					netWorkWifiStrength = NetWorkStrength.getWifiStrength(context);
					Log.i("wifi信号强度2:", netWorkWifiStrength.toString());
					netWorkMobileStrength = NetWorkStrength.getMobileDbm(context);
				}
			}
		}
	};

	// private static Cocos2dxActivity getInstance() {
	// // TODO Auto-generated method stub
	// return instance;
	// }
}
