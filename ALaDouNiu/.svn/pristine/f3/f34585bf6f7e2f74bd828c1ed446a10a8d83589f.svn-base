module("NetWork",package.seeall)

local cmdset = require("NetWork.NetCmdSet").CmdSet
--local pack = require("NetWork.NetPack").CmdSet

NetMgr = {}

--同步建立链接
function NetMgr:CreateConnection(Ip,Port)
    isok =  NetHelper.CreateConnectionBySyn(Ip,Port)
    if isok then
        self.loseHeartBeatRtn = 0
        NetHelper.RegHeartbeatFun(NetMgr.Heartbeat,5)--在C#注册心跳事件
        Debug.log("注册心跳")       
    end
    return isok
end

--异步建立连接 。 callback 需要一个类型为 boolen 的参数 ，返回连接是否建立成功
function NetMgr:CreateConnectionByAsync(Ip,Port,callback)
    if not callback or  type(callback) ~= "function" then
        return
    end

    function Process(isok)
        if isok then
            self.loseHeartBeatRtn = 0
            NetHelper.RegHeartbeatFun(NetMgr.Heartbeat,5)--在C#注册心跳事件
            Debug.log("注册心跳")
        end
        callback(isok)
    end
    NetHelper.CreateConnectionByAsync(Ip,Port,Process)
end

--关闭链接
function NetMgr:CloseConnection()
    NetHelper.Disconnect();
end

--发送数据,msg为要发送的protobuf数据
function NetMgr:SendMsg(cmd,msg)
    local pb_data = nil
    if nil ~= msg then
        pb_data = msg:SerializeToString()
    end
    NetHelper.SendMsg(cmd,pb_data);
    Debug.log("发送消息—>"..cmd)
end

--接收从C#模块发送来的消息
function NetMgr.OnMsg(cmd,bytes)
    Debug.log("收到消息<—"..cmd)
    cmdset:ExecuteAction(cmd,bytes)
end
NetHelper.RegNetMsgProcess(NetMgr.OnMsg)--在C#注册消息处理函数

--当网络异常时
function NetMgr.OnNetEx()
    Debug.log("连接异常！！！！！！！！！！")
    Event.Brocast(EventDefine.OnConnectClose) -- 广播网络掉线事件
    NetMgr.loseHeartBeatRtn = 0
    if NetHelper.DeleteHeartbeat then --为了兼容老的C# warp版本
        NetHelper.DeleteHeartbeat() --删除心跳
    end
end
NetHelper.RegisterNetExEvent(NetMgr.OnNetEx)--在C#注册网络异常事件

--发送心跳
function NetMgr.Heartbeat()
    if NetMgr.loseHeartBeatRtn == nil then
        NetMgr.loseHeartBeatRtn = 0
    end

    --3次心跳内未收到回复
    if NetMgr.loseHeartBeatRtn >= 3 then
        NetMgr:CloseConnection()
        NetMgr.loseHeartBeatRtn = 0
        Event.Brocast(EventDefine.OnConnectClose)
    else
        NetHelper.SendMsg(-1)
        NetMgr.loseHeartBeatRtn = NetMgr.loseHeartBeatRtn + 1
    end
end

function NetMgr.HeartbeatRtn()
    NetMgr.loseHeartBeatRtn = 0
end

--心跳挂起
function NetMgr.HeartbeatPause()
    NetHelper.SendMsg(-4)
end

--心跳恢复
function NetMgr.HeartbeatResume()
    NetHelper.SendMsg(-5)
end
