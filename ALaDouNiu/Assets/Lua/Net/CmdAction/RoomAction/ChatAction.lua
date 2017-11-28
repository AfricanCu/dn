--聊天发送返回
module("NetWork.Action.ChatAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ChatAction = NetCmdActionBase:new()

function ChatAction:Action(bytes)
    local ChatSm  = require("Protol.roomMessage_pb").ChatSm()
    ChatSm:ParseFromString(bytes)

    if ChatSm.code ~= 1 then
        local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
        UIWinMgr:OpenErrorNotice(ChatSm.code)
        return
    end

end

require("NetWork.NetCmdSet").CmdSet[220] = ChatAction --注册处理对象