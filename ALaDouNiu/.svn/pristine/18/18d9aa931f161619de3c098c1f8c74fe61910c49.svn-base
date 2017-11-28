--
module("NetWork.Action.CreateRoomBeforeAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

CreateRoomBeforeAction = NetCmdActionBase:new()

function CreateRoomBeforeAction:Action(bytes)
    local CreateRoomBeforeSm  = require("Protol.roomMessage_pb").CreateRoomBeforeSm()
    CreateRoomBeforeSm:ParseFromString(bytes)


    Debug.log("------------------5456456456465-------------------------")
    Debug.log(stringify(CreateRoomBeforeSm))
    Debug.log("-----------------------5456556465--------------------")

    --[[if CreateRoomSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(CreateRoomSm.code)
        return
    end]]--
    RoomModule.CreateRoomBeforeSm(CreateRoomBeforeSm)
end

require("NetWork.NetCmdSet").CmdSet[234] = CreateRoomBeforeAction --注册处理对象