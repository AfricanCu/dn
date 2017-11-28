--拼牛结果返回
module("NetWork.Action.FinishPukeAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

FinishPukeAction = NetCmdActionBase:new()

function FinishPukeAction:Action(bytes)
    local FinishPukeSm  = require("Protol.bullMessage_pb").FinishPukeSm()
    FinishPukeSm:ParseFromString(bytes)
    Debug.log(stringify(FinishPukeSm))
    
end

require("NetWork.NetCmdSet").CmdSet[513] = FinishPukeAction --注册处理对象