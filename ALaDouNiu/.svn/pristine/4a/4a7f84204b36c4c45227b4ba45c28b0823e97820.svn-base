using System;
using System.Net.Sockets;
using System.Net;
using System.Collections;
using System.Text;
using System.Threading;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.IO;
using UnityEngine;

public static class NetWorkConfig
{
    /// <summary>
    /// 消息头长度
    /// </summary>
    public const int MSG_HEAD_LENGTH = 2;
    /// <summary>
    /// Command长度
    /// </summary>
    public const int MSG_COMMAND_LENGTH = 2;
    /// <summary>
    /// 接受消息缓冲区大小
    /// </summary>
    public const int MSG_BUF_MAX_SIZE = 20480;
}
    

/// <summary>
/// 连接管理器
/// </summary>
public static class ConnectionManager
{
    /// <summary>
    /// 默认消息队列
    /// </summary>
    private static readonly Queue<Msg> MsgQue = new Queue<Msg>();
    private static SocketThread _SocketThread = new SocketThread(MsgQue);
    /// <summary>
    /// 建立连接，如果已有连接，则断开已有连接再建立连接
    /// </summary>
    /// <param name="Ip">IP</param>
    /// <param name="Prot">端口</param>
    /// <param name="IsSyn">是否同步建立连接</param>
    /// <param name="callback">异步创建回调</param>
    /// <returns>连接是否建立成功，如果为异步方式建立连接，则直接返回false</returns>
    public static bool CreateConnection(string Ip, int Prot, bool IsSyn = true, AsyncCallback callback = null)
    {
        if(_SocketThread.HasConnect())
        {
            _SocketThread.CloseConnect();
        }
        try
        {
            _SocketThread.Connect(Ip, Prot, IsSyn, callback);
        }
        catch (Exception ex)
        {
            Debug.LogError(ex);
            return false;
        }
        if (IsSyn)
        {
            return true;
        }
        return false;
    }

    /// <summary>
    /// 注册网络异常事件
    /// </summary>
    /// <param name="callBack"></param>
    public static void RegisterNetExEvent(SocketThread.OnConnectUnexpected callBack)
    {
        _SocketThread.OnConnectionExEvnet += callBack;
    }


    public static void ClearNetExEvent()
    {
        _SocketThread.ClearNetExEvent();
    }

    /// <summary>
    ///  断开链接
    /// </summary>
    public static void CloseConnect()
    {
        _SocketThread.CloseConnect();
        Debug.Log("关闭连接");
    }
    /// <summary>
    /// 清空消息队列
    /// </summary>
    public static void ClearMsgQue()
    {
        MsgQue.Clear();
    }
    /// <summary>
    /// 是否连接
    /// </summary>
    /// <returns></returns>
    public static bool HasConnect()
    {
        return _SocketThread.HasConnect();
    }
    /// <summary>
    /// 重置,断开连接，清空消息队列
    /// </summary>
    /// <returns></returns>
    public static void Reset()
    {
        if (HasConnect())
        {
            CloseConnect();
            ClearMsgQue();
            ClearNetExEvent();
        }
    }
    /// <summary>
    /// 从消息队列中获得消息，如果没有消息，返回null
    /// </summary>
    /// <returns></returns>
    public static Msg GetMsg()
    {
        if (MsgQue.Count > 0)
        {
            return MsgQue.Dequeue();
        }
        return null;
    }
    /// <summary>
    /// 获取消息队列的消息数量
    /// </summary>
    /// <returns></returns>
    public static int GetMsgNum()
    {
        return MsgQue.Count;
    }
    /// <summary>
    /// 发送数据包
    /// </summary>
    /// <param name="mKey"></param>
    /// 用来发送数据的连接ID
    /// <param name="Data"></param>
    /// 需要发送的数据
    public static void SendPack(byte[] buf)
    {
        try
        {
            _SocketThread.SendMsg(buf);
        }
        catch (System.Exception ex)
        {
            Debug.LogError("～连接异常:" + ex);
            CloseConnect();
            return;
        }
    }
    /// <summary>
    /// 开始打包数据
    /// </summary>
    /// <param name="mCommand"></param>
    /// <returns></returns>
    public static List<byte> BeginPack(short mCommand)
    {
        List<byte> Data = new List<byte>();
        byte[] longth = TypeConvert.getBytes(mCommand, false);
        Array.Reverse(longth);//处理大小端
        for (int i = 0; i < longth.Length; ++i)
        {
            Data.Add(longth[i]);
        }
        return Data;
    }
    /// <summary>
    /// 打包数据
    /// </summary>
    /// <param name="Data"></param>
    public static void PackData(List<byte> Data,byte[] data)
    {
        Data.AddRange(data);
    }
    /// <summary>
    /// 结束打包数据
    /// </summary>
    /// <param name="Data"></param>
    public static void EndPack(List<byte> Data)
    {
        //在包头写入包长度
        short mMsgLen = (short)(Data.Count);
        byte[] longth = TypeConvert.getBytes(mMsgLen, false);
        Array.Reverse(longth);//处理大小端
        for (int i = 0; i < longth.Length; ++i)
        {
            Data.Insert(i, longth[i]);
        }
    }

}
