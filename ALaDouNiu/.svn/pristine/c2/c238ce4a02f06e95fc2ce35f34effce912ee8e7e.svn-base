--
module("NetWork.Action.NsAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

NsAction = NetCmdActionBase:new()

function NsAction:Action(bytes)
    local NsSm = require("Protol.roomMessage_pb").NsSm()
    NsSm:ParseFromString(bytes)
end

require("NetWork.NetCmdSet").CmdSet[238] = NsAction --注册处理对象