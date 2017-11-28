--
module("NetWork.Action.ProxyCreateRoomAction",package.seeall)

local MainUserData = require("DynamicData.MainUserData").MainUserData
local UIWinMgr = require("UI.UIWinMgr").UIWinMgr
local ProxyRoomWin = UIWinMgr:GetWindow("ProxyRoomWin")
local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase

ProxyCreateRoomAction = NetCmdActionBase:new()

function ProxyCreateRoomAction:Action(bytes)
    local ProxyCreateRoomSm  = require("Protol.roomMessage_pb").ProxyCreateRoomSm()
    ProxyCreateRoomSm:ParseFromString(bytes)

    -- Debug.log(stringify(CreateRoomSm))

    Debug.log("---------------gjldkfjgfd-------------------")
    Debug.log(stringify(ProxyCreateRoomSm))

    Debug.log("------ProxyCreateRoomSm.code"..ProxyCreateRoomSm.code)
    Debug.log("ProxyCreateRoomSm.playType"..ProxyCreateRoomSm.playType.round)

    if ProxyCreateRoomSm.code ~= 1 then
        -- UIWinMgr:OpenMask("代开房间失败")
        UIWinMgr:OpenErrorNotice(ProxyCreateRoomSm.code)

    else    

        Debug.log("------ProxyCreateRoomSm.roomId"..ProxyCreateRoomSm.roomId)
        Debug.log("----------------hgldfhjkldfjkgd------------------")   
        local NetCmdActionBase = require("NetWork.Action").NetCmdActionBase
        UIWinMgr:OpenWindow("ProxyRoomWin")
        UIWinMgr:CloseWindow("CreateRoomWin")     
        MainUserData:ProxyCreateRoomData(ProxyCreateRoomSm)
    end

    

end

require("NetWork.NetCmdSet").CmdSet[226] = ProxyCreateRoomAction --注册处理对象