--切肤登陆返回
module("NetWork.Action.SwLoginAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

SwLoginAction = NetCmdActionBase:new()

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

function SwLoginAction:Action(bytes)

    local SwLoginSm = require("Protol.loginMessage_pb").SwLoginSm()
    SwLoginSm:ParseFromString(bytes)

    Event.Brocast(EventDefine.SWLogin,SwLoginSm) -- 切服登陆事件

end

require("NetWork.NetCmdSet").CmdSet[5] = SwLoginAction --注册处理对象