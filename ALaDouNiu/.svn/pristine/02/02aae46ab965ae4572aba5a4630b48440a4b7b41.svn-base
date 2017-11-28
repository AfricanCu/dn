--聊天广播推送
module("NetWork.Action.ChatCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ChatCastAction = NetCmdActionBase:new()

function ChatCastAction:Action(bytes)
    local ChatCast  = require("Protol.roomMessage_pb").ChatCast()
    ChatCast:ParseFromString(bytes)

    local GameHost = require("Module.GameModule.GameHost").GameHost
    local sendPlayer = GameHost:GetPlayer(ChatCast.seetIndex)
    sendPlayer:WordChat(ChatCast)
end

require("NetWork.NetCmdSet").CmdSet[221] = ChatCastAction --注册处理对象