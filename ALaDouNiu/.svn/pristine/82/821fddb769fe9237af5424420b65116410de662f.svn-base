
module("NetWork.Action.ApplyJulebuAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ApplyJulebuAction = NetCmdActionBase:new()

function ApplyJulebuAction:Action(bytes)
    local ApplyJulebuSm  = require("Protol.guildMessage_pb").ApplyJulebuSm()
    ApplyJulebuSm:ParseFromString(bytes)
    Debug.log(stringify(ApplyJulebuSm))
    if ApplyJulebuSm.code == 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:CloseMask()
        UIWinMgr:OpenNotice("您的申请已发出...")
        UIWinMgr:CloseWindow("JoinClubWin")
    else
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenNotice("请输入正确的俱乐部ID号...")
        UIWinMgr:OpenWindow("JoinClubWin")
    end

end

require("NetWork.NetCmdSet").CmdSet[1525] = ApplyJulebuAction --注册处理对象