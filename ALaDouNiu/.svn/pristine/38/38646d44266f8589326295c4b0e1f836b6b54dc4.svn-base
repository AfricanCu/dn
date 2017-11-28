
module("NetWork.Action.BigWinnerAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BigWinnerAction = NetCmdActionBase:new()

function BigWinnerAction:Action(bytes)
    local WinnerSm = require("Protol.guildMessage_pb").WinnerSm()
    WinnerSm:ParseFromString(bytes)
    Debug.log(stringify(WinnerSm))
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.WinnerSm(WinnerSm)
end

require("NetWork.NetCmdSet").CmdSet[1542] = BigWinnerAction --注册处理对象