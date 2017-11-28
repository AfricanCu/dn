--押注返回
module("NetWork.Action.BetOnAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BetOnAction = NetCmdActionBase:new()

function BetOnAction:Action(bytes)
    local BetOnSm  = require("Protol.bullMessage_pb").BetOnSm()
    BetOnSm:ParseFromString(bytes)
    Debug.log(stringify(BetOnSm))
    
end

require("NetWork.NetCmdSet").CmdSet[509] = BetOnAction --注册处理对象