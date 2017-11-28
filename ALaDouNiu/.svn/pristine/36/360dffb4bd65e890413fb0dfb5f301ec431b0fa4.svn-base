module("NetWork.Action.JulebuMemberListAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

JulebuMemberListAction = NetCmdActionBase:new()
function JulebuMemberListAction:Action(bytes)
  
    local JulebuMemberListSm = require("Protol.guildMessage_pb").JulebuMemberListSm()
    JulebuMemberListSm:ParseFromString(bytes)
    Debug.log(stringify(JulebuMemberListSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.JulebuMemberListSm(JulebuMemberListSm)
end

require("NetWork.NetCmdSet").CmdSet[1533] = JulebuMemberListAction --注册处理对象