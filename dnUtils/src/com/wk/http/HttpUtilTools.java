package com.wk.http;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.jetty.http.HttpHeaders;
import org.json.JSONObject;

/**
 * 通过post来请求http
 *
 * @author 李星
 */
public class HttpUtilTools {

	public static final Charset utf_8 = Charset.forName("UTF-8");
	/**
	 * HTTP 读取超时时间
	 */
	public final static int readTimeOut = 30 * 1000;
	/**
	 * HTTP 连接超时时间
	 */
	public final static int connectTimeOut = 30 * 1000;

	// -----------------------------------------------------------------------------------
	public static String post(String _url, String paras) throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoInput(true);// 允许接收输入
		httpURLConnection.setDoOutput(true);// 允许接收输出
		httpURLConnection.setUseCaches(false);// 不适用缓存
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		if (paras != null && !paras.equals("")) {
			// 发送消息
			OutputStream dos = httpURLConnection.getOutputStream();
			dos.write(paras.getBytes(utf_8));
			dos.flush();
			dos.close();
		}
		InputStream inputStream = httpURLConnection.getInputStream();
		// BufferedReader dis = new BufferedReader(new InputStreamReader(
		// inputStream));
		// String readLine = dis.readLine();
		// dis.close();
		byte[] bts = new byte[2000];
		int readOver = readOver(inputStream, bts);
		String readLine = new String(bts, 0, readOver);
		while (readOver == bts.length) {
			readOver = readOver(inputStream, bts);
			readLine += new String(bts, 0, readOver);
		}
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
		return readLine;
	}

	/**
	 * HTTP post请求
	 * 
	 * @param _url
	 * @param writeStrBytes
	 * @param isRead
	 * @return 先读长度,字节数组
	 * @throws Exception
	 */
	public static byte[] post(String _url, byte[] writeStrBytes, boolean isRead)
			throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoInput(true);// 允许接收输入
		httpURLConnection.setDoOutput(true);// 允许接收输出
		httpURLConnection.setUseCaches(false);// 不适用缓存
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 发送消息
		OutputStream dos = httpURLConnection.getOutputStream();
		dos.write(writeStrBytes);
		dos.flush();
		dos.close();
		InputStream inputStream = httpURLConnection.getInputStream();
		if (isRead) {
			DataInputStream dis = new DataInputStream(inputStream);
			byte[] readStrBytes = new byte[dis.readInt()];
			dis.readFully(readStrBytes);
			dis.close();
			httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
			return readStrBytes;
		} else {
			return null;
		}
	}

	/**
	 * HTTPS get请求
	 * 
	 * @param _url
	 * @param writeStrBytes
	 * @param isRead
	 * @return JSON
	 * @throws Exception
	 */
	public static JSONObject httpsGetJson(String _url, byte[] writeStrBytes,
			boolean isRead) throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpsURLConnection httpURLConnection = (HttpsURLConnection) openConnection;
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setDoInput(true);// 允许接收输入
		httpURLConnection.setDoOutput(true);// 允许接收输出
		// httpURLConnection.setUseCaches(true);// 不适用缓存
		httpURLConnection.setRequestProperty(HttpHeaders.ACCEPT, "text/json");
		httpURLConnection.setRequestProperty(HttpHeaders.ACCEPT_CHARSET,
				"utf-8");
		httpURLConnection.setRequestProperty(HttpHeaders.CACHE_CONTROL,
				"no-cache");
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		if (writeStrBytes != null && writeStrBytes.length > 0) {
			// 发送消息
			OutputStream dos = httpURLConnection.getOutputStream();
			dos.write(writeStrBytes);
			dos.flush();
			dos.close();
		}
		InputStream inputStream = httpURLConnection.getInputStream();
		if (isRead) {
			BufferedReader f = new BufferedReader(new InputStreamReader(
					inputStream,
					httpURLConnection
							.getRequestProperty(HttpHeaders.ACCEPT_CHARSET)));
			String readLine = f.readLine();
			JSONObject jsonObject = new JSONObject(readLine == null ? ""
					: readLine);
			httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
			return jsonObject;
		} else {
			return null;
		}
	}

	/**
	 * HTTP post请求
	 * 
	 * @param _url
	 * @param writeStrBytes
	 * @param isRead
	 * @return 先读长度,字节数组
	 * @throws Exception
	 */
	public static String httpsPost(String _url, String paras) throws Exception {
		return httpsPost(_url, paras.getBytes(utf_8));
	}

	public static String httpsPost(String _url, byte[] bytes) throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpsURLConnection httpURLConnection = (HttpsURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoInput(true);// 允许接收输入
		httpURLConnection.setDoOutput(true);// 允许接收输出
		// httpURLConnection.setUseCaches(false);// 不适用缓存
		httpURLConnection.setRequestProperty(HttpHeaders.ACCEPT, "text/json");
		httpURLConnection.setRequestProperty(HttpHeaders.ACCEPT_CHARSET,
				"utf-8");
		httpURLConnection.setRequestProperty(HttpHeaders.CACHE_CONTROL,
				"no-cache");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(bytes.length));
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 发送消息
		OutputStream dos = httpURLConnection.getOutputStream();
		dos.write(bytes);
		dos.flush();
		dos.close();
		InputStream inputStream = httpURLConnection.getInputStream();
		byte[] bts = new byte[2000];
		int readOver = readOver(inputStream, bts);
		String ret = new String(bts, 0, readOver);
		while (readOver == bts.length) {
			readOver = readOver(inputStream, bts);
			ret += new String(bts, 0, readOver);
		}
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
		return ret;
	}

	/**
	 * 读取流，尽量将字节数组填满
	 * 
	 * @param input
	 * @param b
	 * @return 读取字节数
	 * @throws IOException
	 */
	public static final int readOver(InputStream input, byte b[])
			throws IOException {
		int off = 0;
		int len = b.length;
		if (len <= 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = input.read(b, off + n, len - n);
			if (count < 0) {
				break;
			}
			n += count;
		}
		return n;
	}
}
