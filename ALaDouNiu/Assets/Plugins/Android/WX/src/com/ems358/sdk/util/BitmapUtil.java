package com.ems358.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

public class BitmapUtil {
	private final static String TAG = "bitmap";

	public static boolean saveBitmap2file(Bitmap bmp, String filename, String filedir) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;

		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filedir + filename);
			Log.i(TAG, "保存完成");
			return bmp.compress(format, quality, stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "保存图片异常" + e.toString());
			return false;
		}
	}

	/**
	 * 必须在子线程中调用(cocos)
	 */
	public static String getHttpBitmapPath(String fileDir, String filename, String url) {
		Bitmap bitmap;
		// final File pictureFileDir = new
		// File(Environment.getExternalStorageDirectory(),
		// "/tengfei/cache/icon/");
		final File pictureFileDir = new File(fileDir);
		if (!pictureFileDir.exists()) {
			pictureFileDir.mkdirs();
		}
		File file = new File(pictureFileDir.getPath() + "/" + filename);

		if (file.exists()) {
			Log.i(TAG, "头像存在，删掉");
			file.delete();
		}
		bitmap = getHttpBitmap(url);
		Log.i(TAG, "准备保存本地头像");
		BitmapUtil.saveBitmap2file(bitmap, filename, pictureFileDir.getPath() + "/");
		return pictureFileDir.getPath() + "/" + filename;
	}

	/**
	 * 必须在子线程中调用
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getIconCacheBitmap(String url) {
		// TODO Auto-generated method stub
		Bitmap bitmap;
		final File pictureFileDir = new File(Environment.getExternalStorageDirectory(), "/tengfei/cache/icon/");
		if (!pictureFileDir.exists()) {
			pictureFileDir.mkdirs();
		}
		// String filename = url.replace(
		// "http://www.ylwqgame.com/wwz/images/icon/", "");
		String filename = getFileName(url);
		Log.i(TAG, "文件名" + filename);
		File file = new File(pictureFileDir.getPath() + "/" + filename);
		if (file.exists()) {
			Log.i(TAG, "头像存在，不需要下载");
			bitmap = BitmapFactory.decodeFile(file.getPath());
		} else {
			bitmap = getHttpBitmap(url);
			Log.i(TAG, "准备保存本地头像");
			BitmapUtil.saveBitmap2file(bitmap, filename, pictureFileDir.getPath() + "/");
		}
		return bitmap;
	}

	/**
	 * 获取本地缓存图片 必须在子线程中使用
	 */
	public static Bitmap getCacheBitmap(String url, String path) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		final File pictureFileDir = new File(Environment.getExternalStorageDirectory(), path);
		if (!pictureFileDir.exists()) {
			pictureFileDir.mkdirs();
		}
		String filename = getFileName(url);
		Log.i(TAG, "文件名" + filename);
		File file = new File(pictureFileDir.getPath() + "/" + filename);
		if (file.exists()) {
			Log.i(TAG, "图片存在，不需要下载");
			bitmap = BitmapFactory.decodeFile(file.getPath());
		} else {
			bitmap = getHttpBitmap(url);
			Log.i(TAG, "准备保存本地图片");
			BitmapUtil.saveBitmap2file(bitmap, filename, pictureFileDir.getPath() + "/");
		}
		return bitmap;
	}

	public static String getFileName(String url) {
		String filename = "";
		// boolean isok = false;
		// // 从UrlConnection中获取文件名称
		// try {
		// URL myURL = new URL(url);
		//
		// URLConnection conn = myURL.openConnection();
		// if (conn == null) {
		// return null;
		// }
		// Map<String, List<String>> hf = conn.getHeaderFields();
		// if (hf == null) {
		// return null;
		// }
		// Set<String> key = hf.keySet();
		// if (key == null) {
		// return null;
		// }
		//
		// for (String skey : key) {
		// List<String> values = hf.get(skey);
		// for (String value : values) {
		// String result;
		// try {
		// result = new String(value.getBytes("ISO-8859-1"), "GBK");
		// int location = result.indexOf("filename");
		// if (location >= 0) {
		// result = result.substring(location
		// + "filename".length());
		// filename = result
		// .substring(result.indexOf("=") + 1);
		// isok = true;
		// }
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }// ISO-8859-1 UTF-8 gb2312
		// }
		// if (isok) {
		// break;
		// }
		// }
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// 从路径中获取
		if (filename == null || "".equals(filename)) {
			filename = url.substring(url.lastIndexOf("/") + 1);
		}
		return filename;

		// if (filename.contains(".")) {
		// return filename.substring(0, filename.lastIndexOf("."));
		// } else {
		// return filename;
		// }
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bitmap;

	}

	/**
	 * 下载文件
	 * 
	 */
	public static String downFile(String path, String fileUrl) {
		File cacheFile = null;
		String filePath = "";
		OutputStream output = null;
		int fileSize = 0;
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn == null) {
				return "";
			}
			cacheFile = new File(filePath);
			fileSize = conn.getContentLength();
			L.i("文件大小" + fileSize);
			if (fileSize <= 0) {
				L.i("无法获取文件大小");
			}
			Map<String, List<String>> hf = conn.getHeaderFields();
			if (hf == null) {
				return "";
			}
			Set<String> key = hf.keySet();
			if (key == null) {
				return null;
			}

			if (cacheFile.exists()) {
				// 缓存文件存在，开始断点续传
				cacheFile.delete();
			}
			InputStream input = conn.getInputStream();
			cacheFile.createNewFile();// 新建文件
			output = new FileOutputStream(cacheFile);
			// output = ServiceDemo.this.openFileOutput(, MODE_PRIVATE);
			// 读取大文件
			byte[] buffer = new byte[1024];
			// 开始下载
			while (true) {
				int numread = input.read(buffer);
				if (numread == -1) {
					break;
				}
				output.write(buffer, 0, numread);
			}
			output.flush();
			output.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			L.i(e.toString());
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			L.i(e.toString());
			return "";
		}
		return "ok";
	}

	/**
	 * 从本地获取bitmap
	 * 
	 */

	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128
				: (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	
	// 需要对图片进行处理，否则微信会在log中输出thumbData检查错误，主要在微信分享中使用
	public static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
		//更改图片大小
		Bitmap localBitmap = Bitmap.createBitmap(80, 80, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);
		int i;
		int j;
		if (bitmap.getHeight() > bitmap.getWidth()) {
			i = bitmap.getWidth();
			j = bitmap.getWidth();
		} else {
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
		while (true) {
			localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 80, 80), null);
			if (paramBoolean)
				bitmap.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
				// F.out(e);
			}
			i = bitmap.getHeight();
			j = bitmap.getHeight();
		}
	}

	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
}
