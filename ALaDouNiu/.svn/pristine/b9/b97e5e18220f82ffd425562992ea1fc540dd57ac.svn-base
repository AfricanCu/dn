--
module("NetWork.Action.ProxyRoomsDelCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRoomsDelCastAction = NetCmdActionBase:new()

function ProxyRoomsDelCastAction:Action(bytes)
    local ProxyRoomsDelCast  = require("Protol.roomMessage_pb").ProxyRoomsDelCast()
    ProxyRoomsDelCast:ParseFromString(bytes)

    Debug.log(stringify(ProxyRoomsDelCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:DelProxyRoom(ProxyRoomsDelCast.roomIds)
end

require("NetWork.NetCmdSet").CmdSet[229] = ProxyRoomsDelCastAction --注册处理对象