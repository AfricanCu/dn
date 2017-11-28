package com.games.cc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 获取网络信号强度
 * 
 * @author ems
 *
 */
public class NetWorkStrength{
	// 没有网络连接
	public static final int NETWORN_NONE = 0;
	// wifi连接
	public static final int NETWORN_WIFI = 1;
	// 手机网络数据连接类型
	public static final int NETWORN_2G = 2;
	public static final int NETWORN_3G = 3;
	public static final int NETWORN_4G = 4;
	public static final int NETWORN_MOBILE = 5;

	static String ssid;         // wifi名称
	static int signalLevel = 0; // wifi信号强度
	static int speed = 0;       // wifi速度
	static String units;        // wifi速度单位

	/**
	 * 监测wifi信号强度
	 * 
	 * @return signalLevel ==> wifi信号强度
	 */
	public static Map<String, Object> getWifiStrength(Context mContext) {

		Map<String, Object> map = new HashMap<String, Object>();

		WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (null != wifiInfo.getBSSID()) {
			// wifi名称
			ssid = wifiInfo.getSSID();
			// wifi信号强度
			signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
			// wifi速度
			speed = wifiInfo.getLinkSpeed();
			// wifi速度单位
			units = WifiInfo.LINK_SPEED_UNITS;
			Log.i("wifi信号：", "ssid=" + ssid + ",signalLevel=" + signalLevel + ",speed=" + speed + ",units=" + units);
		}
		map.put("signalLevel", signalLevel);
		map.put("speed", speed);
		return map;
	}
	
	/**
	 * 获取网络类型
	 * @param mContext2 
	 * 
	 * @return 网络类型
	 */
	public static int GetNetworkType(Context mContext2) {
		// 获取系统的网络服务
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext2
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 如果当前没有网络
		if (null == connectivityManager) {
			return NETWORN_NONE;
		}
		// 获取当前网络类型，如果为空，返回无网络
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return NETWORN_NONE;
		}
		// 判断是不是连接的是不是wifi
		NetworkInfo whetherWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (null != whetherWifi) {
			NetworkInfo.State state = whetherWifi.getState();
			if (null != state) {
				if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
					return NETWORN_WIFI;
				}
			}
		}

		// 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
		NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (null != networkInfo2) {
			NetworkInfo.State state = networkInfo2.getState();
			String strSubTypeName = networkInfo2.getSubtypeName();
			if (null != state) {
				if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
					switch (networkInfo.getSubtype()) {
					// 如果是2G类型
					case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
					case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
					case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						return NETWORN_2G;
					// 如果是3G类型
					case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						return NETWORN_3G;
					// 如果是4g类型
					case TelephonyManager.NETWORK_TYPE_LTE:
						return NETWORN_4G;
					default:
						// 中国移动 联通 电信 三种3G制式
						if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA")
								|| strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							return NETWORN_3G;
						} else {
							return NETWORN_MOBILE;
						}
					}
				}
			}
		}
		return NETWORN_NONE;
	}
	
	
	/**
	 * 获取手机信号强度
	 */
	
	@SuppressLint("NewApi")
	public static int getMobileDbm(Context context){
		int dbm = -1;
		TelephonyManager tm =  (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
		List<CellInfo> cellInfoList = null;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
			try {
				cellInfoList = tm.getAllCellInfo();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(null != cellInfoList)
			{
				for(CellInfo cellInfo : cellInfoList)
				{
					if(cellInfo instanceof CellInfoGsm)
					{
						CellSignalStrengthGsm cellSignalStrengthGsm = ((CellInfoGsm) cellInfo).getCellSignalStrength();
						dbm = cellSignalStrengthGsm.getDbm();
					}
					else if(cellInfo instanceof CellInfoCdma)
					{
						CellSignalStrengthCdma cellSignalStrengthCdma = ((CellInfoCdma) cellInfo).getCellSignalStrength();
						dbm = cellSignalStrengthCdma.getDbm();
					}
					else if(cellInfo instanceof CellInfoWcdma)
					{
					     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
	                     {
	                         CellSignalStrengthWcdma cellSignalStrengthWcdma =
	                                ((CellInfoWcdma)cellInfo).getCellSignalStrength();
	                          dbm = cellSignalStrengthWcdma.getDbm();
	                    }
					}
					else if(cellInfo instanceof CellInfoLte)
					{
						CellSignalStrengthLte cellSignalStrengthLte = ((CellInfoLte)cellInfo).getCellSignalStrength();
                        dbm = cellSignalStrengthLte.getDbm();
					}
				}
			}
		}
		return dbm;
	}
}
