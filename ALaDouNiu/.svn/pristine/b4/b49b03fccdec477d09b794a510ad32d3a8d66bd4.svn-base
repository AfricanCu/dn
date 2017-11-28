--发最后一张牌广播
module("NetWork.Action.FaLastPukeCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

FaLastPukeCastAction = NetCmdActionBase:new()

function FaLastPukeCastAction:Action(bytes)
    local FaLastPukeCast  = require("Protol.bullMessage_pb").FaLastPukeCast()
    FaLastPukeCast:ParseFromString(bytes)
    Debug.log(stringify(FaLastPukeCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:FaLastPukeCast(FaLastPukeCast)
end

require("NetWork.NetCmdSet").CmdSet[511] = FaLastPukeCastAction --注册处理对象