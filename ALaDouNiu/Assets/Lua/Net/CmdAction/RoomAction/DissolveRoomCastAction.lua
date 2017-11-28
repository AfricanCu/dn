--
module("NetWork.Action.DissolveRoomCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

DissolveRoomCastAction = NetCmdActionBase:new()

function DissolveRoomCastAction:Action(bytes)
    local DissolveRoomCast  = require("Protol.roomMessage_pb").DissolveRoomCast()
    DissolveRoomCast:ParseFromString(bytes)

    RoomModule.DissolveRoomCast(DissolveRoomCast)
end

require("NetWork.NetCmdSet").CmdSet[218] = DissolveRoomCastAction --注册处理对象