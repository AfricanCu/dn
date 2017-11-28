package com.ems358.sdk.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtil {

	public static String doGet(String url) {
		HttpGet httpRequest = new HttpGet(url);
		try {
			/* 发送请求并等待响应 */
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			/* 若状态码为200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 读 */
//				String result = EntityUtils.toString(httpResponse.getEntity());
				String result = new String(EntityUtils.toByteArray(httpResponse.getEntity()),"UTF-8");
				 /*去没有用的字符*/
//				result = result.replaceAll("(\r\n|\r|\n|\n\r)", "");
//				if(result.contains("\\")){
//					Log.i("test","删除所有的\\符号");
//					result = result.replace("\\", "");
//				}
				return result;
			} else {
				Log.e("test", "Error Response: "
						+ httpResponse.getStatusLine().toString());
				return null;
			}
		} catch (ClientProtocolException e) {
			Log.e("test", e.getMessage().toString());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.e("test", e.getMessage().toString());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			Log.e("test", e.getMessage().toString());
			e.printStackTrace();
			return null;
		}
	}
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } else {
        	//如果仅仅是用来判断网络连接 
        	//则可以使用 cm.getActiveNetworkInfo().isAvailable();  
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
    }
}
