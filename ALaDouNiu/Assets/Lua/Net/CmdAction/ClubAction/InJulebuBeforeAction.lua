
module("NetWork.Action.InJulebuBeforeAction ",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

InJulebuBeforeAction  = NetCmdActionBase:new()

function InJulebuBeforeAction :Action(bytes)
    local InJulebuBeforeSm  = require("Protol.guildMessage_pb").InJulebuBeforeSm()
    InJulebuBeforeSm:ParseFromString(bytes)
    Debug.log(stringify(InJulebuBeforeSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.InClubBeforeSm(InJulebuBeforeSm)
end

require("NetWork.NetCmdSet").CmdSet[1517] = InJulebuBeforeAction  --注册处理对象