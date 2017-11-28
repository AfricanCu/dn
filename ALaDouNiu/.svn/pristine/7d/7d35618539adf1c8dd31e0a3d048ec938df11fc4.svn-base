--请求玩家数据结果返回处理
module("NetWork.Action.MainUserDataAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

MainUserDataAction = NetCmdActionBase:new()

function MainUserDataAction:Action(bytes)
    local PlayerCast  = require("Protol.loginMessage_pb").PlayerCast()
    PlayerCast:ParseFromString(bytes) -- 数据解析
    Debug.log(stringify(PlayerCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:UpdateUser(PlayerCast)

end

require("NetWork.NetCmdSet").CmdSet[6] = MainUserDataAction --注册处理对象