--
module("NetWork.Action.OfflineCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

OfflineCastAction = NetCmdActionBase:new()

function OfflineCastAction:Action(bytes)
    local OfflineCast = require("Protol.roomMessage_pb").OfflineCast()
    OfflineCast:ParseFromString(bytes)
    Debug.log(stringify(OfflineCast))
    RoomModule.OfflineCast(OfflineCast)
end

require("NetWork.NetCmdSet").CmdSet[214] = OfflineCastAction --注册处理对象