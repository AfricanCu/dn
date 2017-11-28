--押注结果广播
module("NetWork.Action.BetOnCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BetOnCastAction = NetCmdActionBase:new()

function BetOnCastAction:Action(bytes)
    local BetOnCast  = require("Protol.bullMessage_pb").BetOnCast()
    BetOnCast:ParseFromString(bytes)
    Debug.log(stringify(BetOnCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:BetOnCast(BetOnCast.seetIndex, BetOnCast.betTimes)
end

require("NetWork.NetCmdSet").CmdSet[510] = BetOnCastAction --注册处理对象