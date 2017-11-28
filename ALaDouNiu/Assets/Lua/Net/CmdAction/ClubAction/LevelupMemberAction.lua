
module("NetWork.Action.LevelupMemberAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

LevelupMemberAction  = NetCmdActionBase:new()

function LevelupMemberAction :Action(bytes)
    local LevelupMemberSm  = require("Protol.guildMessage_pb").LevelupMemberSm()
    LevelupMemberSm:ParseFromString(bytes)
    Debug.log(stringify(LevelupMemberSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.LevelupMemberSm(LevelupMemberSm)
end

require("NetWork.NetCmdSet").CmdSet[1537] = LevelupMemberAction  --注册处理对象