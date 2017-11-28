--
module("NetWork.Action.PrepareRoomCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

PrepareRoomCastAction = NetCmdActionBase:new()

function PrepareRoomCastAction:Action(bytes)
    local PrepareRoomCast  = require("Protol.roomMessage_pb").PrepareRoomCast()
    PrepareRoomCast:ParseFromString(bytes)
    Debug.log(stringify(PrepareRoomCast))
    RoomModule.PrepareRoomCast(PrepareRoomCast)
end

require("NetWork.NetCmdSet").CmdSet[213] = PrepareRoomCastAction --注册处理对象