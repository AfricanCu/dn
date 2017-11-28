package com.ems358.sdk.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MyTools {

	private static long lastClickTime;

	public static View getViewByPosition(int pos, ListView listView) {
		// TODO Auto-generated method stub
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	// 计算高度，重设listview高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// TODO Auto-generated method stub
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度

			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static void setheight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int maxHeight = 0;
		int itemNum = listAdapter.getCount();
		for (int i = 0; i < itemNum; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			int thisHeight = listItem.getMeasuredHeight();// 计算子项View的宽高
			maxHeight = (maxHeight > thisHeight) ? (maxHeight) : (thisHeight);
		}
		for (int j = 0; j < itemNum; j++) {
			View listItem = listAdapter.getView(j, null, listView);
			ViewGroup.LayoutParams params = listItem.getLayoutParams();
			params.height = maxHeight;
			listItem.setLayoutParams(params);
		}
	}

	public static boolean isToday(Long timestamp) {
		DateUtil todayDate = new DateUtil(System.currentTimeMillis());
		DateUtil date = new DateUtil(timestamp);
		try {
			if (date.getDay() == todayDate.getDay()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			L.i("时间戳异常" + e.toString());
			return false;
		}
	}

	public static String getSimpleTime(Long timestamp) {
		// Long time = Long.parseLong(timestamp);
		// DateUtil date = new DateUtil(time);
		DateUtil date = new DateUtil(timestamp);
		try {
			L.i("时间戳" + timestamp);
			date.setTimeStringFormat("HH:mm");
			return date.getTimeString();
		} catch (Exception e) {
			L.i("时间戳异常" + e.toString());
			return "00:00";
		}
	}

	public static String getDateTime(Long timestamp, boolean isNewLine) {
		DateUtil date = new DateUtil(timestamp);
		try {
			if (isNewLine) {
				date.setDateTimeStringFormat("yyyy-MM-dd\nHH:mm");
			} else {
				date.setDateTimeStringFormat("yyyy-MM-dd HH:mm");
			}

			return date.getDateTimeString();
		} catch (Exception e) {
			L.i("时间戳异常" + e.toString());
			return "00:00";
		}
	}

	/**
	 * 生成随机字符串
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 防止用户多次点击
	 */
	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 以最省资源的方式访问资源id
	 * 
	 */

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 得到下载的文件名
	 */
	public static String getUrlFileName(String url) {
		String filename = "";
		boolean isok = false;
		// 从UrlConnection中获取文件名称
		try {
			URL myURL = new URL(url);

			URLConnection conn = myURL.openConnection();
			if (conn == null) {
				return null;
			}
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return null;
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return null;
			}

			for (String skey : key) {
				List<String> values = hf.get(skey);
				for (String value : values) {
					String result;
					try {
						result = new String(value.getBytes("ISO-8859-1"), "GBK");
						int location = result.indexOf("filename");
						if (location >= 0) {
							result = result.substring(location + "filename".length());
							filename = result.substring(result.indexOf("=") + 1);
							isok = true;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} // ISO-8859-1 UTF-8 gb2312
				}
				if (isok) {
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}

	/**
	 * 重命名文件
	 */
	public static boolean renameToNewFile(File srcDir, String newName) {
		L.i(srcDir.getParentFile().getAbsolutePath() + "/" + newName);
		boolean isOk = srcDir.renameTo(new File(srcDir.getParentFile().getAbsolutePath() + "/" + newName)); // dest新文件夹路径，通过renameto修改
		System.out.println("renameToNewFile is OK ? :" + isOk);
		return isOk;
	}

	/**
	 * 打开apk并且安装
	 * 
	 * @param context
	 * @param file
	 */
	public static void openFile(Context context, File file) {
		// TODO Auto-generated method stub
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 是否有内存卡
	 */

	public static boolean isHasSdCard() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}