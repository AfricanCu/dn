using System.Collections;
using System.Collections.Generic;
using System;
using System.IO;

/// <summary>
/// 客户端消息逻辑分组
/// </summary>
public class Msg
{
    /// <summary>
    /// 消息头，用来标识消息长度，不包括头长度
    /// </summary>
    public readonly short m_Head;
    /// <summary>
    /// 逻辑协议头
    /// </summary>
    public readonly short m_Command;
    /// <summary>
    /// 消息数据
    /// </summary>
    private List<byte> _buf;

    public Msg(byte[] buf)
    {
        _buf = new List<byte>();
        _buf.AddRange(buf);
        m_Head = ReadHead();
        m_Command = ReadCommand();
    }

    /// <summary>
    /// 从消息缓冲区获取消息头，并从缓冲区删除所获得的数据
    /// </summary>
    /// <returns></returns>
    private short ReadHead()
    {
        byte[] Temp = new byte[NetWorkConfig.MSG_HEAD_LENGTH];
        for (int i = 0; i < NetWorkConfig.MSG_HEAD_LENGTH; ++i)
        {
            Temp[i] = _buf[i];
        }
        _buf.RemoveRange(0, NetWorkConfig.MSG_HEAD_LENGTH);
        Array.Reverse(Temp);
        return TypeConvert.getShort(Temp,false);
    }

    /// <summary>
    /// 从消息缓冲区获取Command，并从缓冲区删除所获得的数据
    /// </summary>
    /// <returns></returns>s
    private short ReadCommand()
    {
        byte[] Temp = new byte[NetWorkConfig.MSG_COMMAND_LENGTH];
        for (int i = 0; i < NetWorkConfig.MSG_COMMAND_LENGTH; ++i)
        {
            Temp[i] = _buf[i];
        }
        _buf.RemoveRange(0, NetWorkConfig.MSG_COMMAND_LENGTH);
        Array.Reverse(Temp);
        return TypeConvert.getShort(Temp, false);
    }

    /// <summary>
    /// 获取二进制的消息内容
    /// </summary>
    /// <returns></returns>
    public byte[] ReadData()
    {
        return _buf.ToArray();
    }
}
