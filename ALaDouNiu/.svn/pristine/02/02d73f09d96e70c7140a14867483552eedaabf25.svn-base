--心跳返回处理
module("NetWork.Action.HeartbeatAction",package.seeall)

NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

HeartbeatAction = NetCmdActionBase:new()

function HeartbeatAction:Action(bytes)
    local NetMgr = require("NetWork").NetMgr
    NetMgr.HeartbeatRtn()
end

require("NetWork.NetCmdSet").CmdSet[-1] = HeartbeatAction --注册处理对象  