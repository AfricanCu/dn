--离线语音广播
module("NetWork.Action.ImCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ImCastAction = NetCmdActionBase:new()


function ImCastAction:Action(bytes)
    local ImCast  = require("Protol.roomMessage_pb").ImCast()
    ImCast:ParseFromString(bytes)
    local GameHost = require("Module.GameModule.GameHost").GameHost
    if ImCast.seetIndex ~= GameHost.myIndex then
        local VoiceTool = require("VoiceTool").VoiceTool
        VoiceTool:AddPlay(ImCast.fileid)
    end
end

require("NetWork.NetCmdSet").CmdSet[224] = ImCastAction --注册处理对象