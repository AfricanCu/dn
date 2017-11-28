--小结算
module("NetWork.Action.BullResultCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BullResultCastAction = NetCmdActionBase:new()

function BullResultCastAction:Action(bytes)
    local BullResultCast  = require("Protol.bullMessage_pb").BullResultCast()
    BullResultCast:ParseFromString(bytes)
    Debug.log(stringify(BullResultCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:XiaoJieSuan(BullResultCast)
end

require("NetWork.NetCmdSet").CmdSet[515] = BullResultCastAction --注册处理对象