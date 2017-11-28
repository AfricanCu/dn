--大结算
module("NetWork.Action.GameOverCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

GameOverCastAction = NetCmdActionBase:new()

function GameOverCastAction:Action(bytes)
    local GameOverCast  = require("Protol.bullMessage_pb").GameOverCast()
    GameOverCast:ParseFromString(bytes)
    Debug.log(stringify(GameOverCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost.isGameOver = true

    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local GameOverWin = UIWinMgr:OpenWindow("GameOverWin")
    GameOverWin:SetInfo(GameOverCast)
end

require("NetWork.NetCmdSet").CmdSet[516] = GameOverCastAction --注册处理对象