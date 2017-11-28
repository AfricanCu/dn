using System;
using System.Net.Sockets;
using System.Net;
using System.Threading;
using System.Collections.Generic;
using UnityEngine;
using System.IO;

/// <summary>
/// Socket线程
/// </summary>
public class SocketThread
{
    public int? _int = null;

    public readonly int m_ID;

    public delegate void OnConnectUnexpected();


    /// <summary>
    /// 网络异常事件
    /// </summary>
    public event OnConnectUnexpected OnConnectionExEvnet;

    private readonly Queue<Msg> MsgQue = null;
    private Socket m_Socket = null;
    private Thread t = null;
    private int idx;

    private byte[] m_Buf;
    private int m_BufOffset;

    private byte[] m_SwapBuf;
    private int m_Count;
    
    private bool m_ThreadRun;

    static private int g_ThreadIndex = 0;

    private object SyncObj = new object();

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="mMsgQue">消息接收队列</param>
    public SocketThread(Queue<Msg> mMsgQue)
    {
        idx = g_ThreadIndex;
        ++g_ThreadIndex;

        m_Count = 0;
        MsgQue = mMsgQue;

        m_Buf = new byte[NetWorkConfig.MSG_BUF_MAX_SIZE];
        m_SwapBuf = new byte[NetWorkConfig.MSG_BUF_MAX_SIZE];

        ClearBuf();
    }

    /// <summary>
    /// 建立Socket连接
    /// </summary>
    /// <param name="IP"></param>
    /// <param name="Port"></param>
    /// <param name="IsSyn"></param>
    /// <param name="callback"></param>
    public void Connect(string IP, int Port, bool IsSyn = true, AsyncCallback callback = null)
    {
        ClearBuf();
        CloseConnect();
        String newServerIp = "";
        AddressFamily newAddressFamily = AddressFamily.InterNetwork;
        IPv6SupportMidleware.getIPType(IP, Port.ToString(), out newServerIp, out newAddressFamily);
        if (!string.IsNullOrEmpty(newServerIp))
        {
            IP = newServerIp;
        }

        m_Socket = new Socket(newAddressFamily, SocketType.Stream, ProtocolType.Tcp);
        IPAddress ip = IPAddress.Parse(IP);
        IPEndPoint ipe = new IPEndPoint(ip, Port);

        if (IsSyn)
        {
            Debug.Log("同步方式建立连接:" + ipe.ToString());
            m_Socket.Connect(ipe);
        }
        else
        {
            Debug.Log("异步方式建立连接:" + ipe.ToString());
            m_Socket.BeginConnect(ipe, callback, this);
        }

        if (null == t)
        {
            m_ThreadRun = true;
            t = new Thread(new ThreadStart(run));
            t.Start();
        }

    }
    /// <summary>
    /// 获取消息线程ID
    /// </summary>
    /// <returns></returns>
    public int GetTheradIndex()
    {
        return idx;
    }  

    /// <summary>
    /// 循环从Socket缓冲区取消息
    /// </summary>
    private void run()
    {
        while (true)
        {
            if (!m_ThreadRun)
            {
                break;
            }
            try
            {
                if (null != m_Socket)
                {
                    lock (m_Socket)
                    {
                        if (m_Socket.Available > 0 //是否有网络数据
                            && m_BufOffset < NetWorkConfig.MSG_BUF_MAX_SIZE)//buff 是否已满
                        {
                            byte[] mData = null;

                            if (m_Socket.Available > NetWorkConfig.MSG_BUF_MAX_SIZE - m_BufOffset)// buff 可用缓存不够存下当前接收的所有数据
                            {
                                mData = new byte[NetWorkConfig.MSG_BUF_MAX_SIZE - m_BufOffset];
                                m_Count = m_Socket.Receive(mData, mData.Length, 0);
                            }
                            else //buff 可用缓足够存下当前接收的所有数据
                            {
                                mData = new byte[m_Socket.Available];
                                m_Count = m_Socket.Receive(mData);
                            }

                            for (int i = 0; i < m_Count; ++i)
                            {
                                m_Buf[m_BufOffset] = mData[i];
                                ++m_BufOffset;
                            }
                        }
                        while (HasMsg())
                        {
                            Msg TempMsg = GetMsgFromBuf();
                            MsgQue.Enqueue(TempMsg);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Debug.LogError(e.ToString());
                OnConnectionExEvnet();
                CloseConnect();
                continue;
            }
            Thread.Sleep(30);
        }
    }

    /// <summary>
    /// 从缓冲区获取数据
    /// </summary>
    /// <param name="Length"></param>
    /// <returns></returns>
    private byte[] GetData(int Length)
    {
        byte[] Temp = new byte[Length];
        for (int i = 0; i < Temp.Length; ++i)
        {
            Temp[i] = m_Buf[i];
        }

        Array.Copy(m_Buf, Temp.Length, m_SwapBuf, 0, m_BufOffset - Length);
        Array.Copy(m_SwapBuf, 0, m_Buf, 0, m_BufOffset - Length);
        m_BufOffset -= Length;

        return Temp;
    }

    /// <summary>
    /// 从缓冲区提取一个逻辑分组消息
    /// </summary>
    /// <returns></returns>
    private Msg GetMsgFromBuf()
    {
        try
        {
            if (m_BufOffset < NetWorkConfig.MSG_HEAD_LENGTH)
            {
                return null;
            }
            else
            {
                short length = 0;
                byte[] Temp = new byte[NetWorkConfig.MSG_HEAD_LENGTH];
                for (int i = 0; i < Temp.Length; ++i)
                {
                    Temp[i] = m_Buf[i];
                }
                length = TypeConvert.getShort(Temp);//长度不包含头
                if (m_BufOffset >= length + NetWorkConfig.MSG_HEAD_LENGTH)
                {
                    return new Msg(GetData(length + NetWorkConfig.MSG_HEAD_LENGTH));
                }
            }
            return null;
        }
        catch (System.Exception ex)
        {
            Debug.LogError("缓冲区数据异常,断开连接;" + ex);
            OnConnectionExEvnet();
            CloseConnect();
            return null;
        }
    }

    /// <summary>
    /// 缓冲区中是否有可用逻辑分组数据
    /// </summary>
    /// <returns></returns>
    private bool HasMsg()
    {
        if (m_BufOffset < NetWorkConfig.MSG_HEAD_LENGTH)
        {
            return false;
        }
        else
        {
            short length = 0;
            byte[] Temp = new byte[NetWorkConfig.MSG_HEAD_LENGTH];
            for (int i = 0; i < Temp.Length; ++i)
            {
                Temp[i] = m_Buf[i];
            }
            length = TypeConvert.getShort(Temp);
            if (m_BufOffset >= length + NetWorkConfig.MSG_HEAD_LENGTH)
            {
                return true;
            }
        }
        return false;
    }
    /// <summary>
    /// 清空缓冲区
    /// </summary>
    private void ClearBuf()
    {
        m_BufOffset = 0;
        Array.Clear(m_Buf, 0, m_Buf.Length);
        Array.Clear(m_SwapBuf, 0, m_SwapBuf.Length);
    }
    /// <summary>
    /// 连接是否存在
    /// </summary>
    /// <returns></returns>
    public bool HasConnect()
    {
        if (null != m_Socket && m_Socket.Connected)
            return true;
        return false;
    }
    /// <summary>
    /// 发送数据
    /// </summary>
    /// <param name="buf"></param>
    public void SendMsg(byte[] buf)
    {
        try
        {
            m_Socket.Send(buf);
        }
        catch (Exception ex)
        {
            OnConnectionExEvnet();
            throw ex;
        }
    }
    /// <summary>
    /// 断开连接
    /// </summary>
    public void CloseConnect()
    {
        lock(SyncObj)
        {
            if (null != m_Socket)
            {
                Socket temp = m_Socket;
                m_Socket = null;
                temp.Close();
                Debug.Log("断开连接" + m_ID + "的Socket连接");
                return;
            }
        }
    }


    /// <summary>
    /// 关闭消息接收线程
    /// </summary>
    public void AbortThread()
    {
        try
        {
            lock(SyncObj)
            {
                m_ThreadRun = false;
                t.Abort();
            }
        }
        catch (System.Exception ex)
        {
            Debug.Log("释放消息线程" + idx + ":" + ex);
        }
    }

    public void ClearNetExEvent()
    {
        OnConnectionExEvnet = null;
    }
}
