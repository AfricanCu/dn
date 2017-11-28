module("NetWork.Action.DisagreeApplyAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

DisagreeApplyAction = NetCmdActionBase:new()

function DisagreeApplyAction:Action(bytes)
    local DisagreeApplySm = require("Protol.guildMessage_pb").DisagreeApplySm()
    DisagreeApplySm:ParseFromString(bytes)
    Debug.log(stringify(DisagreeApplySm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.DisagreeApplySm(DisagreeApplySm)
end

require("NetWork.NetCmdSet").CmdSet[1531] = DisagreeApplyAction --注册处理对象