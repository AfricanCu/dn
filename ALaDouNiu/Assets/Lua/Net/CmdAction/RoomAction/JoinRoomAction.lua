--加入房间处理
module("NetWork.Action.JoinRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

JoinRoomAction = NetCmdActionBase:new()


function JoinRoomAction:Action(bytes)
    local JoinRoomSm  = require("Protol.roomMessage_pb").JoinRoomSm()
    JoinRoomSm:ParseFromString(bytes)

    
    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

    Debug.log("房间信息："..tostring(JoinRoomSm))

    --[[if JoinRoomSm.code ~= 1 then
        UIWinMgr:OpenErrorNotice(JoinRoomSm.code)
    end]]--

    local RoomModule = require("Module.RoomModule").RoomModule
    RoomModule.JoinRoomSm(JoinRoomSm)
    
end

require("NetWork.NetCmdSet").CmdSet[205] = JoinRoomAction --注册处理对象