using UnityEngine;
using System.Collections;
using System.Net;
using System.IO;
using System.Text;

public static class HttpHelper
{
    /// <summary>
    /// 同步发起http请求
    /// </summary>
    /// <param name="url"></param>
    /// <param name="timeout"></param>
    /// <returns></returns>
    public static string SendHttpRequestBySyn(string url,int timeout)
    {
        HttpWebRequest request = null;
        request = WebRequest.Create(url) as HttpWebRequest;
        request.Method = "GET";
        request.Timeout = timeout;

        try
        {
            HttpWebResponse response = request.GetResponse() as HttpWebResponse;
            Stream ResponseStream = response.GetResponseStream();
            StreamReader StreamReader = new StreamReader(ResponseStream, Encoding.GetEncoding("UTF-8"));
            string retString = StreamReader.ReadToEnd();
            StreamReader.Close();
            ResponseStream.Close();

            return retString;
        }
        catch (System.Exception ex)
        {
            Debug.LogError(ex);
            return null;
        }
    }


    /// <summary>
    /// 异步发起Http请求
    /// </summary>
    /// <param name="url"></param>
    /// <param name="timeout"></param>
    /// <param name="action"></param>
    public static void SendHttpRequestByASyn(string url,int timeout,System.AsyncCallback callback)
    {
        HttpWebRequest request = null;
        request = WebRequest.Create(url) as HttpWebRequest;
        request.Method = "GET";
        request.Timeout = timeout;
        request.BeginGetResponse(callback, request);
    }
}
