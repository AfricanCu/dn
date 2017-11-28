
module("NetWork.Action.ApplyMemberListAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ApplyMemberListAction  = NetCmdActionBase:new()

function ApplyMemberListAction :Action(bytes)
    local ApplyMemberListSm  = require("Protol.guildMessage_pb").ApplyMemberListSm()
    ApplyMemberListSm:ParseFromString(bytes)
    Debug.log(stringify(ApplyMemberListSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ApplyMemberListSm(ApplyMemberListSm)
    Debug.log(#ApplyMemberListSm.apply.."-------------------------------------------")
end

require("NetWork.NetCmdSet").CmdSet[1527] = ApplyMemberListAction  --注册处理对象