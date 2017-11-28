--拼牛
module("NetWork.Action.FinishPukeCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

FinishPukeCastAction = NetCmdActionBase:new()

function FinishPukeCastAction:Action(bytes)
    local FinishPukeCast  = require("Protol.bullMessage_pb").FinishPukeCast()
    FinishPukeCast:ParseFromString(bytes)
    Debug.log(stringify(FinishPukeCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:FinishPukeCast(FinishPukeCast.seetIndex)
end

require("NetWork.NetCmdSet").CmdSet[514] = FinishPukeCastAction --注册处理对象