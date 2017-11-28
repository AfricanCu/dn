--离线语音消息上传返回处理
module("NetWork.Action.ImAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ImAction = NetCmdActionBase:new()


function ImAction:Action(bytes)
    local ImSm  = require("Protol.roomMessage_pb").ImSm()
    ImSm:ParseFromString(bytes)

end

require("NetWork.NetCmdSet").CmdSet[223] = ImAction --注册处理对象