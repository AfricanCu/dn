module("NetWork.Action.ClearWinnerAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ClearWinnerAction = NetCmdActionBase:new()

function ClearWinnerAction:Action(bytes)
    local ClearWinnerSm = require("Protol.guildMessage_pb").ClearWinnerSm()
    ClearWinnerSm:ParseFromString(bytes)
    Debug.log(stringify(ClearWinnerSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ClearWinnerSm(ClearWinnerSm)
end

require("NetWork.NetCmdSet").CmdSet[1544] = ClearWinnerAction --注册处理对象