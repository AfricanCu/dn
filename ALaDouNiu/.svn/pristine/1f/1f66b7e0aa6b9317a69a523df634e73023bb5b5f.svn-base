--
module("NetWork.Action.DissolveRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

DissolveRoomAction = NetCmdActionBase:new()

function DissolveRoomAction:Action(bytes)
    local DissolveRoomSm  = require("Protol.roomMessage_pb").DissolveRoomSm()
    DissolveRoomSm:ParseFromString(bytes)

    if DissolveRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(DissolveRoomSm.code)
        return
    end

    RoomModule.DissolveRoomSm(DissolveRoomSm)
    
end

require("NetWork.NetCmdSet").CmdSet[210] = DissolveRoomAction --注册处理对象