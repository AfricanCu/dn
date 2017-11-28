package com.ems358.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * @author suming
 */
public final class A {

	/**
	 * @description 获取手机唯一性设备id（imei）
	 * @author BHY
	 * @date 2015-10-21上午11:21:08
	 * @param act
	 * @return String
	 */
	public static String getPhomeIMEI(Activity act) {
		String deviceId = null;
		try {
			TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
			if (null != tm) {
				deviceId = tm.getDeviceId();
				if (null == deviceId || "".equals(deviceId)) {
					Log.d("test", "设备号为空");
					deviceId = UUID.randomUUID().toString();
				}
			}
			Log.d("test", tm.getDeviceId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceId;
	}

	/**
	 * 获取VersionName
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static int getAndroidOSVersion() {
		int osVersion;
		try {
			osVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
			osVersion = 0;
		}

		return osVersion;
	}

	//
	// /**
	// * @author adison 璁剧疆锟狡剧墖鍙傛暟
	// * @param bitmap
	// * @param left
	// * @param top
	// * @param right
	// * @param bottom
	// * @param sourceWidth
	// * @param sourceHeigth
	// * @param screenRadio
	// * @return
	// */
	// public static LayoutParams setImageViewParams(Bitmap bitmap, ImageView
	// imageView, int left, int top, int right, int bottom,
	// float sourceWidth, float sourceHeigth, float screenRadio, boolean
	// fitSource) {
	// float[] cons=getBitmapConfiguration(bitmap, imageView, sourceWidth,
	// sourceHeigth, screenRadio, fitSource);
	// LinearLayout.LayoutParams layoutParams=new
	// LinearLayout.LayoutParams((int)cons[0], (int)cons[1]);
	// layoutParams.gravity=Gravity.CENTER;
	// layoutParams.setMargins(left, top, right, bottom);
	// return layoutParams;
	// }

	/**
	 * 锟斤拷锟皆达拷锟斤拷锟斤拷锟�
	 * 
	 * @param context
	 * @param url
	 */
	public static void openBrowser(Context context, String urlText) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri url = Uri.parse(urlText);
		intent.setData(url);
		context.startActivity(intent);
	}

	/**
	 * 锟叫伙拷全锟斤拷模式
	 * 
	 * @param activity
	 *            Activity
	 * @param isFull
	 * 
	 */
	public static void toggleFullScreen(Activity activity, boolean isFull) {
		hideTitleBar(activity);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 锟斤拷锟斤拷全锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷幕
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setFullScreen(Activity activity) {
		toggleFullScreen(activity, true);
	}

	/**
	 * 锟矫碉拷状态锟斤拷锟侥高讹拷
	 * 
	 * @param activity
	 *            Activity
	 * @return 系统状态锟斤拷锟侥高讹拷
	 */
	public static int getStatusBarHeight(Activity activity) {
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			int dpHeight = Integer.parseInt(field.get(object).toString());
			return activity.getResources().getDimensionPixelSize(dpHeight);
		} catch (Exception e1) {
			e1.printStackTrace();
			return 0;
		}
	}

	/**
	 * 锟斤拷锟截憋拷锟斤拷
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setScreenVertical(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 锟斤拷锟矫猴拷锟斤拷
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void setScreenHorizontal(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void hideSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 锟截憋拷锟斤拷锟斤拷锟�
	 * 
	 * @param context
	 *            锟铰︼拷锟芥枃瀵硅薄锛屼涪锟借埇涓篈ctivity
	 * @param focusingView
	 *            锟斤拷靺ユ硶鎵�鍦ㄧ剑锟酵圭殑View
	 */
	public static void closeSoftInput(Context context, View focusingView) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/**
	 * 锟斤拷准锟斤拷锟斤拷锟�
	 * 
	 * @param activity
	 *            Activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	public interface MessageFilter {

		String filter(String msg);
	}

	public static MessageFilter msgFilter;

	/**
	 * 锟斤拷Activity锟斤拷示Toast
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 * 
	 */
	public static void show(final Activity activity, final String message) {
		final String msg = msgFilter != null ? msgFilter.filter(message) : message;
		activity.runOnUiThread(new Runnable() {

			public void run() {
				// Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
				T.showShort(activity, msg);
			}
		});
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷示toast
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 * 
	 */
	public static void show(final Context context, final String message) {
		final String msg = msgFilter != null ? msgFilter.filter(message) : message;
		Activity a = (Activity) context;
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				T.showShort(context, msg);
			}
		});
	}

	/**
	 * 锟斤拷时锟斤拷锟斤拷示toast
	 * 
	 * @param activity
	 *            Activity
	 * @param message
	 *            娑堟伅鍐呭
	 */
	public static void showL(final Activity activity, final String message) {
		final String msg = msgFilter != null ? msgFilter.filter(message) : message;
		activity.runOnUiThread(new Runnable() {

			public void run() {
				// Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
				T.showShort(activity, msg);
			}
		});
	}

	/**
	 * dp转px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 锟矫碉拷锟斤拷图锟侥高讹拷
	 * 
	 * @param w
	 * @param bmw
	 * @param bmh
	 * @return
	 */
	public static int getViewHeight(int w, int bmw, int bmh) {
		return w * bmh / bmw;
	}

	/**
	 * 锟矫碉拷Activity锟斤拷锟斤拷幕锟斤拷小
	 * 
	 * @param activity
	 * @return
	 */
	public static int[] getScreenSize(Activity activity) {
		int[] screens;
		// if (Constants.screenWidth > 0) {
		// return screens;
		// }
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screens = new int[] { dm.widthPixels, dm.heightPixels };
		return screens;
	}

	// MD5澶夛拷锟斤拷
	public static String Md5(String str) {
		if (str != null && !str.equals("")) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
				byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < md5Byte.length; i++) {
					sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
					sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
				}
				str = sb.toString();
			} catch (NoSuchAlgorithmException e) {

			} catch (Exception e) {
			}
		}
		return str;
	}

	/**
	 * 锟斤拷取锟街伙拷IMEI
	 * 
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 锟叫讹拷锟角凤拷锟斤拷锟斤拷
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Android指锟斤拷锟斤拷apk锟侥硷拷
	 * 
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * byte转十锟斤拷锟斤拷锟斤拷
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 灞匡拷锟斤拷toast
	 * 
	 * @param context
	 * @param str
	 */
	// public static void showToast(Context context, String str) {
	// // Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
	//
	// Toast toast=ToastFactory.getToast(context, str);
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// toast.show();
	// }

	/**
	 * 锟斤拷锟斤拷锟斤拷转byte[]
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] load(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = is.read(buffer)) != -1)
			baos.write(buffer, 0, len);
		baos.close();
		is.close();
		return baos.toByteArray();
	}

	/**
	 * 锟侥硷拷转byte[]
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getFileByte(File file) {
		if (!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			int len = fis.available();
			byte[] bytes = new byte[len];
			fis.read(bytes);
			fis.close();
			return bytes;
		} catch (Exception e) {

		}

		return null;
	}

	/**
	 * 锟斤拷锟斤拷图片锟斤拷小锟矫碉拷锟斤拷锟绞碉拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param value
	 * @return
	 */

	public static int getSimpleNumber(int value) {
		if (value > 30) {
			return 1 + getSimpleNumber(value / 4);
		} else {
			return 1;
		}
	}

	/**
	 * 锟斤拷锟斤拷锟介（String锟斤拷锟斤拷取锟斤拷同元锟斤拷
	 * 锟斤拷锟斤拷思路锟斤拷:1.锟斤拷锟饺斤拷锟斤拷锟斤拷锟斤拷锟斤拷A锟斤拷B锟斤拷锟斤拷(锟斤拷锟斤拷)<br>
	 * 2.锟街憋拷锟紸锟斤拷B锟叫革拷取锟斤拷一元锟斤拷a,b锟斤拷锟斤拷a锟斤拷b锟斤拷锟叫憋拷 锟较ｏ拷<br>
	 * 1) 锟斤拷锟絘锟斤拷b锟斤拷龋锟斤拷锟絘锟斤拷b锟斤拷锟斤拷一指锟斤拷锟斤拷锟斤拷锟斤拷<br>
	 * 2)锟斤拷锟絘小锟斤拷b锟斤拷锟斤拷锟斤拷锟饺锟斤拷锟斤拷一元锟截ｏ拷锟斤拷锟斤拷b锟斤拷 锟斤拷<br>
	 * 3) 锟斤拷锟絘锟斤拷锟斤拷b锟斤拷锟斤拷取B锟斤拷锟斤拷一锟斤拷元锟截ｏ拷锟斤拷a锟斤拷锟叫比斤拷<br>
	 * 3.锟斤拷锟斤拷锟斤拷锟叫诧拷锟斤拷2锟斤拷知锟斤拷A锟斤拷B锟斤拷元锟截讹拷锟饺斤拷锟斤拷<br>
	 * 4.锟斤拷锟截硷拷锟斤拷(锟斤拷锟斤拷锟斤拷同锟斤拷元锟斤拷)<br>
	 * 
	 * @param strArr1
	 * @param strArr2
	 * @return
	 */
	public static List<String> getAllSameElement2(String[] strArr1, String[] strArr2) {
		if (strArr1 == null || strArr2 == null) {
			return null;
		}
		Arrays.sort(strArr1);
		Arrays.sort(strArr2);
		List<String> list = new ArrayList<String>();
		int k = 0;
		int j = 0;
		while (k < strArr1.length && j < strArr2.length) {
			if (strArr1[k].compareTo(strArr2[j]) == 0) {
				if (strArr1[k].equals(strArr2[j])) {
					list.add(strArr1[k]);
					k++;
					j++;
				}
				continue;
			} else if (strArr1[k].compareTo(strArr2[j]) < 0) {
				k++;
			} else {
				j++;
			}
		}
		return list;
	}

	/**
	 * 锟斤拷取锟斤拷前锟斤拷锟疥、锟铰★拷锟斤拷 锟斤拷应锟斤拷时锟斤拷
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getTime() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);
		// System.out.println("锟捷硷拷锟藉寲鍚庯拷袆鏃ワ拷锟斤拷锟较�" + dateNowStr);
		Date d2 = null;
		try {
			d2 = sdf.parse(dateNowStr);
			// System.out.println(d2);
			// System.out.println(d2.getTime());
			return d2.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// I added a generic return type to reduce the casting noise in client code
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
