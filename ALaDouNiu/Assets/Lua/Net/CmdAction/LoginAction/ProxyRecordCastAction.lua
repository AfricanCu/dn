--代开游戏记录
module("NetWork.Action.ProxyRecordCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRecordCastAction = NetCmdActionBase:new()

function ProxyRecordCastAction:Action(bytes)
    local ProxyRecordCast = require("Protol.loginMessage_pb").ProxyRecordCast()
    ProxyRecordCast:ParseFromString(bytes)
    Debug.log(stringify(ProxyRecordCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:UpdateProxyRecord(ProxyRecordCast.record)
end

require("NetWork.NetCmdSet").CmdSet[11] = ProxyRecordCastAction --注册处理对象