
module("NetWork.Action.PlaySetAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

PlaySetAction = NetCmdActionBase:new()

function PlaySetAction:Action(bytes)
    local PlaySetSm  = require("Protol.guildMessage_pb").PlaySetSm()
    PlaySetSm:ParseFromString(bytes)
    Debug.log(stringify(PlaySetSm))
    if PlaySetSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("玩法设置失败...")
        return
    end
    local ClubModule = require("Module.ClubModule").ClubModule
    ClubModule.ClubPlaySetSm(PlaySetSm)
end

require("NetWork.NetCmdSet").CmdSet[1511] = PlaySetAction --注册处理对象