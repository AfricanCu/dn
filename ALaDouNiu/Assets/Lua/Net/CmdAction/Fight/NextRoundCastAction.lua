--点击继续广播
module("NetWork.Action.NextRoundCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

NextRoundCastAction = NetCmdActionBase:new()

function NextRoundCastAction:Action(bytes)
    local NextRoundCast  = require("Protol.bullMessage_pb").NextRoundCast()
    NextRoundCast:ParseFromString(bytes)
    Debug.log(stringify(NextRoundCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    if NextRoundCast.seetIndex == GameHost.myIndex then
        GameHost:ClearPai()
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:CloseWindow("XiaoJieSuanWin")
    end

    local player = GameHost:GetPlayer(NextRoundCast.seetIndex)
    if player ~= nil then
        player:ShowReady(true)
    end
end

require("NetWork.NetCmdSet").CmdSet[523] = NextRoundCastAction --注册处理对象