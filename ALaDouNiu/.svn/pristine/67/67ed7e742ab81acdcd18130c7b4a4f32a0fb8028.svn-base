--
module("NetWork.Action.ProxyRoomsDissovleAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyRoomsDissovleAction = NetCmdActionBase:new()

function ProxyRoomsDissovleAction:Action(bytes)
    local ProxyRoomsDissovleSm  = require("Protol.roomMessage_pb").ProxyRoomsDissovleSm()
    ProxyRoomsDissovleSm:ParseFromString(bytes)

    Debug.log(stringify(ProxyRoomsDissovleSm))
    if ProxyRoomsDissovleSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(ProxyRoomsDissovleSm.code)
    end
end

require("NetWork.NetCmdSet").CmdSet[232] = ProxyRoomsDissovleAction --注册处理对象