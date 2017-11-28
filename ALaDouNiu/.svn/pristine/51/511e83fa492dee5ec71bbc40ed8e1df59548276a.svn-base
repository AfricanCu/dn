-- 代开房预处理消息类
module("NetWork.Action.ProxyCreateRoomBeforeAction",package.seeall)
local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule
ProxyCreateRoomBeforeAction = NetCmdActionBase:new()
function ProxyCreateRoomBeforeAction:Action(bytes)
    Debug.log("收到：241")
    local ProxyCreateRoomBeforeSm  = require("Protol.roomMessage_pb").ProxyCreateRoomBeforeSm()
    ProxyCreateRoomBeforeSm:ParseFromString(bytes)
    Debug.log("-------------ddddd---------------------")
    Debug.log(stringify(ProxyCreateRoomBeforeSm))
    Debug.log("-------------------休息休息---------------")
    -- Debug.log(stringify(ProxyCreateRoomBeforeSm))

    --[[if CreateRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(CreateRoomSm.code)
        return
    end]]--
    RoomModule.CreateProxyRoomBeforeSm(ProxyCreateRoomBeforeSm)
end

require("NetWork.NetCmdSet").CmdSet[241] = ProxyCreateRoomBeforeAction --注册处理对象