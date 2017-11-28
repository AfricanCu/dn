--玩家游戏记录
module("NetWork.Action.PlayerRecordCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

PlayerRecordCastAction = NetCmdActionBase:new()

function PlayerRecordCastAction:Action(bytes)
    local PlayerRecordCast = require("Protol.loginMessage_pb").PlayerRecordCast()
    PlayerRecordCast:ParseFromString(bytes)
    Debug.log(stringify(PlayerRecordCast))
    local MainUserData = require("DynamicData.MainUserData").MainUserData
    MainUserData:UpdateBattleRecord(PlayerRecordCast.record)
end

require("NetWork.NetCmdSet").CmdSet[7] = PlayerRecordCastAction --注册处理对象