--庄变化，谁的庄，如果多人同时抢庄，随机参数为true，需要做一个头像上的随机动画
module("NetWork.Action.BankerCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

BankerCastAction = NetCmdActionBase:new()

function BankerCastAction:Action(bytes)
    local BankerCast  = require("Protol.bullMessage_pb").BankerCast()
    BankerCast:ParseFromString(bytes)
    Debug.log(stringify(BankerCast))
    
    local GameHost = require("Module.GameModule.GameHost").GameHost
    GameHost:ZhuangChg(BankerCast.seetIndex,BankerCast.ran,BankerCast.st)
end

require("NetWork.NetCmdSet").CmdSet[507] = BankerCastAction --注册处理对象