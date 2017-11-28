
module("NetWork.Action.CreateJulebuBeforeAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

CreateJulebuBeforeAction = NetCmdActionBase:new()

function CreateJulebuBeforeAction:Action(bytes)
    local CreateJulebuBeforeSm  = require("Protol.guildMessage_pb").CreateJulebuBeforeSm()
    CreateJulebuBeforeSm:ParseFromString(bytes)
    Debug.log(stringify(CreateJulebuBeforeSm))
    --local RoomModule = require("Module.RoomModule").RoomModule
    --RoomModule.CreateClubBefore(CreateJulebuBeforeSm)
end

require("NetWork.NetCmdSet").CmdSet[1505] = CreateJulebuBeforeAction --注册处理对象