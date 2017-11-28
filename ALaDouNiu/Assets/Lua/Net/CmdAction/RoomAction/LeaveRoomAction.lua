--退出房间处理
module("NetWork.Action.LeaveRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

LeaveRoomAction = NetCmdActionBase:new()

local UIWinMgr = require("UI.UIWinMgr").UIWinMgr

function LeaveRoomAction:Action(bytes)
    local leaveRoomSm  = require("Protol.roomMessage_pb").LeaveRoomSm()
    leaveRoomSm:ParseFromString(bytes)

    Debug.log(stringify(leaveRoomSm))

    RoomModule.LeaveRoomSm(leaveRoomSm)

    if leaveRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(leaveRoomSm.code)
    end
end

require("NetWork.NetCmdSet").CmdSet[208] = LeaveRoomAction --注册处理对象