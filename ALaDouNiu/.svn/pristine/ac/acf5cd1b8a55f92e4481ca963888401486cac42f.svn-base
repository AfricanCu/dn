--
module("NetWork.Action.PrepareRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

PrepareRoomAction = NetCmdActionBase:new()

function PrepareRoomAction:Action(bytes)
    local PrepareRoomSm  = require("Protol.roomMessage_pb").PrepareRoomSm()
    PrepareRoomSm:ParseFromString(bytes)
    Debug.log(stringify(PrepareRoomSm))
    RoomModule.PrepareRoomSm(PrepareRoomSm)
end

require("NetWork.NetCmdSet").CmdSet[212] = PrepareRoomAction --注册处理对象