using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LuaInterface;

public static class NetHelper
{
    /// <summary>
    /// 注册网络消息处理函数
    /// </summary>
    /// <param name="luafun"></param>
    public static void RegNetMsgProcess(LuaFunction luafun)
    {
        ProcessMsg.Instance.RegNetMsgProcess(luafun);
    }
    /// <summary>
    /// 注册网络异常事件
    /// </summary>
    /// <param name="luafun"></param>
    public static void RegisterNetExEvent(LuaFunction luafun)
    {
        ConnectionManager.RegisterNetExEvent(() =>
        {
            luafun.BeginPCall();
            luafun.PCall();
            luafun.EndPCall();
        });
    }

    /// <summary>
    /// 注册心跳
    /// </summary>
    /// <param name="luafun"></param>
    /// <param name="spacing"></param>
    public static void RegHeartbeatFun(LuaFunction luafun, float spacing)
    {
        ProcessMsg.Instance.RegHeartbeatFun(luafun, spacing);
    }

    /// <summary>
    /// 删除心跳
    /// </summary>
    public static void DeleteHeartbeat()
    {
        ProcessMsg.Instance.DeleteHeartbeat();
    }

    /// <summary>
    /// 同步建立TCP连接
    /// </summary>
    /// <param name="Ip"></param>
    /// <param name="Prot"></param>
    /// <returns></returns>
    public static bool CreateConnectionBySyn(string Ip, int Prot)
    {
        bool isOk = ConnectionManager.CreateConnection(Ip, Prot);
        if (isOk)
            Debug.Log("建立链接成功 :" + Ip + ":" + Prot);
        else
            Debug.Log("建立链接失败:" + Ip + ":" + Prot);
        return isOk;
    }

    /// <summary>
    /// 异步建立TCP连接
    /// </summary>
    /// <param name="Ip"></param>
    /// <param name="Prot"></param>
    /// <param name="luafun"></param>
    public static void CreateConnectionByAsync(string Ip, int Prot,LuaFunction luafun)
    {
        bool? isOK = false;
        bool? isComplate = false;

        ConnectionManager.CreateConnection(Ip, Prot,false,(ar) =>
        {
            SocketThread state = ar.AsyncState as SocketThread;
            isOK = ar.IsCompleted && state.HasConnect();
            isComplate = true;
        });

        //绕开子线程访问主线程的问题
        GameObject CallBackObj = new GameObject();
        BehaviourUpdateEvent UpdateEvent = CallBackObj.AddComponent<BehaviourUpdateEvent>();
        UpdateEvent.UpdateAction = () =>
        {
            if((bool)isComplate)
            {
                luafun.BeginPCall();
                luafun.Push((bool)isOK);
                luafun.PCall();
                luafun.EndPCall();
                luafun.Dispose();
                GameObject.DestroyImmediate(CallBackObj);
            }
        };
    }


    /// <summary>
    /// 关闭连接
    /// </summary>
    public static void Disconnect()
    {
        DeleteHeartbeat();
        ConnectionManager.CloseConnect();
    }

    /// <summary>
    /// 发送数据
    /// </summary>
    public static void SendMsg(short cmd, byte[] data)
    {
        List<byte> Pack = ConnectionManager.BeginPack(cmd);
        ConnectionManager.PackData(Pack, data);
        ConnectionManager.EndPack(Pack);
        ConnectionManager.SendPack(Pack.ToArray());
    }

    /// <summary>
    /// 发送数据
    /// </summary>
    public static void SendMsg(short cmd)
    {
        List<byte> Pack = ConnectionManager.BeginPack(cmd);
        ConnectionManager.EndPack(Pack);
        ConnectionManager.SendPack(Pack.ToArray());
    }

    /// <summary>
    /// 同步发起Http请求，返回string
    /// </summary>
    /// <param name="url"></param>
    /// <param name="timeout"></param>
    /// <returns></returns>
    public static string SendHttpRequestBySyn(string url, int timeout)
    {
        return HttpHelper.SendHttpRequestBySyn(url, timeout);
    }

    /// <summary>
    /// 异步发起http请求，基于www实现，返回string
    /// </summary>
    /// <param name="url"></param>
    /// <param name="wwwfrom"></param>
    /// <param name="luafu"></param>
    public static void SendHttpRequestByASyn(string url, WWWForm wwwfrom, LuaFunction luafu)
    {
        LuaMgr.Instance.StartCoroutine_Auto(ProcessASynSend(url, luafu, wwwfrom));
    }

    private static IEnumerator ProcessASynSend(string url, LuaFunction luafun, WWWForm wwwfrom = null)
    {
        Debug.Log("发送Http请求: " + url);

        WWW www = null;

        if (null == wwwfrom)
            www = new WWW(url);
        else
            www = new WWW(url, wwwfrom);

        yield return www;

        if (!string.IsNullOrEmpty(www.error))
        {
            Debug.LogError("请求失败 " + www.error);
            yield break;
        }

        string retString = www.text;

        luafun.BeginPCall();
        luafun.Push(retString);
        luafun.PCall();
        luafun.EndPCall();

        www.Dispose();
    }
}