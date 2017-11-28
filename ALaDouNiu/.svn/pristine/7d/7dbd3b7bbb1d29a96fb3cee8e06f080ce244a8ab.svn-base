
module("NetWork.Action.KickJulebuCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

KickJulebuCastAction = NetCmdActionBase:new()

function KickJulebuCastAction:Action(bytes)
    local KickJulebuCast  = require("Protol.guildMessage_pb").UpdateJulebuCast()
    KickJulebuCast:ParseFromString(bytes)
    Debug.log(stringify(KickJulebuCast))
    --local RoomModule = require("Module.RoomModule").RoomModule
    --RoomModule.KickClubCast(KickJulebuCast)
end

require("NetWork.NetCmdSet").CmdSet[1503] = KickJulebuCastAction --注册处理对象