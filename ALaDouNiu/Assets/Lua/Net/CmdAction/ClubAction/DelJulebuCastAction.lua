module("NetWork.Action.DelJulebuCastAction", package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

DelJulebuCastAction = NetCmdActionBase:new()

function DelJulebuCastAction:Action(bytes)
    local DelJulebuCast = require("Protol.guildMessage_pb").DelJulebuCast()
    DelJulebuCast:ParseFromString(bytes)
    Debug.log(stringify(DelJulebuCast))

    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:DelMyClub(DelJulebuCast)
end

require("NetWork.NetCmdSet").CmdSet[1501] = DelJulebuCastAction --注册处理对象