package gm.server;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StopServer {
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://"
				+ (args.length >= 4 ? args[3] : "localhost") + ":" + args[0]
				+ "/shutdown");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(100000);// 设置连接超时的最大时�?
		connection.setReadTimeout(100000);
		connection.connect();
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(("token=" + args[1] + " " + args[2]).getBytes());
		System.out.println(connection.getResponseCode() + "Shutting down "
				+ url + ": " + connection.getResponseMessage());
		connection.disconnect();// 指示服务器近期不太可能有其他请求
	}
}
