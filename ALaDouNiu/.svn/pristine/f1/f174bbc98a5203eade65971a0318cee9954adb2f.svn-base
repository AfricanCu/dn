--断线重连请求返回
module("NetWork.Action.ReconnectAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ReconnectAction = NetCmdActionBase:new()

function ReconnectAction:Action(bytes)
    local ReconnectSm  = require("Protol.bullMessage_pb").ReconnectSm()
    ReconnectSm:ParseFromString(bytes)
    Debug.log(stringify(ReconnectSm))
    local FightOfflineModule = require("Module.FightOfflineModule").FightOfflineModule
    FightOfflineModule.ReconnectSm(ReconnectSm)
end


require("NetWork.NetCmdSet").CmdSet[518] = ReconnectAction --注册处理对象