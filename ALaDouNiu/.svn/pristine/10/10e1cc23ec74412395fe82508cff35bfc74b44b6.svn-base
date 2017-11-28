--
module("NetWork.Action.CreateRoomAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

CreateRoomAction = NetCmdActionBase:new()

function CreateRoomAction:Action(bytes)
    local CreateRoomSm  = require("Protol.roomMessage_pb").CreateRoomSm()
    CreateRoomSm:ParseFromString(bytes)

    Debug.log("------------收到201------------------")

    Debug.log(stringify(CreateRoomSm))

    --[[if CreateRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(CreateRoomSm.code)
        return
    end]]--
    RoomModule.CreateRoomSm(CreateRoomSm)
end

require("NetWork.NetCmdSet").CmdSet[201] = CreateRoomAction --注册处理对象