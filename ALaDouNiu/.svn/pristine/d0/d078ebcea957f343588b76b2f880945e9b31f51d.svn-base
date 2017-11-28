
module("NetWork.Action.UpdateJulebuCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

UpdateJulebuCastAction = NetCmdActionBase:new()

function UpdateJulebuCastAction:Action(bytes)
    local UpdateJulebuCast  = require("Protol.guildMessage_pb").UpdateJulebuCast()
    UpdateJulebuCast:ParseFromString(bytes)
    Debug.log(stringify(UpdateJulebuCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:UpdateClubCast(UpdateJulebuCast)
end

require("NetWork.NetCmdSet").CmdSet[1502] = UpdateJulebuCastAction --注册处理对象