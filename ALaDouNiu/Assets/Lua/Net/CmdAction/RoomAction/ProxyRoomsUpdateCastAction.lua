--
module("NetWork.Action.ProxyRoomsUpdateCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRoomsUpdateCastAction = NetCmdActionBase:new()

function ProxyRoomsUpdateCastAction:Action(bytes)
    local ProxyRoomsUpdateCast  = require("Protol.roomMessage_pb").ProxyRoomsUpdateCast()
    ProxyRoomsUpdateCast:ParseFromString(bytes)

    Debug.log(stringify(ProxyRoomsUpdateCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:ChgProxyRoom(ProxyRoomsUpdateCast.rooms)
end

require("NetWork.NetCmdSet").CmdSet[230] = ProxyRoomsUpdateCastAction --注册处理对象