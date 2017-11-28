--
module("NetWork.Action.BattleBackAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BattleBackAction = NetCmdActionBase:new()

function BattleBackAction:Action(bytes)
    local battleBackSm  = require("Protol.backMessage_pb").BattleBackSm()
    battleBackSm:ParseFromString(bytes)
    Debug.log(stringify(battleBackSm))

    local ReplayModule = require("Module.ReplayModule").ReplayModule
    ReplayModule:BattleBackSm(battleBackSm)
end


require("NetWork.NetCmdSet").CmdSet[837] = BattleBackAction --注册处理对象