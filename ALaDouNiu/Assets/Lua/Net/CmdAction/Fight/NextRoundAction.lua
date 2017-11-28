--点击继续结果返回
module("NetWork.Action.NextRoundAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

NextRoundAction = NetCmdActionBase:new()

function NextRoundAction:Action(bytes)
    local NextRoundSm  = require("Protol.bullMessage_pb").NextRoundSm()
    NextRoundSm:ParseFromString(bytes)
    Debug.log(stringify(NextRoundSm))
    
end

require("NetWork.NetCmdSet").CmdSet[522] = NextRoundAction --注册处理对象