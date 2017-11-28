--
module("NetWork.Action.ProxyRoomsCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRoomsCastAction = NetCmdActionBase:new()

function ProxyRoomsCastAction:Action(bytes)
    local ProxyRoomsCast  = require("Protol.roomMessage_pb").ProxyRoomsCast()
    ProxyRoomsCast:ParseFromString(bytes)
    
    Debug.log("*****************")
    Debug.log(stringify(ProxyRoomsCast))
    Debug.log("*****************")

    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:UpdateProxyRoom(ProxyRoomsCast.rooms) 
end

require("NetWork.NetCmdSet").CmdSet[227] = ProxyRoomsCastAction --注册处理对象