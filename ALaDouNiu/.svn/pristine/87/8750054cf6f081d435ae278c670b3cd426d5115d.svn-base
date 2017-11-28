--加入房间预处理返回
module("NetWork.Action.JoinRoomBeforeAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

JoinRoomBeforeAction = NetCmdActionBase:new()

function JoinRoomBeforeAction:Action(bytes)
    local JoinRoomBeforeSm = require("Protol.roomMessage_pb").JoinRoomBeforeSm()
    JoinRoomBeforeSm:ParseFromString(bytes)

    RoomModule.JoinRoomBeforeSm(JoinRoomBeforeSm)
end

require("NetWork.NetCmdSet").CmdSet[203] = JoinRoomBeforeAction --注册处理对象