

module("NetWork.Action.KickJulebuMemberAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

KickJulebuMemberAction  = NetCmdActionBase:new()

function KickJulebuMemberAction :Action(bytes)
    local KickJulebuMemberSm  = require("Protol.guildMessage_pb").KickJulebuMemberSm()
    KickJulebuMemberSm:ParseFromString(bytes)
    Debug.log(stringify(KickJulebuMemberSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.KickJulebuMemberSm(KickJulebuMemberSm)
end

require("NetWork.NetCmdSet").CmdSet[1535] = KickJulebuMemberAction 