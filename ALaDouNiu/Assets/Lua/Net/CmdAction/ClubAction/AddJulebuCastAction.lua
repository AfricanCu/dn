
module("NetWork.Action.AddJulebuCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

AddJulebuCastAction = NetCmdActionBase:new()

function AddJulebuCastAction:Action(bytes)
    local AddJulebuCast  = require("Protol.guildMessage_pb").AddJulebuCast()
    AddJulebuCast:ParseFromString(bytes)
    Debug.log(stringify(AddJulebuCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:AddClubCast(AddJulebuCast)
end

require("NetWork.NetCmdSet").CmdSet[1500] = AddJulebuCastAction --注册处理对象