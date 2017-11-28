--
module("NetWork.Action.NsCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

NsCastAction = NetCmdActionBase:new()

function NsCastAction:Action(bytes)
    local NsCast = require("Protol.roomMessage_pb").NsCast()
    NsCast:ParseFromString(bytes)

    local LocationModule = require("Module.LocationModule").LocationModule
    LocationModule:NsCast(NsCast.seetIndex, NsCast.ns)
end

require("NetWork.NetCmdSet").CmdSet[239] = NsCastAction --注册处理对象