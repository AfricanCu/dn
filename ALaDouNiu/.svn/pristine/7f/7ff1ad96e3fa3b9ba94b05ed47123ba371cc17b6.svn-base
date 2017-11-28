--
module("NetWork.Action.ProxyRoomsAddCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRoomsAddCastAction = NetCmdActionBase:new()

function ProxyRoomsAddCastAction:Action(bytes)
    local ProxyRoomsAddCast  = require("Protol.roomMessage_pb").ProxyRoomsAddCast()
    ProxyRoomsAddCast:ParseFromString(bytes)

    Debug.log("房间数增加："..stringify(ProxyRoomsAddCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:AddProxyRoom(ProxyRoomsAddCast.rooms)
end

require("NetWork.NetCmdSet").CmdSet[228] = ProxyRoomsAddCastAction --注册处理对象