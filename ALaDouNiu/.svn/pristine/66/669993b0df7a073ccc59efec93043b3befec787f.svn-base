--抢庄返回
module("NetWork.Action.QiangZhuangAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

QiangZhuangAction = NetCmdActionBase:new()

function QiangZhuangAction:Action(bytes)
    local QiangZhuangSm = require("Protol.bullMessage_pb").QiangZhuangSm()
    QiangZhuangSm:ParseFromString(bytes)
    Debug.log(stringify(QiangZhuangSm))
    
end

require("NetWork.NetCmdSet").CmdSet[505] = QiangZhuangAction --注册处理对象