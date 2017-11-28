--玩家加入或者离开房间消息推送处理
module("NetWork.Action.JoinRoomCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
local RoomModule = require("Module.RoomModule").RoomModule

JoinRoomCastAction = NetCmdActionBase:new()

function JoinRoomCastAction:Action(bytes)
    --暂时只搞加入房间操作，而且目前没有掉线、退房处理
    local joinRoomCast  = require("Protol.roomMessage_pb").JoinRoomCast()
    joinRoomCast:ParseFromString(bytes)
    Debug.log(stringify(joinRoomCast))
    RoomModule.CastRoom(joinRoomCast)
end

require("NetWork.NetCmdSet").CmdSet[206] = JoinRoomCastAction --注册处理对象