
module("NetWork.Action.InJulebuAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

InJulebuAction  = NetCmdActionBase:new()

function InJulebuAction :Action(bytes)
    local InJulebuSm  = require("Protol.guildMessage_pb").InJulebuSm()
    InJulebuSm:ParseFromString(bytes)
    Debug.log(stringify(InJulebuSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.InClubSm(InJulebuSm)
end

require("NetWork.NetCmdSet").CmdSet[1519] = InJulebuAction  --注册处理对象