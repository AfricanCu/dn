package com.ems358.sdk.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ems358.sdk.EmsApi;
import com.ems358.sdk.util.BitmapUtil;
import com.ems358.sdk.util.L;
import com.ems358.sdk.util.MD5Util;
import com.ems358.sdk.util.MyTools;
import com.ems358.sdk.util.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;

public class Update {
	private ProgressDialog updateDialog;
	private Context context;

	public Update(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void show(String url) {

		updateDialog = new ProgressDialog(context);
		updateDialog.setTitle("准备更新");
		// updateDialog.setIndeterminate(false);
		updateDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		updateDialog.setCancelable(false);
		updateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		updateDialog.setProgress(0);
		L.i("开始显示弹框");
		// new
		// AlertDialog.Builder(EmsApi.$this.getContext()).setTitle("你好").show();

		updateDialog.show();
		new DownTask().execute(url);
	}

	public class DownTask extends AsyncTask<String, Integer, String> {
		String cacheFileName;
		String filePathName;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// 这里开始下载
			// 更新下载进度
			File cacheFile = null;
			File downFile = null;
			String path = "tengfei";
			String fileName = "";
			cacheFileName = MD5Util.getMD5(MyTools.getRandomString(8));
			boolean isok = false;
			OutputStream output = null;
			int fileSize = 0;
			int downLoadFileSize = 0;
			try {
				URL url = new URL(params[0]);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				if (conn == null) {
					return "";
				}
				String SDCard = Environment.getExternalStorageDirectory() + "";
				String cachePathName = SDCard + "/" + path + "/" + cacheFileName;// 缓存文件存储路径
				cacheFile = new File(cachePathName);
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

				// 获取文件名
				for (String skey : key) {
					List<String> values = hf.get(skey);
					for (String value : values) {
						String result;
						try {
							result = new String(value.getBytes("UTF-8"), "GBK");
							int location = result.indexOf("filename");
							if (location >= 0) {
								result = result.substring(location + "filename".length());
								fileName = result.substring(result.indexOf("=") + 1);
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
				// 如果文件名为空，从url获取文件名
				if (StringUtils.isEmpty(fileName)) {
					fileName = BitmapUtil.getFileName(params[0]) + ".apk";
				}

				if (cacheFile.exists()) {
					// 缓存文件存在，开始断点续传
				}
				// 如果文件存在，就不下载了
				filePathName = SDCard + "/" + path + "/" + fileName;// 文件存储路径
				downFile = new File(filePathName);
				if (downFile.exists()) {
					return "ok";
				}
				InputStream input = conn.getInputStream();
				String dir = SDCard + "/" + path;
				new File(dir).mkdir();// 新建文件夹
				cacheFile.createNewFile();// 新建文件
				output = new FileOutputStream(cacheFile);
				// output = ServiceDemo.this.openFileOutput(, MODE_PRIVATE);

				// 读取大文件
				byte[] buffer = new byte[1024];
				// 初始化值
				int progress = 0;
				// 开始下载
				while (true) {
					int numread = input.read(buffer);
					if (numread == -1) {
						break;

					}
					output.write(buffer, 0, numread);
					downLoadFileSize += numread;
					if ((int) ((float) downLoadFileSize / fileSize * 100) - progress >= 4) {
						progress = (int) ((float) downLoadFileSize / fileSize * 100);
						publishProgress(downLoadFileSize >= fileSize ? 100 : progress);
					} else if ((int) ((float) downLoadFileSize / fileSize * 100) == 100) {
						progress = 100;
						publishProgress(downLoadFileSize >= fileSize ? 100 : progress);
					}
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
			} finally {
				publishProgress(100);
			}
			// 如果是正常下载的话，更改文件名
			MyTools.renameToNewFile(cacheFile, fileName);
			return "ok";
		}

		protected void onPostExecute(String result) {
			if (result.equals("ok")) {
				// 提示下载成功
				L.i(filePathName);
				MyTools.openFile(EmsApi.$this.context, new File(filePathName));
			} else {
				L.i("下载失败");
				updateDialog.setProgress(0);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// 这里开始下载

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			// 这里更新进度
			if (values[0] < 100) {
				updateDialog.setTitle("正在下载");
				updateDialog.setProgress(values[0]);
			} else {
				// 下载完成
				updateDialog.setTitle("下载完成");
				updateDialog.setProgress(values[0]);
			}

		}

	}

}
