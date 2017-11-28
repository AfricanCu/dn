module("NetWork.Action.SearchMemberAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

SearchMemberAction = NetCmdActionBase:new()

function SearchMemberAction:Action(bytes)
    local SearchMemberSm = require("Protol.guildMessage_pb").SearchMemberSm()
    SearchMemberSm:ParseFromString(bytes)
    Debug.log(stringify(SearchMemberSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.SearchMemberSm(SearchMemberSm)
end

require("NetWork.NetCmdSet").CmdSet[1546] = SearchMemberAction --注册处理对象
