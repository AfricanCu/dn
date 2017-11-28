--谁抢庄谁不抢庄
module("NetWork.Action.QiangZhuangCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

QiangZhuangCastAction = NetCmdActionBase:new()

function QiangZhuangCastAction:Action(bytes)
    local QiangZhuangCast  = require("Protol.bullMessage_pb").QiangZhuangCast()
    QiangZhuangCast:ParseFromString(bytes)
    Debug.log(stringify(QiangZhuangCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:QiangZhuangCast(QiangZhuangCast.seetIndex, QiangZhuangCast.qiang)
end

require("NetWork.NetCmdSet").CmdSet[506] = QiangZhuangCastAction --注册处理对象`