--开始下注
module("NetWork.Action.BetOnBeginCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BetOnBeginCastAction = NetCmdActionBase:new()

function BetOnBeginCastAction:Action(bytes)
    local BetOnBeginCast  = require("Protol.bullMessage_pb").BetOnBeginCast()
    BetOnBeginCast:ParseFromString(bytes)
    Debug.log(stringify(BetOnBeginCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:YaZhuBegin()
end

require("NetWork.NetCmdSet").CmdSet[520] = BetOnBeginCastAction --注册处理对象