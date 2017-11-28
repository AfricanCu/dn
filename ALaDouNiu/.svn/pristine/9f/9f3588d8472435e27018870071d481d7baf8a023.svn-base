--开始抢庄
module("NetWork.Action.QzBeginCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

QzBeginCastAction = NetCmdActionBase:new()

function QzBeginCastAction:Action(bytes)
    local QzBeginCast  = require("Protol.bullMessage_pb").QzBeginCast()
    QzBeginCast:ParseFromString(bytes)
    Debug.log(stringify(QzBeginCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:QiangZhuang()
end

require("NetWork.NetCmdSet").CmdSet[519] = QzBeginCastAction --注册处理对象