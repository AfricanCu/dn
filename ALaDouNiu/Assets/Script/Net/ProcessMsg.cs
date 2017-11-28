using UnityEngine;
using System.Collections;
using LuaInterface;

/// <summary>
/// 消息处理
/// </summary>
public class ProcessMsg : MonoBehaviour
{
    public static ProcessMsg Instance
    {
        get
        {
            if (null == _instance)
            {
                GameObject go = new GameObject();
                go.name = typeof(ProcessMsg).Name;
                go.AddComponent<ProcessMsg>();
            }
            return _instance;
        }
    }

    public static ProcessMsg _instance = null;

    private LuaFunction _PushMsgFun = null;

    private LuaFunction _HeartbeatFun = null;
    private float _sendHeartbeatTiming = 0f;
    private Coroutine _heartbaetCoroutine = null;



    private void Awake()
    {
        if (null != _instance)
        {
            Destroy(this);
        }
        else
        {
            _instance = this;
            DontDestroyOnLoad(_instance.gameObject);
        }
    }

    void Update()
    {
        while (ConnectionManager.GetMsgNum() > 0)
        {
            Msg msg = ConnectionManager.GetMsg();
            if (null != msg)//由于多线程同时操作消息队列，队列的Count是不可信的，所以需要判断一下
            {
                //向lua模块传送消息
                bool isSuccess =  TrySendMsgToLua(msg.m_Command, msg.ReadData());
                if(!isSuccess)
                {
                    Debug.LogError("向lua模块发送网络消息失败，未找到lua模块的消息处理函数");
                }
            }
        }
    }

    /// <summary>
    /// 注册lua模块的消息处理函数
    /// </summary>
    /// <param name="luafun"></param>
    public void RegNetMsgProcess(LuaFunction luafun)
    {
        if(null != _PushMsgFun)
        {
            _PushMsgFun.Dispose();
        }
        _PushMsgFun = luafun;
    }

    /// <summary>
    /// 注册心跳函数
    /// </summary>
    /// <param name="luafun"></param>
    public void RegHeartbeatFun(LuaFunction luafun,float spacing)
    {
        if (null != _HeartbeatFun)
        {
            _HeartbeatFun.Dispose();
        }
        _HeartbeatFun = luafun;
        if(null != _heartbaetCoroutine)
        {
            StopCoroutine(_heartbaetCoroutine);
        }
        _heartbaetCoroutine = StartCoroutine_Auto(SendHeartbeat(spacing));
    }
    
    /// <summary>
    /// 删除心跳事件
    /// </summary>
    public void DeleteHeartbeat()
    {
        _HeartbeatFun = null;
    }

    private IEnumerator SendHeartbeat(float spacing)
    {
        _sendHeartbeatTiming = spacing;
        LuaFunction fun = _HeartbeatFun;
        while (null != _HeartbeatFun)
        {
            _sendHeartbeatTiming += RealTime.deltaTime;
            if (_sendHeartbeatTiming >= spacing 
                && null != _HeartbeatFun)
            {
                _sendHeartbeatTiming = 0;
                fun.BeginPCall();
                fun.PCall();
                fun.EndPCall();
            }
            yield return 1;
        }
        fun.Dispose();
        Debug.Log("释放心跳函数");
    }
            
    private bool TrySendMsgToLua(short cmd, byte[] bytes)
    {
        if(null != _PushMsgFun)
        {
            _PushMsgFun.BeginPCall();
            _PushMsgFun.Push(cmd);
            _PushMsgFun.Push(new LuaByteBuffer(bytes));
            _PushMsgFun.PCall();
            _PushMsgFun.EndPCall();
            return true;
        }
        return false;
    }

    private void OnDestory()
    {
        if (null != _PushMsgFun)
            _PushMsgFun.Dispose();
        if (null != _HeartbeatFun)
            _HeartbeatFun.Dispose();
    }
}
