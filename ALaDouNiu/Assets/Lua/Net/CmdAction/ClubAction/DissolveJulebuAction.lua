
module("NetWork.Action.DissolveJulebuAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

DissolveJulebuAction  = NetCmdActionBase:new()

function DissolveJulebuAction :Action(bytes)
    local DissolveJulebuSm  = require("Protol.guildMessage_pb").DissolveJulebuSm()
    DissolveJulebuSm:ParseFromString(bytes)
    Debug.log(stringify(DissolveJulebuSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.DissolveClubSm(DissolveJulebuSm)
    
end

require("NetWork.NetCmdSet").CmdSet[1515] = DissolveJulebuAction  --注册处理对象