
module("NetWork.Action.AgreeApplyAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

AgreeApplyAction = NetCmdActionBase:new()

function AgreeApplyAction:Action(bytes)
    local AgreeApplySm  = require("Protol.guildMessage_pb").AgreeApplySm()
    AgreeApplySm:ParseFromString(bytes)
    Debug.log(stringify(AgreeApplySm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.AgreeApplySm(AgreeApplySm)

    
end

require("NetWork.NetCmdSet").CmdSet[1529] = AgreeApplyAction --注册处理对象