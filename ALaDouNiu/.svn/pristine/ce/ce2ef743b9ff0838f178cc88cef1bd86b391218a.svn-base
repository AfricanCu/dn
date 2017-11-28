--解散房间返回
module("NetWork.Action.MemberDissolveRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

MemberDissolveRoomAction = NetCmdActionBase:new()

function MemberDissolveRoomAction:Action(bytes)
    local MemberDissolveRoomSm = require("Protol.roomMessage_pb").MemberDissolveRoomSm()
    MemberDissolveRoomSm:ParseFromString(bytes)

    if MemberDissolveRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(MemberDissolveRoomSm .code)
    end

end

require("NetWork.NetCmdSet").CmdSet[501] = MemberDissolveRoomAction --注册处理对象