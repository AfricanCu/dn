--解散房间广播
module("NetWork.Action.MemberDissolveRoomCastAction",package.seeall)

local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

MemberDissolveRoomCastAction = NetCmdActionBase:new()

function MemberDissolveRoomCastAction:Action(bytes)
    local MemberDissolveRoomCast  = require("Protol.roomMessage_pb").MemberDissolveRoomCast()
    MemberDissolveRoomCast:ParseFromString(bytes)

    Debug.log(stringify(MemberDissolveRoomCast))

    local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
    local JieSanWin = UIWinMgr:OpenWindow("JieSanWin")
    JieSanWin:FlushInfo(MemberDissolveRoomCast)
    JieSanWin:BringToTop()
end

require("NetWork.NetCmdSet").CmdSet[502] = MemberDissolveRoomCastAction --注册处理对象