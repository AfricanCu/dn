package com.wk.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * 通过post来请求http
 *
 * @author 李星
 */
public class HttpCommonTools {

	public static final Charset utf_8 = Charset.forName("UTF-8");
	/**
	 * HTTP 读取超时时间
	 */
	private static int readTimeOut = 1000 * 60 * 10;
	/**
	 * HTTP 连接超时时间
	 */
	private static int connectTimeOut = 1000 * 10;

	// -----------------------------------------------------------------------------------
	public static boolean isCanConnect(String _url) {
		try {
			new URL(_url).openConnection().connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * HTTP post请求（带消息长度）
	 * 
	 * 可配置是否读取返回
	 * 
	 * @param _url
	 * @param _content
	 *            发送内容
	 * @param isRead
	 *            是否读取返回
	 * @return
	 * @throws Exception
	 */
	public static String sendHttp(String _url, String _content, boolean isRead)
			throws Exception {
		return sendHttp(_url, _content, connectTimeOut, readTimeOut, isRead);
	}

	/**
	 * HTTP post请求（带消息长度）
	 * 
	 * 消息编码：utf8
	 * 
	 * 可配置多个参数
	 * 
	 * @param _url
	 * @param _content
	 * @param connectTimeout
	 * @param readTimeout
	 * @param isRead
	 *            是否读取返回
	 * @return 接收的消息
	 * @throws Exception
	 */
	public static String sendHttp(String _url, String _content,
			int connectTimeout, int readTimeout, boolean isRead)
			throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setDoInput(true);// 允许接收输入
		httpURLConnection.setDoOutput(true);// 允许接收输出
		httpURLConnection.setUseCaches(false);// 不适用缓存
		httpURLConnection.setConnectTimeout(connectTimeout);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeout); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 发送消息
		DataOutputStream dos = new DataOutputStream(
				httpURLConnection.getOutputStream());
		byte[] writeStrBytes = _content.getBytes(utf_8);
		dos.writeInt(writeStrBytes.length);
		dos.write(writeStrBytes);
		dos.flush();
		dos.close();
		InputStream inputStream = httpURLConnection.getInputStream();
		if (isRead) {
			// 接收消息

			// test
			boolean debug = false;
			if (debug) {
				byte[] b = new byte[1000];
				int readOver = readOver(inputStream, b);
				String _return = new String(b, 4, readOver - 4, utf_8);
				httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
				return _return;
			} else {
				DataInputStream dis = new DataInputStream(inputStream);
				byte[] readStrBytes = new byte[dis.readInt()];
				dis.readFully(readStrBytes);
				String _return = new String(readStrBytes, utf_8);
				dis.close();
				httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
				return _return;
			}

		} else {
			return null;
		}
	}

	// -----------------------------------------------------------------------------------

	/**
	 * 写消息到流并关闭流（带消息长度）
	 * 
	 * 消息编码：utf8
	 * 
	 * @param output
	 * @param message
	 */
	public static void writeMessageAndClose(OutputStream output, String message) {
		byte[] bytes = message.getBytes(utf_8);
		try {
			DataOutputStream out = new DataOutputStream(output);
			out.writeInt(bytes.length);
			out.write(bytes);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写字节数组（byte[]）消息到流
	 * 
	 * @param output
	 * @param message
	 *            byte数组
	 * @param isOnlyMsg
	 *            是否只发送消息（即不发送消息长度）
	 */
	public static void writeIsOnlyMessage(OutputStream output, byte[] message,
			boolean isOnlyMsg) {
		if (isOnlyMsg) {
			try {
				output.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			try {
				DataOutputStream out = new DataOutputStream(output);
				out.writeInt(message.length);
				out.write(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 写字符串（String）消息到流
	 * 
	 * 消息编码：utf8
	 * 
	 * @param output
	 * @param message
	 * @param isOnlyMsg
	 *            是否只发送消息 （即不发送消息长度）
	 */
	public static void writeIsOnlyMessage(OutputStream output, String message,
			boolean isOnlyMsg) {
		byte[] bytes = message.getBytes(utf_8);
		if (isOnlyMsg) {
			try {
				output.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			try {
				DataOutputStream out = new DataOutputStream(output);
				out.writeInt(bytes.length);
				out.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写字节数组（byte[]）消息到流（带消息长度）
	 * 
	 * @param output
	 * @param message
	 */
	public static void writeMessage(OutputStream output, byte[] message) {
		try {
			DataOutputStream out = new DataOutputStream(output);
			out.writeInt(message.length);
			out.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写string消息到流（带消息长度）
	 * 
	 * 编码格式:utf8
	 * 
	 * @param output
	 * @param message
	 */
	public static void writeMessage(OutputStream output, String message) {
		byte[] bytes = message.getBytes(utf_8);
		try {
			DataOutputStream out = new DataOutputStream(output);
			System.out.println(bytes.length);
			out.writeInt(bytes.length);
			out.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取流消息并关闭（带消息长度）
	 * 
	 * @param input
	 * @return 字节数组 byte[]
	 */
	public static byte[] readBytesAndClose(InputStream input) {
		try {
			DataInputStream dis = new DataInputStream(input);
			byte[] readStrBytes = new byte[dis.readInt()];
			dis.readFully(readStrBytes, 0, readStrBytes.length);
			dis.close();
			return readStrBytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取流消息并关闭（带消息长度）
	 * 
	 * @param input
	 * @return 字符串：utf8编码
	 */
	public static String readStringAndClose(InputStream input) {
		try {
			DataInputStream dis = new DataInputStream(input);
			byte[] readStrBytes = new byte[dis.readInt()];
			dis.readFully(readStrBytes);
			dis.close();
			return new String(readStrBytes, utf_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取流（无消息长度）
	 * 
	 * @param input
	 * @return 字符串 utf8编码
	 * @throws IOException
	 */
	public final static String readString(InputStream input) throws IOException {
		BufferedReader d = new BufferedReader(new InputStreamReader(input,
				utf_8));
		StringBuilder builder = new StringBuilder();
		String str = d.readLine();
		while (str != null) {
			builder.append(str);
			str = d.readLine();
		}
		return builder.toString();
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
		DataInputStream dis = new DataInputStream(input);
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = dis.read(b, off + n, len - n);
			if (count < 0) {
				System.err.println("throw new EOFException();count=" + count);
				break;
			}
			n += count;
		}
		return n;
	}

	/**
	 * HTTP POST请求（带消息长度）
	 * 
	 * 编码格式:utf8
	 * 
	 * @param _url
	 * @param _msg
	 * @return byte[] 返回消息（带消息长度）
	 * @throws Exception
	 */
	public static byte[] sendPostHttpMsg(String _url, String _msg)
			throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");// 默认get
		httpURLConnection.setDoOutput(true);// The default is false.
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 发送消息
		writeMessage(httpURLConnection.getOutputStream(), _msg);
		// 接收消息
		byte[] readBytesAndClose = readBytesAndClose(httpURLConnection
				.getInputStream());
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
		return readBytesAndClose;
	}

	/**
	 * HTTP POST请求（无消息长度）
	 * 
	 * 编码格式:utf8
	 * 
	 * @param _url
	 * @param _para
	 * @return 字符串：utf8编码（无消息长度）
	 * @throws Exception
	 */
	public static String sendPostHttpParameter(String _url, String _para)
			throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");// 默认get
		httpURLConnection.setDoOutput(true);// The default is false.s
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 发送消息
		httpURLConnection.getOutputStream().write(_para.getBytes());
		// 接收消息
		String readAndWrite = readString(httpURLConnection.getInputStream());
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
		return readAndWrite;
	}

	/**
	 * HTTP POST请求
	 * 
	 * @param _url
	 * @param param
	 * @return 字符串：utf8编码 （无消息长度）
	 * @throws Exception
	 */
	public static String sendHttp(String _url, String param) throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");// 默认get
		httpURLConnection.setDoOutput(true);// The default is false.
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		OutputStream outputStream = httpURLConnection.getOutputStream();
		outputStream.write(param.getBytes("utf-8"));
		// 接收消息
		String readAndWrite = readString(httpURLConnection.getInputStream());
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
		return readAndWrite;
	}

	/**
	 * HTTP POST请求 并输出
	 * 
	 * @param out
	 *            输出的对象
	 * @param _url
	 * @throws Exception
	 */
	public static void sendHttpAndOutput(Writer out, String _url)
			throws Exception {
		URL url = new URL(_url);
		URLConnection openConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
		httpURLConnection.setRequestMethod("POST");// 默认get
		httpURLConnection.setDoOutput(true);// The default is false.
		httpURLConnection.setConnectTimeout(connectTimeOut);// 设置连接超时的最大时间
		httpURLConnection.setReadTimeout(readTimeOut); // 设置读取超时的最大时间
		httpURLConnection.connect();
		// 接收消息
		readAndWrite(out, httpURLConnection.getInputStream());
		httpURLConnection.disconnect();// 指示服务器近期不太可能有其他请求
	}

	private static void readAndWrite(Writer out, InputStream input)
			throws IOException {
		BufferedReader d = new BufferedReader(new InputStreamReader(input));
		String str = d.readLine();
		while (str != null) {
			out.write(str);
			str = d.readLine();
		}
	}

	public static void main(String[] args) throws Exception {
	}

}
