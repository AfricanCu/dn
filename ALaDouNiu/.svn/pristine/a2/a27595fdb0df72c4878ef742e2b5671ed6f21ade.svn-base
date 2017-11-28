--游戏开局
module("NetWork.Action.RoundBeginCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

RoundBeginCastAction = NetCmdActionBase:new()

function RoundBeginCastAction:Action(bytes)
    local RoundBeginCast  = require("Protol.bullMessage_pb").RoundBeginCast()
    RoundBeginCast:ParseFromString(bytes)
    Debug.log(stringify(RoundBeginCast))
    
    Event.Brocast(EventDefine.OnGameStart,true)
    Event.Brocast(EventDefine.OnGameRoundChange,RoundBeginCast.round)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:FaPaiAni(GameHost.players, GameHost.myIndex, 4, RoundBeginCast.puke)
end

require("NetWork.NetCmdSet").CmdSet[503] = RoundBeginCastAction --注册处理对象